package hu.mag.db;

import java.util.HashMap;

public class DatabaseInfo {

    private String sName = "";
    private String sDisplayName = "";
    private String sManufacturer = "";
    private HashMap<String, TableInfo> hmTableInfo = new HashMap<String, TableInfo>();

    public DatabaseInfo(String sName, String sDisplayName) {
        this.sName = sName;
        this.sDisplayName = sDisplayName;
    }

    public String getName() {
        return sName;
    }

    public void setName(String sName) {
        this.sName = sName;
    }

    public String getDisplayName() {
        return sDisplayName;
    }

    public void setDisplayName(String sDisplayName) {
        this.sDisplayName = sDisplayName;
    }

    public TableInfo getTableInfo(String sName) {
        return (hmTableInfo.get(sName));
    }

    public void addTableInfo(TableInfo ti) {
        hmTableInfo.put(ti.getName(), ti);
    }

    public String getManufacturer() {
        return sManufacturer;
    }

    public void setManufacturer(String sManufacturer) {
        this.sManufacturer = sManufacturer;
    }

}
