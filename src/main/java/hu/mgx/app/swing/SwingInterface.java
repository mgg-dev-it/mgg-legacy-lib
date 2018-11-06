package hu.mgx.app.swing;

import java.awt.Color;
import java.util.HashMap;
import javax.print.attribute.standard.MediaSizeName;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;

public interface SwingInterface {

    public abstract JFrame getAppFrame();

    public abstract void addLanguageXML(String sXMLConfig, String sEncoding);

    public abstract String getBaseTitle();

    public abstract String getFullTitle();

    public abstract JEditorPane getLogPane();

    public abstract void setDesktopBackground(Color bg);

    public abstract HashMap<String, JMenuItem> getMenuMap();

    public abstract String getDateTimeVersion();

    public abstract void setCursorWait();

    public abstract void setCursorDefault();

    public abstract void displayProgressValue(int iValue);

    public abstract void displayProgressWait(boolean bDisplay);

    public abstract JInternalFrame[] getInternalFrames();

    public abstract void viewPdf(byte[] b);

    public abstract void viewPdf(byte[] b, String sTitle);

    public abstract void viewPdf(byte[] b, String sTitle, MediaSizeName mediaSize);

    public abstract void viewPdf(byte[] b, String sTitle, MediaSizeName mediaSize, boolean bMaximized);

    public abstract void viewAndPrintPdf(byte[] b);

    public abstract void viewAndPrintPdf(byte[] b, String sTitle);

    public abstract void viewAndPrintPdf(byte[] b, String sTitle, MediaSizeName mediaSize);

}
