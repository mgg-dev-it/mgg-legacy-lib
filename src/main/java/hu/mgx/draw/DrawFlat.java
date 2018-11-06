package hu.mgx.draw;

import java.awt.*;
import java.awt.geom.*;
import java.text.*;

public class DrawFlat
{

    private double dRatio = 1.0;
    //private DecimalFormat decimalFormat = new DecimalFormat("#,##0.00 m");
    //private DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
    private DecimalFormat decimalFormat = new DecimalFormat("#,##0.##");
    private boolean bDrawMetrics = true;
    private double dMetricsLowerLimit = 0.2;
    private Stroke basicStroke = new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    public DrawFlat(double dRatio)
    {
        this.dRatio = dRatio;
    }

    public void drawArrow(Graphics2D graphics2d, double dBeginX, double dBeginY, double dEndX, double dEndY)
    {
        //AffineTransform saveTransform = graphics2d.getTransform();
        Color saveColor = graphics2d.getColor();

        long x1 = Math.round(dBeginX * dRatio);
        long y1 = Math.round(dBeginY * dRatio);
        long x2 = Math.round(dEndX * dRatio);
        long y2 = Math.round(dEndY * dRatio);
        double dLineTheta = Math.atan2(y2 - y1, x2 - x1);
        System.out.println(dLineTheta);

        graphics2d.setColor(Color.BLUE);
        graphics2d.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
        //        graphics2d.setColor(Color.RED);
        graphics2d.drawLine((int) x1, (int) y1, (int) x1 + (int) (Math.round(Math.cos(dLineTheta - 0.2) * 15)), (int) y1 + (int) (Math.round(Math.sin(dLineTheta - 0.2) * 15)));
        //        graphics2d.setColor(Color.BLUE);
        graphics2d.drawLine((int) x1, (int) y1, (int) x1 + (int) (Math.round(Math.cos(dLineTheta + 0.2) * 15)), (int) y1 + (int) (Math.round(Math.sin(dLineTheta + 0.2) * 15)));
        //        graphics2d.setColor(Color.YELLOW);
        graphics2d.drawLine((int) x2, (int) y2, (int) x2 - (int) (Math.round(Math.cos(dLineTheta + 0.2) * 15)), (int) y2 - (int) (Math.round(Math.sin(dLineTheta + 0.2) * 15)));
        //        graphics2d.setColor(Color.PINK);
        graphics2d.drawLine((int) x2, (int) y2, (int) x2 - (int) (Math.round(Math.cos(dLineTheta - 0.2) * 15)), (int) y2 - (int) (Math.round(Math.sin(dLineTheta - 0.2) * 15)));

        //        affineTransform.translate(Math.round(Math.cos(dLineTheta + vectorAngle) * 20), 0.0);
        //        affineTransform.translate(0.0, Math.round(Math.sin(dLineTheta + vectorAngle) * 20));
        //graphics2d.setTransform(saveTransform);
        graphics2d.setColor(saveColor);
    }

    public void drawWall(Graphics2D graphics2d, double dBeginX, double dBeginY, double dEndX, double dEndY, double dOffsetX, double dOffsetY, boolean bMetricsPosition)
    {
        drawWall(graphics2d, dOffsetX + dBeginX, dOffsetY + dBeginY, dOffsetX + dEndX, dOffsetY + dEndY, basicStroke, bMetricsPosition);
    }

    public void drawWall(Graphics2D graphics2d, double dBeginX, double dBeginY, double dEndX, double dEndY, double dOffsetX, double dOffsetY, Stroke stroke)
    {
        drawWall(graphics2d, dOffsetX + dBeginX, dOffsetY + dBeginY, dOffsetX + dEndX, dOffsetY + dEndY, stroke, true);
    }

    public void drawWall(Graphics2D graphics2d, double dBeginX, double dBeginY, double dEndX, double dEndY, double dOffsetX, double dOffsetY, Stroke stroke, boolean bMetricsPosition)
    {
        drawWall(graphics2d, dOffsetX + dBeginX, dOffsetY + dBeginY, dOffsetX + dEndX, dOffsetY + dEndY, stroke, bMetricsPosition);
    }

    public void drawWall(Graphics2D graphics2d, double dBeginX, double dBeginY, double dEndX, double dEndY, double dOffsetX, double dOffsetY)
    {
        drawWall(graphics2d, dOffsetX + dBeginX, dOffsetY + dBeginY, dOffsetX + dEndX, dOffsetY + dEndY, basicStroke, true);
    }

    public void drawWall(Graphics2D graphics2d, double dBeginX, double dBeginY, double dEndX, double dEndY)
    {
        drawWall(graphics2d, dBeginX, dBeginY, dEndX, dEndY, basicStroke, true);
    }

