package hu.mag.swing.dialog;

import hu.mag.db.FieldInfo;
import hu.mag.swing.MagButton;
import hu.mag.swing.table.MagTableEvent;
import hu.mag.swing.table.MagTableEventListener;
import hu.mag.swing.table.MagTablePanel;
import hu.mgx.app.common.*;
import hu.mgx.app.swing.*;
import hu.mgx.swing.CommonPanel;
import hu.mgx.swing.table.MemoryTable;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import javax.swing.*;

public class SelectFromMagTableDialog extends JDialog implements ActionListener, KeyListener, WindowListener, LanguageEventListener, MagTableEventListener {

    private CommonPanel mainPane;
    private MagButton mbSave;
    private MagButton mbCancel;

    private MagTablePanel magTablePanel;

    public static final int OK = 0;
    public static final int CANCEL = 1;
    public static final int ESCAPE = -1;
    private int iAction = ESCAPE;
    private SwingAppInterface swingAppInterface;
    private int iSelectedRow = -1;
    private boolean bSimple = false;
    private Component compAlignToTop = null;

    public SelectFromMagTableDialog(JFrame f, SwingAppInterface swingAppInterface) {
        super(f);
        this.swingAppInterface = swingAppInterface;
        init();
    }

    public SelectFromMagTableDialog(JFrame f, SwingAppInterface swingAppInterface, boolean bSimple) {
        super(f);
        this.swingAppInterface = swingAppInterface;
        this.bSimple = bSimple;
        init();
    }

    private void init() {
        setModal(true);
        setType(Window.Type.UTILITY);
        if (bSimple) {
            setUndecorated(true);
        }
        //setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        mainPane = new CommonPanel();
        setContentPane(mainPane);

        magTablePanel = new MagTablePanel(swingAppInterface);
        magTablePanel.setFilterVisible(false);
        magTablePanel.setButtonsVisible(false);
        magTablePanel.setStatusVisible(false);
        if (bSimple) {
            mainPane.setInsets(0, 0, 0, 0);
            magTablePanel.setInsets(0, 0, 0, 0);
        }

        mainPane.addToGrid(magTablePanel, 0, 0, 2, 1, 1.0, 1.0, GridBagConstraints.BOTH, GridBagConstraints.CENTER);

        mbSave = MagButton.createMagButton(swingAppInterface, MagButton.BUTTON_OK, this);
        mainPane.addToGrid(mbSave, 1, 0, 1, 1, 0.0, 0.01, GridBagConstraints.NONE, GridBagConstraints.EAST);
        mbCancel = MagButton.createMagButton(swingAppInterface, MagButton.BUTTON_CANCEL, this);
        mainPane.addToGrid(mbCancel, 1, 1, 1, 1, 0.0, 0.01, GridBagConstraints.NONE, GridBagConstraints.WEST);
        getRootPane().setDefaultButton(mbSave);
        pack();
    }

    public void setAlignedToTop(Component c) {
        this.compAlignToTop = c;
    }

    public int showDialog(Component c, String sTitle, ResultSet rs, String sSelectColumn, String sSelectValue, String[] sHiddenColumnNames) {
        magTablePanel.refresh(rs);
        for (int i = 0; i < sHiddenColumnNames.length; i++) {
            FieldInfo fi = magTablePanel.getMagTable().getMagTableModel().getTableInfo().getFieldInfo(sHiddenColumnNames[i]);
            if (fi != null) {
                fi.setHidden(true);
            }
        }
        return (common(c, sTitle, sSelectColumn, sSelectValue, sHiddenColumnNames));
    }

    public int showDialog(Component c, String sTitle, MemoryTable mt, String sSelectColumn, String sSelectValue, String[] sHiddenColumnNames) {
        magTablePanel.refresh(mt);
        for (int i = 0; i < sHiddenColumnNames.length; i++) {
            int iHiddenColumn = mt.findColumn(sHiddenColumnNames[i]);
            if (iHiddenColumn > -1) {
                magTablePanel.getMagTable().getMagTableModel().setHiddenColumn(iHiddenColumn, true);
            }
        }
        return (common(c, sTitle, sSelectColumn, sSelectValue, sHiddenColumnNames));
    }

