package hu.mgx.util;

import java.util.Enumeration;
import java.util.Properties;

public abstract class PropertiesUtils {

    private PropertiesUtils() {
    }

    public static String compareProperties(Properties propOld, Properties propNew) {
        Enumeration elements;
        String sName;
        String sValueOld;
        String sValueNew;

        //végigmegyünk a régieken - törlendõket vagy módosítandókat keresünk
        elements = propOld.propertyNames();
        while (elements.hasMoreElements()) {
            elements.nextElement();
            sName = elements.nextElement().toString();
            sValueOld = propOld.getProperty(sName, "");
            if (!propNew.containsKey(sName)) {
                //új nem tartalmazza -> törlendõ
            } else {
                //új tartalmazza -> ellenõrzendõ
                sValueNew = propNew.getProperty(sName, "");
                if (sValueNew.equals(sValueOld)) {
                    //nem változott az értéke
                } else {
                    //változott az értéke
                }
            }
            //++iElements;
        }

        //végigmegyünk az újakon - beszúrandókat keresünk
        elements = propNew.propertyNames();
        while (elements.hasMoreElements()) {
            elements.nextElement();
            sName = elements.nextElement().toString();
            if (!propOld.containsKey(sName)) {
                //régi nem tartalmazza -> beszúrandó
            }
        }
        return ("");
    }
}
