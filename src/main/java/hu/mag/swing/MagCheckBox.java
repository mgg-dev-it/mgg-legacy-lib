package hu.mag.swing;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import hu.mgx.app.common.*;
import hu.mgx.util.StringUtils;
import java.util.Vector;

public class MagCheckBox extends JCheckBox implements MagFieldInterface {

    private Object oOriginValue = null;
    private Icon iconEnabledFocusGained = null;
    private Icon iconEnabledSelectedFocusGained = null;
    private Icon iconDisabledFocusGained = null;
    private Icon iconDisabledSelectedFocusGained = null;
    private Icon iconEnabledFocusLost = null;
    private Icon iconEnabledSelectedFocusLost = null;
    private Icon iconDisabledFocusLost = null;
    private Icon iconDisabledSelectedFocusLost = null;
    private Vector<MagComponentEventListener> vMagComponentEventListeners = new Vector<>();

    public MagCheckBox() {
        super();
        init();
    }

    public MagCheckBox(String text) {
        super(text);
        init();
    }

    public MagCheckBox(KeyListener keyListener) {
        super();
        init();
        if (keyListener != null) {
            this.addKeyListener(keyListener);
        }
        this.addItemListener(itemListener);
    }

    public MagCheckBox(ItemListener itemListener) {
        super();
        init();
        if (itemListener != null) {
            this.addItemListener(itemListener);
        }
    }

    private void setFocusGainedIcons() {
        this.setIcon(iconEnabledFocusGained);
        this.setSelectedIcon(iconEnabledSelectedFocusGained);
        this.setDisabledIcon(iconDisabledFocusGained);
        this.setDisabledSelectedIcon(iconDisabledSelectedFocusGained);
    }

    private void setFocusLostIcons() {
        this.setIcon(iconEnabledFocusLost);
        this.setSelectedIcon(iconEnabledSelectedFocusLost);
        this.setDisabledIcon(iconDisabledFocusLost);
        this.setDisabledSelectedIcon(iconDisabledSelectedFocusLost);
    }

    private void setIcons() {
        iconEnabledFocusGained = new MagCheckBoxIcon(ColorManager.inputBackgroundFocusGained(), false);
        iconEnabledSelectedFocusGained = new MagCheckBoxIcon(ColorManager.inputBackgroundFocusGained(), true);
        iconDisabledFocusGained = new MagCheckBoxIcon(ColorManager.inputBackgroundDisabled(), false); //impossible ...
        iconDisabledSelectedFocusGained = new MagCheckBoxIcon(ColorManager.inputBackgroundDisabled(), true); //impossible ...
        iconEnabledFocusLost = new MagCheckBoxIcon(ColorManager.inputBackgroundFocusLost(), false);
        iconEnabledSelectedFocusLost = new MagCheckBoxIcon(ColorManager.inputBackgroundFocusLost(), true);
        iconDisabledFocusLost = new MagCheckBoxIcon(ColorManager.inputBackgroundDisabled(), false);
        iconDisabledSelectedFocusLost = new MagCheckBoxIcon(ColorManager.inputBackgroundDisabled(), true);
    }

    private void init() {
        setIcons();
        //setBorder(new EmptyBorder(0, 0, 0, 0));
        setBorder(new EmptyBorder(1, 1, 1, 1));
        addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                fireMagComponentEvent(e);
            }
        });
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                e.getComponent().setBackground(ColorManager.inputBackgroundFocusGained());
                setFocusGainedIcons();
            }

            @Override
            public void focusLost(FocusEvent e) {
                e.getComponent().setBackground(ColorManager.inputBackgroundFocusLost());
                setFocusLostIcons();
            }
        });
        addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    transferFocus();
                }
            }

            public void keyReleased(KeyEvent e) {
            }

            public void keyTyped(KeyEvent e) {
            }
        });
        setHorizontalAlignment(SwingConstants.CENTER);
        setFocusLostIcons();
        //setBackground(ColorManager.inputBackgroundFocusLost());
    }

    public void setFocus() {
        requestFocus();
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            setBackground(ColorManager.inputBackgroundFocusLost());
        } else {
            setBackground(ColorManager.inputBackgroundDisabled());
        }
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

    private void fireMagComponentEvent(ItemEvent e) {
        for (int i = 0; i < vMagComponentEventListeners.size(); i++) {
            MagComponentEvent mce = new MagComponentEvent(this, MagComponentEvent.CHANGED);
            mce.setItemEvent(e);
            vMagComponentEventListeners.elementAt(i).componentEventPerformed(mce);
        }
    }

    @Override
    public boolean isChanged() {
        String s = StringUtils.isNull(oOriginValue, "0");
        Boolean b = new Boolean(s.equalsIgnoreCase("1") || s.equalsIgnoreCase("true") || s.equalsIgnoreCase("I") || s.equalsIgnoreCase("J"));
        return (b.compareTo(isSelected()) != 0);
    }

    @Override
    public void setValue(Object o) {
        oOriginValue = o;
        String s = StringUtils.isNull(o, "0");
        setSelected(s.equalsIgnoreCase("1") || s.equalsIgnoreCase("true") || s.equalsIgnoreCase("I") || s.equalsIgnoreCase("J"));
    }

    @Override
    public Object getValue() {
        return (new Boolean(isSelected()));
    }

    public Boolean getBooleanValue() {
        return (new Boolean(isSelected()));
    }

    @Override
    public void setReadOnly(boolean bReadOnly) {
        setEnabled(!bReadOnly);
        //setEditable(!bReadOnly);
        setFocusable(!bReadOnly);
    }

}
