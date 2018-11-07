package hu.mag.swing.table;

import hu.mag.db.DatabaseInfo;
import hu.mag.db.FieldInfo;
import hu.mag.db.TableInfo;
import hu.mag.db.TableInfoPlus;
import hu.mag.lang.LookupInteger;
import hu.mag.swing.MagComboBoxField;
import hu.mag.swing.MagLookupTextField;
import hu.mgx.app.common.CommonAppUtils;
import hu.mgx.app.common.ConnectionHandlerInterface;
import hu.mgx.app.common.ErrorHandlerInterface;
import hu.mgx.app.swing.SwingAppInterface;
import hu.mgx.swing.table.MemoryTable;
import hu.mgx.util.StringUtils;
import java.awt.Color;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.Format;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class MagTableModel extends DefaultTableModel {

//    class CellIndex extends Object {
//
//        public int row;
//        public int column;
//
//        public CellIndex(int row, int column) {
//            this.row = row;
//            this.column = column;
//        }
//
//        public int getRow() {
//            return (row);
//        }
//
//        public int getColumn() {
//            return (column);
//        }
//
//        @Override
//        public boolean equals(Object obj) {
//            //return (this == obj);
//            //if (obj.getClass() == CellIndex.class) {
//            if (obj instanceof CellIndex) {
//                return (this.row == ((CellIndex) obj).row && this.column == ((CellIndex) obj).column);
//            } else {
//                return (false);
//            }
//        }
//
//        @Override
//        public int hashCode() {
//            int hash = 5;
//            hash = 59 * hash + this.row;
//            hash = 59 * hash + this.column;
//            return hash;
//        }
//
//        @Override
//        public String toString() {
//            return (getClass().getName() + "[row=" + this.row + ", column=" + this.column + "]");
//        }
//    }
    TableRowDefinition trd;
    private HashMap<Integer, Boolean> hmEditableColumns = new HashMap<Integer, Boolean>();
    private HashMap<Integer, Boolean> hmEditableRows = new HashMap<Integer, Boolean>();
    private HashMap<Integer, Boolean> hmSortableColumns = new HashMap<Integer, Boolean>();
    private HashMap<MagCellIndex, Boolean> hmEditableCells = new HashMap<MagCellIndex, Boolean>();

    private HashMap<Integer, Boolean> hmPrimaryKeyColumns = new HashMap<Integer, Boolean>();

    private HashMap<Integer, Boolean> hmRightAlignedColumns = new HashMap<Integer, Boolean>();
    private HashMap<Integer, Boolean> hmCenteredColumns = new HashMap<Integer, Boolean>();

    private HashMap<Integer, Boolean> hmHiddenColumns = new HashMap<Integer, Boolean>();

    private HashMap<Integer, String> hmColumnSpectypes = new HashMap<Integer, String>();

    public static final int ROW_STATUS_UNKNOWN = 0;
    public static final int ROW_STATUS_NEW = 1;
    public static final int ROW_STATUS_MODIFIED = 2;
    public static final int ROW_STATUS_OK = 3;
    public static final int ROW_STATUS_NEW_MODIFIED = 4;

//    public static final int ROW_STATUS_UNKNOWN = 0;
//    public static final int ROW_STATUS_NEW = 1;
//    public static final int ROW_STATUS_NEW_MODIFIED = 2;
//    public static final int ROW_STATUS_LOADED = 3;
//    public static final int ROW_STATUS_LOADED_MODIFIED = 4;
//    public static final int ROW_STATUS_SAVED = 5;
    private Vector<Integer> vRowStatus = new Vector<Integer>();
    private Vector<Color> vRowColorBackground = new Vector<Color>();
    private Vector<Vector<Color>> vCellColorBackground = new Vector<Vector<Color>>();
    private Vector<Vector<Color>> vCellColorForeground = new Vector<Vector<Color>>();
    private Vector<String> vOriginColumnNames = new Vector<String>();
    private Vector<String> vPrimaryKeyColumnNames = null;
    private Vector<Format> vColumnFormat = new Vector<Format>();
    private ResultSet rs = null;
    private ResultSetMetaData rsmd = null;
    private DatabaseMetaData dbmd = null;
    private int iIdentityColumn = -1;
    private String sPrimaryKeyColumnNames = "";
//    private boolean bPrimaryKeyExists = false;
    private String sTableName = "";
    private String sOrderBy = "";
//    private Vector<String> vFieldList = null; //@todo delete : not used
    private DatabaseInfo databaseInfo = null;
    private TableInfo tableInfo = null;
    private SwingAppInterface swingAppInterface = null;
    private Connection connection = null;
    private boolean bEditableTable = false;
    private boolean bUpdateableTable = false;
    private boolean bReadOnlyTable = false;

    private ErrorHandlerInterface ehi = null;

    private Vector<MagComboBoxField> vColumnLookupField = new Vector<MagComboBoxField>();
    private Vector<MagLookupTextField> vColumnLookupTableTextField = new Vector<MagLookupTextField>();
    private Vector<MagLookupTextField> vColumnLookupListTextField = new Vector<MagLookupTextField>();
    private boolean bNew = false;
    private boolean bIsLookupTable = false;
    private int iRowColorColumn = -1;

    private String[] filters;
    protected HashMap<String, String> filtersMap;

    public MagTableModel() {
        super();
    }

    public MagTableModel(TableRowDefinition trd) {
        super(0, trd.getColumnCount());
        this.trd = trd;
    }

    public MagTableModel(MemoryTable memoryTable) {
        this(memoryTable.getDataVector(), memoryTable.getColumnNames());
    }

    public MagTableModel(Vector vData, Vector vColumnNames) {
        super(vData, vColumnNames);
        trd = new TableRowDefinition();
        if (vData.size() < 1) {
            return; //MaG 2016.05.06.
        }
        Vector rowVector = (Vector) vData.elementAt(0);
        Object value = null;
        for (int i = 0; i < rowVector.size(); i++) {
            if (rowVector.elementAt(i) != null) {
                trd.addColumn(rowVector.elementAt(i).getClass());
            } else {
                value = null;
                for (int j = 0; j < vData.size() && value == null; j++) {
                    Vector rowVector2 = (Vector) vData.elementAt(j);
                    value = rowVector2.elementAt(i);
                    if (rowVector2.elementAt(i) != null) {
                        trd.addColumn(rowVector2.elementAt(i).getClass());
                    }
                }
                if (value == null) {
                    trd.addColumn(Object.class);
                }
            }
        }
        iRowColorColumn = -1;
        for (int i = 0; i < vColumnNames.size(); i++) {
            if (vColumnNames.elementAt(i).toString().equalsIgnoreCase("java_row_color")) {
                iRowColorColumn = i;
            }
        }
        for (int i = 0; i < vData.size(); i++) {
            vRowStatus.add(new Integer(ROW_STATUS_OK));
            Color colorRow = null;
            if (iRowColorColumn > -1) {
                colorRow = new Color(StringUtils.intValue(((Vector) vData.elementAt(i)).elementAt(iRowColorColumn).toString()));
            }
            vRowColorBackground.add(colorRow);
            vCellColorBackground.add(createColorVector(rowVector.size()));
            vCellColorForeground.add(createColorVector(rowVector.size()));
        }
        //System.out.println("1 " + vRowStatus.size());
    }

    public MagTableModel(ResultSet rs, ErrorHandlerInterface ehi, DatabaseInfo di) {
//        Vector vColumnNames = new Vector();
//        Vector vData = new Vector();
//        Vector vRecord = new Vector();
//        String sTableName = "";
//        String sFieldName = "";
//        String sFieldDisplayName = "";
//        FieldInfo fi = null;

        this.ehi = ehi;
        this.databaseInfo = di;

        refresh(rs);

//        if (rs == null) {
//            setDataVector(vData, vColumnNames);
//            return;
//        }
//        try {
//            //@todo wish: check, only one tablename, but it odesn't work in mssql
//            sTableName = rs.getMetaData().getTableName(1);
//            this.di = di;
//            if (di != null) {
//                ti = di.getTableInfo(sTableName);
//            }
//            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
//                sFieldDisplayName = rs.getMetaData().getColumnLabel(i + 1);//default...
//                if (ti != null) {
//                    sFieldName = rs.getMetaData().getColumnLabel(i + 1);
//                    fi = ti.getFieldInfo(sFieldName);
//                    if (fi != null) {
//                        sFieldDisplayName = fi.getDisplayName();
//                    }
//                }
//                vColumnNames.add(sFieldDisplayName);
////                System.out.println("Column #" + Integer.toString(i + 1) + " class: " + rs.getMetaData().getColumnClassName(i + 1));
//            }
//            while (rs.next()) {
//                vRecord = new Vector();
//                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
//                    vRecord.add(rs.getObject(i + 1));
//                }
//                vData.add(vRecord);
//            }
//            setDataVector(vData, vColumnNames);
//            for (int i = 0; i < vData.size(); i++) {
//                vRowStatus.add(new Integer(ROW_STATUS_OK));
//                vRowColorBackground.add(null);
//                vCellColorBackground.add(createColorVector(rs.getMetaData().getColumnCount()));
//            }
//
//            trd = new TableRowDefinition();
//            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
//                trd.addColumn(Class.forName(rs.getMetaData().getColumnClassName(i + 1)));
//            }
//        } catch (SQLException sqle) {
//            ehi.handleError(sqle);
//        } catch (ClassNotFoundException cnfe) {
//            ehi.handleError(cnfe);
//        }
    }

    public MagTableModel(SwingAppInterface swingAppInterface, String sConnectionName, String sTableName, DatabaseInfo di) {
        this.swingAppInterface = swingAppInterface;
        this.sTableName = sTableName;
        init(swingAppInterface, sConnectionName, sTableName, "", di);
    }

    public MagTableModel(SwingAppInterface swingAppInterface, String sConnectionName, String sTableName, String sOrderBy, DatabaseInfo di) {
        this.swingAppInterface = swingAppInterface;
        this.sTableName = sTableName;
        this.sOrderBy = sOrderBy;
        init(swingAppInterface, sConnectionName, sTableName, sOrderBy, di);
    }

    public MagTableModel(SwingAppInterface swingAppInterface, String sConnectionName, String sTableName, DatabaseInfo di, String[] filters) {
        this.swingAppInterface = swingAppInterface;
        this.sTableName = sTableName;
        this.filters = filters;
        filtersMap = CommonAppUtils.preProcessArgs(filters);
//        for (int i = 0; i < filters.length; i++) {
//            System.out.println(filters[i]);
//        }
        init(swingAppInterface, sConnectionName, sTableName, "", di);
    }

    public String[] getFilters() {
        return (filters);
    }
//    public MagTableModel(SwingAppInterface swingAppInterface, String sConnectionName, String sTableName, Vector<String> vFieldList, DatabaseInfo di) {
//        this.swingAppInterface = swingAppInterface;
//        this.sTableName = sTableName;
//        this.vFieldList = vFieldList;
//        init(swingAppInterface, sConnectionName, sTableName, vFieldList, di);
//    }
//    private void init(SwingAppInterface swingAppInterface, String sConnectionName, String sTableName, Vector<String> vFieldList, DatabaseInfo di) {

    private void init(SwingAppInterface swingAppInterface, String sConnectionName, String sTableName, String sOrderBy, DatabaseInfo di) {
        bEditableTable = true;
        bUpdateableTable = true;
        connection = swingAppInterface.getConnection(sConnectionName);
        PreparedStatement ps = null;
        ResultSet rspk = null;
        String sTmp = "";
        FieldInfo fi = null;
        String sFilter = "";
        String sSelectList = "";
        int iParameterIndex = 0;
        boolean bPKFieldFound = false;
        //String sOrderBy = "";
        sOrderBy = StringUtils.isNull(sOrderBy, "");

        if (di != null) {
            tableInfo = di.getTableInfo(sTableName);
        }
        if (tableInfo != null) {
            if (tableInfo.getTableName().length() > 0) {
                sTableName = tableInfo.getTableName();
            }
            for (int i = 0; i < tableInfo.getFieldCount(); i++) {
                fi = tableInfo.getFieldInfo(i);
                if (fi.getFilter() != null) {
                    sFilter += (sFilter.equals("") ? "" : " and ") + fi.getName() + "=?";
                }
                if (fi.isValidFromField()) {
                    sFilter += (sFilter.equals("") ? "" : " and ") + "(" + fi.getName() + " is null or " + fi.getName() + "<=getdate())";
                }
                if (fi.isValidToField()) {
                    sFilter += (sFilter.equals("") ? "" : " and ") + "(" + fi.getName() + " is null or " + fi.getName() + ">=getdate())";
                }
            }
            if (!sFilter.equals("")) {
                sFilter = " where " + sFilter;
            }
            //sOrderBy = tableInfo.getOrderBy();
            if (sOrderBy.length() == 0) {
                sOrderBy = tableInfo.getOrderBy();
            }
            //if (!sOrderBy.equals("")) {
            //    sOrderBy = " order by " + sOrderBy;
            //}
        }
        if (!sOrderBy.equals("")) {
            sOrderBy = " order by " + sOrderBy;
        }

        try {
            sSelectList = "";
            if (tableInfo != null) {
                for (int i = 0; i < tableInfo.getFieldCount(); i++) {
                    boolean bVisible = true;
                    if (!tableInfo.getFieldInfo(i).getControlSQL().equalsIgnoreCase("")) {
                        tableInfo.getFieldInfo(i).executeControlSQLIfExists(connection, swingAppInterface);
                        bVisible = tableInfo.getFieldInfo(i).isVisible();
                    }
                    if (bVisible) {
                        if (tableInfo.getFieldInfo(i).isVirtual()) {
                            String sVirtualValue = "'virtual'";
                            Object oVirtualValue = tableInfo.getFieldInfo(i).getVirtualValue();
                            if (oVirtualValue != null) {
                                sVirtualValue = "'" + oVirtualValue.toString() + "'";
                            }
                            String sVirtualValueSQL = tableInfo.getFieldInfo(i).getVirtualValueSQL();
                            if (!sVirtualValueSQL.equalsIgnoreCase("")) {
                                if (!sVirtualValueSQL.contains("[")) {
                                    sVirtualValue = "(" + sVirtualValueSQL + ")";
                                } else {
                                    Pattern regex = Pattern.compile("\\[[a-zA-Z0-9_]+\\]");
                                    Matcher regexMatcher = regex.matcher(sVirtualValueSQL);
                                    while (regexMatcher.find()) {
                                        //System.out.println(regexMatcher.group());
                                        //System.out.println(sVirtualValueSQL);
                                        sVirtualValueSQL = StringUtils.stringReplace(sVirtualValueSQL, regexMatcher.group(), "[" + Integer.toString(tableInfo.getFieldIndexByName(regexMatcher.group().replace("[", "").replace("]", ""))) + "]");
                                        //System.out.println(sVirtualValueSQL);
                                        //sVirtualValueSQL = StringUtils.stringReplace(sVirtualValueSQL, regexMatcher.group(), "[" + Integer.toString(tableInfo.getFieldIndexByName(regexMatcher.group().replace("[", "").replace("]", ""))) + "]"); //MaG 2017.05.25.
                                    }
                                    sVirtualValue = "'" + sVirtualValueSQL + "'";
                                    //sVirtualValue = "(" + sVirtualValueSQL + ")"; //MaG 2017.05.25.
                                }
                            }
                            sSelectList += (sSelectList.equalsIgnoreCase("") ? "" : ", ") + " " + sVirtualValue + " as " + tableInfo.getFieldInfo(i).getName();
                        } else {
                            sSelectList += (sSelectList.equalsIgnoreCase("") ? "" : ", ") + tableInfo.getFieldInfo(i).getName();
                        }
                    }
//                    String sControlSQL = ti.getFieldInfo(i).getControlSQL();
//                    if (!sControlSQL.equalsIgnoreCase("")) {
//                        ti.getFieldInfo(i).executeControlSQLIfExists(connection, appInterface);
//                        if (ti.getFieldInfo(i).isVisible()) {
//                            sSelectList += (sSelectList.equalsIgnoreCase("") ? "" : ", ") + ti.getFieldInfo(i).getName();
//                        }
//                    } else {
//                        sSelectList += (sSelectList.equalsIgnoreCase("") ? "" : ", ") + ti.getFieldInfo(i).getName();
//                    }
                }
            }
            if (sSelectList.trim().equalsIgnoreCase("")) {
                sSelectList = "*";
            }
            if (!sSelectList.equalsIgnoreCase("*")) {
                ps = connection.prepareStatement("select * from " + sTableName + " where 1=0");
                rs = ps.executeQuery();
                rsmd = rs.getMetaData();
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    if (rsmd.isAutoIncrement(i + 1)) {
                        if (tableInfo.getFieldInfo(rsmd.getColumnName(i + 1)) == null) { //an autoincrement column is missing from the field list
                            bEditableTable = false;
                            bUpdateableTable = false;
                        }
                    }
                    if (rsmd.isNullable(i + 1) == ResultSetMetaData.columnNoNulls) { //a "not null" column is missing from the field list
                        if (tableInfo.getFieldInfo(rsmd.getColumnName(i + 1)) == null) {
                            bEditableTable = false;
                            bUpdateableTable = false;
                        }
                    }
                }
                rsmd = null;
                rs.close();
                rs = null;
                ps.close();
                ps = null;
            }

            //System.out.println("select " + sSelectList + " from " + sTableName + " " + sFilter + " " + sOrderBy);
            ps = connection.prepareStatement("select " + sSelectList + " from " + sTableName + " " + sFilter + " " + sOrderBy);
            if (!sFilter.equals("")) {
                iParameterIndex = 0;
                for (int i = 0; i < tableInfo.getFieldCount(); i++) {
                    fi = tableInfo.getFieldInfo(i);
                    if (fi.getFilter() != null) {
                        ++iParameterIndex;
                        if (fi.getFilter().toString().startsWith("@global.")) {
                            ps.setObject(iParameterIndex, swingAppInterface.getGlobal(fi.getFilter().toString().substring("@global.".length())));
                        } else if (fi.getFilter().toString().startsWith("@local.") && filtersMap != null) {
//                            String sFilterValue = CommonAppUtils.getParameterValue(filtersMap, "--" + fi.getFilter().toString().substring("@local.".length())).trim();
//                            if (sFilterValue.length() > 0) {
//                                ps.setObject(iParameterIndex, sFilterValue);
//                            }
                            ps.setObject(iParameterIndex, CommonAppUtils.getParameterValue(filtersMap, "--" + fi.getFilter().toString().substring("@local.".length())));
                        } else {
                            ps.setObject(iParameterIndex, fi.getFilter());
                        }
                    }
                }
            }

            //swingAppInterface.setLogLevel(LoggerInterface.LOG_DEBUG);
            rs = ps.executeQuery();
            rsmd = rs.getMetaData();
            dbmd = connection.getMetaData();
            bPKFieldFound = true;
            //swingAppInterface.logLine("Primary key columns:", LoggerInterface.LOG_DEBUG);
            vPrimaryKeyColumnNames = new Vector<String>();
            rspk = dbmd.getPrimaryKeys(null, null, sTableName);
            //rspk = dbmd.getPrimaryKeys(null, "vughu255", sTableName);
            while (rspk.next()) {
                sTmp = rspk.getString("COLUMN_NAME");
                vPrimaryKeyColumnNames.add(sTmp);
                sPrimaryKeyColumnNames += "|" + sTmp;
                //swingAppInterface.logLine("PK column: " + vPrimaryKeyColumnNames.elementAt(vPrimaryKeyColumnNames.size() - 1), LoggerInterface.LOG_DEBUG);
                if (tableInfo != null) {
                    for (int i = 0; i < rsmd.getColumnCount(); i++) {
                        if (tableInfo.getFieldInfo(sTmp) == null) { //a primary key column is missing from the field list
                            bPKFieldFound = false;
                        }
                    }
                }
            }

            //MaG 2017.07.24.
            if (vPrimaryKeyColumnNames.size() == 0 && swingAppInterface.getDBMSType(sConnectionName) == ConnectionHandlerInterface.DBMS_JPROXY) {
                PreparedStatement psKey = connection.prepareStatement("select col.column_name from user_constraints uc join user_cons_columns col on col.table_name = uc.table_name and col.constraint_name = uc.constraint_name where uc.constraint_type = 'P' and uc.table_name='" + sTableName.toUpperCase() + "'");
                ResultSet rsKey = psKey.executeQuery();
                while (rsKey.next()) {
                    sTmp = rsKey.getString("COLUMN_NAME");
                    vPrimaryKeyColumnNames.add(sTmp);
                    sPrimaryKeyColumnNames += "|" + sTmp;
                }
//                vPrimaryKeyColumnNames.add("GOBETNR");
//                vPrimaryKeyColumnNames.add("GOSECTION");
//                vPrimaryKeyColumnNames.add("GOKEY");
//                sPrimaryKeyColumnNames = "|GOBETNR|GOSECTION|GOKEY";
            }

            if (!bPKFieldFound) {
                bEditableTable = false;
                bUpdateableTable = false;
            }
            sPrimaryKeyColumnNames += "|";
            //swingAppInterface.logLine("primary key colums: " + sPrimaryKeyColumnNames, LoggerInterface.LOG_DEBUG);
            for (int i = 0; i < rsmd.getColumnCount(); i++) {
                if (rsmd.isAutoIncrement(i + 1)) {
                    iIdentityColumn = i;
                }
                if (sPrimaryKeyColumnNames.indexOf(rsmd.getColumnLabel(i + 1)) > -1) {
                    //swingAppInterface.logLine("primary key column #" + Integer.toString(i), LoggerInterface.LOG_DEBUG);
                    this.setColumnPrimaryKey(i, true);
                }
            }
            if (vPrimaryKeyColumnNames.size() == 0) {
                bEditableTable = false;
                bUpdateableTable = false;
            }
        } catch (SQLException sqle) {
            swingAppInterface.handleError(sqle);
        }

        Vector vColumnNames = new Vector();
        Vector vData = new Vector();
        Vector vRecord = new Vector();
        String sFieldName = "";
        String sFieldDisplayName = "";

        try {
            //- columnnames
            for (int i = 0; i < rsmd.getColumnCount(); i++) {
                sFieldName = rsmd.getColumnLabel(i + 1);
                sFieldDisplayName = sFieldName;
                vOriginColumnNames.add(sFieldName);
                if (tableInfo != null) {
                    fi = tableInfo.getFieldInfo(sFieldName);
                    if (fi != null) {
                        sFieldDisplayName = fi.getDisplayName();
                        if (sFieldDisplayName.contains("[CRLF]")) {
                            sFieldDisplayName = "<html><center>" + StringUtils.stringReplace(sFieldDisplayName, "[CRLF]", "<br>") + "</html>";
                        }
                    }
                }
                vColumnNames.add(sFieldDisplayName);
//                System.out.println("Column #" + Integer.toString(i + 1) + " class: " + rs.getMetaData().getColumnClassName(i + 1));
            }

            //- default column formatters
            for (int i = 0; i < rsmd.getColumnCount(); i++) {
                vColumnFormat.add(null);
                //if (Class.forName(rsmd.getColumnClassName(i + 1)).equals(java.lang.Short.class)) {
                if (rsmd.getColumnClassName(i + 1).equals(java.lang.Short.class.getName())) {
                    vColumnFormat.setElementAt(swingAppInterface.getDecimalFormat(), i);
                }
                //if (Class.forName(rsmd.getColumnClassName(i + 1)).equals(java.lang.Integer.class)) {
                if (rsmd.getColumnClassName(i + 1).equals(java.lang.Integer.class.getName())) {
                    vColumnFormat.setElementAt(swingAppInterface.getDecimalFormat(), i);
                }
                //if (Class.forName(rsmd.getColumnClassName(i + 1)).equals(java.lang.Long.class)) {
                if (rsmd.getColumnClassName(i + 1).equals(java.lang.Long.class.getName())) {
                    vColumnFormat.setElementAt(swingAppInterface.getDecimalFormat(), i);
                }
                //if (Class.forName(rsmd.getColumnClassName(i + 1)).equals(java.lang.Double.class)) {
                if (rsmd.getColumnClassName(i + 1).equals(java.lang.Double.class.getName())) {
                    vColumnFormat.setElementAt(swingAppInterface.getDecimalFormat(), i);
                }
                //if (Class.forName(rsmd.getColumnClassName(i + 1)).equals(java.math.BigDecimal.class)) {
                if (rsmd.getColumnClassName(i + 1).equals(java.math.BigDecimal.class.getName())) {
                    vColumnFormat.setElementAt(swingAppInterface.getDecimalFormat(), i);
                }
                //if (Class.forName(rsmd.getColumnClassName(i + 1)).equals(java.util.Date.class)) {
                if (rsmd.getColumnClassName(i + 1).equals(java.util.Date.class.getName())) {
                    vColumnFormat.setElementAt(swingAppInterface.getDateFormat(), i);
                }
                //if (Class.forName(rsmd.getColumnClassName(i + 1)).equals(java.sql.Date.class)) {
                if (rsmd.getColumnClassName(i + 1).equals(java.sql.Date.class.getName())) {
                    vColumnFormat.setElementAt(swingAppInterface.getDateFormat(), i);
                }
                //if (Class.forName(rsmd.getColumnClassName(i + 1)).equals(java.sql.Time.class)) {
                if (rsmd.getColumnClassName(i + 1).equals(java.sql.Time.class.getName())) {
                    vColumnFormat.setElementAt(swingAppInterface.getTimeFormat(), i);
                }
                //if (Class.forName(rsmd.getColumnClassName(i + 1)).equals(java.sql.Timestamp.class)) {
                if (rsmd.getColumnClassName(i + 1).equals(java.sql.Timestamp.class.getName())) {
                    vColumnFormat.setElementAt(swingAppInterface.getDateTimeFormat(), i);
                }
                if (rsmd.getColumnClassName(i + 1).equals("oracle.sql.TIMESTAMP")) {
                    vColumnFormat.setElementAt(swingAppInterface.getDateTimeFormat(), i);
                }
            }

            Connection connLookup = connection;
            String sLookupSQL = "";
            //- column lookups
            for (int i = 0; i < rsmd.getColumnCount(); i++) {
                if (!this.getLookupSQLConnection(i).equalsIgnoreCase("")) {
                    connLookup = swingAppInterface.getConnection(this.getLookupSQLConnection(i));
                }
                vColumnLookupField.add(null);
                if (this.isLookupColumn(i)) {
                    //System.out.println(rsmd.getColumnName(i+1));
                    MagComboBoxField magLookupField = new MagComboBoxField(swingAppInterface);
                    if (!this.getLookup(i).equalsIgnoreCase("")) {
                        magLookupField.setClass(Class.forName(rsmd.getColumnClassName(i + 1)));
                        magLookupField.fillLookup(this.getLookup(i));
                        //System.out.println(this.getLookup(i));
                    }
                    if (!this.getLookupSQL(i).equalsIgnoreCase("")) {
                        sLookupSQL = this.getLookupSQL(i);
                        if (sLookupSQL.contains("@global.ceg")) {
                            sLookupSQL = StringUtils.stringReplace(sLookupSQL, "@global.ceg", swingAppInterface.getGlobalString("ceg"));
                        }
                        //MaG 2017.12.06.
                        if (sLookupSQL.contains("@local.") && filtersMap != null) {
                            //System.out.println(sLookupSQL);
                            Vector<String> v = searchForLocal(sLookupSQL);
                            for (int j = 0; j < v.size(); j++) {
                                //System.out.println(j);
                                //System.out.println(v.elementAt(j));
                                //System.out.println(v.elementAt(j).replace("@local.", ""));
                                //System.out.println(CommonAppUtils.getParameterValue(filtersMap, "--" + v.elementAt(j).replace("@local.", "")));
                                sLookupSQL = StringUtils.stringReplace(sLookupSQL, v.elementAt(j), CommonAppUtils.getParameterValue(filtersMap, "--" + v.elementAt(j).replace("@local.", "")));
                            }
                            //System.out.println(sLookupSQL);
                        }
                        PreparedStatement psLookup = connLookup.prepareStatement(sLookupSQL);
                        ResultSet rsLookup = psLookup.executeQuery();
                        Vector<Object> vValue = new Vector<Object>();
                        Vector<Object> vDisplay = new Vector<Object>();
                        while (rsLookup.next()) {
                            Object oValue = rsLookup.getObject(1);
                            if (oValue.getClass().equals(String.class)) {
                                oValue = ((String) oValue).trim();
                            }
                            vValue.addElement(oValue);
                            vDisplay.addElement(rsLookup.getObject(2));
                        }
                        magLookupField.fillLookup(vValue, vDisplay);
                        rsLookup.close();
                        rsLookup = null;
                        psLookup.close();
                        psLookup = null;
                    }
                    magLookupField.setBorder(new EmptyBorder(0, 0, 0, 0));
                    vColumnLookupField.setElementAt(magLookupField, i);
                }

                vColumnLookupListTextField.add(null);
                if (this.isLookupColumn(i)) {
                    MagLookupTextField magLookupListTextField = null;
                    if (!this.getLookup(i).equalsIgnoreCase("")) {
                        magLookupListTextField = MagLookupTextField.createListStyleMagLookupTextField(swingAppInterface, null, this.getLookup(i));
                        magLookupListTextField.setClass(Class.forName(rsmd.getColumnClassName(i + 1)));
                        ///magLookupTextField.fillLookup(this.getLookup(i));
                    }
                    if (!this.getLookupSQL(i).equalsIgnoreCase("")) {
                        sLookupSQL = this.getLookupSQL(i);
                        if (sLookupSQL.contains("@global.ceg")) {
                            sLookupSQL = StringUtils.stringReplace(sLookupSQL, "@global.ceg", swingAppInterface.getGlobalString("ceg"));
                        }
                        //MaG 2017.12.06.
                        if (sLookupSQL.contains("@local.") && filtersMap != null) {
                            Vector<String> v = searchForLocal(sLookupSQL);
                            for (int j = 0; j < v.size(); j++) {
                                sLookupSQL = StringUtils.stringReplace(sLookupSQL, v.elementAt(j), CommonAppUtils.getParameterValue(filtersMap, "--" + v.elementAt(j).replace("@local.", "")));
                            }
                        }
                        PreparedStatement psLookup = connLookup.prepareStatement(sLookupSQL);
                        ResultSet rsLookup = psLookup.executeQuery();
                        Vector<Object> vValue = new Vector<Object>();
                        Vector<Object> vDisplay = new Vector<Object>();
                        while (rsLookup.next()) {
                            Object oValue = rsLookup.getObject(1);
                            if (oValue.getClass().equals(String.class)) {
                                oValue = ((String) oValue).trim();
                            }
                            vValue.addElement(oValue);
                            vDisplay.addElement(rsLookup.getObject(2));
                        }
                        magLookupListTextField = MagLookupTextField.createListStyleMagLookupTextField(swingAppInterface, null, vValue, vDisplay);
                        ///magLookupTextField.fillLookup(vValue, vDisplay);
                        rsLookup.close();
                        rsLookup = null;
                        psLookup.close();
                        psLookup = null;
                    }
                    //magLookupTextField.setBorder(new EmptyBorder(0, 0, 0, 0));
                    vColumnLookupListTextField.setElementAt(magLookupListTextField, i);
                }

                vColumnLookupTableTextField.add(null);
                if (this.isLookupColumn(i)) {
                    MagLookupTextField magLookupTableTextField = null;
                    if (!this.getLookup(i).equalsIgnoreCase("")) {
                        magLookupTableTextField = MagLookupTextField.createTableStyleMagLookupTextField(swingAppInterface, null, this.getLookup(i));
                        magLookupTableTextField.setClass(Class.forName(rsmd.getColumnClassName(i + 1)));
                    }
                    if (!this.getLookupSQL(i).equalsIgnoreCase("")) {
                        sLookupSQL = this.getLookupSQL(i);
                        if (sLookupSQL.contains("@global.ceg")) {
                            sLookupSQL = StringUtils.stringReplace(sLookupSQL, "@global.ceg", swingAppInterface.getGlobalString("ceg"));
                        }
                        //MaG 2017.12.06.
                        if (sLookupSQL.contains("@local.") && filtersMap != null) {
                            Vector<String> v = searchForLocal(sLookupSQL);
                            for (int j = 0; j < v.size(); j++) {
                                sLookupSQL = StringUtils.stringReplace(sLookupSQL, v.elementAt(j), CommonAppUtils.getParameterValue(filtersMap, "--" + v.elementAt(j).replace("@local.", "")));
                            }
                        }
                        //@todo task: w/o header settable!!!
                        magLookupTableTextField = MagLookupTextField.createTableStyleMagLookupTextField(swingAppInterface, null, connLookup, sLookupSQL, false);
                    }
                    //magLookupTextField.setBorder(new EmptyBorder(0, 0, 0, 0));
                    vColumnLookupTableTextField.setElementAt(magLookupTableTextField, i);
                }
            }

            //test only
//            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
//                System.out.println(rsmd.getColumnTypeName(i + 1));
//            }
            boolean bColor = false;
            //- data
            while (rs.next()) {
                vRecord = new Vector();
                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                    vRecord.add(rs.getObject(i + 1));
                    //MaG 2017.10.08.
                    if (rsmd.getColumnTypeName(i + 1).equalsIgnoreCase("char") || rsmd.getColumnTypeName(i + 1).equalsIgnoreCase("nchar")) {
                        if (vRecord.lastElement() instanceof String) {
                            vRecord.setElementAt(vRecord.lastElement().toString().trim(), vRecord.size() - 1);
                        }
                    }
                    if (tableInfo != null) {
                        if (tableInfo.getFieldInfo(i).isVirtual()) {
                            String sVirtualValueSQL = tableInfo.getFieldInfo(i).getVirtualValueSQL();
                            sVirtualValueSQL = sVirtualValueSQL.replaceAll("''", "'"); //MaG 2017.05.25.
                            if (sVirtualValueSQL.contains("[")) {
                                String sSQL = sVirtualValueSQL;
                                Pattern regex = Pattern.compile("\\[[a-zA-Z0-9_]+\\]");
                                Matcher regexMatcher = regex.matcher(sVirtualValueSQL);
                                while (regexMatcher.find()) {
                                    sSQL = StringUtils.stringReplace(sSQL, regexMatcher.group(), "?");
                                }
                                Object o = null;
                                try {
                                    //System.out.println(sSQL);
                                    PreparedStatement psVirtualValue = connection.prepareStatement(sSQL);
                                    regexMatcher.reset();
                                    int iPIndex = 0;
                                    while (regexMatcher.find()) {
                                        //System.out.println("*" + regexMatcher.group() + "*");
                                        int iColumnIndex = tableInfo.getFieldIndexByName(regexMatcher.group().replace("[", "").replace("]", ""));
                                        psVirtualValue.setObject(++iPIndex, vRecord.elementAt(iColumnIndex));
                                    }
                                    ResultSet rsVirtualValue = psVirtualValue.executeQuery();
                                    if (rsVirtualValue.next()) {
                                        o = rsVirtualValue.getObject(1);
                                    }
                                } catch (SQLException sqle) {
                                    swingAppInterface.handleError(sqle);
                                }
                                //vRecord.set(vRecord.size() - 1, sSQL);
                                vRecord.set(vRecord.size() - 1, o);
                            }
                        }
                        if (!bColor && tableInfo.getFieldInfo(i).isColorField()) {
                            bColor = true;
                        }
                    }
                }
                vData.add(vRecord);
            }

            //fill sql virtual fields if any:
            setDataVector(vData, vColumnNames); //- the main
            //- set row statuses
            for (int i = 0; i < vData.size(); i++) {
                vRowStatus.add(new Integer(ROW_STATUS_OK));
                vRowColorBackground.add(null);
                vCellColorBackground.add(createColorVector(rs.getMetaData().getColumnCount()));
                vCellColorForeground.add(createColorVector(rs.getMetaData().getColumnCount()));
            }
            //System.out.println("2 " + vRowStatus.size());
//            if (bColor) {
//                int iRed = 0;
//                int iGreen = 0;
//                int iBlue = 0;
//                for (int iColumn = 0; iColumn < tableInfo.getFieldCount(); iColumn++) {
//                    if (tableInfo.getFieldInfo(iColumn).isColorField()) {
//                        for (int iRow = 0; iRow < vData.size(); iRow++) {
//                            int iColor = StringUtils.intValue(StringUtils.isNull(this.getValueAt(iRow, iColumn), ""));
//                            iRed = iColor / (256 * 256);
//                            iColor = iColor - iRed * 256 * 256;
//                            iGreen = iColor / 256;
//                            iColor = iColor - iGreen * 256;
//                            iBlue = iColor;
//                            setRowColorBackground(iRow, new Color(iRed, iGreen, iBlue));
//                        }
//                    }
//                }
//            }
            //- set trd
            trd = new TableRowDefinition();
            for (int i = 0; i < rsmd.getColumnCount(); i++) {
                trd.addColumn(Class.forName(rsmd.getColumnClassName(i + 1)));
                //swingAppInterface.logLine("column #" + Integer.toString(i) + " class: " + rsmd.getColumnClassName(i + 1) + " type: " + rsmd.getColumnTypeName(i + 1), LoggerInterface.LOG_DEBUG);
            }
        } catch (SQLException sqle) {
            swingAppInterface.handleError(sqle);
        } catch (ClassNotFoundException cnfe) {
            swingAppInterface.handleError(cnfe);
        }

