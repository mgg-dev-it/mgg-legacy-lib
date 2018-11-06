package hu.mag.excel;

//@todo task: new method: setFileName(String sFileName)
import hu.mag.lang.LookupInteger;
import hu.mag.swing.table.MagTable;
import hu.mgx.app.common.AppInterface;
import hu.mgx.app.common.FormatInterface;
import hu.mgx.app.swing.AppUtils;
import hu.mgx.app.swing.ProgressDisplay;
import hu.mgx.util.StringUtils;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author MaG
 */
public class Excel implements PropertyChangeListener {

    private java.awt.Component parentComponent = null;
    private FormatInterface formatInterface = null;
    private String sDir = "";
    private String sFile = "";

    public final int EXCEL_XLS = 1;
    public final int EXCEL_XLSX = 2;

    private ExcelTask excelTask;
    private ProgressDisplay progressDisplay;
    private int iCurrentRow = 0;

    public Excel(java.awt.Component parentComponent, FormatInterface formatInterface) {
        this.parentComponent = parentComponent;
        this.formatInterface = formatInterface;
    }

    public String getDir() {
        return (sDir);
    }

    public void setDir(String sDir) {
        this.sDir = sDir;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int iProgress = (Integer) evt.getNewValue();
            if (iProgress < 2) {
                progressDisplay.setVisible(false);
                if (iProgress == 0) {
                    AppUtils.messageBox(parentComponent, "Excel export sikeresen befejezõdött. (" + sFile + ")");
                } else {
                    AppUtils.messageBox(parentComponent, "Excel export nem sikerült. (" + sFile + ")");
                }
            } else {
                progressDisplay.setValue(iCurrentRow);
            }
        }
    }

    class ExcelTask extends javax.swing.SwingWorker<Boolean, Void> {

        private java.awt.Component parentComponent;
        private MagTable magTable;
        private AppInterface appInterface;
        private File file;
        private boolean bID;
        private boolean bLookupValue;
        private boolean bResult;

        public ExcelTask(java.awt.Component parentComponent, MagTable magTable, AppInterface appInterface, File file, boolean bID, boolean bLookupValue) {
            super();
            this.parentComponent = parentComponent;
            this.magTable = magTable;
            this.appInterface = appInterface;
            this.file = file;
            this.bID = bID;
            this.bLookupValue = bLookupValue;
            this.bResult = false;
        }

        @Override
        protected Boolean doInBackground() throws Exception {
            return (exportToExcel(magTable, appInterface, file, bID, bLookupValue, this));
        }

        @Override
        public void done() {
            try {
                bResult = get();
                if (bResult) {
                    //setProgress2(-1);
                    setProgress(0);
                } else {
                    //setProgress2(-2);
                    setProgress(1);
                }
            } catch (InterruptedException ignore) {
                //System.out.println(ignore.getLocalizedMessage());
            } catch (java.util.concurrent.ExecutionException e) {
                //System.out.println(e.getLocalizedMessage());
            }
        }

        public void setProgressValue(int i) {
            setProgress(i);
        }
    }

    public void exportToExcelWithFileDialog(AppInterface appInterface, MagTable magTable) {
        File f = AppUtils.chooseFileToCreateStrict(parentComponent, appInterface, "Excel fájlok", sDir, "", "xlsx", "xls");
        if (f == null) {
            return;
        }
        try {
            sDir = f.getCanonicalPath();
        } catch (java.io.IOException ioe) {
            appInterface.handleError(ioe);
            sDir = "";
        }
        sFile = f.getAbsolutePath();
        progressDisplay = new ProgressDisplay(AppUtils.getWindowParent(parentComponent), 0, magTable.getRowCount() - 1);
        progressDisplay.showDialog(parentComponent, "Excel export", f.getName());
        excelTask = new ExcelTask(parentComponent, magTable, appInterface, f, false, false);
        excelTask.addPropertyChangeListener(this);
        excelTask.execute();
    }

