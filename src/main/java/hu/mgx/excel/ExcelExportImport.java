package hu.mgx.excel;

import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.table.*;

import hu.mgx.app.common.*;
import hu.mgx.db.*;
import hu.mgx.app.swing.*;
import hu.mgx.swing.table.*;
import hu.mgx.util.*;

import jxl.*;
import jxl.write.*;

import org.apache.poi.hssf.usermodel.*;

public class ExcelExportImport
{
    
    protected String sDir = "";
    
    public ExcelExportImport ()
    {
    }
    
    public ExcelExportImport (String sDir)
    {
        this.sDir = sDir;
    }
    
    public String getDir ()
    {
        return (sDir);
    }
    
    public void excelExport (java.awt.Component parentComponent, AppInterface appInterface, DefaultTableModel defaultTableModel, TableDefinition tableDefinition, FormatInterface mgxFormat)
    {
        File f = chooseFileToCreate (parentComponent, sDir, appInterface);
        if (f == null)
        {
            return;
        }
        try
        {
            sDir = f.getCanonicalPath ();
        }
        catch (java.io.IOException ioe)
        {
            appInterface.handleError (ioe); //2009.03.09
            sDir = "";
        }
        if (writeExcel (defaultTableModel, tableDefinition, mgxFormat, appInterface, f))
        {
            JOptionPane.showMessageDialog (parentComponent, appInterface.getLanguageString("Excel export sikeresen befejezõdött."));
        }
    }
    
