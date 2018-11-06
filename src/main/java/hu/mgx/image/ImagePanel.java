package hu.mgx.image;

import java.awt.*;

import javax.swing.*;

public class ImagePanel extends JPanel
{

    private ImageIcon imageIcon;

    public ImagePanel(ImageIcon imageIcon)
    {
        super();
        this.imageIcon = imageIcon;
        setBorder(BorderFactory.createEmptyBorder());
        setSize(new Dimension(imageIcon.getIconWidth(), imageIcon.getIconHeight()));
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if (imageIcon != null)
        {
            g.drawImage(imageIcon.getImage(), 1, 1, imageIcon.getIconWidth(), imageIcon.getIconHeight(), this);
        }
        g.setColor(Color.RED);
        g.drawRect(10, 10, 10, 10);
    }

    public void setSize(java.awt.Dimension d)
    {
        setMinimumSize(d);
        setPreferredSize(d);
        setMaximumSize(d);
    }
}
