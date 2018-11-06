package hu.mgx.db;

import java.math.BigDecimal;
import java.sql.*;

public class RS
{

    private Connection conn = null;
    public ResultSet rs;
    private boolean bError = false;
    private boolean bAnyError = false;
    private boolean bLogError = false;
    private String sErrorMsg = "";

    public RS()
    {
        bError = false;
        bAnyError = false;
        bLogError = false;
    }

    public RS(Connection c)
    {
        bError = false;
        bAnyError = false;
        bLogError = false;
        conn = c;
    }

    public boolean getError()
    {
        return (bError);
    }

    public boolean getAnyError()
    {
        return (bAnyError);
    }

    public void setLogError()
    {
        bLogError = true;
    }

    public String getErrorMsg()
    {
        return (sErrorMsg);
    }

    public boolean moveNext()
    {
        boolean bRetVal = false;
        if (rs != null)
        {
            try
            {
                bRetVal = rs.next();
            }
            catch (SQLException e)
            {
                bRetVal = false;
            }
        }
        return (bRetVal);
    }

    public String executeSQL(String sSql, String sCaller)
    {
        bError = false;
        try
        {
            PreparedStatement st = conn.prepareStatement(sSql);
            st.execute();
        }
        catch (Exception e)
        {
            if (bLogError)
            {
                System.err.println(e.getLocalizedMessage());
                e.printStackTrace(System.err);
            }
            bError = true;
            bAnyError = true;
            sErrorMsg = sCaller + " error: " + e.getLocalizedMessage() + " [" + sSql + "]";
            return (sErrorMsg);
        }
        return ("");
    }

    public String executeQuery(String sSql, String sCaller)
    {
        bError = false;
        try
        {
            PreparedStatement st = conn.prepareStatement(sSql);
            rs = st.executeQuery();
        }
        catch (Exception e)
        {
            if (bLogError)
            {
                System.err.println(e.getLocalizedMessage());
                e.printStackTrace(System.err);
            }
            bError = true;
            bAnyError = true;
            sErrorMsg = sCaller + " error: " + e.getLocalizedMessage() + " [" + sSql + "]";
            return (sErrorMsg);
        }
        return ("");
    }

    private String getBytesStringNull(ResultSet rs, String sFieldName, String sNullValue) throws java.sql.SQLException
    {
        byte[] b;
        b = rs.getBytes(sFieldName);
        return (b != null ? new String(b).trim() : sNullValue);
    }

    private String getBytesString(ResultSet rs, String sFieldName) throws java.sql.SQLException
    {
        return (getBytesStringNull(rs, sFieldName, ""));
    }

    public int getIntField(String sFieldName)
    {
        bError = false;
        try
        {
            return (rs.getInt(sFieldName));
        }
        catch (SQLException e)
        {
            bError = true;
            bAnyError = true;
            System.err.println("RS.getIntField error: " + e.getLocalizedMessage());
            e.printStackTrace(System.err);
            return (0);
        }
    }

    public double getDoubleField(String sFieldName)
    {
        bError = false;
        try
        {
            return (rs.getDouble(sFieldName));
        }
        catch (SQLException e)
        {
            bError = true;
            bAnyError = true;
            System.err.println("RS.getDoubleField error: " + e.getLocalizedMessage());
            e.printStackTrace(System.err);
            return (0);
        }
    }

    public String getFieldNull(String sFieldName, String sFieldType, String sNullValue)
    {
        String sRetVal = "?";
        java.sql.Date d;
        java.sql.Time t;
        java.sql.Timestamp ts;
        int i;
        long l;
        float f;
        BigDecimal bd;
        bError = false;
        try
        {
            if (sFieldType.equals("string"))
            {
                sRetVal = rs.getString(sFieldName);
                if (rs.wasNull())
                {
                    sRetVal = sNullValue;
                }
            }
            if (sFieldType.equals("bytesstring"))
            {
                sRetVal = getBytesString(rs, sFieldName);
            //if (rs.wasNull()) sRetVal = "";
            }
            if (sFieldType.equals("int"))
            {
                i = rs.getInt(sFieldName);
                if (rs.wasNull())
                {
                    sRetVal = sNullValue;
                }
                else
                {
                    sRetVal = Integer.toString(i);
                }
            }
            if (sFieldType.equals("long"))
            {
                l = rs.getLong(sFieldName);
                if (rs.wasNull())
                {
                    sRetVal = sNullValue;
                }
                else
                {
                    sRetVal = Long.toString(l);
                }
            }
            if (sFieldType.equals("float"))
            {
                f = rs.getFloat(sFieldName);
                if (rs.wasNull())
                {
                    sRetVal = sNullValue;
                }
                else
                {
                    sRetVal = Float.toString(f);
                }
            }
            if (sFieldType.equals("numeric"))
            {
                bd = rs.getBigDecimal(sFieldName);
                if (rs.wasNull())
                {
                    sRetVal = sNullValue;
                }
                else
                {
                    sRetVal = bd.toString();
                }
            }
            if (sFieldType.equals("date"))
            {
                d = rs.getDate(sFieldName);
                if (rs.wasNull())
                {
                    sRetVal = sNullValue;
                }
                else
                {
                    sRetVal = d.toString();
                }
            }
            if (sFieldType.equals("time"))
            {
                t = rs.getTime(sFieldName);
                if (rs.wasNull())
                {
                    sRetVal = sNullValue;
                }
                else
                {
                    sRetVal = t.toString();
                }
            }
            if (sFieldType.equals("timestamp"))
            {
                ts = rs.getTimestamp(sFieldName);
                if (rs.wasNull())
                {
                    sRetVal = sNullValue;
                }
                else
                {
                    sRetVal = ts.toString();
                }
            }
            if (sFieldType.equals("datetime"))
            {
                ts = rs.getTimestamp(sFieldName);
                if (rs.wasNull())
                {
                    sRetVal = sNullValue;
                }
                else
                {
                    sRetVal = ts.toString();
                    if (sRetVal.endsWith(".0"))
                    {
                        sRetVal = sRetVal.substring(0, sRetVal.length() - 2);
                    }
                }
            }
        }
        catch (SQLException e)
        {
            bError = true;
            bAnyError = true;
            sRetVal = "*";
            sRetVal = sFieldName + "/" + sFieldType + " " + e.getLocalizedMessage();
        }
        //sRetVal = decode (sRetVal);
        sRetVal = sRetVal.trim();
        return (sRetVal);
    }

    public String getField(String sFieldName, String sFieldType)
    {
        return (getFieldNull(sFieldName, sFieldType, ""));
    }
}
