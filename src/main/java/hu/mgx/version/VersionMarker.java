package hu.mgx.version;

import java.io.*;
import java.text.*;

public class VersionMarker
{

    private String sJarFileName = "";
    private static SimpleDateFormat fileNameDateTimeFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");

    public VersionMarker(String[] args)
    {
        init(args);
    }

    private void init(String[] args)
    {
        for (int i = 0; i < args.length; i += 2)
        {
            if (args[i].equalsIgnoreCase("-jartouch"))
            {
                sJarFileName = args[i + 1];
                touch(sJarFileName);
            }
        }
    }

    private void touch(String sFileName)
    {
        int iInsertionPoint = -1;
        for (int i = 0; i < sFileName.length(); i++)
        {
            if (sFileName.charAt(i) == '.')
            {
                iInsertionPoint = i;
            }
        }
        if (iInsertionPoint == -1)
        {
            iInsertionPoint = sFileName.length();
        }
        File fOrigin = new File(sFileName);
        File fNew;
        long lLastModified = fOrigin.lastModified();
        java.util.Date dateLastModified;
        String sLastModified;
        String sNewFileName;
        if (lLastModified > 0)
        {
            dateLastModified = new java.util.Date(lLastModified);
            sLastModified = fileNameDateTimeFormat.format(dateLastModified);
            System.err.println(sLastModified);
            sNewFileName = sFileName.substring(0, iInsertionPoint) + sLastModified + sFileName.substring(iInsertionPoint);
            System.err.println(sNewFileName);
            fNew = new File(sNewFileName);
            fOrigin.renameTo(fNew);
        }
    }

    public static void main(String[] args)
    {
        new VersionMarker(args);
    }
}
