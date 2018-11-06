package hu.mgx.swing;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import hu.mgx.app.common.*;

public class CommonCheckBox extends JCheckBox
{

    public CommonCheckBox()
    {
        super();
        init();
    }

    public CommonCheckBox(String text)
    {
        super(text);
        init();
    }

    public CommonCheckBox(KeyListener keyListener)
    {
        super();
        init();
        if (keyListener != null)
        {
            this.addKeyListener(keyListener);
        }
        this.addItemListener(itemListener);
    }

    public CommonCheckBox(ItemListener itemListener)
    {
        super();
        init();
        if (itemListener != null)
        {
            this.addItemListener(itemListener);
        }
    }

    public CommonCheckBox(ItemListener itemListener, boolean bSelected)
    {
        super();
        init();
        if (itemListener != null)
        {
            this.addItemListener(itemListener);
        }
        this.setSelected(bSelected);
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
}
