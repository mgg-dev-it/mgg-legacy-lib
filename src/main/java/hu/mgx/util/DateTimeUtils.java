package hu.mgx.util;

import hu.mag.swing.MagTextField;
import java.text.*;

import hu.mgx.app.common.FormatInterface;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
//import java.time.temporal.ChronoUnit;
//import java.time.temporal.TemporalUnit;
import java.util.Calendar;
import java.util.GregorianCalendar;

public abstract class DateTimeUtils {

    private static String[] saMonthNames = {"január", "február", "március", "április", "május", "június", "július", "augusztus", "szeptember", "október", "november", "december"};
    private static String[] saMonthAbbreviatedNames = {"jan", "feb", "már", "ápr", "máj", "jún", "júl", "aug", "szep", "okt", "nov", "dec"};

    private DateTimeUtils() {
    }
//    public static String getCurrentDateTimeInString(FormatInterface mgxFormat)
//    {
//        SimpleDateFormat logDateFormat = new SimpleDateFormat("yyyy.MM.dd kk:mm:ss.SSS");
//        return(logDateFormat.format(new java.util.Date()));
//    }

    public static String checkDate(FormatInterface mgxFormat, String sText) {
        String sRetVal = sText;
        //@todo checkdatetime nem kell???
        String sToday = mgxFormat.getDateFormat().format(new java.util.Date());
        String sTmp = sText;
        if (sTmp.length() == 1) {
            if (Character.isDigit(sTmp.charAt(0))) {
                sRetVal = sToday.substring(0, 8) + "0" + sTmp;
            }
        }
        if (sTmp.length() == 2) {
            if (Character.isDigit(sTmp.charAt(0)) && Character.isDigit(sTmp.charAt(1))) {
                sRetVal = sToday.substring(0, 8) + sTmp;
            }
        }
        if (sTmp.length() == 3) {
            if (Character.isDigit(sTmp.charAt(0)) && Character.isDigit(sTmp.charAt(1)) && Character.isDigit(sTmp.charAt(2))) {
                sRetVal = sToday.substring(0, 4) + "0" + sTmp.substring(0, 1) + mgxFormat.getDateSeparator() + sTmp.substring(1, 3);
            }
        }
        if (sTmp.length() == 4) {
            if (Character.isDigit(sTmp.charAt(0)) && Character.isDigit(sTmp.charAt(1)) && Character.isDigit(sTmp.charAt(2)) && Character.isDigit(sTmp.charAt(3))) {
                sRetVal = sToday.substring(0, 4) + sTmp.substring(0, 2) + mgxFormat.getDateSeparator() + sTmp.substring(2, 4);
            }
        }
        try {
            java.util.Date utilDate = mgxFormat.getDateFormat().parse(sRetVal);
        } catch (ParseException e) {
            //@todo MsgBox "Hibás dátumot (" + sTmp + ") adott meg", vbExclamation
            sRetVal = sText;
        }
        return (sRetVal);
    }

    public static java.util.Date getMonthFirstDate() {
        LocalDateTime ldt = LocalDateTime.now();
        ldt = ldt.minusDays(ldt.getDayOfMonth() - 1);
        ldt = ldt.minusHours(ldt.getHour());
        ldt = ldt.minusMinutes(ldt.getMinute());
        ldt = ldt.minusSeconds(ldt.getSecond());
        ldt = ldt.minusNanos(ldt.getNano());
        java.sql.Timestamp ts = Timestamp.valueOf(ldt);
        java.util.Date date = new java.util.Date(ts.getTime());
        return (date);
    }

    public static java.util.Date getMonthFirstDate(int iYear, int iMonth) {
        LocalDateTime ldt = LocalDateTime.of(iYear, iMonth, 1, 0, 0, 0);
        ldt = ldt.minusDays(ldt.getDayOfMonth() - 1);
        ldt = ldt.minusHours(ldt.getHour());
        ldt = ldt.minusMinutes(ldt.getMinute());
        ldt = ldt.minusSeconds(ldt.getSecond());
        ldt = ldt.minusNanos(ldt.getNano());
        java.sql.Timestamp ts = Timestamp.valueOf(ldt);
        java.util.Date date = new java.util.Date(ts.getTime());
        return (date);
    }

    public static java.util.Date getMonthLastDate() {
        LocalDateTime ldt = LocalDateTime.now();
        ldt = ldt.plusMonths(1);
        ldt = ldt.minusDays(ldt.getDayOfMonth() - 1);
        ldt = ldt.minusHours(ldt.getHour());
        ldt = ldt.minusMinutes(ldt.getMinute());
        ldt = ldt.minusSeconds(ldt.getSecond() + 1);
        ldt = ldt.minusNanos(ldt.getNano());
        java.sql.Timestamp ts = Timestamp.valueOf(ldt);
        java.util.Date date = new java.util.Date(ts.getTime());
        return (date);
    }

