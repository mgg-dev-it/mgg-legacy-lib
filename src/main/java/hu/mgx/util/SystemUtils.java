package hu.mgx.util;

import java.text.*;

public abstract class SystemUtils
{

    private final static DecimalFormat decimalFormat = new DecimalFormat("#,##0 byte");

    public final static void memory()
    {
        Runtime runtime = Runtime.getRuntime();
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
        System.out.println("JVM free memory....: " + sFreeMemory);
        System.out.println("JVM total memory...: " + sTotalMemory);
        System.out.println("JVM maximum memory.: " + sMaxMemory);
    }

    public final static String getSystemProperty(String sPropertyName)
    {
        return (System.getProperty(sPropertyName, ""));
    }

    public final static void systemProperty(String sPropertyName)
    {
        System.out.println(StringUtils.rightPad(sPropertyName, 20) + System.getProperty(sPropertyName, ""));
    }

    public final static void systemProperties()
    {
        systemProperty("file.separator");
        systemProperty("java.class.path");
        systemProperty("java.class.version");
        systemProperty("java.home");
        systemProperty("java.vendor");
        systemProperty("java.vendor.url");
        systemProperty("java.version");
        systemProperty("line.separator");
        systemProperty("os.arch");
        systemProperty("os.name");
        systemProperty("os.version");
        systemProperty("user.dir");
        systemProperty("user.home");
        systemProperty("user.name");
    }
}
