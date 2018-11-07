package hu.mgx.app.common;

public interface LoggerInterface {

    public final static int LOG_DEBUG = 1;
    public final static int LOG_INFO = 2; //default
    public final static int LOG_WARN = 3;
    public final static int LOG_ERROR = 4;
    public final static int LOG_FATAL = 5;
    public final static int LOG_SQL = 100;
    //
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_DATED = 1;
    public static final int TYPE_NUMBERED = 2;

    public abstract void logLine(String sLine);

    public abstract void logLine(String sLine, int iLogLevel);

    public abstract void logLine(String sLine, int iLogLevel, String sSearch);

    public abstract void setLogLevel(int iLogLevel);

    public abstract int getLogLevel();

    //van �rtelme menet k�zben megv�ltoztatni? - a l�trehoz�skor kell megadni! 
    public abstract void setLogType(int iLogType);

    public abstract void closeLog();
}
