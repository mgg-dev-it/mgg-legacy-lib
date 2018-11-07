package hu.mag.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;
import javax.swing.UIManager;

public class MagCheckBoxIcon implements Icon {

    private Color color = null;
    boolean bSelected = false;
    private int iWidth = 0;
    private int iHeight = 0;

    public MagCheckBoxIcon(Color color, boolean bSelected) {
        this.color = color;
        this.bSelected = bSelected;
        Icon icon = UIManager.getIcon("CheckBox.icon");
        iWidth = icon.getIconWidth();
        iHeight = icon.getIconHeight();
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(color);
        g.fillRect(x, y, iWidth, iHeight);
        g.setColor(Color.black);
        g.drawRect(x, y, iWidth, iHeight);
        if (bSelected) {
            //x: upper left corner, horizontal axis
            //y: upper left corner, vertical axis
            g.drawLine(x + iWidth / 2, y + iHeight - 3, x + iWidth - 3, y + 3);
            g.drawLine(x + iWidth / 2, y + iHeight - 3 + 1, x + iWidth - 3, y + 3 + 1);
            g.drawLine(x + iWidth / 2, y + iHeight - 3, x + 4, y + iHeight * 6 / 10);
            g.drawLine(x + iWidth / 2, y + iHeight - 3 + 1, x + 4, y + iHeight * 6 / 10 + 1);
        }
    }

    @Override
    public int getIconWidth() {
        return (iWidth);
    }

    @Override
    public int getIconHeight() {
        return (iHeight);
    }

}
