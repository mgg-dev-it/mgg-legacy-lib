package hu.mag.swing;

import hu.mag.swing.table.MagTablePanel;
import hu.mgx.app.common.CommonAppUtils;
import hu.mgx.app.swing.*;

import java.awt.*;
import java.util.HashMap;

/**
 *
 * @author MaG
 */
public class MagMaintInternalFrame extends MagInternalFrame {

    private String sConnectionName = "";
    private String sTableName = "";
    private MagTablePanel magTablePanel = null;

    public MagMaintInternalFrame(SwingAppInterface swingAppInterface, String[] args) {
        super("", swingAppInterface, args);
    }

    @Override
    protected void initializeMembers() {
        HashMap<String, String> argsMap = CommonAppUtils.preProcessArgs(args);
        sConnectionName = CommonAppUtils.getParameterValue(argsMap, "-connection");
        sTableName = CommonAppUtils.getParameterValue(argsMap, "-table");
        swingAppInterface.connect(sConnectionName);
        this.setTitle(sTableName);
    }

    @Override
    protected void createControls() {
        magTablePanel = new MagTablePanel(swingAppInterface, sConnectionName, sTableName);
        magTablePanel.getMagTable().setAddNewRowAtTheEnd(true);
        magTablePanel.setParentFrame(swingAppInterface.getAppFrame());
        if (magTablePanel.getMagTableModel().getTableInfo() != null) {
            this.setTitle(magTablePanel.getMagTableModel().getTableInfo().getDisplayName());
        } else {
            this.setTitle(sTableName);
        }

    }

    @Override
    protected void createLayout() {
        mainPane.addToCurrentRow(magTablePanel, 1, 1, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER);
    }

    @Override
    public void formatChanged() {
        magTablePanel.formatChanged();
    }

}
