package hu.mgx.calendar;

public class MgxCalendarTime {
    
    private int iYear = 0;
    private int iMonth = 0;
    private int iDay = 0;

    private int iHourFrom = 0;
    private int iMinuteFrom = 0;
    private int iSecondFrom = 0;
    private long lSecondOfDayFrom = 0;

    private int iHourTo = 0;
    private int iMinuteTo = 0;
    private int iSecondTo = 0;
    private long lSecondOfDayTo = 0;

    private int iType = MgxCalendarTimeType.UNKNOWN;
    
    public MgxCalendarTime(int iYear, int iMonth, int iDay) {
        this(iYear, iMonth, iDay, 0, 0, 0, 23, 59, 59); //?????
    }
    
    public MgxCalendarTime(int iYear, int iMonth, int iDay, int iHourFrom, int iMinuteFrom, int iSecondFrom) {
        this(iYear, iMonth, iDay, iHourFrom, iMinuteFrom, iSecondFrom, 23, 59, 59); //?????
    }
    
    public MgxCalendarTime(int iYear, int iMonth, int iDay, int iHourFrom, int iMinuteFrom, int iSecondFrom, int iHourTo, int iMinuteTo, int iSecondTo) {
        this.iYear = iYear;
        this.iMonth = iMonth;
        this.iDay = iDay;
        this.iHourFrom = iHourFrom;
        this.iMinuteFrom = iMinuteFrom;
        this.iSecondFrom = iSecondFrom;
        this.iHourTo = iHourTo;
        this.iMinuteTo = iMinuteTo;
        this.iSecondTo = iSecondTo;
    }
    
    private void compute(){
        lSecondOfDayFrom = 3600L * iHourFrom + 60L * iMinuteFrom + iSecondFrom;
        lSecondOfDayTo = 3600L * iHourTo + 60L * iMinuteTo + iSecondTo;
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
    
}