    private boolean writeExcel (DefaultTableModel defaultTableModel, TableDefinition tableDefinition, FormatInterface mgxFormat, AppInterface appInterface, File f)
    {
        Label label = null;
        
        String sNullValue = "NULL";
        short shColumn;
        short shOffset = 0;
        int iRow;
        String sValue = "";
        double d;
        
        try
        {
            WritableCellFormat wcfLeft = new WritableCellFormat ();
            wcfLeft.setAlignment (jxl.format.Alignment.LEFT);
            WritableCellFormat wcfRight = new WritableCellFormat ();
            wcfRight.setAlignment (jxl.format.Alignment.RIGHT);
            
            WritableWorkbook workbook = Workbook.createWorkbook (f);
            WritableSheet sheet = workbook.createSheet ("Export", 0);
            
            for (shColumn = 0; shColumn < defaultTableModel.getColumnCount (); shColumn++)
            {
                if (tableDefinition.getFieldDefinition (shColumn).isID ())
                {
                    ++shOffset;
                }
                else
                {
                    label = new Label (shColumn - shOffset, 0, StringUtils.isNull (defaultTableModel.getColumnName (shColumn), sNullValue));
                    sheet.addCell (label);
                }
            }
            
            for (iRow = 0; iRow < defaultTableModel.getRowCount (); iRow++)
            {
                shOffset = 0;
                for (shColumn = 0; shColumn < defaultTableModel.getColumnCount (); shColumn++)
                {
                    if (tableDefinition.getFieldDefinition (shColumn).isID ())
                    {
                        ++shOffset;
                    }
                    else
                    {
                        Object o = defaultTableModel.getValueAt (iRow, shColumn);
                        sValue = (o == null ? "" : o.toString ());
                        if (tableDefinition.getFieldDefinition (shColumn).isLookup ())
                        {
                            sValue = tableDefinition.getLookupDisplayFromValue (tableDefinition.getFieldDefinition (shColumn).getLookup (), sValue);
                        }
                        else
                        {
                            switch(tableDefinition.getFieldDefinition (shColumn).getType ())
                            {
                                case hu.mgx.db.FieldType.DECIMAL:
                                case hu.mgx.db.FieldType.INT:
                                    break;
                                case hu.mgx.db.FieldType.DATETIME:
                                    if (o != null)
                                    {
                                        sValue = mgxFormat.getDateTimeFormat ().format (o);
                                    }
                                    break;
                                case hu.mgx.db.FieldType.DATE:
                                    if (o != null)
                                    {
                                        if (o.getClass ().getName ().startsWith ("[B"))
                                        {
                                            String sTmp = new String ((byte[]) o);
                                            sTmp += " 00:00:00";
                                            try
                                            {
                                                o = new java.util.Date (mgxFormat.getSQLDateTimeFormat ().parse (sTmp).getTime ());
                                                sValue = mgxFormat.getDateFormat ().format (o);
                                            }
                                            catch (java.text.ParseException pe)
                                            {
                                                o = null;
                                                sValue="";
                                            }
                                        }
                                        else
                                        {
                                            sValue = mgxFormat.getDateFormat ().format (o);
                                        }
                                    }
                                    break;
                                case hu.mgx.db.FieldType.BIT:
                                    if (o != null)
                                    {
                                        if (o.toString ().equals ("1") || o.toString ().equals ("true"))
                                        {
                                            sValue = "+";
                                        }
                                        else
                                        {
                                            sValue = "";
                                        }
                                    }
                                    break;
                                default:
                                    if (digitsOnly (sValue))
                                    {
                                        
                                    }
                                    break;
                            }
                        }
                        label = new Label (shColumn - shOffset, iRow + 1, StringUtils.isNull (sValue, sNullValue));
                        switch(tableDefinition.getFieldDefinition (shColumn).getType ())
                        {
                            case hu.mgx.db.FieldType.DECIMAL:
                            case hu.mgx.db.FieldType.INT:
                            case hu.mgx.db.FieldType.FLOAT:
                                label.setCellFormat (wcfRight);
                                break;
                            default:
                                break;
                        }
                        sheet.addCell (label);
                    }
                }
            }
//
//
//
//            for (int iRow = -1; iRow < memoryTable.getRowCount(); ++iRow)
//            {
//                for (int iCol = 0; iCol < memoryTable.getColumnCount(); ++iCol)
//                {
//                    //System.out.println(Integer.toString(iRow) + " - " + Integer.toString(iCol));
//                    if (iRow == -1)
//                    {
//                        label = new Label(iCol, iRow + 1, StringUtils.isNull(memoryTable.getColumnName(iCol), sNullValue));
//                    }
//                    else
//                    {
//                        label = new Label(iCol, iRow + 1, StringUtils.isNull(memoryTable.getValueAt(iRow, iCol), sNullValue));
//                    }
//                    try
//                    {
//                        sheet.addCell(label);
//                    }
//                    catch (WriteException e)
//                    {
//                        appInterface.handleError(e);
//                    }
//                }
//            }
            workbook.write ();
            workbook.close ();
        }
        catch (hu.mgx.db.TableDefinitionException tde)
        {
            appInterface.handleError (tde);
            return(false);
        }
        catch (java.io.IOException ioe)
        {
            appInterface.handleError (ioe);
            return(false);
        }
        catch (WriteException e)
        {
            appInterface.handleError (e);
            return(false);
        }
        return(true);
    }
    
    protected File chooseFileToCreate (java.awt.Component parentComponent, String sDir, AppInterface appInterface)
    {
        JFileChooser jFileChooser = new JFileChooser (sDir);
        ExampleFileFilter filter = new ExampleFileFilter ();
        filter.addExtension ("xls");
        filter.setDescription (appInterface.getLanguageString("Excel fájlok"));
        jFileChooser.setFileFilter (filter);
        int iDialogResult = jFileChooser.showSaveDialog (parentComponent);
        if (iDialogResult != JFileChooser.APPROVE_OPTION)
        {
            return (null);
        }
        File f = jFileChooser.getSelectedFile ();
        if (f.getName ().equals (""))
        {
            return (null);
        }
        if (!f.exists ())
        {
            if (!f.getName ().endsWith (".xls"))
            {
                if (f.getName ().indexOf ('.') < 0)
                {
                    //egyáltalán nics kiterjesztése
                    try
                    {
                        f = new File (f.getCanonicalPath () + ".xls");
                    }
                    catch (IOException ioe)
                    {
                        appInterface.handleError (ioe); //2009.03.09
                    }
                }
                else
                {
                    //van kiterjesztése, de az nem ".xls"
                }
            }
        }
        if (f.exists ())
        {
            if (!AppUtils.yesNoQuestion (parentComponent, appInterface.getLanguageString("Excel export"), appInterface.getLanguageString("A fájl létezik. Felülírjam?"), appInterface))
            {
                return (null);
            }
        }
        return (f);
    }
    
