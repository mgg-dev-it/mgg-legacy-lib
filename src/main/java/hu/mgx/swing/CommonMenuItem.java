package hu.mgx.swing;

import javax.swing.*;

public class CommonMenuItem extends JMenuItem
{

    private int iLanguageStringID = 0;
    private String sLanguageStringKey = "";

    public CommonMenuItem()
    {
        super();
    }

    public CommonMenuItem(int iLanguageStringID)
    {
        super();
        this.iLanguageStringID = iLanguageStringID;
    }

    public int getLanguageStringID()
    {
        return iLanguageStringID;
    }

    public void setLanguageStringID(int iLanguageStringID)
    {
        this.iLanguageStringID = iLanguageStringID;
    }

    public String getLanguageStringKey()
    {
        return sLanguageStringKey;
    }

    public void setLanguageStringKey(String sLanguageStringKey)
    {
        this.sLanguageStringKey = sLanguageStringKey;
    }
}
