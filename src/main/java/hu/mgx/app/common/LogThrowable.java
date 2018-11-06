package hu.mgx.app.common;

import java.io.*;

public class LogThrowable
{

    private Throwable throwable;
    private String localizedMessage = "";
    private String printStackTrace = "";

    public LogThrowable(Throwable throwable)
    {
        this.throwable = throwable;
        localizedMessage = throwable.getLocalizedMessage();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        throwable.printStackTrace(ps);
        ps.flush();
        try
        {
            baos.flush();
        }
        catch (IOException ioe)
        {
        }
        printStackTrace = new String(baos.toByteArray());
    }

    public String getLocalizedMessage()
    {
        return (localizedMessage);
    }

    public String getPrintStackTrace()
    {
        return (printStackTrace);
    }

    public Throwable getThrowable()
    {
        return (throwable);
    }
}
