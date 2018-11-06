package hu.mgx.swing.text;

import java.awt.*;

import javax.swing.text.*;

import hu.mgx.app.common.*;
import hu.mgx.swing.*;
import hu.mgx.util.StringUtils;

public class DecimalDocument extends PlainDocument
{

    private CommonTextField ctf;
    private String sThousandSeparator = "";
    private char cDecimalSeparator = ' ';
    private String sDecimalSeparator = "";
    private int iPrecision = 0;
    private int iScale = 0;
    private FormatInterface mgxFormat;

    public DecimalDocument(CommonTextField ctf, FormatInterface mgxFormat, int iPrecision, int iScale) throws NullPointerException
    { //@todo maxvalue, minvalue!!!
        super();
        if (ctf == null)
        {
            throw new NullPointerException();
        }
        this.ctf = ctf;
        this.sThousandSeparator = new String(new StringBuffer().append(mgxFormat.getGroupingSeparator()));
        this.cDecimalSeparator = mgxFormat.getDecimalSeparator();
        this.sDecimalSeparator = new String(new StringBuffer().append(cDecimalSeparator));
        this.iPrecision = iPrecision;
        this.iScale = iScale;
        this.mgxFormat = mgxFormat;
    }

    private boolean valuableCharacter(char c)
    {
        if (Character.isDigit(c) || c == cDecimalSeparator)
        {
            return (true);
        }
        return (false);
    }

    private int getValuableCaretPosition()
    {
        String sTmp = "";
        int iValuableCaretPosition = 0;
        int iRealCaretPosition = ctf.getCaretPosition();
        try
        {
            sTmp = getText(0, getLength());
        }
        catch (BadLocationException ble)
        {
        }
        for (int i = 0; i < iRealCaretPosition; i++)
        {
            if (valuableCharacter(sTmp.charAt(i)))
            {
                ++iValuableCaretPosition;
            }
        }
        return (iValuableCaretPosition);
    }

    private void setValuableCaretPosition(int iValuableCaretPosition)
    {
        String sTmp = "";
        try
        {
            sTmp = getText(0, getLength());
        }
        catch (BadLocationException ble)
        {
        }
        int p = 0;
        for (int i = 0; i < sTmp.length(); i++)
        {
            if (p == iValuableCaretPosition)
            {
                ctf.setCaretPosition(i);
                return;
            }
            if (valuableCharacter(sTmp.charAt(i)))
            {
                ++p;
            }
        }
    }

    private void format(AttributeSet a, int iNewCaretPosition) throws BadLocationException
    {
        String sRetVal = "";
        int j = 0;
        int iMax = 0;
        String sTmp = getText(0, getLength());
        String sSign = "";
        String sTail = "";
        String sDecSep = sDecimalSeparator; //2010.05.20
        if ((sTmp.length() > 0) && (sTmp.substring(0, 1).equals("-")))
        {
            sTmp = sTmp.substring(1);
            sSign = "-";
        }
        sTmp = StringUtils.stringReplace(sTmp, sThousandSeparator, "");
        iMax = sTmp.indexOf(cDecimalSeparator);
        if (iMax < 0)
        {
            iMax = sTmp.length();
            sDecSep = ""; //2010.05.20
        }
        else
        {
            sTail = sTmp.substring(iMax + 1);
        }
        for (int i = iMax; i > 0; i--)
        {
            sRetVal = sTmp.substring(i - 1, i) + sRetVal;
            ++j;
            if (j == 3 && i > 1)
            {
                sRetVal = sThousandSeparator + sRetVal;
                j = 0;
            }
        }
        sRetVal = sSign + sRetVal + sDecSep + sTail; //2010.05.20
        super.remove(0, getLength());
        super.insertString(0, sRetVal, a);
        setValuableCaretPosition(iNewCaretPosition);
    }

    private boolean checkPrecision() throws BadLocationException
    {
        String sTmp = getText(0, getLength());
        int j = 0;
        for (int i = 0; i < sTmp.length(); i++)
        {
            if (Character.isDigit(sTmp.charAt(i)))
            {
                ++j;
            }
        }
        if ((j > 0) && (j >= iPrecision))
        {
            return (false);
        }
        return (true);
    }

    private boolean checkScale() throws BadLocationException
    {
        String sTmp = getText(0, getLength());
        int iCaretPosition = ctf.getCaretPosition();
        int iPos = sTmp.indexOf(cDecimalSeparator);
        if (iPos < 0)
        {
            return (true);
        }
        if (iCaretPosition < iPos)
        {
            return (true);
        }
        sTmp = sTmp.substring(iPos);
        if (sTmp.length() > iScale)
        {
            return (false);
        }
        return (true);
    }

    private boolean checkScale2()
    {
        if (iScale < 1)
        {
            return (false);
        }
        int iCaretPosition = ctf.getCaretPosition();
        String sTmp = "";
        try
        {
            sTmp = getText(0, getLength());
        }
        catch (BadLocationException ble)
        {
        }
        int p = 0;
        for (int i = iCaretPosition; i < sTmp.length(); i++)
        {
            if (Character.isDigit(sTmp.charAt(i)))
            {
                ++p;
            }
        }
        if (p > iScale)
        {
            return (false);
        }
        return (true);
    }

    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException
    {
        if (str == null)
        {
            return;
        }
        char[] source = str.toCharArray();
        char[] result = new char[source.length];
        int j = 0;
        boolean bSign = false;
        String sTmp = "";

        int iValuableCaretPosition = getValuableCaretPosition();

        for (int i = 0; i < result.length; i++)
        {
            if (Character.isDigit(source[i]) && checkPrecision() && checkScale())
            {
                result[j++] = source[i];
            }
            else if (source[i] == '-')
            {
                bSign = true;
            }
            else if (source[i] == cDecimalSeparator && checkPrecision())
            {
                sTmp = getText(0, getLength()) + result;
                if (sTmp.indexOf(cDecimalSeparator) < 0)
                {
                    if (checkScale2())
                    {
                        result[j++] = source[i];
                    }
                    else
                    {
                        Toolkit.getDefaultToolkit().beep();
                    }
                }
                else
                {
                    Toolkit.getDefaultToolkit().beep();
                }
            }
            else
            {
                Toolkit.getDefaultToolkit().beep();
            }
        }
        super.insertString(offs, new String(result, 0, j), a);
        if (bSign)
        {
            if ((getLength() > 0) && (getText(0, 1).equals("-")))
            {
                super.remove(0, 1);
            }
            else
            {
                super.insertString(0, "-", a);
            }
        }
        format(a, iValuableCaretPosition + j);
    }

    public void remove(int offs, int len) throws BadLocationException
    {
        int iValuableCaretPosition = getValuableCaretPosition();
        String sDel = getText(offs, len);
        if (sDel.equals(sThousandSeparator))
        {
            if (ctf.isVkDelete())
            {
                ++len;
            }
            if (ctf.isVkBackSpace())
            {
                --offs;
            }
        }
        super.remove(offs, len);
        if (ctf.isVkDelete())
        {
            format(null, iValuableCaretPosition);
        }
        if (ctf.isVkBackSpace())
        {
            format(null, iValuableCaretPosition - len);
        }
    }
}
