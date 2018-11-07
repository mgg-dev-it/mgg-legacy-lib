/*
 * Report.java
 *
 * Created on 2004. december 9., 15:23
 */
package hu.mgx.report;

/**
 *
 * @author  gmagyar
 */
import java.util.*;

import javax.swing.table.*;

import hu.mgx.app.common.*;
import hu.mgx.db.*;
import hu.mgx.draw.*;
import hu.mgx.swing.table.*;

public class Report
{

    private ReportSection pageHeader = null;
    private ReportSection reportHeader = null;
    private Vector vSections = new Vector();
    private ReportSection reportFooter = null;
    private ReportSection pageFooter = null;
    private VirtualDrawSurface vds = null;
    private int iPageHeaderHeight = 0;
    private int iPageFooterHeight = 0;
    private FormatInterface mgxFormat;

    public Report(FormatInterface mgxFormat)
    {
        this.mgxFormat = mgxFormat;
    }

    public void addSection(ReportSection section)
    {
        vSections.add(section);
    }

    public void setPageHeader(ReportSection section)
    {
        pageHeader = section;
        iPageHeaderHeight = pageHeader.getSectionHeight();
    }

    public int getPageHeaderHeight()
    {
        return (iPageHeaderHeight);
    }

    public void setPageFooter(ReportSection section)
    {
        pageFooter = section;
        /////iPageFooterHeight = pageHeader.getSectionHeight();
        iPageFooterHeight = pageFooter.getSectionHeight();
    }

    public int getPageFooterHeight()
    {
        return (iPageFooterHeight);
    }

    public void setReportHeader(ReportSection section)
    {
        reportHeader = section;
    }

    public void setReportFooter(ReportSection section)
    {
        reportFooter = section;
    }

    public void drawPageHeader()
    {
        if (pageHeader != null)
        {
            pageHeader.drawToVirtualSurface(vds, this, mgxFormat);
        }
    }

    public void drawPageFooter()
    {
        if (pageFooter != null)
        {
            pageFooter.drawToVirtualSurface(vds, this, mgxFormat);
        }
    }

    public void drawReportHeader()
    {
    }

    public void drawReportFooter()
    {
    }

    public void drawToVirtualSurface(VirtualDrawSurface vds)
    {
        this.vds = vds;
        ReportSection section;
        //drawPageHeader ();
        for (int i = 0; i < vSections.size(); i++)
        {
            section = (ReportSection) vSections.elementAt(i);
            if (section.getDraw())
            {
                section.drawToVirtualSurface(vds, this, mgxFormat);
            }
        }
        for (int i = 0; i < vds.getPageCount(); i++)
        {
            vds.setCurrentPage(i);
            if (pageHeader != null)
            {
                pageHeader.drawToVirtualSurface(vds, this, mgxFormat);
            }
            if (pageFooter != null)
            {
                pageFooter.drawToVirtualSurface(vds, this, mgxFormat);
            }
        }
    }

    public ReportSection getSectionByName(String sSectionName)
    {
        ReportSection section;
        for (int i = 0; i < vSections.size(); i++)
        {
            section = (ReportSection) vSections.elementAt(i);
            if (section.getName().equals(sSectionName))
            {
                return (section);
            }
        }
        return (null);
    }

    public boolean sectionMemoryTable(ReportSection section, MemoryTable memoryTable)
    {
        section.setMemoryTable(memoryTable);
        return (true);
    }

    public boolean sectionMemoryTable(String sSectionName, MemoryTable memoryTable)
    {
        ReportSection section;
        for (int i = 0; i < vSections.size(); i++)
        {
            section = (ReportSection) vSections.elementAt(i);
            if (section.getName().equals(sSectionName))
            {
                section.setMemoryTable(memoryTable);
            }
        }
        if (pageHeader != null)
        {
            if (pageHeader.getName().equals(sSectionName))
            {
                pageHeader.setMemoryTable(memoryTable);
            }
        }
        if (pageFooter != null)
        {
            if (pageFooter.getName().equals(sSectionName))
            {
                pageFooter.setMemoryTable(memoryTable);
            }
        }
        if (reportHeader != null)
        {
            if (reportHeader.getName().equals(sSectionName))
            {
                reportHeader.setMemoryTable(memoryTable);
            }
        }
        if (reportFooter != null)
        {
            if (reportFooter.getName().equals(sSectionName))
            {
                reportFooter.setMemoryTable(memoryTable);
            }
        }
        return (true);
    }

    public MemoryTable createMemoryTable(String sSectionName)
    {
        ReportSection section;
        for (int i = 0; i < vSections.size(); i++)
        {
            section = (ReportSection) vSections.elementAt(i);
            if (section.getName().equals(sSectionName))
            {
                return (section.createMemoryTable());
            }
        }
        if (pageHeader != null)
        {
            if (pageHeader.getName().equals(sSectionName))
            {
                return (pageHeader.createMemoryTable());
            }
        }
        if (pageFooter != null)
        {
            if (pageFooter.getName().equals(sSectionName))
            {
                return (pageFooter.createMemoryTable());
            }
        }
        if (reportHeader != null)
        {
            if (reportHeader.getName().equals(sSectionName))
            {
                return (reportHeader.createMemoryTable());
            }
        }
        if (reportFooter != null)
        {
            if (reportFooter.getName().equals(sSectionName))
            {
                return (reportFooter.createMemoryTable());
            }
        }
        return (null);
    }

