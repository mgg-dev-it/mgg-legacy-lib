package hu.mag.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JRadioButton;

/**
 *
 * @author MaG
 */
public class MagRadioButton extends JRadioButton {

    private Vector<MagComponentEventListener> vMagComponentEventListeners = new Vector<>();

    public MagRadioButton() {
        super();
        init();
    }

    public MagRadioButton(Icon icon) {
        super(icon);
        init();
    }

    public MagRadioButton(Action a) {
        super(a);
        init();
    }

    public MagRadioButton(Icon icon, boolean selected) {
        super(icon, selected);
        init();
    }

    public MagRadioButton(String text) {
        super(text);
        init();
    }

    public MagRadioButton(String text, boolean selected) {
        super(text, selected);
        init();
    }

    public MagRadioButton(String text, Icon icon) {
        super(text, icon);
        init();
    }

    public MagRadioButton(String text, Icon icon, boolean selected) {
        super(text, icon, selected);
        init();
    }

    private void init() {
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireMagComponentEventClicked();
            }
        });
    }

    public void addMagComponentEventListener(MagComponentEventListener mcel) {
        for (int i = 0; i < vMagComponentEventListeners.size(); i++) {
            if (vMagComponentEventListeners.elementAt(i).equals(mcel)) {
                return;
            }
        }
        vMagComponentEventListeners.add(mcel);
    }

    public void removeMagComponentEventListener(MagComponentEventListener mcel) {
        for (int i = 0; i < vMagComponentEventListeners.size(); i++) {
            if (vMagComponentEventListeners.elementAt(i).equals(mcel)) {
                vMagComponentEventListeners.remove(i);
            }
        }
    }

    private void fireMagComponentEventClicked() {
        for (int i = 0; i < vMagComponentEventListeners.size(); i++) {
            MagComponentEvent mce = new MagComponentEvent(this, MagComponentEvent.CLICKED);
            vMagComponentEventListeners.elementAt(i).componentEventPerformed(mce);
        }
    }

}
