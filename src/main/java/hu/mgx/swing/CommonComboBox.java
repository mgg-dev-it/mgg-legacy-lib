package hu.mgx.swing;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import hu.mgx.app.common.*;

public class CommonComboBox extends JComboBox
{

    public CommonComboBox()
    {
        super();
        init();
    }

    public CommonComboBox(KeyListener keyListener)
    {
        super();
        init();
        if (keyListener != null)
        {
            this.addKeyListener(keyListener);
        }
    }

    public CommonComboBox(KeyListener keyListener, Dimension dimension)
    {
        this(keyListener);
        setSize(dimension);
    }

    public CommonComboBox(KeyListener keyListener, int iWidth, int iHeight)
    {
        this(keyListener);
        setSize(new Dimension(iWidth, iHeight));
    }

    private void init()
    {
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
        if (this.getItemCount() > 0)
        {
            setSelectedIndex(0);
        }
        setBackground(ColorManager.inputBackgroundFocusLost());
    }

    public void setFocus()
    {
        requestFocus();
    }

    public void setSize(java.awt.Dimension d)
    {
        setMinimumSize(d);
        setPreferredSize(d);
        setMaximumSize(d);
    }
}
