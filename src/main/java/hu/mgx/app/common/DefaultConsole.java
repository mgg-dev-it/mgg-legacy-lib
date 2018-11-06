package hu.mgx.app.common;

import hu.mgx.app.console.ConsoleInterface;
import java.io.InputStream;
import java.io.PrintStream;

public class DefaultConsole implements ConsoleInterface
{

    public InputStream getInputStream()
    {
        return (System.in);
    }

    public PrintStream getOutputStream()
    {
        return (System.out);
    }

    public PrintStream getErrorStream()
    {
        return (System.err);
    }
}
