package hu.mgx.app.swing;

import hu.mag.swing.MagFieldInterface;
import hu.mag.swing.table.MagTablePanel;
import hu.mgx.swing.CommonPanel;
import hu.mgx.app.common.*;
import hu.mgx.swing.table.MemoryTable;
import hu.mgx.util.ExampleFileFilter;
import hu.mgx.util.StringUtils;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;

public abstract class AppUtils {

    private AppUtils() {
    }

//    public static String getVersionString(int iMajor, int iMinor, int iRevision)
//    {
//        return (Integer.toString(iMajor) + "." + StringUtils.right("00" + Integer.toString(iMinor), 2) + "." + StringUtils.right("000" + Integer.toString(iRevision), 3));
//    }
//    public static String getTitle(String sAppName, int iMajor, int iMinor, int iRevision)
//    {
//        return (sAppName + (iMajor > 0 || iMinor > 0 || iRevision > 0 ? " " + getVersionString(iMajor, iMinor, iRevision) : ""));
//    }
    public static JLabel createNamedLabel(String sText, String sName) {
        JLabel jLabel = createLabel(sText, SwingConstants.LEFT);
        jLabel.setName(sName);
        return (jLabel);
    }

    public static JLabel createNamedLabel(String sText, String sName, int iHorizontalAlignment) {
        JLabel jLabel = createLabel(sText, iHorizontalAlignment);
        jLabel.setName(sName);
        return (jLabel);
    }

    public static JLabel createLabel(String sText) {
        return (createLabel(sText, SwingConstants.LEFT));
    }

    public static JLabel createLabel(String sText, String sTooltipText) {
        return (createLabel(sText, sTooltipText, SwingConstants.LEFT));
    }

    public static JLabel createLabelColor(String sText, Color color) {
        JLabel jLabel = createLabel(sText, SwingConstants.LEFT);
        jLabel.setForeground(color);
        return (jLabel);
    }

    public static JLabel createLabelWithColon(String sText) {
        return (createLabel(" " + sText + ": ", SwingConstants.LEFT));
    }

    public static JLabel createLabel(String sText, int iHorizontalAlignment) {
        JLabel jLabel = new JLabel(sText, iHorizontalAlignment);
        return (jLabel);
    }

    public static JLabel createLabel(String sText, String sToolTipText, int iHorizontalAlignment) {
        JLabel jLabel = new JLabel(sText, iHorizontalAlignment);
        jLabel.setToolTipText(sToolTipText);
        return (jLabel);
    }

    public static JLabel createLabelWithColon(String sText, int iHorizontalAlignment) {
        JLabel jLabel = new JLabel(" " + sText + ": ", iHorizontalAlignment);
        return (jLabel);
    }

    public static JLabel createLabelImage(Class classImage, String sImageName, int iHorizontalAlignment) {
        java.net.URL imageURL = classImage.getResource(sImageName);
        if (imageURL != null) {
            //ImageIcon imageIcon = new ImageIcon(imageURL);
            //return (new JLabel(imageIcon));
            return (new JLabel(new ImageIcon(imageURL), iHorizontalAlignment));
        }
        return (new JLabel(""));
    }

