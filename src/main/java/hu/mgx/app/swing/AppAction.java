package hu.mgx.app.swing;

import hu.mgx.swing.*;

public class AppAction
{

    private ToolbarButton toolbarButton;
    private CommonMenuItem commonMenuItem;
    private boolean bEnabled = true;

    public AppAction(ToolbarButton toolbarButton, CommonMenuItem commonMenuItem)
    {
        this.toolbarButton = toolbarButton;
        this.commonMenuItem = commonMenuItem;
    }
//    public AppAction(ToolbarButton toolbarButton, CommonMenuItem commonMenuItem, ActionListener actionListener, String sActionCommand) {
//        toolbarButton.setActionCommand(sActionCommand);
//        toolbarButton.addActionListener(actionListener);
//        commonMenuItem.setActionCommand(sActionCommand);
//        commonMenuItem.addActionListener(actionListener);
//        this.toolbarButton = toolbarButton;
//        this.commonMenuItem = commonMenuItem;
//    }
    public boolean isEnabled()
    {
        return bEnabled;
    }

    public void setEnabled(boolean bEnabled)
    {
        toolbarButton.setEnabled(bEnabled);
        commonMenuItem.setEnabled(bEnabled);
        this.bEnabled = bEnabled;
    }
}
