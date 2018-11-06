package hu.mgx.swing;

import java.awt.*;
import javax.swing.*;

public class CommonInternalFrame extends JInternalFrame {

    public CommonInternalFrame() {
        super("", true, true, true, true);
    }

    public CommonInternalFrame(String sTitle) {
        super(sTitle, true, true, true, true);
    }

//    public JComponent getPrintableComponent()
//    {
//        return (null);
//    }
//    public String getExportableTableName()
//    {
//        return ("");
//    }
    public void closing() {
    }

    public boolean canClose() {
        return (true);
    }

    public void setCursorWait() {
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
    }

    public void setCursorDefault() {
        this.setCursor(Cursor.getDefaultCursor());
    }

    public void setMaximumNoError(boolean b) {
        try {
            super.setMaximum(true);
        } catch (java.beans.PropertyVetoException ex) {
        }
    }

    public void formatChanged() {
    }

    public void languageChanged() {
    }
}
