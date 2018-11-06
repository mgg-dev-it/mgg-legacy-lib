package hu.mgx.db;

import java.sql.*;
import java.util.*;

public class DataBase
{

    private String sName = "";
    private String sDisplayName = "";
    private CONN conn = null;
    private Vector<TableDefinition> tableDefinitions = new Vector<TableDefinition>();

    public DataBase()
    {
    }

    public DataBase(CONN conn, String sName, String sDisplayName) throws DataBaseException
    {
        this.conn = conn;
        this.sName = sName;
        this.sDisplayName = sDisplayName;
    }

    public void setName(String s)
    {
        sName = s;
    }

    public String getName()
    {
        return (sName);
    }

    public void setDisplayName(String s)
    {
        sDisplayName = s;
    }

    public String getDisplayName()
    {
        return (sDisplayName);
    }

    public Connection getConnection()
    {
        return (conn.getConnection());
    }

    public CONN getConn()
    {
        return (conn);
    }

    public DBMS getDBMS()
    {
        //return (getConn().getDBMS());
        return (conn.getDBMS());
    }

    public String getContinueFieldName()
    {
        return (getConn().getDBMS().getContinueFieldName());
    }

    public TableDefinition addTableDefinition(String sName) throws TableDefinitionException
    {
        tableDefinitions.add(new TableDefinition(sName));
        return (tableDefinitions.lastElement());
    }

    public TableDefinition getTableDefinition(int i) throws DataBaseException
    {
        if ((i < 0) || (i > tableDefinitions.size() - 1))
        {
            throw new DataBaseException("Wrong table serial! (" + Integer.toString(i) + ")");
        }
        return (tableDefinitions.elementAt(i));
    }

    public TableDefinition getTableDefinitionByName(String sName) throws DataBaseException
    {
        for (int i = 0; i < tableDefinitions.size(); i++)
        {
            if (tableDefinitions.elementAt(i).getName().equals(sName))
            {
                return (tableDefinitions.elementAt(i));
            }
        }
        throw new DataBaseException("Wrong table name! (" + sName + ")");
    }

    public TableDefinition cloneTableDefinitionByName(String sName) throws DataBaseException
    {
        for (int i = 0; i < tableDefinitions.size(); i++)
        {
            if (tableDefinitions.elementAt(i).getName().equals(sName))
            {
                return (tableDefinitions.elementAt(i).clone());
            }
        }
        throw new DataBaseException("Wrong table name! (" + sName + ")");
    }

    public boolean fieldExists(String sFieldName, String sTableName)
    {
        try
        {
            conn.getConnection().prepareStatement(getConn().getDBMS().getSqlForFieldExists(sTableName, sFieldName)).execute();
        }
        catch (SQLException e)
        {
            return (false);
        }
        return (true);
    }
}