    public void drawWall(Graphics2D graphics2d, double dBeginX, double dBeginY, double dEndX, double dEndY, Stroke stroke, boolean bMetricsPosition)
    {
        AffineTransform saveTransform = graphics2d.getTransform();
        Color saveColor = graphics2d.getColor();
        Stroke saveStroke = graphics2d.getStroke();
        graphics2d.setStroke(stroke);

        long x1 = Math.round(dBeginX * dRatio);
        long y1 = Math.round(dBeginY * dRatio);
        long x2 = Math.round(dEndX * dRatio);
        long y2 = Math.round(dEndY * dRatio);

        graphics2d.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
        graphics2d.setStroke(saveStroke);

        double dLength = Math.sqrt(Math.pow(dEndX - dBeginX, 2.0) + Math.pow(dEndY - dBeginY, 2.0));
        if (bDrawMetrics && (dLength > dMetricsLowerLimit))
        {
            double dLineTheta = Math.atan2(y2 - y1, x2 - x1);
            String sLength = "";
            Font f = graphics2d.getFont();
            FontMetrics fm = graphics2d.getFontMetrics(f);
            AffineTransform affineTransform = new AffineTransform();
            affineTransform.setToIdentity();

            sLength = decimalFormat.format(dLength * 100.0);
            Rectangle2D r = fm.getStringBounds(sLength, graphics2d);
            //graphics2d.setColor(Color.BLUE);

            //double vectorLength = Math.sqrt(Math.pow(r.getWidth()/2.0, 2.0) + Math.pow(r.getHeight()/2.0, 2.0));
            //            double vectorLength = Math.sqrt(Math.pow(r.getWidth()/2.0, 2.0) + Math.pow(2.0, 2.0));
            double vectorLength = Math.sqrt(Math.pow(r.getWidth() / 2.0, 2.0) + Math.pow((bMetricsPosition ? 2.0 : 2.0 - r.getHeight()), 2.0));
            //double vectorAngle = Math.atan2(-1 * r.getHeight()/2.0, r.getWidth()/2.0);
            //            double vectorAngle = Math.atan2(2.0, r.getWidth()/2.0);
            double vectorAngle = Math.atan2(0.0 + (bMetricsPosition ? 2.0 : 2.0 - r.getHeight()), r.getWidth() / 2.0);

            affineTransform.translate(Math.round(-1.0 * Math.cos(dLineTheta + vectorAngle) * vectorLength), 0.0);
            affineTransform.translate(0.0, Math.round(-1.0 * Math.sin(dLineTheta + vectorAngle) * vectorLength));

            affineTransform.translate(Math.round((x2 - x1) / 2), Math.round((y2 - y1) / 2));

            affineTransform.rotate(dLineTheta, x1, y1);
            graphics2d.transform(affineTransform);
            graphics2d.setColor(Color.RED);
            graphics2d.drawString(sLength, x1, y1);
            graphics2d.setTransform(saveTransform);
            graphics2d.setColor(saveColor);
        }
    }

    public boolean isDrawMetrics()
    {
        return bDrawMetrics;
    }

    public void setDrawMetrics(boolean bDrawMetrics)
    {
        this.bDrawMetrics = bDrawMetrics;
    }    //    public void drawWall_old(Graphics2D graphics2d, float fBeginX, float fBeginY, float fLengthX, float fLengthY){
    //        AffineTransform saveTransform = graphics2d.getTransform();
    //        Color saveColor = graphics2d.getColor();
    //
    //        int x1 = Math.round(fBeginX * fRatio);
    //        int y1 = Math.round(fBeginY * fRatio);
    //        int x2 = Math.round((fBeginX + fLengthX) * fRatio);
    //        int y2 = Math.round((fBeginY + fLengthY) * fRatio);
    //        float fLength = 0.0f;
    //        String sLength = "";
    //        Font f = graphics2d.getFont();
    //        FontMetrics fm = graphics2d.getFontMetrics(f);
    //        AffineTransform affineTransform = new AffineTransform();
    //        affineTransform.setToIdentity();
    //
    //        fLength = (float)Math.sqrt(Math.pow(fLengthX, 2.0) + Math.pow(fLengthY, 2.0));
    //        sLength = decimalFormat.format(fLength);
    //        Rectangle2D r = fm.getStringBounds(sLength, graphics2d);
    //        graphics2d.setColor(Color.BLUE);
    //        graphics2d.drawLine(x1, y1, x2, y2);
    //
    //        double vectorLength = Math.sqrt(Math.pow(r.getWidth()/2.0, 2.0) + Math.pow(r.getHeight()/2.0, 2.0));
    //        double vectorAngle = Math.atan2(r.getHeight()/2.0, r.getWidth()/2.0);
    //
    //        affineTransform.translate(Math.round(-1.0 * Math.cos(Math.atan2(y2 - y1, x2 - x1) + vectorAngle) * vectorLength), 0.0);
    //        affineTransform.translate(0.0, Math.round(-1.0 * Math.sin(Math.atan2(y2 - y1, x2 - x1) + vectorAngle) * vectorLength));
    //
    //        affineTransform.translate(Math.round((x2 - x1)/2), Math.round((y2 - y1)/2));
    //
    ////        System.out.println(Math.atan2(y2 - y1, x2 - x1));
    ////        System.out.println(Math.sin(Math.atan2(y2 - y1, x2 - x1)));
    ////        System.out.println(Math.cos(Math.atan2(y2 - y1, x2 - x1)));
    //        affineTransform.rotate(Math.atan2(y2 - y1, x2 - x1), x1, y1);
    //        graphics2d.transform(affineTransform);
    //        graphics2d.setColor(Color.RED);
    //        graphics2d.drawString(sLength, x1, y1);
    //        graphics2d.setTransform(saveTransform);
    //        graphics2d.setColor(saveColor);
    //    }
}
