package hu.mag.db;

import hu.mgx.app.common.AppInterface;
import hu.mgx.util.StringUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FieldInfo {
    
    private String sName = "";
    private String sDisplayName = "";
    
    private boolean bUpperCase = false;
    private boolean bText = false;
    private boolean bLookup = false;
    private String sLookup = "";
    private String sLookupSQL = "";
    private String sLookupSQLConnection = "";
    private Object oFilter = null;
    private Object oDefaultValue = null;
    private String sDefaultValueSQL = "";
    private boolean bReadOnly = false;
    private boolean bMandatory = false;
    private String sAllowedCharacters = "";
    private String sToolTipText = "";
    private boolean bModifier = false;
    private boolean bModificationTime = false;
    private boolean bCreationTime = false;
    private String sControlSQL = "";
    private boolean bVisible = true;
    private boolean bVirtual = false;
    private Object oVirtualValue = null;
    private String sVirtualValueSQL = "";
    private int iMinWidth = 0;
    private int iMaxWidth = 0;
    private String sSpecType = "";
    private boolean bHidden = false;
    private String sRecordInfo = "";
    private boolean bColorField = false;
    private boolean bValidFrom = false;
    private boolean bValidTo = false;
    
    public FieldInfo(String sName, String sDisplayName) {
        this.sName = sName;
        this.sDisplayName = sDisplayName;
    }
    
    public void setName(String sName) {
        this.sName = sName;
    }
    
    public String getName() {
        return sName;
    }
    
    public void setDisplayName(String sDisplayName) {
        this.sDisplayName = sDisplayName;
    }
    
    public String getDisplayName() {
        return sDisplayName;
    }
    
    public void setFilter(Object oFilter) {
        this.oFilter = oFilter;
    }
    
    public Object getFilter() {
        return oFilter;
    }
    
    public void setDefaultValue(Object oDefaultValue) {
        this.oDefaultValue = oDefaultValue;
    }
    
    public Object getDefaultValue() {
        return oDefaultValue;
    }
    
    public void setDefaultValueSQL(String sDefaultValueSQL) {
        this.sDefaultValueSQL = sDefaultValueSQL;
    }
    
    public String getDefaultValueSQL() {
        return (sDefaultValueSQL);
    }
    
    public void setUpperCase(boolean bUpperCase) {
        this.bUpperCase = bUpperCase;
    }
    
    public boolean isUpperCase() {
        return (bUpperCase);
    }
    
    public void setLookup(String sLookup) {
        this.sLookup = sLookup;
        bLookup = true;
    }
    
    public String getLookup() {
        return (sLookup);
    }
    
    public void setLookupSQL(String sLookupSQL) {
        this.sLookupSQL = sLookupSQL;
        bLookup = true;
    }
    
    public String getLookupSQL() {
        return (sLookupSQL);
    }
    
    public boolean isLookup() {
        return (bLookup);
    }
    
    public void setLookupSQLConnection(String sLookupSQLConnection) {
        this.sLookupSQLConnection = sLookupSQLConnection;
    }
    
    public String getLookupSQLConnection() {
        return (sLookupSQLConnection);
    }
    
    public void setText(boolean b) {
        this.bText = b;
    }
    
    public boolean isText() {
        return (bText);
    }
    
    public void setReadOnly(boolean bReadOnly) {
        this.bReadOnly = bReadOnly;
    }
    
    public boolean isReadOnly() {
        return (bReadOnly);
    }
    
    public void setMandatory(boolean bMandatory) {
        this.bMandatory = bMandatory;
    }
    
    public boolean isMandatory() {
        return (bMandatory);
    }
    
    public void setAllowedCharacters(String sAllowedCharacters) {
        this.sAllowedCharacters = sAllowedCharacters;
    }
    
    public String getAllowedCharacters() {
        return (sAllowedCharacters);
    }
    
    public void setToolTipText(String sToolTipText) {
        this.sToolTipText = sToolTipText;
    }
    
    public String getToolTipText() {
        return (sToolTipText);
    }
    
    public void setModifier(boolean bModifier) {
        this.bModifier = bModifier;
    }
    
    public boolean isModifier() {
        return (bModifier);
    }
    
    public void setModificationTime(boolean bModificationTime) {
        this.bModificationTime = bModificationTime;
    }
    
    public boolean isModificationTime() {
        return (bModificationTime);
    }
    
    public void setCreationTime(boolean bCreationTime) {
        this.bCreationTime = bCreationTime;
    }
    
    public boolean isCreationTime() {
        return (bCreationTime);
    }
    
    public void setControlSQL(String sControlSQL) {
        this.sControlSQL = sControlSQL;
    }
    
    public String getControlSQL() {
        return (sControlSQL);
    }
    
    public boolean isVisible() {
        return (bVisible);
    }
    
    public boolean executeControlSQLIfExists(Connection conn, AppInterface appInterface) {
        if (sControlSQL.equalsIgnoreCase("")) {
            return (false);
        }
        bVisible = true;
        String s[] = sControlSQL.split("\\|");
        if (s.length > 1) {
            for (int i = 1; i < s.length; i++) {
                Object oGlobal = appInterface.getGlobal(s[i]);
                if (oGlobal == null) {
                    return (false);
                }
                s[0] = StringUtils.stringReplace(s[0], "<" + s[i] + ">", oGlobal.toString());
            }
        }
        try {
            PreparedStatement ps = conn.prepareStatement(s[0]);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                    if (rs.getMetaData().getColumnName(i + 1).equalsIgnoreCase("visible")) {
                        bVisible = rs.getString(i + 1).equalsIgnoreCase("1");
                    }
                }
            }
        } catch (SQLException sqle) {
            appInterface.handleError(sqle);
            return (false);
        }
        return (true);
    }
    
    public void setVirtual(boolean bVirtual) {
        this.bVirtual = bVirtual;
    }
    
    public boolean isVirtual() {
        return (bVirtual);
    }
    
    public void setVirtualValue(Object oVirtualValue) {
        this.oVirtualValue = oVirtualValue;
        this.bVirtual = true;
        this.bReadOnly = true;
    }
    
    public Object getVirtualValue() {
        return oVirtualValue;
    }
    
    public void setVirtualValueSQL(String sVirtualValueSQL) {
        this.sVirtualValueSQL = sVirtualValueSQL;
        this.bVirtual = true;
        this.bReadOnly = true;
    }
    
    public String getVirtualValueSQL() {
        return (sVirtualValueSQL);
    }
    
    public void setMinWidth(int iMinWidth) {
        this.iMinWidth = iMinWidth;
    }
    
    public int getMinWidth() {
        return (iMinWidth);
    }
    
    public void setMaxWidth(int iMaxWidth) {
        this.iMaxWidth = iMaxWidth;
    }
    
    public int getMaxWidth() {
        return (iMaxWidth);
    }
    
    public void setSpecType(String sSpecType) {
        this.sSpecType = sSpecType;
    }
    
    public String getSpecType() {
        return (sSpecType);
    }
    
    public void setHidden(boolean bHidden) {
        this.bHidden = bHidden;
    }
    
    public boolean isHidden() {
        return (bHidden);
    }
    
    public void setRecordInfo(String sRecordInfo) {
        this.sRecordInfo = sRecordInfo;
    }
    
    public String getRecordInfo() {
        return (sRecordInfo);
    }
    
    public boolean isRecordInfo() {
        return (!sRecordInfo.equalsIgnoreCase(""));
    }

    public void setColorField(boolean bColorField) {
        this.bColorField = bColorField;
    }
    
    public boolean isColorField() {
        return (bColorField);
    }
    
    public void setValidFromField(boolean bValidFrom) {
        this.bValidFrom = bValidFrom;
    }
    
    public boolean isValidFromField() {
        return (bValidFrom);
    }
    
    public void setValidToField(boolean bValidTo) {
        this.bValidTo = bValidTo;
    }
    
    public boolean isValidToField() {
        return (bValidTo);
    }
    
