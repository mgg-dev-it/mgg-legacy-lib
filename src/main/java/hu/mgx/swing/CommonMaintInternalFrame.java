package hu.mgx.swing;

import hu.mag.swing.table.MagTablePanel;
import hu.mgx.app.swing.*;
import hu.mgx.swing.table.MemoryTable;

import java.awt.*;

public class CommonMaintInternalFrame extends CommonInternalFrame {

    private SwingApp app;
    private CommonPanel mainPane;
    private String sConnectionName = "";
    private String sTableName = "";
    private MemoryTable memoryTable = null;
    private MagTablePanel magTablePanel = null;
    private String sDisplayName = "";

    public CommonMaintInternalFrame(SwingApp swingApp, String sConnectionName, String sTableName) {
        this.app = swingApp;
        this.sConnectionName = sConnectionName;
        this.sTableName = sTableName;
        memoryTable = null;
        init();
    }

    public CommonMaintInternalFrame(SwingApp swingApp, String sConnectionName, MemoryTable memoryTable, String sDisplayName) {
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
        mainPane.setPreferredSize(new Dimension(1000, 500));
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
            magTablePanel.setButtonsVisible(false);
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

    public void setReadOnly() {
        magTablePanel.getMagTableModel().setAllColumnEditable(false);
        magTablePanel.getMagButtonPanel().setEnabled(false);
        magTablePanel.getMagButtonPanel().setVisible(false);
    }
}
