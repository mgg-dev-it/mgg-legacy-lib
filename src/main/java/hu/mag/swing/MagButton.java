package hu.mag.swing;

import hu.mgx.app.common.LanguageEvent;
import hu.mgx.app.common.LanguageEventListener;
import hu.mgx.app.swing.SwingAppInterface;
import java.awt.Event;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 *
 * @author MaG
 */
public class MagButton extends JButton implements LanguageEventListener {

    private SwingAppInterface swingAppInterface;
    private int iButtonType = 0;

    public static final int BUTTON_OK = 1;
    public static final int BUTTON_CANCEL = 2;
    public static final int BUTTON_SAVE = 3;
    public static final int BUTTON_YES = 4;
    public static final int BUTTON_NO = 5;

    public static final int BUTTON_REFRESH = 101;
    public static final int BUTTON_NEW = 102;
    public static final int BUTTON_EDIT = 103;
    public static final int BUTTON_DELETE = 104;
    public static final int BUTTON_EXCEL = 105;
    public static final int BUTTON_DETAILS = 106;

    public static final int BUTTON_SELECT_ALL = 201;
    public static final int BUTTON_SELECT_REVERSE = 202;
    public static final int BUTTON_SELECT_NONE = 203;

    public static final int BUTTON_PDF_VIEW = 301;
    public static final int BUTTON_PDF_PRINT = 302;
    //public static final int BUTTON_PDF_REPORT = 303;

    private Vector<MagComponentEventListener> vMagComponentEventListeners = new Vector<>();

    public MagButton(String sText, SwingAppInterface swingAppInterface) {
        super(sText);
        this.swingAppInterface = swingAppInterface;
        init();
    }

