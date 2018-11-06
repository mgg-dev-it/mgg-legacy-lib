package hu.mgx.app.swing;

import hu.mgx.swing.CommonPanel;
import hu.mgx.app.common.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

public class ButtonDialog extends JDialog implements ActionListener, KeyListener
{

    private CommonPanel mainPane;
    private JLabel jLabelMsg;
    public static final int ESCAPE = -1;
    private int iAction = ESCAPE;
    private Vector<JButton> vButtons = null;
    private Vector<Integer> vKeyCodes = null;

    public ButtonDialog(JFrame f, String[] sButtons)
    {
        super(f);
        init(sButtons);
    }

    public ButtonDialog(JDialog d, String[] sButtons)
    {
        super(d);
        init(sButtons);
    }

    public ButtonDialog(Component c, String[] sButtons)
    {
        super();
        init(sButtons);
    }

    private void init(String[] sButtons)
    {
        setModal(true);
        mainPane = new CommonPanel();
        mainPane.setInsets(5, 7, 5, 7);
        setContentPane(mainPane);

        mainPane.addToGrid(jLabelMsg = new JLabel(""), 0, 0, sButtons.length, 1, 1.0, 1.0, GridBagConstraints.NONE, GridBagConstraints.CENTER);
        jLabelMsg.setVerticalAlignment(SwingConstants.CENTER);
        jLabelMsg.setHorizontalAlignment(SwingConstants.CENTER);

        vButtons = new Vector<JButton>();
        vKeyCodes = new Vector<Integer>();
        for (int i = 0; i < sButtons.length; i++)
        {
            vButtons.add(AppUtils.createButton(sButtons[i], "Action" + Integer.toString(i), this));
            mainPane.addToGrid(vButtons.elementAt(i), 1, i, 1, 1, 1.0, 1.0, GridBagConstraints.NONE, GridBagConstraints.CENTER);
            AppUtils.setButtonCaptionAndMnemonic(vButtons.elementAt(i), sButtons[i]);
            vKeyCodes.add(new Integer(vButtons.elementAt(i).getMnemonic()));
            vButtons.elementAt(i).setFocusable(true);
            vButtons.elementAt(i).addKeyListener(this);
        }

        pack();
    }

    public int showDialog(Component c, String sTitle, String sMsg)
    {
        return (showDialog(c, sTitle, sMsg, 0));
    }

    public int showDialog(Component c, String sTitle, String sMsg, int iDefaultButton)
    {
        setTitle(sTitle);
        jLabelMsg.setText(sMsg);
        iAction = ESCAPE;
        pack();
        setLocationRelativeTo(c);
        vButtons.elementAt(iDefaultButton).requestFocus();
        setVisible(true);
        return (iAction);
    }

    private void actionESCAPE()
    {
        iAction = ESCAPE;
        setVisible(false);
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand().startsWith("Action"))
        {
            iAction = hu.mgx.util.StringUtils.intValue(e.getActionCommand().substring(6));
            setVisible(false);
        }
    }

    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            for (int i = 0; i < vButtons.size(); i++)
            {
                if (vButtons.elementAt(i).isFocusOwner())
                {
                    iAction = i;
                    setVisible(false);
                }
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            for (int i = 0; i < vButtons.size(); i++)
            {
                if (vButtons.elementAt(i).isFocusOwner())
                {
                    if (i > 0)
                    {
                        vButtons.elementAt(i - 1).requestFocus();
                    }
                    else
                    {
                        vButtons.elementAt(vButtons.size() - 1).requestFocus();
                    }
                }
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            for (int i = 0; i < vButtons.size(); i++)
            {
                if (vButtons.elementAt(i).isFocusOwner())
                {
                    if (i < vButtons.size() - 1)
                    {
                        vButtons.elementAt(i + 1).requestFocus();
                    }
                    else
                    {
                        vButtons.elementAt(0).requestFocus();
                    }
                }
            }
        }

        for (int i = 0; i < vButtons.size(); i++)
        {
            if (e.getKeyCode() == vKeyCodes.elementAt(i).intValue())
            {
                iAction = i;
                setVisible(false);
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
