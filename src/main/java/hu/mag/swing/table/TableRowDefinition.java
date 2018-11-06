package hu.mag.swing.table;

import java.util.Vector;

public class TableRowDefinition {

    Vector<Class> vColumns = new Vector<Class>();

    public TableRowDefinition() {
        vColumns = new Vector();
    }

    public TableRowDefinition(Vector vColumns) {
        this.vColumns = vColumns;
    }

    public void addColumn(Class c) {
        vColumns.add(c);
    }

    public Class getColumn(int i) {
        if (i < 0) {
            return (Object.class);
        }
        if (i >= vColumns.size()) {
            return (Object.class);
        }
        return (vColumns.elementAt(i));
    }

    public int getColumnCount() {
        return (vColumns.size());
    }
}
