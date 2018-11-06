package hu.mag.swing.dashboard;

import hu.mag.graphics.Graphics2DUtils;
import hu.mgx.swing.CommonPanel;
import hu.mgx.util.StringUtils;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.Rectangle;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

/**
 *
 * @author MaG
 */
public class DashboardView extends CommonPanel {

    private Color colorBackground;
    private Color color0;
    private Color color1;
    private Color colorTitle;
    private String sTitle;
    private int iMinValue;
    private int iMaxValue;
    private Font font;
    private Vector<String> vCategoryName;
    private Vector<DashboardViewBar> vCategoryDVB;
    private JLabel jLabelLayoutRemainder;

    public DashboardView() {
        super();
        init();
    }

    private void init() {
        colorBackground = new Color(192, 192, 192);
        color0 = new Color(255, 255, 255);
        //color1 = new Color(64, 255, 255);
        color1 = new Color(192, 224, 255); //#C0E0FF
        iMinValue = 0;
        iMaxValue = 100;
        font = new Font("Arial", Font.PLAIN, 12);
        setBorder(BorderFactory.createEmptyBorder());
        setInsets(3, 5, 3, 5);
        setBackground(colorBackground);
        vCategoryName = new Vector<>();
        vCategoryDVB = new Vector<>();
        jLabelLayoutRemainder = new JLabel("");
    }

    public void setBackground(Color c) {
        colorBackground = c;
    }

    public void addCategory(String sName, String sTitle) {
        for (int i = 0; i < vCategoryDVB.size(); i++) {
            if (vCategoryName.elementAt(i).equalsIgnoreCase(sName)) {
                return;
            }
        }
        vCategoryName.add(sName);
        vCategoryDVB.add(new DashboardViewBar());
        vCategoryDVB.lastElement().setTitle(sTitle);
        vCategoryDVB.lastElement().setValue(0);
        if (vCategoryDVB.size() > 1) {
            this.getLayout().removeLayoutComponent(jLabelLayoutRemainder);
        }
        setCurrentColumn(0);
        addToCurrentRow(vCategoryDVB.lastElement(), 1, 1, 0.01, 1.0, GridBagConstraints.BOTH, GridBagConstraints.CENTER);
        addToNextRow(jLabelLayoutRemainder = new JLabel(""), 1, 1, 1.0, 1.0, GridBagConstraints.BOTH, GridBagConstraints.CENTER);
    }

    public void setValue(String sName, int iValue) {
        for (int i = 0; i < vCategoryDVB.size(); i++) {
            if (vCategoryName.elementAt(i).equalsIgnoreCase(sName)) {
                vCategoryDVB.elementAt(i).setValue(iValue);
            }
        }
    }

    public void setMaxValue(int iMaxValue) {
        this.iMaxValue = iMaxValue;
    }

    private class DashboardViewBar extends CommonPanel {

//        private Color colorBackground;
//        private Color colorBar;
//        private Color colorTitle;
        private String sTitle;
//        private int iMinValue;
//        private int iMaxValue;
        private int iValue;
//        private Font font;

        public DashboardViewBar() {
            super();
            setBorder(BorderFactory.createEmptyBorder());
            setInsets(0, 0, 0, 0);
            iMinValue = 0;
            iMaxValue = 100;
            iValue = iMinValue;
//            this.setFixSize(new Dimension(this.getWidth(), 10));
//            font = new Font("Arial", Font.PLAIN, 12);
//            this.setMinimumSize(new Dimension(this.getWidth(), Graphics2DUtils.geTextArea(this.getGraphics(), font, sTitle).height + 2));
////            //System.out.println(Graphics2DUtils.geTextArea(g, font, sTitle).height);
//            this.setPreferredSize(new Dimension(this.getWidth(), Graphics2DUtils.geTextArea(this.getGraphics(), font, sTitle).height + 2));
//            this.setMaximumSize(new Dimension(this.getWidth(), Graphics2DUtils.geTextArea(this.getGraphics(), font, sTitle).height + 2));
            //this.setBorder(BorderFactory.createLineBorder(Color.blue, 1));
        }

        public void setTitle(String sTitle) {
            this.sTitle = sTitle;
        }

        public void setValue(int iValue) {
            this.iValue = Math.min(iMaxValue, Math.max(iMinValue, iValue));
        }

        public void setHeightToFont() {
            this.setFixSize(new Dimension(this.getWidth(), Graphics2DUtils.geTextArea(this.getGraphics(), font, sTitle).height + 2));
            //System.out.println(Graphics2DUtils.geTextArea(this.getGraphics(), font, sTitle).height);
            revalidate();
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            setHeightToFont();

            int w = this.getWidth();
            int h = this.getHeight();
            g.setColor(color0);
            g.fillRect(0, 0, w - 1, h - 1);
            g.setColor(new Color(0, 0, 136)); //#000088
            g.drawRect(0, 0, w - 1, h - 1);
            g.setColor(color1);
            g.fillRect(1, 1, (w - 2) * (iValue - iMinValue) / (iMaxValue - iMinValue), h - 2);
            Graphics2DUtils.drawString(g, font, Color.blue, new Rectangle(5, 0, w - 5, h), StringUtils.stringReplace(sTitle, "<value>", Integer.toString(iValue)), Graphics2DUtils.LEFT, Graphics2DUtils.CENTER);
        }

    }
}
