package hu.mgx.report;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.text.*;
import java.util.*;

import hu.mgx.app.common.*;
import hu.mgx.draw.*;
import hu.mgx.swing.table.*;

public class ReportSection
{

    private String sName = "";
    private Vector vCells = new Vector();
    private Vector vRenderedCells = new Vector();
    private MemoryTable memoryTable;
    private int iSectionHeight = 0;
    private Report callerReport = null;
    private boolean bHeader = false;
    private boolean bFooter = false;
    private int iBlock = 1;
    private int iBlockSize = 0;
    private boolean bDraw = true;
    private boolean bAutoColumnWidth = false;

    public ReportSection(String sName)
    {
        this.sName = sName;
    }

    public ReportSection(String sName, boolean bHeader, boolean bFooter)
    {
        this.sName = sName;
        this.bHeader = bHeader;
        this.bFooter = bFooter;
    }

    public void addCell(Cell cell)
    {
        vCells.add(cell);
        if ((cell.getY() + cell.getHeight()) > iSectionHeight)
        {
            iSectionHeight = cell.getY() + cell.getHeight();
        }
    }

    public String getName()
    {
        return (sName);
    }

    public int getSectionHeight()
    {
        return (iSectionHeight);
    }

    public void setBlock(int iBlock)
    {
        this.iBlock = iBlock;
    }

    public void setBlockSize(int iBlockSize)
    {
        this.iBlockSize = iBlockSize;
    }

    public void setDraw(boolean bDraw)
    {
        this.bDraw = bDraw;
    }

    public boolean getDraw()
    {
        return (bDraw);
    }

