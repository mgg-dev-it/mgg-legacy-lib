package hu.mgx.app.console;

import java.io.*;

public interface ConsoleInterface
{

    public InputStream getInputStream();

    public PrintStream getOutputStream();

    public PrintStream getErrorStream();
}