//    public void exportToExcelWithFileDialog2(AppInterface appInterface, MagTable magTable) {
//        File f = AppUtils.chooseFileToCreateStrict(parentComponent, appInterface, "Excel fájlok", sDir, "xlsx", "xls");
//        if (f == null) {
//            return;
//        }
//        try {
//            sDir = f.getCanonicalPath();
//        } catch (java.io.IOException ioe) {
//            appInterface.handleError(ioe);
//            sDir = "";
//        }
//        sFile = f.getAbsolutePath();
////        progressDisplay = new ProgressDisplay(AppUtils.getWindowParent(parentComponent), 0, magTable.getRowCount() - 1);
////        progressDisplay.showDialog(parentComponent, "Excel export", f.getName());
////        excelTask = new ExcelTask(parentComponent, magTable, appInterface, f, false, false);
////        excelTask.addPropertyChangeListener(this);
////        excelTask.execute();
//        exportToExcel(magTable, appInterface, f, false, false, null);
//    }
    private boolean exportToExcel(MagTable magTable, AppInterface appInterface, File file, boolean bID, boolean bLookupValue, ExcelTask task) {
        Workbook wb = null;
        if (file.getName().endsWith(".xls")) {
            wb = new HSSFWorkbook();
        }
        if (file.getName().endsWith(".xlsx")) {
            wb = new XSSFWorkbook();
        }
        if (wb == null) {
            return (false);
        }

        iCurrentRow = 0;
        //@todo task: check free memory continuously (xlsx requires more memory... but produces smaller file :)
        //@todo task: maybe use progress bar when it runs longer than 1 second
        //@todo task: check maximum number of columns 255 for *.xls, 1048576 for *.xlsx
        Sheet sheet = wb.createSheet();
        Row row = null;
        Cell cell = null;
        int iOffset = 0;
        //header
        row = sheet.createRow(0);
        for (int iColumn = 0; iColumn < magTable.getColumnCount(); iColumn++) {
            if (magTable.getMagTableModel().isIdentityColumn(iColumn)) {
                if (bID) {
                    addCellToRow(row, iColumn - iOffset, StringUtils.filterHTML(magTable.getColumnName(iColumn)), wb);
                } else {
                    ++iOffset;
                }
            } else {
                addCellToRow(row, iColumn - iOffset, StringUtils.filterHTML(magTable.getColumnName(iColumn)), wb);
                if (magTable.getMagTableModel().isLookupColumn(iColumn)) {
                    if (bLookupValue) {
                        --iOffset;
                        addCellToRow(row, iColumn - iOffset, "(" + StringUtils.filterHTML(magTable.getColumnName(iColumn)) + ")", wb);
                    }
                }
            }
        }
        //data
        for (int iRow = 0; iRow < magTable.getRowCount(); iRow++) {
            iCurrentRow = iRow;
            row = sheet.createRow(iRow + 1);
            iOffset = 0;
            for (int iColumn = 0; iColumn < magTable.getColumnCount(); iColumn++) {
                //@todo task : filter out ID columns (or parameter for control - filtering or not)
                //@todo task : add a column for lookup fields source value (maybe parametering whether add or not, whether exporting tha source or the display value)
                //@todo task : use different setCellValue methods based on getValueAt returning object type (class)
                if (magTable.getMagTableModel().isIdentityColumn(iColumn)) {
                    if (bID) {
                        addCellToRow(row, iColumn - iOffset, magTable.getValueAt(iRow, iColumn), wb);
                    } else {
                        ++iOffset;
                    }
                } else {
                    cell = addCellToRow(row, iColumn - iOffset, magTable.getValueAt(iRow, iColumn), wb);
                    if (magTable.getMagTableModel().isLookupColumn(iColumn)) {
                        cell.setCellValue(StringUtils.isNull(magTable.getMagTableModel().getColumnLookupField(iColumn).getDisplay(magTable.getValueAt(iRow, iColumn)), ""));
                        if (bLookupValue) {
                            --iOffset;
                            addCellToRow(row, iColumn - iOffset, magTable.getValueAt(iRow, iColumn), wb);
                        }
                    }
                }
                //teszt:
                if (wb instanceof XSSFWorkbook) {
                    java.awt.Color c = magTable.getMagTableModel().getCellColorBackground(magTable.convertRowIndexToModel(iRow), magTable.convertColumnIndexToModel(iColumn));
                    if (c != null) {
                        XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();
                        XSSFColor myColor = new XSSFColor(c);
                        style.setFillForegroundColor(myColor);
                        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                        row.getCell(iColumn).setCellStyle(style);
                    }
                }
            }
            if (task != null) {
                task.setProgressValue(10 + iRow % 2);
            }
        }
        for (int iColumn = 0; iColumn < magTable.getColumnCount(); iColumn++) {
            sheet.autoSizeColumn(iColumn, true);
        }

        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            wb.write(out);
            out.close();
        } catch (FileNotFoundException fnfe) {
            appInterface.handleError(fnfe);
        } catch (java.io.IOException ioe) {
            appInterface.handleError(ioe);
        } finally {
//            progressDisplay.setVisible(false);
        }

        return (true);
    }

    private Cell addCellToRow(Row row, int index, Object value, Workbook wb) {
        Cell cell = row.createCell(index);
        //Cell.CELL_TYPE_BLANK
        //Cell.CELL_TYPE_BOOLEAN
        //Cell.CELL_TYPE_ERROR
        //Cell.CELL_TYPE_FORMULA
        //Cell.CELL_TYPE_NUMERIC
        //Cell.CELL_TYPE_STRING
        if (value == null) {
            cell.setCellValue("");
        } else if (value.getClass().equals(java.lang.Short.class)) {
            cell.setCellValue(((java.lang.Short) value).doubleValue());
        } else if (value.getClass().equals(java.lang.Integer.class)) {
            cell.setCellValue(((java.lang.Integer) value).doubleValue());
        } else if (value.getClass().equals(java.lang.Long.class)) {
            cell.setCellValue(((java.lang.Long) value).doubleValue());
        } else if (value.getClass().equals(java.lang.Double.class)) {
            cell.setCellValue(((java.lang.Double) value).doubleValue());
        } else if (value.getClass().equals(java.lang.Boolean.class)) {
            cell.setCellValue(((java.lang.Boolean) value).booleanValue());
        } else if (value.getClass().equals(java.math.BigDecimal.class)) {
            cell.setCellValue(((java.math.BigDecimal) value).doubleValue());
        } else if (value.getClass().equals(java.util.Date.class)) {
            cell.setCellValue((java.util.Date) value);
        } else if (value.getClass().equals(java.sql.Date.class)) {
//            System.out.println("---");
//            System.out.println(((java.sql.Date) value).toString());
//            System.out.println(new Long(((java.sql.Date) value).getTime()).toString());
//            System.out.println(new java.util.Date(((java.sql.Date) value).getTime()).toString());
            //java.sql.Date sqldate = (java.sql.Date) value;
            //java.util.Date utildate = new java.util.Date(sqldate.getTime());
            //cell.setCellValue(utildate);
            CellStyle cellStyle = wb.createCellStyle();
            CreationHelper createHelper = wb.getCreationHelper();
            //@todo task : date format from appformat
            short dateFormat = createHelper.createDataFormat().getFormat("yyyy-MM-dd");
            if (formatInterface != null) {
                dateFormat = createHelper.createDataFormat().getFormat(formatInterface.getDatePattern());
            }
            cellStyle.setDataFormat(dateFormat);
            cell.setCellValue(new java.util.Date(((java.sql.Date) value).getTime()));
            cell.setCellStyle(cellStyle);
        } //        else if (value.getClass().equals(java.sql.Time.class)) {
        //        }
        //        else if (value.getClass().equals(java.sql.Timestamp.class)) {
        //        }
        else if (value.getClass().equals(LookupInteger.class)) {
            cell.setCellValue(((LookupInteger) value).getDisplay());
        } else {
            //cell.setCellValue(StringUtils.isNull(value, ""));
            String s = StringUtils.isNull(value, "");
            boolean bMultiLine = false;
            if (s.contains("<br>")) {
                s = StringUtils.stringReplace(s, "<br>", StringUtils.sCrLf);
                bMultiLine = true;
            }
            s = StringUtils.filterHTML(s);
            cell.setCellValue(s);
            if (bMultiLine) {
                CellStyle cellStyle = wb.createCellStyle();
                cellStyle.setWrapText(true);
                cell.setCellStyle(cellStyle);
            }
        }
        return (cell);
    }

    protected boolean digitsOnly(String s) {
        String sDigits = new String("0123456789");
        for (int i = 0; i < s.length(); i++) {
            if (sDigits.indexOf(s.substring(i, i + 1)) < 0) {
                return (false);
            }
        }
        return (true);
    }

}
