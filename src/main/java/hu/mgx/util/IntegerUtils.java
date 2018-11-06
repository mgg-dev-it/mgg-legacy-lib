package hu.mgx.util;

/**
 *
 * @author MaG
 */
public abstract class IntegerUtils {

    private IntegerUtils() {
    }

    public static Integer isNull(Integer intValue, Integer intIfNull) {
        if (intValue == null) {
            return (intIfNull);
        }
        return (intValue);
    }

    public static Integer isNull(Object oValue, Integer intIfNull) {
        if (oValue == null) {
            return (intIfNull);
        }
        if (oValue.getClass() == Integer.class) {
            return ((Integer) oValue);
        }
        return (intIfNull);
    }

    public static Integer convertToInt(Object o) {
        Integer integer = convertToInteger(o);
        return (integer == null ? 0 : integer.intValue());
    }

    public static Integer convertToInteger(Object o) {
        Integer integer = null;
        if (o == null) {
            return (integer);
        }
        //MaG 2017.12.04.
        if (o instanceof java.lang.Boolean) {
            return ((java.lang.Boolean) o ? 1 : 0);
        }
        if (o instanceof java.math.BigDecimal) {
            return (((java.math.BigDecimal) o).intValue());
        }
        //return (StringUtils.intValue(o.toString()));
        return (StringUtils.intValue(o.toString().replace(" ", "").replace(StringUtils.sNbsp, "")));
    }

    public static int intValue(Integer integer) {
        if (integer == null) {
            return (0);
        }
        return (integer.intValue());
    }

    public static Integer createNullInteger() {
        Integer integer = null;
        return (integer);
    }

    public static boolean contains(int[] ia, int i) {
        for (int j = 0; j < ia.length; j++) {
            if (ia[j] == i) {
                return (true);
            }
        }
        return (false);
    }
}
