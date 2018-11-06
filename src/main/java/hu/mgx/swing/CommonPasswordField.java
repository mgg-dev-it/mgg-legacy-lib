package hu.mgx.swing;

import java.awt.*;
import java.awt.event.*;
import java.text.*;

import javax.swing.*;
import javax.swing.text.*;

import hu.mgx.app.common.*;
import hu.mgx.db.*;
import hu.mgx.util.*;
import hu.mgx.swing.text.*;

public class CommonPasswordField extends JPasswordField implements DataField
{

    private FieldDefinition fieldDefinition;
    private SimpleDateFormat simpleDateFormat;
    private SimpleDateFormat simpleDateTimeFormat;
    private boolean bVkDelete = false;
    private boolean bVkBackSpace = false;
    private String sGroupingSeparator = "";
    private String sDateSeparator = "";
    private Object oOrigin = null;
    private FormatInterface mgxFormat;
    private Color colorNormal = ColorManager.inputForegroundNormal();
    private Color colorChanged = ColorManager.inputForegroundChanged();
    private DecimalFormat decimalFormat = new DecimalFormat("#,##0");

    public CommonPasswordField(FieldDefinition fieldDefinition, FormatInterface mgxFormat)
    {
        super();
        this.fieldDefinition = fieldDefinition;
        this.mgxFormat = mgxFormat;
        init();
    }

    public CommonPasswordField(FieldDefinition fieldDefinition, FormatInterface mgxFormat, KeyListener keyListener)
    {
        super();
        this.fieldDefinition = fieldDefinition;
        this.mgxFormat = mgxFormat;
        init();
        if (keyListener != null)
        {
            this.addKeyListener(keyListener);
        }
    }

    public CommonPasswordField(FieldDefinition fieldDefinition, FormatInterface mgxFormat, KeyListener keyListener, Dimension dimension)
    {
        this(fieldDefinition, mgxFormat, keyListener);
        setSize(dimension);
    }

    public CommonPasswordField(FieldDefinition fieldDefinition, FormatInterface mgxFormat, KeyListener keyListener, int iWidth, int iHeight)
    {
        this(fieldDefinition, mgxFormat, keyListener);
        setSize(new Dimension(iWidth, iHeight));
    }

    public CommonPasswordField(FieldDefinition fieldDefinition, FormatInterface mgxFormat, int columns)
    {
        super(columns);
        this.fieldDefinition = fieldDefinition;
        this.mgxFormat = mgxFormat;
        init();
    }

    private void init()
    {
        simpleDateFormat = mgxFormat.getDateFormat();
        simpleDateTimeFormat = mgxFormat.getDateTimeFormat();
        sDateSeparator = new String(new StringBuffer().append(mgxFormat.getDateSeparator()));
        sGroupingSeparator = new String(new StringBuffer().append(mgxFormat.getGroupingSeparator()));
        String sDecimals = "";
        if (fieldDefinition.getScale() > 0)
        {
            while (sDecimals.length() < fieldDefinition.getScale())
            {
                sDecimals = sDecimals + "0";
            }
            sDecimals = "." + sDecimals;
        }
        decimalFormat = new DecimalFormat("#,##0" + sDecimals);
        DecimalFormatSymbols decimalFormatSymbols = decimalFormat.getDecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator(mgxFormat.getDecimalSeparator());
        decimalFormatSymbols.setGroupingSeparator(mgxFormat.getGroupingSeparator());
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
        if (fieldDefinition.getType() == FieldType.CHAR)
        {
            setDocument(new CharacterDocument(fieldDefinition.getLength(), fieldDefinition.isUppercase()));
        }

        addFocusListener(new FocusListener()
                 {

                     public void focusGained(FocusEvent e)
                     {
                         e.getComponent().setBackground(ColorManager.inputBackgroundFocusGained());
                         ((CommonPasswordField) e.getComponent()).selectAll();
                     }

                     public void focusLost(FocusEvent e)
                     {
                         e.getComponent().setBackground(ColorManager.inputBackgroundFocusLost());
                     }
                 });
        addKeyListener(new KeyListener()
               {

                   public void keyPressed(KeyEvent e)
                   {
                       if (e.getKeyCode() == KeyEvent.VK_ENTER)
                       {
                           transferFocus();
                       }
                   }

                   public void keyReleased(KeyEvent e)
                   {
                       color();
                   }

                   public void keyTyped(KeyEvent e)
                   {
                   }
               });
        setBackground(ColorManager.inputBackgroundFocusLost());
        setForeground(colorNormal);
    }