/////////////////////////////////////////////////////////        
        this.setAllColumnEditable(true);
        if (tableInfo != null) {
            for (int i = 0; i < tableInfo.getFieldCount(); i++) {
                fi = tableInfo.getFieldInfo(i);
                if (fi.getFilter() != null) {
                    this.setColumnEditable(i, false);
                }
            }
        }

//        if (ti != null) {
//            if (ti.isExtendedScrollToLast()) {
//                
//            }
//        }
//        if (iIdentityColumn > -1) {
//            mtm.setColumnEditable(iIdentityColumn, false);
//        }
//        for (int i = 0; i < this.getColumnCount(); i++) {
//            if (sPrimaryKeyColumnNames.indexOf(this.getColumnName(i)) > -1) {
//                appInterface.logLine("primary key column #" + Integer.toString(i), LoggerInterface.LOG_DEBUG);
//                this.setColumnPrimaryKey(i, true);
//            }
//        }
//        magTable = new MagTable(mtm, this);
//
//        TableUtils.setAutoColumnWidth(magTable);
//        return (magTable);
    }

    private Vector<String> searchForLocal(String s) {
        Vector<String> v = new Vector<String>();
        int iPos = 0;
        int iBegin = 0;
        int iEnd = 0;
        String sPrefix = "@local.";
        while (s.indexOf(sPrefix, iPos) > -1) {
            iBegin = s.indexOf(sPrefix, iPos);
            iEnd = iBegin + sPrefix.length();
            while (iEnd < s.length() && StringUtils.isChars(s.substring(iEnd, iEnd + 1), "_")) {
                ++iEnd;
            }
            v.add(s.substring(iBegin, iEnd));
            iPos = iEnd;
        }
        return (v);
    }

    private Vector<Color> createColorVector(int iSize) {
        Color color = null;
        Vector<Color> vColor = new Vector<Color>();
        for (int i = 0; i < iSize; i++) {
            vColor.add(color);
        }
        return (vColor);
    }

    public DatabaseInfo getDatabaseInfo() {
        return (databaseInfo);
    }

    public TableInfo getTableInfo() {
        return (tableInfo);
    }

    @Override
    public Class getColumnClass(int c) {
        return (trd.getColumn(c));
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        //Boolean bEditable = false;
        boolean bEditable = false;
        boolean bTableInfoPlus = false;

//        if (!bPrimaryKeyExists) {
//            return (false);
//        }
//?        if (row == 1 && col == 3) {
//?            row = row;
//?        }
//        if (row == 0 && col == 4) {
//            row = row;
//        }
        if (bReadOnlyTable) {
            return (false);
        }
        if (!bEditableTable) {
            return (false);
        }
        if (hmEditableColumns.containsKey(new Integer(col))) {
            bEditable = hmEditableColumns.get(new Integer(col));
        } else {
            return (false);
        }
        if (bEditable) {
            if (tableInfo != null) {
                if (tableInfo.getTableInfoPlus() != null) {
                    Boolean b = tableInfo.getTableInfoPlus().isCellEditable(row, col);
                    if (b != null) {
                        bTableInfoPlus = true;
                        if (!b.booleanValue()) {
                            return (false);
                        }
                    }
                }
            }
            if (!bTableInfoPlus && isReadOnlyColumn(col)) {
                return (false);
            }
            //@todo idea: here call an extern object's method, which "find out" from the current record's data, whether this field/column is readonly or not
            if (isModifierColumn(col)) {
                return (false);
            }
            if (isModificationTimeColumn(col)) {
                return (false);
            }
        }
        if (bEditable) {
            if (hmEditableRows.size() == 0) {
                bEditable = true;
            } else {
                if (hmEditableRows.containsKey(new Integer(row))) {
                    bEditable = hmEditableRows.get(new Integer(row));
                } else {
                    if (vRowStatus.elementAt(row).intValue() == ROW_STATUS_NEW) {
                        bEditable = true;
                    } else {
                        bEditable = false;
                    }
                }
            }
        }
        //MaG 2018.03.09.
        //if (bEditable) {
        //editable cell ?
        if (hmEditableCells.size() > 0) {
            if (hmEditableCells.containsKey(new MagCellIndex(row, col))) {
                bEditable = hmEditableCells.get(new MagCellIndex(row, col));
                //System.out.println(Integer.toString(row) + " " + Integer.toString(col) + " " + (bEditable ? "true" : "false"));
            } else {
                //bEditable = false;
            }
        }
        //}
        if (bEditable) {
            if (isKnownClass(getColumnClass(col))) {
                return true;
            }
        }
        return (false);
    }

    public static boolean isKnownClass(Class c) {
        //@todo task: [B -->  byte[] array not editable, display only the length of the data
        //System.out.println(c.getName());
        if (c.equals(String.class)) {
            return true;
        }
        if (c.equals(java.lang.Short.class)) {
            return true;
        }
        if (c.equals(java.lang.Integer.class)) {
            return true;
        }
        if (c.equals(java.lang.Long.class)) {
            return true;
        }
        if (c.equals(java.lang.Double.class)) {
            return true;
        }
        if (c.equals(java.lang.Float.class)) {
            return true;
        }
        if (c.equals(java.math.BigDecimal.class)) {
            return true;
        }
        if (c.equals(LookupInteger.class)) {
            return true;
        }
        if (c.equals(Boolean.class)) {
            return true;
        }
        if (c.equals(java.util.Date.class)) {
            return true;
        }
        if (c.equals(java.sql.Date.class)) {
            return true;
        }
        if (c.equals(java.sql.Time.class)) {
            return true;
        }
        if (c.equals(java.sql.Timestamp.class)) {
            return true;
        }
        if (c.getClass().getName().equals("oracle.sql.TIMESTAMP")) {
            return true;
        }
        return (false);
    }

    public int getFirstEditableColumnInRow(int iRow) {
        for (int iCol = 0; iCol < this.getColumnCount(); iCol++) {
            if (isCellEditable(iRow, iCol)) {
                return (iCol);
            }
        }
        return (-1);
    }

    public void setColumnPrimaryKey(int iColumn, boolean bPrimaryKey) {
        if (hmPrimaryKeyColumns.containsKey(new Integer(iColumn))) {
            hmPrimaryKeyColumns.remove(new Integer(iColumn));
        }
        hmPrimaryKeyColumns.put(new Integer(iColumn), new Boolean(bPrimaryKey));
    }

    public boolean isColumnPrimaryKey(int iColumn) {
        if (hmPrimaryKeyColumns.containsKey(new Integer(iColumn))) {
            return (hmPrimaryKeyColumns.get(new Integer(iColumn)));
        } else {
            return (false);
        }
    }

    public void setCellEditable(int iRow, int iColumn, boolean bEditable) {
        if (hmEditableCells.containsKey(new MagCellIndex(iRow, iColumn))) {
            hmEditableCells.remove(new MagCellIndex(iRow, iColumn));
        }
        hmEditableCells.put(new MagCellIndex(iRow, iColumn), new Boolean(bEditable));
    }

    public void setColumnEditable(int iColumn, boolean bEditable) {
        if (hmEditableColumns.containsKey(new Integer(iColumn))) {
            hmEditableColumns.remove(new Integer(iColumn));
        }
        hmEditableColumns.put(new Integer(iColumn), new Boolean(bEditable));
    }

    public void setAllColumnEditable(boolean bEditable) {
        for (int iColumn = 0; iColumn < getColumnCount(); iColumn++) {
            if (iColumn == iIdentityColumn) {
                this.setColumnEditable(iColumn, false);
            } else {
                this.setColumnEditable(iColumn, bEditable);
            }
        }
    }

    public void setRowEditable(int iRow, boolean bEditable) {
        if (hmEditableRows.containsKey(new Integer(iRow))) {
            hmEditableRows.remove(new Integer(iRow));
        }
        hmEditableRows.put(new Integer(iRow), new Boolean(bEditable));
    }

    public int getRowStatus(int iRow) {
        if (iRow < 0 || iRow >= vRowStatus.size()) {
            return (0);
        }
        return (vRowStatus.elementAt(iRow).intValue());
    }

    public void setRowStatus(int iRow, int iRowStatus) {
        if (iRow < 0 || iRow >= vRowStatus.size()) {
            return;
        }
        vRowStatus.setElementAt(iRowStatus, iRow);
        bNew = iRowStatus == ROW_STATUS_NEW;
    }

    public Color getCellColorForeground(int iRow, int iCol) {
        if (iRow < 0 || iRow >= vCellColorForeground.size()) {
            return (null);
        }
        Vector<Color> rowVector = (Vector) vCellColorForeground.elementAt(iRow);
        if (iCol < 0 || iCol >= rowVector.size()) {
            return (null);
        }
        return (rowVector.elementAt(iCol));
    }

    public void setCellColorForeground(int iRow, int iCol, Color color) {
        if (iRow < 0 || iRow >= vCellColorForeground.size()) {
            return;
        }
        Vector<Color> rowVector = vCellColorForeground.elementAt(iRow);
        if (iCol < 0 || iCol >= rowVector.size()) {
            return;
        }
        rowVector.setElementAt(color, iCol);
    }

    public void removeAllColorForeground() {
        for (int iRow = 0; iRow < vCellColorForeground.size(); iRow++) {
            Vector<Color> rowVector = vCellColorForeground.elementAt(iRow);
            for (int iCol = 0; iCol < rowVector.size(); iCol++) {
                rowVector.setElementAt(null, iCol);
            }
        }

    }

    public Color getRowColorBackground(int iRow) {
        if (iRow < 0 || iRow >= vRowColorBackground.size()) {
            return (null);
        }
        return (vRowColorBackground.elementAt(iRow));
    }

    public void setRowColorBackground(int iRow, Color color) {
        if (iRow < 0 || iRow >= vRowColorBackground.size()) {
            return;
        }
        vRowColorBackground.setElementAt(color, iRow);
    }

    public Color getCellColorBackground(int iRow, int iCol) {
        if (iRow < 0 || iRow >= vCellColorBackground.size()) {
            return (null);
        }
        Vector<Color> rowVector = (Vector) vCellColorBackground.elementAt(iRow);
        if (iCol < 0 || iCol >= rowVector.size()) {
            return (null);
        }
        return (rowVector.elementAt(iCol));
    }

    public void setCellColorBackground(int iRow, int iCol, Color color) {
        if (iRow < 0 || iRow >= vCellColorBackground.size()) {
            return;
        }
        Vector<Color> rowVector = vCellColorBackground.elementAt(iRow);
        if (iCol < 0 || iCol >= rowVector.size()) {
            return;
        }
        rowVector.setElementAt(color, iCol);
    }

    public void removeAllColorBackground() {
        for (int iRow = 0; iRow < vRowColorBackground.size(); iRow++) {
            vRowColorBackground.setElementAt(null, iRow);
        }
        for (int iRow = 0; iRow < vCellColorBackground.size(); iRow++) {
            Vector<Color> rowVector = vCellColorBackground.elementAt(iRow);
            for (int iCol = 0; iCol < rowVector.size(); iCol++) {
                rowVector.setElementAt(null, iCol);
            }
        }

    }

    public boolean isNewRecord() {
        return (bNew);
    }

    @Override
    public void addRow(Vector rowData) {
        super.addRow(rowData);
//        vRowStatus.add(new Integer(ROW_STATUS_OK));
//        System.out.println(vRowStatus.size());
//        vRowColorBackground.add(null);
//        vCellColorBackground.add(createColorVector(this.getColumnCount()));
//        vCellColorForeground.add(createColorVector(this.getColumnCount()));
    }

    @Override
    public void insertRow(int row, Vector rowData) {
        super.insertRow(row, rowData);
        vRowStatus.insertElementAt(new Integer(ROW_STATUS_OK), row);
        //System.out.println("inserted " + vRowStatus.size());
        vRowColorBackground.insertElementAt(null, row);
        vCellColorBackground.insertElementAt(createColorVector(this.getColumnCount()), row);
        vCellColorForeground.insertElementAt(createColorVector(this.getColumnCount()), row);
    }

    public void addRow(Vector rowData, int iRowStatus) {
        super.addRow(rowData);
//        vRowStatus.add(new Integer(iRowStatus));
//        System.out.println(vRowStatus.size());
//        vRowColorBackground.add(null);
//        vCellColorBackground.add(createColorVector(this.getColumnCount()));
//        vCellColorForeground.add(createColorVector(this.getColumnCount()));
    }

    public void addNewRow(Vector rowData) {
        if (bNew) {
            return;
        }
        super.addRow(rowData);
        vRowStatus.setElementAt(new Integer(ROW_STATUS_NEW), vRowStatus.size() - 1);
//        System.out.println(vRowStatus.size());
//        vRowStatus.add(new Integer(ROW_STATUS_NEW));
//        System.out.println(vRowStatus.size());
//        vRowColorBackground.add(null);
//        vCellColorBackground.add(createColorVector(this.getColumnCount()));
//        vCellColorForeground.add(createColorVector(this.getColumnCount()));
        bNew = true;
    }

    @Override
    public void removeRow(int row) {
        super.removeRow(row);
        vRowStatus.remove(row);
        bNew = false;
        //dataVector.removeElementAt(row);
        //fireTableRowsDeleted(row, row);
    }

    public String getOriginColumnName(int iColumn) {
        if (iColumn < 0 || iColumn >= vOriginColumnNames.size()) {
            return ("");
        }
        return (vOriginColumnNames.elementAt(iColumn));
    }

    public int getColumnIndexByOriginColumnName(String sOriginColumnName) {
        for (int i = 0; i < vOriginColumnNames.size(); i++) {
            if (vOriginColumnNames.elementAt(i).equalsIgnoreCase(sOriginColumnName)) {
                return (i);
            }
        }
        return (-1);
    }

    public Format getColumnFormatter(int iColumn) {
        if (iColumn < 0 || iColumn >= vOriginColumnNames.size()) {
            return (null);
        }
        return (vColumnFormat.elementAt(iColumn));
    }

    public MagComboBoxField getColumnLookupField(int iColumn) {
        if (iColumn < 0 || iColumn >= vOriginColumnNames.size()) {
            return (null);
        }
        return (vColumnLookupField.elementAt(iColumn));
    }

    public MagLookupTextField getColumnLookupListTextField(int iColumn) {
        if (iColumn < 0 || iColumn >= vOriginColumnNames.size()) {
            return (null);
        }
        return (vColumnLookupListTextField.elementAt(iColumn));
    }

    public MagLookupTextField getColumnLookupTableTextField(int iColumn) {
        if (iColumn < 0 || iColumn >= vOriginColumnNames.size()) {
            return (null);
        }
        return (vColumnLookupTableTextField.elementAt(iColumn));
    }

    public boolean isIdentityColumn(int iColumn) {
        return (iColumn == iIdentityColumn);
    }

    public int getMaxLength(int iColumn) {
        if (rsmd == null) {
            return (-1);
        }
        try {
            if (rsmd.getColumnType(iColumn + 1) == java.sql.Types.FLOAT || rsmd.getColumnType(iColumn + 1) == java.sql.Types.REAL) {
                return (10);
            }
            return (rsmd.getPrecision(iColumn + 1));
        } catch (SQLException sqle) {
            swingAppInterface.handleError(sqle);
        }
        return (-1);
    }

    public int getDecimals(int iColumn) {
        if (rsmd == null) {
            return (0);
        }
        try {
            if (rsmd.getColumnType(iColumn + 1) == java.sql.Types.FLOAT || rsmd.getColumnType(iColumn + 1) == java.sql.Types.REAL) {
                return (2);
            }
            return (rsmd.getScale(iColumn + 1));
        } catch (SQLException sqle) {
            swingAppInterface.handleError(sqle);
        }
        return (0);
    }

    public boolean isUpperCaseColumn(int iColumn) {
        if (tableInfo == null) {
            return (false);
        }
        FieldInfo fi = tableInfo.getFieldInfo(getOriginColumnName(iColumn));
        if (fi != null) {
            return (fi.isUpperCase());
        }
        return (false);
    }

    public boolean isTextColumn(int iColumn) {
        if (tableInfo == null) {
            return (false);
        }
        FieldInfo fi = tableInfo.getFieldInfo(getOriginColumnName(iColumn));
        if (fi != null) {
            return (fi.isText());
        }
        return (false);
    }

    public boolean isLookupColumn(int iColumn) {
        if (tableInfo == null) {
            return (false);
        }
        FieldInfo fi = tableInfo.getFieldInfo(getOriginColumnName(iColumn));
        if (fi != null) {
            return (fi.isLookup());
        }
        return (false);
    }

    public String getLookup(int iColumn) {
        if (tableInfo == null) {
            return ("");
        }
        FieldInfo fi = tableInfo.getFieldInfo(getOriginColumnName(iColumn));
        if (fi != null) {
            return (fi.getLookup());
        }
        return ("");
    }

    public String getLookupSQL(int iColumn) {
        if (tableInfo == null) {
            return ("");
        }
        FieldInfo fi = tableInfo.getFieldInfo(getOriginColumnName(iColumn));
        if (fi != null) {
            return (fi.getLookupSQL());
        }
        return ("");
    }

    public String getLookupSQLConnection(int iColumn) {
        if (tableInfo == null) {
            return ("");
        }
        FieldInfo fi = tableInfo.getFieldInfo(getOriginColumnName(iColumn));
        if (fi != null) {
            return (fi.getLookupSQLConnection());
        }
        return ("");
    }

    public boolean isReadOnlyColumn(int iColumn) {
        if (tableInfo == null) {
            return (false);
        }
        FieldInfo fi = tableInfo.getFieldInfo(getOriginColumnName(iColumn));
        if (fi != null) {
            return (fi.isReadOnly());
        }
        return (false);
    }

    public boolean isMandatoryColumn(int iColumn) {
        if (tableInfo == null) {
            return (false);
        }
        FieldInfo fi = tableInfo.getFieldInfo(getOriginColumnName(iColumn));
        if (fi != null) {
            return (fi.isMandatory());
        }
        return (false);
    }

    public String getAllowedCharacters(int iColumn) {
        if (tableInfo == null) {
            return ("");
        }
        FieldInfo fi = tableInfo.getFieldInfo(getOriginColumnName(iColumn));
        if (fi != null) {
            return (fi.getAllowedCharacters());
        }
        return ("");
    }

    public String getToolTipText(int iColumn) {
        if (tableInfo == null) {
            return ("");
        }
        FieldInfo fi = tableInfo.getFieldInfo(getOriginColumnName(iColumn));
        if (fi != null) {
            return (fi.getToolTipText());
        }
        return ("");
    }

    public boolean isModifierColumn(int iColumn) {
        if (tableInfo == null) {
            return (false);
        }
        FieldInfo fi = tableInfo.getFieldInfo(getOriginColumnName(iColumn));
        if (fi != null) {
            return (fi.isModifier());
        }
        return (false);
    }

    public boolean isModificationTimeColumn(int iColumn) {
        if (tableInfo == null) {
            return (false);
        }
        FieldInfo fi = tableInfo.getFieldInfo(getOriginColumnName(iColumn));
        if (fi != null) {
            return (fi.isModificationTime());
        }
        return (false);
    }

    public boolean isCreationTimeColumn(int iColumn) {
        if (tableInfo == null) {
            return (false);
        }
        FieldInfo fi = tableInfo.getFieldInfo(getOriginColumnName(iColumn));
        if (fi != null) {
            return (fi.isCreationTime());
        }
        return (false);
    }

    public boolean isVirtualColumn(int iColumn) {
        if (tableInfo == null) {
            return (false);
        }
        FieldInfo fi = tableInfo.getFieldInfo(getOriginColumnName(iColumn));
        if (fi != null) {
            return (fi.isVirtual());
        }
        return (false);
    }

    public Object getColumnVirtualValue(int iColumn) {
        if (tableInfo == null) {
            return (null);
        }
        FieldInfo fi = tableInfo.getFieldInfo(getOriginColumnName(iColumn));
        if (fi != null) {
            return (fi.getVirtualValue());
        }
        return (null);
    }

    public String getColumnVirtualValueSQL(int iColumn) {
        if (tableInfo == null) {
            return ("");
        }
        FieldInfo fi = tableInfo.getFieldInfo(getOriginColumnName(iColumn));
        if (fi != null) {
            return (fi.getVirtualValueSQL());
        }
        return ("");
    }

    public int getColumnMinWidth(int iColumn) {
        if (tableInfo == null) {
            return (0);
        }
        FieldInfo fi = tableInfo.getFieldInfo(getOriginColumnName(iColumn));
        if (fi != null) {
            return (fi.getMinWidth());
        }
        return (0);
    }

    public int getColumnMaxWidth(int iColumn) {
        if (tableInfo == null) {
            return (0);
        }
        FieldInfo fi = tableInfo.getFieldInfo(getOriginColumnName(iColumn));
        if (fi != null) {
            return (fi.getMaxWidth());
        }
        return (0);
    }

    public String getColumnSpecType(int iColumn) {
        if (tableInfo == null) {
            if (hmColumnSpectypes.containsKey(new Integer(iColumn))) {
                return (hmColumnSpectypes.get(new Integer(iColumn)));
            }
            return ("");
        }
        FieldInfo fi = tableInfo.getFieldInfo(getOriginColumnName(iColumn));
        if (fi != null) {
            return (fi.getSpecType());
        }
        return ("");
    }

    public void setColumnSpecType(int iColumn, String sSpecType) {
        if (tableInfo == null) {
            if (hmColumnSpectypes.containsKey(new Integer(iColumn))) {
                hmColumnSpectypes.remove(new Integer(iColumn));
            }
            hmColumnSpectypes.put(new Integer(iColumn), sSpecType);
            return;
        }
        FieldInfo fi = tableInfo.getFieldInfo(getOriginColumnName(iColumn));
        if (fi != null) {
            fi.setSpecType(sSpecType);
        }
    }

