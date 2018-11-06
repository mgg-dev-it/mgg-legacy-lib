/*
 * XML f�jlokat kezel� interf�sz.
 * Egyszerre t�bb f�jlt is tud kezelni, a hozz�juk rendelt logikai nevek alapj�n.
 *
 *
 */
package hu.mgx.app.common;

import hu.mgx.xml.XMLElement;
import java.io.File;

public interface XMLInterface
{

    /*
     * F�jlb�l olvas be XML strukt�r�t, �s logikai nevet rendel hozz�.
     */
    public boolean readXMLFile(String sName, File file);

    /*
     * F�jlba �r ki XML strukt�r�t, hozz�rendelt logikai n�v alapj�n.
     */
    public void writeXMLFile(String sName, File file);

    //element(attributes) ... content ... child elements
    public java.util.Vector<XMLElement> getElements(String sXMLName, String sElementName);

    //public XMLElement getElementByName(String sXMLName, String sElementName, String sAncestorName);
}