    public void setAutoColumnWidth(boolean b)
    {
        bAutoColumnWidth = b;
    }
    //--- VirtualDrawSurface-bõl másolva ---
    private String fit(String s, int iCellWidth, Graphics g2d, Font f)
    {
        FontMetrics fm = g2d.getFontMetrics(f);
        Rectangle2D r;
        int iLastSpace = 0;
        for (int i = 0; i < s.length(); i++)
        {
            //System.err.print(s.charAt(i));
            //System.err.print("-");
            //System.err.println((int)s.charAt(i));
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
    //--- VirtualDrawSurface-bõl másolva ---
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

    private String getCellContent(Cell cell, MemoryTable memoryTable, int row)
    {
        String sRetVal = "";
        String sCellNames[];
        //--- egyszerû cella tartalmának meghatározása ---
        if (cell.getCellType() != CellType.SPECIAL_COMPOUND)
        {
            sRetVal = (memoryTable != null ? (memoryTable.getValueAt(row, cell.getName()) != null ? memoryTable.getValueAt(row, cell.getName()).toString() : "") : cell.getValue());
        }
        //--- összetett cella tartalmának meghatározása ---
        if (cell.getCellType() == CellType.SPECIAL_COMPOUND)
        {
            sCellNames = cell.getNames();
            sRetVal = "";
            for (int n = 0; n < sCellNames.length; n++)
            {
                if (sCellNames[n].startsWith("[") && sCellNames[n].endsWith("]"))
                {
                    sRetVal += sCellNames[n].substring(1, sCellNames[n].length() - 1);
                }
                else
                {
                    sRetVal += (memoryTable != null ? (memoryTable.getValueAt(row, sCellNames[n]) != null ? memoryTable.getValueAt(row, sCellNames[n]).toString() : "") : cell.getValue());
                }
            }
            sRetVal = sRetVal.trim();
        }
        return (sRetVal);
    }

    public void drawToVirtualSurface(VirtualDrawSurface vds, Report callerReport, FormatInterface mgxFormat)
    {
        drawToVirtualSurface(vds, memoryTable, callerReport, mgxFormat, null);
    }

    public void drawToVirtualSurface(VirtualDrawSurface vds, Report callerReport, FormatInterface mgxFormat, Graphics g2d)
    {
        drawToVirtualSurface(vds, memoryTable, callerReport, mgxFormat, g2d);
    }

    public void drawToVirtualSurface(VirtualDrawSurface vds, MemoryTable memoryTable, Report callerReport, FormatInterface mgxFormat)
    {
        drawToVirtualSurface(vds, memoryTable, callerReport, mgxFormat, null);
    }

    public void drawToVirtualSurface(VirtualDrawSurface vds, MemoryTable memoryTable, Report callerReport, FormatInterface mgxFormat, Graphics g2d)
    {
        this.callerReport = callerReport;
        SimpleDateFormat simpleDateTimeFormat = mgxFormat.getDateTimeFormat();
        int iRowCount = 1;
        if (memoryTable != null)
        {
            iRowCount = memoryTable.getRowCount();
        }
        Cell cell;
        String sCellNames[] = null;
        int iWidth = vds.getWidth();
        int iHeight = vds.getHeight();
        double dRatio = iWidth / 1000.0;
        int iPageHeaderHeight = (int) (callerReport.getPageHeaderHeight() * dRatio);
        int iPageFooterHeight = (int) (callerReport.getPageFooterHeight() * dRatio);
        int iYOffset = (bHeader ? 0 : (bFooter ? iHeight - iPageFooterHeight : iPageHeaderHeight));
        int iYMax = (bFooter ? iHeight : (bHeader ? iPageHeaderHeight : iHeight - iPageFooterHeight));
        int iFreePosition = (bHeader ? 0 : (bFooter ? iHeight - iPageFooterHeight : 0));
        String s;
        int iCellX = 0;
        int iCellY = 0;
        int iCellWidth = 0;
        int iCellHeight = 0;
        int iCurrentBlock = -1;
        int iCellFrom = 0;
        int iCellTo = 0;
        //String sCellName = "";
        int iRenderedSectionHeight = 0;
        int iHeightDifference = 0;
        FontMetrics fm;
        Rectangle2D r;
        Vector v;
        Font f;
        boolean bDotted;

        if (g2d == null)
        {
            g2d = (Graphics2D) new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB).getGraphics();
        }

//        if (bAutoColumnWidth){
//            ReportUtils reportUtils = new ReportUtils(null);
//            Vector v2 = reportUtils.calculateColumnWidths(memoryTable, tableDefinition, mgxFormat, cell.getFont(), vColumnNames0);
//        }

        //---------------------------------
        //--- végigmegyünk a rekordokon ---
        //---------------------------------
        for (int j = 0; j < iRowCount; j++)
        {

            if (iBlock < 2)
            {
                iCellFrom = 0;
                iCellTo = vCells.size();
                iCurrentBlock = 0;
                //--- renderelés ---
                iRenderedSectionHeight = iSectionHeight;
                iHeightDifference = 0;
                vRenderedCells = new Vector();
                for (int i = iCellFrom; i < iCellTo; i++)
                {
                    vRenderedCells.add(((Cell) vCells.elementAt(i)).copy());
                }
                for (int i = iCellFrom; i < iCellTo; i++)
                {
                    cell = (Cell) vCells.elementAt(i);
                    //                    iCellX = (int)(cell.getX()*dRatio);
                    //                    iCellY = iFreePosition+(int)(cell.getY()*dRatio);
                    iCellWidth = (int) (cell.getWidth() * dRatio);
                    //                    iCellHeight = (int)(cell.getHeight()*dRatio);
                    if (cell.isStretched() && g2d != null)
                    {
                        s = getCellContent(cell, memoryTable, j);
                        f = cell.getFont().deriveFont((float) (cell.getFont().getSize() * dRatio));
                        fm = g2d.getFontMetrics(f);
                        r = fm.getStringBounds(s, g2d);
                        v = split(s, iCellWidth, g2d, f);
                        iHeightDifference = (int) ((v.size() < 2 ? 0 : (v.size() - 1) * new Double(r.getHeight()).intValue()) / dRatio);
                        ((Cell) vRenderedCells.elementAt(i)).setHeight(((Cell) vRenderedCells.elementAt(i)).getHeight() + iHeightDifference);
                        if ((((Cell) vRenderedCells.elementAt(i)).getY() + ((Cell) vRenderedCells.elementAt(i)).getHeight() + iHeightDifference) > iRenderedSectionHeight)
                        {
                            iRenderedSectionHeight = ((Cell) vRenderedCells.elementAt(i)).getY() + ((Cell) vRenderedCells.elementAt(i)).getHeight() + iHeightDifference;
                        //--- a többi (hátralévõ) cellákat el kell tolni a magasságdifferenciával és a szekció magasságát is ehhez kell igazítani ---
                        }
                        for (int i2 = i + 1; i2 < iCellTo; i2++)
                        {
                            ((Cell) vRenderedCells.elementAt(i2)).setY(((Cell) vRenderedCells.elementAt(i2)).getY() + iHeightDifference);
                            //((Cell)vRenderedCells.elementAt(i2)).setY(iHeightDifference);
                            if ((((Cell) vRenderedCells.elementAt(i2)).getY() + ((Cell) vRenderedCells.elementAt(i2)).getHeight()) > iRenderedSectionHeight)
                            {
                                iRenderedSectionHeight = ((Cell) vRenderedCells.elementAt(i2)).getY() + ((Cell) vRenderedCells.elementAt(i2)).getHeight();
                            }
                            iCellY = ((Cell) vRenderedCells.elementAt(i2)).getY();
                        }
                    }
                }
            }
            else
            {
                ++iCurrentBlock;
                if (iCurrentBlock >= iBlock)
                {
                    iCurrentBlock = 0;
                }
                iCellFrom = iCurrentBlock * iBlockSize;
                iCellTo = (iCurrentBlock + 1) * iBlockSize;
                //--- renderelés ---
                iRenderedSectionHeight = iSectionHeight;
                vRenderedCells = new Vector();
                for (int i = 0; i < vCells.size(); i++)
                {
                    //for (int i=iCellFrom; i<iCellTo; i++) {
                    vRenderedCells.add((Cell) vCells.elementAt(i));
                }
            }

            if (!bHeader && !bFooter)
            {
                if (iCurrentBlock == 0)
                {
                    iFreePosition = (vds.getFreePosition() < iYOffset ? iYOffset : vds.getFreePosition());
                    if ((iYMax - iFreePosition) < (int) (iRenderedSectionHeight * dRatio))
                    {
                        vds.addPage();
                        iFreePosition = (vds.getFreePosition() < iYOffset ? iYOffset : vds.getFreePosition());
                    }
                }
            }
            //-------------------------------
            //--- végigmegyünk a cellákon ---
            //-------------------------------
            //for (int i=0; i<vCells.size(); i++) {
            for (int i = iCellFrom; i < iCellTo; i++)
            {
                //cell = (Cell)vCells.elementAt(i);
                cell = (Cell) vRenderedCells.elementAt(i);
                s = "";

                //------------------------------------------------
                //--- egyszerû cella tartalmának meghatározása ---
                //------------------------------------------------
                if (cell.getCellType() != CellType.SPECIAL_COMPOUND)
                {
                    s = (memoryTable != null ? (memoryTable.getValueAt(j, cell.getName()) != null ? memoryTable.getValueAt(j, cell.getName()).toString() : "") : cell.getValue());
                }
                //-------------------------------------------------
                //--- összetett cella tartalmának meghatározása ---
                //-------------------------------------------------
                if (cell.getCellType() == CellType.SPECIAL_COMPOUND)
                {
                    sCellNames = cell.getNames();
                    s = "";
                    for (int n = 0; n < sCellNames.length; n++)
                    {
                        if (sCellNames[n].startsWith("[") && sCellNames[n].endsWith("]"))
                        {
                            s += sCellNames[n].substring(1, sCellNames[n].length() - 1);
                        }
                        else
                        {
                            s += (memoryTable != null ? (memoryTable.getValueAt(j, sCellNames[n]) != null ? memoryTable.getValueAt(j, sCellNames[n]).toString() : "") : cell.getValue());
                        }
                    }
                    s = s.trim();
                }

                //--- statikus cella tartalma ---
                if (cell.getCellType() == CellType.SPECIAL_STATIC)
                {
                    s = cell.getValue();
                }

                //--- oldalszám cella tartalma ---
                if (cell.getCellType() == CellType.SPECIAL_PAGE)
                {
                    s = Integer.toString(vds.getCurrentPage() + 1) + "/" + Integer.toString(vds.getPageCount());// + " oldal";
                }

                //--- dátum/idõ cella tartalma ---
                if (cell.getCellType() == CellType.SPECIAL_DATETIME)
                {
                    s = simpleDateTimeFormat.format(new java.util.Date());
                }

                //--- cella pozíciója és mérete ---
                iCellX = (int) (cell.getX() * dRatio);
                iCellY = iFreePosition + (int) (cell.getY() * dRatio);
                iCellWidth = (int) (cell.getWidth() * dRatio);
                iCellHeight = (int) (cell.getHeight() * dRatio);

                //--- vonal rajzolás ---
                if ((cell.getCellType() == CellType.SPECIAL_LINE) || (cell.getCellType() == CellType.SPECIAL_DOTTED_LINE))
                {
                    bDotted = (cell.getCellType() == CellType.SPECIAL_DOTTED_LINE);
                    if (cell.getValign() == DrawAlign.TOP)
                    {
                        vds.drawLine(iCellX, iCellY, iCellX + iCellWidth, iCellY, bDotted);
                    }
                    if (cell.getValign() == DrawAlign.MIDDLE)
                    {
                        vds.drawLine(iCellX, iCellY + (int) (iCellHeight / 2), iCellX + iCellWidth, iCellY + (int) (iCellHeight / 2), bDotted);
                    }
                    if (cell.getValign() == DrawAlign.BOTTOM)
                    {
                        vds.drawLine(iCellX, iCellY + iCellHeight, iCellX + iCellWidth, iCellY + iCellHeight, bDotted);
                    }
                    if (cell.getValign() == DrawAlign.BOTH)
                    {
                        vds.drawLine(iCellX, iCellY, iCellX + iCellWidth, iCellY, bDotted);
                        vds.drawLine(iCellX, iCellY + iCellHeight, iCellX + iCellWidth, iCellY + iCellHeight, bDotted);
                    }
                    if (cell.getAlign() == DrawAlign.LEFT)
                    {
                        vds.drawLine(iCellX, iCellY, iCellX, iCellY + iCellHeight, bDotted);
                    }
                    if (cell.getAlign() == DrawAlign.CENTER)
                    {
                        vds.drawLine(iCellX + (int) (iCellWidth / 2), iCellY, iCellX + (int) (iCellWidth / 2), iCellY + iCellHeight, bDotted);
                    }
                    if (cell.getAlign() == DrawAlign.RIGHT)
                    {
                        vds.drawLine(iCellX + iCellWidth, iCellY, iCellX + iCellWidth, iCellY + iCellHeight, bDotted);
                    }
                    if (cell.getAlign() == DrawAlign.BOTH)
                    {
                        vds.drawLine(iCellX, iCellY, iCellX, iCellY + iCellHeight, bDotted);
                        vds.drawLine(iCellX + iCellWidth, iCellY, iCellX + iCellWidth, iCellY + iCellHeight, bDotted);
                    }
                }
                //--- szöveg írás ---
                else
                {
                    vds.drawString(s, cell.getFont().deriveFont((float) (cell.getFont().getSize() * dRatio)), iCellX, iCellY, iCellWidth, iCellHeight, cell.getAlign(), cell.getValign(), cell.hasBorder(), cell.isClipped(), cell.isWrapped(), cell.isShrinked());
                }
            }
        }
    }

    public void setMemoryTable(MemoryTable memoryTable)
    {
        this.memoryTable = memoryTable;
    }

    public MemoryTable createMemoryTable()
    {
        MemoryTable memoryTable;
        String[] sColumnNames = new String[vCells.size()];
        for (int i = 0; i < vCells.size(); i++)
        {
            sColumnNames[i] = ((Cell) vCells.elementAt(i)).getName();
        }
        memoryTable = new MemoryTable(sColumnNames);
        return (memoryTable);
    }
}
