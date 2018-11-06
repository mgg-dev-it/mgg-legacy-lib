package hu.mgx.swing.table;

import java.awt.*;
import java.text.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import hu.mgx.app.common.*;

public class DateTimeRenderer implements TableCellRenderer
{

    private JTextField jTextField;
    private SimpleDateFormat simpleDateTimeFormat;
    private Color cSelectedColor;
    private FormatInterface mgxFormat;

    public DateTimeRenderer(Color cSelectedColor, FormatInterface mgxFormat)
    {
        super();
        this.cSelectedColor = cSelectedColor;
        this.mgxFormat = mgxFormat;
        simpleDateTimeFormat = mgxFormat.getDateTimeFormat();
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
            jTextField.setText(simpleDateTimeFormat.format(value));
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
