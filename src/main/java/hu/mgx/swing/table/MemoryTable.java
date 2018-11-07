package hu.mgx.swing.table;

import hu.mag.swing.table.MagTable;
import hu.mgx.app.common.AppInterface;
import hu.mgx.app.common.ErrorHandlerInterface;
import hu.mgx.app.common.FormatInterface;
import hu.mgx.app.common.LoggerInterface;
import hu.mgx.util.BigDecimalUtils;
import hu.mgx.util.BooleanUtils;
import hu.mgx.util.DateTimeUtils;
import hu.mgx.util.DoubleUtils;
import hu.mgx.util.FloatUtils;
import hu.mgx.util.IntegerUtils;
import hu.mgx.util.LongUtils;
import hu.mgx.util.ShortUtils;
import hu.mgx.util.StringUtils;
import hu.mgx.util.VectorUtils;
import java.io.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.table.*;

public class MemoryTable extends DefaultTableModel {

    private HashMap<Integer, Boolean> hmEditableColumns = new HashMap<>();
    private HashMap<Integer, Boolean> hmSortableColumns = new HashMap<>();
    private String sName = "";

    public MemoryTable() {
        super();
    }

    public MemoryTable(Vector vData, Vector vColumnNames) {
        super(vData, vColumnNames);
    }

    public MemoryTable(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }

    public MemoryTable(Object[] columnNames) {
        super(columnNames, 0);
    }

    public MemoryTable(Object[] columnNames, int rowCount) {
        super(columnNames, rowCount);
    }

    public MemoryTable(Vector columnNames) {
        super(columnNames, 0);
    }

    public MemoryTable(Vector columnNames, int rowCount) {
        super(columnNames, rowCount);
    }

    public Object getValueAt(int row, String columnName) {
        int column = this.findColumn(columnName);
        if (column < 0) {
            return ("");
        }
        if (column >= getColumnCount()) {
            return ("");
        }
        if (row < 0) {
            return ("");
        }
        if (row >= getRowCount()) {
            return ("");
        }
        return (this.getValueAt(row, column));
    }

    public Object getValue(String columnName) {
        return (getValueAt(0, columnName));
    }

    public String getStringValueAt(int row, int col) {
        return (StringUtils.isNull(this.getValueAt(row, col), ""));
    }

    public String getStringValueAt(int row, String columnName) {
        return (StringUtils.isNull(this.getValueAt(row, columnName), ""));
    }

    public String getStringValue(String columnName) {
        return (StringUtils.isNull(this.getValueAt(0, columnName), ""));
    }

    public Short getShortValueAt(int row, int col) {
        return (ShortUtils.convertToShort(this.getValueAt(row, col)));
    }

    public Short getShortValueAt(int row, String columnName) {
        return (ShortUtils.convertToShort(this.getValueAt(row, columnName)));
    }

    public Short getShortValue(String columnName) {
        return (getShortValueAt(0, columnName));
    }

    public Long getLongValueAt(int row, int col) {
        return (LongUtils.convertToLong(this.getValueAt(row, col)));
    }

    public Long getLongValueAt(int row, String columnName) {
        return (LongUtils.convertToLong(this.getValueAt(row, columnName)));
    }

    public Long getLongValue(String columnName) {
        return (getLongValueAt(0, columnName));
    }

    public Integer getIntegerValueAt(int row, int col) {
        return (IntegerUtils.convertToInteger(this.getValueAt(row, col)));
    }

    public Integer getIntegerValueAt(int row, String columnName) {
        return (IntegerUtils.convertToInteger(this.getValueAt(row, columnName)));
    }

    public Integer getIntegerValue(String columnName) {
        return (getIntegerValueAt(0, columnName));
    }

    public int getIntValueAt(int row, int col) {
        Integer integer = IntegerUtils.convertToInteger(this.getValueAt(row, col));
        if (integer == null) {
            return (0);
        }
        return (integer.intValue());
    }

    public int getIntValueAt(int row, String columnName) {
        Integer integer = IntegerUtils.convertToInteger(this.getValueAt(row, columnName));
        if (integer == null) {
            return (0);
        }
        return (integer.intValue());
    }

    public int getIntValue(String columnName) {
        return (getIntValueAt(0, columnName));
    }

    public double getDblValueAt(int row, int column) {
        return (StringUtils.doubleValue(this.getValueAt(row, column)));
    }

    public double getDblValueAt(int row, String columnName) {
        return (StringUtils.doubleValue(this.getValueAt(row, columnName)));
    }

    public double getDblValue(String columnName) {
        return (StringUtils.doubleValue(this.getValueAt(0, columnName)));
    }

    public Double getDoubleValueAt(int row, int col) {
        return (DoubleUtils.convertToDouble(this.getValueAt(row, col)));
    }

    public Double getDoubleValueAt(int row, String columnName) {
        return (DoubleUtils.convertToDouble(this.getValueAt(row, columnName)));
    }

    public Double getDoubleValue(String columnName) {
        return (getDoubleValueAt(0, columnName));
    }

    public Float getFloatValueAt(int row, int col) {
        return (FloatUtils.convertToFloat(this.getValueAt(row, col)));
    }

