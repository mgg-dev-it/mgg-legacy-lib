package hu.mgx.swing.text;

import java.awt.*;
import javax.swing.text.*;

public class CharacterDocument extends PlainDocument
{

    private int iMaxLength = 0;
    private boolean bUpperCase;

    public CharacterDocument(int iMaxLength, boolean bUpperCase)
    {
        super();
        this.iMaxLength = iMaxLength;
        this.bUpperCase = bUpperCase;
    }

    private boolean checkPrecision() throws BadLocationException
    {
        int iActualLength = getLength();
        if ((iActualLength > 0) && (iActualLength >= iMaxLength))
        {
            return (false);
        }
        return (true);
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException
    {
        if (str == null)
        {
            return;
        }
        char[] source = str.toCharArray();
        char[] result = new char[source.length];
        int j = 0;

        for (int i = 0; i < result.length; i++)
        {
            if (checkPrecision())
            {
                result[j++] = (bUpperCase ? Character.toUpperCase(source[i]) : source[i]);
            }
            else
            {
                Toolkit.getDefaultToolkit().beep();
            }
        }
        super.insertString(offs, new String(result, 0, j), a);
    }

    public void setMaxLength(int iMaxLength)
    {
        this.iMaxLength = iMaxLength;
    }
}
