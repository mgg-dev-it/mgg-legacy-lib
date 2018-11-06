package hu.mgx.app.console;

import hu.mag.db.DatabaseInfo;
import hu.mag.message.DefaultMessageHandler;
import hu.mag.message.MessageEvent;
import hu.mag.message.MessageEventListener;
import java.io.*;
import java.text.*;

import hu.mgx.app.common.*;
import hu.mgx.util.IntegerUtils;
import hu.mgx.util.StringUtils;
import java.sql.*;
import java.util.*;

public class ConsoleApp implements ConsoleAppInterface {

    private String sAppName = "";
    private DefaultErrorHandler defaultErrorHandler = null;
    private DefaultLogger defaultLogger = null;
    private DefaultConsole defaultConsole = new DefaultConsole();
    private DefaultAppFormat consoleAppFormat = new DefaultAppFormat();
    private DefaultLanguageHandler defaultLanguageHandler = null;
    private DefaultConnectionHandler defaultConnectionHandler = null;
    private DefaultMessageHandler defaultMessageHandler = null;
    private HashMap<String, Object> hmGlobals = new HashMap<String, Object>();
    private PermissionInterface permissionHandler = null;

    public ConsoleApp(String sAppName) {
        this(sAppName, TYPE_NORMAL);
    }

    public ConsoleApp(String sAppName, int iLogType) {
        this.sAppName = sAppName;
        defaultLogger = new DefaultLogger(sAppName, iLogType, this);
        defaultErrorHandler = new DefaultErrorHandler(defaultLogger);
        defaultLanguageHandler = new DefaultLanguageHandler();
        defaultConnectionHandler = new DefaultConnectionHandler(this);
        defaultMessageHandler = new DefaultMessageHandler();
    }

    //--- AppInterface
    public String getAppName() {
        return (sAppName);
    }

    //--- ErrorHandlerInterface
    public void handleError(Exception e) {
        defaultErrorHandler.handleError(e);
    }

    //--- ErrorHandlerInterface
    public void handleError(Exception e, int iErrorLevel) {
        defaultErrorHandler.handleError(e, iErrorLevel);
    }

    //--- ErrorHandlerInterface
    public void handleError(String sInfo) {
        defaultErrorHandler.handleError(sInfo);
    }

    //--- ErrorHandlerInterface
    public void handleError(String sInfo, int iErrorLevel) {
        defaultErrorHandler.handleError(sInfo, iErrorLevel);
    }

    //--- ErrorHandlerInterface
    public void handleError(Exception e, String sInfo) {
        defaultErrorHandler.handleError(e, sInfo);
    }

    //--- ErrorHandlerInterface
    public void handleError(Exception e, String sInfo, int iErrorLevel) {
        defaultErrorHandler.handleError(e, sInfo, iErrorLevel);
    }

    //--- LoggerInterface
    public void logLine(String sLine) {
        defaultLogger.logLine(sLine);
    }

    //--- LoggerInterface
    public void logLine(String sLine, int iLogLevel) {
        defaultLogger.logLine(sLine, iLogLevel);
    }

    //--- LoggerInterface
    public void logLine(String sLine, int iLogLevel, String sSearch) {
        defaultLogger.logLine(sLine, iLogLevel, sSearch);
    }

    //--- LoggerInterface
    public void setLogLevel(int iLogLevel) {
        defaultLogger.setLogLevel(iLogLevel);
    }

    //--- LoggerInterface
    public int getLogLevel() {
        return (defaultLogger.getLogLevel());
    }

    //--- LoggerInterface
    public void setLogType(int iLogType) {
        defaultLogger.setLogType(iLogType);
    }

    //--- LoggerInterface
    public void closeLog() {
        defaultLogger.closeLog();
    }

    //--- FormatInterface
    public char getDateSeparator() {
        return (consoleAppFormat.getDateSeparator());
    }

    //--- FormatInterface
    public void setDateSeparator(char cDateSeparator) {
        consoleAppFormat.setDateSeparator(cDateSeparator);
    }

    //--- FormatInterface
    public char getTimeSeparator() {
        return (consoleAppFormat.getTimeSeparator());
    }

