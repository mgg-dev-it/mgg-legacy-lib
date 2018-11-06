package hu.mgx.swing;

import java.awt.event.*;

import javax.swing.*;

import hu.mgx.app.common.*;
import hu.mgx.db.*;
import hu.mgx.swing.text.*;

public class CommonEditorField extends JEditorPane implements DataField
{

    private FieldDefinition fieldDefinition;
    private Object oOrigin = null;

    public CommonEditorField()
    {
        super();
        init();
    }

    public CommonEditorField(KeyListener keyListener)
    {
        super();
        init();
        if (keyListener != null)
        {
            this.addKeyListener(keyListener);
        }
    }

    public CommonEditorField(FieldDefinition fieldDefinition, KeyListener keyListener)
    {
        super();
        this.fieldDefinition = fieldDefinition;
        init();
        if (keyListener != null)
        {
            this.addKeyListener(keyListener);
        }
    }

    public CommonEditorField(String type, String text)
    {
        super(type, text);
        init();
    }

    private void init()
    {
        if (fieldDefinition != null)
        {
            if (fieldDefinition.getType() == FieldType.CHAR)
            {
                setDocument(new CharacterDocument(fieldDefinition.getLength(), fieldDefinition.isUppercase()));
            }
        }
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
        addKeyListener(new KeyListener()
               {

                   public void keyPressed(KeyEvent e)
                   {
                       if ((e.getModifiers() == KeyEvent.CTRL_MASK) && (e.getKeyCode() == KeyEvent.VK_ENTER))
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
    }

    public boolean changed()
    {
        String sTmp = getText();
        if (oOrigin != null)
        {
            if (!oOrigin.toString().equals(sTmp))
            {
                return (true);
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
            setForeground(ColorManager.inputForegroundChanged());
        }
        else
        {
            setForeground(ColorManager.inputForegroundNormal());
        }
    }

    public Object getValue()
    {
        return (getText());
    }

    public void setFocus()
    {
        requestFocus();
    }

    public void setValue(Object o)
    {
        oOrigin = o;
        setForeground(ColorManager.inputForegroundNormal());
        if (o == null)
        {
            setText("");
            return;
        }
        setText(o.toString());
    }

    public boolean check()
    {
        return (true);
    }

    public javax.swing.JLabel getFieldLabel()
    {
        return (new JLabel("fieldDefinition kell!"));
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

    public void addLine(String t)
    {
        setText(getText() + t + hu.mgx.util.StringUtils.sCrLf);

    }

    public Object getOldValue()
    {
        return (oOrigin);
    }
}
