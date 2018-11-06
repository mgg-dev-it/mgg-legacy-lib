package hu.mgx.sql.query;

public class QueryWhere
{

    private String sTableName;
    private String sFieldName;
    private String sCondition;

    public QueryWhere(String sTableName, String sFieldName, String sCondition)
    {
        this.sTableName = sTableName;
        this.sFieldName = sFieldName;
        this.sCondition = sCondition;
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

    public java.lang.String getCondition()
    {
        return sCondition;
    }

    public void setCondition(java.lang.String sCondition)
    {
        this.sCondition = sCondition;
    }
}
