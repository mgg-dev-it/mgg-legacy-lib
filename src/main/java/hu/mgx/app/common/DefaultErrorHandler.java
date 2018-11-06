package hu.mgx.app.common;

import hu.mgx.app.console.*;
import java.io.*;

public class DefaultErrorHandler implements ErrorHandlerInterface
{

    private ConsoleInterface consoleInterface = null;
    private LoggerInterface loggerInterface = null;

    public DefaultErrorHandler()
    {
        this(new DefaultConsole());
    }

    public DefaultErrorHandler(ConsoleInterface consoleInterface)
    {
        this.consoleInterface = consoleInterface;
    }

    public DefaultErrorHandler(LoggerInterface loggerInterface)
    {
        this.loggerInterface = loggerInterface;
    }

    public void handleError(Exception e)
    {
        this.handleError(e, null, 0);
    }

    public void handleError(Exception e, int iErrorLevel)
    {
        this.handleError(e, null, iErrorLevel);
    }

    public void handleError(String sInfo)
    {
        this.handleError(null, sInfo, 0);
    }

    public void handleError(String sInfo, int iErrorLevel)
    {
        this.handleError(null, sInfo, iErrorLevel);
    }

    public void handleError(Exception e, String sInfo)
    {
        this.handleError(e, sInfo, 0);
    }

    public void handleError(Exception e, String sInfo, int iErrorLevel)
    {
        String sErrMsg = "";
        if (sInfo == null)
        {
            sInfo = "";
        }

        if (e != null)
        {
            sErrMsg = e.getLocalizedMessage();
            if (loggerInterface != null)
            {
                loggerInterface.logLine(sErrMsg, LoggerInterface.LOG_ERROR);
            }
            if (consoleInterface != null)
            {
                consoleInterface.getErrorStream().println(sErrMsg);
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            PrintStream errorStream = new PrintStream(byteArrayOutputStream);
            e.printStackTrace(errorStream);
            if (loggerInterface != null)
            {
                loggerInterface.logLine(byteArrayOutputStream.toString(), LoggerInterface.LOG_ERROR);
            }
            if (consoleInterface != null)
            {
                consoleInterface.getErrorStream().println(byteArrayOutputStream.toString());
            }
        }
        if (sInfo.length() > 0)
        {
            if (loggerInterface != null)
            {
                loggerInterface.logLine(sInfo, LoggerInterface.LOG_ERROR);
            }
            if (consoleInterface != null)
            {
                consoleInterface.getErrorStream().println(sInfo);
            }
        }
        if (iErrorLevel > 8)
        {
            System.exit(-1);
        }
    }
}
