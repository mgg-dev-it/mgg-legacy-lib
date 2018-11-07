package hu.mgx.app.swing;

import hu.mag.swing.MagComboBoxField;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import hu.mgx.app.common.*;
import hu.mgx.swing.*;
import hu.mgx.util.StringUtils;

public class FormatDialog extends JDialog implements ActionListener, KeyListener {

    private CommonPanel mainPane;
    private JButton jButtonYes;
    private JButton jButtonNo;
    private MagComboBoxField mlfDatePartOrder;
    private MagComboBoxField mlfDateSeparator;
    private MagComboBoxField mlfDecimalSeparator;
    public static final int YES = 0;
    public static final int NO = 1;
    public static final int ESCAPE = -1;
    private int iAction = ESCAPE;
    private FormatInterface formatInterface;
    private SwingAppInterface swingAppInterface;
    private final static String ACTION_YES = "Action_Yes";
    private final static String ACTION_NO = "Action_No";

    public FormatDialog(JFrame f, FormatInterface formatInterface, SwingAppInterface swingAppInterface) {
        super(f);
        this.formatInterface = formatInterface;
        this.swingAppInterface = swingAppInterface;
        init();
    }

    private void init() {
        String sXMLConfig = "<?xml version='1.0' encoding='ISO-8859-2'?>";
        sXMLConfig += "<!-- Comment -->";
        sXMLConfig += "<app name='app' major='0' minor='0' revision='0' width='800' height='600'>";
        sXMLConfig += "    <language>";
        sXMLConfig += "        <languageitem key='FormatDialog Formátum'>";
        sXMLConfig += "            <translation lang='hu'>Formátum</translation>";
        sXMLConfig += "            <translation lang='en'>Format</translation>";
        sXMLConfig += "            <translation lang='de'>Format</translation>";
        sXMLConfig += "        </languageitem>";
        sXMLConfig += "        <languageitem key='FormatDialog Dátum'>";
        sXMLConfig += "            <translation lang='hu'>Dátum</translation>";
        sXMLConfig += "            <translation lang='en'>Date</translation>";
        sXMLConfig += "            <translation lang='de'>Datum</translation>";
        sXMLConfig += "        </languageitem>";
        sXMLConfig += "        <languageitem key='FormatDialog Igen'>";
        sXMLConfig += "            <translation lang='hu'>Igen</translation>";
        sXMLConfig += "            <translation lang='en'>Yes</translation>";
        sXMLConfig += "            <translation lang='de'>Ja</translation>";
        sXMLConfig += "        </languageitem>";
        sXMLConfig += "        <languageitem key='FormatDialog Ment�s'>";
        sXMLConfig += "            <translation lang='hu'>Mentés</translation>";
        sXMLConfig += "            <translation lang='en'>Save</translation>";
        sXMLConfig += "            <translation lang='de'>Speichern</translation>";
        sXMLConfig += "        </languageitem>";
        sXMLConfig += "        <languageitem key='FormatDialog Nem'>";
        sXMLConfig += "            <translation lang='hu'>Mégsem</translation>";
        sXMLConfig += "            <translation lang='en'>Cancel</translation>";
        sXMLConfig += "            <translation lang='de'>Abbrechen</translation>";
        sXMLConfig += "        </languageitem>";
        sXMLConfig += "        <languageitem key='FormatDialog Dátum sorrend'>";
        sXMLConfig += "            <translation lang='hu'>Dátum sorrend</translation>";
        sXMLConfig += "            <translation lang='en'>Date order</translation>";
        sXMLConfig += "            <translation lang='de'>Datum Ordnung</translation>";
        sXMLConfig += "        </languageitem>";
        sXMLConfig += "        <languageitem key='FormatDialog Év'>";
        sXMLConfig += "            <translation lang='hu'>Év</translation>";
        sXMLConfig += "            <translation lang='en'>Year</translation>";
        sXMLConfig += "            <translation lang='de'>Jahr</translation>";
        sXMLConfig += "        </languageitem>";
        sXMLConfig += "        <languageitem key='FormatDialog Hónap'>";
        sXMLConfig += "            <translation lang='hu'>Hónap</translation>";
        sXMLConfig += "            <translation lang='en'>Month</translation>";
        sXMLConfig += "            <translation lang='de'>Monat</translation>";
        sXMLConfig += "        </languageitem>";
        sXMLConfig += "        <languageitem key='FormatDialog Nap'>";
        sXMLConfig += "            <translation lang='hu'>Nap</translation>";
        sXMLConfig += "            <translation lang='en'>Day</translation>";
        sXMLConfig += "            <translation lang='de'>Tag</translation>";
        sXMLConfig += "        </languageitem>";
        sXMLConfig += "        <languageitem key='FormatDialog Dátum elválasztó'>";
        sXMLConfig += "            <translation lang='hu'>Dátum elválasztó</translation>";
        sXMLConfig += "            <translation lang='en'>Date separator</translation>";
        sXMLConfig += "            <translation lang='de'>Datum Separator</translation>";
        sXMLConfig += "        </languageitem>";
        sXMLConfig += "        <languageitem key='FormatDialog Tizedes elválasztó'>";
        sXMLConfig += "            <translation lang='hu'>Tizedes elválasztó</translation>";
        sXMLConfig += "            <translation lang='en'>Decimal separator</translation>";
        sXMLConfig += "            <translation lang='de'>Decimal Separator</translation>";
        sXMLConfig += "        </languageitem>";
        sXMLConfig += "    </language>";
        sXMLConfig += "</app>";
//        swingAppInterface.addLanguageXML(sXMLConfig, "ISO-8859-2");
        swingAppInterface.addLanguageXML(sXMLConfig, "UTF-8");

        setModal(true);
        mainPane = new CommonPanel();
        mainPane.setInsets(2, 3, 2, 3);
        setContentPane(mainPane);

        mlfDatePartOrder = new MagComboBoxField(swingAppInterface);
        mlfDatePartOrder.addKeyListener(this);
        String sDatePartOrderLookup = "";
        sDatePartOrderLookup += "yyyy-MM-dd|" + swingAppInterface.getLanguageString("FormatDialog Év") + " " + swingAppInterface.getLanguageString("FormatDialog H�nap") + " " + swingAppInterface.getLanguageString("FormatDialog Nap");
        sDatePartOrderLookup += "@";
        sDatePartOrderLookup += "dd-MM-yyyy|" + swingAppInterface.getLanguageString("FormatDialog Nap") + " " + swingAppInterface.getLanguageString("FormatDialog H�nap") + " " + swingAppInterface.getLanguageString("FormatDialog �v");
        sDatePartOrderLookup += "@";
        sDatePartOrderLookup += "MM-dd-yyyy|" + swingAppInterface.getLanguageString("FormatDialog Hónap") + " " + swingAppInterface.getLanguageString("FormatDialog Nap") + " " + swingAppInterface.getLanguageString("FormatDialog �v");
        mlfDatePartOrder.fillLookup(sDatePartOrderLookup);
        mainPane.addToCurrentRow(AppUtils.createLabelWithColon(swingAppInterface.getLanguageString("FormatDialog Dátum")), 1, 1, 0.0, 0.0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        mainPane.addToCurrentRow(mlfDatePartOrder, 2, 1, 0.0, 0.0, GridBagConstraints.BOTH);
        mainPane.addToCurrentRow(new JLabel(""), GridBagConstraints.REMAINDER, 1, 0.0, 1.0, GridBagConstraints.BOTH);
        mainPane.nextRow();

        //@todo question: how to choose, whether the point character should appear at the end of the date or not?
        mlfDateSeparator = new MagComboBoxField(swingAppInterface);
        mlfDateSeparator.addKeyListener(this);
        mlfDateSeparator.fillLookup("/|/@-|-@.|.");
        mainPane.addToCurrentRow(AppUtils.createLabelWithColon(swingAppInterface.getLanguageString("FormatDialog Dátum elválasztó")), 1, 1, 0.0, 0.0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        mainPane.addToCurrentRow(mlfDateSeparator, 2, 1, 0.0, 0.0, GridBagConstraints.BOTH);
        mainPane.addToCurrentRow(new JLabel(""), GridBagConstraints.REMAINDER, 1, 0.0, 1.0, GridBagConstraints.BOTH);
        mainPane.nextRow();

        mlfDecimalSeparator = new MagComboBoxField(swingAppInterface);
        mlfDecimalSeparator.addKeyListener(this);
        mlfDecimalSeparator.fillLookup(".|.@,|,");
        mainPane.addToCurrentRow(AppUtils.createLabelWithColon(swingAppInterface.getLanguageString("FormatDialog Tizedes elválasztó")), 1, 1, 0.0, 0.0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        mainPane.addToCurrentRow(mlfDecimalSeparator, 2, 1, 0.0, 0.0, GridBagConstraints.BOTH);
        mainPane.addToCurrentRow(new JLabel(""), GridBagConstraints.REMAINDER, 1, 0.0, 1.0, GridBagConstraints.BOTH);
        mainPane.nextRow();

        mainPane.addToCurrentRow(new JLabel(""), 1, 1, 0.0, 0.0, GridBagConstraints.NONE);
        mainPane.addToCurrentRow(jButtonYes = AppUtils.createButton(swingAppInterface.getLanguageString("FormatDialog Mentés"), ACTION_YES, this), 1, 1, 0.0, 0.0, GridBagConstraints.NONE, GridBagConstraints.CENTER);
        AppUtils.setButtonCaptionAndMnemonic(jButtonYes, swingAppInterface.getLanguageString("FormatDialog Mentés"));
        mainPane.addToCurrentRow(jButtonNo = AppUtils.createButton(swingAppInterface.getLanguageString("FormatDialog Nem"), ACTION_NO, this), 1, 1, 0.0, 0.0, GridBagConstraints.NONE, GridBagConstraints.CENTER);
        AppUtils.setButtonCaptionAndMnemonic(jButtonNo, swingAppInterface.getLanguageString("FormatDialog Nem"));
        mainPane.addToCurrentRow(new JLabel(""), GridBagConstraints.REMAINDER, 1, 0.0, 1.0, GridBagConstraints.BOTH);
        mainPane.nextRow();

        mainPane.addToCurrentRow(new JLabel(""), GridBagConstraints.REMAINDER, 1, 1.0, 1.0, GridBagConstraints.BOTH);
    }

    public int showFormatDialog(JFrame f) {
        setTitle(swingAppInterface.getLanguageString("FormatDialog Formátum"));

        String sDatePattern = formatInterface.getDatePattern();
        String sDateOrder = StringUtils.filter(sDatePattern, "yMd");
        char cDateSeparator = ' ';
        switch (sDateOrder) {
            case "yyyyMMdd":
                cDateSeparator = sDatePattern.charAt(4);
                break;
            case "ddMMyyyy":
                cDateSeparator = sDatePattern.charAt(2);
                break;
            case "MMddyyyy":
                cDateSeparator = sDatePattern.charAt(2);
                break;
            default:
                break;
        }
        mlfDatePartOrder.setValue(StringUtils.stringReplace(sDatePattern, cDateSeparator, "-"));
        mlfDateSeparator.setValue(new StringBuffer().append(cDateSeparator).toString());
        mlfDecimalSeparator.setValue(new StringBuffer().append(formatInterface.getDecimalSeparator()).toString());
        iAction = ESCAPE;
        pack();
        setLocationRelativeTo(f);
        mlfDatePartOrder.requestFocus();
        setVisible(true);
        return (iAction);
    }

    private void actionYES() {
        formatInterface.setDatePattern(StringUtils.stringReplace(mlfDatePartOrder.getValue().toString(), "-", mlfDateSeparator.getValue().toString()));
        formatInterface.setDecimalSeparator(mlfDecimalSeparator.getValue().toString().charAt(0));
        iAction = YES;
        setVisible(false);
    }

    private void actionNO() {
        iAction = NO;
        setVisible(false);
    }

    private void actionESCAPE() {
        iAction = ESCAPE;
        setVisible(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(ACTION_YES)) {
            actionYES();
        }
        if (e.getActionCommand().equals(ACTION_NO)) {
            actionNO();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            actionYES();
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            actionESCAPE();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
