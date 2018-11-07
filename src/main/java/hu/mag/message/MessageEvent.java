package hu.mag.message;

/**
 *
 * @author MaG
 */
public class MessageEvent {

    private Object source = null;
    private int iEventID = 0;
    private String sMessage = "";

    /**
     * This flag indicates the event's character.
     */
    public static final int MSG_MESSAGE = 1;

    /**
     * Constructs a <code>MessageEvent</code> object.
     * <p>
     * @param source The object that originated the event
     * @param iEventID An integer that identifies the event.
     * @param sMessage The message.
     * @throws IllegalArgumentException if <code>source</code> is null
     * @see #getSource()
     * @see #getEventID()
     */
    public MessageEvent(Object source, int iEventID, String sMessage) {
        this.source = source;
        this.iEventID = iEventID;
        this.sMessage = sMessage;
        if (source == null) {
            throw (new IllegalArgumentException("source parameter is null"));
        }
    }

    @Override
    public String toString() {
        String s = getClass().getName() + " ";
        switch (iEventID) {
            case MSG_MESSAGE:
                s += "MSG_MESSAGE";
                s += " ";
                s += sMessage;
                break;
            default:
                s += "?";
                break;
        }
        return (s);
    }

    public Object getSource() {
        return (source);
    }

    public int getEventID() {
        return (iEventID);
    }

    public String getMessage() {
        return (sMessage);
    }

}
