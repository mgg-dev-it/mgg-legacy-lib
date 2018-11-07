package hu.mag.swing.table;

import hu.mag.swing.MagButton;
import hu.mag.swing.MagComboBoxField;
import hu.mag.swing.MagLookupTextField;
import hu.mag.swing.MagTextField;
import hu.mgx.app.common.LanguageEvent;
import hu.mgx.app.common.LanguageEventListener;
import hu.mgx.app.swing.AppUtils;
import hu.mgx.app.swing.SwingAppInterface;
import hu.mgx.swing.CommonPanel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellEditor;

public class MagTableDetail extends JDialog implements ActionListener, KeyListener, WindowListener, LanguageEventListener {

    private SwingAppInterface swingAppInterface = null;
    private MagTable magTable = null;
    private CommonPanel cpMain = null;
    private Vector<Component> vFields = null;
    private CommonPanel cpDataPanel = null;
    private JScrollPane jspDataPanel = null;
    private CommonPanel cpButtonPanel = null;
//    private JButton jButtonOK = null;
//    private JButton jButtonCancel = null;
    private MagButton mbSave;
    private MagButton mbCancel;

    public static final int OK = 1;
    public static final int ESCAPE = -1;
    private int iAction = ESCAPE;

    public MagTableDetail(SwingAppInterface swingAppInterface, MagTable magTable) {
        super(swingAppInterface.getAppFrame(), "", true);
        this.swingAppInterface = swingAppInterface;
        this.magTable = magTable;
        init();
    }

    private void init() {
        int iRow = magTable.getSelectedRow();
        if (iRow < 0) {
            return;
        }
        setModal(true);
        setType(Window.Type.UTILITY);
        //setUndecorated(true);

        cpMain = new CommonPanel();
        cpMain.setBorder(new EmptyBorder(0, 0, 0, 0));

        cpDataPanel = new CommonPanel();
        cpDataPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        Font font = this.getFont();
        FontMetrics fm = this.getFontMetrics(font);
        int iWidth = 0;
        Object oCellValue = null;
        String sCellValue = "";
        MagCellRenderer mcr = new MagCellRenderer(magTable.getMagTableModel(), swingAppInterface);

//        vLabels = new Vector<JLabel>();
        vFields = new Vector<Component>();
        for (int iColumn = 0; iColumn < magTable.getColumnCount(); iColumn++) {
            //@todo error: ArrayIndexOutOfBoundsException
            TableCellEditor tce = magTable.getColumnModel().getColumn(iColumn).getCellEditor();
            Component ccc = tce.getTableCellEditorComponent(magTable, magTable.getValueAt(iRow, iColumn), false, iRow, iColumn);
            //vFields.add(magTable.getColumnModel().getColumn(iColumn).getCellEditor().getTableCellEditorComponent(magTable, magTable.getValueAt(iRow, iColumn), false, iRow, iColumn));
            vFields.add(ccc);
            if (vFields.lastElement() instanceof MagTextField) {
                ((MagTextField) vFields.lastElement()).setBorder(BorderFactory.createLineBorder(new Color(128, 128, 128), 1));
            }
            if (vFields.lastElement() instanceof MagLookupTextField) {
                ((MagLookupTextField) vFields.lastElement()).setOwner(this);
                ((MagLookupTextField) vFields.lastElement()).setBorder(BorderFactory.createLineBorder(new Color(128, 128, 128), 1));
            }

            iWidth = 150;
            sCellValue = "";
            oCellValue = magTable.getValueAt(iRow, iColumn);
            MagComboBoxField magLookupField = magTable.getMagTableModel().getColumnLookupField(iColumn);
            if (magLookupField != null) {
                sCellValue = magLookupField.getDisplay(oCellValue);
            } else {
                sCellValue = mcr.getTableCellRendererComponent(magTable, oCellValue, false, false, iRow, iColumn).toString();
                //System.out.println(sCellValue);
                if (sCellValue.startsWith("javax.swing.JCheckBox[")) {
                    sCellValue = "";
                }
            }
            if (fm.stringWidth(sCellValue) > iWidth) {
                iWidth = fm.stringWidth(sCellValue) * 110 / 100;
            }
            if (iWidth < 100) {
                iWidth = 100;
            }
            if (iWidth > 1000) {
                iWidth = 1000;
            }

            vFields.lastElement().setPreferredSize(new Dimension(iWidth, 21));
            vFields.lastElement().setEnabled(magTable.getMagTableModel().isCellEditable(iRow, iColumn));
            vFields.lastElement().addKeyListener(this);
            cpDataPanel.addToCurrentRow(AppUtils.createLabel(magTable.getMagTableModel().getColumnName(iColumn) + ": "), 1, 1, 0, 0, GridBagConstraints.BOTH);
            cpDataPanel.addToCurrentRow(vFields.lastElement(), 1, 1, 0, 1.0, GridBagConstraints.BOTH);
            cpDataPanel.nextRow();
        }

        jspDataPanel = new JScrollPane(cpDataPanel);
        jspDataPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        cpMain.addToCurrentRow(jspDataPanel, 2, 1, 1.0, 1.0, GridBagConstraints.BOTH);
        cpMain.nextRow();

        cpButtonPanel = new CommonPanel();
        cpButtonPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        //cpButtonPanel.setInsets(0, 0, 0, 0);

//        jButtonOK = AppUtils.createButton("OK", "action_ok", this);
//        jButtonCancel = AppUtils.createButton("Mégsem", "action_cancel", this);
//        cpButtonPanel.addToCurrentRow(jButtonOK, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.CENTER);
//        cpButtonPanel.addToCurrentRow(jButtonCancel, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.CENTER);
        mbSave = MagButton.createMagButton(swingAppInterface, MagButton.BUTTON_SAVE, this);
        cpButtonPanel.addToCurrentRow(mbSave, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.CENTER);
        mbCancel = MagButton.createMagButton(swingAppInterface, MagButton.BUTTON_CANCEL, this);
        cpButtonPanel.addToCurrentRow(mbCancel, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.CENTER);

        cpMain.addToCurrentRow(cpButtonPanel, 1, 1, 0, 1, GridBagConstraints.NONE, GridBagConstraints.CENTER);

        setContentPane(cpMain);
        pack();
    }

