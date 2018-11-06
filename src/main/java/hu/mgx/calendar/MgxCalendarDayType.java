package hu.mgx.calendar;

public abstract class MgxCalendarDayType {

    public final static int UNKNOWN = 0; //meghatározatlan
    public final static int WORK = 1; //munkanap: "hétfõ-péntek"
    public final static int REST = 2; //munkaszüneti nap: "szombat-vasárnap"
    public final static int HOLIDAY = 3; //ünnepnap: január 1., március 15., stb.
    public final static int REST_BEFORE_WORK = 4; //munkanapot megelõzõ munkaszüneti nap: "vasárnap" MÉG NEM HASZNÁLOM!!!
    public final static int WORK_BEFORE_REST = 5; //munkaszüneti napot megelõzõ munkanap: "péntek" MÉG NEM HASZNÁLOM!!!
    
    //--- Ünnepnapok:
    //január 1.
    //március 15.
    //húsvét vasárnap
    //húsvét hétfõ
    //május 1.
    //pünkösd vasárnap
    //pünkösd hétfõ
    //augusztus 20.
    //október 23.
    //november 1.
    //december 25.
    //december 26.
    
    //public MgxCalendarDayType() {
    //}
    
}
