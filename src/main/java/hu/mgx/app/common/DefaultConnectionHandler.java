package hu.mgx.app.common;

import hu.mag.db.DatabaseInfo;
import hu.mgx.swing.table.*;
import hu.mgx.util.StringUtils;

import java.sql.*;
import java.util.*;

public class DefaultConnectionHandler implements ConnectionHandlerInterface {

    private HashMap<String, Integer> connectionIndexMap = null;
    private HashMap<String, Connection> connectionMap = null;
    private ErrorHandlerInterface errorHandlerInterface = null;
    private MemoryTable mtConnections = new MemoryTable(new String[]{"name", "driver", "url", "userid", "password", "type"});
    private HashMap<String, DatabaseInfo> dbInfoMap = null;
    private Exception lastException = null;

    public DefaultConnectionHandler(ErrorHandlerInterface errorHandlerInterface) {
        this.errorHandlerInterface = errorHandlerInterface;
        connectionIndexMap = new HashMap<String, Integer>();
        connectionMap = new HashMap<String, Connection>();
        dbInfoMap = new HashMap<String, DatabaseInfo>();
    }

    @Override
    public boolean createConnection(String sName, String sDriver, String sURL, String sUserid, String sPassword) {
        return (createConnection(sName, sDriver, sURL, sUserid, sPassword, DBMS_DEFAULT));
    }

    @Override
    public boolean createConnection(String sName, String sDriver, String sURL, String sUserid, String sPassword, String sDBMSType) {
        int iDBMSType = DBMS_DEFAULT;
        if (sDBMSType.equalsIgnoreCase("mssql")) {
            iDBMSType = DBMS_MSSQL;
        }
        if (sDBMSType.equalsIgnoreCase("mysql")) {
            iDBMSType = DBMS_MYSQL;
        }
        if (sDBMSType.equalsIgnoreCase("javadb")) {
            iDBMSType = DBMS_JAVADB;
        }
        if (sDBMSType.equalsIgnoreCase("jproxy")) {
            iDBMSType = DBMS_JPROXY;
        }
        return (createConnection(sName, sDriver, sURL, sUserid, sPassword, iDBMSType));
    }

    @Override
    public boolean createConnection(String sName, String sDriver, String sURL, String sUserid, String sPassword, int iDBMSType) {
        if (connectionIndexMap.containsKey(sName)) {
            return (false);
        }
        mtConnections.addRow();
        mtConnections.setValueAt(sName, mtConnections.getRowCount() - 1, "name");
        mtConnections.setValueAt(sDriver, mtConnections.getRowCount() - 1, "driver");
        mtConnections.setValueAt(sURL, mtConnections.getRowCount() - 1, "url");
        mtConnections.setValueAt(sUserid, mtConnections.getRowCount() - 1, "userid");
        mtConnections.setValueAt(sPassword, mtConnections.getRowCount() - 1, "password");
        mtConnections.setValueAt(new Integer(iDBMSType), mtConnections.getRowCount() - 1, "type");
        connectionIndexMap.put(sName, new Integer(mtConnections.getRowCount()));
        return (true);
    }

    @Override
    public boolean connect(String sName) {
        Integer integerIndex = null;
        String sDriver = "";
        String sURL = "";
        String sUserid = "";
        String sPassword = "";
//        Integer iType;
        Connection connection = null;
        String sRootName = sName;
        String sParameter = "";
        String[] sParameterArray = null;

        if (sName.contains("|")) {
            sParameter = sName.substring(sName.indexOf("|") + 1, sName.length());
            sParameterArray = sParameter.split("\\|");
            sRootName = sName.substring(0, sName.indexOf("|"));
        }

        integerIndex = connectionIndexMap.get(sRootName);
        if (integerIndex == null) {
            return (false);
        }

        //@todo task : check, whether connection exists in connectionMap - maybe don't need to connect again when someone call directly this method
        sDriver = mtConnections.getValueAt(integerIndex.intValue() - 1, "driver").toString();
        sURL = mtConnections.getValueAt(integerIndex.intValue() - 1, "url").toString();
        sUserid = mtConnections.getValueAt(integerIndex.intValue() - 1, "userid").toString();
        sPassword = mtConnections.getValueAt(integerIndex.intValue() - 1, "password").toString();
//        iType = (Integer) mtConnections.getValueAt(integerIndex.intValue() - 1, "type");

        if (sParameterArray != null) {
            for (int i = 0; i < sParameterArray.length; i++) {
                sDriver = StringUtils.stringReplace(sDriver, "%" + Integer.toString(i + 1), sParameterArray[i]);
                sURL = StringUtils.stringReplace(sURL, "%" + Integer.toString(i + 1), sParameterArray[i]);
                sUserid = StringUtils.stringReplace(sUserid, "%" + Integer.toString(i + 1), sParameterArray[i]);
                sPassword = StringUtils.stringReplace(sPassword, "%" + Integer.toString(i + 1), sParameterArray[i]);
            }
        }

        //MaG 2018.02.08.
        if (sURL.contains("%")) {
            return (false);
        }

//        if (!sDriver.equalsIgnoreCase("org.apache.derby.jdbc.EmbeddedDriver")) {
        try {
            Class.forName(sDriver);
        } catch (ClassNotFoundException cnfe) {
            lastException = cnfe;
            errorHandlerInterface.handleError(cnfe);
            return (false);
        }
//        }

        try {
            connection = DriverManager.getConnection(sURL, sUserid, sPassword);
            connectionMap.put(sName, connection);
        } catch (SQLException sqle) {
            lastException = sqle;
            errorHandlerInterface.handleError(sqle, sqle.getSQLState());
            return (false);
        }
        return (true);
    }

