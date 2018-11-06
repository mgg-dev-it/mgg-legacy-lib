package hu.mgx.swing;

import java.awt.*;

import javax.swing.*;

public class CommonLabel extends JLabel
{

    public CommonLabel()
    {
        super();
    }

    public CommonLabel(int iWidth, int iHeight)
    {
        this();
        setSize(new Dimension(iWidth, iHeight));
    }

    public void setSize(java.awt.Dimension d)
    {
        setMinimumSize(d);
        setPreferredSize(d);
        setMaximumSize(d);
    }
}
