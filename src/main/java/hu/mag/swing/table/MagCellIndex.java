package hu.mag.swing.table;

/**
 *
 * @author MaG
 */
public class MagCellIndex extends Object {

    private int row;
    private int column;

    public MagCellIndex(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return (row);
    }

    public int getColumn() {
        return (column);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MagCellIndex) {
            return (this.row == ((MagCellIndex) obj).row && this.column == ((MagCellIndex) obj).column);
        } else {
            return (false);
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + this.row;
        hash = 59 * hash + this.column;
        return hash;
    }

    @Override
    public String toString() {
        return (getClass().getName() + "[row=" + this.row + ", column=" + this.column + "]");
    }
}
