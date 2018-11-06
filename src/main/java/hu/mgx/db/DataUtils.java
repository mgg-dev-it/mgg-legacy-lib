package hu.mgx.db;

import java.sql.*;
import java.text.*;

import javax.swing.*;

import hu.mgx.app.common.*;
import hu.mgx.lang.*;
import hu.mgx.swing.*;
import hu.mgx.swing.table.*;
import hu.mgx.util.*;

public abstract class DataUtils
{

    public static Object getValueFromRS(ResultSet rs, String sColumnName, FieldDefinition fieldDefinition)
    {
        String sTmp = "";
        int iTmp = 0;
        java.sql.Date date;
        java.sql.Timestamp timestamp;
        java.util.Date udate;
        byte[] b;
        try
        {
            if (fieldDefinition.getType() == hu.mgx.db.FieldType.DATE)
            {
                date = rs.getDate(sColumnName);
                if (!rs.wasNull())
                {
                    udate = new java.util.Date(date.getTime());
                    return (udate);
                }
                else
                {
                    return (null);
                }
            }
            else if (fieldDefinition.getType() == hu.mgx.db.FieldType.DATETIME)
            {
                timestamp = rs.getTimestamp(sColumnName);
                if (!rs.wasNull())
                {
                    udate = new java.util.Date(timestamp.getTime());
                    return (udate);
                }
                else
                {
                    return (null);
                }
            }
            else if (fieldDefinition.getType() == hu.mgx.db.FieldType.INT)
            {
                // 2006-10-08
                //return(new Integer(rs.getInt(sColumnName)));
                iTmp = rs.getInt(sColumnName);
                if (rs.wasNull())
                {
                    return (null);
                }
                else
                {
                    return (new Integer(rs.getInt(sColumnName)));
                }
            }
            else if (fieldDefinition.getType() == hu.mgx.db.FieldType.BIT)
            {
                return (new Integer(rs.getInt(sColumnName)));
            }
            else if (fieldDefinition.getType() == hu.mgx.db.FieldType.DECIMAL)
            {
                return (new Double(rs.getDouble(sColumnName)));
            }
            else if (fieldDefinition.getType() == hu.mgx.db.FieldType.MEDIUMBLOB)
            {
                b = rs.getBytes(sColumnName);
                if (!rs.wasNull())
                {
                    return (new ByteArray(b));
                }
                else
                {
                    return (null);
                }
            }
            else
            {
                sTmp = rs.getString(sColumnName);
                return (sTmp);
            }
        }
        catch (SQLException e)
        {
            System.err.println(e.getLocalizedMessage());
            e.printStackTrace(System.err);
            return (null);
        }
    }

