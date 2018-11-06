package hu.mgx.util;

import hu.mgx.app.swing.SwingAppInterface;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Utility class for doing background processes in Swing.
 *
 * @todo: example!
 *
 * @author MaG
 */
public class BackgroundTask extends javax.swing.SwingWorker<Boolean, Void> implements PropertyChangeListener {

    private final int RESULT_OK = 1;
    private final int RESULT_FAILED = 2;

    private SwingAppInterface swingAppInterface;
    private int iProgressValue;
    private boolean bResult;

    /**
     * Creates a new BackgroundTask.
     *
     * @param swingAppInterface
     */
    public BackgroundTask(SwingAppInterface swingAppInterface) {
        super();
        this.swingAppInterface = swingAppInterface;
        this.iProgressValue = 10;
        this.bResult = false;
        this.addPropertyChangeListener(this);
    }

    @Override
    protected Boolean doInBackground() throws Exception {
        return (startTask());
    }

    @Override
    public void done() {
        try {
            bResult = get();
            if (bResult) {
                setProgress(RESULT_OK);
            } else {
                setProgress(RESULT_FAILED);
            }
        } catch (InterruptedException ie) {
            swingAppInterface.handleError(ie);
        } catch (java.util.concurrent.ExecutionException ee) {
            swingAppInterface.handleError(ee);
        }
    }

    /**
     * Call-back procedure. The running task should call it in order to give
     * information about progress change. The real progress is calculated and
     * displayed in the {@link #progressChanged} method, which should be
     * overwritten.
     */
//    public void setProgressValue() {
//        setProgress(iProgressValue);
//        iProgressValue = 21 - iProgressValue;
//    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            if (evt.getSource().equals(this)) {
                int iProgress = (Integer) evt.getNewValue();
                if (iProgress == RESULT_OK) {
                    endTaskOK();
                }
                if (iProgress == RESULT_FAILED) {
                    endTaskFailed();
                }
//                if (iProgress < 2) { // ready
//                    if (iProgress == RESULT_OK) {
//                        endTaskOK();
//                    } else {
//                        endTaskFailed();
//                    }
//                } else {
////                    progressChanged();
//                }
            }
        }
    }

    /**
     * This method should be overwritten in order to call the main method of the
     * process.
     *
     * @return
     */
    public boolean startTask() {
        return (true);
    }
//    public boolean startTask(BackgroundTask backgroundTask) {
//        return (true);
//    }

    /**
     * This method is called when the main task ends with OK result. This method
     * should be overwritten in order to catch the event.
     */
    public void endTaskOK() {
    }

    /**
     * This method is called when the main task ends with Failed result. This
     * method should be overwritten in order to catch the event.
     */
    public void endTaskFailed() {
    }

    /**
     * The in {@link #startTask(hu.mgx.util.BackgroundTask)} started method
     * calls {@link #setProgressValue()} method, which fires this method.
     */
//    public void progressChanged() {
//    }
}
