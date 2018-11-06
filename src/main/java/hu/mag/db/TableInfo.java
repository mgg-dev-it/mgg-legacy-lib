package hu.mag.db;

import java.util.HashMap;
import java.util.Vector;

public class TableInfo {

    private String sName = "";
    private String sTableName = "";
    private String sDisplayName = "";
    private String sOrderBy = "";
    private String sExtended = "";
    private HashMap<String, Integer> hmFieldInfo = new HashMap<String, Integer>();
    private Vector<FieldInfo> vFieldInfo = new Vector<FieldInfo>();
    private TableInfoPlus tableInfoPlus;

    public TableInfo(String sName, String sTableName, String sDisplayName, String sOrderBy, String sExtended) {
        this.sName = sName;
        this.sTableName = sTableName;
        this.sDisplayName = sDisplayName;
        this.sOrderBy = sOrderBy;
        this.sExtended = sExtended;
        this.tableInfoPlus = null;
    }

    public String getName() {
        return sName;
    }

    public void setName(String sName) {
        this.sName = sName;
    }

    public String getTableName() {
        return sTableName;
    }

    public void setTableName(String sTableName) {
        this.sName = sTableName;
    }

    public String getDisplayName() {
        return sDisplayName;
    }

    public void setDisplayName(String sDisplayName) {
        this.sDisplayName = sDisplayName;
    }

    public String getOrderBy() {
        return sOrderBy;
    }

    public void setOrderBy(String sOrderBy) {
        this.sOrderBy = sOrderBy;
    }

    public void addFieldInfo(FieldInfo fi) {
        //hmFieldInfo.put(fi.getName(), fi);
        vFieldInfo.add(fi);
        hmFieldInfo.put(fi.getName(), new Integer(vFieldInfo.size() - 1));
    }

    public FieldInfo getFieldInfo(String sName) {
        Integer intIndex = hmFieldInfo.get(sName);
        if (intIndex == null) {
            return (null);
        }
        return (vFieldInfo.elementAt(intIndex.intValue()));
    }

    public FieldInfo getFieldInfo(int index) {
        if (index < 0 || index >= getFieldCount()) {
            return (null);
        }
        return (vFieldInfo.elementAt(index));
    }

    public int getFieldIndexByName(String sName) {
        Integer intIndex = hmFieldInfo.get(sName);
        if (intIndex == null) {
            return (-1);
        }
        return (intIndex.intValue());
    }

    public int getFieldCount() {
        return (vFieldInfo.size());
    }

    public boolean isExtendedScrollToLast() {
        return (sExtended.toLowerCase().contains("scrolltolast"));
    }

    public void setTableInfoPlus(TableInfoPlus tableInfoPlus) {
        this.tableInfoPlus = tableInfoPlus;
    }

    public TableInfoPlus getTableInfoPlus() {
        return (this.tableInfoPlus);
    }
}
