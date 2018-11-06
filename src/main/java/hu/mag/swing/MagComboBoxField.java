package hu.mag.swing;

import hu.mgx.app.common.AppInterface;
import hu.mgx.app.common.ColorManager;
import hu.mgx.swing.table.MemoryTable;
import hu.mgx.util.IntegerUtils;
import hu.mgx.util.StringUtils;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.JComboBox;

public class MagComboBoxField extends JComboBox implements MagFieldInterface {

    private AppInterface appInterface = null;
    private Object oOriginValue = null;
    private Class c = null;
    //private HashMap<Integer, String> hashmap = new HashMap<Integer, String>();
    private Vector<Object> vValue = new Vector<Object>();
    private Vector<Object> vDisplay = new Vector<Object>();
    private HashMap<Object, Object> hashmap = new HashMap<Object, Object>();
    private HashMap<Object, Integer> hmIndex = new HashMap<Object, Integer>();
    private boolean bIsRefreshing = false;
    private Vector<MagComponentEventListener> vMagComponentEventListeners = new Vector<>();

//    private String sStringAfterFocusGained = "";
    public MagComboBoxField(AppInterface appInterface) {
        super();
        this.appInterface = appInterface;
        init();
    }

    public MagComboBoxField(AppInterface appInterface, Dimension d) {
        super();
        this.appInterface = appInterface;
        this.setMinimumSize(d);
        this.setPreferredSize(d);
        this.setMaximumSize(d);
        init();
    }

    private void init() {
        setBackground(ColorManager.inputBackgroundFocusLost());
        addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                fireMagComponentEvent(e);
            }
        });
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                e.getComponent().setBackground(ColorManager.inputBackgroundFocusGained());
//                if (!sStringAfterFocusGained.equals("")) {
//                    System.out.println(sStringAfterFocusGained);
//                    //((JTextField) e.getComponent()).setText(sStringAfterFocusGained);
//                    KeyEvent ke = new KeyEvent(e.getComponent(), KeyEvent.KEY_TYPED, 1, 0, KeyEvent.VK_UNDEFINED, sStringAfterFocusGained.charAt(0));
//                    ((MagComboBoxField) e.getComponent()).processKeyEvent(ke);
//                    sStringAfterFocusGained = "";
//                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                e.getComponent().setBackground(ColorManager.inputBackgroundFocusLost());
            }
        });
//        for (int i = 0; i < this.getComponentCount(); i++) {
//            if (this.getComponent(i) instanceof javax.swing.JComponent) {
//                ((javax.swing.JComponent) this.getComponent(i)).setBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0));
//            }
//        }
    }

    public void setNoBorder() {
//        for (int i = 0; i < this.getComponentCount(); i++) {
//            if (this.getComponent(i) instanceof javax.swing.JComponent) {
//                ((javax.swing.JComponent) this.getComponent(i)).setBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0));
//            }
//        }
    }

//    public void setInnerBorder(Border border) {
//        for (int i = 0; i < this.getComponentCount(); i++) {
//            if (this.getComponent(i) instanceof javax.swing.JComponent) {
//                ((javax.swing.JComponent) this.getComponent(i)).setBorder(border);
//            }
//        }
//    }
    public void setClass(Class c) {
        this.c = c;
        this.setValue(null);
    }

    @Override
    public boolean isChanged() {
        if (this.getSelectedIndex() < 0) {
            return (oOriginValue != null);
        }
        if (vValue.elementAt(this.getSelectedIndex()) == null) {
            return (oOriginValue != null);
        }
        return (!vValue.elementAt(this.getSelectedIndex()).equals(oOriginValue));
    }

    @Override
    public void setValue(Object oKey) {
        oOriginValue = oKey;
        Integer intIndex = hmIndex.get(oKey);
        if (intIndex != null) {
            this.setSelectedIndex(intIndex.intValue());
        } else {
            this.setSelectedIndex(-1);
        }
    }

    @Override
    public Object getValue() {
        if (this.getSelectedIndex() < 0) {
            return (null);
        }
        return (vValue.elementAt(this.getSelectedIndex()));
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

//    public void fillLookup(String sConnectionName, String sSQL) {
//        MemoryTable mt = MemoryTable.loadFromSQLQuery(appInterface, sConnectionName, sSQL);
//        Vector<Object> vValue = mt.getColumnAsVector("value");
//        Vector<Object> vDisplay = mt.getColumnAsVector("display");
//        fillLookup(vValue, vDisplay);
//    }
    public void fillLookup(String sConnectionName, String sSQL) {
        MemoryTable mt = MemoryTable.loadFromSQLQuery(appInterface, sConnectionName, sSQL);
        fillLookup(mt.getColumnAsVector("value"), mt.getColumnAsVector("display"));
        mt = null;
    }

    public void fillLookup(String sLookup) {
        //value1|display1@value2|display2
        String sNext = "";
        String sValue = "";
        String sDisplay = "";

        bIsRefreshing = true;
        Object oValue = null;

        if (this.c == null) {
            this.setClass(String.class);
        }
        int i = 0;
        this.removeAllItems();
        this.vValue.clear();
        this.vDisplay.clear();
        this.hashmap.clear();
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
                if (c.equals(Integer.class)) {
                    oValue = Integer.parseInt(sValue);
                }
                //MaG 2017.10.24.
                //if (c.equals(String.class)) {
                if (sValue.equalsIgnoreCase("<null>")) {
                    oValue = null;
                }
                //}

                //addItem(sValue, sDisplay);
                this.addItem(sDisplay);
                this.vValue.add(oValue);
                this.vDisplay.add(sDisplay);
                this.hashmap.put(oValue, sDisplay);
                this.hmIndex.put(oValue, new Integer(i));
                ++i;
            }
        }
        bIsRefreshing = false;
    }

    public void fillLookup(Vector<Object> vValue, Vector<Object> vDisplay) {
        if (vValue.size() != vDisplay.size()) {
            return;
        }
        bIsRefreshing = true;
        this.removeAllItems();
        this.vValue.clear();
        this.vDisplay.clear();
        this.hashmap.clear();
        for (int i = 0; i < vValue.size(); i++) {
            this.addItem(vDisplay.elementAt(i).toString());
            this.vValue.add(vValue.elementAt(i));
            this.vDisplay.add(vDisplay.elementAt(i));
            this.hashmap.put(vValue.elementAt(i), vDisplay.elementAt(i));
            this.hmIndex.put(vValue.elementAt(i), new Integer(i));
        }
        bIsRefreshing = false;
    }

    public String getDisplay(Object oKey) {
        Integer intIndex = hmIndex.get(oKey);
        if (intIndex != null) {
            return (vDisplay.elementAt(intIndex).toString());
        }
        if (oKey != null && oKey.getClass().equals(String.class)) {
            intIndex = hmIndex.get(((String) oKey).trim());
            if (intIndex != null) {
                return (vDisplay.elementAt(intIndex).toString());
            }
        }
        return ("");
    }

