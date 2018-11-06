package hu.mgx.report;

import java.awt.*;

public class Cell
{

    private String sName = "";
    private String sNames[] = null;
    private String sValue = "";
    private int cellType = CellType.NULL;
    private Font font = null;
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

    public Cell(String sName, String sValue, int cellType, Font font, int x, int y, int width, int height, int align, int valign, boolean bBorder)
    {
        this(sName, sValue, cellType, font, x, y, width, height, align, valign, bBorder, true, false, false);
    }

    public Cell(String sName, String sValue, int cellType, Font font, int x, int y, int width, int height, int align, int valign, boolean bBorder, boolean bClip, boolean bWrap, boolean bShrink)
    {
        this(sName, sValue, cellType, font, x, y, width, height, align, valign, bBorder, bClip, bWrap, bShrink, false);
    }

    public Cell(String sName, String sValue, int cellType, Font font, int x, int y, int width, int height, int align, int valign, boolean bBorder, boolean bClip, boolean bWrap, boolean bShrink, boolean bStretch)
    {
        this.sName = sName;
        this.sValue = sValue;
        this.cellType = cellType;
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

    public Cell(String sNames[], String sValue, Font font, int x, int y, int width, int height, int align, int valign, boolean bBorder)
    {
        this(sNames, sValue, font, x, y, width, height, align, valign, bBorder, true, false);
    }

    public Cell(String sNames[], String sValue, Font font, int x, int y, int width, int height, int align, int valign, boolean bBorder, boolean bClip, boolean bWrap)
    {
        this(sNames, sValue, font, x, y, width, height, align, valign, bBorder, bClip, bWrap, false);
    }

    public Cell(String sNames[], String sValue, Font font, int x, int y, int width, int height, int align, int valign, boolean bBorder, boolean bClip, boolean bWrap, boolean bStretch)
    {
        this.sName = sNames[0];
        this.sNames = sNames;
        this.sValue = sValue;
        this.cellType = CellType.SPECIAL_COMPOUND;
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
        this.bStretch = bStretch;
    }

    public Cell copy()
    {
        if (this.cellType != CellType.SPECIAL_COMPOUND)
        {
            return (new Cell(sName, sValue, cellType, font, x, y, width, height, align, valign, bBorder, bClip, bWrap, bShrink, bStretch));
        }
        else
        {
            return (new Cell(sNames, sValue, font, x, y, width, height, align, valign, bBorder, bClip, bWrap, bStretch));
        }
    }

    public String getName()
    {
        return (sName);
    }

    public String[] getNames()
    {
        return (sNames);
    }

    public String getValue()
    {
        return (sValue);
    }

    public int getCellType()
    {
        return (cellType);
    }

    public Font getFont()
    {
        return (font);
    }

    public int getX()
    {
        return (x);
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int getY()
    {
        return (y);
    }

    public int getWidth()
    {
        return (width);
    }

    public void setHeight(int height)
    {
        this.height = height;
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
