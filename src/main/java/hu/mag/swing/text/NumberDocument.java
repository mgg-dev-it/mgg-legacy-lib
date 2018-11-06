package hu.mag.swing.text;

import hu.mag.swing.MagTextField;

import javax.swing.text.*;

import hu.mgx.app.common.*;
import hu.mgx.util.StringUtils;

public class NumberDocument extends PlainDocument {

    private MagTextField magTextField;
    private String sThousandSeparator = "";
    private char cDecimalSeparator = ' ';
    private String sDecimalSeparator = "";
    private int iPrecision = 0;
    private int iScale = 0;
    //private FormatInterface mgxFormat;
    private String sSpecType = "";

    public NumberDocument(MagTextField magTextField, FormatInterface formatInterface, int iPrecision, int iScale, String sSpecType) throws NullPointerException { //@todo maxvalue, minvalue!!!
        super();
        if (magTextField == null) {
            throw new NullPointerException();
        }
        this.magTextField = magTextField;
        this.sThousandSeparator = new String(new StringBuffer().append(formatInterface.getGroupingSeparator()));
        if (sSpecType.equalsIgnoreCase("year")) {
            this.sThousandSeparator = "";
        }
        if (sSpecType.equalsIgnoreCase("iktatoszam")) {
            this.sThousandSeparator = "";
        }
        this.cDecimalSeparator = formatInterface.getDecimalSeparator();
        this.sDecimalSeparator = new String(new StringBuffer().append(cDecimalSeparator));
        this.iPrecision = iPrecision;
        this.iScale = iScale;
        this.sSpecType = sSpecType;
        if (sSpecType.equalsIgnoreCase("timesheet_time_time")) {
            this.iPrecision = 4;
            this.iScale = 2;
        }
        if (sSpecType.equalsIgnoreCase("timesheet_time_decimal")) {
            this.iPrecision = 4;
            this.iScale = 2;
        }
        //this.mgxFormat = formatInterface;
        //System.out.println("creating NumberDocument for " + magTextField.getName() + " iPrecision=" + Integer.toString(iPrecision));
    }