    //--- FormatInterface
    public void setTimeSeparator(char cTimeSeparator) {
        consoleAppFormat.setTimeSeparator(cTimeSeparator);
    }

    //--- FormatInterface
    public String getDatePattern() {
        return (consoleAppFormat.getDatePattern());
    }

    //--- FormatInterface
    public void setDatePattern(String sDatePattern) {
        consoleAppFormat.setDatePattern(sDatePattern);
    }

    //--- FormatInterface
    public String getTimePattern() {
        return (consoleAppFormat.getTimePattern());
    }

    //--- FormatInterface
    public void setTimePattern(String sTimePattern) {
        consoleAppFormat.setTimePattern(sTimePattern);
    }

    //--- FormatInterface
    public String getDateTimePattern() {
        return (consoleAppFormat.getDateTimePattern());
    }

    //--- FormatInterface
    public SimpleDateFormat getDateFormat() {
        return (consoleAppFormat.getDateFormat());
    }

    //--- FormatInterface
    public SimpleDateFormat getTimeFormat() {
        return (consoleAppFormat.getTimeFormat());
    }

    //--- FormatInterface
    public SimpleDateFormat getDateTimeFormat() {
        return (consoleAppFormat.getDateTimeFormat());
    }

    //--- FormatInterface
    public SimpleDateFormat getClockDateTimeFormat() {
        return (consoleAppFormat.getClockDateTimeFormat());
    }

    //--- FormatInterface
    public SimpleDateFormat getLogDateTimeFormat() {
        return (consoleAppFormat.getLogDateTimeFormat());
    }

    //--- FormatInterface
    public SimpleDateFormat getSQLDateFormat() {
        return (consoleAppFormat.getSQLDateFormat());
    }

    //--- FormatInterface
    public SimpleDateFormat getSQLTimeFormat() {
        return (consoleAppFormat.getSQLTimeFormat());
    }

    //--- FormatInterface
    public SimpleDateFormat getSQLDateTimeFormat() {
        return (consoleAppFormat.getSQLDateTimeFormat());
    }

    //--- FormatInterface
    public SimpleDateFormat getFileNameDateTimeFormat() {
        return (consoleAppFormat.getFileNameDateTimeFormat());
    }

    //--- FormatInterface
    public char getDecimalSeparator() {
        return (consoleAppFormat.getDecimalSeparator());
    }

    //--- FormatInterface
    public void setDecimalSeparator(char cDecimalSeparator) {
        consoleAppFormat.setDecimalSeparator(cDecimalSeparator);
    }

    //--- FormatInterface
    public char getGroupingSeparator() {
        return (consoleAppFormat.getGroupingSeparator());
    }

    //--- FormatInterface
    public void setGroupingSeparator(char cGroupingSeparator) {
        consoleAppFormat.setGroupingSeparator(cGroupingSeparator);
    }

    //--- FormatInterface
    public DecimalFormat getDecimalFormat() {
        return (consoleAppFormat.getDecimalFormat());
    }

    //--- FormatInterface
    public DecimalFormat getDecimalFormat(int iScale) {
        return (consoleAppFormat.getDecimalFormat(iScale));
    }

    //--- ConsoleInterface
    public InputStream getInputStream() {
        return (defaultConsole.getInputStream());
    }

    //--- ConsoleInterface
    public PrintStream getOutputStream() {
        return (defaultConsole.getOutputStream());
    }

    //--- ConsoleInterface
    public PrintStream getErrorStream() {
        return (defaultConsole.getErrorStream());
    }

    //--- VersionInterface
    public int getMajor() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //--- VersionInterface
    public int getMinor() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //--- VersionInterface
    public int getRevision() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //--- VersionInterface
    public VersionHistory getVersionHistory() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //--- LanguageInterface
    public void clearLanguages() {
        defaultLanguageHandler.clearLanguages();
    }

    //--- LanguageInterface
    public void setLanguageSubstitute(boolean bSubstitute) {
        defaultLanguageHandler.setLanguageSubstitute(bSubstitute);
    }

