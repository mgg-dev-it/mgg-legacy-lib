package hu.mag.swing.dashboard;

import hu.mgx.app.swing.SwingAppInterface;
import hu.mgx.swing.CommonPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.Timer;

/**
 *
 * @author MaG
 */
public class DashboardPanel extends CommonPanel implements ActionListener {

    private final SwingAppInterface swingAppInterface;
    private CommonPanel cp;
//    private int iRow;
//    private int iColumn;
    private Timer timerRun = null;
    private Timer timerStart = null;
    private Vector<DashboardSubPanel> vDSP;
    private int iSubPanelWidth;
    private int iSubPanelHeight;
    private int iInset;

    public DashboardPanel(SwingAppInterface swingAppInterface) {
        super();
        this.swingAppInterface = swingAppInterface;
        iSubPanelWidth = 200;
        iSubPanelHeight = 200;
        iInset = 5;
        setInsets(0, 0, 0, 0);
        setBorder(BorderFactory.createEmptyBorder());
        cp = CommonPanel.createBorderLessCommonPanel();
        cp.setInsets(iInset, iInset, iInset, iInset);
        addToCurrentRow(cp, 1, 1, 0.0, 0.0);
        addToCurrentRow(new JLabel(), GridBagConstraints.REMAINDER, 1, 0.0, 1.0);
        addToNextRow(new JLabel(), 1, GridBagConstraints.REMAINDER, 1.0, 0.0);
//        iRow = 0;
//        iColumn = 0;
        vDSP = new Vector<>();

        timerRun = new javax.swing.Timer(1000, this);
        timerRun.setRepeats(true);

        timerStart = new javax.swing.Timer(2000, this);
        timerStart.setRepeats(false);
        timerStart.start();
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        if (cp != null) {
            cp.setBackground(bg);
        }
    }

    private void setSubPanelSize(DashboardSubPanel dashboardSubPanel) {
        dashboardSubPanel.setFixSize(new Dimension(dashboardSubPanel.getGridWidth() * iSubPanelWidth + (dashboardSubPanel.getGridWidth() - 1) * iInset, dashboardSubPanel.getGridHeight() * iSubPanelHeight + (dashboardSubPanel.getGridWidth() - 1) * iInset));
    }

    public void addSubPanelToCurrentRow(DashboardSubPanel dashboardSubPanel) {
        vDSP.add(dashboardSubPanel);
        setSubPanelSize(dashboardSubPanel);
        cp.addToCurrentRow(dashboardSubPanel, dashboardSubPanel.getGridWidth(), dashboardSubPanel.getGridHeight(), 0.0, 0.0);
    }

    public void addSubPanelToNextRow(DashboardSubPanel dashboardSubPanel) {
        vDSP.add(dashboardSubPanel);
        setSubPanelSize(dashboardSubPanel);
        cp.addToNextRow(dashboardSubPanel, dashboardSubPanel.getGridWidth(), dashboardSubPanel.getGridHeight(), 0.0, 0.0);
    }

    public void setSubPanelWidth(int iSubPanelWidth) {
        this.iSubPanelWidth = iSubPanelWidth;
    }

    public void setSubPanelHeight(int iSubPanelHeight) {
        this.iSubPanelHeight = iSubPanelHeight;
    }

    public void setInset(int iInset) {
        this.iInset = iInset;
    }

    private void clock() {
        for (int i = 0; i < vDSP.size(); i++) {
            vDSP.elementAt(i).clock();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == null) {
            if (e.getSource() != null) {
                if (e.getSource().equals(timerStart)) {
                    timerRun.start();
                }
                if (e.getSource().equals(timerRun)) {
                    clock();
                }
            }
        }
    }
}
