/*
 * XML fájlokat kezelõ interfész.
 * Egyszerre több fájlt is tud kezelni, a hozzájuk rendelt logikai nevek alapján.
 *
 *
 */
package hu.mgx.app.common;

import hu.mgx.xml.XMLElement;
import java.io.File;

public interface XMLInterface
{

    /*
     * Fájlból olvas be XML struktúrát, és logikai nevet rendel hozzá.
     */
    public boolean readXMLFile(String sName, File file);

    /*
     * Fájlba ír ki XML struktúrát, hozzárendelt logikai név alapján.
     */
    public void writeXMLFile(String sName, File file);

    //element(attributes) ... content ... child elements
    public java.util.Vector<XMLElement> getElements(String sXMLName, String sElementName);

    //public XMLElement getElementByName(String sXMLName, String sElementName, String sAncestorName);
}
