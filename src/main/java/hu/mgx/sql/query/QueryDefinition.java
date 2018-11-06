package hu.mgx.sql.query;

import java.util.*;

import hu.mgx.db.*;

public class QueryDefinition
{

    private String sName = "";
    private String sDescription = "";
    private Vector vSelect = new Vector();
    private Vector vColumnNames = new Vector();
    private Vector vFrom = new Vector();
    private Vector vRelation = new Vector();
    private Vector vWhere = new Vector();
    private Vector vOrderBy = new Vector();
    public final static boolean ASCENDING = true;
    public final static boolean DESCENDING = false;

    public QueryDefinition()
    {
        init();
    }

    public QueryDefinition(String sName, String sDescription)
    {
        this.sName = sName;
        this.sDescription = sDescription;
        init();
    }

    public java.lang.String getName()
    {
        return sName;
    }

    public void setName(java.lang.String sName)
    {
        this.sName = sName;
    }

    public java.lang.String getDescription()
    {
        return sDescription;
    }

    public void setDescription(java.lang.String sDescription)
    {
        this.sDescription = sDescription;
    }

    private void init()
    {
        vSelect = new Vector();
        vColumnNames = new Vector();
        vFrom = new Vector();
        vRelation = new Vector();
        vWhere = new Vector();
        vOrderBy = new Vector();
    }

    public void addSelect(String sTableName, String sFieldName)
    {
        addSelect(sTableName, sFieldName, sFieldName);
    }

    public void addSelect(String sTableName, String sFieldName, String sColumnName)
    {
        vSelect.add(new QuerySelect(sTableName, sFieldName));
        vColumnNames.add(sColumnName);
    }

    public void addSelect(String sTableName, String sFieldName, String sSubSelect, String sColumnName)
    {
        vSelect.add(new QuerySelect(sTableName, sFieldName, sSubSelect));
        vColumnNames.add(sColumnName);
    }

    public void modifySelect(String sTableName, String sFieldName, String sSubSelect)
    {
        QuerySelect querySelect = null;
        for (int i = 0; i < vSelect.size(); i++)
        {
            querySelect = (QuerySelect) vSelect.elementAt(i);
            if (querySelect.getTableName().equals(sTableName) && querySelect.getFieldName().equals(sFieldName))
            {
                querySelect.setSubSelect(sSubSelect);
            }
        }
    }

    public void addFrom(String sTableName)
    {
        vFrom.add(sTableName);
    }

    public void addRelation(String sTableName1, String sFieldName1, String sTableName2, String sFieldName2)
    {
        vRelation.add(sTableName1);
        vRelation.add(sFieldName1);
        vRelation.add(sTableName2);
        vRelation.add(sFieldName2);
    }

    public void addWhere(QueryWhere queryWhere)
    {
        vWhere.add(queryWhere);
    }

    public void addWhere(String sTableName, String sFieldName, String sCondition)
    {
        vWhere.add(new QueryWhere(sTableName, sFieldName, sCondition));
    }

    public void addOrderBy(String sTableName, String sFieldName)
    {
        addOrderBy(sTableName, sFieldName, true);
    }

    public void addOrderBy(String sTableName, String sFieldName, boolean bAscending)
    {
        vOrderBy.add(sTableName);
        vOrderBy.add(sFieldName);
        vOrderBy.add((bAscending ? "" : " desc"));
    }

    private boolean tableIsInFromClause(String sTableName)
    {
        for (int i = 0; i < vFrom.size(); i++)
        {
            if ((" " + vFrom.elementAt(i).toString() + " ").indexOf(" " + sTableName + " ") > -1)
            {
                return (true);
            }
        }
        return (false);
    }

    public String getSQL()
    {
        String sSQL = "";
        QuerySelect querySelect = null;
        QueryWhere queryWhere = null;
        boolean bWhere = false;
        sSQL += "select ";
        for (int i = 0; i < vSelect.size(); i++)
        {
            querySelect = (QuerySelect) vSelect.elementAt(i);
            sSQL += (i == 0 ? "" : ", ") + (querySelect.getSubSelect().equals("") ? querySelect.getTableName() + "." + querySelect.getFieldName() : querySelect.getSubSelect());
        }
        sSQL += " from ";
        for (int i = 0; i < vFrom.size(); i++)
        {
            sSQL += (i == 0 ? "" : ", ") + vFrom.elementAt(i).toString();
        }
        for (int i = 0; i < vRelation.size(); i += 4)
        {
            sSQL += (bWhere ? " and " : " where ") + vRelation.elementAt(i).toString() + "." + vRelation.elementAt(i + 1).toString() + "=" + vRelation.elementAt(i + 2).toString() + "." + vRelation.elementAt(i + 3).toString();
            bWhere = true;
        }
        for (int i = 0; i < vWhere.size(); i++)
        {
            queryWhere = (QueryWhere) vWhere.elementAt(i);
            if (queryWhere.getTableName().equals("") && queryWhere.getFieldName().equals(""))
            {
                sSQL += (bWhere ? " and " : " where ") + queryWhere.getCondition();
            }
            else
            {
                if (tableIsInFromClause(queryWhere.getTableName()))
                {
                    sSQL += (bWhere ? " and " : " where ") + queryWhere.getTableName() + "." + queryWhere.getFieldName() + " " + queryWhere.getCondition();
                }
            }
            bWhere = true;
        }
        if (vOrderBy.size() > 0)
        {
            sSQL += " order by ";
        }
        for (int i = 0; i < vOrderBy.size(); i += 3)
        {
            sSQL += (i == 0 ? "" : ", ") + (!vOrderBy.elementAt(i).toString().equals("") ? vOrderBy.elementAt(i).toString() + "." : "") + vOrderBy.elementAt(i + 1).toString() + vOrderBy.elementAt(i + 2).toString();
        }
        //System.out.println(sSQL);
        return (sSQL);
    }

    public TableDefinition getTableDefinition(DataBase db, String sQueryName)
    {
        QuerySelect querySelect = null;
        TableDefinition tableDefinition = new TableDefinition(sQueryName);
        for (int i = 0; i < vSelect.size(); i++)
        {
            querySelect = (QuerySelect) vSelect.elementAt(i);
            try
            {
                tableDefinition.addFieldDefinition(db.getTableDefinitionByName(querySelect.getTableName()).getFieldDefinitionByName(querySelect.getFieldName()));
            }
            catch (DataBaseException dbe)
            {
                System.err.println(dbe.getLocalizedMessage());
                dbe.printStackTrace(System.err);
            }
            catch (TableDefinitionException tde)
            {
                System.err.println(querySelect.getTableName());
                System.err.println(querySelect.getFieldName());
                System.err.println(tde.getLocalizedMessage());
                tde.printStackTrace(System.err);
            }
        }
        return (tableDefinition);
    }

    public Vector getColumnNames()
    {
        return (vColumnNames);
    }

    public Vector getTableNames()
    {
        return (vFrom);
    }
}
