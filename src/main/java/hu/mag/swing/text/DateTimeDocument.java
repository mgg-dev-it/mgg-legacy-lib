package hu.mag.swing.text;

import hu.mag.swing.MagTextField;

import javax.swing.text.*;

import hu.mgx.app.swing.SwingAppInterface;
import hu.mgx.util.StringUtils;
import java.math.BigDecimal;
import java.util.Vector;

public class DateTimeDocument extends PlainDocument {

    protected MagTextField magTextField;
    private String sDatePattern = "";
    private String sTimePattern = "";
    private String sDateTimePattern = "";
    protected char cDateSeparator = ' ';
    protected String sDateSeparator = "";
    protected char cTimeSeparator = ' ';
    protected String sTimeSeparator = "";
    private char cDecimalSeparator = ' ';
    private String sDecimalSeparator = "";
    protected SwingAppInterface swingAppInterface;
    protected int iPrecision = 0;
    private Class c = null;
    private String sSpecType = "";
    private int iDecimals = -1;

    public DateTimeDocument(MagTextField mtf, SwingAppInterface swingAppInterface, Class c, String sSpecType, int iDecimals) throws NullPointerException {
        super();
        if (mtf == null) {
            throw new NullPointerException();
        }
        this.magTextField = mtf;
        this.swingAppInterface = swingAppInterface;
        this.c = c;
        this.sSpecType = sSpecType;
        this.iDecimals = iDecimals;
        setPatterns();
    }

    private void setPatterns() {
        this.sDatePattern = swingAppInterface.getDatePattern();
        this.sTimePattern = swingAppInterface.getTimePattern();
        this.sDateTimePattern = swingAppInterface.getDateTimePattern();
        this.cDateSeparator = swingAppInterface.getDateSeparator();
        this.sDateSeparator = new String(new StringBuffer().append(cDateSeparator));
        this.cTimeSeparator = swingAppInterface.getTimeSeparator();
        this.sTimeSeparator = new String(new StringBuffer().append(cTimeSeparator));
        this.cDecimalSeparator = swingAppInterface.getDecimalSeparator();
        this.sDecimalSeparator = new String(new StringBuffer().append(cDecimalSeparator));
        if (c.equals(java.sql.Date.class) || c.equals(java.util.Date.class)) {
            iPrecision = getPrecisionFromFormat(sDatePattern); //8
        }
        if (c.equals(java.sql.Time.class)) {
            iPrecision = getPrecisionFromFormat(sTimePattern); //6
        }
        if (c.equals(java.math.BigDecimal.class)) {
            if (sSpecType.equals("decimal_time")) {
                iPrecision = getPrecisionFromFormat(sTimePattern) + iDecimals; //6+2?
            }
        }
        if (c.equals(java.sql.Timestamp.class)) {
            iPrecision = getPrecisionFromFormat(sDateTimePattern); //14
        }
        if (iPrecision == 0) {
            //@todo task : unkown class exception
        }
    }

    private int getPrecisionFromFormat(String sFormat) {
        int iPrecision = 0;
        for (int i = 0; i < sFormat.length(); i++) {
            if (new String("yMdHms").indexOf(sFormat.charAt(i)) > -1) {
                ++iPrecision;
            }
        }
        return (iPrecision);
    }

    private boolean valuableCharacter(char ch) {
        //if (Character.isDigit(ch) || ch == cDecimalSeparator){
        if (Character.isDigit(ch)) {
            return (true);
        }
        if (c.equals(java.math.BigDecimal.class)) {
            if (sSpecType.equals("decimal_time")) {
                if (ch == cDecimalSeparator) {
                    return (true);
                }
            }
        }
        return (false);
    }

    private int getValuableCaretPosition() {
        String sTmp = "";
        int iValuableCaretPosition = 0;
        int iRealCaretPosition = magTextField.getCaretPosition();
        try {
            sTmp = getText(0, getLength());
        } catch (BadLocationException ble) {
        }
        for (int i = 0; i < iRealCaretPosition; i++) {
            if (valuableCharacter(sTmp.charAt(i))) {
                ++iValuableCaretPosition;
            }
        }
        return (iValuableCaretPosition);
    }

    protected void setValuableCaretPosition(int iValuableCaretPosition) {
        String sTmp = "";
        try {
            sTmp = getText(0, getLength());
        } catch (BadLocationException ble) {
        }
        int p = 0;
        for (int i = 0; i < sTmp.length(); i++) {
            if (p == iValuableCaretPosition) {
                magTextField.setCaretPosition(i);
                return;
            }
            if (valuableCharacter(sTmp.charAt(i))) {
                ++p;
            }
        }
    }

