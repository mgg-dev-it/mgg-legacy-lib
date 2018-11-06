package hu.mgx.app.swing;

import hu.mgx.swing.CommonPanel;
import java.awt.*;
import java.text.*;

import javax.swing.*;

public class ProgressDisplay extends JDialog
{

    private CommonPanel mainPane;
    private JLabel jLabel;
    public static final int YES = 0;
    public static final int NO = 1;
    public static final int ESCAPE = -1;
    private int iAction = ESCAPE;
    private JProgressBar jProgressBar;

    public ProgressDisplay(Window w, int min, int max)
    {
        super(w);
        init(min, max);
    }

    public ProgressDisplay(JFrame f, int min, int max)
    {
        super(f);
        init(min, max);
    }

    private void init(int min, int max)
    {
        setModal(false);
        //setUndecorated(true);
        setType(Type.UTILITY);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        mainPane = new CommonPanel();
        mainPane.setInsets(5, 7, 5, 7);
        setContentPane(mainPane);
        mainPane.addToGrid(jLabel = new JLabel(""), 0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NONE, GridBagConstraints.CENTER);
        jLabel.setVerticalAlignment(SwingConstants.CENTER);
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        jProgressBar = new JProgressBar(min, max);
        jProgressBar.setStringPainted(true);
        jProgressBar.setPreferredSize(new Dimension(300, 21));
        mainPane.addToGrid(jProgressBar, 1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.BOTH, GridBagConstraints.CENTER);
        pack();
    }

    public int showDialog(Component c, String sTitle, String sMsg)
    {
        setTitle(sTitle);
        jLabel.setText(sMsg);
        setLocationRelativeTo(c);
        iAction = ESCAPE;
        pack();
        setVisible(true);
        return (iAction);
    }

    public void setMessage(String sMsg)
    {
        jLabel.setText(sMsg);
        this.validate();
    }

    public void setValue(int i)
    {
        jProgressBar.setValue(i);
        double dTotal = new Double(jProgressBar.getMaximum() - jProgressBar.getMinimum()).doubleValue();
        double dCurrent = new Double(jProgressBar.getValue() - jProgressBar.getMinimum()).doubleValue();
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00%");
        //DecimalFormat decimalFormat = new DecimalFormat("#,##0.##%");
        jProgressBar.setString(decimalFormat.format(dCurrent / dTotal));
        this.validate();
    }
}