    private boolean valuableCharacter(char c) {
        //if (Character.isDigit(c) || c == cDecimalSeparator) {
        //MaG 2018.01.29.
        if (Character.isDigit(c) || c == cDecimalSeparator || c == '-') {
            return (true);
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

    private void setValuableCaretPosition(int iValuableCaretPosition) {
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

    private void format(AttributeSet a, int iNewCaretPosition) throws BadLocationException {
        String sRetVal = "";
        int j = 0;
        int iMax = 0;
        String sTmp = getText(0, getLength());
//        System.out.println(sTmp);
        String sSign = "";
        String sTail = "";
        String sDecSep = sDecimalSeparator; //2010.05.20
        if ((sTmp.length() > 0) && (sTmp.substring(0, 1).equals("-"))) {
            sTmp = sTmp.substring(1);
            sSign = "-";
        }
        sTmp = StringUtils.stringReplace(sTmp, sThousandSeparator, "");
        iMax = sTmp.indexOf(cDecimalSeparator);

//        System.out.println(sTmp);
//        System.out.println(iMax);
        if (iMax < 0) {
            iMax = sTmp.length();
            sDecSep = ""; //2010.05.20
        } else {
            sTail = sTmp.substring(iMax + 1);
        }
        for (int i = iMax; i > 0; i--) {
            sRetVal = sTmp.substring(i - 1, i) + sRetVal;
            ++j;
            if (j == 3 && i > 1) {
                sRetVal = sThousandSeparator + sRetVal;
                j = 0;
            }
        }
        sRetVal = sSign + sRetVal + sDecSep + sTail; //2010.05.20

//        System.out.println(sDecSep);
//        System.out.println(sTail);
//        System.out.println(sRetVal);
        super.remove(0, getLength());
        super.insertString(0, sRetVal, a);
        setValuableCaretPosition(iNewCaretPosition);
        //MaG 2018.01.29.
        if (sTmp.length() == 0 && sSign.equalsIgnoreCase("-")) {
            magTextField.setCaretPosition(1);
        }
    }

    private boolean checkPrecision() throws BadLocationException {
        String sTmp = getText(0, getLength());
        int j = 0;
        for (int i = 0; i < sTmp.length(); i++) {
            if (Character.isDigit(sTmp.charAt(i))) {
                ++j;
            }
        }
        if ((j > 0) && (j >= iPrecision)) {
            //System.out.println("checkPrecision j=" + Integer.toString(j) + " iPrecision=" + Integer.toString(iPrecision) + " return false");
            return (false);
        }
        //System.out.println("checkPrecision j=" + Integer.toString(j) + " iPrecision=" + Integer.toString(iPrecision) + " return true");
        return (true);
    }

    private boolean checkScale() throws BadLocationException {
        String sTmp = getText(0, getLength());
        int iCaretPosition = magTextField.getCaretPosition();
        int iPos = sTmp.indexOf(cDecimalSeparator);
        if (iPos < 0) {
            return (true);
        }
        if (iCaretPosition < iPos) {
            return (true);
        }
        sTmp = sTmp.substring(iPos);
        if (sTmp.length() > iScale) {
            return (false);
        }
        return (true);
    }

    private boolean checkScale2() {
        if (iScale < 1) {
            return (false);
        }
        int iCaretPosition = magTextField.getCaretPosition();
        String sTmp = "";
        try {
            sTmp = getText(0, getLength());
        } catch (BadLocationException ble) {
        }
        int p = 0;
        for (int i = iCaretPosition; i < sTmp.length(); i++) {
            if (Character.isDigit(sTmp.charAt(i))) {
                ++p;
            }
        }
        if (p > iScale) {
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
        int s = 0;
        boolean bSign = false;
        String sTmp = "";

        int iValuableCaretPosition = getValuableCaretPosition();

        for (int i = 0; i < result.length; i++) {
            if (Character.isDigit(source[i]) && checkPrecision() && checkScale()) {
                result[j++] = source[i];
            } else if (source[i] == '-') {
                bSign = true;
            } else if (source[i] == ':' && sSpecType.equalsIgnoreCase("timesheet_time_time") && !getText(0, getLength()).contains(":")) {
                result[j++] = source[i];
            } else if (source[i] == '.' && sSpecType.equalsIgnoreCase("timesheet_time_time")) {
                //} else if (source[i] == '.' && sSpecType.equalsIgnoreCase("timesheet_time_decimal") && !getText(0, getLength()).contains(".")) {
//            } else if (source[i] == '.' && sSpecType.equalsIgnoreCase("timesheet_time_decimal")) {
//                result[j++] = source[i];
            } else if (source[i] == ' ') //MaG 2017.05.11.
            {
                //do nothing ... not to beep ... MaG 2017.05.11.
            } else if (source[i] == cDecimalSeparator && checkPrecision()) {
                sTmp = getText(0, getLength()) + result;
                if (sTmp.indexOf(cDecimalSeparator) < 0) {
                    if (checkScale2()) {
                        result[j++] = source[i];
                    } else {
                        //java.awt.Toolkit.getDefaultToolkit().beep();
                    }
                } else {
                    //java.awt.Toolkit.getDefaultToolkit().beep();
                }
            } else {
                //java.awt.Toolkit.getDefaultToolkit().beep();
            }
        }
        super.insertString(offs, new String(result, 0, j), a);
        if (bSign) {
            if ((getLength() > 0) && (getText(0, 1).equals("-"))) {
                super.remove(0, 1);
                s = -1;
            } else {
                super.insertString(0, "-", a);
                s = 1;
            }
        }
        //format(a, iValuableCaretPosition + j);
        format(a, iValuableCaretPosition + j + s);
    }

    @Override
    public void remove(int offs, int len) throws BadLocationException {
        int iValuableCaretPosition = getValuableCaretPosition();
        String sDel = getText(offs, len);
        if (sDel.equals(sThousandSeparator)) {
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
}
