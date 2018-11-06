package hu.mgx.app.swing;

import hu.mgx.swing.CommonPanel;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import hu.mgx.app.common.*;
import hu.mgx.db.*;
import hu.mgx.swing.*;

public class TextInputDialog extends JDialog implements ActionListener, KeyListener
{

    private CommonPanel mainPane;
    private String sStringOK = "&OK";
    private String sStringCancel = "&Mégsem";
    private JButton jButtonOK;
    private JButton jButtonCancel;
    private CommonTextField commonTextField;
    public static final int OK = 0;
    public static final int CANCEL = 1;
    public static final int ESCAPE = -1;
    private int iAction = ESCAPE;
    private final static String ACTION_OK = "ACTION_OK";
    private final static String ACTION_CANCEL = "ACTION_CANCEL";
    private AppInterface appInterface;
    private LanguageHandlerInterface appModulLanguage = null;

    public TextInputDialog(JFrame f, AppInterface appInterface)
    {
        super(f);
        this.appInterface = appInterface;
        this.appModulLanguage = null;
        init();
    }

    public TextInputDialog(JFrame f, AppInterface appInterface, LanguageHandlerInterface appModulLanguage)
    {
        super(f);
        this.appInterface = appInterface;
        this.appModulLanguage = appModulLanguage;
        init();
    }

    private void init()
    {
        if (appModulLanguage != null)
        {
            sStringOK = appModulLanguage.getLanguageString(DefaultLanguageConstants.STRING_OK);
            sStringCancel = appModulLanguage.getLanguageString(DefaultLanguageConstants.STRING_CANCEL);
        }
        setModal(true);
        mainPane = new CommonPanel();
        mainPane.setInsets(5, 7, 5, 7);
        setContentPane(mainPane);

        mainPane.addToGrid(commonTextField = new CommonTextField(new FieldDefinition(" ", " ", hu.mgx.db.FieldType.CHAR, 255, 0, 0, false, false, false), appInterface), 0, 0, 2, 1, 1.0, 1.0, GridBagConstraints.NONE, GridBagConstraints.CENTER);
        commonTextField.setPreferredSize(new Dimension(300, 21));
        commonTextField.addKeyListener(this);

        mainPane.addToGrid(jButtonOK = AppUtils.createButton(sStringOK, ACTION_OK, this), 1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NONE, GridBagConstraints.EAST);
        AppUtils.setButtonCaptionAndMnemonic(jButtonOK, sStringOK);
        jButtonOK.getMnemonic();
        jButtonOK.setFocusable(true);
        jButtonOK.addKeyListener(this);

        mainPane.addToGrid(jButtonCancel = AppUtils.createButton(sStringCancel, ACTION_CANCEL, this), 1, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        AppUtils.setButtonCaptionAndMnemonic(jButtonCancel, sStringCancel);
        jButtonCancel.getMnemonic();
        jButtonCancel.setFocusable(true);
        jButtonCancel.addKeyListener(this);

        pack();
    }

    public int showDialog(Component c, String sTitle, String sText)
    {
        return (showDialog(c, sTitle, sText, CANCEL));
    }

    public int showDialog(Component c, String sTitle, String sText, int iDefaultButton)
    {
        setTitle(sTitle);
        commonTextField.setText(sText);
        setLocationRelativeTo(c);
        iAction = ESCAPE;
        pack();
        commonTextField.requestFocus();
        setVisible(true);
        return (iAction);
    }

    public String getText()
    {
        return (commonTextField.getText());
    }

    private void actionOK()
    {
        iAction = OK;
        setVisible(false);
    }

    private void actionCANCEL()
    {
        iAction = CANCEL;
        setVisible(false);
    }

    private void actionESCAPE()
    {
        iAction = ESCAPE;
        setVisible(false);
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand().equals(ACTION_OK))
        {
            actionOK();
        }
        if (e.getActionCommand().equals(ACTION_CANCEL))
        {
            actionCANCEL();
        }
    }

    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            if (commonTextField.isFocusOwner())
            {
                actionOK();
            }
            if (jButtonOK.isFocusOwner())
            {
                actionOK();
            }
            if (jButtonCancel.isFocusOwner())
            {
                actionCANCEL();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
            actionESCAPE();
        }
    }

    public void keyReleased(KeyEvent e)
    {
    }

    public void keyTyped(KeyEvent e)
    {
    }
}
