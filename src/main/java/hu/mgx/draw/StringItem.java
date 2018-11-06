package hu.mgx.draw;

import java.awt.*;

public class StringItem
{

    private String str;
    private Font font;
    private int x;
    private int y;
    private int width;
    private int height;
    private int align;
    private int valign;
    private boolean bBorder;
    private boolean bClip;
    private boolean bWrap;
    private boolean bShrink;
    private boolean bStretch;

    public StringItem(String str, Font font, int x, int y, int width, int height, int align, int valign, boolean bBorder)
    {
        this(str, font, x, y, width, height, align, valign, bBorder, true, false, false);
    }

    public StringItem(String str, Font font, int x, int y, int width, int height, int align, int valign, boolean bBorder, boolean bClip, boolean bWrap, boolean bShrink)
    {
        this(str, font, x, y, width, height, align, valign, bBorder, bClip, bWrap, bShrink, false);
    }

    public StringItem(String str, Font font, int x, int y, int width, int height, int align, int valign, boolean bBorder, boolean bClip, boolean bWrap, boolean bShrink, boolean bStretch)
    {
        this.str = str;
        this.font = font;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.align = align;
        this.valign = valign;
        this.bBorder = bBorder;
        this.bClip = bClip;
        this.bWrap = bWrap;
        this.bShrink = bShrink;
        this.bStretch = bStretch;
    }

    public String getString()
    {
        return (str);
    }

    public Font getFont()
    {
        return (font);
    }

    public int getX()
    {
        return (x);
    }

    public int getY()
    {
        return (y);
    }

    public int getWidth()
    {
        return (width);
    }

    public int getHeight()
    {
        return (height);
    }

    public int getAlign()
    {
        return (align);
    }

    public int getValign()
    {
        return (valign);
    }

    public boolean hasBorder()
    {
        return (bBorder);
    }

    public boolean isClipped()
    {
        return (bClip);
    }

    public boolean isWrapped()
    {
        return (bWrap);
    }

    public boolean isShrinked()
    {
        return (bShrink);
    }

    public boolean isStretched()
    {
        return (bStretch);
    }
}
