package hu.mgx.util;

import java.io.*;

import hu.mgx.app.common.*;

public class Runner2 implements Runnable {

    private AppInterface appInterface;
    private boolean bNoConsole;
    private StreamGobbler errorGobbler = null;
    private StreamGobbler outputGobbler = null;
    private String sCommand = "";
    private boolean bSpaceProtection = false;
    private boolean bNoLogger = false;
    private LoggerInterface loggerInterface = null;

    public Runner2(String sCommand, AppInterface appInterface, boolean bNoConsole) {
        this(sCommand, appInterface, bNoConsole, false);
    }

    public Runner2(String sCommand, AppInterface appInterface, boolean bNoConsole, boolean bSpaceProtection) {
        this(sCommand, appInterface, bNoConsole, bSpaceProtection, false);
    }

    public Runner2(String sCommand, AppInterface appInterface, boolean bNoConsole, boolean bSpaceProtection, boolean bNoLogger) {
        this.sCommand = sCommand;
        this.appInterface = appInterface;
        this.bNoConsole = bNoConsole;
        this.bSpaceProtection = bSpaceProtection;
        this.bNoLogger = bNoLogger;
        if (!bNoLogger) {
            loggerInterface = appInterface;
        }
    }

    private int runCommand(String sCommand) {
        int exitVal = -1;

        String sJVMVersion = System.getProperty("java.version", "");
        if (bSpaceProtection && (sJVMVersion.compareTo("1.7.0_21") >= 0)) {
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

    public void run() {
        runCommand(sCommand);
    }
}
