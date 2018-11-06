package hu.mag.swing;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.Format;
import java.text.NumberFormat;
import javax.swing.JFormattedTextField;
import javax.swing.border.EmptyBorder;

public class MagFormattedTextField extends JFormattedTextField {

    private String s = "";

    public MagFormattedTextField() {
        super();
        init();
    }

    public MagFormattedTextField(Format format) {
        super(format);
        init();
    }

    private void init() {
        this.setBorder(new EmptyBorder(0, 0, 0, 0));
        addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                e.getComponent().setBackground(new Color(255, 255, 192));
                ((JFormattedTextField) e.getComponent()).selectAll();
                if (!s.equals("")) {
                    ((JFormattedTextField) e.getComponent()).setText(s);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                e.getComponent().setBackground(Color.WHITE);
            }
        });
    }

    public void setStringAfterFocusGained(String s) {
        this.s = s;
    }
}
