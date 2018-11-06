package hu.mgx.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 *
 * @author MaG
 */
public abstract class BigDecimalUtils {

    private BigDecimalUtils() {
    }

    public static BigDecimal convertToBigDecimalIsNull(Object o, BigDecimal bdIfNull) {
        return (isNull(convertToBigDecimal(o), bdIfNull));
    }

    public static BigDecimal convertToBigDecimal(Object o) {
        BigDecimal bd = null;
        if (o == null) {
            return (bd);
        }
        if (o instanceof java.lang.Integer) {
            return (new BigDecimal(((java.lang.Integer) o).intValue()));
        }
        if (o instanceof java.lang.Long) {
            return (new BigDecimal(((java.lang.Long) o).longValue()));
        }
        if (o instanceof java.lang.Float) {
            return (new BigDecimal(((java.lang.Float) o).floatValue()));
        }
        if (o instanceof java.math.BigDecimal) {
            return ((java.math.BigDecimal) o);
        }
        //return (StringUtils.bigDecimalValue(o.toString()));
        //System.out.println(o.toString().replace(" ", "").replace(StringUtils.sNbsp, "").replace(",", "."));
        return (StringUtils.bigDecimalValue(o.toString().replace(" ", "").replace(StringUtils.sNbsp, "").replace(",", ".")));
    }

    public static BigDecimal isNull(BigDecimal bdValue, BigDecimal bdIfNull) {
        if (bdValue == null) {
            return (bdIfNull);
        }
        return (bdValue);
    }

    public static BigDecimal round(BigDecimal bd, int iScale) {
        return (bd.setScale(iScale, RoundingMode.HALF_UP));
    }

}
