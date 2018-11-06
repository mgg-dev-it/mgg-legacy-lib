package hu.mgx.util;

/**
 *
 * @author MaG
 */
public class DoubleUtils {

    public static Double convertToDouble(Object o) {
        Double dbl = null;
        if (o == null) {
            return (dbl);
        }
        if (o instanceof java.lang.Boolean) {
            return (new Double((java.lang.Boolean) o ? 1 : 0));
        }
        if (o instanceof java.math.BigDecimal) {
            return (((java.math.BigDecimal) o).doubleValue());
        }
        return (StringUtils.doubleValue(o.toString().replace(" ", "").replace(StringUtils.sNbsp, "")));
    }

}
