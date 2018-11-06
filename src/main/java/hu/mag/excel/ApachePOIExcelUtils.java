package hu.mag.excel;

import hu.mgx.app.common.ErrorHandlerInterface;
import hu.mgx.swing.table.MemoryTable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 *
 * @author MaG
 */
public abstract class ApachePOIExcelUtils {

    public static Cell createCellInRow(Row row, int iColumn, String sValue, CellStyle cellStyle) {
        Cell cell = row.createCell(iColumn);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(sValue);
        return (cell);
    }

    public static Cell createCellInRow(Row row, int iColumn, double dValue, CellStyle cellStyle) {
        Cell cell = row.createCell(iColumn);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(dValue);
        return (cell);
    }

    public static Cell createFormulaCellInRow(Row row, int iColumn, String sFormula, CellStyle cellStyle) {
        Cell cell = row.createCell(iColumn);
        cell.setCellType(CellType.FORMULA);
        cell.setCellFormula(sFormula);
        cell.setCellStyle(cellStyle);
        return (cell);
    }

    public static Vector<MemoryTable> readExcelIntoMemoryTables(File file) {
        boolean bDebug = false;
        Workbook wb = null;
        int iFirstRow = 0;
        int iLastRow = 0;
        int iFirstColumn = 0;
        int iLastColumn = 0;
        Sheet sheet;
        Row row;
        Cell cell;
        Vector<MemoryTable> vTables = new Vector<MemoryTable>();
        MemoryTable memoryTable;
        Vector<String> vColumnNames;
        Vector<Vector<Object>> vData;
        Vector<Object> vRow;

        try {
            wb = WorkbookFactory.create(file);
        } catch (IOException ioe) {
        } catch (InvalidFormatException ife) {
        } catch (EncryptedDocumentException ede) {
        }
        if (wb == null) {
            return (vTables);
        }
        int iNumberOfSheets = wb.getNumberOfSheets();
        if (bDebug) {
            System.out.println("Number of Sheets = " + Integer.toString(iNumberOfSheets));
        }
        for (int iSheet = 0; iSheet < iNumberOfSheets; iSheet++) {
            iFirstRow = wb.getSheetAt(iSheet).getFirstRowNum();
            iLastRow = wb.getSheetAt(iSheet).getLastRowNum();
            if (bDebug) {
                System.out.println("First row of sheet #" + Integer.toString(iSheet) + " = " + Integer.toString(iFirstRow));
                System.out.println("Last row of sheet #" + Integer.toString(iSheet) + " = " + Integer.toString(iLastRow));
            }
            iFirstColumn = 1000;
            iLastColumn = 0;
            sheet = wb.getSheetAt(iSheet);
            //System.out.println(sheet.getSheetName());
            //search for first and last column
            for (int iRow = iFirstRow; iRow <= iLastRow; iRow++) {
                row = sheet.getRow(iRow);
                if (row != null) {
                    if (row.getFirstCellNum() < iFirstColumn) {
                        iFirstColumn = row.getFirstCellNum();
                    }
                    if (row.getLastCellNum() > iLastColumn) {
                        iLastColumn = row.getLastCellNum();
                    }
                }
            }
            --iLastColumn; //MaG 2018.03.10.
            if (bDebug) {
                System.out.println("First column of sheet #" + Integer.toString(iSheet) + " = " + Integer.toString(iFirstColumn));
                System.out.println("Last column of sheet #" + Integer.toString(iSheet) + " = " + Integer.toString(iLastColumn));
            }
            vData = new Vector<Vector<Object>>();
            vRow = new Vector<Object>();
            vColumnNames = new Vector<String>();
            vColumnNames.addElement("Rownum");
            //MaG 2018.03.13.
            if (iFirstColumn < 0) {
                iFirstColumn = 0;
            }
            for (int iColumn = iFirstColumn; iColumn <= iLastColumn; iColumn++) {
                vColumnNames.addElement("Column" + Integer.toString(iColumn));
            }
            for (int iRow = iFirstRow; iRow <= iLastRow; iRow++) {
                row = sheet.getRow(iRow);
                vRow = new Vector<Object>();
                //vRow.addElement(Integer.toString(iRow));
                vRow.addElement(new Integer(iRow));
                for (int iColumn = iFirstColumn; iColumn <= iLastColumn; iColumn++) {
                    if (row != null) {
                        cell = row.getCell(iColumn); //, Row.RETURN_BLANK_AS_NULL);
                        if (cell != null) {
//                            if (iSheet == 1) {
//                                if (iRow == 1) {
//                                    System.out.println(Integer.toString(iColumn) + " " + Integer.toString(cell.getColumnIndex()) + " " + cell.getStringCellValue());
//                                }
//                            }
                            //int iCellType = cell.getCellType();
                            //switch (iCellType) {

                            switch (cell.getCellTypeEnum()) {
                                //case Cell.CELL_TYPE_STRING:
                                case STRING:
                                    vRow.add(cell.getStringCellValue());
                                    break;
                                //case Cell.CELL_TYPE_NUMERIC:
                                case NUMERIC:
                                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                        vRow.add(cell.getDateCellValue());
                                    } else {
                                        //vRow.add(Double.toString(cell.getNumericCellValue()));
                                        vRow.add(new Double(cell.getNumericCellValue()));
                                    }
                                    break;
                                case _NONE:
                                    vRow.add("");
                                    break;
                                case BLANK:
                                    vRow.add("");
                                    break;
                                case FORMULA:
                                    switch (cell.getCachedFormulaResultTypeEnum()) {
                                        case NUMERIC:
                                            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                                vRow.add(cell.getDateCellValue());
                                            } else {
                                                vRow.add(new Double(cell.getNumericCellValue()));
                                            }
                                            break;
                                        case STRING:
                                            vRow.add(cell.getStringCellValue());
                                            break;
                                        default:
                                            vRow.add(cell.getCellFormula());
                                            break;
                                    }
                                    break;
                                case BOOLEAN:
                                    vRow.add(cell.getBooleanCellValue());
                                    break;
                                case ERROR:
                                    vRow.add(cell.getErrorCellValue());
                                    break;
                                default:
                                    vRow.add(cell.getStringCellValue());
                            }
                        } else {
                            vRow.add("");
                        }
                    } else {
                        vRow.add("");
                    }
                }
                vData.addElement(vRow);
            }
            memoryTable = new MemoryTable(vData, vColumnNames);
            memoryTable.setName(sheet.getSheetName());
            vTables.add(memoryTable);
        }
        return (vTables);
    }

    public static void updateExcelFileWithSQL(File fileIn, File fileOut, Connection connection, String sSQL, int iSourceColumn, int iDestinationColumn, ErrorHandlerInterface ehi) {
        Workbook wb = null;
        Sheet sheet;
        Row row;
        Cell cell;
        String sCellValue;
        ResultSet rs;
        PreparedStatement ps;
        try {
            wb = WorkbookFactory.create(fileIn);
        } catch (IOException ioe) {
        } catch (InvalidFormatException ife) {
        } catch (EncryptedDocumentException ede) {
        }
        if (wb == null) {
            return;
        }
        try {
            ps = connection.prepareStatement(sSQL);
            sheet = wb.getSheetAt(0);
            for (int iRow = sheet.getFirstRowNum(); iRow <= sheet.getLastRowNum(); iRow++) {
                System.out.println(Integer.toString(iRow));
                row = sheet.getRow(iRow);
                if (row != null) {
                    cell = row.getCell(iSourceColumn);
                    if (cell != null) {
                        sCellValue = cell.getStringCellValue();
                        System.out.println(sCellValue);
                        ps.setString(1, sCellValue);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            sCellValue = rs.getString(1);
                            cell = row.createCell(iDestinationColumn);
                            cell.setCellValue(sCellValue);
                            System.out.println(sCellValue);
                        }
                    }
                }
            }
        } catch (SQLException sqle) {
            ehi.handleError(sqle);
        }

        FileOutputStream out;
        try {
            out = new FileOutputStream(fileOut);
            wb.write(out);
            out.close();
        } catch (FileNotFoundException fnfe) {
            ehi.handleError(fnfe);
        } catch (java.io.IOException ioe) {
            ehi.handleError(ioe);
        }
    }
}