//    public void setHiddenColumn(String sColumnName, boolean bHidden) {
//        int iColumn = getColumnIndexByOriginColumnName(sColumnName);
//        if (iColumn < 0) {
//            return;
//        }
//        setHiddenColumn(iColumn, bHidden);
//    }
    public void setHiddenColumn(int iColumn, boolean bHidden) {
        if (hmHiddenColumns.containsKey(new Integer(iColumn))) {
            hmHiddenColumns.remove(new Integer(iColumn));
        }
        hmHiddenColumns.put(new Integer(iColumn), new Boolean(bHidden));
    }

    public boolean isHiddenColumn(int iColumn) {
        if (iRowColorColumn > -1) {
            if (iColumn == iRowColorColumn) {
                return (true);
            }
        }
        if (hmHiddenColumns.containsKey(new Integer(iColumn))) {
            return (hmHiddenColumns.get(new Integer(iColumn)));
//        } else {
//            return (false);
        }
        if (tableInfo == null) {
            return (false);
        }
        FieldInfo fi = tableInfo.getFieldInfo(getOriginColumnName(iColumn));
        if (fi != null) {
            return (fi.isHidden());
        }
        return (false);
    }

    public boolean isRecordInfoColumn(int iColumn) {
        if (tableInfo == null) {
            return (false);
        }
        FieldInfo fi = tableInfo.getFieldInfo(getOriginColumnName(iColumn));
        if (fi != null) {
            return (fi.isRecordInfo());
        }
        return (false);
    }

    public void setCenteredColumn(int iColumn, boolean bCentered) {
        if (hmCenteredColumns.containsKey(new Integer(iColumn))) {
            hmCenteredColumns.remove(new Integer(iColumn));
        }
        hmCenteredColumns.put(new Integer(iColumn), new Boolean(bCentered));
    }

    public boolean isCenteredColumn(int iColumn) {
        Boolean bCentered = false;
        if (hmCenteredColumns.containsKey(new Integer(iColumn))) {
            bCentered = hmCenteredColumns.get(new Integer(iColumn));
        }
        return (bCentered);
    }

    public void setRightAlignedColumn(int iColumn, boolean bRightAligned) {
        if (hmRightAlignedColumns.containsKey(new Integer(iColumn))) {
            hmRightAlignedColumns.remove(new Integer(iColumn));
        }
        hmRightAlignedColumns.put(new Integer(iColumn), new Boolean(bRightAligned));
    }

    public boolean isRightAlignedColumn(int iColumn) {
        Boolean bRightAligned = false;
        if (hmRightAlignedColumns.containsKey(new Integer(iColumn))) {
            bRightAligned = hmRightAlignedColumns.get(new Integer(iColumn));
        }
        return (bRightAligned);
    }

    public boolean isColorColumn(int iColumn) {
        if (tableInfo == null) {
            return (false);
        }
        FieldInfo fi = tableInfo.getFieldInfo(getOriginColumnName(iColumn));
        if (fi != null) {
            return (fi.isColorField());
        }
        return (false);
    }

    public String getTableName() {
        return (sTableName);
    }

    public ResultSetMetaData getResultSetMetaData() {
        return (rsmd);
    }

    public void addEmptyRow() {
        //@todo question: should we prohibit the sorting during the new record editing state? //magTable.getRowSorter().setSortKeys(null);
        //@todo task: when sorter is active, the "last" row is maybe not the last one (e.g. default values, etc.)
        FieldInfo fi = null;
        Vector vRecord = new Vector();
        Object o = null;
        Integer intNull = null;
        String sDefaultValueSQL = "";
        String sGlobal = "";
        int iGlobalBegin = -1;
        int iGlobalEnd = -1;
        String sGlobalVariable = "";

        for (int iCol = 0; iCol < this.getColumnCount(); iCol++) {
            o = null;
            if (tableInfo != null) {
                fi = tableInfo.getFieldInfo(getOriginColumnName(iCol));
                if (fi != null) {
                    if (fi.getFilter() != null) {
                        //o = fi.getFilter();
                        if (fi.getFilter().toString().startsWith("@global.")) {
                            o = swingAppInterface.getGlobal(fi.getFilter().toString().substring("@global.".length()));
                        } else if (fi.getFilter().toString().startsWith("@local.") && filtersMap != null) {
                            o = CommonAppUtils.getParameterValue(filtersMap, "--" + fi.getFilter().toString().substring("@local.".length()));
                            //System.out.println(o.toString());
                        } else {
                            o = fi.getFilter();
                            if (!getColumnClass(iCol).equals(o.getClass())) {
                                if (o.getClass().equals(String.class)) {
                                    if (getColumnClass(iCol).equals(Integer.class)) {
                                        o = new Integer(o.toString());
                                    }
                                    if (getColumnClass(iCol).equals(java.math.BigDecimal.class)) {
                                        o = new java.math.BigDecimal(o.toString());
                                    }
                                }
                            }
                        }
                    } else {
                        if (fi.getDefaultValue() != null) {
                            o = fi.getDefaultValue();
                            if (o.toString().contains("@local.") && filtersMap != null) {
                                Vector<String> v = searchForLocal(o.toString());
                                if (v.size() == 1) {
                                    //System.out.println(o);
                                    o = CommonAppUtils.getParameterValue(filtersMap, "--" + v.elementAt(0).replace("@local.", ""));
                                    //System.out.println(o);
                                    if (o.getClass().equals(String.class)) {
                                        if (getColumnClass(iCol).equals(Integer.class)) {
                                            if (o.toString().equalsIgnoreCase("null")) {
                                                o = intNull;
                                            } else {
                                                o = new Integer(o.toString());
                                            }
                                        }
                                        if (getColumnClass(iCol).equals(java.math.BigDecimal.class)) {
                                            o = new java.math.BigDecimal(o.toString());
                                        }
                                    }
                                }
                            } else {
                                if (getColumnClass(iCol).equals(o.getClass())) {
                                    o = fi.getDefaultValue();
                                } else {
                                    if (o.getClass().equals(String.class)) {
                                        if (getColumnClass(iCol).equals(Integer.class)) {
                                            o = new Integer(o.toString());
                                        }
                                        if (getColumnClass(iCol).equals(java.math.BigDecimal.class)) {
                                            o = new java.math.BigDecimal(o.toString());
                                        }
                                    }
                                }
                            }
                        }
                        if (!fi.getDefaultValueSQL().equalsIgnoreCase("")) {
                            try {
                                sDefaultValueSQL = fi.getDefaultValueSQL();
                                if (sDefaultValueSQL.toString().contains("[@global.")) {
                                    iGlobalBegin = sDefaultValueSQL.indexOf("[@global.");
                                    iGlobalEnd = sDefaultValueSQL.indexOf("]", iGlobalBegin);
                                    sGlobal = sDefaultValueSQL.substring(iGlobalBegin, iGlobalEnd + 1);
                                    sGlobalVariable = sDefaultValueSQL.substring(iGlobalBegin + 1, iGlobalEnd);
                                    sGlobalVariable = sGlobalVariable.substring("@global.".length());
                                    sGlobalVariable = StringUtils.isNull(swingAppInterface.getGlobal(sGlobalVariable), "");
                                    if (!sGlobalVariable.equalsIgnoreCase("")) {
                                        sDefaultValueSQL = StringUtils.stringReplace(sDefaultValueSQL, sGlobal, sGlobalVariable);
                                    }
                                }
                                PreparedStatement psDefaultValue = connection.prepareStatement(sDefaultValueSQL);
                                ResultSet rsDefaultValue = psDefaultValue.executeQuery();
                                if (rsDefaultValue.next()) {
                                    o = rsDefaultValue.getObject(1);
                                }
                            } catch (SQLException sqle) {
                                swingAppInterface.handleError(sqle);
                            }
                        }
                    }
                }
            }
            vRecord.add(o);
        }
        this.addNewRow(vRecord);
    }

    public boolean isEditableTable() {
        return (bEditableTable);
    }

    public void setEditableTable(boolean bEditable) {
        bEditableTable = bEditable;
    }

    public void setReadOnlyTable(boolean bReadOnly) {
        this.bReadOnlyTable = bReadOnly;
    }

    public boolean isReadOnlyTable() {
        return (bReadOnlyTable);
    }

    public boolean isUpdateableTable() {
        return (bUpdateableTable);
    }

    public void setLookupTable(boolean b) {
        this.bIsLookupTable = b;
    }

    public boolean isLookupTable() {
        return (bIsLookupTable);
    }

    public void refresh(ResultSet rs) {
        Vector vColumnNames = new Vector();
        Vector vData = new Vector();
        Vector vRecord = new Vector();
        String sTableName = "";
        String sFieldName = "";
        String sFieldDisplayName = "";
        FieldInfo fi = null;

        if (this.ehi == null) {
            setDataVector(vData, vColumnNames);
            return;
        }

        if (rs == null) {
            setDataVector(vData, vColumnNames);
            return;
        }
        try {
            //@todo wish: check, only one tablename, but it odesn't work in mssql
            sTableName = rs.getMetaData().getTableName(1);
            this.databaseInfo = databaseInfo;
            if (databaseInfo != null) {
                tableInfo = databaseInfo.getTableInfo(sTableName);
            }
            if (tableInfo == null) { //MaG 2016.06.07
                tableInfo = new TableInfo(rs.getMetaData().getTableName(1), "", rs.getMetaData().getTableName(1), "", "");
                vOriginColumnNames = new Vector<String>();
                vColumnLookupField = new Vector<MagComboBoxField>();
                vColumnLookupTableTextField = new Vector<MagLookupTextField>();
                vColumnLookupListTextField = new Vector<MagLookupTextField>();
                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                    tableInfo.addFieldInfo(new FieldInfo(rs.getMetaData().getColumnLabel(i + 1), rs.getMetaData().getColumnLabel(i + 1)));
                    vOriginColumnNames.add(rs.getMetaData().getColumnLabel(i + 1));
                    vColumnLookupField.add(null);
                    vColumnLookupTableTextField.add(null);
                    vColumnLookupListTextField.add(null);
                }
            }
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                sFieldDisplayName = rs.getMetaData().getColumnLabel(i + 1);//default...
                if (tableInfo != null) {
                    sFieldName = rs.getMetaData().getColumnLabel(i + 1);
                    fi = tableInfo.getFieldInfo(sFieldName);
                    if (fi != null) {
                        sFieldDisplayName = fi.getDisplayName();
                    }
                }
                if (sFieldDisplayName.contains("|CRLF|")) {
                    sFieldDisplayName = "<html><center>" + StringUtils.stringReplace(sFieldDisplayName, "|CRLF|", "<br>") + "</html>";
                }
                vColumnNames.add(sFieldDisplayName);
//                System.out.println("Column #" + Integer.toString(i + 1) + " class: " + rs.getMetaData().getColumnClassName(i + 1));
            }
            while (rs.next()) {
                vRecord = new Vector();
                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                    vRecord.add(rs.getObject(i + 1));
                }
                vData.add(vRecord);
            }
            setDataVector(vData, vColumnNames);

            vRowStatus = new Vector<Integer>();
            vRowColorBackground = new Vector<Color>();
            vCellColorBackground = new Vector<Vector<Color>>();
            vCellColorForeground = new Vector<Vector<Color>>();

            for (int i = 0; i < vData.size(); i++) {
                vRowStatus.add(new Integer(ROW_STATUS_OK));
                vRowColorBackground.add(null);
                vCellColorBackground.add(createColorVector(rs.getMetaData().getColumnCount()));
                vCellColorForeground.add(createColorVector(rs.getMetaData().getColumnCount()));
            }
            //System.out.println("3 " + vRowStatus.size());

            trd = new TableRowDefinition();
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                trd.addColumn(Class.forName(rs.getMetaData().getColumnClassName(i + 1)));
            }
            rs.close(); //MaG 2017.10.24.
            rs = null; //MaG 2017.10.24.
        } catch (SQLException sqle) {
            ehi.handleError(sqle);
        } catch (ClassNotFoundException cnfe) {
            ehi.handleError(cnfe);
        }
    }

//    public void generateTableinfo() {
//        tableInfo = new TableInfo("", "", "", "");
////        vOriginColumnNames = new Vector<String>();
//        for (int i = 0; i < getColumnCount(); i++) {
////            vOriginColumnNames.add(getColumnName(i));
//            tableInfo.addFieldInfo(new FieldInfo(getColumnName(i), getColumnName(i)));
//            //System.out.println(getColumnName(i));
//        }
//    }
    public String getStringValueAt(int row, int column) {
        return (StringUtils.isNull(super.getValueAt(row, column), ""));
    }

    public Vector getColumnIdentifiers() {
        return columnIdentifiers;
    }

    public void setTableInfoPlus(TableInfoPlus tip) {
        tableInfo.setTableInfoPlus(tip);
        initRowStatuses();
    }

    private void initRowStatuses() {
        if (tableInfo != null) {
            if (tableInfo.getTableInfoPlus() != null) {
                for (int i = 0; i < getRowCount(); i++) {
                    tableInfo.getTableInfoPlus().setRowStatuses(i);
                }
            }
        }
    }
}
