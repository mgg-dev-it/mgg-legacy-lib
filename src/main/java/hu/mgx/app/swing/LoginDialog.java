package hu.mgx.app.swing;

import hu.mgx.swing.CommonPanel;
import hu.mgx.app.common.DefaultLanguageConstants;
import hu.mgx.app.common.LanguageHandlerInterface;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import hu.mgx.app.common.*;
import hu.mgx.db.*;
import hu.mgx.swing.*;

public class LoginDialog extends JDialog implements ActionListener, KeyListener {

    private CommonPanel mainPane;
    private String sStringLogin = "&Bejelentkezés";
    private String sStringCancel = "&Mégsem";
    private String sStringUserID = "Felhasznaló azonosító";
    private String sStringPassword = "Jelszó";
    private String sStringPasswordChange = "&Jelszócsere";
    private String sStringOldPassword = "Régi jelszó";
    private String sStringNewPassword = "Új jelszó";
    private String sStringNewPasswordAgain = "Új jelszó még egyszer";
    private JButton jButtonYes;
    private JButton jButtonNo;
    private JLabel jLabelPw;
    private JLabel jLabelNewPw1;
    private JLabel jLabelNewPw2;
    private JLabel jLabelCapslock;
    private CommonTextField ctf_username;
    private CommonPasswordField cpf_password;
    private CommonPasswordField cpf_password_new1;
    private CommonPasswordField cpf_password_new2;
    public static final int YES = 0;
    public static final int NO = 1;
    public static final int ESCAPE = -1;
    private int iAction = ESCAPE;
    private AppInterface appInterface;
    private final static String ACTION_YES = "Action_Yes";
    private final static String ACTION_NO = "Action_No";
    private LanguageHandlerInterface appModulLanguage = null;
    private boolean bPasswordChange = false;

    public LoginDialog(JFrame f, AppInterface appInterface) {
        super(f);
        this.appInterface = appInterface;
        this.appModulLanguage = null;
        init();
    }

    public LoginDialog(JFrame f, AppInterface appInterface, LanguageHandlerInterface appModulLanguage) {
        super(f);
        this.appInterface = appInterface;
        this.appModulLanguage = appModulLanguage;
        init();
    }

