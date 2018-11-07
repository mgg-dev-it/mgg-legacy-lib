package hu.mag.swing;

import hu.mag.swing.table.MagRowFilter;
import hu.mag.swing.table.MagTable;
import hu.mag.swing.table.MagTableModel;
import hu.mgx.app.common.ColorManager;
import hu.mgx.app.swing.SwingAppInterface;
import hu.mgx.swing.CommonPanel;
import hu.mgx.swing.table.MemoryTable;
import hu.mgx.util.IntegerUtils;
import hu.mgx.util.StringUtils;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.UIManager;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;

/**
 * Special text field. User can only choose elements from a list or from a
 * table.
 *
 * @author MaG
 */
//public class MagLookupTextField extends JTextField implements MagFieldInterface, MagTableInterface {
public class MagLookupTextField extends JTextField implements MagFieldInterface {

    /**
     * Developer info.
     *
     * Keys: - any displayable key: displays the list/table and activates the
     * filtering - control keys: - Up Arrow, Down Arrow, Page Up, Page Down,
     * Ctrl-Home, Ctrl-End navigate in list/table - Tab, Shift-Tab, Enter:
     * choose the selected item and close the list/table
     *
     * Mouse: - one click: select an item in the list/table and closes the
     * list/table
     *
     */
    //@todo idea: when value is null, then those item is not selectable? OR above a certain index, items are not selectable? OR it needs an other vector of selectable stauses?
    //@todo task: prevent empty list caused problems ...
    //@todo task: set maximum width of a popup window so that it can not stretch/grow over the app frame
    //@todo task: test with extreme wide tables (many columns with long texts)
    //@todo task: list auto width for its content
    //@todo task: filtering list ...
    //@todo task: up/down arrows are not visible in case of a wide field ....
    private final SwingAppInterface swingAppInterface;
    private Object oOriginValue = null;
    private JScrollPane jScrollPane;
    private JList jList;
    private MagTable magTable;
    private MagTableModel magTableModel;
    private TableRowSorter sorter;
    private MagRowFilter<MagTableModel, Object> mrf = null;
    private JWindow jWindow;
    private CommonPanel commonPanel;
    private boolean bPopupIsShown;
    private int iVisibleRowCount;
    private Object oValue;
    private boolean bReadOnly = false;

    private Class c = null;
    private Vector<Object> vValue = new Vector<Object>();
    private Vector<Object> vDisplay = new Vector<Object>();
    //private HashMap<Object, Object> hashmap = new HashMap<Object, Object>();
    private HashMap<Object, Integer> hmIndex = new HashMap<Object, Integer>();
    private boolean bIsRefreshing = false;
    private String sStringAfterFocusGained = "";

    private static final int POSITION_OK = 0;
    private static final int POSITION_LEFT = 1;
    private static final int POSITION_TOP = 2;
    private static final int POSITION_RIGHT = 4;
    private static final int POSITION_BOTTOM = 8;

    public static final int LIST_STYLE = 0;
    public static final int TABLE_STYLE = 1;
    private int iStyle = LIST_STYLE;
    //private int iStyle = TABLE_STYLE;

    private int iScrollBarWidth;
    private int iPopupWidth;
    private int iPopupHeight;
    private int iPreviousCalculatedVisibleRowcount;
    private int iCalculatedVisibleRowcount = -1;
    private boolean bFilterFromTheStart;
    private boolean bNoFilter;

    private boolean bFilledFromString;

    private boolean bLiveData = false;
    private String sLiveDataLookupSQL;
    private boolean bLiveDataColumnNames;
    private Connection connLiveData;

    private boolean bShowPopupOnFocus = true;

    private Vector<MagComponentEventListener> vMagComponentEventListeners = new Vector<>();

    public MagLookupTextField(SwingAppInterface swingAppInterface) {
//        this(swingAppInterface, null, null, null);
        this.swingAppInterface = swingAppInterface;
        init();
    }

//    public MagLookupTextField(SwingAppInterface swingAppInterface, String sLookup) {
//        this(swingAppInterface, null, sLookup);
//    }
    public MagLookupTextField(SwingAppInterface swingAppInterface, Dimension d) {
//        this(swingAppInterface, d, null, null);
        this.swingAppInterface = swingAppInterface;
        this.setPreferredSize(d);
        init();
    }

//    public MagLookupTextField(SwingAppInterface swingAppInterface, Vector<Object> vValue, Vector<Object> vDisplay) {
//        this(swingAppInterface, null, vValue, vDisplay);
//    }
//    public MagLookupTextField(SwingAppInterface swingAppInterface, Dimension d, String sLookup) {
//        super();
//        this.swingAppInterface = swingAppInterface;
//        if (d != null) {
////            this.setMinimumSize(d);
//            this.setPreferredSize(d);
////            this.setMaximumSize(d);
//        }
//        init(null, null, sLookup);
//    }
//    public MagLookupTextField(SwingAppInterface swingAppInterface, Dimension d, Vector<Object> vValue, Vector<Object> vDisplay) {
//        super();
//        this.swingAppInterface = swingAppInterface;
//        if (d != null) {
////            this.setMinimumSize(d);
//            this.setPreferredSize(d);
////            this.setMaximumSize(d);
//        }
//        init(vValue, vDisplay, null);
//    }
    //private void init(Vector<Object> vValue, Vector<Object> vDisplay, String sLookup) {
    private void init() {

        setMargins();
        //this.setMargin(new Insets(1, 1, 1, this.getHeight() - 1));

        this.setFocusTraversalKeysEnabled(false); //focus traversal done programmatically

        //installAncestorListener();
        addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                hidePopup();
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {
                hidePopup();
            }

            @Override
            public void ancestorMoved(AncestorEvent event) {
                //if (event.getSource() != MagLookupTextField.this) {
                hidePopup();
                //movePopup();
                //}
            }
        });

        bPopupIsShown = false;
        iVisibleRowCount = 7;
        this.oValue = null;
        this.bFilterFromTheStart = false;
        this.bNoFilter = false;
        this.bFilledFromString = false;

        iScrollBarWidth = ((Integer) UIManager.get("ScrollBar.width")).intValue();
        jWindow = new JWindow(swingAppInterface.getAppFrame());

        Vector vColumnNames = new Vector();
        vColumnNames.add(" ");
        Vector<Vector> vData = new Vector<>();
        Vector vRecord = new Vector();
        vRecord.add(" ");
        vData.add(vRecord);
        magTableModel = new MagTableModel(vData, vColumnNames);
        magTableModel.setLookupTable(true);
        sorter = new TableRowSorter<MagTableModel>(magTableModel);
//*        magTable = new MagTable(swingAppInterface, "", magTableModel, this);
        magTable = new MagTable(swingAppInterface, "", magTableModel, null);
        magTable.setRowSorter(sorter);
        magTable.getTableHeader().setEnabled(false);
        magTable.setTableHeader(null);
//*        installMouseListener(magTable);
        jScrollPane = new JScrollPane(magTable);
        magTable.setFocusable(false);

        DefaultListModel dlm = new DefaultListModel();
        jList = new JList(dlm);
        jList.setVisibleRowCount(iVisibleRowCount);
        jList.setBackground(new Color(255, 255, 192));
        jList.setFocusable(false);
//*        installMouseListener(jList);

        if (iStyle == TABLE_STYLE) {
            jScrollPane = new JScrollPane(magTable);
        } else {
            jScrollPane = new JScrollPane(jList);
        }
        commonPanel = CommonPanel.createCommonPanel(this.getBorder(), 0, 0, 0, 0);
        //jScrollPane = new JScrollPane(jList);
        jScrollPane.setBorder(BorderFactory.createEmptyBorder());
        commonPanel.addToCurrentRow(jScrollPane, 1, 1, 1.0, 1.0, GridBagConstraints.BOTH, GridBagConstraints.CENTER);

        jWindow.setContentPane(commonPanel);
        jWindow.setMinimumSize(new Dimension(20, 20));
        jWindow.pack();

