package hu.mgx.xml;

import java.util.Vector;

public class XMLElement {

    private String sName = null;
    private Vector<XMLElement> vChildren = null;
    private Vector<XMLAttribute> vAttributes = null;
    private String sText = null;

    public XMLElement(String sName) {
        this(sName, "");
    }

    public XMLElement(String sName, String sText) {
        init(sName, sText);
    }

    private void init(String sName, String sText) {
        this.sName = sName;
        this.sText = sText;
        vChildren = new Vector<XMLElement>();
        vAttributes = new Vector<XMLAttribute>();
        sText = "";
    }

    public void addChild(XMLElement xmlElement) {
        vChildren.addElement(xmlElement);
    }

    public void addAttribute(XMLAttribute xmlAttribute) {
        vAttributes.addElement(xmlAttribute);
    }

    public void setText(String sText) {
        this.sText = sText;
    }

    public void setName(String sName) {
        this.sName = sName;
    }

    public Vector<XMLElement> getChildren() {
        return (vChildren);
    }

    public boolean hasChild() {
        //return (vChildren != null);
        return (vChildren.size() > 0);
    }

    public Vector<XMLAttribute> getAttributes() {
        return (vAttributes);
    }

    public String getAttribute(String sName) {
        for (int i = 0; i < vAttributes.size(); i++) {
            if (vAttributes.elementAt(i).getName().equals(sName)) {
                return (vAttributes.elementAt(i).getValue());
            }
        }
        return ("");
    }

    public boolean hasAttribute(String sName) {
        for (int i = 0; i < vAttributes.size(); i++) {
            if (vAttributes.elementAt(i).getName().equals(sName)) {
                return (true);
            }
        }
        return (false);
    }

    public void setAttribute(String sName, String sValue) {
        for (int i = 0; i < vAttributes.size(); i++) {
            if (vAttributes.elementAt(i).getName().equals(sName)) {
                vAttributes.elementAt(i).setValue(sValue);
            }
        }
    }

    public String getText() {
        return (this.sText);
    }

    public String getName() {
        return (this.sName);
    }
}
