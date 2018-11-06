package hu.mag.swing.table;

import java.awt.Cursor;
import static java.awt.event.InputEvent.CTRL_DOWN_MASK;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.JTableHeader;

/**
 *
 * @author MaG
 */
public class MagBasicTableHeaderUI extends BasicTableHeaderUI {

    protected JTableHeader jTableHeader;

    public MagBasicTableHeaderUI(JTableHeader jTableHeader) {
        this.jTableHeader = jTableHeader;
    }

    public class MagMouseInputHandler extends MouseInputHandler {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (!jTableHeader.isEnabled()) {
                return;
            }
            boolean bCtrlPressed = false;
            if ((e.getModifiersEx() & CTRL_DOWN_MASK) == CTRL_DOWN_MASK) {
                bCtrlPressed = true;
            }
            if (e.getClickCount() % 2 == 1 && SwingUtilities.isLeftMouseButton(e)) {
                JTable table = jTableHeader.getTable();
                RowSorter sorter;
                if (table != null && (sorter = table.getRowSorter()) != null) {
                    int columnIndex = jTableHeader.columnAtPoint(e.getPoint());
                    if (columnIndex != -1) {
                        columnIndex = table.convertColumnIndexToModel(columnIndex);
//                        if (!bCtrlPressed) {
//                            sorter.setSortKeys(null);
//                        }
                        //sorter.toggleSortOrder(columnIndex);
                        table.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                        toggleSortOrder(columnIndex, bCtrlPressed);
                        table.setCursor(Cursor.getDefaultCursor());
                    }
                }
            }
        }
    }

    //@todo task: maximum number of sortable columns? (maxSortKeys in DefaultRowSorter)
    //@todo task: sortable/not sortable settings of a column
    private void toggleSortOrder(int column, boolean bCtrlPressed) {
        RowSorter sorter = jTableHeader.getTable().getRowSorter();
//        checkColumn(column);
//        if (isSortable(column)) {
        List<RowSorter.SortKey> keys = new ArrayList<RowSorter.SortKey>(sorter.getSortKeys());
        RowSorter.SortKey sortKey;

        int iKeyCount = keys.size();
        boolean bFound = false;

        int sortIndex;
        for (sortIndex = keys.size() - 1; sortIndex >= 0; sortIndex--) {
            if (keys.get(sortIndex).getColumn() == column) {
                bFound = true;
                break;
            }
        }
//        System.out.println("keys' size = " + Integer.toString(keys.size()));
//        System.out.println("sortIndex = " + Integer.toString(sortIndex));
        if (!bCtrlPressed) {
            if (bFound && iKeyCount == 1) {
                keys.set(0, toggle(keys.get(0)));
            } else {
                //sorter.setSortKeys(null);
                keys.clear();
                sortKey = new RowSorter.SortKey(column, SortOrder.ASCENDING);
                keys.add(sortKey);
            }
        } else {
            if (bFound) {
                keys.set(sortIndex, toggle(keys.get(sortIndex)));
            } else {
                if (keys.size() < 5) {
                    sortKey = new RowSorter.SortKey(column, SortOrder.ASCENDING);
                    keys.add(sortKey);
                }
            }
        }

        if (1 == 0) {
            if (sortIndex == -1) {
                // Key doesn't exist
                sortKey = new RowSorter.SortKey(column, SortOrder.ASCENDING);
                //keys.add(0, sortKey);
                keys.add(sortKey);
            } else if (sortIndex == 0) {
                // It's the primary sorting key, toggle it
                keys.set(0, toggle(keys.get(0)));
            } else {
                // It's not the first, but was sorted on, remove old
                // entry, insert as first with ascending.
                keys.remove(sortIndex);
                keys.add(0, new RowSorter.SortKey(column, SortOrder.ASCENDING));
            }
            if (keys.size() > 3) {
                //keys = keys.subList(0, 3);
                keys = keys.subList(keys.size() - 3, keys.size());
            }
        }

        sorter.setSortKeys(keys);
//        }
    }

    private RowSorter.SortKey toggle(RowSorter.SortKey key) {
        if (key.getSortOrder() == SortOrder.ASCENDING) {
            return new RowSorter.SortKey(key.getColumn(), SortOrder.DESCENDING);
        }
        return new RowSorter.SortKey(key.getColumn(), SortOrder.ASCENDING);
    }

    protected MouseInputListener createMouseInputListener() {
        return new MagMouseInputHandler();
    }

}
