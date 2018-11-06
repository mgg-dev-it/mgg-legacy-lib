package hu.mag.message;

/**
 *
 * @author MaG
 */
public interface MessageEventListener {
    
    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    public void messageEventPerformed(MessageEvent e);
}
