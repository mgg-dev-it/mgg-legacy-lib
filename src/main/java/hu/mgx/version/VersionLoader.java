/**
 * VersionLoader
 *
 * @author Magyar Gábor
 *
 * @version 1.0.0 2005.04.04.
 *
 */
package hu.mgx.version;

import hu.mgx.app.common.StreamGobbler;
import hu.mgx.util.StringUtils;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class VersionLoader {

    private DecimalFormat decimalFormat = new DecimalFormat("#,##0 byte");
    private String sJVM = "java.exe";
    private String sOptions = "";
    private String sClassPath = ".";
    private String sJARPattern = "";
    private String sJARPrefix = "";
    private String sMainClass = "";
    private String sOsName = "";
    private String sOsArch = "";
    private String sOsVersion = "";
    private String sSunArchDataModel = "";
    private String sDir = "";
    private String sParameters = "";
    private boolean bNoConsole = false;
    private StreamGobbler errorGobbler = null;
    private StreamGobbler outputGobbler = null;

    public VersionLoader(String[] args) {
        DecimalFormatSymbols decimalFormatSymbols = decimalFormat.getDecimalFormatSymbols();
        decimalFormatSymbols.setGroupingSeparator(' ');
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
        init(args);
    }

    private void init(String[] args) {
        int exitVal = 0;

        //java.util.Properties p = System.getProperties();
        //p.list(System.out);
        sOsName = System.getProperty("os.name", "");
        if (sOsName.indexOf("95") > -1) {
            bNoConsole = true;
        }
        if (sOsName.indexOf("98") > -1) {
            bNoConsole = true;
        }
        sOsArch = System.getProperty("os.arch", "");
        sOsVersion = System.getProperty("os.version", "");
        sSunArchDataModel = System.getProperty("sun.arch.data.model", "");
        Runtime runtime = Runtime.getRuntime();
        java.lang.Process process;
        long lFreeMemory = runtime.freeMemory();
        long lTotalMemory = runtime.totalMemory();
        long lMaxMemory = runtime.maxMemory();
        int iLen = 0;
        String sFreeMemory = decimalFormat.format(lFreeMemory);
        if (sFreeMemory.length() > iLen) {
            iLen = sFreeMemory.length();
        }
        String sTotalMemory = decimalFormat.format(lTotalMemory);
        if (sTotalMemory.length() > iLen) {
            iLen = sTotalMemory.length();
        }
        String sMaxMemory = decimalFormat.format(lMaxMemory);
        if (sMaxMemory.length() > iLen) {
            iLen = sMaxMemory.length();
        }
        sFreeMemory = StringUtils.leftPad(sFreeMemory, iLen);
        sTotalMemory = StringUtils.leftPad(sTotalMemory, iLen);
        sMaxMemory = StringUtils.leftPad(sMaxMemory, iLen);

        int i = 0;
        int j = 0;
        while (i < args.length) {
            if (i < args.length) {
                if (args[i].equalsIgnoreCase("-jvm")) {
                    sJVM = args[i + 1];
                    i += 2;
                }
            }
            if (i < args.length) {
                if (args[i].equalsIgnoreCase("-main")) {
                    sMainClass = args[i + 1];
                    i += 2;
                }
            }
            if (i < args.length) {
                if (args[i].equalsIgnoreCase("-option")) {
                    sOptions = sOptions + " " + args[i + 1];
                    i += 2;
                }
            }
            if (i < args.length) {
                if (args[i].equalsIgnoreCase("-classpath")) {
                    sClassPath = sClassPath + ";" + args[i + 1];
                    i += 2;
                }
            }
            if (i < args.length) {
                if (args[i].equalsIgnoreCase("-jarpattern")) {
                    sJARPattern = args[i + 1];
                    i += 2;
                }
            }
            if (i < args.length) {
                if (args[i].equalsIgnoreCase("-dir")) {
                    sDir = args[i + 1];
                    i += 2;
                }
            }
            if (i < args.length) {
                if (args[i].equalsIgnoreCase("-parameter")) {
                    sParameters = sParameters + " " + args[i + 1];
                    i += 2;
                }
            }
            if (i < args.length) {
                if (args[i].equalsIgnoreCase("-jarprefix")) {
                    sJARPrefix = args[i + 1];
                    i += 2;
                }
            }
            if (i < args.length) {
                if (args[i].equalsIgnoreCase("-noconsole")) {
                    bNoConsole = true;
                    i++;
                }
            }
            if (j == i) {
                i++;
            }
            j = i;
        }
        sJARPattern = StringUtils.stringReplace(sJARPattern, "@", "*");
        if (sJARPattern.equals("")) {
            sJARPattern = sJARPrefix + "*.jar";
        }
        System.out.println("Operating System...: " + sOsName);
        System.out.println("OS Architecture....: " + sOsArch);
        System.out.println("OS Version.........: " + sOsVersion);
        System.out.println("SunArchDataModel...: " + sSunArchDataModel);
        System.out.println("No console.........: " + (bNoConsole ? "true" : "false"));
        System.out.println("JVM free memory....: " + sFreeMemory);
        System.out.println("JVM total memory...: " + sTotalMemory);
        System.out.println("JVM maximum memory.: " + sMaxMemory);
        System.out.println("JVM................: " + sJVM);
        System.out.println("Main class.........: " + sMainClass);
        System.out.println("Classpath..........: " + sClassPath);
        System.out.println("Options............: " + sOptions);
        System.out.println("JAR prefix ........: " + sJARPrefix);
        System.out.println("JAR pattern........: " + sJARPattern);
        System.out.println("Dir................: " + sDir);
        System.out.println("Parameters.........: " + sParameters);
        String sMainJarFileName = getMainJarFileName(sDir);
        System.out.println("Main JAR file......: " + sMainJarFileName);
        if (sMainJarFileName.equals("")) {
            System.out.println("ERROR..............: " + "No program file found!");
            System.exit(1);
        }
        //if (sJVM.contains(" ")) {
        //    sJVM = "'" + sJVM + "'";
        //}
        //String sExec = sJVM + " " + sOptions + " -classpath " + sClassPath + ";" + sMainJarFileName + " " + sMainClass;
        //String sExec = sJVM + " " + sOptions + (!sClassPath.equalsIgnoreCase("") ? " -classpath " + sClassPath + ";" : "") + (!sDir.equalsIgnoreCase("") ? sDir : "") + sMainJarFileName + " " + sMainClass;
        //MAG 2016.01.14. String sExec = sJVM + " " + sOptions + " -classpath " + (!sClassPath.equalsIgnoreCase(".") ? sClassPath + ";" : "") + (!sDir.equalsIgnoreCase("") ? sDir : "") + sMainJarFileName + " " + sMainClass;
        //MaG 2017.02.01. String sExec = sJVM + " " + sOptions + (!sClassPath.equalsIgnoreCase(".") ? " -classpath " + sClassPath + ";" : " -jar ") + (!sDir.equalsIgnoreCase("") ? sDir : "") + sMainJarFileName + (!sClassPath.equalsIgnoreCase(".") ? " " + sMainClass : ""); //MAG 2016.01.14.
        String sExec = sJVM + " " + sOptions + (!sClassPath.equalsIgnoreCase(".") ? " -classpath " + sClassPath + ";" : " -jar ") + (!sDir.equalsIgnoreCase("") ? sDir : "") + sMainJarFileName + (!sClassPath.equalsIgnoreCase(".") ? " " + sMainClass : "") + " " + sParameters; //MaG 2017.02.01. 
        System.out.println("Start..............: " + sExec);
        try {
            process = runtime.exec(sExec);
            if (bNoConsole) {
                errorGobbler = new StreamGobbler(process.getErrorStream(), "ERROR");
                outputGobbler = new StreamGobbler(process.getInputStream(), "OUTPUT");
                errorGobbler.start();
                outputGobbler.start();
                exitVal = process.waitFor();
                System.out.println("Exit value.........: " + exitVal);
            }
        } catch (IOException ioe) {
            System.err.println(ioe.getLocalizedMessage());
            ioe.printStackTrace(System.err);
            return;
        } catch (InterruptedException ie) {
            System.err.println(ie.getLocalizedMessage());
            ie.printStackTrace(System.err);
            return;
        }
    }

    private String getMainJarFileName(String sDir) {
        VersionFileFilter vff = new VersionFileFilter(sJARPattern);
        String sFileName = "";
        if (sDir.equalsIgnoreCase("")) {
            sDir = ".";
        }
        File dir = new File(sDir);
        if (!dir.isDirectory()) {
            return ("");
        }
        File[] files = dir.listFiles(vff);
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                System.out.println("...................: " + files[i].getName());
                if (sFileName.compareTo(files[i].getName()) < 0) {
                    sFileName = files[i].getName();
                }
            }
        }
        return (sFileName);
    }

    public static void main(String[] args) {
        new VersionLoader(args);
    }
}