    private void init() {
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireMagComponentEvent(e);
            }
        });
        String sXMLConfig = "<?xml version='1.0' encoding='ISO-8859-2'?>";
        sXMLConfig += "<!-- Comment -->";
        sXMLConfig += "<app name='app' major='0' minor='0' revision='0' width='800' height='600'>";
        sXMLConfig += "    <language>";
        sXMLConfig += "        <languageitem key='MagButton " + Integer.toString(BUTTON_OK) + "'>";
        sXMLConfig += "            <translation lang='hu'>Ok</translation>";
        sXMLConfig += "            <translation lang='en'>Ok</translation>";
        sXMLConfig += "            <translation lang='de'>Ok</translation>";
        sXMLConfig += "        </languageitem>";
        sXMLConfig += "        <languageitem key='MagButton " + Integer.toString(BUTTON_CANCEL) + "'>";
        sXMLConfig += "            <translation lang='hu'>Mégsem</translation>";
        sXMLConfig += "            <translation lang='en'>Cancel</translation>";
        sXMLConfig += "            <translation lang='de'>Abbrechen</translation>";
        sXMLConfig += "        </languageitem>";
        sXMLConfig += "        <languageitem key='MagButton " + Integer.toString(BUTTON_SAVE) + "'>";
        sXMLConfig += "            <translation lang='hu'>Mentés</translation>";
        sXMLConfig += "            <translation lang='en'>Save</translation>";
        sXMLConfig += "            <translation lang='de'>Speichern</translation>";
        sXMLConfig += "        </languageitem>";
        sXMLConfig += "        <languageitem key='MagButton " + Integer.toString(BUTTON_YES) + "'>";
        sXMLConfig += "            <translation lang='hu'>Igen</translation>";
        sXMLConfig += "            <translation lang='en'>Yes</translation>";
        sXMLConfig += "            <translation lang='de'>Ja</translation>";
        sXMLConfig += "        </languageitem>";
        sXMLConfig += "        <languageitem key='MagButton " + Integer.toString(BUTTON_NO) + "'>";
        sXMLConfig += "            <translation lang='hu'>Nem</translation>";
        sXMLConfig += "            <translation lang='en'>No</translation>";
        sXMLConfig += "            <translation lang='de'>Nein</translation>";
        sXMLConfig += "        </languageitem>";
        sXMLConfig += "        <languageitem key='MagButton " + Integer.toString(BUTTON_REFRESH) + "'>";
        sXMLConfig += "            <translation lang='hu'>Frissítés</translation>";
        sXMLConfig += "            <translation lang='en'>Refresh</translation>";
        sXMLConfig += "            <translation lang='de'>Aktualisieren</translation>";
        sXMLConfig += "        </languageitem>";
        sXMLConfig += "        <languageitem key='MagButton " + Integer.toString(BUTTON_NEW) + "'>";
        sXMLConfig += "            <translation lang='hu'>Új</translation>";
        sXMLConfig += "            <translation lang='en'>New</translation>";
        sXMLConfig += "            <translation lang='de'>Neu</translation>";
        sXMLConfig += "        </languageitem>";
        sXMLConfig += "        <languageitem key='MagButton " + Integer.toString(BUTTON_EDIT) + "'>";
        sXMLConfig += "            <translation lang='hu'>Szerkesztés</translation>";
        sXMLConfig += "            <translation lang='en'>Edit</translation>";
        sXMLConfig += "            <translation lang='de'>Bearbeiten</translation>";
        sXMLConfig += "        </languageitem>";
        sXMLConfig += "        <languageitem key='MagButton " + Integer.toString(BUTTON_DELETE) + "'>";
        sXMLConfig += "            <translation lang='hu'>Törlés</translation>";
        sXMLConfig += "            <translation lang='en'>Delete</translation>";
        sXMLConfig += "            <translation lang='de'>L�schen</translation>";
        sXMLConfig += "        </languageitem>";
        sXMLConfig += "        <languageitem key='MagButton " + Integer.toString(BUTTON_EXCEL) + "'>";
        sXMLConfig += "            <translation lang='hu'>Excel export</translation>";
        sXMLConfig += "            <translation lang='en'>Excel</translation>";
        sXMLConfig += "            <translation lang='de'>Excel</translation>";
        sXMLConfig += "        </languageitem>";
        sXMLConfig += "        <languageitem key='MagButton " + Integer.toString(BUTTON_DETAILS) + "'>";
        sXMLConfig += "            <translation lang='hu'>Részletek</translation>";
        sXMLConfig += "            <translation lang='en'>Details</translation>";
        sXMLConfig += "            <translation lang='de'>Detail</translation>";
        sXMLConfig += "        </languageitem>";
        sXMLConfig += "        <languageitem key='MagButton " + Integer.toString(BUTTON_SELECT_ALL) + "'>";
        sXMLConfig += "            <translation lang='hu'>Mindegyik</translation>";
        sXMLConfig += "            <translation lang='en'>All</translation>";
        sXMLConfig += "            <translation lang='de'>Alle</translation>";
        sXMLConfig += "        </languageitem>";
        sXMLConfig += "        <languageitem key='MagButton " + Integer.toString(BUTTON_SELECT_REVERSE) + "'>";
        sXMLConfig += "            <translation lang='hu'>Kijelölés megfordítása</translation>";
        sXMLConfig += "            <translation lang='en'>Reverse</translation>";
        sXMLConfig += "            <translation lang='de'>Umkehren</translation>";
        sXMLConfig += "        </languageitem>";
        sXMLConfig += "        <languageitem key='MagButton " + Integer.toString(BUTTON_SELECT_NONE) + "'>";
        sXMLConfig += "            <translation lang='hu'>Egyik sem</translation>";
        sXMLConfig += "            <translation lang='en'>None</translation>";
        sXMLConfig += "            <translation lang='de'>Keiner</translation>";
        sXMLConfig += "        </languageitem>";
        sXMLConfig += "        <languageitem key='MagButton " + Integer.toString(BUTTON_PDF_VIEW) + "'>";
        sXMLConfig += "            <translation lang='hu'>PDF megtekintés</translation>";
        sXMLConfig += "            <translation lang='en'>PDF View</translation>";
        sXMLConfig += "            <translation lang='de'>PDF Ansicht</translation>";
        sXMLConfig += "        </languageitem>";
        sXMLConfig += "        <languageitem key='MagButton " + Integer.toString(BUTTON_PDF_PRINT) + "'>";
        sXMLConfig += "            <translation lang='hu'>PDF nyomtatás</translation>";
        sXMLConfig += "            <translation lang='en'>PDF Print</translation>";
        sXMLConfig += "            <translation lang='de'>PDF Druck</translation>";
        sXMLConfig += "        </languageitem>";
        sXMLConfig += "    </language>";
        sXMLConfig += "</app>";
