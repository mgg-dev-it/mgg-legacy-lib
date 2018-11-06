package hu.mag.swing;

import hu.mag.swing.text.DateTimeDocument;
import hu.mag.swing.text.NumberDocument;
import hu.mag.swing.text.StringDocument;
import hu.mgx.app.common.ColorManager;
import hu.mgx.app.swing.SwingAppInterface;
import hu.mgx.util.BigDecimalUtils;
import hu.mgx.util.DateTimeUtils;
import hu.mgx.util.DoubleUtils;
import hu.mgx.util.IntegerUtils;
import hu.mgx.util.StringUtils;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import java.text.ParseException;
import java.util.Vector;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class MagTextField extends JTextField implements MagFieldInterface {

    private SwingAppInterface swingAppInterface = null;
    private Object oOriginValue = null;
    protected Color colorNormal = ColorManager.inputForegroundNormal();
    protected Color colorChanged = ColorManager.inputForegroundChanged();
    private int iMaxLength = -1;
    private int iDecimals = -1;
    private boolean bUpperCase = false;
    private String sStringAfterFocusGained = "";
    private boolean bVkDelete = false;
    private boolean bVkBackSpace = false;
    private Class c = null;
    private DateTimeDocument dtd = null;
    private String sSpecType = "";
    private String sAllowedCharacters = "";
    private boolean bReadOnly = false;

    private Vector<MagComponentEventListener> vMagComponentEventListeners = new Vector<>();

    public MagTextField(SwingAppInterface swingAppInterface) {
        super();
        this.swingAppInterface = swingAppInterface;
        //this.setBorder(new EmptyBorder(0, 0, 0, 0));
        init();
    }

    public MagTextField(SwingAppInterface swingAppInterface, Dimension d) {
        super();
        this.swingAppInterface = swingAppInterface;
        //this.setBorder(new EmptyBorder(0, 0, 0, 0));
        super.setMinimumSize(d);
        super.setPreferredSize(d);
        super.setMaximumSize(d);
        init();
    }

    public static MagTextField createMagTextField(SwingAppInterface swingAppInterface, String sName, Border border, Class c) {
        MagTextField mtf = new MagTextField(swingAppInterface);
        if (sName != null) {
            mtf.setName(sName);
        }
        if (border != null) {
            mtf.setBorder(border);
        }
        if (c != null) {
            mtf.setClass(c);
        }
        //mtf.setClass(java.util.Date.class);
        //mtf.setMinimumSize(new Dimension(70, 21));
        //mtf.setPreferredSize(new Dimension(70, 21));
        return (mtf);
    }

    public static MagTextField createUtilDateMagTextField(SwingAppInterface swingAppInterface) {
        MagTextField mtf = new MagTextField(swingAppInterface);
        mtf.setClass(java.util.Date.class);
        mtf.setMinimumSize(new Dimension(70, 21));
        mtf.setPreferredSize(new Dimension(70, 21));
        return (mtf);
    }

    public static MagTextField createSQLDateMagTextField(SwingAppInterface swingAppInterface) {
        MagTextField mtf = new MagTextField(swingAppInterface);
        mtf.setClass(java.sql.Date.class);
        mtf.setMinimumSize(new Dimension(70, 21));
        mtf.setPreferredSize(new Dimension(70, 21));
        return (mtf);
    }

    public static MagTextField createSQLTimeMagTextField(SwingAppInterface swingAppInterface) {
        MagTextField mtf = new MagTextField(swingAppInterface);
        mtf.setClass(java.sql.Time.class);
        mtf.setMinimumSize(new Dimension(70, 21));
        mtf.setPreferredSize(new Dimension(70, 21));
        return (mtf);
    }

    public static MagTextField createDateTimeMagTextField(SwingAppInterface swingAppInterface) {
        return (createSQLTimestampMagTextField(swingAppInterface));
    }

    public static MagTextField createSQLTimestampMagTextField(SwingAppInterface swingAppInterface) {
        MagTextField mtf = new MagTextField(swingAppInterface);
        mtf.setClass(java.sql.Timestamp.class);
        mtf.setMinimumSize(new Dimension(120, 21));
        mtf.setPreferredSize(new Dimension(120, 21));
        return (mtf);
    }

    public static MagTextField createIntegerMagTextField(SwingAppInterface swingAppInterface) {
        return (createIntegerMagTextField(swingAppInterface, 120, 21));
    }

    public static MagTextField createIntegerMagTextField(SwingAppInterface swingAppInterface, int iWidth, int iHeight) {
        MagTextField mtf = new MagTextField(swingAppInterface);
        mtf.setMaxLength(10);
        mtf.setClass(java.lang.Integer.class);
        mtf.setMinimumSize(new Dimension(iWidth, iHeight));
        mtf.setPreferredSize(new Dimension(iWidth, iHeight));
        return (mtf);
    }

    public static MagTextField createLongMagTextField(SwingAppInterface swingAppInterface) {
        return (createLongMagTextField(swingAppInterface, 120, 21));
    }

    public static MagTextField createLongMagTextField(SwingAppInterface swingAppInterface, int iWidth, int iHeight) {
        MagTextField mtf = new MagTextField(swingAppInterface);
        mtf.setMaxLength(50);
        mtf.setClass(java.lang.Long.class);
        mtf.setMinimumSize(new Dimension(iWidth, iHeight));
        mtf.setPreferredSize(new Dimension(iWidth, iHeight));
        return (mtf);
    }

    public static MagTextField createStringMagTextFieldRight(SwingAppInterface swingAppInterface) {
        MagTextField magTextField = createStringMagTextField(swingAppInterface, 120, 21);
        magTextField.setHorizontalAlignment(JTextField.RIGHT);
        return (magTextField);
    }

    public static MagTextField createStringMagTextField(SwingAppInterface swingAppInterface) {
        return (createStringMagTextField(swingAppInterface, 120, 21));
    }

    public static MagTextField createStringMagTextField(SwingAppInterface swingAppInterface, int iWidth, int iHeight) {
        MagTextField mtf = new MagTextField(swingAppInterface);
        mtf.setClass(java.lang.String.class);
        mtf.setMinimumSize(new Dimension(iWidth, iHeight));
        mtf.setPreferredSize(new Dimension(iWidth, iHeight));
        return (mtf);
    }

    private void init() {
        this.setMargin(new Insets(1, 1, 1, 1));
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                JTextField jtf = (JTextField) e.getComponent();
                if (!jtf.isEditable()) {
                    return;
                }
                e.getComponent().setBackground(ColorManager.inputBackgroundFocusGained());
                jtf.selectAll();
                if (!sStringAfterFocusGained.equals("")) {
                    jtf.setText(sStringAfterFocusGained);
                    sStringAfterFocusGained = "";
                }
                fireMagComponentEventGotFocus();
            }

            @Override
            public void focusLost(FocusEvent e) {
                JTextField jtf = (JTextField) e.getComponent();
                if (!jtf.isEditable()) {
                    return;
                }
                //MaG 2017.11.09.
                if (sSpecType.equalsIgnoreCase("year")) {
                    if (StringUtils.intValue(jtf.getText()) < 1900) {
                        jtf.setText("1900");
                    }
                    if (StringUtils.intValue(jtf.getText()) > 2100) {
                        jtf.setText("2100");
                    }
                }
                //MaG 2017.11.09.
                if (sSpecType.equalsIgnoreCase("month")) {
                    if (StringUtils.intValue(jtf.getText()) < 1) {
                        jtf.setText("1");
                    }
                    if (StringUtils.intValue(jtf.getText()) > 12) {
                        jtf.setText("12");
                    }
                }
                e.getComponent().setBackground(ColorManager.inputBackgroundFocusLost());
                //MaG 2016.01.23. autoCompleteData();
                //System.out.println(StringUtils.isNull(getValue(), ""));
                if (c != null) {
                    Object o = oOriginValue; //save the original value
                    setValue(getValue()); //MaG 2016.01.23. in order to format bad dates like 05.35. :)
                    oOriginValue = o; //restore the original value
                    color(); //color again
                }
                fireMagComponentEventLostFocus();
            }
        });
        addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                bVkDelete = false;
                bVkBackSpace = false;
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    bVkDelete = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    bVkBackSpace = true;
                }
                fireMagComponentEventKeyPressed(e);
            }

            public void keyReleased(KeyEvent e) {
                color();
                fireMagComponentEventKeyReleased(e);
            }

            public void keyTyped(KeyEvent e) {
                fireMagComponentEventKeyTyped(e);
            }
        });
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!bReadOnly) {
                    if (e.getClickCount() == 2) {
                        fireMagComponentEventDblClicked();
                    }
                    if (e.getClickCount() == 1) {
                        fireMagComponentEventClicked();
                    }
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

    public void setStringAfterFocusGained(String s) {
        this.sStringAfterFocusGained = s;
    }

    public void setMaxLength(int iMaxLength) {
        this.iMaxLength = iMaxLength;
        if (c != null && c.equals(String.class)) {
            setValue(getValue());
        }
        if (c != null && (c.equals(java.lang.Short.class) || c.equals(java.lang.Integer.class) || c.equals(java.lang.Long.class) || c.equals(java.lang.Double.class) || c.equals(java.lang.Float.class))) {
            setValue(getValue());
        }
    }

    public void setDecimals(int iDecimals) {
        this.iDecimals = iDecimals;
    }

    public void setUpperCase(boolean bUpperCase) {
        this.bUpperCase = bUpperCase;
        if (c != null && c.equals(String.class)) {
            setValue(getValue());
        }
    }

    public void setClass(Class c) {
        this.c = c;
        if (c.equals(java.lang.Integer.class)) {
            this.setHorizontalAlignment(JTextField.RIGHT);
            this.setDecimals(0);
        }
        if (c.equals(java.lang.Long.class)) {
            this.setHorizontalAlignment(JTextField.RIGHT);
            this.setDecimals(0);
        }
        if (c.equals(java.lang.Short.class)) {
            this.setHorizontalAlignment(JTextField.RIGHT);
            this.setDecimals(0);
        }
        if (c.equals(java.lang.Double.class)) {
            this.setHorizontalAlignment(JTextField.RIGHT);
        }
        if (c.equals(java.lang.Float.class)) {
            this.setHorizontalAlignment(JTextField.RIGHT);
        }
        if (c.equals(java.math.BigDecimal.class)) {
            this.setHorizontalAlignment(JTextField.RIGHT);
        }
        this.setValue(null);
    }

    @Override
    public void setValue(Object value) {
        String s = "";
        oOriginValue = value;
        if (value == null) {
            setText("");
//            return;
            fireMagComponentEventChanged();
        }
        if (c == null && value != null) {
            //c = value.getClass();
            this.setClass(value.getClass());
        }
        if (c != null && c.equals(String.class)) {
            setDocument(new StringDocument(iMaxLength, bUpperCase, sAllowedCharacters));
            if (value != null) {
                s = value.toString();
                if (iMaxLength > -1 && s.length() > iMaxLength) {
                    s = s.substring(0, iMaxLength);
                }
                if (bUpperCase) {
                    s = s.toUpperCase();
                }
                setText(s);
                this.setCaretPosition(0); //MaG 2017.02.03. witohut this long texts are positioned to their end
            }
            color();
            fireMagComponentEventChanged();
            return;
        }
        if (c != null && (c.equals(java.lang.Short.class) || c.equals(java.lang.Integer.class) || c.equals(java.lang.Long.class) || c.equals(java.lang.Double.class) || c.equals(java.lang.Float.class))) {
            setDocument(new NumberDocument(this, swingAppInterface, iMaxLength, iDecimals, sSpecType));
            if (value != null) {
                if (sSpecType.equals("timesheet_time_time")) {
                    int iTimeSheetSeconds = IntegerUtils.convertToInt(value);
                    String sTimeSheetValue = Integer.toString(iTimeSheetSeconds / 3600) + ":" + StringUtils.right("00" + Integer.toString((iTimeSheetSeconds % 3600) / 60), 2);
                    if (sTimeSheetValue.endsWith(":00")) {
                        sTimeSheetValue = sTimeSheetValue.substring(0, sTimeSheetValue.length() - 3);
                    }
                    setText(sTimeSheetValue);
                } else if (sSpecType.equals("timesheet_time_decimal")) {
                    int iTimeSheetSeconds = IntegerUtils.convertToInt(value);
                    BigDecimal bdSeconds = new BigDecimal(iTimeSheetSeconds);
                    String sTimeSheetValue = bdSeconds.divide(new BigDecimal(3600), 2, RoundingMode.HALF_UP).toPlainString();
                    if (sTimeSheetValue.endsWith(".00")) {
                        sTimeSheetValue = sTimeSheetValue.substring(0, sTimeSheetValue.length() - 3);
                    }
                    setText(sTimeSheetValue);
                } else {
                    //setText(swingAppInterface.getDecimalFormat().format(value)); //a formattált számban lévõ szóköz invalid karakter (beep) hibát okozott
                    //System.out.println(value.toString());
                    setText(swingAppInterface.getDecimalFormat().format(value).replace(" ", "")); //a formattált számban lévõ szóköz invalid karakter (beep) hibát okozott
                    //setText(value.toString());
                }
            } else {
                setText(""); //MaG 2017.10.31.
            }
            color();
            fireMagComponentEventChanged();
            return;
        }
        if (c != null && c.equals(java.math.BigDecimal.class)) {
            if (sSpecType.equals("decimal_time")) {
                dtd = new DateTimeDocument(this, swingAppInterface, c, sSpecType, iDecimals);
                setDocument(dtd);
                if (value != null) {
                    setText(DateTimeDocument.convertDecimaltimeToString((java.math.BigDecimal) value, swingAppInterface));
                }
                color();
                fireMagComponentEventChanged();
                return;
            }
            setDocument(new NumberDocument(this, swingAppInterface, iMaxLength, iDecimals, ""));
            if (value != null) {
                //setText(swingAppInterface.getDecimalFormat(iDecimals).format(value)); //a formattált számban lévõ szóköz invalid karakter (beep) hibát okozott
                String sTmp = swingAppInterface.getDecimalFormat(iDecimals).format(value).replace(" ", "");
                if (sTmp.endsWith(".")) {
                    sTmp = sTmp.substring(0, sTmp.length() - 1);
                }
                setText(sTmp); //a formattált számban lévõ szóköz invalid karakter (beep) hibát okozott, és a végén lévõ tizedespont is
                //setText(swingAppInterface.getDecimalFormat(iDecimals).format(value).replace(" ", "")); //a formattált számban lévõ szóköz invalid karakter (beep) hibát okozott
                //setText(value.toString());
            }
            color();
            fireMagComponentEventChanged();
            return;
        }
        if (c != null && (c.equals(java.sql.Date.class) || c.equals(java.util.Date.class))) {
            dtd = new DateTimeDocument(this, swingAppInterface, c, sSpecType, iDecimals);
            setDocument(dtd);
            if (value != null) {
                setText(swingAppInterface.getDateFormat().format(value));
            }
            color();
            fireMagComponentEventChanged();
            return;
        }
        if (c != null && c.equals(java.sql.Time.class)) {
            dtd = new DateTimeDocument(this, swingAppInterface, c, sSpecType, iDecimals);
            setDocument(dtd);
            if (value != null) {
                setText(swingAppInterface.getTimeFormat().format(value));
            }
            color();
            fireMagComponentEventChanged();
            return;
        }
        if (c != null && c.equals(java.sql.Timestamp.class)) {
            dtd = new DateTimeDocument(this, swingAppInterface, c, sSpecType, iDecimals);
            setDocument(dtd);
            if (value != null) {
                setText(swingAppInterface.getDateTimeFormat().format(value));
            }
            color();
            fireMagComponentEventChanged();
            return;
        }
        if (value != null) {
            setText(value.toString());
        }
        color();
        fireMagComponentEventChanged();
    }

    public Class getValueClass() {
        return (c);
    }

    @Override
    public Object getValue() {
        String sNull = null;
        if (c.equals(String.class)) {
            if (oOriginValue == null) {
                if (getText().equalsIgnoreCase("")) {
                    return (sNull);
                }
            }
            return (getText());
        }
        if (c.equals(java.lang.Short.class) || c.equals(java.lang.Integer.class) || c.equals(java.lang.Long.class) || c.equals(java.lang.Double.class) || c.equals(java.lang.Float.class)) {
            if (sSpecType.equalsIgnoreCase("timesheet_time_time")) {
                final Integer intNull = null;
                if (getText() == null || getText().length() == 0) {
                    return (intNull);
                }
                if (!getText().contains(":") || getText().endsWith(":")) {
                    return (new Integer(IntegerUtils.convertToInt(getText()) * 3600));
                } else {
                    String[] saTimeValue = getText().split(":");
                    return (new Integer(IntegerUtils.convertToInt(saTimeValue[0]) * 3600 + IntegerUtils.convertToInt(saTimeValue[1]) * 60));
                }
            }
            if (sSpecType.equalsIgnoreCase("timesheet_time_decimal")) {
                final Integer intNull = null;
                if (getText() == null || getText().length() == 0) {
                    return (intNull);
                }
                if (!getText().contains(".") || getText().endsWith(".")) {
                    return (new Integer(IntegerUtils.convertToInt(getText()) * 3600));
                } else {
                    return (new Integer(new BigDecimal(getText()).multiply(new BigDecimal(3600)).intValue()));
                }
            }
            //MaG 2017.11.09.
            if (sSpecType.equalsIgnoreCase("year")) {
                if (StringUtils.intValue(getText()) < 1900) {
                    setText("1900");
                }
                if (StringUtils.intValue(getText()) > 2100) {
                    setText("2100");
                }
            }
            //MaG 2017.11.09.
            if (sSpecType.equalsIgnoreCase("month")) {
                if (StringUtils.intValue(getText()) < 1) {
                    setText("1");
                }
                if (StringUtils.intValue(getText()) > 12) {
                    setText("12");
                }
            }
            if (getText().equalsIgnoreCase("")) {
                return (null);
            }
            //MaG 2018.01.29.
            if (getText().equalsIgnoreCase("-")) {
                setText("");
                return (null);
            }
            Number n = null;
            try {
                n = swingAppInterface.getDecimalFormat().parse(getText());
            } catch (ParseException pe) {
                swingAppInterface.handleError(pe);
            }
            if (c.equals(java.lang.Short.class)) {
                return (n.shortValue());
            }
            if (c.equals(java.lang.Integer.class)) {
                return (n.intValue());
            }
            if (c.equals(java.lang.Long.class)) {
                return (n.longValue());
            }
            if (c.equals(java.lang.Double.class)) {
                return (n.doubleValue());
            }
            if (c.equals(java.lang.Float.class)) {
                return (n.floatValue());
            }
        }
        if (c.equals(java.math.BigDecimal.class)) {
            java.math.BigDecimal bd = null;
            DecimalFormat df = swingAppInterface.getDecimalFormat();
            df.setParseBigDecimal(true);
            if (getText().trim().equalsIgnoreCase("")) {
                return (bd);
            }
            if (getText().trim().equalsIgnoreCase("-")) {
                return (bd);
            }
            if (sSpecType.equals("decimal_time")) {
                String sTmp = getText();
                String sWhole = sTmp;
                String sFraction = "";
                if (sTmp.indexOf(swingAppInterface.getDecimalSeparator()) > -1) {
                    sWhole = sTmp.substring(0, sTmp.indexOf(swingAppInterface.getDecimalSeparator()));
                    sFraction = sTmp.substring(sTmp.indexOf(swingAppInterface.getDecimalSeparator()) + 1);
                }
                bd = new BigDecimal(0);
                String s[] = sWhole.split(":");
                for (int i = 0; i < s.length; i++) {
                    if (i > 0) {
                        bd = bd.multiply(new BigDecimal(60));
                    }
                    if (s[i].equalsIgnoreCase("")) {
                        s[i] = "0";
                    }
                    bd = bd.add(new BigDecimal(s[i]));
                }
                if (!sFraction.equalsIgnoreCase("")) {
                    bd = bd.add(new BigDecimal("0." + sFraction));
                }
                return (bd);
            }
            if (sSpecType.equalsIgnoreCase("timesheet_time_time")) {
                bd = null;
                if (getText() == null || getText().length() == 0) {
                    return (bd);
                }
                if (!getText().contains(":")) {
                    return (new BigDecimal(IntegerUtils.convertToInt(getText()) * 3600));
                } else {
                    String[] saTimeValue = getText().split(":");
                    return (new BigDecimal(new Integer(IntegerUtils.convertToInt(saTimeValue[0]) * 3600 + IntegerUtils.convertToInt(saTimeValue[1]) * 60)));
                }
            }
            if (sSpecType.equalsIgnoreCase("timesheet_time_decimal")) {
                final Integer intNull = null;
                if (getText() == null || getText().length() == 0) {
                    return (bd);
                }
                if (!getText().contains(".")) {
                    return (new BigDecimal(IntegerUtils.convertToInt(getText()) * 3600));
                } else {
                    return (new BigDecimal(getText()).multiply(new BigDecimal(3600)));
                }
            }
            try {
                bd = (BigDecimal) df.parse(getText());
            } catch (ParseException pe) {
                swingAppInterface.handleError(pe);
            }
            return (bd);
        }
        if (c.equals(java.util.Date.class)) {
            autoCompleteData(); //JTable editing stopped miatt
            java.util.Date d = null;
            if (getText().equalsIgnoreCase("")) {
                return (d);
            }
            try {
                d = swingAppInterface.getDateFormat().parse(getText());
            } catch (ParseException pe) {
                swingAppInterface.handleError(pe);
            }
            return (d);
        }
        if (c.equals(java.sql.Date.class)) {
            autoCompleteData(); //JTable editing stopped miatt
            java.util.Date d = null;
            java.sql.Date t = null;
            if (getText().equalsIgnoreCase("")) {
                return (t);
            }
            try {
                d = swingAppInterface.getDateFormat().parse(getText());
            } catch (ParseException pe) {
                swingAppInterface.handleError(pe);
            }
            return (new java.sql.Date(d.getTime()));
        }
        if (c.equals(java.sql.Time.class)) {
            autoCompleteData(); //JTable editing stopped miatt
            java.util.Date d = null;
            java.sql.Time t = null;
            if (getText().equalsIgnoreCase("")) {
                return (t);
            }
            try {
                d = swingAppInterface.getTimeFormat().parse(getText());
            } catch (ParseException pe) {
                swingAppInterface.handleError(pe);
            }
            return (new java.sql.Time(d.getTime()));
        }
        if (c.equals(java.sql.Timestamp.class)) {
            autoCompleteData(); //JTable editing stopped miatt
            java.util.Date d = null;
            java.sql.Timestamp t = null;
            if (getText().equalsIgnoreCase("")) {
                return (t);
            }
            try {
                d = swingAppInterface.getDateTimeFormat().parse(getText());
            } catch (ParseException pe) {
                swingAppInterface.handleError(pe);
            }
            return (new java.sql.Timestamp(d.getTime()));
        }
        return (null);
    }

    public String getStringValue() {
        return (StringUtils.isNull(this.getValue(), ""));
    }

    public Integer getIntegerValue() {
        return (IntegerUtils.convertToInteger(this.getValue()));
    }

    public BigDecimal getBigDecimalValue() {
        return (BigDecimalUtils.convertToBigDecimal(this.getValue()));
    }

    public BigDecimal getBigDecimalValueNullProtected() {
        return (BigDecimalUtils.convertToBigDecimalIsNull(this.getValue(), BigDecimal.ZERO));
    }

    public Double getDoubleValue() {
        return (DoubleUtils.convertToDouble(this.getValue()));
    }

    public double getDblValue() {
        return (DoubleUtils.convertToDouble(this.getValue()).doubleValue());
    }

    public java.util.Date getUtilDateValue() {
        return (DateTimeUtils.convertToUtilDate(this.getValue()));
    }

    public int getIntValue() {
        Integer integer = IntegerUtils.convertToInteger(this.getValue());
        if (integer == null) {
            return (0);
        }
        return (integer.intValue());
    }

    public boolean isVkDelete() {
        return (bVkDelete);
    }

    public boolean isVkBackSpace() {
        return (bVkBackSpace);
    }

    private String autoCompleteDateData(String sText) {
        String sDatePattern = swingAppInterface.getDatePattern();
        String sDateOrder = StringUtils.filter(sDatePattern, "yMd");

        sText = StringUtils.digitFilter(sText);
        if (sText.equalsIgnoreCase("")) {
            return ("");
        }
        java.util.GregorianCalendar gc = new java.util.GregorianCalendar();
        gc = new java.util.GregorianCalendar(gc.get(java.util.GregorianCalendar.YEAR), gc.get(java.util.GregorianCalendar.MONTH), gc.get(java.util.GregorianCalendar.DAY_OF_MONTH));
        String sYear = StringUtils.leftPad(Integer.toString(gc.get(java.util.GregorianCalendar.YEAR)), '0', 4);
        String sMonth = StringUtils.leftPad(Integer.toString(gc.get(java.util.GregorianCalendar.MONTH) + 1), '0', 2);
        String sDay = StringUtils.leftPad(Integer.toString(gc.get(java.util.GregorianCalendar.DAY_OF_MONTH)), '0', 2);
        String sToday = sDateOrder;
        sToday = StringUtils.stringReplace(sToday, "yyyy", sYear);
        sToday = StringUtils.stringReplace(sToday, "MM", sMonth);
        sToday = StringUtils.stringReplace(sToday, "dd", sDay);

        if ((sText.length() == 1) || (sText.length() == 3)) {
            sText = "0" + sText;
        }
        if (sDatePattern.startsWith("y")) {
            sText = StringUtils.left(sToday, 8 - sText.length()) + sText;
        } else {
            sText += StringUtils.right(sToday, 8 - sText.length());
        }

        sDay = sText.substring(sDateOrder.indexOf("dd"), sDateOrder.indexOf("dd") + 2);
        sMonth = sText.substring(sDateOrder.indexOf("MM"), sDateOrder.indexOf("MM") + 2);
        sYear = sText.substring(sDateOrder.indexOf("yyyy"), sDateOrder.indexOf("yyyy") + 4);

        String sRetVal = "";
        if (sDatePattern.startsWith("y")) {
            sRetVal = sYear + sMonth + sDay;
        } else {
            if (sDatePattern.startsWith("d")) {
                sRetVal = sDay + sMonth + sYear;
            } else {
                sRetVal = sMonth + sDay + sYear;
            }
        }
        return (sRetVal);
    }

    private String autoCompleteTimeData(String sText) {
        sText = StringUtils.digitFilter(sText);
        if (sText.equalsIgnoreCase("")) {
            return ("");
        }
        if ((sText.length() == 1) || (sText.length() == 3) || (sText.length() == 5)) {
            sText = "0" + sText;
        }
        sText = StringUtils.rightPad(sText, '0', 6);
        String sHour = StringUtils.left(sText, 2);
        String sMinute = StringUtils.mid(sText, 2, 2);
        String sSecond = StringUtils.right(sText, 2);
        return (sHour + sMinute + sSecond);
    }

    private void autoCompleteData() {
        final String sYearPattern = "yyyy";
        final String sMonthPattern = "MM";
        final String sDayPattern = "dd";
        final String sHourPattern = "HH";
        final String sMinutePattern = "mm";
        final String sSecondPattern = "ss";

        if (c == null) {
            return;
        }
        String sDatePattern = swingAppInterface.getDatePattern(); //yyyy/MM/dd
        if ((sDatePattern.indexOf(sYearPattern) < 0) || (sDatePattern.indexOf(sMonthPattern) < 0) || (sDatePattern.indexOf(sDayPattern) < 0)) {
            return;
        }
        String sTimePattern = swingAppInterface.getTimePattern(); //HH:mm:ss
        if ((sTimePattern.indexOf(sHourPattern) < 0) || (sTimePattern.indexOf(sMinutePattern) < 0) || (sTimePattern.indexOf(sSecondPattern) < 0)) {
            return;
        }

        String sText = getText();
        String sDate = "";
        String sTime = "";

        if (c.equals(java.sql.Date.class) || c.equals(java.util.Date.class)) {
            setText(autoCompleteDateData(sText));
        }
        if (c.equals(java.sql.Time.class)) {
            setText(autoCompleteTimeData(sText));
        }
        if (c.equals(java.sql.Timestamp.class)) {
            if (!sText.trim().equalsIgnoreCase("")) {
                if (sText.indexOf(" ") > -1) {
                    sDate = StringUtils.left(sText, sText.indexOf(" "));
                    sTime = StringUtils.mid(sText, sText.indexOf(" ") + 1);
                    setText(autoCompleteDateData(sDate) + " " + autoCompleteTimeData(sTime));
                } else {
                    if (sSpecType.equals("time_only")) {
                        sDate = "01";
                        sTime = sText;
                    } else {
                        sDate = sText;
                        sTime = "000000";
                    }
                    setText(autoCompleteDateData(sDate) + " " + autoCompleteTimeData(sTime));
                }
            }
        }
    }

    @Override
    public boolean isChanged() {
        String sTmp = getText();

        if (oOriginValue == null) {
            return (!sTmp.equalsIgnoreCase(""));
        }
        if (c.equals(String.class)) {
            return (!oOriginValue.toString().equals(sTmp));
        }
        if (c.equals(java.lang.Short.class)) {
            return (new Short(StringUtils.shortValue(StringUtils.stringReplace(sTmp, " ", ""))).compareTo((Short) oOriginValue) != 0);
        }
        if (c.equals(java.lang.Integer.class)) {
            return (new Integer(StringUtils.intValue(StringUtils.stringReplace(sTmp, " ", ""))).compareTo((Integer) oOriginValue) != 0);
        }
        if (c.equals(java.lang.Long.class)) {
            return (new Long(StringUtils.longValue(StringUtils.stringReplace(sTmp, " ", ""))).compareTo((Long) oOriginValue) != 0);
        }
        if (c.equals(java.lang.Double.class)) {
            return (new Double(StringUtils.doubleValue(sTmp)).compareTo((Double) oOriginValue) != 0);
        }
        if (c.equals(java.lang.Float.class)) {
            return (new Float(StringUtils.floatValue(sTmp)).compareTo((Float) oOriginValue) != 0);
        }
        if (c.equals(java.math.BigDecimal.class)) {
            return (StringUtils.bigDecimalValue(StringUtils.stringReplace(sTmp, " ", "")).compareTo((BigDecimal) oOriginValue) != 0);
        }
        if (c.equals(java.util.Date.class)) {
            java.util.Date d = null;
            try {
                d = swingAppInterface.getDateFormat().parse(getText());
            } catch (ParseException pe) {
                return (true);
            }
            return (d.compareTo((java.util.Date) oOriginValue) != 0);
        }
        if (c.equals(java.sql.Date.class)) {
            java.util.Date d = null;
            try {
                d = swingAppInterface.getDateFormat().parse(getText());
            } catch (ParseException pe) {
                return (true);
            }
            return (new java.sql.Date(d.getTime()).compareTo((java.sql.Date) oOriginValue) != 0);
        }
        if (c.equals(java.sql.Time.class)) {
            java.util.Date d = null;
            try {
                d = swingAppInterface.getTimeFormat().parse(getText());
            } catch (ParseException pe) {
                return (true);
            }
            return (new java.sql.Time(d.getTime()).compareTo((java.sql.Time) oOriginValue) != 0);
        }
        if (c.equals(java.sql.Timestamp.class)) { //tört másodperceket nem ellenõrzi!!!
            String sOrigin = swingAppInterface.getDateTimeFormat().format(oOriginValue);
            return (!sOrigin.equals(getText()));
        }
//        if (c.equals(java.sql.Timestamp.class)) {
//            java.util.Date d = null;
//            try {
//                d = swingAppInterface.getDateTimeFormat().parse(getText());
//            } catch (ParseException pe) {
//                return (true);
//            }
//            return (new java.sql.Timestamp(d.getTime()).compareTo((java.sql.Timestamp) oOriginValue) != 0);
//        }
        return (false);
    }

    private void color() {
        if (isChanged()) {
            setForeground(colorChanged);
        } else {
            setForeground(colorNormal);
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

    public void setForegroundColorNormal(Color c) {
        colorNormal = c;
        color();
    }

    public void setForegroundColorChanged(Color c) {
        colorChanged = c;
        color();
    }

    public void setActualToOrigin() {
        setValue(oOriginValue);
        color();
    }

    public void setOriginToActual() {
        oOriginValue = getValue();
        color();
    }

    public Object getOldValue() {
        return (oOriginValue);
    }

    public void formatChanged() {
        if (dtd != null) {
            dtd.formatChanged();
        }
    }

    public void setSpecType(String sSpecType) {
        this.sSpecType = sSpecType;
    }

    public String getSpecType() {
        return (sSpecType);
    }

    public boolean isEmpty() {
        return (this.getText().trim().equalsIgnoreCase(""));
    }

    public Integer compareUtilDate(MagTextField mtf) {
        Integer iNull = null;
        if (this.getValue().getClass() != java.util.Date.class) {
            return (iNull);
        }
        if (mtf == null) {
            return (iNull);
        }
        if (mtf.getValue().getClass() != java.util.Date.class) {
            return (iNull);
        }
        return (new Integer(((java.util.Date) this.getValue()).compareTo((java.util.Date) mtf.getValue())));
    }

    public Dimension getContentSize() {
        int iHeight = getFontMetrics(getFont()).getHeight() + getInsets().top + getInsets().bottom;
        int iWidth = getFontMetrics(getFont()).stringWidth(getText()) + getInsets().left + getInsets().right;
        return (new Dimension(iWidth, iHeight));
    }

    public void setFixSizeToContent() {
        setFixSizeToContent(0);
    }

    public void setFixSizeToContent(int iMinCharNum) {
        setFixSizeToString(getText(), iMinCharNum);
    }

    public void setMaxLengthAndFixSize(int iMaxLength) {
        setMaxLength(iMaxLength);
        setFixSizeToString(StringUtils.repeat("M", iMaxLength));
    }

    public void setFixSizeToCharNum(int iCharNum) {
        setFixSizeToString(StringUtils.repeat("M", iCharNum));
    }

    public void setFixSizeToString(String s) {
        setFixSizeToString(s, 0);
    }

    public void setFixSizeToString(String s, int iMinCharNum) {
        int iHeight = getFontMetrics(getFont()).getHeight() + getInsets().top + getInsets().bottom;
        int iWidth = getFontMetrics(getFont()).stringWidth(s) + getInsets().left + getInsets().right;
        if (iMinCharNum > 0) {
            int iWidthMin = getFontMetrics(getFont()).stringWidth(StringUtils.repeat("M", iMinCharNum)) + getInsets().left + getInsets().right;
            iWidth = Math.max(iWidth, iWidthMin);
        }
        iWidth = Math.max(iWidth, iHeight);
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

    public void setAllowedCharacters(String sAllowedCharacters) {
        this.sAllowedCharacters = sAllowedCharacters;
    }

    public String getAllowedCharacters() {
        return (sAllowedCharacters);
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

    private void fireMagComponentEventGotFocus() {
        for (int i = 0; i < vMagComponentEventListeners.size(); i++) {
            MagComponentEvent mce = new MagComponentEvent(this, MagComponentEvent.GOTFOCUS);
            vMagComponentEventListeners.elementAt(i).componentEventPerformed(mce);
        }
    }

    private void fireMagComponentEventLostFocus() {
        for (int i = 0; i < vMagComponentEventListeners.size(); i++) {
            MagComponentEvent mce = new MagComponentEvent(this, MagComponentEvent.LOSTFOCUS);
            vMagComponentEventListeners.elementAt(i).componentEventPerformed(mce);
        }
    }

    private void fireMagComponentEventChanged() {
        for (int i = 0; i < vMagComponentEventListeners.size(); i++) {
            MagComponentEvent mce = new MagComponentEvent(this, MagComponentEvent.CHANGED);
            vMagComponentEventListeners.elementAt(i).componentEventPerformed(mce);
        }
    }

    private void fireMagComponentEventClicked() {
        for (int i = 0; i < vMagComponentEventListeners.size(); i++) {
            MagComponentEvent mce = new MagComponentEvent(this, MagComponentEvent.CLICKED);
            vMagComponentEventListeners.elementAt(i).componentEventPerformed(mce);
        }
    }

    private void fireMagComponentEventDblClicked() {
        for (int i = 0; i < vMagComponentEventListeners.size(); i++) {
            MagComponentEvent mce = new MagComponentEvent(this, MagComponentEvent.DBLCLICKED);
            vMagComponentEventListeners.elementAt(i).componentEventPerformed(mce);
        }
    }

    private void fireMagComponentEventKeyPressed(KeyEvent keyEvent) {
        for (int i = 0; i < vMagComponentEventListeners.size(); i++) {
            MagComponentEvent mce = new MagComponentEvent(this, MagComponentEvent.KEY_PRESSED);
            mce.setKeyEvent(keyEvent);
            vMagComponentEventListeners.elementAt(i).componentEventPerformed(mce);
        }
    }

    private void fireMagComponentEventKeyReleased(KeyEvent keyEvent) {
        for (int i = 0; i < vMagComponentEventListeners.size(); i++) {
            MagComponentEvent mce = new MagComponentEvent(this, MagComponentEvent.KEY_RELEASED);
            mce.setKeyEvent(keyEvent);
            vMagComponentEventListeners.elementAt(i).componentEventPerformed(mce);
        }
    }

    private void fireMagComponentEventKeyTyped(KeyEvent keyEvent) {
        for (int i = 0; i < vMagComponentEventListeners.size(); i++) {
            MagComponentEvent mce = new MagComponentEvent(this, MagComponentEvent.KEY_TYPED);
            mce.setKeyEvent(keyEvent);
            vMagComponentEventListeners.elementAt(i).componentEventPerformed(mce);
        }
    }

}
