package hu.mag.swing;

import hu.mag.swing.text.DateTimeDocument;
import hu.mag.swing.text.StringDocument;
import hu.mgx.app.common.ColorManager;
import hu.mgx.app.swing.SwingAppInterface;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JEditorPane;
import javax.swing.UIManager;

public class MagEditorField extends JEditorPane implements MagFieldInterface {

    private SwingAppInterface swingAppInterface = null;
    private Object oOriginValue = null;
    protected Color colorNormal = ColorManager.inputForegroundNormal();
    protected Color colorChanged = ColorManager.inputForegroundChanged();
    private int iMaxLength = -1;
    private int iDecimals = -1;
    private boolean bUpperCase = false;
    private String sStringAfterFocusGained = "";
    private boolean bVkDelete = false;
    private boolean bVkBackSpace = false;
    private Class c = null;
    private DateTimeDocument dtd = null;

    public MagEditorField(SwingAppInterface swingAppInterface) {
        super();
        this.swingAppInterface = swingAppInterface;
        //this.setBorder(new EmptyBorder(0, 0, 0, 0));
        //this.setBorder(new LineBorder(Color.BLACK, 1));
        this.setBorder(UIManager.getBorder("TextField.border"));
        init();
    }

    public MagEditorField(SwingAppInterface swingAppInterface, Dimension d) {
        super();
        this.swingAppInterface = swingAppInterface;
        //this.setBorder(new EmptyBorder(0, 0, 0, 0));
        //this.setBorder(new LineBorder(Color.BLACK, 1));
        this.setBorder(UIManager.getBorder("TextField.border"));
        this.setMinimumSize(d);
        this.setPreferredSize(d);
        this.setMaximumSize(d);
        init();
    }

    private void init() {
        this.setMargin(new Insets(1, 1, 1, 1));
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                e.getComponent().setBackground(new Color(255, 255, 192));
//                ((JEditorPane) e.getComponent()).selectAll();
                if (!sStringAfterFocusGained.equals("")) {
                    ((JEditorPane) e.getComponent()).setText(sStringAfterFocusGained);
                    sStringAfterFocusGained = "";
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                e.getComponent().setBackground(Color.WHITE);
//                autoCompleteData();
            }
        });
        addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                bVkDelete = false;
                bVkBackSpace = false;
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    bVkDelete = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    bVkBackSpace = true;
                }
            }

            public void keyReleased(KeyEvent e) {
                color();
            }

            public void keyTyped(KeyEvent e) {
            }
        });
    }

    public void setStringAfterFocusGained(String s) {
        this.sStringAfterFocusGained = s;
    }

    public void setMaxLength(int iMaxLength) {
        this.iMaxLength = iMaxLength;
    }

    public void setDecimals(int iDecimals) {
        this.iDecimals = iDecimals;
    }

    public void setUpperCase(boolean bUpperCase) {
        this.bUpperCase = bUpperCase;
    }

    public void setClass(Class c) {
        this.c = c;
        this.setValue(null);
    }

    @Override
    public void setValue(Object value) {
        String s = "";
        oOriginValue = value;
        if (value == null) {
            setText("");
        }
        if (c == null) {
            c = value.getClass();
        }
        if (c.equals(String.class)) {
            setDocument(new StringDocument(iMaxLength, bUpperCase, true));
            if (value != null) {
                s = value.toString();
                if (iMaxLength > -1 && s.length() > iMaxLength) {
                    s = s.substring(0, iMaxLength);
                }
                if (bUpperCase) {
                    s = s.toUpperCase();
                }
                setText(s);
            }
            return;
        }
        if (value != null) {
            setText(value.toString());
        }
    }

    @Override
    public Object getValue() {
        if (c.equals(String.class)) {
            return (getText());
        }
        return (null);
    }

    public boolean isVkDelete() {
        return (bVkDelete);
    }

    public boolean isVkBackSpace() {
        return (bVkBackSpace);
    }

    @Override
    public boolean isChanged() {
        String sTmp = getText();

        if (oOriginValue == null) {
            return (!sTmp.equalsIgnoreCase(""));
        }
        if (c.equals(String.class)) {
            return (!oOriginValue.toString().equals(sTmp));
        }
        return (false);
    }

    private void color() {
        if (isChanged()) {
            setForeground(colorChanged);
        } else {
            setForeground(colorNormal);
        }
    }

    public void setForegroundColorNormal(Color c) {
        colorNormal = c;
        color();
    }

    public void setForegroundColorChanged(Color c) {
        colorChanged = c;
        color();
    }

    public void setActualToOrigin() {
        setValue(oOriginValue);
        color();
    }

    public void setOriginToActual() {
        oOriginValue = getValue();
        color();
    }

    public Object getOldValue() {
        return (oOriginValue);
    }

    public void formatChanged() {
        if (dtd != null) {
            dtd.formatChanged();
        }
    }

    @Override
    public void setReadOnly(boolean bReadOnly) {
        setEditable(!bReadOnly);
        setFocusable(!bReadOnly);
    }

}
