package hu.mgx.xml;

import hu.mgx.app.common.*;
import hu.mgx.util.StringUtils;

import java.io.*;
import java.util.*;

public class DefaultXMLHandler implements XMLInterface {

    private HashMap<String, XMLElement> xmlElementRoots = null;
    private ErrorHandlerInterface errorHandlerInterface = null;

    public DefaultXMLHandler(ErrorHandlerInterface errorHandlerInterface) {
        this.errorHandlerInterface = errorHandlerInterface;
        xmlElementRoots = new HashMap<String, XMLElement>();
    }

    public boolean readXMLString(String sName, String sXML) {
        return (readXMLString(sName, sXML, "UTF8"));
    }

    public boolean readXMLString(String sName, String sXML, String sEncoding) {
        ByteArrayInputStream bais = null;
        try {
            bais = new ByteArrayInputStream(sXML.getBytes(sEncoding));
        } catch (UnsupportedEncodingException ex) {
            errorHandlerInterface.handleError(ex);
        }
        org.jdom.input.SAXBuilder saxBuilder = new org.jdom.input.SAXBuilder(false);
        try {
            org.jdom.Document document = saxBuilder.build(bais);
            org.jdom.Element elementRoot = document.getRootElement();
            XMLElement xmlElementRoot = new XMLElement(elementRoot.getName(), elementRoot.getTextTrim());
            xmlElementRoots.put(sName, xmlElementRoot);
            loadElement(xmlElementRoot, elementRoot);
        } catch (Exception ex) {
            errorHandlerInterface.handleError(ex);
            return (false);
        }
        return (true);
    }

