package hu.mgx.xml;

public class XMLAttribute {

    private String sName = null;
    private String sValue = null;

    public XMLAttribute(String sName, String sValue) {
        this.sName = sName;
        this.sValue = sValue;
    }

    public String getName() {
        return (this.sName);
    }

    public String getValue() {
        return (this.sValue);
    }

    public void setValue(String sValue) {
        this.sValue = sValue;
    }
}
