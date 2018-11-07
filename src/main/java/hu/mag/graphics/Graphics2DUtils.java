package hu.mag.graphics;

import hu.mgx.util.StringUtils;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;

/**
 *
 * @author MaG
 */
public abstract class Graphics2DUtils {

    public static final int TOP = 1;
    public static final int BOTTOM = 2;
    public static final int CENTER = 3;
    public static final int LEFT = 4;
    public static final int RIGHT = 5;

    /**
     * drawLine wrapper for double parameters
     *
     * @param graphics graphics
     * @param x1 x1
     * @param y1 y1
     * @param x2 x2
     * @param y2 y2
     */
    public static void drawLine(Graphics graphics, double x1, double y1, double x2, double y2) {
        graphics.drawLine(new Double(x1).intValue(), new Double(y1).intValue(), new Double(x2).intValue(), new Double(y2).intValue());
    }

    /**
     * drawOval wrapper for double parameters
     *
     * @param graphics graphics
     * @param x x
     * @param y y
     * @param width width
     * @param height height
     */
    public static void drawOval(Graphics graphics, double x, double y, double width, double height) {
        graphics.drawOval(new Double(x).intValue(), new Double(y).intValue(), new Double(width).intValue(), new Double(height).intValue());
    }

    /**
     * fillOval wrapper for double parameters
     *
     * @param graphics graphics
     * @param x x
     * @param y y
     * @param width width
     * @param height height
     */
    public static void fillOval(Graphics graphics, double x, double y, double width, double height) {
        graphics.fillOval(new Double(x).intValue(), new Double(y).intValue(), new Double(width).intValue(), new Double(height).intValue());
    }

    /**
     * calculates the maximum Fontsize with which a string can draw into a given
     * wide area
     *
     * @param graphics graphics
     * @param font font 
     * @param sText sText
     * @param iTextWidth iTextWidth
     * @return int
     */
    public static int getMaxFontSize(Graphics graphics, Font font, String sText, int iTextWidth) {
        String[] s = sText.split(StringUtils.sCrLf);
        int iFontSize = 4;
        boolean bFit = true;
        int iLoop = 0;
        while (bFit && (++iLoop < 100)) {
            ++iFontSize;
            font = font.deriveFont(new Float(iFontSize).floatValue());
            FontMetrics fm = graphics.getFontMetrics(font);
            for (int i = 0; i < s.length; i++) {
                //System.out.println(Integer.toString(iFontSize) + " " + Integer.toString(fm.stringWidth(s[i])) + " " + Integer.toString(iTextWidth));
                if (fm.stringWidth(s[i]) > iTextWidth) {
                    bFit = false;
                }
            }
        }
        return (iFontSize - 1);
    }

    /**
     * calculates the area of a string
     *
     * @param graphics graphics
     * @param font font
     * @param sText sText
     * @return Rectangle
     */
    public static Rectangle geTextArea(Graphics graphics, Font font, String sText) {
        int iWidth = 0;
        int iHeight = 0;
        FontMetrics fm = graphics.getFontMetrics(font);
        String[] s = sText.split(StringUtils.sCrLf);
        for (int i = 0; i < s.length; i++) {
            Rectangle r = fm.getStringBounds(s[i], graphics).getBounds();
            if (r.width > iWidth) {
                iWidth = r.width;
            }
            iHeight += r.height;
        }
        return (new Rectangle(0, 0, iWidth, iHeight));

        //FontMetrics fm = graphics.getFontMetrics(font);
        //Rectangle2D r = fm.getStringBounds(sText, graphics);
        //return (r.getBounds());
//        Rectangle r1 = graphics.getFontMetrics(font).getStringBounds(sText, graphics).getBounds();
//        return (new Rectangle(0, 0, r1.width, r1.height));
    }

    /**
     *
     * @param graphics graphics
     * @param font font
     * @param color color
     * @param x x
     * @param y y
     * @param sText sText
     * @param iHorizonatlAlignment iHorizonatlAlignment
     * @param iVerticalAlignment iVerticalAlignment
     */
    public static void drawString(Graphics graphics, Font font, Color color, int x, int y, String sText, int iHorizonatlAlignment, int iVerticalAlignment) {
        Insets insets = new Insets(0, 0, 0, 0);
        drawString(graphics, font, color, x, y, sText, iHorizonatlAlignment, iVerticalAlignment, insets);
    }

