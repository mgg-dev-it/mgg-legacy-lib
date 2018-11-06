package hu.mag.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.Vector;

/**
 *
 * @author MaG
 */
public class Chart {

    //@todo http://www.highcharts.com
    //@todo column chart, line chart, series, categories, ticks, etc.
    private int iOrigoX = 0;
    private int iOrigoY = 0;

    private boolean bHorizontalAxis = false;
    private int iHorizontalAxisStart = 0;
    private int iHorizontalAxisEnd = 0;
    private int iHorizontalAxisTick = 0;
    private boolean bHorizontalAxisArrow = false;

    private boolean bVerticalAxis = false;
    private int iVerticalAxisStart = 0;
    private int iVerticalAxisEnd = 0;
    private int iVerticalAxisTick = 0;
    private boolean bVerticalAxisArrow = false;

    private final static int UNDEFINED = 0;
    public final static int COLUMN_CHART = 1;
    private int iType = UNDEFINED;

    private Vector<ChartValue> vChartValues;

    private Insets margin = new Insets(10, 10, 10, 10);

    private Color colorBackground = Color.white;
    private Color colorForeground = Color.black;
    private Color colorFill1 = Color.blue;
    private Color colorFill2 = Color.red;

    public Chart(int iType) {
        this.iType = iType;
        vChartValues = new Vector<>();
    }

    public void setType(int iType) {
        this.iType = iType;
    }

    public void setMargin(int top, int left, int bottom, int right) {
        margin = new Insets(top, left, bottom, right);
    }

    public void setBackgroundColor(Color color) {
        this.colorBackground = color;
    }

    public void setForegroundColor(Color color) {
        this.colorForeground = color;
    }

    public void setFillColor1(Color color) {
        this.colorFill1 = color;
    }

    public void setFillColor2(Color color) {
        this.colorFill2 = color;
    }

    public void deleteChartValues() {
        vChartValues.clear();
    }

    public void addChartValue(int iXValue, int iYValue) {
        vChartValues.add(new ChartValue(iXValue, iYValue));
    }

    public void addChartValue(String sCategory, int iXValue, int iYValue) {
        vChartValues.add(new ChartValue(sCategory, iXValue, iYValue));
    }

    public void addChartValue(String sCategory, int iXValue, int iYValue, String sXLabel) {
        vChartValues.add(new ChartValue(sCategory, iXValue, iYValue, sXLabel));
    }

    public void addChartValue(int iXValue, int iYValue, String sXLabel, String sYLabel) {
        vChartValues.add(new ChartValue(iXValue, iYValue, sXLabel, sYLabel));
    }

    public void setOrigo(int x, int y) {
        iOrigoX = x;
        iOrigoY = y;
    }

    public void showHorizontalAxis(int iStart, int iEnd, int iTick, boolean bArrow) {
        bHorizontalAxis = true;
        iHorizontalAxisStart = iStart;
        iHorizontalAxisEnd = iEnd;
        iHorizontalAxisTick = iTick;
        bHorizontalAxisArrow = bArrow;
    }

    public void hideHorizontalAxis() {
        bHorizontalAxis = false;
    }

    public void showVerticalAxis(int iStart, int iEnd, int iTick, boolean bArrow) {
        bVerticalAxis = true;
        iVerticalAxisStart = iStart;
        iVerticalAxisEnd = iEnd;
        iVerticalAxisTick = iTick;
        bVerticalAxisArrow = bArrow;
    }

    public void hideVerticalAxis() {
        bVerticalAxis = false;
    }

    /**
     * calculates a normalized virtual X value between 0 and 1 (between
     * horizontal axis start and end)
     *
     * @param iRealX
     * @return
     */
    private double calculateVirtualX(int iRealX) {
        return (new Double(iRealX - iHorizontalAxisStart).doubleValue() / new Double(iHorizontalAxisEnd - iHorizontalAxisStart).doubleValue());
    }

    /**
     * calculates the drawing X value from the virtual X value
     *
     * @param dblVirtualX
     * @param d
     * @return
     */
    private int calculateDrawX(double dblVirtualX, Dimension d) {
        return (margin.left + new Double(dblVirtualX * (d.width - margin.left - margin.right)).intValue());
    }

    /**
     * calculates the drawing X value from the real X value
     *
     * @param iRealX
     * @param d
     * @return
     */
    private int calculateX(int iRealX, Dimension d) {
        return (calculateDrawX(calculateVirtualX(iRealX), d));
    }

    /**
     * calculates a normalized virtual Y value between 0 and 1 (between vertical
     * axis start and end)
     *
     * @param iRealY
     * @return
     */
    private double calculateVirtualY(int iRealY) {
        return (new Double(iRealY - iVerticalAxisStart).doubleValue() / new Double(iVerticalAxisEnd - iVerticalAxisStart).doubleValue());
    }

