package hu.mgx.sql;

import java.io.*;
import java.sql.*;
import java.text.*;

import hu.mgx.db.*;

import org.apache.poi.hssf.usermodel.*;

public class SQLExcel
{

    private CONN conn;
    private String sSQL;
    private ResultSet resultSet;
    private ResultSetMetaData resultSetMetaData;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
    private SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public SQLExcel(CONN conn, String sSQL)
    {
        this.conn = conn;
        conn.connect();
        this.sSQL = sSQL;
    }

    public boolean convertSQLToExcel()
    {
        try
        {
            resultSet = conn.getConnection().prepareStatement(sSQL).executeQuery();
            resultSetMetaData = resultSet.getMetaData();
        }
        catch (SQLException sqle)
        {
            System.err.println(sqle.getLocalizedMessage());
            sqle.printStackTrace(System.err);
            return (false);
        }
        return (true);
    }

    private HSSFWorkbook createExcel(ResultSet resultSet)
    {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet("Excel");
        HSSFRow hssfRow;
        HSSFCell cell;
        try
        {
            resultSetMetaData = resultSet.getMetaData();
        }
        catch (SQLException sqle)
        {
            System.err.println(sqle.getLocalizedMessage());
            sqle.printStackTrace(System.err);
            return (hssfWorkbook);
        }
        double d;
        java.util.Date utilDate;
        String sValue = "";
        int iRow = 0;
        short shColumn;
        hssfRow = hssfSheet.createRow(iRow);
        try
        {
            for (shColumn = 0; shColumn < resultSetMetaData.getColumnCount(); shColumn++)
            {
                cell = hssfRow.createCell(shColumn);
                //cell.setEncoding(HSSFCell.ENCODING_UTF_16);
                cell.setCellValue(resultSetMetaData.getColumnName(shColumn + 1));
                hssfSheet.setColumnWidth(shColumn, (short) 4000);
            }
            while (resultSet.next())
            {
                ++iRow;
                hssfRow = hssfSheet.createRow(iRow);
                for (shColumn = 0; shColumn < resultSetMetaData.getColumnCount(); shColumn++)
                {
                    cell = hssfRow.createCell(shColumn);
                    Object o = resultSet.getObject(shColumn + 1);
                    sValue = (o == null ? "" : o.toString());
                    //System.err.println(sValue);
                    //cell.setEncoding(HSSFCell.ENCODING_UTF_16);
                    //System.err.println(resultSetMetaData.getColumnType(shColumn + 1));
                    if (resultSetMetaData.getColumnType(shColumn + 1) == java.sql.Types.CHAR || resultSetMetaData.getColumnType(shColumn + 1) == java.sql.Types.VARCHAR)
                    {
                        cell.setCellValue(sValue);
                    //cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//                        try {
//                            d = Double.parseDouble(sValue);
//                            cell.setCellValue(d);
//                        }
//                        catch (NumberFormatException e) {
//                            cell.setCellValue(sValue);
//                        }
                    }
                    else if (resultSetMetaData.getColumnType(shColumn + 1) == java.sql.Types.DECIMAL || resultSetMetaData.getColumnType(shColumn + 1) == java.sql.Types.INTEGER)
                    {
                        if (sValue.equals(""))
                        {
                            cell.setCellValue("");
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
                            cell.setCellValue(d);
                        }
                    }
                    else if (resultSetMetaData.getColumnType(shColumn + 1) == java.sql.Types.DATE)
                    {
                        if (o == null)
                        {
                            cell.setCellValue("");
                        }
                        else
                        {
                            sValue = simpleDateFormat.format(o);
                            cell.setCellValue(sValue);
                        }
                    }
                    else if (resultSetMetaData.getColumnType(shColumn + 1) == java.sql.Types.TIMESTAMP)
                    {
                        if (o == null)
                        {
                            cell.setCellValue("");
                        }
                        else
                        {
                            sValue = simpleDateTimeFormat.format(o);
                            cell.setCellValue(sValue);
                        }
                    }
                }
            }
        }
        catch (SQLException sqle)
        {
            System.err.println(sqle.getLocalizedMessage());
            sqle.printStackTrace(System.err);
            return (hssfWorkbook);
        }
        return (hssfWorkbook);
    }

    public boolean export(File f)
    {
        try
        {
            PreparedStatement preparedStatement = conn.getConnection().prepareStatement(sSQL);
            resultSet = preparedStatement.executeQuery();
            resultSetMetaData = resultSet.getMetaData();
        }
        catch (SQLException sqle)
        {
            System.err.println(sqle.getLocalizedMessage());
            sqle.printStackTrace(System.err);
            return (false);
        }
        FileOutputStream fileOut;
        HSSFWorkbook wb = createExcel(resultSet);
        try
        {
            fileOut = new FileOutputStream(f);
        }
        catch (FileNotFoundException e)
        {
            //AppUtil.handleError(appErrorHandler, e, null);
            return (false);
        }
        try
        {
            wb.write(fileOut);
            fileOut.close();
        }
        catch (IOException e)
        {
            //AppUtil.handleError(appErrorHandler, e, null);
            return (false);
        }
        return (true);
    }
}
