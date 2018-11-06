package hu.mag.db;

import hu.mag.swing.table.MagTableModel;

/**
 * Additional, mainly data dependent information about tables. Usage: create
 * descendant and overwrite the proper method. Not overwritten methods don't
 * influence the system. (They return with 'null')
 *
 * @author MaG
 */
public class TableInfoPlus {

    protected MagTableModel magTableModel;

    /**
     * <code>TableInfoPlus</code> constructor.
     *
     * @param magTableModel is the <code>MagTableModel</code> of the table.
     */
    public TableInfoPlus(MagTableModel magTableModel) {
        this.magTableModel = magTableModel;
    }

    /**
     * Returns whether a column is editable or not. The overwritten method can
     * query the row content and decide, whether a column is editable or not.
     *
     * @param iModelRow is the model row number of the cell.
     * @param iModelColumn is the model column number of the cell.
     * @return whether a column is editable or not. (Null if it is not
     * controlled in this object.)
     */
    public Boolean isCellEditable(int iModelRow, int iModelColumn) {
        Boolean b = null;
        return (b);
    }

    /**
     * Called at cell's edit end.
     *
     * @param iModelRow is the model row number of the cell.
     * @param iModelColumn is the model column number of the cell.
     */
    public void editEnd(int iModelRow, int iModelColumn) {
    }

    /**
     * Set row statuses (readonly columns, etc.) Should be called during data
     * loading for all the rows.
     *
     * @param iModelRow is the model row number of the cell.
     */
    public void setRowStatuses(int iModelRow) {
    }

    /**
     * Returns whether a row is deletable or not. The overwritten method can
     * query the row content and decide, whether a row is deletable or not.
     *
     * @param iModelRow is the model row number of the cell.
     * @return whether a row is deletable or not. (Null if it is not controlled
     * in this object.)
     */
    public Boolean isRowDeletable(int iModelRow) {
        Boolean b = null;
        return (b);
    }

    /**
     * Returns whether a row is valid or not. The overwritten method can query
     * the row content and decide, whether a row is valid or not.
     *
     * @param iModelRow is the model row number of the cell.
     * @return whether a row is valid or not. (Null if it is not controlled in
     * this object.)
     */
    public Boolean isRowValid(int iModelRow) {
        Boolean b = null;
        return (b);
    }

    /**
     * Returns a calculated columnvalue.
     *
     * @param iModelRow is the model row number of the cell.
     * @param iModelColumn is the model column number of the cell.
     * @return the calculated column value.
     */
    public Object calculatedColumnValue(int iModelRow, int iModelColumn) {
        Object o = null;
        return (o);
    }
}
