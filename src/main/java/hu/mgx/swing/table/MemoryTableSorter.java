package hu.mgx.swing.table;

import hu.mgx.util.StringUtils;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;

/**
 *
 * @author MaG
 */
public class MemoryTableSorter implements Comparator<Integer> {

    private MemoryTable memoryTable;
    private String sOrderBy;
    private Integer[] iaOriginToSortedRowIndexes;
    private Integer[] iaSortedToOriginRowIndexes;
    private Vector<Integer> vColumn;
    private Vector<Class> vClass;
    private Vector<Boolean> vDescending;

    /**
     *
     * @param memoryTable memoryTable
     * @param sOrderBy - "1, 2, 3" - "name, city, age" - "age desc, name" - "3,
     * 2 desc, 5"
     */
    public MemoryTableSorter(MemoryTable memoryTable, String sOrderBy) {
        this.memoryTable = memoryTable;
        this.sOrderBy = sOrderBy;
        init();
    }

    private void init() {
        vColumn = new Vector<Integer>();
        vClass = new Vector<Class>();
        vDescending = new Vector<Boolean>();
        String[] sa = sOrderBy.split(",");
        boolean bDescending = false;
        int iColumn = -1;
        for (int i = 0; i < sa.length; i++) {
            sa[i] = sa[i].trim();
            if (sa[i].trim().endsWith(" asc")) {
                sa[i] = sa[i].substring(0, sa[i].length() - 4);
            }
            if (sa[i].trim().endsWith(" desc")) {
                sa[i] = sa[i].substring(0, sa[i].length() - 5);
                bDescending = true;
            }
            //System.out.println("* " + sa[i]);
            if (StringUtils.isDigits(sa[i])) {
                iColumn = StringUtils.intValue(sa[i]);
            } else {
                iColumn = memoryTable.findColumn(sa[i]);
            }
            if (iColumn > -1 && iColumn < memoryTable.getColumnCount()) {
                //now we have  the column index, the column class and the direction
                vColumn.add(new Integer(iColumn));
                vClass.add(memoryTable.getColumnClass(iColumn));
                vDescending.add(new Boolean(bDescending));
            }
        }

        iaOriginToSortedRowIndexes = new Integer[memoryTable.getRowCount()];
        for (int i = 0; i < iaOriginToSortedRowIndexes.length; i++) {
            iaOriginToSortedRowIndexes[i] = i;
        }
        Arrays.sort(iaOriginToSortedRowIndexes, this);

        //debug
//        for (int i = 0; i < iaOriginToSortedRowIndexes.length; i++) {
//            System.out.println(iaOriginToSortedRowIndexes[i]);
//        }
        iaSortedToOriginRowIndexes = new Integer[memoryTable.getRowCount()];
        for (int i = 0; i < iaSortedToOriginRowIndexes.length; i++) {
            iaSortedToOriginRowIndexes[iaOriginToSortedRowIndexes[i]] = i;
        }

        //debug
//        for (int i = 0; i < iaSortedToOriginRowIndexes.length; i++) {
//            System.out.println(iaSortedToOriginRowIndexes[i]);
//        }
    }

    public int convertRowIndexToOrigin(int iSortedRowIndex) {
        if (iSortedRowIndex < 0 || iSortedRowIndex >= iaSortedToOriginRowIndexes.length) {
            return (iSortedRowIndex);
        }
        return (iaSortedToOriginRowIndexes[iSortedRowIndex]);
    }

    public int convertRowIndexToSorted(int iOriginRowIndex) {
        if (iOriginRowIndex < 0 || iOriginRowIndex >= iaOriginToSortedRowIndexes.length) {
            return (iOriginRowIndex);
        }
        return (iaOriginToSortedRowIndexes[iOriginRowIndex]);
    }

