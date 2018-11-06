/*
 * Connection kezelõ interfész.
 * Egyszerre több connection-t is tud kezelni, a hozzájuk rendelt logikai nevek alapján.
 *
 *
 */
package hu.mgx.app.common;

import hu.mag.db.DatabaseInfo;

public interface ConnectionHandlerInterface {

    public final static int DBMS_DEFAULT = 0;
    public final static int DBMS_MSSQL = 1;
    public final static int DBMS_MYSQL = 2;
    public final static int DBMS_JAVADB = 3;
    public final static int DBMS_JPROXY = 4;

    public abstract boolean createConnection(String sName, String sDriver, String sURL, String sUserid, String sPassword);

    public abstract boolean createConnection(String sName, String sDriver, String sURL, String sUserid, String sPassword, String sDBMSType);

    public abstract boolean createConnection(String sName, String sDriver, String sURL, String sUserid, String sPassword, int iDBMSType);

    public abstract boolean connect(String sName);

    public abstract java.sql.Connection getConnection(String sName);

    public abstract java.sql.Connection getTemporaryConnection(String sName);

    public abstract int getDBMSType(String sName);

    public abstract boolean disconnect(String sName);

    public abstract void setDatabaseInfo(String sName, DatabaseInfo di);

    public abstract DatabaseInfo getDatabaseInfo(String sName);

    public abstract Exception getConnectionLastException();

    public abstract java.util.Vector<String> getConnectionNames();

    public abstract boolean checkConnection(String sName);
}
