package hu.mag.swing.table;

import hu.mag.swing.MagInternalFrame;
import hu.mgx.app.common.CommonAppUtils;
//import hu.mgx.swing.*;
import hu.mgx.app.swing.*;

import java.awt.*;
import javax.swing.JTabbedPane;

public class MaintMultiInternalFrame extends MagInternalFrame {

//    protected SwingApp app;
//    protected CommonPanel mainPane;
//    protected String sConnectionName;
//    protected String sTableName;
//    protected MagTablePanel magTablePanel;
    protected JTabbedPane jTabbedPane;

    public MaintMultiInternalFrame(String sTitle, SwingAppInterface swingAppInterface, String[] args) {
        super(sTitle, swingAppInterface, args);
    }

    @Override
    protected void createControls() {
        jTabbedPane = new JTabbedPane();
        jTabbedPane.setFocusable(false);
        jTabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        for (int i = 0; i < 10; i++) {
            String sConnectionName = CommonAppUtils.getParameterValue(argsMap, "-connection" + Integer.toString(i));
            String sTableName = CommonAppUtils.getParameterValue(argsMap, "-table" + Integer.toString(i));
            String sTitle = CommonAppUtils.getParameterValue(argsMap, "-title" + Integer.toString(i));
            if (sConnectionName.length() > 0 && sTableName.length() > 0 && sTitle.length() > 0) {
                MagTablePanel magTablePanel = new MagTablePanel(swingAppInterface, sConnectionName, sTableName);
                magTablePanel.getMagTable().setAddNewRowAtTheEnd(true);
                magTablePanel.setParentFrame(swingAppInterface.getAppFrame());
                jTabbedPane.addTab(sTitle, magTablePanel);
            }
        }
    }

    @Override
    protected void createLayout() {
        mainPane.addToCurrentRow(jTabbedPane, 1, 1, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER);
        mainPane.setPreferredSize(new Dimension(new Double(swingAppInterface.getAppFrame().getSize().width * 0.7).intValue(), new Double(swingAppInterface.getAppFrame().getSize().height * 0.7).intValue()));
    }

}
