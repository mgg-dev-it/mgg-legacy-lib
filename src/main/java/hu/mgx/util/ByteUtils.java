package hu.mgx.util;

import java.io.*;

public abstract class ByteUtils {

    private static char[] hexDigits
            = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
            };

    public ByteUtils() {
    }

    public static byte[] mergeByteArrays(byte[] b1, byte[] b2) {
        byte[] buffer = new byte[b1.length + b2.length];
        System.arraycopy(b1, 0, buffer, 0, b1.length);
        System.arraycopy(b2, 0, buffer, b1.length, b2.length);
        return (buffer);
    }

    public static byte[] mergeByteArrays(byte[] b1, int iLength1, byte[] b2, int iLength2) {
        if (iLength1 > b1.length) {
            iLength1 = b1.length;
        }
        if (iLength2 > b2.length) {
            iLength2 = b2.length;
        }
        byte[] buffer = new byte[iLength1 + iLength2];
        System.arraycopy(b1, 0, buffer, 0, iLength1);
        System.arraycopy(b2, 0, buffer, iLength1, iLength2);
        return (buffer);
    }

//    public static byte[] byteArrayCopy(byte[] b, int iOffset, int iLength)
//    {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        if (iLength < 0)
//        {
//            iLength = b.length - iOffset;
//        }
//        baos.write(b, iOffset, iLength);
//        return (baos.toByteArray());
//    }
    public static int byteArraySearch(byte[] miben, byte[] mit, int iOffset, int iLength) {
        int iRetVal = -1;
        if (iLength < 0) {
            iLength = miben.length - iOffset;
        }
        for (int i = iOffset; (iRetVal < 0) && i < (iOffset + iLength - mit.length); ++i) {
            if (miben[i] == mit[0]) {
                for (int j = 0; (j < mit.length) && (miben[i + j] == mit[j]); j++) {
                    if ((j == mit.length - 1) && (miben[i + j] == mit[j])) {
                        return (i);
                    }
                }
            }
        }
        return (iRetVal);
    }

    public static String byteArraytoHexString(byte[] b) {
        StringBuffer sbRetVal = new StringBuffer("");
        int iByte;
        int high;
        int low;
        for (int i = 0; i < b.length; i++) {
            iByte = (b[i] < 0 ? 255 - ~b[i] : b[i]);
            high = iByte / 16;
            sbRetVal.append(hexDigits[high]);
            low = iByte % 16;
            sbRetVal.append(hexDigits[low]);
        }
        return (sbRetVal.toString());
    }
}