    public static void setTableColumn(JTable jTable, int iColumn, FieldDefinition fieldDefinition, FormatInterface mgxFormat)
    {
        if ((fieldDefinition.getType() == hu.mgx.db.FieldType.INT) && (!fieldDefinition.isLookup()))
        {
            jTable.getColumnModel().getColumn(iColumn).setMinWidth(50);
            jTable.getColumnModel().getColumn(iColumn).setPreferredWidth(50);
            jTable.getColumnModel().getColumn(iColumn).setMaxWidth(100);
        }
        if (!fieldDefinition.isVisible() || fieldDefinition.isPW())
        {
            jTable.getColumnModel().getColumn(iColumn).setMinWidth(0);
            jTable.getColumnModel().getColumn(iColumn).setPreferredWidth(0);
            jTable.getColumnModel().getColumn(iColumn).setMaxWidth(0);
        }
        if (fieldDefinition.isModifier())
        {
            jTable.getColumnModel().getColumn(iColumn).setMinWidth(80);
            jTable.getColumnModel().getColumn(iColumn).setPreferredWidth(80);
            jTable.getColumnModel().getColumn(iColumn).setMaxWidth(80);
        }
        if (fieldDefinition.isModificationTime())
        {
            jTable.getColumnModel().getColumn(iColumn).setMinWidth(120);
            jTable.getColumnModel().getColumn(iColumn).setPreferredWidth(120);
            jTable.getColumnModel().getColumn(iColumn).setMaxWidth(120);
        }
        if (fieldDefinition.isID())
        {
            jTable.getColumnModel().getColumn(iColumn).setMinWidth(0);
            jTable.getColumnModel().getColumn(iColumn).setPreferredWidth(0);
            jTable.getColumnModel().getColumn(iColumn).setMaxWidth(0);
        }
        if (fieldDefinition.isSerial() || fieldDefinition.isListOrder())
        {
            jTable.getColumnModel().getColumn(iColumn).setMinWidth(75);
            jTable.getColumnModel().getColumn(iColumn).setPreferredWidth(75);
            jTable.getColumnModel().getColumn(iColumn).setMaxWidth(75);
        }
        if (fieldDefinition.getType() == hu.mgx.db.FieldType.BIT)
        {
            jTable.getColumnModel().getColumn(iColumn).setMinWidth(50);
            jTable.getColumnModel().getColumn(iColumn).setPreferredWidth(50);
            jTable.getColumnModel().getColumn(iColumn).setMaxWidth(100);
        }
        if (fieldDefinition.getType() == hu.mgx.db.FieldType.DATE)
        {
            jTable.getColumnModel().getColumn(iColumn).setMinWidth(80);
            jTable.getColumnModel().getColumn(iColumn).setPreferredWidth(80);
            jTable.getColumnModel().getColumn(iColumn).setMaxWidth(80);
        }
        if (fieldDefinition.getType() == hu.mgx.db.FieldType.MEDIUMBLOB)
        {
            jTable.getColumnModel().getColumn(iColumn).setMinWidth(0);
            jTable.getColumnModel().getColumn(iColumn).setPreferredWidth(0);
            jTable.getColumnModel().getColumn(iColumn).setMaxWidth(0);
        }
        //        if (fieldDefinition.getPreferredWidth() > -1) {
        //            jTable.getColumnModel().getColumn(iColumn).setMinWidth(fieldDefinition.getPreferredWidth());
        //            jTable.getColumnModel().getColumn(iColumn).setPreferredWidth(fieldDefinition.getPreferredWidth());
        //            jTable.getColumnModel().getColumn(iColumn).setMaxWidth(fieldDefinition.getPreferredWidth());
        //        }
        if (fieldDefinition.isLookup())
        {
            jTable.getColumnModel().getColumn(iColumn).setCellRenderer(new LookupRenderer(jTable.getSelectionBackground(), fieldDefinition, fieldDefinition.getLookup(), mgxFormat));
        }
        else if (fieldDefinition.getType() == hu.mgx.db.FieldType.INT)
        {
            jTable.getColumnModel().getColumn(iColumn).setCellRenderer(new DecimalRenderer(jTable.getSelectionBackground(), 0, mgxFormat));
        }
        else if (fieldDefinition.getType() == hu.mgx.db.FieldType.BIT)
        {
            jTable.getColumnModel().getColumn(iColumn).setCellRenderer(new BitRenderer(jTable.getSelectionBackground()));
        }
        else if (fieldDefinition.getType() == hu.mgx.db.FieldType.DECIMAL)
        {
            jTable.getColumnModel().getColumn(iColumn).setCellRenderer(new DecimalRenderer(jTable.getSelectionBackground(), fieldDefinition.getScale(), mgxFormat));
        }
        else if (fieldDefinition.getType() == hu.mgx.db.FieldType.DATE)
        {
//            if (fieldDefinition.isCallDate())
//            {
//                jTable.getColumnModel().getColumn(iColumn).setCellRenderer(new DateRendererRed(jTable.getSelectionBackground(), mgxFormat));
//            }
//            else if (fieldDefinition.isServiceDate())
//            {
//                jTable.getColumnModel().getColumn(iColumn).setCellRenderer(new DateRendererService(jTable.getSelectionBackground(), mgxFormat));
//            }
//            else
//            {
            jTable.getColumnModel().getColumn(iColumn).setCellRenderer(new DateRenderer(jTable.getSelectionBackground(), mgxFormat));
//            }
        }
        else if (fieldDefinition.getType() == hu.mgx.db.FieldType.DATETIME)
        {
            jTable.getColumnModel().getColumn(iColumn).setCellRenderer(new DateTimeRenderer(jTable.getSelectionBackground(), mgxFormat));
        }
        else if ((fieldDefinition.getType() == hu.mgx.db.FieldType.CHAR) && (fieldDefinition.isText()))
        {
            jTable.getColumnModel().getColumn(iColumn).setCellRenderer(new TextRenderer(jTable.getSelectionBackground()));
        }
        if (fieldDefinition.getPreferredWidth() > -1)
        {
            jTable.getColumnModel().getColumn(iColumn).setMinWidth(fieldDefinition.getPreferredWidth());
            jTable.getColumnModel().getColumn(iColumn).setPreferredWidth(fieldDefinition.getPreferredWidth());
            jTable.getColumnModel().getColumn(iColumn).setMaxWidth(fieldDefinition.getPreferredWidth());
        }
    }