    /**
     * draws a string to a point
     *
     * @param graphics graphics
     * @param x x
     * @param color color
     * @param y y
     * @param font font
     * @param sText sText
     * @param iHorizonatlAlignment iHorizonatlAlignment
     * @param iVerticalAlignment iVerticalAlignment
     * @param insets insets
     */
    public static void drawString(Graphics graphics, Font font, Color color, int x, int y, String sText, int iHorizonatlAlignment, int iVerticalAlignment, Insets insets) {
        Rectangle rectangleTextArea = geTextArea(graphics, font, sText);
        int iWidth = rectangleTextArea.width;
        int iHeight = rectangleTextArea.height;

        y -= graphics.getFontMetrics(font).getDescent();
        switch (iHorizonatlAlignment) {
            case LEFT:
                x += insets.left;
                break;
            case CENTER:
                x -= iWidth / 2;
                break;
            case RIGHT:
                x -= (iWidth + insets.right);
                break;
            default:
        }
        switch (iVerticalAlignment) {
            case TOP:
                //y += (iHeight + insets.top);
                y += insets.top;
                break;
            case CENTER:
                //y += iHeight / 2;
                y -= iHeight / 2;
                break;
            case BOTTOM:
                y -= (iHeight + insets.bottom);
                break;
            default:
        }
        Font fontSave = graphics.getFont();
        graphics.setFont(font);
        //graphics.drawString(sText, x, y);
        Color colorSave = graphics.getColor();
        graphics.setColor(color);

        FontMetrics fm = graphics.getFontMetrics(font);
        String[] s = sText.split(StringUtils.sCrLf);
        for (int i = 0; i < s.length; i++) {
            Rectangle r = fm.getStringBounds(s[i], graphics).getBounds();
            y += r.height;
            graphics.drawString(s[i], x, y);
        }

        //graphics.drawRect(x, y + rectangleTextBounds.y, rectangleTextBounds.width, rectangleTextBounds.height);
        //graphics.drawRect(x, y + graphics.getFontMetrics(font).getDescent() - rectangleTextArea.height, rectangleTextArea.width, rectangleTextArea.height);
        graphics.setFont(fontSave);
        graphics.setColor(colorSave);
    }

    /**
     * draws a string into a rectangle
     *
     * @param graphics graphics
     * @param font font
     * @param color color
     * @param rectangle rectangle
     * @param sText sText
     * @param iHorizonatlAlignment iHorizonatlAlignment
     * @param iVerticalAlignment iVerticalAlignment
     */
    public static void drawString(Graphics graphics, Font font, Color color, Rectangle rectangle, String sText, int iHorizonatlAlignment, int iVerticalAlignment) {
        Insets insets = new Insets(0, 0, 0, 0);
        drawString(graphics, font, color, rectangle, sText, iHorizonatlAlignment, iVerticalAlignment, insets);
    }

    /**
     * draws a string into a rectangle
     *
     * @param graphics graphics
     * @param rectangle rectangle
     * @param color color
     * @param font font
     * @param sText sText
     * @param iHorizonatlAlignment iHorizonatlAlignment
     * @param iVerticalAlignment iVerticalAlignment
     * @param insets insets
     */
    public static void drawString(Graphics graphics, Font font, Color color, Rectangle rectangle, String sText, int iHorizonatlAlignment, int iVerticalAlignment, Insets insets) {
        //Rectangle rectangleTextBounds = geTextArea(graphics, font, sText);
        int x = 0;
        int y = 0;
        switch (iHorizonatlAlignment) {
            case LEFT:
                x = rectangle.x;
                break;
            case CENTER:
                x = rectangle.x + rectangle.width / 2;
                break;
            case RIGHT:
                x = rectangle.x + rectangle.width;
                break;
            default:
        }
        switch (iVerticalAlignment) {
            case TOP:
                y = rectangle.y;
                break;
            case CENTER:
                y = rectangle.y + rectangle.height / 2;
                break;
            case BOTTOM:
                y = rectangle.y + rectangle.height;
                break;
            default:
        }
        //x += rectangleTextBounds.x;
        //y += rectangleTextBounds.y;
        drawString(graphics, font, color, x, y, sText, iHorizonatlAlignment, iVerticalAlignment, insets);
        //graphics.drawRect(x + rectangleTextBounds.x, y + rectangleTextBounds.y, rectangleTextBounds.width, rectangleTextBounds.height);
//        Font fontSave = graphics.getFont();
//        graphics.setFont(font);
//        graphics.drawString(sText, x, y);
//        graphics.setFont(fontSave);
    }

}
