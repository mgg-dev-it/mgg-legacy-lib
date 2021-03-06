package hu.mag.message;

/**
 *
 * @author MaG
 */
public interface MessageEventListener {
    
    /**
     * Invoked when an action occurs.
     *
     * @param e e
     */
    public void messageEventPerformed(MessageEvent e);
}
