package hu.mgx.swing.table;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import hu.mgx.app.common.*;

public class TextRenderer implements TableCellRenderer
{

    private JTextField jTextField;
    private Color cSelectedColor;
    private char cCR = 10;
    private char cLF = 13;

    public TextRenderer(Color cSelectedColor)
    {
        super();
        this.cSelectedColor = cSelectedColor;
        jTextField = new JTextField();
        jTextField.setBorder(new EmptyBorder(0, 0, 0, 0));
    }

    public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        if (value == null)
        {
            jTextField.setText("");
        }
        else
        {
            jTextField.setText(value.toString().replace(cCR, ' ').replace(cLF, ' '));
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
