package hu.mgx.swing.table;

import java.awt.*;
import java.text.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import hu.mgx.app.common.*;

public class DecimalRenderer implements TableCellRenderer
{

    private JTextField jTextField;
    private DecimalFormat decimalFormat = new DecimalFormat("#,##0");
    private Color cSelectedColor;
    private int iScale = 0;
    private FormatInterface mgxFormat;
    private boolean bSuppressZero = false;

    public DecimalRenderer(Color cSelectedColor, int iScale, FormatInterface mgxFormat)
    {
        this(cSelectedColor, iScale, mgxFormat, false);
    }

    public DecimalRenderer(Color cSelectedColor, int iScale, FormatInterface mgxFormat, boolean bSuppressZero)
    {
        super();
        this.cSelectedColor = cSelectedColor;
        this.iScale = iScale;
        this.mgxFormat = mgxFormat;
        this.bSuppressZero = bSuppressZero;
        String sDecimals = "";
        if (iScale > 0)
        {
            while (sDecimals.length() < iScale)
            {
                sDecimals = sDecimals + "0";
            }
            sDecimals = "." + sDecimals;
        }
        decimalFormat = new DecimalFormat("#,##0" + sDecimals);
        DecimalFormatSymbols decimalFormatSymbols = decimalFormat.getDecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator(mgxFormat.getDecimalSeparator());
        decimalFormatSymbols.setGroupingSeparator(mgxFormat.getGroupingSeparator());
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
        jTextField = new JTextField();
        jTextField.setBorder(new EmptyBorder(0, 0, 0, 0));
        jTextField.setHorizontalAlignment(JTextField.RIGHT);
    }

    public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        if (value == null)
        {
            jTextField.setText("");
        }
        else
        {
            if (bSuppressZero && value.toString().equals("0"))
            {
                jTextField.setText("");
            }
            else
            {
                jTextField.setText(decimalFormat.format(value));
            }
        }
        if (isSelected)
        {
            jTextField.setBackground(cSelectedColor);
        }
        else
        {
            jTextField.setBackground(ColorManager.inputBackgroundFocusLost());
        }
        return (jTextField);
    }
}
