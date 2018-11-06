package hu.mgx.util;

import java.io.*;

import hu.mgx.app.common.*;

public class Runner {

    private AppInterface appInterface;
    private boolean bNoConsole;
    private StreamGobbler errorGobbler = null;
    private StreamGobbler outputGobbler = null;
    private LoggerInterface loggerInterface = null;

    public Runner(AppInterface appInterface, boolean bNoConsole) {
        this(appInterface, bNoConsole, null);
    }

    public Runner(AppInterface appInterface, boolean bNoConsole, LoggerInterface loggerInterface) {
        this.appInterface = appInterface;
        this.bNoConsole = bNoConsole;
        this.loggerInterface = loggerInterface;
    }

    public int runCommand(String sCommand) {
        int exitVal = -1;

        String sJVMVersion = System.getProperty("java.version", "");
        if (sJVMVersion.compareTo("1.7.0_21")>=0) {
            sCommand = StringUtils.spaceProtection(sCommand);
        }

        Runtime runtime = Runtime.getRuntime();
        java.lang.Process process;
        try {
            process = runtime.exec(sCommand);
            if (bNoConsole) {
                errorGobbler = new StreamGobbler(process.getErrorStream(), "ERROR", loggerInterface);
                outputGobbler = new StreamGobbler(process.getInputStream(), "OUTPUT", loggerInterface);
                errorGobbler.start();
                outputGobbler.start();
                exitVal = process.waitFor();
            }
        } catch (IOException ioe) {
            appInterface.handleError(ioe);
        } catch (InterruptedException ie) {
            appInterface.handleError(ie);
        }
        return (exitVal);
    }
}
