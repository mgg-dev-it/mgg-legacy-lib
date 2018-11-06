package hu.mgx.swing.table;

import java.awt.*;

import javax.swing.*;
import javax.swing.table.*;

public class BitRenderer implements TableCellRenderer
{

    private JCheckBox jCheckBox;
    private Color cSelectedColor;

    public BitRenderer(Color c)
    {
        super();
        jCheckBox = new JCheckBox();
        jCheckBox.setHorizontalAlignment(SwingConstants.CENTER);
        cSelectedColor = c;
    }

    public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        if (value == null)
        {
            jCheckBox.setSelected(false);
        }
        else if (value.toString().equals(""))
        {
            jCheckBox.setSelected(false);
        }
        else if (value.toString().equals("0"))
        {
            jCheckBox.setSelected(false);
        }
        else if (value.toString().equals("1"))
        {
            jCheckBox.setSelected(true);
        }
        else if (value.toString().equals("false"))
        {
            jCheckBox.setSelected(false);
        }
        else if (value.toString().equals("true"))
        {
            jCheckBox.setSelected(true);
        }
        if (isSelected)
        {
            jCheckBox.setBackground(cSelectedColor);
        }
        else
        {
            jCheckBox.setBackground(Color.WHITE);
        }
        return (jCheckBox);
    }
}
