package hu.mgx.util;

/**
 *
 * @author MaG
 */
public abstract class ShortUtils {

    public static Short convertToShort(Object o) {
        Short shrt = null;
        short s0 = 0;
        short s1 = 1;
        if (o == null) {
            return (shrt);
        }
        if (o instanceof java.lang.Boolean) {
            return (new Short((java.lang.Boolean) o ? s1 : s0));
        }
        if (o instanceof java.math.BigDecimal) {
            return (((java.math.BigDecimal) o).shortValue());
        }
        return (StringUtils.shortValue(o.toString().replace(" ", "").replace(StringUtils.sNbsp, "")));
    }

}
