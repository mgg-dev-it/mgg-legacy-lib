package hu.mag.swing.table;

//@todo task : "select buttonpanel": all / none /revert 
//@todo task: rename "Filter" to "GUI Filter" or "Typed Filter" or something else, because Filter itself means table based filter (in xml, and in where ...)
import hu.mag.db.DatabaseInfo;
import hu.mag.db.TableInfo;
import hu.mag.swing.MagButton;
import hu.mag.swing.MagButtonPanel;
import hu.mag.swing.MagTextField;
import hu.mgx.app.common.LanguageEvent;
import hu.mgx.app.common.LanguageEventListener;
import hu.mgx.app.common.LoggerInterface;
import hu.mgx.app.swing.AppUtils;
import hu.mgx.app.swing.SwingAppInterface;
import hu.mgx.swing.CommonPanel;
import hu.mgx.swing.table.MemoryTable;
import hu.mgx.util.DateTimeUtils;
import hu.mgx.util.StringUtils;
import hu.mgx.xml.DefaultXMLHandler;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

public class MagTablePanel extends CommonPanel implements MagTableInterface, MagTableEventListener, ActionListener, RowSorterListener, ChangeListener, KeyListener, LanguageEventListener {

    private SwingAppInterface swingAppInterface;
    private MagTableModel magTableModel;
    //private TableRowSorter<MagTableModel> sorter;
    private TableRowSorter sorter;
    private MagTable magTable;

    private DatabaseInfo databaseInfo = null;

    private CommonPanel cpTable = null;
    private JScrollPane jspTable = null;
    private JViewport jvTable = null;

    private CommonPanel cpInfo = null;
    private JScrollPane jspInfo = null;
    private String sConnectionName = null;
    private String sTableName = null;
    private String sOrderBy = null;
    private Vector vData = null;
    private Vector vColumnNames = null;
    private ResultSet rs = null;

    private String[] filters = null;
    //protected HashMap<String, String> filtersMap;

    private JPopupMenu popup;
    private MouseListener popupListener = null;
    private JTree jTree = null;

    private CommonPanel cpUpper = null;
    private JLabel lblFilter = null;
    private MagTextField mtfFilter = null;
    private MagRowFilter<MagTableModel, Object> mrf = null;

    private CommonPanel cpLower = null;
    private JLabel lblStatus = null;
    private int iMaxColumnWidth = 0;

    private Vector<MagTablePanel> vSynchronizedTablePanels = new Vector<MagTablePanel>();
    private boolean bSynchronized = true;

    private DefaultXMLHandler defaultXMLHandler = null;

    private MagButtonPanel magButtonPanel = null;
    //private JScrollPane jspButton = null;

    private int iModifier = 0;

    private hu.mag.excel.Excel excel = null;

//    private Component parentComponent = null;
    private Frame parentFrame = null;

    private Set<Integer> setEnabledRows = null;
    private boolean bEnabledRowsExclude = false;
    private boolean bReadOnly = false;
    //
    //private javax.swing.Timer timerClock = null;
    private java.util.Date startTime = null;
    private java.util.Date stopTime = null;

    public MagTablePanel(SwingAppInterface swingAppInterface, String sConnectionName, String sTableName) {
        this(swingAppInterface, sConnectionName, sTableName, "", Event.CTRL_MASK);
    }

    public MagTablePanel(SwingAppInterface swingAppInterface, String sConnectionName, String sTableName, String sOrderBy) {
        this(swingAppInterface, sConnectionName, sTableName, sOrderBy, Event.CTRL_MASK);
    }

    public MagTablePanel(SwingAppInterface swingAppInterface, String sConnectionName, String sTableName, String sOrderBy, int iModifier) {
        super();
        this.swingAppInterface = swingAppInterface;
        this.sConnectionName = sConnectionName;
        this.sTableName = sTableName;
        this.sOrderBy = sOrderBy;
        this.filters = null;
        this.vData = null;
        this.vColumnNames = null;
        this.rs = null;
        this.iModifier = iModifier;
        init();
    }

    public MagTablePanel(SwingAppInterface swingAppInterface, String sConnectionName, String sTableName, String[] filters) {
        this(swingAppInterface, sConnectionName, sTableName, filters, Event.CTRL_MASK);
    }

    public MagTablePanel(SwingAppInterface swingAppInterface, String sConnectionName, String sTableName, String[] filters, int iModifier) {
        super();
        this.swingAppInterface = swingAppInterface;
        this.sConnectionName = sConnectionName;
        this.sTableName = sTableName;
        this.sOrderBy = "";
        this.filters = filters;
        //filtersMap = CommonAppUtils.preProcessArgs(filters);
        this.vData = null;
        this.vColumnNames = null;
        this.rs = null;
        this.iModifier = iModifier;
        init();
    }

    public MagTablePanel(SwingAppInterface swingAppInterface, String sConnectionName, Vector vData, Vector vColumnNames) {
        this(swingAppInterface, sConnectionName, vData, vColumnNames, Event.CTRL_MASK);
    }

    public MagTablePanel(SwingAppInterface swingAppInterface, String sConnectionName, Vector vData, Vector vColumnNames, int iModifier) {
        super();
        this.swingAppInterface = swingAppInterface;
        this.sConnectionName = sConnectionName;
        this.sTableName = "";
        this.sOrderBy = "";
        this.filters = null;
        this.vData = vData;
        this.vColumnNames = vColumnNames;
        this.rs = null;
        this.iModifier = iModifier;
        init();
    }