//        swingAppInterface.addLanguageXML(sXMLConfig, "ISO-8859-2");
        swingAppInterface.addLanguageXML(sXMLConfig, "UTF-8");
    }

    public static MagButton createMagButton(SwingAppInterface swingAppInterface, String sText, String sActionCommand, ActionListener actionListener, int iMnemonic, String sTooltipText) {
        MagButton magButton = new MagButton(sText, swingAppInterface);
        magButton.setMargin(new Insets(0, 5, 0, 5));
        magButton.setActionCommand(sActionCommand);
        if (actionListener != null) {
            magButton.addActionListener(actionListener);
        }
        if (iMnemonic > 0) {
            magButton.setMnemonic(iMnemonic);
        }
        magButton.setToolTipText(sTooltipText);
        magButton.setFocusable(false);
        return (magButton);
    }

    public static MagButton createMagButton(SwingAppInterface swingAppInterface, int iButtonType, ActionListener actionListener) {
        return (createMagButton(swingAppInterface, iButtonType, actionListener, Event.CTRL_MASK));
    }

    public static MagButton createMagButton(SwingAppInterface swingAppInterface, int iButtonType, ActionListener actionListener, int iModifier) {
        String sText = "";
        String sActionCommand = "";
        int iMnemonic = 0;
        //String sTooltipText = null;
        String sTooltipText = " ";
        sText = swingAppInterface.getLanguageString("MagButton " + Integer.toString(iButtonType));
        Character keyChar;
        int iKeyCode = 0;
        if ((iModifier & Event.ALT_MASK) > 0) {
//            sTooltipText += "Alt-";
        }
        if ((iModifier & Event.CTRL_MASK) > 0) {
//            sTooltipText += "Ctrl-";
        }
        if ((iModifier & Event.SHIFT_MASK) > 0) {
//            sTooltipText += "Shift-";
        }
        switch (iButtonType) {
            case BUTTON_OK:
                sActionCommand = "action_ok";
                iKeyCode = KeyEvent.VK_ENTER;
                iModifier = 0;
                break;
            case BUTTON_CANCEL:
                sActionCommand = "action_cancel";
                iKeyCode = KeyEvent.VK_ESCAPE;
                iModifier = 0;
                break;
            case BUTTON_SAVE:
                sActionCommand = "action_save";
                iKeyCode = KeyEvent.VK_S;
                break;
            case BUTTON_YES:
                sActionCommand = "action_yes";
                iKeyCode = KeyEvent.VK_Y;
                break;
            case BUTTON_NO:
                sActionCommand = "action_no";
                iKeyCode = KeyEvent.VK_N;
                break;
            case BUTTON_REFRESH:
                sActionCommand = "action_refresh";
                iKeyCode = KeyEvent.VK_R;
                break;
            case BUTTON_NEW:
                sActionCommand = "action_new";
                iKeyCode = KeyEvent.VK_N;
                break;
            case BUTTON_EDIT:
                sActionCommand = "action_edit";
                iKeyCode = KeyEvent.VK_E;
                break;
            case BUTTON_DELETE:
                sActionCommand = "action_delete";
                iKeyCode = KeyEvent.VK_D;
                break;
            case BUTTON_EXCEL:
                sActionCommand = "action_excel";
                iKeyCode = KeyEvent.VK_X;
                break;
            case BUTTON_DETAILS:
                sActionCommand = "action_details";
                iKeyCode = KeyEvent.VK_I;
                break;
            case BUTTON_SELECT_ALL:
                sActionCommand = "action_select_all";
                iKeyCode = KeyEvent.VK_A;
                break;
            case BUTTON_SELECT_REVERSE:
                sActionCommand = "action_select_reverse";
                iKeyCode = KeyEvent.VK_R;
                break;
            case BUTTON_SELECT_NONE:
                sActionCommand = "action_select_none";
                iKeyCode = KeyEvent.VK_N;
                break;
            case BUTTON_PDF_VIEW:
                sActionCommand = "action_pdf_view";
                iKeyCode = 0;
                break;
            case BUTTON_PDF_PRINT:
                sActionCommand = "action_pdf_print";
                iKeyCode = 0;
                break;
            default:
                throw new IllegalArgumentException("unknown button type " + Integer.toString(iButtonType));
        }
        MagButton magButton = createMagButton(swingAppInterface, sText, sActionCommand, actionListener, iMnemonic, sTooltipText);
        magButton.setButtonType(iButtonType);
        magButton.setText(swingAppInterface.getLanguageString("MagButton " + Integer.toString(iButtonType)));
        magButton.setToolTipText(magButton.getText());

        final String sFinalActionCommand = sActionCommand;
        //KeyStroke keyStroke = KeyStroke.getKeyStroke(keyChar, iModifier);
        KeyStroke keyStroke = KeyStroke.getKeyStroke(iKeyCode, iModifier);
        Action action = new AbstractAction(sActionCommand) {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println(sFinalActionCommand);
                actionListener.actionPerformed(new ActionEvent(magButton, 0, sFinalActionCommand)); //@todo: MagComponentEvent?
            }
        };

        //http://www.codejava.net/java-se/swing/setting-shortcut-key-and-hotkey-for-menu-item-and-button-in-swing
        //magButton.setAction(action);
        //action.putValue(Action.MNEMONIC_KEY, iKeyCode);
        magButton.getActionMap().put(sActionCommand, action);
        magButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, sActionCommand);
//        System.out.println(sActionCommand + " " + Integer.toString(iModifier));
//        Object[] keys = magButton.getActionMap().allKeys();
//        for (int i = 0; i < keys.length; i++) {
//            System.out.println(keys[i].toString());
//        }

