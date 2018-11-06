package hu.mag.swing;

import hu.mgx.swing.CommonPanel;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Toolkit;
import javax.swing.JComponent;
import javax.swing.JWindow;

/**
 *
 * @author MaG
 */
public class MagPopupWindow_Del extends JWindow {

    private JComponent ownerComponent;
    private CommonPanel content;

    public MagPopupWindow_Del(JComponent ownerComponent, Frame ownerFrame, CommonPanel content) {
        super(ownerFrame);
        this.ownerComponent=ownerComponent;
        this.content = content;
        setContentPane(content);
        setMinimumSize(new Dimension(20, 20));
        pack();
    }

    public void showPopup() {
//        int iPopupHeight = content.getBorder().getBorderInsets(content).top + content.getBorder().getBorderInsets(content).bottom;
//        int iPopupWidth = content.getBorder().getBorderInsets(content).left + content.getBorder().getBorderInsets(content).right;
        int iPopupHeight = content.getHeight();
        int iPopupWidth = content.getWidth();

        if (iPopupWidth < ownerComponent.getWidth()) {
            iPopupWidth = ownerComponent.getWidth();
        }
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());

        setPreferredSize(new Dimension(iPopupWidth, iPopupHeight));
        pack();

        int iHorizontalOffset = 0;
        if ((screenSize.width - screenInsets.right - iPopupWidth - ownerComponent.getLocationOnScreen().x) < 0) {
            iHorizontalOffset = screenSize.width - screenInsets.right - iPopupWidth - ownerComponent.getLocationOnScreen().x;
            //MaG 2017.07.27.
            if (ownerComponent.getLocationOnScreen().x + iHorizontalOffset < 0) {
                iHorizontalOffset = -1 * ownerComponent.getLocationOnScreen().x + screenInsets.left + 10;
            }
        }
        setLocation(ownerComponent.getLocationOnScreen().x + iHorizontalOffset, ownerComponent.getLocationOnScreen().y + ownerComponent.getHeight());

        setVisible(true);
    }

    public void hidePopup() {
        setVisible(false);
    }
}
