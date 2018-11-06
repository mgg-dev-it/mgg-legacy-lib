package hu.mgx.jexcel;

import java.io.*;
import java.util.*;

import jxl.*;
import jxl.write.*;

import hu.mgx.app.common.*;
import hu.mgx.swing.table.*;
import hu.mgx.util.*;

public class JExcelUtils {

    public JExcelUtils() {
    }

    public boolean writeMemoryTableIntoExcelFile(AppInterface appInterface, MemoryTable memoryTable, String sFilename, String sNullValue) {
        return (writeMemoryTableIntoExcelFile(appInterface, memoryTable, sFilename, sNullValue, ""));
    }

    public boolean writeMemoryTableIntoExcelFile(AppInterface appInterface, MemoryTable memoryTable, String sFilename, String sNullValue, String sSelect) {
        return (writeMemoryTableIntoExcelFile(appInterface, memoryTable, new File(sFilename), sNullValue, sSelect));
    }

    public boolean writeMemoryTableIntoExcelFile(AppInterface appInterface, MemoryTable memoryTable, File f, String sNullValue, String sSelect) {
        Label label = null;
        try {
            WritableWorkbook writableWorkbook = Workbook.createWorkbook(f);
            WritableSheet sheet = writableWorkbook.createSheet("Export", 0);
            for (int iRow = -1; iRow < memoryTable.getRowCount(); ++iRow) {
                for (int iCol = 0; iCol < memoryTable.getColumnCount(); ++iCol) {
                    //System.out.println(Integer.toString(iRow) + " - " + Integer.toString(iCol));
                    if (iRow == -1) {
                        label = new Label(iCol, iRow + 1, StringUtils.isNull(memoryTable.getColumnName(iCol), sNullValue));
                    } else {
                        label = new Label(iCol, iRow + 1, StringUtils.isNull(memoryTable.getValueAt(iRow, iCol), sNullValue));
                    }
                    try {
                        sheet.addCell(label);
                    } catch (WriteException e) {
                        appInterface.handleError(e);
                        return (false);
                    }
                }
            }
            sSelect = StringUtils.isNull(sSelect, "");
            if (!sSelect.equals("")) {
                WritableSheet sheet2 = writableWorkbook.createSheet("Select", 1);
                label = new Label(0, 0, sSelect);
                try {
                    sheet2.addCell(label);
                } catch (WriteException e) {
                    appInterface.handleError(e);
                    return (false);
                }
            }
//            label = new Label(0, 2, "label");
//            jxl.write.Number number = new jxl.write.Number(3, 4, 3.1415926);
            try {
//                sheet.addCell(number);
                writableWorkbook.write();
                writableWorkbook.close();
            } catch (jxl.write.WriteException e) {
                appInterface.handleError(e);
                return (false);
            }
        } catch (IOException ioe) {
            appInterface.handleError(ioe);
            return (false);
        }
        return (true);
    }

    public boolean writeMemoryTableIntoExcelSheet(AppInterface appInterface, MemoryTable memoryTable, WritableWorkbook writableWorkbook, String sSheetTitle, int iSheetIndex, String sNullValue) {
        return (writeMemoryTableIntoExcelSheet(appInterface, memoryTable, writableWorkbook, sSheetTitle, iSheetIndex, sNullValue, null));
    }

