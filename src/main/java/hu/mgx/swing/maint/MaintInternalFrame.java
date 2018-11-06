package hu.mgx.swing.maint;

import hu.mgx.swing.CommonInternalFrame;
import hu.mgx.swing.CommonPanel;
import java.awt.*;
import java.awt.event.*;

import hu.mgx.app.common.*;
import hu.mgx.db.*;
import hu.mgx.app.swing.*;

public class MaintInternalFrame extends CommonInternalFrame implements ActionListener, MaintPanelListener
{

    private CommonPanel mainPane;
    private MaintPanel maintPanel;

    public MaintInternalFrame(DataBase db, String sTableName, AppInterface appInterface, int iUserId, boolean bInsert, boolean bUpdate, boolean bDelete, boolean bExcelExport, int iSortByColumn, MaintPanelExtensionInterface maintPanelExtensionInterface)
    {
        super();
        mainPane = new CommonPanel();
        setContentPane(mainPane);
        maintPanel = new MaintPanel(db, sTableName, appInterface, this, iUserId, true, bInsert, bUpdate, bDelete, bExcelExport, iSortByColumn, maintPanelExtensionInterface);
        mainPane.addToGrid(maintPanel, 0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.BOTH);
        pack();
    }

    public MaintInternalFrame(CONN Conn, TableDefinition td, AppInterface appInterface, int iUserId, boolean bInsert, boolean bUpdate, boolean bDelete, boolean bExcelExport, int iSortByColumn, MaintPanelExtensionInterface maintPanelExtensionInterface)
    {
        super();
        mainPane = new CommonPanel();
        setContentPane(mainPane);
        maintPanel = new MaintPanel(Conn, td, appInterface, this, iUserId, true, bInsert, bUpdate, bDelete, bExcelExport, iSortByColumn, maintPanelExtensionInterface);
        mainPane.addToGrid(maintPanel, 0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.BOTH);
        pack();
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand().equals("New"))
        {
            maintPanel.actionNew();
        }
        if (e.getActionCommand().equals("Edit"))
        {
            maintPanel.actionEdit();
        }
        if (e.getActionCommand().equals("OK"))
        {
            maintPanel.actionOK();
        }
        if (e.getActionCommand().equals("Delete"))
        {
            maintPanel.actionDelete();
        }
        if (e.getActionCommand().equals("Refresh"))
        {
        }
        if (e.getActionCommand().equals("Excel"))
        {
            maintPanel.excelExport();
        }
        if (e.getActionCommand().equals("Escape"))
        {
            maintPanel.actionEscape();
        }
    }

    public void maintPanelClose()
    {
        this.dispose();
    }
}
