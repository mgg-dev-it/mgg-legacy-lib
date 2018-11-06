package hu.mag.swing;

import hu.mag.swing.table.MagTablePanel;
import hu.mgx.app.common.CommonAppUtils;
import hu.mgx.app.swing.SwingAppInterface;
import java.awt.Dimension;
import java.awt.GridBagConstraints;

/**
 *
 * @author MaG
 */
public class TableViewMagInternalFrame extends MagInternalFrame {

    protected MagTablePanel magTablePanel;

    public TableViewMagInternalFrame(String sTitle, SwingAppInterface swingAppInterface, String[] args) {
        super(sTitle, swingAppInterface, args);
    }

    @Override
    protected void createControls() {
        String sConnectionName = CommonAppUtils.getParameterValue(argsMap, "-connection");
        String sTableName = CommonAppUtils.getParameterValue(argsMap, "-table");
        String sOrderBy = CommonAppUtils.getParameterValue(argsMap, "-orderby");
        Boolean bExcelButton = CommonAppUtils.parameterExists(argsMap, "-excel");
        magTablePanel = new MagTablePanel(swingAppInterface, sConnectionName, sTableName, sOrderBy);
        if (bExcelButton) {
            magTablePanel.getMagButtonPanel().setAllButtonVisible(false);
            magTablePanel.getMagButtonPanel().setButtonVisible(true, MagButton.BUTTON_EXCEL);
        } else {
            magTablePanel.getMagButtonPanel().setEnabled(false);
            magTablePanel.getMagButtonPanel().setVisible(false);
        }
        magTablePanel.getMagTableModel().setReadOnlyTable(true);
        magTablePanel.setPreferredSize(new Dimension(new Double(swingAppInterface.getAppFrame().getSize().width * 0.7).intValue(), new Double(swingAppInterface.getAppFrame().getSize().height * 0.7).intValue()));
    }

    @Override
    protected void createLayout() {
        mainPane.addToCurrentRow(magTablePanel, 1, 1, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER);
    }

}
