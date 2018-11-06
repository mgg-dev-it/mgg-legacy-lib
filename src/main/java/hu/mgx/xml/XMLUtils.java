package hu.mgx.xml;

public abstract class XMLUtils
{

    public static String getElementText(XMLElement xmlElement)
    {
        if (xmlElement == null)
        {
            return (null);
        }
        return (xmlElement.getText());
    }
}
