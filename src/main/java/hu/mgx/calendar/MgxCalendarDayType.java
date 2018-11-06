package hu.mgx.calendar;

public abstract class MgxCalendarDayType {

    public final static int UNKNOWN = 0; //meghat�rozatlan
    public final static int WORK = 1; //munkanap: "h�tf�-p�ntek"
    public final static int REST = 2; //munkasz�neti nap: "szombat-vas�rnap"
    public final static int HOLIDAY = 3; //�nnepnap: janu�r 1., m�rcius 15., stb.
    public final static int REST_BEFORE_WORK = 4; //munkanapot megel�z� munkasz�neti nap: "vas�rnap" M�G NEM HASZN�LOM!!!
    public final static int WORK_BEFORE_REST = 5; //munkasz�neti napot megel�z� munkanap: "p�ntek" M�G NEM HASZN�LOM!!!
    
    //--- �nnepnapok:
    //janu�r 1.
    //m�rcius 15.
    //h�sv�t vas�rnap
    //h�sv�t h�tf�
    //m�jus 1.
    //p�nk�sd vas�rnap
    //p�nk�sd h�tf�
    //augusztus 20.
    //okt�ber 23.
    //november 1.
    //december 25.
    //december 26.
    
    //public MgxCalendarDayType() {
    //}
    
}
