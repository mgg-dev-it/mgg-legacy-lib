package hu.mag.swing.table;

import hu.mag.swing.MagButton;
import hu.mag.swing.MagFieldInterface;
import hu.mgx.app.common.LanguageEvent;
import hu.mgx.app.common.LanguageEventListener;
import hu.mgx.app.swing.AppUtils;
import hu.mgx.app.swing.SwingAppInterface;
import hu.mgx.swing.CommonPanel;
import hu.mgx.util.StringUtils;
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
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class MagSimpleRecordEditor extends JDialog implements ActionListener, KeyListener, WindowListener, LanguageEventListener {

    private SwingAppInterface swingAppInterface = null;
    private Vector<String> vLabels;
    private Vector<Component> vEditors;
    private CommonPanel cpMain = null;
    private Vector<Component> vFields = null;
    private CommonPanel cpDataPanel = null;
    private JScrollPane jspDataPanel = null;
    private CommonPanel cpButtonPanel = null;
    private MagButton mbSave;
    private MagButton mbCancel;

    public static final int OK = 1;
    public static final int ESCAPE = -1;
    private int iAction = ESCAPE;

    public MagSimpleRecordEditor(SwingAppInterface swingAppInterface, Vector vLabels, Vector vEditors) {
        super(swingAppInterface.getAppFrame(), "", true);
        this.swingAppInterface = swingAppInterface;
        this.vLabels = vLabels;
        this.vEditors = vEditors;
        init();
    }

    private void init() {
        if (vLabels.size() != vEditors.size()) {
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
        String sCellValue = "";

        vFields = new Vector<Component>();
        for (int iField = 0; iField < vLabels.size(); iField++) {
            Component ccc = vEditors.elementAt(iField);

            vFields.add(ccc);

            iWidth = 150;
            sCellValue = "";
            if (ccc instanceof MagFieldInterface) {
                sCellValue = (StringUtils.isNull(((MagFieldInterface) ccc).getValue(), ""));
            }
            if (fm.stringWidth(sCellValue) > iWidth) {
                iWidth = fm.stringWidth(sCellValue) * 110 / 100;
            }
            if (iWidth < 100) {
                iWidth = 100;
            }
            if (iWidth > 2000) {
                iWidth = 2000;
            }

            vFields.lastElement().setPreferredSize(new Dimension(iWidth, 21));
            vFields.lastElement().addKeyListener(this);
            cpDataPanel.addToCurrentRow(AppUtils.createLabel(vLabels.elementAt(iField) + ": "), 1, 1, 0, 0, GridBagConstraints.BOTH);
            cpDataPanel.addToCurrentRow(vFields.lastElement(), 1, 1, 0, 1.0, GridBagConstraints.BOTH);
            cpDataPanel.nextRow();
        }

        jspDataPanel = new JScrollPane(cpDataPanel);
        jspDataPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        cpMain.addToCurrentRow(jspDataPanel, 2, 1, 1.0, 1.0, GridBagConstraints.BOTH);
        cpMain.nextRow();

        cpButtonPanel = new CommonPanel();
        cpButtonPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        //mbSave = MagButton.createMagButton(swingAppInterface, MagButton.BUTTON_SAVE, this);
        mbSave = MagButton.createMagButton(swingAppInterface, MagButton.BUTTON_OK, this);
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