    //--- LanguageInterface
    public boolean isLanguageSubstitute() {
        return (defaultLanguageHandler.isLanguageSubstitute());
    }

    //--- LanguageInterface
    //--- LanguageInterface
    public void setMarkUndefined(boolean bMarkUndefined) {
        defaultLanguageHandler.setMarkUndefined(bMarkUndefined);
    }

    //--- LanguageInterface
    public boolean isMarkUndefined() {
        return (defaultLanguageHandler.isMarkUndefined());
    }

    public boolean addLanguage(String sLanguage) {
        return (defaultLanguageHandler.addLanguage(sLanguage));
    }

    //--- LanguageInterface
    public boolean setLanguage(String sLanguage) {
        return (defaultLanguageHandler.setLanguage(sLanguage));
    }

    //--- LanguageInterface
    public String getLanguage() {
        return (defaultLanguageHandler.getLanguage());
    }

    //--- LanguageInterface
    public Vector<String> getAvailableLanguages() {
        return (defaultLanguageHandler.getAvailableLanguages());
    }

    //--- LanguageInterface
    public void setLanguageString(int iKey, String sValue) {
        defaultLanguageHandler.setLanguageString(iKey, sValue);
    }

    //--- LanguageInterface
    public void setLanguageString(String sLanguage, int iKey, String sValue) {
        defaultLanguageHandler.setLanguageString(sLanguage, iKey, sValue);
    }

    //--- LanguageInterface
    public String getLanguageString(int iKey) {
        return (defaultLanguageHandler.getLanguageString(iKey));
    }

    //--- LanguageInterface
    public String getLanguageString(int iKey, String sDefaultValue) {
        return (defaultLanguageHandler.getLanguageString(iKey, sDefaultValue));
    }

    //--- LanguageInterface
    public String getLanguageString(String sLanguage, int iKey, String sDefaultValue) {
        return (defaultLanguageHandler.getLanguageString(sLanguage, iKey, sDefaultValue));
    }

    public void setLanguageString(String sKey, String sValue) {
        defaultLanguageHandler.setLanguageString(sKey, sValue);
    }

    public void setLanguageString(String sLanguage, String sKey, String sValue) {
        defaultLanguageHandler.setLanguageString(sLanguage, sKey, sValue);
    }

    public String getLanguageString(String sKey) {
        return (defaultLanguageHandler.getLanguageString(sKey));
    }

    public String getLanguageString(String sKey, String sDefaultValue) {
        return (defaultLanguageHandler.getLanguageString(sKey, sDefaultValue));
    }

    public String getLanguageString(String sLanguage, String sKey, String sDefaultValue) {
        return (defaultLanguageHandler.getLanguageString(sLanguage, sKey, sDefaultValue));
    }

    public void setLanguageStringXML(String sXML) {
        defaultLanguageHandler.setLanguageStringXML(sXML);
    }

    //--- LanguageInterface
    public void setEnabledLanguages(String sEnabledLanguages) {
        defaultLanguageHandler.setEnabledLanguages(sEnabledLanguages);
    }

    //--- ConnectionHandlerInterface
    @Override
    public boolean createConnection(String sName, String sDriver, String sURL, String sUserid, String sPassword) {
        return (defaultConnectionHandler.createConnection(sName, sDriver, sURL, sUserid, sPassword));
    }

    //--- ConnectionHandlerInterface
    @Override
    public boolean createConnection(String sName, String sDriver, String sURL, String sUserid, String sPassword, String sDBMSType) {
        return (defaultConnectionHandler.createConnection(sName, sDriver, sURL, sUserid, sPassword, sDBMSType));
    }

    //--- ConnectionHandlerInterface
    @Override
    public boolean createConnection(String sName, String sDriver, String sURL, String sUserid, String sPassword, int iDBMSType) {
        return (defaultConnectionHandler.createConnection(sName, sDriver, sURL, sUserid, sPassword, iDBMSType));
    }

    //--- ConnectionHandlerInterface
    @Override
    public boolean connect(String sName) {
        return (defaultConnectionHandler.connect(sName));
    }

