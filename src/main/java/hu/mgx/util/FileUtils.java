package hu.mgx.util;

import hu.mgx.app.common.*;
import java.io.*;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public abstract class FileUtils {

    //source: https://en.wikipedia.org/wiki/List_of_file_signatures
    public static int FT_ERROR = -1;
    public static int FT_UNKNOWN = 0;
    public static int FT_UNICODE = 1;
    public static int FT_CFBF = 2; //Compound File Binary Format (doc, xls, ppt, msg): https://en.wikipedia.org/wiki/Compound_File_Binary_Format
    public static int FT_ZIP = 3; //ZIP

    public FileUtils() {
    }

    public static String readFileIntoString(String sFileName) throws FileNotFoundException, IOException {
        return (new String(readFileIntoByteArray(new File(sFileName))));
    }

    public static String readFileIntoString(String sFileName, ErrorHandlerInterface ehi) {
        String sRetVal = "";
        //File f = new File(sFileName);
        try {
            //sRetVal = new String(readFileIntoByteArray(f));
            sRetVal = new String(readFileIntoByteArray(new File(sFileName)));
        } catch (FileNotFoundException fnfe) {
            ehi.handleError(fnfe);
        } catch (IOException ioe) {
            ehi.handleError(ioe);
        }
        return (sRetVal);
    }

    public static byte[] readFileIntoByteArray(String sFileName) throws FileNotFoundException, IOException {
        return (readFileIntoByteArray(new File(sFileName)));
    }

    public static byte[] readFileIntoByteArray(File file) throws FileNotFoundException, IOException {
        byte[] buffer = new byte[1024];
        FileInputStream fis;
        fis = new FileInputStream(file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int n = 0;
        while (-1 != (n = fis.read(buffer))) {
            baos.write(buffer, 0, n);
        }
        baos.flush();
        baos.close();
        return (baos.toByteArray());
    }

    public static void writeByteArrayToFile(byte[] buffer, File file) throws FileNotFoundException, IOException {
        FileOutputStream fos = null;
        fos = new FileOutputStream(file);
        fos.write(buffer);
        fos.flush();
        fos.close();
    }

    public static String readInputStreamIntoString(InputStream inputStream) throws FileNotFoundException, IOException {
        return (new String(readInputStreamIntoByteArray(inputStream)));
    }

    public static byte[] readInputStreamIntoByteArray(InputStream inputStream) throws FileNotFoundException, IOException {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int n = 0;
        while (-1 != (n = inputStream.read(buffer))) {
            baos.write(buffer, 0, n);
        }
        baos.flush();
        baos.close();
        return (baos.toByteArray());
    }

    public static String getFileNameSeparator() {
        return (File.separator);
    }

    public static String getDirectoryNameWithoutEndigSeparator(String sDir) {
        if (sDir.substring(sDir.length() - 1, sDir.length()).equals(getFileNameSeparator())) {
            return (sDir.substring(0, sDir.length() - 1));
        }
        return (sDir);
    }

    public static String getDirectoryNameWithEndigSeparator(String sDir) {
        if (!sDir.substring(sDir.length() - 1, sDir.length()).equals(getFileNameSeparator())) {
            return (sDir + getFileNameSeparator());
        }
        return (sDir);
    }

    public static boolean renameFile(AppInterface appInterface, String sFrom, String sTo) {
        File fileFrom = null;
        File fileTo = null;

        fileFrom = new File(sFrom);
        fileTo = new File(sTo);
        return (fileFrom.renameTo(fileTo));
    }

    public static long copyFile(AppInterface appInterface, String sFrom, String sTo) {
        File fileFrom = null;
        File fileTo = null;

        fileFrom = new File(sFrom);
        fileTo = new File(sTo);
        return (copyFile(appInterface, fileFrom, fileTo));
    }

    public static long copyFile(AppInterface appInterface, File fileFrom, File fileTo) {
        File fileDestinationDir = null;
        FileInputStream fis = null;
        FileOutputStream fos = null;
        long lBytesCopied = 0;

        fileDestinationDir = fileTo.getParentFile();

        if (!fileDestinationDir.exists()) {
            if (!fileDestinationDir.mkdirs()) {
                return (0);
            }
        }
        try {
            fis = new FileInputStream(fileFrom);
            fos = new FileOutputStream(fileTo);
            byte[] buf = new byte[32768];
            int i = 0;
            while ((i = fis.read(buf)) != -1) {
                fos.write(buf, 0, i);
                lBytesCopied += i;
            }
            if (fis != null) {
                fis.close();
            }
            if (fos != null) {
                fos.flush();
                fos.close();
            }
            fileTo.setLastModified(fileFrom.lastModified());
        } catch (Exception e) {
            appInterface.handleError(e);
            return (0);
        }
        return (lBytesCopied);
    }

    public static String createFileNameWithTimestamp(String sPrefix, String sExtension) {
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyyMMdd_HHmmssSSS");
        String sTimestamp = dateFormat.format(new java.util.Date());
        String sRetVal = sPrefix + "_" + sTimestamp + "." + sExtension;
        return (sRetVal);
    }

    public static boolean createDir(String sDir) {
        File f = new File(sDir);
        return (f.mkdir());
    }

    public static String createDirWithTimestamp(String sPrefix) {
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyyMMdd_HHmmssSSS");
        String sTimestamp = dateFormat.format(new java.util.Date());
        String sDir = sPrefix + "_" + sTimestamp;
        File f = new File(sDir);
        if (f.mkdir()) {
            return (sDir);
        }
        return ("");
    }

    // <root path="c:\windows" name="akármi">
    //     <file name="fájlnév1" size=123></file>
    //     <file name="fájlnév2" size=456></file>
    //     <dir name="dir1">
    //         <file name="fájlnév11" size=789></file>
    //         <dir name="dir11">
    //         </dir>
    //     </dir>
    // </root>
    public static StringBuffer dirxml(String sDir, String sName) {
        return (dirxml(sDir, sName, true, 0, 99));
    }

    public static StringBuffer dirxml(String sDir, String sName, boolean bRecursive, int iLevel, int iMaxLevel) {
        return (dirxml(new File(sDir), sName, bRecursive, iLevel, iMaxLevel));
    }

    public static StringBuffer dirxml(File dir, String sName, boolean bRecursive, int iLevel, int iMaxLevel) {
        StringBuffer sbRetVal = new StringBuffer("");
        StringBuffer sbTmp;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");

        //File dir = new File(sDir);
        String sIndent = StringUtils.leftPad("", (iLevel + 1) * 4);

        if (iLevel >= iMaxLevel) {
            return (sbRetVal);
        }
        if (iLevel == 0) {
            sbRetVal.append("<root path='");
            //sbRetVal.append(sDir);
            sbRetVal.append(dir.getAbsolutePath());
            sbRetVal.append("' name='");
            sbRetVal.append(sName);
            sbRetVal.append("'>");
            sbRetVal.append(StringUtils.sCrLf);
        }
        File[] files = dir.listFiles();

        Arrays.sort(files, new FileComparator());

        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                sbRetVal.append(sIndent);
                sbRetVal.append("<file name='");
                sbRetVal.append(files[i].getName());
                sbRetVal.append("' size=");
                sbRetVal.append(files[i].length());
                sbRetVal.append(" datum='");
                sbRetVal.append(sdf.format(new java.util.Date(files[i].lastModified())));
                sbRetVal.append("' lastmodified=");
                sbRetVal.append(files[i].lastModified());
                sbRetVal.append(" md5='");
                sbRetVal.append(md5x(files[i]));
                sbRetVal.append("'></file>");
                sbRetVal.append(StringUtils.sCrLf);
            }
            if (files[i].isDirectory()) {
                sbRetVal.append(sIndent);
                sbRetVal.append("<dir name='");
                sbRetVal.append(files[i].getName());
                sbRetVal.append("'>");
                if (bRecursive) {
                    sbTmp = dirxml(files[i], sName, bRecursive, iLevel + 1, iMaxLevel);
                    if (sbTmp.length() > 0) {
                        sbRetVal.append(StringUtils.sCrLf);
                        sbRetVal.append(sbTmp);
                        sbRetVal.append(sIndent);
                    }
//                    sbRetVal.append(StringUtils.sCrLf);
//                    sbRetVal.append(dirxml(files[i], sName, bRecursive, iLevel + 1, iMaxLevel));
//                    sbRetVal.append(sIndent);
                }
                sbRetVal.append("</dir>");
                sbRetVal.append(StringUtils.sCrLf);
            }
        }
        if (iLevel == 0) {
            sbRetVal.append("</root>");
            sbRetVal.append(StringUtils.sCrLf);
        }
        return (sbRetVal);
    }

    public static FileInfo getFileInfoStructure(String sDir) {
        return (getFileInfoStructure(new File(sDir)));
    }

    public static FileInfo getFileInfoStructure(String sDir, boolean bDir) {
        return (getFileInfoStructure(new File(sDir), bDir, false));
    }

    public static FileInfo getFileInfoStructure(String sDir, boolean bDir, boolean bFilesFirst) {
        return (getFileInfoStructure(new File(sDir), bDir, bFilesFirst));
    }

    public static FileInfo getFileInfoStructure(File fDir) {
        return (getFileInfoStructure(fDir, false, false));
    }

    public static FileInfo getFileInfoStructure(File fDir, boolean bDir, boolean bFilesFirst) {
        FileInfo fi = new FileInfo(fDir);

        File[] files = fDir.listFiles();
        if (files != null) {
            //Arrays.sort(files, new FileComparator());
            if (bFilesFirst) {
                Arrays.sort(files, createFileComparatorFilesFirst());
            } else {
                Arrays.sort(files, createFileComparatorDirectoriesFirst());
            }

            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    fi.addChild(new FileInfo(files[i]));
                }
                if (files[i].isDirectory()) {
                    fi.addChild(getFileInfoStructure(files[i], bDir, bFilesFirst));
                }
            }
        } else {
            if (bDir) {
                fi.addChild(new FileInfo(fDir));
            }
        }

        return (fi);
    }

    public static File[] getFileInfoLinear(FileInfo fi) {
        File[] f = new File[1];
        Vector<File> vFile = new Vector<File>();

        vFile.addElement(fi.getFile());
        List listChildren = fi.getChildren();
        Iterator<FileInfo> iteratorListChildren = listChildren.iterator();
        for (int i = 0; iteratorListChildren.hasNext(); ++i) {
            FileInfo fii = iteratorListChildren.next();
            f = getFileInfoLinear(fii);
            for (int j = 0; j < f.length; j++) {
                vFile.addElement(f[j]);
            }
        }
        f = new File[vFile.size()];
        vFile.toArray(f);
        return (f);
    }

    public static java.util.Comparator<File> createFileComparatorFilesFirst() {
        return (new FileComparator() {
            @Override
            public int compare(File f1, File f2) {
                if (f1.isDirectory() && !f2.isDirectory()) {
                    return (1);
                }
                if (!f1.isDirectory() && f2.isDirectory()) {
                    return (-1);
                }
                return (f1.getName().compareToIgnoreCase(f2.getName()));
            }
        });
    }

    public static java.util.Comparator<File> createFileComparatorDirectoriesFirst() {
        return (new FileComparator() {
        });
    }

    public static void debugFileInfo(FileInfo fi, AppInterface appInterface) {
        appInterface.logLine(fi.getFile().getName());
        List listChildren = fi.getChildren();
        Iterator<FileInfo> iteratorListChildren = listChildren.iterator();
        for (int i = 0; iteratorListChildren.hasNext(); ++i) {
            FileInfo fii = iteratorListChildren.next();
            debugFileInfo(fii, appInterface);
        }
    }

    public static String md5x(String sFileName) {
        String sRetVal = "";
        try {
            sRetVal = md5(new File(sFileName));
        } catch (IOException ex) {
        } catch (NoSuchAlgorithmException ex) {
        }
        return (sRetVal);
    }

    public static String md5x(File file) {
        String sRetVal = "";
        try {
            sRetVal = md5(file);
        } catch (IOException ex) {
        } catch (NoSuchAlgorithmException ex) {
        }
        return (sRetVal);
    }

    public static String md5(String sFileName) throws FileNotFoundException, IOException, java.security.NoSuchAlgorithmException {
        return (md5(new File(sFileName)));
    }

    public static String md5(File file) throws FileNotFoundException, IOException, java.security.NoSuchAlgorithmException {
        byte[] buffer = new byte[1024];
        FileInputStream fis;
        fis = new FileInputStream(file);
        StringBuilder sbMD5 = new StringBuilder();
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        int n = 0;
        while (-1 != (n = fis.read(buffer))) {
            messageDigest.update(buffer, 0, n);
        }
        byte b[] = messageDigest.digest();
        for (int i = 0; i < b.length; i++) {
            sbMD5.append(hu.mgx.util.StringUtils.right("00" + Integer.toHexString(b[i] & 0xff), 2));
        }
        return (sbMD5.toString());
    }

    public static Vector<String> readTextFileIntoStringVector(ErrorHandlerInterface errorHandlerInterface, File file) {
        return (readTextFileIntoStringVector(errorHandlerInterface, file, null));
    }

    public static Vector<String> readTextFileIntoStringVector(ErrorHandlerInterface errorHandlerInterface, File file, Charset cs) {
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        String sLine = null;
        Vector<String> vLines = new Vector<String>();

        try {
            fis = new FileInputStream(file);
            if (cs == null) {
                isr = new InputStreamReader(fis);
            } else {
                isr = new InputStreamReader(fis, cs);
            }
            br = new BufferedReader(isr);
            while ((sLine = br.readLine()) != null) {
                vLines.add(sLine);
            }
        } catch (FileNotFoundException fnfe) {
            errorHandlerInterface.handleError(fnfe);
        } catch (IOException ioe) {
            errorHandlerInterface.handleError(ioe);
        }
        return (vLines);
    }

    public static Vector<String> readTextFileIntoStringVector(ErrorHandlerInterface errorHandlerInterface, String sFileName) {
        return (readTextFileIntoStringVector(errorHandlerInterface, sFileName, null));
    }

    public static Vector<String> readTextFileIntoStringVector(ErrorHandlerInterface errorHandlerInterface, String sFileName, Charset cs) {
        return (readTextFileIntoStringVector(errorHandlerInterface, new File(sFileName), cs));
    }

    public static Vector<String> loadTextFileToStringVector(ErrorHandlerInterface errorHandlerInterface, File f) {
        boolean bIsUnicode = FileUtils.isUnicode(f);
        Vector<String> vLines = null;
        if (bIsUnicode) {
            vLines = FileUtils.readTextFileIntoStringVector(errorHandlerInterface, f, java.nio.charset.StandardCharsets.UTF_16);
        } else {
            vLines = FileUtils.readTextFileIntoStringVector(errorHandlerInterface, f);
        }
        return (vLines);
    }

    public static Vector<String> loadTextFileToStringVector(ErrorHandlerInterface errorHandlerInterface, String sFileName) {
        return (loadTextFileToStringVector(errorHandlerInterface, new File(sFileName)));
    }

    public static void writeStringVectorToTextFile(ErrorHandlerInterface errorHandlerInterface, Vector<String> vLines, String sFileName) {
        FileOutputStream fos = null;
        PrintWriter pw = null;
        try {
            fos = new FileOutputStream(sFileName);
            pw = new PrintWriter(fos);
        } catch (FileNotFoundException fnfe) {
            errorHandlerInterface.handleError(fnfe);
            return;
        }
        for (int iLine = 0; iLine < vLines.size(); iLine++) {
            pw.println(vLines.elementAt(iLine));
        }
        pw.flush();
        pw.close();
    }

    public static String getExtension(String sFileName) {
        if (sFileName.contains(".")) {
            return (sFileName.substring(sFileName.lastIndexOf(".") + 1));
        }
        return ("");
    }

    public static String getNameWithoutExtension(String sFileName) {
        if (sFileName.contains(".")) {
            return (sFileName.substring(0, sFileName.lastIndexOf(".")));
        }
        return (sFileName);
    }

    public static void hexDump(String sFileName) {
        try (InputStream in = new FileInputStream(sFileName)) {
            int r;
            while ((r = in.read()) != -1) {
                System.out.format("%02x ", 0xFF & r);
            }
            System.out.println();
        } catch (IOException ioe) {
            System.err.println(ioe.getLocalizedMessage());
            ioe.printStackTrace(System.err);
        }
    }

    public static Vector<Integer> getFileTypeInfo(File f) {
        Vector<Integer> vReturn = null;
        byte[] b = null;
        try {
            b = hu.mgx.util.FileUtils.readFileIntoByteArray(f);
        } catch (FileNotFoundException fnfe) {
            return (vReturn);
        } catch (IOException ioe) {
            return (vReturn);
        }
        if (b == null) {
            return (vReturn);
        }

        vReturn = new Vector<Integer>();
        if (b.length > 2) {
            if (Byte.toUnsignedInt(b[0]) == 0xFF && Byte.toUnsignedInt(b[1]) == 0xFE) {
                vReturn.add(FT_UNICODE);
            }
            if (Byte.toUnsignedInt(b[0]) == 0xFE && Byte.toUnsignedInt(b[1]) == 0xFF) {
                vReturn.add(FT_UNICODE);
            }
        }
        if (b.length > 8) {
            if (Byte.toUnsignedInt(b[0]) == 0xD0 && Byte.toUnsignedInt(b[1]) == 0xCF && Byte.toUnsignedInt(b[2]) == 0x11 && Byte.toUnsignedInt(b[3]) == 0xE0 && Byte.toUnsignedInt(b[4]) == 0xA1 && Byte.toUnsignedInt(b[5]) == 0xB1 && Byte.toUnsignedInt(b[6]) == 0x1A && Byte.toUnsignedInt(b[7]) == 0xE1) {
                vReturn.add(FT_CFBF);
            }
        }
        if (b.length > 4) {
            if (Byte.toUnsignedInt(b[0]) == 0x50 && Byte.toUnsignedInt(b[1]) == 0x4B && Byte.toUnsignedInt(b[2]) == 0x03 && Byte.toUnsignedInt(b[3]) == 0x04) {
                vReturn.add(FT_ZIP);
            }
        }
        if (vReturn.size() == 0) {
            vReturn.add(FT_UNKNOWN);
        }
        return (vReturn);
    }

    public static boolean isUnicode(File f) {
        //return (getFileType(f) == FT_UNICODE);
        byte[] b = null;
        try {
            b = hu.mgx.util.FileUtils.readFileIntoByteArray(f);
        } catch (FileNotFoundException fnfe) {
            return (false);
        } catch (IOException ioe) {
            return (false);
        }
        if (b == null) {
            return (false);
        }
        if (b.length > 2) {
            if (Byte.toUnsignedInt(b[0]) == 0xFF && Byte.toUnsignedInt(b[1]) == 0xFE) {
                return (true);
            }
            if (Byte.toUnsignedInt(b[0]) == 0xFE && Byte.toUnsignedInt(b[1]) == 0xFF) {
                return (true);
            }
        }
        return (false);
    }

    public static boolean isExists(String sFilename) {
        try {
            return (new File(sFilename).exists());
        } catch (SecurityException se) {
            return (false);
        }
    }
}
