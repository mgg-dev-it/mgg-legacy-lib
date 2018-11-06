package hu.mgx.app.common;

public interface TimerLoggerInterface
{

    public abstract void startTimer(String sName);

    public abstract void startPartTimer(String sName);

    public abstract void stopPartTimer(String sName);

    public abstract void stopTimer(String sName);

    public abstract void writeToLog();
}
