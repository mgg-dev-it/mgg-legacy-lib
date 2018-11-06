package hu.mgx.util;

import java.io.File;
import java.util.Vector;

public class FileInfo {

    private File f = null;
    private Vector<FileInfo> vChildren = null;
    //private Vector<File> vFiles = null;

    public FileInfo(File f) {
        init(f);
    }

    private void init(File f) {
        this.f = f;
        vChildren = new Vector<FileInfo>();
    }

    public void addChild(FileInfo fi) {
        vChildren.addElement(fi);
    }

    public Vector<FileInfo> getChildren() {
        return (vChildren);
    }

    public File getFile() {
        return (f);
    }
}