    //@todo task * : formatting based on the given formats!!!
    protected void format(AttributeSet a, int iNewCaretPosition) throws BadLocationException {
        String sRetVal = "";
        int j = 0;
        String sTmp = getText(0, getLength());
        sTmp = StringUtils.stringReplace(sTmp, sDateSeparator, "");
        sTmp = StringUtils.stringReplace(sTmp, sTimeSeparator, "");
        int l = sTmp.length();
        int iSpacePosition = sTmp.indexOf(" ");
        int iDecimalSeparatorPosition = sTmp.indexOf(cDecimalSeparator);

        //int iSeparator1 = 0;
        //int iSeparator2 = 0;
        Vector<Integer> vDateSeparatorPositions = new Vector<Integer>();
        // yyyy/MM/dd (l-2 és l-4)   dd/MM/yyyy (2 és 4)
        if (sDatePattern.startsWith("yyyy")) {
            for (int i = sDatePattern.length() - 1; i > -1; i--) {
                if (sDatePattern.charAt(i) == cDateSeparator) {
                    vDateSeparatorPositions.add(sDatePattern.length() - i - 1 - vDateSeparatorPositions.size());
                }
            }
        } else {
            for (int i = 0; i < sDatePattern.length(); i++) {
                if (sDatePattern.charAt(i) == cDateSeparator) {
                    vDateSeparatorPositions.add(i - vDateSeparatorPositions.size());
                }
            }
        }

        for (int i = 0; i < l; ++i) {
            sRetVal = sRetVal + sTmp.substring(i, i + 1);
            ++j;

            if (c.equals(java.sql.Date.class) || c.equals(java.util.Date.class)) {
                //@todo ezek az értékek (8-4, 8-6) a formátumból kideríthetők???
//                if (j == l - 2 || j == l - 4) {
//                    sRetVal = sRetVal + sDateSeparator;
//                }
                for (int k = 0; k < vDateSeparatorPositions.size(); k++) {
                    if (sDatePattern.startsWith("yyyy")) {
                        if (j == l - vDateSeparatorPositions.elementAt(k).intValue()) {
                            sRetVal = sRetVal + sDateSeparator;
                        }
                    } else {
                        if (l > j && j == vDateSeparatorPositions.elementAt(k).intValue()) {
                            sRetVal = sRetVal + sDateSeparator;
                        }
                    }
                }
            }

            if (c.equals(java.sql.Time.class)) {
                if (j == l - 2 || j == l - 4) {
                    sRetVal = sRetVal + sTimeSeparator;
                }
            }

            if (c.equals(java.math.BigDecimal.class)) {
                if (sSpecType.equals("decimal_time")) {
                    if (iDecimalSeparatorPosition > -1) {
                        if (j == iDecimalSeparatorPosition - 2 || j == iDecimalSeparatorPosition - 4) {
                            sRetVal = sRetVal + sTimeSeparator;
                        }
                    } else {
                        if (j == l - 2 || j == l - 4) {
                            sRetVal = sRetVal + sTimeSeparator;
                        }
                    }
                }
            }

            if (c.equals(java.sql.Timestamp.class)) {
                if (iSpacePosition > -1) {
                    if (j < iSpacePosition && (j == iSpacePosition - 2 || j == iSpacePosition - 4)) {
                        sRetVal = sRetVal + sDateSeparator;
                    }
                    if (j > iSpacePosition && (j == l - 2 && l - 3 > iSpacePosition || j == l - 4 && l - 5 > iSpacePosition)) {
                        sRetVal = sRetVal + sTimeSeparator;
                    }
                } else {
                    if (j == l - 2 || j == l - 4) {
                        sRetVal = sRetVal + sDateSeparator;
                    }
                }
            }
        }
        super.remove(0, getLength());
        super.insertString(0, sRetVal, a);
        setValuableCaretPosition(iNewCaretPosition);
    }

