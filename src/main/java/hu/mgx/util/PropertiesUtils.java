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

        //v�gigmegy�nk a r�gieken - t�rlend�ket vagy m�dos�tand�kat keres�nk
        elements = propOld.propertyNames();
        while (elements.hasMoreElements()) {
            elements.nextElement();
            sName = elements.nextElement().toString();
            sValueOld = propOld.getProperty(sName, "");
            if (!propNew.containsKey(sName)) {
                //�j nem tartalmazza -> t�rlend�
            } else {
                //�j tartalmazza -> ellen�rzend�
                sValueNew = propNew.getProperty(sName, "");
                if (sValueNew.equals(sValueOld)) {
                    //nem v�ltozott az �rt�ke
                } else {
                    //v�ltozott az �rt�ke
                }
            }
            //++iElements;
        }

        //v�gigmegy�nk az �jakon - besz�rand�kat keres�nk
        elements = propNew.propertyNames();
        while (elements.hasMoreElements()) {
            elements.nextElement();
            sName = elements.nextElement().toString();
            if (!propOld.containsKey(sName)) {
                //r�gi nem tartalmazza -> besz�rand�
            }
        }
        return ("");
    }
}
