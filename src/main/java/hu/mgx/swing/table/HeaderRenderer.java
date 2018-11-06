package hu.mgx.swing.table;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class HeaderRenderer implements TableCellRenderer
{

    public static final int ORDER_NONE = 0;
    public static final int ORDER_ASC = 1;
    public static final int ORDER_DESC = 2;
    private JLabel jLabel;
    private int iOrder;

    public HeaderRenderer(int iOrder)
    {
        super();
        this.iOrder = iOrder;
        jLabel = new JLabel();
        jLabel.setBorder(new EmptyBorder(0, 0, 0, 0));
    }

    public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        if (table != null)
        {
            JTableHeader header = table.getTableHeader();
            if (header != null)
            {
                jLabel.setForeground(header.getForeground());
                jLabel.setBackground(header.getBackground());
                jLabel.setFont(header.getFont());
            }
        }

        jLabel.setText((value == null) ? "" : value.toString());
        jLabel.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        jLabel.setForeground(Color.BLACK);
        if (iOrder == ORDER_DESC)
        {
            jLabel.setForeground(Color.RED);
        }
        if (iOrder == ORDER_ASC)
        {
            jLabel.setForeground(Color.BLUE);
        }
        jLabel.setHorizontalAlignment(JLabel.CENTER);
        return (jLabel);
    }
}