    public MagTablePanel(SwingAppInterface swingAppInterface, String sConnectionName, MemoryTable mt) {
        super();
        this.swingAppInterface = swingAppInterface;
        this.sConnectionName = sConnectionName;
        this.sTableName = "";
        this.sOrderBy = "";
        this.filters = null;
        this.vData = mt.getDataVector();
        this.vColumnNames = mt.getColumnNames();
        this.rs = null;
        init();
    }

    public MagTablePanel(SwingAppInterface swingAppInterface, ResultSet rs) {
        super();
        this.swingAppInterface = swingAppInterface;
        this.sConnectionName = "";
        this.sTableName = "";
        this.sOrderBy = "";
        this.filters = null;
        this.vData = null;
        this.vColumnNames = null;
        this.rs = rs;
        init();
    }

    public MagTablePanel(SwingAppInterface swingAppInterface) {
        super();
        Vector<String> vRecord = new Vector();
        Vector<Vector<String>> vData = new Vector();
        Vector<String> vColumnNames = new Vector();
        vColumnNames.add(" ");
        vRecord.add(" ");
        vData.add(vRecord);
        this.swingAppInterface = swingAppInterface;
        this.sConnectionName = "";
        this.sTableName = "";
        this.sOrderBy = "";
        this.filters = null;
        this.vData = vData;
        this.vColumnNames = vColumnNames;
        this.rs = null;
        init();
    }

//    public void initXML(String sXMLConfig, String sEncoding) {
//        String sName = "";
//        String sDisplayName = "";
//        String sValue = "";
//        Vector vXMLData = new Vector();
//        Vector vXMLRow = new Vector<String>();
//        Vector vXMLColumnNames = new Vector<String>();
//
//        defaultXMLHandler = new DefaultXMLHandler(swingAppInterface);
//        defaultXMLHandler.readXMLString("config", sXMLConfig, sEncoding);
//
//        XMLElement xmlTable = defaultXMLHandler.getFirstElement("config", "table");
//        Vector<XMLElement> vColumns = defaultXMLHandler.getElements(xmlTable, "columns", false);
//        for (int i = 0; i < vColumns.size(); i++) {
//            Vector<XMLElement> vColumn = defaultXMLHandler.getElements(vColumns.elementAt(i), "column", false);
//            for (int j = 0; j < vColumn.size(); j++) {
//                sName = vColumn.elementAt(j).getAttribute("name");
//                sDisplayName = vColumn.elementAt(j).getAttribute("displayname");
//                vXMLColumnNames.add(sName);
//            }
//        }
//        Vector<XMLElement> vRecords = defaultXMLHandler.getElements(xmlTable, "records", false);
//        for (int i = 0; i < vRecords.size(); i++) {
//            Vector<XMLElement> vRecord = defaultXMLHandler.getElements(vRecords.elementAt(i), "record", false);
//            for (int j = 0; j < vRecord.size(); j++) {
//                Vector<XMLElement> vColumn = defaultXMLHandler.getElements(vRecord.elementAt(j), "column", false);
//                for (int k = 0; k < vColumn.size(); k++) {
//                    sName = vColumn.elementAt(j).getAttribute("name");
//                    sValue = vColumn.elementAt(j).getText();
//                }
//            }
//        }
//    }
    private void init() {
        excel = new hu.mag.excel.Excel(this, swingAppInterface);
        this.setBorder(new EmptyBorder(2, 2, 2, 2));
        this.setInsets(0, 0, 0, 0);

        String sXMLConfig = "<?xml version='1.0' encoding='ISO-8859-2'?>";
        sXMLConfig += "<!-- Comment -->";
        sXMLConfig += "<app name='app' major='0' minor='0' revision='0' width='800' height='600'>";
        sXMLConfig += "    <language>";
        sXMLConfig += "        <languageitem key='MagTablePanel Filter'>";
        sXMLConfig += "            <translation lang='hu'>Szûrés</translation>";
        sXMLConfig += "            <translation lang='en'>Filter</translation>";
        sXMLConfig += "            <translation lang='de'>Filter</translation>";
        sXMLConfig += "        </languageitem>";
        sXMLConfig += "    </language>";
        sXMLConfig += "</app>";
        swingAppInterface.addLanguageXML(sXMLConfig, "ISO-8859-2");

        lblStatus = AppUtils.createLabel(" ");

        cpUpper = new CommonPanel();
        cpUpper.setBorder(new EmptyBorder(0, 0, 0, 0));
        cpUpper.setInsets(0, 0, 0, 0);

        lblFilter = new JLabel(swingAppInterface.getLanguageString("MagTablePanel Filter") + ": ");
        cpUpper.addToCurrentRow(lblFilter, 1, 1, 0, 0);

        mtfFilter = new MagTextField(swingAppInterface);
        mtfFilter.setMaxLength(999);
        mtfFilter.setValue(new String(""));
        mtfFilter.addKeyListener(this);
        cpUpper.addToCurrentRow(mtfFilter, 1, 1, 0, 1.0);

        this.addToGrid(cpUpper, 0, 0, 1, 1, 0, 0, GridBagConstraints.BOTH);

        cpTable = new CommonPanel();
        cpTable.setBorder(new EmptyBorder(0, 0, 0, 0));
        cpTable.setInsets(0, 0, 0, 0);

        databaseInfo = swingAppInterface.getDatabaseInfo(sConnectionName);
        if (!sTableName.equalsIgnoreCase("")) {
            if (filters == null) {
                magTableModel = new MagTableModel(swingAppInterface, sConnectionName, sTableName, sOrderBy, databaseInfo);
            } else {
                magTableModel = new MagTableModel(swingAppInterface, sConnectionName, sTableName, databaseInfo, filters);
            }
        }
//        if (!sTableName.equalsIgnoreCase("")) {
//            magTableModel = new MagTableModel(swingAppInterface, sConnectionName, sTableName, databaseInfo);
//        }
        if (vData != null && vColumnNames != null) {
            magTableModel = new MagTableModel(vData, vColumnNames);
        }
        if (rs != null) {
            magTableModel = new MagTableModel(rs, swingAppInterface, null);
        }
        sorter = new TableRowSorter<MagTableModel>(magTableModel);
        sorter.addRowSorterListener(this);

        for (int i = 0; i < magTableModel.getColumnCount(); i++) {
            if (magTableModel.isLookupColumn(i)) {
                sorter.setComparator(i, new MagLookupComparator((magTableModel.getColumnLookupField(i))));
            }
        }

        magTable = new MagTable(swingAppInterface, sConnectionName, magTableModel, this);
        magTable.setRowSorter(sorter);
        magTable.setAutoColumnWidth(true, iMaxColumnWidth);
        magTable.addMagTableEventListener(this);
        jspTable = new JScrollPane(magTable);
        magTable.addPopupListener(jspTable);
//        magTable.setParentComponent(this);
        magTable.setjScrollPane(jspTable);

        jvTable = jspTable.getViewport();
        jvTable.addChangeListener(this);

        cpTable.addToCurrentRow(jspTable, 1, 1, 1.0, 1.0, GridBagConstraints.BOTH);
        cpTable.nextRow();
        magButtonPanel = MagButtonPanel.createMagButtonPanelHorizontal(swingAppInterface, this, iModifier, MagButton.BUTTON_REFRESH, MagButton.BUTTON_NEW, MagButton.BUTTON_DETAILS, MagButton.BUTTON_SAVE, MagButton.BUTTON_DELETE, MagButton.BUTTON_EXCEL, MagButton.BUTTON_PDF_VIEW);
        magButtonPanel.setButtonEnabled(false, MagButton.BUTTON_REFRESH);
        cpTable.addToCurrentRow(magButtonPanel, 1, 1, 0.0, 1.0, GridBagConstraints.BOTH, GridBagConstraints.WEST);
        //jspButton = new JScrollPane(magButtonPanel);
        //cpTable.addToCurrentRow(jspButton, 1, 1, 0.01, 1.0, GridBagConstraints.BOTH, GridBagConstraints.WEST);

        this.addToGrid(cpTable, 1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.BOTH);

        DefaultMutableTreeNode top = new DefaultMutableTreeNode("Tableinfo");
        jTree = new JTree(top);
        jTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        jspInfo = new JScrollPane(jTree);
        cpInfo = new CommonPanel();
        cpInfo.setBorder(new EmptyBorder(0, 0, 0, 0));
        cpInfo.setInsets(0, 0, 0, 0);
        cpInfo.addToGrid(jspInfo, 0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.BOTH);
        this.addToGrid(cpInfo, 1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.BOTH);
        cpInfo.setVisible(false);

        cpLower = new CommonPanel();
        cpLower.setBorder(new EmptyBorder(0, 0, 0, 0));
        cpLower.setInsets(0, 0, 0, 0);

        cpLower.addToCurrentRow(lblStatus, 1, 1, 0, 1.0, GridBagConstraints.BOTH);

        this.addToGrid(cpLower, 2, 0, 1, 1, 0, 0, GridBagConstraints.BOTH);

        popup = new JPopupMenu();
        JMenuItem menuItem1 = new JMenuItem(swingAppInterface.getLanguageString("Vissza a táblához"));
        menuItem1.setActionCommand("action_back");
        menuItem1.addActionListener(this);
        popup.add(menuItem1);
        JMenuItem menuItem2 = new JMenuItem(swingAppInterface.getLanguageString("XML export"));
        menuItem2.setActionCommand("action_xml_export");
        menuItem2.addActionListener(this);
        popup.add(menuItem2);
        popupListener = new PopupListener(popup);
        cpInfo.addMouseListener(popupListener);
        jTree.addMouseListener(popupListener);

        setButtonStatuses();

        TableInfo ti = magTableModel.getTableInfo();
        if (ti != null) {
            if (ti.isExtendedScrollToLast()) {
                scrollToLast();
//                Rectangle cellRect = magTable.getCellRect(magTable.getRowCount() - 1, 0, false);
//                if (cellRect != null) {
//                    jvTable.scrollRectToVisible(cellRect);
//                }
//                magTable.setRowSelectionInterval(magTable.getRowCount() - 1, magTable.getRowCount() - 1);
            }
        }
        //timerClock = new javax.swing.Timer(1000, this);
        //timerClock.setRepeats(true);
        //timerClock.start();
        bSynchronized = true;
    }

