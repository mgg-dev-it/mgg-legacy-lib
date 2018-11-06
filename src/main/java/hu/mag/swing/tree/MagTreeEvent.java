package hu.mag.swing.tree;

/**
 *
 * @author MaG
 */
public class MagTreeEvent {

    private Object source = null;
    private int iEventID = 0;
    private String sItemIdentifier = "";

    /**
     * This flag indicates the event's character.
     */
    public static final int ITEM_SELECTED = 1;

    /**
     * Constructs a <code>MagTableEvent</code> object.
     * <p>
     * @param source The object that originated the event
     * @param iEventID An integer that identifies the event.
     * @param sItemIdentifier The item identifier.
     * @throws IllegalArgumentException if <code>source</code> is null
     * @see #getSource()
     * @see #getEventID()
     * @see #getItemIdentifier()
     */
    public MagTreeEvent(Object source, int iEventID, String sItemIdentifier) {
        this.source = source;
        this.iEventID = iEventID;
        this.sItemIdentifier = sItemIdentifier;
        if (source == null) {
            throw (new IllegalArgumentException("source parameter is null"));
        }
    }

    public Object getSource() {
        return (source);
    }

    public int getEventID() {
        return (iEventID);
    }

    public String getItemIdentifier() {
        return (sItemIdentifier);
    }

}
