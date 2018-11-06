package hu.mgx.swing;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

public class CommonPanel extends JPanel {

    private GridBagLayout gridBagLayout;
    protected GridBagConstraints gridBagConstraints;
    private Component componentHasFocus = null;
    private int iCurrentRow = 0;
    private int iCurrentColumn = 0;
    private String sTitle = "";

    public CommonPanel() {
        super();
        gridBagLayout = new GridBagLayout();
        gridBagConstraints = new GridBagConstraints();
        setLayout(gridBagLayout);
        setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
    }

    public void setInsets(int top, int left, int bottom, int right) {
        gridBagConstraints.insets = new Insets(top, left, bottom, right);
    }

    public void addToGrid(Component comp, int row, int column, int gridwidth, int gridheight, double weightrow, double weightcolumn, int fill) {
        addToGrid(comp, row, column, gridwidth, gridheight, weightrow, weightcolumn, fill, GridBagConstraints.CENTER);
    }

    public void addToGrid(Component comp, int row, int column, int gridwidth, int gridheight, double weightrow, double weightcolumn, int fill, int anchor) {
        gridBagConstraints.gridx = column;
        gridBagConstraints.gridy = row;
        gridBagConstraints.weightx = weightcolumn;
        gridBagConstraints.weighty = weightrow;
        gridBagConstraints.gridwidth = gridwidth;
        gridBagConstraints.gridheight = gridheight;
        gridBagConstraints.fill = fill;
        gridBagConstraints.anchor = anchor;
        gridBagLayout.setConstraints(comp, gridBagConstraints);
        add(comp);
    }

    public void removeFromGrid(Component comp) {
        gridBagLayout.removeLayoutComponent(comp);
    }

    public void setFocusedComponent(Component c) {
        componentHasFocus = c;
    }

    public Component getFocusedComponent() {
        return (componentHasFocus);
    }

    public void setFixSize(Dimension dimension) {
        setMinimumSize(dimension);
        setPreferredSize(dimension);
        setMaximumSize(dimension);
    }

    public void setRow(int iRow) {
        this.iCurrentRow = iRow;
        this.iCurrentColumn = 0;
    }

    public void setCurrentColumn(int iColumn) {
        this.iCurrentColumn = iColumn;
    }

    public void nextRow() {
        ++this.iCurrentRow;
        this.iCurrentColumn = 0;
    }

    public void nextColumn() {
        ++this.iCurrentColumn;
    }

    public void addLabelToCurrentRow(Component comp) {
        addLabelToCurrentRow(comp, 1, 1);
    }

