package hu.mgx.calendar;

import java.util.*;

public class MgxCalendarDay {
    
    private int iYear = 0;
    private int iMonth = 0;
    private int iDay = 0;
    private int iDayOfWeek = 0;
    private int iWeek = 0;
    private int iType = MgxCalendarDayType.UNKNOWN;
    
    public MgxCalendarDay(int iYear, int iMonth, int iDay) {
        this.iYear = iYear;
        this.iMonth = iMonth;
        this.iDay = iDay;
        setWeek();
        setDayOfWeek();
        setDayType();
    }
    
    public MgxCalendarDay(int iYear, int iMonth, int iDay, int iWeek, int iDayOfWeek, int iType) {
        this.iYear = iYear;
        this.iMonth = iMonth;
        this.iDay = iDay;
        this.iWeek = iWeek;
        this.iDayOfWeek = iDayOfWeek;
        this.iType = iType;
    }
    
    public int getYear() {
        return (iYear);
    }
    
    public int getMonth() {
        return (iMonth);
    }
    
    public int getDay() {
        return (iDay);
    }
    
    public int getDayOfWeek() {
        return (iDayOfWeek);
    }
    
    public int getType() {
        return (iType);
    }
    
    public void setWeek(){
        MgxCalendar mgxCalendar = new MgxCalendar(iYear, iMonth, iDay);
        iWeek = mgxCalendar.get(MgxCalendar.WEEK_OF_YEAR);
    }
    
    public void setDayOfWeek(){
        MgxCalendar mgxCalendar = new MgxCalendar(iYear, iMonth, iDay);
        iDayOfWeek = mgxCalendar.get(MgxCalendar.DAY_OF_WEEK);
    }
    
    public boolean isEasterOrPentecost(int iYear, int iMonth, int iDay){
        if ((iYear == 2005) && (iMonth == 2) && ((iDay == 27) || (iDay == 28))) return(true);
        if ((iYear == 2005) && (iMonth == 4) && ((iDay == 15) || (iDay == 16))) return(true);
        if ((iYear == 2006) && (iMonth == 3) && ((iDay == 16) || (iDay == 17))) return(true);
        if ((iYear == 2006) && (iMonth == 5) && ((iDay == 4) || (iDay == 5))) return(true);
        return(false);
    }
    
    public void setDayType(){
        setDayOfWeek();
        if ((iDayOfWeek == GregorianCalendar.SATURDAY) || (iDayOfWeek == GregorianCalendar.SUNDAY)) iType = MgxCalendarDayType.REST;
        else iType = MgxCalendarDayType.WORK;
        if ((iMonth == 0) && (iDay == 1)) iType = MgxCalendarDayType.HOLIDAY;
        if ((iMonth == 2) && (iDay == 15)) iType = MgxCalendarDayType.HOLIDAY;
        if ((iMonth == 4) && (iDay == 1)) iType = MgxCalendarDayType.HOLIDAY;
        if ((iMonth == 7) && (iDay == 20)) iType = MgxCalendarDayType.HOLIDAY;
        if ((iMonth == 9) && (iDay == 23)) iType = MgxCalendarDayType.HOLIDAY;
        if ((iMonth == 10) && (iDay == 1)) iType = MgxCalendarDayType.HOLIDAY;
        if ((iMonth == 11) && (iDay == 25)) iType = MgxCalendarDayType.HOLIDAY;
        if ((iMonth == 11) && (iDay == 26)) iType = MgxCalendarDayType.HOLIDAY;
        if (isEasterOrPentecost (iYear, iMonth, iDay)) iType = MgxCalendarDayType.HOLIDAY;
    }
}
