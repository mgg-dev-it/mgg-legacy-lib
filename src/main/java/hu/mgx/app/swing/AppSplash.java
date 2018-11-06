package hu.mgx.app.swing;

import java.awt.*;
import java.awt.image.*;

import javax.swing.*;

public class AppSplash extends JWindow implements ImageObserver
{

    private Image image;
    private JPanel jPanel;
    private MediaTracker tracker;

    public AppSplash(Image img, int iWidth, int iHeight)
    {
        init(img, iWidth, iHeight, "");
    }

    public AppSplash(Image img, int iWidth, int iHeight, String sTitle)
    {
        if (iWidth < 0)
        {
            iWidth = img.getWidth(this);
        }
        if (iHeight < 0)
        {
            iHeight = img.getWidth(this);
        }
        init(img, iWidth, iHeight, sTitle);
    }

    private void init(Image img, final int iWidth, final int iHeight, final String sTitle)
    {
        image = img;
        jPanel = new JPanel()
        {

            public void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                g.drawImage(image, 1, 1, iWidth, iHeight, this);
                ;
                g.setColor(new Color(216, 240, 255));
                g.drawRect(0, 0, iWidth + 1, iHeight + 1);
                if (!sTitle.trim().equals(""))
                {
                    g.setFont(new Font("Arial", Font.ITALIC, 16));
                    g.drawString(sTitle, 16, 30);
                }
            }
        };

        tracker = new MediaTracker(jPanel);
        tracker.addImage(image, 0);
        try
        {
            tracker.waitForAll();
        }
        catch (InterruptedException e)
        {
        }

        jPanel.setPreferredSize(new Dimension(iWidth + 2, iHeight + 3));
        setContentPane(jPanel);
        pack();
        setLocationRelativeTo(null);
    }
}
