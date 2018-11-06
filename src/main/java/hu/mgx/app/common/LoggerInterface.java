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
    public static final int TYPE_DATED = 1; // log fájl név kiegészítése a dátummal, naponta új fájl készül
    public static final int TYPE_NUMBERED = 2; // log fájl sorszámozása, adott méret (sorszám?) felett új készül
    //??? régi log fájlok törlése (bizonyos napnál régebbi log fájlok törlõdnek - fájl dátum alapján, akkor is ha sorszámozott, akkor is ha dátumozott, és akkor is, ha egyik sem, de túl régi a korábbi futás!
    //??? "multi" log fájl, azaz több program írhat bele egyidõben (például hálózaton futtatott program) Ehhez már kellene egy folyamat azonosító (IP cím?)

    public abstract void logLine(String sLine);

    public abstract void logLine(String sLine, int iLogLevel);

    public abstract void logLine(String sLine, int iLogLevel, String sSearch);

    public abstract void setLogLevel(int iLogLevel);

    public abstract int getLogLevel();

    //van értelme menet közben megváltoztatni? - a létrehozáskor kell megadni! 
    public abstract void setLogType(int iLogType);

    public abstract void closeLog();
}
