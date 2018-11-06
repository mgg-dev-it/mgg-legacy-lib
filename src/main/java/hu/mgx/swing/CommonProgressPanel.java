package hu.mgx.swing;

import hu.mgx.app.common.AppInterface;

import java.awt.*;
import java.text.*;
import java.util.*;
import javax.swing.*;

public class CommonProgressPanel extends CommonPanel
{

    private static int _is = -1;
    public final static int STYLE_1 = ++_is;
    public final static int STYLE_2 = ++_is;
    //
    private AppInterface appInterface = null;
    private int iStyle = 0;
    //
    private DecimalFormat dfPercentage = new DecimalFormat("#,##0.00%");
    private DecimalFormat dfBytes = new DecimalFormat("#,##0");
    private JProgressBar jProgressBarOne = null;
    private JProgressBar jProgressBarTotal = null;
    private JLabel jLabelSpeed = null;
    private JLabel jLabelStart = null;
    private JLabel jLabelFinish = null;
    private JLabel jLabelStartDate = null;
    private JLabel jLabelFinishDate = null;
    //
    private Date dateStart = null;
    private Date dateFinish = null;

    public CommonProgressPanel(AppInterface appInterface, int iStyle)
    {
        super();
        this.appInterface = appInterface;
        this.iStyle = iStyle;
        init();
    }

    private void init()
    {
        createPanel();
    }

    private void createControls()
    {
        jProgressBarOne = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);
        jProgressBarOne.setStringPainted(true);
        jProgressBarTotal = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);
        jProgressBarTotal.setStringPainted(true);
        jLabelSpeed = new JLabel(" ");
        jLabelStart = new JLabel("Kezdés:");
        jLabelFinish = new JLabel("Várható befejezés:");
        dateStart = new Date();
        dateFinish = new Date();
        jLabelStartDate = new JLabel("");
        jLabelFinishDate = new JLabel("");
    }

    private void createPanel()
    {
        setBorder(BorderFactory.createEmptyBorder());
        //setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
        //super.gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        createControls();

        setRow(0);
        addToCurrentRow(jLabelSpeed, 2, 1, 0.0, 1.0, GridBagConstraints.NONE, GridBagConstraints.CENTER);

        if (iStyle == STYLE_2)
        {
            nextRow();
            addToCurrentRow(jProgressBarOne, 2, 1, 0.0, 1.0, GridBagConstraints.BOTH, GridBagConstraints.WEST);
        }

        nextRow();
        addToCurrentRow(jProgressBarTotal, 2, 1, 0.0, 1.0, GridBagConstraints.BOTH, GridBagConstraints.WEST);

        nextRow();
        addToCurrentRow(jLabelStart, 1, 1, 0.0, 0.0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        addToCurrentRow(jLabelStartDate, 1, 1, 0.0, 0.0, GridBagConstraints.NONE, GridBagConstraints.WEST);

        nextRow();
        addToCurrentRow(jLabelFinish, 1, 1, 0.0, 0.0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        addToCurrentRow(jLabelFinishDate, 1, 1, 0.0, 0.0, GridBagConstraints.NONE, GridBagConstraints.WEST);
    }

    public void start()
    {
        jProgressBarOne.setValue(0);
        jProgressBarOne.setString(dfPercentage.format(0.0));
        jProgressBarTotal.setValue(0);
        jProgressBarTotal.setString(dfPercentage.format(0.0));
        dateStart = new Date();
        jLabelStartDate.setText(appInterface.getDateTimeFormat().format(dateStart));
        jLabelFinishDate.setText("");
    }

    public void setPartValue(double dValue)
    {
        if (dValue < 1.0)
        {
            dValue = 0;
        }
        if (dValue > 100)
        {
            dValue = 100;
        }
        jProgressBarOne.setValue(new Double(dValue).intValue());
        jProgressBarOne.setString(dfPercentage.format(dValue / 100.0));
    }

    public void setValue(double dValue)
    {
        setValue(dValue, 0);
    }

    public void setValue(double dValue, long lBytes)
    {
        Date dateCurrent = new Date();
        long lEstimatedFinishTime = 0;
        long lElapsedTime = 0;

        if (dValue < 1.0)
        {
            dValue = 0;
        }
        if (dValue > 100)
        {
            dValue = 100;
        }
        if (dValue >= 1.0)
        {
            lElapsedTime = dateCurrent.getTime() - dateStart.getTime();
            lEstimatedFinishTime = dateStart.getTime() + new Double(lElapsedTime * 100.0 / dValue).longValue();
            dateFinish = new Date(lEstimatedFinishTime);
            jLabelFinishDate.setText(appInterface.getDateTimeFormat().format(dateFinish));
        }
        jProgressBarTotal.setValue(new Double(dValue).intValue());
        jProgressBarTotal.setString(dfPercentage.format(dValue / 100.0));
        if (lBytes > 0 && lElapsedTime > 1000)
        {
            jLabelSpeed.setText(dfBytes.format(new Long(lBytes / (lElapsedTime / 1000))) + " bytes / sec");
        }
    }

    public void finish()
    {
        dateFinish = new Date();
        jLabelFinishDate.setText(appInterface.getDateTimeFormat().format(dateFinish));
        jLabelFinish = new JLabel("Befejezve:");
        jProgressBarOne.setValue(100);
        jProgressBarOne.setString(dfPercentage.format(1.0));
        jProgressBarTotal.setValue(100);
        jProgressBarTotal.setString(dfPercentage.format(1.0));
    }
}
