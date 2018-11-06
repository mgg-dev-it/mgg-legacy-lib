package hu.mgx.swing.table;

import java.sql.*;
import java.util.*;

import javax.swing.table.*;

import hu.mgx.app.common.*;
import hu.mgx.db.*;
import hu.mgx.lang.*;
import hu.mgx.sql.*;

public class MgxTableModel extends DefaultTableModel
{

    private Connection conn = null;
    private TableDefinition td = null;
    private Vector ownColumnIdentifiers;
    private Vector vOriginRowData = null;
    private SQL sql;
    private AppInterface appInterface = null;

    public MgxTableModel(Connection conn, AppInterface appInterface)
    {
        super();
        this.conn = conn;
        this.appInterface = appInterface;
    }

    public MgxTableModel(Connection conn, TableDefinition td, AppInterface appInterface)
    {
        super();
        this.conn = conn;
        this.appInterface = appInterface;
        init(td);
    }

    public void init(TableDefinition td)
    {
        ownColumnIdentifiers = new Vector();
        this.td = td;
        setColumnCount(td.getFieldCount());
        try
        {
            for (int i = 0; i < td.getFieldCount(); i++)
            {
                ownColumnIdentifiers.add(td.getFieldDefinition(i).getDisplayName());
            }
        }
        catch (TableDefinitionException e)
        {
            System.err.println(e.getLocalizedMessage());
            e.printStackTrace(System.err);
        }
        setColumnIdentifiers(ownColumnIdentifiers);
        load(td);
    }

    public java.lang.Class getColumnClass(int columnIndex)
    {
        try
        {
            if (td.getFieldDefinition(columnIndex).getType() == hu.mgx.db.FieldType.INT)
            {
                return (java.lang.Integer.class);
            }
            if (td.getFieldDefinition(columnIndex).getType() == hu.mgx.db.FieldType.DATE)
            {
                return (java.util.Date.class);
            }
            if (td.getFieldDefinition(columnIndex).getType() == hu.mgx.db.FieldType.DATETIME)
            {
                return (java.util.Date.class);
            }
        }
        catch (TableDefinitionException e)
        {
            System.err.println(e.getLocalizedMessage());
            e.printStackTrace(System.err);
        }
        return (java.lang.Object.class);
    }

    public void load(TableDefinition td)
    {
        String sTmp = "";
        int iTmp = 0;
        java.sql.Date date;
        java.sql.Timestamp timestamp;
        java.util.Date udate;
        Vector vRow = null;
        this.td = td;
        try
        {
            setRowCount(0);
            sql = new SQL(new CONN(conn), appInterface);
            TemporaryResultSet trs = sql.doSelect(td);
            ResultSet rs = trs.getResultSet();
            while (rs.next())
            {
                vRow = new Vector();
                for (int i = 0; i < td.getFieldCount(); i++)
                {
                    if (td.getFieldDefinition(i).getType() == hu.mgx.db.FieldType.DATE)
                    {
                        date = rs.getDate(td.getFieldDefinition(i).getName());
                        if (!rs.wasNull())
                        {
                            udate = new java.util.Date(date.getTime());
                            vRow.add(udate);
                        }
                        else
                        {
                            vRow.add(null);
                        }
                    }
                    else if (td.getFieldDefinition(i).getType() == hu.mgx.db.FieldType.DATETIME)
                    {
                        timestamp = rs.getTimestamp(td.getFieldDefinition(i).getName());
                        if (!rs.wasNull())
                        {
                            udate = new java.util.Date(timestamp.getTime());
                            vRow.add(udate);
                        }
                        else
                        {
                            vRow.add(null);
                        }
                    }
                    else if (td.getFieldDefinition(i).getType() == hu.mgx.db.FieldType.INT)
                    {
                        iTmp = rs.getInt(td.getFieldDefinition(i).getName());
                        if (rs.wasNull())
                        {
                            vRow.add(null);
                        }
                        else
                        {
                            vRow.add(new Integer(iTmp));
                        }
                    }
                    else if (td.getFieldDefinition(i).getType() == hu.mgx.db.FieldType.DECIMAL)
                    {
                        vRow.add(new Double(rs.getDouble(td.getFieldDefinition(i).getName())));
                    }
                    else if (td.getFieldDefinition(i).getType() == hu.mgx.db.FieldType.MEDIUMBLOB)
                    {
                        vRow.add(new ByteArray(rs.getBytes(td.getFieldDefinition(i).getName())));
                    }
                    else
                    {
                        sTmp = rs.getString(td.getFieldDefinition(i).getName());
                        vRow.add(sTmp);
                    }
                }
                addRow(vRow);
            }
            rs.close();
            trs.close();
        }
        catch (SQLException e)
        {
            System.err.println(e.getLocalizedMessage());
            e.printStackTrace(System.err);
        }
        catch (Exception e)
        {
            System.err.println(e.getLocalizedMessage());
            e.printStackTrace(System.err);
        }
    }

    public boolean isCellEditable(int row, int column)
    {
        return (false);
    }

    private int translateRow(int row)
    {
        return (row);
    }

    public void saveOriginRow(int row1)
    {
        int row = translateRow(row1);
        if ((row < 0) || (row > getRowCount() - 1))
        {
            vOriginRowData = null;
            return;
        }
        vOriginRowData = new Vector();
        for (int i = 0; i < ((Vector) (getDataVector().elementAt(row))).size(); i++)
        {
            vOriginRowData.add(((Vector) (getDataVector().elementAt(row))).elementAt(i));
        }
    }

    public Object getValueAt(int aRow, int aColumn)
    {
        int i = translateRow(aRow);
        Vector rowVector = (Vector) dataVector.elementAt(i);
        return rowVector.elementAt(aColumn);
    }

    public void setValueAt(Object aValue, int aRow, int aColumn)
    {
        int i = translateRow(aRow);
        Vector rowVector = (Vector) dataVector.elementAt(i);
        rowVector.setElementAt(aValue, aColumn);
        fireTableCellUpdated(i, aColumn);
    }
}