    //--- ConnectionHandlerInterface
    @Override
    public Connection getConnection(String sName) {
        return (defaultConnectionHandler.getConnection(sName));
    }

    //--- ConnectionHandlerInterface
    @Override
    public Connection getTemporaryConnection(String sName) {
        return (defaultConnectionHandler.getTemporaryConnection(sName));
    }

    //--- ConnectionHandlerInterface
    @Override
    public int getDBMSType(String sName) {
        return (defaultConnectionHandler.getDBMSType(sName));
    }

    //--- ConnectionHandlerInterface
    @Override
    public boolean disconnect(String sName) {
        return (defaultConnectionHandler.disconnect(sName));
    }

    //--- ConnectionHandlerInterface
    @Override
    public void setDatabaseInfo(String sName, DatabaseInfo di) {
        defaultConnectionHandler.setDatabaseInfo(sName, di);
    }

    //--- ConnectionHandlerInterface
    @Override
    public DatabaseInfo getDatabaseInfo(String sName) {
        return (defaultConnectionHandler.getDatabaseInfo(sName));
    }

    @Override
    public Exception getConnectionLastException() {
        return (defaultConnectionHandler.getConnectionLastException());
    }

    @Override
    public Vector<String> getConnectionNames() {
        return (defaultConnectionHandler.getConnectionNames());
    }

    @Override
    public boolean checkConnection(String sName) {
        return (defaultConnectionHandler.checkConnection(sName));
    }

    //--- MessageHandlerInterface
    @Override
    public void handleMessage(MessageEvent e) {
        defaultMessageHandler.handleMessage(e);
    }

    //--- MessageHandlerInterface
    @Override
    public void addMessageEventListener(MessageEventListener mel) {
        defaultMessageHandler.addMessageEventListener(mel);
    }

    //--- MessageHandlerInterface
    @Override
    public void removeMessageEventListener(MessageEventListener mel) {
        defaultMessageHandler.removeMessageEventListener(mel);
    }

    @Override
    public void setGlobal(String sKey, Object oValue) {
        hmGlobals.put(sKey, oValue);
    }

    @Override
    public Object getGlobal(String sKey) {
        return (hmGlobals.get(sKey));
    }

    @Override
    public String getGlobalString(String sKey) {
        return (StringUtils.isNull(hmGlobals.get(sKey), ""));
    }

    @Override
    public String getGlobalStringIsNull(String sKey, String sIfNull) {
        return (StringUtils.isNull(hmGlobals.get(sKey), sIfNull));
    }

    @Override
    public Integer getGlobalInteger(String sKey) {
        return (IntegerUtils.convertToInteger(hmGlobals.get(sKey)));
    }

    public void setPermissionHandler(PermissionInterface pi) {
        this.permissionHandler = pi;
    }

    public PermissionInterface getPermissionHandler() {
        return (permissionHandler);
    }

    //--- PermissionInterface
    @Override
    public Object getPermissionUserIdentifier() {
        if (permissionHandler != null) {
            return (permissionHandler.getPermissionUserIdentifier());
        }
        return (null);
    }

    //--- PermissionInterface
    @Override
    public Object getPermissionUserName() {
        if (permissionHandler != null) {
            return (permissionHandler.getPermissionUserName());
        }
        return (null);
    }

    //--- PermissionInterface
    @Override
    public Object getPermissionUserDisplayName() {
        if (permissionHandler != null) {
            return (permissionHandler.getPermissionUserDisplayName());
        }
        return (null);
    }

    //--- PermissionInterface
    @Override
    public boolean permissionLogin(Object oUserID, Object oPassword) {
        if (permissionHandler != null) {
            return (permissionHandler.permissionLogin(oUserID, oPassword));
        }
        return (false);
    }

    //--- PermissionInterface
    @Override
    public boolean permissionLogout() {
        if (permissionHandler != null) {
            return (permissionHandler.permissionLogout());
        }
        return (false);
    }

    //--- PermissionInterface
    @Override
    public boolean hasPermission(Object oPermission) {
        if (permissionHandler != null) {
            return (permissionHandler.hasPermission(oPermission));
        }
        return (false);
    }

}
