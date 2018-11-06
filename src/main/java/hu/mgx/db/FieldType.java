package hu.mgx.db;

public abstract class FieldType
{

    public final static int NULL = 0;
    public final static int INTEGER = 10; //java.lang.Integer
    public final static int INT = INTEGER;
    public final static int CHARACTER = 20; //java.lang.String
    public final static int CHAR = CHARACTER;
    public final static int PHONE = 21;
    public final static int DATE = 30; //java.sql.Date
    public final static int TIME = 04; //java.sql.Time
    public final static int DATETIME = 50; //java.sql.Timestamp
    public final static int TIMESTAMP = 60; //java.sql.Timestamp
    public final static int BOOLEAN = 70; //java.lang.Boolean
    public final static int BIT = 71; //java.lang.Boolean
    public final static int DECIMAL = 100; //java.lang.Double
    public final static int FLOAT = 101; //java.lang.Float
    public final static int PASSWORD = 999; //java.lang.String
    public final static int TINYBLOB = 1001;
    public final static int BLOB = 1002;
    public final static int MEDIUMBLOB = 1003;
    public final static int LONGBLOB = 1004;

    public final static String getDisplayName(int iFieldType)
    {
        switch (iFieldType)
        {
            case FieldType.NULL:
                return ("NULL");
            case FieldType.INT:
                return ("INT");
            case FieldType.CHAR:
                return ("CHAR");
            case FieldType.PHONE:
                return ("PHONE");
            case FieldType.PASSWORD:
                return ("PASSWORD");
            case FieldType.DATE:
                return ("DATE");
            case FieldType.TIME:
                return ("TIME");
            case FieldType.DATETIME:
                return ("DATETIME");
            case FieldType.TIMESTAMP:
                return ("TIMESTAMP");
            case FieldType.BOOLEAN:
                return ("BOOLEAN");
            case FieldType.BIT:
                return ("BIT");
            case FieldType.DECIMAL:
                return ("DECIMAL");
            case FieldType.FLOAT:
                return ("FLOAT");
            case FieldType.TINYBLOB:
                return ("TINYBLOB");
            case FieldType.BLOB:
                return ("BLOB");
            case FieldType.MEDIUMBLOB:
                return ("MEDIUMBLOB");
            case FieldType.LONGBLOB:
                return ("LONGBLOB");
            default:
                return ("ismeretlen");
        }
    }
}
