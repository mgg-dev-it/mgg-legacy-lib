package hu.mgx.app.swing;

import java.sql.*;

import hu.mgx.util.*;

public class AppParameters
{

    //--- essential base parameters ---
    private Class rootClass = null;
    private String sAppName = "";
    private int iMajor = 0;
    private int iMinor = 0;
    private int iRevision = 0;
    private int iAppWidth = 1024;
    private int iAppHeight = 768;
    private String sTitleLanguageStringKey = "";
    private Connection connection = null; //?

    //--- not essential, maybe composed parameters ---
    private String sLanguage = "";
    private String sCountry = "";
    private String sResourceBundleBaseName = "";
    private String sPropertyFileName = "";
    private String sLogFileName = "";
    private String sSplashImageFileName = "";
    private String sIconImageFileName = "";    //--- own variables ---
    private String sFileSeparator = "";

    public AppParameters()
    {
    }

    public AppParameters(Class rootClass, String sAppName, int iMajor, int iMinor, int iRevision, int iAppWidth, int iAppHeight)
    {
        this(rootClass, sAppName, iMajor, iMinor, iRevision, iAppWidth, iAppHeight, "");
    }

    public AppParameters(Class rootClass, String sAppName, int iMajor, int iMinor, int iRevision, int iAppWidth, int iAppHeight, String sTitleLanguageStringKey)
    {
        this.rootClass = rootClass;
        this.sAppName = sAppName;
        this.iMajor = iMajor;
        this.iMinor = iMinor;
        this.iRevision = iRevision;
        this.iAppWidth = iAppWidth;
        this.iAppHeight = iAppHeight;
        this.sTitleLanguageStringKey = sTitleLanguageStringKey;
//        if (sTitleLanguageStringKey.equals(""))
//        {
//            sTitleLanguageStringKey = sAppName;
//        }
        createComposedParameters();
    }

    public final void createComposedParameters()
    {
        sFileSeparator = SystemUtils.getSystemProperty("file.separator");
        if (sLanguage.equals(""))
        {
            sLanguage = "hu";
        }
        if (sCountry.equals(""))
        {
            sCountry = "HU";
        }
        if (sResourceBundleBaseName.equals(""))
        {
            sResourceBundleBaseName = (rootClass.getPackage().getName().equals("") ? "" : rootClass.getPackage().getName() + ".") + sAppName + "Resources";
        }
        if (sPropertyFileName.equals(""))
        {
            sPropertyFileName = "." + sFileSeparator + sAppName + ".properties";
        }
        if (sLogFileName.equals(""))
        {
            sLogFileName = "." + sFileSeparator + sAppName + ".log";
        }
    }

    public java.lang.String getTitleLanguageStringKey()
    {
        return sTitleLanguageStringKey;
    }

    public java.lang.String getAppName()
    {
        return sAppName;
    }

    public void setAppName(java.lang.String sAppName)
    {
        this.sAppName = sAppName;
    }

    public int getMajor()
    {
        return iMajor;
    }

    public void setMajor(int iMajor)
    {
        this.iMajor = iMajor;
    }

    public int getMinor()
    {
        return iMinor;
    }

    public void setMinor(int iMinor)
    {
        this.iMinor = iMinor;
    }

    public int getRevision()
    {
        return iRevision;
    }

    public int getAppWidth()
    {
        return iAppWidth;
    }

    public int getAppHeight()
    {
        return iAppHeight;
    }

    public void setRevision(int iRevision)
    {
        this.iRevision = iRevision;
    }

    public java.lang.String getLanguage()
    {
        return sLanguage;
    }

    public void setLanguage(java.lang.String sLanguage)
    {
        this.sLanguage = sLanguage;
    }

    public java.lang.String getCountry()
    {
        return sCountry;
    }

    public void setCountry(java.lang.String sCountry)
    {
        this.sCountry = sCountry;
    }

    public java.lang.String getResourceBundleBaseName()
    {
        return sResourceBundleBaseName;
    }

    public void setResourceBundleBaseName(java.lang.String sResourceBundleBaseName)
    {
        this.sResourceBundleBaseName = sResourceBundleBaseName;
    }

    public java.lang.String getPropertyFileName()
    {
        return sPropertyFileName;
    }

    public void setPropertyFileName(java.lang.String sPropertyFileName)
    {
        this.sPropertyFileName = sPropertyFileName;
    }

    public java.lang.String getLogFileName()
    {
        return sLogFileName;
    }

    public void setLogFileName(java.lang.String sLogFileName)
    {
        this.sLogFileName = sLogFileName;
    }
}
