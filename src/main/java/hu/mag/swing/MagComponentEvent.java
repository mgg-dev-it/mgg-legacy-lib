package hu.mag.swing;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;

/**
 *
 * @author MaG
 */
public class MagComponentEvent {

    private Object source = null;
    private int iEventID = 0;
    private ActionEvent actionEvent = null;
    private ItemEvent itemEvent = null;
    private KeyEvent keyEvent = null;

    /**
     * Event IDs.
     */
    public static final int NULL_EVENT = 0;
    public static final int CHANGED = 1;
    public static final int PUSHED = 2;
    public static final int LOSTFOCUS = 3;
    public static final int CLICKED = 4;
    public static final int DBLCLICKED = 5;
    public static final int KEY_PRESSED = 6;
    public static final int KEY_RELEASED = 7;
    public static final int KEY_TYPED = 8;
    public static final int GOTFOCUS = 9;
    //gotfocus, lostfocus, click, dblclick, keydown, keypress, keyup, mousedown, mousemove, mouseup, validate

    public static final int ACTION_OK = 101;
    public static final int ACTION_CANCEL = 102;
    public static final int ACTION_ESCAPE = 103;

    /**
     * Constructs a <code>MagComponentEvent</code> object.
     * <p>
     * @param source The object that originated the event
     * @param iEventID An integer that identifies the event.
     * @throws IllegalArgumentException if <code>source</code> is null
     * @see #getSource()
     * @see #getEventID()
     */
    public MagComponentEvent(Object source, int iEventID) {
        this.source = source;
        this.iEventID = iEventID;
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

    /**
     * @return the actionEvent
     */
    public ActionEvent getActionEvent() {
        return actionEvent;
    }

    /**
     * @param actionEvent the actionEvent to set
     */
    public void setActionEvent(ActionEvent actionEvent) {
        this.actionEvent = actionEvent;
    }

    /**
     * @return the itemEvent
     */
    public ItemEvent getItemEvent() {
        return itemEvent;
    }

    /**
     * @param itemEvent the itemEvent to set
     */
    public void setItemEvent(ItemEvent itemEvent) {
        this.itemEvent = itemEvent;
    }

    /**
     * @return the keyEvent
     */
    public KeyEvent getKeyEvent() {
        return keyEvent;
    }

    /**
     * @param keyEvent the keyEvent to set
     */
    public void setKeyEvent(KeyEvent keyEvent) {
        this.keyEvent = keyEvent;
    }

    @Override
    public String toString() {
        String sRetVal = "";
        if (source instanceof Component) {
            //sRetVal += ((Component) source).getName();
            sRetVal += ((Component) source).getClass().getName();
        } // else if ...
        sRetVal += " ";
        switch (iEventID) {
            case NULL_EVENT:
                sRetVal += "NULL_EVENT";
                break;
            case CHANGED:
                sRetVal += "CHANGED";
                break;
            case PUSHED:
                sRetVal += "PUSHED";
                break;
            default:
        }
        sRetVal += " ";
        if (itemEvent != null) {
            sRetVal += itemEvent.toString();
        }
        if (actionEvent != null) {
            sRetVal += actionEvent.toString();
        }
        return (sRetVal);
    }
}
