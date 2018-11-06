package hu.mgx.app.swing;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

public class ToolbarButton extends JButton implements MouseListener
{

    public ToolbarButton(ImageIcon imageIcon, String sToolTipText, ActionListener actionListener, String sActionCommand)
    {
        super(imageIcon);
        this.setBorder(new BevelBorder(BevelBorder.RAISED));
        this.setBorderPainted(false);
        this.setFocusable(false);
        this.addMouseListener(this);
        this.setToolTipText(sToolTipText);
        this.addActionListener(actionListener);
        this.setActionCommand(sActionCommand);
    }

    public void mouseClicked(MouseEvent e)
    {
    }

    public void mouseEntered(MouseEvent e)
    {
        if (this.isEnabled())
        {
            this.setBorderPainted(true);
        }
    }

    public void mouseExited(MouseEvent e)
    {
        this.setBorderPainted(false);
    }

    public void mousePressed(MouseEvent e)
    {
    }

    public void mouseReleased(MouseEvent e)
    {
    }
}
