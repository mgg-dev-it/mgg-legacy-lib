package hu.mgx.app.common;

import hu.mgx.util.StringUtils;
import java.text.*;

public class DefaultAppFormat implements FormatInterface {

    //@todo idea: define named enabled formats (e.g. yyyy/MM/dd, yyyy-MM-dd)?
    // Date
    private char cDateSeparator = '/';
    private char[] caDatePattern = {'y', 'y', 'y', 'y', cDateSeparator, 'M', 'M', cDateSeparator, 'd', 'd'};
    private String sDatePattern = new String(caDatePattern);
    // Time
    private char cTimeSeparator = ':';
    private char[] caTimePattern = {'H', 'H', cTimeSeparator, 'm', 'm', cTimeSeparator, 's', 's'};
    private String sTimePattern = new String(caTimePattern);
    private String sDateTimePattern = new String(sDatePattern + " " + sTimePattern);
    // Formatters
    private SimpleDateFormat dateFormat = new SimpleDateFormat(sDatePattern);
    private SimpleDateFormat timeFormat = new SimpleDateFormat(sTimePattern);
    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat(sDateTimePattern);
    private SimpleDateFormat clockDateTimeFormat = new SimpleDateFormat("yyyy. MMMM dd." + " " + sTimePattern);
    private SimpleDateFormat logDateTimeFormat = new SimpleDateFormat(sDatePattern + " " + sTimePattern + ".SSS");
    private SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sqlTimeFormat = new SimpleDateFormat("HH:mm:ss");
    private SimpleDateFormat sqlDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat fileNameDateTimeFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
    private DecimalFormat decimalFormat = new DecimalFormat("#,##0.##");
    private String sDecimalBasePattern = "#,##0";

    // Decimal separator
    private char cDecimalSeparator = '.'; //.
    // Grouping (thousand) separator
    private char cGroupingSeparator = ' '; //space

    //private final char[] caDateSeparator = {cDateSeparator};
    //private final char[] caThousandSeparator = {cThousandSeparator};
    public DefaultAppFormat() {
        setDecimalFormatSymbols();
    }

    private void setDecimalFormatSymbols() {
        DecimalFormatSymbols dfs = decimalFormat.getDecimalFormatSymbols();
        dfs.setDecimalSeparator(cDecimalSeparator);
        dfs.setGroupingSeparator(cGroupingSeparator);
        decimalFormat.setDecimalFormatSymbols(dfs);
    }

    @Override
    public char getDateSeparator() {
        return cDateSeparator;
    }

    @Override
    public void setDateSeparator(char cDateSeparator) {
        char cOldDateSeparator = this.cDateSeparator;
        this.cDateSeparator = cDateSeparator;
        setDatePattern(getDatePattern().replace(cOldDateSeparator, cDateSeparator));
    }

    @Override
    public char getTimeSeparator() {
        return cTimeSeparator;
    }

    @Override
    public void setTimeSeparator(char cTimeSeparator) {
        char cOldTimeSeparator = this.cTimeSeparator;
        this.cTimeSeparator = cTimeSeparator;
        setTimePattern(getTimePattern().replace(cOldTimeSeparator, cTimeSeparator));
    }

    @Override
    public java.lang.String getDatePattern() {
        return sDatePattern;
    }

    @Override
    public void setDatePattern(java.lang.String sDatePattern) {
        //check enabled formats
        String sDateOrder = StringUtils.filter(sDatePattern, "yMd");
        switch (sDateOrder) {
            case "yyyyMMdd":
                cDateSeparator = sDatePattern.charAt(4);
                break;
            case "ddMMyyyy":
                cDateSeparator = sDatePattern.charAt(2);
                break;
            case "MMddyyyy":
                cDateSeparator = sDatePattern.charAt(2);
                break;
            default:
                //@todo task : raise exception
                return;
        }
        this.sDatePattern = sDatePattern;
        dateFormat = new SimpleDateFormat(sDatePattern);
        dateTimeFormat = new SimpleDateFormat(sDatePattern + " " + sTimePattern);
    }

    @Override
    public java.lang.String getTimePattern() {
        return sTimePattern;
    }

    @Override
    public void setTimePattern(java.lang.String sTimePattern) {
        this.sTimePattern = sTimePattern;
        dateTimeFormat = new SimpleDateFormat(sDatePattern + " " + sTimePattern);
    }

    @Override
    public java.lang.String getDateTimePattern() {
        return sDateTimePattern;
    }

    @Override
    public SimpleDateFormat getDateFormat() {
        return (dateFormat);
    }

    @Override
    public SimpleDateFormat getTimeFormat() {
        return (timeFormat);
    }

    @Override
    public SimpleDateFormat getDateTimeFormat() {
        return (dateTimeFormat);
    }

    @Override
    public SimpleDateFormat getClockDateTimeFormat() {
        return (clockDateTimeFormat);
    }

    @Override
    public SimpleDateFormat getLogDateTimeFormat() {
        return (logDateTimeFormat);
    }

    @Override
    public SimpleDateFormat getSQLDateFormat() {
        return (sqlDateFormat);
    }

    @Override
    public SimpleDateFormat getSQLTimeFormat() {
        return (sqlTimeFormat);
    }

    @Override
    public SimpleDateFormat getSQLDateTimeFormat() {
        return (sqlDateTimeFormat);
    }

    @Override
    public SimpleDateFormat getFileNameDateTimeFormat() {
        return (fileNameDateTimeFormat);
    }

    @Override
    public char getDecimalSeparator() {
        return cDecimalSeparator;
    }

    @Override
    public void setDecimalSeparator(char cDecimalSeparator) {
        this.cDecimalSeparator = cDecimalSeparator;
        setDecimalFormatSymbols();
    }

    @Override
    public char getGroupingSeparator() {
        return cGroupingSeparator;
    }

    @Override
    public void setGroupingSeparator(char cGroupingSeparator) {
        this.cGroupingSeparator = cGroupingSeparator;
        setDecimalFormatSymbols();
    }

    @Override
    public DecimalFormat getDecimalFormat() {
        return decimalFormat;
    }

    @Override
    public DecimalFormat getDecimalFormat(int iScale) {
        if (iScale < 0) {
            iScale = 0;
        }
        DecimalFormat df = new DecimalFormat(sDecimalBasePattern + (iScale > 0 ? "." : "") + StringUtils.repeat("0", iScale));
        DecimalFormatSymbols dfs = decimalFormat.getDecimalFormatSymbols();
        dfs.setDecimalSeparator(cDecimalSeparator);
        dfs.setGroupingSeparator(cGroupingSeparator);
        df.setDecimalFormatSymbols(dfs);
        return (df);
    }

}
