package hu.mgx.app.common;

import hu.mgx.app.console.*;
import java.io.*;
import java.text.*;

public class DefaultLogger implements LoggerInterface {

    private int iLogLevelLimit = LOG_INFO;
    private int iLogType = TYPE_NORMAL;
    private PrintWriter logWriter = null;
    private ConsoleInterface consoleInterface = null;
    private SimpleDateFormat logDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS");
    private SimpleDateFormat fileDateFormat = new SimpleDateFormat("yyyyMMdd");
    private String sFilename = null;
    private String sFilenameEnd = null;
    private int iDeleteFilesOlderThanDays = 0; // ??? régi log fájlok törlése (bizonyos napnál régebbi log fájlok törlõdnek - fájl dátum alapján, akkor is ha sorszámozott, akkor is ha dátumozott, és akkor is, ha egyik sem, de túl régi a korábbi futás!

    public DefaultLogger(String sFilename) {
        this(sFilename, TYPE_NORMAL);
    }

    public DefaultLogger(String sFilename, int iLogType) {
        this(sFilename, iLogType, null);
    }

    public DefaultLogger(String sFilename, int iLogType, ConsoleInterface consoleInterface) {
        this.sFilename = sFilename;
        this.iLogType = iLogType;
        this.consoleInterface = consoleInterface;
        this.sFilenameEnd = "";
        init();
    }

    private void init() {
        if (iLogType == TYPE_DATED) {
            sFilenameEnd = "_" + fileDateFormat.format(new java.util.Date());
        }
        createLogFile();
    }

    private void createLogFile() {
        try {
            logWriter = new PrintWriter(new FileOutputStream(sFilename + sFilenameEnd + ".log", true));
        } catch (FileNotFoundException e) {
            System.err.println(e.getLocalizedMessage());
            e.printStackTrace(System.err);
        }
    }

    public void logLine(String sLine) {
        logLine(sLine, LOG_INFO);
    }

    public void logLine(String sLine, int iLogLevel) {
        logLine(sLine, iLogLevel, "");
    }

    public void logLine(String sLine, int iLogLevel, String sSearch) {
        String sLogLine = "";
        if ((iLogLevel != LOG_SQL) && (iLogLevel < this.iLogLevelLimit)) {
            return;
        }

        if (iLogType == TYPE_DATED) {
            if (!sFilenameEnd.equals("_" + fileDateFormat.format(new java.util.Date()))) {
                logWriter.close();
                sFilenameEnd = "_" + fileDateFormat.format(new java.util.Date());
                createLogFile();
            }
        }

        String sLevel = "";
        switch (iLogLevel) {
            case LOG_DEBUG:
                sLevel = "DEBUG";
                break;
            case LOG_INFO:
                sLevel = "INFO";
                break;
            case LOG_WARN:
                sLevel = "WARN";
                break;
            case LOG_ERROR:
                sLevel = "ERROR";
                break;
            case LOG_FATAL:
                sLevel = "FATAL";
                break;
            case LOG_SQL:
                sLevel = "SQL";
                break;
            default:
                sLevel = "UNKNOWN";
                break;
        }
        sLogLine = "[" + logDateFormat.format(new java.util.Date()) + "] [" + sLevel + "] " + sLine;
        logWriter.println(sLogLine);
        logWriter.flush();
        if (consoleInterface != null) {
            if (iLogLevel >= LOG_INFO) {
                consoleInterface.getOutputStream().println(sLogLine);
            }
        }
    }

    public void setLogLevel(int iLogLevel) {
        this.iLogLevelLimit = iLogLevel;
    }

    public int getLogLevel() {
        return (iLogLevelLimit);
    }

    public void setLogType(int iLogType) {
        this.iLogType = iLogType;
    }

    public void closeLog() {
        logWriter.close();
    }
}
