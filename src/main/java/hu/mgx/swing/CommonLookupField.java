package hu.mgx.swing;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;

import javax.swing.*;

import hu.mgx.app.common.*;
import hu.mgx.db.*;

public class CommonLookupField extends JComboBox implements DataField
{

    private FieldDefinition fieldDefinition;
    private JComboBox valueComboBox;
    private SimpleDateFormat simpleDateFormat;
    private int iOrigin;
    private int iPrevious;
    private FormatInterface mgxFormat;
    private boolean bEmptyTop = false;
    private boolean bProgramSelect = false;
    private boolean b9Selectable = false;

    public CommonLookupField(FieldDefinition fieldDefinition, FormatInterface mgxFormat)
    {
        super();
        this.fieldDefinition = fieldDefinition;
        this.mgxFormat = mgxFormat;
        init();
    }

    public CommonLookupField(FieldDefinition fieldDefinition, FormatInterface mgxFormat, KeyListener keyListener)
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

    public CommonLookupField(FieldDefinition fieldDefinition, FormatInterface mgxFormat, KeyListener keyListener, boolean bEmptyTop)
    {
        super();
        this.fieldDefinition = fieldDefinition;
        this.mgxFormat = mgxFormat;
        this.bEmptyTop = bEmptyTop;
        init();
        if (keyListener != null)
        {
            this.addKeyListener(keyListener);
        }
    }

    public CommonLookupField(FieldDefinition fieldDefinition, FormatInterface mgxFormat, KeyListener keyListener, Dimension dimension)
    {
        this(fieldDefinition, mgxFormat, keyListener);
        setSize(dimension);
    }

    public CommonLookupField(FieldDefinition fieldDefinition, FormatInterface mgxFormat, KeyListener keyListener, int iWidth, int iHeight)
    {
        this(fieldDefinition, mgxFormat, keyListener);
        setSize(new Dimension(iWidth, iHeight));
    }

    public CommonLookupField(FieldDefinition fieldDefinition, FormatInterface mgxFormat, KeyListener keyListener, int iWidth, int iHeight, boolean bEmptyTop)
    {
        this(fieldDefinition, mgxFormat, keyListener, bEmptyTop);
        setSize(new Dimension(iWidth, iHeight));
    }

    private void init()
    {
        bProgramSelect = true;
        simpleDateFormat = mgxFormat.getDateFormat();
        valueComboBox = new JComboBox();
        addFocusListener(new FocusListener()
                 {

                     public void focusGained(FocusEvent e)
                     {
                         e.getComponent().setBackground(ColorManager.inputBackgroundFocusGained());
                     }

                     public void focusLost(FocusEvent e)
                     {
                         e.getComponent().setBackground(ColorManager.inputBackgroundFocusLost());
                     }
                 });
        addItemListener(new ItemListener()
                {

                    public void itemStateChanged(ItemEvent e)
                    {
                        //if (!bProgramSelect)
                        if (!bProgramSelect && !b9Selectable)
                        {
                            if (e.getStateChange() == e.SELECTED)
                            {
                                if ((getSelectedIndex() > -1) && (getSelectedIndex() != iOrigin))
                                {
                                    String s = getItemAt(getSelectedIndex()).toString();
                                    if ((s.length() > 0) && (s.charAt(0) == '['))
                                    {
                                        setSelectedIndex(iPrevious);
                                    }
                                }
                                iPrevious = getSelectedIndex();
                            }
                        }

                        setColor();
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
                   }

                   public void keyTyped(KeyEvent e)
                   {
                   }
               });
        setLookup(fieldDefinition.getLookup());
        if (this.getItemCount() > 0)
        {
            setSelected(0);
        }
        setBackground(ColorManager.inputBackgroundFocusLost());
        setColor();
        bProgramSelect = false;
    }

    private void setSelected(int iIndex)
    {
        bProgramSelect = true;
        setSelectedIndex(iIndex);
        iOrigin = iIndex;
        iPrevious = iIndex;
        bProgramSelect = false;
    }

    public void set9Selectable(boolean b9Selectable)
    {
        this.b9Selectable = b9Selectable;
    }
    
