package hu.mag.swing;

import java.awt.Dimension;
import javax.swing.JLabel;

/**
 *
 * @author MaG
 */
public class MagLabel extends JLabel {

    public MagLabel() {
        super();
    }

    public MagLabel(String sText) {
        super(sText);
    }

    public MagLabel(int iWidth, int iHeight) {
        this("",iWidth, iHeight);
    }

    public MagLabel(String sText,int iWidth, int iHeight) {
        this(sText);
        setFixSize(new Dimension(iWidth, iHeight));
    }

    public void setFixSize(java.awt.Dimension d) {
        setMinimumSize(d);
        setPreferredSize(d);
        setMaximumSize(d);
    }
}
