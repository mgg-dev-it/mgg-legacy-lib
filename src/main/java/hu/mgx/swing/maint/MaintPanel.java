package hu.mgx.swing.maint;

import hu.mgx.swing.CommonPanel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;

import hu.mgx.app.common.*;
import hu.mgx.db.*;
import hu.mgx.excel.*;
import hu.mgx.sql.*;
import hu.mgx.swing.*;
import hu.mgx.app.swing.*;
import hu.mgx.swing.table.*;

public class MaintPanel extends CommonPanel implements ActionListener, KeyListener
{

    public static final long serialVersionUID = 0;
    private Dimension dimPreferred = new Dimension(720, 500); //800 x 600 -hoz?
    private DataBase db = null;
    private Connection connection = null;
    private String sTableName;
    private TableDefinition td;
    private MgxTable mgxTable;
    private MgxTableModel mgxTableModel;
    private TableSorterNewStyle sorter;
    private JScrollPane jScrollPane;
    private JScrollPane jScrollPane2;
    private AppInterface appInterface = null;
    private JTabbedPane jTabbedPane;
    private CommonPanel listPanel;
    private CommonPanel detailPanel;
    private CommonEditorField cef;
    private CommonBitField cbf;
    private CommonTextField ctf;
    private CommonLookupField clf;
    private Vector<DataField> vFields;
    private MaintPanelListener maintPanelListener;
    private Record oldRecord;
    private Record newRecord;
    private SQL sql;
    private boolean bExistingRecord = false;
    private int iUserId;
    private int iFirstActiveField = 0;
    private boolean bButtons;
    private JButton btnNew;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnOK;
    private JButton btnExcelExport;
    private boolean bInsert = true;
    private boolean bUpdate = true;
    private boolean bDelete = true;
    private boolean bExcelExport = true;
    private int iSortByColumn = -1;
    private MaintPanelExtensionInterface maintPanelExtensionInterface = null;

    /*
     * régi, eredeti
     */
    public MaintPanel(DataBase db, String sTableName, AppInterface appInterface, MaintPanelListener maintPanelListener, int iUserId, boolean bButtons, boolean bInsert, boolean bUpdate, boolean bDelete, boolean bExcelExport, int iSortByColumn, MaintPanelExtensionInterface maintPanelExtensionInterface)
    {
        super();
        setBorder(new EmptyBorder(0, 0, 0, 0));
        this.appInterface = appInterface;
        this.db = db;
        this.connection = db.getConnection();
        this.sTableName = sTableName;
        this.maintPanelListener = maintPanelListener;
        this.iUserId = iUserId;
        this.bButtons = bButtons;
        this.bInsert = bInsert;
        this.bUpdate = bUpdate;
        this.bDelete = bDelete;
        this.bExcelExport = bExcelExport;
        this.iSortByColumn = iSortByColumn;
        sql = new SQL(db.getConn(), appInterface);
        this.maintPanelExtensionInterface = maintPanelExtensionInterface;
        init();
    }

    /*
     * új változat az általános karbantartóhoz 2009.01.28
     */
    public MaintPanel(CONN Conn, TableDefinition td, AppInterface appInterface, MaintPanelListener maintPanelListener, int iUserId, boolean bButtons, boolean bInsert, boolean bUpdate, boolean bDelete, boolean bExcelExport, int iSortByColumn, MaintPanelExtensionInterface maintPanelExtensionInterface)
    {
        super();
        setBorder(new EmptyBorder(0, 0, 0, 0));
        this.td = td;
        this.appInterface = appInterface;
        this.connection = Conn.getConnection();
        this.maintPanelListener = maintPanelListener;
        this.iUserId = iUserId;
        this.bButtons = bButtons;
        this.bInsert = bInsert;
        this.bUpdate = bUpdate;
        this.bDelete = bDelete;
        this.bExcelExport = bExcelExport;
        this.iSortByColumn = iSortByColumn;
        sql = new SQL(Conn, appInterface);
        this.maintPanelExtensionInterface = maintPanelExtensionInterface;
        init();
    }