    public static java.util.Date getMonthLastDate(int iYear, int iMonth) {
        LocalDateTime ldt = LocalDateTime.of(iYear, iMonth, 1, 0, 0, 0);
        ldt = ldt.plusMonths(1);
        ldt = ldt.minusDays(ldt.getDayOfMonth() - 1);
        ldt = ldt.minusHours(ldt.getHour());
        ldt = ldt.minusMinutes(ldt.getMinute());
        ldt = ldt.minusSeconds(ldt.getSecond() + 1);
        ldt = ldt.minusNanos(ldt.getNano());
        java.sql.Timestamp ts = Timestamp.valueOf(ldt);
        java.util.Date date = new java.util.Date(ts.getTime());
        return (date);
    }

    public static LocalDateTime getPreviousMonthBegin() {
        LocalDateTime ldt = LocalDateTime.now();
        ldt = ldt.minusDays(ldt.getDayOfMonth() - 1);
        ldt = ldt.minusHours(ldt.getHour());
        ldt = ldt.minusMinutes(ldt.getMinute());
        ldt = ldt.minusSeconds(ldt.getSecond());
        ldt = ldt.minusNanos(ldt.getNano());
        ldt = ldt.minusMonths(1);
        return (ldt);
    }

    public static LocalDateTime getPreviousMonthEnd() {
        LocalDateTime ldt = LocalDateTime.now();
        ldt = ldt.minusDays(ldt.getDayOfMonth() - 1);
        ldt = ldt.minusHours(ldt.getHour());
        ldt = ldt.minusMinutes(ldt.getMinute());
        ldt = ldt.minusSeconds(ldt.getSecond() + 1);
        ldt = ldt.minusNanos(ldt.getNano());
        //ldt = ldt.minusMonths(1);
        return (ldt);
    }

    public static int getSecondsInDay() {
        return (getSecondsInDay(new java.util.Date()));
    }

    public static int getSecondsInDay(java.util.Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int iSecond = c.get(Calendar.SECOND);
        int iMinute = c.get(Calendar.MINUTE);
        int iHour = c.get(Calendar.HOUR_OF_DAY);
        return (iHour * 3600 + iMinute * 60 + iSecond);
    }

    public static java.util.Date getCurrentDateOnly() {
        Calendar c = Calendar.getInstance();
        c.setTime(new java.util.Date());
        c.add(Calendar.HOUR_OF_DAY, -1 * c.get(Calendar.HOUR_OF_DAY));
        c.add(Calendar.MINUTE, -1 * c.get(Calendar.MINUTE));
        c.add(Calendar.SECOND, -1 * c.get(Calendar.SECOND));
        c.add(Calendar.MILLISECOND, -1 * c.get(Calendar.MILLISECOND));
        return (c.getTime());
    }

    public static int getCurrentYear() {
        return (getYear(new java.util.Date()));
    }