    public static JLabel createLabelImage(Class classImage, String sImageName, int iHorizontalAlignment, int iWidth, int iHeight) {
        java.net.URL imageURL = classImage.getResource(sImageName);
        if (imageURL != null) {
            //ImageIcon imageIcon = new ImageIcon(imageURL);
            //return (new JLabel(imageIcon));
            return (new JLabel(new ImageIcon(new ImageIcon(imageURL).getImage().getScaledInstance(iWidth, iHeight, Image.SCALE_DEFAULT)), iHorizontalAlignment));
        }
        return (new JLabel(""));
    }

//    public static JLabel createLabelImage(Class classImage, String sImageName) {
//        Image image = null;
//        java.net.URL imageURL = classImage.getResource(sImageName);
//        if (imageURL != null) {
//            image = Toolkit.getDefaultToolkit().getImage(imageURL);
//        }
////        ImageIcon imageIcon = new ImageIcon(sImageName);
//        ImageIcon imageIcon = new ImageIcon(image);
//        JLabel jLabel = new JLabel(imageIcon);
//        return (jLabel);
//    }
//
    public static JTextField createTextField(Dimension d) {
        JTextField jTextField = new JTextField();
        jTextField.setMinimumSize(d);
        jTextField.setPreferredSize(d);
        jTextField.setMaximumSize(d);
        jTextField.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {
                e.getComponent().setBackground(Color.YELLOW);
                ((JTextField) e.getComponent()).selectAll();
            }

            public void focusLost(FocusEvent e) {
                e.getComponent().setBackground(Color.WHITE);
            }
        });
        return (jTextField);
    }

    public static JTextField createTextField(String sText, int iColumns) {
        JTextField jTextField = new JTextField(sText, iColumns);
        jTextField.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {
                e.getComponent().setBackground(Color.YELLOW);
                ((JTextField) e.getComponent()).selectAll();
            }

            public void focusLost(FocusEvent e) {
                e.getComponent().setBackground(Color.WHITE);
            }
        });
        return (jTextField);
    }

    public static JButton createButton(String sText, String sActionCommand, ActionListener actionListener, String sTooltipText) {
        JButton jButton = new JButton(sText);
        jButton.setActionCommand(sActionCommand);
        if (actionListener != null) {
            jButton.addActionListener(actionListener);
        }
        jButton.setToolTipText(sTooltipText);
        jButton.setFocusable(false);
        return (jButton);
    }

    public static JButton createButton(String sText, String sActionCommand, ActionListener actionListener) {
        return (createButton(sText, sActionCommand, actionListener, ""));
    }

    public static JButton createButton(String sText, String sActionCommand, ActionListener actionListener, int mnemonic, String sTooltipText) {
        JButton jButton = new JButton(sText);
        jButton.setActionCommand(sActionCommand);
        if (actionListener != null) {
            jButton.addActionListener(actionListener);
        }
        jButton.setMnemonic(mnemonic);
        jButton.setToolTipText(sTooltipText);
        jButton.setFocusable(false);
        return (jButton);
    }

    public static JButton createButton(String sText, String sActionCommand, ActionListener actionListener, int mnemonic) {
        return (createButton(sText, sActionCommand, actionListener, mnemonic, ""));
    }

    public static JButton createButton(String sText, ActionListener actionListener) {
        JButton jButton = new JButton(sText);
        if (actionListener != null) {
            jButton.addActionListener(actionListener);
        }
        return (jButton);
    }

    public static JRadioButton createRadioButton(String sText, String sActionCommand, ActionListener actionListener) {
        JRadioButton jRadioButton = new JRadioButton(sText);
        jRadioButton.setActionCommand(sActionCommand);
        if (actionListener != null) {
            jRadioButton.addActionListener(actionListener);
        }
        return (jRadioButton);
    }

    public static JCheckBox createCheckBox(String sText, String sActionCommand, ActionListener actionListener) {
        JCheckBox jCheckBox = new JCheckBox(sText);
        jCheckBox.setActionCommand(sActionCommand);
        if (actionListener != null) {
            jCheckBox.addActionListener(actionListener);
        }
        jCheckBox.setBorder(new EmptyBorder(0, 0, 0, 0));
        jCheckBox.setBackground(Color.WHITE);
        jCheckBox.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {
                e.getComponent().setBackground(Color.YELLOW);
            }

            public void focusLost(FocusEvent e) {
                e.getComponent().setBackground(Color.WHITE);
            }
        });
        return (jCheckBox);
    }

    public static CommonPanel createEmptyPanel() {
        CommonPanel cp = new CommonPanel();
        cp.setInsets(2, 3, 2, 3);
        cp.setBorder(new EmptyBorder(0, 0, 0, 0));
        return (cp);
    }

    public static CommonPanel createEmptyPanelWithText(Dimension dimPreferred) {
        CommonPanel jPanel = new CommonPanel();
        jPanel.setInsets(2, 3, 2, 3);
        //        jPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        jPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        jPanel.setPreferredSize(dimPreferred);
        //---
        jPanel.addToGrid(createLabel("empty panel", SwingConstants.LEFT), 0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.BOTH);
        jPanel.addToGrid(new JLabel(""), 0, 1, GridBagConstraints.REMAINDER, 1, 0.0, 1.0, GridBagConstraints.BOTH);

        //--- utols� sor rugalmas...
        jPanel.addToGrid(new JLabel(""), 1, 0, GridBagConstraints.REMAINDER, 1, 1.0, 1.0, GridBagConstraints.BOTH);
        return (jPanel);
    }

    public static CommonPanel putPanelIntoScrollPane(CommonPanel cpInner, Dimension dPreferred) {
        CommonPanel cpOuter = new CommonPanel();
        cpOuter.setBorder(new EmptyBorder(0, 0, 0, 0));

        JScrollPane jspInnerOuter = new JScrollPane(cpInner);
        jspInnerOuter.setPreferredSize(dPreferred);
        jspInnerOuter.getVerticalScrollBar().setUnitIncrement(jspInnerOuter.getVerticalScrollBar().getMaximum() / 10);
        cpOuter.addToGrid(jspInnerOuter, 0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.BOTH);
        return (cpOuter);
    }