    private void init()
    {
        vFields = new Vector<DataField>();
        jTabbedPane = new JTabbedPane();

        mgxTableModel = new MgxTableModel(connection, appInterface);
        if (db != null)
        {
            try
            {
                td = db.getTableDefinitionByName(sTableName);
            }
            catch (DataBaseException dbe)
            {
                appInterface.handleError(dbe); //2009.01.28
            }
        }
        //table.setDebug(true);
        td.executeSetFieldRealTimeSQLLookupValues(connection);
        mgxTableModel.init(td);
        sorter = new TableSorterNewStyle(mgxTableModel);
        mgxTable = new MgxTable(sorter, this);
        sorter.addMouseListenerToHeaderInTable(mgxTable);

        if (iSortByColumn > -1)
        {
            sorter.sortByColumn(iSortByColumn);
        }

        mgxTable.setPreferredScrollableViewportSize(dimPreferred);
        mgxTable.addKeyListener(this);
        for (int i = 0; i < td.getFieldCount(); i++)
        {
            try
            {
                DataUtils.setTableColumn(mgxTable, i, td.getFieldDefinition(i), appInterface);
            }
            catch (TableDefinitionException e)
            {
            }
        }

        listPanel = createListPanel();
        setButtons();
        detailPanel = createDetailPanel();
        jTabbedPane.addTab(appInterface.getLanguageString(DefaultLanguageConstants.STRING_CAPTION_LIST), null, listPanel, appInterface.getLanguageString(DefaultLanguageConstants.STRING_CAPTION_LIST));
        jTabbedPane.setMnemonicAt(0, 'L');
        jScrollPane2 = new JScrollPane(detailPanel);
        //jTabbedPane.addTab ("Részletek", null, jScrollPane2, "Részletek");
        jTabbedPane.addTab(appInterface.getLanguageString(DefaultLanguageConstants.STRING_CAPTION_DETAILS), null, jScrollPane2, appInterface.getLanguageString(DefaultLanguageConstants.STRING_CAPTION_DETAILS));
        jTabbedPane.setMnemonicAt(1, 'R');
        jTabbedPane.setFocusable(false);
        jTabbedPane.setEnabledAt(1, false);
        addToGrid(jTabbedPane, 0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.BOTH);

        if (mgxTable.getRowCount() > 0)
        {
            mgxTable.setRowSelectionInterval(0, 0);
        }
        setFocusToTable();
    }

    private void setButtons()
    {
        if (btnNew != null)
        {
            btnNew.setEnabled(bInsert);
        }
        if (btnEdit != null)
        {
            btnEdit.setEnabled((mgxTable.getRowCount() > 0) && (mgxTable.getSelectedRowCount() > 0));
        }
        if (btnDelete != null)
        {
            btnDelete.setEnabled(bDelete);
        }
        if (btnExcelExport != null)
        {
            btnExcelExport.setEnabled(bExcelExport);
        }
    }

    private void setFocusToTable()
    {
        setButtons();
        mgxTable.requestFocus();
    }

    private void setFocusToDetail()
    {
        //((DataField) vFields.elementAt (iFirstActiveField)).setFocus ();
        vFields.elementAt(iFirstActiveField).setFocus();
    }

    public void setFocus_()
    {
        if (jTabbedPane.getSelectedIndex() == 0)
        {
            setFocusToTable();
        }
        else if (jTabbedPane.getSelectedIndex() == 1)
        {
            setFocusToDetail();
        }
    }

