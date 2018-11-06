package hu.mag.swing.table;

import hu.mag.swing.MagButton;
import hu.mgx.swing.*;
import hu.mgx.app.swing.*;
import hu.mgx.swing.table.MemoryTable;

import java.awt.*;

public class MaintInternalFrame extends CommonInternalFrame {

    private SwingApp app;
    private CommonPanel mainPane;
    private String sConnectionName = "";
    private String sTableName = "";
    private MagTablePanel magTablePanel = null;
    private MemoryTable memoryTable = null;
    private String sDisplayName = "";

    public MaintInternalFrame(SwingApp swingApp, String sConnectionName, String sTableName) {
        this.app = swingApp;
        this.sConnectionName = sConnectionName;
        this.sTableName = sTableName;
        init();
    }

    public MaintInternalFrame(SwingApp swingApp, String sConnectionName, MemoryTable memoryTable, String sDisplayName) {
        this.app = swingApp;
        this.sConnectionName = sConnectionName;
        this.sTableName = null;
        this.memoryTable = memoryTable;
        this.sDisplayName = sDisplayName;
        init();
    }

    private void init() {
        //setTitle(sTableName);
        mainPane = new CommonPanel();
        setContentPane(mainPane);
        mainPane.setPreferredSize(AppUtils.getResizedScreenSize(0.8));
        //app.setLogLevel(LoggerInterface.LOG_DEBUG);
        app.connect(sConnectionName);
        String sTitle = "";
        if (sTableName != null) {
            magTablePanel = new MagTablePanel(app, sConnectionName, sTableName);
            magTablePanel.getMagTable().setAddNewRowAtTheEnd(true);
            sTitle = sTableName;
            if (magTablePanel.getMagTableModel().getTableInfo() != null) {
                if (magTablePanel.getMagTableModel().getTableInfo().getDisplayName() != null) {
                    sTitle = magTablePanel.getMagTableModel().getTableInfo().getDisplayName();
                }
            }
        } else {
            magTablePanel = new MagTablePanel(app, sConnectionName, memoryTable);
            //magTablePanel.setButtonsVisible(false);
            magTablePanel.getMagButtonPanel().setButtonVisible(false, MagButton.BUTTON_NEW);
            magTablePanel.getMagButtonPanel().setButtonVisible(false, MagButton.BUTTON_REFRESH);
            magTablePanel.getMagButtonPanel().setButtonVisible(false, MagButton.BUTTON_DETAILS);
            magTablePanel.getMagButtonPanel().setButtonVisible(false, MagButton.BUTTON_SAVE);
            magTablePanel.getMagButtonPanel().setButtonVisible(false, MagButton.BUTTON_DELETE);
            magTablePanel.getMagButtonPanel().setButtonVisible(false, MagButton.BUTTON_PDF_VIEW);
            sTitle = sDisplayName;
        }
        magTablePanel.setParentFrame(app);
        mainPane.addToGrid(magTablePanel, 0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.BOTH);
        setTitle(sTitle);

        pack();
        setVisible(true);
    }

    @Override
    public void formatChanged() {
        magTablePanel.formatChanged();
    }

    public MagTablePanel getMagTablePanel() {
        return (magTablePanel);
    }

    public void setReadOnly() {
        magTablePanel.getMagTableModel().setAllColumnEditable(false);
        magTablePanel.getMagButtonPanel().setEnabled(false);
        magTablePanel.getMagButtonPanel().setVisible(false);
    }
}
