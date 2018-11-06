package hu.mag.swing.table;

/**
 *
 * @author MaG
 */
public class MagTableEvent {

    private Object source = null;
    private int iEventID = 0;
    private int iRow = 0;
    private int iCol = 0;
    private int iResponse = 0;

    /**
     * This flag indicates the event's character.
     */
    public static final int ROW_COL_CHANGED = 1;
    public static final int ROW_STATUS_CHANGED = 2;
    public static final int NEW_ROW_ADDED = 3;
    public static final int EDIT_BEGIN = 4;
    public static final int EDIT_END = 5;
    public static final int EDIT_CANCEL = 6;
    public static final int CLICK = 7;
    public static final int DBL_CLICK = 8;
    public static final int RIGHT_CLICK = 9;
    public static final int ROW_SAVED = 10;
    public static final int ROW_DELETED = 11;
    public static final int BEFORE_UPDATE = 101;
    public static final int AFTER_UPDATE = 102;
    public static final int BEFORE_INSERT = 103;
    public static final int AFTER_INSERT = 104;

    /**
     * Constructs a <code>MagTableEvent</code> object.
     * <p>
     * @param source The object that originated the event
     * @param iEventID An integer that identifies the event.
     * @param iRow Row
     * @param iCol Column
     * @throws IllegalArgumentException if <code>source</code> is null
     * @see #getSource()
     * @see #getEventID()
     * @see #getRow()
     * @see #getCol()
     */
    public MagTableEvent(Object source, int iEventID, int iRow, int iCol) {
        this.source = source;
        this.iEventID = iEventID;
        this.iRow = iRow;
        this.iCol = iCol;
        this.iResponse = 0;
        if (source == null) {
            throw (new IllegalArgumentException("source parameter is null"));
        }
    }

    @Override
    public String toString() {
        String s = getClass().getName() + " ";
        switch (iEventID) {
            case ROW_COL_CHANGED:
                s += "ROW_COL_CHANGED";
                break;
            case ROW_STATUS_CHANGED:
                s += "ROW_STATUS_CHANGED";
                break;
            case NEW_ROW_ADDED:
                s += "NEW_ROW_ADDED";
                break;
            case EDIT_BEGIN:
                s += "EDIT_BEGIN";
                break;
            case EDIT_END:
                s += "EDIT_END";
                break;
            case EDIT_CANCEL:
                s += "EDIT_CANCEL";
                break;
            case CLICK:
                s += "CLICK";
                break;
            case DBL_CLICK:
                s += "DBL_CLICK";
                break;
            case RIGHT_CLICK:
                s += "RIGHT_CLICK";
                break;
            case ROW_SAVED:
                s += "ROW_SAVED";
                break;
            case ROW_DELETED:
                s += "ROW_DELETED";
                break;
            case BEFORE_UPDATE:
                s += "BEFORE_UPDATE";
                break;
            case AFTER_UPDATE:
                s += "AFTER_UPDATE";
                break;
            case BEFORE_INSERT:
                s += "BEFORE_INSERT";
                break;
            case AFTER_INSERT:
                s += "AFTER_INSERT";
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

    public int getRow() {
        return (iRow);
    }

    public int getCol() {
        return (iCol);
    }

    public int getResponse() {
        return (iResponse);
    }

    public void setResponse(int iResponse) {
        this.iResponse = iResponse;
    }
}