//    public static void handleError(Exception e, JFrame parentFrame)
//    {
//        (new ErrorBox(parentFrame)).showError(parentFrame, "Hiba", e.getLocalizedMessage());
//    }
//    public static void handleError(AppErrorHandler a, Exception e, JFrame parentFrame)
//    {
//        if (a == null)
//        {
//            System.err.println(e.getLocalizedMessage());
//            e.printStackTrace(System.err);
//            ErrorBox errorBox = new ErrorBox(parentFrame);
//            errorBox.showError(parentFrame, "Hiba", e.getLocalizedMessage());
//            errorBox.dispose();
//        }
//        else
//        {
//            a.handleAppError(e, new ErrorSeverity(0));
//        }
//    }
    public static boolean yesNoQuestion(String sTitle, String sMsg, LanguageHandlerInterface languageHandlerInterface) {
        YesNoDialog yesNoDialog = new YesNoDialog();
        int iRetVal = yesNoDialog.showDialog(sTitle, sMsg);
        yesNoDialog.dispose();
        yesNoDialog = null;
        //return (yesNoDialog.showDialog(sTitle, sMsg) == YesNoDialog.YES);
        return (iRetVal == YesNoDialog.YES);
    }

    public static boolean yesNoQuestion(Frame frame, String sTitle, String sMsg, LanguageHandlerInterface languageHandlerInterface) {
        YesNoDialog yesNoDialog = new YesNoDialog(frame, languageHandlerInterface);
        int iRetVal = yesNoDialog.showDialog(frame, sTitle, sMsg);
        yesNoDialog.dispose();
        yesNoDialog = null;
        //return (yesNoDialog.showDialog(frame, sTitle, sMsg) == YesNoDialog.YES);
        return (iRetVal == YesNoDialog.YES);
    }

    public static boolean yesNoQuestion(JFrame f, String sTitle, String sMsg, LanguageHandlerInterface languageHandlerInterface) {
        YesNoDialog yesNoDialog = new YesNoDialog(f, languageHandlerInterface);
        int iRetVal = yesNoDialog.showDialog(f, sTitle, sMsg);
        yesNoDialog.dispose();
        yesNoDialog = null;
        //return (yesNoDialog.showDialog(f, sTitle, sMsg) == YesNoDialog.YES);
        return (iRetVal == YesNoDialog.YES);
    }

    public static boolean yesNoQuestion(JDialog d, String sTitle, String sMsg, LanguageHandlerInterface languageHandlerInterface) {
        YesNoDialog yesNoDialog = new YesNoDialog(d, languageHandlerInterface);
        int iRetVal = yesNoDialog.showDialog(d, sTitle, sMsg);
        yesNoDialog.dispose();
        yesNoDialog = null;
        //return (yesNoDialog.showDialog(d, sTitle, sMsg) == YesNoDialog.YES);
        return (iRetVal == YesNoDialog.YES);
    }

    public static boolean yesNoQuestion(Component c, String sTitle, String sMsg, LanguageHandlerInterface languageHandlerInterface) {
        YesNoDialog yesNoDialog = new YesNoDialog(c, languageHandlerInterface);
        int iRetVal = yesNoDialog.showDialog(c, sTitle, sMsg);
        yesNoDialog.dispose();
        yesNoDialog = null;
        //return (yesNoDialog.showDialog(c, sTitle, sMsg) == YesNoDialog.YES);
        return (iRetVal == YesNoDialog.YES);
    }

    public static int yesNoCancelQuestion(Component c, String sTitle, String sMsg, LanguageHandlerInterface languageHandlerInterface) {
        YesNoDialog yesNoDialog = new YesNoDialog(c, languageHandlerInterface);
        int iRetVal = yesNoDialog.showDialog(c, sTitle, sMsg);
        //return (yesNoDialog.showDialog(c, sTitle, sMsg));
        return (iRetVal);
    }

    public static void messageBox(java.awt.Component parentComponent, java.lang.String message) {
        JOptionPane.showMessageDialog(parentComponent, message, "", JOptionPane.PLAIN_MESSAGE);
    }

    public static void messageBox(java.awt.Component parentComponent, java.lang.String message, java.lang.String title) {
        JOptionPane.showMessageDialog(parentComponent, message, title, JOptionPane.PLAIN_MESSAGE);
    }

    public static void messageBox(java.awt.Component parentComponent, java.lang.String message, java.lang.String title, int iMessageType) {
        JOptionPane.showMessageDialog(parentComponent, message, title, iMessageType);
    }

    public static void storeCheckBoxState(JCheckBox jCheckBox, String sCheckBoxName, Properties properties) {
        if (jCheckBox == null) {
            return;
        }
        properties.setProperty("checkbox." + sCheckBoxName, (jCheckBox.isSelected() ? "1" : "0"));
    }

    public static void restoreCheckBoxState(JCheckBox jCheckBox, String sCheckBoxName, Properties properties) {
        if (jCheckBox == null) {
            return;
        }
        String sState = properties.getProperty("checkbox." + sCheckBoxName, "*");
        if (!sState.equals("*")) {
            jCheckBox.setSelected(sState.equals("1"));
        }
    }

    public static void storeRadioButtonState(JRadioButton jRadioButton, String sRadioButtonName, Properties properties) {
        if (jRadioButton == null) {
            return;
        }
        properties.setProperty("radiobutton." + sRadioButtonName, (jRadioButton.isSelected() ? "1" : "0"));
    }

    public static void restoreRadioButtonState(JRadioButton jRadioButton, String sRadioButtonName, Properties properties) {
        if (jRadioButton == null) {
            return;
        }
        String sState = properties.getProperty("radiobutton." + sRadioButtonName, "*");
        if (!sState.equals("*")) {
            jRadioButton.setSelected(sState.equals("1"));
        }
    }

    public static void storeTextFieldState(JTextField jTextField, String sTextFieldName, Properties properties) {
        if (jTextField == null) {
            return;
        }
        properties.setProperty("textfield." + sTextFieldName, jTextField.getText());
    }

    public static void restoreTextFieldState(JTextField jTextField, String sTextFieldName, Properties properties) {
        if (jTextField == null) {
            return;
        }
        String sState = properties.getProperty("textfield." + sTextFieldName, "*");
        if (!sState.equals("*")) {
            jTextField.setText(sState);
        }
    }

    public static String getCaption(String sCaption) {
        String sRetVal = "";
        for (int i = 0; i < sCaption.length(); i++) {
            if (sCaption.charAt(i) == '@') {
                return (sRetVal);
            }
            if (sCaption.charAt(i) != '&') {
                sRetVal += sCaption.substring(i, i + 1);
            }
        }
        return (sRetVal);
    }

    public static String getLabelCaption(String sCaption) {
        return (getCaption(sCaption) + ": ");
    }

    public static int getMnemonic(String sCaption) {
        int i = sCaption.indexOf("&");
        if ((i > -1) && (i < (sCaption.length() - 1))) {
            //return ((int) sCaption.charAt(i + 1));
            return ((int) sCaption.toUpperCase().charAt(i + 1)); //MAG 2015.06.09.
        }
        if (sCaption.length() > 0) {
            return ((int) sCaption.toUpperCase().charAt(0)); //MAG 2015.06.09.
        }
        return (0);
    }

    public static KeyStroke getKeyStroke(String sCaption) {
        String sKeyStroke = null;
        int i = sCaption.indexOf("@");
        if ((i > -1) && (i < (sCaption.length() - 1))) {
            sKeyStroke = sCaption.substring(i + 1);
        }
        return (KeyStroke.getKeyStroke(sKeyStroke));
    }

    public static void setButtonCaptionAndMnemonic(JButton jButton, String sCaption) {
        jButton.setText(getCaption(sCaption));
        int iMnemonic = getMnemonic(sCaption);
        if (iMnemonic > 0) {
            jButton.setMnemonic(iMnemonic);
        }
    }

    public static void delay(int iDelayInMilliseconds) {
        Long lStart = System.currentTimeMillis();
//        System.out.println("begin " + lStart + " " + (lStart + iDelayInMilliseconds));
        while ((lStart + iDelayInMilliseconds) > System.currentTimeMillis()) {
        }
//        System.out.println("end " + System.currentTimeMillis());
    }

    public static java.awt.Window getWindowParent(java.awt.Component component) {
        if (component instanceof java.awt.Window) {
            return ((java.awt.Window) component);
        }
        java.awt.Component comp = component.getParent();
        while (comp != null) {
            if (comp instanceof java.awt.Window) {
                return ((java.awt.Window) comp);
            }
            comp = comp.getParent();
        }
        return (null);
    }

    public static void setEnabled(boolean bEnabled, Component... components) {
        for (int i = 0; i < components.length; i++) {
            components[i].setEnabled(bEnabled);
        }
    }

    public static void setReadOnly(boolean bReadOnly, MagFieldInterface... mfi) {
        for (int i = 0; i < mfi.length; i++) {
            mfi[i].setReadOnly(bReadOnly);
        }
    }

    public static boolean splashExists() {
        final SplashScreen splash = SplashScreen.getSplashScreen();
        return (splash != null);
    }

    public static void displaySplash(String sTitle, int iDelayInMilliseconds) {
        displaySplash(sTitle, "", iDelayInMilliseconds);
    }

    public static void displaySplash(String sTitle, String sSubTitle, int iDelayInMilliseconds) {
        final SplashScreen splash = SplashScreen.getSplashScreen();
        if (splash != null) {
            Graphics2D g = splash.createGraphics();
            if (g != null) {
                double dSplashWidth = splash.getSize().getWidth();
                double dSplashHeight = splash.getSize().getHeight();
                g.setComposite(AlphaComposite.Clear);
                g.fillRect(0, 0, new Double(dSplashWidth).intValue(), new Double(dSplashHeight).intValue());
                g.setPaintMode();
                g.setColor(new Color(112, 146, 190));
                Font font = new Font("Arial", Font.ITALIC, 24);
                g.setFont(font);
                Rectangle2D r = g.getFontMetrics().getStringBounds(sTitle, g);
                g.drawString(sTitle, new Double((dSplashWidth - r.getWidth()) / 2).intValue(), new Double(dSplashHeight * 0.8 - r.getHeight() / 2).intValue());
                Rectangle2D r2 = g.getFontMetrics().getStringBounds(sSubTitle, g);
                g.drawString(sSubTitle, new Double((dSplashWidth - r2.getWidth()) / 2).intValue(), new Double(dSplashHeight * 0.9 - r2.getHeight() / 2).intValue());
                splash.update();
                if (iDelayInMilliseconds > 0) {
                    AppUtils.delay(iDelayInMilliseconds);
                }
            }
        }
    }

    public static void removeSplash() {
        final SplashScreen splash = SplashScreen.getSplashScreen();
        if (splash != null) {
            splash.close();
        }
    }

    public static void setCursorWait(JInternalFrame jif) {
        jif.setCursor(new Cursor(Cursor.WAIT_CURSOR));
    }

    public static void setCursorDefault(JInternalFrame jif) {
        jif.setCursor(Cursor.getDefaultCursor());
    }

