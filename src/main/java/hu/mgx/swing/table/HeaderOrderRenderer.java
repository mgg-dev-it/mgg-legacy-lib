package hu.mgx.swing.table;

import hu.mgx.swing.CommonPanel;
import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import hu.mgx.app.swing.*;

public class HeaderOrderRenderer implements TableCellRenderer
{

    protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);
    public static final int ORDER_NONE = 0;
    public static final int ORDER_ASC = 1;
    public static final int ORDER_DESC = 2;
    private JLabel jLabel;
    private CommonPanel commonPanel;
    private int iOrder;

    public HeaderOrderRenderer(int iOrder)
    {
        this.iOrder = iOrder;
        init();
    }

    private void init()
    {
        commonPanel = new CommonPanel();
        commonPanel.setInsets(0, 1, 0, 1);
        commonPanel.setBorder(new EmptyBorder(0, 1, 0, 1));

        jLabel = new JLabel()
        {

            public void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                paintArrow(this, g);
            }
        };
        jLabel.setBorder(new EmptyBorder(0, 1, 0, 1));

    }

    private void paintArrow(Component c, Graphics g)
    {
        if (iOrder == ORDER_NONE)
        {
            return;
        }
        int iArrowWidth = 6;
        int iArrowHeight = 8;
        int cy = c.getHeight() / 2;
        int cx = c.getWidth() - cy;
        Point p1 = new Point(0, 0);
        Point p2 = new Point(0, 0);
        Point p3 = new Point(0, 0);
        g.setColor(Color.BLUE);
        Polygon p = new Polygon();
        if (iOrder != ORDER_NONE)
        {
            if (iOrder == ORDER_DESC)
            {
                p1 = new Point(cx - (iArrowWidth / 2), cy - (iArrowHeight / 2));
                p2 = new Point(cx + (iArrowWidth / 2), cy - (iArrowHeight / 2));
                p3 = new Point(cx, cy + (iArrowHeight / 2));
            }
            if (iOrder == ORDER_ASC)
            {
                p1 = new Point(cx - (iArrowWidth / 2), cy + (iArrowHeight / 2));
                p2 = new Point(cx + (iArrowWidth / 2), cy + (iArrowHeight / 2));
                p3 = new Point(cx, cy - (iArrowHeight / 2));
            }
            p.addPoint(p1.x, p1.y);
            p.addPoint(p2.x, p2.y);
            p.addPoint(p3.x, p3.y);
            g.fillPolygon(p);
            g.setColor(new Color(128, 128, 255));
            g.drawLine(p1.x, p1.y, p2.x, p2.y);
            g.drawLine(p2.x, p2.y, p3.x, p3.y);
            g.drawLine(p3.x, p3.y, p1.x, p1.y);
        }
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
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
            jLabel.setForeground(Color.BLUE);
        }
        if (iOrder == ORDER_ASC)
        {
            jLabel.setForeground(Color.BLUE);
        }
        jLabel.setHorizontalAlignment(JLabel.CENTER);


        JPanel jPanelX = new JPanel()
        {

            public void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                int w2 = this.getWidth() / 2;
                int h2 = this.getHeight() / 2;
                g.setColor(Color.BLUE);
                //g.fillRect(w / 4, h / 4, w / 2, h / 2); // x, y, w, h
                Polygon p = new Polygon();
                p.addPoint(w2 - 3, h2 - 4);
                p.addPoint(w2 + 3, h2 - 4);
                p.addPoint(w2, h2 + 4);
                g.fillPolygon(p);
            }
        };
        jPanelX.setBorder(new EmptyBorder(0, 1, 0, 1));

        commonPanel.setRow(0);
        commonPanel.addToCurrentRow(jLabel, 1, 1, 1.0, 1.0, GridBagConstraints.BOTH);
        if (iOrder != ORDER_NONE)
        {
        }

        return (jLabel);
    }
}
