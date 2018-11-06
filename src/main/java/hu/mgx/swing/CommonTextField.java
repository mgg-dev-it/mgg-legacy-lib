package hu.mgx.swing;

import java.awt.*;
import java.awt.event.*;
import java.text.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;

import hu.mgx.app.common.*;
import hu.mgx.db.*;
import hu.mgx.util.*;
import hu.mgx.swing.text.*;

public class CommonTextField extends JTextField implements DataField
{

    protected FieldDefinition fieldDefinition;
    protected SimpleDateFormat simpleDateFormat;
    protected SimpleDateFormat simpleDateTimeFormat;
    protected boolean bVkDelete = false;
    protected boolean bVkBackSpace = false;
    protected String sGroupingSeparator = "";
    protected String sDateSeparator = "";
    protected Object oOrigin = null;
    protected AppInterface appInterface;
    protected Color colorNormal = ColorManager.inputForegroundNormal();
    protected Color colorChanged = ColorManager.inputForegroundChanged();
    protected DecimalFormat decimalFormat = new DecimalFormat("#,##0");
    protected DecimalFormatSymbols decimalFormatSymbols;
    protected String sDecimals = "";
    protected boolean bWindow = false;
    protected int iOffset = 0;

    public CommonTextField(FieldDefinition fieldDefinition, AppInterface appInterface)
    {
        super();
        this.fieldDefinition = fieldDefinition;
        this.appInterface = appInterface;
        init();
    }

    public CommonTextField(FieldDefinition fieldDefinition, AppInterface appInterface, KeyListener keyListener)
    {
        super();
        this.fieldDefinition = fieldDefinition;
        this.appInterface = appInterface;
        init();
        if (keyListener != null)
        {
            this.addKeyListener(keyListener);
        }
    }

    public CommonTextField(FieldDefinition fieldDefinition, AppInterface appInterface, KeyListener keyListener, Dimension dimension)
    {
        this(fieldDefinition, appInterface, keyListener);
        setSize(dimension);
    }

    public CommonTextField(FieldDefinition fieldDefinition, AppInterface appInterface, KeyListener keyListener, int iWidth, int iHeight)
    {
        this(fieldDefinition, appInterface, keyListener);
        setSize(new Dimension(iWidth, iHeight));
    }

    public CommonTextField(FieldDefinition fieldDefinition, AppInterface appInterface, int columns)
    {
        super(columns);
        this.fieldDefinition = fieldDefinition;
        this.appInterface = appInterface;
        init();
    }