// + Public sName As String
// - Public sType As String
// + Public sDisplayName As String
// - Public sAlignment As String
// - Public sValue As String
//   Private imageValue() As Byte
//   Public bImageValue As Boolean
//   Public iImageWidth As Integer
//   Public iImageHeight As Integer
// - Public sOldValue As String
// - Public bKey As Boolean
// - Public bKeyEmptyAllowed As Boolean
// - Public bID As Boolean
//   Public bPW As Boolean
//   Public iLength As Integer  'maxlen
//   Public iMinLength As Integer  'minlen
// + Public bFilter As Boolean
// + Public sFilterValue As String
// + Public sAllowedChars As String
// + Public sDefaultValue As String
// + Public bReadOnly As Boolean
// + Public sSQLForDefaultValue As String
// + Public sLookup As String
// + Public sSQLForLookup As String
//   Public sSQLForIDLookup As String
//   Public iColumnWidth As Integer
//   Public iControl As Integer
// + Public bUppercase As Boolean
// + Public bMandatory As Boolean
//   Public bAdvisable As Boolean
// * Public bModifier As Boolean
// * Public bModificationTime As Boolean
// + Public sToolTipText As String
//   Public bFormLookupUgyfel As Boolean
//   Public bFormLookupItjSzj As Boolean
//   Public bFormLookupSzoveg As Boolean
//   Public bFormLookupOrszag As Boolean
//   Public lBackColor As Long
//   Public vMinValue As Variant
// ?  Public bRememberPreviousValue As Boolean 'MAG 2011.04.04.
//   Public sPreviousValue As String 'MAG 2011.04.04.
// * Public bVirtual As Boolean
// * Public sSQLForVirtual As String
// * Public sSQLFilterForVirtual As String
//   Public bUgyfelLookup As Boolean
//   Public bGkidLookup As Boolean
//   Public bIDLookup As Boolean
//   Public sIDLookupSQL As String
//   Public sIDLookupIDTextBoxName As String
//   Public bSzamlaKiIdLookup As Boolean
//   Public bVisible As Boolean
// + Public bColorField As Boolean
//   Public sColorFieldName As String
//   Public sEvaluateForMandatory As String
//   Public sEvaluateForTaxNumberHU As String
}