    protected boolean writeWorkbook (java.awt.Component parentComponent, AppInterface appInterface, File f, HSSFWorkbook wb)
    {
        FileOutputStream fileOut;
        try
        {
            fileOut = new FileOutputStream (f);
        }
        catch (FileNotFoundException e)
        {
            appInterface.handleError (e);
            return (false);
        }
        try
        {
            wb.write (fileOut);
            fileOut.flush ();
            fileOut.close ();
        }
        catch (IOException e)
        {
            appInterface.handleError (e);
            return (false);
        }
        return (true);
    }
    
    protected boolean digitsOnly (String s)
    {
        String sDigits = new String ("0123456789");
        for (int i = 0; i < s.length (); i++)
        {
            if (sDigits.indexOf (s.substring (i, i + 1)) < 0)
            {
                return (false);
            }
        }
        return (true);
    }
    
//    public HSSFWorkbook createWorkbook (DefaultTableModel defaultTableModel, TableDefinition tableDefinition, FormatInterface mgxFormat, AppInterface appInterface)
//    {
//        logLine (appInterface, "createWorkbook 10");
//        HSSFWorkbook wb = new HSSFWorkbook ();
//        logLine (appInterface, "createWorkbook 20");
//        HSSFSheet sheet = wb.createSheet ("Munka1");
//        logLine (appInterface, "createWorkbook 30");
//        HSSFRow row;
//        logLine (appInterface, "createWorkbook 40");
//        HSSFCell cell;
//        logLine (appInterface, "createWorkbook 50");
//        double d;
//        logLine (appInterface, "createWorkbook 60");
//        //java.util.Date utilDate;
//        logLine (appInterface, "createWorkbook 70");
//        String sValue = "";
//        logLine (appInterface, "createWorkbook 80");
//        int iRow;
//        logLine (appInterface, "createWorkbook 90");
//        short shColumn;
//        logLine (appInterface, "createWorkbook 100");
//        row = sheet.createRow (0);
//        logLine (appInterface, "createWorkbook 110");
//        short shOffset = 0;
//        logLine (appInterface, "createWorkbook 120");
//        HSSFCellStyle cellStyle = wb.createCellStyle ();
//        logLine (appInterface, "createWorkbook 130");
//        cellStyle.setAlignment (HSSFCellStyle.ALIGN_CENTER);
//        logLine (appInterface, "createWorkbook 140");
//        try
//        {
//            for (shColumn = 0; shColumn < defaultTableModel.getColumnCount (); shColumn++)
//            {
//                logLine (appInterface, "createWorkbook head " + Integer.toString (shColumn));
//                if (tableDefinition.getFieldDefinition (shColumn).isID ())
//                {
//                    ++shOffset;
//                }
//                else
//                {
//                    logLine (appInterface, "createWorkbook head " + defaultTableModel.getColumnName (shColumn));
//                    cell = row.createCell ((short) (shColumn - shOffset));
//                    logLine (appInterface, "createWorkbook 200");
//                    //cell.setEncoding (HSSFCell.ENCODING_UTF_16);
//                    //cell.setCellValue (defaultTableModel.getColumnName (shColumn));
//                    HSSFRichTextString h1 = new HSSFRichTextString ("");
//                    logLine (appInterface, "createWorkbook 201");
//                    HSSFRichTextString h2 = new HSSFRichTextString (defaultTableModel.getColumnName (shColumn));
//                    logLine (appInterface, "createWorkbook 202");
//                    cell.setCellValue (h2);
//                    logLine (appInterface, "createWorkbook 210");
//                    cell.setCellStyle (cellStyle);
//                    logLine (appInterface, "createWorkbook 220");
//                }
//            }
//            for (iRow = 0; iRow < defaultTableModel.getRowCount (); iRow++)
//            {
//                logLine (appInterface, "createWorkbook iRow=" + Integer.toString (iRow));
//                row = sheet.createRow (iRow + 1);
//                shOffset = 0;
//                for (shColumn = 0; shColumn < defaultTableModel.getColumnCount (); shColumn++)
//                {
//                    if (tableDefinition.getFieldDefinition (shColumn).isID ())
//                    {
//                        ++shOffset;
//                    }
//                    else
//                    {
////                        System.out.println(tableDefinition.getFieldDefinition(shColumn).getName());
////                        System.out.println(tableDefinition.getFieldDefinition(shColumn).getDisplayName());
//
//                        cell = row.createCell ((short) (shColumn - shOffset));
//                        Object o = defaultTableModel.getValueAt (iRow, shColumn);
//                        sValue = (o == null ? "" : o.toString ());
//                        //System.err.println(sValue);
//                        //cell.setEncoding (HSSFCell.ENCODING_UTF_16);
//                        if (tableDefinition.getFieldDefinition (shColumn).isLookup ())
//                        {
//                            sValue = tableDefinition.getLookupDisplayFromValue (tableDefinition.getFieldDefinition (shColumn).getLookup (), sValue);
//                            //cell.setCellValue (sValue);
//                            cell.setCellValue (new HSSFRichTextString (sValue));
//                        }
//                        else
//                        {
//                            if (tableDefinition.getFieldDefinition (shColumn).getType () == hu.mgx.db.FieldType.DECIMAL || tableDefinition.getFieldDefinition (shColumn).getType () == hu.mgx.db.FieldType.INT)
//                            {
//                                if (sValue.equals (""))
//                                {
//                                    //cell.setCellValue("");
//                                    cell.setCellValue (new HSSFRichTextString (""));
//                                }
//                                else
//                                {
//                                    try
//                                    {
//                                        d = Double.parseDouble (sValue);
//                                    }
//                                    catch (NumberFormatException e)
//                                    {
//                                        d = 0.0;
//                                    }
//                                    cell.setCellValue (d);
//                                }
//                            }
//                            else if (tableDefinition.getFieldDefinition (shColumn).getType () == hu.mgx.db.FieldType.DATE)
//                            {
//                                if (o == null)
//                                {
//                                    //cell.setCellValue("");
//                                    cell.setCellValue (new HSSFRichTextString (""));
//                                }
//                                else
//                                {
//                                    if (o.getClass ().getName ().startsWith ("[B"))
//                                    {
//                                        String sTmp = new String ((byte[]) o);
//                                        sTmp += " 00:00:00";
//                                        try
//                                        {
//                                            o = new java.util.Date (mgxFormat.getSQLDateTimeFormat ().parse (sTmp).getTime ());
//                                            sValue = mgxFormat.getDateFormat ().format (o);
//                                        }
//                                        catch (java.text.ParseException pe)
//                                        {
//                                            o = null;
//                                        }
//                                    }
//                                    else
//                                    {
//                                        sValue = mgxFormat.getDateFormat ().format (o);
//                                    }
//
////                                    System.out.println(Integer.toString(iRow));
////                                    System.out.println(tableDefinition.getFieldDefinition(shColumn).getName());
////                                    System.out.println(tableDefinition.getFieldDefinition(shColumn).getDisplayName());
////                                    System.out.println(o.toString());
////                                    System.out.println(o.getClass().getName());
////                                    sValue = mgxFormat.getDateFormat().format(o);
//                                    //cell.setCellValue(sValue);
//                                    cell.setCellValue (new HSSFRichTextString (sValue));
//                                }
//                            }
//                            else if (tableDefinition.getFieldDefinition (shColumn).getType () == hu.mgx.db.FieldType.DATETIME)
//                            {
//                                if (o == null)
//                                {
//                                    //cell.setCellValue("");
//                                    cell.setCellValue (new HSSFRichTextString (""));
//                                }
//                                else
//                                {
//                                    sValue = mgxFormat.getDateTimeFormat ().format (o);
//                                    //cell.setCellValue(sValue);
//                                    cell.setCellValue (new HSSFRichTextString (sValue));
//                                }
//                            }
//                            else if (tableDefinition.getFieldDefinition (shColumn).getType () == hu.mgx.db.FieldType.BIT)
//                            {
//                                if (o == null)
//                                {
//                                    //cell.setCellValue("");
//                                    cell.setCellValue (new HSSFRichTextString (""));
//                                }
//                                else
//                                {
//                                    if (o.toString ().equals ("1") || o.toString ().equals ("true"))
//                                    {
//                                        //cell.setCellValue("+");
//                                        cell.setCellValue (new HSSFRichTextString ("+"));
//                                    }
//                                    else
//                                    {
//                                        //cell.setCellValue("");
//                                        cell.setCellValue (new HSSFRichTextString (""));
//                                    }
//                                }
//                            }
//                            //                            else if (tableDefinition.getFieldDefinition(shColumn).getType() == hu.mgx.db.FieldType.CHAR) {
//                            //                                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//                            //                                cell.setCellValue(sValue);
//                            //                            }
//                            else
//                            {
//                                if (digitsOnly (sValue))
//                                {
//                                    if (sValue.equals (""))
//                                    {
//                                        //cell.setCellValue("");
//                                        cell.setCellValue (new HSSFRichTextString (""));
//                                    }
//                                    else
//                                    {
//                                        try
//                                        {
//                                            d = Double.parseDouble (sValue);
//                                        }
//                                        catch (NumberFormatException e)
//                                        {
//                                            d = 0.0;
//                                        }
//                                        cell.setCellValue (d);
//                                    }
//                                }
//                                else
//                                {
//                                    cell.setCellType (HSSFCell.CELL_TYPE_STRING);
//                                    //cell.setCellValue(sValue);
//                                    cell.setCellValue (new HSSFRichTextString (sValue));
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        catch (Exception e)
//        {
//            appInterface.handleError (e);
//            return (null);
//        }
//        return (wb);
//    }
    
