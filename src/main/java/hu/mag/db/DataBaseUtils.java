package hu.mag.db;

import hu.mag.swing.table.MagTableModel;
import hu.mgx.app.common.AppInterface;
import hu.mgx.util.IntegerUtils;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseUtils {

    private AppInterface appInterface = null;

    public DataBaseUtils(AppInterface appInterface) {
        this.appInterface = appInterface;
    }

    public void dbInfo(String sConnectionName) {
        Connection conn = appInterface.getTemporaryConnection(sConnectionName);
        DatabaseMetaData dbmd = null;
        ResultSet rsCatalog = null;
        ResultSet rsSchema = null;
        ResultSet rsTable = null;
        ResultSet rs = null;
        try {
            dbmd = conn.getMetaData();
            rsCatalog = dbmd.getCatalogs();
            while (rsCatalog.next()) {
                appInterface.logLine(rsCatalog.getString("TABLE_CAT"));
            }

            appInterface.logLine("--------------------");
            rsSchema = dbmd.getSchemas("sqlszla", null);
//            if (rsSchema.next()) {
//                for (int i = 0; i < rsSchema.getMetaData().getColumnCount(); i++) {
//                    appInterface.logLine(rsSchema.getMetaData().getColumnName(i + 1));
//                }
//            }
            while (rsSchema.next()) {
                appInterface.logLine(rsSchema.getString("TABLE_SCHEM"));
            }

            appInterface.logLine("--------------------");
            //rsTable = dbmd.getTables("sqlszla", "sqlszla_user", "ceg", null);
            String types[] = {"TABLE"};
            rsTable = dbmd.getTables("sqlszla", "dbo", null, types);
//            if (rsTable.next()) {
//                for (int i = 0; i < rsTable.getMetaData().getColumnCount(); i++) {
//                    appInterface.logLine(rsTable.getMetaData().getColumnName(i + 1));
//                }
//            }
            while (rsTable.next()) {
                //appInterface.logLine(rsTable.getString("TABLE_SCHEM") + " " + rsTable.getString("TABLE_NAME") + " " + rsTable.getString("TABLE_TYPE") + " " + rsTable.getString("REMARKS"));
                if (!rsTable.getString("TABLE_NAME").startsWith("!")) {
                    rs = conn.prepareStatement("select top 1 * from " + rsTable.getString("TABLE_NAME")).executeQuery();
                    for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                        if (!MagTableModel.isKnownClass(Class.forName(rs.getMetaData().getColumnClassName(i + 1)))) {
                            appInterface.logLine(rsTable.getString("TABLE_NAME") + "." + rs.getMetaData().getColumnName(i + 1) + " " + rs.getMetaData().getColumnClassName(i + 1));
                        }
                    }
                }
            }
            conn.close();
            conn = null;
        } catch (SQLException sqle) {
            appInterface.handleError(sqle);
        } catch (ClassNotFoundException cnfe) {
            appInterface.handleError(cnfe);
        }

        //PreparedStatement ps = null;
        //ResultSet rspk = null;
    }

    public boolean executeSQL(String sConnectionName, String sSql) {
        PreparedStatement ps = null;
        Connection conn = appInterface.getTemporaryConnection(sConnectionName);
        if (conn == null) {
            return (false);
        }
        try {
            //conn.prepareStatement(sSql).execute();
            ps = conn.prepareStatement(sSql);
            ps.execute();
            ps.close();
            ps = null;
            conn.close();
            conn = null;
        } catch (SQLException sqle) {
            appInterface.handleError(sqle);
            return (false);
        }
        return (true);
    }

    //@todo le kell cserélni memorytable-t visszaadóra ...