//        if (iButtonType == BUTTON_NEW) {
//            //KeyStroke keyNew = KeyStroke.getKeyStroke(KeyEvent.VK_N, iModifier);
//            KeyStroke keyNew = KeyStroke.getKeyStroke(iKeyCode, iModifier);
//            //KeyStroke keyNew = KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK);
//            //KeyStroke keySave = KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK);
//            //magButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keySave, "performSave"); 
//            Action performNew = new AbstractAction("new") {
//                public void actionPerformed(ActionEvent e) {
//                    //do your save
//                    //System.out.println("new");
//                    actionListener.actionPerformed(new ActionEvent(magButton, 0, "action_new"));
//                }
//            };
//            magButton.getActionMap().put("new", performNew);
//            magButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyNew, "new");
////            Object[] keys = magButton.getActionMap().allKeys();
////            for (int i = 0; i < keys.length; i++) {
////                System.out.println(keys[i].toString());
////            }
//        }
        return (magButton);
    }

//    public static MagButton createMagButton(SwingAppInterface swingAppInterface, String sText, String sActionCommand, String sTooltipText, int iKeyCode, int iModifier, int iMnemonic, ActionListener actionListener) {
//        MagButton magButton = createMagButton(swingAppInterface, sText, sActionCommand, actionListener, iMnemonic, sTooltipText);
//
//        final String sFinalActionCommand = sActionCommand;
//        KeyStroke keyStroke = KeyStroke.getKeyStroke(iKeyCode, iModifier);
//        Action action = new AbstractAction(sActionCommand) {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                actionListener.actionPerformed(new ActionEvent(magButton, 0, sFinalActionCommand)); //@todo: MagComponentEvent?
//            }
//        };
//        //http://www.codejava.net/java-se/swing/setting-shortcut-key-and-hotkey-for-menu-item-and-button-in-swing
//        //magButton.setAction(action);
//        //action.putValue(Action.MNEMONIC_KEY, iKeyCode);
//        magButton.getActionMap().put(sActionCommand, action);
//        magButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, sActionCommand);
//        return (magButton);
//    }
    public static MagButton createMagButton(SwingAppInterface swingAppInterface, String sText, String sActionCommand, String sTooltipText, int iKeyCode, int iModifier, int iMnemonic, MagComponentEventListener magComponentEventListener) {
        MagButton magButton = createMagButton(swingAppInterface, sText, sActionCommand, null, iMnemonic, sTooltipText);
        magButton.addMagComponentEventListener(magComponentEventListener);
        final String sFinalActionCommand = sActionCommand;
        KeyStroke keyStroke = KeyStroke.getKeyStroke(iKeyCode, iModifier);
        Action action = new AbstractAction(sActionCommand) {
            @Override
            public void actionPerformed(ActionEvent e) {
                magButton.fireMagComponentEvent(new ActionEvent(magButton, 0, sFinalActionCommand));
            }
        };
        //http://www.codejava.net/java-se/swing/setting-shortcut-key-and-hotkey-for-menu-item-and-button-in-swing
        //magButton.setAction(action);
        //action.putValue(Action.MNEMONIC_KEY, iKeyCode);
        magButton.getActionMap().put(sActionCommand, action);
        if (iKeyCode != 0) { //MaG 2017.10.11.
            magButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, sActionCommand);
        }
        return (magButton);
    }

    public int getButtonType() {
        return iButtonType;
    }

    public void setButtonType(int iButtonType) {
        this.iButtonType = iButtonType;
    }

    @Override
    public void languageEventPerformed(LanguageEvent e) {
        if (e.getEventID() == LanguageEvent.LANGUAGE_CHANGED) {
            if (iButtonType > 0) {
                this.setText(swingAppInterface.getLanguageString("MagButton " + Integer.toString(iButtonType)));
                this.setToolTipText(this.getText());
                this.repaint();
            }
        }
    }

    public void addMagComponentEventListener(MagComponentEventListener mcel) {
        for (int i = 0; i < vMagComponentEventListeners.size(); i++) {
            if (vMagComponentEventListeners.elementAt(i).equals(mcel)) {
                return;
            }
        }
        vMagComponentEventListeners.add(mcel);
    }

    public void removeMagComponentEventListener(MagComponentEventListener mcel) {
        for (int i = 0; i < vMagComponentEventListeners.size(); i++) {
            if (vMagComponentEventListeners.elementAt(i).equals(mcel)) {
                vMagComponentEventListeners.remove(i);
            }
        }
    }

    private void fireMagComponentEvent(ActionEvent e) {
        for (int i = 0; i < vMagComponentEventListeners.size(); i++) {
            MagComponentEvent mce = new MagComponentEvent(this, MagComponentEvent.PUSHED);
            mce.setActionEvent(e);
            vMagComponentEventListeners.elementAt(i).componentEventPerformed(mce);
        }
    }
}
