package hu.mgx.app.swing;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.awt.print.*;

import javax.swing.*;

import hu.mgx.draw.*;

public class PrintablePage extends JPanel
{

    private BufferedImage bimg;
    private Graphics2D gb;
    private PageFormat pageFormat;
    private int iWidth;
    private int iHeight;
    private int iPanelWidth;
    private int iPanelHeight;
    private double dPaperSizeRatio = 1.0;
    private double dPrintSizeRatio = 1.0;
    protected int iFontSize = 12;
    protected Font font = new Font("Arial", Font.PLAIN, iFontSize);
    protected FontMetrics fm = getFontMetrics(font);
    protected int iRowHeight = fm.getHeight();
    protected int iRowMaxAscent = fm.getMaxAscent();
    protected int iRowMaxDescent = fm.getMaxDescent();
    private VirtualDrawSurface virtualDrawSurface;
    private int iPage;

    public PrintablePage(PageFormat pageFormat, VirtualDrawSurface virtualDrawSurface, int iPage)
    {
        this.virtualDrawSurface = virtualDrawSurface;
        this.iPage = iPage;
        assignPageFormat(pageFormat);
        init();
    }

    public void assignPageFormat(PageFormat pageFormat)
    {
        this.pageFormat = pageFormat;
        iWidth = (int) pageFormat.getWidth();
        iHeight = (int) pageFormat.getHeight();
        iPanelWidth = (int) (iWidth * dPaperSizeRatio);
        iPanelHeight = (int) (iHeight * dPaperSizeRatio);
        setMinimumSize(new Dimension(iPanelWidth, iPanelHeight));
        setPreferredSize(new Dimension(iPanelWidth, iPanelHeight));
        setMaximumSize(new Dimension(iPanelWidth, iPanelHeight));
    }

    private void init()
    {
        bimg = new BufferedImage(iWidth, iHeight, BufferedImage.TYPE_INT_RGB);
        gb = bimg.createGraphics();
        gb.setColor(Color.WHITE);
        gb.fillRect(0, 0, iWidth, iHeight);
        gb.setColor(Color.BLACK);
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, iPanelWidth, iPanelHeight);
        g.setColor(Color.BLACK);

        ((Graphics2D) g).transform(new AffineTransform(dPaperSizeRatio, 0, 0, dPaperSizeRatio, 0, 0));
        ((Graphics2D) g).translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        virtualDrawSurface.drawToGraphics(iPage, (Graphics2D) g);

        ((Graphics2D) g).transform(new AffineTransform(dPrintSizeRatio, 0, 0, dPrintSizeRatio, 0, 0));
    }

    public void drawString(String s, int x, int y)
    {
        gb.drawString(s, x, y);
    }

    public void setPaperSizeRatio(double d)
    {
        dPaperSizeRatio = d;
        assignPageFormat(pageFormat);
    }

    public Graphics2D getGraphics2D()
    {
        return (gb);
    }
}