    public Float getFloatValueAt(int row, String columnName) {
        return (FloatUtils.convertToFloat(this.getValueAt(row, columnName)));
    }

    public Float getFloatValue(String columnName) {
        return (getFloatValueAt(0, columnName));
    }

    public BigDecimal getBigDecimalValueAt(int row, int col) {
        return (BigDecimalUtils.convertToBigDecimal(this.getValueAt(row, col)));
    }

    public BigDecimal getBigDecimalValueAt(int row, String columnName) {
        return (BigDecimalUtils.convertToBigDecimal(this.getValueAt(row, columnName)));
    }

    public BigDecimal getBigDecimalValue(String columnName) {
        return (getBigDecimalValueAt(0, columnName));
    }

    public java.util.Date getUtilDateValueAt(int row, int col) {
        return (DateTimeUtils.convertToUtilDate(getValueAt(row, col)));
    }

    public java.util.Date getUtilDateValueAt(int row, String columnName) {
        return (DateTimeUtils.convertToUtilDate(getValueAt(row, columnName)));
    }

    public java.util.Date getUtilDateValueAt(int row, String columnName, FormatInterface fi) {
        return (DateTimeUtils.convertToUtilDate(fi, getValueAt(row, columnName)));
    }

    public java.util.Date getUtilDateValueAt(FormatInterface fi, int row, String columnName) {
        return (DateTimeUtils.convertToUtilDate(fi, getValueAt(row, columnName)));
    }

    public java.util.Date getUtilDateValueAt(SimpleDateFormat sdf, int row, String columnName) {
        return (DateTimeUtils.convertToUtilDate(sdf, getValueAt(row, columnName)));
    }

    public java.util.Date getUtilDateValue(String columnName) {
        return (getUtilDateValueAt(0, columnName));
    }

    public java.util.Date getUtilDateValue(FormatInterface fi, String columnName) {
        return (getUtilDateValueAt(fi, 0, columnName));
    }

    public void addRow() {
        super.setRowCount(super.getRowCount() + 1);
    }

    public void addRow(Object... oValues) {
        super.setRowCount(super.getRowCount() + 1);
        for (int iColumn = 0; iColumn < oValues.length; iColumn++) {
            if (iColumn < this.getColumnCount()) {
                setValueAt(oValues[iColumn], this.getRowCount() - 1, iColumn);
            }
        }
    }

    public void setValueAt(Object aValue, int row, int column) {
        super.setValueAt(aValue, row, column);
    }

    public void setValueAt(Object aValue, int row, String columnName) {
        int column = this.findColumn(columnName);
        if (column >= 0 && column < getColumnCount()) {
            super.setValueAt(aValue, row, column);
        }
    }

    public boolean isCellEditable(int row, int column) {
        if (hmEditableColumns.containsKey(new Integer(column))) {
            Boolean b = hmEditableColumns.get(new Integer(column));
            return (b.booleanValue());
        }
        return (false); //alapértelmezésben egyik cella sem szerkeszthető
    }

    public boolean isColumnSortable(int column) {
        if (hmSortableColumns.containsKey(new Integer(column))) {
            Boolean b = hmSortableColumns.get(new Integer(column));
            return (b.booleanValue());
        }
        return (true); //alapértelmezésben minden oszlop szerint rendezhető
    }

    public Class getColumnClass(int columnIndex) {
        if (this.getRowCount() < 1) {
            return (Object.class);
        }
        if (columnIndex < 0) {
            return (Object.class);
        }
        if (columnIndex >= getColumnCount()) {
            return (Object.class);
        }
        if (this.getValueAt(0, columnIndex) == null) {
            return (Object.class);
        }
        return (this.getValueAt(0, columnIndex).getClass());
    }

    public void dump(PrintStream ps) {
        ps.println(getName());
        for (int iColumn = 0; iColumn < super.getColumnCount(); iColumn++) {
            ps.print(getColumnName(iColumn));
            ps.print(";");
        }
        ps.println();
        for (int iRow = 0; iRow < super.getRowCount(); iRow++) {
            for (int iColumn = 0; iColumn < super.getColumnCount(); iColumn++) {
                ps.print(getValueAt(iRow, iColumn));
                ps.print(";");
            }
            ps.println();
        }
    }

    public void dump(LoggerInterface loggerInterface) {
        loggerInterface.logLine(getName());
        String sLine = "";
        for (int iColumn = 0; iColumn < super.getColumnCount(); iColumn++) {
            sLine += getColumnName(iColumn);
            sLine += ";";
        }
        loggerInterface.logLine(sLine);
        for (int iRow = 0; iRow < super.getRowCount(); iRow++) {
            sLine = "";
            for (int iColumn = 0; iColumn < super.getColumnCount(); iColumn++) {
                sLine += getValueAt(iRow, iColumn);
                sLine += ";";
            }
            loggerInterface.logLine(sLine);
        }
    }

    public void dumpRow(LoggerInterface loggerInterface, int iRow) {
        String sLine = "";
        if (iRow < 0 || iRow >= super.getRowCount()) {
            return;
        }
        for (int iColumn = 0; iColumn < super.getColumnCount(); iColumn++) {
            sLine += getValueAt(iRow, iColumn);
            sLine += ";";
        }
        loggerInterface.logLine(sLine);
    }

