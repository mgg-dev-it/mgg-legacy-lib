package hu.mag.swing.table;

import hu.mag.lang.LookupInteger;
import hu.mag.swing.MagColorScheme;
import hu.mag.swing.MagEditorField;
import hu.mag.swing.MagFormattedTextField;
import hu.mag.swing.MagComboBoxField;
import hu.mag.swing.MagLookupTextField;
import hu.mag.swing.MagTextAreaField;
import hu.mag.swing.MagTextField;
import hu.mag.swing.text.DateTimeDocument;
import hu.mgx.app.common.LanguageEvent;
import hu.mgx.app.common.LanguageEventListener;
import hu.mgx.app.common.LoggerInterface;
import hu.mgx.app.swing.AppUtils;
import static hu.mgx.app.swing.SwingApp.DEVELOPER_VERSION;
import hu.mgx.app.swing.SwingAppInterface;
import hu.mgx.util.BigDecimalUtils;
import hu.mgx.util.DateTimeUtils;
import hu.mgx.util.IntegerUtils;
import hu.mgx.util.StringUtils;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
//import javax.swing.event.ListSelectionEvent;
//import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;

public class MagTable extends JTable implements ActionListener, LanguageEventListener, ComponentListener, ChangeListener, MouseListener {

    private SwingAppInterface swingAppInterface = null;
    private MagTableModel magTableModel = null;
    private String sConnectionName = null;
    private boolean bInEditMode = false;
    private MagTableInterface magTableInterface = null;
    private Vector vOrigin = null;
    private JPopupMenu popup;
    private MouseListener popupListener = null;
    private JMenuItem miNewRecord = null;
    private JMenuItem miDeleteRecord = null;
    private JMenuItem miDetail = null;
    private JMenuItem miDeveloperInfo = null;
    private boolean bAddNewRowAtTheEnd = false;
    private boolean bPopupEnabled = true;
    private HashMap<JMenuItem, String> hmPopupMenus = new HashMap<JMenuItem, String>();
    private Vector<MagTableEventListener> vMagTableEventListeners = new Vector<MagTableEventListener>();
    private Frame parentFrame = null;
    private JScrollPane jScrollPane = null;
    private boolean bAutoColumnWidthParamWithHeader = false;
    private int iAutoColumnWidthParamMaxWidth = 0;
    private boolean bVerticalScrollBarIsVisible = false;
    private boolean bSetAutoResizeWhenScrollPaneResizes = true;
    private boolean bNoResize = false;

    public MagTable(SwingAppInterface swingAppInterface, String sConnectionName, MagTableModel magTableModel, MagTableInterface magTableInterface) {
        super(magTableModel);
        this.swingAppInterface = swingAppInterface;
        this.sConnectionName = sConnectionName;
        this.magTableModel = magTableModel;
        this.magTableInterface = magTableInterface;
        this.setAutoResizeMode(AUTO_RESIZE_OFF); //don't change
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //don't change
        this.setSurrendersFocusOnKeystroke(true); //don't change
        this.setAutoCreateRowSorter(true); //?
        this.getTableHeader().setReorderingAllowed(false); //don't change
        init();
    }

//addToSelection
//cancel
//clearSelection
//copy
//cut
//extendTo
//focusHeader
//moveSelectionTo
//paste
//scrollDownChangeSelection
//scrollDownExtendSelection
//scrollLeftChangeSelection
//scrollLeftExtendSelection
//scrollRightChangeSelection
//scrollRightExtendSelection
//scrollUpChangeSelection
//scrollUpExtendSelection
//selectAll
//selectFirstColumn
//selectFirstColumnExtendSelection
//selectFirstRow
//selectFirstRowExtendSelection
//selectLastColumn
//selectLastColumnExtendSelection
//selectLastRow
//selectLastRowExtendSelection
//selectNextColumn
//selectNextColumnCell
//selectNextColumnChangeLead
//selectNextColumnExtendSelection
//selectNextRow
//selectNextRowCell
//selectNextRowChangeLead
//selectNextRowExtendSelection
//selectPreviousColumn
//selectPreviousColumnCell
//selectPreviousColumnChangeLead
//selectPreviousColumnExtendSelection
//selectPreviousRow
//selectPreviousRowCell
//selectPreviousRowChangeLead
//selectPreviousRowExtendSelection
//startEditing
//toggleAndAnchor
    private void init() {
        //InputMap im = super.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        //im.put(KeyStroke.getKeyStroke("ENTER"), "none");

        //BasicTableUI 
//        System.out.println("---");
//        Object[] keys = this.getActionMap().allKeys();
//        Arrays.sort(keys);
//        for (int i = 0; i < keys.length; i++) {
//            System.out.println(keys[i].toString());
//        }
//        System.out.println("---");
//        KeyStroke[] keyStrokes = super.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).allKeys();
//        if (keyStrokes != null) {
//            //Arrays.sort(keyStrokes);
//            for (int i = 0; i < keyStrokes.length; i++) {
//                System.out.println(keyStrokes[i].toString());
//            }
//        }
        this.setDefaultRenderer(String.class, new MagCellRenderer(magTableModel, swingAppInterface));
        for (int i = 0; i < getColumnCount(); i++) {
            this.getColumnModel().getColumn(i).setCellRenderer(new MagCellRenderer(magTableModel, swingAppInterface));
        }

        this.setDefaultEditor(String.class, new MagCellEditor(magTableModel, -1, swingAppInterface));
        for (int i = 0; i < getColumnCount(); i++) {
            this.getColumnModel().getColumn(i).setCellEditor(new MagCellEditor(magTableModel, i, swingAppInterface));
        }

        //selectionModel = this.getSelectionModel();
        //selectionModel.addListSelectionListener(this);
        ActionMap am = super.getActionMap();
        final Action actionSelectNextColumnCell = super.getActionMap().get("selectNextColumnCell"); //BasicTableUI NEXT_COLUMN_CELL (TAB)
        am.put("selectNextColumnCell", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MagTable table = (MagTable) e.getSource();
                int lastRow = table.getRowCount() - 1;
                int lastColumn = table.getColumnCount() - 1;
                //MaG 2015.12.03. if (table.getSelectionModel().getLeadSelectionIndex() == lastRow && table.getColumnModel().getSelectionModel().getLeadSelectionIndex() == lastColumn) {
                if (table.getSelectionModel().getLeadSelectionIndex() == lastRow && possibleStepToRight(table.getSelectionModel().getLeadSelectionIndex(), table.getColumnModel().getSelectionModel().getLeadSelectionIndex()) < 1) { //MaG 2015.12.03. 

//                    if (magTableModel.getRowStatus(table.getSelectionModel().getLeadSelectionIndex()) == MagTableModel.ROW_STATUS_NEW) {
//                        return;
//                    }
                    if (magTableInterface != null) {
                        if (!magTableInterface.tabPressedInLastCell(lastRow, lastColumn)) {
                            return;
                        }
                    }
                    TableCellEditor tce = table.getCellEditor();
                    if (tce != null) {
                        if (!tce.stopCellEditing()) {
                            return;
                        }
                    }
                    if (magTableModel.getRowStatus(table.getSelectionModel().getLeadSelectionIndex()) == MagTableModel.ROW_STATUS_NEW) {
                        magTableModel.setRowStatus(table.getSelectionModel().getLeadSelectionIndex(), (isRowChanged(table.getSelectionModel().getLeadSelectionIndex()) ? MagTableModel.ROW_STATUS_NEW_MODIFIED : MagTableModel.ROW_STATUS_NEW));
                        if (magTableModel.getRowStatus(table.getSelectionModel().getLeadSelectionIndex()) == MagTableModel.ROW_STATUS_NEW) {
                            return;
                        }
                    }
                    if (!checkIfRowContentNotChangedOrSavedOrDeletedBeforeRowChange(lastRow)) {
                        return;
                    }
                    //if (bAddNewRowAtTheEnd) {
                    if (bAddNewRowAtTheEnd && !magTableModel.isReadOnlyTable() && magTableModel.isUpdateableTable()) {
                        table.getMagTableModel().addEmptyRow();
                        fireMagTableEventNewRowAdded(table.getRowCount() - 1);
//                        setVisibleCurrentCell();
                    }
                }
//                actionSelectNextColumnCell.actionPerformed(e);
                int iRow = table.getSelectionModel().getLeadSelectionIndex();
                int iColumn = table.getColumnModel().getSelectionModel().getLeadSelectionIndex();
                int iPossibleStep = possibleStepToRight(iRow, iColumn, true);
                if (iPossibleStep > 0) {
                    for (int i = 0; i < iPossibleStep; i++) {
                        actionSelectNextColumnCell.actionPerformed(e);
                    }
                }
            }
        }
        );
        final Action actionSelectPreviousColumnCell = super.getActionMap().get("selectPreviousColumnCell"); //BasicTableUI PREVIOUS_COLUMN_CELL (Shift TAB)
        am.put("selectPreviousColumnCell", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MagTable table = (MagTable) e.getSource();
                if (table.getSelectionModel().getLeadSelectionIndex() == 0 && table.getColumnModel().getSelectionModel().getLeadSelectionIndex() == 0) {
                    return;
                }
                //actionSelectPreviousColumnCell.actionPerformed(e);
                //MagTable table = (MagTable) e.getSource();
                int iRow = table.getSelectionModel().getLeadSelectionIndex();
                int iColumn = table.getColumnModel().getSelectionModel().getLeadSelectionIndex();
                int iPossibleStep = possibleStepToLeft(iRow, iColumn, true);
                if (iPossibleStep > 0) {
                    for (int i = 0; i < iPossibleStep; i++) {
                        actionSelectPreviousColumnCell.actionPerformed(e);
                    }
                }
            }
        }
        );

        final Action actionSelectNextColumn = super.getActionMap().get("selectNextColumn"); //BasicTableUI NEXT_COLUMN
        am.put("selectNextColumn", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MagTable table = (MagTable) e.getSource();
                int iRow = table.getSelectionModel().getLeadSelectionIndex();
                int iColumn = table.getColumnModel().getSelectionModel().getLeadSelectionIndex();
                int iPossibleStep = possibleStepToRight(iRow, iColumn);
                if (iPossibleStep > 0) {
                    for (int i = 0; i < iPossibleStep; i++) {
                        actionSelectNextColumn.actionPerformed(e);
                    }
                }
            }
        }
        );

        final Action actionSelectPreviousColumn = super.getActionMap().get("selectPreviousColumn"); //BasicTableUI PREVIOUS_COLUMN
        am.put("selectPreviousColumn", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MagTable table = (MagTable) e.getSource();
                int iRow = table.getSelectionModel().getLeadSelectionIndex();
                int iColumn = table.getColumnModel().getSelectionModel().getLeadSelectionIndex();
                int iPossibleStep = possibleStepToLeft(iRow, iColumn);
                if (iPossibleStep > 0) {
                    for (int i = 0; i < iPossibleStep; i++) {
                        actionSelectPreviousColumn.actionPerformed(e);
                    }
                }
            }
        }
        );

        String sXMLConfig = "<?xml version='1.0' encoding='ISO-8859-2'?>";
        sXMLConfig += "<!-- Comment -->";
        sXMLConfig += "<app name='MagTable' major='0' minor='0' revision='0' width='800' height='600'>";
        sXMLConfig += "    <language>";
        sXMLConfig += "        <languageitem key='MagTable Új rekord'>";
        sXMLConfig += "            <translation lang='hu'>Új</translation>";
        sXMLConfig += "            <translation lang='en'>New</translation>";
        sXMLConfig += "            <translation lang='de'>Neu</translation>";
        sXMLConfig += "        </languageitem>";
        sXMLConfig += "        <languageitem key='MagTable Részletek'>";
        sXMLConfig += "            <translation lang='hu'>Részletek</translation>";
        sXMLConfig += "            <translation lang='en'>Details</translation>";
        sXMLConfig += "            <translation lang='de'>Detail</translation>";
        sXMLConfig += "        </languageitem>";
        sXMLConfig += "        <languageitem key='MagTable Rekord törlése'>";
        sXMLConfig += "            <translation lang='hu'>Törlés</translation>";
        sXMLConfig += "            <translation lang='en'>Delete</translation>";
        sXMLConfig += "            <translation lang='de'>Löschen</translation>";
        sXMLConfig += "        </languageitem>";
        sXMLConfig += "        <languageitem key='MagTable Fejlesztõi információ'>";
        sXMLConfig += "            <translation lang='hu'>Fejlesztõi információ</translation>";
        sXMLConfig += "            <translation lang='en'>Developer info</translation>";
        sXMLConfig += "            <translation lang='de'>Entwickler Info</translation>";
        sXMLConfig += "        </languageitem>";
        sXMLConfig += "        <languageitem key='MagTable Filter'>";
        sXMLConfig += "            <translation lang='hu'>Szûrés</translation>";
        sXMLConfig += "            <translation lang='en'>Filter</translation>";
        sXMLConfig += "            <translation lang='de'>Filter</translation>";
        sXMLConfig += "        </languageitem>";
        sXMLConfig += "        <languageitem key='MagTable RowCount'>";
        sXMLConfig += "            <translation lang='hu'>%1 sor</translation>";
        sXMLConfig += "            <translation lang='en'>%1 row</translation>";
        sXMLConfig += "            <translation lang='de'>%1 Zeile</translation>";
        sXMLConfig += "        </languageitem>";
        sXMLConfig += "    </language>";
        sXMLConfig += "</app>";
        swingAppInterface.addLanguageXML(sXMLConfig, "ISO-8859-2");

        //@todo task: not only append but insert new row
        //@todo task: "cancel" menuitem in case of new row (and modified rows?)
        popup = new JPopupMenu();
        //if (magTableModel.isUpdateableTable()) {
        if (magTableModel.isUpdateableTable() && !magTableModel.isReadOnlyTable()) {
            miNewRecord = new JMenuItem(swingAppInterface.getLanguageString("MagTable Új rekord"));
            miNewRecord.setActionCommand("action_new");
            miNewRecord.addActionListener(this);
            popup.add(miNewRecord);
        }

        miDetail = new JMenuItem(swingAppInterface.getLanguageString("MagTable Részletek"));
        miDetail.setActionCommand("action_details");
        miDetail.addActionListener(this);
        popup.add(miDetail);

        //if (magTableModel.isUpdateableTable()) {
        if (magTableModel.isUpdateableTable() && !magTableModel.isReadOnlyTable()) {
            miDeleteRecord = new JMenuItem(swingAppInterface.getLanguageString("MagTable Rekord törlése"));
            miDeleteRecord.setActionCommand("action_delete");
            miDeleteRecord.addActionListener(this);
            popup.add(miDeleteRecord);
        }

        if (swingAppInterface.getDateTimeVersion().equals(DEVELOPER_VERSION)) {
            miDeveloperInfo = new JMenuItem(swingAppInterface.getLanguageString("MagTable Fejlesztõi információ"));
            miDeveloperInfo.setActionCommand("action_developer_info");
            miDeveloperInfo.addActionListener(this);
            popup.add(miDeveloperInfo);
        }

        //this.removeMouseListener(popupListener); //MaG 2017.10.08.
        popupListener = new PopupListener(popup, this, magTableModel);
        this.addMouseListener(popupListener);
        showRowcountStatus();

        this.addMouseListener(this); //MaG 2016.06.03.

        if (getRowCount() > 0) {
            this.getSelectionModel().setLeadSelectionIndex(0);
            setFirstEditableCell();
            fireMagTableEventRowStatusChanged(0);
            //storeOriginRowValues(0); //MaG 2018.05.11.
        }

