package hu.mgx.app.common;

import java.text.*;

public interface FormatInterface {

    public abstract char getDateSeparator();

    public abstract void setDateSeparator(char cDateSeparator);

    public abstract char getTimeSeparator();

    public abstract void setTimeSeparator(char cTimeSeparator);

    public abstract java.lang.String getDatePattern();

    public abstract void setDatePattern(java.lang.String sDatePattern);

    public abstract java.lang.String getTimePattern();

    public abstract void setTimePattern(java.lang.String sTimePattern);

    public abstract java.lang.String getDateTimePattern();

    public abstract SimpleDateFormat getDateFormat();

    public abstract SimpleDateFormat getTimeFormat();

    public abstract SimpleDateFormat getDateTimeFormat();

    public abstract SimpleDateFormat getClockDateTimeFormat();

    public abstract SimpleDateFormat getLogDateTimeFormat();

    public abstract SimpleDateFormat getSQLDateFormat();

    public abstract SimpleDateFormat getSQLTimeFormat();

    public abstract SimpleDateFormat getSQLDateTimeFormat();

    public abstract SimpleDateFormat getFileNameDateTimeFormat();

    public abstract char getDecimalSeparator();

    public abstract void setDecimalSeparator(char cDecimalSeparator);

    public abstract char getGroupingSeparator();

    public abstract void setGroupingSeparator(char cGroupingSeparator);

    public abstract DecimalFormat getDecimalFormat();

    public abstract DecimalFormat getDecimalFormat(int iScale);
}
