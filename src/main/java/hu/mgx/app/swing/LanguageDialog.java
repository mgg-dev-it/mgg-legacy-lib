package hu.mgx.app.swing;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import hu.mgx.app.common.*;
import hu.mgx.db.*;
import hu.mgx.swing.*;
import java.util.*;

public class LanguageDialog extends JDialog implements ActionListener, KeyListener {

    private CommonPanel mainPane;
    private String sStringLanguageSelect = "&Nyelv választás";
    private String sStringYes = "&Igen";
    private String sStringCancel = "&Mégsem";
    private String sStringLanguage = "Felhasználó azonosító";
    private JButton jButtonYes;
    private JButton jButtonNo;
    private CommonLookupField clf_languages;
    public static final int YES = 0;
    public static final int NO = 1;
    public static final int ESCAPE = -1;
    private int iAction = ESCAPE;
    private FormatInterface mgxFormat;
    private final static String ACTION_YES = "Action_Yes";
    private final static String ACTION_NO = "Action_No";
    private LanguageHandlerInterface languageHandlerInterface = null;

    public LanguageDialog(JFrame f, FormatInterface mgxFormat, LanguageHandlerInterface lhi) {
        super(f);
        this.mgxFormat = mgxFormat;
        this.languageHandlerInterface = lhi;
        init();
    }

    private void init() {
        if (languageHandlerInterface != null) {
            sStringLanguageSelect = languageHandlerInterface.getLanguageString(DefaultLanguageConstants.STRING_LANGUAGE_SELECT);
            sStringYes = languageHandlerInterface.getLanguageString(DefaultLanguageConstants.STRING_YES);
            sStringCancel = languageHandlerInterface.getLanguageString(DefaultLanguageConstants.STRING_CANCEL);
            sStringLanguage = languageHandlerInterface.getLanguageString(DefaultLanguageConstants.STRING_LANGUAGE);
        }
        setModal(true);
        mainPane = new CommonPanel();
        mainPane.setInsets(2, 3, 2, 3);
        setContentPane(mainPane);

        mainPane.addToGrid(new JLabel(AppUtils.getLabelCaption(sStringLanguage)), 0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        mainPane.addToGrid(clf_languages = new CommonLookupField(new FieldDefinition("", "", FieldType.CHAR), mgxFormat, this), 0, 1, 2, 1, 0.0, 0.0, GridBagConstraints.BOTH);
        mainPane.addToGrid(new JLabel(""), 0, 3, GridBagConstraints.REMAINDER, 1, 0.0, 1.0, GridBagConstraints.BOTH);

        mainPane.addToGrid(new JLabel(""), 4, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NONE);
        mainPane.addToGrid(jButtonYes = AppUtils.createButton(sStringYes, ACTION_YES, this), 4, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NONE, GridBagConstraints.CENTER);
        AppUtils.setButtonCaptionAndMnemonic(jButtonYes, sStringYes);
        mainPane.addToGrid(jButtonNo = AppUtils.createButton(sStringCancel, ACTION_NO, this), 4, 2, 1, 1, 1.0, 1.0, GridBagConstraints.NONE, GridBagConstraints.CENTER);
        AppUtils.setButtonCaptionAndMnemonic(jButtonNo, sStringCancel);
    }

    public int showLoginDialog(JFrame f, String sLanguage) {
        Vector<String> vAvailableLanguages = languageHandlerInterface.getAvailableLanguages();
        String sLookup = "";
        for (int i = 0; i < vAvailableLanguages.size(); i++) {
            sLookup += (sLookup.equals("") ? "" : "@") + vAvailableLanguages.elementAt(i) + "|" + languageHandlerInterface.getLanguageString(vAvailableLanguages.elementAt(i), DefaultLanguageConstants.STRING_LANGUAGE_NAME, vAvailableLanguages.elementAt(i));
        }
        clf_languages.setLookup(sLookup);
        clf_languages.setValue(sLanguage);

        setTitle(AppUtils.getCaption(sStringLanguageSelect));

        iAction = ESCAPE;
        pack();
        setLocationRelativeTo(f);
        clf_languages.requestFocus();
        setVisible(true);
        return (iAction);
    }

    public String getLanguage() {
        return (clf_languages.getValue().toString());
    }

    private void actionYES() {
        iAction = YES;
        //hide();
        setVisible(false);
    }

    private void actionNO() {
        iAction = NO;
        //hide();
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
            actionYES();
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            actionESCAPE();
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }
}