    public void setColumnEditable(int iColumn, boolean bEditable) {
        if (hmEditableColumns.containsKey(new Integer(iColumn))) {
            hmEditableColumns.remove(new Integer(iColumn));
        }
        hmEditableColumns.put(new Integer(iColumn), new Boolean(bEditable));
    }

    public void setColumnSortable(int iColumn, boolean bSortable) {
        if (hmSortableColumns.containsKey(new Integer(iColumn))) {
            hmSortableColumns.remove(new Integer(iColumn));
        }
        hmSortableColumns.put(new Integer(iColumn), new Boolean(bSortable));
    }

    public String getRowAsString(int iRow, String sDelimiter) {
        StringBuffer sbRetVal = new StringBuffer("");
        for (int iColumn = 0; iColumn < super.getColumnCount(); iColumn++) {
            sbRetVal.append(StringUtils.isNull(this.getValueAt(iRow, iColumn), ""));
            if (iColumn < super.getColumnCount() - 1) {
                sbRetVal.append(sDelimiter);
            }
        }
        return (sbRetVal.toString());
    }

    public Vector getRow(int iRow) {
        return ((Vector) super.getDataVector().elementAt(iRow));
    }

    public Vector getSubRow(int iRow, int[] iColumnIndexes) {
        Vector vRow = (Vector) super.getDataVector().elementAt(iRow);
        Vector vReturn = new Vector();
        for (int i = 0; i < iColumnIndexes.length; i++) {
            vReturn.add(vRow.elementAt(iColumnIndexes[i]));
        }
        return (vReturn);
    }

    public Vector getColumn(String columnName) {
        Vector vColumn = new Vector();
        int iColumn = this.findColumn(columnName);
        if (iColumn < 0) {
            return (vColumn);
        }
        if (iColumn >= getColumnCount()) {
            return (vColumn);
        }
        for (int iRow = 0; iRow < getRowCount(); iRow++) {
            vColumn.add(this.getValueAt(iRow, iColumn));
        }
        return (vColumn);
    }

    public Vector<Object> getColumnAsVector(String columnName) {
        return (getColumnAsVector(this.findColumn(columnName)));
    }

    public Vector<Object> getColumnAsVector(int iColumn) {
        Vector<Object> vData = new Vector<Object>();
        if (iColumn < 0) {
            return (vData);
        }
        if (iColumn >= getColumnCount()) {
            return (vData);
        }
        for (int iRow = 0; iRow < getRowCount(); iRow++) {
            vData.add(this.getValueAt(iRow, iColumn));
        }
        return (vData);
    }

    public Vector getColumnAsDataVector(int iColumn) {
        Vector vRow;// = new Vector();
        Vector<Vector> vData = new Vector<Vector>();
        if (iColumn < 0) {
            return (vData);
        }
        if (iColumn >= getColumnCount()) {
            return (vData);
        }
        for (int iRow = 0; iRow < getRowCount(); iRow++) {
            vRow = new Vector();
            vRow.add(this.getValueAt(iRow, iColumn));
            vData.add(vRow);
        }
        return (vData);
    }

    public Vector getColumnAsDataVector(String columnName) {
        return (getColumnAsDataVector(this.findColumn(columnName)));
    }

    public Vector getColumnNames() {
        Vector<String> v = new Vector<String>();
        for (int i = 0; i < getColumnCount(); i++) {
            v.add(getColumnName(i));
        }
        return (v);
    }

    public static MemoryTable readTabSeparatedStringRecordsIntoMemoryTable(Vector<String> vLines) {
        MemoryTable mt = null;
        Vector<String> vColumnNames = new Vector<String>();
        Vector<String> vRow = null;
        int iColumnCount = 0;
        String sCells[] = vLines.elementAt(0).split("\\t");
        for (int i = 0; i < sCells.length; i++) {
            vColumnNames.add(sCells[i]);
        }
        mt = new MemoryTable(vColumnNames, 0);
        iColumnCount = sCells.length;
        for (int iLine = 1; iLine < vLines.size(); iLine++) {
            sCells = vLines.elementAt(iLine).split("\\t");
            vRow = new Vector<String>();
            for (int i = 0; i < sCells.length; i++) {
                vRow.add(sCells[i]);
            }
            while (vRow.size() < iColumnCount) {
                vRow.add("");
            }
            mt.addRow(vRow);
        }
        return (mt);
    }