    public static void setDefault(DataField dataField, FieldDefinition fieldDefinition, hu.mgx.sql.SQL sql, int iUserId, FormatInterface mgxFormat)
    {
        String sDefaultValue;
        String sSql;
        ResultSet rs = null;

        dataField.setValue(null);
        try
        {
            sDefaultValue = fieldDefinition.getDefaultValue();
            if (!sDefaultValue.equals(""))
            {
                if (fieldDefinition.getType() == hu.mgx.db.FieldType.INT)
                {
                    try
                    {
                        dataField.setValue(new Integer(Integer.parseInt(sDefaultValue)));
                    }
                    catch (NumberFormatException nfe)
                    {
                    }
                }
                if (fieldDefinition.getType() == hu.mgx.db.FieldType.DATE)
                {
                    try
                    {
                        dataField.setValue(mgxFormat.getDateFormat().parse(sDefaultValue));
                    }
                    catch (ParseException pe)
                    {
                        System.err.println(pe.getLocalizedMessage());
                    }
                }
                if (fieldDefinition.getType() == hu.mgx.db.FieldType.DATETIME)
                {
                    //@todo kidolgozni, a többi alap mezõtípusra is!!!
                }
                if (fieldDefinition.getType() == hu.mgx.db.FieldType.BIT)
                {
                    dataField.setValue(sDefaultValue);
                }
            }
            sSql = fieldDefinition.getSQLForDefaultValue();
            if (!sSql.equals(""))
            {
                rs = sql.executeQuery(sSql);
                if (rs != null)
                {
                    if (rs.next())
                    {
                        if (fieldDefinition.getType() == hu.mgx.db.FieldType.INT)
                        {
                            dataField.setValue(new Integer(rs.getInt("defaultvalue")));
                        }
                        if (fieldDefinition.getType() == hu.mgx.db.FieldType.DATE)
                        {
                            //@todo kidolgozni, a többi alap mezõtípusra is!!!
                        }
                        if (fieldDefinition.getType() == hu.mgx.db.FieldType.DATETIME)
                        {
                            //@todo kidolgozni, a többi alap mezõtípusra is!!!
                        }
                    }
                }
            }
            if (fieldDefinition.isModifier())
            {
                dataField.setValue(new Integer(iUserId));
            }
        }
        catch (SQLException sqle)
        {
            System.err.println(sqle.getLocalizedMessage());
            sqle.printStackTrace(System.err);
        }
    }

    public static FieldDefinition convertFieldDefinitionToMandatory(FieldDefinition fdIn)
    {
        FieldDefinition fdOut = new FieldDefinition(fdIn.getName(), fdIn.getDisplayName(), fdIn.getType(), fdIn.getLength(), fdIn.getPrecision(), fdIn.getScale(), fdIn.isAllowNulls(), fdIn.isKey(), fdIn.isID(), fdIn.isMandatory());
        fdOut.setMandatory(true);
        return (fdOut);
    }

    public static boolean checkField(java.awt.Component parentComponent, DataField dataField, FieldDefinition fieldDefinition, boolean bExistingRecord, AppInterface appInterface)
    {
        dataField.check();
        if ((!fieldDefinition.isAllowNulls()) && (!fieldDefinition.isID()))
        {
            if (dataField.getValue() == null)
            {
                dataField.setFocus();
                JOptionPane.showMessageDialog(parentComponent, StringUtils.stringReplace(appInterface.getLanguageString("Kötelezõen kitöltendõ mezõ: XXX"), "XXX", fieldDefinition.getDisplayName()));
                return (false);
            }
        }
        if (fieldDefinition.isMandatory())
        {
            if (dataField.getValue() == null || dataField.getValue().toString().equals(""))
            {
                dataField.setFocus();
                JOptionPane.showMessageDialog(parentComponent, StringUtils.stringReplace(appInterface.getLanguageString("Kötelezõen kitöltendõ mezõ: XXX"), "XXX", fieldDefinition.getDisplayName()));
                return (false);
            }
        }
        if (!fieldDefinition.checkValue(parentComponent, dataField.getValue()))
        {
            dataField.setFocus();
            return (false);
        }
        if (bExistingRecord)
        {
            if (!fieldDefinition.checkModifiedValue(parentComponent, dataField.getValue(), dataField.getOldValue()))
            {
                dataField.setFocus();
                return (false);
            }
        }
        if (!bExistingRecord)
        {
            if (!fieldDefinition.checkInsertValue(parentComponent, dataField.getValue()))
            {
                dataField.setFocus();
                return (false);
            }
        }
        return (true);
    }

