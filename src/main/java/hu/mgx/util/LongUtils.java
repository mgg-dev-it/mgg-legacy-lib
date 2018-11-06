package hu.mgx.util;

/**
 *
 * @author MaG
 */
public abstract class LongUtils {

    public static Long convertToLong(Object o) {
        Long lng = null;
        if (o == null) {
            return (lng);
        }
        if (o instanceof java.lang.Boolean) {
            return (new Long((java.lang.Boolean) o ? 1 : 0));
        }
        if (o instanceof java.math.BigDecimal) {
            return (((java.math.BigDecimal) o).longValue());
        }
        return (StringUtils.longValue(o.toString().replace(" ", "").replace(StringUtils.sNbsp, "")));
    }

}