//    public String getDisplay(Object oKey) {
//        if (oKey != null && oKey.getClass().equals(String.class)) {
//            oKey = ((String) oKey).trim();
//        }
//        Integer intIndex = hmIndex.get(oKey);
//        if (intIndex != null) {
//            return (vDisplay.elementAt(intIndex).toString());
//        } else {
//            return ("");
//        }
//    }
    public boolean isRefreshing() {
        return (bIsRefreshing);
    }

//    public void setStringAfterFocusGained(String s) {
//        System.out.println(s);
//        this.sStringAfterFocusGained = s;
//    }
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

    private void fireMagComponentEvent(ItemEvent e) {
        if (bIsRefreshing) {
            return;
        }
        for (int i = 0; i < vMagComponentEventListeners.size(); i++) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                MagComponentEvent mce = new MagComponentEvent(this, MagComponentEvent.CHANGED);
                mce.setItemEvent(e);
                vMagComponentEventListeners.elementAt(i).componentEventPerformed(mce);

            } else {
                MagComponentEvent mce = new MagComponentEvent(this, MagComponentEvent.NULL_EVENT);
                mce.setItemEvent(e);
                vMagComponentEventListeners.elementAt(i).componentEventPerformed(mce);
            }
        }
    }

    public void setFixSizeToContent(int iTop, int iLeft, int iBottom, int iRight) {
        String s = "";
        int iMaxWidth = -1;
        for (int i = 0; i < vDisplay.size(); i++) {
            int iWidth = getFontMetrics(getFont()).stringWidth(vDisplay.elementAt(i).toString());
            if (iWidth > iMaxWidth) {
                s = vDisplay.elementAt(i).toString();
                iMaxWidth = iWidth;
            }
        }
        setFixSizeToString(s, iTop, iLeft, iBottom, iRight);
    }

    public void setFixSizeToString(String s, int iTop, int iLeft, int iBottom, int iRight) {
        int iHeight = 0;
        int iWidth = 0;
//        if (getBorder() != null) {
//            iHeight = getFontMetrics(getFont()).getHeight() + getInsets().top + getInsets().bottom;
//            iWidth = getFontMetrics(getFont()).stringWidth(s) + getInsets().left + getInsets().right;
//        } else {
        iHeight = getFontMetrics(getFont()).getHeight() + iTop + iBottom;
        iWidth = getFontMetrics(getFont()).stringWidth(s) + iLeft + iRight;
//        }
        //System.out.println(iHeight);
        //System.out.println(iWidth);
        if (iWidth < iHeight) {
            iWidth = iHeight;
        }
        iWidth += iHeight;

        setFixSize(new Dimension(iWidth, iHeight));
    }

    public void setFixSize(int iWidth, int iHeight) {
        setFixSize(new Dimension(iWidth, iHeight));
    }

    public void setFixSize(Dimension d) {
        super.setMinimumSize(d);
        super.setPreferredSize(d);
        super.setMaximumSize(d);
    }

    @Override
    public void setReadOnly(boolean bReadOnly) {
        setEnabled(!bReadOnly);
        //setEditable(!bReadOnly);
        //setFocusable(!bReadOnly);
    }

}