    @Override
    public Connection getTemporaryConnection(String sName) {
        Integer integerIndex = null;
        String sDriver = "";
        String sURL = "";
        String sUserid = "";
        String sPassword = "";
        Connection connection = null;
        String sRootName = sName;
        String sParameter = "";
        String[] sParameterArray = null;

        if (sName.contains("|")) {
            sParameter = sName.substring(sName.indexOf("|") + 1, sName.length());
            sParameterArray = sParameter.split("\\|");
            sRootName = sName.substring(0, sName.indexOf("|"));
        }

        integerIndex = connectionIndexMap.get(sRootName);
        if (integerIndex == null) {
            return (connection);
        }

        //@todo task : check, whether connection exists in connectionMap - maybe don't need to connect again when someone call directly this method
        sDriver = mtConnections.getValueAt(integerIndex.intValue() - 1, "driver").toString();
        sURL = mtConnections.getValueAt(integerIndex.intValue() - 1, "url").toString();
        sUserid = mtConnections.getValueAt(integerIndex.intValue() - 1, "userid").toString();
        sPassword = mtConnections.getValueAt(integerIndex.intValue() - 1, "password").toString();

        if (sParameterArray != null) {
            for (int i = 0; i < sParameterArray.length; i++) {
                sDriver = StringUtils.stringReplace(sDriver, "%" + Integer.toString(i + 1), sParameterArray[i]);
                sURL = StringUtils.stringReplace(sURL, "%" + Integer.toString(i + 1), sParameterArray[i]);
                sUserid = StringUtils.stringReplace(sUserid, "%" + Integer.toString(i + 1), sParameterArray[i]);
                sPassword = StringUtils.stringReplace(sPassword, "%" + Integer.toString(i + 1), sParameterArray[i]);
            }
        }

        //MaG 2018.02.08.
        if (sURL.contains("%")) {
            return (connection);
        }

//        if (!sDriver.equalsIgnoreCase("org.apache.derby.jdbc.EmbeddedDriver")) {
        try {
            Class.forName(sDriver);
        } catch (ClassNotFoundException cnfe) {
            lastException = cnfe;
            errorHandlerInterface.handleError(cnfe);
            return (connection);
        }
//        }

        try {
            connection = DriverManager.getConnection(sURL, sUserid, sPassword);
            //ezt nem!!! connectionMap.put(sName, connection);
        } catch (SQLException sqle) {
            lastException = sqle;
            errorHandlerInterface.handleError(sqle, sqle.getSQLState());
            return (connection);
        }

        return (connection);
    }

    @Override
    public Connection getConnection(String sName) {
        //MAG 2015.03.23.
        if (connectionMap.get(sName) == null) {
            this.connect(sName);
        } 
        else {
            try {
                if (!connectionMap.get(sName).isValid(0)) {
                    this.connect(sName);
                }
                if (connectionMap.get(sName).isClosed()) {
                    this.connect(sName);
                }
            } catch (SQLException sqle) {
            }
        }
        return (connectionMap.get(sName));
    }

    @Override
    public int getDBMSType(String sName) {
        Integer integerIndex = connectionIndexMap.get(sName);
        if (integerIndex == null) {
            return (DBMS_DEFAULT);
        }
        return (hu.mgx.util.StringUtils.intValue(mtConnections.getValueAt(integerIndex.intValue() - 1, "type").toString()));

    }

    @Override
    public boolean disconnect(String sName) {
        Connection connection = connectionMap.get(sName);
        connectionMap.remove(sName); //2017.04.12.
        try {
            if (connection != null) {
                if (!connection.isClosed()) {
                    connection.close();
                }
                connection = null;
            }
        } catch (SQLException sqle) {
            lastException = sqle;
            errorHandlerInterface.handleError(sqle);
            return (false);
        }
        return (true);
    }

    @Override
    public void setDatabaseInfo(String sName, DatabaseInfo di) {
        dbInfoMap.put(sName, di);
    }

    @Override
    public DatabaseInfo getDatabaseInfo(String sName) {
        return (dbInfoMap.get(sName));
    }

    @Override
    public Exception getConnectionLastException() {
        return (lastException);
    }

    @Override
    public Vector<String> getConnectionNames() {
        Vector<String> v = new Vector<String>();
        for (int i = 0; i < mtConnections.getRowCount(); i++) {
            v.add(mtConnections.getValueAt(i, "name").toString());
        }
        return (v);
    }

    @Override
    public boolean checkConnection(String sName) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sSQL = null;

        sSQL = "select 'connected' as retval";

        try {
            connection = this.getConnection(sName);
            if (connection == null) {
                return (false);
            }

            if (connectionIndexMap.get(sName) != null) {
                if (mtConnections.getRow(connectionIndexMap.get(sName).intValue() - 1).elementAt(1).toString().equalsIgnoreCase("org.apache.derby.jdbc.EmbeddedDriver")) {
                    sSQL = "select 'connected' as retval from sysibm.sysdummy1";
                }
            }

            ps = connection.prepareStatement(sSQL);
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getString("retval").equalsIgnoreCase("connected")) {
                    return (true);
                }
            }
        } catch (SQLException sqle) {
            errorHandlerInterface.handleError(sqle);
        }
        return (false);
    }

}
