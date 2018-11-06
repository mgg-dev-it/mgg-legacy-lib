package hu.mag.swing;

import static hu.mag.swing.MagButton.createMagButton;
import hu.mgx.app.common.LanguageEvent;
import hu.mgx.app.common.LanguageEventListener;
import hu.mgx.app.swing.SwingAppInterface;
import hu.mgx.swing.CommonPanel;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author MaG
 */
public class MagButtonPanel extends CommonPanel implements LanguageEventListener {

    private SwingAppInterface swingAppInterface;
    private Vector<MagButton> vMagButtons = new Vector<MagButton>();
    private Vector<Boolean> vEnabled = new Vector<Boolean>();
    private Vector<Boolean> vVisible = new Vector<Boolean>();

    public MagButtonPanel(SwingAppInterface swingAppInterface) {
        super();
        this.swingAppInterface = swingAppInterface;
    }

    public void addMagButton(MagButton magButton) {
        vMagButtons.add(magButton);
        vEnabled.add(new Boolean(true));
        vVisible.add(new Boolean(true));
        this.addToCurrentRow(magButton, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
    }

    public static MagButtonPanel createMagButtonPanelHorizontal(SwingAppInterface swingAppInterface, ActionListener actionListener, int iModifier, int... iButtons) {
        MagButtonPanel magButtonPanel = new MagButtonPanel(swingAppInterface);
        magButtonPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        magButtonPanel.setInsets(1, 1, 1, 1);

        for (int i = 0; i < iButtons.length; i++) {
            MagButton mb = createMagButton(swingAppInterface, iButtons[i], actionListener, iModifier);
            magButtonPanel.addMagButton(mb);
            //magButtonPanel.addToCurrentRow(mb, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        }
        magButtonPanel.addToCurrentRow(new JLabel(""), GridBagConstraints.REMAINDER, 1, 0.0, 1.0);
        return magButtonPanel;
    }

    public static MagButtonPanel createMagButtonPanelSelectHorizontal(SwingAppInterface swingAppInterface, ActionListener actionListener, int iModifier) {
        MagButtonPanel magButtonPanel = new MagButtonPanel(swingAppInterface);
        magButtonPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        magButtonPanel.setInsets(1, 1, 1, 1);

        int[] iButtons = {MagButton.BUTTON_SELECT_ALL, MagButton.BUTTON_SELECT_REVERSE, MagButton.BUTTON_SELECT_NONE};
        for (int i = 0; i < iButtons.length; i++) {
            MagButton mb = createMagButton(swingAppInterface, iButtons[i], actionListener, iModifier);
            magButtonPanel.addMagButton(mb);
            magButtonPanel.addToCurrentRow(mb, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        }
        magButtonPanel.addToCurrentRow(new JLabel(""), GridBagConstraints.REMAINDER, 1, 0.0, 1.0);
        return magButtonPanel;
    }

    @Override
    public void languageEventPerformed(LanguageEvent e) {
        for (int i = 0; i < vMagButtons.size(); i++) {
            vMagButtons.elementAt(i).languageEventPerformed(e);
        }
    }

    public void setButtonVisible(boolean bVisible, int iButtonType) {
        if (bVisible) {
            this.setVisible(true);
        }
        for (int i = 0; i < vMagButtons.size(); i++) {
            if (vMagButtons.elementAt(i).getButtonType() == iButtonType) {
                vMagButtons.elementAt(i).setVisible(bVisible);
                vVisible.setElementAt(new Boolean(bVisible), i);
            }
        }
    }

    public void setButtonVisible(boolean bVisible, MagButton magButton) {
        if (bVisible) {
            this.setVisible(true);
        }
        for (int i = 0; i < vMagButtons.size(); i++) {
            if (vMagButtons.elementAt(i) == magButton) {
                vMagButtons.elementAt(i).setVisible(bVisible);
                vVisible.setElementAt(new Boolean(bVisible), i);
            }
        }
    }

    public void setAllButtonVisible(boolean bVisible) {
        if (bVisible) {
            this.setVisible(true);
        }
        for (int i = 0; i < vMagButtons.size(); i++) {
            vMagButtons.elementAt(i).setVisible(bVisible);
            vVisible.setElementAt(new Boolean(bVisible), i);
        }
    }

    public void setEnabledAll(boolean bEnabled) {
        for (int i = 0; i < vMagButtons.size(); i++) {
            vMagButtons.elementAt(i).setEnabled(bEnabled);
            vEnabled.setElementAt(new Boolean(bEnabled), i);
        }
    }

    public void setEnabled(boolean bEnabled, int... iButtonTypes) {
        for (int i = 0; i < iButtonTypes.length; i++) {
            setButtonEnabled(bEnabled, iButtonTypes[i]);
        }
    }

    public void setButtonEnabled(boolean bEnabled, int iButtonType) {
        for (int i = 0; i < vMagButtons.size(); i++) {
            if (vMagButtons.elementAt(i).getButtonType() == iButtonType) {
                vMagButtons.elementAt(i).setEnabled(bEnabled);
                vEnabled.setElementAt(new Boolean(bEnabled), i);
            }
        }
    }

    public void setButtonEnabled(boolean bEnabled, MagButton magButton) {
        for (int i = 0; i < vMagButtons.size(); i++) {
            if (vMagButtons.elementAt(i) == magButton) {
                vMagButtons.elementAt(i).setEnabled(bEnabled);
                vEnabled.setElementAt(new Boolean(bEnabled), i);
            }
        }
    }

    @Override
    public void setEnabled(boolean bEnabled) {
        super.setEnabled(bEnabled);
        for (int i = 0; i < vMagButtons.size(); i++) {
            if (bEnabled) {
                vMagButtons.elementAt(i).setEnabled(vEnabled.elementAt(i).booleanValue());
            } else {
                vMagButtons.elementAt(i).setEnabled(false);
            }
        }
    }

    @Override
    public void setVisible(boolean bVisible) {
        super.setVisible(bVisible);
        for (int i = 0; i < vMagButtons.size(); i++) {
            if (bVisible) {
                vMagButtons.elementAt(i).setVisible(vVisible.elementAt(i).booleanValue());
            } else {
                vMagButtons.elementAt(i).setVisible(false);
            }
        }
    }
}
