package hu.mgx.swing.table;

import javax.swing.*;
import javax.swing.table.*;

public class ButtonRenderer implements TableCellRenderer
{

    private JButton jButton;

    public ButtonRenderer(String sCaption)
    {
        super();
        jButton = new JButton(sCaption);
    }

    public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        return (jButton);
    }
}