    public boolean readXMLFile(String sName, java.io.File file, String sEncoding) {
        FileInputStream fis = null;
        InputStreamReader isr = null;
        org.jdom.input.SAXBuilder saxBuilder = new org.jdom.input.SAXBuilder(false);
        try {
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis, sEncoding);
            //org.jdom.Document document = saxBuilder.build(file);
            org.jdom.Document document = saxBuilder.build(isr);
            org.jdom.Element elementRoot = document.getRootElement();
            XMLElement xmlElementRoot = new XMLElement(elementRoot.getName(), elementRoot.getTextTrim());
            xmlElementRoots.put(sName, xmlElementRoot);
            loadElement(xmlElementRoot, elementRoot);
        } catch (Exception ex) {
            errorHandlerInterface.handleError(ex);
            return (false);
        }
        return (true);
    }

    public boolean readXMLFile(String sName, java.io.File file) {
        org.jdom.input.SAXBuilder saxBuilder = new org.jdom.input.SAXBuilder(false);
        try {
            org.jdom.Document document = saxBuilder.build(file);
            org.jdom.Element elementRoot = document.getRootElement();
            XMLElement xmlElementRoot = new XMLElement(elementRoot.getName(), elementRoot.getTextTrim());
            xmlElementRoots.put(sName, xmlElementRoot);
            loadElement(xmlElementRoot, elementRoot);
        } catch (Exception ex) {
            errorHandlerInterface.handleError(ex);
            return (false);
        }
        return (true);
    }

    private void loadElement(XMLElement xmlElement, org.jdom.Element e) {
        List listAttributes = e.getAttributes();
        Iterator<org.jdom.Attribute> iteratorListAttributes = listAttributes.iterator();
        for (int i = 0; iteratorListAttributes.hasNext(); ++i) {
            org.jdom.Attribute a = iteratorListAttributes.next();
            xmlElement.addAttribute(new XMLAttribute(a.getName(), a.getValue()));
        }
        XMLElement x = null;
        List listChildren = e.getChildren();
        Iterator<org.jdom.Element> iteratorListChildren = listChildren.iterator();
        for (int i = 0; iteratorListChildren.hasNext(); ++i) {
            org.jdom.Element c = iteratorListChildren.next();
            xmlElement.addChild(x = new XMLElement(c.getName(), c.getText()));
            loadElement(x, c);
        }
    }

    public void writeXMLFile(String sName, java.io.File file) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Vector<XMLElement> getElementsByAttribute(String sXMLName, String sElementName, String sAttributeName, String sAttributeValue) {
        XMLElement xmlElementRoot = xmlElementRoots.get(sXMLName);
        if (xmlElementRoot == null) {
            errorHandlerInterface.handleError(null, "XML név (" + sXMLName + ") nem található.", 0);
            return (null);
        }
        return (getElements(xmlElementRoot, sElementName, sAttributeName, sAttributeValue));
    }

    public XMLElement getFirstElementByAttribute(String sXMLName, String sElementName, String sAttributeName, String sAttributeValue) {
        XMLElement xmlElementRoot = xmlElementRoots.get(sXMLName);
        if (xmlElementRoot == null) {
            errorHandlerInterface.handleError(null, "XML név (" + sXMLName + ") nem található.", 0);
            return (null);
        }
        Vector<XMLElement> xmlElements = getElements(xmlElementRoot, sElementName, sAttributeName, sAttributeValue);
        if (xmlElements.size() > 0) {
            return (xmlElements.get(0));
        }
        return (null);
    }

    public XMLElement getFirstElementByAttribute(XMLElement xmlElement, String sElementName, String sAttributeName, String sAttributeValue) {
        Vector<XMLElement> xmlElements = getElements(xmlElement, sElementName, sAttributeName, sAttributeValue);
        if (xmlElements.size() > 0) {
            return (xmlElements.get(0));
        }
        return (null);
    }

    public Vector<XMLElement> getElements(String sXMLName, String sElementName) {
        XMLElement xmlElementRoot = xmlElementRoots.get(sXMLName);
        if (xmlElementRoot == null) {
            errorHandlerInterface.handleError(null, "XML név (" + sXMLName + ") nem található.", 0);
            return (null);
        }
        return (getElements(xmlElementRoot, sElementName));
    }

    private boolean hasAttribute(XMLElement xmlElement, String sAttributeName, String sAttributeValue) {
        Vector<XMLAttribute> vAttributes = xmlElement.getAttributes();
        for (int i = 0; i < vAttributes.size(); i++) {
            if (vAttributes.get(i).getName().equals(sAttributeName)) {
                if (sAttributeValue == null) {
                    return (true);
                }
                if (vAttributes.get(i).getValue().equals(sAttributeValue)) {
                    return (true);
                }
            }
        }
        return (false);
    }

    public XMLElement getRootElementByName(String sName) {
        return (xmlElementRoots.get(sName));
    }

    public XMLElement getFirstElement(XMLElement xmlElement, String sElementName) {
        Vector<XMLElement> xmlElements = getElements(xmlElement, sElementName, null, null);
        if (xmlElements.size() > 0) {
            return (xmlElements.get(0));
        }
        return (null);
    }

    public XMLElement getFirstElement(String sXMLName, String sElementName) {
        XMLElement xmlElementRoot = xmlElementRoots.get(sXMLName);
        if (xmlElementRoot == null) {
            errorHandlerInterface.handleError(null, "XML név (" + sXMLName + ") nem található.", 0);
            return (null);
        }
        Vector<XMLElement> xmlElements = getElements(xmlElementRoot, sElementName, null, null);
        if (xmlElements.size() > 0) {
            return (xmlElements.get(0));
        }
        return (null);
    }

    public Vector<XMLElement> getElements(XMLElement xmlElement) {
        return (getElements(xmlElement, null, null, null));
    }

    public Vector<XMLElement> getElements(XMLElement xmlElement, String sElementName) {
        return (getElements(xmlElement, sElementName, null, null));
    }

    public Vector<XMLElement> getElements(XMLElement xmlElement, String sElementName, boolean bRecursive) {
        return (getElements(xmlElement, sElementName, null, null, bRecursive));
    }

    public Vector<XMLElement> getElements(XMLElement xmlElement, String sElementName, String sAttributeName, String sAttributeValue) {
        return (getElements(xmlElement, sElementName, sAttributeName, sAttributeValue, true));
    }

    public Vector<XMLElement> getElements(XMLElement xmlElement, String sElementName, String sAttributeName, String sAttributeValue, boolean bRecursive) {
        Vector<XMLElement> returnList = new Vector<XMLElement>();
        //2013.09.12. if (xmlElement.getName().equals(sElementName)) {
        if (sElementName == null || xmlElement.getName().equals(StringUtils.isNull(sElementName, ""))) { //2013.09.12.
            if (sAttributeName == null) {
                returnList.add(xmlElement);
            } else {
                if (hasAttribute(xmlElement, sAttributeName, sAttributeValue)) {
                    returnList.add(xmlElement);
                }
            }
        }
        List listChildren = xmlElement.getChildren();
        Iterator<XMLElement> iteratorListChildren = listChildren.iterator();
        for (int i = 0; iteratorListChildren.hasNext(); ++i) {
            XMLElement childXMLElement = iteratorListChildren.next();
            if (bRecursive) {
                Vector<XMLElement> tmpList = getElements(childXMLElement, sElementName, sAttributeName, sAttributeValue); //recursion!
                for (int j = 0; j < tmpList.size(); j++) {
                    returnList.add(tmpList.elementAt(j));
                }
            } else {
                //2013.09.12. if (childXMLElement.getName().equals(sElementName))
                if (sElementName == null || childXMLElement.getName().equals(StringUtils.isNull(sElementName, ""))) //2013.09.12.
                {
                    if (sAttributeName == null) {
                        returnList.add(childXMLElement);
                    } else {
                        if (hasAttribute(childXMLElement, sAttributeName, sAttributeValue)) {
                            returnList.add(childXMLElement);
                        }
                    }
                }
            }
        }
        return (returnList);
    }

    public void debugListXML(String sXMLName) {
        XMLElement xmlElementRoot = xmlElementRoots.get(sXMLName);
        if (xmlElementRoot == null) {
            errorHandlerInterface.handleError(null, "XML név (" + sXMLName + ") nem található.", 0);
            return;
        }
        debugListElement(xmlElementRoot, 0);
    }

    private void debugListElement(XMLElement xmlElement, int iLevel) {
        String sIdent = hu.mgx.util.StringUtils.pad(" ", iLevel++ * 4);
        System.out.print(sIdent + "<" + xmlElement.getName());
        List listAttributes = xmlElement.getAttributes();
        Iterator<XMLAttribute> iteratorListAttributes = listAttributes.iterator();
        for (int i = 0; iteratorListAttributes.hasNext(); ++i) {
            XMLAttribute a = iteratorListAttributes.next();
            System.out.print(" " + a.getName() + "=\"" + a.getValue() + "\"");
        }
        System.out.print(">" + xmlElement.getText().trim());
        List listChildren = xmlElement.getChildren();
        if (listChildren.size() > 0) {
            System.out.println("");
            Iterator<XMLElement> iteratorListChildren = listChildren.iterator();
            for (int i = 0; iteratorListChildren.hasNext(); ++i) {
                XMLElement c = iteratorListChildren.next();
                debugListElement(c, iLevel);
            }
            System.out.print(sIdent);
        }
        System.out.println("</" + xmlElement.getName() + ">");
    }
}