    public boolean writeMemoryTableIntoExcelSheet(AppInterface appInterface, MemoryTable memoryTable, WritableWorkbook writableWorkbook, String sSheetTitle, int iSheetIndex, String sNullValue, String sColumnTypes) {
        Label label = null;
        WritableSheet sheet = writableWorkbook.createSheet(sSheetTitle, iSheetIndex);
        WritableCellFormat integerFormat = new WritableCellFormat(NumberFormats.INTEGER);
        jxl.write.Number number2 = null;
        if (sColumnTypes == null) {
            sColumnTypes = "";
        }
        String sa[] = sColumnTypes.split(",");
        boolean bInt = false;
        for (int iRow = -1; iRow < memoryTable.getRowCount(); ++iRow) {
            for (int iCol = 0; iCol < memoryTable.getColumnCount(); ++iCol) {
                //System.out.println(Integer.toString(iRow) + " - " + Integer.toString(iCol));
                if (iRow == -1) {
                    label = new Label(iCol, iRow + 1, StringUtils.isNull(memoryTable.getColumnName(iCol), sNullValue));
                } else {
                    bInt = false;
                    if (sa.length > iCol) {
                        if (sa[iCol].equals("int")) {
                            bInt = true;
                        }
                    }
                    if (bInt) {
                        number2 = new jxl.write.Number(iCol, iRow + 1, StringUtils.bigDecimalValue(StringUtils.isNull(memoryTable.getValueAt(iRow, iCol), "0")).doubleValue(), integerFormat);
                    } else {
                        label = new Label(iCol, iRow + 1, StringUtils.isNull(memoryTable.getValueAt(iRow, iCol), sNullValue));
                    }
                }
                try {
                    if (bInt) {
                        sheet.addCell(number2);
                    } else {
                        sheet.addCell(label);
                    }
                } catch (WriteException e) {
                    appInterface.handleError(e);
                    return (false);
                }
            }
        }
        CellView cv = null;
        for (int i = 0; i < sheet.getColumns(); i++) {
            cv = sheet.getColumnView(i);
            cv.setAutosize(true);
            if (sa.length > i) {
                if (sa[i].equals("hide")) {
                    cv.setSize(0);
                    cv.setAutosize(false);
                }
            }
            sheet.setColumnView(i, cv);
        }
        return (true);
    }

    public String getCellValueAsString(Sheet sheet, int iRow, int iColumn) {
        if (iColumn >= sheet.getColumns() || iRow >= sheet.getRows()) {
            return ("");
        }

        Cell cell = sheet.getCell(iColumn, iRow);
        return (StringUtils.isNull(cell.getContents(), "").trim());
    }

    private Vector<String> getColumnNames(Sheet sheet) {
        Vector<String> vColumnNames = new Vector<String>();
        int i = 0;
        String s = getCellValueAsString(sheet, 0, i);
        while (!s.equals("") && i < 100) {
            vColumnNames.add(s);
            s = getCellValueAsString(sheet, 0, ++i);
        }
        return (vColumnNames);
    }

    public Vector getSheetNames(Workbook workbook) {
        Vector<String> vSheetNames = null;

        String sSheetNames[] = workbook.getSheetNames();
        vSheetNames = new Vector<String>();
        for (int i = 0; i < sSheetNames.length; i++) {
            vSheetNames.add(sSheetNames[i]);
        }
        return (vSheetNames);
    }

    public Workbook getWorkbook(AppInterface appInterface, String sFileName) {
        Workbook workbook = null;

        try {
            workbook = Workbook.getWorkbook(new File(sFileName));
        } catch (Exception e) {
            appInterface.handleError(e);
            return (null);
        }
        return (workbook);
    }

    public Sheet getSheetByName(Workbook workbook, String sSheetName) {
        for (int i = 0; i < workbook.getSheets().length; i++) {
            if (workbook.getSheet(i).getName().equals(sSheetName)) {
                return(workbook.getSheet(i));
            }
        }
        return(null);
    }

    public MemoryTable readExcelFileIntoMemoryTable(AppInterface appInterface, String sFileName) {
        MemoryTable mt = null;
        Workbook workbook = null;
        Sheet sheet = null;
        Vector<String> vColumnNames = null;
        Vector<String> vRow = null;
        int iColumnCount = 0;
        boolean bEmptyRow = false;
        String s = null;
        int iRow = 1;

        try {
            workbook = Workbook.getWorkbook(new File(sFileName));
        } catch (Exception e) {
            appInterface.handleError(e);
            return (null);
        }
        sheet = workbook.getSheet(0);
        vColumnNames = getColumnNames(sheet);
        mt = new MemoryTable(vColumnNames, 0);
        iColumnCount = vColumnNames.size();

        while (!bEmptyRow && iRow < sheet.getRows()) {
            bEmptyRow = true;
            vRow = new Vector<String>();
            for (int i = 0; i < iColumnCount; i++) {
                s = getCellValueAsString(sheet, iRow, i);
                vRow.add(s);
                if (!s.equals("")) {
                    bEmptyRow = false;
                }
            }
            mt.addRow(vRow);
            ++iRow;
        }

        workbook.close();
        return (mt);
    }
}
