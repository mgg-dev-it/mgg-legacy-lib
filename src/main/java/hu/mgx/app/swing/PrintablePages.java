package hu.mgx.app.swing;

import java.util.*;

public class PrintablePages
{

    private Vector vPrintablePages = null;

    public PrintablePages()
    {
        vPrintablePages = new Vector();
    }

    public void add(PrintablePage printablePage)
    {
        vPrintablePages.add(printablePage);
    }

    public int getPageCount()
    {
        return (vPrintablePages.size());
    }

    public PrintablePage getPrintablePage(int index)
    {
        if (index < 0)
        {
            return (null);
        }
        if (index >= vPrintablePages.size())
        {
            return (null);
        }
        return ((PrintablePage) vPrintablePages.elementAt(index));
    }

}