//        if (1 == 1) {
//            return;
//        }
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                //e.getComponent().setBackground(new Color(255, 255, 192));
                e.getComponent().setBackground(ColorManager.inputBackgroundFocusGained());
                ((JTextField) e.getComponent()).selectAll();
                if (!sStringAfterFocusGained.equals("")) {
                    ((JTextField) e.getComponent()).setText(sStringAfterFocusGained);
                    sStringAfterFocusGained = "";
                }
                //MaG 2017.10.31. showPopup();
                //MaG 2017.10.31.
                if (bShowPopupOnFocus) {
                    showPopup();
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                //e.getComponent().setBackground(Color.WHITE);
                JTextField jtf = (JTextField) e.getComponent();
                if (!jtf.isEditable()) {
                    return;
                }
                e.getComponent().setBackground(ColorManager.inputBackgroundFocusLost());
                //MaG 2016.01.23. autoCompleteData();
                //System.out.println(StringUtils.isNull(getValue(), ""));
//                if (c != null) {
//                    setValue(getValue()); //MaG 2016.01.23. in order to format bad dates like 05.35. :)
//                }
                hidePopup();
            }
        });

        addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                boolean bControlKeyPressed = false;
//                bVkDelete = false;
//                bVkBackSpace = false;
//                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
//                    bVkDelete = true;
//                }
//                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
//                    bVkBackSpace = true;
//                }
                //System.out.println(e.getKeyChar() + " pressed");
                if (e.getKeyCode() == KeyEvent.VK_PAGE_UP) {
                    popupStepUp(true);
                    bControlKeyPressed = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    popupStepUp(false);
                    bControlKeyPressed = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    popupStepDown(false);
                    bControlKeyPressed = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
                    popupStepDown(true);
                    bControlKeyPressed = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_HOME && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
                    popupJumpToHome();
                    bControlKeyPressed = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_END && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
                    popupJumpToEnd();
                    bControlKeyPressed = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    selectValue();
                    hidePopup();
                    bControlKeyPressed = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_TAB) {
                    selectValue();
                    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
                    manager.focusNextComponent();
                    bControlKeyPressed = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_TAB && ((e.getModifiers() & KeyEvent.SHIFT_MASK) != 0)) {
                    selectValue();
                    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
                    manager.focusPreviousComponent();
                    bControlKeyPressed = true;
                }
                if ((e.getModifiers() & KeyEvent.ALT_MASK) != 0) {
                    bControlKeyPressed = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_0 && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
                    selectNullValue();
                    hidePopup();
                    bControlKeyPressed = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    hidePopup();
                    setValue(oValue);
                    bControlKeyPressed = true;
                }
                //MaG 2018.09.05.
                if (e.getKeyCode() == KeyEvent.VK_WINDOWS) {
                    bControlKeyPressed = true;
                }
//                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
//                    hidePopup();
//                } else {
//                    showPopup();
//                }
                if (!bControlKeyPressed) {
                    if (e.getKeyCode() >= 32) {
                        showPopup();
//                        swingAppInterface.logLine("showPopup");
//                        doFilter();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
//                color();
                //System.out.println(e.getKeyChar() + " released");
//                swingAppInterface.logLine(Integer.toString(e.getKeyCode()));
                if (e.getKeyCode() == KeyEvent.VK_PAGE_UP) {
                    return;
                }
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    return;
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    return;
                }
                if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
                    return;
                }
                if (e.getKeyCode() == KeyEvent.VK_HOME && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
                    return;
                }
                if (e.getKeyCode() == KeyEvent.VK_END && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
                    return;
                }
                if (e.getKeyCode() >= 32) {
//                    doFilter();
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
                //System.out.println(e.getKeyChar() + " typed");
                //showPopup();
//                swingAppInterface.logLine(Integer.toString(e.getKeyCode()));
//                if (e.getKeyCode() >= 32) {
//                    doFilter();
//                }
            }
        });

        addComponentListener(new ComponentListener() {

            @Override
            public void componentResized(ComponentEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                resizePopup();
                //System.out.println(e.getSource().getClass().getName());
                //System.out.println(((MagLookupTextField) e.getSource()).getWidth());
                hidePopup();
                setMargins();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                //resizePopup();
                //hidePopup();
            }

            @Override
            public void componentShown(ComponentEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        this.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                doFilter();
//                swingAppInterface.logLine("changedUpdate");
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                doFilter();
//                swingAppInterface.logLine("removeUpdate");
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                doFilter();
//                swingAppInterface.logLine("insertUpdate");
            }
        });

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    showPopup();
                    //doFilter();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

    }

    public void setOwner(JDialog jDialogOwner) {
        jWindow = new JWindow(jDialogOwner);
        jWindow.setContentPane(commonPanel);
        jWindow.setMinimumSize(new Dimension(20, 20));
        jWindow.pack();
    }

    private void setMargins() {
        this.setMargin(new Insets(1, 1, 1, this.getHeight() - 1));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawTriangle(this, g);
    }

    public void drawTriangle(Component component, Graphics g) {
        int d = component.getHeight() / 3 - 2;
        int y = (component.getHeight() + d) / 2;
        int x = component.getWidth() - 2 * d - 1;
        Point p1 = new Point(x, y);
        Point p2 = new Point(x - d, y - d);
        Point p3 = new Point(x + d, y - d);
        g.setColor(Color.GRAY);
        g.drawLine(p1.x, p1.y, p2.x, p2.y);
        g.drawLine(p2.x, p2.y, p3.x, p3.y);
        g.drawLine(p3.x, p3.y, p1.x, p1.y);
        Polygon p = new Polygon();
        p.addPoint(p1.x, p1.y);
        p.addPoint(p2.x, p2.y);
        p.addPoint(p3.x, p3.y);
        g.fillPolygon(p);
    }

    private void selectNullValue() {
        this.oValue = null;
        this.setText("");
        //magTable.setRowSelectionInterval(-1, -1);
        magTable.clearSelection();
        fireMagComponentEvent();
    }

    private void selectValue() {
        if (iStyle == TABLE_STYLE) {
            if (magTable.getSelectedRow() < 0) {
                return;
            }
            this.oValue = vValue.elementAt(magTable.convertRowIndexToModel(magTable.getSelectedRow()));
            if (!this.getText().equals(magTable.getValueAt(magTable.getSelectedRow(), 0).toString())) {
                this.setText(magTable.getValueAt(magTable.getSelectedRow(), 0).toString());
            }
            //this.setText(magTable.getValueAt(magTable.getSelectedRow(), 0).toString());
        } else {
            if (jList.getSelectedIndex() < 0) {
                return;
            }
            this.oValue = vValue.elementAt(jList.getSelectedIndex());
            if (!this.getText().equals(vDisplay.elementAt(jList.getSelectedIndex()).toString())) {
                this.setText(vDisplay.elementAt(jList.getSelectedIndex()).toString());
            }
            //this.setText(vDisplay.elementAt(jList.getSelectedIndex()).toString());
        }
        fireMagComponentEvent();
    }

    //@todo task: doFilter for jList too
    private void doFilter() {
        if (bNoFilter) {
            return;
        }
        if (bLiveData) {
            fillTableLookupLive();
        }
//        System.out.println("a");
        showPopup();
//        System.out.println("b");
        if (this.getText().equalsIgnoreCase("")) {
            return;
        }
        if (iStyle == TABLE_STYLE) {
            String sFilter = StringUtils.convertFilterStringToRegexString(this.getText(), bFilterFromTheStart, false);
            try {
                mrf = MagRowFilter.createMagRowFilter(sFilter, magTableModel);
            } catch (java.util.regex.PatternSyntaxException e) {
                return;
            }
            sorter.setRowFilter(mrf);
            if (magTable.getSelectedRow() < 0) {
                if (magTable.getRowCount() > 0) {
                    magTable.setRowSelectionInterval(0, 0);
                }
            } else {
//            swingAppInterface.logLine(Integer.toString(magTable.getSelectedRow()));
//            swingAppInterface.logLine(Integer.toString(magTable.convertRowIndexToModel(magTable.getSelectedRow())));
//            swingAppInterface.logLine(Integer.toString(sorter.convertRowIndexToView(magTable.convertRowIndexToModel(magTable.getSelectedRow()))));
//            swingAppInterface.logLine("-------");
                if (sorter.convertRowIndexToView(magTable.convertRowIndexToModel(magTable.getSelectedRow())) < 0) {
                    if (magTable.getRowCount() > 0) {
                        magTable.setRowSelectionInterval(0, 0);
                    }
                } else {
                    magTable.scrollRectToVisible(magTable.getCellRect(magTable.getSelectedRow(), 0, true));
                }
            }
            //swingAppInterface.logLine(Integer.toString(sorter.getViewRowCount()) + "/" + Integer.toString(iCalculatedVisibleRowcount));
            //if (!jScrollPane.getVerticalScrollBar().isVisible()) {
//            if (sorter.getViewRowCount() <= iCalculatedVisibleRowcount) {
//                //swingAppInterface.logLine("vscrollbar is not visible");
//                jWindow.setPreferredSize(new Dimension(iPopupWidth - iScrollBarWidth, iPopupHeight));
//                jWindow.pack();
//            }
            jWindow.setPreferredSize(new Dimension(iPopupWidth - (sorter.getViewRowCount() <= iCalculatedVisibleRowcount ? iScrollBarWidth : 0), iPopupHeight));
            jWindow.pack();
        } else {
        }
        if (bLiveData) {
            showPopup();
        }
    }

    protected void installMouseListener(MagTable mt) {
        mt.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    selectValue();
                    hidePopup();
                }