    public static MemoryTable readCSVFileIntoMemoryTable(AppInterface appInterface, String sFileName) {
        File file = null;
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        String sLine = null;
        Vector<String> vLines = new Vector<String>();
        String sCells[] = null;
        MemoryTable mt = null;
        Vector<String> vColumnNames = new Vector<String>();
        Vector<String> vRow = null;
        int iColumnCount = 0;

        try {
            file = new File(sFileName);
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            while ((sLine = br.readLine()) != null) {
                //appInterface.logLine(sLine);
                vLines.add(sLine);
            }
        } catch (FileNotFoundException fnfe) {
            appInterface.handleError(fnfe);
        } catch (IOException ioe) {
            appInterface.handleError(ioe);
        }
        if (vLines.size() < 1) {
            return (mt);
        }
        sCells = vLines.elementAt(0).split(";");
        for (int i = 0; i < sCells.length; i++) {
            vColumnNames.add(sCells[i]);
        }
        mt = new MemoryTable(vColumnNames, 0);
        iColumnCount = sCells.length;
        for (int iLine = 1; iLine < vLines.size(); iLine++) {
            sCells = vLines.elementAt(iLine).split(";");
            vRow = new Vector<String>();
            for (int i = 0; i < sCells.length; i++) {
                vRow.add(sCells[i]);
            }
            while (vRow.size() < iColumnCount) {
                vRow.add("");
            }
            mt.addRow(vRow);
        }
        return (mt);
    }

    public static MemoryTable createFieldMap() {
//        String[] sColumnNames = {"map_fieldname", "map_columnname", "map_datatype", "map_key"};
//        MemoryTable mt = new MemoryTable(sColumnNames);
//        return (mt);
        return (createMemoryTable("map_fieldname", "map_columnname", "map_datatype", "map_key"));
    }

    public static MemoryTable createFixLengthRecordMap() {
        return (createMemoryTable("nr", "name", "from", "to", "length", "decimal", "format", "alignment", "fix"));
    }

    public static MemoryTable createMemoryTable(HashMap<String, Object> hm) {
        Vector<String> vColumnNames = new Vector<>();
        Vector<Vector<Object>> vData = new Vector<>();
        Vector<Object> vRow = new Vector<>();
        //@todo vektorok feltöltése
        String[] sNamesArray = hm.keySet().toArray(new String[0]);
        for (int i = 0; i < sNamesArray.length; i++) {
            vColumnNames.add(sNamesArray[i]);
            vRow.add(hm.get(sNamesArray[i]));
        }
        vData.add(vRow);
        return (new MemoryTable(vData, vColumnNames));
    }

    public static MemoryTable createMemoryTable(String... sFieldNames) {
        Vector<String> vColumnNames = new Vector<>();
        for (int i = 0; i < sFieldNames.length; i++) {
            vColumnNames.add(sFieldNames[i]);
        }
        return (new MemoryTable(vColumnNames));
    }

    public static MemoryTable createEmptyTable() {
        Vector<String> vColumnNames = new Vector<>();
        return (new MemoryTable(vColumnNames));
    }

    private void setStatementParameter(PreparedStatement ps, String sDataType, int iParameterIndex, int iRow, String sColumnName) throws SQLException {
        switch (sDataType) {
            case "string":
                ps.setString(iParameterIndex, this.getStringValueAt(iRow, sColumnName));
                break;
            case "decimal":
                ps.setBigDecimal(iParameterIndex, this.getBigDecimalValueAt(iRow, sColumnName));
                break;
            case "int":
            case "integer":
                ps.setInt(iParameterIndex, this.getIntValueAt(iRow, sColumnName));
                break;
            case "autodate":
                String sValue = this.getStringValueAt(iRow, sColumnName);
                String sPossibleFormat = DateTimeUtils.getPossibleFormat(sValue);
                if (sPossibleFormat.length() > 0) {
                    SimpleDateFormat sdf = new SimpleDateFormat(sPossibleFormat);
                    ps.setTimestamp(iParameterIndex, new java.sql.Timestamp(this.getUtilDateValueAt(sdf, iRow, sColumnName).getTime()));
                } else {
                    ps.setString(iParameterIndex, this.getStringValueAt(iRow, sColumnName));
                }
                break;
//            case "date":
//                ps.setDate(iParameterIndex, new java.sql.Date(this.getUtilDateValueAt(sdf1, iRow, sColumnName).getTime()));
//                break;
//            case "datetime":
//                //System.out.println(this.getUtilDateValueAt(sdf, iRow, sColumnName).toString());
//                ps.setTimestamp(iParameterIndex, new java.sql.Timestamp(this.getUtilDateValueAt(sdf2, iRow, sColumnName).getTime()));
//                break;
            default:
                ps.setString(iParameterIndex, this.getStringValueAt(iRow, sColumnName));
        }
    }

