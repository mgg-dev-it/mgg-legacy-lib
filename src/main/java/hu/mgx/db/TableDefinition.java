package hu.mgx.db;

import java.sql.*;
import java.util.*;

public class TableDefinition implements Cloneable {

    private String sName = "";
    private Vector fieldDefinitions = new Vector();
    private String sOrderBy = "";
    private RecordCheck recordChecker = null;

    public TableDefinition() {
    }

    public TableDefinition(String sName) {
        this.sName = sName;
    }

    public String getName() {
        return (sName);
    }

    public FieldDefinition addFieldDefinition(FieldDefinition fd) {
        fieldDefinitions.add(fd);
        return (fd);
    }

    public int getFieldCount() {
        return (fieldDefinitions.size());
    }

    public FieldDefinition getFieldDefinition(int i) throws TableDefinitionException {
        if ((i < 0) || (i > fieldDefinitions.size() - 1)) {
            throw new TableDefinitionException("Wrong field serial! (" + Integer.toString(i) + ")");
        }
        return ((FieldDefinition) fieldDefinitions.elementAt(i));
    }

    public FieldDefinition getFieldDefinitionByName(String sName) throws TableDefinitionException {
        for (int i = 0; i < fieldDefinitions.size(); i++) {
            //            System.err.println(((FieldDefinition)fieldDefinitions.elementAt(i)).getName());
            if (((FieldDefinition) fieldDefinitions.elementAt(i)).getName().equalsIgnoreCase(sName)) {
                return ((FieldDefinition) fieldDefinitions.elementAt(i));
            }
        }
        throw new TableDefinitionException("Unknown field name! (" + sName + ")");
    }

    public void executeSetFieldRealTimeSQLLookupValues(Connection connection) {
        String sSQL = "";
        String sLookup = "";
//        String sFieldName = "";
        PreparedStatement pst;
        ResultSet rs;

        for (int i = 0; i < fieldDefinitions.size(); i++) {
            sSQL = ((FieldDefinition) fieldDefinitions.elementAt(i)).getRealTimeSQLLookup();
            if (!sSQL.equals("")) {
                //System.err.println(sSQL);
                try {
                    //ResultSet rs = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sSQL);
                    pst = connection.prepareStatement(sSQL, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                    rs = pst.executeQuery();

                    sLookup = "";
                    while (rs.next()) {
                        if (!sLookup.equals("")) {
                            sLookup += "@";
                            //sLookup += encode(rs.getString("value").trim());
                        }
                        sLookup += rs.getString("value").trim();
                        sLookup += "|";
                        //sLookup += encode(rs.getString("display").trim());
                        //2009.07.07. sLookup += rs.getString("display").trim();
                        sLookup += hu.mgx.util.StringUtils.isNull(rs.getString("display"), "?").trim(); //2009.07.07.
                    }
                    pst.close();
                    pst = null;
                    rs.close();
                    rs = null;
                    //System.err.println(sLookup);
                    ((FieldDefinition) fieldDefinitions.elementAt(i)).setLookup(sLookup);
                } catch (SQLException e) {
                }
            }
        }
        return;
    }

    public String getLookupDisplayFromValue(String sLookup, String sValue) {
        String sNext = "";
        String sVal = "";
        String sDisplay = "";
        StringTokenizer st = new StringTokenizer(sLookup, "@", false);
        while (st.hasMoreTokens()) {
            sNext = st.nextToken();//.trim();
            //System.err.println(sNext);
            StringTokenizer st1 = new StringTokenizer(sNext, "|", false);
            if (st1.hasMoreTokens()) {
                sVal = st1.nextToken().trim();
                //System.err.println(sVal);
                sDisplay = st1.nextToken().trim();
                //System.err.println(sDisplay);
                if (sVal.equals(sValue)) {
                    return (sDisplay);
                }
            }
        }
        return (sValue);
    }

    public String getLookupValueFromDisplay(String sLookup, String sDisplay) {
        String sNext = "";
        String sDis = "";
        String sValue = "";
        StringTokenizer st = new StringTokenizer(sLookup, "@", false);
        while (st.hasMoreTokens()) {
            sNext = st.nextToken();//.trim();
            //System.err.println(sNext);
            StringTokenizer st1 = new StringTokenizer(sNext, "|", false);
            if (st1.hasMoreTokens()) {
                sValue = st1.nextToken().trim();
                //System.err.println(sVal);
                sDis = st1.nextToken().trim();
                //System.err.println(sDisplay);
                if (sDis.equals(sDisplay)) {
                    return (sValue);
                }
            }
        }
        return (sDisplay);
    }

    public java.lang.String getOrderBy() {
        return sOrderBy;
    }

    public void setOrderBy(java.lang.String sOrderBy) {
        this.sOrderBy = sOrderBy;
    }

    public TableDefinition clone() {
        TableDefinition newTD = new TableDefinition(sName);
        newTD.setOrderBy(sOrderBy);
        for (int i = 0; i < fieldDefinitions.size(); i++) {
            newTD.addFieldDefinition((FieldDefinition) fieldDefinitions.elementAt(i));
        }
        return (newTD);
    }

    public void setRecordChecker(RecordCheck recordChecker) {
        this.recordChecker = recordChecker;
    }

    public boolean checkRecord(java.awt.Component parentComponent, hu.mgx.sql.Record oldRecord, hu.mgx.sql.Record newRecord) {
        if (this.recordChecker == null) {
            return (true);
        }
        return (recordChecker.checkRecord(parentComponent, oldRecord, newRecord));
    }

    public boolean checkInsertRecord(java.awt.Component parentComponent, hu.mgx.sql.Record oldRecord, hu.mgx.sql.Record newRecord) {
        if (this.recordChecker == null) {
            return (true);
        }
        return (recordChecker.checkInsertRecord(parentComponent, oldRecord, newRecord));
    }

    public void debugStructure() {
        for (int i = 0; i < getFieldCount(); i++) {
            try {
                System.out.println(getName() + "." + getFieldDefinition(i).getDisplayName());
            } catch (TableDefinitionException ex) {
            }
        }
    }
}