//                if (e.getClickCount() == 2) {
//                    selectValue();
//                    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
//                    manager.focusNextComponent();
//                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }

    protected void installMouseListener(JList jList) {
        jList.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    selectValue();
                    hidePopup();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }

//    protected void installAncestorListener() {
//        addAncestorListener(new AncestorListener() {
//            @Override
//            public void ancestorAdded(AncestorEvent event) {
//                hidePopup();
//            }
//
//            @Override
//            public void ancestorRemoved(AncestorEvent event) {
//                hidePopup();
//            }
//
//            @Override
//            public void ancestorMoved(AncestorEvent event) {
//                //if (event.getSource() != MagLookupTextField.this) {
//                hidePopup();
//                //movePopup();
//                //}
//            }
//        });
//    }
    private int checkWindowLocation() {
        int iRetVal = POSITION_OK;

        Container container = swingAppInterface.getAppFrame().getContentPane();
        Point containerLocation = container.getLocationOnScreen();
        Rectangle containerRectangle = container.getBounds();
        int iContainerLeft = new Double(containerLocation.getX()).intValue();
        int iContainerTop = new Double(containerLocation.getY()).intValue();
        int iContainerRight = new Double(containerLocation.getX() + containerRectangle.getWidth()).intValue();
        int iContainerBottom = new Double(containerLocation.getY() + containerRectangle.getHeight()).intValue();

        Point windowLocation = jWindow.getLocationOnScreen();
        Rectangle windowRectangle = jWindow.getBounds();
        int iWindowLeft = new Double(windowLocation.getX()).intValue();
        int iWindowTop = new Double(windowLocation.getY()).intValue();
        int iWindowRight = new Double(windowLocation.getX() + windowRectangle.getWidth()).intValue();
        int iWindowBottom = new Double(windowLocation.getY() + windowRectangle.getHeight()).intValue();

        String s = "(";
        s += Integer.toString(iContainerLeft);
        s += ", ";
        s += Integer.toString(iContainerTop);
        s += ", ";
        s += Integer.toString(iContainerRight);
        s += ", ";
        s += Integer.toString(iContainerBottom);
        s += ") (";
        s += Integer.toString(iWindowLeft);
        s += ", ";
        s += Integer.toString(iWindowTop);
        s += ", ";
        s += Integer.toString(iWindowRight);
        s += ", ";
        s += Integer.toString(iWindowBottom);
        s += ")";
        if (iWindowLeft < iContainerLeft) {
            s += " left";
            iRetVal |= POSITION_LEFT;
        }
        if (iWindowTop < iContainerTop) {
            s += " top";
            iRetVal |= POSITION_TOP;
        }
        if (iWindowRight > iContainerRight) {
            s += " right";
            iRetVal |= POSITION_RIGHT;
        }
        if (iWindowBottom > iContainerBottom) {
            s += " bottom";
            iRetVal |= POSITION_BOTTOM;
        }
        System.out.println(s + " " + Integer.toString(iRetVal));
        return (iRetVal);
    }

    private void popupJumpToHome() {
        showPopup();
        if (iStyle == TABLE_STYLE) {
            magTable.setRowSelectionInterval(0, 0);
            magTable.scrollRectToVisible(magTable.getCellRect(0, 0, true));
        } else {
            jList.setSelectedIndex(0);
            jList.scrollRectToVisible(jList.getCellBounds(0, 0));
        }
    }

    private void popupStepUp(boolean bPage) {
        showPopup();
        int iSelected;
        if (iStyle == TABLE_STYLE) {
            iSelected = magTable.getSelectedRow();
            iSelected -= (bPage ? iCalculatedVisibleRowcount : 1);
            iSelected = Math.max(0, iSelected);
            magTable.setRowSelectionInterval(iSelected, iSelected);
            magTable.scrollRectToVisible(magTable.getCellRect(iSelected, 0, true));
        } else {
            iSelected = jList.getSelectedIndex();
            iSelected -= (bPage ? jList.getVisibleRowCount() : 1);
            iSelected = Math.max(0, iSelected);
            jList.setSelectedIndex(iSelected);
            jList.scrollRectToVisible(jList.getCellBounds(iSelected, iSelected));
        }
    }

    private void popupJumpToEnd() {
        showPopup();
        if (iStyle == TABLE_STYLE) {
            magTable.setRowSelectionInterval(magTable.getRowCount() - 1, magTable.getRowCount() - 1);
            magTable.scrollRectToVisible(magTable.getCellRect(magTable.getRowCount() - 1, 0, true));
        } else {
            jList.setSelectedIndex(jList.getModel().getSize() - 1);
            jList.scrollRectToVisible(jList.getCellBounds(jList.getModel().getSize() - 1, jList.getModel().getSize() - 1));
        }
    }

    private void popupStepDown(boolean bPage) {
        showPopup();
        int iSelected;
        if (iStyle == TABLE_STYLE) {
            iSelected = magTable.getSelectedRow();
            iSelected += (bPage ? iCalculatedVisibleRowcount : 1);
            iSelected = Math.min(iSelected, magTable.getRowCount() - 1);
            magTable.setRowSelectionInterval(iSelected, iSelected);
            magTable.scrollRectToVisible(magTable.getCellRect(iSelected, 0, true));
        } else {
            iSelected = jList.getSelectedIndex();
            iSelected += (bPage ? jList.getVisibleRowCount() : 1);
            iSelected = Math.min(iSelected, jList.getModel().getSize() - 1);
            jList.setSelectedIndex(iSelected);
            jList.scrollRectToVisible(jList.getCellBounds(iSelected, iSelected));
        }
    }

    private void resizePopup() {
        if (!bPopupIsShown) {
            return;
        }
//        jWindow.setPreferredSize(new Dimension(this.getWidth(), iPopupHeight - (jScrollPane.getHorizontalScrollBar().isVisible() ? 0 : iScrollBarWidth)));
//        jWindow.pack();
    }

    private void movePopup() {
        if (!bPopupIsShown) {
            return;
        }
        int iHeight = new Double(jList.getCellBounds(0, 0).getHeight() * jList.getVisibleRowCount()).intValue() + commonPanel.getBorder().getBorderInsets(commonPanel).top + commonPanel.getBorder().getBorderInsets(commonPanel).bottom + jScrollPane.getBorder().getBorderInsets(commonPanel).top + jScrollPane.getBorder().getBorderInsets(commonPanel).bottom;
//        jList.setPreferredSize(new Dimension(this.getWidth() - commonPanel.getBorder().getBorderInsets(commonPanel).left - commonPanel.getBorder().getBorderInsets(commonPanel).right - jScrollPane.getBorder().getBorderInsets(commonPanel).left - jScrollPane.getBorder().getBorderInsets(commonPanel).right - iScrollBarWidth, iListHeight));
        jWindow.setPreferredSize(new Dimension(this.getWidth(), iHeight));
        jWindow.pack();
        jWindow.setLocationRelativeTo(this);
        jWindow.setLocation(jWindow.getX() + (jWindow.getWidth() - this.getWidth()) / 2, jWindow.getY() + jWindow.getHeight() / 2 + this.getHeight() / 2);

        //checkWindowLocation();
//        String s = "";
////        s+="(";
////        s += Integer.toString(new Double(swingAppInterface.getAppFrame().getContentPane().getBounds().getX()).intValue());
////        s += ", ";
////        s += Integer.toString(new Double(swingAppInterface.getAppFrame().getContentPane().getBounds().getY()).intValue());
////        s += ", ";
////        s += Integer.toString(new Double(swingAppInterface.getAppFrame().getContentPane().getBounds().getWidth()).intValue());
////        s += ", ";
////        s += Integer.toString(new Double(swingAppInterface.getAppFrame().getContentPane().getBounds().getHeight()).intValue());
////        s += ") (";
////        s += Integer.toString(new Double(swingAppInterface.getAppFrame().getContentPane().getLocationOnScreen().getX()).intValue());
////        s += ", ";
////        s += Integer.toString(new Double(swingAppInterface.getAppFrame().getContentPane().getLocationOnScreen().getY()).intValue());
////        s += ") ";
//        s += " (";
//        s += Integer.toString(new Double(swingAppInterface.getAppFrame().getContentPane().getLocationOnScreen().getX()).intValue());
//        s += ", ";
//        s += Integer.toString(new Double(swingAppInterface.getAppFrame().getContentPane().getLocationOnScreen().getY()).intValue());
//        s += " - ";
//        s += Integer.toString(new Double(swingAppInterface.getAppFrame().getContentPane().getLocationOnScreen().getX() + swingAppInterface.getAppFrame().getContentPane().getBounds().getWidth()).intValue());
//        s += ", ";
//        s += Integer.toString(new Double(swingAppInterface.getAppFrame().getContentPane().getLocationOnScreen().getY() + swingAppInterface.getAppFrame().getContentPane().getBounds().getHeight()).intValue());
//        s += ") ";
//        s += " (";
//        s += Integer.toString(new Double(jWindow.getBounds().getX()).intValue());
//        s += ", ";
//        s += Integer.toString(new Double(jWindow.getBounds().getY()).intValue());
////        s += ", ";
////        s += Integer.toString(new Double(jWindow.getBounds().getWidth()).intValue());
////        s += ", ";
////        s += Integer.toString(new Double(jWindow.getBounds().getHeight()).intValue());
//        s += " - ";
//        s += Integer.toString(new Double(jWindow.getBounds().getX() + jWindow.getBounds().getWidth()).intValue());
//        s += ", ";
//        s += Integer.toString(new Double(jWindow.getBounds().getY() + jWindow.getBounds().getHeight()).intValue());
//        s += ")";
//        System.out.println(s);
//        //System.out.println(Double.toString(jWindow.getLocationOnScreen().getX()) + " - " + Double.toString(jWindow.getLocationOnScreen().getY()));
    }

    //@todo task: test it with table header!!!
    private int determinePopupPlaceAndVisibleRowCount() {
        int iCellHeight = 25;
        //Rectangle r = jList.getCellBounds(0, 0);
        //int iCellHeight = (r != null ? r.height : 25);
        //iCellHeight = (iUI == TABLE ? magTable.getRowHeight() : (r != null ? r.height : 25));
        if (iStyle == TABLE_STYLE) {
            iCellHeight = magTable.getRowHeight();
        } else {
            Rectangle r = jList.getCellBounds(0, 0);
            iCellHeight = (r != null ? r.height : 25);
        }

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
//        System.out.println("Screen bounds (left, top, right, bottom) = (" + Integer.toString(screenInsets.left) + ", " + Integer.toString(screenInsets.top) + ", " + Integer.toString(screenSize.width - screenInsets.right) + ", " + Integer.toString(screenSize.height - screenInsets.bottom) + ")");
//        System.out.println("Text field bounds (left, top, right, bottom) = (" + Integer.toString(this.getLocationOnScreen().x) + ", " + Integer.toString(this.getLocationOnScreen().y) + ", " + Integer.toString(this.getBounds().width) + ", " + Integer.toString(this.getBounds().height) + ")");
//        System.out.println("Popup height = " + Integer.toString(iPopupHeight));
        int iRowcount = iVisibleRowCount + 1;
        iRowcount = (iStyle == TABLE_STYLE ? Math.min(iVisibleRowCount, magTable.getMagTableModel().getRowCount()) : Math.min(iVisibleRowCount, jList.getModel().getSize())) + 1;
        int iPopupHeight = 0;
        int iSpaceUnder = -1;
        int iSpaceAbove = -1;
        while (iSpaceUnder < 0 && iSpaceAbove < 0 && iRowcount > 0) {
            if (iStyle == TABLE_STYLE) {
                iPopupHeight = iCellHeight * --iRowcount;
            } else {
                jList.setVisibleRowCount(--iRowcount);
                iPopupHeight = iCellHeight * jList.getVisibleRowCount();
            }
            iPopupHeight += commonPanel.getBorder().getBorderInsets(commonPanel).top + commonPanel.getBorder().getBorderInsets(commonPanel).bottom + jScrollPane.getBorder().getBorderInsets(commonPanel).top + jScrollPane.getBorder().getBorderInsets(commonPanel).bottom;
            iSpaceUnder = screenSize.height - screenInsets.bottom - (this.getLocationOnScreen().y + this.getBounds().height + iPopupHeight);
            iSpaceAbove = this.getLocationOnScreen().y - iPopupHeight - screenInsets.top;
        }
        if (iRowcount < 1) {
            return (0);
        }
        if (iSpaceUnder > 0) {
            return (iRowcount);
        }
//        if (this.getLocationOnScreen().y + this.getBounds().height + iPopupHeight > screenSize.height - screenInsets.bottom) {
//            return (-1 * iCalculatedVisibleRowcount);
//        }
        return (-1 * iRowcount);
    }

    private void showPopup() {
        if (bReadOnly) {
            return;
        }
//        if (getGraphicsConfiguration() == null) {
//            return;
//        }
//        if (!this.isVisible()) {
//            return;
//        }

        //iCalculatedVisibleRowcount = determinePopupPlaceAndVisibleRowCount();
        //if (bPopupIsShown) {
        //if (bPopupIsShown && iCalculatedVisibleRowcount == iPreviousCalculatedVisibleRowcount) {
        if (bPopupIsShown && !bLiveData) {
            return;
        }
        // feljebb került! iCalculatedVisibleRowcount = determinePopupPlaceAndVisibleRowCount();
        //iPreviousCalculatedVisibleRowcount = iCalculatedVisibleRowcount;
        iCalculatedVisibleRowcount = determinePopupPlaceAndVisibleRowCount();

        //swingAppInterface.logLine(Integer.toString(iCalculatedVisibleRowcount));
        //jList.setVisibleRowCount(Math.abs(iCalculatedVisibleRowcount));
        int iSign = (iCalculatedVisibleRowcount >= 0 ? 1 : -1);
        iCalculatedVisibleRowcount = Math.abs(iCalculatedVisibleRowcount);
        int iCellHeight = 25;
        iPopupWidth = this.getWidth();
        if (iStyle == TABLE_STYLE) {
            sorter.setRowFilter(null);
            if (magTable.getSelectedRow() > -1) {
                magTable.scrollRectToVisible(magTable.getCellRect(magTable.getSelectedRow(), 0, true));
            }
            iCellHeight = magTable.getRowHeight();
            iPopupWidth = 0;
            for (int i = 0; i < magTable.getColumnCount(); i++) {
                iPopupWidth += magTable.getColumnModel().getColumn(i).getWidth();
                //iPopupWidth += magTable.getColumnModel().getColumn(i).getWidth() + 2 * magTable.getIntercellSpacing().width;
                //iPopupWidth += magTable.getColumnModel().getColumn(i).getWidth() + 2 * magTable.getIntercellSpacing().width + magTable.getBorder().getBorderInsets(commonPanel).left + magTable.getBorder().getBorderInsets(commonPanel).right;
            }
            if (sorter.getViewRowCount() > iCalculatedVisibleRowcount) {
                iPopupWidth += iScrollBarWidth;
            }
        } else {
            Rectangle r = jList.getCellBounds(0, 0);
            iCellHeight = (r != null ? r.height : 25);
            jList.setVisibleRowCount(Math.abs(iCalculatedVisibleRowcount));
        }
        iPopupHeight = 0;
        if (iStyle == TABLE_STYLE) {
            iPopupHeight = iCellHeight * iCalculatedVisibleRowcount;
            //iPopupHeight += magTable.getTableHeader().getHeaderRect(0).height;
            //iPopupHeight += iScrollBarWidth;
            if (magTable.getTableHeader() != null) {
                iPopupHeight += magTable.getTableHeader().getHeight();
            }
        } else {
            iPopupHeight = iCellHeight * jList.getVisibleRowCount();
            iPopupHeight += iScrollBarWidth;
        }
        iPopupHeight += commonPanel.getBorder().getBorderInsets(commonPanel).top + commonPanel.getBorder().getBorderInsets(commonPanel).bottom + jScrollPane.getBorder().getBorderInsets(commonPanel).top + jScrollPane.getBorder().getBorderInsets(commonPanel).bottom;
        iPopupWidth += commonPanel.getBorder().getBorderInsets(commonPanel).left + commonPanel.getBorder().getBorderInsets(commonPanel).right + jScrollPane.getBorder().getBorderInsets(commonPanel).left + jScrollPane.getBorder().getBorderInsets(commonPanel).right;

        if (iPopupWidth < this.getWidth()) {
            iPopupWidth = this.getWidth();
        }
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
        if (iPopupWidth > screenSize.width - screenInsets.left - screenInsets.right - 10) {
            iPopupWidth = screenSize.width - screenInsets.left - screenInsets.right - 10;
        }
        //System.out.println(Integer.toString(iPopupWidth) + " - " + Integer.toString(iPopupHeight));
        jWindow.setPreferredSize(new Dimension(iPopupWidth, iPopupHeight));
        jWindow.pack();
        //jWindow.setLocationRelativeTo(this);
        //jWindow.setLocation(jWindow.getX() + (jWindow.getWidth() - this.getWidth()) / 2, jWindow.getY() + iSign * jWindow.getHeight() / 2 + iSign * this.getHeight() / 2);
        int iHorizontalOffset = 0;
        if ((screenSize.width - screenInsets.right - iPopupWidth - this.getLocationOnScreen().x) < 0) {
            iHorizontalOffset = screenSize.width - screenInsets.right - iPopupWidth - this.getLocationOnScreen().x;
            //MaG 2017.07.27.
            if (this.getLocationOnScreen().x + iHorizontalOffset < 0) {
                iHorizontalOffset = -1 * this.getLocationOnScreen().x + screenInsets.left + 10;
            }
        }
        if (iSign > 0) {
            jWindow.setLocation(this.getLocationOnScreen().x + iHorizontalOffset, this.getLocationOnScreen().y + iSign * this.getHeight());
        } else {
            jWindow.setLocation(this.getLocationOnScreen().x + iHorizontalOffset, this.getLocationOnScreen().y + iSign * jWindow.getHeight());
        }
        jWindow.setVisible(true);
//        if (bLiveData) {
//            jWindow.revalidate();
//        }
        bPopupIsShown = true;
//        resizePopup();
    }

    private void hidePopup() {
        jWindow.setVisible(false);
        bPopupIsShown = false;
    }

    public void setClass(Class c) {
        this.c = c;
        this.setValue(null);
    }

    @Override
    public boolean isChanged() {
        if (oOriginValue == null && oValue != null) {
            return (true);
        }
        if (oOriginValue != null && oValue == null) {
            return (true);
        }
        if (oOriginValue == null && oValue == null) {
            return (false);
        }
        return (!oOriginValue.equals(oValue));
    }

    @Override
    public void setValue(Object oKeyValue) {
        if (bLiveData) {
            fillTableLookupLive(oKeyValue == null ? "null" : oKeyValue.toString(), "");
        }
        oOriginValue = oKeyValue;
        if (bFilledFromString) {
            String sStringValue = StringUtils.isNull(oKeyValue, "");
            this.oValue = sStringValue;
//            if (!sStringValue.equalsIgnoreCase("")) {
//                System.out.println(sStringValue);
//            }
        } else {
            this.oValue = oKeyValue;
        }
        //Integer intIndex = hmIndex.get(oKeyValue);
        Integer intIndex = hmIndex.get(this.oValue);
        if (iStyle == TABLE_STYLE) {
            if (intIndex != null) {
                sorter.setRowFilter(null);
//                swingAppInterface.logLine(intIndex.toString());
//                swingAppInterface.logLine(Integer.toString(magTable.getRowCount()));
                magTable.setRowSelectionInterval(intIndex.intValue(), intIndex.intValue());
                bNoFilter = true;
                this.setText(vDisplay.elementAt(magTable.getSelectedRow()).toString());
                bNoFilter = false;
            } else {
                magTable.clearSelection();
                bNoFilter = true;
                this.setText("");
                bNoFilter = false;
            }
        } else {
            if (intIndex != null) {
                jList.setSelectedIndex(intIndex.intValue());
                bNoFilter = true;
                this.setText(vDisplay.elementAt(jList.getSelectedIndex()).toString());
                bNoFilter = false;
            } else {
                jList.setSelectedIndex(-1);
                bNoFilter = true;
                this.setText("");
                bNoFilter = false;
            }
        }
    }

    public Class getValueClass() {
        return (c);
    }

    @Override
    public Object getValue() {
        return (oValue);
    }

    public String getStringValue() {
        return (StringUtils.isNull(this.getValue(), ""));
    }

    public Integer getIntegerValue() {
        return (IntegerUtils.convertToInteger(this.getValue()));
    }

    public int getIntValue() {
        Integer integer = IntegerUtils.convertToInteger(this.getValue());
        if (integer == null) {
            return (0);
        }
        return (integer.intValue());
    }

    /**
     * Fills the list with the given value-display pairs.
     *
     * @param sLookup Lookup string of value-display pairs
     * (value1|display1@value2|display2)
     */
    public void fillListLookup(String sLookup) {
        this.bFilledFromString = true;
        String sNext = "";
        String sValue = "";
        String sDisplay = "";

        bIsRefreshing = true;
        Object oValue = null;

        if (this.c == null) {
            this.setClass(String.class);
        }
        int i = 0;
        DefaultListModel dlm = new DefaultListModel();
        //this.removeAllItems();
        //jList.removeAll();
        vValue.clear();
        this.vDisplay.clear();
        //this.hashmap.clear();
        StringTokenizer st = new StringTokenizer(sLookup, "@", false);
//        if (bEmptyTop)
//        {
//            addItem("", "");
//        }
        while (st.hasMoreTokens()) {
            sNext = st.nextToken();//.trim();
            StringTokenizer st1 = new StringTokenizer(sNext, "|", false);
            if (st1.hasMoreTokens()) {
                sValue = "";
                sDisplay = "";
                if (st1.hasMoreTokens()) {
                    sValue = st1.nextToken().trim();
                }
                if (st1.hasMoreTokens()) {
                    sDisplay = st1.nextToken().trim();
                }

                oValue = new String(sValue);
//                if (c.equals(Integer.class)) {
//                    oValue = Integer.parseInt(sValue);
//                }

                //addItem(sValue, sDisplay);
                dlm.addElement(sDisplay);
                //jList.getModel().
                vValue.add(oValue);
                vDisplay.add(sDisplay);
                //this.hashmap.put(oValue, sDisplay);
                hmIndex.put(oValue, new Integer(i));
                ++i;
            }
        }
        jList.setModel(dlm);
        bIsRefreshing = false;
    }

    public void fillListLookup(Vector<Object> vValue, Vector<Object> vDisplay) {
        if (vValue.size() != vDisplay.size()) {
            return;
        }
        bIsRefreshing = true;
        DefaultListModel dlm = new DefaultListModel();
        this.vValue.clear();
        this.vDisplay.clear();
        //this.hashmap.clear();
        for (int i = 0; i < vValue.size(); i++) {
            dlm.addElement(vDisplay.elementAt(i).toString());
            this.vValue.add(vValue.elementAt(i));
            this.vDisplay.add(vDisplay.elementAt(i));
            //this.hashmap.put(vValue.elementAt(i), vDisplay.elementAt(i));
            this.hmIndex.put(vValue.elementAt(i), new Integer(i));
        }
        jList.setModel(dlm);
        bIsRefreshing = false;
    }

    /**
     * Fills the table with the given value-display pairs.
     *
     * @param sLookup Lookup string of value-display pairs
     * (value1|display11|display12...@value2|display21|display22...)
     */
    public void fillTableLookup(String sLookup) {
        this.bFilledFromString = true;
        Vector<Object> vValue = new Vector<Object>();
        Vector<Object> vRecord = new Vector<Object>();
        Vector<Vector<Object>> vData = new Vector<Vector<Object>>();
        Vector<Object> vColumnNames = null;
        String sNext = "";
        int i = 0;
        bIsRefreshing = true;
        StringTokenizer st = new StringTokenizer(sLookup, "@", false);
        while (st.hasMoreTokens()) {
            sNext = st.nextToken();
            StringTokenizer st1 = new StringTokenizer(sNext, "|", false);
            i = 0;
            vRecord = new Vector<Object>();
            while (st1.hasMoreTokens()) {
                if (i == 0) {
                    vValue.addElement(st1.nextToken().trim());
                } else {
                    vRecord.addElement(st1.nextToken().trim());
                }
                i++;
            }
            vData.addElement(vRecord);
        }
        fillTableLookup(vValue, vData, vColumnNames);
        bIsRefreshing = false;
    }

    public void fillTableLookupLive() {
        fillTableLookupLive("null", this.getText());
    }

    public void fillTableLookupLive(String sValue, String sSearch) {
        String sSQL = sLiveDataLookupSQL.replace("<value>", sValue).replace("<search>", sSearch.trim());
        //System.out.println(sSQL);
        fillTableLookup(connLiveData, sSQL, bLiveDataColumnNames);
    }

    public void fillTableLookup(Connection connection, String sLookupSQL, boolean bColumnNames) {
        this.bFilledFromString = false;
        try {
            PreparedStatement psLookup = connection.prepareStatement(sLookupSQL);
            ResultSet rsLookup = psLookup.executeQuery();
            ResultSetMetaData rsm = rsLookup.getMetaData();
            int iColumnCount = rsm.getColumnCount();
            Vector<Object> vValue = new Vector<Object>();
            Vector<Object> vRecord = new Vector<Object>();
            Vector<Vector<Object>> vData = new Vector<Vector<Object>>();
            Vector<Object> vColumnNames = null;
            //if (bColumnNames && iColumnCount > 2) {
            if (bColumnNames) {
                vColumnNames = new Vector<Object>();
                for (int i = 1; i < rsm.getColumnCount(); i++) {
                    vColumnNames.addElement(rsm.getColumnLabel(i + 1));
                }
            }
            while (rsLookup.next()) {
                Object o = rsLookup.getObject(1);
                if (o != null && o.getClass().equals(String.class)) {
                    o = ((String) o).trim();
                }
                if (o != null) {
                    c = o.getClass();
                }
                vValue.addElement(o);
                vRecord = new Vector<Object>();
                if (iColumnCount > 1) {
                    for (int iColumn = 2; iColumn <= iColumnCount; iColumn++) {
                        vRecord.addElement(rsLookup.getObject(iColumn));
                    }
                } else {
                    vRecord.addElement(o);
                }
                vData.addElement(vRecord);
            }
            fillTableLookup(vValue, vData, vColumnNames);

            rsm = null;
            rsLookup.close();
            rsLookup = null;
            psLookup.close();
            psLookup = null;
        } catch (SQLException sqle) {
        }
    }

    public void fillTableLookup(MemoryTable mt) {
        bIsRefreshing = true;
        this.bFilledFromString = false;
        this.vValue.clear();
        this.vDisplay.clear();
        this.hmIndex.clear();
//        Vector vColumnNames = mt.getColumnNames();
//        vColumnNames.remove(0);
//        Vector<Vector<Object>> vData = mt.getDataVector();
        for (int iRow = 0; iRow < mt.getRowCount(); iRow++) {
            this.vValue.add(mt.getValueAt(iRow, 0));
            this.vDisplay.add(mt.getValueAt(iRow, 1));
            this.hmIndex.put(mt.getValueAt(iRow, 0), new Integer(iRow));
//            vData.elementAt(iRow).remove(0);
        }

        Vector vColumnNames = mt.getColumnNames();
        //vColumnNames.remove(0);
        if (vColumnNames.size() > 1) {
            vColumnNames.remove(0);
        }
        Vector<Vector<Object>> vData = mt.getDataVector();
        for (int iRow = 0; iRow < mt.getRowCount(); iRow++) {
            //vData.elementAt(iRow).remove(0);
            if (vData.elementAt(iRow).size() > 1) {
                vData.elementAt(iRow).remove(0);
            }
        }

        magTableModel = new MagTableModel(vData, vColumnNames);
        magTableModel.setLookupTable(true);
        sorter = new TableRowSorter<MagTableModel>(magTableModel);
        magTable = null;
//*        magTable = new MagTable(swingAppInterface, "", magTableModel, this);
        magTable = new MagTable(swingAppInterface, "", magTableModel, null);
        magTable.setRowSorter(sorter);
        installMouseListener(magTable);
        magTable.getTableHeader().setEnabled(false);
        magTable.setFocusable(false);
        magTable.setAutoColumnWidth();
        jScrollPane.setViewportView(magTable);
        bIsRefreshing = false;
    }

    public void fillTableLookup(Vector<Object> vValue, Vector<Vector<Object>> vData, Vector<Object> vColumnNames) {
        bIsRefreshing = true;
        this.bFilledFromString = false;
        this.vValue.clear();
        this.vDisplay.clear();
        //this.hashmap.clear();
        this.hmIndex.clear();
        for (int i = 0; i < vValue.size(); i++) {
            this.vValue.add(vValue.elementAt(i));
            this.vDisplay.add(vData.elementAt(i).elementAt(0));
            //this.hashmap.put(vValue.elementAt(i), vDisplay.elementAt(i));
            this.hmIndex.put(vValue.elementAt(i), new Integer(i));
        }

        boolean bNoHeader = (vColumnNames == null);
        if (bNoHeader) {
            vColumnNames = new Vector();
            if (vData.size() > 0) {
                for (int i = 0; i < vData.elementAt(0).size(); i++) {
                    vColumnNames.add("");
                }
            }
        }
        magTableModel = new MagTableModel(vData, vColumnNames);
        magTableModel.setLookupTable(true);
        sorter = new TableRowSorter<MagTableModel>(magTableModel);
        magTable = null;
//*        magTable = new MagTable(swingAppInterface, "", magTableModel, this);
        magTable = new MagTable(swingAppInterface, "", magTableModel, null);
        magTable.setRowSorter(sorter);
        installMouseListener(magTable);
        magTable.getTableHeader().setEnabled(false);
        if (bNoHeader) {
            magTable.setTableHeader(null);
        }
        magTable.setFocusable(false);
        magTable.setAutoColumnWidth();
        jScrollPane.setViewportView(magTable);
        bIsRefreshing = false;
    }

    public String getDisplay(Object oKeyValue) {
//        if (oKey != null && oKey.getClass().equals(String.class)) {
//            oKey = ((String) oKey).trim();
//        }
        //System.out.println("---");
        //System.out.println(oKey.toString());

        if (bFilledFromString) {
            String sStringValue = StringUtils.isNull(oKeyValue, "");
            this.oValue = sStringValue;
//            if (!sStringValue.equalsIgnoreCase("")) {
//                System.out.println(sStringValue);
//            }
        } else {
            this.oValue = oKeyValue;
        }
        //Integer intIndex = hmIndex.get(oKeyValue);
        Integer intIndex = hmIndex.get(this.oValue);

        //Integer intIndex = hmIndex.get(oKeyValue);
        if (intIndex != null) {
            //System.out.println(intIndex.toString());
            //System.out.println(vDisplay.elementAt(intIndex).toString());
            return (vDisplay.elementAt(intIndex).toString());
        }

//        if (oKeyValue != null && oKeyValue.getClass().equals(String.class)) {
//            intIndex = hmIndex.get(((String) oKeyValue).trim());
//            if (intIndex != null) {
//                return (vDisplay.elementAt(intIndex).toString());
//            }
//        }
        if (oKeyValue != null && oKeyValue.getClass().equals(java.lang.Integer.class)) {
            intIndex = hmIndex.get(((Integer) oKeyValue).toString());
            if (intIndex != null) {
                return (vDisplay.elementAt(intIndex).toString());
            }
        }

        return ("");
    }

    public boolean isRefreshing() {
        return (bIsRefreshing);
    }

    public void setStringAfterFocusGained(String s) {
        this.sStringAfterFocusGained = s;
    }

    public void setVisibleRowCount(int iVisibleRowCount) {
        this.iVisibleRowCount = Math.max(1, iVisibleRowCount);
    }

    public void setFilterFromTheStart(boolean b) {
        bFilterFromTheStart = b;
    }

    public boolean isFilterFromTheStart() {
        return (bFilterFromTheStart);
    }

    public void setShowPopupOnFocus(boolean b) {
        bShowPopupOnFocus = b;
    }

    public boolean isShowPopupOnFocus() {
        return (bShowPopupOnFocus);
    }

