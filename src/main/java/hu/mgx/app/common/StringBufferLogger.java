package hu.mgx.app.common;

public class StringBufferLogger implements LoggerInterface {

    private StringBuffer sbLog = null;
    private int iLogLevel = LOG_INFO;

    public StringBufferLogger() {
        init();
    }

    private void init() {
        sbLog = new StringBuffer();
    }

    public void logLine(String sLine) {
        logLine(sLine, LOG_INFO);
    }

    public void logLine(String sLine, int iLogLevel) {
        logLine(sLine, LOG_INFO, "");
    }

    public void logLine(String sLine, int iLogLevel, String sSearch) {
        if (this.iLogLevel > iLogLevel) {
            return;
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
            default:
                sLevel = "INFO";
                break;
        }
        sbLog.append("[").append(sLevel).append("] ").append(sLine).append("\n");
    }

    public void setLogLevel(int iLogLevel) {
        this.iLogLevel = iLogLevel;
    }

    public int getLogLevel() {
        return (iLogLevel);
    }

    public void setLogType(int iLogType) {
    }

    public void closeLog() {
    }

    public String getLogString() {
        return (sbLog.toString());
    }

    public StringBuffer getLogStringBuffer() {
        return (sbLog);
    }
}