    protected void init()
    {
        simpleDateFormat = appInterface.getDateFormat();
        simpleDateTimeFormat = appInterface.getDateTimeFormat();
        sDateSeparator = new String(new StringBuffer().append(appInterface.getDateSeparator()));
        sGroupingSeparator = new String(new StringBuffer().append(appInterface.getGroupingSeparator()));
        sDecimals = "";
        if (fieldDefinition.getScale() > 0)
        {
            while (sDecimals.length() < fieldDefinition.getScale())
            {
                sDecimals = sDecimals + "0";
            }
            sDecimals = "." + sDecimals;
        }
        sDecimals = "#,##0" + sDecimals;
        decimalFormat = new DecimalFormat(sDecimals);
        decimalFormatSymbols = decimalFormat.getDecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator(appInterface.getDecimalSeparator());
        decimalFormatSymbols.setGroupingSeparator(appInterface.getGroupingSeparator());
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
        if (fieldDefinition.getType() == FieldType.CHAR)
        {
            setDocument(new CharacterDocument(fieldDefinition.getLength(), fieldDefinition.isUppercase()));
        }
        if (fieldDefinition.getType() == FieldType.INT)
        {
            setDocument(new DecimalDocument(this, appInterface, fieldDefinition.getPrecision(), fieldDefinition.getScale()));
            setHorizontalAlignment(JTextField.RIGHT);
        }
        if (fieldDefinition.getType() == FieldType.DECIMAL)
        {
            setDocument(new DecimalDocument(this, appInterface, fieldDefinition.getPrecision(), fieldDefinition.getScale()));
            setHorizontalAlignment(JTextField.RIGHT);
        }
        if (fieldDefinition.getType() == FieldType.DATE)
        {
            setDocument(new DateDocument(this, appInterface));
        }
        if (fieldDefinition.getType() == FieldType.DATETIME)
        {
            //@todo datetimedocument
            //setHorizontalAlignment(JTextField.RIGHT);
        }

        addFocusListener(new FocusListener()
                 {

                     public void focusGained(FocusEvent e)
                     {
                         e.getComponent().setBackground(ColorManager.inputBackgroundFocusGained());
                         ((CommonTextField) e.getComponent()).selectAll();
                     }

                     public void focusLost(FocusEvent e)
                     {
                         e.getComponent().setBackground(ColorManager.inputBackgroundFocusLost());
                         if (fieldDefinition.getType() == FieldType.DATE)
                         {
                             checkDate();
                         }
                     }
                 });
        addKeyListener(new KeyListener()
               {

                   public void keyPressed(KeyEvent e)
                   {
                       int iCaretPosition = 0;
                       bVkDelete = false;
                       bVkBackSpace = false;
                       if (e.getKeyCode() == KeyEvent.VK_LEFT && isFormattedField())
                       {
                           iCaretPosition = getCaretPosition();
                           if (iCaretPosition > 1)
                           {
                               try
                               {
                                   if (isSeparator(getText(iCaretPosition - 2, 1)))
                                   {
                                       setCaretPosition(iCaretPosition - 1);
                                   }
                                   else if (isSeparator(getText(iCaretPosition - 1, 1)))
                                   {
                                       setCaretPosition(iCaretPosition - 1);
                                   }
                               }
                               catch (BadLocationException ble)
                               {
                                   //do nothing
                                   System.err.println(ble.getLocalizedMessage());
                                   ble.printStackTrace(System.err);
                               }
                           }
                       }
                       if (e.getKeyCode() == KeyEvent.VK_RIGHT && isFormattedField())
                       {
                           iCaretPosition = getCaretPosition();
                           if (iCaretPosition < getText().length())
                           {
                               try
                               {
                                   if (isSeparator(getText(iCaretPosition + 1, 1)))
                                   {
                                       setCaretPosition(iCaretPosition + 1);
                                   }
                                   else if (isSeparator(getText(iCaretPosition, 1)))
                                   {
                                       setCaretPosition(iCaretPosition + 1);
                                   }
                               }
                               catch (BadLocationException ble)
                               {
                                   //do nothing
                                   System.err.println(ble.getLocalizedMessage());
                                   ble.printStackTrace(System.err);
                               }
                           }
                       }
                       if (e.getKeyCode() == KeyEvent.VK_DELETE)
                       {
                           bVkDelete = true;
                       }
                       if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
                       {
                           bVkBackSpace = true;
                       }
                       if (e.getKeyCode() == KeyEvent.VK_ENTER)
                       {
                           if (fieldDefinition.getType() == FieldType.DATE)
                           {
                               checkDate();
                           }
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
        setDisabledTextColor(new Color(0, 0, 0));
    }

    public void setBorder(Border b)
    {
        super.setBorder(b);
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
        String sTmp = getText();
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
            if (!getText().equals(""))
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

    private boolean isFormattedField()
    {
        if (fieldDefinition.getType() == FieldType.INT)
        {
            return (true);
        }
        if (fieldDefinition.getType() == FieldType.DECIMAL)
        {
            return (true);
        }
        if (fieldDefinition.getType() == FieldType.DATE)
        {
            return (true);
        }
        return (false);
    }

    private boolean isSeparator(String s)
    {
        if (s.equals(sGroupingSeparator))
        {
            return (true);
        }
        if (s.equals(sDateSeparator))
        {
            return (true);
        }
        return (false);
    }

    public boolean isVkDelete()
    {
        return (bVkDelete);
    }

    public boolean isVkBackSpace()
    {
        return (bVkBackSpace);
    }

    public void setWindow(int iOffset)
    {
        this.iOffset = iOffset;
        bWindow = true;
    }

    private void checkDate()
    { //@todo a formátumból lehetne vezérelni???
        //@todo checkdatetime nem kell???
        //String sNow = simpleDateTimeFormat.format(new java.util.Date());
        String sToday = simpleDateFormat.format(new java.util.Date());
        String sTmp = getText();

        boolean bAuto = false;
        java.util.GregorianCalendar gc = new java.util.GregorianCalendar();
        gc = new java.util.GregorianCalendar(gc.get(java.util.GregorianCalendar.YEAR), gc.get(java.util.GregorianCalendar.MONTH), gc.get(java.util.GregorianCalendar.DAY_OF_MONTH));

        if (sTmp.length() == 1)
        {
            if (Character.isDigit(sTmp.charAt(0)))
            {
                setText(sToday.substring(0, 8) + "0" + sTmp);
                bAuto = true;
            }
        }
        if (sTmp.length() == 2)
        {
            if (Character.isDigit(sTmp.charAt(0)) && Character.isDigit(sTmp.charAt(1)))
            {
                setText(sToday.substring(0, 8) + sTmp);
                bAuto = true;
            }
        }
        if (sTmp.length() == 3)
        {
            if (Character.isDigit(sTmp.charAt(0)) && Character.isDigit(sTmp.charAt(1)) && Character.isDigit(sTmp.charAt(2)))
            {
                setText(sToday.substring(0, 4) + "0" + sTmp.substring(0, 1) + sDateSeparator + sTmp.substring(1, 3));
                bAuto = true;
            }
        }
        if (sTmp.length() == 4)
        {
            if (Character.isDigit(sTmp.charAt(0)) && Character.isDigit(sTmp.charAt(1)) && Character.isDigit(sTmp.charAt(2)) && Character.isDigit(sTmp.charAt(3)))
            {
                setText(sToday.substring(0, 4) + sTmp.substring(0, 2) + sDateSeparator + sTmp.substring(2, 4));
                bAuto = true;
            }
        }
        try
        {
            java.util.Date utilDate = simpleDateFormat.parse(getText());
            if (bAuto && bWindow)
            {
                gc.add(java.util.GregorianCalendar.DATE, iOffset);
                if (utilDate.compareTo(gc.getTime()) < 0)
                {
                    gc.setTime(utilDate);
                    gc.add(java.util.GregorianCalendar.YEAR, 1);
                    utilDate.setTime(gc.getTime().getTime());
                    setText(simpleDateFormat.format(utilDate));
                }
            }
        }
        catch (ParseException e)
        {
            //@todo MsgBox "Hibás dátumot (" + sTmp + ") adott meg", vbExclamation
        }
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
        int i;
        double d;
        java.util.Date utilDate;
        if (fieldDefinition.getType() == FieldType.INT)
        {
            if (getText().equals(""))
            {
                return (null);
            }
            try
            {
                i = Integer.parseInt(hu.mgx.util.StringUtils.stringReplace(getText(), sGroupingSeparator, "")); //@todo filter funkció kellene, amelyik csak a számokat, elõjelet és a tizedespontot engedi át...
            }
            catch (NumberFormatException e)
            {
                i = 0;
            }
            return (new Integer(i));
        }
        if (fieldDefinition.getType() == FieldType.DECIMAL)
        {
            if (getText().equals(""))
            {
                return (null);
            }
            try
            {
                d = Double.parseDouble(hu.mgx.util.StringUtils.stringReplace(getText(), sGroupingSeparator, "")); //@todo filter funkció kellene, amelyik csak a számokat, elõjelet és a tizedespontot engedi át...
            }
            catch (NumberFormatException e)
            {
                d = 0.0;
            }
            return (new Double(d));
        }
        if (fieldDefinition.getType() == FieldType.DATE)
        {
            if (getText().equals(""))
            {
                return (null);
            }
            try
            {
                utilDate = simpleDateFormat.parse(getText());
            }
            catch (ParseException e)
            {
                utilDate = new java.util.Date();
            }
            return (utilDate);
        }
        if (fieldDefinition.getType() == FieldType.DATETIME)
        {
            if (getText().equals(""))
            {
                return (null);
            }
            try
            {
                utilDate = simpleDateTimeFormat.parse(getText());
            }
            catch (ParseException e)
            {
                utilDate = new java.util.Date();
            }
            return (utilDate);
        }
        return (getText());
    }

    public boolean check()
    {
        if (fieldDefinition.getType() == FieldType.DATE)
        {
            checkDate();
        }
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

    public void setMaxLength(int iMaxLength)
    {
        if (fieldDefinition.getType() == FieldType.CHAR)
        {
            ((CharacterDocument) getDocument()).setMaxLength(iMaxLength);
            if (getText().length() > iMaxLength)
            {
                setText(getText().substring(0, iMaxLength));
            }
        }
    }

    public Object getOldValue()
    {
        return (oOrigin);
    }
}