    /**
     * calculates the drawing Y value from the virtual Y value
     *
     * @param dblVirtualY
     * @param d
     * @return
     */
    private int calculateDrawY(double dblVirtualY, Dimension d) {
        return (d.height - margin.bottom - new Double(dblVirtualY * (d.height - margin.top - margin.bottom)).intValue());
    }

    /**
     * calculates the drawing Y value from the real Y value
     *
     * @param iRealY
     * @param d
     * @return
     */
    private int calculateY(int iRealY, Dimension d) {
        return (calculateDrawY(calculateVirtualY(iRealY), d));
    }

    public void draw(Graphics graphics, Dimension d) {
        int iSurfaceWidth = d.width;
        int iSurfaceHeight = d.height;
//        int iDrawWidth = iSurfaceWidth - margin.left - margin.right;
//        int iDrawHeight = iSurfaceHeight - margin.top - margin.bottom;
        graphics.setColor(colorBackground);
        graphics.drawRect(0, 0, iSurfaceWidth, iSurfaceHeight);
        graphics.fillRect(0, 0, iSurfaceWidth, iSurfaceHeight);

        graphics.setColor(colorForeground);
//        graphics.drawOval(iOrigoX - 5, iSurfaceHeight - iOrigoY - 5, 10, 10);

//        double dMultiplyX = iDrawWidth / (iHorizontalAxisEnd - iHorizontalAxisStart);
//        double dMultiplyY = new Double(iDrawHeight).doubleValue() / new Double(iVerticalAxisEnd - iVerticalAxisStart).doubleValue();
        if (!bHorizontalAxis) {
            for (int i = 0; i < vChartValues.size(); i++) {
                iHorizontalAxisStart = (i == 0 ? vChartValues.elementAt(i).getXValue() : Math.min(iHorizontalAxisStart, vChartValues.elementAt(i).getXValue()));
                iHorizontalAxisEnd = (i == 0 ? vChartValues.elementAt(i).getXValue() : Math.max(iHorizontalAxisStart, vChartValues.elementAt(i).getXValue()));
            }
            //--iHorizontalAxisStart;
            //++iHorizontalAxisEnd;
            iHorizontalAxisStart -= (iHorizontalAxisEnd - iHorizontalAxisStart) * 0.2d;
            iHorizontalAxisEnd += (iHorizontalAxisEnd - iHorizontalAxisStart) * 0.2d;
        }
        if (!bVerticalAxis) {
            for (int i = 0; i < vChartValues.size(); i++) {
                iVerticalAxisStart = (i == 0 ? vChartValues.elementAt(i).getYValue() : Math.min(iVerticalAxisStart, vChartValues.elementAt(i).getYValue()));
                iVerticalAxisEnd = (i == 0 ? vChartValues.elementAt(i).getYValue() : Math.max(iVerticalAxisEnd, vChartValues.elementAt(i).getYValue()));
//                System.out.println(vChartValues.elementAt(i).getYValue());
//                System.out.println(iVerticalAxisStart);
//                System.out.println(iVerticalAxisEnd);
//                System.out.println("-");
            }
            //--iVerticalAxisStart;
            //++iVerticalAxisEnd;
            iVerticalAxisStart -= (iVerticalAxisEnd - iVerticalAxisStart) * 0.2d;
            iVerticalAxisEnd += (iVerticalAxisEnd - iVerticalAxisStart) * 0.2d;
//            System.out.println(iVerticalAxisStart);
//            System.out.println(iVerticalAxisEnd);
//            System.out.println("----");
        }
        if (bHorizontalAxis) {
            if (iOrigoY >= iVerticalAxisStart && iOrigoY <= iVerticalAxisEnd) {
                double dblVirtualY = calculateVirtualY(iOrigoY);
                int iDrawY = calculateDrawY(dblVirtualY, d);
                graphics.drawLine(margin.left, iDrawY, iSurfaceWidth - margin.right, iDrawY);
            }
//            graphics.drawLine(iOrigoX + iHorizontalAxisStart, iSurfaceHeight - iOrigoY, iOrigoX + iHorizontalAxisEnd, iSurfaceHeight - iOrigoY);
//            if (bHorizontalAxisArrow) {
//                graphics.drawLine(iOrigoX + iHorizontalAxisEnd, iSurfaceHeight - iOrigoY, iOrigoX + iHorizontalAxisEnd - 5, iSurfaceHeight - iOrigoY - 5);
//                graphics.drawLine(iOrigoX + iHorizontalAxisEnd, iSurfaceHeight - iOrigoY, iOrigoX + iHorizontalAxisEnd - 5, iSurfaceHeight - iOrigoY + 5);
//            }
        }
        if (bVerticalAxis) {
            if (iOrigoX >= iHorizontalAxisStart && iOrigoX <= iHorizontalAxisEnd) {
                double dblVirtualX = calculateVirtualX(iOrigoX);
                int iDrawX = calculateDrawX(dblVirtualX, d);
                graphics.drawLine(iDrawX, margin.top, iDrawX, iSurfaceHeight - margin.bottom);
            }
//            graphics.drawLine(iOrigoX, iSurfaceHeight - (iOrigoY + iVerticalAxisStart), iOrigoX, iSurfaceHeight - (iOrigoY + iVerticalAxisEnd));
//            if (bVerticalAxisArrow) {
//                graphics.drawLine(iOrigoX, iSurfaceHeight - (iOrigoY + iVerticalAxisEnd), iOrigoX - 5, iSurfaceHeight - (iOrigoY + iVerticalAxisEnd - 5));
//                graphics.drawLine(iOrigoX, iSurfaceHeight - (iOrigoY + iVerticalAxisEnd), iOrigoX + 5, iSurfaceHeight - (iOrigoY + iVerticalAxisEnd - 5));
//            }
        }

        if (iType == COLUMN_CHART) {
            Font font = new Font("Arial", Font.PLAIN, 24);
            int iColumnWidth = (calculateX(1, d) - calculateX(0, d)) / 2;
            int iFontSize = 24;
            for (int i = 0; i < vChartValues.size(); i++) {
                String sLabelX = vChartValues.elementAt(i).getXLabel();
                if (sLabelX == null) {
                    sLabelX = Integer.toString(vChartValues.elementAt(i).getXValue());
                }
                int iFS = Graphics2DUtils.getMaxFontSize(graphics, font, sLabelX, iColumnWidth);
                iFontSize = Math.min(iFontSize, iFS);
            }
            iFontSize=Math.max(iFontSize, 10);
            font = new Font("Arial", Font.PLAIN, iFontSize);
//            int iDrawX = 0;
//            int iDrawY = 0;
//            int iDrawYBase = 0;
            int iPrevX = 0;
            int iDrawYOffset = 0;
            int iSumY = 0;
            boolean bFirstColor = false;
            for (int i = 0; i < vChartValues.size(); i++) {
                bFirstColor = !bFirstColor;
                int iX = vChartValues.elementAt(i).getXValue();
                int iY = vChartValues.elementAt(i).getYValue();
                if (i == 0 || iPrevX != iX) {
                    iDrawYOffset = 0;
                    iSumY = 0;
                    bFirstColor = true;
                }
                graphics.setColor(bFirstColor ? colorFill1 : colorFill2);

                int iDrawX = calculateX(vChartValues.elementAt(i).getXValue(), d);
                int iDrawY = calculateY(vChartValues.elementAt(i).getYValue(), d);
                int iDrawYBase = calculateY(iOrigoY, d);
                iSumY += iY;
                graphics.drawRect(iDrawX - iColumnWidth / 2, iDrawY - iDrawYOffset, iColumnWidth, iDrawYBase - iDrawY);
                graphics.fillRect(iDrawX - iColumnWidth / 2, iDrawY - iDrawYOffset, iColumnWidth, iDrawYBase - iDrawY);
                String sLabelX = vChartValues.elementAt(i).getXLabel();
                if (sLabelX == null) {
                    sLabelX = Integer.toString(vChartValues.elementAt(i).getXValue());
                }
                if (iDrawYOffset == 0) {
                    Rectangle rX = new Rectangle(iDrawX - iColumnWidth / 2, iDrawYBase - iDrawYOffset, iColumnWidth, iDrawYBase - iDrawY);
                    Graphics2DUtils.drawString(graphics, font, colorForeground, rX, sLabelX, Graphics2DUtils.CENTER, Graphics2DUtils.TOP);
                }
                String sLabelY = vChartValues.elementAt(i).getYLabel();
                if (sLabelY == null) {
                    //sLabelY = Integer.toString(vChartValues.elementAt(i).getYValue());
                    sLabelY = Integer.toString(iSumY);
                }
                if (i == (vChartValues.size() - 1) || iX != vChartValues.elementAt(i + 1).getXValue()) {
                    Rectangle rY = new Rectangle(iDrawX - iColumnWidth / 2, iDrawY - (iDrawYBase - iDrawY) - iDrawYOffset, iColumnWidth, iDrawYBase - iDrawY);
                    Graphics2DUtils.drawString(graphics, font, colorForeground, rY, sLabelY, Graphics2DUtils.CENTER, Graphics2DUtils.BOTTOM);
                }
                //Rectangle rY = new Rectangle(iDrawX - iColumnWidth / 2, iDrawY - (iDrawYBase - iDrawY) - iDrawYOffset, iColumnWidth, iDrawYBase - iDrawY);
                //Graphics2DUtils.drawString(graphics, font, colorForeground, rY, sLabelY, Graphics2DUtils.CENTER, Graphics2DUtils.BOTTOM);

                iDrawYOffset += (iDrawYBase - iDrawY);
                iPrevX = iX;
            }
        }
//        for (int i = 0; i < vChartValues.size(); i++) {
//            int iDrawX = calculateX(vChartValues.elementAt(i).getXValue(), d);
//            int iDrawY = calculateY(vChartValues.elementAt(i).getYValue(), d);
//            graphics.drawOval(iDrawX - 3, iDrawY - 3, 6, 6);
//        }
    }
}
