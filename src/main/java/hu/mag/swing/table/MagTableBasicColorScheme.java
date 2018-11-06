package hu.mag.swing.table;

import hu.mag.swing.MagColorScheme;
import java.awt.Color;

/**
 *
 * @author MaG
 */
public class MagTableBasicColorScheme extends MagColorScheme {

    public final static String CellBackgroundEnabledBase = "CellBackgroundEnabledBase";
    public final static String CellBackgroundEnabledBaseAlternate = "CellBackgroundEnabledBaseAlternate";
    public final static String CellBackgroundEnabledSelected = "CellBackgroundEnabledSelected";

    public final static String CellBackgroundDisabledBase = "CellBackgroundDisabledBase";
    public final static String CellBackgroundDisabledBaseAlternate = "CellBackgroundDisabledBaseAlternate";
    public final static String CellBackgroundDisabledSelected = "CellBackgroundDisabledSelected";

    public final static String CellBackgroundEnabledNew = "CellBackgroundEnabledNew";
    public final static String CellBackgroundEnabledNewModified = "CellBackgroundEnabledNewModified";
    public final static String CellBackgroundEnabledModified = "CellBackgroundEnabledModified";

    public final static String CellBackgroundEnabledPrimaryKey = "CellBackgroundEnabledPrimaryKey";
    public final static String CellBackgroundEnabledPrimaryKeySelected = "CellBackgroundEnabledPrimaryKeySelected";
    public final static String CellBackgroundEnabledPrimaryKeyNew = "CellBackgroundEnabledPrimaryKeyNew";
    public final static String CellBackgroundEnabledPrimaryKeyNewModified = "CellBackgroundEnabledPrimaryKeyNewModified";
    public final static String CellBackgroundEnabledPrimaryKeyModified = "CellBackgroundEnabledPrimaryKeyModified";

    public MagTableBasicColorScheme() {
        super("MagTableBasicColorScheme");
        super.addColor(CellBackgroundEnabledBase, new Color(208, 255, 208));
        super.addColor(CellBackgroundEnabledBaseAlternate, new Color(192, 255, 192));
        super.addColor(CellBackgroundEnabledSelected, new Color(128, 255, 128));

        super.addColor(CellBackgroundDisabledBase, new Color(240, 240, 240));
        super.addColor(CellBackgroundDisabledBaseAlternate, new Color(224, 224, 224));
        //super.addColor(CellBackgroundDisabledSelected, new Color(208, 208, 208));
        super.addColor(CellBackgroundDisabledSelected, new Color(192, 192, 192));

        super.addColor(CellBackgroundEnabledNew, new Color(224, 192, 255)); //violet
        super.addColor(CellBackgroundEnabledNewModified, new Color(255, 192, 224)); //rose
        super.addColor(CellBackgroundEnabledModified, new Color(255, 224, 192)); //orange

        super.addColor(CellBackgroundEnabledPrimaryKey, new Color(208, 255, 255)); //cyan
        super.addColor(CellBackgroundEnabledPrimaryKeySelected, new Color(176, 255, 255)); //cyan selected
        super.addColor(CellBackgroundEnabledPrimaryKeyNew, new Color(192, 224, 255)); //azure
        super.addColor(CellBackgroundEnabledPrimaryKeyNewModified, new Color(224, 192, 255)); //violet
        super.addColor(CellBackgroundEnabledPrimaryKeyModified, new Color(224, 255, 192)); //chartreuse green
    }

}
