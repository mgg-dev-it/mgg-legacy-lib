package hu.mgx.util;

import hu.mgx.app.common.AppInterface;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.*;

/**
 *
 * @author MaG
 */
public abstract class ZipUtils {

    // maybe ... :
    // http://docs.oracle.com/javase/7/docs/technotes/guides/io/fsp/zipfilesystemprovider.html
    //+methods:
    // - File ...
    // - list (vector?) of filenames
    // - list (vector?) of Files
    //
    //
    //
    public static void createZipFile(AppInterface appInterface, String sZipfileName, String sFileOrDirectory) {
        createZipFile(appInterface, sZipfileName, sFileOrDirectory, StandardCharsets.UTF_8);
    }

    public static void createZipFile(AppInterface appInterface, String sZipfileName, String sFileOrDirectory, Charset charset) {
        addToZipFile(appInterface, sZipfileName, sFileOrDirectory, charset, true);
    }

    public static void addToZipFile(AppInterface appInterface, String sZipfileName, String sFileOrDirectory) {
        addToZipFile(appInterface, sZipfileName, sFileOrDirectory, false);
    }

    public static void addToZipFile(AppInterface appInterface, String sZipfileName, String sFileOrDirectory, Charset charset) {
        addToZipFile(appInterface, sZipfileName, sFileOrDirectory, charset, false);
    }

    public static void addToZipFile(AppInterface appInterface, String sZipfileName, String sFileOrDirectory, boolean bCreateZipIfNeeded) {
        addToZipFile(appInterface, sZipfileName, sFileOrDirectory, StandardCharsets.UTF_8, false);
    }

    public static void addToZipFile(AppInterface appInterface, String sZipfileName, String sFileOrDirectory, Charset charset, boolean bCreateZipIfNeeded) {
        File fileZip;
        FileOutputStream fos;
        ZipOutputStream zos;
        ZipEntry ze;
        File fileSource;
        FileInputStream fis;
        byte[] buffer = new byte[1024];
        int iLength;

        fileSource = new File(sFileOrDirectory);
        if (!fileSource.exists()) {
            appInterface.handleError(sFileOrDirectory + " not exists!");
            return;
        }
        fileZip = new File(sZipfileName);
        if (fileZip.isDirectory()) {
            appInterface.handleError(sZipfileName + " is directory!");
            return;
        }
        if (!bCreateZipIfNeeded && !fileZip.exists()) {
            appInterface.handleError(sZipfileName + " not exists!");
        }
        if (fileZip.exists()) {
            //@todo task: copy from one zip to "another" (to the same ...)
        }
        try {
            fos = new FileOutputStream(sZipfileName);
            zos = new ZipOutputStream(fos, charset);
            ze = new ZipEntry(fileSource.getName());
            ze.setMethod(ZipEntry.DEFLATED);
            zos.putNextEntry(ze);

            if (fileSource.isDirectory()) {
                //many files
            }
            fis = new FileInputStream(sFileOrDirectory);
            while ((iLength = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, iLength);
            }
            fis.close();
            zos.flush();
            zos.closeEntry();
            zos.close();
        } catch (FileNotFoundException e) {
            appInterface.handleError(e);
        } catch (IOException ioe) {
            appInterface.handleError(ioe);
        }
    }

