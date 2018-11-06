/*
 * RectangleItem.java
 *
 * Created on 2004. december 9., 13:37
 */
package hu.mgx.draw;

/**
 *
 * @author gmagyar
 */
public class RectangleItem {

    private int x;
    private int y;
    private int width;
    private int height;

    /**
     * Creates a new instance of RectangleItem
     *
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public RectangleItem(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return (x);
    }

    public int getY() {
        return (y);
    }

    public int getWidth() {
        return (width);
    }

    public int getHeight() {
        return (height);
    }
}