//        if (getMagTableModel().getTableInfo() != null) {
//            if (getMagTableModel().getTableInfo().isExtendedScrollToLast()) {
//                Rectangle cellRect = magTable.getCellRect(magTable.getSelectionModel().getLeadSelectionIndex(), magTable.getColumnModel().getSelectionModel().getLeadSelectionIndex(), false);
//                if (cellRect != null) {
//                    jvTable.scrollRectToVisible(cellRect);
//                }
//            }
//        }
        setNewHeaderUI();
    }

    private int possibleStepToLeft(int iRow, int iColumn) {
        return (possibleStepToLeft(iRow, iColumn, false));
    }

    private int possibleStepToLeft(int iRow, int iColumn, boolean bCanChangeRow) {
        if ((iRow == 0) && (iColumn == 0)) {
            return (0);
        }
        for (int iR = iRow; iR > (bCanChangeRow ? -1 : iRow - 1); iR--) {
            for (int iC = (iR == iRow ? iColumn - 1 : this.getColumnCount() - 1); iC > -1; iC--) {
                //if (!getMagTableModel().isHiddenColumn(iC) && !getMagTableModel().isIdentityColumn(iC) && getMagTableModel().isCellEditable(iRow, iC) && !getMagTableModel().isReadOnlyColumn(iC) && !getMagTableModel().isVirtualColumn(iC)) {
                if (!getMagTableModel().isHiddenColumn(iC) && !getMagTableModel().isIdentityColumn(iC) && getMagTableModel().isCellEditable(convertRowIndexToModel(iR), convertColumnIndexToModel(iC)) && !getMagTableModel().isVirtualColumn(iC)) {
                    return ((iRow - iR) * getColumnCount() + (iColumn - iC));
                }
            }
        }
        return (0);
    }

    private int possibleStepToRight(int iRow, int iColumn) {
        return (possibleStepToRight(iRow, iColumn, false));
    }

    private int possibleStepToRight(int iRow, int iColumn, boolean bCanChangeRow) {
        if ((iRow == this.getRowCount() - 1) && (iColumn == this.getColumnCount() - 1)) {
            return (0);
        }
        for (int iR = iRow; iR < (bCanChangeRow ? this.getRowCount() : iRow + 1); iR++) {
            for (int iC = (iR == iRow ? iColumn + 1 : 0); iC < this.getColumnCount(); iC++) {
                //if (!getMagTableModel().isHiddenColumn(iC) && !getMagTableModel().isIdentityColumn(iC) && getMagTableModel().isCellEditable(iRow, iC) && !getMagTableModel().isReadOnlyColumn(iC) && !getMagTableModel().isVirtualColumn(iC)) {
                if (!getMagTableModel().isHiddenColumn(iC) && !getMagTableModel().isIdentityColumn(iC) && getMagTableModel().isCellEditable(convertRowIndexToModel(iR), convertColumnIndexToModel(iC)) && !getMagTableModel().isVirtualColumn(iC)) {
                    return ((iR - iRow) * getColumnCount() + (iC - iColumn));
                }
            }
        }
        return (0);
    }

    public void setFirstEditableCell() {
        if (magTableModel.getRowStatus(this.getSelectionModel().getLeadSelectionIndex()) != MagTableModel.ROW_STATUS_NEW) {
            return;
        }
        int iRow = this.getSelectionModel().getLeadSelectionIndex();
        int iColumn = -1; //this.getColumnModel().getSelectionModel().getLeadSelectionIndex();
        int iPossibleStep = possibleStepToRight(iRow, iColumn);
        if (iPossibleStep > 0) {
            this.getColumnModel().getSelectionModel().setLeadSelectionIndex(iPossibleStep - 1);
            this.setColumnSelectionInterval(iPossibleStep - 1, iPossibleStep - 1);
        }
    }

    public void addPopupMenuItem(String sCaption, String sActionCommand, ActionListener actionListener) {
        addPopupMenuItem(sCaption, sActionCommand, actionListener, false);
    }

    public void addPopupMenuItem(String sCaption, String sActionCommand, ActionListener actionListener, boolean bForRecordsOnly) {
        JMenuItem menuItem = new JMenuItem(sCaption);
        menuItem.setActionCommand(sActionCommand);
        menuItem.addActionListener(actionListener);
        popup.add(menuItem);
        if (bForRecordsOnly) {
            hmPopupMenus.put(menuItem, sCaption);
        }
    }

    public void addPopupListener(Component comp) {
        comp.addMouseListener(popupListener);
    }

    public void removePopupListener(Component comp) {
        comp.removeMouseListener(popupListener);
    }

    @Override
    public void languageEventPerformed(LanguageEvent e) {
        showRowcountStatus();
    }

    @Override
    public void componentResized(ComponentEvent e) {
        if (bNoResize) {
            return;
        }
        if (bSetAutoResizeWhenScrollPaneResizes) {
            if (e.getComponent().equals(jScrollPane)) {
                setAutoColumnWidth(bAutoColumnWidthParamWithHeader, iAutoColumnWidthParamMaxWidth);
            }
        }
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    @Override
    public void stateChanged(ChangeEvent e) {
//        if (e.getSource().equals(jScrollPane.getViewport())) {
//            //if (bVerticalScrollBarIsVisible != jScrollPane.getVerticalScrollBar().isVisible()) {
//            if (Boolean.valueOf(bVerticalScrollBarIsVisible).compareTo(Boolean.valueOf(jScrollPane.getVerticalScrollBar().isVisible())) != 0) {
//                swingAppInterface.logLine(Boolean.toString(jScrollPane.getVerticalScrollBar().isVisible()) + " event");
//            }
//            bVerticalScrollBarIsVisible = jScrollPane.getVerticalScrollBar().isVisible();
//        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!isEnabled()) {
            return;
        }
        int iColumn = this.columnAtPoint(new Point(e.getX(), e.getY()));
        int iRow = this.rowAtPoint(new Point(e.getX(), e.getY()));
        if (e.getClickCount() == 1) {
            //MaG 2016.10.12. bõvítve jobb egérgombbal és pontos oszlop információval fireMagTableEventClick(this.getSelectedRow());
            if (SwingUtilities.isRightMouseButton(e)) {
                this.setRowSelectionInterval(iRow, iRow);
                fireMagTableEventRightClick(iRow, iColumn);
            } else {
                fireMagTableEventClick(iRow, iColumn);
            }
        }
        if (e.getClickCount() == 2) {
            //fireMagTableEventDblClick(this.getSelectedRow());
            fireMagTableEventDblClick(this.getSelectedRow(), iColumn);
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private class PopupListener extends MouseAdapter {

        private JPopupMenu popup;
        private MagTable magTable;
        private MagTableModel magTableModel;

        public PopupListener(JPopupMenu popupMenu, MagTable magTable, MagTableModel magTableModel) {
            this.popup = popupMenu;
            this.magTable = magTable;
            this.magTableModel = magTableModel;
//            if (magTableModel.getFilters() != null) {
//                System.out.println(magTableModel.getTableName());
//                System.out.println(magTableModel.toString());
//                System.out.println(magTable.toString());
//                System.out.println(this.toString());
//                for (int i = 0; i < magTableModel.getFilters().length; i++) {
//                    System.out.println(magTableModel.getFilters()[i]);
//                }
//            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            showPopup(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            showPopup(e);
        }

        private void showPopup(MouseEvent e) {
            int iEnabledCount = 0;
            if (e.isPopupTrigger()) {
                if (!bPopupEnabled) {
                    return;
                }
//                if (magTableModel.getFilters() != null) {
//                    System.out.println(magTableModel.getTableName());
//                    System.out.println(magTableModel.toString());
//                    System.out.println(magTable.toString());
//                    System.out.println(this.toString());
//                    for (int i = 0; i < magTableModel.getFilters().length; i++) {
//                        System.out.println("20 " + magTableModel.getFilters()[i]);
//                    }
//                }
                if (miNewRecord != null) {
                    //miNewRecord.setEnabled(!magTableModel.isNewRecord());
                    miNewRecord.setEnabled(!magTableModel.isNewRecord() && !magTableModel.isReadOnlyTable());
                    if (miNewRecord.isEnabled()) {
                        ++iEnabledCount;
                    }
                }
                int iColumn = magTable.columnAtPoint(new Point(e.getX(), e.getY()));
                int iRow = magTable.rowAtPoint(new Point(e.getX(), e.getY()));
                if (miDetail != null) {
                    miDetail.setEnabled(iColumn > -1 && iRow > -1 && (!magTableModel.isNewRecord() || magTableModel.getRowStatus(iRow) == MagTableModel.ROW_STATUS_NEW));
                    if (miDetail.isEnabled()) {
                        ++iEnabledCount;
                    }
                }
                if (miDeleteRecord != null) {
                    //miDeleteRecord.setEnabled(iColumn > -1 && iRow > -1 && (!magTableModel.isNewRecord() || magTableModel.getRowStatus(iRow) == MagTableModel.ROW_STATUS_NEW));
                    miDeleteRecord.setEnabled(iColumn > -1 && iRow > -1 && !magTableModel.isReadOnlyTable() && (!magTableModel.isNewRecord() || magTableModel.getRowStatus(iRow) == MagTableModel.ROW_STATUS_NEW));
                    if (miDeleteRecord.isEnabled()) {
                        ++iEnabledCount;
                    }
                }
                JMenuItem jmi;
                Iterator<JMenuItem> iterator = hmPopupMenus.keySet().iterator();
                while (iterator.hasNext()) {
                    jmi = iterator.next();
                    jmi.setEnabled(iColumn > -1 && iRow > -1 && (!magTableModel.isNewRecord() || magTableModel.getRowStatus(iRow) == MagTableModel.ROW_STATUS_NEW));
                    if (jmi.isEnabled()) {
                        ++iEnabledCount;
                    }
                }
                if (miDeveloperInfo != null) {
                    ++iEnabledCount;
                }
                if (!magTableModel.isNewRecord() && iColumn > -1 && iRow > -1) {
                    magTable.changeSelection(iRow, iColumn, false, false);
                    //magTable.setRowSelectionInterval(iRow, iRow);
                }

                if (iEnabledCount > 0) {
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        }
    }

    public MagTableModel getMagTableModel() {
        return (magTableModel);
    }

    @Override
    public int getColumnCount() {
        return (magTableModel.getColumnCount());
    }

    public String[] getColumnNames() {
        String[] saColumnNames = new String[this.getColumnCount()];
        for (int iCol = 0; iCol < this.getColumnCount(); iCol++) {
            saColumnNames[iCol] = this.getColumnName(iCol);
        }
        return (saColumnNames);
    }

    public Vector getOriginValues() {
        return (vOrigin);
    }

    public Vector getRow(int iRow) {
        return ((Vector) this.getMagTableModel().getDataVector().elementAt(iRow));
    }

    @Override
    public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
        if (!isEnabled()) {
            return;
        }
        if (!changeSelectionIfPossible(rowIndex, columnIndex, toggle, extend)) {
            return;
        }
        if (bInEditMode) {
            return;
        }
        super.changeSelection(rowIndex, columnIndex, toggle, extend);
        fireMagTableEventRowColChanged(rowIndex, columnIndex);
    }

    private void fireMagTableEventRowColChanged(int rowIndex, int columnIndex) {
        for (int i = 0; i < vMagTableEventListeners.size(); i++) {
            MagTableEvent mte = new MagTableEvent(this, MagTableEvent.ROW_COL_CHANGED, rowIndex, columnIndex);
            vMagTableEventListeners.elementAt(i).tableEventPerformed(mte);
        }
    }

    private void fireMagTableEventRowStatusChanged(int rowIndex) {
        for (int i = 0; i < vMagTableEventListeners.size(); i++) {
            MagTableEvent mte = new MagTableEvent(this, MagTableEvent.ROW_STATUS_CHANGED, rowIndex, -1);
            vMagTableEventListeners.elementAt(i).tableEventPerformed(mte);
        }
    }

    private void fireMagTableEventNewRowAdded(int rowIndex) {
        for (int i = 0; i < vMagTableEventListeners.size(); i++) {
            MagTableEvent mte = new MagTableEvent(this, MagTableEvent.NEW_ROW_ADDED, rowIndex, -1);
            vMagTableEventListeners.elementAt(i).tableEventPerformed(mte);
        }
    }

    private void fireMagTableEventEditBegin(int rowIndex, int columnIndex) {
        for (int i = 0; i < vMagTableEventListeners.size(); i++) {
            MagTableEvent mte = new MagTableEvent(this, MagTableEvent.EDIT_BEGIN, rowIndex, columnIndex);
            vMagTableEventListeners.elementAt(i).tableEventPerformed(mte);
        }
    }

    private void fireMagTableEventEditEnd(int rowIndex, int columnIndex) {
        if (getMagTableModel().getTableInfo() != null) {
            if (getMagTableModel().getTableInfo().getTableInfoPlus() != null) {
                getMagTableModel().getTableInfo().getTableInfoPlus().editEnd(convertRowIndexToModel(rowIndex), convertColumnIndexToModel(columnIndex));
            }
        }
        for (int i = 0; i < vMagTableEventListeners.size(); i++) {
            MagTableEvent mte = new MagTableEvent(this, MagTableEvent.EDIT_END, rowIndex, columnIndex);
            vMagTableEventListeners.elementAt(i).tableEventPerformed(mte);
        }
    }

    private void fireMagTableEventEditCancel(int rowIndex, int columnIndex) {
        for (int i = 0; i < vMagTableEventListeners.size(); i++) {
            MagTableEvent mte = new MagTableEvent(this, MagTableEvent.EDIT_CANCEL, rowIndex, columnIndex);
            vMagTableEventListeners.elementAt(i).tableEventPerformed(mte);
        }
    }

    private void fireMagTableEventClick(int row, int col) {
        for (int i = 0; i < vMagTableEventListeners.size(); i++) {
            MagTableEvent mte = new MagTableEvent(this, MagTableEvent.CLICK, row, col);
            vMagTableEventListeners.elementAt(i).tableEventPerformed(mte);
        }
    }

    //private void fireMagTableEventDblClick(int rowIndex) {
    private void fireMagTableEventDblClick(int row, int col) {
        for (int i = 0; i < vMagTableEventListeners.size(); i++) {
            MagTableEvent mte = new MagTableEvent(this, MagTableEvent.DBL_CLICK, row, col);
            vMagTableEventListeners.elementAt(i).tableEventPerformed(mte);
        }
    }

    private void fireMagTableEventRightClick(int row, int col) {
        for (int i = 0; i < vMagTableEventListeners.size(); i++) {
            MagTableEvent mte = new MagTableEvent(this, MagTableEvent.RIGHT_CLICK, row, col);
            vMagTableEventListeners.elementAt(i).tableEventPerformed(mte);
        }
    }

    private void fireMagTableEventRowSaved(int row) {
        for (int i = 0; i < vMagTableEventListeners.size(); i++) {
            MagTableEvent mte = new MagTableEvent(this, MagTableEvent.ROW_SAVED, row, -1);
            vMagTableEventListeners.elementAt(i).tableEventPerformed(mte);
        }
    }

    private void fireMagTableEventRowDeleted(int row) {
        for (int i = 0; i < vMagTableEventListeners.size(); i++) {
            MagTableEvent mte = new MagTableEvent(this, MagTableEvent.ROW_DELETED, row, -1);
            vMagTableEventListeners.elementAt(i).tableEventPerformed(mte);
        }
    }

    private int fireMagTableEventBeforeInsert(int row) {
        int iResponse = 0;
        for (int i = 0; i < vMagTableEventListeners.size(); i++) {
            MagTableEvent mte = new MagTableEvent(this, MagTableEvent.BEFORE_INSERT, row, -1);
            vMagTableEventListeners.elementAt(i).tableEventPerformed(mte);
            if (mte.getResponse() != 0) {
                iResponse = mte.getResponse();
            }
        }
        return (iResponse);
    }

    private int fireMagTableEventBeforeUpdate(int row) {
        int iResponse = 0;
        for (int i = 0; i < vMagTableEventListeners.size(); i++) {
            MagTableEvent mte = new MagTableEvent(this, MagTableEvent.BEFORE_UPDATE, row, -1);
            vMagTableEventListeners.elementAt(i).tableEventPerformed(mte);
            if (mte.getResponse() != 0) {
                iResponse = mte.getResponse();
            }
        }
        return (iResponse);
    }

    @Override
    public boolean editCellAt(int row, int column) {
        if (!isEnabled()) {
            return (false);
        }
        boolean bSuperEditCellAt = super.editCellAt(row, column);
        bInEditMode = bSuperEditCellAt;
        if (bSuperEditCellAt) {
            fireMagTableEventEditBegin(row, column);
        }
        return (bSuperEditCellAt);
    }

    @Override
    public boolean editCellAt(int row, int column, EventObject e) {
        if (!isEnabled()) {
            return (false);
        }

        //MaG 2018.09.05.
        if (e instanceof KeyEvent) {
            if (((KeyEvent) e).getKeyCode() == KeyEvent.VK_WINDOWS) {
                return (false);
            }
        }

        int iModelRow = this.convertRowIndexToModel(row);
        int iModelColumn = this.convertColumnIndexToModel(column);
        String sChar = "";

        if (magTableModel.getColumnClass(iModelColumn).equals(Boolean.class)) {
            Object value = magTableModel.getValueAt(iModelRow, iModelColumn);
            //appInterface.logLine(magTableModel.getValueAt(iModelRow, iModelColumn).toString());
            if (e instanceof KeyEvent) {
                if (((KeyEvent) e).getKeyCode() == 32) {
                    magTableModel.setValueAt((value == null || !((Boolean) value).booleanValue()), iModelRow, iModelColumn);
                }
            }
            if (e instanceof MouseEvent) {
                if (((MouseEvent) e).getClickCount() == 1) {
                    if (getSelectedRow() != row) {
                        changeSelection(row, column, false, false);
                    }

                    magTableModel.setValueAt((value == null || !((Boolean) value).booleanValue()), iModelRow, iModelColumn);
                    fireMagTableEventEditEnd(row, column); //MaG 2017.05.23.
                    int iRowStatus = magTableModel.getRowStatus(iModelRow);
                    if ((iRowStatus == MagTableModel.ROW_STATUS_NEW) || (iRowStatus == MagTableModel.ROW_STATUS_NEW_MODIFIED)) { //MAG 2015.09.05.
                        magTableModel.setRowStatus(iModelRow, (isRowChanged(row) ? MagTableModel.ROW_STATUS_NEW_MODIFIED : MagTableModel.ROW_STATUS_NEW));
                    } else {
                        magTableModel.setRowStatus(iModelRow, (isRowChanged(row) ? MagTableModel.ROW_STATUS_MODIFIED : MagTableModel.ROW_STATUS_OK));
                    }
                    if (iRowStatus != magTableModel.getRowStatus(iModelRow)) {
                        fireMagTableEventRowStatusChanged(row);
                    }
                    this.repaint();
                }
            }
            return (false);
        }
        if (magTableModel.isTextColumn(iModelColumn)) {
            boolean bStartEdit = false;
            if (e instanceof ActionEvent) { //for F2 ... coming from input/actionmap of JTable ... JComponent
                bStartEdit = true;
            }
            if (e instanceof KeyEvent) {
                bStartEdit = true;
                int iKeyCode = ((KeyEvent) e).getKeyCode();
                int iID = ((KeyEvent) e).getID();
                int iModifiers = ((KeyEvent) e).getModifiers();
                int iModifiersEx = ((KeyEvent) e).getModifiersEx();
                sChar = new StringBuffer().append(((KeyEvent) e).getKeyChar()).toString();
                //swingAppInterface.logLine("KeyEvent" + " ID=" + Integer.toString(iID) + " Modifiers=" + Integer.toString(iModifiers) + " ModifiersEx=" + Integer.toString(iModifiersEx) + " KeyCode=" + Integer.toString(iKeyCode));
                if ((iModifiers & InputEvent.ALT_MASK) == InputEvent.ALT_MASK) {
                    bStartEdit = false;
                }
                if ((iModifiers & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK) {
                    bStartEdit = false;
                }
                if (iKeyCode == KeyEvent.VK_ESCAPE) {
                    bStartEdit = false;
                }
                if (iKeyCode >= KeyEvent.VK_F1 && iKeyCode <= KeyEvent.VK_F12) {
                    bStartEdit = false;
                }
            }
            if (e instanceof MouseEvent) {
                if (((MouseEvent) e).getClickCount() == 2) {
                    if (getSelectedRow() != row) {
                        changeSelection(row, column, false, false);
                    }
                    bStartEdit = true;
                }
            }
            if (bStartEdit) {
                MagTextAreaFieldDialog magEditorFieldDialog = new MagTextAreaFieldDialog(parentFrame, swingAppInterface);
                int iRetVal = magEditorFieldDialog.showDialog(parentFrame, magTableModel.getColumnName(iModelColumn), StringUtils.isNull(magTableModel.getValueAt(iModelRow, iModelColumn), "") + sChar);
                if (iRetVal == MagEditorFieldDialog.OK) {
                    Object value = magEditorFieldDialog.getText();
                    magTableModel.setValueAt(value, iModelRow, iModelColumn);
                    int iRowStatus = magTableModel.getRowStatus(iModelRow);
                    if ((iRowStatus == MagTableModel.ROW_STATUS_NEW) || (iRowStatus == MagTableModel.ROW_STATUS_NEW_MODIFIED)) { //MAG 2015.09.06.
                        magTableModel.setRowStatus(iModelRow, (isRowChanged(row) ? MagTableModel.ROW_STATUS_NEW_MODIFIED : MagTableModel.ROW_STATUS_NEW));
                    } else {
                        magTableModel.setRowStatus(iModelRow, (isRowChanged(row) ? MagTableModel.ROW_STATUS_MODIFIED : MagTableModel.ROW_STATUS_OK));
                    }
                    if (iRowStatus != magTableModel.getRowStatus(iModelRow)) {
                        fireMagTableEventRowStatusChanged(row);
                    }
                    this.repaint();
                    return (false);
                } else {
                    return (false);
                }
            } else {
                return (false);
            }
        }

        //MaG 2018.09.05.
        if (e instanceof KeyEvent) {
            if (((KeyEvent) e).getKeyCode() == KeyEvent.VK_DELETE) {
                magTableModel.setValueAt(null, iModelRow, iModelColumn);
                fireMagTableEventEditEnd(iModelRow, iModelColumn); //MaG 2018.09.12.
                return (false);
            }
        }

        boolean b = super.editCellAt(row, column, e);
        bInEditMode = b;
        if (b) {
            fireMagTableEventEditBegin(row, column);
        }
        return (b);
    }

    @Override
    public void editingStopped(ChangeEvent e) {
        bInEditMode = false;
        super.editingStopped(e);
        fireMagTableEventEditEnd(this.getSelectedRow(), this.getSelectedColumn());
    }

    @Override
    public void editingCanceled(ChangeEvent e) {
        bInEditMode = false;
        super.editingCanceled(e);
        fireMagTableEventEditCancel(this.getSelectedRow(), this.getSelectedColumn());
    }

    @Override
    protected boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed) {
        if (!isEnabled()) {
            return (false);
        }
        char c = e.getKeyChar();
//        int i = e.getKeyCode();
        String s;

        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_ESCAPE) {
            if (!super.isEditing()) {
                int iRow = this.getSelectionModel().getLeadSelectionIndex();
                //if (magTableModel.getRowStatus(iRow) == MagTableModel.ROW_STATUS_NEW) {
                if (magTableModel.getRowStatus(convertRowIndexToModel(iRow)) == MagTableModel.ROW_STATUS_NEW) {
                    //if ((magTableModel.getRowStatus(convertRowIndexToModel(iRow)) == MagTableModel.ROW_STATUS_NEW) || (magTableModel.getRowStatus(convertRowIndexToModel(iRow)) == MagTableModel.ROW_STATUS_NEW_MODIFIED)) {
                    this.getMagTableModel().removeRow(convertRowIndexToModel(iRow));
                    int iNextRow = iRow - 1;
                    if (iNextRow < 0) {
                        iNextRow = 0;
                    }
                    if (this.getRowCount() > 0) {
                        //this.getSelectionModel().setLeadSelectionIndex(iNextRow);
                        this.setRowSelectionInterval(iNextRow, iNextRow);
                        setFirstEditableCell();
                        storeOriginRowValues(iNextRow);
                        fireMagTableEventRowColChanged(iNextRow, -1);
                    }
                }
                return false;
            } else {
                editingCanceled(new ChangeEvent(this));
                return false;
            }
        }

        if ((ks.getModifiers() & InputEvent.ALT_MASK) == InputEvent.ALT_MASK) {
            return false;
        }

        if (super.getEditorComponent() != null) {
            if (super.getEditorComponent() instanceof MagLookupTextField) {
                if (keyCode == KeyEvent.VK_DOWN) {
                    return (false);
                }
                if (keyCode == KeyEvent.VK_UP) {
                    return (false);
                }
            }
        }

//        if (code == KeyEvent.VK_F2) {
//            if (!super.isEditing()) {
//                return false;
//            }
//        }
//        if (pressed) {
//            c = e.getKeyChar();
//            i = ks.getKeyCode();
//            System.out.println(ks.getKeyCode());
//            System.out.println(c);
//            System.out.println(e.getKeyCode());
//            System.out.println(c1);
//        }
//        processKeyBinding(ks, e, WHEN_FOCUSED, pressed);
        boolean retValue = super.processKeyBinding(ks, e, condition, pressed);
        Component editor = super.getEditorComponent();
//        ks.getModifiers()
//                e.getModifiers()
//        if (editor != null) {
//        e.getKeyCode()
//        KeyEvent.VK_F2
//                new KeyEvent()

        //if (editor != null && ks.getModifiers()==0 && e.getModifiers()==0 && e.getKeyCode()!=KeyEvent.VK_F2) {
        //if (editor != null && (ks.getModifiers() == 0 || ks.getModifiers() == KeyEvent.SHIFT_MASK) && e.getKeyCode() != KeyEvent.VK_F2 && e.getKeyCode() != KeyEvent.VK_ESCAPE) {
        if (editor != null && (ks.getModifiers() == 0 || ks.getModifiers() == KeyEvent.SHIFT_MASK)) {
            if (editor instanceof MagTextField) {
                if (retValue) {
//                    System.out.println(c);
//                    System.out.println(i);
//                    if (c > 31) {
//                        ((MagTextField) editor).setText(new StringBuffer(c).toString());
//                    }
                    //s = StringUtils.right(((MagTextField) editor).getText(), 1);
                    if (keyCode == KeyEvent.VK_F2) {
                        s = "";
                    } else {
                        s = new StringBuffer().append(c).toString();
                    }
                    //((MagTextField) editor).setText("");
                    ((MagTextField) editor).setStringAfterFocusGained(s);
//                    ((MagTextField) editor).setText("");
//                    System.out.println(s);
//                    ((MagTextField) editor).setText(s);
//                    ((MagTextField) editor).select(((MagTextField) editor).getText().length(), ((MagTextField) editor).getText().length());
//                    ((MagTextField) editor).select(0, 0);
//                    ((MagTextField) editor).requestFocus();
//                    ((MagTextField) editor).setText(s);
//                    System.out.println("nos?");
                }
            }
//            if (editor instanceof MagComboBoxField) {
////                if (retValue) {
//                    if (keyCode == KeyEvent.VK_F2) {
//                        s = "";
//                    } else {
//                        s = new StringBuffer().append(c).toString();
//                    }
//                    ((MagComboBoxField) editor).setStringAfterFocusGained(s);
////                }
//            }
            if (editor instanceof MagFormattedTextField) {
                if (retValue) {
                    s = StringUtils.right(((MagFormattedTextField) editor).getText(), 1);
                    ((MagFormattedTextField) editor).setStringAfterFocusGained(s);
                }

            }
            if (editor instanceof MagEditorField) {
                if (retValue) {
                    s = StringUtils.right(((MagEditorField) editor).getText(), 1);
                    ((MagEditorField) editor).setStringAfterFocusGained(s);
                }

            }
            if (editor instanceof MagTextAreaField) {
                if (retValue) {
                    s = StringUtils.right(((MagTextAreaField) editor).getText(), 1);
                    ((MagTextAreaField) editor).setStringAfterFocusGained(s);
                }
            }
            if (editor instanceof MagLookupTextField) {
                if (retValue) {
                    s = StringUtils.right(((MagLookupTextField) editor).getText(), 1);
                    ((MagLookupTextField) editor).setStringAfterFocusGained(s);
                }
            }
        }
//        // Start editing when a key is typed. UI classes can disable this behavior
//        // by setting the client property JTable.autoStartsEdit to Boolean.FALSE.
//        if (!retValue && condition == WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
//                && isFocusOwner()
//                && !Boolean.FALSE.equals(getClientProperty("JTable.autoStartsEdit"))) {
//            // We do not have a binding for the event.
//            Component editorComponent = getEditorComponent();
//            if (editorComponent == null) {
//                // Only attempt to install the editor on a KEY_PRESSED,
//                if (e == null || e.getID() != KeyEvent.KEY_PRESSED) {
//                    return false;
//                }
//                // Don't start when just a modifier is pressed
//                int code = e.getKeyCode();
//                if (code == KeyEvent.VK_SHIFT || code == KeyEvent.VK_CONTROL
//                        || code == KeyEvent.VK_ALT) {
//                    return false;
//                }
//                // Try to install the editor
//                int leadRow = getSelectionModel().getLeadSelectionIndex();
//                int leadColumn = getColumnModel().getSelectionModel().
//                        getLeadSelectionIndex();
//                if (leadRow != -1 && leadColumn != -1 && !isEditing()) {
//                    if (!editCellAt(leadRow, leadColumn, e)) {
//                        return false;
//                    }
//                }
//                editorComponent = getEditorComponent();
//                if (editorComponent == null) {
//                    return false;
//                }
//            }
//            // If the editorComponent is a JComponent, pass the event to it.
//            if (editorComponent instanceof JComponent) {
//                retValue = ((JComponent) editorComponent).processKeyBinding(ks, e, WHEN_FOCUSED, pressed);
//                // If we have started an editor as a result of the user
//                // pressing a key and the surrendersFocusOnKeystroke property
//                // is true, give the focus to the new editor.
//                if (getSurrendersFocusOnKeystroke()) {
//                    editorComponent.requestFocus();
//                }
//            }
//        }
        return retValue;
    }

    @Override
    public int convertRowIndexToModel(int viewRowIndex) {
        if (viewRowIndex < 0) {
            return (viewRowIndex);
        }
        return (super.convertRowIndexToModel(viewRowIndex));
    }

    @Override
    public int convertRowIndexToView(int modelRowIndex) {
        if (modelRowIndex < 0) {
            return (modelRowIndex);
        }
        return (super.convertRowIndexToView(modelRowIndex));
    }

    @Override
    public int convertColumnIndexToModel(int viewColumnIndex) {
        if (viewColumnIndex < 0) {
            return (viewColumnIndex);
        }
        return (super.convertColumnIndexToModel(viewColumnIndex));
    }

    @Override
    public int convertColumnIndexToView(int modelColumnIndex) {
        if (modelColumnIndex < 0) {
            return (modelColumnIndex);
        }
        return (super.convertColumnIndexToView(modelColumnIndex));
    }

    private boolean changeSelectionIfPossible(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
        if (!isEnabled()) {
            return (false);
        }
        swingAppInterface.logLine("changeRowCol row=" + Integer.toString(rowIndex) + " col=" + Integer.toString(columnIndex) + " selected row=" + Integer.toString(getSelectedRow()) + " selected col=" + Integer.toString(getSelectedColumn()), LoggerInterface.LOG_DEBUG);
        swingAppInterface.logLine("model RowCol row=" + Integer.toString(convertRowIndexToModel(rowIndex)) + " col=" + Integer.toString(convertColumnIndexToModel(columnIndex)) + " selected row=" + Integer.toString(convertRowIndexToModel(getSelectedRow())) + " selected col=" + Integer.toString(convertColumnIndexToModel(getSelectedColumn())), LoggerInterface.LOG_DEBUG);

        int iTableRow = getSelectedRow();
        int iModelRow = convertRowIndexToModel(getSelectedRow());

        //coloring the row background
        int iRowStatus = magTableModel.getRowStatus(iModelRow);
        if ((iRowStatus == MagTableModel.ROW_STATUS_NEW) || (iRowStatus == MagTableModel.ROW_STATUS_NEW_MODIFIED)) {
            magTableModel.setRowStatus(iModelRow, (isRowChanged(iTableRow) ? MagTableModel.ROW_STATUS_NEW_MODIFIED : MagTableModel.ROW_STATUS_NEW));
        } else {
            magTableModel.setRowStatus(iModelRow, (isRowChanged(iTableRow) ? MagTableModel.ROW_STATUS_MODIFIED : MagTableModel.ROW_STATUS_OK));
        }
        this.repaint();
        if (iRowStatus != magTableModel.getRowStatus(iModelRow)) {
            fireMagTableEventRowStatusChanged(iTableRow);
        }
        //change the row if needed ... and if possible
        if (iTableRow != rowIndex) {
            if (magTableInterface != null) {
                if (!magTableInterface.beforeRowChange(iTableRow, rowIndex)) {
                    return (false);
                }
            }
            if (!checkIfRowContentNotChangedOrSavedOrDeletedBeforeRowChange(iTableRow)) {
                return (false);
            }
            if (rowIndex >= 0 && rowIndex < this.getRowCount()) {
                storeOriginRowValues(rowIndex);
            }
            iRowStatus = magTableModel.getRowStatus(iModelRow);
            magTableModel.setRowStatus(iModelRow, MagTableModel.ROW_STATUS_OK);
            if (iRowStatus != magTableModel.getRowStatus(iModelRow)) {
                fireMagTableEventRowStatusChanged(iTableRow);
            }
            if (magTableInterface != null) {
                magTableInterface.rowChanged(iTableRow, rowIndex);
            }
        }
        return (true);
    }

    private boolean columnValueChanged(int iRow, int iColumn) {
        if (magTableModel.isVirtualColumn(iColumn)) {
            return (false);
        }
        if (vOrigin.elementAt(iColumn) == null && this.getValueAt(iRow, iColumn) != null) {
            return (true);
        }
        if (vOrigin.elementAt(iColumn) != null && this.getValueAt(iRow, iColumn) == null) {
            return (true);
        }
        if (vOrigin.elementAt(iColumn) != null && this.getValueAt(iRow, iColumn) != null) {
//            System.out.println(vOrigin.elementAt(iColumn).toString());
//            System.out.println(this.getValueAt(iRow, iColumn).toString());
            if (!vOrigin.elementAt(iColumn).toString().equals(this.getValueAt(iRow, iColumn).toString())) {
                return (true);
            }
        }
        return (false);
    }

    private boolean keyChanged(int iRow) {
        for (int i = 0; i < getColumnCount(); i++) {
            if (magTableModel.isColumnPrimaryKey(i)) {
                if (columnValueChanged(iRow, i)) {
                    return (true);
                }
            }
        }
        return (false);
    }

    private String getPrimaryKeyFieldValuesForMessage() {
        String sRetVal = "";
        String sID = "";
        boolean bOnlyID = true;
        int iRecordInfoFieldCount = 0;
        for (int iColumn = 0; iColumn < getColumnCount(); iColumn++) {
            if (magTableModel.isColumnPrimaryKey(iColumn)) {
                sRetVal += (sRetVal.equalsIgnoreCase("") ? "" : ", ") + StringUtils.left(this.getStringValueAt(this.getSelectedRow(), iColumn).trim(), 50);
                if (!magTableModel.isIdentityColumn(iColumn)) {
                    bOnlyID = false;
                }
            }
            if (magTableModel.isRecordInfoColumn(iColumn)) {
                ++iRecordInfoFieldCount;
            }
        }
        if (bOnlyID) {
            sID = sRetVal;
            sRetVal = "";
            if (iRecordInfoFieldCount > 0) {
                for (int iColumn = 0; iColumn < getColumnCount(); iColumn++) {
                    if (magTableModel.isRecordInfoColumn(iColumn)) {
                        sRetVal += (sRetVal.equalsIgnoreCase("") ? "" : ", ") + StringUtils.left(this.getDisplayValueAt(this.getSelectedRow(), iColumn).trim(), 50);
                    }
                }
            } else {
                int iCount = 0;
                for (int iColumn = 0; iColumn < getColumnCount(); iColumn++) {
                    if (!magTableModel.isColumnPrimaryKey(iColumn) && iCount < 3) {
                        sRetVal += (sRetVal.equalsIgnoreCase("") ? "" : ", ") + StringUtils.left(this.getDisplayValueAt(this.getSelectedRow(), iColumn).trim(), 50);
                        ++iCount;
                    }
                }
            }
            sRetVal += " [ID=" + sID + "]";
        }
        return (sRetVal);
    }

    //@todo task: translate messages
    private boolean checkRecord(int iRow) {
        Connection conn = swingAppInterface.getConnection(sConnectionName);
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sSQL = "";
        String sWhere = "";
        String sTableName = "";
        int iKeyColumn = 0;

        //not from db
        if (magTableModel.getResultSetMetaData() == null) {
            return (true);
        }
        //check if it is exists already (PK when not ID)
        //@todo task : check not only pk or id, but other indexes - maybe slow!!!
        try {
            sTableName = magTableModel.getTableName();
            if (keyChanged(iRow)) {
                sWhere = "";
                iKeyColumn = 0;
                for (int i = 0; i < getColumnCount(); i++) {
                    if (magTableModel.isColumnPrimaryKey(i) && !magTableModel.isIdentityColumn(i)) {
                        ++iKeyColumn;
                        sWhere += (sWhere.equals("") ? "" : " and ") + magTableModel.getOriginColumnName(i) + "=?";
                    }
                }
                if (iKeyColumn > 0) {
                    iKeyColumn = 0;
                    sSQL = "select 1 from " + sTableName + " where " + sWhere;
                    swingAppInterface.logLine(sSQL, LoggerInterface.LOG_DEBUG);
                    ps = conn.prepareStatement(sSQL);
                    for (int i = 0; i < getColumnCount(); i++) {
                        if (magTableModel.isColumnPrimaryKey(i) && !magTableModel.isIdentityColumn(i)) {
                            ps.setObject(iKeyColumn + 1, this.getValueAt(iRow, i));
                            ++iKeyColumn;
                        }
                    }
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        //@todo task: azonosítókat beletenni az üzenetbe
                        //@todo wish: az elsõ érintett mezõre helyezni a fókuszt
                        AppUtils.messageBox(parentFrame, "Ilyen azonosítójú rekord már létezik!");
                        return (false);
                    }
                }
            }

            for (int i = 0; i < getColumnCount(); i++) {

                //2017.06.06.
                if (getMagTableModel().getTableInfo() != null) {
                    if (getMagTableModel().getTableInfo().getTableInfoPlus() != null) {
                        Object o = getMagTableModel().getTableInfo().getTableInfoPlus().calculatedColumnValue(convertRowIndexToModel(iRow), convertColumnIndexToModel(i));
                        if (o != null) {
                            this.setValueAt(o, iRow, i);
                        }
                    }
                }

                if (magTableModel.isModificationTimeColumn(i)) {
                    this.setValueAt(new java.sql.Timestamp(new java.util.Date().getTime()), iRow, i);
                }
                //check if "not null" fields are filled - except identity ones
                if (magTableModel.getResultSetMetaData().isNullable(i + 1) == ResultSetMetaData.columnNoNulls) {
                    //if (!magTableModel.isIdentityColumn(i)) {
                    if (!magTableModel.isIdentityColumn(i) && !magTableModel.isVirtualColumn(i)) {
                        if (this.getValueAt(iRow, i) == null) {
                            AppUtils.messageBox(parentFrame, magTableModel.getColumnName(i) + " mezõ nem lehet üres!");
                            setColumnSelectionInterval(i, i); //MaG 2018.10.13.
                            return (false);
                        }
                    }
                }
                if (magTableModel.isMandatoryColumn(i)) {
                    if (StringUtils.isNull(this.getValueAt(iRow, i), "").equalsIgnoreCase("")) {
                        AppUtils.messageBox(parentFrame, magTableModel.getColumnName(i) + " mezõt kötelezõ kitölteni!");
                        setColumnSelectionInterval(i, i); //MaG 2018.10.12.
                        return (false);
                    }
                }
                if (!magTableModel.getAllowedCharacters(i).equalsIgnoreCase("")) {
                    if (!StringUtils.containsAllowedCharactersOrEmpty(StringUtils.isNull(this.getValueAt(iRow, i), ""), magTableModel.getAllowedCharacters(i))) {
                        AppUtils.messageBox(parentFrame, magTableModel.getColumnName(i) + " mezõ nem megengedett karaktert tartalmaz! (Megengedett karakterek: " + magTableModel.getAllowedCharacters(i) + ")");
                        setColumnSelectionInterval(i, i); //MaG 2018.10.13.
                        return (false);
                    }
                }
                if (magTableModel.isModifierColumn(i)) {
                    this.setValueAt(swingAppInterface.getGlobal("_db_modifier"), iRow, i);
                }
//                if (magTableModel.isModificationTimeColumn(i)) {
//                    this.setValueAt(new java.sql.Timestamp(new java.util.Date().getTime()), iRow, i);
//                }
                //check field length
                if (magTableModel.getResultSetMetaData().getPrecision(i + 1) > 0 && magTableModel.getResultSetMetaData().getColumnClassName(i + 1).equals("java.lang.String") && StringUtils.isNull(this.getValueAt(iRow, i), "").length() > magTableModel.getResultSetMetaData().getPrecision(i + 1)) {
                    AppUtils.messageBox(parentFrame, magTableModel.getColumnName(i) + " mezõ tartalma legfeljebb " + magTableModel.getResultSetMetaData().getPrecision(i + 1) + " karakter hosszú lehet");
                    setColumnSelectionInterval(i, i); //MaG 2018.10.13.
                    return (false);
                }
                //@todo task: check precision and scale when Number!!!
            }

            //MaG 2018.11.12.
            if (getMagTableModel().getTableInfo() != null) {
                if (getMagTableModel().getTableInfo().getTableInfoPlus() != null) {
                    if (!getMagTableModel().getTableInfo().getTableInfoPlus().isRowValid(convertRowIndexToModel(iRow))) {
                        return (false);
                    }
                }
            }

        } catch (SQLException sqle) {
            swingAppInterface.handleError(sqle);
            return (false);
        }
        return (true);
    }

    //@todo translation: Figyelem! Törli a rekordot? / azonosítójú rekord nem létezik / rekord törlésre került / rekord törlésre nem sikerült
    private boolean deleteRecord(int iRow) {
        if (iRow < 0) {
            return (false);
        }

        if (magTableModel.getRowStatus(convertRowIndexToModel(iRow)) == MagTableModel.ROW_STATUS_NEW_MODIFIED) {
            if (!AppUtils.yesNoQuestion(parentFrame, "Figyelem!", "Törli az új rekordot?", swingAppInterface)) {
                return (false);
            }
            this.getMagTableModel().removeRow(convertRowIndexToModel(iRow));
            int iNextRow = iRow - 1;
            if (iNextRow < 0) {
                iNextRow = 0;
            }
            if (this.getRowCount() > 0) {
                //this.getSelectionModel().setLeadSelectionIndex(iNextRow);
                this.setRowSelectionInterval(iNextRow, iNextRow);
                setFirstEditableCell();
                storeOriginRowValues(iNextRow);
                fireMagTableEventRowColChanged(iNextRow, -1);
            }
            fireMagTableEventRowDeleted(iRow);
            return (true);
        }

        Connection conn = swingAppInterface.getConnection(sConnectionName);
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sSQLCheck = "";
        String sSQLDelete = "";
        String sWhere = "";
        String sTableName = "";
        int iKeyColumn = 0;
        String sRecordIdentifier = getPrimaryKeyFieldValuesForMessage();
        String sLogSQL = "";
        String sLogSQLWhere = "";
        String sLogXML = "";
        String sLogXMLFields = "";
        String sLogXMLKeys = "";

//        int iModelRow = this.convertRowIndexToModel(iRow);
        sTableName = magTableModel.getTableName();
        if (sTableName.equalsIgnoreCase("")) {
            return (true);
        }
        //if (!magTableModel.isUpdateableTable()) {
        if (!magTableModel.isUpdateableTable() || magTableModel.isReadOnlyTable()) {
            return (true);
        }
        //not from db
        if (magTableModel.getResultSetMetaData() == null) {
            return (true);
        }

        if (!AppUtils.yesNoQuestion(parentFrame, "Figyelem!", "Törli (" + sRecordIdentifier + ") rekordot?", swingAppInterface)) {
            return (false);
        }
        try {
            sTableName = magTableModel.getTableName();
            sWhere = "";
            iKeyColumn = 0;
            for (int i = 0; i < getColumnCount(); i++) {
                if (magTableModel.isColumnPrimaryKey(i)) {
                    ++iKeyColumn;
                    sWhere += (sWhere.equals("") ? "" : " and ") + magTableModel.getOriginColumnName(i) + "=?";
                    sLogSQLWhere += (sLogSQLWhere.equals("") ? "" : " and ") + magTableModel.getOriginColumnName(i) + "=" + convertObjectValueToString(vOrigin.elementAt(i));
                    sLogXMLKeys += "<key name='" + magTableModel.getOriginColumnName(i) + "'>" + convertObjectValueToString(vOrigin.elementAt(i), false) + "</key>";
                }
            }
            iKeyColumn = 0;
            sSQLCheck = "select 1 from " + sTableName + " where " + sWhere;
            //MaG 2017.07.24. swingAppInterface.logLine(sSQLCheck, LoggerInterface.LOG_SQL, sTableName);
            ps = conn.prepareStatement(sSQLCheck);
            for (int i = 0; i < getColumnCount(); i++) {
                if (magTableModel.isColumnPrimaryKey(i)) {
                    ps.setObject(iKeyColumn + 1, this.getValueAt(iRow, i));
                    ++iKeyColumn;
                }
            }
            rs = ps.executeQuery();
            if (!rs.next()) {
                AppUtils.messageBox(parentFrame, "(" + sRecordIdentifier + ") azonosítójú rekord nem létezik!");
                return (false);
            }
            rs.close();
            ps.close();

            iKeyColumn = 0;
            sSQLDelete = "delete from " + sTableName + " where " + sWhere;
            //MaG 2017.07.24. swingAppInterface.logLine(sSQLDelete, LoggerInterface.LOG_SQL, sTableName);
            ps = conn.prepareStatement(sSQLDelete);
            for (int i = 0; i < getColumnCount(); i++) {
                if (magTableModel.isColumnPrimaryKey(i)) {
                    ps.setObject(iKeyColumn + 1, this.getValueAt(iRow, i));
                    ++iKeyColumn;
                }
            }
            sLogSQL = "delete from " + sTableName + " where " + sLogSQLWhere;
            sLogXML = "<?xml version='1.0' encoding='iso-8859-2'?><delete table='" + sTableName + "'>" + sLogXMLKeys + sLogXMLFields + "<sql>" + sLogSQL + "</sql></delete>";
            swingAppInterface.logLine(sLogSQL, LoggerInterface.LOG_SQL, sTableName);
            swingAppInterface.logLine(sLogXML, LoggerInterface.LOG_SQL, sTableName);
            int iResult = ps.executeUpdate();
            if (iResult == 1) {
                this.getMagTableModel().removeRow(convertRowIndexToModel(iRow));
                int iNextRow = iRow - 1;
                if (iNextRow < 0) {
                    iNextRow = 0;
                }
                if (this.getRowCount() > 0) {
                    //this.getSelectionModel().setLeadSelectionIndex(iNextRow);
                    this.setRowSelectionInterval(iNextRow, iNextRow);
                    setFirstEditableCell();
                    storeOriginRowValues(iNextRow);
                    fireMagTableEventRowColChanged(iNextRow, -1);
                }
                showRowcountStatus();
                fireMagTableEventRowDeleted(iRow); //MaG 2018.05.07.
                AppUtils.messageBox(parentFrame, "(" + sRecordIdentifier + ") rekord törlésre került.");
                //MaG 2018.05.07. fireMagTableEventRowDeleted(iRow);
                return (true);
            } else {
                AppUtils.messageBox(parentFrame, "(" + sRecordIdentifier + ") rekord törlése nem sikerült (" + Integer.toString(iResult) + ")");
                this.requestFocus();
                return (false);
            }
        } catch (SQLException sqle) {
            swingAppInterface.handleError(sqle);
            return (false);
        }
    }

    private String convertObjectValueToString(Object oValue) {
        return convertObjectValueToString(oValue, true);
    }

    private String convertObjectValueToString(Object oValue, boolean bWithApostrophes) {
        String sApostrophe = (bWithApostrophes ? "'" : "");
        if (oValue == null) {
            return "NULL";
        }
        if (oValue instanceof String) {
            return (sApostrophe + oValue.toString() + sApostrophe);
        }

        if (oValue instanceof BigDecimal) {
            return (((BigDecimal) oValue).toString());
        }
        if (oValue instanceof BigInteger) {
            return (((BigInteger) oValue).toString());
        }
        if (oValue instanceof Byte) {
            return (((Byte) oValue).toString());
        }
        if (oValue instanceof Double) {
            return (((Double) oValue).toString());
        }
        if (oValue instanceof Float) {
            return (((Float) oValue).toString());
        }
        if (oValue instanceof Integer) {
            return (((Integer) oValue).toString());
        }
        if (oValue instanceof Long) {
            return (((Long) oValue).toString());
        }
        if (oValue instanceof Short) {
            return (((Short) oValue).toString());
        }

        if (oValue instanceof LookupInteger) {
            return (((LookupInteger) oValue).toString());
        }

        if (oValue instanceof Boolean) {
            return (((Boolean) oValue).booleanValue() ? "true" : "false");
        }

        if (oValue instanceof java.sql.Date) {
            return (sApostrophe + swingAppInterface.getSQLDateFormat().format((java.sql.Date) oValue) + sApostrophe);
        }
        if (oValue instanceof java.sql.Time) {
            return (sApostrophe + swingAppInterface.getSQLTimeFormat().format((java.sql.Time) oValue) + sApostrophe);
        }
        if (oValue instanceof java.sql.Timestamp) {
            return (sApostrophe + swingAppInterface.getSQLDateTimeFormat().format((java.sql.Timestamp) oValue) + sApostrophe);
        }
        if (oValue instanceof java.util.Date) {
            return (sApostrophe + swingAppInterface.getSQLDateFormat().format((java.util.Date) oValue) + sApostrophe);
        }
        return (sApostrophe + oValue.toString() + sApostrophe);
    }

    private boolean saveRecord(int iRow) {
        int iModelRow = this.convertRowIndexToModel(iRow);

        int iAutoIncrementCount = 0;
        int iVirtualCount = 0;
        swingAppInterface.logLine("save row #" + Integer.toString(iRow), LoggerInterface.LOG_DEBUG);

        Connection connection = swingAppInterface.getConnection(sConnectionName);
        PreparedStatement ps = null;
        String sSQL = "";
        String sColumNames = "";
        String sColumValues = "";
        String sSet = "";
        String sWhere = "";
        String sLogSQL = "";
        String sLogSQLFields = "";
        String sLogSQLValues = "";
        String sLogSQLSet = "";
        String sLogSQLWhere = "";
        String sLogXML = "";
        String sLogXMLFields = "";
        String sLogXMLKeys = "";
        String sTableName = "";
        int iFieldNumber = 0;
        int iIDCol = -1;

        sTableName = magTableModel.getTableName();
        if (sTableName.equalsIgnoreCase("")) {
            return (true);
        }
        //if (!magTableModel.isUpdateableTable()) {
        if (!magTableModel.isUpdateableTable() || magTableModel.isReadOnlyTable()) {
            return (true);
        }
        if (!checkRecord(iRow)) {
            return (false);
        }

        int iRowStatus = magTableModel.getRowStatus(iModelRow);
        if ((iRowStatus == MagTableModel.ROW_STATUS_NEW) || (iRowStatus == MagTableModel.ROW_STATUS_NEW_MODIFIED)) {
            //insert
            try {
                int iResponse = fireMagTableEventBeforeInsert(iRow);
                if (iResponse == 0) {
                    for (int i = 0; i < getColumnCount(); i++) {
                        //MaG 2018.01.09.
                        if (magTableModel.isCreationTimeColumn(i)) {
                            this.setValueAt(new java.sql.Timestamp(new java.util.Date().getTime()), iRow, i);
                        }
                        if (magTableModel.isIdentityColumn(i)) {
                            ++iAutoIncrementCount;
                            iIDCol = i;
                        } else if (magTableModel.isVirtualColumn(i)) {
                            ++iVirtualCount;
                        } else {
                            sColumNames += (sColumNames.equals("") ? "" : ", ") + magTableModel.getOriginColumnName(i);
                            sColumValues += (sColumValues.equals("") ? "" : ", ") + "?";
                            sLogSQLFields += (sLogSQLFields.equals("") ? "" : ", ") + magTableModel.getOriginColumnName(i);
                            sLogSQLValues += (sLogSQLValues.equals("") ? "" : ", ") + convertObjectValueToString(this.getValueAt(iRow, i));
                            sLogXMLFields += "<field name='" + magTableModel.getOriginColumnName(i) + "'>" + convertObjectValueToString(this.getValueAt(iRow, i), false) + "</field>";
                        }
                    }
                    sSQL = "insert into " + sTableName + " (" + sColumNames + ") values (" + sColumValues + ")";
                    ps = connection.prepareStatement(sSQL, PreparedStatement.RETURN_GENERATED_KEYS);
                    iAutoIncrementCount = 0;
                    iVirtualCount = 0;
                    for (int i = 0; i < getColumnCount(); i++) {
                        if (magTableModel.isIdentityColumn(i)) {
                            ++iAutoIncrementCount;
                        } else if (magTableModel.isVirtualColumn(i)) {
                            ++iVirtualCount;
                        } else {
                            ps.setObject(i + 1 - iAutoIncrementCount - iVirtualCount, this.getValueAt(iRow, i));
                        }
                    }
                    sLogSQL = "insert into " + sTableName + " (" + sLogSQLFields + ") values (" + sLogSQLValues + ")";
                    sLogXML = "<?xml version='1.0' encoding='iso-8859-2'?><insert table='" + sTableName + "'>" + sLogXMLFields + "<sql>" + sLogSQL + "</sql></insert>";
                    swingAppInterface.logLine(sLogSQL, LoggerInterface.LOG_SQL, sTableName);
                    swingAppInterface.logLine(sLogXML, LoggerInterface.LOG_SQL, sTableName);
                    int iCount = ps.executeUpdate();
                    if (iIDCol > -1) {
                        ResultSet rsForGeneratedKeys = ps.getGeneratedKeys();
                        if (rsForGeneratedKeys != null) {
                            if (rsForGeneratedKeys.next()) {
                                this.setValueAt(rsForGeneratedKeys.getObject(1), iRow, iIDCol);
                            }
                        }
                    }
                    swingAppInterface.logLine(Integer.toString(iCount) + " row has inserted", LoggerInterface.LOG_DEBUG);
                    storeOriginRowValues(iRow);
                    magTableModel.setRowStatus(iModelRow, MagTableModel.ROW_STATUS_OK);
                    this.repaint();
                    fireMagTableEventRowStatusChanged(iRow);
                    fireMagTableEventRowSaved(iRow);
                } else {
                    //deleteRecord(iRow);
                }
            } catch (SQLException sqle) {
                swingAppInterface.handleError(sqle);
                return (false);
            }
        } else {
            //update
            try {
                //@todo task: check if key field values has isChanged and the new exists
                int iResponse = fireMagTableEventBeforeUpdate(iRow);
                if (iResponse == 0) {
                    for (int i = 0; i < getColumnCount(); i++) {
                        if (magTableModel.isIdentityColumn(i)) {
                            ++iAutoIncrementCount;
                            iIDCol = i;
                        } else if (magTableModel.isVirtualColumn(i)) {
                            ++iVirtualCount;
                        } else {
                            if (columnValueChanged(iRow, i)) {
                                sSet += (sSet.equals("") ? "" : ", ") + magTableModel.getOriginColumnName(i) + "=?";
                                //sLogSQLSet += (sLogSQLSet.equals("") ? "" : ", ") + magTableModel.getOriginColumnName(i) + "=<field" + Integer.toString(i) + ">";
                                sLogSQLSet += (sLogSQLSet.equals("") ? "" : ", ") + magTableModel.getOriginColumnName(i) + "=" + convertObjectValueToString(this.getValueAt(iRow, i));
                                sLogXMLFields += "<field name='" + magTableModel.getOriginColumnName(i) + "'><old>" + convertObjectValueToString(vOrigin.elementAt(i)) + "</old><new>" + convertObjectValueToString(this.getValueAt(iRow, i), false) + "</new></field>";
                            }
                        }
                        if (magTableModel.isColumnPrimaryKey(i)) {
                            sWhere += (sWhere.equals("") ? "" : " and ") + magTableModel.getOriginColumnName(i) + "=?";
                            //sLogSQLWhere += (sLogSQLWhere.equals("") ? "" : " and ") + magTableModel.getOriginColumnName(i) + "=<key" + Integer.toString(i) + ">";
                            sLogSQLWhere += (sLogSQLWhere.equals("") ? "" : " and ") + magTableModel.getOriginColumnName(i) + "=" + convertObjectValueToString(vOrigin.elementAt(i));
                            sLogXMLKeys += "<key name='" + magTableModel.getOriginColumnName(i) + "'>" + convertObjectValueToString(vOrigin.elementAt(i), false) + "</key>";
                        }
                    }
                    sSQL = "update " + sTableName + " set " + sSet + " where " + sWhere;
                    //sLogSQL = "update " + sTableName + " set " + sLogSQLSet + " where " + sLogSQLWhere;
                    iFieldNumber = 0;
                    ps = connection.prepareStatement(sSQL);
                    iAutoIncrementCount = 0;
                    iVirtualCount = 0;
                    for (int i = 0; i < getColumnCount(); i++) {
                        if (magTableModel.isIdentityColumn(i)) {
                            ++iAutoIncrementCount;
                        } else if (magTableModel.isVirtualColumn(i)) {
                            ++iVirtualCount;
                            //++iFieldNumber;
                        } else {
                            if (columnValueChanged(iRow, i)) {
                                ++iFieldNumber;
                                ps.setObject(iFieldNumber, this.getValueAt(iRow, i));
                                //sLogSQL = StringUtils.stringReplace(sLogSQL, "<field" + Integer.toString(i) + ">", convertObjectValueToStringForLog(this.getValueAt(iRow, i)));
                            }
                        }
                    }
                    for (int i = 0; i < getColumnCount(); i++) {
                        if (magTableModel.isColumnPrimaryKey(i)) {
                            ++iFieldNumber;
                            ps.setObject(iFieldNumber, vOrigin.elementAt(i));
                            //sLogSQL = StringUtils.stringReplace(sLogSQL, "<key" + Integer.toString(i) + ">", convertObjectValueToStringForLog(this.getValueAt(iRow, i)));
                        }
                    }
                    sLogSQL = "update " + sTableName + " set " + sLogSQLSet + " where " + sLogSQLWhere;
                    sLogXML = "<?xml version='1.0' encoding='iso-8859-2'?><update table='" + sTableName + "'>" + sLogXMLKeys + sLogXMLFields + "<sql>" + sLogSQL + "</sql></update>";
                    swingAppInterface.logLine(sLogSQL, LoggerInterface.LOG_SQL, sTableName);
                    swingAppInterface.logLine(sLogXML, LoggerInterface.LOG_SQL, sTableName);
                    int iCount = ps.executeUpdate();
                    swingAppInterface.logLine(Integer.toString(iCount) + " row has updated", LoggerInterface.LOG_DEBUG);
                    storeOriginRowValues(iRow);
                    magTableModel.setRowStatus(iModelRow, MagTableModel.ROW_STATUS_OK);
                    this.repaint();
                    fireMagTableEventRowStatusChanged(iRow);
                    fireMagTableEventRowSaved(iRow);
                } else {
                    restoreOriginRowValues(iRow);
                    magTableModel.setRowStatus(iModelRow, MagTableModel.ROW_STATUS_OK);
                    this.repaint();
                }
            } catch (SQLException sqle) {
                swingAppInterface.handleError(sqle);
                return (false);
            }
        }

        if (iVirtualCount > 0) {
            for (int i = 0; i < getColumnCount(); i++) {
                if (magTableModel.isVirtualColumn(i)) {
                    String sVirtualValueSQL = magTableModel.getColumnVirtualValueSQL(i);
                    sVirtualValueSQL = sVirtualValueSQL.replaceAll("''", "'"); //MaG 2017.05.25.
                    if (sVirtualValueSQL.contains("[")) {
                        String sSQLv = sVirtualValueSQL;
                        Pattern regex = Pattern.compile("\\[[a-zA-Z0-9_]+\\]");
                        Matcher regexMatcher = regex.matcher(sVirtualValueSQL);
                        while (regexMatcher.find()) {
                            sSQLv = StringUtils.stringReplace(sSQLv, regexMatcher.group(), "?");
                        }
                        Object o = null;
                        try {
                            PreparedStatement psVirtualValue = connection.prepareStatement(sSQLv);
                            regexMatcher.reset();
                            int iPIndex = 0;
                            while (regexMatcher.find()) {
                                int iIndex = magTableModel.getTableInfo().getFieldIndexByName(regexMatcher.group().replace("[", "").replace("]", ""));
                                psVirtualValue.setObject(++iPIndex, this.getValueAt(iRow, iIndex));
                            }
                            ResultSet rsVirtualValue = psVirtualValue.executeQuery();
                            if (rsVirtualValue.next()) {
                                o = rsVirtualValue.getObject(1);
                            }
                        } catch (SQLException sqle) {
                            swingAppInterface.handleError(sqle);
                        }
                        this.setValueAt(o, iRow, i);
                        //vRecord.set(vRecord.size() - 1, o);
                    }
                }
            }
            magTableModel.setRowStatus(iModelRow, MagTableModel.ROW_STATUS_OK);
        }
        //@todo task : place the new / modified record to the right place (according to the order)

        //appInterface.logLine(sLogSQL, LoggerInterface.LOG_DEBUG);
        showRowcountStatus();
        return (true);
    }

    public void showRowcountStatus() {
        showRowcountStatus("");
    }

    public void showRowcountStatus(String sAdditionalInfo) {
        //magTableInterface.status(Integer.toString(this.getRowCount()) + " sor");
        String sStatus = swingAppInterface.getLanguageString("MagTable RowCount");
        //sStatus = StringUtils.stringReplace(sStatus, "%1", Integer.toString(this.magTableModel.getRowCount()));
        sStatus = StringUtils.stringReplace(sStatus, "%1", Integer.toString(this.getRowCount()) + "/" + Integer.toString(this.magTableModel.getRowCount())) + (sAdditionalInfo.equalsIgnoreCase("") ? "" : " " + sAdditionalInfo);
        if (magTableInterface != null) {
            magTableInterface.status(sStatus);
        }
    }

    @Override
    public Object getValueAt(int row, int column) {
        return magTableModel.getValueAt(convertRowIndexToModel(row), convertColumnIndexToModel(column));
    }

    public String getStringValueAt(int row, int column) {
        return StringUtils.isNull(magTableModel.getValueAt(convertRowIndexToModel(row), convertColumnIndexToModel(column)), "");
    }

    public double getDblValueAt(int row, int column) {
        return (StringUtils.doubleValue(this.getValueAt(row, column)));
    }

    public double getDblValueAt(int row, String columnName) {
        return (StringUtils.doubleValue(this.getValueAt(row, columnName)));
    }

    public Integer getIntegerValueAt(int row, int column) {
        return (IntegerUtils.convertToInteger(this.getValueAt(row, column)));
    }

    public Integer getIntegerValueAt(int row, String columnName) {
        return (IntegerUtils.convertToInteger(this.getValueAt(row, columnName)));
    }

    public int getIntValueAt(int row, int column) {
        //return StringUtils.intValue(StringUtils.isNull(magTableModel.getValueAt(convertRowIndexToModel(row), convertColumnIndexToModel(column)), ""));
        return StringUtils.intValue(StringUtils.isNull(this.getValueAt(row, column), ""));
    }

    public int getIntValueAt(int row, String columnName) {
        return StringUtils.intValue(StringUtils.isNull(this.getValueAt(row, columnName), ""));
    }

    public long getLongValueAt(int row, int column) {
        return StringUtils.longValue(StringUtils.isNull(this.getValueAt(row, column), ""));
    }

    public long getLongValueAt(int row, String columnName) {
        return StringUtils.longValue(StringUtils.isNull(this.getValueAt(row, columnName), ""));
    }

    public BigDecimal getBigDecimalValue(String columnName) {
        return (getBigDecimalValueAt(0, columnName));
    }

    public BigDecimal getBigDecimalValueAt(int row, int col) {
        return (BigDecimalUtils.convertToBigDecimal(this.getValueAt(row, col)));
    }

    public BigDecimal getBigDecimalValueAt(int row, String columnName) {
        return (BigDecimalUtils.convertToBigDecimal(this.getValueAt(row, columnName)));
    }

    public java.util.Date getUtilDateValueAt(int row, int col) {
        return (DateTimeUtils.convertToUtilDate(getValueAt(row, col)));
    }

    public java.util.Date getUtilDateValueAt(int row, String columnName) {
        return (DateTimeUtils.convertToUtilDate(getValueAt(row, columnName)));
    }

    public Object getValueAt(int row, String columnName) {
        int iModelColumn = magTableModel.findColumn(columnName);
        if (iModelColumn < 0) {
            return (new String(""));
        }
        if (iModelColumn >= getColumnCount()) {
            return (new String(""));
        }
        return (this.getValueAt(row, convertColumnIndexToView(iModelColumn)));
    }

    public String getStringValueAt(int row, String columnName) {
        return StringUtils.isNull(this.getValueAt(row, columnName), "");
    }

    public void setValueAt(Object aValue, int row, String columnName) {
        int iModelColumn = magTableModel.findColumn(columnName);
        if (iModelColumn < 0) {
            return;
        }
        if (iModelColumn >= getColumnCount()) {
            return;
        }
        magTableModel.setValueAt(aValue, convertRowIndexToModel(row), iModelColumn);
    }

    //@todo task: extract model column and row values to variables at the beginning
    public String getDisplayValueAt(int row, int column) {
        String sRetVal = "";
        Object oValue = magTableModel.getValueAt(convertRowIndexToModel(row), convertColumnIndexToModel(column));
        sRetVal = StringUtils.isNull(oValue, "");
        if (oValue != null) {
            Class c = oValue.getClass();
            if (c.equals(java.sql.Date.class) || c.equals(java.util.Date.class)) {
                if (oValue != null) {
                    sRetVal = swingAppInterface.getDateFormat().format(oValue);
                }
            }
            if (c.equals(java.math.BigDecimal.class)) {
                if (magTableModel.getColumnSpecType(convertColumnIndexToModel(column)).equals("decimal_time")) {
                    if (oValue != null) {
                        sRetVal = DateTimeDocument.convertDecimaltimeToString((java.math.BigDecimal) oValue, swingAppInterface);
                    }
                } else {
                    if (oValue != null) {
                        sRetVal = swingAppInterface.getDecimalFormat(magTableModel.getDecimals(convertColumnIndexToModel(column))).format(oValue);
                    }
                }
            }
        }
        if (getMagTableModel().isLookupColumn(convertColumnIndexToModel(column))) {
            sRetVal = getMagTableModel().getColumnLookupField(convertColumnIndexToModel(column)).getDisplay(oValue);
        }
        return (sRetVal);
    }

    private boolean isRowChanged(int iRow) {
        if (iRow < 0 || iRow >= this.getRowCount()) {
            return (false);
        }
//        appInterface.logLine("isChanged row? #" + Integer.toString(iRow), LoggerInterface.LOG_DEBUG);
        if (vOrigin != null) {
//            for (int i = 0; i < getColumnCount(); i++) {
//                appInterface.logLine("column #" + Integer.toString(i) + " " + StringUtils.isNull(vOrigin.elementAt(i), "") + " = " + StringUtils.isNull(this.getValueAt(iRow, i), ""), LoggerInterface.LOG_DEBUG);
//            }
            for (int i = 0; i < getColumnCount(); i++) {
                if (!magTableModel.isVirtualColumn(i)) {
                    if (vOrigin.elementAt(i) == null && this.getValueAt(iRow, i) != null) {
                        swingAppInterface.logLine("column #" + Integer.toString(i) + " " + StringUtils.isNull(vOrigin.elementAt(i), "NULL") + " != " + StringUtils.isNull(this.getValueAt(iRow, i), "NULL"), LoggerInterface.LOG_DEBUG);
                        swingAppInterface.logLine("row #" + Integer.toString(iRow) + " has changed", LoggerInterface.LOG_DEBUG);
                        return (true);
                    }
                    if (vOrigin.elementAt(i) != null && this.getValueAt(iRow, i) == null) {
                        swingAppInterface.logLine("column #" + Integer.toString(i) + " " + StringUtils.isNull(vOrigin.elementAt(i), "NULL") + " != " + StringUtils.isNull(this.getValueAt(iRow, i), "NULL"), LoggerInterface.LOG_DEBUG);
                        swingAppInterface.logLine("row #" + Integer.toString(iRow) + " has changed", LoggerInterface.LOG_DEBUG);
                        return (true);
                    }
                    if (vOrigin.elementAt(i) != null && this.getValueAt(iRow, i) != null) {
                        if (!vOrigin.elementAt(i).toString().trim().equals(this.getValueAt(iRow, i).toString().trim())) {
                            swingAppInterface.logLine("column #" + Integer.toString(i) + " " + StringUtils.isNull(vOrigin.elementAt(i), "NULL") + " != " + StringUtils.isNull(this.getValueAt(iRow, i), "NULL"), LoggerInterface.LOG_DEBUG);
                            swingAppInterface.logLine("row #" + Integer.toString(iRow) + " has changed", LoggerInterface.LOG_DEBUG);
                            return (true);
                        }
                    }
                }
            }
        }
        swingAppInterface.logLine("row #" + Integer.toString(iRow) + " has not changed", LoggerInterface.LOG_DEBUG);
        return (false);
    }

    private boolean checkIfRowContentNotChangedOrSavedOrDeletedBeforeRowChange(int iRow) {
        swingAppInterface.logLine("isRowContentOK (" + Integer.toString(iRow) + ")", LoggerInterface.LOG_DEBUG);
        if (iRow < 0 || iRow >= this.getRowCount()) {
            return (true);
        }
        //- if not isChanged, then it is OK when existing row, but not OK when new row
        if (!isRowChanged(iRow)) {
            if (magTableModel.getRowStatus(convertRowIndexToModel(iRow)) == MagTableModel.ROW_STATUS_NEW) {
                //not isChanged new row - to be deleted
                magTableModel.removeRow(convertRowIndexToModel(iRow));
                return (true);
            }
            return (magTableModel.getRowStatus(convertRowIndexToModel(iRow)) != MagTableModel.ROW_STATUS_NEW);
        }
        return (saveRecord(iRow));
    }

    public int getColumnPreferredWidth(int iColumn) {
        return (getColumnModel().getColumn(iColumn).getPreferredWidth());
    }

    public void setColumnWidth(int iColumn, int iWidth) {
        getColumnModel().getColumn(iColumn).setMinWidth(iWidth);
        getColumnModel().getColumn(iColumn).setPreferredWidth(iWidth);
        getColumnModel().getColumn(iColumn).setMaxWidth(iWidth);
    }

    public void setColumnPreferredWidth(int iColumn, int iWidth) {
        if (getColumnModel().getColumn(iColumn).getMaxWidth() < (iWidth + 100)) {
            getColumnModel().getColumn(iColumn).setMaxWidth(iWidth + 100);
        }
        getColumnModel().getColumn(iColumn).setPreferredWidth(iWidth);
    }

    public void setMinColumnWidthAll(int iWidth) {
        for (int i = 0; i < getColumnCount(); i++) {
            getColumnModel().getColumn(i).setMinWidth(iWidth);
        }
    }

    public void setAutoColumnWidth() {
        setAutoColumnWidth(true, 0);
    }

    //@todo task: set minimum width of a column? (or give a typical value, which width will be the minimum width of the column
    public void setAutoColumnWidth(boolean bWithHeader, int iMaximumColumnWidth) {
        bAutoColumnWidthParamWithHeader = bWithHeader;
        iAutoColumnWidthParamMaxWidth = iMaximumColumnWidth;
        Font font = this.getFont();
        //FontMetrics fm = new javax.swing.JPanel().getFontMetrics(font);
        FontMetrics fm = this.getFontMetrics(font);
        Object oCellValue = null;
        String sCellValue = "";
        MagCellRenderer mcr = new MagCellRenderer(magTableModel, swingAppInterface);
        Vector<Integer> vColumnWidth = new Vector<Integer>();
        Vector<Integer> vColumnCanGrow = new Vector<Integer>();
        for (int i = 0; i < getColumnCount(); i++) {
            vColumnWidth.add(new Integer(0));
        }
        if (bWithHeader) {
            for (int i = 0; i < getColumnCount(); i++) {
                sCellValue = this.getColumnModel().getColumn(i).getHeaderValue().toString();
                //sCellValue = this.getColumnModel().getColumn(i).getHeaderValue().toString().trim(); //MaG 2017.11.09.
                if (sCellValue.startsWith("<html>") && sCellValue.contains("<br>")) {
                    sCellValue = StringUtils.stringReplace(sCellValue, "<html>", "");
                    sCellValue = StringUtils.stringReplace(sCellValue, "</html>", "");
                    sCellValue = StringUtils.stringReplace(sCellValue, "<center>", "");
                    sCellValue = StringUtils.stringReplace(sCellValue, "&nbsp;", " ");
                    String[] sCellValueSplit = sCellValue.split("<br>");
                    for (int j = 0; j < sCellValueSplit.length; j++) {
                        sCellValueSplit[j] = sCellValueSplit[j] + "X"; //for the triangles which show the sorting
                        if (fm.stringWidth(sCellValueSplit[j]) > vColumnWidth.elementAt(i).intValue()) {
                            vColumnWidth.setElementAt(new Integer(fm.stringWidth(sCellValueSplit[j])), i);
                        }
                    }
                } else {
                    sCellValue = sCellValue + "XX"; //for the triangles which show the sorting
                    if (fm.stringWidth(sCellValue) > vColumnWidth.elementAt(i).intValue()) {
                        vColumnWidth.setElementAt(new Integer(fm.stringWidth(sCellValue)), i);
                    }
                }
            }
        }
        //System.out.println("");
        for (int iRow = 0; iRow < this.getRowCount(); iRow++) {
            for (int iColumn = 0; iColumn < getColumnCount(); iColumn++) {
                sCellValue = "";
                oCellValue = this.getValueAt(iRow, iColumn);
                MagComboBoxField magLookupField = getMagTableModel().getColumnLookupField(iColumn);
                if (magLookupField != null) {
                    sCellValue = magLookupField.getDisplay(oCellValue);
                } else {
                    //sCellValue = mcr.getTableCellRendererComponent(this, oCellValue, false, false, iRow, iColumn).toString();
                    Component component = mcr.getTableCellRendererComponent(this, oCellValue, false, false, iRow, iColumn);
                    if (component.getClass() == javax.swing.JCheckBox.class) {
                        sCellValue = "false";
                    } else if (component.getClass() == javax.swing.JButton.class) {
                        sCellValue = ((javax.swing.JButton) component).getText() + "XXX";
                    } else {
                        sCellValue = component.toString();
                        //sCellValue = component.toString().trim(); //MaG 2017.11.09.
                    }
                    //System.out.println(sCellValue);

                    if (oCellValue != null) {
                        if (oCellValue.getClass() == Boolean.class) {
                            sCellValue = "false";
                        }
                    }
                }

                //MaG 2018.10.24. HTML can be in the cells too ...
//                if (fm.stringWidth(sCellValue) > vColumnWidth.elementAt(iColumn).intValue()) {
//                    vColumnWidth.setElementAt(new Integer(fm.stringWidth(sCellValue)), iColumn);
//                }
                if (sCellValue.startsWith("<html>") && sCellValue.contains("<br>")) {
                    sCellValue = StringUtils.stringReplace(sCellValue, "<html>", "");
                    sCellValue = StringUtils.stringReplace(sCellValue, "</html>", "");
                    sCellValue = StringUtils.stringReplace(sCellValue, "<center>", "");
                    sCellValue = StringUtils.stringReplace(sCellValue, "&nbsp;", " ");
                    String[] sCellValueSplit = sCellValue.split("<br>");
                    for (int j = 0; j < sCellValueSplit.length; j++) {
                        if (fm.stringWidth(sCellValueSplit[j]) > vColumnWidth.elementAt(iColumn).intValue()) {
                            vColumnWidth.setElementAt(new Integer(fm.stringWidth(sCellValueSplit[j])), iColumn);
                        }
                    }
                } else {
                    if (fm.stringWidth(sCellValue) > vColumnWidth.elementAt(iColumn).intValue()) {
                        vColumnWidth.setElementAt(new Integer(fm.stringWidth(sCellValue)), iColumn);
                    }
                }
            }
        }
        for (int iColumn = 0; iColumn < getColumnCount(); iColumn++) {
            if (iMaximumColumnWidth > 0) {
                if (vColumnWidth.elementAt(iColumn).intValue() > iMaximumColumnWidth) {
                    vColumnWidth.setElementAt(new Integer(iMaximumColumnWidth), iColumn);
                }
            }
            int iMinColumnWidth = getMagTableModel().getColumnMinWidth(iColumn);
            if (iMinColumnWidth > 0) {
                if (vColumnWidth.elementAt(iColumn).intValue() < iMinColumnWidth) {
                    vColumnWidth.setElementAt(new Integer(iMinColumnWidth), iColumn);
                }
            }
            int iMaxColumnWidth = getMagTableModel().getColumnMaxWidth(iColumn);
            boolean bCanGrow = iMaxColumnWidth < 0 && Math.abs(iMaxColumnWidth) > Math.abs(iMinColumnWidth);
            if (bCanGrow) {
                vColumnCanGrow.add(iColumn);
            }
            iMaxColumnWidth = Math.abs(iMaxColumnWidth);
            if (iMaxColumnWidth > 0) {
                if (vColumnWidth.elementAt(iColumn).intValue() > iMaxColumnWidth) {
                    vColumnWidth.setElementAt(new Integer(iMaxColumnWidth), iColumn);
                }
            }
            //@todo find out a real good calculation ...
            //setColumnPreferredWidth(i, vColumnWidth.elementAt(i).intValue() + 20);
            //setColumnPreferredWidth(i, new Double(vColumnWidth.elementAt(i).doubleValue() * 1.15).intValue());
            //setColumnPreferredWidth(iColumn, new Double(vColumnWidth.elementAt(iColumn).doubleValue() * 1.05).intValue() + 10);
            setColumnPreferredWidth(iColumn, new Double(vColumnWidth.elementAt(iColumn).doubleValue() * 1.05).intValue() + 15);
            if (getMagTableModel().isHiddenColumn(iColumn)) {
                setColumnWidth(iColumn, 0);
            }
        }
        int iTotalWidth = 0;
        for (int iColumn = 0; iColumn < getColumnCount(); iColumn++) {
            iTotalWidth += getColumnModel().getColumn(iColumn).getPreferredWidth();
        }
        if (vColumnCanGrow.size() > 0) {
            if (jScrollPane != null) {
//                swingAppInterface.logLine(Boolean.toString(jScrollPane.getVerticalScrollBar().isVisible()));
                //jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
                //jScrollPane.repaint();
                //swingAppInterface.logLine(Double.toString(this.getPreferredSize().getWidth()));
//                swingAppInterface.logLine(Double.toString(jScrollPane.getViewportBorderBounds().getWidth()));
//                swingAppInterface.logLine(Double.toString(jScrollPane.getViewport().getWidth()));
//                swingAppInterface.logLine(Double.toString(jScrollPane.getVisibleRect().getWidth()));
                //swingAppInterface.logLine(Double.toString(jScrollPane.getPreferredSize().getWidth()));
                //this.getPreferredSize().getHeight()
                //int iDiff = new Double(jScrollPane.getViewportBorderBounds().getWidth() - jScrollPane.getVerticalScrollBar().getPreferredSize().getWidth()).intValue() - iTotalWidth;
                //*int iDiff = new Double(jScrollPane.getViewportBorderBounds().getWidth()).intValue() - iTotalWidth;
                int iDiff = new Double(jScrollPane.getVisibleRect().getWidth()).intValue() - iTotalWidth;
                //if (jScrollPane.getVerticalScrollBar().isVisible()) {
                if (this.getPreferredSize().getHeight() > jScrollPane.getViewportBorderBounds().getHeight()) {
                    iDiff -= jScrollPane.getVerticalScrollBar().getPreferredSize().getWidth();
                }
                if (iDiff > 0) {
                    for (int i = 0; i < vColumnCanGrow.size(); i++) {
                        setColumnPreferredWidth(vColumnCanGrow.elementAt(i), getColumnModel().getColumn(vColumnCanGrow.elementAt(i)).getPreferredWidth() + (iDiff / vColumnCanGrow.size()) - 10);
                    }
                }
                //jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//                swingAppInterface.logLine(Boolean.toString(jScrollPane.getVerticalScrollBar().isVisible()) + " *");
            }
        }
    }

    public String getRenderedStringValue(int iRow, int iColumn) {
        MagCellRenderer mcr = new MagCellRenderer(magTableModel, swingAppInterface);
        String sCellValue = "";
        Object oCellValue = this.getValueAt(iRow, iColumn);
        MagComboBoxField magLookupField = getMagTableModel().getColumnLookupField(iColumn);
        if (magLookupField != null) {
            sCellValue = magLookupField.getDisplay(oCellValue);
        } else {
            //sCellValue = mcr.getTableCellRendererComponent(this, oCellValue, false, false, iRow, iColumn).toString();
            Component component = mcr.getTableCellRendererComponent(this, oCellValue, false, false, iRow, iColumn);
            if (component.getClass() == javax.swing.JCheckBox.class) {
                sCellValue = ((javax.swing.JCheckBox) component).isSelected() ? "Igen" : "Nem";
                sCellValue = "false";
            } else if (component.getClass() == javax.swing.JButton.class) {
                sCellValue = " " + ((javax.swing.JButton) component).getText() + " ";
            } else {
                sCellValue = component.toString();
                //sCellValue = component.toString().trim(); //MaG 2017.11.09.
            }
            //System.out.println(sCellValue);

            if (oCellValue != null) {
                if (oCellValue.getClass() == Boolean.class) {
                    sCellValue = ((Boolean) oCellValue).booleanValue() ? "Igen" : "Nem";
                    //sCellValue = "false";
                }
            }
        }
        return (sCellValue);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isEnabled()) {
            return;
        }
        if (e.getActionCommand().equals("action_new")) {
            editingCanceled(new ChangeEvent(this));
            if (isRowChanged(this.getSelectedRow())) {
                return;
            }
            //@todo error: failed when filtered
            this.getMagTableModel().addEmptyRow();
            fireMagTableEventNewRowAdded(this.getRowCount() - 1);

            this.setRowSelectionInterval(this.getRowCount() - 1, this.getRowCount() - 1);

            int iCol = magTableModel.getFirstEditableColumnInRow(this.getRowCount() - 1);
            if (iCol < 0) {
                iCol = 0;
            }
            this.setColumnSelectionInterval(iCol, iCol);
            storeOriginRowValues(this.getRowCount() - 1);

            fireMagTableEventRowColChanged(this.getRowCount() - 1, this.convertColumnIndexToView(iCol));
            this.requestFocusInWindow(); //MaG 2017.10.08.
        }
        if (e.getActionCommand().equals("action_details")) {
            editingStopped(new ChangeEvent(this));
            MagTableDetail mtd = new MagTableDetail(swingAppInterface, this);
            String sPrimaryKey = getPrimaryKeyFieldValuesForMessage();
            if (!sPrimaryKey.equalsIgnoreCase("")) {
                sPrimaryKey = " (" + sPrimaryKey + ")";
            }
            String sTitle = sPrimaryKey;
            if (getMagTableModel().getTableInfo() != null) {
                sTitle = getMagTableModel().getTableInfo().getDisplayName() + sPrimaryKey;
            }
            int iRetVal = mtd.showDialog(parentFrame, sTitle);
            if (iRetVal == MagTableDetail.OK) {
                Vector<Component> vFields = mtd.getFields();
                for (int i = 0; i < vFields.size(); i++) {
                    if (vFields.elementAt(i) instanceof MagTextField) {
                        this.setValueAt(((MagTextField) vFields.elementAt(i)).getValue(), this.getSelectedRow(), i);
                    }
                    if (vFields.elementAt(i) instanceof MagComboBoxField) {
                        this.setValueAt(((MagComboBoxField) vFields.elementAt(i)).getValue(), this.getSelectedRow(), i);
                    }
                }
                if (isRowChanged(this.getSelectedRow())) {
                    int iRowStatus = magTableModel.getRowStatus(convertRowIndexToModel(getSelectedRow()));
                    if (iRowStatus == MagTableModel.ROW_STATUS_NEW) {
                        magTableModel.setRowStatus(convertRowIndexToModel(getSelectedRow()), MagTableModel.ROW_STATUS_NEW_MODIFIED);
                    }
                    if (iRowStatus == MagTableModel.ROW_STATUS_OK) {
                        magTableModel.setRowStatus(convertRowIndexToModel(getSelectedRow()), MagTableModel.ROW_STATUS_MODIFIED);
                    }
                    //fireMagTableEventRowStatusChanged(this.getSelectedRow());
                    if (!saveRecord(this.getSelectedRow())) {
                        //@todo idea: may display again the details?
                    }
                    //fireMagTableEventRowStatusChanged(this.getSelectedRow());
                    fireMagTableEventRowColChanged(this.getSelectedRow(), -1);
                }
            }
        }
        if (e.getActionCommand().equals("action_save")) {
            editingStopped(new ChangeEvent(this));
            if (saveRecord(this.getSelectedRow())) {
            }
        }
        if (e.getActionCommand().equals("action_delete")) {
            editingCanceled(new ChangeEvent(this));
            if (deleteRecord(this.getSelectedRow())) {
            }
        }
        if (e.getActionCommand().equals("action_excel")) {
            editingCanceled(new ChangeEvent(this));
        }
        if (e.getActionCommand().equals("action_developer_info")) {
            editingCanceled(new ChangeEvent(this));
            if (magTableInterface != null) {
                magTableInterface.requestForInfo();
            }
        }
    }

    public void setParentFrame(Frame frame) {
        parentFrame = frame;
    }

    public void setjScrollPane(JScrollPane jsp) {
        jScrollPane = jsp;
        jScrollPane.addComponentListener(this);
        jScrollPane.getViewport().addChangeListener(this);
    }

    private void restoreOriginRowValues(int iRow) {
        for (int i = 0; i < getColumnCount(); i++) {
            this.setValueAt(vOrigin.elementAt(i), iRow, i);
        }
    }

    private void storeOriginRowValues(int iRow) {
        vOrigin = new Vector();
        for (int i = 0; i < getColumnCount(); i++) {
            vOrigin.add(this.getValueAt(iRow, i));
        }
    }

    public void setRowCol(int iRow, int iCol) {
        setRowSelectionInterval(iRow, iRow);
        setColumnSelectionInterval(iCol, iCol);
        storeOriginRowValues(iRow);
    }

    public void setAddNewRowAtTheEnd(boolean b) {
        bAddNewRowAtTheEnd = b;
    }

    public boolean getAddNewRowAtTheEnd() {
        return (bAddNewRowAtTheEnd);
    }

    public String saveTableContentToXML(JTable jTable) {
        return (saveTableContentToXML(jTable, "ISO-8859-2"));
    }

    public String saveTableContentToXML(JTable jTable, String sCharSetName) {
        String sXML = "";
        sXML += "<?xml version='1.0' encoding='ISO-8859-2'?>";
        sXML += "<table name='" + magTableModel.getTableName() + "'>";
        sXML += "<columns>";
        for (int iColumn = 0; iColumn < magTableModel.getColumnCount(); iColumn++) {
            sXML += "<column name='" + magTableModel.getOriginColumnName(iColumn) + "' displayname='" + magTableModel.getColumnName(iColumn) + "' classname='" + magTableModel.getColumnClass(iColumn).getName() + "'>";
            sXML += "</column>";
        }
        sXML += "</columns>";
        sXML += "<records>";
        for (int iRow = 0; iRow < magTableModel.getRowCount(); iRow++) {
            sXML += "<record>";
            for (int iColumn = 0; iColumn < magTableModel.getColumnCount(); iColumn++) {
                sXML += "<column name='" + magTableModel.getOriginColumnName(iColumn) + "'>";
                sXML += convertObjectValueToString(getValueAt(iRow, iColumn), false);
                sXML += "</column>";
            }
            sXML += "</record>";
        }
        sXML += "</records>";
        sXML += "</table>";
        if (sCharSetName.equalsIgnoreCase("ISO-8859-2")) {
            return (sXML);
        }
        try {
            String s = new String(sXML.getBytes("ISO-8859-2"), sCharSetName);
            return (s);
        } catch (UnsupportedEncodingException uee) {
            swingAppInterface.handleError(uee);
        }
        return (sXML);
    }

    public void formatChanged() {
        MagCellEditor mce;
        for (int iCol = 0; iCol < this.getColumnCount(); iCol++) {
            Object o = this.getCellEditor(0, iCol);
            if (o != null) {
                if (o instanceof MagCellEditor) {
                    mce = (MagCellEditor) o;
                    mce.formatChanged();
                }
            }
        }
        this.repaint();
    }

    public void setPopupEnabled(boolean b) {
        this.bPopupEnabled = b;
    }

    public void removeMagTableEventListener(MagTableEventListener mtel) {
        for (int i = 0; i < vMagTableEventListeners.size(); i++) {
            if (vMagTableEventListeners.elementAt(i).equals(mtel)) {
                vMagTableEventListeners.remove(i);
                return;
            }
        }
    }

    public void addMagTableEventListener(MagTableEventListener mtel) {
        for (int i = 0; i < vMagTableEventListeners.size(); i++) {
            if (vMagTableEventListeners.elementAt(i).equals(mtel)) {
                return;
            }
        }
        vMagTableEventListeners.add(mtel);
    }

    public void setModel(MagTableModel magTableModel) {
        this.magTableModel = magTableModel;
        super.setModel(magTableModel);
    }

    public int getRowStatus(int iRow) {
        return (getMagTableModel().getRowStatus(convertRowIndexToModel(iRow)));
    }

    public int getSelectedRowStatus() {
        return (getMagTableModel().getRowStatus(convertRowIndexToModel(getSelectedRow())));
    }

    public void setNewHeaderUI() {
        //display:
        for (int i = 0; i < getTableHeader().getColumnModel().getColumnCount(); i++) {
            getTableHeader().getColumnModel().getColumn(i).setHeaderRenderer(new MagHeaderCellRenderer());
        }
        //behaviour:
        getTableHeader().setUI(new MagBasicTableHeaderUI(getTableHeader()));
    }

    public int getColumnIndexByName(String sName) {
        for (int iModelColumn = 0; iModelColumn < magTableModel.getColumnCount(); iModelColumn++) {
            if (magTableModel.getColumnName(iModelColumn).equals(sName)) {
                //return (iModelColumn);
                return (convertColumnIndexToView(iModelColumn));
            }
        }
        return (-1);
    }

    public void setNoFocusBorder(boolean b) {
        for (int i = 0; i < getColumnModel().getColumnCount(); i++) {
            ((MagCellRenderer) getColumnModel().getColumn(i).getCellRenderer()).setNoFocusBorder(b);
        }
    }

    public void setMagColorScheme(MagColorScheme magColorScheme) {
        for (int i = 0; i < getColumnModel().getColumnCount(); i++) {
            ((MagCellRenderer) getColumnModel().getColumn(i).getCellRenderer()).setMagColorScheme(magColorScheme);
        }
    }

    public int findFirst(String sColumnName, Object oValue) {
        return (findFirst(getColumnIndexByName(sColumnName), oValue));
    }

    public int findFirst(int iColumn, Object oValue) {
        Class c = oValue.getClass();
        for (int iRow = 0; iRow < this.getRowCount(); iRow++) {
            if (c.equals(java.lang.Integer.class)) {
                if (this.getIntValueAt(iRow, iColumn) == ((Integer) oValue).intValue()) {
                    return (iRow);
                }
            }
            if (c.equals(java.lang.Long.class)) {
                if (this.getLongValueAt(iRow, iColumn) == ((Long) oValue).longValue()) {
                    return (iRow);
                }
            }
        }
        return (-1);
    }

    public void setAutoResizeWhenScrollPaneResizes(boolean b) {
        bSetAutoResizeWhenScrollPaneResizes = b;
    }

    public void setNoResize(boolean b) {
        bNoResize = b;
    }

    public void stopEdit() {
        editingStopped(new ChangeEvent(this));
    }

}
