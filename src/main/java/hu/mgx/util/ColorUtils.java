package hu.mgx.util;

import java.awt.Color;

/**
 *
 * @author MaG
 */
public abstract class ColorUtils {

    private ColorUtils() {
    }

    public static long colorToLong(Color c) {
        return (c.getRed() * 65536L + c.getGreen() * 256L + c.getBlue());
    }

    public static Color longToColor(long l) {
        int iRed = 0;
        int iGreen = 0;
        int iBlue = 0;
        long lRed = 0L;
        long lGreen = 0L;
        if (l > -1 && l < 256L * 256L * 256L) {
            lRed = l / 65536;
            if (lRed > -1 && lRed < 256) {
                iRed = (int) lRed;
            }
            l -= lRed * 65536;
            lGreen = l / 256;
            if (lGreen > -1 && lGreen < 256) {
                iGreen = (int) lGreen;
            }
            l -= lGreen * 256;
            iBlue = (int) l;
        }
        return (new Color(iRed, iGreen, iBlue));
    }

    public static Color vbLongToColor(long l) {
        int iRed = 0;
        int iGreen = 0;
        int iBlue = 0;
        long lRed = 0L;
        long lGreen = 0L;
        if (l > -1 && l < 256L * 256L * 256L) {
            lRed = l / 65536;
            if (lRed > -1 && lRed < 256) {
                iRed = (int) lRed;
            }
            l -= lRed * 65536;
            lGreen = l / 256;
            if (lGreen > -1 && lGreen < 256) {
                iGreen = (int) lGreen;
            }
            l -= lGreen * 256;
            iBlue = (int) l;
        }
        return (new Color(iBlue, iGreen, iRed));
    }

    public static String colorToHTML(Color c) {
        String red = Integer.toHexString(c.getRed());
        String green = Integer.toHexString(c.getGreen());
        String blue = Integer.toHexString(c.getBlue());
        return "#" + (red.length() == 1 ? "0" + red : red) + (green.length() == 1 ? "0" + green : green) + (blue.length() == 1 ? "0" + blue : blue);
    }

}
