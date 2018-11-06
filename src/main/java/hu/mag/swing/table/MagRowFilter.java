package hu.mag.swing.table;

import hu.mag.swing.MagComboBoxField;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.RowFilter;

public class MagRowFilter<M, I> extends RowFilter<Object, Object> {

    private Matcher matcher = null;
    private MagTableModel magTableModel = null;
    private Set<Integer> setRows = null;
    private boolean bSetRowsExclude = false;
    private boolean bNegative = false;

    public MagRowFilter(Pattern regex) {
        this(regex, null);
    }

    public MagRowFilter(Pattern regex, MagTableModel magTableModel) {
        this(regex, magTableModel, null);
    }

    public MagRowFilter(Pattern regex, MagTableModel magTableModel, Set<Integer> setRows) {
        this(regex, magTableModel, setRows, false);
    }

    public MagRowFilter(Pattern regex, MagTableModel magTableModel, Set<Integer> setRows, boolean bSetRowsExclude) {
        this(regex, magTableModel, setRows, bSetRowsExclude, false);
    }

    public MagRowFilter(Pattern regex, MagTableModel magTableModel, Set<Integer> setRows, boolean bSetRowsExclude, boolean bNegative) {
        if (regex == null) {
            throw new IllegalArgumentException("Pattern must be non-null");
        }
        matcher = regex.matcher("");
        this.magTableModel = magTableModel;
        this.setRows = setRows;
        this.bSetRowsExclude = bSetRowsExclude;
        this.bNegative = bNegative;
    }

//    @Override
//    public boolean include(Entry<? extends Object, ? extends Object> value) {
//        int count = value.getValueCount();
//        while (--count >= 0) {
//            if (include(value, count)) {
//                return (true);
//            }
//        }
//        return (false);
//    }
//    protected boolean include(Entry<? extends Object, ? extends Object> value, int index) {
//        if (value.getIdentifier() instanceof Integer) {
//            int iRow = ((Integer) value.getIdentifier()).intValue();
//            if (magTableModel != null) {
//                if (magTableModel.getRowStatus(iRow) == MagTableModel.ROW_STATUS_NEW || magTableModel.getRowStatus(iRow) == MagTableModel.ROW_STATUS_NEW_MODIFIED) {
//                    return (true); //new rows are always visible
//                }
//            }
//            if (setRows != null) {
//                if (bSetRowsExclude) {
//                    if (setRows.contains(new Integer(iRow))) {
//                        return (false);
//                    }
//                } else {
//                    if (!setRows.contains(new Integer(iRow))) {
//                        return (false);
//                    }
//                }
//            }
//        }
//        String sValue = value.getStringValue(index);
//        if (magTableModel != null) {
//            if (magTableModel.isLookupColumn(index)) {
//                MagComboBoxField magLookupField = magTableModel.getColumnLookupField(index);
//                sValue = magLookupField.getDisplay(value.getValue(index));
//            }
//        }
//        matcher.reset(sValue);
//        return (matcher.find());
//    }
    @Override
    public boolean include(Entry<? extends Object, ? extends Object> value) {
        if (value.getIdentifier() instanceof Integer) {
            int iRow = ((Integer) value.getIdentifier()).intValue();
            if (magTableModel != null) {
                if (magTableModel.getRowStatus(iRow) == MagTableModel.ROW_STATUS_NEW || magTableModel.getRowStatus(iRow) == MagTableModel.ROW_STATUS_NEW_MODIFIED) {
                    return (true); //new rows are always visible
                }
            }
            if (setRows != null) {
                if (bSetRowsExclude) {
                    if (setRows.contains(new Integer(iRow))) {
                        return (false);
                    }
                } else {
                    if (!setRows.contains(new Integer(iRow))) {
                        return (false);
                    }
                }
            }
        }
        int count = value.getValueCount();
        while (--count >= 0) {
            if (include(value, count)) {
//                if (bNegative) {
//                    return (false);
//                } else {
//                    return (true);
//                }
                return (bNegative ? false : true);
                //return (true);
            }
        }
//        if (bNegative) {
//            return (true);
//        } else {
//            return (false);
//        }
        return (bNegative ? true : false);
        //return (false);
    }

    protected boolean include(Entry<? extends Object, ? extends Object> value, int index) {
        String sValue = value.getStringValue(index);
        if (magTableModel != null) {
            if (magTableModel.isLookupColumn(index)) {
                MagComboBoxField magLookupField = magTableModel.getColumnLookupField(index);
                sValue = magLookupField.getDisplay(value.getValue(index));
            }
        }
//        matcher.reset(sValue);
//        System.out.println("Value=" + sValue + " " + (matcher.find() ? "found" : "not found"));
        matcher.reset(sValue);
//        if (bNegative) {
//            return (!matcher.find());
//        } else {
//            return (matcher.find());
//        }
        return (matcher.find());
    }

    public static <M, I> MagRowFilter<M, I> createMagRowFilter(String regex) {
        return (MagRowFilter<M, I>) new MagRowFilter(Pattern.compile(regex));
    }

    public static <M, I> MagRowFilter<M, I> createMagRowFilter(String regex, MagTableModel magTableModel) {
        return (MagRowFilter<M, I>) new MagRowFilter(Pattern.compile(regex), magTableModel);
    }

    public static <M, I> MagRowFilter<M, I> createMagRowFilter(String regex, MagTableModel magTableModel, Set<Integer> setRows) {
        return (MagRowFilter<M, I>) new MagRowFilter(Pattern.compile(regex), magTableModel, setRows);
    }

    public static <M, I> MagRowFilter<M, I> createMagRowFilter(String regex, MagTableModel magTableModel, Set<Integer> setRows, boolean bExclude) {
        return (MagRowFilter<M, I>) new MagRowFilter(Pattern.compile(regex), magTableModel, setRows, bExclude);
    }

    public static <M, I> MagRowFilter<M, I> createMagRowFilter(String regex, MagTableModel magTableModel, Set<Integer> setRows, boolean bExclude, boolean bNegative) {
        return (MagRowFilter<M, I>) new MagRowFilter(Pattern.compile(regex), magTableModel, setRows, bExclude, bNegative);
    }

}