    public HashMap<String, Integer> insertIntoMSSQLTable(AppInterface appInterface, Connection connection, String sTableName, boolean bCheckOnly, boolean bUpdate, MemoryTable mtMap) {
        HashMap<String, Integer> hm = new HashMap<>();
        int iExistCount = 0;
        int iInsertCount = 0;
        int iUpdateCount = 0;
        boolean bAllFieldsAreKey = true;
        for (int iRow = 0; iRow < mtMap.getRowCount() && bAllFieldsAreKey; iRow++) {
            if (!BooleanUtils.convertToBoolean(mtMap.getValueAt(iRow, "map_key"))) {
                bAllFieldsAreKey = false;
            }
        }

        boolean bKeyFieldExists = false;
        for (int iRow = 0; iRow < mtMap.getRowCount() && !bKeyFieldExists; iRow++) {
            if (BooleanUtils.convertToBoolean(mtMap.getValueAt(iRow, "map_key"))) {
                bKeyFieldExists = true;
            }
        }

        String sInsert = "insert into " + sTableName + "(";
        for (int iRow = 0; iRow < mtMap.getRowCount(); iRow++) {
            sInsert += (iRow > 0 ? ", " : "") + mtMap.getValueAt(iRow, "map_fieldname");
        }
        sInsert += ") values (";
        for (int iRow = 0; iRow < mtMap.getRowCount(); iRow++) {
            sInsert += (iRow > 0 ? ", " : "") + "?";
        }
        sInsert += ")";

        String sSelect = "select 1 from " + sTableName + " where ";
        for (int iRow = 0; iRow < mtMap.getRowCount(); iRow++) {
            if (BooleanUtils.convertToBoolean(mtMap.getValueAt(iRow, "map_key"))) {
                sSelect += (sSelect.trim().endsWith("where") ? "" : " and ") + mtMap.getValueAt(iRow, "map_fieldname") + "=?";
            }
        }

        String sUpdate = "update " + sTableName + " set ";
        for (int iRow = 0; iRow < mtMap.getRowCount(); iRow++) {
            //sUpdate += (iRow > 0 ? ", " : "") + mtMap.getValueAt(iRow, "map_fieldname") + "=?";
            if (!BooleanUtils.convertToBoolean(mtMap.getValueAt(iRow, "map_key"))) {
                sUpdate += (sUpdate.trim().endsWith("set") ? "" : ", ") + mtMap.getValueAt(iRow, "map_fieldname") + "=?";
            }
        }
        sUpdate += " where ";
        for (int iRow = 0; iRow < mtMap.getRowCount(); iRow++) {
            if (BooleanUtils.convertToBoolean(mtMap.getValueAt(iRow, "map_key"))) {
                sUpdate += (sUpdate.trim().endsWith("where") ? "" : " and ") + mtMap.getValueAt(iRow, "map_fieldname") + "=?";
            }
        }

//        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
//        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        PreparedStatement psSelect = null;
        PreparedStatement psInsert = null;
        PreparedStatement psUpdate = null;
        ResultSet rs = null;
        boolean bExists = false;
        try {
            psSelect = connection.prepareStatement(sSelect);
            psInsert = connection.prepareStatement(sInsert);
            psUpdate = connection.prepareStatement(sUpdate);

            for (int iRow = 0; iRow < this.getRowCount(); iRow++) {

                bExists = false;
                //check existence if any key field exists
                if (bKeyFieldExists) {
                    //psSelect = connection.prepareStatement(sSelect);

                    int i = 0;
                    //int iRow = 1; //@todo: it would be a loop!!!
                    for (int iMapRow = 0; iMapRow < mtMap.getRowCount(); iMapRow++) {
                        if (BooleanUtils.convertToBoolean(mtMap.getValueAt(iMapRow, "map_key"))) {
                            setStatementParameter(psSelect, mtMap.getStringValueAt(iMapRow, "map_datatype"), ++i, iRow, mtMap.getStringValueAt(iMapRow, "map_columnname"));
                        }
                    }
                    rs = psSelect.executeQuery();
                    bExists = rs.next();
                    appInterface.logLine(bExists ? "exists" : "not exists", LoggerInterface.LOG_DEBUG);
                    iExistCount += (bExists ? 1 : 0);
                }
                if (!bCheckOnly) {
                    //insert when not exists (or can not check, because there is no key field...)
                    if (!bExists) {
                        //insert
                        //psInsert = connection.prepareStatement(sInsert);
                        int i = 0;
                        //int iRow = 1; //@todo: it would be a loop!!!
                        for (int iMapRow = 0; iMapRow < mtMap.getRowCount(); iMapRow++) {
                            setStatementParameter(psInsert, mtMap.getStringValueAt(iMapRow, "map_datatype"), ++i, iRow, mtMap.getStringValueAt(iMapRow, "map_columnname"));
                        }
                        int iCount = psInsert.executeUpdate();
                        iInsertCount += iCount;
                        appInterface.logLine(Integer.toString(iCount) + " record inserted", LoggerInterface.LOG_DEBUG);
                    } else {
                        if (bUpdate) {
                            //update when not only key fields are there
                            if (!bAllFieldsAreKey) {
                                //psUpdate = connection.prepareStatement(sUpdate);
                                int i = 0;
                                //int iRow = 1; //@todo: it would be a loop!!!
                                for (int iMapRow = 0; iMapRow < mtMap.getRowCount(); iMapRow++) {
                                    if (!BooleanUtils.convertToBoolean(mtMap.getValueAt(iMapRow, "map_key"))) {
                                        setStatementParameter(psUpdate, mtMap.getStringValueAt(iMapRow, "map_datatype"), ++i, iRow, mtMap.getStringValueAt(iMapRow, "map_columnname"));
                                    }
                                }
                                for (int iMapRow = 0; iMapRow < mtMap.getRowCount(); iMapRow++) {
                                    if (BooleanUtils.convertToBoolean(mtMap.getValueAt(iMapRow, "map_key"))) {
                                        setStatementParameter(psUpdate, mtMap.getStringValueAt(iMapRow, "map_datatype"), ++i, iRow, mtMap.getStringValueAt(iMapRow, "map_columnname"));
                                    }
                                }
                                int iCount = psUpdate.executeUpdate();
                                iUpdateCount += iCount;
                                appInterface.logLine(Integer.toString(iCount) + " record updated", LoggerInterface.LOG_DEBUG);
                            }
                        }
                    }
                }
            }
        } catch (SQLException sqle) {
            appInterface.handleError(sqle);
        }

        //debug:
        appInterface.logLine(sInsert, LoggerInterface.LOG_DEBUG);
        if (bKeyFieldExists) {
            appInterface.logLine(sSelect, LoggerInterface.LOG_DEBUG);
        }
        if (bKeyFieldExists && !bAllFieldsAreKey) {
            appInterface.logLine(sUpdate, LoggerInterface.LOG_DEBUG);
        }
        hm.put("exist", iExistCount);
        hm.put("insert", iInsertCount);
        hm.put("update", iUpdateCount);
        return (hm);
    }

