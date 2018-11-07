package hu.mgx.swing.text;

import java.awt.*;

import javax.swing.text.*;

import hu.mgx.app.common.*;
import hu.mgx.swing.*;
import hu.mgx.util.StringUtils;

public class DateDocument extends PlainDocument
{

    protected CommonTextField ctf;
    protected char cDateSeparator = ' ';
    protected String sDateSeparator = "";
    protected AppInterface appInterface;
    protected int iPrecision = 8; //@todo ezt a formátumból ki lehet deríteni???

    public DateDocument(CommonTextField ctf, AppInterface appInterface) throws NullPointerException
    {
        super();
        if (ctf == null)
        {
            throw new NullPointerException();
        }
        this.ctf = ctf;
        this.appInterface = appInterface;
        this.cDateSeparator = appInterface.getDateSeparator();
        this.sDateSeparator = new String(new StringBuffer().append(cDateSeparator));
    }

    private boolean valuableCharacter(char c)
    {
        if (Character.isDigit(c))
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

    protected void setValuableCaretPosition(int iValuableCaretPosition)
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

    protected void format(AttributeSet a, int iNewCaretPosition) throws BadLocationException
    {
        String sRetVal = "";
        int j = 0;
        String sTmp = getText(0, getLength());
        sTmp = StringUtils.stringReplace(sTmp, sDateSeparator, "");
        int l = sTmp.length();
        for (int i = 0; i < l; ++i)
        {
            sRetVal = sRetVal + sTmp.substring(i, i + 1);
            ++j;
            if ((j == 4 && l > 4) || (j == 6 && l > 6))
            { //@todo ezek az értékek (4, 6) a formátumból kideríthetők???
                sRetVal = sRetVal + sDateSeparator;
            }
        }
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

    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException
    {
        if (str == null)
        {
            return;
        }
        char[] source = str.toCharArray();
        char[] result = new char[source.length];
        int j = 0;
        int iValuableCaretPosition = getValuableCaretPosition();
        for (int i = 0; i < result.length; i++)
        {
            if (Character.isDigit(source[i]) && checkPrecision())
            {
                result[j++] = source[i];
            }
            else if (source[i] == cDateSeparator)
            {
                //do nothing
            }
            else
            {
                Toolkit.getDefaultToolkit().beep();
            }
        }
        super.insertString(offs, new String(result, 0, j), a);
        format(a, iValuableCaretPosition + j);
    }

    public void remove(int offs, int len) throws BadLocationException
    {
        int iValuableCaretPosition = getValuableCaretPosition();
        String sDel = getText(offs, len);
        if (sDel.equals(sDateSeparator))
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

    protected void superInsertString(int offs, String str, AttributeSet a) throws BadLocationException
    {
        super.insertString(offs, str, a);
    }

    protected void superRemove(int offs, int len) throws BadLocationException
    {
        super.remove(offs, len);
    }
}
