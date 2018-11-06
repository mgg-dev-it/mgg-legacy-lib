package hu.mag.swing.table;

import hu.mgx.util.StringUtils;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author MaG
 */
public class MagHeaderCellRenderer implements TableCellRenderer {

    private JLabel jLabel;
    int iKeyOrder = -1;
    SortOrder sortOrder = SortOrder.UNSORTED;

    public MagHeaderCellRenderer() {
        init();
    }

    private void init() {
        jLabel = new JLabel() {

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                paintArrow(this, g);
            }
        };
        jLabel.setHorizontalAlignment(JLabel.CENTER);
    }

    private void drawTriangle(int x, int y, boolean bAscending, Graphics g, Color color) {
        Point p1 = new Point(x, y);
        Point p2 = new Point(x - 3, bAscending ? y - 3 : y + 3);
        Point p3 = new Point(x + 3, bAscending ? y - 3 : y + 3);
        g.setColor(color);
        Polygon p = new Polygon();
        p.addPoint(p1.x, p1.y);
        p.addPoint(p2.x, p2.y);
        p.addPoint(p3.x, p3.y);
        g.fillPolygon(p);
        g.drawLine(p1.x, p1.y, p2.x, p2.y);
        g.drawLine(p2.x, p2.y, p3.x, p3.y);
        g.drawLine(p3.x, p3.y, p1.x, p1.y);
    }

    //@todo task: calculate values from Component height (width?)
    private void paintArrow(Component c, Graphics g) {
        if (sortOrder == SortOrder.UNSORTED) {
            return;
        }
        Font font = jLabel.getFont();
        FontMetrics fm = jLabel.getFontMetrics(font);
        int iTextWidth = 0;
        String sText = jLabel.getText();
        if (sText.startsWith("<html>") && sText.contains("<br>")) {
            sText = StringUtils.stringReplace(sText, "<html>", "");
            sText = StringUtils.stringReplace(sText, "</html>", "");
            sText = StringUtils.stringReplace(sText, "<center>", "");
            String[] sTextSplit = sText.split("<br>");
            for (int j = 0; j < sTextSplit.length; j++) {
                sTextSplit[j] = sTextSplit[j] + "XX"; //for the triangles which show the sorting
                if (fm.stringWidth(sTextSplit[j]) > iTextWidth) {
                    iTextWidth = fm.stringWidth(sTextSplit[j]);
                }
            }
        } else {
            iTextWidth = fm.stringWidth(jLabel.getText() + "XX");
        }
        //int iTextWidth = fm.stringWidth(jLabel.getText() + " ");
        int x = (c.getWidth() + iTextWidth) / 2;

        boolean bAscending = sortOrder == SortOrder.ASCENDING;
        Color color;
        for (int i = 0; i <= iKeyOrder; i++) {
            color = new Color(i * 48, i * 48, 192);
            drawTriangle(x, bAscending ? c.getHeight() - 3 - (4 - iKeyOrder) - 3 * i : 3 + (3 - iKeyOrder) + 3 * i, bAscending, g, color);
        }
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column
    ) {
        boolean bSorted = false;
        iKeyOrder = -1;
        sortOrder = SortOrder.UNSORTED;
        if (table != null) {
            JTableHeader header = table.getTableHeader();
            if (header != null) {
                jLabel.setForeground(header.getForeground());
                jLabel.setBackground(header.getBackground());
                jLabel.setFont(header.getFont());
            }
            List<SortKey> keys = new ArrayList<SortKey>(table.getRowSorter().getSortKeys());
            for (int i = 0; i < keys.size(); i++) {
                if (keys.get(i).getColumn() == column) {
                    bSorted = true;
                    iKeyOrder = i;
                    sortOrder = keys.get(i).getSortOrder();
                }
            }
        }
        String sText = (value == null) ? "" : value.toString();
        if (iKeyOrder > -1) {
            sText = sText + "  ";
        }
        jLabel.setText(sText);
        jLabel.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        jLabel.setForeground(bSorted ? Color.BLUE : Color.BLACK);
        return (jLabel);
    }

}
