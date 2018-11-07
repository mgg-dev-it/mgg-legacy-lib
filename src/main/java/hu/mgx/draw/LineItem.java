/*
 * LineItem.java
 *
 * Created on 2004. december 9., 13:39
 */
package hu.mgx.draw;

/**
 *
 * @author gmagyar
 */
public class LineItem {

    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private boolean bDotted = false;

    /**
     * Creates a new instance of LineItem
     *
     * @param x1 x1
     * @param y1 y1
     * @param x2 x2
     * @param y2 y2
     */
    public LineItem(int x1, int y1, int x2, int y2) {
        this(x1, y1, x2, y2, false);
    }

    public LineItem(int x1, int y1, int x2, int y2, boolean bDotted) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.bDotted = bDotted;
    }

    public int getX1() {
        return (x1);
    }

    public int getY1() {
        return (y1);
    }

    public int getX2() {
        return (x2);
    }

    public int getY2() {
        return (y2);
    }

    public boolean isDotted() {
        return (bDotted);
    }
}
