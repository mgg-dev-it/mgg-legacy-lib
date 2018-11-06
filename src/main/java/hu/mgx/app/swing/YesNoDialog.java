package hu.mgx.app.swing;

import hu.mgx.swing.CommonPanel;
import hu.mgx.app.common.DefaultLanguageConstants;
import hu.mgx.app.common.LanguageHandlerInterface;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class YesNoDialog extends JDialog implements ActionListener, KeyListener {

    private CommonPanel mainPane;
    private String sStringYes = "&Igen";
    private String sStringNo = "&Nem";
    private int iKeyCodeYes = 0;
    private int iKeyCodeNo = 0;
    private JButton jButtonYes;
    private JButton jButtonNo;
    private JLabel jLabelMsg;
    public static final int YES = 0;
    public static final int NO = 1;
    public static final int ESCAPE = -1;
    private int iAction = ESCAPE;
    private final static String ACTION_YES = "Action_Yes";
    private final static String ACTION_NO = "Action_No";
    private LanguageHandlerInterface languageHandlerInterface = null;

    public YesNoDialog() {
        super();
        this.languageHandlerInterface = null;
        init();
    }

    public YesNoDialog(JFrame f) {
        this(f, null);
    }

    public YesNoDialog(JFrame f, LanguageHandlerInterface languageHandlerInterface) {
        super(f);
        this.languageHandlerInterface = languageHandlerInterface;
        init();
    }

    public YesNoDialog(Frame frame, LanguageHandlerInterface languageHandlerInterface) {
        super(frame);
        this.languageHandlerInterface = languageHandlerInterface;
        init();
    }

    public YesNoDialog(JDialog d) {
        this(d, null);
    }

    public YesNoDialog(JDialog d, LanguageHandlerInterface languageHandlerInterface) {
        super(d);
        this.languageHandlerInterface = languageHandlerInterface;
        init();
    }

    public YesNoDialog(Component c) {
        this(c, null);
    }

    public YesNoDialog(Component c, LanguageHandlerInterface languageHandlerInterface) {
        super();
        this.languageHandlerInterface = languageHandlerInterface;
        init();
    }

    private void init() {
        if (languageHandlerInterface != null) {
            sStringYes = languageHandlerInterface.getLanguageString(DefaultLanguageConstants.STRING_YES);
            sStringNo = languageHandlerInterface.getLanguageString(DefaultLanguageConstants.STRING_NO);
        }
        setModal(true);
        mainPane = new CommonPanel();
        mainPane.setInsets(5, 7, 5, 7);
        setContentPane(mainPane);

        mainPane.addToGrid(jLabelMsg = new JLabel(""), 0, 0, 2, 1, 1.0, 1.0, GridBagConstraints.NONE, GridBagConstraints.CENTER);
        jLabelMsg.setVerticalAlignment(SwingConstants.CENTER);
        jLabelMsg.setHorizontalAlignment(SwingConstants.CENTER);

        mainPane.addToGrid(jButtonYes = AppUtils.createButton(sStringYes, ACTION_YES, this), 1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NONE, GridBagConstraints.EAST);
        AppUtils.setButtonCaptionAndMnemonic(jButtonYes, sStringYes);
        iKeyCodeYes = jButtonYes.getMnemonic();
        jButtonYes.setFocusable(true);
        jButtonYes.addKeyListener(this);

        mainPane.addToGrid(jButtonNo = AppUtils.createButton(sStringNo, ACTION_NO, this), 1, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        AppUtils.setButtonCaptionAndMnemonic(jButtonNo, sStringNo);
        iKeyCodeNo = jButtonNo.getMnemonic();
        jButtonNo.setFocusable(true);
        //this.getRootPane().setDefaultButton(jButtonNo);
        jButtonNo.addKeyListener(this);

        pack();
    }

    public int showDialog(String sTitle, String sMsg) {
        return (showDialog(null, sTitle, sMsg, NO));
    }

    public int showDialog(Component c, String sTitle, String sMsg) {
        return (showDialog(c, sTitle, sMsg, NO));
    }

    public int showDialog(Component c, String sTitle, String sMsg, int iDefaultButton) {
        setTitle(sTitle);
        jLabelMsg.setText(sMsg);
        iAction = ESCAPE;
        pack();
        setLocationRelativeTo(c);
        if (iDefaultButton == YES) {
            jButtonYes.requestFocus();
        }
        if (iDefaultButton == NO) {
            jButtonNo.requestFocus();
        }
        setVisible(true);
        return (iAction);
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
            if (jButtonYes.isFocusOwner()) {
                actionYES();
            }
            if (jButtonNo.isFocusOwner()) {
                actionNO();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            jButtonYes.requestFocus();
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            jButtonNo.requestFocus();
        }
        if (e.getKeyCode() == iKeyCodeYes) {
            actionYES();
        }
        if (e.getKeyCode() == iKeyCodeNo) {
            actionNO();
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
