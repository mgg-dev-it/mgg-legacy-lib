package hu.mgx.app.common;

import hu.mgx.xml.XMLElement;
import java.io.File;

public interface XMLInterface
{

    public boolean readXMLFile(String sName, File file);

    public void writeXMLFile(String sName, File file);

    //element(attributes) ... content ... child elements
    public java.util.Vector<XMLElement> getElements(String sXMLName, String sElementName);

    //public XMLElement getElementByName(String sXMLName, String sElementName, String sAncestorName);
}
