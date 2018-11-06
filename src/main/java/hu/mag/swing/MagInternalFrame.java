package hu.mag.swing;

import hu.mgx.app.common.CommonAppUtils;
import hu.mgx.app.swing.SwingAppInterface;
import hu.mgx.swing.CommonInternalFrame;
import hu.mgx.swing.CommonPanel;
import java.util.HashMap;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

/**
 *
 * @author MaG
 */
public class MagInternalFrame extends CommonInternalFrame {

    protected SwingAppInterface swingAppInterface;
    protected String sTitle;
    protected String[] args;
    protected HashMap<String, String> argsMap;
    protected CommonPanel mainPane;
    protected boolean bLoaded = false;

    public MagInternalFrame(String sTitle, SwingAppInterface swingAppInterface, String[] args) {
        super(sTitle);
        this.sTitle = sTitle;
        this.swingAppInterface = swingAppInterface;
        this.args = args;
        init(args);
    }

    private void init(String[] args) {
        argsMap = CommonAppUtils.preProcessArgs(args);
        mainPane = CommonPanel.createCommonPanel();
        setContentPane(mainPane);
        initializeMembers();
        createControls();
        createLayout();
        pack();
        setVisible(true);
        setMinimumSize(getSize());
        run();
        bLoaded = true;
    }

    protected void initializeMembers() {
    }

    protected void createControls() {
    }

    protected void createLayout() {
    }

    protected void run() {
    }

    public void repack() {
        pack();
        setMinimumSize(getSize());
    }

}
