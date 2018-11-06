package hu.mag.swing.table;

import hu.mag.swing.MagComboBoxField;
import java.util.Comparator;

public class MagLookupComparator implements Comparator {

    private MagComboBoxField magLookupField = null;

    public MagLookupComparator(MagComboBoxField magLookupField) {
        this.magLookupField = magLookupField;
    }

    @Override
    public int compare(Object o1, Object o2) {
        String s1 =magLookupField.getDisplay(o1).toString();
        String s2 =magLookupField.getDisplay(o2).toString();
        return(s1.compareTo(s2));
    }

}