    private Object convertValue(String sValue)
    {
        int i;
        double d;
        java.util.Date utilDate;
        if (fieldDefinition.getType() == FieldType.INT)
        {
            if (sValue.equals(""))
            {
                return (null);
            }
            try
            {
                i = Integer.parseInt(sValue);
            }
            catch (NumberFormatException e)
            {
                i = 0;
            }
            return (new Integer(i));
        }
        if (fieldDefinition.getType() == FieldType.DECIMAL)
        {
            if (sValue.equals(""))
            {
                return (null);
            }
            try
            {
                d = Double.parseDouble(sValue);
            }
            catch (NumberFormatException e)
            {
                d = 0.0;
            }
            return (new Double(d));
        }
        if (fieldDefinition.getType() == FieldType.DATE)
        {
            if (sValue.equals(""))
            {
                return (null);
            }
            try
            {
                utilDate = simpleDateFormat.parse(sValue);
            }
            catch (ParseException e)
            {
                utilDate = new java.util.Date();
            }
            return (new java.sql.Date(utilDate.getTime()));
        }
        return (sValue);
    }

    public void addItem(String sValue, String sDisplay)
    {
        addItem(new String(sDisplay));
        valueComboBox.addItem(convertValue(sValue));
    }

    public void setLookup(String sLookup)
    {
        String sNext = "";
        String sValue = "";
        String sDisplay = "";

        //valueComboBox.removeAll();
        //removeAll();
        removeAllItems(); //new 20050517
        valueComboBox.removeAllItems(); //new 20050517
        StringTokenizer st = new StringTokenizer(sLookup, "@", false);
        if (bEmptyTop)
        {
            addItem("", "");
        }
        while (st.hasMoreTokens())
        {
            sNext = st.nextToken();//.trim();
            StringTokenizer st1 = new StringTokenizer(sNext, "|", false);
            if (st1.hasMoreTokens())
            {
                sValue = "";
                sDisplay = "";
                if (st1.hasMoreTokens())
                {
                    sValue = st1.nextToken().trim();
                }
                if (st1.hasMoreTokens())
                {
                    sDisplay = st1.nextToken().trim();
                }
                addItem(sValue, sDisplay);
            }
        }
        return;
    }

    public Object getValue()
    {
        if (getSelectedIndex() < 0 || getItemCount() == 0)
        {
            return (null);
        }
        return (valueComboBox.getItemAt(getSelectedIndex()));
    }

    //2009.02.01 új
    public Object getDisplayValue()
    {
        if (getSelectedIndex() < 0 || getItemCount() == 0)
        {
            return (null);
        }
        return (this.getItemAt(getSelectedIndex()));
    }

    public void setValue(Object o)
    {
        if (o == null)
        {
            if (this.getItemCount() > 0)
            {
                setSelected(0);
            }
            else
            {
                setSelected(-1);
            }
            setColor();
            return;
        }
        for (int i = 0; i < valueComboBox.getItemCount(); i++)
        {
            if (valueComboBox.getItemAt(i) != null)
            {
                if (valueComboBox.getItemAt(i).toString().equals(o.toString()))
                {
                    setSelected(i);
                    setColor();
                    return;
                }
            }
        }
        if (this.getItemCount() > 0)
        {
            setSelected(0);
        }
        setColor();
    }

    public void setDisplay(Object o)
    {
        if (o == null)
        {
            if (this.getItemCount() > 0)
            {
                setSelected(0);
            }
            else
            {
                setSelected(-1);
            }
            setColor();
            return;
        }
        for (int i = 0; i < this.getItemCount(); i++)
        {
            if (this.getItemAt(i).toString().equals(o.toString()))
            {
                setSelected(i);
                setColor();
                return;
            }
        }
        if (this.getItemCount() > 0)
        {
            setSelected(0);
        }
        setColor();
    }

    public void setFocus()
    {
        requestFocus();
    }

    private void setColor()
    {
        if (changed())
        {
            setForeground(ColorManager.inputForegroundChanged());
        }
        else
        {
            setForeground(ColorManager.inputForegroundNormal());
        }
    }

    public boolean changed()
    {
        if (getSelectedIndex() != iOrigin)
        {
            return (true);
        }
        return (false);
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
        setSelectedIndex(iOrigin);
        setColor();
    }

    public void setOriginToActual()
    {
        iOrigin = getSelectedIndex();
        iPrevious = getSelectedIndex();
        setColor();
    }

    public Object getOldValue()
    {
        if (iOrigin < 0 || getItemCount() == 0)
        {
            return (null);
        }
        return (valueComboBox.getItemAt(iOrigin));
    }
}
