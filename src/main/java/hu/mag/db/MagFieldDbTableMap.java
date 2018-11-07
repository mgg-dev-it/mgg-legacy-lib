package hu.mag.db;

import hu.mag.swing.MagFieldInterface;
import java.util.Vector;

/**
 *
 * @author MaG
 */
public class MagFieldDbTableMap {

    private String sTableName;
    private Vector<MagFieldInterface> vMFIs;
    private Vector<String> vFieldNames;
    private Vector<String> vFieldDisplayNames;

    public MagFieldDbTableMap(String sTableName) {
        this.sTableName = sTableName;
        vMFIs = new Vector<>();
        vFieldNames = new Vector<>();
        vFieldDisplayNames = new Vector<>();
        init();
    }

    private void init() {
    }

    public void addField(MagFieldInterface mfi, String sFieldName, String sFieldDisplayName) {
        vMFIs.add(mfi);
        vFieldNames.add(sFieldName);
        vFieldDisplayNames.add(sFieldDisplayName);
    }

    public void addField(MagFieldInterface mfi, String sFieldName) {
        this.addField(mfi, sFieldName, sFieldName);
    }

    public int getFieldCount() {
        return (vMFIs.size());
    }

    public MagFieldInterface getMagFieldInterface(int i) {
        if (i < 0 || i >= vMFIs.size()) {
            return (null);
        }
        //System.out.println(vFieldNames.elementAt(i));
        return (vMFIs.elementAt(i));
    }

    public String getFieldName(int i) {
        if (i < 0 || i >= vFieldNames.size()) {
            return (null);
        }
        return (vFieldNames.elementAt(i));
    }

    public String getFieldDisplayName(int i) {
        if (i < 0 || i >= vFieldDisplayNames.size()) {
            return (null);
        }
        return (vFieldDisplayNames.elementAt(i));
    }

    public void clear() {
        vMFIs.clear();
        vFieldNames.clear();
        vFieldDisplayNames.clear();
    }
}
