package hu.mgx.calendar;

import java.util.*;

public class MgxCalendarMonth {
    
    private int iYear = 0;
    private int iMonth = 0;
    private Vector vDays = null;
    
    public MgxCalendarMonth(int iYear, int iMonth) {
        this.iYear = iYear;
        this.iMonth = iMonth;
    }
    
    public int getYear() {
        return (iYear);
    }
    
    public int getMonth() {
        return (iMonth);
    }
    
    public void addDay(MgxCalendarDay mgxCalendarDay) throws MgxCalendarException {
        if (mgxCalendarDay.getYear() != iYear || mgxCalendarDay.getMonth() != iMonth) throw new MgxCalendarException("Nem ebbe a hónapba tartozik a nap!" + Integer.toString(iYear) + "/" + Integer.toString(iMonth) + " <> " + Integer.toString(mgxCalendarDay.getYear()) + "/" + Integer.toString(mgxCalendarDay.getMonth()));
        vDays.add(mgxCalendarDay);
    }
    
    public MgxCalendarDay getDay(int iDay){
        return((MgxCalendarDay)vDays.elementAt(iDay));
    }

    public MgxCalendarDay [] getDays(){
        MgxCalendarDay mgxCalendarDays[] = new  MgxCalendarDay[vDays.size()];
        vDays.copyInto(mgxCalendarDays);
//        for (int i=0; i<mgxCalendarDays.length; i++){
//            mgxCalendarDays[i] = (MgxCalendarDay)vDays.elementAt(i);
//        }
        return(mgxCalendarDays);
    }

}