    public static LocalDate convertToLocalDate(java.util.Date date) {
        return (date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }

    public static java.util.Date convertToUtilDate(FormatInterface fi, Object o) {
        return (convertToUtilDate(fi.getDateFormat(), o));
    }

    public static java.util.Date convertToUtilDate(SimpleDateFormat sdf, Object o) {
        java.util.Date date = null;
        if (o == null) {
            return (date);
        }
        if (o instanceof java.sql.Date) {
            return (new java.util.Date(((java.sql.Date) o).getTime()));
        }
        if (o instanceof java.util.Date) {
            return ((java.util.Date) o);
        }
        if (o instanceof java.sql.Time) {
            return (new java.util.Date(((java.sql.Time) o).getTime()));
        }
        if (o instanceof java.sql.Timestamp) {
            return (new java.util.Date(((java.sql.Timestamp) o).getTime()));
        }
        if (o instanceof java.time.LocalDate) {
            //return (new java.util.Date(((java.time.LocalDate)o).getYear(),((java.time.LocalDate)o).getMonthValue(),((java.time.LocalDate)o).getDayOfMonth()));
            return (java.util.Date.from(((java.time.LocalDate) o).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
        if (sdf != null) {
            try {
                date = sdf.parse(o.toString());
            } catch (ParseException e) {
                //System.out.println(e.getLocalizedMessage());
            }
        } else {
            String s = o.toString().trim();
            if (s.length() == 10) {
                if (StringUtils.isDigits(s.substring(0, 4)) && !StringUtils.isDigits(s.substring(4, 5)) && StringUtils.isDigits(s.substring(5, 7)) && !StringUtils.isDigits(s.substring(7, 8)) && StringUtils.isDigits(s.substring(8, 10))) {
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        date = sdf1.parse(o.toString());
                    } catch (ParseException e) {
                        //System.out.println(e.getLocalizedMessage());
                    }
                }
            }
        }
        return (date);
    }

    public static String getPossibleFormat(String s) {
        String sRetVal = "";
        String a[] = s.trim().split(" ");
        for (int i = 0; i < a.length; i++) {
            //System.out.println(a[i]);
            String b = StringUtils.getDistinctChars(StringUtils.digitFilterOut(a[i]));
            //System.out.println(b);
            //b=StringUtils.getDistinctChars(b);
            //System.out.println(b);
            if (b.length() == 1) {
                String c = a[i].replace('0', '#');
                c = c.replace('1', '#');
                c = c.replace('2', '#');
                c = c.replace('3', '#');
                c = c.replace('4', '#');
                c = c.replace('5', '#');
                c = c.replace('6', '#');
                c = c.replace('7', '#');
                c = c.replace('8', '#');
                c = c.replace('9', '#');
                c = c.replace(b.charAt(0), '/');
                if (b.charAt(0) == '/' || b.charAt(0) == '.') {
                    if (c.equalsIgnoreCase("####/##/##")) {
                        sRetVal += (sRetVal.length() == 0 ? "" : " ") + "yyyy" + b.charAt(0) + "MM" + b.charAt(0) + "dd";
                    }
                    if (c.equalsIgnoreCase("####/##/##/")) {
                        sRetVal += (sRetVal.length() == 0 ? "" : " ") + "yyyy" + b.charAt(0) + "MM" + b.charAt(0) + "dd" + b.charAt(0);
                    }

                }
                if (b.charAt(0) == ':') {
                    if (c.equalsIgnoreCase("##/##/##")) {
                        sRetVal += (sRetVal.length() == 0 ? "" : " ") + "HH:mm:ss";
                    }
                    if (c.equalsIgnoreCase("##/##")) {
                        sRetVal += (sRetVal.length() == 0 ? "" : " ") + "HH:mm";
                    }
                }
            }
        }
        return (sRetVal);
    }

    public static java.util.Date convertToUtilDate(Object o) {
        SimpleDateFormat sdf = null;
        return (convertToUtilDate(sdf, o));
    }

    public static int compareAsUtilDate(MagTextField mtf1, MagTextField mtf2) {
        return (convertToUtilDate(mtf1.getValue()).compareTo(convertToUtilDate(mtf2.getValue())));
    }

    public static boolean isBetween(java.util.Date date, java.util.Date dateFrom, java.util.Date dateTo) {
        if (date.compareTo(dateFrom) > 0 && date.compareTo(dateTo) < 0) {
            return (true);
        }
        return (false);
    }

    public static boolean overlapDateRange(java.util.Date dr1From, java.util.Date dr1To, java.util.Date dr2From, java.util.Date dr2To) {
        if (dr1From.compareTo(dr1To) > 0) {
            return (false);
        }
        if (dr2From.compareTo(dr2To) > 0) {
            return (false);
        }
        if (isBetween(dr1From, dr2From, dr2To)) {
            return (true);
        }
        if (isBetween(dr1To, dr2From, dr2To)) {
            return (true);
        }
        if (isBetween(dr2From, dr1From, dr1To)) {
            return (true);
        }
        if (isBetween(dr2To, dr1From, dr1To)) {
            return (true);
        }
        return (false);
    }

    public static int getYear(java.util.Date date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        return (gc.get(GregorianCalendar.YEAR));
    }

    public static int getMonth(java.util.Date date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        return (gc.get(GregorianCalendar.MONTH) + 1);
    }

    public static int getDay(java.util.Date date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        return (gc.get(GregorianCalendar.DAY_OF_MONTH));
    }

    public static int getDayOfWeek(java.util.Date date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        return (gc.get(GregorianCalendar.DAY_OF_WEEK));
    }

    public static String getDayOfWeekShortName(java.util.Date date) {
        int iDayOfWeek = getDayOfWeek(date);
        switch (iDayOfWeek) {
            case Calendar.MONDAY:
                return ("H");
            case Calendar.TUESDAY:
                return ("K");
            case Calendar.WEDNESDAY:
                return ("Sze");
            case Calendar.THURSDAY:
                return ("Cs");
            case Calendar.FRIDAY:
                return ("P");
            case Calendar.SATURDAY:
                return ("Szo");
            case Calendar.SUNDAY:
                return ("V");
            default:
                return ("");
        }
    }

    public static String getMonthName(LocalDate date) {
        int iMonth = date.getMonthValue();
        if (iMonth >= 1 && iMonth <= 12) {
            return (saMonthNames[iMonth - 1]);
        }
        return ("");
    }

    public static String getMonthName(java.util.Date date) {
        if (date == null) {
            return ("");
        }
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        int iMonth = gc.get(GregorianCalendar.MONTH) + 1;
        if (iMonth >= 1 && iMonth <= 12) {
            return (saMonthNames[iMonth - 1]);
        }
        return ("");
    }

    public static String getMonthAbbreviatedName(int iMonth) {
        if (iMonth >= 1 && iMonth <= 12) {
            return (saMonthAbbreviatedNames[iMonth - 1]);
        }
        return ("");
    }

    public static String getDayOfWeekName(java.util.Date date) {
        if (date == null) {
            return ("");
        }
        int iDayOfWeek = getDayOfWeek(date);
        switch (iDayOfWeek) {
            case Calendar.MONDAY:
                return ("Hétfõ");
            case Calendar.TUESDAY:
                return ("Kedd");
            case Calendar.WEDNESDAY:
                return ("Szerda");
            case Calendar.THURSDAY:
                return ("Csütörtök");
            case Calendar.FRIDAY:
                return ("Péntek");
            case Calendar.SATURDAY:
                return ("Szombat");
            case Calendar.SUNDAY:
                return ("Vasárnap");
            default:
                return ("");
        }
    }

    public static int getHour(java.util.Date date) {
        if (date == null) {
            return (0);
        }
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        return (gc.get(GregorianCalendar.HOUR_OF_DAY));
    }

    public static int getMinute(java.util.Date date) {
        if (date == null) {
            return (0);
        }
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        return (gc.get(GregorianCalendar.MINUTE));
    }

    public static int getSecond(java.util.Date date) {
        if (date == null) {
            return (0);
        }
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        return (gc.get(GregorianCalendar.SECOND));
    }

    public static int getMilliSecond(java.util.Date date) {
        if (date == null) {
            return (0);
        }
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        return (gc.get(GregorianCalendar.MILLISECOND));
    }

    public static java.util.Date addDay(java.util.Date date, int iDay) {
        if (date == null) {
            return (date);
        }
        return (new java.util.Date(date.getTime() + 86400L * 1000L * iDay));
    }

    public static java.util.Date addHour(java.util.Date date, int iHour) {
        if (date == null) {
            return (date);
        }
        return (new java.util.Date(date.getTime() + 3600L * 1000L * iHour));
    }

    public static java.util.Date addMinute(java.util.Date date, int iMinute) {
        if (date == null) {
            return (date);
        }
        return (new java.util.Date(date.getTime() + 60L * 1000L * iMinute));
    }

    public static java.util.Date addSecond(java.util.Date date, int iSecond) {
        if (date == null) {
            return (date);
        }
        return (new java.util.Date(date.getTime() + 1L * 1000L * iSecond));
    }

    public static boolean isDateOnly(java.util.Date d) {
        return (getHour(d) == 0 && getMinute(d) == 0 && getSecond(d) == 0 && getMilliSecond(d) == 0);
    }

    public static int getWeek(LocalDate date) {
        //@todo nem jó még ...
        //int iWeek = date.get(ChronoField.ALIGNED_WEEK_OF_YEAR);

        int iDayOfYear = date.getDayOfYear();
        int iStartingDayOfYear = date.minusDays(date.getDayOfYear() - 1).getDayOfWeek().getValue();
        if (1 <= iStartingDayOfYear && iStartingDayOfYear <= 4) {
            //hétfõ, kedd, szerda vagy csütörtök az év elsõ napja
            iDayOfYear += (iStartingDayOfYear - 1);
            return (((iDayOfYear - 1) / 7) + 1);
        } else {
            //péntek, szombat vagy vasárnap az év elsõ napja
            iDayOfYear += (iStartingDayOfYear - 1);
            int iWeek = (iDayOfYear - 1) / 7;
            if (iWeek == 0) {
                iWeek = getWeek(date.minusDays(date.getDayOfYear()));
            }
            return (iWeek);
        }
//        //System.out.println(iStartingDayOfYear);
//        iDayOfYear -= iStartingDayOfYear;
//        if (iStartingDayOfYear > 4) {
//            iDayOfYear -= 7;
//        }
//        int iWeek = ((iDayOfYear) / 7) + 1;
//        //iStartingDayOfYear==1 1-7 8-14 1,2
//        //iStartingDayOfYear==2 1-6 7-13 1,2
//        //iStartingDayOfYear==3 1-5 6-12 1,2
//        //iStartingDayOfYear==4 1-4 5-11 1,2
//        //iStartingDayOfYear==5 1-3 4-10 53,1
//        //iStartingDayOfYear==6 1-2 3-9  53,1
//        //iStartingDayOfYear==7 1-1 2-8  53,1
//        //52*7=364
//        return (iWeek);
    }

}
