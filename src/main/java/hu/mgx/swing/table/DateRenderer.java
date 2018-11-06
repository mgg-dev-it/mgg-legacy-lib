package hu.mgx.swing.table;

import java.awt.*;
import java.text.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import hu.mgx.app.common.*;

public class DateRenderer implements TableCellRenderer
{

    private JTextField jTextField;
    private SimpleDateFormat simpleDateFormat;
    private Color cSelectedColor;
    private FormatInterface mgxFormat;

    public DateRenderer(Color cSelectedColor, FormatInterface mgxFormat)
    {
        super();
        this.cSelectedColor = cSelectedColor;
        this.mgxFormat = mgxFormat;
        simpleDateFormat = mgxFormat.getDateFormat();
        jTextField = new JTextField();
        jTextField.setBorder(new EmptyBorder(0, 0, 0, 0));
        jTextField.setHorizontalAlignment(JTextField.CENTER);
    }

    public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        if (value == null)
        {
            jTextField.setText("");
        }
        else
        {
            jTextField.setText(simpleDateFormat.format((java.util.Date) value));
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
