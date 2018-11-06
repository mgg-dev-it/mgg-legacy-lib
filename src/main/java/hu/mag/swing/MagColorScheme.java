package hu.mag.swing;

import java.awt.Color;
import java.util.HashMap;

/**
 *
 * @author MaG
 */
public class MagColorScheme {

    private String sName;
    private HashMap<String, Color> hmColors;

    public MagColorScheme(String sName) {
        this.sName = sName;
        hmColors = new HashMap<>();
    }

    public void addColor(String sName, Color color) {
        hmColors.put(sName, color);
    }

    public Color getColor(String sName) {
        return (hmColors.get(sName));
    }

    public boolean contains(String sName) {
        return (hmColors.containsKey(sName));
    }
}