    public MemoryTable excelImport (java.awt.Component parentComponent, ErrorHandlerInterface errorHandlerInterface, AppInterface appInterface)
    {
        return (excelImport (parentComponent, errorHandlerInterface, null, appInterface));
    }
    
    public MemoryTable excelImport (java.awt.Component parentComponent, ErrorHandlerInterface errorHandlerInterface, String sInitialDir, AppInterface appInterface)
    {
        MemoryTable memoryTable = null;
        HSSFWorkbook wb = null;
        File f = chooseFileToOpen (parentComponent, sInitialDir, appInterface);
        if (f != null)
        {
            wb = readWorkbook (errorHandlerInterface, f);
            if (wb != null)
            {
                memoryTable = createMemoryTable (wb);
            }
        }
        return (memoryTable);
    }
    
    public File chooseFileToOpen (java.awt.Component parentComponent, AppInterface appInterface)
    {
        return (chooseFileToOpen (parentComponent, null, appInterface));
    }
    
    public File chooseFileToOpen (java.awt.Component parentComponent, String sInitialDir, AppInterface appInterface)
    {
        JFileChooser jFileChooser = new JFileChooser ();
        ExampleFileFilter filter = new ExampleFileFilter ();
        filter.addExtension ("xls");
        filter.setDescription (appInterface.getLanguageString("Excel fájlok"));
        jFileChooser.setFileFilter (filter);
        if (sInitialDir != null)
        {
            jFileChooser.setCurrentDirectory (new java.io.File (sInitialDir));
        }
        int iDialogResult = jFileChooser.showOpenDialog (parentComponent);
        if (iDialogResult != JFileChooser.APPROVE_OPTION)
        {
            return (null);
        }
        File f = jFileChooser.getSelectedFile ();
        if (!f.exists ())
        {
            JOptionPane.showMessageDialog (parentComponent, appInterface.getLanguageString("A fájl nem található."));
            return (null);
        }
        return (f);
    }
    