    @Override
    public int compare(Integer intRow1, Integer intRow2) {
        int iCompare = 0;
        //System.out.println("- " + Integer.toString(vColumn.size()));
        for (int i = 0; i < vColumn.size() && iCompare == 0; i++) {
            if (vClass.elementAt(i) == java.lang.Integer.class) {
                iCompare = memoryTable.getIntegerValueAt(intRow1.intValue(), vColumn.elementAt(i).intValue()).compareTo(memoryTable.getIntegerValueAt(intRow2.intValue(), vColumn.elementAt(i).intValue()));
            } else if (vClass.elementAt(i) == java.lang.Long.class) {
                iCompare = memoryTable.getLongValueAt(intRow1.intValue(), vColumn.elementAt(i).intValue()).compareTo(memoryTable.getLongValueAt(intRow2.intValue(), vColumn.elementAt(i).intValue()));
            } else if (vClass.elementAt(i) == java.lang.Short.class) {
                iCompare = memoryTable.getShortValueAt(intRow1.intValue(), vColumn.elementAt(i).intValue()).compareTo(memoryTable.getShortValueAt(intRow2.intValue(), vColumn.elementAt(i).intValue()));
            } else if (vClass.elementAt(i) == java.lang.Double.class) {
                iCompare = memoryTable.getDoubleValueAt(intRow1.intValue(), vColumn.elementAt(i).intValue()).compareTo(memoryTable.getDoubleValueAt(intRow2.intValue(), vColumn.elementAt(i).intValue()));
            } else if (vClass.elementAt(i) == java.lang.Float.class) {
                iCompare = memoryTable.getFloatValueAt(intRow1.intValue(), vColumn.elementAt(i).intValue()).compareTo(memoryTable.getFloatValueAt(intRow2.intValue(), vColumn.elementAt(i).intValue()));
            } else if (vClass.elementAt(i) == java.math.BigDecimal.class) {
                iCompare = memoryTable.getBigDecimalValueAt(intRow1.intValue(), vColumn.elementAt(i).intValue()).compareTo(memoryTable.getBigDecimalValueAt(intRow2.intValue(), vColumn.elementAt(i).intValue()));
            } else if (vClass.elementAt(i) == java.util.Date.class) {
                iCompare = memoryTable.getUtilDateValueAt(intRow1.intValue(), vColumn.elementAt(i).intValue()).compareTo(memoryTable.getUtilDateValueAt(intRow2.intValue(), vColumn.elementAt(i).intValue()));
            } else if (vClass.elementAt(i) == java.sql.Date.class) {
                iCompare = new java.sql.Date(memoryTable.getUtilDateValueAt(intRow1.intValue(), vColumn.elementAt(i).intValue()).getTime()).compareTo(new java.sql.Date(memoryTable.getUtilDateValueAt(intRow2.intValue(), vColumn.elementAt(i).intValue()).getTime()));
            } else if (vClass.elementAt(i) == java.sql.Time.class) {
                iCompare = new java.sql.Time(memoryTable.getUtilDateValueAt(intRow1.intValue(), vColumn.elementAt(i).intValue()).getTime()).compareTo(new java.sql.Time(memoryTable.getUtilDateValueAt(intRow2.intValue(), vColumn.elementAt(i).intValue()).getTime()));
            } else if (vClass.elementAt(i) == java.sql.Timestamp.class) {
                iCompare = new java.sql.Timestamp(memoryTable.getUtilDateValueAt(intRow1.intValue(), vColumn.elementAt(i).intValue()).getTime()).compareTo(new java.sql.Timestamp(memoryTable.getUtilDateValueAt(intRow2.intValue(), vColumn.elementAt(i).intValue()).getTime()));
            } else {
                iCompare = memoryTable.getStringValueAt(intRow1.intValue(), vColumn.elementAt(i).intValue()).compareTo(memoryTable.getStringValueAt(intRow2.intValue(), vColumn.elementAt(i).intValue()));
            }
            if (vDescending.elementAt(i) && iCompare != 0) {
                iCompare = (iCompare < 0 ? 1 : -1);
            }
            //System.out.println(Integer.toString(i) + " " + Integer.toString(iCompare));
        }
        if (iCompare == 0) {
            iCompare = intRow1.compareTo(intRow2);
        }
        return (iCompare);
    }

    public MemoryTable copySortedTable() {
        MemoryTable mt = new MemoryTable(memoryTable.getColumnNames());
        for (int iRow = 0; iRow < memoryTable.getRowCount(); iRow++) {
            mt.addRow(memoryTable.getRow(this.convertRowIndexToSorted(iRow)));
        }
        return (mt);
    }
}
