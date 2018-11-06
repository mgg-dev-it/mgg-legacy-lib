package hu.mgx.sql.query;

public class QuerySelect
{

    private String sTableName;
    private String sFieldName;
    private String sSubSelect;

    public QuerySelect(String sTableName, String sFieldName)
    {
        this.sTableName = sTableName;
        this.sFieldName = sFieldName;
        this.sSubSelect = "";
    }

    public QuerySelect(String sTableName, String sFieldName, String sSubSelect)
    {
        this.sTableName = sTableName;
        this.sFieldName = sFieldName;
        this.sSubSelect = sSubSelect;
    }

    public java.lang.String getTableName()
    {
        return sTableName;
    }

    public void setTableName(java.lang.String sTableName)
    {
        this.sTableName = sTableName;
    }

    public java.lang.String getFieldName()
    {
        return sFieldName;
    }

    public void setFieldName(java.lang.String sFieldName)
    {
        this.sFieldName = sFieldName;
    }

    public java.lang.String getSubSelect()
    {
        return sSubSelect;
    }

    public void setSubSelect(java.lang.String sSubSelect)
    {
        this.sSubSelect = sSubSelect;
    }
}