    public static boolean checkRecord(java.awt.Component parentComponent, hu.mgx.sql.Record oldRecord, hu.mgx.sql.Record newRecord, TableDefinition tableDefinition, boolean bExistingRecord, AppInterface appInterface)
    {
        if (!tableDefinition.checkRecord(parentComponent, oldRecord, newRecord))
        {
            //dataField.setFocus();
            return (false);
        }
        if (!bExistingRecord)
        {
            if (!tableDefinition.checkInsertRecord(parentComponent, oldRecord, newRecord))
            {
                //dataField.setFocus();
                return (false);
            }
        }
        return (true);
    }

    public static void dumpRsToLog(ResultSet rs, AppInterface appInterface)
    {
        StringBuffer sbLine = new StringBuffer();
        String sTmp = "";
        try
        {
            //appInterface.logLine("rs.getMetaData().getColumnCount() = " + Integer.toString(rs.getMetaData().getColumnCount()));
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++)
            {
                //appInterface.logLine(Integer.toString(i));
                sbLine.append(rs.getMetaData().getColumnName(i)).append("|");
            }
            appInterface.logLine(sbLine.toString());
            while (rs.next())
            {
                sbLine = new StringBuffer();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++)
                {
                    sTmp = rs.getString(i);
                    if (rs.wasNull())
                    {
                        sTmp = "NULL";
                    }
                    sbLine.append(sTmp).append("|");
                }
                appInterface.logLine(sbLine.toString());
            }
            rs.beforeFirst();
        }
        catch (SQLException sqle)
        {
            appInterface.handleError(sqle);
        }
    }

    private static String getIndexInfoIntoXML(DatabaseMetaData dbmd, String sCatalogName, String sTableName, AppInterface appInterface, int iLevel)
    {
        ResultSet rs;
        String sXML = "";

        try
        {
            rs = dbmd.getIndexInfo(sCatalogName, "", sTableName, false, false);
            while (rs.next())
            {
                sXML += StringUtils.repeat("    ", iLevel) + "<index name=\"" + rs.getString(6) + "\"";
                sXML += " column=\"" + rs.getString(9) + "\"";
                sXML += " ordinal=\"" + rs.getString(8) + "\"";
                sXML += "></index>" + StringUtils.sCrLf;
            }
        }
        catch (Exception e)
        {
            appInterface.handleError(e);
        }
        return (sXML);
    }

    private static String getColumnInfoIntoXML(DatabaseMetaData dbmd, String sCatalogName, String sTableName, AppInterface appInterface, int iLevel)
    {
        ResultSet rs;
        String sXML = "";

        try
        {
            rs = dbmd.getColumns(sCatalogName, "", sTableName, null);
            while (rs.next())
            {
                sXML += StringUtils.repeat("    ", iLevel) + "<column name=\"" + rs.getString(4) + "\"";
                sXML += " typecode=\"" + rs.getString(5) + "\"";
                sXML += " type=\"" + rs.getString(6) + "\"";
                sXML += " size=\"" + rs.getString(7) + "\"";
                sXML += " decimal=\"" + rs.getString(9) + "\"";
                sXML += " radix=\"" + rs.getString(10) + "\"";
                sXML += " nullable=\"" + rs.getString(11) + "\"";
                sXML += " ordinal=\"" + rs.getString(17) + "\"";
                sXML += " is_nullable=\"" + rs.getString(18) + "\"";
//                sXML += " autoincrement=\"" + rs.getString(23) + "\"";
                sXML += "></column>" + StringUtils.sCrLf;
            }
        }
        catch (Exception e)
        {
            appInterface.handleError(e);
        }
        return (sXML);
    }

    public static String getTableInfoIntoXML(DatabaseMetaData dbmd, String sCatalogName, AppInterface appInterface, int iLevel)
    {
        ResultSet rs;
        String sXML = "";

        try
        {
            rs = dbmd.getTables(sCatalogName, "", "", null);
            while (rs.next())
            {
                sXML += StringUtils.repeat("    ", iLevel) + "<table name=\"" + rs.getString(3) + "\">" + StringUtils.sCrLf;
                sXML += getColumnInfoIntoXML(dbmd, sCatalogName, rs.getString(3), appInterface, iLevel + 1);
                sXML += getIndexInfoIntoXML(dbmd, sCatalogName, rs.getString(3), appInterface, iLevel + 1);
                sXML += StringUtils.repeat("    ", iLevel) + "</table>" + StringUtils.sCrLf;
            }
        }
        catch (Exception e)
        {
            appInterface.handleError(e);
        }
        return (sXML);
    }

    private static String getSchemaInfoIntoXML(DatabaseMetaData dbmd, String sCatalogName, String sSchemaName, AppInterface appInterface, int iLevel)
    {
        ResultSet rs;
        String sXML = "";

        sXML += StringUtils.repeat("    ", iLevel) + "<schema name=\"" + sSchemaName + "\">" + StringUtils.sCrLf;
        try
        {
            rs = dbmd.getTables(sCatalogName, "", "", null);
            while (rs.next())
            {
                sXML += getTableInfoIntoXML(dbmd, sCatalogName, appInterface, iLevel + 1);
            }
        }
        catch (Exception e)
        {
            appInterface.handleError(e);
        }
        sXML += StringUtils.repeat("    ", iLevel) + "</schema>" + StringUtils.sCrLf;
        return (sXML);
    }

    private static String getCatalogInfoIntoXML(DatabaseMetaData dbmd, String sCatalogName, AppInterface appInterface, int iLevel)
    {
        ResultSet rs;
        String sXML = "";
        int iSchemaCount = 0;

        try
        {
            sXML += StringUtils.repeat("    ", iLevel) + "<catalog name=\"" + sCatalogName + "\">" + StringUtils.sCrLf;
            //rs = dbmd.getSchemas(sCatalogName, null);
            iSchemaCount = 0;
            rs = dbmd.getSchemas();
            while (rs.next())
            {
                sXML += getSchemaInfoIntoXML(dbmd, sCatalogName, rs.getString(1), appInterface, iLevel + 1);
                ++iSchemaCount;
            }
            if (iSchemaCount == 0)
            {
                sXML += getTableInfoIntoXML(dbmd, sCatalogName, appInterface, iLevel + 1);
            }
            sXML += StringUtils.repeat("    ", iLevel) + "</catalog>" + StringUtils.sCrLf;
        }
        catch (Exception e)
        {
            appInterface.handleError(e);
        }
        return (sXML);
    }

    public static String getMetadataInfoIntoXML(DatabaseMetaData dbmd, AppInterface appInterface)
    {
        ResultSet rs;
        String sXML = "";
        int iLevel = 0;

        sXML += "<?xml version=\"1.0\" encoding=\"iso-8859-2\"?>" + StringUtils.sCrLf;
        sXML += "<DatabaseMetaData>" + StringUtils.sCrLf;
        try
        {
            sXML += StringUtils.repeat("    ", 1) + "<catalog term=\"" + dbmd.getCatalogTerm() + "\">" + StringUtils.sCrLf;
            sXML += StringUtils.repeat("    ", 1) + "<catalog separator=\"" + dbmd.getCatalogSeparator() + "\">" + StringUtils.sCrLf;
            sXML += StringUtils.repeat("    ", 1) + "<schema term=\"" + dbmd.getSchemaTerm() + "\">" + StringUtils.sCrLf;
            rs = dbmd.getCatalogs();
            while (rs.next())
            {
                if (rs.getString(1).equals("xyz"))
                {
                    sXML += getCatalogInfoIntoXML(dbmd, rs.getString(1), appInterface, iLevel + 1);
                }
            }
        }
        catch (java.sql.SQLException sqle)
        {
            appInterface.handleError(sqle);
        }
        sXML += "</DatabaseMetaData>" + StringUtils.sCrLf;
        return (sXML);
    }
}