    public void addLabelToCurrentRow(Component comp, int gridwidth, int gridheight) {
        addToCurrentRow(comp, gridwidth, gridheight, 0d, 0d, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
    }

    public void addFieldToCurrentRow(Component comp) {
        addFieldToCurrentRow(comp, 1, 1);
    }

    public void addFieldToCurrentRow(Component comp, int gridwidth, int gridheight) {
        //addToCurrentRow(comp, gridwidth, gridheight, 0d, 0d, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
        addToCurrentRow(comp, gridwidth, gridheight, 1d, 1d, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
    }

    public void addLabelAndFieldToCurrentRow(Component componentLabel, Component componentField) {
        addToCurrentRow(componentLabel, 1, 1, 0d, 0d, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
        addToCurrentRow(componentField, 1, 1, 1d, 1d, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
    }

    public void addLabelAndFieldToCurrentRow(Component componentLabel, Component componentField, boolean bFieldCanGrow) {
        addToCurrentRow(componentLabel, 1, 1, 0d, 0d, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
        addToCurrentRow(componentField, 1, 1, 1d, 1d, (bFieldCanGrow ? GridBagConstraints.HORIZONTAL : GridBagConstraints.NONE), GridBagConstraints.WEST);
    }

    public void addToCurrentRow(Component comp, int gridwidth, int gridheight, double weightrow, double weightcolumn) {
        addToCurrentRow(comp, gridwidth, gridheight, weightrow, weightcolumn, GridBagConstraints.BOTH);
    }

    public void addToCurrentRow(Component comp, int gridwidth, int gridheight, double weightrow, double weightcolumn, int fill) {
        addToCurrentRow(comp, gridwidth, gridheight, weightrow, weightcolumn, fill, GridBagConstraints.CENTER);
    }

    public void addToCurrentRow(Component comp, int gridwidth, int gridheight, double weightrow, double weightcolumn, int fill, int anchor) {
        addToGrid(comp, this.iCurrentRow, this.iCurrentColumn, gridwidth, gridheight, weightrow, weightcolumn, fill, anchor);
        iCurrentColumn += gridwidth;
    }

    public void addLabelToNextRow(Component comp) {
        addLabelToNextRow(comp, 1, 1);
    }

    public void addLabelToNextRow(Component comp, int gridwidth, int gridheight) {
        addToNextRow(comp, gridwidth, gridheight, 0d, 0d, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
    }

    public void addFieldToNextRow(Component comp) {
        addFieldToNextRow(comp, 1, 1);
    }

    public void addFieldToNextRow(Component comp, int gridwidth, int gridheight) {
        //addToNextRow(comp, gridwidth, gridheight, 0d, 0d, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
        addToNextRow(comp, gridwidth, gridheight, 1d, 1d, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
    }

    public void addLabelAndFieldToNextRow(Component componentLabel, Component componentField) {
        addToNextRow(componentLabel, 1, 1, 0d, 0d, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
        addToNextRow(componentField, 1, 1, 1d, 1d, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
    }

    public void addLabelAndFieldToNextRow(Component componentLabel, Component componentField, boolean bFieldCanGrow) {
        addToNextRow(componentLabel, 1, 1, 0d, 0d, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
        addToNextRow(componentField, 1, 1, 1d, 1d, (bFieldCanGrow ? GridBagConstraints.HORIZONTAL : GridBagConstraints.NONE), GridBagConstraints.WEST);
    }

    public void addToNextRow(Component comp, int gridwidth, int gridheight, double weightrow, double weightcolumn) {
        addToNextRow(comp, gridwidth, gridheight, weightrow, weightcolumn, GridBagConstraints.BOTH);
    }

    public void addToNextRow(Component comp, int gridwidth, int gridheight, double weightrow, double weightcolumn, int fill) {
        addToNextRow(comp, gridwidth, gridheight, weightrow, weightcolumn, fill, GridBagConstraints.CENTER);
    }

    public void addToNextRow(Component comp, int gridwidth, int gridheight, double weightrow, double weightcolumn, int fill, int anchor) {
        nextRow();
        addToCurrentRow(comp, gridwidth, gridheight, weightrow, weightcolumn, fill, anchor);
    }

    public static CommonPanel createBorderedCommonPanel(String sTitle) {
        CommonPanel commonPanel = createCommonPanel();
        commonPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), sTitle));
        return (commonPanel);
    }

    public static CommonPanel createBorderedCommonPanel(String sTitle, int top, int left, int bottom, int right) {
        CommonPanel commonPanel = createCommonPanel();
        commonPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), sTitle));
        commonPanel.setInsets(top, left, bottom, right);
        return (commonPanel);
    }

    public void setBorderedCommonPanelTitle(String sTitle) {
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), sTitle));
        this.sTitle = sTitle;
    }

    public String getBorderedCommonPanelTitle() {
        return (sTitle);
    }

    public static CommonPanel createCommonPanel() {
        return (createCommonPanel(2, 2, 2, 2));
    }

    public static CommonPanel createCommonPanel(int top, int left, int bottom, int right) {
        CommonPanel commonPanel = new CommonPanel();
        commonPanel.setBorder(BorderFactory.createEmptyBorder());
        commonPanel.setInsets(top, left, bottom, right);
        return (commonPanel);
    }

    public static CommonPanel createCommonPanel(Border border, int top, int left, int bottom, int right) {
        CommonPanel commonPanel = new CommonPanel();
        commonPanel.setBorder(border);
        commonPanel.setInsets(top, left, bottom, right);
        return (commonPanel);
    }

    public static CommonPanel createBorderLessCommonPanel() {
        return (createCommonPanel(BorderFactory.createEmptyBorder(), 2, 2, 2, 2));
    }

}
