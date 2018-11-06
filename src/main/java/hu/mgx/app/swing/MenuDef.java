package hu.mgx.app.swing;

import javax.swing.*;

public class MenuDef
{

    private String sText = "";
    private int iMnemonic = 0;
    private KeyStroke keyStroke = null;
    private String sTitle = "";

    private MenuDef()
    {
    }

    public MenuDef(String sText, int iMnemonic, KeyStroke keyStroke)
    {
        this.sText = sText;
        this.iMnemonic = iMnemonic;
        this.keyStroke = keyStroke;
        this.sTitle = sText;
    }

    public MenuDef(String sText, int iMnemonic)
    {
        this(sText, iMnemonic, null);
    }

    public java.lang.String getText()
    {
        return sText;
    }

    public void setText(java.lang.String sText)
    {
        this.sText = sText;
    }

    public int getMnemonic()
    {
        return iMnemonic;
    }

    public void setMnemonic(int iMnemonic)
    {
        this.iMnemonic = iMnemonic;
    }

    public javax.swing.KeyStroke getKeyStroke()
    {
        return keyStroke;
    }

    public void setKeyStroke(javax.swing.KeyStroke keyStroke)
    {
        this.keyStroke = keyStroke;
    }

    public java.lang.String getTitle()
    {
        return sTitle;
    }

    public void setTitle(java.lang.String sTitle)
    {
        this.sTitle = sTitle;
    }
}