    private void init() {
        if (appModulLanguage != null) {
            sStringLogin = appModulLanguage.getLanguageString(DefaultLanguageConstants.STRING_LOGIN);
            sStringCancel = appModulLanguage.getLanguageString(DefaultLanguageConstants.STRING_CANCEL);
            sStringUserID = appModulLanguage.getLanguageString(DefaultLanguageConstants.STRING_USER_ID);
            sStringPassword = appModulLanguage.getLanguageString(DefaultLanguageConstants.STRING_PASSWORD);
            sStringPasswordChange = appModulLanguage.getLanguageString(DefaultLanguageConstants.STRING_PASSWORD_CHANGE);
            sStringOldPassword = appModulLanguage.getLanguageString(DefaultLanguageConstants.STRING_OLD_PASSWORD);
            sStringNewPassword = appModulLanguage.getLanguageString(DefaultLanguageConstants.STRING_NEW_PASSWORD);
            sStringNewPasswordAgain = appModulLanguage.getLanguageString(DefaultLanguageConstants.STRING_NEW_PASSWORD_AGAIN);
        }
        setModal(true);
        mainPane = new CommonPanel();
        mainPane.setInsets(2, 3, 2, 3);
        setContentPane(mainPane);

        mainPane.addToGrid(new JLabel(AppUtils.getLabelCaption(sStringUserID)), 0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        mainPane.addToGrid(ctf_username = new CommonTextField(new FieldDefinition("", "", FieldType.CHAR, 255, 0, 0, true, false, false), appInterface), 0, 1, 2, 1, 0.0, 0.0, GridBagConstraints.BOTH);
//        mainPane.addToGrid(new JLabel(""), 0, 3, GridBagConstraints.REMAINDER, 1, 0.0, 1.0, GridBagConstraints.BOTH);

        mainPane.addToGrid(jLabelPw = new JLabel(AppUtils.getLabelCaption(sStringPassword)), 1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        mainPane.addToGrid(cpf_password = new CommonPasswordField(new FieldDefinition("", "", FieldType.CHAR, 255, 0, 0, true, false, false), appInterface), 1, 1, 2, 1, 0.0, 0.0, GridBagConstraints.BOTH);
        mainPane.addToGrid(new JLabel(""), 1, 3, GridBagConstraints.REMAINDER, 1, 0.0, 1.0, GridBagConstraints.BOTH);

        mainPane.addToGrid(jLabelNewPw1 = new JLabel(AppUtils.getLabelCaption(sStringNewPassword)), 2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        mainPane.addToGrid(cpf_password_new1 = new CommonPasswordField(new FieldDefinition("", "", FieldType.CHAR, 255, 0, 0, true, false, false), appInterface), 2, 1, 2, 1, 0.0, 0.0, GridBagConstraints.BOTH);
        mainPane.addToGrid(new JLabel(""), 2, 3, GridBagConstraints.REMAINDER, 1, 0.0, 1.0, GridBagConstraints.HORIZONTAL);

        mainPane.addToGrid(jLabelNewPw2 = new JLabel(AppUtils.getLabelCaption(sStringNewPasswordAgain)), 3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        mainPane.addToGrid(cpf_password_new2 = new CommonPasswordField(new FieldDefinition("", "", FieldType.CHAR, 255, 0, 0, true, false, false), appInterface), 3, 1, 2, 1, 0.0, 0.0, GridBagConstraints.BOTH);
//        mainPane.addToGrid(new JLabel(""), 3, 3, GridBagConstraints.REMAINDER, 1, 0.0, 1.0, GridBagConstraints.HORIZONTAL);

        mainPane.addToGrid(new JLabel(""), 4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NONE);
        mainPane.addToGrid(jLabelCapslock = new JLabel("CapsLock"), 4, 1, 2, 1, 0.0, 1.0, GridBagConstraints.BOTH);
        jLabelCapslock.setForeground(Color.red);
        jLabelCapslock.setHorizontalAlignment(JLabel.CENTER);
        //jLabelCapslock.setBorder(BorderFactory.createLineBorder(Color.black));
//        mainPane.addToGrid(new JLabel(""), 4, 3, GridBagConstraints.REMAINDER, 1, 0.0, 1.0, GridBagConstraints.BOTH);

        mainPane.addToGrid(new JLabel(""), 5, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NONE);
        mainPane.addToGrid(jButtonYes = AppUtils.createButton(sStringLogin, ACTION_YES, this), 5, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NONE, GridBagConstraints.CENTER);
        mainPane.addToGrid(jButtonNo = AppUtils.createButton(sStringCancel, ACTION_NO, this), 5, 2, 1, 1, 1.0, 1.0, GridBagConstraints.NONE, GridBagConstraints.CENTER);

        ctf_username.addKeyListener(this);
        cpf_password.addKeyListener(this);
        cpf_password_new1.addKeyListener(this);
        cpf_password_new2.addKeyListener(this);
    }

    public int showLoginDialog(JFrame f, String sUserName, String sPassword) {
        bPasswordChange = false;
        setTitle(AppUtils.getCaption(sStringLogin));
        ctf_username.setText(sUserName);
        jLabelPw.setText(AppUtils.getLabelCaption(sStringPassword));
        cpf_password.setText(sPassword);

        ctf_username.setEnabled(true);
        jLabelNewPw1.setEnabled(false);
        jLabelNewPw1.setVisible(false);
        cpf_password_new1.setEnabled(false);
        cpf_password_new1.setVisible(false);
        jLabelNewPw2.setEnabled(false);
        jLabelNewPw2.setVisible(false);
        cpf_password_new2.setEnabled(false);
        cpf_password_new2.setVisible(false);

        AppUtils.setButtonCaptionAndMnemonic(jButtonYes, sStringLogin);
        AppUtils.setButtonCaptionAndMnemonic(jButtonNo, sStringCancel);

        setCapsLockLabel();

        iAction = ESCAPE;
        pack();
        setLocationRelativeTo(f);
        if (ctf_username.getText().trim().equalsIgnoreCase("")) {
            ctf_username.requestFocus();
        } else {
            cpf_password.requestFocus();
        }

        //show();
        setVisible(true);
        return (iAction);
    }

    public int showChangePasswordDialog(JFrame f, String sUserName, String sPassword) {
        bPasswordChange = true;
        setTitle(AppUtils.getCaption(sStringPasswordChange));
        ctf_username.setText(sUserName);
        jLabelPw.setText(AppUtils.getLabelCaption(sStringOldPassword));
        cpf_password.setText(sPassword);

        ctf_username.setEnabled(false);
        jLabelNewPw1.setEnabled(true);
        jLabelNewPw1.setVisible(true);
        cpf_password_new1.setEnabled(true);
        cpf_password_new1.setVisible(true);
        jLabelNewPw2.setEnabled(true);
        jLabelNewPw2.setVisible(true);
        cpf_password_new2.setEnabled(true);
        cpf_password_new2.setVisible(true);

        AppUtils.setButtonCaptionAndMnemonic(jButtonYes, sStringPasswordChange);
        AppUtils.setButtonCaptionAndMnemonic(jButtonNo, sStringCancel);

        setCapsLockLabel();

        iAction = ESCAPE;
        pack();
        setLocationRelativeTo(f);
        cpf_password.requestFocus();
        setVisible(true);
        return (iAction);
    }

    private void setCapsLockLabel() {
        boolean bCapsLock = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
        jLabelCapslock.setText((bCapsLock ? "A CapsLock be van kapcsolva!" : "")); //@todo task: nyelves�t�s
        pack();
    }

    public String getUserName() {
        return (ctf_username.getValue().toString());
    }

    public String getPassword() {
        return (cpf_password.getValue().toString());
    }

    public String getNewPassword1() {
        return (cpf_password_new1.getValue().toString());
    }

    public String getNewPassword2() {
        return (cpf_password_new2.getValue().toString());
    }

    private void actionYES() {
        iAction = YES;
        setVisible(false);
    }

    private void actionNO() {
        iAction = NO;
        setVisible(false);
    }

    private void actionESCAPE() {
        iAction = ESCAPE;
        setVisible(false);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(ACTION_YES)) {
            actionYES();
        }
        if (e.getActionCommand().equals(ACTION_NO)) {
            actionNO();
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!bPasswordChange && e.getSource().equals(cpf_password)) {
                actionYES();
            }
            if (bPasswordChange && e.getSource().equals(cpf_password_new2)) {
                actionYES();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            actionESCAPE();
        }
    }

    public void keyReleased(KeyEvent e) {
        setCapsLockLabel();
    }

    public void keyTyped(KeyEvent e) {
    }
}
