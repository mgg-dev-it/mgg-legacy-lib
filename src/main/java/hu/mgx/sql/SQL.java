package hu.mgx.sql;

import java.sql.*;

import hu.mgx.app.common.*;
import hu.mgx.db.*;
import hu.mgx.lang.*;

public class SQL {

    private CONN conn = null;
    private Connection connection = null;
    private AppInterface appInterface = null;

    public SQL(CONN conn, AppInterface appInterface) {
        this.conn = conn;
        connection =
                conn.getConnection();
        this.appInterface = appInterface;
    }

    public SQL(Connection connection, AppInterface appInterface) {
        this.connection = connection;
        this.appInterface = appInterface;
    }

    private class DebugString {

        public String sDebugString = "";
    }

    public TemporaryResultSet doSelect(TableDefinition td) {
        String sSQL = "select * from " + td.getName();
        String sWhere = "";
        String sOrder = "";

        if (!td.getOrderBy().equals("")) {
            sOrder = " order by " + td.getOrderBy();
        }

        for (int i = 0; i
                < td.getFieldCount(); i++) {
            try {
                if (td.getFieldDefinition(i).isFilter()) {
                    sWhere += (sWhere.equals("") ? " where " : " and ") + td.getFieldDefinition(i).getName() + "=";
                    sWhere +=
                            td.getFieldDefinition(i).getFilter(); //@todo típus szerint kellene esetleg aposztróf, de ezt most a filterbe kell tenni
                }

                if ((td.getFieldDefinition(i).isKey()) && (td.getOrderBy().equals(""))) {
                    sOrder += (sOrder.equals("") ? " order by " : ", ") + td.getFieldDefinition(i).getName();
                }

            } catch (TableDefinitionException tde) {
                appInterface.handleError(tde);
            }

        }
        sSQL += " " + sWhere + " ";
        sSQL +=
                " " + sOrder + " ";

        TemporaryResultSet trs = new TemporaryResultSet(connection, appInterface);
        trs.executeQuery(sSQL);
        return (trs);
    }

//    public ResultSet select(TableDefinition td)
//    {
//        String sSQL = "select * from " + td.getName();
//        String sWhere = "";
//        String sOrder = "";
//
//        if (!td.getOrderBy().equals(""))
//        {
//            sOrder = " order by " + td.getOrderBy();
//        }
//        for (int i = 0; i < td.getFieldCount(); i++)
//        {
//            try
//            {
//                if (td.getFieldDefinition(i).isFilter())
//                {
//                    sWhere += (sWhere.equals("") ? " where " : " and ") + td.getFieldDefinition(i).getName() + "=";
//                    sWhere += td.getFieldDefinition(i).getFilter(); //@todo típus szerint kellen esetleg aposztróf, de ezt most a filterbe kell tenni
//                }
//                if ((td.getFieldDefinition(i).isKey()) && (td.getOrderBy().equals("")))
//                {
//                    sOrder += (sOrder.equals("") ? " order by " : ", ") + td.getFieldDefinition(i).getName();
//                }
//            }
//            catch (TableDefinitionException tde)
//            {
//                appInterface.handleError(tde);
//            }
//        }
//        sSQL += " " + sWhere + " ";
//        sSQL += " " + sOrder + " ";
//        ResultSet rs = null;
//        try
//        {
//            PreparedStatement pst = connection.prepareStatement(sSQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//            rs = pst.executeQuery();
//        }
//        catch (SQLException sqle)
//        {
//            appInterface.handleError(sqle, sSQL);
//        }
//        return (rs);
//    }
    public boolean execute(String sSQL) {
        boolean bRetVal = false;
        PreparedStatement pst = null;
        try {
            pst = connection.prepareStatement(sSQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            bRetVal = pst.execute();

            //2010.12.02.
            if (!bRetVal) {
                bRetVal = (pst.getUpdateCount() > -1);
            }
            pst.close();
        } catch (SQLException sqle) {
            appInterface.handleError(sqle, sSQL);
        }

        return (bRetVal);
    }

    public ResultSet executeQuery(String sSQL) {
        ResultSet rs = null;
        PreparedStatement pst = null;
        try {
            pst = connection.prepareStatement(sSQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
        } catch (SQLException sqle) {
            appInterface.handleError(sqle, sSQL);
        }

        return (rs);
    }

    public boolean exists(String sSQL) {
        boolean bFound = false;
        ResultSet rs = null;
        PreparedStatement pst = null;
        try {
            pst = connection.prepareStatement(sSQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            if (rs.next()) {
                bFound = true;
            }
        } catch (SQLException sqle) {
            appInterface.handleError(sqle, sSQL);
        }
        return (bFound);
    }

    public ResultSet executeQuery2(PreparedStatement pst, ResultSet rs, String sSQL) {
        try {
            pst = connection.prepareStatement(sSQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
        } catch (SQLException sqle) {
            appInterface.handleError(sqle, sSQL);
        }

        return (rs);
    }

    private void setPreparedStatementValues(String sDebugInfo, PreparedStatement pst, Record record, int iFieldType, int iPstFieldIndex, int iRecordFieldIndex, DebugString dbgstr) {
        java.sql.Date sdate;
        java.util.Date udate;
        java.sql.Timestamp timestamp;
        ByteArray byteArray;

        String sClassName = "";
        String sValue = "";

        try {
            if (record.getFieldValue(iRecordFieldIndex) != null) {
                sClassName = record.getFieldValue(iRecordFieldIndex).getClass().getName();
                sValue =
                        record.getFieldValue(iRecordFieldIndex).toString();
            }

            //appInterface.logLine("setPreparedStatementValues " + sDebugInfo + " (" + hu.mgx.db.FieldType.getDisplayName(iFieldType) + ") " + Integer.toString(iRecordFieldIndex) + " (" + sClassName + ") " + sValue, LoggerInterface.LOG_DEBUG);
            if (iFieldType == hu.mgx.db.FieldType.INT) {
                if (record.getFieldValue(iRecordFieldIndex) == null) {
                    pst.setNull(iPstFieldIndex, java.sql.Types.INTEGER);
                    dbgstr.sDebugString = hu.mgx.util.StringUtils.stringReplace(dbgstr.sDebugString, "<" + Integer.toString(iPstFieldIndex) + ">", "null");
                } else {
                    pst.setInt(iPstFieldIndex, ((Integer) record.getFieldValue(iRecordFieldIndex)).intValue());
                    dbgstr.sDebugString = hu.mgx.util.StringUtils.stringReplace(dbgstr.sDebugString, "<" + Integer.toString(iPstFieldIndex) + ">", record.getFieldValue(iRecordFieldIndex).toString());
                }

            }
            if (iFieldType == hu.mgx.db.FieldType.BIT) {
                if (record.getFieldValue(iRecordFieldIndex) == null) {
                    pst.setNull(iPstFieldIndex, java.sql.Types.INTEGER);
                    dbgstr.sDebugString = hu.mgx.util.StringUtils.stringReplace(dbgstr.sDebugString, "<" + Integer.toString(iPstFieldIndex) + ">", "null");
                } else {
                    pst.setInt(iPstFieldIndex, ((Integer) record.getFieldValue(iRecordFieldIndex)).intValue());
                    dbgstr.sDebugString = hu.mgx.util.StringUtils.stringReplace(dbgstr.sDebugString, "<" + Integer.toString(iPstFieldIndex) + ">", record.getFieldValue(iRecordFieldIndex).toString());
                }

            }
            if (iFieldType == hu.mgx.db.FieldType.DECIMAL) {
                if (record.getFieldValue(iRecordFieldIndex) == null) {
                    pst.setNull(iPstFieldIndex, java.sql.Types.DECIMAL);
                    dbgstr.sDebugString = hu.mgx.util.StringUtils.stringReplace(dbgstr.sDebugString, "<" + Integer.toString(iPstFieldIndex) + ">", "null");
                } else {
                    pst.setDouble(iPstFieldIndex, ((Double) record.getFieldValue(iRecordFieldIndex)).doubleValue());
                    dbgstr.sDebugString = hu.mgx.util.StringUtils.stringReplace(dbgstr.sDebugString, "<" + Integer.toString(iPstFieldIndex) + ">", record.getFieldValue(iRecordFieldIndex).toString());
                }

            } else if (iFieldType == hu.mgx.db.FieldType.CHAR) {
                if (record.getFieldValue(iRecordFieldIndex) == null) {
                    pst.setNull(iPstFieldIndex, java.sql.Types.CHAR);
                    dbgstr.sDebugString = hu.mgx.util.StringUtils.stringReplace(dbgstr.sDebugString, "<" + Integer.toString(iPstFieldIndex) + ">", "null");
                } else {
                    pst.setString(iPstFieldIndex, (String) record.getFieldValue(iRecordFieldIndex));
                    dbgstr.sDebugString = hu.mgx.util.StringUtils.stringReplace(dbgstr.sDebugString, "<" + Integer.toString(iPstFieldIndex) + ">", "'" + record.getFieldValue(iRecordFieldIndex).toString() + "'");
                }

            } else if (iFieldType == hu.mgx.db.FieldType.MEDIUMBLOB) {
                if (record.getFieldValue(iRecordFieldIndex) == null) {
                    pst.setNull(iPstFieldIndex, java.sql.Types.BLOB);
                    dbgstr.sDebugString = hu.mgx.util.StringUtils.stringReplace(dbgstr.sDebugString, "<" + Integer.toString(iPstFieldIndex) + ">", "null");
                } else {
                    byteArray = (ByteArray) record.getFieldValue(iRecordFieldIndex);
                    pst.setBytes(iPstFieldIndex, byteArray.getBytes());
                    dbgstr.sDebugString = hu.mgx.util.StringUtils.stringReplace(dbgstr.sDebugString, "<" + Integer.toString(iPstFieldIndex) + ">", "<MEDIUMBLOB>");
                }

            } else if (iFieldType == hu.mgx.db.FieldType.DATE) {
                if (record.getFieldValue(iRecordFieldIndex) == null) {
                    pst.setNull(iPstFieldIndex, java.sql.Types.DATE);
                    dbgstr.sDebugString = hu.mgx.util.StringUtils.stringReplace(dbgstr.sDebugString, "<" + Integer.toString(iPstFieldIndex) + ">", "null");
                } else {
                    udate = (java.util.Date) record.getFieldValue(iRecordFieldIndex);
                    sdate = new java.sql.Date(udate.getTime());
                    pst.setDate(iPstFieldIndex, sdate);
                    dbgstr.sDebugString = hu.mgx.util.StringUtils.stringReplace(dbgstr.sDebugString, "<" + Integer.toString(iPstFieldIndex) + ">", "'" + appInterface.getDateTimeFormat().format(record.getFieldValue(iRecordFieldIndex)) + "'");
                }

            } else if (iFieldType == hu.mgx.db.FieldType.DATETIME) {
                if (record.getFieldValue(iRecordFieldIndex) == null) {
                    pst.setNull(iPstFieldIndex, java.sql.Types.DATE);
                    dbgstr.sDebugString = hu.mgx.util.StringUtils.stringReplace(dbgstr.sDebugString, "<" + Integer.toString(iPstFieldIndex) + ">", "null");
                } else {
                    udate = (java.util.Date) record.getFieldValue(iRecordFieldIndex);
                    timestamp = new Timestamp(udate.getTime());
                    pst.setTimestamp(iPstFieldIndex, timestamp);
                    dbgstr.sDebugString = hu.mgx.util.StringUtils.stringReplace(dbgstr.sDebugString, "<" + Integer.toString(iPstFieldIndex) + ">", "'" + appInterface.getDateTimeFormat().format(record.getFieldValue(iRecordFieldIndex)) + "'");
                }

            } else {
            }
        } catch (SQLException sqle) {
            appInterface.handleError(sqle);
        }
    }

    public boolean insert(TableDefinition td, Record record) {
        return (insert(td, record, LoggerInterface.LOG_DEBUG));
    }

    public boolean insert(TableDefinition td, Record record, int iLogLevel) {
        boolean bSuccess = true;
        String sSQLCheck = "";
        DebugString debugStringCheck = new DebugString();
        String sSQLInsert = "";
        DebugString debugStringInsert = new DebugString();
        String sFieldList = "";
        String sValues = "";
        String sWhere = "";
        int iFieldType = hu.mgx.db.FieldType.NULL;
        int iCheckField = 0;
        int iInsertField = 0;
        boolean bHasID = false;
        boolean bHasKey = false;
        boolean bIDKey = false;
        PreparedStatement pstCheck;

        PreparedStatement pstInsert;

        for (int i = 0; i
                < td.getFieldCount(); i++) {
            try {
                if (td.getFieldDefinition(i).isID()) {
                    bHasID = true;
                }
                // Ha egy ID mezö kulcs is, akkor nem ellenörizhetünk duplikáltságot, mert az ID definíciójából következően mindig új értéket kap.
                if (td.getFieldDefinition(i).isID() && td.getFieldDefinition(i).isKey()) {
                    bIDKey = true;
                }
                // ID mezőt nem szúrunk be ... bár később lekérdezzük: lásd getLastInsertedID() !!!
                // Ha nem ID mező, akkor hozzáadjuk a mezőlistához, az érték listához, valamint ha kulcs, akkor a where-listához is
                if (!td.getFieldDefinition(i).isID()) {
                    sFieldList += (!sFieldList.equals("") ? ", " : "") + td.getFieldDefinition(i).getName();
                    sValues +=
                            (!sValues.equals("") ? ", " : "") + "?";
                    if (td.getFieldDefinition(i).isKey()) {
                        bHasKey = true;
                        sWhere +=
                                (!sWhere.equals("") ? " and " : "") + td.getFieldDefinition(i).getName() + "=?";
                    }

                }
            } catch (TableDefinitionException tde) {
                bSuccess = false;
                appInterface.handleError(tde);
            }

        }
        if ((!bHasKey) && (sWhere.equals(""))) {
            sWhere = "1=0";
        }

        sSQLCheck = "select 1 from " + td.getName() + " where " + sWhere;
        debugStringCheck.sDebugString = sSQLCheck;
        int iTmp = 0;
        while (debugStringCheck.sDebugString.indexOf("?") > -1 && iTmp < 999) {
            debugStringCheck.sDebugString = hu.mgx.util.StringUtils.stringReplaceFirst(debugStringCheck.sDebugString, "?", "<" + Integer.toString(++iTmp) + ">");
        }

        sSQLInsert = "insert into " + td.getName() + " (" + sFieldList + ") values (" + sValues + ")";
        debugStringInsert.sDebugString = sSQLInsert;
        iTmp =
                0;
        while (debugStringInsert.sDebugString.indexOf("?") > -1 && iTmp < 999) {
            debugStringInsert.sDebugString = hu.mgx.util.StringUtils.stringReplaceFirst(debugStringInsert.sDebugString, "?", "<" + Integer.toString(++iTmp) + ">");
        }

        //appInterface.logLine("SQLInsert = " + sSQLInsert, iLogLevel);
        //appInterface.logLine("sSQLInsertDebug = " + debugStringInsert.sDebugString, iLogLevel);
        //appInterface.logLine("SQLCheck = " + sSQLCheck, LoggerInterface.LOG_DEBUG);
        //appInterface.logLine("sSQLCheckDebug = " + debugStringCheck.sDebugString, LoggerInterface.LOG_DEBUG);
        try {
            pstCheck = connection.prepareStatement(sSQLCheck, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstInsert = connection.prepareStatement(sSQLInsert, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            iCheckField = 0;
            iInsertField = 0;
            for (int i = 0; i < td.getFieldCount(); i++) {
                iFieldType = td.getFieldDefinition(i).getType();
                if (!td.getFieldDefinition(i).isID()) {
                    ++iInsertField;
                    setPreparedStatementValues("pstInsert", pstInsert, record, iFieldType, iInsertField, i, debugStringInsert);
                    if (td.getFieldDefinition(i).isKey()) {
                        ++iCheckField;
                        setPreparedStatementValues("pstCheck", pstCheck, record, iFieldType, iCheckField, i, debugStringCheck);
                    }

                }
            }
            if (!bIDKey && bHasKey) {
                //appInterface.logLine("sSQLCheckDebug = " + debugStringCheck.sDebugString, iLogLevel);
                ResultSet rs = pstCheck.executeQuery();
                if (rs.next()) {
                    while (rs.next()) {
                        ;
                    }

                    rs.close();
                    bSuccess =
                            false;
                    appInterface.handleError("Létező rekord, nem lehet beszúrni!");
                } else {
                    appInterface.logLine(debugStringInsert.sDebugString, iLogLevel);
                    pstInsert.executeUpdate();
                }

            } else {
                appInterface.logLine(debugStringInsert.sDebugString, iLogLevel);
                pstInsert.executeUpdate();
            }

        } catch (TableDefinitionException tde) {
            bSuccess = false;
            appInterface.handleError(tde);
        } catch (SQLException sqle) {
            bSuccess = false;
            appInterface.handleError(sqle, sSQLCheck + " " + sSQLInsert);
        }

        if (bHasID) {
            int iID = getLastInsertedID(td.getName());
            if (iID > -1) {
                appInterface.logLine("Last inserted ID = " + Integer.toString(iID), iLogLevel);
                for (int i = 0; i < td.getFieldCount(); i++) {
                    try {
                        if (td.getFieldDefinition(i).isID()) {
                            record.setField(i, new Integer(iID));
                        }

                    } catch (TableDefinitionException tde) {
                        bSuccess = false;
                        appInterface.handleError(tde);
                    }

                }
            }
        }
        return (bSuccess);
    }

    private boolean isMySQL() {
        if (conn != null && (conn.isMySQL())) {
            return (true);
        }

        return (false);
    }

    private boolean isMsSQL() {
        if (conn != null && (conn.isMsSQL())) {
            return (true);
        }

        return (false);
    }

    public int getLastInsertedID(String sTableName) {
        ResultSet rs;
        if (isMySQL()) {
            try {
                rs = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery("select last_insert_id()");
                if (rs.next()) {
                    return (rs.getInt(1));
                }
                return (-1);
            } catch (SQLException e) {
                return (-1);
            }
        }
        if (isMsSQL()) {
            try {
                rs = connection.createStatement().executeQuery("select ident_current('" + sTableName + "') as retval");
                if (rs.next()) {
                    return (rs.getInt(1));
                }
                return (-1);
            } catch (SQLException e) {
                System.out.println(e.getLocalizedMessage());
                return (-1);
            }
        }
        return (-1);
    }

    private boolean equal(Object oOld, Object oNew, int iFieldType) {
        if (oOld == null && oNew != null) {
            return (false);
        }

        if (oOld != null && oNew == null) {
            return (false);
        }

        if (oOld != null && oNew != null) {
            if (iFieldType == hu.mgx.db.FieldType.DATETIME) {
                if (((java.util.Date) oOld).compareTo((java.util.Date) oNew) != 0) {
                    return (false);
                }

            } else {
                if (!oOld.toString().equals(oNew.toString())) {
                    return (false);
                }

            }
        }
        return (true);
    }

    public boolean update(TableDefinition td, Record oldRecord, Record newRecord) {
        return (update(td, oldRecord, newRecord, LoggerInterface.LOG_DEBUG));
    }

    public boolean update(TableDefinition td, Record oldRecord, Record newRecord, int iLogLevel) {
        boolean bSuccess = true;
        String sSet = "";
        String sSQLUpdate = "";
        DebugString debugStringUpdate = new DebugString();
        String sSQLOriginExists = "";
        DebugString debugStringOriginExists = new DebugString();
        String sSQLNewNotExists = "";
        DebugString debugStringNewNotExists = new DebugString();
        String sWhere = "";
        int iFieldType = hu.mgx.db.FieldType.NULL;
        int iCheckField = 0;
        int iUpdateField = 0;
        int iOffsetField = 0;
        boolean bKeyChanged = false;
        boolean bChanged = false;
        boolean bHasKey = false;
        boolean bIDKey = false;
        ResultSet rs;
        Object oOld;
        Object oNew;
        String sOldClassName;
        String sNewClassName;
        String sOldString;
        String sNewString;

        for (int i = 0; i
                < td.getFieldCount(); i++) {
            try {
                if (td.getFieldDefinition(i).isID() && td.getFieldDefinition(i).isKey()) {
                    bIDKey = true;
                } // Ha egy ID mezö kulcs is, akkor nem ellenörizhetünk duplikáltságot, mert az ID definíciójából következően mindig új értéket kap.

                oOld = oldRecord.getFieldValue(i);
                oNew =
                        newRecord.getFieldValue(i);
                if (!equal(oOld, oNew, td.getFieldDefinition(i).getType())) { // Mező értéke megváltozott
                    sOldClassName = "null";
                    sOldString = "";
                    sNewClassName = "null";
                    sNewString = "";
                    if (oOld != null) {
                        sOldClassName = oOld.getClass().getName();
                        if (oOld.getClass().getName().indexOf("Byte") < 0) {
                            sOldString = hu.mgx.util.StringUtils.isNull(oOld.toString(), "NULL");
                        }

                    }
                    if (oNew != null) {
                        sNewClassName = oNew.getClass().getName();
                        if (oNew.getClass().getName().indexOf("Byte") < 0) {
                            sNewString = hu.mgx.util.StringUtils.isNull(oNew.toString(), "NULL");
                        }

                    }
                    //appInterface.logLine(td.getFieldDefinition(i).getName() + " (" + sOldClassName + ") " + sOldString + " <> (" + sNewClassName + ") " + sNewString, LoggerInterface.LOG_DEBUG);
                    bChanged =
                            true;
                    if (!td.getFieldDefinition(i).isID()) {
                        if (td.getFieldDefinition(i).isKey()) {
                            bKeyChanged = true;
                        } // Kulcs mező változott: ellenőrizni a leendő rekordot duplikáltságra

                        sSet += (!sSet.equals("") ? ", " : "") + td.getFieldDefinition(i).getName() + "=?";
                        ++iOffsetField;
                    }

                }
                if (td.getFieldDefinition(i).isKey()) {
                    bHasKey = true;
                    sWhere +=
                            (!sWhere.equals("") ? " and " : "") + td.getFieldDefinition(i).getName() + "=?";
                }

            } catch (TableDefinitionException tde) {
                bSuccess = false;
                appInterface.handleError(tde);
            }

        }
        if (bChanged) {
            sSQLOriginExists = "select 1 from " + td.getName() + " where " + sWhere;
            debugStringOriginExists.sDebugString = sSQLOriginExists;
            int iTmp = 0;
            while (debugStringOriginExists.sDebugString.indexOf("?") > -1 && iTmp < 999) {
                debugStringOriginExists.sDebugString = hu.mgx.util.StringUtils.stringReplaceFirst(debugStringOriginExists.sDebugString, "?", "<" + Integer.toString(++iTmp) + ">");
            }

            sSQLNewNotExists = "select 1 from " + td.getName() + " where " + sWhere;
            debugStringNewNotExists.sDebugString = sSQLNewNotExists;
            iTmp = 0;
            while (debugStringNewNotExists.sDebugString.indexOf("?") > -1 && iTmp < 999) {
                debugStringNewNotExists.sDebugString = hu.mgx.util.StringUtils.stringReplaceFirst(debugStringNewNotExists.sDebugString, "?", "<" + Integer.toString(++iTmp) + ">");
            }

            sSQLUpdate = "update " + td.getName() + " set " + sSet + " where " + sWhere;
            debugStringUpdate.sDebugString = sSQLUpdate;
            iTmp = 0;
            while (debugStringUpdate.sDebugString.indexOf("?") > -1 && iTmp < 999) {
                debugStringUpdate.sDebugString = hu.mgx.util.StringUtils.stringReplaceFirst(debugStringUpdate.sDebugString, "?", "<" + Integer.toString(++iTmp) + ">");
            }

            //appInterface.logLine("sSQLOriginExists = " + sSQLOriginExists, LoggerInterface.LOG_DEBUG);
            //appInterface.logLine("sSQLNewNotExists = " + sSQLNewNotExists, LoggerInterface.LOG_DEBUG);
            //appInterface.logLine("sSQLUpdate = " + sSQLUpdate, iLogLevel);
            //appInterface.logLine("iOffsetField = " + Integer.toString(iOffsetField), LoggerInterface.LOG_DEBUG);
            try {
                PreparedStatement pstOriginExists = connection.prepareStatement(sSQLOriginExists, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                PreparedStatement pstNewNotExists = connection.prepareStatement(sSQLNewNotExists, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                PreparedStatement pstUpdate = connection.prepareStatement(sSQLUpdate, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                iCheckField =
                        0;
                iUpdateField =
                        0;
                for (int i = 0; i
                        < td.getFieldCount(); i++) {
                    iFieldType = td.getFieldDefinition(i).getType();
                    if (!td.getFieldDefinition(i).isID()) {
                        oOld = oldRecord.getFieldValue(i);
                        oNew = newRecord.getFieldValue(i);
                        if ((oOld == null && oNew != null) || (oOld != null && oNew == null) || (oOld != null && oNew != null && !oOld.toString().equals(oNew.toString()))) { // Mező értéke megváltozott
                            ++iUpdateField;
                            setPreparedStatementValues("pstUpdate set", pstUpdate, newRecord, iFieldType, iUpdateField, i, debugStringUpdate);
                        }

                    }
                    if (td.getFieldDefinition(i).isKey()) {
                        ++iCheckField;
                        setPreparedStatementValues("pstOriginExists", pstOriginExists, oldRecord, iFieldType, iCheckField, i, debugStringOriginExists);
                        setPreparedStatementValues("pstNewNotExists", pstNewNotExists, newRecord, iFieldType, iCheckField, i, debugStringNewNotExists);
                        setPreparedStatementValues("pstUpdate where", pstUpdate, oldRecord, iFieldType, iOffsetField + iCheckField, i, debugStringUpdate);
                    }

                }
                if (!bIDKey && bHasKey && bKeyChanged) {

                    //appInterface.logLine("sSQLNewNotExists = " + debugStringNewNotExists.sDebugString, iLogLevel);
                    rs = pstNewNotExists.executeQuery();
                    if (rs.next()) {
                        while (rs.next()) {
                            ;
                        }

                        rs.close();
                        appInterface.handleError("Létező rekordra nem lehet módosítani!");
                    } else {
                        //appInterface.logLine("sSQLOriginExists = " + debugStringOriginExists.sDebugString, iLogLevel);
                        rs = pstOriginExists.executeQuery();
                        if (rs.next()) {
                            while (rs.next()) {
                                ; //csak végigszaladunk a recordset-en ... már nem tudom, miért kellett ...
                            }

                            rs.close();
                            //appInterface.logLine("check OK. update mehet!", LoggerInterface.LOG_DEBUG);
                            appInterface.logLine(debugStringUpdate.sDebugString, iLogLevel);
                            pstUpdate.execute();
                        } else {
                            appInterface.handleError("Nem létező rekord, nem lehet módosítani!");
                        }

                    }
                } else {
                    //appInterface.logLine("sSQLOriginExists = " + debugStringOriginExists.sDebugString, iLogLevel);
                    rs = pstOriginExists.executeQuery();
                    if (rs.next()) {
                        while (rs.next()) {
                            ;
                        }

                        rs.close();
                        //appInterface.logLine("check OK. update mehet!", LoggerInterface.LOG_DEBUG);
                        appInterface.logLine(debugStringUpdate.sDebugString, iLogLevel);
                        pstUpdate.execute();
                    } else {
                        appInterface.handleError("Nem létező rekord, nem lehet módosítani!");
                    }

                }
            } catch (TableDefinitionException tde) {
                bSuccess = false;
                appInterface.handleError(tde);
            } catch (SQLException sqle) {
                bSuccess = false;
                appInterface.handleError(sqle, sSQLOriginExists + " " + sSQLNewNotExists + " " + sSQLUpdate);
            }

        }
        return (bSuccess);
    }

    public boolean delete(TableDefinition td, Record record, int iLogLevel) {
        boolean bSuccess = true;
        String sSQLCheck = "";
        DebugString debugStringCheck = new DebugString();
        String sSQLDelete = "";
        DebugString debugStringDelete = new DebugString();
        String sWhere = "";
        int iFieldType = hu.mgx.db.FieldType.NULL;
        int iField = 0;
        boolean bHasKey = false;
        for (int i = 0; i
                < td.getFieldCount(); i++) {
            try {
                if (td.getFieldDefinition(i).isKey()) {
                    bHasKey = true;
                    sWhere +=
                            (!sWhere.equals("") ? " and " : "") + td.getFieldDefinition(i).getName() + "=?";
                }

            } catch (TableDefinitionException tde) {
                bSuccess = false;
                appInterface.handleError(tde);
            }

        }
        if ((!bHasKey) && (sWhere.equals(""))) {
            sWhere = "1=0";
        }

        sSQLCheck = "select 1 from " + td.getName() + " where " + sWhere;
        debugStringCheck.sDebugString = sSQLCheck;
        int iTmp = 0;
        while (debugStringCheck.sDebugString.indexOf("?") > -1 && iTmp < 999) {
            debugStringCheck.sDebugString = hu.mgx.util.StringUtils.stringReplaceFirst(debugStringCheck.sDebugString, "?", "<" + Integer.toString(++iTmp) + ">");
        }

        sSQLDelete = "delete from " + td.getName() + " where " + sWhere;
        debugStringDelete.sDebugString = sSQLDelete;
        iTmp = 0;
        while (debugStringDelete.sDebugString.indexOf("?") > -1 && iTmp < 999) {
            debugStringDelete.sDebugString = hu.mgx.util.StringUtils.stringReplaceFirst(debugStringDelete.sDebugString, "?", "<" + Integer.toString(++iTmp) + ">");
        }

        //appInterface.logLine(sSQLDelete, LoggerInterface.LOG_DEBUG);
        //appInterface.logLine(sSQLCheck, LoggerInterface.LOG_DEBUG);
        try {
            PreparedStatement pstCheck = connection.prepareStatement(sSQLCheck, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            PreparedStatement pstDelete = connection.prepareStatement(sSQLDelete, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            iField =
                    0;
            for (int i = 0; i
                    < td.getFieldCount(); i++) {
                iFieldType = td.getFieldDefinition(i).getType();
                if (td.getFieldDefinition(i).isKey()) {
                    ++iField;
                    setPreparedStatementValues("pstCheck", pstCheck, record, iFieldType, iField, i, debugStringCheck);
                    setPreparedStatementValues("pstDelete", pstDelete, record, iFieldType, iField, i, debugStringDelete);
                }

            }
            if (bHasKey) {
                //appInterface.logLine("sSQLCheckDebug = " + debugStringCheck.sDebugString, iLogLevel);
                ResultSet rs = pstCheck.executeQuery();
                if (rs.next()) {
                    while (rs.next()) {
                        ;
                    }

                    rs.close();
                    appInterface.logLine(debugStringDelete.sDebugString, iLogLevel);
                    pstDelete.execute();
                } else {
                    appInterface.handleError("Nem létező rekord, nem lehet törölni!");
                }

            } else {
                appInterface.handleError("Nincsenek kulcsmezök, nem lehet törölni!");
            }

        } catch (TableDefinitionException tde) {
            bSuccess = false;
            appInterface.handleError(tde);
        } catch (SQLException sqle) {
            bSuccess = false;
            appInterface.handleError(sqle, sSQLCheck + " " + sSQLDelete);
        }

        return (bSuccess);
    }
}
