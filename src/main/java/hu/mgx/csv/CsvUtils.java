package hu.mgx.csv;

import java.io.*;

import hu.mgx.app.common.*;
import hu.mgx.swing.table.*;
import hu.mgx.util.*;

public class CsvUtils
{

    public CsvUtils()
    {
    }

    public void writeMemoryTableIntoCsvFile(AppInterface appInterface, MemoryTable memoryTable, String sFilename, String sNullValue, String sSelect, String sSeparator, boolean bInApostrophes)
    {
        File f = new File(sFilename);
        FileOutputStream fos = null;
        StringBuffer sb = null;
        String s = "";
        try
        {
            fos = new FileOutputStream(f);
            for (int iRow = -1; iRow < memoryTable.getRowCount(); ++iRow)
            {
                sb = new StringBuffer("");
                for (int iCol = 0; iCol < memoryTable.getColumnCount(); ++iCol)
                {
                    if (iRow == -1)
                    {
                        s = StringUtils.isNull(memoryTable.getColumnName(iCol), sNullValue);
                    }
                    else
                    {
                        s = StringUtils.isNull(memoryTable.getValueAt(iRow, iCol), sNullValue);
                    }
                    s = StringUtils.stringReplace(s, StringUtils.sCrLf, "\\n");
                    if (bInApostrophes && !s.equals(sNullValue))
                    {
                        sb.append("\\'");
                    }
                    sb.append(s);
                    if (bInApostrophes && !s.equals(sNullValue))
                    {
                        sb.append("\\'");
                    }
                    sb.append(sSeparator);
                }
                sb.append(StringUtils.sCrLf);
                fos.write(sb.toString().getBytes("ISO-8859-1"));
            }
            fos.flush();
            fos.close();
        }
        catch (IOException ioe)
        {
            appInterface.handleError(ioe);
        }
    }
}
