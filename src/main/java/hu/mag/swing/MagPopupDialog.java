package hu.mag.swing;

import hu.mgx.swing.CommonPanel;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;

/**
 *
 * @author MaG
 */
public class MagPopupDialog extends JDialog implements KeyListener, MagComponentEventListener {

    private JComponent ownerComponent;
    private Frame ownerFrame;
    private final CommonPanel content;
    public static final int OK = 0;
    public static final int CANCEL = 1;
    public static final int ESCAPE = -1;
    private int iAction = ESCAPE;
    private boolean bUndecorated = true;

    private ActionListener actionListener;

    public MagPopupDialog(JComponent ownerComponent, Frame ownerFrame, CommonPanel content) {
        super(ownerFrame);
        this.ownerFrame = ownerFrame;
        this.ownerComponent = ownerComponent;
        this.content = content;
        bUndecorated = true;
        init();
    }

    public MagPopupDialog(JComponent ownerComponent, Frame ownerFrame, CommonPanel content, boolean bUndecorated, String sTitle) {
        super(ownerFrame);
        this.ownerFrame = ownerFrame;
        this.ownerComponent = ownerComponent;
        this.content = content;
        this.bUndecorated = bUndecorated;
        this.setTitle(sTitle);
        init();
    }

    private void init() {
        //@todo na, ezt lenne jó megoldani!!! content.addMagComponentListener(this);
        setModal(true);
        //setType(Window.Type.NORMAL);
        //setType(Window.Type.UTILITY);
        //setType(Window.Type.POPUP);
        setUndecorated(bUndecorated);
        setContentPane(content);
        setMinimumSize(new Dimension(20, 20));


        actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hidePopup();
            }
        };
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        this.getRootPane().registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);


        pack();
    }

    public int showPopup(JComponent ownerComponent) {
        this.ownerComponent = ownerComponent;
        return (this.showPopup());
    }

    public int showPopup() {
        int iPopupHeight = getHeight();
        int iPopupWidth = getWidth();

//        if (iPopupWidth < ownerComponent.getWidth()) {
//            iPopupWidth = ownerComponent.getWidth();
//        }
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());

        setPreferredSize(new Dimension(iPopupWidth, iPopupHeight));
        pack();

        if (ownerComponent == null) {
            if (ownerFrame != null) {
                setLocation(ownerFrame.getLocationOnScreen().x + ownerFrame.getWidth() / 2 - this.getWidth() / 2, ownerFrame.getLocationOnScreen().y + ownerFrame.getHeight() / 2 - this.getHeight() / 2);
            }
        } else {
            int iHorizontalOffset = 0;
            if ((screenSize.width - screenInsets.right - iPopupWidth - ownerComponent.getLocationOnScreen().x) < 0) {
                iHorizontalOffset = screenSize.width - screenInsets.right - iPopupWidth - ownerComponent.getLocationOnScreen().x;
                //MaG 2017.07.27.
                if (ownerComponent.getLocationOnScreen().x + iHorizontalOffset < 0) {
                    iHorizontalOffset = -1 * ownerComponent.getLocationOnScreen().x + screenInsets.left + 10;
                }
            }
            setLocation(ownerComponent.getLocationOnScreen().x + iHorizontalOffset, ownerComponent.getLocationOnScreen().y + ownerComponent.getHeight());
        }

        setVisible(true);
        return (iAction);
    }

    public void hidePopup() {
        setVisible(false);
    }

    private void actionOK() {
        iAction = OK;
        //iSelectedRow = magTablePanel.getMagTable().getSelectedRow();
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

    public void keyPressed(KeyEvent e) {
//        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
//            //if (mbSave.isFocusOwner()) {
//            actionOK();
//            //}
//            //if (mbCancel.isFocusOwner()) {
//            actionCANCEL();
//            //}
//        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            actionESCAPE();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void componentEventPerformed(MagComponentEvent e) {
        if (e.getEventID() == MagComponentEvent.ACTION_OK) {
            actionOK();
        }
        if (e.getEventID() == MagComponentEvent.ACTION_CANCEL) {
            actionCANCEL();
        }
        if (e.getEventID() == MagComponentEvent.ACTION_ESCAPE) {
            actionESCAPE();
        }
    }

}