    public MemoryTable copyTableToMemoryTable(DefaultTableModel defaultTableModel, TableDefinition tableDefinition, FormatInterface mgxFormat)
    {
        MemoryTable memoryTable = new MemoryTable();
        Vector vColumnNames = new Vector();
        Vector vRow = new Vector();

        //Vector vLengths = new Vector();
        //Vector vStrings = new Vector();
        double d;
        //java.util.Date utilDate;
        String sValue = "";
        String sCellValue = "";
        int iRow;
        short shColumn;
        short shOffset = 0;
        try
        {
            for (shColumn = 0; shColumn < defaultTableModel.getColumnCount(); shColumn++)
            {
                if (tableDefinition.getFieldDefinition(shColumn).isID())
                {
                    ++shOffset;
                }
                else
                {
                    vColumnNames.add(tableDefinition.getFieldDefinition(shColumn).getName() + Integer.toString(shColumn));
                }
            }
            memoryTable = new MemoryTable(vColumnNames, 0);
            for (iRow = 0; iRow < defaultTableModel.getRowCount(); iRow++)
            {
                shOffset = 0;
                vRow = new Vector();
                for (shColumn = 0; shColumn < defaultTableModel.getColumnCount(); shColumn++)
                {
                    if (tableDefinition.getFieldDefinition(shColumn).isID())
                    {
                        ++shOffset;
                    }
                    else
                    {
                        Object o = defaultTableModel.getValueAt(iRow, shColumn);
                        sValue = (o == null ? "" : o.toString());
                        //System.err.println(sValue);
                        if (tableDefinition.getFieldDefinition(shColumn).isLookup())
                        {
                            sValue = tableDefinition.getLookupDisplayFromValue(tableDefinition.getFieldDefinition(shColumn).getLookup(), sValue);
                            sCellValue = sValue;
                        }
                        else
                        {
                            if (tableDefinition.getFieldDefinition(shColumn).getType() == hu.mgx.db.FieldType.DECIMAL || tableDefinition.getFieldDefinition(shColumn).getType() == hu.mgx.db.FieldType.INT)
                            {
                                if (sValue.equals(""))
                                {
                                    sCellValue = "";
                                }
                                else
                                {
                                    try
                                    {
                                        d = Double.parseDouble(sValue);
                                    }
                                    catch (NumberFormatException e)
                                    {
                                        d = 0.0;
                                    }
                                    //sCellValue=d;
                                    sCellValue = sValue;
                                }
                            }
                            else if (tableDefinition.getFieldDefinition(shColumn).getType() == hu.mgx.db.FieldType.DATE)
                            {
                                if (o == null)
                                {
                                    sCellValue = "";
                                }
                                else
                                {
                                    if (o.getClass().getName().startsWith("[B"))
                                    {
                                        String sTmp = new String((byte[]) o);
                                        sTmp += " 00:00:00";
                                        try
                                        {
                                            o = new java.util.Date(mgxFormat.getSQLDateTimeFormat().parse(sTmp).getTime());
                                            sValue = mgxFormat.getDateFormat().format(o);
                                        }
                                        catch (java.text.ParseException pe)
                                        {
                                            o = null;
                                        }
                                        if (sValue.equals("1999/01/01"))
                                        {
                                            sValue = "Nem aktuális";
                                        }
                                        else if (sValue.equals("1999/01/02"))
                                        {
                                            sValue = "Folyamatban";
                                        }
                                        else if (sValue.equals("1999/01/03"))
                                        {
                                            sValue = "Meglátogatni";
                                        }
                                        else if (sValue.equals("1999/01/04"))
                                        {
                                            sValue = "Beszállító";
                                        }
                                    }
                                    else
                                    {
                                        sValue = mgxFormat.getDateFormat().format(o);
                                    }
                                    //sValue = mgxFormat.getDateFormat().format(o);
                                    sCellValue = sValue;
                                }
                            }
                            else if (tableDefinition.getFieldDefinition(shColumn).getType() == hu.mgx.db.FieldType.DATETIME)
                            {
                                if (o == null)
                                {
                                    sCellValue = "";
                                }
                                else
                                {
                                    sValue = mgxFormat.getDateTimeFormat().format(o);
                                    sCellValue = sValue;
                                }
                            }
                            else if (tableDefinition.getFieldDefinition(shColumn).getType() == hu.mgx.db.FieldType.BIT)
                            {
                                if (o == null)
                                {
                                    sCellValue = "";
                                }
                                else
                                {
                                    if (o.toString().equals("1") || o.toString().equals("true"))
                                    {
                                        sCellValue = "+";
                                    }
                                    else
                                    {
                                        sCellValue = "";
                                    }
                                }
                            }
                            //                            else if (tableDefinition.getFieldDefinition(shColumn).getType() == hu.mgx.db.FieldType.CHAR) {
                            //                                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                            //                                cell.setCellValue(sValue);
                            //                            }
                            else
                            {
                                sCellValue = sValue;
                            }
                        }
                        //System.err.print(sCellValue);
                        //System.err.print("|");
                        vRow.add(sCellValue);
                    }
                }
                memoryTable.addRow(vRow);
            //System.err.println();
            }
        }
        catch (TableDefinitionException e)
        {
            System.err.println(e.getLocalizedMessage());
            e.printStackTrace(System.err);
        }
        return (memoryTable);
    }
}
