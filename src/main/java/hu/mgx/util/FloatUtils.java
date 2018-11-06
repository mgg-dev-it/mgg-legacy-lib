package hu.mgx.util;

/**
 *
 * @author MaG
 */
public class FloatUtils {

    public static Float convertToFloat(Object o) {
        Float dbl = null;
        if (o == null) {
            return (dbl);
        }
        if (o instanceof java.lang.Boolean) {
            return (new Float((java.lang.Boolean) o ? 1 : 0));
        }
        if (o instanceof java.math.BigDecimal) {
            return (((java.math.BigDecimal) o).floatValue());
        }
        return (StringUtils.floatValue(o.toString().replace(" ", "").replace(StringUtils.sNbsp, "")));
    }

}
