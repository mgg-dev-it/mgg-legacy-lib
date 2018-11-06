package hu.mgx.db;

public interface RecordCheck
{

    public boolean checkRecord(java.awt.Component parentComponent, hu.mgx.sql.Record oldRecord, hu.mgx.sql.Record newRecord);

    public boolean checkInsertRecord(java.awt.Component parentComponent, hu.mgx.sql.Record oldRecord, hu.mgx.sql.Record newRecord);
}