    private boolean checkPrecision() throws BadLocationException {
        String sTmp = getText(0, getLength());
        int j = 0;
        int iDecimalCount = 0;
        int iDecimalSeparatorPosition = -1;
        boolean bDecimals = false;
        boolean bAfterDecimalSeparator = false;
//        System.out.println(Integer.toString(magTextField.getSelectionStart())+" - "+Integer.toString(magTextField.getSelectionEnd()));
//        if (magTextField.getSelectionEnd() - magTextField.getSelectionStart() > 0) {
//            return (true);
//        }
        int iCaretPosition = magTextField.getCaretPosition();
        for (int i = 0; i < sTmp.length(); i++) {
            if (sTmp.charAt(i) == cDecimalSeparator) {
                bDecimals = true;
                iDecimalSeparatorPosition = i;
            }
            if (Character.isDigit(sTmp.charAt(i))) {
                //++j;
                if (bDecimals) {
                    ++iDecimalCount;
                } else {
                    ++j;
                }
            }
        }
        if (iDecimalSeparatorPosition > -1 && iCaretPosition > iDecimalSeparatorPosition) {
            bAfterDecimalSeparator = true;
        }
        //if ((j > 0) && (j >= iPrecision)) {
        //if ((j > 0) && (j >= iPrecision - (iDecimals < 0 ? 0 : iDecimals))) {
        if (!bAfterDecimalSeparator && (j > 0) && (j >= iPrecision - (iDecimals < 0 ? 0 : iDecimals))) {
            return (false);
        }
        //if ((iDecimalCount > 0) && (iDecimalCount >= iDecimals)) {
        if (bAfterDecimalSeparator && (iDecimalCount > 0) && (iDecimalCount >= iDecimals)) {
            return (false);
        }
        return (true);
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if (str == null) {
            return;
        }
        char[] source = str.toCharArray();
        char[] result = new char[source.length];
        int j = 0;
//        String sTmp="";
        int iValuableCaretPosition = getValuableCaretPosition();
        for (int i = 0; i < result.length; i++) {
            if (Character.isDigit(source[i]) && checkPrecision()) {
                result[j++] = source[i];
            } else if (source[i] == cDateSeparator) {
                //do nothing
            } else if (source[i] == cTimeSeparator) {
                //do nothing
            } else if (source[i] == ' ') {
                if (c.equals(java.sql.Timestamp.class)) {
                    if (getText(0, getLength()).indexOf(' ') < 0) {
                        result[j++] = source[i];
                    }
                }
            } else if (source[i] == cDecimalSeparator) { // && checkPrecision()
                if (c.equals(java.math.BigDecimal.class)) {
                    if (sSpecType.equals("decimal_time")) {
                        if (getText(0, getLength()).indexOf(cDecimalSeparator) < 0) {
                            result[j++] = source[i];
                        }
                    }
                }
            } else {
//                int aaa = 0; //for a breakpoint
//                java.awt.Toolkit.getDefaultToolkit().beep();
            }
        }
        super.insertString(offs, new String(result, 0, j), a);
        format(a, iValuableCaretPosition + j);
    }

    @Override
    public void remove(int offs, int len) throws BadLocationException {
        int iValuableCaretPosition = getValuableCaretPosition();
        String sDel = getText(offs, len);
        if (sDel.equals(sDateSeparator) || sDel.equals(sTimeSeparator) || sDel.equals(" ")) {
            if (magTextField.isVkDelete()) {
                ++len;
            }
            if (magTextField.isVkBackSpace()) {
                --offs;
            }
        }
        super.remove(offs, len);
        if (magTextField.isVkDelete()) {
            format(null, iValuableCaretPosition);
        }
        if (magTextField.isVkBackSpace()) {
            format(null, iValuableCaretPosition - len);
        }
    }

    protected void superInsertString(int offs, String str, AttributeSet a) throws BadLocationException {
        super.insertString(offs, str, a);
    }

    protected void superRemove(int offs, int len) throws BadLocationException {
        super.remove(offs, len);
    }

    public void formatChanged() {
        setPatterns();
    }

    public static String convertDecimaltimeToString(java.math.BigDecimal bdValue, SwingAppInterface swingAppInterface) {
        if (bdValue == null) {
            return ("");
        }

        char cTimeSeparator = swingAppInterface.getTimeSeparator();
        char cDecimalSeparator = swingAppInterface.getDecimalSeparator();

        long lWhole = ((java.math.BigDecimal) bdValue).longValue();
        long lSeconds = lWhole % 60;
        long lMinutes = ((lWhole - lSeconds) / 60) % 60;
        long lHours = ((lWhole - lSeconds - (lMinutes * 60)) / 3600) % 60;

        String sRetVal = (lHours != 0 ? Long.toString(lHours) + cTimeSeparator : "");
        sRetVal += (lHours != 0 || lMinutes != 0 ? (sRetVal.length() == 0 ? Long.toString(lMinutes) : StringUtils.leftPad(Long.toString(lMinutes), '0', 2)) + cTimeSeparator : "");
        sRetVal += (sRetVal.length() == 0 ? Long.toString(lSeconds) : StringUtils.leftPad(Long.toString(lSeconds), '0', 2));

        java.math.BigDecimal bdFraction = ((java.math.BigDecimal) bdValue).subtract(new java.math.BigDecimal(lWhole));

        sRetVal += (bdFraction.compareTo(BigDecimal.ZERO) == 0 ? cDecimalSeparator + "00" : cDecimalSeparator + StringUtils.rightPad(bdFraction.toString().substring(2), '0', 2));
        return (sRetVal);

    }

//    public void setSpecType(String sSpecType) {
//        this.sSpecType = sSpecType;
//    }
//
//    public String getSpecType() {
//        return (sSpecType);
//    }
}
