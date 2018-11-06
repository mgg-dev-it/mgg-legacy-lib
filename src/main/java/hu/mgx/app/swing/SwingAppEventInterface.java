package hu.mgx.app.swing;

import javax.swing.event.InternalFrameEvent;

public interface SwingAppEventInterface {

    public boolean appBeforeExit();

    public boolean appExit();

    public void appInternalFrameClosed(InternalFrameEvent e);

}
