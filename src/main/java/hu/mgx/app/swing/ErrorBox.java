package hu.mgx.app.swing;

import hu.mgx.swing.CommonPanel;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class ErrorBox extends JDialog implements ActionListener, KeyListener
{

    private CommonPanel mainPane;
    private JButton jButtonClose;
    private JEditorPane jEditorPane;

    public ErrorBox(JFrame f)
    {
        super(f);
        init();
    }

    public void showError(JFrame f, String sTitle, String sErrMsg)
    {
        setTitle(sTitle);
        jEditorPane.setText(sErrMsg);
        setLocationRelativeTo(f);
        setVisible(true);
    }

    private void init()
    {
        setModal(true);
        mainPane = new CommonPanel();
        setContentPane(mainPane);
        //2011.05.16. jEditorPane = new JEditorPane("text/html", "");
        jEditorPane = new JEditorPane("text/plain", ""); //2011.05.16.
        jEditorPane.setEditable(false);
        jEditorPane.addKeyListener(this);
        JScrollPane jScrollPane = new JScrollPane(jEditorPane);
        jScrollPane.setMinimumSize(new Dimension(100, 100));
        jScrollPane.setPreferredSize(new Dimension(400, 200));
        mainPane.addToGrid(jScrollPane, 0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.BOTH);
        mainPane.addToGrid(jButtonClose = AppUtils.createButton("OK", "Close", this), 1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NONE, GridBagConstraints.CENTER);
        jButtonClose.addKeyListener(this);
        pack();
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand().equals("Close"))
        {
            setVisible(false);
        }
    }

    public void keyTyped(KeyEvent e)
    {
    }

    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
            e.consume();
            setVisible(false);
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            e.consume();
            setVisible(false);
        }
    }

    public void keyReleased(KeyEvent e)
    {
    }
}
