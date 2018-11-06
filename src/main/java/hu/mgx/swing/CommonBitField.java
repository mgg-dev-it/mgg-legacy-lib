package hu.mgx.swing;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import hu.mgx.app.common.*;
import hu.mgx.db.*;

public class CommonBitField extends JCheckBox implements DataField
{

    private FieldDefinition fieldDefinition;
    private boolean bOrigin = false;
    private String sFieldLabel = "";

    public CommonBitField(FieldDefinition fieldDefinition, KeyListener keyListener)
    {
        super();
        this.fieldDefinition = fieldDefinition;
        this.sFieldLabel = fieldDefinition.getDisplayName();
        init();
        if (keyListener != null)
        {
            this.addKeyListener(keyListener);
        }
    }

    public CommonBitField(String sFieldLabel, KeyListener keyListener)
    {
        super();
        this.sFieldLabel = sFieldLabel;
        init();
        if (keyListener != null)
        {
            this.addKeyListener(keyListener);
        }
    }

    public CommonBitField(KeyListener keyListener)
    {
        super();
        init();
        if (keyListener != null)
        {
            this.addKeyListener(keyListener);
        }
    }

    private void init()
    {
        setBorder(new EmptyBorder(0, 0, 0, 0));
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
        setHorizontalAlignment(SwingConstants.CENTER);
        setBackground(ColorManager.inputBackgroundFocusLost());
        setColor();
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
        return (bOrigin != isSelected());
    }

    public Object getValue()
    {
        return (isSelected() ? new Integer(1) : new Integer(0));
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
        if (o == null)
        {
            setSelected(false);
            return;
        }
        if (o.toString().equals("0"))
        {
            setSelected(false);
        }
        if (o.toString().equals("1"))
        {
            setSelected(true);
        }
        if (o.toString().equals("false"))
        {
            setSelected(false);
        }
        if (o.toString().equals("true"))
        {
            setSelected(true);
        }
        bOrigin = isSelected();
        setColor();
    }

    public boolean check()
    {
        return (true);
    }

    public JLabel getFieldLabel()
    {
        return (new JLabel(" " + sFieldLabel + ":"));
    }

    public void setActualToOrigin()
    {
        setSelected(bOrigin);
        setColor();
    }

    public void setOriginToActual()
    {
        bOrigin = isSelected();
        setColor();
    }

    public Object getOldValue()
    {
        return (bOrigin ? new Integer(1) : new Integer(0));
    }
}