//    @Override
//    public boolean beforeRowChange(int iPreviousRow, int iNextRow) {
//        return (true);
////        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void rowChanged(int iPreviousRow, int iNextRow) {
////        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public boolean tabPressedInLastCell(int iPreviousRow, int iNextRow) {
//        return (true);
////        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void requestForInfo() {
////        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void status(String sStatus) {
////        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
    public void setStyle(int i) {
        if (i != LIST_STYLE && i != TABLE_STYLE) {
            return;
        }
        iStyle = i;
        if (iStyle == LIST_STYLE) {
            jScrollPane.setViewportView(jList);
        }
        if (iStyle == TABLE_STYLE) {
            jScrollPane.setViewportView(magTable);
        }
    }

    /**
     * Factory method which creates a table style MagLookupTextField.
     *
     * @param swingAppInterface SwingAppInterface
     * @param d the preferred size (dimension) of the text field
     * @return MagLookupTextField
     */
    public static MagLookupTextField createTableStyleMagLookupTextField(SwingAppInterface swingAppInterface, Dimension d) {
        MagLookupTextField mltf = new MagLookupTextField(swingAppInterface);
        mltf.setStyle(MagLookupTextField.TABLE_STYLE);
        if (d != null) {
            mltf.setPreferredSize(d);
            mltf.setMinimumSize(d);
        }
        //mltf.fillTableLookup(" | ");
        return (mltf);
    }

    /**
     * Factory method which creates a table style MagLookupTextField.
     *
     * @param swingAppInterface SwingAppInterface
     * @param d the preferred size (dimension) of the text field
     * @param sLookup Lookup string of value-display pairs
     * (value1|display1@value2|display2)
     * @return MagLookupTextField
     */
    public static MagLookupTextField createTableStyleMagLookupTextField(SwingAppInterface swingAppInterface, Dimension d, String sLookup) {
        MagLookupTextField mltf = new MagLookupTextField(swingAppInterface);
        mltf.setStyle(MagLookupTextField.TABLE_STYLE);
        if (d != null) {
            mltf.setPreferredSize(d);
        }
        mltf.fillTableLookup(sLookup);
        return (mltf);
    }

    /**
     * Factory method which creates a table style MagLookupTextField.
     *
     * @param swingAppInterface SwingAppInterface
     * @param d the preferred size (dimension) of the text field
     * @param connection connection
     * @param sLookupSQL Lookup sql for getting value-display pairs
     * (value1|display11|display12...@value2|display21|display22...)
     * @param bColumnNames bColumnNames
     * @return MagLookupTextField
     */
    public static MagLookupTextField createTableStyleMagLookupTextField(SwingAppInterface swingAppInterface, Dimension d, Connection connection, String sLookupSQL, boolean bColumnNames) {
        MagLookupTextField mltf = new MagLookupTextField(swingAppInterface);
        mltf.setStyle(MagLookupTextField.TABLE_STYLE);
        if (d != null) {
            mltf.setPreferredSize(d);
        }
        mltf.fillTableLookup(connection, sLookupSQL, bColumnNames);
        return (mltf);
    }

    /**
     * Factory method which creates a table style MagLookupTextField.
     *
     * @param swingAppInterface SwingAppInterface
     * @param d the preferred size (dimension) of the text field
     * @param sConnectionName sConnectionName
     * @param sLookupSQL sLookupSQL
     * @param bColumnNames bColumnNames
     * @return MagLookupTextField
     */
    public static MagLookupTextField createTableStyleMagLookupTextField(SwingAppInterface swingAppInterface, Dimension d, String sConnectionName, String sLookupSQL, boolean bColumnNames) {
        MagLookupTextField mltf = new MagLookupTextField(swingAppInterface);
        mltf.setStyle(MagLookupTextField.TABLE_STYLE);
        if (d != null) {
            mltf.setPreferredSize(d);
            mltf.setMinimumSize(d); //MaG 2017.07.27.
        }
        Connection connection = swingAppInterface.getConnection(sConnectionName);
        mltf.fillTableLookup(connection, sLookupSQL, bColumnNames);
        return (mltf);
    }

    /**
     * Factory method which creates a table style MagLookupTextField.
     *
     * @param swingAppInterface SwingAppInterface
     * @param d the preferred size (dimension) of the text field
     * @param sConnectionName sConnectionName
     * @param sLookupSQL sLookupSQL
     * @param bColumnNames bColumnNames
     * @return MagLookupTextField
     */
    public static MagLookupTextField createTableStyleMagLookupTextFieldLive(SwingAppInterface swingAppInterface, Dimension d, String sConnectionName, String sLookupSQL, boolean bColumnNames) {
        MagLookupTextField mltf = new MagLookupTextField(swingAppInterface);
        mltf.setStyle(MagLookupTextField.TABLE_STYLE);
        if (d != null) {
            mltf.setPreferredSize(d);
            mltf.setMinimumSize(d); //MaG 2017.07.27.
        }
        Connection connection = swingAppInterface.getConnection(sConnectionName);
        mltf.bLiveData = true;
        mltf.connLiveData = swingAppInterface.getConnection(sConnectionName);
        mltf.sLiveDataLookupSQL = sLookupSQL;
        mltf.bLiveDataColumnNames = bColumnNames;
        //mltf.fillTableLookup(connection, sLookupSQL, bColumnNames);
        return (mltf);
    }

    /**
     * Factory method which creates a table style MagLookupTextField.
     *
     * @param swingAppInterface SwingAppInterface
     * @param d the preferred size (dimension) of the text field
     * @param memoryTable memoryTable
     * @return MagLookupTextField
     */
    public static MagLookupTextField createTableStyleMagLookupTextField(SwingAppInterface swingAppInterface, Dimension d, MemoryTable memoryTable) {
        MagLookupTextField mltf = new MagLookupTextField(swingAppInterface);
        mltf.setStyle(MagLookupTextField.TABLE_STYLE);
        if (d != null) {
            mltf.setPreferredSize(d);
            mltf.setMinimumSize(d); //MaG 2017.07.27.
        }
        mltf.fillTableLookup(memoryTable);
        return (mltf);
    }

    /**
     * Factory method which creates a table style MagLookupTextField.
     *
     * @param swingAppInterface SwingAppInterface
     * @param d the preferred size (dimension) of the text field
     * @param vValue Vector of values
     * @param vData Table data (Vector of Vectors)
     * @param vColumnNames Vector of columnnames. If null, no tableheader will
     * be displayed.
     * @return MagLookupTextField
     */
    public static MagLookupTextField createTableStyleMagLookupTextField(SwingAppInterface swingAppInterface, Dimension d, Vector<Object> vValue, Vector<Vector<Object>> vData, Vector<Object> vColumnNames) {
        MagLookupTextField mltf = new MagLookupTextField(swingAppInterface);
        mltf.setStyle(MagLookupTextField.TABLE_STYLE);
        if (d != null) {
            mltf.setPreferredSize(d);
        }
        mltf.fillTableLookup(vValue, vData, vColumnNames);
        return (mltf);
    }

    /**
     * Factory method which creates a table style MagLookupTextField.
     *
     * @param swingAppInterface SwingAppInterface
     * @param d the preferred size (dimension) of the text field
     * @param sItems Item list which serve for both the values and display
     * elements
     * @return MagLookupTextField
     */
    public static MagLookupTextField createTableStyleMagLookupTextField(SwingAppInterface swingAppInterface, Dimension d, String... sItems) {
        MagLookupTextField mltf = new MagLookupTextField(swingAppInterface);
        mltf.setStyle(MagLookupTextField.TABLE_STYLE);
        if (d != null) {
            mltf.setPreferredSize(d);
        }
        Vector<Object> vValue = new Vector<Object>();
        Vector<Vector<Object>> vData = new Vector<Vector<Object>>();
        Vector<Object> vRecord;
        Vector<Object> vColumnNames = null;
        mltf.setClass(String.class);
        for (int i = 0; i < sItems.length; i++) {
//            if (i == 0) {
//                mltf.setClass(String.class);
//            }
            vValue.addElement(sItems[i]);
            vRecord = new Vector<Object>();
            vRecord.addElement(sItems[i]);
            vData.addElement(vRecord);
        }
        mltf.fillTableLookup(vValue, vData, vColumnNames);
        return (mltf);
    }

    /**
     * Factory method which creates a combo style MagLookupTextField.
     *
     * @param swingAppInterface SwingAppInterface
     * @param d the preferred size (dimension) of the text field
     * @param sLookup Lookup string of value-display pairs
     * (value1|display1@value2|display2)
     * @return MagLookupTextField
     */
    public static MagLookupTextField createListStyleMagLookupTextField(SwingAppInterface swingAppInterface, Dimension d, String sLookup) {
        MagLookupTextField mltf = new MagLookupTextField(swingAppInterface);
        mltf.setStyle(MagLookupTextField.LIST_STYLE);
        if (d != null) {
            mltf.setPreferredSize(d);
        }
        mltf.fillListLookup(sLookup);
        return (mltf);
    }

    /**
     * Factory method which creates a combo style MagLookupTextField.
     *
     * @param swingAppInterface SwingAppInterface
     * @param d the preferred size (dimension) of the text field
     * @param vValue Vector of values
     * @param vDisplay Vector of display values
     * @return MagLookupTextField
     */
    public static MagLookupTextField createListStyleMagLookupTextField(SwingAppInterface swingAppInterface, Dimension d, Vector<Object> vValue, Vector<Object> vDisplay) {
        MagLookupTextField mltf = new MagLookupTextField(swingAppInterface);
        mltf.setStyle(MagLookupTextField.LIST_STYLE);
        if (d != null) {
            mltf.setPreferredSize(d);
        }
        mltf.fillListLookup(vValue, vDisplay);
        return (mltf);
    }

    public void addMagComponentEventListener(MagComponentEventListener mcel) {
        for (int i = 0; i < vMagComponentEventListeners.size(); i++) {
            if (vMagComponentEventListeners.elementAt(i).equals(mcel)) {
                return;
            }
        }
        vMagComponentEventListeners.add(mcel);
    }

    public void removeMagComponentEventListener(MagComponentEventListener mcel) {
        for (int i = 0; i < vMagComponentEventListeners.size(); i++) {
            if (vMagComponentEventListeners.elementAt(i).equals(mcel)) {
                vMagComponentEventListeners.remove(i);
            }
        }
    }

    private void fireMagComponentEvent() {
        if (bIsRefreshing) {
            return;
        }
        for (int i = 0; i < vMagComponentEventListeners.size(); i++) {
            MagComponentEvent mce = new MagComponentEvent(this, MagComponentEvent.CHANGED);
            vMagComponentEventListeners.elementAt(i).componentEventPerformed(mce);
        }
    }

    @Override
    public void setReadOnly(boolean bReadOnly) {
        this.bReadOnly = bReadOnly;
        setEditable(!bReadOnly);
        setFocusable(!bReadOnly);
        if (bReadOnly) {
            setBackground(ColorManager.inputBackgroundDisabled());
        } else {
            setBackground(ColorManager.inputBackgroundFocusLost());
        }
    }

//    public void free() {
//        jWindow = null;
//        magTable = null;
//    }
}
