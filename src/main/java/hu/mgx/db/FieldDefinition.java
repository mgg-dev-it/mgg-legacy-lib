package hu.mgx.db;

import java.sql.*;

public class FieldDefinition
{

    private String sName;
    private String sDisplayName;
    private int iType;
    private int iLength;
    private int iPrecision; // összes számjegy száma egy számban
    private int iScale; // számjegyek száma egy számban a tizedesjeltől jobbra
    private boolean bAllowNulls = true;
    private boolean bMandatory = false;
    private boolean bKey = false;
    private boolean bID = false;
    private boolean bSerial = false;
    private boolean bListOrder = false;
    private boolean bPW = false;
    private boolean bText = false;
    private boolean bPermission = false;
    private boolean bPermissionString = false;
    private boolean bUppercase = false;
    private boolean bReadOnly = false;
    private String sDefaultValue = "";
    private String sSQLForDefaultValue = "";
    private boolean bModifier = false;
    private boolean bModificationTime = false;
    //private boolean bLookup = false;
    private String sLookup = "";
    private String sRealTimeSQLLookup = "";
    private String sFilter = "";
    private boolean bVisible = true;
    private int iPreferredWidth = -1;
    private FieldCheck fieldChecker = null;
//    private boolean bCallDate = false;
//    private boolean bServiceDate = false;
    private String sToolTip = "";

    public FieldDefinition()
    {
    }

    public FieldDefinition(String sName, String sDisplayName, int iType)
    {
        this(sName, sDisplayName, iType, 10, 10, 0, true, false, false);
    }

    public FieldDefinition(String sName, String sDisplayName, int iType, int iLength, int iPrecision, int iScale, boolean bAllowNulls, boolean bKey, boolean bID)
    {
        this(sName, sDisplayName, iType, iLength, iPrecision, iScale, bAllowNulls, bKey, bID, false);
    }

    public FieldDefinition(String sName, String sDisplayName, int iType, int iLength, int iPrecision, int iScale, boolean bAllowNulls, boolean bKey, boolean bID, boolean bMandatory)
    {
        this.sName = sName;
        this.sDisplayName = sDisplayName;
        this.iType = iType;
        this.iLength = iLength;
        this.iPrecision = iPrecision;
        this.iScale = (iType == FieldType.INT || iScale < 0 ? 0 : iScale);
        this.bAllowNulls = bAllowNulls;
        this.bKey = bKey;
        this.bID = bID;
        this.bMandatory = bMandatory;
        if (bID)
        {
            bReadOnly = true;
        }
    }

    public void setName(String sName)
    {
        this.sName = sName;
    }

    public String getName()
    {
        return (sName);
    }

    public void setDisplayName(String sDisplayName)
    {
        this.sDisplayName = sDisplayName;
    }

    public String getDisplayName()
    {
        return (sDisplayName);
    }

    public int getType()
    {
        return (iType);
    }

    public void setType(int iType)
    {
        this.iType = iType;
    }

    public int getLength()
    {
        return (iLength);
    }

    public void setLength(int iLength)
    {
        this.iLength = iLength;
    }

    public int getPrecision()
    {
        return (iPrecision);
    }

    public int getScale()
    {
        return (iScale);
    }

    public boolean isAllowNulls()
    {
        return (bAllowNulls);
    }

    public void setMandatory(boolean b)
    {
        bMandatory = b;
        if (bMandatory)
        {
            bAllowNulls = false;
        }
    }

    public boolean isMandatory()
    {
        return (bMandatory);
    }

    public void setKey(boolean b)
    {
        bKey = b;
        if (bKey)
        {
            bAllowNulls = false;
        }
    }

    public boolean isKey()
    {
        return (bKey);
    }

    public void setID(boolean b)
    {
        bID = b;
        if (bID)
        {
            bReadOnly = true;
        }
    //        setDefaultValue("0");
    }

    public boolean isID()
    {
        return (bID);
    }

    public void setSerial(boolean b)
    {
        bSerial = b;
    }

    public boolean isSerial()
    {
        return (bSerial);
    }

    public void setListOrder(boolean b)
    {
        bListOrder = b;
    }

    public boolean isListOrder()
    {
        return (bListOrder);
    }

    public void setPW(boolean b)
    {
        bPW = b;
    }

    //public boolean getPW(){return (bPW);}
    public boolean isPW()
    {
        return (bPW);
    }

    public void setPermission(boolean b)
    {
        bPermission = b;
    }

    public boolean isPermission()
    {
        return (bPermission);
    }

    public void setPermissionString(boolean b)
    {
        bPermissionString = b;
    }

    public boolean isPermissionString()
    {
        return (bPermissionString);
    }

    public void setText(boolean b)
    {
        bText = b;
    }

    public boolean isText()
    {
        return (bText);
    }

    public void setReadOnly(boolean b)
    {
        bReadOnly = b;
    }

    public boolean isReadOnly()
    {
        return (bReadOnly);
    }

    public void setUppercase(boolean b)
    {
        bUppercase = b;
    }

    public boolean isUppercase()
    {
        return (bUppercase);
    }

