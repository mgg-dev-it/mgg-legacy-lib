package hu.mgx.db;

public interface FieldCheck
{

    public boolean checkValue(java.awt.Component parentComponent, Object value);

    public boolean checkModifiedValue(java.awt.Component parentComponent, Object value, Object oldValue);

    public boolean checkInsertValue(java.awt.Component parentComponent, Object value);
}