    private CommonPanel createListPanel()
    {
        CommonPanel jPanel = new CommonPanel();
        jPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        jPanel.setPreferredSize(dimPreferred);
        jScrollPane = new JScrollPane(mgxTable);
        jPanel.addToGrid(jScrollPane, 0, 0, (bButtons ? 6 : 1), 1, 1.0, 1.0, GridBagConstraints.BOTH);
        if (bButtons)
        {
            jPanel.addToGrid(btnNew = AppUtils.createButton(appInterface.getLanguageString(DefaultLanguageConstants.STRING_CAPTION_ADD), "New", this, "Ctrl-N"), 1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.BOTH);
            jPanel.addToGrid(btnEdit = AppUtils.createButton((bUpdate ? appInterface.getLanguageString(DefaultLanguageConstants.STRING_CAPTION_MODIFY) : appInterface.getLanguageString(DefaultLanguageConstants.STRING_CAPTION_VIEW)), "Edit", this, "Ctrl-E"), 1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.BOTH);
            jPanel.addToGrid(btnDelete = AppUtils.createButton(appInterface.getLanguageString("Törlés"), "Delete", this, "Ctrl-D"), 1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.BOTH);
            jPanel.addToGrid(AppUtils.createButton(appInterface.getLanguageString("Frissítés"), "Refresh", this, "Ctrl-R"), 1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.BOTH);
            jPanel.addToGrid(btnExcelExport = AppUtils.createButton(appInterface.getLanguageString(DefaultLanguageConstants.STRING_CAPTION_EXCEL), "Excel", this, "Ctrl-X"), 1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.BOTH);
            jPanel.addToGrid(new JLabel(""), 1, 5, GridBagConstraints.REMAINDER, 1, 0.0, 1.0, GridBagConstraints.BOTH);
        }
        return (jPanel);
    }

