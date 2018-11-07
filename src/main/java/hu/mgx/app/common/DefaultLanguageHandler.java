package hu.mgx.app.common;

import java.util.Properties;
import java.util.Vector;

/**
 * An implementation of <code>LanguageHandlerInterface</code>
 *
 * @author MaG
 */
public class DefaultLanguageHandler implements LanguageHandlerInterface {

    private String sLanguage = "";
    private Vector vLanguages = null;
    private Properties pLanguage = null;
    private boolean bSubstitute = true;
    private boolean bMarkUndefined = true;
    private String sEnabledLanguages = "";

    public DefaultLanguageHandler() {
        init();
    }

    private void init() {
        clearLanguages();
        addLanguage("hu");
        setLanguageString(DefaultLanguageConstants.STRING_LANGUAGE_NAME, "Magyar");
        setLanguageString(DefaultLanguageConstants.STRING_YES, "&Igen");
        setLanguageString(DefaultLanguageConstants.STRING_NO, "&Nem");
        setLanguageString(DefaultLanguageConstants.STRING_CANCEL, "&Mégsem");
        setLanguageString(DefaultLanguageConstants.STRING_OK, "&OK");
        setLanguageString(DefaultLanguageConstants.STRING_LOGIN, "&Bejelentkezés");
        setLanguageString(DefaultLanguageConstants.STRING_USER_ID, "Felhasználó azonosító");
        setLanguageString(DefaultLanguageConstants.STRING_PASSWORD, "Jelszó");
        setLanguageString(DefaultLanguageConstants.STRING_PASSWORD_CHANGE, "&Jelszócsere");
        setLanguageString(DefaultLanguageConstants.STRING_OLD_PASSWORD, "Régi jelszó");
        setLanguageString(DefaultLanguageConstants.STRING_NEW_PASSWORD, "Új jelszó");
        setLanguageString(DefaultLanguageConstants.STRING_NEW_PASSWORD_AGAIN, "Új jelszó még egyszer");
        setLanguageString(DefaultLanguageConstants.STRING_LANGUAGE, "Nyelv");
        setLanguageString(DefaultLanguageConstants.STRING_LANGUAGE_SELECT, "Nyelv választás");
        setLanguageString(DefaultLanguageConstants.STRING_EXIT, "Kilépés");
        setLanguageString(DefaultLanguageConstants.STRING_APP_EXIT_QUESTION, "Bezárja az alkalmazást?");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_FILE, "&Fájl");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_NEW, "Új@ctrl N");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_OPEN, "Megnyitás");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_CLOSE, "Bezárás");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_SAVE, "Mentés");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_SAVEAS, "Mentés másként");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_PAGESETUP, "Oldalbeállítás");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_PRINTPREVIEW, "Nyomtatási kép");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_PRINT, "Nyomtatás");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_LOGIN, "&Bejelentkezés");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_PASSWORDCHANGE, "&Jelszócsere");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_EDIT, "Szerkesztés");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_CUT, "Kivágás");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_COPY, "Másolás");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_PASTE, "Beillesztés");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_OPTIONS, "&Beálllítások");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_LANGUAGE, "&Nyelv");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_WINDOW, "&Ablak");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_EXIT, "&Kilépés@alt X");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_HELP, "&Súgó@F1");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_ABOUT, "&Névjegy");

        setLanguageString(DefaultLanguageConstants.STRING_CAPTION_DETAILS, "Részletek");
        setLanguageString(DefaultLanguageConstants.STRING_CAPTION_LIST, "Lista");
        setLanguageString(DefaultLanguageConstants.STRING_CAPTION_ADD_NEW_RECORD, "Új rekord hozzáadása");
        setLanguageString(DefaultLanguageConstants.STRING_CAPTION_MODIFY_RECORD, "Rekord módosítása");
        setLanguageString(DefaultLanguageConstants.STRING_CAPTION_ADD, "Hozzáadás");
        setLanguageString(DefaultLanguageConstants.STRING_CAPTION_MODIFY, "Módosítás");
        setLanguageString(DefaultLanguageConstants.STRING_CAPTION_VIEW, "Megtekintés");
        //setLanguageString(DefaultLanguageConstants.STRING_CAPTION_DELETE, "Törlés");
        //setLanguageString(DefaultLanguageConstants.STRING_CAPTION_REFRESH, "Frissítés");
        setLanguageString(DefaultLanguageConstants.STRING_CAPTION_EXCEL, "Excel");
        setLanguageString(DefaultLanguageConstants.STRING_CAPTION_OK, "OK");
        //setLanguageString(DefaultLanguageConstants.STRING_CAPTION_CANCEL, "Mégsem");
        //setLanguageString(DefaultLanguageConstants.STRING_CAPTION_SAVE, "Mentés");

        setLanguageString(DefaultLanguageConstants.STRING_MESSAGE_ERROR, "Hiba");
        setLanguageString(DefaultLanguageConstants.STRING_MESSAGE_PASSWORD_CHANGE_SUCCESSFUL, "Jelszócsere sikerült.");
        setLanguageString(DefaultLanguageConstants.STRING_MESSAGE_PASSWORD_CHANGE_UNSUCCESSFUL, "Jelszócsere nem sikerúlt.");
        setLanguageString(DefaultLanguageConstants.STRING_MESSAGE_DELETE, "Törlés!");
        setLanguageString(DefaultLanguageConstants.STRING_MESSAGE_DELETE_REALLY, "Biztos benne, hogy törölni akarja a rekordot?");

        setLanguageString(DefaultLanguageConstants.STRING_CAPTION_PRPN_PORTRAIT, "Álló");
        setLanguageString(DefaultLanguageConstants.STRING_CAPTION_PRPN_LANDSCAPE, "Fekvő");
        setLanguageString(DefaultLanguageConstants.STRING_CAPTION_PRPN_MAGNIFY, " Nagyítás(%):");
        setLanguageString(DefaultLanguageConstants.STRING_CAPTION_PRPN_PAGE, " Oldal:");
        setLanguageString(DefaultLanguageConstants.STRING_CAPTION_PRPN_PRINT, "Nyomtatás");
        setLanguageString(DefaultLanguageConstants.STRING_CAPTION_PRPN_PAGE_SETUP, "Oldalbeállítás");

        addLanguage("ro");
        setLanguageString(DefaultLanguageConstants.STRING_LANGUAGE_NAME, "Román");
        setLanguageString(DefaultLanguageConstants.STRING_YES, "&Da");
        setLanguageString(DefaultLanguageConstants.STRING_NO, "&Nu");
        setLanguageString(DefaultLanguageConstants.STRING_CANCEL, "Revocare");
        setLanguageString(DefaultLanguageConstants.STRING_OK, "&OK");
        setLanguageString(DefaultLanguageConstants.STRING_LOGIN, "&Inregistrare");
        setLanguageString(DefaultLanguageConstants.STRING_USER_ID, "User ID");
        setLanguageString(DefaultLanguageConstants.STRING_PASSWORD, "Parola");
        setLanguageString(DefaultLanguageConstants.STRING_PASSWORD_CHANGE, "Schimbare parola");
        setLanguageString(DefaultLanguageConstants.STRING_OLD_PASSWORD, "Parola veche");
        setLanguageString(DefaultLanguageConstants.STRING_NEW_PASSWORD, "Parola noua");
        setLanguageString(DefaultLanguageConstants.STRING_NEW_PASSWORD_AGAIN, "Repetati parola");
        setLanguageString(DefaultLanguageConstants.STRING_LANGUAGE, "");
        setLanguageString(DefaultLanguageConstants.STRING_LANGUAGE_SELECT, "");
        setLanguageString(DefaultLanguageConstants.STRING_EXIT, "Iesire");
        setLanguageString(DefaultLanguageConstants.STRING_APP_EXIT_QUESTION, "Inchideti utilizarea?");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_FILE, "");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_NEW, "");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_OPEN, "");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_CLOSE, "");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_SAVE, "");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_SAVEAS, "");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_PAGESETUP, "");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_PRINTPREVIEW, "");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_PRINT, "");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_LOGIN, "");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_PASSWORDCHANGE, "Schimbare parola");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_EDIT, "");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_CUT, "");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_COPY, "");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_PASTE, "");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_OPTIONS, "");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_LANGUAGE, "");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_WINDOW, "");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_EXIT, "Iesire@alt X");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_HELP, "");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_ABOUT, "");

        setLanguageString(DefaultLanguageConstants.STRING_CAPTION_DETAILS, "Detalii");
        setLanguageString(DefaultLanguageConstants.STRING_CAPTION_LIST, "Lista");
        setLanguageString(DefaultLanguageConstants.STRING_CAPTION_ADD_NEW_RECORD, "Adaugare record non");
        setLanguageString(DefaultLanguageConstants.STRING_CAPTION_MODIFY_RECORD, "Modificare record");
        setLanguageString(DefaultLanguageConstants.STRING_CAPTION_ADD, "AdauMogare");
        setLanguageString(DefaultLanguageConstants.STRING_CAPTION_MODIFY, "Modificane");
        //setLanguageString(DefaultLanguageConstants.STRING_CAPTION_VIEW, "Megtekint�s");
        //setLanguageString(DefaultLanguageConstants.STRING_CAPTION_DELETE, "Stergere");
        //setLanguageString(DefaultLanguageConstants.STRING_CAPTION_REFRESH, "Aktualizare");
        setLanguageString(DefaultLanguageConstants.STRING_CAPTION_EXCEL, "Excel");
        setLanguageString(DefaultLanguageConstants.STRING_CAPTION_OK, "OK");
        //setLanguageString(DefaultLanguageConstants.STRING_CAPTION_CANCEL, "Revocare");
        //setLanguageString(DefaultLanguageConstants.STRING_CAPTION_SAVE, "Salvare");

        setLanguageString(DefaultLanguageConstants.STRING_MESSAGE_ERROR, "Error");
        //setLanguageString(DefaultLanguageConstants.STRING_MESSAGE_PASSWORD_CHANGE_SUCCESSFUL, "");
        setLanguageString(DefaultLanguageConstants.STRING_MESSAGE_PASSWORD_CHANGE_UNSUCCESSFUL, "Schimbare parola nereusita");
        setLanguageString(DefaultLanguageConstants.STRING_MESSAGE_DELETE, "Stergere!");
        setLanguageString(DefaultLanguageConstants.STRING_MESSAGE_DELETE_REALLY, "Sunteti sigur ca vreti sa stergeti recordul?");

        setLanguageString(DefaultLanguageConstants.STRING_CAPTION_PRPN_PORTRAIT, "Portrait");
        setLanguageString(DefaultLanguageConstants.STRING_CAPTION_PRPN_LANDSCAPE, "Landscape");
        setLanguageString(DefaultLanguageConstants.STRING_CAPTION_PRPN_MAGNIFY, " Marire(%):");
        setLanguageString(DefaultLanguageConstants.STRING_CAPTION_PRPN_PAGE, " Pagina:");
        setLanguageString(DefaultLanguageConstants.STRING_CAPTION_PRPN_PRINT, "Listare");
        setLanguageString(DefaultLanguageConstants.STRING_CAPTION_PRPN_PAGE_SETUP, "Reglare pagina");

        addLanguage("en");
        setLanguageString(DefaultLanguageConstants.STRING_LANGUAGE_NAME, "English");
        setLanguageString(DefaultLanguageConstants.STRING_YES, "&Yes");
        setLanguageString(DefaultLanguageConstants.STRING_NO, "&No");
        setLanguageString(DefaultLanguageConstants.STRING_CANCEL, "&Cancel");
        setLanguageString(DefaultLanguageConstants.STRING_OK, "&OK");
        setLanguageString(DefaultLanguageConstants.STRING_LOGIN, "&Login");
        setLanguageString(DefaultLanguageConstants.STRING_USER_ID, "Username");
        setLanguageString(DefaultLanguageConstants.STRING_PASSWORD, "Password");
        setLanguageString(DefaultLanguageConstants.STRING_PASSWORD_CHANGE, "&Password change");
        setLanguageString(DefaultLanguageConstants.STRING_OLD_PASSWORD, "Old password");
        setLanguageString(DefaultLanguageConstants.STRING_NEW_PASSWORD, "New password");
        setLanguageString(DefaultLanguageConstants.STRING_NEW_PASSWORD_AGAIN, "New password again");
        setLanguageString(DefaultLanguageConstants.STRING_LANGUAGE, "Language");
        setLanguageString(DefaultLanguageConstants.STRING_LANGUAGE_SELECT, "Language selection");
        setLanguageString(DefaultLanguageConstants.STRING_EXIT, "Exit");
        setLanguageString(DefaultLanguageConstants.STRING_APP_EXIT_QUESTION, "Close application?");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_FILE, "&File");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_NEW, "New@ctrl N");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_OPEN, "Open");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_CLOSE, "Close");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_SAVE, "Save ");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_SAVEAS, "Save as");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_PAGESETUP, "Page setup");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_PRINTPREVIEW, "Print preview");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_PRINT, "Print");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_LOGIN, "&Login");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_PASSWORDCHANGE, "&Password change");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_EDIT, "Edit");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_CUT, "Cut");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_COPY, "Copy");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_PASTE, "Paste");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_OPTIONS, "&Options");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_LANGUAGE, "&Language");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_WINDOW, "&Window");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_EXIT, "E&xit@alt X");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_HELP, "&Help@F1");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_ABOUT, "&About");

        addLanguage("de");
        setLanguageString(DefaultLanguageConstants.STRING_LANGUAGE_NAME, "Deutsch");
        setLanguageString(DefaultLanguageConstants.STRING_YES, "&Ja");
        setLanguageString(DefaultLanguageConstants.STRING_NO, "&Nein");
        setLanguageString(DefaultLanguageConstants.STRING_CANCEL, "&Abbrechen");
        setLanguageString(DefaultLanguageConstants.STRING_OK, "&OK");
        setLanguageString(DefaultLanguageConstants.STRING_LOGIN, "&Anmelden");
        setLanguageString(DefaultLanguageConstants.STRING_USER_ID, "Benutzername");
        setLanguageString(DefaultLanguageConstants.STRING_PASSWORD, "Passwort");
        setLanguageString(DefaultLanguageConstants.STRING_PASSWORD_CHANGE, "&Passwort wechsel");
        setLanguageString(DefaultLanguageConstants.STRING_OLD_PASSWORD, "Alt passwort");
        setLanguageString(DefaultLanguageConstants.STRING_NEW_PASSWORD, "Neu passwort");
        setLanguageString(DefaultLanguageConstants.STRING_NEW_PASSWORD_AGAIN, "Neu passwort noch einmal");
        setLanguageString(DefaultLanguageConstants.STRING_LANGUAGE, "Sprache");
        setLanguageString(DefaultLanguageConstants.STRING_LANGUAGE_SELECT, "Sprachwechsel");
        setLanguageString(DefaultLanguageConstants.STRING_EXIT, "Beenden");
        setLanguageString(DefaultLanguageConstants.STRING_APP_EXIT_QUESTION, "Beenden Anwendung?");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_FILE, "&Datei");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_NEW, "Neu@ctrl N");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_OPEN, "Open");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_CLOSE, "Close");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_SAVE, "Save ");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_SAVEAS, "Save as");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_PAGESETUP, "Page setup");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_PRINTPREVIEW, "Print preview");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_PRINT, "Print");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_LOGIN, "&Login");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_PASSWORDCHANGE, "&Password change");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_EDIT, "Edit");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_CUT, "Cut");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_COPY, "Copy");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_PASTE, "Paste");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_OPTIONS, "&Options");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_LANGUAGE, "&Sprache");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_WINDOW, "&Fenster");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_EXIT, "Be&enden@alt X");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_HELP, "&Hilfe@F1");
        setLanguageString(DefaultLanguageConstants.STRING_MENU_ABOUT, "&About");

        setLanguageStringXML_HU_RO("Ment�s", "Salvare"); //OK
        setLanguageStringXML_HU_RO("T�rl�s", "Stergere"); //OK
        setLanguageStringXML_HU_RO("M�gsem", "Revocare"); //OK
        setLanguageStringXML_HU_RO("Friss�t�s", "Actualizare"); //OK
    }

    @Override
    public void clearLanguages() {
        vLanguages = new Vector();
        System.gc();
    }

    @Override
    public void setLanguageSubstitute(boolean bSubstitute) {
        this.bSubstitute = bSubstitute;
    }

    @Override
    public boolean isLanguageSubstitute() {
        return bSubstitute;
    }

    @Override
    public void setMarkUndefined(boolean bMarkUndefined) {
        this.bMarkUndefined = bMarkUndefined;
    }

    @Override
    public boolean isMarkUndefined() {
        return (bMarkUndefined);
    }

    @Override
    public boolean addLanguage(String sLanguage) {
        if (sLanguage.equalsIgnoreCase("")) {
            return (false);
        }
        for (int i = 0; i < vLanguages.size(); i += 2) {
            if (vLanguages.elementAt(i).toString().equals(sLanguage)) {
                this.sLanguage = sLanguage;
                pLanguage = (Properties) (vLanguages.elementAt(i + 1));
                return (false);
            }
        }
        this.sLanguage = sLanguage;
        pLanguage = new Properties();
        vLanguages.addElement(sLanguage);
        vLanguages.addElement(pLanguage);
        return (true);
    }

    @Override
    public boolean setLanguage(String sLanguage) {
        for (int i = 0; i < vLanguages.size(); i += 2) {
            if (vLanguages.elementAt(i).toString().equals(sLanguage)) {
                this.sLanguage = sLanguage;
                pLanguage = (Properties) (vLanguages.elementAt(i + 1));
                return (true);
            }
        }
//        this.sLanguage = sLanguage;
//        pLanguage = new Properties();
//        vLanguages.addElement(sLanguage);
//        vLanguages.addElement(pLanguage);
        return (false);
    }

    @Override
    public String getLanguage() {
        return sLanguage;
    }

    @Override
    public void setEnabledLanguages(String sEnabledLanguages) {
        this.sEnabledLanguages = sEnabledLanguages;
    }

    @Override
    public Vector<String> getAvailableLanguages() {
        Vector<String> v = new Vector<String>();
        for (int i = 0; i < vLanguages.size(); i += 2) {
            if (sEnabledLanguages.equalsIgnoreCase("") || sEnabledLanguages.indexOf(vLanguages.elementAt(i).toString()) > -1) {
                v.add(vLanguages.elementAt(i).toString());
            }
        }
        return (v);
    }

    @Override
    public void setLanguageString(int iKey, String sValue) {
        setLanguageString(Integer.toString(iKey), sValue);
    }

    @Override
    public void setLanguageString(String sLanguage, int iKey, String sValue) {
        setLanguageString(sLanguage, Integer.toString(iKey), sValue);
    }

    @Override
    public String getLanguageString(int iKey) {
        return (getLanguageString(iKey, "???"));
    }

    @Override
    public String getLanguageString(int iKey, String sDefaultValue) {
        return (getLanguageString(Integer.toString(iKey), sDefaultValue));
    }

    @Override
    public String getLanguageString(String sLanguage, int iKey, String sDefaultValue) {
        return (getLanguageString(sLanguage, Integer.toString(iKey), sDefaultValue));
    }

    @Override
    public void setLanguageString(String sKey, String sValue) {
        pLanguage.setProperty(sKey, sValue);
    }

    @Override
    public void setLanguageString(String sLanguage, String sKey, String sValue) {
        if (setLanguage(sLanguage)) {
            pLanguage.setProperty(sKey, sValue);
        }
    }

    @Override
    public String getLanguageString(String sKey) {
        return (getLanguageString(sKey, (bMarkUndefined ? "{" : "") + sKey + (bMarkUndefined ? "}" : "")));
    }

    @Override
    public String getLanguageString(String sKey, String sDefaultValue) {
        if (!bSubstitute) //nem helyettes�ti m�s nyelven, ha nem tal�lja ...
        {
            return (pLanguage.getProperty(sKey, sDefaultValue));
        }
        String sImpossibleString = "@#&<f}]{>_!%=ge32423r32br23";
        String sRetVal = pLanguage.getProperty(sKey, sImpossibleString);
        //if (sRetVal.equals(sImpossibleString))
        if ((sRetVal.equals(sImpossibleString)) || (sRetVal.equals(""))) {
            Properties p = null;
            for (int i = 0; i < vLanguages.size(); i += 2) {
                if (sEnabledLanguages.equalsIgnoreCase("") || sEnabledLanguages.indexOf(vLanguages.elementAt(i).toString()) > -1) {
                    p = (Properties) (vLanguages.elementAt(i + 1));
                    sRetVal = p.getProperty(sKey, sImpossibleString);
                    if ((!sRetVal.equals(sImpossibleString)) && (!sRetVal.equals(""))) {
                        return ((bMarkUndefined ? "{" : "") + sRetVal + (bMarkUndefined ? "}" : ""));
                    }
                }
            }
            sRetVal = sDefaultValue;
        }
        return (sRetVal);
    }

    @Override
    public String getLanguageString(String sLanguage, String sKey, String sDefaultValue) {
        String sRetVal = "";
        String sOldLanguage = getLanguage();
        if (setLanguage(sLanguage)) {
            sRetVal = getLanguageString(sKey, sDefaultValue);
            setLanguage(sOldLanguage);
            return (sRetVal);
        }
        return (sDefaultValue);
    }

    private int setLanguageStringXMLItem(String sKey, String sXML) {
        int iItemStart = -1;
        int iItemStartClose = -1;
        int iItemEnd = -1;
        int iNextItem = -1;
        String sItem = "";
        String sLang = "";

        sXML = sXML.trim();
//        System.out.println(sXML);
        //kulcs:
        iItemStart = sXML.indexOf("<item ");
        iItemStartClose = sXML.indexOf(">", iItemStart);
        iItemEnd = sXML.indexOf("</item>");
//        System.out.println(Integer.toString(iItemStart));
//        System.out.println(Integer.toString(iItemStartClose));
//        System.out.println(Integer.toString(iItemEnd));
        if (iItemStart < 0) {
            return (-1); //hibakezel�s - nincs <item ...> elem
        }
        if (iItemStartClose < 0) {
            return (-1); //hibakezel�s - nincs <item ...> lez�r� elem
        }
        if (iItemEnd < 0) {
            return (-1); //hibakezel�s - nincs </item> elem
        }
        iItemStart = iItemStartClose + 1;
        if (iItemEnd <= iItemStart) {
            return (-1); //hibakezel�s - itemEnd <= itemStart
        }
        sItem = sXML.substring(iItemStart, iItemEnd);
        sLang = sXML.substring(iItemStartClose - 2, iItemStartClose);
//        System.out.println(sLang);
//        System.out.println(sItem);

        setLanguageString(sLang, sKey, sItem);

        iNextItem = iItemEnd + "</item>".length();
        return (iNextItem);
    }

    @Override
    public void setLanguageStringXML(String sXML) {
        int iKeyStart = -1;
        int iKeyEnd = -1;
        int iNextItem = -1;
        String sKey = "";

        sXML = sXML.trim();
        //kulcs:
        iKeyStart = sXML.indexOf("<key>");
        iKeyEnd = sXML.indexOf("</key>");
        if (iKeyStart < 0) {
            return; //hibakezel�s - nincs <key> elem
        }
        if (iKeyEnd < 0) {
            return; //hibakezel�s - nincs </key> elem
        }
        iKeyStart += iKeyStart + "<key>".length();
        if (iKeyEnd <= iKeyStart) {
            return; //hibakezel�s - keyEnd <= keyStart
        }
        sKey = sXML.substring(iKeyStart, iKeyEnd);
//        System.out.println("Key=" + sKey);
        //sz�vegek a k�l�nb�z� nyelveken:
        iNextItem = setLanguageStringXMLItem(sKey, sXML);
//        System.out.println(Integer.toString(iNextItem));
        while (iNextItem > -1) {
            sXML = sXML.substring(iNextItem);
            iNextItem = setLanguageStringXMLItem(sKey, sXML);
        }
    }

    public void setLanguageStringXML_HU(String sHU) {
        setLanguageStringXML("<key>" + sHU + "</key><item lang=hu>" + sHU + "</item><item lang=ro>{" + sHU + "}</item>");
    }

    public void setLanguageStringXML_HU_RO(String sHU, String sRO) {
        setLanguageStringXML("<key>" + sHU + "</key><item lang=hu>" + sHU + "</item><item lang=ro>" + sRO + "</item>");
    }

    public void setLanguageStringXML_key_HU_EN(String sKey, String sHU, String sEN) {
        setLanguageStringXML("<key>" + sKey + "</key><item lang=hu>" + sHU + "</item><item lang=en>" + sEN + "</item>");
    }
}
