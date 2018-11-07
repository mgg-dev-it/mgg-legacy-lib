package hu.mgx.sql;

import hu.mgx.app.common.*;

import java.sql.*;
import java.util.*;

import hu.mgx.db.*;

public class Record
{

    private Vector vFields = new Vector();
    private TableDefinition td;

    public Record(TableDefinition td)
    {
        this.td = td;
        init();
    }

    private void init()
    {
        for (int i = 0; i < td.getFieldCount(); i++)
        {
            vFields.add(new Object());
        }
    }

    public void setFromRs(TableDefinition td, ResultSet rs)
    {
        int iFieldType = FieldType.NULL;
        String sFieldName = "";
        for (int i = 0; i < td.getFieldCount(); i++)
        {
            try
            {
                iFieldType = td.getFieldDefinition(i).getType();
                sFieldName = td.getFieldDefinition(i).getName();
                switch (iFieldType)
                {
                    case FieldType.INT:
                        vFields.setElementAt(new Integer(rs.getInt(sFieldName)), i);
                        break;

                    case FieldType.CHAR:
                    case FieldType.PHONE:
                    case FieldType.PASSWORD:
                        vFields.setElementAt(rs.getString(sFieldName), i);
                        break;

                    case FieldType.DATE:
                        vFields.setElementAt(rs.getDate(sFieldName), i);
                        break;

                    case FieldType.TIME:
                        break;

                    case FieldType.DATETIME:
                        break;

                    case FieldType.TIMESTAMP:
                        break;

                    case FieldType.BOOLEAN:
                        break;

                    case FieldType.BIT:
                        break;

                    case FieldType.DECIMAL:
                        break;

                    case FieldType.FLOAT:
                        break;

                    case FieldType.TINYBLOB:
                        break;

                    case FieldType.BLOB:
                        break;

                    case FieldType.MEDIUMBLOB:
                        break;

                    case FieldType.LONGBLOB:
                        break;
                    default:
                        break;
                }
            }
            catch (TableDefinitionException te)
            {
                vFields.setElementAt(new String(""), i); //@todo ez jó így???
            }
            catch (SQLException sqle)
            {
                vFields.setElementAt(new String(""), i); //@todo ez jó így???
            }
        }
    }

    public void setField(int iFieldNumber, Object o)
    {
        vFields.setElementAt(o, iFieldNumber);
    }

    public Object getFieldValue(int iFieldNumber)
    {
        return (vFields.elementAt(iFieldNumber));
    }

    public Record copy()
    {
        Record newRecord = new Record(td);
        for (int i = 0; i < vFields.size(); i++)
        {
            newRecord.setField(i, this.getFieldValue(i));
        }
        return (newRecord);
    }

    private String dumpField(Object o, int iFieldType)
    {
        if (o == null)
        {
            return ("NULL");
        }
        switch (iFieldType)
        {
            case FieldType.NULL:
                return ("NULL");
            case FieldType.INT:
                return (((Integer) o).toString());
            case FieldType.CHAR:
                return (((String) o).toString());
            case FieldType.PHONE:
                return ("PHONE");
            case FieldType.PASSWORD:
                return ("PASSWORD");
            case FieldType.DATE:
                return (((java.util.Date) o).toString());
            case FieldType.TIME:
                return ("TIME");
            case FieldType.DATETIME:
                return (new Timestamp(((java.util.Date) o).getTime()).toString());
            case FieldType.TIMESTAMP:
                return ("TIMESTAMP");
            case FieldType.BOOLEAN:
                return ("BOOLEAN");
            case FieldType.BIT:
                return (((Integer) o).toString());
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
                return ("unknown");
        }
    }

    public void dump()
    {
        for (int i = 0; i < vFields.size(); i++)
        {
            try
            {
                System.out.println(td.getFieldDefinition(i).getName() + "(" + FieldType.getDisplayName(td.getFieldDefinition(i).getType()) + ") = " + dumpField(this.getFieldValue(i), td.getFieldDefinition(i).getType()));
            }
            catch (TableDefinitionException te)
            {
                System.out.println(this.getFieldValue(i).toString());
            }
        }
    }

    public void logDump(AppInterface appInterface)
    {
        for (int i = 0; i < vFields.size(); i++)
        {
            try
            {
                appInterface.logLine(td.getFieldDefinition(i).getName() + "(" + FieldType.getDisplayName(td.getFieldDefinition(i).getType()) + ") = " + dumpField(this.getFieldValue(i), td.getFieldDefinition(i).getType()));
            }
            catch (TableDefinitionException te)
            {
                appInterface.handleError(te);
            }
        }
    }
}
