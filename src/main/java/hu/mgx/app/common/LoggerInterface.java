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
    public static final int TYPE_DATED = 1; // log f�jl n�v kieg�sz�t�se a d�tummal, naponta �j f�jl k�sz�l
    public static final int TYPE_NUMBERED = 2; // log f�jl sorsz�moz�sa, adott m�ret (sorsz�m?) felett �j k�sz�l
    //??? r�gi log f�jlok t�rl�se (bizonyos napn�l r�gebbi log f�jlok t�rl�dnek - f�jl d�tum alapj�n, akkor is ha sorsz�mozott, akkor is ha d�tumozott, �s akkor is, ha egyik sem, de t�l r�gi a kor�bbi fut�s!
    //??? "multi" log f�jl, azaz t�bb program �rhat bele egyid�ben (p�ld�ul h�l�zaton futtatott program) Ehhez m�r kellene egy folyamat azonos�t� (IP c�m?)

    public abstract void logLine(String sLine);

    public abstract void logLine(String sLine, int iLogLevel);

    public abstract void logLine(String sLine, int iLogLevel, String sSearch);

    public abstract void setLogLevel(int iLogLevel);

    public abstract int getLogLevel();

    //van �rtelme menet k�zben megv�ltoztatni? - a l�trehoz�skor kell megadni! 
    public abstract void setLogType(int iLogType);

    public abstract void closeLog();
}
