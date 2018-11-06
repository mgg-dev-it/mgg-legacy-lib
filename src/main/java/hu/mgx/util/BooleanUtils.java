package hu.mgx.util;

/**
 *
 * @author MaG
 */
public abstract class BooleanUtils {

    public static Boolean convertToBoolean(Object o) {
        Boolean b = null;
        if (o == null) {
            return (b);
        }
        if (o instanceof Boolean) {
            return ((Boolean) o);
        }
        if (o instanceof java.lang.Integer) {
            Integer i = (Integer) o;
            if (i.intValue() == 0) {
                return (Boolean.FALSE);
            }
            return (Boolean.TRUE);
        }
        if (o instanceof java.lang.String) {
            String s = ((String) o).trim().toLowerCase();
            switch (s) {
                case "false":
                case "no":
                case "0":
                case "":
                    return (Boolean.FALSE);
                case "true":
                case "yes":
                case "1":
                    return (Boolean.TRUE);
            }
        }
        return (b);
    }

    public static Boolean isNull(Boolean bValue, Boolean bIfNull) {
        if (bValue == null) {
            return (bIfNull);
        }
        return (bValue);
    }
}
