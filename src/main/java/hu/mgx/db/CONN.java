package hu.mgx.db;

import hu.mgx.app.common.DefaultErrorHandler;
import hu.mgx.app.common.ErrorHandlerInterface;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CONN {

    private Connection conn = null;
    private String sDriver = "";
    private String sURL = "";
    private String sUserid = "";
    private String sPassword = "";
    private boolean bError = false;
    private boolean bAnyError = false;
    private boolean bLogError = false;
    private String sErrorMsg = "";
    private Exception lastException = null;
    private boolean bMySQL = false;
    private boolean bMsSQL = false;
    private DBMS dbms = null;
    private ErrorHandlerInterface errorHandlerInterface;

    public CONN(Connection conn) {
        this("", "", "", "");
        this.conn = conn;
    }

    public CONN(String sDriver, String sURL, String sUserid, String sPassword) {
        this(sDriver, sURL, sUserid, sPassword, DBMS.DEFAULT);
    }

    public CONN(String sDriver, String sURL, String sUserid, String sPassword, ErrorHandlerInterface errorHandlerInterface) {
        this(sDriver, sURL, sUserid, sPassword, DBMS.DEFAULT, errorHandlerInterface);
    }

    public CONN(String sDriver, String sURL, String sUserid, String sPassword, int iDBMS) {
        this(sDriver, sURL, sUserid, sPassword, iDBMS, new DefaultErrorHandler());
    }

    public CONN(String sDriver, String sURL, String sUserid, String sPassword, int iDBMS, ErrorHandlerInterface errorHandlerInterface) {
        bError = false;
        bAnyError = false;
        bLogError = false;
        lastException = null;
        this.sDriver = sDriver;
        this.sURL = sURL;
        this.sUserid = sUserid;
        this.sPassword = sPassword;
        dbms = new DBMS(iDBMS);
        this.errorHandlerInterface = errorHandlerInterface;
    }

    public String getDriver() {
        return (sDriver);
    }

    public String getURL() {
        return (sURL);
    }

    public String getUserid() {
        return (sUserid);
    }

    public String getPassword() {
        return (sPassword);
    }

    public DBMS getDBMS() {
        return (dbms);
    }

    public boolean getError() {
        return (bError);
    }

    public boolean getAnyError() {
        return (bAnyError);
    }

    public void setLogError() {
        bLogError = true;
    }

    public String getErrorMsg() {
        return (sErrorMsg);
    }

    public Exception getLastException() {
        return (lastException);
    }

    public boolean connect() {
        bError = false;
        try {
            Class.forName(sDriver);
        } catch (Exception e) {
            bError = true;
            bAnyError = true;
            lastException = e;
            errorHandlerInterface.handleError(e);
            //System.err.println("Class.forName error: " + e.getLocalizedMessage());
            //e.printStackTrace(System.err);
            return (false);
        }
        try {
            conn = DriverManager.getConnection(sURL, sUserid, sPassword);
        } catch (Exception e) {
            bError = true;
            bAnyError = true;
            lastException = e;
            errorHandlerInterface.handleError(e);
            //System.err.println("DriverManager.getConnection error: " + e.getLocalizedMessage());
            //e.printStackTrace(System.err);
            return (false);
        }
        return (true);
    }

    public boolean connectIfNeeded() {
        if (conn == null) {
            return (connect());
        } else {
            return (true);
        }
    }

    public boolean disconnect() {
        bError = false;
        try {
            if (conn != null) {
                if (!conn.isClosed()) {
                    conn.close();
                }
                conn = null;
            }
        } catch (Exception e) {
            bError = true;
            bAnyError = true;
            lastException = e;
            errorHandlerInterface.handleError(e);
            //System.err.println("Disconnecting error: " + e.getLocalizedMessage());
            //e.printStackTrace(System.err);
            return (false);
        }
        return (true);
    }

    public Connection getConnection() {
        return (conn);
    }

    public void setMySQL(boolean b) {
        bMySQL = b;
    }

    public boolean isMySQL() {
        return (bMySQL);
    }

    public void setMsSQL(boolean b) {
        bMsSQL = b;
    }

    public boolean isMsSQL() {
        return (bMsSQL);
    }

    public String executeSQL(String sSql, String sCaller) {
        bError = false;
        try {
            PreparedStatement st = conn.prepareStatement(sSql);
            st.execute();
        } catch (Exception e) {
            if (bLogError) {
                errorHandlerInterface.handleError(e);
                //System.err.println(e.getLocalizedMessage());
                //e.printStackTrace(System.err);
            }
            bError = true;
            bAnyError = true;
            lastException = e;
            sErrorMsg = sCaller + " error: " + e.getLocalizedMessage() + " [" + sSql + "]";
            return (sErrorMsg);
        }
        return ("");
    }

    public boolean executeSQL(String sSql) {
        try {
            conn.prepareStatement(sSql).execute();
        } catch (SQLException sqle) {
            errorHandlerInterface.handleError(sqle);
            return (false);
        }
        return (true);
    }

    public boolean beginTransaction() {
        try {
            conn.setAutoCommit(false);
            return (true);
        } catch (SQLException sqle) {
            errorHandlerInterface.handleError(sqle);
        }
        return (false);
    }

    public boolean commitTransaction() {
        try {
            conn.commit();
            conn.setAutoCommit(true);
            return (true);
        } catch (SQLException sqle) {
            errorHandlerInterface.handleError(sqle);
        }
        return (false);
    }

    public boolean rollbackTransaction() {
        try {
            conn.rollback();
            conn.setAutoCommit(true);
            return (true);
        } catch (SQLException sqle) {
            errorHandlerInterface.handleError(sqle);
        }
        return (false);
    }
}
