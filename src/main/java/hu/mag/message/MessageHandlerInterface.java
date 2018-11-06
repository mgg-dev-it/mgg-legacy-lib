package hu.mag.message;

/**
 *
 * @author MaG
 */
public interface MessageHandlerInterface {

    public abstract void handleMessage(MessageEvent e);

    public abstract void addMessageEventListener(MessageEventListener mel);

    public abstract void removeMessageEventListener(MessageEventListener mel);

}
