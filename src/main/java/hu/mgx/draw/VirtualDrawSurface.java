/*
 * VirtualDrawSurface.java
 *
 */
package hu.mgx.draw;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

/**
 * Virtuális rajzoló felület.
 * <p>
 * x koordináta: "jobbra"
 * y koordináta: "lefele"
 */
public class VirtualDrawSurface
{

    private int iWidth = 0;
    private int iHeight = 0;
    private Font defaultFont;
    private Vector vPages = new Vector();
    private int iPageIndex = 0;
    private int iFreePosition = 0;
    private int iMarginTop = 0;
    private int iMarginLeft = 0;
    private int iMarginBottom = 0;
    private int iMarginRight = 0;

    public VirtualDrawSurface(int iWidth, int iHeight, Font defaultFont)
    {
        this.iWidth = iWidth;
        this.iHeight = iHeight;
        this.defaultFont = defaultFont;
        vPages = new Vector();
        vPages.add(new Vector());
        iPageIndex = 0;
    }

    /**
     * Cellába ír!
     *
     * @param str A kiírandó szöveg
     * @param font A használandó font
     * @param x x
     * @param y y
     * @param width width
     * @param height height
     * @param align align
     * @param valign valign
     * @param bBorder bBorder
     * @param bClip bClip
     * @param bWrap bWrap
     * @param bShrink bShrink
     */
    public void drawString(String str, Font font, int x, int y, int width, int height, int align, int valign, boolean bBorder, boolean bClip, boolean bWrap, boolean bShrink)
    {
        ((Vector) vPages.elementAt(iPageIndex)).add(new StringItem(str, font, x, y, width, height, align, valign, bBorder, bClip, bWrap, bShrink));
        if ((y + height) > iFreePosition)
        {
            iFreePosition = y + height + 1;
        }
    }

    public void drawString(String str, Font font, int x, int y, int width, int height, int align, int valign, boolean bBorder)
    {
        ((Vector) vPages.elementAt(iPageIndex)).add(new StringItem(str, font, x, y, width, height, align, valign, bBorder));
        if ((y + height) > iFreePosition)
        {
            iFreePosition = y + height + 1;
        }
    }

    public void drawString(String str, int x, int y, int width, int height)
    {
        drawString(str, defaultFont, x, y, width, height, DrawAlign.CENTER, DrawAlign.MIDDLE, false);
    }

    public void drawString(String str, Font font, int x, int y, int width, int height)
    {
        drawString(str, font, x, y, width, height, DrawAlign.CENTER, DrawAlign.MIDDLE, false);
    }

    public void drawString(String str, Font font, int x, int y, int width, int height, boolean bBorder)
    {
        drawString(str, font, x, y, width, height, DrawAlign.CENTER, DrawAlign.MIDDLE, bBorder);
    }

    public void drawString(String str, int x, int y, int width, int height, int align)
    {
        drawString(str, defaultFont, x, y, width, height, align, DrawAlign.MIDDLE, false);
    }

    public void drawString(String str, int x, int y, int width, int height, int align, int valign)
    {
        drawString(str, defaultFont, x, y, width, height, align, valign, false);
    }

    public void drawString(String str, int x, int y, int width, int height, int align, int valign, boolean bBorder)
    {
        drawString(str, defaultFont, x, y, width, height, align, valign, bBorder);
    }

    public void drawLine(int x1, int y1, int x2, int y2)
    {
        ((Vector) vPages.elementAt(iPageIndex)).add(new LineItem(x1, y1, x2, y2));
        if (y1 > iFreePosition)
        {
            iFreePosition = y1 + 1;
        }
        if (y2 > iFreePosition)
        {
            iFreePosition = y2 + 1;
        }
    }

    public void drawLine(int x1, int y1, int x2, int y2, boolean bDotted)
    {
        ((Vector) vPages.elementAt(iPageIndex)).add(new LineItem(x1, y1, x2, y2, bDotted));
        if (y1 > iFreePosition)
        {
            iFreePosition = y1 + 1;
        }
        if (y2 > iFreePosition)
        {
            iFreePosition = y2 + 1;
        }
    }