    public void scrollToLast() {
        Rectangle cellRect = magTable.getCellRect(magTable.getRowCount() - 1, 0, false);
        if (cellRect != null) {
            jvTable.scrollRectToVisible(cellRect);
        }
        magTable.setRowSelectionInterval(magTable.getRowCount() - 1, magTable.getRowCount() - 1);
    }

    //RowSorterListener
    @Override
    public void sorterChanged(RowSorterEvent e) {
        //@todo task * : inform the table about the changing, to set the selected row to the right one
    }

    //ChangeListener
    @Override
    public void stateChanged(ChangeEvent e) {
        Rectangle viewRect = jvTable.getViewRect();
        if (magTable == null) {
            return;
        }
        int first = magTable.rowAtPoint(new Point(0, viewRect.y));
        if (first == -1) {
            return; // Table is empty
        }
        int last = magTable.rowAtPoint(new Point(0, viewRect.y + viewRect.height - 1));
        if (last == -1) {
            last = magTableModel.getRowCount() - 1; // Handle empty space below last row
        }
        //swingAppInterface.logLine("stateChanged first row=" + Integer.toString(first) + " last row=" + Integer.toString(last), LoggerInterface.LOG_DEBUG);
        if (bSynchronized) {
            if (vSynchronizedTablePanels.size() > 0) {
                for (int i = 0; i < vSynchronizedTablePanels.size(); i++) {
                    if (vSynchronizedTablePanels.elementAt(i) != null) {
                        if (vSynchronizedTablePanels.elementAt(i).getMagTable() != null) {
                            vSynchronizedTablePanels.elementAt(i).getMagTable().scrollRectToVisible(viewRect);
                        }
                    }
                }
            }
        }
    }