//    public static File chooseFileToCreateStrict(java.awt.Component parentComponent, AppInterface appInterface, String sDescription, String sDir, String... sExtensions) {
//        return (chooseFileToCreate(parentComponent, appInterface, sDescription, sDir, "", false, true, sExtensions));
//    }
    public static File chooseFileToCreateStrict(java.awt.Component parentComponent, AppInterface appInterface, String sDescription, String sDir, String sFilename, String... sExtensions) {
        return (chooseFileToCreate(parentComponent, appInterface, sDescription, sDir, sFilename, false, true, sExtensions));
    }

    public static File chooseFileToCreate(java.awt.Component parentComponent, AppInterface appInterface, String sDescription, String sDir, String sFilename, boolean bAcceptAll, boolean bCheckExtension, String... sExtensions) {
        JFileChooser jFileChooser = new JFileChooser(sDir);
        ExampleFileFilter filter = new ExampleFileFilter();
        String sExtensionList = "";
        for (int i = 0; i < sExtensions.length; i++) {
            filter.addExtension(sExtensions[i]);
            sExtensionList += (sExtensionList.equalsIgnoreCase("") ? "." : ", .") + sExtensions[i];
        }
        filter.setDescription(appInterface.getLanguageString(sDescription));
        jFileChooser.setFileFilter(filter);
        jFileChooser.setAcceptAllFileFilterUsed(bAcceptAll);
        sFilename = StringUtils.isNull(sFilename, "");
        if (sFilename.length() > 0) {
            jFileChooser.setSelectedFile(new File(sDir + File.separator + sFilename));
        }
        int iDialogResult = jFileChooser.showSaveDialog(parentComponent);
        if (iDialogResult != JFileChooser.APPROVE_OPTION) {
            return (null);
        }
        File f = jFileChooser.getSelectedFile();
        if (f.getName().equals("")) {
            return (null);
        }
        if (!bAcceptAll) {
            if (f.getName().indexOf('.') < 0) {
                f = new File(f.getAbsolutePath() + "." + sExtensions[0]);
            }
            boolean bExtensionOK = false;
            for (int i = 0; i < sExtensions.length; i++) {
                if (f.getName().endsWith(sExtensions[i])) {
                    bExtensionOK = true;
                }
            }
            if (!bExtensionOK) {
                AppUtils.messageBox(parentComponent, "A f�jln�v kiterjeszt�se csak " + sExtensionList + " lehet");
                return (null);
            }
        }
        if (f.exists()) {
            //@todo task : nyelves�t�s
            if (!AppUtils.yesNoQuestion(parentComponent, appInterface.getLanguageString(sDescription), appInterface.getLanguageString(f.getAbsolutePath() + " f�jl l�tezik. Fel�l�rjam?"), appInterface)) {
                return (null);
            }
        }
        return (f);
    }

    public static File chooseFileToOpenStrict(java.awt.Component parentComponent, AppInterface appInterface, String sDescription, String sDir, String... sExtensions) {
        return (chooseFileToOpen(parentComponent, appInterface, sDescription, sDir, false, true, sExtensions));
    }

    public static File chooseFileToOpen(java.awt.Component parentComponent, AppInterface appInterface, String sDescription, String sDir, boolean bAcceptAll, boolean bCheckExtension, String... sExtensions) {
        JFileChooser jFileChooser = new JFileChooser(sDir);
        ExampleFileFilter filter = new ExampleFileFilter();
        String sExtensionList = "";
        for (int i = 0; i < sExtensions.length; i++) {
            filter.addExtension(sExtensions[i]);
            sExtensionList += (sExtensionList.equalsIgnoreCase("") ? "." : ", .") + sExtensions[i];
        }
        filter.setDescription(appInterface.getLanguageString(sDescription));
        jFileChooser.setFileFilter(filter);
        jFileChooser.setAcceptAllFileFilterUsed(bAcceptAll);
        int iDialogResult = jFileChooser.showOpenDialog(parentComponent);
        if (iDialogResult != JFileChooser.APPROVE_OPTION) {
            return (null);
        }
        File f = jFileChooser.getSelectedFile();
        if (f.getName().equals("")) {
            return (null);
        }
        if (!bAcceptAll) {
            if (f.getName().indexOf('.') < 0) {
                f = new File(f.getAbsolutePath() + "." + sExtensions[0]);
            }
            boolean bExtensionOK = false;
            for (int i = 0; i < sExtensions.length; i++) {
                if (f.getName().endsWith(sExtensions[i])) {
                    bExtensionOK = true;
                }
            }
            if (!bExtensionOK) {
                AppUtils.messageBox(parentComponent, "A f�jln�v kiterjeszt�se csak " + sExtensionList + " lehet");
                return (null);
            }
        }
        if (!f.exists()) {
            //@todo task : nyelves�t�s
            AppUtils.messageBox(parentComponent, appInterface.getLanguageString(f.getAbsolutePath() + " f�jl nem l�tezik."), appInterface.getLanguageString(sDescription));
            return (null);
        }
        return (f);
    }

    public static File[] chooseFilesToOpenStrict(java.awt.Component parentComponent, AppInterface appInterface, String sDescription, String sDir, String... sExtensions) {
        return (chooseFilesToOpen(parentComponent, appInterface, sDescription, sDir, false, true, sExtensions));
    }

    public static File[] chooseFilesToOpen(java.awt.Component parentComponent, AppInterface appInterface, String sDescription, String sDir, boolean bAcceptAll, boolean bCheckExtension, String... sExtensions) {
        JFileChooser jFileChooser = new JFileChooser(sDir);
        ExampleFileFilter filter = new ExampleFileFilter();
        String sExtensionList = "";
        for (int i = 0; i < sExtensions.length; i++) {
            filter.addExtension(sExtensions[i]);
            sExtensionList += (sExtensionList.equalsIgnoreCase("") ? "." : ", .") + sExtensions[i];
        }
        filter.setDescription(appInterface.getLanguageString(sDescription));
        jFileChooser.setFileFilter(filter);
        jFileChooser.setAcceptAllFileFilterUsed(bAcceptAll);
        jFileChooser.setMultiSelectionEnabled(true);
        int iDialogResult = jFileChooser.showOpenDialog(parentComponent);
        if (iDialogResult != JFileChooser.APPROVE_OPTION) {
            return (null);
        }
        File[] f = jFileChooser.getSelectedFiles();
        if (f.length < 1) {
            return (null);

        }
        for (int i = 0; i < f.length; i++) {
            if (!bAcceptAll) {
                if (f[i].getName().indexOf('.') < 0) {
                    f[i] = new File(f[i].getAbsolutePath() + "." + sExtensions[0]);
                }
                boolean bExtensionOK = false;
                for (int j = 0; j < sExtensions.length; j++) {
                    if (f[i].getName().endsWith(sExtensions[j])) {
                        bExtensionOK = true;
                    }
                }
                if (!bExtensionOK) {
                    AppUtils.messageBox(parentComponent, "A f�jln�v kiterjeszt�se csak " + sExtensionList + " lehet");
                    return (null);
                }
            }
            if (!f[i].exists()) {
                //@todo task : nyelves�t�s
                AppUtils.messageBox(parentComponent, appInterface.getLanguageString(f[i].getAbsolutePath() + " f�jl nem l�tezik."), appInterface.getLanguageString(sDescription));
                return (null);
            }
        }
        return (f);
    }

    public static Dimension getResizedScreenSize(double dblResizeBy) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension screenSizeResized = screenSize;
        screenSizeResized.setSize(screenSize.getWidth() * dblResizeBy, screenSize.getHeight() * dblResizeBy);
        return (screenSizeResized);
    }

    public static Dimension getResizedScreenSize(double dblResizeWithBy, double dblResizeHeightBy) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension screenSizeResized = screenSize;
        screenSizeResized.setSize(screenSize.getWidth() * dblResizeWithBy, screenSize.getHeight() * dblResizeHeightBy);
        return (screenSizeResized);
    }

    public static JTabbedPane createWrapperTabbedPane(Component c, String sTitle) {
        JTabbedPane jtp = new JTabbedPane();
        jtp.setFocusable(false);
        jtp.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        jtp.addTab(sTitle, c);
        return (jtp);
    }

    public static JSplitPane createHorizontalSplitPane(Component cLeft, Component cRight, double dblResizeWeight) {
        JSplitPane jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, cLeft, cRight);
        jsp.setOneTouchExpandable(true);
        jsp.setResizeWeight(dblResizeWeight);
        return (jsp);
    }

    public static JSplitPane createVerticalSplitPane(Component cLeft, Component cRight, double dblResizeWeight) {
        JSplitPane jsp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, cLeft, cRight);
        jsp.setOneTouchExpandable(true);
        jsp.setResizeWeight(dblResizeWeight);
        return (jsp);
    }

    public static JTabbedPane createMultiMemoryTableViewerTabbedPane(SwingAppInterface swingAppInterface, Vector<MemoryTable> vMt) {
        JTabbedPane jtp = new JTabbedPane();
        jtp.setFocusable(false);
        jtp.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        for (int i = 0; i < vMt.size(); i++) {
            String sTitle = vMt.elementAt(i).getName().trim();
            if (sTitle.length() == 0) {
                sTitle = Integer.toString(i);
            }
            jtp.addTab(sTitle, new MagTablePanel(swingAppInterface, "", vMt.elementAt(i)));
        }
        return (jtp);
    }
}