//    public ResultSet executeSQLQuery(String sConnectionName, String sSql) {
//        Connection conn = appInterface.getTemporaryConnection(sConnectionName);
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//
//        if (conn == null) {
//            return (rs);
//        }
//        try {
//            //rs = conn.prepareStatement(sSql).executeQuery();
//            ps = conn.prepareStatement(sSql);
//            rs = ps.executeQuery();
//            rs.close();
//            rs = null;
//            ps.close();
//            ps = null;
//            conn.close();
//            conn = null;
//        } catch (SQLException sqle) {
//            appInterface.handleError(sqle);
//            return (rs);
//        }
//        return (rs);
//    }
    public boolean next(ResultSet rs) {
        try {
            if (rs.next()) {
                return (true);
            }
        } catch (SQLException sqle) {
            appInterface.handleError(sqle);
        }
        return (false);
    }

    public boolean close(ResultSet rs) {
        try {
            rs.close();
            return (true);
        } catch (SQLException sqle) {
            appInterface.handleError(sqle);
        }
        return (false);
    }

    public String getString(ResultSet rs, String sColumnName) {
        String sRetVal = null;
        try {
            sRetVal = rs.getString(sColumnName);
        } catch (SQLException sqle) {
            appInterface.handleError(sqle);
        }
        return (sRetVal);
    }

    public String getStringTrim(ResultSet rs, String sColumnName, String sNullValue) {
        String sRetVal = null;
        try {
            sRetVal = rs.getString(sColumnName);
            if (rs.wasNull()) {
                sRetVal = sNullValue;
            }
            if (sRetVal != null) {
                sRetVal = sRetVal.trim();
            }
        } catch (SQLException sqle) {
            appInterface.handleError(sqle);
        }
        return (sRetVal);
    }

    public Integer getInteger(ResultSet rs, String sColumnName) {
        Integer integer = null;
        try {
            integer = rs.getInt(sColumnName);
            if (rs.wasNull()) {
                integer = null;
            }
        } catch (SQLException sqle) {
            appInterface.handleError(sqle);
        }
        return (integer);
    }

    public static int executeSQLInsertReturnID(AppInterface ai, String sConnectionName, String sSql) {
        int iRetVal = 0;
        Connection conn = ai.getTemporaryConnection(sConnectionName);
        if (conn == null) {
            return (iRetVal);
        }
        try {
            PreparedStatement ps = conn.prepareStatement(sSql, PreparedStatement.RETURN_GENERATED_KEYS);
            int iCount = ps.executeUpdate();
            if (iCount > 0) {
                ResultSet rsForGeneratedKeys = ps.getGeneratedKeys();
                if (rsForGeneratedKeys != null) {
                    if (rsForGeneratedKeys.next()) {
                        //return (IntegerUtils.convertToInteger(rsForGeneratedKeys.getObject(1)).intValue());
                        iRetVal = IntegerUtils.convertToInteger(rsForGeneratedKeys.getObject(1)).intValue();
                    }
                    rsForGeneratedKeys.close();
                    rsForGeneratedKeys = null;
                }
            }
            ps.close();
            ps = null;
            conn.close();
            conn = null;
        } catch (SQLException sqle) {
            ai.handleError(sqle);
            //return (0);
        }
        return (iRetVal);
    }

    public static boolean executeSQL(AppInterface ai, String sConnectionName, String sSql) {
        Connection conn = ai.getTemporaryConnection(sConnectionName);
        if (conn == null) {
            return (false);
        }
        try {
            Statement st = conn.createStatement();
            st.execute(sSql);
            st.close();
            st = null;
            conn.close();
            conn = null;
        } catch (SQLException sqle) {
            ai.handleError(sqle);
            return (false);
        }
        return (true);
    }

// there was problems with the null values -> "null" has no class ...    
//    public static boolean executeSQLWithParameters(AppInterface ai, String sConnectionName, String sSql, Object... oParam) {
//        Connection conn = ai.getTemporaryConnection(sConnectionName);
//        if (conn == null) {
//            return (false);
//        }
//        try {
//            PreparedStatement pst = conn.prepareStatement(sSql);
//            for (int iParam = 0; iParam < oParam.length; iParam++) {
//                System.out.println(oParam[iParam].toString());
//                if (oParam[iParam].getClass() == Integer.class) {
//                    if (oParam[iParam] == null) {
//                        pst.setNull(iParam + 1, java.sql.Types.INTEGER);
//                    } else {
//                        pst.setInt(iParam + 1, ((Integer) oParam[iParam]).intValue());
//                    }
//                } else if (oParam[iParam].getClass() == String.class) {
//                    if (oParam[iParam] == null) {
//                        pst.setNull(iParam + 1, java.sql.Types.VARCHAR);
//                    } else {
//                        pst.setString(iParam + 1, (String) oParam[iParam]);
//                    }
//                } else if (oParam[iParam].getClass() == java.math.BigDecimal.class) {
//                    if (oParam[iParam] == null) {
//                        pst.setNull(iParam + 1, java.sql.Types.DECIMAL);
//                    } else {
//                        pst.setBigDecimal(iParam + 1, (java.math.BigDecimal) oParam[iParam]);
//                    }
//                } else {
//                    pst.setObject(iParam + 1, oParam[iParam]);
//                }
//            }
//            pst.execute(sSql);
//            pst.close();
//            pst = null;
//            conn.close();
//            conn = null;
//        } catch (SQLException sqle) {
//            ai.handleError(sqle);
//            return (false);
//        }
//        return (true);
//    }

//    public static ResultSet executeSQLQuery(AppInterface ai, String sConnectionName, String sSql) {
//        Connection conn = ai.getTemporaryConnection(sConnectionName);
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//
//        if (conn == null) {
//            return (rs);
//        }
//        try {
//            ps = conn.prepareStatement(sSql);
//            rs = ps.executeQuery();
//            ps.close();
//            ps = null;
//            conn.close();
//            conn = null;
//        } catch (SQLException sqle) {
//            ai.handleError(sqle);
//            return (rs);
//        }
//        return (rs);
//    }
    public static boolean rsNext(AppInterface ai, ResultSet rs) {
        try {
            if (rs.next()) {
                return (true);
            }
        } catch (SQLException sqle) {
            ai.handleError(sqle);
        }
        return (false);
    }

    public static boolean rsClose(AppInterface ai, ResultSet rs) {
        try {
            rs.close();
            return (true);
        } catch (SQLException sqle) {
            ai.handleError(sqle);
        }
        return (false);
    }

}
