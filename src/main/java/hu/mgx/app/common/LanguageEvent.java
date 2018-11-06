package hu.mgx.app.common;

/**
 *
 * @author MaG
 */
public class LanguageEvent {

    private Object source = null;
    private int iEventID = 0;
    private String sLanguage = null;

    /**
     * This flag indicates that language has changed.
     */
    public static final int LANGUAGE_CHANGED = 1;

    /**
     * Constructs a <code>LanguageEvent</code> object.
     * <p>
     * @param source The object that originated the event
     * @param iEventID An integer that identifies the event.
     * @param sLanguage language name or code
     * @throws IllegalArgumentException if <code>source</code> is null
     * @see #getSource()
     * @see #getEventID()
     * @see #getLanguage()
     */
    public LanguageEvent(Object source, int iEventID, String sLanguage) {
        this.source = source;
        this.iEventID = iEventID;
        this.sLanguage = sLanguage;
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

    public String getLanguage() {
        return (sLanguage);
    }

}
