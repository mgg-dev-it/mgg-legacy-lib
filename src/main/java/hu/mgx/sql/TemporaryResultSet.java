package hu.mgx.sql;

import java.sql.*;

import hu.mgx.app.common.*;

public class TemporaryResultSet
{

    private Connection connection = null;
    private ResultSet rs = null;
    private PreparedStatement pst = null;
    private ErrorHandlerInterface errorHandlerInterface = null;
    
    public TemporaryResultSet(Connection connection, ErrorHandlerInterface errorHandlerInterface)
    {
        this.connection = connection;
        this.errorHandlerInterface = errorHandlerInterface;
    }

    public ResultSet executeQuery(String sSQL)
    {
        try
        {
            pst = connection.prepareStatement(sSQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
        }
        catch (SQLException sqle)
        {
            if (errorHandlerInterface != null) errorHandlerInterface.handleError(sqle);
        }
        return (rs);
    }

    public ResultSet getResultSet()
    {
        return (rs);
    }

    public void close()
    {
        try
        {
            pst.close();
        }
        catch (SQLException sqle)
        {
            if (errorHandlerInterface != null) errorHandlerInterface.handleError(sqle);
        }
    }
}
