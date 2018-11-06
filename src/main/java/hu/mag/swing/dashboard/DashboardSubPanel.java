package hu.mag.swing.dashboard;

import hu.mgx.app.swing.SwingAppInterface;
import hu.mgx.swing.CommonPanel;
import hu.mgx.util.DateTimeUtils;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 *
 * @author MaG
 */
public class DashboardSubPanel extends CommonPanel {

    private final SwingAppInterface swingAppInterface;
    private final DashboardPanel dashboardPanel;
    private int iGridWidth;
    private int iGridHeight;
    private Color colorBackground;
    private Color colorBorder;
    private JLabel jLabelTitle;
    private CommonPanel cp;
    private JLabel jLabelClock;
    private boolean bClockVisible;
    private String sClock;
    private int iType;
    public final static int DSP_EMPTY = 0;
    public final static int DSP_HORIZONTAL_BARS = 1;
    public final static int DSP_PANEL = 2;
    private DashboardView dv;
    private int iRefreshPeriod;
    private int iRefreshCounter;
    private boolean bSynchronizeRefresh;
    private boolean bInRefresh;

    public DashboardSubPanel(SwingAppInterface swingAppInterface, DashboardPanel dashboardPanel, String sTitle) {
        super();
        this.swingAppInterface = swingAppInterface;
        this.dashboardPanel = dashboardPanel;
        this.iType = DSP_EMPTY;
        iGridWidth = 1;
        iGridHeight = 1;
        iRefreshPeriod = 60;
        iRefreshCounter = 1;
        bSynchronizeRefresh = true;
        bInRefresh = false;
        //colorBorder = new Color(64, 255, 255);
        colorBorder = new Color(36, 113, 163);
        setInsets(0, 0, 0, 0);
        this.setBorder(BorderFactory.createLineBorder(colorBorder, 5));
        //this.colorBackground = new Color(224, 241, 255); //E0F1FF
        this.colorBackground = new Color(36, 113, 163);
        this.setBackground(colorBackground);
        jLabelTitle = new JLabel(sTitle, SwingConstants.CENTER);
        jLabelTitle.setFont(new Font("Arial", Font.BOLD, 12));
        jLabelTitle.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
        jLabelTitle.setForeground(new Color(218, 247, 166)); //#DAF7A6
        addToCurrentRow(jLabelTitle, 1, 1, 0.0, 0.0, GridBagConstraints.BOTH, GridBagConstraints.CENTER);
        cp = CommonPanel.createBorderLessCommonPanel();
        cp.setInsets(0, 0, 0, 0);
        cp.setBackground(this.getBackground());
        addToNextRow(cp, 1, 1, 1.0, 1.0, GridBagConstraints.BOTH, GridBagConstraints.CENTER);

        jLabelClock = new JLabel(" ", SwingConstants.CENTER);
        jLabelClock.setFont(new Font("Arial", Font.PLAIN, 12));
        jLabelClock.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
        jLabelClock.setForeground(new Color(218, 247, 166)); //#DAF7A6
        addToNextRow(jLabelClock, 1, 1, 0.0, 0.0, GridBagConstraints.BOTH, GridBagConstraints.CENTER);
        bClockVisible = false;
    }

    public void setType(int iType) {
        if (this.iType != DSP_EMPTY) {
            //System.out.println("xxx");
            return;
        }
        this.iType = iType;
        if (this.iType == DSP_HORIZONTAL_BARS) {
            dv = new DashboardView();
            dv.setBackground(colorBackground);
            cp.addToCurrentRow(dv, 1, 1, 1.0, 1.0, GridBagConstraints.BOTH, GridBagConstraints.CENTER);
        }
    }

    public void setTitle(String sTitle) {
        jLabelTitle.setText(sTitle);
    }

    public void setClockVisible(boolean bVisible) {
        bClockVisible = bVisible;
    }

    //@todo: to be deleted!!!
    public CommonPanel getPanel() {
        return (cp);
    }

    public void setPanel(CommonPanel commonPanel) {
        if (iType == DSP_PANEL) {
            cp.addToCurrentRow(commonPanel, 1, 1, 1.0, 1.0, GridBagConstraints.BOTH, GridBagConstraints.CENTER);
        }
    }

    public void setBordercolor(Color c) {
        if (c == null) {
            this.setBorder(BorderFactory.createLineBorder(colorBorder, 5));
        } else {
            this.setBorder(BorderFactory.createLineBorder(c, 5));
        }
    }

    public void setGridWidth(int iGridWidth) {
        this.iGridWidth = Math.max(Math.min(iGridWidth, 3), 1);
    }

    public int getGridWidth() {
        return (iGridWidth);
    }

    public void setGridHeight(int iGridHeight) {
        this.iGridHeight = Math.max(Math.min(iGridHeight, 3), 1);
    }

    public int getGridHeight() {
        return (iGridHeight);
    }

    public void setRefreshPeriod(int iRefreshPeriod) {
        this.iRefreshPeriod = Math.max(iRefreshPeriod, 1);
        iRefreshCounter = 1;
        bSynchronizeRefresh = true;
    }

    public int getRefreshPeriod() {
        return (iRefreshPeriod);
    }

    public void enable() {
    }

    public void disable() {
    }

    public void addCategory(String sName, String sTitle) {
        if (iType == DSP_HORIZONTAL_BARS) {
            dv.addCategory(sName, sTitle);
        }
    }

    public void setValue(String sName, int iValue) {
        dv.setValue(sName, iValue);
    }

    public void setMaxValue(int iMaxValue) {
        dv.setMaxValue(iMaxValue);
    }

    public void clock() {
        Date date = new Date();
        sClock = swingAppInterface.getClockDateTimeFormat().format(date);
        if (bClockVisible) {
            jLabelClock.setText(swingAppInterface.getClockDateTimeFormat().format(new Date()) + (bInRefresh ? " *" : ""));
        } else {
            jLabelClock.setText("");
        }
        if (--iRefreshCounter <= 0) {
            //do not start another thread when bInRefresh==true (but after a while?...)
            if (!bInRefresh || iRefreshCounter < -60) {
                refreshInThread(this);
                iRefreshCounter = iRefreshPeriod;
                if (bSynchronizeRefresh) {
                    int iSecondsInDay = DateTimeUtils.getSecondsInDay(date);
                    int i = iSecondsInDay % iRefreshCounter;
                    iRefreshCounter -= i;
                }
            }
        }
    }

    private void refreshInThread(CommonPanel cpParent) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                bInRefresh = true;
                //cpParent.setBackground(Color.white); //@todo "hourglass" - running rounded bar under the clock ...
                refresh();
                bInRefresh = false;
                //cpParent.setBackground(colorBackground);  //@todo "hourglass" off
            }
        });
        t.setPriority(Thread.NORM_PRIORITY);
        t.start();
    }

    protected void refresh() {
    }

    public static DashboardSubPanel createDashboardSubPanelHorizontalBars(SwingAppInterface swingAppInterface, DashboardPanel dashboardPanel, String sTitle) {
        DashboardSubPanel dashboardSubPanel = new DashboardSubPanel(swingAppInterface, dashboardPanel, sTitle);
        dashboardSubPanel.setType(DSP_HORIZONTAL_BARS);
        return (dashboardSubPanel);
    }
}