    public void writeIntoMSSQLTable(Connection connection, String sTableName, ErrorHandlerInterface ehi) {
        Vector<Integer> vColumnWidth = new Vector<Integer>();
        int iWidth = 0;
        for (int iColumn = 0; iColumn < getColumnCount(); iColumn++) {
            vColumnWidth.add(0);
            for (int iRow = 0; iRow < getRowCount(); iRow++) {
                iWidth = StringUtils.isNull(getValueAt(iRow, iColumn), "").length();
                if (iWidth > vColumnWidth.elementAt(iColumn)) {
                    vColumnWidth.setElementAt(iWidth, iColumn);
                }
            }
        }
        String sCreate = "create table " + sTableName + "(";
        for (int iColumn = 0; iColumn < getColumnCount(); iColumn++) {
            if (iColumn > 0) {
                sCreate += ", ";
            }
            sCreate += getColumnNames().elementAt(iColumn).toString().toLowerCase() + " varchar(" + Integer.toString(vColumnWidth.elementAt(iColumn) + 1) + ") null";
        }
        sCreate += ")";
        String sInsert = "insert into " + sTableName + "(";
        for (int iColumn = 0; iColumn < getColumnCount(); iColumn++) {
            if (iColumn > 0) {
                sInsert += ", ";
            }
            sInsert += getColumnNames().elementAt(iColumn).toString().toLowerCase();
        }
        sInsert += ") values (";
        for (int iColumn = 0; iColumn < getColumnCount(); iColumn++) {
            if (iColumn > 0) {
                sInsert += ", ";
            }
            sInsert += "?";
        }
        sInsert += ")";

        try {
            connection.createStatement().execute(sCreate);
            PreparedStatement ps = connection.prepareStatement(sInsert);
            for (int iRow = 0; iRow < getRowCount(); iRow++) {
                for (int iColumn = 0; iColumn < getColumnCount(); iColumn++) {
                    ps.setString(iColumn + 1, StringUtils.isNull(getValueAt(iRow, iColumn), ""));
                }
                ps.executeUpdate();
            }
        } catch (SQLException sqle) {
            ehi.handleError(sqle);
        }
    }

    public static MemoryTable loadFromStringVector(Vector<String> vLines) {
        return (loadFromStringVector(vLines, ""));
    }

    public static MemoryTable loadFromStringVector(Vector<String> vLines, String sColumnName) {
        MemoryTable mt = null;
        Vector vColumnNames = new Vector();
        vColumnNames.add(sColumnName);
        Vector vData = new Vector();
        Vector vRow = null;
        for (int i = 0; i < vLines.size(); i++) {
            vRow = new Vector();
            vRow.add(vLines.elementAt(i));
            vData.add(vRow);
        }
        mt = new MemoryTable(vData, vColumnNames);
        vLines = null;
        vColumnNames = null;
        return (mt);
    }

    public static MemoryTable loadFromSQLQuery(AppInterface appInterface, String sConnectionName, String sSQL) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Vector vData = new Vector();
        Vector vRow = null;
        Vector vColumnNames = null;
        MemoryTable mt = null;
        vColumnNames = new Vector();

