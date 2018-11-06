package hu.mgx.calendar;

import java.util.*;

import hu.mgx.swing.table.*;

public class MgxCalendar extends GregorianCalendar
{

    Locale locale = null;

    public MgxCalendar()
    {
        super();
        locale = Locale.getDefault();
        setMinimalDaysInFirstWeek(4);
    }

    public MgxCalendar(int iYear, int iMonth, int iDay)
    {
        super(iYear, iMonth, iDay);
        locale = Locale.getDefault();
        setMinimalDaysInFirstWeek(4);
    }

    public String getMonthName(int iMonth)
    {
        if (locale.getLanguage().equals("hu") && locale.getCountry().equals("HU"))
        {
            switch (iMonth)
            {
                case Calendar.JANUARY:
                    return ("Január");
                case Calendar.FEBRUARY:
                    return ("Február");
                case Calendar.MARCH:
                    return ("Március");
                case Calendar.APRIL:
                    return ("Április");
                case Calendar.MAY:
                    return ("Május");
                case Calendar.JUNE:
                    return ("Június");
                case Calendar.JULY:
                    return ("Július");
                case Calendar.AUGUST:
                    return ("Augusztus");
                case Calendar.SEPTEMBER:
                    return ("Szeptember");
                case Calendar.OCTOBER:
                    return ("Október");
                case Calendar.NOVEMBER:
                    return ("November");
                case Calendar.DECEMBER:
                    return ("December");
            }
        }
        else
        {
            switch (iMonth)
            {
                case Calendar.JANUARY:
                    return ("January");
                case Calendar.FEBRUARY:
                    return ("February");
                case Calendar.MARCH:
                    return ("March");
                case Calendar.APRIL:
                    return ("April");
                case Calendar.MAY:
                    return ("May");
                case Calendar.JUNE:
                    return ("June");
                case Calendar.JULY:
                    return ("July");
                case Calendar.AUGUST:
                    return ("August");
                case Calendar.SEPTEMBER:
                    return ("September");
                case Calendar.OCTOBER:
                    return ("October");
                case Calendar.NOVEMBER:
                    return ("November");
                case Calendar.DECEMBER:
                    return ("December");
            }
        }
        return ("");
    }

    public String getDayName(int iDay)
    {
        if (locale.getLanguage().equals("hu") && locale.getCountry().equals("HU"))
        {
            switch (iDay)
            {
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
            }
        }
        else
        {
            switch (iDay)
            {
                case Calendar.MONDAY:
                    return ("Monday");
                case Calendar.TUESDAY:
                    return ("Tuesday");
                case Calendar.WEDNESDAY:
                    return ("Wednesday");
                case Calendar.THURSDAY:
                    return ("Thursday");
                case Calendar.FRIDAY:
                    return ("Friday");
                case Calendar.SATURDAY:
                    return ("Saturday");
                case Calendar.SUNDAY:
                    return ("Sunday");
            }
        }
        return ("");
    }

    public String getDayAbbreviation(int iDay)
    {
        if (locale.getLanguage().equals("hu") && locale.getCountry().equals("HU"))
        {
            switch (iDay)
            {
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
            }
        }
        else
        {
            switch (iDay)
            {
                case Calendar.MONDAY:
                    return ("Mon");
                case Calendar.TUESDAY:
                    return ("Tue");
                case Calendar.WEDNESDAY:
                    return ("Wed");
                case Calendar.THURSDAY:
                    return ("Thu");
                case Calendar.FRIDAY:
                    return ("Fri");
                case Calendar.SATURDAY:
                    return ("Sat");
                case Calendar.SUNDAY:
                    return ("Sun");
            }
        }
        return ("");
    }

    public int getNextDay(int iDay)
    {
        switch (iDay)
        {
            case Calendar.MONDAY:
                return (Calendar.TUESDAY);
            case Calendar.TUESDAY:
                return (Calendar.WEDNESDAY);
            case Calendar.WEDNESDAY:
                return (Calendar.THURSDAY);
            case Calendar.THURSDAY:
                return (Calendar.FRIDAY);
            case Calendar.FRIDAY:
                return (Calendar.SATURDAY);
            case Calendar.SATURDAY:
                return (Calendar.SUNDAY);
            case Calendar.SUNDAY:
                return (Calendar.MONDAY);
        }
        return (super.getFirstDayOfWeek());
    }

    public MemoryTable getMonthPage(int iYear, int iMonth)
    {
        GregorianCalendar localCalendar = new GregorianCalendar();
        int iFirstDayOfWeek = super.getFirstDayOfWeek();
        localCalendar.setFirstDayOfWeek(iFirstDayOfWeek);
        localCalendar.setMinimalDaysInFirstWeek(super.getMinimalDaysInFirstWeek());
        Vector vDays = new Vector();
        vDays.add("Hét");
        int iDay = iFirstDayOfWeek;
        for (int i = 0; i < 7; i++)
        {
            //vDays.add(getDayName(iDay));
            vDays.add(getDayAbbreviation(iDay));
            iDay = getNextDay(iDay);
        }
        //MemoryTable memoryTable = new MemoryTable(new String [] {"1","2","3"});
        MemoryTable memoryTable = new MemoryTable(vDays, 0);

        int iFrom = 1;
        localCalendar.set(GregorianCalendar.YEAR, iYear);
        localCalendar.set(GregorianCalendar.MONTH, iMonth);
        localCalendar.set(GregorianCalendar.DAY_OF_MONTH, iFrom);
        int iTo = localCalendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

//        memoryTable.addRow();
//        memoryTable.setValueAt(new Integer(iFrom), memoryTable.getRowCount()-1, 0);
//        memoryTable.setValueAt(new Integer(iTo), memoryTable.getRowCount()-1, 1);
//        
//        localCalendar.set(GregorianCalendar.DAY_OF_MONTH, 1);
//        memoryTable.setValueAt(new Integer(localCalendar.get(GregorianCalendar.DAY_OF_WEEK)), memoryTable.getRowCount()-1, 2);

        int iDayOfWeek;
        memoryTable.addRow();
        memoryTable.setValueAt(new Integer(localCalendar.get(GregorianCalendar.WEEK_OF_YEAR)), memoryTable.getRowCount() - 1, 0);
        for (iDay = iFrom; iDay <= iTo; iDay++)
        {
            localCalendar.set(GregorianCalendar.DAY_OF_MONTH, iDay);
            iDayOfWeek = localCalendar.get(GregorianCalendar.DAY_OF_WEEK);
            if (iDay > 1 && ((iDayOfWeek + 7 - iFirstDayOfWeek) % 7 == 0))
            {
                memoryTable.addRow();
                memoryTable.setValueAt(new Integer(localCalendar.get(GregorianCalendar.WEEK_OF_YEAR)), memoryTable.getRowCount() - 1, 0);
            }
            memoryTable.setValueAt(new Integer(iDay), memoryTable.getRowCount() - 1, (iDayOfWeek + 7 - iFirstDayOfWeek) % 7 + 1);
        }
        return (memoryTable);
    }
}
