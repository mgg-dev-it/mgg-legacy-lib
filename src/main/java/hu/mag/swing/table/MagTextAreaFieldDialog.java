package hu.mag.swing.table;

import hu.mag.swing.MagButton;
import hu.mag.swing.MagTextAreaField;
import hu.mgx.app.swing.*;
import hu.mgx.swing.CommonPanel;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import hu.mgx.app.common.*;
import hu.mgx.util.StringUtils;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;

public class MagTextAreaFieldDialog extends JDialog implements ActionListener, KeyListener, WindowListener, LanguageEventListener {

    private CommonPanel mainPane;
    private MagButton mbSave;
    private MagButton mbCancel;

    private MagTextAreaField magTextAreaField;
    private JScrollPane jScrollPane;

    public static final int OK = 0;
    public static final int CANCEL = 1;
    public static final int ESCAPE = -1;
    private int iAction = ESCAPE;
    private final static String ACTION_OK = "ACTION_OK";
    private final static String ACTION_CANCEL = "ACTION_CANCEL";
    private SwingAppInterface swingAppInterface;

    public MagTextAreaFieldDialog(JFrame f, SwingAppInterface swingAppInterface) {
        super(f);
        this.swingAppInterface = swingAppInterface;
        init();
    }

    public MagTextAreaFieldDialog(Frame f, SwingAppInterface swingAppInterface) {
        super(f);
        this.swingAppInterface = swingAppInterface;
        init();
    }

    private void init() {
        setModal(true);
        setType(Window.Type.UTILITY);
        //setUndecorated(true);
//        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        mainPane = new CommonPanel();
        //mainPane.setInsets(5, 7, 5, 7);
        mainPane.setInsets(2, 2, 2, 2);
        //mainPane.setInsets(0, 0, 0, 0);
        setContentPane(mainPane);

        magTextAreaField = new MagTextAreaField(swingAppInterface);
        magTextAreaField.setClass(String.class);
        magTextAreaField.addKeyListener(this);
        magTextAreaField.setLineWrap(false);

        jScrollPane = new JScrollPane(magTextAreaField);
        jScrollPane.setPreferredSize(new Dimension(300, 300));

        mainPane.addToGrid(jScrollPane, 0, 0, 2, 1, 1.0, 1.0, GridBagConstraints.BOTH, GridBagConstraints.CENTER);

        mbSave = MagButton.createMagButton(swingAppInterface, MagButton.BUTTON_SAVE, this);
        mainPane.addToGrid(mbSave, 1, 0, 1, 1, 0.0, 1.0, GridBagConstraints.NONE, GridBagConstraints.EAST);

        mbCancel = MagButton.createMagButton(swingAppInterface, MagButton.BUTTON_CANCEL, this);
        mainPane.addToGrid(mbCancel, 1, 1, 1, 1, 0.0, 1.0, GridBagConstraints.NONE, GridBagConstraints.WEST);

        pack();
    }

    public int showDialog(Component c, String sTitle, String sText) {
        return (showDialog(c, sTitle, sText, CANCEL));
    }

    public int showDialog(Component c, String sTitle, String sText, int iDefaultButton) {
        setTitle(sTitle);
        magTextAreaField.setText(sText);
        magTextAreaField.setCaretPosition(0);
        jScrollPane.setPreferredSize(calcSize());
        pack();
        setLocationRelativeTo(c);
        iAction = ESCAPE;
        magTextAreaField.setCaretPosition(sText.length());
        magTextAreaField.requestFocus();
        setVisible(true);
        return (iAction);
    }

    private Dimension calcSize() {
        int iWidth = 200;
        int iHeight = 200;
        if (magTextAreaField == null) {
            return (new Dimension(iWidth, iHeight));
        }
        Font font = magTextAreaField.getFont();
        FontMetrics fm = magTextAreaField.getFontMetrics(font);

//        swingAppInterface.logLine(Integer.toString(magTextAreaField.getLineCount()));
        //iHeight = magTextAreaField.getLineCount() * (fm.getAscent() + fm.getDescent() + fm.getLeading());
        //iHeight = magTextAreaField.getLineCount() * (fm.getHeight() + 1);
        iHeight = new Double(magTextAreaField.getLineCount() * fm.getHeight() * 1.05).intValue();
        for (int iLine = 0; iLine < magTextAreaField.getLineCount(); iLine++) {
            try {
                String s = magTextAreaField.getText(magTextAreaField.getLineStartOffset(iLine), magTextAreaField.getLineEndOffset(iLine) - magTextAreaField.getLineStartOffset(iLine));
                s = StringUtils.stringReplace(s, StringUtils.sCr, "");
                s = StringUtils.stringReplace(s, StringUtils.sLf, "");
//                swingAppInterface.logLine(Integer.toString(iLine) + " " + s);
                iWidth = Math.max(iWidth, new Double(fm.stringWidth(s) * 1.05).intValue());
            } catch (BadLocationException ex) {
            }
        }
        //magTextAreaField.getLineEndOffset(OK)
        //fm.stringWidth(ACTION_OK);
        //fm.getHeight()
        //fm.getAscent()+fm.getDescent()+fm.getLeading()
        iWidth = Math.max(iWidth, 300);
        iHeight = Math.max(iHeight, 300);
        return (new Dimension(iWidth, iHeight));
    }

    public String getText() {
        return (magTextAreaField.getText());
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