        try {
            conn = appInterface.getTemporaryConnection(sConnectionName);
            //ps = appInterface.getConnection(sConnectionName).prepareStatement(sSQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps = conn.prepareStatement(sSQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = ps.executeQuery();
            for (int iColumn = 0; iColumn < rs.getMetaData().getColumnCount(); iColumn++) {
                vColumnNames.add(rs.getMetaData().getColumnName(iColumn + 1));
            }
            while (rs.next()) {
                vRow = new Vector();
                for (int iColumn = 0; iColumn < rs.getMetaData().getColumnCount(); iColumn++) {
                    vRow.add(rs.getObject(iColumn + 1));
                }
                vData.add(vRow);
            }
            rs.close();
            rs = null;
            ps.close();
            ps = null;
            conn.close();
            conn = null;
        } catch (SQLException sqle) {
            appInterface.handleError(sqle, sSQL);
        }
        mt = new MemoryTable(vData, vColumnNames);
        vData = null;
        vColumnNames = null;
        return (mt);
    }

    public static Vector<MemoryTable> loadMultiFromSQLQuery(AppInterface appInterface, String sConnectionName, String sSQL) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Vector<MemoryTable> vMt = new Vector<>();

        try {
            conn = appInterface.getTemporaryConnection(sConnectionName);
            ps = conn.prepareStatement(sSQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = ps.executeQuery();
            MemoryTable mt = loadFromResultSet(appInterface, rs);
            vMt.add(mt);
            while (ps.getMoreResults()) {
                rs = ps.getResultSet();
                mt = loadFromResultSet(appInterface, rs);
                vMt.add(mt);
            }
            ps.close();
            ps = null;
            conn.close();
            conn = null;
        } catch (SQLException sqle) {
            appInterface.handleError(sqle, sSQL);
        }
        return (vMt);
    }

    public static MemoryTable loadFromResultSet(ErrorHandlerInterface errorHandlerInterface, ResultSet rs) {
        MemoryTable mt = null;
        Vector vColumnNames = new Vector();
        Vector vData = null;
        Vector vRow = null;
        try {
            for (int iColumn = 0; iColumn < rs.getMetaData().getColumnCount(); iColumn++) {
                vColumnNames.add(rs.getMetaData().getColumnName(iColumn + 1));
            }
            //rs.beforeFirst();
            vData = new Vector();
            while (rs.next()) {
                vRow = new Vector();
                for (int iColumn = 0; iColumn < rs.getMetaData().getColumnCount(); iColumn++) {
                    vRow.add(rs.getObject(iColumn + 1));
                }
                vData.add(vRow);
            }
        } catch (SQLException sqle) {
            errorHandlerInterface.handleError(sqle);
        }
        mt = new MemoryTable(vData, vColumnNames);
        return (mt);
    }

    public boolean addMemoryTable(MemoryTable mt) {
        Vector v1 = this.getColumnNames();
        Vector v2 = mt.getColumnNames();
        if (v1.size() != v2.size()) {
            return (false);
        }
        for (int i = 0; i < v1.size(); i++) {
            if (!v1.elementAt(i).toString().equalsIgnoreCase(v2.elementAt(i).toString())) {
                return (false);
            }
        }
        for (int iRow = 0; iRow < mt.getRowCount(); iRow++) {
            this.addRow(mt.getRow(iRow));
        }
        return (true);
    }