    private CommonPanel createDetailPanel()
    {
        CommonPanel jPanel = new CommonPanel();
        jPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2), BorderFactory.createEtchedBorder(BevelBorder.LOWERED)));
        jPanel.setMinimumSize(dimPreferred);
        jPanel.setPreferredSize(dimPreferred);

        int iRow = -1;
        int iColumn = 0;
        int iWidth = 400;
        iFirstActiveField = -1;
        JLabel jLabel;
        JScrollPane jScrollPaneDetail;
        boolean bFieldOK = false;

        for (int i = 0; i < td.getFieldCount(); i++)
        {
            try
            {
                jPanel.addToGrid(jLabel = AppUtils.createLabel(" " + td.getFieldDefinition(i).getDisplayName() + ": "), ++iRow, iColumn = 0, 1, 1, 0.0, 0.0, GridBagConstraints.BOTH);
                jLabel.setVisible(td.getFieldDefinition(i).isVisible());
                if (!td.getFieldDefinition(i).isReadOnly() && (iFirstActiveField < 0))
                {
                    iFirstActiveField = i;
                }
                if (maintPanelExtensionInterface != null)
                {
                    //Component component = maintPanelExtensionInterface.createFieldComponent (td, i);
                    Component component = maintPanelExtensionInterface.createFieldComponent(td, i);
                    if (component != null)
                    {
                        jPanel.addToGrid(component, iRow, ++iColumn, (bButtons ? 2 : 1), 1, 0.0, 0.0, GridBagConstraints.NONE, GridBagConstraints.WEST);
                        vFields.add((DataField) component);
                        jPanel.addToGrid(new JLabel(""), iRow, ++iColumn, GridBagConstraints.REMAINDER, 1, 0.0, 0.0, GridBagConstraints.BOTH);
                        bFieldOK = true;
                    }
                }
                if (!bFieldOK)
                {
                    if (td.getFieldDefinition(i).isLookup())
                    {
                        jPanel.addToGrid(clf = new CommonLookupField(td.getFieldDefinition(i), appInterface), iRow, ++iColumn, (bButtons ? 2 : 1), 1, 0.0, 1.0, GridBagConstraints.NONE, GridBagConstraints.WEST);
                        clf.addKeyListener(this);
                        clf.setEnabled(!td.getFieldDefinition(i).isReadOnly() && !td.getFieldDefinition(i).isID() && !td.getFieldDefinition(i).isModifier() && !td.getFieldDefinition(i).isModificationTime());
                        clf.setVisible(td.getFieldDefinition(i).isVisible());
                        clf.setSize(new Dimension(400, 21));
                        vFields.add(clf);
                        jPanel.addToGrid(new JLabel(""), iRow, ++iColumn, GridBagConstraints.REMAINDER, 1, 0.0, 0.0, GridBagConstraints.BOTH);
                    }
                    else if (td.getFieldDefinition(i).getType() == FieldType.BIT)
                    {
                        jPanel.addToGrid(cbf = new CommonBitField(td.getFieldDefinition(i), null), iRow, ++iColumn, (bButtons ? 2 : 1), 1, 0.0, 0.0, GridBagConstraints.NONE, GridBagConstraints.WEST);
                        cbf.addKeyListener(this);
                        cbf.setEnabled(!td.getFieldDefinition(i).isReadOnly() && !td.getFieldDefinition(i).isID() && !td.getFieldDefinition(i).isModifier() && !td.getFieldDefinition(i).isModificationTime());
                        cbf.setVisible(td.getFieldDefinition(i).isVisible());
                        vFields.add(cbf);
                        jPanel.addToGrid(new JLabel(""), iRow, ++iColumn, GridBagConstraints.REMAINDER, 1, 0.0, 1.0, GridBagConstraints.BOTH);
                    }
                    else if (td.getFieldDefinition(i).isText())
                    {
                        cef = new CommonEditorField();
                        jScrollPaneDetail = new JScrollPane(cef);
                        jScrollPaneDetail.getVerticalScrollBar().setFocusable(false);
                        jScrollPaneDetail.getHorizontalScrollBar().setFocusable(false);
                        jPanel.addToGrid(jScrollPaneDetail, iRow, ++iColumn, (bButtons ? 2 : 1), 1, 0.0, 0.0, GridBagConstraints.NONE, GridBagConstraints.WEST);
                        cef.addKeyListener(this);
                        cef.setEnabled(!td.getFieldDefinition(i).isReadOnly() && !td.getFieldDefinition(i).isID() && !td.getFieldDefinition(i).isModifier() && !td.getFieldDefinition(i).isModificationTime());
                        cef.setVisible(td.getFieldDefinition(i).isVisible());
                        cef.setSize(new Dimension(400, 150));
                        jScrollPaneDetail.setMinimumSize(new Dimension(400, 150));
                        jScrollPaneDetail.setPreferredSize(new Dimension(400, 150));
                        jScrollPaneDetail.setMaximumSize(new Dimension(400, 150));
                        vFields.add(cef);
                        jPanel.addToGrid(new JLabel(""), iRow, ++iColumn, GridBagConstraints.REMAINDER, 1, 0.0, 0.0, GridBagConstraints.BOTH);
                    }
                    else
                    {
                        if ((td.getFieldDefinition(i).getType() == FieldType.INT) || (td.getFieldDefinition(i).getType() == FieldType.DATE))
                        {
                            iWidth = 80;
                        }
                        else if (td.getFieldDefinition(i).getType() == FieldType.DATETIME)
                        {
                            iWidth = 130;
                        }
                        else
                        {
                            iWidth = 400;
                        }
                        jPanel.addToGrid(ctf = new CommonTextField(td.getFieldDefinition(i), appInterface), iRow, ++iColumn, (bButtons ? 2 : 1), 1, 0.0, 1.0, GridBagConstraints.NONE, GridBagConstraints.WEST);
                        ctf.addKeyListener(this);
                        ctf.setToolTipText(td.getFieldDefinition(i).getToolTip());
                        ctf.setEnabled(!td.getFieldDefinition(i).isReadOnly() && !td.getFieldDefinition(i).isID() && !td.getFieldDefinition(i).isModifier() && !td.getFieldDefinition(i).isModificationTime());
                        ctf.setVisible(td.getFieldDefinition(i).isVisible());
                        ctf.setSize(new Dimension(iWidth, 21));
                        vFields.add(ctf);
                        jPanel.addToGrid(new JLabel(""), iRow, ++iColumn, GridBagConstraints.REMAINDER, 1, 0.0, 0.0, GridBagConstraints.BOTH);
                    }
                }
            }
            catch (TableDefinitionException tde)
            {
                appInterface.handleError(tde);
            }
        }

        if (bButtons)
        {
            jPanel.addToGrid(new JLabel(""), ++iRow, iColumn = 0, 1, 1, 0.0, 0.0, GridBagConstraints.BOTH);
            jPanel.addToGrid(btnOK = AppUtils.createButton(appInterface.getLanguageString(DefaultLanguageConstants.STRING_CAPTION_OK), "OK", this, "Ctrl-S"), iRow, ++iColumn, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.WEST);
            btnOK.setEnabled((!bExistingRecord && bInsert) || (bExistingRecord && bUpdate));
            jPanel.addToGrid(AppUtils.createButton(appInterface.getLanguageString("Mégsem"), "Escape", this, appInterface.getLanguageString("Mégsem")), iRow, ++iColumn, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.WEST);
            jPanel.addToGrid(new JLabel(""), iRow, ++iColumn, GridBagConstraints.REMAINDER, 1, 0.0, 1.0, GridBagConstraints.BOTH);
        }

        jPanel.addToGrid(new JLabel(""), ++iRow, iColumn = 0, GridBagConstraints.REMAINDER, 1, 1.0, 1.0, GridBagConstraints.BOTH);

        return (jPanel);
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand().equals("New"))
        {
            actionNew();
        }
        if (e.getActionCommand().equals("Edit"))
        {
            actionEdit();
        }
        if (e.getActionCommand().equals("OK"))
        {
            actionOK();
        }
        if (e.getActionCommand().equals("Delete"))
        {
            actionDelete();
        }
        if (e.getActionCommand().equals("Refresh"))
        {
        }
        if (e.getActionCommand().equals("Excel"))
        {
            excelExport();
        }
        if (e.getActionCommand().equals("Escape"))
        {
            actionEscape();
        }
    }

    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
            e.consume();
            actionEscape();
        }
        if (((e.getModifiers() == KeyEvent.CTRL_MASK) && (e.getKeyCode() == KeyEvent.VK_E)) || (e.getKeyCode() == KeyEvent.VK_ENTER))
        {
            if (jTabbedPane.getSelectedIndex() == 0)
            {
                e.consume();
                actionEdit();
            }
        }
        if ((e.getModifiers() == KeyEvent.CTRL_MASK) && (e.getKeyCode() == KeyEvent.VK_S))
        {
            if (jTabbedPane.getSelectedIndex() == 1)
            {
                e.consume();
                save();
            }
        }
        if (((e.getModifiers() == KeyEvent.CTRL_MASK) && (e.getKeyCode() == KeyEvent.VK_D)) || (e.getKeyCode() == KeyEvent.VK_DELETE))
        {
            if (jTabbedPane.getSelectedIndex() == 0)
            {
                e.consume();
                actionDelete();
            }
        }
        if (((e.getModifiers() == KeyEvent.CTRL_MASK) && (e.getKeyCode() == KeyEvent.VK_N)) || (e.getKeyCode() == KeyEvent.VK_INSERT))
        {
            if (jTabbedPane.getSelectedIndex() == 0)
            {
                e.consume();
                actionNew();
            }
        }
        if ((e.getModifiers() == KeyEvent.CTRL_MASK) && (e.getKeyCode() == KeyEvent.VK_X))
        {
            if (jTabbedPane.getSelectedIndex() == 0)
            {
                e.consume();
                excelExport();
            }
        }
    }

    public void keyReleased(KeyEvent e)
    {
    }

    public void keyTyped(KeyEvent e)
    {
    }

    public void cellClicked(int row, int columnt, int clickCount)
    {
        if (clickCount == 2)
        {
            actionEdit();
        }
    }

    public void setSelectedIndex(int index)
    {
        jTabbedPane.setEnabledAt(index, true);
        jTabbedPane.setSelectedIndex(index);
        jTabbedPane.setEnabledAt(1 - index, false);
    }

    public int getSelectedIndex()
    {
        return (jTabbedPane.getSelectedIndex());
    }

    public boolean changed()
    {
        boolean bChanged = false;
        for (int i = 0; i < td.getFieldCount(); i++)
        {
            //if (((DataField) vFields.elementAt (i)).changed ())
            if (vFields.elementAt(i).changed())
            {
                bChanged = true;
            }
        }
        return (bChanged);
    }

    public void goToPage0()
    {
        jTabbedPane.setTitleAt(0, appInterface.getLanguageString(DefaultLanguageConstants.STRING_CAPTION_LIST));
        jTabbedPane.setTitleAt(1, "");
        setSelectedIndex(0);
        setFocusToTable();
    }

    public void goToPage1(String sTitle)
    {
        jTabbedPane.setTitleAt(0, "");
        jTabbedPane.setTitleAt(1, sTitle);
        setSelectedIndex(1);
        setFocusToDetail();
    }

    private void save()
    {
        if (!bExistingRecord && !bInsert)
        {
            return;
        }
        if (bExistingRecord && !bUpdate)
        {
            return;
        }
        for (int i = 0; i < td.getFieldCount(); i++)
        {
            try
            {
                if (!DataUtils.checkField(this, vFields.elementAt(i), td.getFieldDefinition(i), bExistingRecord, appInterface))
                {
                    return;
                }
            }
            catch (TableDefinitionException e)
            {
            }
        }

        boolean bSuccess = false;
        newRecord = new Record(td);
        for (int i = 0; i < td.getFieldCount(); i++)
        {
            try
            {
                if (td.getFieldDefinition(i).isModifier())
                {
                    vFields.elementAt(i).setValue(new Integer(iUserId));
                }
                if (td.getFieldDefinition(i).isModificationTime())
                {
                    vFields.elementAt(i).setValue(new java.util.Date());
                }
            }
            catch (TableDefinitionException e)
            {
            }
            newRecord.setField(i, vFields.elementAt(i).getValue());
        }

        //2010.12.10.
        if (!DataUtils.checkRecord(this, oldRecord, newRecord, td, bExistingRecord, appInterface))
        {
            return;
        }

        if (bExistingRecord)
        {
            bSuccess = sql.update(td, oldRecord, newRecord);
        }
        else
        {
            bSuccess = sql.insert(td, newRecord);
        }
        if (bSuccess)
        {
            if (bExistingRecord)
            {
                int ii = mgxTable.getSelectedRow();
                for (int i = 0; i < td.getFieldCount(); i++)
                {
                    sorter.setValueAt(vFields.elementAt(i).getValue(), ii, i);
                }
            }
            else
            {
                Vector<Object> rowData = new Vector<Object>();
                for (int i = 0; i < td.getFieldCount(); i++)
                {
                    try
                    {
                        if (td.getFieldDefinition(i).isID())
                        {
                            vFields.elementAt(i).setValue(newRecord.getFieldValue(i));
                        }
                    }
                    catch (TableDefinitionException e)
                    {
                    }
                    rowData.add(vFields.elementAt(i).getValue());
                }
                ((MgxTableModel) sorter.getModel()).addRow(rowData);
                if (mgxTable.getRowCount() > 0)
                {
                    mgxTable.setRowSelectionInterval(mgxTable.getRowCount() - 1, mgxTable.getRowCount() - 1);
                }
            }

        }
        goToPage0();
    }

    public void actionNew()
    {
        if (!bInsert)
        {
            return;
        }
        bExistingRecord = false;
        btnOK.setEnabled((!bExistingRecord && bInsert) || (bExistingRecord && bUpdate));
        String s = "";
        for (int i = 0; i < td.getFieldCount(); i++)
        {
            try
            {
                //DataUtils.setDefault ((DataField) vFields.elementAt (i), td.getFieldDefinition (i), sql, iUserId, appInterface);
                DataUtils.setDefault(vFields.elementAt(i), td.getFieldDefinition(i), sql, iUserId, appInterface);
            }
            catch (TableDefinitionException tde)
            {
                System.err.println(tde.getLocalizedMessage());
                tde.printStackTrace(System.err);
            }
        }
        goToPage1(appInterface.getLanguageString(DefaultLanguageConstants.STRING_CAPTION_ADD_NEW_RECORD));
    }

    public void actionEdit()
    {
        if (mgxTable.getSelectedRow() > -1)
        {
            bExistingRecord = true;
            btnOK.setEnabled((!bExistingRecord && bInsert) || (bExistingRecord && bUpdate));
            oldRecord = new Record(td);
            int ii = mgxTable.getSelectedRow();
            for (int i = 0; i < td.getFieldCount(); i++)
            {
                Object o = sorter.getValueAt(ii, i);
                oldRecord.setField(i, o); //null???

                String s = (o == null ? "" : o.toString());
                try
                {
                    if (td.getFieldDefinition(i).getType() == FieldType.DATE)
                    {
                        s = s.replace('-', '/');
                    }
                    else if (td.getFieldDefinition(i).getType() == FieldType.DATETIME)
                    {
                        s = s.replace('-', '/');
                    }
                }
                catch (TableDefinitionException e)
                {
                }
                //((DataField) vFields.elementAt (i)).setValue (o);
                vFields.elementAt(i).setValue(o);
            }
        }
        goToPage1(appInterface.getLanguageString(DefaultLanguageConstants.STRING_CAPTION_MODIFY_RECORD));
    }

    public void actionDelete()
    {
        if (!bDelete)
        {
            return;
        }
        if (mgxTable.getSelectedRow() > -1)
        {
            if (!AppUtils.yesNoQuestion(this, appInterface.getLanguageString(DefaultLanguageConstants.STRING_MESSAGE_DELETE), "<html><p align='center'>" + appInterface.getLanguageString(DefaultLanguageConstants.STRING_MESSAGE_DELETE_REALLY) + "</p></html>", appInterface))
            {
                return;
            }
            oldRecord = new Record(td);
            int ii = mgxTable.getSelectedRow();
            for (int i = 0; i < td.getFieldCount(); i++)
            {
                oldRecord.setField(i, sorter.getValueAt(ii, i));
            }
            boolean bSuccess = sql.delete(td, oldRecord, LoggerInterface.LOG_INFO);
            if (bSuccess)
            {
                ((MgxTableModel) sorter.getModel()).removeRow(sorter.getIndex(ii));
                if (mgxTable.getRowCount() > (ii - 1))
                {
                    mgxTable.setRowSelectionInterval(ii - 1, ii - 1);
                }
                else if (mgxTable.getRowCount() > 0)
                {
                    mgxTable.setRowSelectionInterval(0, 0);
                }
                setButtons();
            }
        }
    }

    public void actionOK()
    {
        if (jTabbedPane.getSelectedIndex() == 0)
        {
            actionEdit();
        }
        else if (jTabbedPane.getSelectedIndex() == 1)
        {
            save();
        }
    }

    public void actionEscape()
    {
        if (jTabbedPane.getSelectedIndex() == 0)
        {
            maintPanelListener.maintPanelClose();
        }
        else if (jTabbedPane.getSelectedIndex() == 1)
        {
            if (changed())
            {
                if ((bExistingRecord && bUpdate) || (!bExistingRecord && bInsert))
                {
                    if (!AppUtils.yesNoQuestion(this, "Figyelem!", "<html><p align='center'>A nem mentett adatok elvesznek!</p><p align='center'>Folytatja mentés nélkül?</p></html>", appInterface))
                    {
                        return;
                    }
                }
            }
            goToPage0();
        }
    }

    public void actionRefresh()
    {
        if (db != null)
        {
            try
            {
                td = db.getTableDefinitionByName(sTableName);
            }
            catch (DataBaseException dbe)
            {
                appInterface.handleError(dbe); //2009.01.28
            }
        }
        td.executeSetFieldRealTimeSQLLookupValues(connection);
        mgxTableModel.load(td);
        if (mgxTable.getRowCount() > 0)
        {
            mgxTable.setRowSelectionInterval(0, 0);
        }
        setButtons();
    }

    public void excelExport()
    {
        if (!bExcelExport)
        {
            return;
        }
        ExcelExportImport excelExportImport = new ExcelExportImport();
        excelExportImport.excelExport(this, appInterface, mgxTableModel, td, appInterface);
    }
}
