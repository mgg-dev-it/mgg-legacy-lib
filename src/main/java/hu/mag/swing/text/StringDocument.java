package hu.mag.swing.text;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class StringDocument extends PlainDocument {

    private int iMaxLength = 0;
    private boolean bUpperCase;
    private boolean bMultiline;
    private String sAllowedCharacters = "";

    public StringDocument(int iMaxLength, boolean bUpperCase) {
        this(iMaxLength, bUpperCase, false, "");
    }

    public StringDocument(int iMaxLength, boolean bUpperCase, boolean bMultiline) {
        this(iMaxLength, bUpperCase, bMultiline, "");
    }

    public StringDocument(int iMaxLength, boolean bUpperCase, String sAllowedCharacters) {
        this(iMaxLength, bUpperCase, false, sAllowedCharacters);
    }

    public StringDocument(int iMaxLength, boolean bUpperCase, boolean bMultiline, String sAllowedCharacters) {
        super();
        this.iMaxLength = iMaxLength;
        this.bUpperCase = bUpperCase;
        this.bMultiline = bMultiline;
        this.sAllowedCharacters = sAllowedCharacters;
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if (str == null) {
            return;
        }
        char[] source = str.toCharArray();
        char[] result = new char[source.length];
        int j = 0;

        for (int i = 0; i < result.length; i++) {
            //if (getLength() < iMaxLength) {
            //if (sAllowedCharacters.equalsIgnoreCase("") || StringUtils.containsAllowedCharactersOrEmpty(source[i], sAllowedCharacters)) {
            //if (sAllowedCharacters.equalsIgnoreCase("") || sAllowedCharacters.indexOf(source[i]) != -1) {
            //if (iMaxLength < 0 || getLength() < iMaxLength) {
            if ((iMaxLength < 0 || getLength() < iMaxLength) && (sAllowedCharacters.equalsIgnoreCase("") || sAllowedCharacters.indexOf(source[i]) != -1)) {
                //result[j++] = (bUpperCase ? Character.toUpperCase(source[i]) : source[i]);
                //if (source[i] > 31) {
                if (source[i] > 31 || (bMultiline && (source[i] == 10 || source[i] == 13))) {
                    result[j++] = (bUpperCase ? Character.toUpperCase(source[i]) : source[i]);
                }
            } else {
                java.awt.Toolkit.getDefaultToolkit().beep();
            }
        }
        super.insertString(offs, new String(result, 0, j), a);
    }

    public void setAllowedCharacters(String sAllowedCharacters) {
        this.sAllowedCharacters = sAllowedCharacters;
    }

    public String getAllowedCharacters() {
        return (sAllowedCharacters);
    }

}