//    public MemoryTable copyMemoryTable() {
//        return (new MemoryTable(this.getDataVector(), this.getColumnNames()));
//    }
    public MemoryTable copyMemoryTable() {
        Vector vColumnNames = new Vector();
        for (int iCol = 0; iCol < this.getColumnNames().size(); iCol++) {
            vColumnNames.add(this.getColumnNames().elementAt(iCol));
        }
        Vector vData = new Vector();
        Vector vRow;
        for (int iRow = 0; iRow < this.getDataVector().size(); iRow++) {
            Vector v = (Vector) this.getDataVector().elementAt(iRow);
            vRow = new Vector();
            for (int iCol = 0; iCol < v.size(); iCol++) {
                vRow.add(v.elementAt(iCol));
            }
            vData.add(vRow);
        }
        return (new MemoryTable(vData, vColumnNames));
        //return (new MemoryTable(this.getDataVector(), this.getColumnNames()));
    }

    public static MemoryTable copyFromMagTable(MagTable magTable) {
        MemoryTable mt = new MemoryTable(magTable.getColumnNames());
        for (int iRow = 0; iRow < magTable.getRowCount(); iRow++) {
            mt.addRow(magTable.getRow(iRow));
        }
        return (mt);
    }

    public static Object getValueFromSQL(AppInterface appInterface, String sConnectionName, String sSQL, String sColumnName) {
        return (MemoryTable.loadFromSQLQuery(appInterface, sConnectionName, sSQL).getValueAt(0, sColumnName));
    }

    public static String getStringValueFromSQL(AppInterface appInterface, String sConnectionName, String sSQL, String sColumnName) {
        return (MemoryTable.loadFromSQLQuery(appInterface, sConnectionName, sSQL).getStringValueAt(0, sColumnName));
    }

    public static boolean dataExists(AppInterface appInterface, String sConnectionName, String sSQL) {
        return (MemoryTable.loadFromSQLQuery(appInterface, sConnectionName, sSQL).getRowCount() > 0);
    }

    public int[] getColumnIndexes(String[] sColumnNames) {
        int[] iColumnIndexes = new int[sColumnNames.length];
        for (int i = 0; i < sColumnNames.length; i++) {
            iColumnIndexes[i] = this.findColumn(sColumnNames[i]);
        }
        return (iColumnIndexes);
    }

    public MemoryTable selectSumGroupBy(String[] sSumColumnnames, String[] sGroupByColumnnames) {
        String[] sOutputColumnNames = {};

        //produce output column names
        String[] sColumnNames = StringUtils.convertVectorToArray(this.getColumnNames());
        for (int i = 0; i < sSumColumnnames.length; i++) {
            if (!StringUtils.existsInArray(sOutputColumnNames, sSumColumnnames[i], true)) {
                if (StringUtils.existsInArray(sColumnNames, sSumColumnnames[i], false)) {
                    sOutputColumnNames = StringUtils.addElementsToArray(sOutputColumnNames, sSumColumnnames[i]);
                }
            }
        }
        for (int i = 0; i < sGroupByColumnnames.length; i++) {
            if (!StringUtils.existsInArray(sOutputColumnNames, sGroupByColumnnames[i], true)) {
                if (StringUtils.existsInArray(sColumnNames, sGroupByColumnnames[i], false)) {
                    sOutputColumnNames = StringUtils.addElementsToArray(sOutputColumnNames, sGroupByColumnnames[i]);
                }
            }
        }

        //sum column can't be a group column
        sSumColumnnames = StringUtils.subtractElementsFromArray(sSumColumnnames, sGroupByColumnnames);

        //select output columns
        int[] iOutputColumnIndexes = this.getColumnIndexes(sOutputColumnNames);
        MemoryTable mt1 = new MemoryTable(sOutputColumnNames);
        for (int iRow = 0; iRow < this.getRowCount(); iRow++) {
            mt1.addRow(this.getSubRow(iRow, iOutputColumnIndexes));
        }

        //collect distinct group by values
        int[] iGroupByColumnIndexes = this.getColumnIndexes(sGroupByColumnnames);
        MemoryTable mt2 = new MemoryTable(sOutputColumnNames);
        for (int iRow = 0; iRow < mt1.getRowCount(); iRow++) {
            //for (int iRow = 0; iRow < mt1.getRowCount() && iRow < 2; iRow++) {
            if (!mt2.isRowExists(this.getSubRow(iRow, iGroupByColumnIndexes), sGroupByColumnnames)) {
                mt2.addRow(this.getSubRow(iRow, iOutputColumnIndexes));
            }
        }

        //technically set null of non-group by columns
        for (int iRow = 0; iRow < mt2.getRowCount(); iRow++) {
            for (int iCol = 0; iCol < mt2.getColumnCount(); iCol++) {
                if (!StringUtils.existsInArray(sGroupByColumnnames, mt2.getColumnName(iCol), false)) {
                    mt2.setValueAt(null, iRow, iCol);
                }
            }
        }

        //sum columns
        BigDecimal bd1;
        BigDecimal bd2;
        int[] iSumByColumnIndexes = this.getColumnIndexes(sSumColumnnames);
        int[] iSumByColumnIndexesInOutput = mt2.getColumnIndexes(sSumColumnnames);
        for (int iRow = 0; iRow < mt1.getRowCount(); iRow++) {
            //for (int iRow = 0; iRow < mt1.getRowCount() && iRow < 2; iRow++) {
            int iRow2 = mt2.findRow(this.getSubRow(iRow, iGroupByColumnIndexes), sGroupByColumnnames);
            //System.out.println(iRow2*1111);
            if (iRow2 > -1) {
                for (int iCol = 0; iCol < sSumColumnnames.length; iCol++) {
                    //System.out.println(this.getValueAt(iRow, iSumByColumnIndexes[iCol]).toString());
                    //System.out.println(this.getValueAt(iRow, iSumByColumnIndexes[iCol]).getClass().getName());
                    if (this.getValueAt(iRow, iSumByColumnIndexes[iCol]) == null) {
                        bd1 = BigDecimal.ZERO;
                    } else {
                        bd1 = BigDecimalUtils.convertToBigDecimal(this.getValueAt(iRow, iSumByColumnIndexes[iCol]));
                    }
                    //System.out.println(bd1.toString());
                    if (mt2.getValueAt(iRow2, iSumByColumnIndexesInOutput[iCol]) == null) {
                        bd2 = BigDecimal.ZERO;
                    } else {
                        bd2 = BigDecimalUtils.convertToBigDecimal(mt2.getValueAt(iRow2, iSumByColumnIndexesInOutput[iCol]));
                    }
                    //System.out.println(bd2.toString());
                    mt2.setValueAt(bd2.add(bd1), iRow2, iSumByColumnIndexesInOutput[iCol]);
                }

            }
        }

        return (mt2);
    }

    public int findRow(Object oValue, String sColumnName) {
        Vector v = new Vector();
        v.add(oValue);
        String s[] = new String[1];
        s[0] = sColumnName;
        return (findRow(v, s));
    }

    public int findRow(Vector vValues, String[] sColumnNames) {
        int[] iColumnIndexes = this.getColumnIndexes(sColumnNames);
        boolean bEquals;
        for (int iRow = 0; iRow < this.getRowCount(); iRow++) {
            bEquals = true;
            for (int iCol = 0; iCol < iColumnIndexes.length; iCol++) {
                //if (this.getValueAt(iRow, iColumnIndexes[iCol]) != null) { //MaG 2018.09.13.
                if (!this.getValueAt(iRow, iColumnIndexes[iCol]).toString().equals(vValues.elementAt(iCol).toString())) {
                    bEquals = false;
                }
                //}
            }
            if (bEquals) {
                return (iRow);
            }
        }
        return (-1);
    }

    public boolean isRowExists(Vector vValues, String[] sColumnNames) {
        return (findRow(vValues, sColumnNames) > -1);
    }

    public boolean hasRow() {
        return (getRowCount() > 0);
    }

    public void setName(String sName) {
        this.sName = sName;
    }

    public String getName() {
        return (sName);
    }
}