    public void drawRectangle(int x, int y, int width, int height)
    {
        ((Vector) vPages.elementAt(iPageIndex)).add(new RectangleItem(x, y, width, height));
        if ((y + height) > iFreePosition)
        {
            iFreePosition = y + height + 1;
        }
    }

    public void setFont(Font font)
    {
    }

    public void addPage()
    {
        vPages.add(new Vector());
        ++iPageIndex;
        iFreePosition = 0;
    }

    public int getCurrentPage()
    {
        return (iPageIndex);
    }

    public void setCurrentPage(int iPageIndex)
    {
        if (iPageIndex < vPages.size())
        {
            this.iPageIndex = iPageIndex;
        }
    }

    public int getPageCount()
    {
        return (vPages.size());
    }

    public int getFreePosition()
    {
        return (iFreePosition);
    }

    /**
     * Lekérdezi a rajzterület szélességét.
     * @return A terület szélessége
     */
    public int getWidth()
    {
        return iWidth;
    }

    /**
     * Lekérdezi a rajzterület magasságát.
     * @return A terület magassága
     */
    public int getHeight()
    {
        return iHeight;
    }

    /**
     * A megadott Graphics2D objektumra rajzolja a pageIndex által meghatározott oldalon lévő elemeket.
     * @param pageIndex pageIndex
     * @param g2d g2d
     * @return true, ha sikeres volt a rajzolás (van ilyen oldal, akkor is, ha üres), illetve false, ha sikertelen volt a rajzolás (nincs ilyen oldal)
     */
    public boolean drawToGraphics(int pageIndex, Graphics2D g2d)
    {
        if (pageIndex >= vPages.size())
        {
            return (false);
        }
        Vector vPage = (Vector) vPages.elementAt(pageIndex);
        //for (int i=0; i<((Vector)vPages.elementAt(pageIndex)).size(); i++) {
        for (int i = 0; i < vPage.size(); i++)
        {
            //if (((Vector)vPages.elementAt(pageIndex)).elementAt(i).getClass().getName().endsWith("StringItem")) {
            if (vPage.elementAt(i) instanceof StringItem)
            {
                drawStringToGraphics(g2d, (StringItem) vPage.elementAt(i));
            }
            //if (((Vector)vPages.elementAt(pageIndex)).elementAt(i).getClass().getName().endsWith("LineItem")) {
            if (vPage.elementAt(i) instanceof LineItem)
            {
                drawLineToGraphics(g2d, (LineItem) vPage.elementAt(i));
            }
            //if (((Vector)vPages.elementAt(pageIndex)).elementAt(i).getClass().getName().endsWith("RectangleItem")) {
            if (vPage.elementAt(i) instanceof RectangleItem)
            {
                drawRectangleToGraphics(g2d, (RectangleItem) vPage.elementAt(i));
            }
        }
        return (true);
    }

    private String fit(String s, int iCellWidth, Graphics g2d, Font f)
    {
        FontMetrics fm = g2d.getFontMetrics(f);
        Rectangle2D r;
        int iLastSpace = 0;
        for (int i = 0; i < s.length(); i++)
        {
            if (i < (s.length() - 1) && s.substring(i, i + 2).equals(hu.mgx.util.StringUtils.sLfCr))
            {
                return (s.substring(0, i + 2));
            }
            if (i < (s.length() - 1) && s.substring(i, i + 2).equals(hu.mgx.util.StringUtils.sCrLf))
            {
                return (s.substring(0, i + 2));
            //if (s.substring(i, i+1).equals(hu.mgx.util.StringUtils.sCr)) return(s.substring(0, i+1));
            //if (s.substring(i, i+1).equals(hu.mgx.util.StringUtils.sLf)) return(s.substring(0, i+1));
            }
            if (s.charAt(i) == 13)
            {
                return (s.substring(0, i + 1));
            }
            if (s.charAt(i) == 10)
            {
                return (s.substring(0, i + 1));
            }
            if (s.substring(i, i + 1).equals(" "))
            {
                iLastSpace = i;
            }
            r = fm.getStringBounds(s.substring(0, i + 1), g2d);
            if (r.getWidth() > iCellWidth)
            {
                if (iLastSpace > 0)
                {
                    return (s.substring(0, iLastSpace + 1));
                }
                else
                {
                    return (s.substring(0, i));
                }
            }
        }
        return (s);
    }

