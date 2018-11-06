package hu.mgx.util;

import hu.mgx.app.common.*;
import java.sql.*;

public abstract class DatabaseUtils {

    public DatabaseUtils() {
    }

    public static int convertResultSetMetadataTypesToFieldTypes(int iRSMType) {
        if (iRSMType == java.sql.Types.INTEGER) {
            return (hu.mgx.db.FieldType.INT);
        }
        switch (iRSMType) {
            case java.sql.Types.INTEGER:
                return (hu.mgx.db.FieldType.INT);
            case java.sql.Types.CHAR:
                return (hu.mgx.db.FieldType.CHAR);
            case java.sql.Types.DATE:
                return (hu.mgx.db.FieldType.DATE);
            case java.sql.Types.TIME:
                return (hu.mgx.db.FieldType.TIME);
            case java.sql.Types.VARCHAR:
                return (hu.mgx.db.FieldType.CHAR);
            case java.sql.Types.BOOLEAN:
                return (hu.mgx.db.FieldType.BOOLEAN);
            case java.sql.Types.BIT:
                return (hu.mgx.db.FieldType.BIT);
            case java.sql.Types.DECIMAL:
                return (hu.mgx.db.FieldType.DECIMAL);
            case java.sql.Types.FLOAT:
                return (hu.mgx.db.FieldType.FLOAT);
            case java.sql.Types.BLOB:
                return (hu.mgx.db.FieldType.BLOB);
            case java.sql.Types.NULL:
                return (hu.mgx.db.FieldType.NULL);
            case java.sql.Types.TIMESTAMP:
                return (hu.mgx.db.FieldType.TIMESTAMP);
            case java.sql.Types.NUMERIC:
                return (hu.mgx.db.FieldType.DECIMAL);
        }
        return (hu.mgx.db.FieldType.CHAR);
    }

    public static void logMetaData(AppInterface appInterface, ResultSet rs) {
        ResultSetMetaData rsm = null;
        try {
            rsm = rs.getMetaData();
            for (int i = 1; i <= rsm.getColumnCount(); i++) {
                appInterface.logLine("ColumnClassName = " + rsm.getColumnClassName(i));
            }
        } catch (SQLException ex) {
            appInterface.handleError(ex);
        }
    }

    public static void setCallableStatementInt(CallableStatement cstmt, int iParameterIndex, Integer value) throws SQLException {
        if (value == null) {
            cstmt.setNull(iParameterIndex, java.sql.Types.INTEGER);
        } else {
            cstmt.setInt(iParameterIndex, value.intValue());
        }
    }
}
