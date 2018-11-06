package hu.mgx.app.swing;

import java.util.*;
import java.text.*;

import hu.mgx.app.common.*;

public class AppTimerLogger implements TimerLoggerInterface
{

    private FormatInterface mgxFormat = null;
    private LoggerInterface loggerInterface = null;
    private boolean bActive = false;
    private String sVersionInfo = "";
    private SimpleDateFormat simpleDateFormat = null;
    private Date dateStart = null;
    private Date dateStop = null;
    private HashMap<String, Date> hmParts = null;
    private Vector<String> vLogLines = null;
    private Vector<Long> vLogTimes = null;
    private String sName = null;
    private DecimalFormat displayDiffFormat = new DecimalFormat("#,##0.##");

    public AppTimerLogger(FormatInterface mgxFormat, LoggerInterface loggerInterface, boolean bActive, String sVersionInfo)
    {
        this.mgxFormat = mgxFormat;
        this.simpleDateFormat = mgxFormat.getLogDateTimeFormat();
        this.loggerInterface = loggerInterface;
        this.bActive = bActive;
        this.sVersionInfo = sVersionInfo;
    }

    public void setActive(boolean bActive)
    {
        this.bActive = bActive;
    }

    public boolean isActive()
    {
        return (bActive);
    }

    private String formatDiffTime(long l)
    {
        return (hu.mgx.util.StringUtils.leftPad(displayDiffFormat.format(l), 12) + " ms");
    }

    private String formatDiffTime2(long l)
    {
        if (l == 0)
        {
            return ("                 ");
        }
        return (" " + hu.mgx.util.StringUtils.leftPad(displayDiffFormat.format(l), 12) + " ms ");
    }

    public void startTimer(String sName)
    {
        if (!bActive)
        {
            return;
        }
        this.sName = sName;
        vLogLines = new Vector<String>();
        vLogTimes = new Vector<Long>();
        hmParts = new HashMap<String, Date>();
        dateStart = new Date();
        vLogLines.add(formatDiffTime(0) + " " + formatDiffTime2(0) + " " + sName + " timer start");
        vLogTimes.add(new Long(0));
    }

    public void startPartTimer(String sName)
    {
        if (!bActive)
        {
            return;
        }
        if (hmParts == null)
        {
            return;
        }
        if (hmParts.containsKey(sName))
        {
            hmParts.remove(sName);
        }
        Date dStart = new Date();
        hmParts.put(sName, dStart);
        vLogLines.add(formatDiffTime(dStart.getTime() - dateStart.getTime()) + " " + formatDiffTime2(0) + " " + sName + " start");
        vLogTimes.add(new Long(0));
    }

    private long diff(Date dStart, Date dStop)
    {
        return (dStop.getTime() - dStart.getTime());
    }

    public void stopPartTimer(String sName)
    {
        if (!bActive)
        {
            return;
        }
        if (hmParts == null)
        {
            return;
        }
        Date dStop = new Date();
        if (hmParts.containsKey(sName))
        {
            Date dStart = hmParts.get(sName);
            hmParts.remove(sName);
            vLogLines.add(formatDiffTime(dStop.getTime() - dateStart.getTime()) + " " + formatDiffTime2(dStop.getTime() - dStart.getTime()) + " " + sName + " stop");
            vLogTimes.add(new Long(dStop.getTime() - dStart.getTime()));
        }
    }

    public void stopTimer(String sName)
    {
        if (!bActive)
        {
            return;
        }
        this.sName = ""; //2011.02.21.
        dateStop = new Date();
        vLogLines.add(formatDiffTime(dateStop.getTime() - dateStart.getTime()) + " " + formatDiffTime2(dateStop.getTime() - dateStart.getTime()) + " " + sName + " timer stop");
        vLogTimes.add(new Long(dateStop.getTime() - dateStart.getTime()));
    }

    public void writeToLog()
    {
        if (!bActive)
        {
            return;
        }
        long lTotal = dateStop.getTime() - dateStart.getTime();
        String sTmp = "";
        for (int i = 0; i < vLogLines.size(); i++)
        {
            sTmp = sVersionInfo + (!sVersionInfo.equals("") ? " " : "");
            sTmp += vLogLines.elementAt(i);
            if (vLogTimes.elementAt(i).compareTo(new Long(0)) != 0)
            {
                sTmp += " " + Long.toString((vLogTimes.elementAt(i).longValue() * 100) / lTotal) + "%";
            }
            loggerInterface.logLine(sTmp);
        }
    }
}
