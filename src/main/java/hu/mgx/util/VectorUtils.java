package hu.mgx.util;

import hu.mgx.swing.table.MemoryTable;
import java.util.Vector;

/**
 *
 * @author MaG
 */
public abstract class VectorUtils {

    public static Vector<Vector> convertVectorToData(Vector v) {
        Vector<Vector> vData = new Vector<Vector>();
        Vector vRow;
        for (int i = 0; i < v.size(); i++) {
            vRow = new Vector();
            vRow.add(v.elementAt(i));
            vData.add(vRow);
        }
        return (vData);
    }

    public static Vector convertStringToVector(String s, MemoryTable mtRecordMap) {
        Vector<String> v = new Vector<>();
        int iPosition = 0;
        int iLength = 0;
        for (int i = 0; i < mtRecordMap.getRowCount(); i++) {
            iPosition = mtRecordMap.getIntValueAt(i, "from");
            iLength = mtRecordMap.getIntValueAt(i, "length");
            v.add(StringUtils.mid(s, iPosition, iLength));
        }
        return (v);
    }

    public static Vector convertStringsArrayToVector(String... s) {
        Vector v = new Vector();
        for (int i = 0; i < s.length; i++) {
            v.add(s[i]);
        }
        return (v);
    }

    public static Vector convertObjectArrayToVector(Object... o) {
        Vector v = new Vector();
        for (int i = 0; i < o.length; i++) {
            v.add(o[i]);
        }
        return (v);
    }
}