    public static void addToZIP(AppInterface appInterface, String sSourceDirOrFile, String sZIPFile) {
        File fSourceDirOrFile = new File(sSourceDirOrFile);
        if (!fSourceDirOrFile.exists()) {
            return;
        }
        File fZIPFile = new File(sZIPFile);
        String sZIPDir = fZIPFile.getParent();
        Map<String, String> env = new HashMap<>();
        env.put("create", "true");
        URI uri = URI.create(StringUtils.stringReplace("jar:file:///" + sZIPFile, "\\", "/"));
        int iRootNameCount = Paths.get(sSourceDirOrFile).getNameCount();
        try {
            Files.createDirectories(Paths.get(sZIPDir));
        } catch (IOException ioe) {
            appInterface.handleError(ioe);
        }

        File[] f = new File[0];
        if (fSourceDirOrFile.isFile()) {
            f = new File[1];
            f[0] = fSourceDirOrFile;
        }
        if (fSourceDirOrFile.isDirectory()) {
            f = FileUtils.getFileInfoLinear(FileUtils.getFileInfoStructure(fSourceDirOrFile, true, true));
        }
        try (FileSystem zipfs = FileSystems.newFileSystem(uri, env)) {
            for (int i = 0; i < f.length; i++) {
//                appInterface.logLine(f[i].getAbsolutePath());
                Path pathToSourceFile = Paths.get(f[i].getAbsolutePath());
                Path pathInZipfile = zipfs.getPath(f[i].getAbsolutePath());
//                appInterface.logLine(pathToSourceFile.toString());
//                appInterface.logLine(pathInZipfile.toString());
//                appInterface.logLine(Integer.toString(iRootNameCount));
//                appInterface.logLine(Integer.toString(pathToSourceFile.getNameCount()));
                String sPathInZIP = "";
                if (iRootNameCount < pathToSourceFile.getNameCount() + (f[i].isDirectory() ? 1 : 0)) {
                    Path pathShort = pathInZipfile.subpath(iRootNameCount, pathToSourceFile.getNameCount() + (f[i].isDirectory() ? 1 : 0));
                    Files.createDirectories(pathShort);
                    sPathInZIP = pathShort.toString();
                }
                //Path pathShort = pathInZipfile.subpath(iRootNameCount, pathToSourceFile.getNameCount() + (f[i].isDirectory() ? 1 : 0));
                //Files.createDirectories(pathShort);
                if (!f[i].isDirectory()) {
                    pathInZipfile = zipfs.getPath(sPathInZIP + java.io.File.separator + f[i].getName());
                    Files.copy(pathToSourceFile, pathInZipfile, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
                }
            }
        } catch (IOException ioe) {
            appInterface.handleError(ioe);
        }
    }

    public static void saveDirectoryContentIntoZip(AppInterface appInterface, String sSourceDir, String sZIPFile) {
        File fSourceDir = new File(sSourceDir);
        if (!fSourceDir.exists()) {
            return;
        }
        if (!fSourceDir.isDirectory()) {
            return;
        }
        File fZIPFile = new File(sZIPFile);
        String sZIPDir = fZIPFile.getParent();
        Map<String, String> env = new HashMap<>();
        env.put("create", "true");
        URI uri = URI.create(StringUtils.stringReplace("jar:file:///" + sZIPFile, "\\", "/"));
        int iRootNameCount = Paths.get(sSourceDir).getNameCount();
        try {
            Files.createDirectories(Paths.get(sZIPDir));
        } catch (IOException ioe) {
            appInterface.handleError(ioe);
        }
        try (FileSystem zipfs = FileSystems.newFileSystem(uri, env)) {
            File[] f = FileUtils.getFileInfoLinear(FileUtils.getFileInfoStructure(fSourceDir, true, true));
            for (int i = 0; i < f.length; i++) {
                Path pathToSourceFile = Paths.get(f[i].getAbsolutePath());
                Path pathInZipfile = zipfs.getPath(f[i].getAbsolutePath());
                Path pathShort = pathInZipfile.subpath(iRootNameCount, pathToSourceFile.getNameCount() + (f[i].isDirectory() ? 1 : 0));
                Files.createDirectories(pathShort);
                if (!f[i].isDirectory()) {
                    pathInZipfile = zipfs.getPath(pathShort.toString() + java.io.File.separator + f[i].getName());
                    Files.copy(pathToSourceFile, pathInZipfile, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
                }
            }
        } catch (IOException ioe) {
            appInterface.handleError(ioe);
        }
    }
}
