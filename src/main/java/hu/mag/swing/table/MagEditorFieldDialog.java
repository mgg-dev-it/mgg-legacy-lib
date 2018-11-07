package hu.mag.swing.table;

import hu.mag.swing.MagButton;
import hu.mag.swing.MagEditorField;
import hu.mgx.app.swing.*;
import hu.mgx.swing.CommonPanel;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import hu.mgx.app.common.*;

public class MagEditorFieldDialog extends JDialog implements ActionListener, KeyListener, WindowListener, LanguageEventListener {

    private CommonPanel mainPane;
//    private String sStringOK = "&OK";
//    private String sStringCancel = "&Mégsem";
//    private JButton jButtonOK;
//    private JButton jButtonCancel;
    private MagButton mbSave;
    private MagButton mbCancel;

    //private CommonTextField commonTextField;
    private MagEditorField magEditorField;

    public static final int OK = 0;
    public static final int CANCEL = 1;
    public static final int ESCAPE = -1;
    private int iAction = ESCAPE;
    private final static String ACTION_OK = "ACTION_OK";
    private final static String ACTION_CANCEL = "ACTION_CANCEL";
    private SwingAppInterface swingAppInterface;
    private LanguageHandlerInterface appModulLanguage = null;

    public MagEditorFieldDialog(JFrame f, SwingAppInterface swingAppInterface) {
        super(f);
        this.swingAppInterface = swingAppInterface;
        this.appModulLanguage = null;
        init();
    }

    public MagEditorFieldDialog(JFrame f, SwingAppInterface swingAppInterface, LanguageHandlerInterface appModulLanguage) {
        super(f);
        this.swingAppInterface = swingAppInterface;
        this.appModulLanguage = appModulLanguage;
        init();
    }

    private void init() {
//        if (appModulLanguage != null) {
//            sStringOK = appModulLanguage.getLanguageString(DefaultLanguageConstants.STRING_OK);
//            sStringCancel = appModulLanguage.getLanguageString(DefaultLanguageConstants.STRING_CANCEL);
//        }
        setModal(true);
        setType(Window.Type.UTILITY);
        //setUndecorated(true);
//        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        mainPane = new CommonPanel();
        //mainPane.setInsets(5, 7, 5, 7);
        mainPane.setInsets(2, 2, 2, 2);
        //mainPane.setInsets(0, 0, 0, 0);
        setContentPane(mainPane);

        magEditorField = new MagEditorField(swingAppInterface);
        //mef7.setMaxLength(10);
        //mef7.setUpperCase(true);
        magEditorField.setClass(String.class);
        //magEditorField.setPreferredSize(new Dimension(150, 150));

        mainPane.addToGrid(magEditorField, 0, 0, 2, 1, 1.0, 1.0, GridBagConstraints.BOTH, GridBagConstraints.CENTER);
        magEditorField.setPreferredSize(new Dimension(300, 300));
        magEditorField.addKeyListener(this);

        mbSave = MagButton.createMagButton(swingAppInterface, MagButton.BUTTON_SAVE, this);
        mainPane.addToGrid(mbSave, 1, 0, 1, 1, 0.0, 1.0, GridBagConstraints.NONE, GridBagConstraints.EAST);

//        mainPane.addToGrid(jButtonOK = AppUtils.createButton(sStringOK, ACTION_OK, this), 1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NONE, GridBagConstraints.EAST);
//        AppUtils.setButtonCaptionAndMnemonic(jButtonOK, sStringOK);
//        jButtonOK.getMnemonic();
//        jButtonOK.setFocusable(true);
//        jButtonOK.addKeyListener(this);
        mbCancel = MagButton.createMagButton(swingAppInterface, MagButton.BUTTON_CANCEL, this);
        mainPane.addToGrid(mbCancel, 1, 1, 1, 1, 0.0, 1.0, GridBagConstraints.NONE, GridBagConstraints.WEST);

//        mainPane.addToGrid(jButtonCancel = AppUtils.createButton(sStringCancel, ACTION_CANCEL, this), 1, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NONE, GridBagConstraints.WEST);
//        AppUtils.setButtonCaptionAndMnemonic(jButtonCancel, sStringCancel);
//        jButtonCancel.getMnemonic();
//        jButtonCancel.setFocusable(true);
//        jButtonCancel.addKeyListener(this);
        pack();
    }

    public int showDialog(Component c, String sTitle, String sText) {
        return (showDialog(c, sTitle, sText, CANCEL));
    }

    public int showDialog(Component c, String sTitle, String sText, int iDefaultButton) {
        setTitle(sTitle);
        magEditorField.setText(sText);
        setLocationRelativeTo(c);
        iAction = ESCAPE;
        pack();
        magEditorField.requestFocus();
        setVisible(true);
        return (iAction);
    }

    public String getText() {
        return (magEditorField.getText());
    }

    private void actionOK() {
        iAction = OK;
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
        if (e.getActionCommand().equals(ACTION_OK)) {
            actionOK();
        }
        if (e.getActionCommand().equals(ACTION_CANCEL)) {
            actionCANCEL();
        }
        if (e.getActionCommand().equals("action_save")) {
            actionOK();
        }
        if (e.getActionCommand().equals("action_cancel")) {
            actionCANCEL();
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
//            if (magEditorField.isFocusOwner()) {
//                actionOK();
//            }
//            if (jButtonOK.isFocusOwner()) {
//                actionOK();
//            }
//            if (jButtonCancel.isFocusOwner()) {
//                actionCANCEL();
//            }
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
