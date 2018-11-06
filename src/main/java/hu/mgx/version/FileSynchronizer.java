/**
 * FileSynchronizer
 *
 * @author Magyar Gábor
 *
 * @version 1.0.1 2005.05.06.
 * -copybatch and -deletebatch parameter added
 *
 * @version 1.0.0 2005.04.04.
 *
 */
package hu.mgx.version;

import java.io.*;
import java.text.*;
import java.util.*;

import hu.mgx.util.*;

public class FileSynchronizer
{

    private Runtime runtime;
    private DecimalFormat decimalFormat = new DecimalFormat("#,##0 byte");
    private String sFilePattern = "";
    private String sDirMaster = "";
    private String sDirSlave = "";
    private String sCopyBatch = "pcopy.bat";
    private String sDeleteBatch = "pdelete.bat";
    private final static String sLine = "----------------------------------------";

    public FileSynchronizer(String[] args)
    {
        runtime = Runtime.getRuntime();
        DecimalFormatSymbols decimalFormatSymbols = decimalFormat.getDecimalFormatSymbols();
        decimalFormatSymbols.setGroupingSeparator(' ');
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
        init(args);
    }

    private void init(String[] args)
    {
        long lFreeMemory = runtime.freeMemory();
        long lTotalMemory = runtime.totalMemory();
        long lMaxMemory = runtime.maxMemory();
        int iLen = 0;
        String sFreeMemory = decimalFormat.format(lFreeMemory);
        if (sFreeMemory.length() > iLen)
        {
            iLen = sFreeMemory.length();
        }
        String sTotalMemory = decimalFormat.format(lTotalMemory);
        if (sTotalMemory.length() > iLen)
        {
            iLen = sTotalMemory.length();
        }
        String sMaxMemory = decimalFormat.format(lMaxMemory);
        if (sMaxMemory.length() > iLen)
        {
            iLen = sMaxMemory.length();
        }
        sFreeMemory = StringUtils.leftPad(sFreeMemory, iLen);
        sTotalMemory = StringUtils.leftPad(sTotalMemory, iLen);
        sMaxMemory = StringUtils.leftPad(sMaxMemory, iLen);
        System.out.println("FileSynchronizer v. 1.0.1");
        System.out.println(sLine);
        System.out.println("JVM free memory....: " + sFreeMemory);
        System.out.println("JVM total memory...: " + sTotalMemory);
        System.out.println("JVM maximum memory.: " + sMaxMemory);
        System.out.println(sLine);

        for (int i = 0; i < args.length; i += 2)
        {
            if (args[i].equalsIgnoreCase("-filepattern"))
            {
                sFilePattern = args[i + 1];
            }
            if (args[i].equalsIgnoreCase("-dirmaster"))
            {
                sDirMaster = args[i + 1];
            }
            if (args[i].equalsIgnoreCase("-dirslave"))
            {
                sDirSlave = args[i + 1];
            }
            if (args[i].equalsIgnoreCase("-copybatch"))
            {
                sCopyBatch = args[i + 1];
            }
            if (args[i].equalsIgnoreCase("-deletebatch"))
            {
                sDeleteBatch = args[i + 1];
            }
        }
        System.out.println("File pattern.......: " + sFilePattern);
        System.out.println("Master directory...: " + sDirMaster);
        System.out.println("Slave directory....: " + sDirSlave);
        System.out.println("Copy batch.........: " + sCopyBatch);
        System.out.println("Delete batch.......: " + sDeleteBatch);
        doSynchronize(getFiles(sDirMaster, sFilePattern), getFiles(sDirSlave, sFilePattern), sDirSlave);
    }

    private Vector getFiles(String sDir, String sFilePattern)
    {
        System.out.println(sLine);
        //System.out.println("Master:");
        Vector v = new Vector();
        VersionFileFilter vff = new VersionFileFilter(sFilePattern);
        //String sFileName = "";
        File dir = new File(sDir);
        if (!dir.isDirectory())
        {
            return (v);
        }
        File[] files = dir.listFiles(vff);
        for (int i = 0; i < files.length; i++)
        {
            if (files[i].isFile())
            {
                System.out.println(sDir + "\\" + files[i].getName());
                v.add(files[i]);
            //if (sFileName.compareTo(files[i].getName()) < 0) sFileName = files[i].getName();
            }
        }
        return (v);
    }

    private void doSynchronize(Vector vMasterFiles, Vector vSlaveFiles, String sDirSlave)
    {
        System.out.println(sLine);
        for (int i = 0; i < vMasterFiles.size(); i++)
        {
            copyNew((File) vMasterFiles.elementAt(i), vSlaveFiles, sDirSlave);
        }
        System.out.println(sLine);
        for (int i = 0; i < vSlaveFiles.size(); i++)
        {
            deleteOld((File) vSlaveFiles.elementAt(i), vMasterFiles);
        }
    }

    private void copyNew(File fMaster, Vector vSlaveFiles, String sDirSlave)
    {
        File fSlave;
        Date dMaster = new Date(fMaster.lastModified());
        Date dSlave;
        File fSlaveDir = new File(sDirSlave);
        boolean bNewer = true;
        String sExec = "";
        java.lang.Process process;
        for (int i = 0; i < vSlaveFiles.size(); i++)
        {
            fSlave = (File) vSlaveFiles.elementAt(i);
            dSlave = new Date(fSlave.lastModified());
            if (fMaster.getName().equals(fSlave.getName()))
            {
                if (dMaster.compareTo(dSlave) <= 0)
                {
                    bNewer = false;
                }
            }
        }
        if (bNewer)
        {
            //copy
            try
            {
                sExec = sCopyBatch + " " + fMaster.getCanonicalPath() + " " + fSlaveDir.getCanonicalPath();
                System.err.println(sExec);
                process = runtime.exec(sExec);
                process.waitFor();
                System.err.println(process.exitValue());
            }
            catch (IOException ioe)
            {
                System.err.println(ioe.getLocalizedMessage());
                ioe.printStackTrace(System.err);
            }
            catch (InterruptedException ie)
            {
                System.err.println(ie.getLocalizedMessage());
                ie.printStackTrace(System.err);
            }
        }
    }

    private void deleteOld(File fSlave, Vector vMasterFiles)
    {
        File fMaster;
        boolean bNotExists = true;
        String sExec = "";
        java.lang.Process process;
        for (int i = 0; i < vMasterFiles.size(); i++)
        {
            fMaster = (File) vMasterFiles.elementAt(i);
            if (fSlave.getName().equals(fMaster.getName()))
            {
                bNotExists = false;
            }
        }
        if (bNotExists)
        {
            //delete
            try
            {
                sExec = sDeleteBatch + " " + fSlave.getCanonicalPath();
                System.err.println(sExec);
                process = runtime.exec(sExec);
                process.waitFor();
                System.err.println(process.exitValue());
            }
            catch (IOException ioe)
            {
                System.err.println(ioe.getLocalizedMessage());
                ioe.printStackTrace(System.err);
            }
            catch (InterruptedException ie)
            {
                System.err.println(ie.getLocalizedMessage());
                ie.printStackTrace(System.err);
            }
        }
    }

    public static void main(String[] args)
    {
        new FileSynchronizer(args);
    }
}
