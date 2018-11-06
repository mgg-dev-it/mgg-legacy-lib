package hu.mgx.report;

public class CellType
{

    public final static int NULL = 0;
    public final static int INTEGER = 10; //java.lang.Integer
    public final static int INT = INTEGER;
    public final static int CHARACTER = 20; //java.lang.String
    public final static int CHAR = CHARACTER;
    public final static int DATE = 30; //java.sql.Date
    public final static int TIME = 04; //java.sql.Time
    public final static int DATETIME = 50; //java.sql.Timestamp
    public final static int TIMESTAMP = 60; //java.sql.Timestamp
    public final static int BOOLEAN = 70; //java.lang.Boolean
    public final static int BIT = 71; //java.lang.Boolean
    public final static int DECIMAL = 100; //java.lang.Double
    public final static int FLOAT = 101; //java.lang.Float
    public final static int PASSWORD = 999; //java.lang.String
    public final static int SPECIAL_STATIC = 1000;
    public final static int SPECIAL_PAGE = 1001;
    public final static int SPECIAL_DATE = 1002;
    public final static int SPECIAL_TIME = 1003;
    public final static int SPECIAL_DATETIME = 1004;
    public final static int SPECIAL_LINE = 1005;
    public final static int SPECIAL_DOTTED_LINE = 1007;
    public final static int SPECIAL_COMPOUND = 1006;
}
