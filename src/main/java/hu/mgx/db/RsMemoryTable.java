package hu.mgx.db;

//--- Nincs kipróbálva!!!
import hu.mgx.app.common.AppInterface;
import java.sql.*;
import java.util.*;
import hu.mgx.swing.table.*;
import hu.mgx.util.StringUtils;
import javax.swing.JOptionPane;

public class RsMemoryTable extends MemoryTable
{

    private ResultSet resultSet = null;
    private ResultSetMetaData resultSetMetaData = null;
    private TableDefinition tableDefinition = null;
    private Vector vColumnNames = null;
    private int iMaxRecords = -1;
    private AppInterface appInterface = null;

    public RsMemoryTable(ResultSet resultSet)
    {
        super();
        this.resultSet = resultSet;
        init();
    }

    public RsMemoryTable(ResultSet resultSet, TableDefinition tableDefinition)
    {
        this(resultSet, tableDefinition, null, -1, null);
//        super();
//        this.resultSet = resultSet;
//        this.tableDefinition = tableDefinition;
//        init();
    }

    public RsMemoryTable(ResultSet resultSet, TableDefinition tableDefinition, int iMaxRecords)
    {
        this(resultSet, tableDefinition, null, iMaxRecords, null);
    }

    public RsMemoryTable(ResultSet resultSet, Vector vColumnNames)
    {
        this(resultSet, null, vColumnNames, -1, null);
//        super();
//        this.resultSet = resultSet;
//        this.vColumnNames = vColumnNames;
//        init();
    }

    public RsMemoryTable(ResultSet resultSet, Vector vColumnNames, int iMaxRecords)
    {
        this(resultSet, null, vColumnNames, iMaxRecords, null);
    }

    public RsMemoryTable(ResultSet resultSet, Vector vColumnNames, int iMaxRecords, AppInterface appInterface)
    {
        this(resultSet, null, vColumnNames, iMaxRecords, appInterface);
    }

    public RsMemoryTable(ResultSet resultSet, TableDefinition tableDefinition, Vector vColumnNames, int iMaxRecords, AppInterface appInterface)
    {
        super();
        this.resultSet = resultSet;
        this.tableDefinition = tableDefinition;
        this.vColumnNames = vColumnNames;
        this.iMaxRecords = iMaxRecords;
        this.appInterface = appInterface;
        init();
    }

    private void init()
    {
        int iColumnCount;
        int iColumn;
        Vector vColumnIdentifiers = new Vector();
        Vector vRowData;// = new Vector();
        //Vector vAllData = new Vector();
        //vAllData = new Vector();
        int iRowCount = 0;
        boolean bMax = false;
        try
        {
            resultSetMetaData = resultSet.getMetaData();
            iColumnCount = resultSetMetaData.getColumnCount();
            for (iColumn = 0; iColumn < iColumnCount; iColumn++)
            {
                //System.err.println(resultSetMetaData.getColumnType(iColumn+1));
                if (vColumnNames != null)
                {
                    vColumnIdentifiers.add(vColumnNames.elementAt(iColumn));
                }
                else if (tableDefinition != null)
                {
                    try
                    {
                        vColumnIdentifiers.add(tableDefinition.getFieldDefinition(iColumn).getDisplayName());
                    }
                    catch (TableDefinitionException tde)
                    {
                        vColumnIdentifiers.add("?");
                    }
                }
                else
                {
                    vColumnIdentifiers.add(resultSetMetaData.getColumnName(iColumn + 1));
                }
            }
            super.setColumnIdentifiers(vColumnIdentifiers);
            while (resultSet.next() && !bMax)
            {
                vRowData = new Vector(iColumnCount);
                //vRowData.clear();
                for (iColumn = 0; iColumn < resultSetMetaData.getColumnCount(); iColumn++)
                {
                    //if (resultSetMetaData.getColumnName(iColumn + 1).equals("call")){
                    //    System.out.println(hu.mgx.util.StringUtils.isNull(resultSet.getObject(iColumn + 1), "NULL"));
                    //}
                    if (resultSetMetaData.getColumnType(iColumn + 1) == java.sql.Types.INTEGER)
                    {
                        vRowData.add(new Integer(resultSet.getInt(iColumn + 1)));
                    //System.err.println(vRowData.elementAt(vRowData.size()-1).toString());
                    }
                    else
                    {
                        vRowData.add(resultSet.getObject(iColumn + 1));
                    //System.out.println(vRowData.elementAt(vRowData.size()-1).toString());
                    }
                }
//                super.addRow(vRowData);
                if ((++iRowCount > iMaxRecords) && (iMaxRecords > -1))
                {
                    bMax = true;
                }
                else
                {
                    super.addRow(vRowData);
                }
            }
        }
        catch (SQLException sqle)
        {
            System.err.println(sqle.getLocalizedMessage());
            sqle.printStackTrace(System.err);
//            return;
        }
//        finally{
//            vColumnIdentifiers.clear();
//        }
        if (bMax)
        {
            if (appInterface != null)
            {
                JOptionPane.showMessageDialog(null, StringUtils.stringReplace(appInterface.getLanguageString("Több, mint XXX rekordot választott ki, de megjeleníteni csak ennyit lehet. Szûkítse a kiválasztandókat a szelekcióval!"), "XXX", Integer.toString(iMaxRecords)), appInterface.getLanguageString("Figyelem!"), JOptionPane.WARNING_MESSAGE);
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Több, mint " + Integer.toString(iMaxRecords) + " rekordot választott ki, de megjeleníteni csak ennyit lehet. Szûkítse a kiválasztandókat a szelekcióval!", "Figyelem!", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    public void dump()
    {
        for (int i = 0; i < getColumnCount(); i++)
        {
            System.err.print(getColumnName(i) + "|");
        }
        System.err.println();
        for (int r = 0; r < getRowCount(); r++)
        {
            for (int i = 0; i < getColumnCount(); i++)
            {
                System.err.print((getValueAt(r, i) == null ? "NULL" : getValueAt(r, i).toString()) + "|");
            }
            System.err.println();
        }
    }
}