    public void removeSynchronizedTablePanel(MagTablePanel mtp) {
        for (int i = 0; i < vSynchronizedTablePanels.size(); i++) {
            if (vSynchronizedTablePanels.elementAt(i).equals(mtp)) {
                vSynchronizedTablePanels.remove(i);
                mtp.removeSynchronizedTablePanel(this);
            }
        }
    }

    public void addSynchronizedTablePanel(MagTablePanel mtp) {
        for (int i = 0; i < vSynchronizedTablePanels.size(); i++) {
            if (vSynchronizedTablePanels.elementAt(i).equals(mtp)) {
                return;
            }
        }
        vSynchronizedTablePanels.add(mtp);
        mtp.addSynchronizedTablePanel(this);
    }

    //KeyListener
    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getSource() == mtfFilter) {
            doFilter(null);
        }
    }

    //KeyListener
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getSource() == mtfFilter) {
            doFilter(null);
        }
    }

    //KeyListener
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == mtfFilter) {
            doFilter(null);
        }
    }

    //LanguageEventListener
    @Override
    public void languageEventPerformed(LanguageEvent e) {
        magButtonPanel.languageEventPerformed(e);
        lblFilter.setText(swingAppInterface.getLanguageString("MagTablePanel Filter") + ": ");
        magTable.languageEventPerformed(e);
    }

    //MagTableEventListener
    @Override
    public void tableEventPerformed(MagTableEvent e) {
        if (e.getEventID() == MagTableEvent.ROW_COL_CHANGED) {
            setButtonStatuses();
        }
        if (e.getEventID() == MagTableEvent.ROW_STATUS_CHANGED) {
            setButtonStatuses();
        }
        if (e.getEventID() == MagTableEvent.NEW_ROW_ADDED) {
            doFilter(null);
        }
    }

    private void setButtonStatuses() {
        int iRowStatus = MagTableModel.ROW_STATUS_UNKNOWN;

        if (magTable.getRowCount() > 0) {
            if (magTable.getSelectedRow() > -1) {
                iRowStatus = magTable.getSelectedRowStatus();
            }
        }
        magButtonPanel.setButtonEnabled(iRowStatus != MagTableModel.ROW_STATUS_UNKNOWN && (iRowStatus == MagTableModel.ROW_STATUS_MODIFIED || iRowStatus == MagTableModel.ROW_STATUS_NEW_MODIFIED), MagButton.BUTTON_SAVE);

        boolean bDeletable = true;
        if (magTableModel.getTableInfo() != null) {
            if (magTableModel.getTableInfo().getTableInfoPlus() != null) {
                Boolean b = magTableModel.getTableInfo().getTableInfoPlus().isRowDeletable(magTable.convertRowIndexToModel(magTable.getSelectedRow()));
                if (b != null) {
                    if (!b.booleanValue()) {
                        bDeletable = false;
                    }
                }
            }
        }
        //MaG 2017.10.31.
        if (!magTableModel.isUpdateableTable() || magTableModel.isReadOnlyTable() || magTable.getRowCount() < 1) {
            bDeletable = false;
        }
        //magButtonPanel.setButtonEnabled(iRowStatus != MagTableModel.ROW_STATUS_UNKNOWN && iRowStatus != MagTableModel.ROW_STATUS_MODIFIED && iRowStatus != MagTableModel.ROW_STATUS_NEW_MODIFIED && iRowStatus != MagTableModel.ROW_STATUS_NEW, MagButton.BUTTON_DELETE);
        magButtonPanel.setButtonEnabled(bDeletable && iRowStatus != MagTableModel.ROW_STATUS_UNKNOWN && iRowStatus != MagTableModel.ROW_STATUS_MODIFIED && iRowStatus != MagTableModel.ROW_STATUS_NEW, MagButton.BUTTON_DELETE);

        magButtonPanel.setButtonEnabled(iRowStatus != MagTableModel.ROW_STATUS_UNKNOWN, MagButton.BUTTON_DETAILS);
    }

    class PopupListener extends MouseAdapter {

        JPopupMenu popup;
        MagTable magTable;
        MagTableModel magTableModel;

        PopupListener(JPopupMenu popupMenu) {
            popup = popupMenu;
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
            if (e.isPopupTrigger()) {
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    //MagTableInterface
    @Override
    public void rowChanged(int iPreviousRow, int iNextRow) {
        //app.logLine("MagTableInterface rowChanged (" + Integer.toString(iPreviousRow) + " -> " + Integer.toString(iNextRow) + ")", LoggerInterface.LOG_DEBUG);
    }

    //MagTableInterface
    @Override
    public boolean beforeRowChange(int iPreviousRow, int iNextRow) {
        //app.logLine("MagTableInterface beforeRowChange (" + Integer.toString(iPreviousRow) + " -> " + Integer.toString(iNextRow) + ")", LoggerInterface.LOG_DEBUG);
        return (true);
    }

    //MagTableInterface
    @Override
    public boolean tabPressedInLastCell(int iPreviousRow, int iNextRow) {
        //app.logLine("tabPressedInLastCell (" + Integer.toString(iPreviousRow) + " -> " + Integer.toString(iNextRow) + ")", LoggerInterface.LOG_DEBUG);
        return (true);
    }

    //MagTableInterface
    @Override
    public void requestForInfo() {
        cpTable.setVisible(false);
        cpInfo.setVisible(true);
    }

    //MagTableInterface
    @Override
    public void status(String sStatus) {
        lblStatus.setText(sStatus);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("action_back")) {
            cpInfo.setVisible(false);
            cpTable.setVisible(true);
        }
        if (e.getActionCommand().equals("action_xml_export")) {
            StringSelection data = new StringSelection(magTable.saveTableContentToXML(magTable));
            this.getToolkit().getSystemClipboard().setContents(data, data);
            AppUtils.messageBox(this, "Export végrehajtva, eredménye a vágólapon található.");
        }
        if (e.getSource() != null) {
            if (e.getActionCommand().equals("action_new")) {
                magTable.actionPerformed(new ActionEvent(this, 0, "action_new"));
                //MaG 2018.10.15.
                if (magTable.getRowCount()==1) {
                    magTable.setAutoColumnWidth();
                }
                magTable.setFirstEditableCell();
                magTable.requestFocusInWindow();
                Rectangle cellRect = magTable.getCellRect(magTable.getSelectionModel().getLeadSelectionIndex(), magTable.getColumnModel().getSelectionModel().getLeadSelectionIndex(), false);
                if (cellRect != null) {
                    jvTable.scrollRectToVisible(cellRect);
                }
            }
            if (e.getActionCommand().equals("action_save")) {
                magTable.actionPerformed(new ActionEvent(this, 0, "action_save"));
            }
            if (e.getActionCommand().equals("action_details")) {
                magTable.actionPerformed(new ActionEvent(this, 0, "action_details"));
            }
            if (e.getActionCommand().equals("action_delete")) {
                magTable.actionPerformed(new ActionEvent(this, 0, "action_delete"));
                setButtonStatuses(); //MaG 2017.12.06.
            }
            if (e.getActionCommand().equals("action_pdf_view")) {
                String[] args = StringUtils.createArrayFromElements();
                swingAppInterface.viewPdf(new MagTableReport(swingAppInterface, args).getReport(this.magTable));
            }
            if (e.getActionCommand().equals("action_pdf_print")) {
                String[] args = StringUtils.createArrayFromElements();
                swingAppInterface.viewAndPrintPdf(new MagTableReport(swingAppInterface, args).getReport(this.magTable));
            }
            if (e.getActionCommand().equals("action_excel")) {
                this.excelExport();
                //magTable.actionPerformed(new ActionEvent(this, 0, "action_excel"));
                //excel.exportToExcelWithFileDialog(swingAppInterface, magTable);
            }
        }
    }

    public void excelExport() {
        magTable.actionPerformed(new ActionEvent(this, 0, "action_excel"));
        excel.exportToExcelWithFileDialog(swingAppInterface, magTable);
    }

    private void doFilter(MagTablePanel mtp) {
        if (mtp != null) {
            if (mtp.equals(this)) {
                return;
            }
        }

        boolean bNegative = false;
        String sFilter = mtfFilter.getText();
        if (StringUtils.startsWith(mtfFilter.getText(), "-")) {
            sFilter = StringUtils.mid(sFilter, 1);
            bNegative = true;
        }
        sFilter = StringUtils.convertFilterStringToRegexString(sFilter);
        //System.out.println("Filter=" + sFilter + (bNegative ? " (negative)" : ""));
        try {
            if (setEnabledRows == null) {
                mrf = MagRowFilter.createMagRowFilter(sFilter, magTableModel, null, false, bNegative);
            } else {
                mrf = MagRowFilter.createMagRowFilter(sFilter, magTableModel, setEnabledRows, bEnabledRowsExclude, bNegative);
            }
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(mrf);
        magTable.showRowcountStatus();

        if (vSynchronizedTablePanels.size() > 0) {
            for (int i = 0; i < vSynchronizedTablePanels.size(); i++) {
                vSynchronizedTablePanels.elementAt(i).setFilter(mtfFilter.getText(), (mtp != null ? mtp : this));
            }
        }
    }

    public void setEnabledRows(Set<Integer> setRows) {
        setEnabledRows(setRows, false);
    }

    public void setEnabledRows(Set<Integer> setRows, boolean bExclude) {
        setEnabledRows = setRows;
        bEnabledRowsExclude = bExclude;
        boolean bNegative = false;
        String sFilter = mtfFilter.getText();
        if (StringUtils.startsWith(mtfFilter.getText(), "-")) {
            sFilter = StringUtils.mid(sFilter, 1);
            bNegative = true;
        }
        sFilter = StringUtils.convertFilterStringToRegexString(sFilter);
        try {
            mrf = MagRowFilter.createMagRowFilter(sFilter, magTableModel, setRows, bExclude, bNegative);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(mrf);
        magTable.showRowcountStatus();

        if (vSynchronizedTablePanels.size() > 0) {
            for (int i = 0; i < vSynchronizedTablePanels.size(); i++) {
                vSynchronizedTablePanels.elementAt(i).setFilter(mtfFilter.getText(), this);
            }
        }
    }

    public void setEnabledRowsAll() {
        setEnabledRows = null;
        bEnabledRowsExclude = false;
        boolean bNegative = false;
        String sFilter = mtfFilter.getText();
        if (StringUtils.startsWith(mtfFilter.getText(), "-")) {
            sFilter = StringUtils.mid(sFilter, 1);
            bNegative = true;
        }
        sFilter = StringUtils.convertFilterStringToRegexString(sFilter);
        try {
            mrf = MagRowFilter.createMagRowFilter(sFilter, magTableModel, null, false, bNegative);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(mrf);
        magTable.showRowcountStatus();

        if (vSynchronizedTablePanels.size() > 0) {
            for (int i = 0; i < vSynchronizedTablePanels.size(); i++) {
                vSynchronizedTablePanels.elementAt(i).setFilter(mtfFilter.getText(), this);
            }
        }
    }

    public JScrollPane getJScrollPane() {
        return (jspTable);
    }

    public MagTable getMagTable() {
        return (magTable);
    }

    public MagTableModel getMagTableModel() {
        return (magTableModel);
    }

    public void setMaxColumnWidth(int iMaxColumnWidth) {
        this.iMaxColumnWidth = iMaxColumnWidth;
        magTable.setAutoColumnWidth(true, iMaxColumnWidth);
    }

    public int getMaxColumnWidth() {
        return (iMaxColumnWidth);
    }

    public void setFilter(String sFilter, MagTablePanel mtp) {
        swingAppInterface.logLine("setFilter (" + sFilter + ") in " + (mtp == null ? "" : mtp.toString()), LoggerInterface.LOG_DEBUG);
        if (mtp != null) {
            if (mtp.equals(this)) {
                return;
            }
        }
        swingAppInterface.logLine("setFilter OK (" + sFilter + ") in " + (mtp == null ? "" : mtp.toString()), LoggerInterface.LOG_DEBUG);
        mtfFilter.setValue(sFilter);
        doFilter(mtp);
    }

    public void setFilters(String[] filters) {
        this.filters = filters;
    }

    public void refreshTableModel_old() {
        DatabaseInfo di = swingAppInterface.getDatabaseInfo(sConnectionName);
        if (!sTableName.equalsIgnoreCase("")) {
            magTableModel = new MagTableModel(swingAppInterface, sConnectionName, sTableName, di);
        }
        sorter = new TableRowSorter<MagTableModel>(magTableModel);
        sorter.addRowSorterListener(this);
        magTable.setModel(magTableModel);
        //magTable = new MagTable(swingAppInterface, sConnectionName, magTableModel, this);
        magTable.setRowSorter(sorter);
    }

    public void refreshTableModel() {
        DatabaseInfo di = swingAppInterface.getDatabaseInfo(sConnectionName);
        if (!sTableName.equalsIgnoreCase("")) {
            if (filters == null) {
                magTableModel = new MagTableModel(swingAppInterface, sConnectionName, sTableName, di);
            } else {
                magTableModel = new MagTableModel(swingAppInterface, sConnectionName, sTableName, di, filters);
            }
        }
        refreshCommon();
    }

    public void refresh(String sTableName) {
        magTableModel = new MagTableModel(swingAppInterface, sConnectionName, sTableName, databaseInfo);
        refreshCommon();
    }

    public void refresh(MemoryTable memoryTable) {
        magTableModel = new MagTableModel(memoryTable);
        refreshCommon();
    }

    //@todo task : it seems that refresh(Vector vData, Vector vColumnNames) works! Try to correct refresh(ResultSet rs) too!
    public void refresh(Vector vData, Vector vColumnNames) {
        magTableModel = new MagTableModel(vData, vColumnNames);
        refreshCommon();
    }

    public void refreshToEmpty() {
        Vector<String> vRecord = new Vector();
        Vector<Vector<String>> vData = new Vector();
        Vector<String> vColumnNames = new Vector();
        vColumnNames.add(" ");
        vRecord.add(" ");
        vData.add(vRecord);
        magTableModel = new MagTableModel(vData, vColumnNames);
        refreshCommon();
    }

    private void refreshCommon() {
        sorter = new TableRowSorter<MagTableModel>(magTableModel);
        sorter.addRowSorterListener(this);

        for (int i = 0; i < magTableModel.getColumnCount(); i++) {
            if (magTableModel.isLookupColumn(i)) {
                sorter.setComparator(i, new MagLookupComparator((magTableModel.getColumnLookupField(i))));
            }
        }
        //magTable=null; //MaG 2017.10.08.
        //magTable.removeMagTableEventListener(this); //MaG 2017.10.08.
        if (magTable != null) {
            magTable.removePopupListener(jspTable); //MaG 2017.10.08.
        }
        magTable = null; //MaG 2017.11.24.
        magTable = new MagTable(swingAppInterface, sConnectionName, magTableModel, this);
        magTable.setRowSorter(sorter);
        magTable.setAutoColumnWidth(true, iMaxColumnWidth);
        magTable.addMagTableEventListener(this);
        //jspTable = new JScrollPane(magTable);
        jspTable.setViewportView(magTable);
        magTable.addPopupListener(jspTable);
        magTable.setjScrollPane(jspTable);
        setButtonStatuses(); //MaG 2017.12.06.
    }

//    public void refresh(ResultSet rs) {
//        magTableModel.refresh(rs);
//        for (int i = 0; i < magTable.getColumnCount(); i++) {
//            magTable.getColumnModel().getColumn(i).setCellRenderer(new MagCellRenderer(magTableModel, swingAppInterface));
//        }
//        magTable.languageEventPerformed(new LanguageEvent(this, LanguageEvent.LANGUAGE_CHANGED, swingAppInterface.getLanguage()));
//    }
    public void refresh(ResultSet rs) {
        magTableModel = new MagTableModel(rs, swingAppInterface, databaseInfo);
        refreshCommon();
    }

    public void refreshSQL(String sConnectionName, String sSQL) {
        //refreshSQL(swingAppInterface.getConnection(sConnectionName), sSQL);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        startTimer();
        try {
            conn = swingAppInterface.getTemporaryConnection(sConnectionName);
            ps = conn.prepareStatement(sSQL);
            rs = ps.executeQuery();
            this.refresh(rs);
            rs.close();
            rs = null;
            ps.close();
            ps = null;
            conn.close();
            conn = null;
        } catch (SQLException sqle) {
            stopTimer();
            swingAppInterface.handleError(sqle);
        }
        stopTimer();
        magTable.showRowcountStatus(getFormattedElapsedTime());
    }

    public void refreshSQL(Connection connection, String sSQL) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        startTimer();
        try {
            ps = connection.prepareStatement(sSQL);
            rs = ps.executeQuery();
            this.refresh(rs);
            rs.close();
            rs = null;
            ps.close();
            ps = null;
        } catch (SQLException sqle) {
            stopTimer();
            swingAppInterface.handleError(sqle);
        }
        stopTimer();
        magTable.showRowcountStatus(getFormattedElapsedTime());
    }

    public void formatChanged() {
        magTable.formatChanged();
    }

    public void addPopupMenuItem(String sCaption, String sActionCommand, ActionListener actionListener) {
        addPopupMenuItem(sCaption, sActionCommand, actionListener, false);
    }

    public void addPopupMenuItem(String sCaption, String sActionCommand, ActionListener actionListener, boolean bForRecordsOnly) {
        magTable.addPopupMenuItem(sCaption, sActionCommand, actionListener, bForRecordsOnly);
    }

    public void setEnabled(boolean bEnabled) {
        magTable.setEnabled(bEnabled);
        magButtonPanel.setEnabled(bEnabled);
    }

    public MagButtonPanel getMagButtonPanel() {
        return (magButtonPanel);
    }

    public void setFilterVisible(boolean bVisible) {
        cpUpper.setVisible(bVisible);
    }

    public void setButtonsVisible(boolean bVisible) {
        magButtonPanel.setVisible(bVisible);
    }

    public void setStatusVisible(boolean bVisible) {
        cpLower.setVisible(bVisible);
    }
//    public void setParentComponent(Component c) {
//        parentComponent = c;
//        magTable.setParentComponent(c);
//    }

    public void setParentFrame(Frame frame) {
        parentFrame = frame;
        magTable.setParentFrame(frame);
    }

    public static MagTablePanel createEmptyMagTablePanel(SwingAppInterface swingAppInterface) {
        Vector vColumnNames = new Vector();
        vColumnNames.add(" ");
        return (createEmptyMagTablePanel(swingAppInterface, vColumnNames));
    }

    public static MagTablePanel createEmptyMagTablePanel(SwingAppInterface swingAppInterface, Vector vColumnNames) {
        Vector<Vector> vData = new Vector<>();
        Vector vRecord = new Vector();
        if (vColumnNames == null) {
            vColumnNames = new Vector();
        }
        for (int i = 0; i < vColumnNames.size(); i++) {
            vRecord.add("");
        }
        vData.add(vRecord);
        MagTablePanel magTablePanel = new MagTablePanel(swingAppInterface, "", vData, vColumnNames);
        magTablePanel.getMagButtonPanel().setEnabled(false);
        magTablePanel.getMagButtonPanel().setVisible(false);
        return (magTablePanel);
    }

    public static MagTablePanel createMagTablePanel(SwingAppInterface swingAppInterface, Vector vData, Vector vColumnNames) {
        MagTablePanel magTablePanel = new MagTablePanel(swingAppInterface, "", vData, vColumnNames);
        magTablePanel.getMagButtonPanel().setEnabled(false);
        magTablePanel.getMagButtonPanel().setVisible(false);
        return (magTablePanel);
    }

    public void checkAll(int iColumn) {
        for (int iRow = 0; iRow < getMagTable().getRowCount(); iRow++) {
            getMagTable().setValueAt(new Boolean(true), iRow, iColumn);
        }
    }

    public void checkReverse(int iColumn) {
        boolean b = false;
        Object o = null;
        for (int iRow = 0; iRow < getMagTable().getRowCount(); iRow++) {
            o = getMagTable().getValueAt(iRow, iColumn);
            if (o instanceof Boolean) {
                b = !((Boolean) o).booleanValue();
            }
            getMagTable().setValueAt(new Boolean(b), iRow, iColumn);
        }
    }

    public void checkNone(int iColumn) {
        for (int iRow = 0; iRow < getMagTable().getRowCount(); iRow++) {
            getMagTable().setValueAt(new Boolean(false), iRow, iColumn);
        }
    }

    public void adjustSizeToContent(Window windowContainer, int iMaximumWidth, int iMaximumHeight) {
        this.adjustSizeToContent(windowContainer, 100, 100, iMaximumWidth, iMaximumHeight);
    }

    public void adjustSizeToContent(Window windowContainer, int iMinimumWidth, int iMinimumHeight, int iMaximumWidth, int iMaximumHeight) {
        this.setPreferredSize(new Dimension(iMinimumWidth, iMinimumHeight)); //it's a trick to work the remaining code ...
        windowContainer.pack();
        int iScrollBarWidth = ((Integer) UIManager.get("ScrollBar.width")).intValue();
        int iWidth = this.getMagTable().getWidth() + iScrollBarWidth;
        iWidth += this.getBorder().getBorderInsets(this).left;
        iWidth += this.getBorder().getBorderInsets(this).right;
        iWidth += this.getJScrollPane().getBorder().getBorderInsets(this.getJScrollPane()).left;
        iWidth += this.getJScrollPane().getBorder().getBorderInsets(this.getJScrollPane()).right;
        iWidth = Math.min(iMaximumWidth, iWidth);
        iWidth = Math.max(iMinimumWidth, iWidth);
        int iHeight = this.getMagTable().getHeight() + iScrollBarWidth;
        iHeight += this.getMagTable().getTableHeader().getHeight();
        iHeight += this.getBorder().getBorderInsets(this).top;
        iHeight += this.getBorder().getBorderInsets(this).bottom;
        iHeight += this.getJScrollPane().getBorder().getBorderInsets(this.getJScrollPane()).top;
        iHeight += this.getJScrollPane().getBorder().getBorderInsets(this.getJScrollPane()).bottom;
        iHeight = Math.min(iMaximumHeight, iHeight);
        iHeight = Math.max(iMinimumHeight, iHeight);
        this.setPreferredSize(new Dimension(iWidth, iHeight));
        windowContainer.pack();
        if (!this.getJScrollPane().getVerticalScrollBar().isVisible()) {
            iWidth -= iScrollBarWidth;
            this.setPreferredSize(new Dimension(iWidth, iHeight));
            windowContainer.pack();
        }
        if (!this.getJScrollPane().getHorizontalScrollBar().isVisible()) {
            iHeight -= iScrollBarWidth;
            this.setPreferredSize(new Dimension(iWidth, iHeight));
            windowContainer.pack();
        }
    }

    public void setTitledBorder(String sTitle) {
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), sTitle));
    }

    public void setReadOnly(boolean b) {
        this.bReadOnly = b;
        magTableModel.setReadOnlyTable(b);
        //MaG 2017.12.06. magButtonPanel.setEnabledAll(!b);
        setButtonsVisible(!b);
    }

    private void startTimer() {
        startTime = new java.util.Date();
        //timerClock.start();
    }

    private void stopTimer() {
        stopTime = new java.util.Date();
        //timerClock.stop();
    }

    private String getFormattedElapsedTime() {
        String sRetVal = "";
        if (startTime != null && stopTime != null && stopTime.getTime() > startTime.getTime()) {
            long l = stopTime.getTime() - startTime.getTime();
            sRetVal = Long.toString(l);
            if (sRetVal.length() < 4) {
                sRetVal = StringUtils.right("0000" + Long.toString(l), 4);
            }
            sRetVal = "(Eltelt idõ: " + StringUtils.left(sRetVal, sRetVal.length() - 3) + "." + StringUtils.right(sRetVal, 3) + " mp)";
        }
        return (sRetVal);
    }

    public void setDateOnlyColumns() {
        //mtpVaria.getMagTableModel().setColumnSpecType(1, "date_only");
        //getMagTableModel().setColumnSpecType(1, "date_only");
        MagTable magTable = getMagTable();
        for (int iCol = 0; iCol < magTable.getColumnCount(); iCol++) {
            boolean bOk = true;
            for (int iRow = 0; bOk && iRow < magTable.getRowCount(); iRow++) {
                Object oValue = magTable.getValueAt(iRow, iCol);
                if (oValue != null) {
                    if (oValue.getClass() != java.util.Date.class) {
                        bOk = false;
                    }
                    if (bOk) {
                        java.util.Date d = (java.util.Date) oValue;
                        if (!DateTimeUtils.isDateOnly(d)) {
                            bOk = false;
                        }
                    }
                }
            }
            if (bOk) {
                getMagTableModel().setColumnSpecType(iCol, "date_only");
                //System.out.println("Date only " + Integer.toString(iCol));
            }
        }
    }

    public void setSynchronized(boolean b) {
        this.bSynchronized = b;
    }

    public boolean isSynchronized() {
        return (bSynchronized);
    }

}
