package hu.mgx.db;

public class DBMS
{

    public static final int DEFAULT = 0;
    public static final int MSSQL = 1;
    public static final int MYSQL = 2;
    public static final int JAVADB = 3;
    private int iDBMS = 0;

    public DBMS()
    {
        this(DEFAULT);
    }

    public DBMS(int iDBMS)
    {
        this.iDBMS = iDBMS;
    }

    public static int findByName(String sDbmsName)
    {
        if (sDbmsName.equalsIgnoreCase("mssql"))
        {
            return (MSSQL);
        }
        if (sDbmsName.equalsIgnoreCase("mysql"))
        {
            return (MYSQL);
        }
        if (sDbmsName.equalsIgnoreCase("javadb"))
        {
            return (JAVADB);
        }
        return (DEFAULT);
    }

    public String getContinueFieldName()
    {
        String sRetVal = "";
        switch (iDBMS)
        {
            case MYSQL:
                sRetVal = "continue";
                break;
            case DEFAULT:
            case MSSQL:
            default:
                sRetVal = "[continue]";
                break;
        }
        return (sRetVal);
    }

    public String getSqlForIsNull(String sExpression, String sReplacement)
    {
        String sRetVal = "";
        switch (iDBMS)
        {
            case MYSQL:
                sRetVal = "ifnull(" + sExpression + "," + sReplacement + ")";
                break;
            case DEFAULT:
            case MSSQL:
            default:
                sRetVal = "isnull(" + sExpression + "," + sReplacement + ")";
                break;
        }
        return (sRetVal);
    }

    public String getSqlForNow()
    {
        String sRetVal = "";
        switch (iDBMS)
        {
            case MYSQL:
                sRetVal = "now()";
                break;
            case DEFAULT:
            case MSSQL:
            default:
                sRetVal = "getDate()";
                break;
        }
        return (sRetVal);
    }

    public String getSqlForNullDate()
    {
        String sRetVal = "";
        switch (iDBMS)
        {
            case MYSQL:
                sRetVal = "'0000-00-00'";
                break;
            case DEFAULT:
            case MSSQL:
            default:
                sRetVal = "null";
                break;
        }
        return (sRetVal);
    }

    public String getSqlForLeast(String sA, String sB)
    {
        String sRetVal = "";
        switch (iDBMS)
        {
            case MYSQL:
                sRetVal = "least(" + sA + "," + sB + ")";
                break;
            case DEFAULT:
            case MSSQL:
            default:
                sRetVal = "case when " + sA + "<" + sB + " then " + sA + " else " + sB + " end";
                break;
        }
        return (sRetVal);
    }

    public String getSqlForGreatest(String sA, String sB)
    {
        String sRetVal = "";
        switch (iDBMS)
        {
            case MYSQL:
                sRetVal = "greatest(" + sA + "," + sB + ")";
                break;
            case DEFAULT:
            case MSSQL:
            default:
                sRetVal = "case when " + sA + ">" + sB + " then " + sA + " else " + sB + " end";
                break;
        }
        return (sRetVal);
    }

    public String getSqlForConcat3(String sA, String sB, String sC)
    {
        String sRetVal = "";
        switch (iDBMS)
        {
            case MYSQL:
                sRetVal = "concat(" + sA + "," + sB + "," + sC + ")";
                break;
            case DEFAULT:
            case MSSQL:
            default:
                sRetVal = sA + "+" + sB + "+" + sC;
                break;
        }
        return (sRetVal);
    }

    public String getSqlForConcat7(String sA, String sB, String sC, String sD, String sE, String sF, String sG)
    {
        String sRetVal = "";
        switch (iDBMS)
        {
            case MYSQL:
                sRetVal = "concat(" + sA + "," + sB + "," + sC + "," + sD + "," + sE + "," + sF + "," + sG + ")";
                break;
            case DEFAULT:
            case MSSQL:
            default:
                sRetVal = sA + "+" + sB + "+" + sC + "+" + sD + "+" + sE + "+" + sF + "+" + sG;
                break;
        }
        return (sRetVal);
    }

    public String getSqlForTimediff(String sA, String sB)
    {
        String sRetVal = "";
        switch (iDBMS)
        {
            case MYSQL:
                sRetVal = "timediff(" + sA + "," + sB + ")";
                break;
            case DEFAULT:
            case MSSQL:
            default:
                sRetVal = "(" + sA + " - " + sB + ")";
                break;
        }
        return (sRetVal);
    }

    public String getSqlForFieldExists(String sTableName, String sFieldName)
    {
        String sRetVal = "";
        switch (iDBMS)
        {
            case MYSQL:
                sRetVal = "select " + sFieldName + " from " + sTableName + " limit 1";
                break;
            case DEFAULT:
            case MSSQL:
            default:
                sRetVal = "select top 1 " + sFieldName + " from " + sTableName;
                break;
        }
        return (sRetVal);
    }
}