    private Vector split(String s, int iCellWidth, Graphics g2d, Font f)
    {
        Vector v = new Vector();
        String sFit = "";
        while (s.length() > 0)
        {
            sFit = fit(s, iCellWidth, g2d, f);
            s = s.substring(sFit.length());
            sFit = hu.mgx.util.StringUtils.stringReplace(sFit, hu.mgx.util.StringUtils.sCr, "");
            sFit = hu.mgx.util.StringUtils.stringReplace(sFit, hu.mgx.util.StringUtils.sLf, "");
            v.add(sFit);
        }
        return (v);
    }

    private void drawStringToGraphics(Graphics g2d, StringItem si)
    {
        int x;
        int y;
        String s = si.getString();
        Font f = si.getFont();
        FontMetrics fm = g2d.getFontMetrics(f);
        Rectangle2D r = fm.getStringBounds(s, g2d);
        Rectangle clipBounds = g2d.getClipBounds();
        Vector v;
        //        System.err.println("---");
        //        System.err.println(s);
        //        System.err.println(r.getX());
        //        System.err.println(r.getY());
        //        System.err.println(r.getWidth());
        //        System.err.println(r.getHeight());
        g2d.setFont(f);
        if ((si.isWrapped() || si.isStretched()) && (r.getWidth() > si.getWidth() || s.indexOf(10) > -1 || s.indexOf(13) > -1))
        {
            x = si.getX();
            y = (int) (si.getY() - r.getY());
            v = split(s, si.getWidth(), g2d, f);
            for (int i = 0; i < v.size(); i++)
            {
                g2d.drawString(v.elementAt(i).toString(), x, y);
                y += r.getHeight();
            }
        }
        else
        {
            x = (int) (si.getX() + (si.getWidth() - r.getWidth()) / 2);
            if (si.getAlign() == DrawAlign.LEFT)
            {
                x = si.getX();
            }
            if (si.getAlign() == DrawAlign.RIGHT)
            {
                x = (int) (si.getX() + si.getWidth() - r.getWidth());
            }
            y = (int) (si.getY() + (si.getHeight() - r.getHeight()) / 2 - r.getY());
            if (si.getValign() == DrawAlign.TOP)
            {
                y = (int) (si.getY() - r.getY());
            }
            if (si.getValign() == DrawAlign.BOTTOM)
            {
                //y = (int)(si.getY()+si.getHeight());
                y = (int) (si.getY() + si.getHeight() - fm.getDescent());
            }

            if (si.isClipped())
            {
                clipBounds = g2d.getClipBounds();
                g2d.clipRect(si.getX(), si.getY(), si.getWidth(), si.getHeight()); //nem enged a cell�n k�v�lre �rni!
            //g2d.setClip(si.getX(), si.getY(), si.getWidth(), si.getHeight()); //nem enged a cell�n k�v�lre �rni!
            }
            g2d.drawString(si.getString(), x, y);
            if (si.isClipped())
            {
                //g2d.setClip(null);
                g2d.setClip(clipBounds);
            }
        }

        if (si.hasBorder())
        {
            g2d.drawRect(si.getX(), si.getY(), si.getWidth(), si.getHeight());
        }
    }

    private void drawLineToGraphics(Graphics2D g2d, LineItem li)
    {
        if (li.isDotted())
        {
            Stroke stroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, new float[]
                                            {
                                                1.0f
                                            }, 0.0f);
            Stroke saveStroke = g2d.getStroke();
            g2d.setStroke(stroke);
            g2d.drawLine(li.getX1(), li.getY1(), li.getX2(), li.getY2());
            g2d.setStroke(saveStroke);
        }
        else
        {
            g2d.drawLine(li.getX1(), li.getY1(), li.getX2(), li.getY2());
        }
    }

    private void drawRectangleToGraphics(Graphics g2d, RectangleItem ri)
    {
        g2d.drawRect(ri.getX(), ri.getY(), ri.getWidth(), ri.getHeight());
    }
}