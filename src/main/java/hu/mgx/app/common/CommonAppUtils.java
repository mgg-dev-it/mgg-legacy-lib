package hu.mgx.app.common;

import hu.mgx.util.StringUtils;
import java.util.*;

public abstract class CommonAppUtils {

    public static HashMap<String, String> preProcessArgs(String[] args) {
        HashMap<String, String> hashMap = new HashMap<String, String>();

        for (int i = 0; i < args.length; i++) {
            //if (args[i].startsWith("-")) {
            if (args[i].startsWith("-") && !StringUtils.isDigits(StringUtils.mid(args[i], 1, 1))) {
                //if ((i == args.length - 1) || (args[i + 1].startsWith("-"))) {
                if ((i == args.length - 1) || (args[i + 1].startsWith("-") && !StringUtils.isDigits(StringUtils.mid(args[i + 1], 1, 1)))) {
                    hashMap.put(args[i], null);
                } else {
                    hashMap.put(args[i], args[i + 1]);
                    ++i;
                }
            } else {
                hashMap.put(args[i], null);
            }
        }
        return (hashMap);
    }

    public static String getParameterValue(HashMap<String, String> hashMap, String sParameterName) {
        return (StringUtils.isNull(hashMap.get(sParameterName), ""));
    }

    public static String getParameterValueUpperCase(HashMap<String, String> hashMap, String sParameterName) {
        return (StringUtils.isNull(hashMap.get(sParameterName), "").toUpperCase());
    }

    public static boolean parameterExists(HashMap<String, String> hashMap, String sParameterName) {
        if (hashMap.containsKey(sParameterName)) {
            return (true);
        }
        return (false);
    }

    public static String getMemoryInfo() {
        Runtime runtime = Runtime.getRuntime();;
        long lDivider = 1;
        String sUnit = "Byte";
        long lFreeMemory = runtime.freeMemory();
        long lTotalMemory = runtime.totalMemory();
        long lMaxMemory = runtime.maxMemory();
        if (lMaxMemory > 1023) {
            lDivider = 1024;
            sUnit = "KByte";
        }
        if (lMaxMemory > 1048575) {
            lDivider = 1048576;
            sUnit = "MByte";
        }
        return ("memory (free/total/maximum): " + Long.toString(lFreeMemory / lDivider) + "/" + Long.toString(lTotalMemory / lDivider) + "/" + Long.toString(lMaxMemory / lDivider) + " " + sUnit);
    }
}
