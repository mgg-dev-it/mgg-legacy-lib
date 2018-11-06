package hu.mgx.app.common;

import java.io.*;

public class StreamGobbler extends Thread
{

    InputStream is;
    String type;
    OutputStream os;
    private LoggerInterface loggerInterface = null;

    public StreamGobbler(InputStream is, String type)
    {
        this(is, type, null, null);
    }

    public StreamGobbler(InputStream is, String type, OutputStream redirect)
    {
        this(is, type, redirect, null);
    }

    public StreamGobbler(InputStream is, String type, LoggerInterface loggerInterface)
    {
        this(is, type, null, loggerInterface);
    }

    public StreamGobbler(InputStream is, String type, OutputStream redirect, LoggerInterface loggerInterface)
    {
        this.is = is;
        this.type = type;
        this.os = redirect;
        this.loggerInterface = loggerInterface;
    }

    public void run()
    {
        try
        {
            PrintWriter pw = null;
            if (os != null)
            {
                pw = new PrintWriter(os);
            }

            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null)
            {
                if (pw != null)
                {
                    pw.println(line);
                }
                if (loggerInterface != null)
                {
                    loggerInterface.logLine(type + ">" + line);
                }
            }
            if (pw != null)
            {
                pw.flush();
            }
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
}