    public Vector<Component> getFields() {
        return (vFields);
    }

    public int showDialog(Component c, String sTitle) {
        this.setTitle(sTitle);
        if (this.getSize().height > swingAppInterface.getAppFrame().getSize().height) {
            this.setSize(new Dimension(this.getSize().width + 100, swingAppInterface.getAppFrame().getSize().height - 100));
        }
        if (this.getSize().width > swingAppInterface.getAppFrame().getSize().width) {
            this.setSize(new Dimension(swingAppInterface.getAppFrame().getSize().width - 100, this.getSize().height));
        }
        pack();
        //setLocationRelativeTo(magTable);
        setLocationRelativeTo(c);
        setVisible(true);
        return (iAction);
    }

    private void actionOK() {
        iAction = OK;
        setVisible(false);
    }

    private void actionESCAPE() {
        iAction = ESCAPE;
        setVisible(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == null) {
            if (e.getSource() != null) {
            }
            return;
        }
        if (e.getActionCommand().equals("action_ok")) {
            actionOK();
        }
        if (e.getActionCommand().equals("action_cancel")) {
            actionESCAPE();
        }
        if (e.getActionCommand().equals("action_save")) {
            actionOK();
        }
        if (e.getActionCommand().equals("action_cancel")) {
            actionESCAPE();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
//            actionOK();
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            actionESCAPE();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void windowOpened(WindowEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowClosing(WindowEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowClosed(WindowEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        actionESCAPE();
    }

    @Override
    public void windowIconified(WindowEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowActivated(WindowEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void languageEventPerformed(LanguageEvent e) {
        mbSave.languageEventPerformed(e);
        mbCancel.languageEventPerformed(e);
    }

}