    /**
     * Beállítja a statikus  lookup value-display párokat.
     * <p>Formátuma: "value1|display1@...@value(n)|display(n)"
     * <p>A felhasználó számára a "display..." értékek jelennek meg,
     * az adatbázisban pedig a "value..." értékek tárolódnak.
     * @param sLookup A lookup value-display párok.
     */
    public void setLookup(String sLookup)
    {
        this.sLookup = sLookup;
    }

    /**
     * Lekérdezi a statikus lookup value-display párokat.
     * <p>Formátuma: "value1|display1@...@value(n)|display(n)"
     * <p>A felhasználó számára a "display..." értékek jelennek meg,
     * az adatbázisban pedig a "value..." értékek tárolódnak.
     * @return A lookup value-display párok.
     */
    public String getLookup()
    {
        return (sLookup);
    }

    /**
     * Lekérdezi a mező lookup tulajdonságát.
     * @return <p>true, ha lookup a mező; <p>false, ha nem.
     */
    public boolean isLookup()
    {
        return (!sLookup.trim().equals(""));
    }

    public void setRealTimeSQLLookup(String sRealTimeSQLLookup)
    {
        this.sRealTimeSQLLookup = sRealTimeSQLLookup;
    }

    public String getRealTimeSQLLookup()
    {
        return (sRealTimeSQLLookup);
    }

    public void setFilter(String sFilter)
    {
        this.sFilter = sFilter;
        if (isFilter())
        {
            bReadOnly = true;
        }
    }

    public String getFilter()
    {
        return (sFilter);
    }

    public boolean isFilter()
    {
        return (!sFilter.trim().equals(""));
    }

    public void setDefaultValue(String s)
    {
        sDefaultValue = s;
    }

    public String getDefaultValue()
    {
        if (isFilter())
        {
            return (sFilter);
        }
        else
        {
            return (sDefaultValue);
        }
    }

    public void setSQLForDefaultValue(String s)
    {
        sSQLForDefaultValue = s;
    }

    public String getSQLForDefaultValue()
    {
        return (sSQLForDefaultValue);
    }

    public void setModifier(boolean b)
    {
        bModifier = b;
    }

    public boolean isModifier()
    {
        return (bModifier);
    }

    public void setModificationTime(boolean b)
    {
        bModificationTime = b;
    }

    public boolean isModificationTime()
    {
        return (bModificationTime);
    }

    public void setVisible(boolean bVisible)
    {
        this.bVisible = bVisible;
    }

    public boolean isVisible()
    {
        return (bVisible);
    }

    public void setPreferredWidth(int iPreferredWidth)
    {
        this.iPreferredWidth = iPreferredWidth;
    }

    public int getPreferredWidth()
    {
        return (iPreferredWidth);
    }

    public void setFieldChecker(FieldCheck fieldChecker)
    {
        this.fieldChecker = fieldChecker;
    }

    public boolean checkValue(java.awt.Component parentComponent, Object value)
    {
        if (this.fieldChecker == null)
        {
            return (true);
        }
        return (fieldChecker.checkValue(parentComponent, value));
    }

    public boolean checkModifiedValue(java.awt.Component parentComponent, Object value, Object oldValue)
    {
        if (this.fieldChecker == null)
        {
            return (true);
        }
        return (fieldChecker.checkModifiedValue(parentComponent, value, oldValue));
    }

    public boolean checkInsertValue(java.awt.Component parentComponent, Object value)
    {
        if (this.fieldChecker == null)
        {
            return (true);
        }
        return (fieldChecker.checkInsertValue(parentComponent, value));
    }

//    public void setCallDate(boolean bCallDate)
//    {
//        this.bCallDate = bCallDate;
//    }
//
//    public boolean isCallDate()
//    {
//        return (bCallDate);
//    }
//
//    public void setServiceDate(boolean bServiceDate)
//    {
//        this.bServiceDate = bServiceDate;
//    }
//
//    public boolean isServiceDate()
//    {
//        return (bServiceDate);
//    }
    public void executeSetRealTimeSQLLookupValue(Connection connection)
    {
        String sLookup = "";
        String sFieldName = "";
        PreparedStatement pst;
        ResultSet rs;

        if (!getRealTimeSQLLookup().equals(""))
        {
            //System.err.println(sSQL);
            try
            {
                pst = connection.prepareStatement(getRealTimeSQLLookup(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                rs = pst.executeQuery();

                sLookup = "";
                while (rs.next())
                {
                    if (!sLookup.equals(""))
                    {
                        sLookup += "@";
                    }
                    //sLookup += encode(rs.getString("value").trim());
                    sLookup += rs.getString("value").trim();
                    sLookup += "|";
                    //sLookup += encode(rs.getString("display").trim());
                    sLookup += rs.getString("display").trim();
                }
                pst.close();
                pst = null;
                rs.close();
                rs = null;
                //System.err.println(sLookup);
                setLookup(sLookup);
            }
            catch (SQLException e)
            {
            }
        }
        return;
    }

    public String getToolTip()
    {
        return sToolTip;
    }

    public void setToolTip(String sToolTip)
    {
        this.sToolTip = sToolTip;
    }
}