    protected HSSFWorkbook readWorkbook (ErrorHandlerInterface errorHandlerInterface, File f)
    {
        HSSFWorkbook wb = null;
        
        FileInputStream fileIn;
        try
        {
            fileIn = new FileInputStream (f);
        }
        catch (FileNotFoundException e)
        {
            if (errorHandlerInterface != null)
            {
                errorHandlerInterface.handleError (e);
            }
            else
            {
                JOptionPane.showMessageDialog (null, e.getLocalizedMessage ());
            }
            return (null);
        }
        try
        {
            wb = new HSSFWorkbook (fileIn);
            fileIn.close ();
        }
        catch (IOException e)
        {
            if (errorHandlerInterface != null)
            {
                errorHandlerInterface.handleError (e);
            }
            else
            {
                JOptionPane.showMessageDialog (null, e.getLocalizedMessage ());
            }
            return (null);
        }
        return (wb);
    }
    
    protected MemoryTable createMemoryTable (HSSFWorkbook wb)
    {
        MemoryTable memoryTable = null;
        HSSFRow row;
        HSSFCell cell;
        Vector vColumnNames = new Vector ();
        Vector vData = new Vector ();
        
        //System.err.println(wb.getNumberOfSheets());
        if (wb.getNumberOfSheets () < 1)
        {
            return (null);
        }
        HSSFSheet sheet = wb.getSheetAt (0);
        //System.err.println(sheet.getPhysicalNumberOfRows());
        if (sheet.getPhysicalNumberOfRows () < 1)
        {
            return (null);
        }
        row = sheet.getRow (0);
        //System.err.println(row.getFirstCellNum());
        //System.err.println(row.getLastCellNum());
        
        for (short sh = row.getFirstCellNum (); sh < row.getLastCellNum (); sh++)
        {
            vColumnNames.add (new Integer (sh));
        }
        memoryTable = new MemoryTable (vColumnNames, 0);
        
        for (int i = 0; i < sheet.getPhysicalNumberOfRows (); i++)
        {
            //System.err.println("row="+Integer.toString(i));
            row = sheet.getRow (i);
            vData = new Vector ();
            for (short sh = row.getFirstCellNum (); sh < row.getLastCellNum (); sh++)
            {
                //System.err.println("column="+Integer.toString(sh));
                cell = row.getCell (sh);
                if (cell == null)
                {
                    vData.add ("");
                }
                else
                {
                    if (cell.getCellType () == HSSFCell.CELL_TYPE_STRING)
                    {
                        vData.add (cell.getStringCellValue ());
                    }
                    else if (cell.getCellType () == HSSFCell.CELL_TYPE_NUMERIC)
                    {
                        vData.add (new Double (cell.getNumericCellValue ()));
                    }
                    else if (cell.getCellType () == HSSFCell.CELL_TYPE_BOOLEAN)
                    {
                        vData.add ((cell.getBooleanCellValue () ? "1" : "0"));
                    }
                    else
                    {
                        vData.add (cell.getStringCellValue ());
                    }
                }
            }
            memoryTable.addRow (vData);
        }
        
        //        cell = row.getCell((short)0);
        //        System.err.println(cell.getStringCellValue());
        //        cell = row.getCell((short)1);
        //        System.err.println(cell.getStringCellValue());
        //        System.err.println("---");
        
        //        HSSFCell cell = row.getCell((short)3);
        //        cell.getCellType();
        //        if (cell == null)
        //            cell = row.createCell((short)3);
        //        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        //        cell.setCellValue("a test");
        return (memoryTable);
    }
}
