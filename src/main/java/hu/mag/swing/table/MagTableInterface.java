package hu.mag.swing.table;

public interface MagTableInterface {

    public abstract boolean beforeRowChange(int iPreviousRow, int iNextRow);

    public abstract void rowChanged(int iPreviousRow, int iNextRow);

    public abstract boolean tabPressedInLastCell(int iPreviousRow, int iNextRow);

    public abstract void requestForInfo();

    public abstract void status(String sStatus);
}