    private int common(Component c, String sTitle, String sSelectColumn, String sSelectValue, String[] sHiddenColumnNames) {
        setTitle(sTitle);
        if (sHiddenColumnNames.length > 0) {
            magTablePanel.getMagTable().setAutoColumnWidth();
        }
        magTablePanel.getMagTable().addMagTableEventListener(this);

        if (!sSelectColumn.equalsIgnoreCase("")) {
            boolean bFound = false;
            for (int i = 0; !bFound && i < magTablePanel.getMagTable().getRowCount(); i++) {
                if (magTablePanel.getMagTable().getStringValueAt(i, sSelectColumn).equals(sSelectValue)) {
                    magTablePanel.getMagTable().setRowSelectionInterval(i, i);
                    bFound = true;
                }
            }
        }

        magTablePanel.adjustSizeToContent(this, c.getWidth() - 100, c.getHeight() - 100);
        setLocationRelativeTo(c);
        iAction = ESCAPE;
        pack();

        if (compAlignToTop != null) {
//            System.out.println("c.getLocationOnScreen().x = " + c.getLocationOnScreen().x);
//            System.out.println("c.getLocationOnScreen().y = " + c.getLocationOnScreen().y);
//            System.out.println("compAlignToTop.getLocationOnScreen().x = " + compAlignToTop.getLocationOnScreen().x);
//            System.out.println("compAlignToTop.getLocationOnScreen().y = " + compAlignToTop.getLocationOnScreen().y);
//            System.out.println("compAlignToTop.getLocation().x = " + compAlignToTop.getLocation().x);
//            System.out.println("compAlignToTop.getLocation().y = " + compAlignToTop.getLocation().y);
//            System.out.println("this.getHeight() = " + this.getHeight());
//            System.out.println("this.getWidth() = " + this.getWidth());
            this.setLocation(compAlignToTop.getLocationOnScreen().x - (this.getWidth() - compAlignToTop.getWidth()) / 2, compAlignToTop.getLocationOnScreen().y - this.getHeight() - 8);
        }

        //to disable Enter and ESCAPE keys (selectNextRowCell and cancel actions)
        InputMap im = magTablePanel.getMagTable().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        im.put(KeyStroke.getKeyStroke("ENTER"), "none");
        im.put(KeyStroke.getKeyStroke("ESCAPE"), "none");

        magTablePanel.getMagTable().setNoFocusBorder(true);

        magTablePanel.getMagTable().requestFocus();
        setVisible(true);
        return (iAction);
    }

    public int getSelectedRow() {
        return (iSelectedRow);
    }

    public MagTablePanel getMagTablePanel() {
        return (magTablePanel);
    }

    private void actionOK() {
        iAction = OK;
        iSelectedRow = magTablePanel.getMagTable().getSelectedRow();
        setVisible(false);
    }

    private void actionCANCEL() {
        iAction = CANCEL;
        setVisible(false);
    }

    private void actionESCAPE() {
        iAction = ESCAPE;
        setVisible(false);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("action_ok")) {
            actionOK();
        }
        if (e.getActionCommand().equals("action_cancel")) {
            actionCANCEL();
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (mbSave.isFocusOwner()) {
                actionOK();
            }
            if (mbCancel.isFocusOwner()) {
                actionCANCEL();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            actionESCAPE();
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
        actionESCAPE();
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void languageEventPerformed(LanguageEvent e) {
        mbSave.languageEventPerformed(e);
        mbCancel.languageEventPerformed(e);
    }

    @Override
    public void tableEventPerformed(MagTableEvent e) {
        if (e.getEventID() == MagTableEvent.CLICK) {
            if (bSimple) {
                actionOK();
            }
        }
        if (e.getEventID() == MagTableEvent.DBL_CLICK) {
            actionOK();
        }
    }
}