    public void setForegroundColorNormal(Color c)
    {
        colorNormal = c;
        color();
    }

    public void setForegroundColorChanged(Color c)
    {
        colorChanged = c;
        color();
    }

    public boolean changed()
    {
        String sTmp = new String(getPassword());
        if (oOrigin != null)
        {
            if (fieldDefinition.getType() == FieldType.DATE)
            {
                if (!simpleDateFormat.format(oOrigin).equals(sTmp))
                {
                    return (true);
                }
            }
            else if (fieldDefinition.getType() == FieldType.DATETIME)
            {
                if (!simpleDateTimeFormat.format(oOrigin).equals(sTmp))
                {
                    return (true);
                }
            }
            else if (fieldDefinition.getType() == FieldType.INT)
            {
                sTmp = StringUtils.stringReplace(sTmp, sGroupingSeparator, "");
                if (!oOrigin.toString().equals(sTmp))
                {
                    return (true);
                }
            }
            else
            {
                if (!oOrigin.toString().equals(sTmp))
                {
                    return (true);
                }
            }
        }
        else
        {
            if (!new String(getPassword()).equals(""))
            {
                return (true);
            }
        }
        return (false);
    }

    private void color()
    {
        if (changed())
        {
            setForeground(colorChanged);
        }
        else
        {
            setForeground(colorNormal);
        }
    }

    protected boolean isVkDelete()
    {
        return (bVkDelete);
    }

    protected boolean isVkBackSpace()
    {
        return (bVkBackSpace);
    }

    protected Document createDefaultModel()
    {
        return new PlainDocument();
    }

    public void setFocus()
    {
        requestFocus();
    }

    public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);
        if (enabled)
        {
            setBackground(ColorManager.inputBackgroundFocusLost());
        }
        else
        {
            setBackground(ColorManager.inputBackgroundDisabled());
        }
    }

    public void setValue(Object o)
    {
        oOrigin = o;
        setForeground(colorNormal);
        if (o == null)
        {
            setText("");
            return;
        }
        if (fieldDefinition.getType() == FieldType.INT)
        {
            if (o.toString().equals(""))
            {
                setText("");
                return;
            }
            setText(o.toString()); //DecimalDocument majd formázza
            return;
        }
        if (fieldDefinition.getType() == FieldType.DECIMAL)
        {
            if (o.toString().equals(""))
            {
                setText("");
                return;
            }
            setText(o.toString()); //DecimalDocument majd formázza
            return;
        }
        if (fieldDefinition.getType() == FieldType.DATE)
        {
            if (o.toString().equals(""))
            {
                setText("");
                return;
            }
            setText(simpleDateFormat.format(o));
            return;
        }
        if (fieldDefinition.getType() == FieldType.DATETIME)
        {
            if (o.toString().equals(""))
            {
                setText("");
                return;
            }
            setText(simpleDateTimeFormat.format(o));
            return;
        }
        setText(o.toString());
    }

    public Object getValue()
    {
        return (new String(getPassword()));
    }

    public boolean check()
    {
        return (true);
    }

    public void setSize(java.awt.Dimension d)
    {
        setMinimumSize(d);
        setPreferredSize(d);
        setMaximumSize(d);
    }

    public javax.swing.JLabel getFieldLabel()
    {
        return (new JLabel(" " + fieldDefinition.getDisplayName() + ":"));
    }

    public javax.swing.JLabel getFieldLabel(String s)
    {
        return (new JLabel(" " + fieldDefinition.getDisplayName() + " " + s + ":"));
    }

    public void setActualToOrigin()
    {
        setValue(oOrigin);
        color();
    }

    public void setOriginToActual()
    {
        oOrigin = getValue();
        color();
    }

    public Object getOldValue()
    {
        return (oOrigin);
    }
}
