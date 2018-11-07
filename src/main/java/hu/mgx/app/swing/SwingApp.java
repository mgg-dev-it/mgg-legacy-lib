package hu.mgx.app.swing;

import hu.mag.db.DatabaseInfo;
import hu.mag.db.FieldInfo;
import hu.mag.db.TableInfo;
import hu.mag.message.DefaultMessageHandler;
import hu.mag.message.MessageEvent;
import hu.mag.message.MessageEventListener;
import hu.mag.swing.MagComboBoxField;
import hu.mag.swing.MagTextField;
import hu.mag.swing.dialog.SelectFromMagTableDialog;
import hu.mag.swing.pdf.PdfViewerInternalFrame;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.beans.PropertyVetoException;
import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import java.text.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import hu.mgx.app.common.*;
import hu.mgx.db.*;
import hu.mgx.swing.*;
import hu.mgx.swing.maint.*;
import hu.mgx.swing.app.modul.*;
import hu.mgx.swing.table.MemoryTable;
import hu.mgx.util.*;
import hu.mgx.xml.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.print.attribute.standard.MediaSizeName;

public class SwingApp extends JFrame implements InternalFrameListener, ActionListener, SwingAppInterface, MouseListener {

    private String sAppName = "";
    private int iMajor = 0; //new-hoz nem kell
    private int iMinor = 0; //new-hoz nem kell
    private int iRevision = 0; //new-hoz nem kell
    private String sDateTimeVersion = "";
    private int iAppWidth = 1024;
    private int iAppHeight = 768;
    private SwingAppEventInterface appEventInterface = null;
    private Properties properties = null;
//    private Properties propVersion = null;
    private SimpleDateFormat logDateFormat = new SimpleDateFormat("yyyy.MM.dd kk:mm:ss.SSS");
    private SimpleDateFormat fileDateFormat = new SimpleDateFormat("yyyyMMdd");
    private JDesktopPane jDesktopPane;
    private JToolBar jToolBar;
    private JMenuBar jMenuBar;
    private PrinterJob printerJob;
    private PageFormat pageFormat;

    private JMenu windowMenu = null;
    private int iWindowMenuItemCount = 0;
    private ActionListener actionListenerForWindowMenu = null;
    private int iPlace = 0;
    private int iCurrentApplication = -1;
    private ArrayList<Integer> alApplications = null;
    private ArrayList<JMenu> alWindowMenus = null;
    private ArrayList<Integer> alWindowMenuItemCounts = null;
    private ArrayList<ActionListener> alActionListenersForWindowMenu = null;
    private ArrayList<Integer> alPlaces = null;
    private ArrayList<ArrayList<JInternalFrame>> alalJInternalFrames = null;

//    private String sPrintPreviewTitle = "Nyomtatási előkép";
    private ErrorBox errorBox = null;
    private Dimension dimScreen;
    private Connection connection = null;
    private String sUserName = "";
    private String sPassword = "";
    private int iUserID = 0;
    private String sNewPassword1 = "";
    private String sNewPassword2 = "";
    private FormatInterface formatInterface = new DefaultAppFormat();
    private Properties guiProperties;
//    private int iElements = 0;
//    private int iCurrentElement = 0;
//    private ProgressDisplay progressDisplay;
//    private javax.swing.Timer timer;
//    private Enumeration elements;
//    private String sName = "";
//    private String sValue = "";
//    private Properties saveproperties;
    //--- képzett paraméterek ---
    //--- saját változók ---
    private Toolkit toolkit = null;
    private PrintWriter logWriter = null;
    private String sLogFilename = null;
    private String sLogFilenameEnd = null;
    private AppTimerLogger appTimerLogger = null;
    //private DefaultLanguageHandler defaultLanguageHandler = new DefaultLanguageHandler();;

    public final static String DEVELOPER_VERSION = "[developer]";

    public SwingApp() {
        //this(sAppName, 0, 0, 0, 1024, 768);
        this.languageHandlerInterface = new DefaultLanguageHandler();
        this.defaultConnectionHandler = new DefaultConnectionHandler(this);
        this.defaultMessageHandler = new DefaultMessageHandler();
        //appInit("");
        appNewInit();
    }

//    public SwingApp(String sAppName)
//    {
//        this(sAppName, 0, 0, 0, 1024, 768);
//    }
//    public SwingApp(String sAppName, int iMajor, int iMinor, int iRevision, int iAppWidth, int iAppHeight)
//    {
//        this(sAppName, iMajor, iMinor, iRevision, iAppWidth, iAppHeight, null);
//    }
    public SwingApp(String sAppName, int iMajor, int iMinor, int iRevision, int iAppWidth, int iAppHeight, SwingAppEventInterface appEventInterface) {
        super();
        this.sAppName = sAppName;
        this.appEventInterface = appEventInterface;
        this.iMajor = iMajor;
        this.iMinor = iMinor;
        this.iRevision = iRevision;
        this.iAppWidth = iAppWidth;
        this.iAppHeight = iAppHeight;
        appInit(sAppName);
    }

    private void appInit(String sAppName) {
//        JFrame.setDefaultLookAndFeelDecorated(true);
        toolkit = Toolkit.getDefaultToolkit();
//        try {
//            sLogFilename = ".\\" + sAppName;
//            sLogFilenameEnd = "_" + fileDateFormat.format(new java.util.Date());
//            logWriter = new PrintWriter(new FileOutputStream(sLogFilename + sLogFilenameEnd + ".log", true));
//        } catch (FileNotFoundException e) {
//            System.out.println(e.getLocalizedMessage());
//            e.printStackTrace(System.out);
//        }
        sLogFilename = ".\\" + sAppName;
        sLogFilenameEnd = "_" + fileDateFormat.format(new java.util.Date());
        createLogFile();

        appTimerLogger = new AppTimerLogger(formatInterface, this, true, getVersionString(iMajor, iMinor, iRevision, sDateTimeVersion));

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                askAppExit();
            }
        });

        jMenuBar = new JMenuBar();
        jDesktopPane = new JDesktopPane();
        printerJob = PrinterJob.getPrinterJob();
        pageFormat = createA4PageFormat();
        guiProperties = new Properties();

        dimScreen = toolkit.getScreenSize();
        //jDesktopPane.setPreferredSize(new java.awt.Dimension(dimScreen.width-32, dimScreen.height-64));
        jDesktopPane.setPreferredSize(new java.awt.Dimension(iAppWidth - 32, iAppHeight - 64));
        jDesktopPane.setMinimumSize(new java.awt.Dimension(640, 480));

        getContentPane().add(jDesktopPane, java.awt.BorderLayout.CENTER);
        setJMenuBar(jMenuBar);
        setTitle(sAppName + (getVersionString(iMajor, iMinor, iRevision, sDateTimeVersion).equals("") ? "" : " - " + getVersionString(iMajor, iMinor, iRevision, sDateTimeVersion)));

        errorBox = new ErrorBox(this);
        pack();
        if (toolkit.isFrameStateSupported(JFrame.MAXIMIZED_BOTH)) {
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            //jDesktopPane.setPreferredSize(new java.awt.Dimension(dimScreen.width-32, dimScreen.height-64));
            this.setSize(new java.awt.Dimension(dimScreen.width - 16, dimScreen.height - 48));
            setLocation(8, 8);
        }
    }

    public void setTitleWithVersion() {
        setTitle(sAppName + (getVersionString(iMajor, iMinor, iRevision, sDateTimeVersion).equals("") ? "" : " - " + getVersionString(iMajor, iMinor, iRevision, sDateTimeVersion)));
        //JOptionPane.showMessageDialog(null, sDateTimeVersion);
        //JOptionPane.showMessageDialog(null, getVersionString(iMajor, iMinor, iRevision, sDateTimeVersion));
    }

    public void setTitleWithVersion(String sParam) {
        setTitle(sAppName + (getVersionString(iMajor, iMinor, iRevision, sDateTimeVersion).equals("") ? "" : " - " + getVersionString(iMajor, iMinor, iRevision, sDateTimeVersion)) + (sParam.equals("") ? "" : " - " + sParam));
    }

    public void setTitleWithVersionWithPrefix(String sPrefix) {
        sPrefix = StringUtils.isNull(sPrefix, "").trim();
        setTitle((sPrefix.equals("") ? "" : sPrefix + " - ") + sAppName + (getVersionString(iMajor, iMinor, iRevision, sDateTimeVersion).equals("") ? "" : " - " + getVersionString(iMajor, iMinor, iRevision, sDateTimeVersion)));
    }

    private PageFormat createA4PageFormat() {
        PageFormat localPageFormat = new PageFormat();
        Paper paper = new Paper();
        paper.setSize(595, 841);
        //paper.setImageableArea(10, 10, 575, 821);
        //paper.setImageableArea(36, 36, 523, 769);
        paper.setImageableArea(72, 72, 451, 697);
        localPageFormat.setPaper(paper);
        return (localPageFormat);
    }

    public String getVersionString(int iMajor, int iMinor, int iRevision, String sDateTimeVersion) {
        if (iMajor > 0 || iMinor > 0 || iRevision > 0) {
            return (Integer.toString(iMajor) + "." + StringUtils.right("00" + Integer.toString(iMinor), 2) + "." + StringUtils.right("000" + Integer.toString(iRevision), 3));
        }
        if (!StringUtils.isEmpty(sDateTimeVersion)) {
            return (sDateTimeVersion);
        }
        return ("");
    }

    public Dimension getScreenSize() {
        return (dimScreen);
    }

    public int getAppWidth() {
        return iAppWidth;
    }

    public int getAppHeight() {
        return iAppHeight;
    }

    public AppTimerLogger getAppTimerLogger() {
        return (appTimerLogger);
    }

    public void setAppTimerActive(boolean bActive) {
        appTimerLogger.setActive(bActive);
    }

    public boolean appExitQuestion() {
        String sTitle = getOldLanguageString(DefaultLanguageConstants.STRING_EXIT, "Kilépés");
        String sQuestion = getOldLanguageString(DefaultLanguageConstants.STRING_APP_EXIT_QUESTION, "Bezárja az alkalmazást?");
        YesNoDialog yesNoDialog = new YesNoDialog(this, languageHandlerInterface);
        return (yesNoDialog.showDialog(this, sTitle, sQuestion, YesNoDialog.NO) == YesNoDialog.YES);
    }

    public void askAppExit() {
        boolean bExitOK = true;
        if (appExitQuestion()) {
            if (canCloseCommonInternalFrames()) {
                closingCommonInternalFrames();
                if (appEventInterface != null) {
                    appEventInterface.appBeforeExit();
                }
                if (appEventInterface != null) {
                    bExitOK = appEventInterface.appExit();
                }
                if (bExitOK) {
                    appExit();
                }
            }
        }
    }

    public void appExit() {
        closeConnections();
        if (logWriter != null) {
            logWriter.close();
            deleteLogFileWhenEmpty();
        }
        dispose();
        System.exit(0);
    }

//    private void storeUserProperty(Connection connection, int iUserID, String sName, String sValue)
//    {
//        PreparedStatement preparedStatement;
//        try
//        {
//            preparedStatement = connection.prepareStatement("insert into usr_properties (usr, name, value) values (?, ?, ?)");
//            preparedStatement.setInt(1, iUserID);
//            preparedStatement.setString(2, sName);
//            preparedStatement.setString(3, sValue);
//            preparedStatement.execute();
//            preparedStatement.closing();
//        }
//        catch (SQLException sqle)
//        {
//            System.err.println(sqle.getLocalizedMessage());
//            sqle.printStackTrace(System.err);
//        }
//    }
//
//    private boolean storeUserProperties(JFrame jFrame, Properties properties)
//    {
//        if (connection == null)
//        {
//            return (false);
//        //appExit();
//        }
//
//        if (iUserID < 1)
//        {
//            return (false);
//        //appExit();
//        }
//
//        if (properties == null)
//        {
//            return (false);
//        //appExit();
//        }
//
//        this.saveproperties = properties;
//        elements = saveproperties.propertyNames();
//        while (elements.hasMoreElements())
//        {
//            elements.nextElement();
//            ++iElements;
//        }
//        progressDisplay = new ProgressDisplay(jFrame, 0, iElements + 1);
//        progressDisplay.showDialog(jFrame, "Kilépés", "Előkészítés");
//        //addToDesktopPane(progressDisplay);
//        PreparedStatement preparedStatement;
//        try
//        {
//            preparedStatement = connection.prepareStatement("delete from usr_properties where usr=?");
//            preparedStatement.setInt(1, iUserID);
//            preparedStatement.execute();
//        }
//        catch (SQLException sqle)
//        {
//            System.err.println(sqle.getLocalizedMessage());
//            sqle.printStackTrace(System.err);
//        }
//        //        System.err.println(iCurrentElement);
//        progressDisplay.setValue(++iCurrentElement);
//        //        progressDisplay.validate();
//
//        elements = properties.propertyNames();
//        progressDisplay.setMessage("Felhasználói beállítások mentése");
//        timer = new javax.swing.Timer(10, new ActionListener()
//                              {
//
//                                  public void actionPerformed(ActionEvent evt)
//                                  {
//                                      if (elements.hasMoreElements())
//                                      {
//                                          sName = elements.nextElement().toString();
//                                          sValue = saveproperties.getProperty(sName, "");
//                                          storeUserProperty(connection, iUserID, sName, sValue);
//                                          ++iCurrentElement;
//                                          //System.err.println(iCurrentElement);
//                                          progressDisplay.setValue(iCurrentElement);
//                                      }
//                                      else
//                                      {
//                                          timer.stop();
//                                          progressDisplay.setVisible(false);
//                                          appExit();
//                                      }
//                                  }
//                              });
//        timer.setRepeats(true);
//        timer.start();
//        return (true);
//    }
    private void closingCommonInternalFrames() {
        JInternalFrame internalFrames[] = jDesktopPane.getAllFrames();
        for (int iTmp = 0; iTmp < internalFrames.length; iTmp++) {
            ((CommonInternalFrame) internalFrames[iTmp]).closing();
        }
    }

    private boolean canCloseCommonInternalFrames() {
        JInternalFrame internalFrames[] = jDesktopPane.getAllFrames();
        for (int iTmp = 0; iTmp < internalFrames.length; iTmp++) {
            if (!((CommonInternalFrame) internalFrames[iTmp]).canClose()) {
                return (false);
            }
        }
        return (true);
    }

//    private boolean saveSettings()
//    {
//        return (storeUserProperties(this, guiProperties));
//    }
    public void addToDesktopPane(java.awt.Component c) {
        jDesktopPane.add(c);
        jDesktopPane.validate();
    }

    public void removeFromDesktopPane(java.awt.Component c) {
        jDesktopPane.remove(c);
        // kell ez ? jDesktopPane.validate();
    }

    public boolean isInternalFrameExists(String sTitle) {
        JInternalFrame internalFrames[] = jDesktopPane.getAllFrames();
        for (int i = 0; i < internalFrames.length; i++) {
            if (internalFrames[i].getTitle().equals(sTitle)) {
                return (true);
            }
        }
        return (false);
    }

    public boolean selectInternalFrameIfExists(String sTitle) {
        JInternalFrame internalFrames[] = jDesktopPane.getAllFrames();
        for (int i = 0; i < internalFrames.length; i++) {
            if (internalFrames[i].getTitle().equals(sTitle)) {
                try {
                    internalFrames[i].setSelected(true);
                } catch (PropertyVetoException e) {
                    handleError(e, 0);
                }
                return (true);
            }
        }
        return (false);
    }

    public JInternalFrame getInternalFrameByTitle(String sTitle) {
        JInternalFrame internalFrames[] = jDesktopPane.getAllFrames();
        for (int i = 0; i < internalFrames.length; i++) {
            if (internalFrames[i].getTitle().equals(sTitle)) {
                return (internalFrames[i]);
            }
        }
        return (null);
    }

    public void setInternalFrameSelectedByTitle(String sTitle) {
        JInternalFrame internalFrame = getInternalFrameByTitle(sTitle);
        if (internalFrame != null) {
            try {
                internalFrame.setSelected(true);
            } catch (PropertyVetoException e) {
                handleError(e, 0);
            }
        }
    }

    @Override
    public JInternalFrame[] getInternalFrames() {
        return (jDesktopPane.getAllFrames());
    }

    public void addInternalFrameMaximized(JInternalFrame f, String sActionCommand) {
        JInternalFrame f2 = addInternalFrame(f, sActionCommand);
        try {
            if (f2 != null) {
                f.setMaximum(true);
            }
        } catch (java.beans.PropertyVetoException ex) {
        }
    }

    public boolean setInternalFrameMaximizedByTitle(String sTitle) {
        JInternalFrame f = getInternalFrameByTitle(sTitle);
        if (f == null) {
            return (false);
        }
        try {
            f.setMaximum(true);
        } catch (java.beans.PropertyVetoException ex) {
            return (false);
        }
        return (true);
    }

    public JInternalFrame addInternalFrame(JInternalFrame f, String sActionCommand) {
        int iWindowSerial;
        String sMenuItemPrefix;
        int iMnemonic;

        JInternalFrame internalFrames[] = jDesktopPane.getAllFrames();
        for (int i = 0; i < internalFrames.length; i++) {
            if (internalFrames[i].getTitle().equals(f.getTitle())) {
                try {
                    internalFrames[i].setSelected(true);
                } catch (PropertyVetoException e) {
                    handleError(e, 0);
                }
                return (null);
            }
        }
        //@todo a már nem használt helyeket töltse fel a program, különben a végén minden form a képernyő alján lesz!!!
        if (internalFrames.length == 0) {
            iPlace = 0;
        }
        //f.setLocation(iPlace * 32, iPlace * 32);
        f.setLocation(iPlace * 28, iPlace * 28); //MaG 2018.04.03.
        //f.setLocation(iPlace * 8, iPlace * 8);
        ++iPlace;
        if (iPlace > 10) {
            iPlace = 0;
        }
        //if ((iPlace*32)>(jDesktopPane.getHeight()-32)) iPlace=0;

        if (this.getIconImage() != null) {
            f.setFrameIcon(new ImageIcon(this.getIconImage())); //2015.09.28.
        }

        addToDesktopPane(f);
        f.addInternalFrameListener(this);
        if (windowMenu != null) {
            iWindowSerial = windowMenu.getItemCount() - iWindowMenuItemCount + 1;
            if ((iWindowSerial < 0) || (iWindowSerial > 9)) {
                sMenuItemPrefix = "   ";
                iMnemonic = 0;
            } else {
                sMenuItemPrefix = Integer.toString(iWindowSerial) + ". ";
                iMnemonic = KeyEvent.VK_0 + iWindowSerial;
            }
            addMenuItem(sMenuItemPrefix + f.getTitle(), iMnemonic, null, windowMenu, actionListenerForWindowMenu, sActionCommand);
        }
        //2015.03.16.
        try {
            f.setSelected(true);
        } catch (PropertyVetoException e) {
            handleError(e, 0);
        }
        setStatusBarWindowInfo();
        return (f);
    }

    public void placeInternalFrames() {
        int iDiff = 28;
        iPlace = 0;
        JInternalFrame internalFrames[] = jDesktopPane.getAllFrames();
        for (int i = 0; i < internalFrames.length; i++) {
            internalFrames[i].setLocation(iPlace * iDiff, iPlace * iDiff);
            internalFrames[i].moveToFront();
            ++iPlace;
            if ((iPlace * iDiff) > (jDesktopPane.getHeight() - iDiff)) {
                iPlace = 0;
            }
        }
    }

    public void closeAllInternalFrame() {
        JInternalFrame internalFrames[] = jDesktopPane.getAllFrames();
        for (int i = internalFrames.length; i > 0; i--) {
            //internalFrames[i-1].hide();
            internalFrames[i - 1].dispose();
        }
    }

    public void deleteInternalFrame(JInternalFrame f) {
        JInternalFrame internalFrames[] = jDesktopPane.getAllFrames();
        for (int i = 0; i < internalFrames.length; i++) {
            if (internalFrames[i].equals(f)) {
                internalFrames[i].dispose();
                internalFrames[i] = null;
            }
        }
    }
//    public void hideAllInternalFrame() {
//        JInternalFrame internalFrames[] = jDesktopPane.getAllFrames();
//        for (int i = internalFrames.length; i > 0; i--) {
//            internalFrames[i - 1].hide();
//        }
//    }
//    public void showAllInternalFrame() {
//        JInternalFrame internalFrames[] = jDesktopPane.getAllFrames();
//        for (int i = internalFrames.length; i > 0; i--) {
//            internalFrames[i - 1].show();
//        }
//    }

    public void showMaintInternalFrame(DataBase db, String sTable, String sTitle, String sActionCommand, int iUserID, boolean bInsert, boolean bUpdate, boolean bDelete, boolean bExcelExport, int iSortByColumn, MaintPanelExtensionInterface maintPanelExtensionInterface) {
        MaintInternalFrame mif;
        sTitle = sTitle.replace("&", "");
        if (!selectInternalFrameIfExists(sTitle)) {
            mif = new MaintInternalFrame(db, sTable, this, iUserID, bInsert, bUpdate, bDelete, bExcelExport, iSortByColumn, maintPanelExtensionInterface);
            mif.setTitle(sTitle);
            mif.pack();
            addInternalFrame(mif, sActionCommand);
            mif.setVisible(true);
        }
    }

    public void showMaintInternalFrame(CONN Conn, TableDefinition td, String sTitle, String sActionCommand, int iUserID, boolean bInsert, boolean bUpdate, boolean bDelete, boolean bExcelExport, int iSortByColumn, MaintPanelExtensionInterface maintPanelExtensionInterface) {
        MaintInternalFrame mif;
        sTitle = sTitle.replace("&", "");
        if (!selectInternalFrameIfExists(sTitle)) {
            mif = new MaintInternalFrame(Conn, td, this, iUserID, bInsert, bUpdate, bDelete, bExcelExport, iSortByColumn, maintPanelExtensionInterface);
            mif.setTitle(sTitle);
            mif.pack();
            addInternalFrame(mif, sActionCommand);
            mif.setVisible(true);
        }
    }

    public void addApplication(int iApplicationCode) {
        this.addApplication(iApplicationCode, null, null);
    }

    public void addApplication(int iApplicationCode, JMenu jmWindow, ActionListener al) {
        if (alApplications == null) {
            alApplications = new ArrayList<Integer>();
            alWindowMenus = new ArrayList<JMenu>();
            alWindowMenuItemCounts = new ArrayList<Integer>();
            alActionListenersForWindowMenu = new ArrayList<ActionListener>();
            alPlaces = new ArrayList<Integer>();
            alalJInternalFrames = new ArrayList<ArrayList<JInternalFrame>>();
        }
        if (!alApplications.contains(new Integer(iApplicationCode))) {
            alApplications.add(new Integer(iApplicationCode));
            alWindowMenus.add(jmWindow); // null volt
            if (al != null) {
                setWindowMenu(jmWindow, al);
            }
            alWindowMenuItemCounts.add(new Integer(0));
            alActionListenersForWindowMenu.add(al); // null volt
            alPlaces.add(new Integer(0));
            alalJInternalFrames.add(new ArrayList<JInternalFrame>());
        }
    }

    public int getCurrentApplication() {
        return (iCurrentApplication);
    }

    public void changeApplication(int iApplicationCode) {
        addApplication(iApplicationCode);

        //only if changed...
        if (iCurrentApplication == iApplicationCode) {
            return;
        }

        //save current application's data
        if (iCurrentApplication > -1) {
            if (alApplications.contains(new Integer(iCurrentApplication))) {
                alWindowMenus.set(iCurrentApplication, windowMenu);
                alWindowMenuItemCounts.set(iCurrentApplication, new Integer(iWindowMenuItemCount));
                alActionListenersForWindowMenu.set(iCurrentApplication, actionListenerForWindowMenu);
                alPlaces.set(iCurrentApplication, new Integer(iPlace));

                ArrayList<JInternalFrame> alOtherJInternalFrames = new ArrayList<JInternalFrame>();
                for (int i = 0; i < alalJInternalFrames.size(); i++) {
                    if (i != iCurrentApplication) {
                        for (int j = 0; j < alalJInternalFrames.get(i).size(); j++) {
                            alOtherJInternalFrames.add(alalJInternalFrames.get(i).get(j));
                        }
                    }
                }
                ArrayList<JInternalFrame> alCurrentJInternalFrames = new ArrayList<JInternalFrame>();
                JInternalFrame jInternalFrames[] = jDesktopPane.getAllFrames();
                for (int i = jInternalFrames.length - 1; i > -1; i--) {
                    if (!alOtherJInternalFrames.contains(jInternalFrames[i])) {
                        alCurrentJInternalFrames.add(jInternalFrames[i]);
                        jInternalFrames[i].hide();
                        //System.out.println("hide " + jInternalFrames[i].getTitle());
                    }
                }
                alalJInternalFrames.set(iCurrentApplication, alCurrentJInternalFrames);
            }
            //MaG 2016.10.13.}

            //load new application's data
            iCurrentApplication = iApplicationCode;
            windowMenu = alWindowMenus.get(iCurrentApplication);
            iWindowMenuItemCount = alWindowMenuItemCounts.get(iCurrentApplication).intValue();
            actionListenerForWindowMenu = alActionListenersForWindowMenu.get(iCurrentApplication);
            iPlace = alPlaces.get(iCurrentApplication).intValue();
            ArrayList<JInternalFrame> alJInternalFrames = alalJInternalFrames.get(iCurrentApplication);
            for (int i = 0; i < alJInternalFrames.size(); i++) {
                alJInternalFrames.get(i).show();
                //System.out.println("show " + alJInternalFrames.get(i).getTitle());
            }
        }//MaG 2016.10.13.
        else {
            iCurrentApplication = iApplicationCode;
            windowMenu = alWindowMenus.get(iCurrentApplication);
            iWindowMenuItemCount = alWindowMenuItemCounts.get(iCurrentApplication).intValue();
            actionListenerForWindowMenu = alActionListenersForWindowMenu.get(iCurrentApplication);
            iPlace = alPlaces.get(iCurrentApplication).intValue();
        }
    }

    public String getMenuText(String sName, ResourceBundle resourceBundle) {
        return (resourceBundle.getString(sName + "_Text"));
    }

    public int getMenuMnemonic(String sName, ResourceBundle resourceBundle) {
        if (resourceBundle.getString(sName + "_Mnemonic").equals("")) {
            return (0);
        }
        return (((Integer) (resourceBundle.getObject(sName + "_Mnemonic"))).intValue());
    }

    public KeyStroke getMenuAccelerator(String sName, ResourceBundle resourceBundle) {
        if (resourceBundle.getString(sName + "_Accelerator").equals("")) {
            return (null);
        }
        return ((KeyStroke) resourceBundle.getObject(sName + "_Accelerator"));
    }

    public MenuDef getMenuDef(String sName, ResourceBundle resourceBundle) {
        return ((MenuDef) resourceBundle.getObject("Menu_" + sName));
    }

    public void addMenuHorizontalGlue() {
        jMenuBar.add(Box.createHorizontalGlue());
    }

    public JMenu addMenu(String sText, int iMnemonic) {
        JMenu jMenuTmp = new JMenu();
        jMenuTmp.setText(sText);
        if (iMnemonic > 0) {
            jMenuTmp.setMnemonic(iMnemonic);
        }
        jMenuBar.add(jMenuTmp);
        return (jMenuTmp);
    }

    public JMenu addMenuResource(String sMenu, ResourceBundle r) {
        return (addMenu(getMenuDef(sMenu, r).getText(), getMenuDef(sMenu, r).getMnemonic()));
    }

    public JMenuItem addMenuItem(String sText, int iMnemonic, KeyStroke keyStroke, JMenu jMenuParent, ActionListener l, String sActionCommand) {
        JMenuItem jMenuItemTmp = new JMenuItem();
        jMenuItemTmp.setText(sText);
        jMenuItemTmp.setActionCommand(sActionCommand);
        jMenuParent.add(jMenuItemTmp);
        if (iMnemonic > 0) {
            jMenuItemTmp.setMnemonic(iMnemonic);
        }
        if (keyStroke != null) {
            jMenuItemTmp.setAccelerator(keyStroke);
        }
        jMenuItemTmp.addActionListener(l);
        return (jMenuItemTmp);
    }

    public JMenuItem addMenuItem(String sText, int iMnemonic, KeyStroke keyStroke, JMenu jMenuParent, ActionListener l) {
        return (addMenuItem(sText, iMnemonic, keyStroke, jMenuParent, l, sText));
    }

    public JMenuItem addMenuItemResource(String sMenu, JMenu jMenuParent, ActionListener l, ResourceBundle r) {
        //return (addMenuItem(getMenuText("Menu_"+sMenu, r), getMenuMnemonic("Menu_"+sMenu, r), getMenuAccelerator("Menu_"+sMenu, r), jMenuParent, l,sMenu));
        return (addMenuItem(getMenuDef(sMenu, r).getText(), getMenuDef(sMenu, r).getMnemonic(), getMenuDef(sMenu, r).getKeyStroke(), jMenuParent, l, sMenu));
    }

    public JSeparator addMenuSeparator(JMenu jMenuParent) {
        JSeparator jSeparatorTmp = new JSeparator();
        jMenuParent.add(jSeparatorTmp);
        return (jSeparatorTmp);
    }

    public void setWindowMenu(JMenu m, ActionListener l) {
        windowMenu = m;
        actionListenerForWindowMenu = l;
        iWindowMenuItemCount = 0; //windowMenu.getItemCount(); //@@@todo 1-től induljon ... !!!
    }

    public void loadProperties() {
        loadProperties(sAppName);
    }

    public void loadProperties(String sAppName) {
        loadProperties(sAppName, ".");
    }

    public void loadProperties(String sAppName, String sDirectory) {
        properties = new Properties();
        try {
            FileInputStream in = new FileInputStream(sDirectory + "\\" + sAppName + ".properties");
            properties.load(in);
            in.close();
        } catch (IOException e) {
            handleError(e, 9);
        }
    }

    @Override
    public String getDateTimeVersion() {
        return (sDateTimeVersion);
    }

    public void setDateTimeVersion(String sDTV) {
        this.sDateTimeVersion = sDTV;
        if (!StringUtils.isEmpty(sDTV)) {
            this.iMajor = 0;
            this.iMinor = 0;
            this.iRevision = 0;
            appParameters.setMajor(0);
            appParameters.setMinor(0);
            appParameters.setRevision(0);
        }
    }

    public void storeProperties() {
        storeProperties(sAppName);
    }

    public void storeProperties(String sAppName) {
        storeProperties(sAppName, ".");
    }

    public void storeProperties(String sAppName, String sDirectory) {
        if (properties == null) {
            properties = new Properties();
        }
        try {
            FileOutputStream out = new FileOutputStream(sDirectory + "\\" + sAppName + ".properties");
            properties.store(out, sAppName);
            out.close();
        } catch (IOException e) {
            handleError(e);
        }
    }

    public String getProperty(String sKey) {
        return (properties.getProperty(sKey));
    }

    public String getProperty(String sKey, String sDefaultValue) {
        return (properties.getProperty(sKey, sDefaultValue));
    }

    public Vector<String> getPropertyNames(String sPrefix) {
        Vector v = new Vector();
        String s = "";
        Enumeration e = properties.propertyNames();
        while (e.hasMoreElements()) {
            s = e.nextElement().toString();
            if (s.startsWith(sPrefix)) {
                v.add(s);
            }
        }
        return (v);
    }

    public void setProperty(String sKey, String sValue) {
        properties.setProperty(sKey, sValue);
    }

    public void removeProperty(String sKey) {
        properties.remove(sKey);
    }

    public PageFormat pageSetup() {
        pageFormat = printerJob.pageDialog(pageFormat);
        //        System.err.println(pageFormat.getWidth());
        //        System.err.println(pageFormat.getHeight());
        //        System.err.println(pageFormat.getImageableX());
        //        System.err.println(pageFormat.getImageableY());
        //        System.err.println(pageFormat.getImageableWidth());
        //        System.err.println(pageFormat.getImageableHeight());
        return (pageFormat);
    }

    public PageFormat getPageFormat() {
        return (pageFormat);
    }

    public void setPageFormat(PageFormat pageFormat) {
        this.pageFormat = pageFormat;

    }

    //    public void printBook(Book book){
    //        printerJob.setPageable(book);
    //        try{
    //            printerJob.print();
    //        }
    //        catch (Exception e){
    //            System.err.println(e.getLocalizedMessage());
    //            e.printStackTrace(System.err);
    //        }
    //    }
    //    public void printPreview(String sActionCommand) {
    //        PrintPreviewInternalFrame ppif;
    //        JInternalFrame selectedFrame = jDesktopPane.getSelectedFrame();
    //        if ((selectedFrame!=null) && !(selectedFrame instanceof PrintPreviewInternalFrame)) {
    //            if (isInternalFrameExists(sPrintPreviewTitle)){
    //                ppif = (PrintPreviewInternalFrame)getInternalFrameByTitle(sPrintPreviewTitle);
    //            }
    //            else{
    //                ppif = new PrintPreviewInternalFrame(pageFormat);
    //                ppif.setTitle(sPrintPreviewTitle);
    //                addInternalFrame(ppif, sActionCommand);
    //                ppif.pack();
    //            }
    //            ppif.setVisible(true);
    //            ppif.preview(selectedFrame, pageFormat);
    //            //PrintUtilities.printComponent(selectedFrame, pageFormat);
    //        }
    //    }
    public void print() {
        JInternalFrame selectedFrame = jDesktopPane.getSelectedFrame();
        if (selectedFrame != null) {
            PrintUtilities.printComponent(selectedFrame, pageFormat);
        }
    }

    @Override
    public void internalFrameActivated(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameClosed(InternalFrameEvent e) {
        boolean bFound = false;
        int iItemCount = 0;
        int iFoundItem = 0;
        String sTmp;
        int iMnemonic;
        if (windowMenu != null) {
            iItemCount = windowMenu.getItemCount();
            for (int i = 0; i < iItemCount; i++) {
                if (bFound) {
                    sTmp = windowMenu.getItem(i).getText();
                    sTmp = (char) (sTmp.charAt(0) - 1) + sTmp.substring(1);
                    windowMenu.getItem(i).setText(sTmp);
                    iMnemonic = KeyEvent.VK_0 + (int) (sTmp.charAt(0)) - (int) ('0');
                    windowMenu.getItem(i).setMnemonic(iMnemonic);
                } else {
                    if (windowMenu.getItem(i) instanceof JMenuItem) {
                        //if (windowMenu.getItem(i).getText().endsWith(e.getInternalFrame().getTitle())) {
                        if (windowMenu.getItem(i).getText().endsWith(StringUtils.isNull(e.getInternalFrame().getTitle(), ""))) {
                            iFoundItem = i;
                            bFound = true;
                        }
                    }
                }
            }
            if (bFound) {
                windowMenu.remove(iFoundItem);
            }
        }
        //e.getInternalFrame().removeInternalFrameListener(this); //MaG 2017.10.25.
        //MaG 2017.10.25.
        InternalFrameListener ifl[] = e.getInternalFrame().getInternalFrameListeners();
        for (int i = 0; i < ifl.length; i++) {
            e.getInternalFrame().removeInternalFrameListener(ifl[i]);
        }
        removeFromDesktopPane(e.getInternalFrame()); //2005.05.13.

        if (jDesktopPane.getSelectedFrame() != null) {
            jDesktopPane.getSelectedFrame().requestFocus();
        }
        //if (jDesktopPane.getSelectedFrame() != null) jDesktopPane.getSelectedFrame().restoreSubcomponentFocus();
        //if (jDesktopPane.getSelectedFrame() != null) jDesktopPane.getSelectedFrame().show();
        if (appEventInterface != null) {
            appEventInterface.appInternalFrameClosed(e);
        }
        setStatusBarWindowInfo();
        System.gc();
    }

    public void setSelectedFrameByTitle(String sTitle) {
        JInternalFrame internalFrames[] = jDesktopPane.getAllFrames();
        for (int i = 0; i < internalFrames.length; i++) {
            if (internalFrames[i].getTitle().equals(sTitle)) {
                try {
                    internalFrames[i].setSelected(true);
//                    jDesktopPane.setSelectedFrame(internalFrames[i]);
                } catch (PropertyVetoException e) {
                    handleError(e, 0);
                }
                return;
            }
        }
    }

    @Override
    public void internalFrameClosing(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameDeactivated(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameDeiconified(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameIconified(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameOpened(InternalFrameEvent e) {
    }

    public boolean loginDialog() {
        LoginDialog loginDialog = new LoginDialog(this, this, languageHandlerInterface);
        if (loginDialog.showLoginDialog(this, sUserName, sPassword) == LoginDialog.YES) {
            sUserName = loginDialog.getUserName();
            sPassword = loginDialog.getPassword();
            return (true);
        } else {
            sUserName = "";
            sPassword = "";
            return (false);
        }
    }

    public boolean passwordChangeDialog() {
        LoginDialog loginDialog = new LoginDialog(this, this, languageHandlerInterface);
        sPassword = "";
        if (loginDialog.showChangePasswordDialog(this, sUserName, sPassword) == LoginDialog.YES) {
            sUserName = loginDialog.getUserName();
            sPassword = loginDialog.getPassword();
            sNewPassword1 = loginDialog.getNewPassword1();
            sNewPassword2 = loginDialog.getNewPassword2();
            return (true);
        } else {
            return (false);
        }
    }

    public Connection getConnection() {
        return (connection);
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public java.lang.String getUserName() {
        return sUserName;
    }

    public void setUserName(String sUserName) {
        this.sUserName = sUserName;
    }

    public int getUserID() {
        return (iUserID);
    }

    public void setUserID(int iUserID) {
        this.iUserID = iUserID;
    }

    public java.lang.String getPassword() {
        return sPassword;
    }

    public void setPassword(String sPassword) {
        this.sPassword = sPassword;
    }

    public java.lang.String getNewPassword1() {
        return sNewPassword1;
    }

    public java.lang.String getNewPassword2() {
        return sNewPassword2;
    }

    public FormatInterface getFormatInterface() {
        return (formatInterface);
    }

    public LanguageHandlerInterface getLanguageHandlerInterface() {
        return (languageHandlerInterface);
    }

    public Object setGUIProperty(String sKey, String sValue) {
        return (guiProperties.setProperty(sKey, sValue));
    }

    public Object setGUIPropertyIfMissing(String sKey, String sValue) {
        if (guiProperties.containsKey(sKey)) {
            return (guiProperties.getProperty(sKey));
        }
        return (guiProperties.setProperty(sKey, sValue));
    }

    public String getGUIProperty(String sKey) {
        return (guiProperties.getProperty(sKey));
    }

    public String getGUIProperty(String sKey, String sDefaultValue) {
        return (guiProperties.getProperty(sKey, sDefaultValue));
    }

    public void setGUIProperties(Properties properties) {
        this.guiProperties = properties;
    }

    public Properties getGUIProperties() {
        return (guiProperties);
    }

    //-------------------------
    //--- temporary methods ---
    //-------------------------
    public void setLanguageHandlerInterface(LanguageHandlerInterface lhi) throws java.lang.NullPointerException {
        if (lhi == null) {
            throw (new java.lang.NullPointerException());
        }
        this.languageHandlerInterface = lhi;
    }
    //----------------------
    //--- new properties ---
    //----------------------
    private String sLanguage = "";
    private String sCountry = "";
    private Locale locale = null;
    //2011.05.17. private ResourceBundle resources = null;
    private AppParameters appParameters = null;
    private Runtime runtime;
    private CommonPanel statusBar = null;
    private JEditorPane jEditorPaneLog = null;
    private JScrollPane jScrollPaneLog = null;
    private JLabel jLabelLog = null;
    private javax.swing.Timer timerClock = null;
    private javax.swing.Timer timerSplash = null;
    private JLabel jLabelClock = null;
    private JLabel jLabelMemory = null;
    private String sLabelMemoryToolTipText = null;
    private String sLabelMemoryToolTipTextConnectionInfo = null;
    private boolean bMemoryDisplayMode = false;
    private JLabel jLabelStatus = null;
    private DecimalFormat memoryFormat = new DecimalFormat("#,##0");
    private JLabel jLabelIP = null;
    private InetAddress inetAddress = null;
    private JProgressBar jProgressBarProgress = null;
    private JLabel jLabelWindow = null;
    private CommonPanel infoBar = null;
    private JLabel jLabelInfo = null;
    private CommonPanel cpControlBar = null;
    private Image imageSplash = null;
    private AppSplash appSplash = null;
    private String sImageName = "";
    private Class classImage = null;
    //--- modules ---
    private LanguageHandlerInterface languageHandlerInterface = null;
    private DefaultConnectionHandler defaultConnectionHandler = null;
    private DefaultMessageHandler defaultMessageHandler = null;
    private AppModulAuthorizationInterface appModulAuthorization = null;
    private PermissionInterface permissionHandler = null;
    private LoggerInterface loggerInterface = null;
    private boolean bNoSQLLog = false;
    private boolean bSilent = false;
    private int iCursorWaitCount = 0;
    private int iLogLevelLimit = LOG_INFO;
    private int iLogType = TYPE_NORMAL;
    //--- XML related ---
    private DefaultXMLHandler defaultXMLHandler = null;

    private HashMap<String, JMenuItem> hmMenus = null;

    private HashMap<String, Object> hmGlobals = new HashMap<String, Object>();

    private Color colorOldBackground = null;

    //-------------------
    //--- new methods ---
    //-------------------
    public SwingApp(AppParameters appParameters) throws java.lang.NullPointerException {
        if (appParameters == null) {
            throw (new java.lang.NullPointerException());
        }
        this.appParameters = appParameters;
        this.languageHandlerInterface = new DefaultLanguageHandler();
        this.defaultConnectionHandler = new DefaultConnectionHandler(this);
        this.defaultMessageHandler = new DefaultMessageHandler();
        appNewInit();
    }

    public SwingApp(AppParameters appParameters, LanguageHandlerInterface lhi, SwingAppEventInterface appEventInterface) throws java.lang.NullPointerException {
        if (appParameters == null) {
            throw (new java.lang.NullPointerException());
        }
        if (lhi == null) {
            throw (new java.lang.NullPointerException());
        }
        if (appEventInterface == null) {
            throw (new java.lang.NullPointerException());
        }
        this.appParameters = appParameters;
        this.languageHandlerInterface = lhi;
        this.appEventInterface = appEventInterface;
        this.defaultConnectionHandler = new DefaultConnectionHandler(this);
        this.defaultMessageHandler = new DefaultMessageHandler();
        appNewInit();
    }

    private void appNewInit() {
        sAppName = appParameters.getAppName();
        runtime = Runtime.getRuntime();
        try {
            inetAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException uhe) {
            //System.err.println(uhe.getLocalizedMessage());
            //uhe.printStackTrace(System.err);
        }
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                askAppExit();
            }
        });

        toolkit = Toolkit.getDefaultToolkit();
        //ezt is xml-ből kellene:
//        if (!appParameters.getLogFileName().equals(""))
//        {
//        try {
//            sLogFilename = ".\\" + sAppName;
//            sLogFilenameEnd = "_" + fileDateFormat.format(new java.util.Date());
//            logWriter = new PrintWriter(new FileOutputStream(sLogFilename + sLogFilenameEnd + ".log", true));
//        } catch (FileNotFoundException e) {
//            System.out.println(e.getLocalizedMessage());
//            e.printStackTrace(System.out);
//        }
        if (!appParameters.getLogFileName().equalsIgnoreCase("")) {
            sLogFilename = ".\\" + sAppName;
            sLogFilenameEnd = "_" + fileDateFormat.format(new java.util.Date());
            createLogFile();
        }
//        }

        getContentPane().add(jDesktopPane = new JDesktopPane(), java.awt.BorderLayout.CENTER);
        setJMenuBar(jMenuBar = new JMenuBar());
        setTitleBasedOnLanguage();

        printerJob = PrinterJob.getPrinterJob();
        pageFormat = createA4PageFormat();
        guiProperties = new Properties();
        errorBox = new ErrorBox(this);

        iAppWidth = appParameters.getAppWidth();
        iAppHeight = appParameters.getAppHeight();
        dimScreen = toolkit.getScreenSize();
        //jDesktopPane.setPreferredSize(new java.awt.Dimension(dimScreen.width-32, dimScreen.height-64));
        jDesktopPane.setPreferredSize(new java.awt.Dimension(iAppWidth - 32, iAppHeight - 64));
        jDesktopPane.setMinimumSize(new java.awt.Dimension(640, 480));
        this.setMinimumSize(new java.awt.Dimension(1000, 700));

        //jDesktopPane.setBackground(Color.white); //MAG 2015-04-27 for Windows laf
        pack();
        if (toolkit.isFrameStateSupported(JFrame.MAXIMIZED_BOTH)) {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            setSize(new java.awt.Dimension(dimScreen.width - 16, dimScreen.height - 48));
            setLocation(8, 8);
        }

        //iAppWidth=new Double(getSize().getWidth()).intValue(); //2009.12.17
        //iAppHeight=new Double(getSize().getHeight()).intValue(); //2009.12.17
        properties = new Properties();
        if (!appParameters.getPropertyFileName().equals("")) {
            if (new java.io.File(appParameters.getPropertyFileName()).exists()) {
                loadProperties(properties, appParameters.getPropertyFileName());
            } else {
                properties.setProperty("language", "hu");
                properties.setProperty("country", "HU");
                storeProperties(properties, appParameters.getPropertyFileName());
            }
        }
        if (properties.getProperty("language", "").equals("") || properties.getProperty("country", "").equals("")) {
            properties.setProperty("language", "hu");
            properties.setProperty("country", "HU");
        }
        setLocale(properties.getProperty("language"), properties.getProperty("country"));
    }

    @Override
    public void setGlobal(String sKey, Object oValue) {
        hmGlobals.put(sKey, oValue);
    }

    @Override
    public Object getGlobal(String sKey) {
        return (hmGlobals.get(sKey));
    }

    @Override
    public String getGlobalString(String sKey) {
        return (StringUtils.isNull(hmGlobals.get(sKey), ""));
    }

    @Override
    public String getGlobalStringIsNull(String sKey, String sIfNull) {
        return (StringUtils.isNull(hmGlobals.get(sKey), sIfNull));
    }

    @Override
    public Integer getGlobalInteger(String sKey) {
        return (IntegerUtils.convertToInteger(hmGlobals.get(sKey)));
    }

    public void setMenuEnabledByName(String sName, boolean bEnabled) {
        JMenuItem mi = hmMenus.get(sName);
        if (mi != null) {
            mi.setEnabled(bEnabled);
        }
    }

    public boolean isMenuEnabledByName(String sName) {
        JMenuItem mi = hmMenus.get(sName);
        if (mi != null) {
            return (mi.isEnabled());
        }
        return (false);
    }

    public void setMenuEnabledAll(boolean bEnabled) {
        String[] sArray = hmMenus.keySet().toArray(new String[0]);
        for (int i = 0; i < hmMenus.size(); i++) {
            hmMenus.get(sArray[i]).setEnabled(bEnabled);
        }
    }

    public void setMenuVisibleByName(String sName, boolean bVisible) {
        JMenuItem mi = hmMenus.get(sName);
        if (mi != null) {
            mi.setVisible(bVisible);
        }
    }

    public void setMenuVisibleAll(boolean bVisible) {
        String[] sArray = hmMenus.keySet().toArray(new String[0]);
        for (int i = 0; i < hmMenus.size(); i++) {
            hmMenus.get(sArray[i]).setVisible(bVisible);
        }
    }

    private void createMenus(XMLElement menu, JMenu jmenuParent, ActionListener actionListener) {
        CommonMenu cm = null;
        CommonMenuItem cmi = null;
        Vector<XMLElement> vMenuItem = defaultXMLHandler.getElements(menu, "menuitem", false);
        for (int j = 1; j < vMenuItem.size(); j++) {
            if (vMenuItem.elementAt(j).getAttribute("type").equals("separator")) {
                addMenuSeparator(jmenuParent);
                return;
            }
            //System.out.println(vMenuItem.elementAt(j).getAttribute("name"));
            //if (vMenuItem.elementAt(j).getAttribute("name").equals("mnuOMV")) {
            //    System.out.println("");
            //}
            if (vMenuItem.elementAt(j).hasChild()) {
                cm = this.addSubMenu(vMenuItem.elementAt(j).getAttribute("name"), jmenuParent, actionListener, vMenuItem.elementAt(j).getAttribute("action"));
                if (vMenuItem.elementAt(j).getAttribute("enabled").equals("false")) {
                    cm.setEnabled(false);
                }
                if (vMenuItem.elementAt(j).getAttribute("visible").equals("false")) {
                    cm.setVisible(false);
                }
                hmMenus.put(vMenuItem.elementAt(j).getAttribute("name"), cm);
                createMenus(vMenuItem.elementAt(j), cm, actionListener);
            } else {
                cmi = this.addMenuItem(vMenuItem.elementAt(j).getAttribute("name"), jmenuParent, actionListener, vMenuItem.elementAt(j).getAttribute("action"));
                if (vMenuItem.elementAt(j).getAttribute("enabled").equals("false")) {
                    cmi.setEnabled(false);
                }
                if (vMenuItem.elementAt(j).getAttribute("visible").equals("false")) {
                    cmi.setVisible(false);
                }
                hmMenus.put(vMenuItem.elementAt(j).getAttribute("name"), cmi);
            }
        }
    }

    public void initXML(AppInterface appInterface, String sXMLConfig, String sEncoding, ActionListener actionListener) {
        CommonMenu commonMenu = null;
        String sLang = "";
        String sKey = "";
        String sItem = "";
        String sAppIcon = "";
        String sLogType = "";
        defaultXMLHandler = new DefaultXMLHandler(appInterface);
        defaultXMLHandler.readXMLString("config", sXMLConfig, sEncoding);

        //app
        XMLElement xmlApp = defaultXMLHandler.getFirstElement("config", "app");
        sAppIcon = xmlApp.getAttribute("icon");
        ImageIcon imageIcon = new ImageIcon(sAppIcon);
        setIconImage(imageIcon.getImage());
        //java.net.URL imageURL = Main.class.getResource("images/splash_512_316.jpg");
        //if (imageURL != null) {
        //    app.setIconImage(Toolkit.getDefaultToolkit().getImage(imageURL));
        //}

        //log
        XMLElement xmlLog = defaultXMLHandler.getFirstElement("config", "log");
        sLogType = xmlLog.getAttribute("type");
        if (sLogType.equals("dated")) {
            setLogType(TYPE_DATED);
        }

        //language
        XMLElement xmlLanguage = defaultXMLHandler.getFirstElement("config", "language");
        Vector<XMLElement> vLanguage = defaultXMLHandler.getElements(xmlLanguage, "languageitem", false);
        for (int i = 0; i < vLanguage.size(); i++) {
            sKey = vLanguage.elementAt(i).getAttribute("key");
            //System.out.println(sKey);
            Vector<XMLElement> vTranslation = defaultXMLHandler.getElements(vLanguage.elementAt(i), "translation", false);
            for (int j = 0; j < vTranslation.size(); j++) {
                sLang = vTranslation.elementAt(j).getAttribute("lang");
                //System.out.println(sLang);
                sItem = vTranslation.elementAt(j).getText();
                //System.out.println(sItem);
                setLanguageString(sLang, sKey, sItem);
            }
        }
        setLanguage("hu");

        //menu
        hmMenus = new HashMap<String, JMenuItem>();
        XMLElement xmlMenu = defaultXMLHandler.getFirstElement("config", "menu"); //the whole menu structure
        Vector<XMLElement> vMenu = defaultXMLHandler.getElements(xmlMenu, "menuitem", false); //the horizontal menu items on the menu bar
        for (int i = 0; i < vMenu.size(); i++) {
            commonMenu = this.addMenu(vMenu.elementAt(i).getAttribute("name"), vMenu.elementAt(i).getAttribute("action"));
            if (vMenu.elementAt(i).getAttribute("type").equals("window")) {
                setWindowMenu(commonMenu, actionListener);
            }
            if (vMenu.elementAt(i).getAttribute("enabled").equals("false")) {
                commonMenu.setEnabled(false);
            }
            if (vMenu.elementAt(i).getAttribute("visible").equals("false")) {
                commonMenu.setVisible(false);
            }
            hmMenus.put(vMenu.elementAt(i).getAttribute("name"), commonMenu);
            createMenus(vMenu.elementAt(i), commonMenu, actionListener);
            if (vMenu.elementAt(i).getAttribute("type").equals("window")) {
                addMenuSeparator(commonMenu); //MaG 2016.10.13.
            }
        }
        iWindowMenuItemCount = 0; // windowMenu.getItemCount(); //MAG 2014.07.22. setWindowMenu(...) t�l korán hívja meg
        if (this.sDateTimeVersion.equals(DEVELOPER_VERSION)) {
            String sXMLDeveloper = "<?xml version='1.0' encoding='ISO-8859-2'?>";
            sXMLDeveloper += "<!-- Comment -->";
            sXMLDeveloper += "<app name='swingapp' major='0' minor='0' revision='0' width='800' height='600'>";
            sXMLDeveloper += "    <language>";
            sXMLDeveloper += "        <languageitem key='mnuDeveloper'>";
            sXMLDeveloper += "            <translation lang='hu'>_Fejlesztő</translation>";
            sXMLDeveloper += "            <translation lang='en'>_Developer</translation>";
            sXMLDeveloper += "            <translation lang='de'>_Entwickler</translation>";
            sXMLDeveloper += "        </languageitem>";
            sXMLDeveloper += "        <languageitem key='mnuDeveloperScreenshot'>";
            sXMLDeveloper += "            <translation lang='hu'>Képernyőmentés</translation>";
            sXMLDeveloper += "            <translation lang='en'>Screenshot</translation>";
            sXMLDeveloper += "            <translation lang='de'>Bildschirmfoto</translation>";
            sXMLDeveloper += "        </languageitem>";
            sXMLDeveloper += "        <languageitem key='mnuDeveloperMockup'>";
            sXMLDeveloper += "            <translation lang='hu'>Mockup</translation>";
            sXMLDeveloper += "            <translation lang='en'>Mockup</translation>";
            sXMLDeveloper += "            <translation lang='de'>Mockup</translation>";
            sXMLDeveloper += "        </languageitem>";
            sXMLDeveloper += "    </language>";
            sXMLDeveloper += "</app>";
            DefaultXMLHandler xmlHandler = new DefaultXMLHandler(appInterface);
//            this.addLanguageXML(sXMLDeveloper, "ISO-8859-2", xmlHandler);
            this.addLanguageXML(sXMLDeveloper, "UTF-8", xmlHandler);
            commonMenu = this.addMenu("mnuDeveloper", "mnuDeveloper");
            hmMenus.put("mnuDeveloper", commonMenu);
            CommonMenuItem commonMenuItem = this.addMenuItem("mnuDeveloperScreenshot", commonMenu, this, "mnuDeveloperScreenshot");
            hmMenus.put("mnuDeveloperScreenshot", commonMenuItem);
            commonMenuItem = this.addMenuItem("mnuDeveloperMockup", commonMenu, this, "mnuDeveloperMockup");
            hmMenus.put("mnuDeveloperMockup", commonMenuItem);
        }

        //toolbar
        XMLElement xmlToolbar = defaultXMLHandler.getFirstElement("config", "toolbar");
        Vector<XMLElement> vToolbar = defaultXMLHandler.getElements(xmlToolbar, "toolbaritem", false);
        if (vToolbar.size() > 0) {
            addToolbar();
            for (int i = 0; i < vToolbar.size(); i++) {
                if (vToolbar.elementAt(i).getAttribute("type").equals("separator")) {
                    addToolbarSeparator();
                } else {
                    addToolbarButton(vToolbar.elementAt(i).getAttribute("image"), vToolbar.elementAt(i).getAttribute("name"), actionListener, vToolbar.elementAt(i).getAttribute("action"));
                }
            }
        }

        //statusbar
        XMLElement xmlStatusbar = defaultXMLHandler.getFirstElement("config", "statusbar");
        Vector<XMLElement> vStatusbar = defaultXMLHandler.getElements(xmlStatusbar, "statusbaritem", false);
        if (vStatusbar.size() > 0) {
            addStatusbar();
            for (int i = 0; i < vStatusbar.size(); i++) {
                if (vStatusbar.elementAt(i).getAttribute("type").equals("separator")) {
                    addStatusbarSeparator();
                }
                if (vStatusbar.elementAt(i).getAttribute("type").equals("ip")) {
                    addStatusbarIP(0.0);
                }
                if (vStatusbar.elementAt(i).getAttribute("type").equals("empty")) {
                    addStatusbarEmpty(1.0);
                }
                if (vStatusbar.elementAt(i).getAttribute("type").equals("progress")) {
                    addStatusbarProgress(0.0);
                }
                if (vStatusbar.elementAt(i).getAttribute("type").equals("memory")) {
                    addStatusbarMemory(0.0);
                }
                if (vStatusbar.elementAt(i).getAttribute("type").equals("clock")) {
                    addStatusbarClock(0.0);
                }
                if (vStatusbar.elementAt(i).getAttribute("type").equals("window")) {
                    addStatusbarWindow(0.0);
                }
            }
            if (xmlStatusbar.getAttribute("log").equals("yes")) {
                addStatusbarLog();
            }
        }

        //connections
        XMLElement xmlConnections = defaultXMLHandler.getFirstElement("config", "connections");
        Vector<XMLElement> vConnections = null;
        if (xmlConnections != null) {
            //vConnections = (xmlConnections == null ? null : defaultXMLHandler.getElements(xmlConnections, "connection", false));
            vConnections = defaultXMLHandler.getElements(xmlConnections, "connection", false);
            for (int i = 0; i < vConnections.size(); i++) {
                this.createConnection(vConnections.elementAt(i).getAttribute("name"), vConnections.elementAt(i).getAttribute("driver"), vConnections.elementAt(i).getAttribute("url"), vConnections.elementAt(i).getAttribute("userid"), vConnections.elementAt(i).getAttribute("password"), vConnections.elementAt(i).getAttribute("type"));
                this.setDatabaseInfo(vConnections.elementAt(i).getAttribute("name"), initDatabaseInfo(vConnections.elementAt(i)));
            }
        }
    }

    @Override
    public void addLanguageXML(String sXMLConfig, String sEncoding) {
        this.addLanguageXML(sXMLConfig, sEncoding, null);
    }

    public void addLanguageXML(String sXMLConfig, String sEncoding, DefaultXMLHandler xmlHnd) {
        String sLang = "";
        String sKey = "";
        String sItem = "";
        DefaultXMLHandler xmlHandler = xmlHnd;

        if (xmlHandler == null) {
            xmlHandler = defaultXMLHandler;
        }
        if (xmlHandler == null) {
            return;
        }

        String sLanguage = getLanguage();
        xmlHandler.readXMLString("config", sXMLConfig, sEncoding);
        //language
        XMLElement xmlLanguage = xmlHandler.getFirstElement("config", "language");
        Vector<XMLElement> vLanguage = xmlHandler.getElements(xmlLanguage, "languageitem", false);
        for (int i = 0; i < vLanguage.size(); i++) {
            sKey = vLanguage.elementAt(i).getAttribute("key");
            //System.out.println(sKey);
            Vector<XMLElement> vTranslation = xmlHandler.getElements(vLanguage.elementAt(i), "translation", false);
            for (int j = 0; j < vTranslation.size(); j++) {
                sLang = vTranslation.elementAt(j).getAttribute("lang");
                //System.out.println(sLang);
                sItem = vTranslation.elementAt(j).getText();
                //System.out.println(sItem);
                setLanguageString(sLang, sKey, sItem);
            }
        }
        setLanguage(sLanguage);
    }

    private DatabaseInfo initDatabaseInfo(XMLElement xmlConnection) {
        DatabaseInfo di = null;
        TableInfo ti = null;
        FieldInfo fi = null;
        XMLElement xmlDi = defaultXMLHandler.getFirstElement(xmlConnection, "databaseinfo");
        if (xmlDi != null) {
            di = new DatabaseInfo(xmlDi.getAttribute("name"), xmlDi.getAttribute("displayname"));
            //System.out.println(xmlDi.getAttribute("name") + " - " + xmlDi.getAttribute("displayname"));
            Vector<XMLElement> vxmlTi = defaultXMLHandler.getElements(xmlDi, "tableinfo", false);
            for (int i = 0; i < vxmlTi.size(); i++) {
                ti = new TableInfo(vxmlTi.elementAt(i).getAttribute("name"), vxmlTi.elementAt(i).getAttribute("tablename"), vxmlTi.elementAt(i).getAttribute("displayname"), vxmlTi.elementAt(i).getAttribute("orderby"), vxmlTi.elementAt(i).getAttribute("extended"));
                //System.out.println(vxmlTi.elementAt(i).getAttribute("name") + " - " + vxmlTi.elementAt(i).getAttribute("displayname"));
                Vector<XMLElement> vxmlFi = defaultXMLHandler.getElements(vxmlTi.elementAt(i), "fieldinfo", false);
                for (int j = 0; j < vxmlFi.size(); j++) {
                    fi = new FieldInfo(vxmlFi.elementAt(j).getAttribute("name"), vxmlFi.elementAt(j).getAttribute("displayname"));
                    if (vxmlFi.elementAt(j).getAttribute("uppercase").equalsIgnoreCase("true")) {
                        fi.setUpperCase(true);
                    }
                    if (vxmlFi.elementAt(j).getAttribute("text").equalsIgnoreCase("true")) {
                        fi.setText(true);
                    }
                    if (!vxmlFi.elementAt(j).getAttribute("filter").equalsIgnoreCase("")) {
                        fi.setFilter(vxmlFi.elementAt(j).getAttribute("filter"));
                    }
                    if (!vxmlFi.elementAt(j).getAttribute("lookup").equalsIgnoreCase("")) {
                        fi.setLookup(vxmlFi.elementAt(j).getAttribute("lookup"));
                    }
                    if (!vxmlFi.elementAt(j).getAttribute("lookupsql").equalsIgnoreCase("")) {
                        fi.setLookupSQL(vxmlFi.elementAt(j).getAttribute("lookupsql"));
                    }
                    if (!vxmlFi.elementAt(j).getAttribute("lookupsqlconnection").equalsIgnoreCase("")) {
                        fi.setLookupSQLConnection(vxmlFi.elementAt(j).getAttribute("lookupsqlconnection"));
                    }
                    if (!vxmlFi.elementAt(j).getAttribute("defaultvalue").equalsIgnoreCase("")) {
                        fi.setDefaultValue(vxmlFi.elementAt(j).getAttribute("defaultvalue"));
                    }
                    if (!vxmlFi.elementAt(j).getAttribute("defaultvaluesql").equalsIgnoreCase("")) {
                        fi.setDefaultValueSQL(vxmlFi.elementAt(j).getAttribute("defaultvaluesql"));
                    }
                    if (vxmlFi.elementAt(j).getAttribute("readonly").equalsIgnoreCase("true")) {
                        fi.setReadOnly(true);
                    }
                    if (vxmlFi.elementAt(j).getAttribute("mandatory").equalsIgnoreCase("true")) {
                        fi.setMandatory(true);
                    }
                    if (!vxmlFi.elementAt(j).getAttribute("allowedcharacters").equalsIgnoreCase("")) {
                        fi.setAllowedCharacters(vxmlFi.elementAt(j).getAttribute("allowedcharacters"));
                    }
                    if (!vxmlFi.elementAt(j).getAttribute("tooltiptext").equalsIgnoreCase("")) {
                        fi.setToolTipText(vxmlFi.elementAt(j).getAttribute("tooltiptext"));
                    }
                    if (vxmlFi.elementAt(j).getAttribute("modifier").equalsIgnoreCase("true")) {
                        fi.setModifier(true);
                    }
                    if (vxmlFi.elementAt(j).getAttribute("modificationtime").equalsIgnoreCase("true")) {
                        fi.setModificationTime(true);
                    }
                    if (vxmlFi.elementAt(j).getAttribute("creationtime").equalsIgnoreCase("true")) {
                        fi.setCreationTime(true);
                    }
                    if (!vxmlFi.elementAt(j).getAttribute("controlsql").equalsIgnoreCase("")) {
                        fi.setControlSQL(vxmlFi.elementAt(j).getAttribute("controlsql"));
                    }
                    if (vxmlFi.elementAt(j).getAttribute("virtual").equalsIgnoreCase("true")) {
                        fi.setVirtual(true);
                        fi.setReadOnly(true);
                        fi.setMandatory(false);
                    }
                    if (!vxmlFi.elementAt(j).getAttribute("virtualvalue").equalsIgnoreCase("")) {
                        fi.setVirtualValue(vxmlFi.elementAt(j).getAttribute("virtualvalue"));
                        fi.setDefaultValue(vxmlFi.elementAt(j).getAttribute("virtualvalue")); //MaG 2018.10.15.
                    }
                    if (!vxmlFi.elementAt(j).getAttribute("virtualvaluesql").equalsIgnoreCase("")) {
                        fi.setVirtualValueSQL(vxmlFi.elementAt(j).getAttribute("virtualvaluesql"));
                    }
                    if (!vxmlFi.elementAt(j).getAttribute("minwidth").equalsIgnoreCase("")) {
                        fi.setMinWidth(StringUtils.intValue(vxmlFi.elementAt(j).getAttribute("minwidth")));
                    }
                    if (!vxmlFi.elementAt(j).getAttribute("maxwidth").equalsIgnoreCase("")) {
                        fi.setMaxWidth(StringUtils.intValue(vxmlFi.elementAt(j).getAttribute("maxwidth")));
                    }
                    if (!vxmlFi.elementAt(j).getAttribute("spectype").equalsIgnoreCase("")) {
                        fi.setSpecType(vxmlFi.elementAt(j).getAttribute("spectype"));
                    }
                    if (vxmlFi.elementAt(j).getAttribute("hidden").equalsIgnoreCase("true")) {
                        fi.setHidden(true);
                    }
                    if (!vxmlFi.elementAt(j).getAttribute("recordinfo").equalsIgnoreCase("")) {
                        fi.setRecordInfo(vxmlFi.elementAt(j).getAttribute("recordinfo"));
                    }
                    if (vxmlFi.elementAt(j).getAttribute("colorfield").equalsIgnoreCase("true")) {
                        fi.setColorField(true);
                    }
                    if (vxmlFi.elementAt(j).getAttribute("validfrom").equalsIgnoreCase("true")) {
                        fi.setValidFromField(true);
                    }
                    if (vxmlFi.elementAt(j).getAttribute("validto").equalsIgnoreCase("true")) {
                        fi.setValidToField(true);
                    }
                    //System.out.println(vxmlFi.elementAt(j).getAttribute("name") + " - " + vxmlFi.elementAt(j).getAttribute("displayname"));
                    ti.addFieldInfo(fi);
                }
                di.addTableInfo(ti);
            }
        }
        return (di);
    }

    public void setVersionFromFile(Class c) {//Main.class
        String sVersion = "";
        Properties propVersion = new Properties();
        java.io.InputStream is = c.getResourceAsStream("version.ver"); //Main.class
        if (is != null) {
            try {
                propVersion.load(is);
                is.close();
                sVersion = propVersion.getProperty("version", "");
            } catch (IOException ioe) {
            }
        } else {
            sVersion = DEVELOPER_VERSION;
        }
        this.setDateTimeVersion(sVersion);
        this.setTitleWithVersion();
    }

    private String getTitleBasedOnLanguage() {
        String sTitle = "";
        if (appParameters.getTitleLanguageStringKey().equals("")) {
            sTitle = appParameters.getAppName();
        } else {
            sTitle = languageHandlerInterface.getLanguageString(appParameters.getTitleLanguageStringKey());
        }
        String sVersionString = getVersionString(appParameters.getMajor(), appParameters.getMinor(), appParameters.getRevision(), sDateTimeVersion);
        return (sTitle + (sVersionString.equals("") ? "" : " - " + sVersionString));
    }

    private void setTitleBasedOnLanguage() {
        setTitle(getTitleBasedOnLanguage());
    }

    //--- TOOLBAR
    public void addToolbar() {
        if (this.jToolBar == null) {
            this.jToolBar = new JToolBar();
            this.jToolBar.setFloatable(false);
            this.jToolBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, this.jToolBar.getBackground().darker())); //2011.05.13.
            //this.jToolBar.setMargin(new Insets(10, 10, 10, 10)); nem hatásos
            getContentPane().add(this.jToolBar, java.awt.BorderLayout.NORTH);
        }
    }

    public ToolbarButton addToolbarButton(ImageIcon imageIcon, String sToolTipText, ActionListener actionListener, String sActionCommand) {
        ToolbarButton toolbarButton;
        this.addToolbar();
        jToolBar.add(toolbarButton = new ToolbarButton(imageIcon, sToolTipText, actionListener, sActionCommand));
        return (toolbarButton);
    }

    public ToolbarButton addToolbarButton(String sImageName, String sToolTipText, ActionListener actionListener, String sActionCommand) {
        return (addToolbarButton(new ImageIcon(getClass().getResource(sImageName)), sToolTipText, actionListener, sActionCommand));
    }

    public void addToolbarSeparator() {
        if (this.jToolBar != null) {
            jToolBar.addSeparator();
        }
    }

    //--- Control Bar
    public void addControlBar() {
        if (this.cpControlBar == null) {
            this.cpControlBar = new CommonPanel();
            this.cpControlBar.setInsets(0, 0, 0, 0);
            this.cpControlBar.setBorder(new EmptyBorder(1, 1, 1, 1));
            this.cpControlBar.setPreferredSize(new java.awt.Dimension(150, 150));
            getContentPane().add(this.cpControlBar, java.awt.BorderLayout.EAST);
        }
    }

    //--- INFOBAR
    public void addInfobar() {
        if (this.infoBar == null) {
            this.infoBar = new CommonPanel();
            //this.infoBar.setInsets(1, 1, 1, 1);
            this.infoBar.setInsets(0, 0, 0, 0);
            //this.infoBar.setBorder(new EtchedBorder(EtchedBorder.RAISED));
            this.infoBar.setBorder(new EmptyBorder(1, 1, 1, 1));
            //this.infoBar.setBorder(new EmptyBorder(0, 0, 0, 0));
            this.infoBar.setPreferredSize(new java.awt.Dimension(21, 21));
            //this.infoBar.setLayout(new java.awt.BorderLayout());
            getContentPane().add(this.infoBar, java.awt.BorderLayout.NORTH);
        }
        if (jLabelInfo == null) {
            jLabelInfo = new JLabel();
            jLabelInfo.setFont(new Font("Arial", Font.PLAIN, 12));
            //jLabelInfo.setBorder(BorderFactory.createRaisedBevelBorder());
            //jLabelInfo.setBorder(new EtchedBorder(EtchedBorder.RAISED));
            jLabelInfo.setBorder(new EmptyBorder(0, 7, 0, 7));
            this.infoBar.addToCurrentRow(jLabelInfo, 1, 1, 0.0, 1.0, GridBagConstraints.BOTH, GridBagConstraints.CENTER);
        }
    }

    public void setInfo(String sInfo) {
        if (jLabelInfo != null) {
            jLabelInfo.setText(sInfo);
        }
    }

    public void setInfoAlignment(int iAlignment) {
        if (jLabelInfo != null) {
            jLabelInfo.setHorizontalAlignment(iAlignment);
        }
    }

    //--- STATUSBAR
    public void addStatusbar() {
        if (this.statusBar == null) {
            this.statusBar = new CommonPanel();
            //this.statusBar.setInsets(1, 1, 1, 1);
            this.statusBar.setInsets(0, 0, 0, 0);
            //this.statusBar.setBorder(new EtchedBorder(EtchedBorder.RAISED));
            this.statusBar.setBorder(new EmptyBorder(1, 1, 1, 1));
            //this.statusBar.setBorder(new EmptyBorder(0, 0, 0, 0));
            this.statusBar.setPreferredSize(new java.awt.Dimension(21, 21));
            //this.statusBar.setLayout(new java.awt.BorderLayout());
            getContentPane().add(this.statusBar, java.awt.BorderLayout.SOUTH);
        }
    }

    public void addStatusbarBase() {
        addStatusbarIP(0);
        addStatusbarSeparator();
        //addStatusbarEmpty(1);
        addStatusbarStatus(1);
        addStatusbarSeparator();
        addStatusbarMemory(0);
        addStatusbarSeparator();
        addStatusbarWindow(0);
        addStatusbarSeparator();
        addStatusbarProgress(0);
        addStatusbarSeparator();
        addStatusbarClock(0);
    }

    private void addTimerClock() {
        if (timerClock == null) {
            timerClock = new javax.swing.Timer(1000, this);
            timerClock.setRepeats(true);
            timerClock.start();
        }
    }

    public void addStatusbarClock(double weightcolumn) {
        addStatusbar();
        if (jLabelClock == null) {
            jLabelClock = new JLabel();
            jLabelClock.setFont(new Font("Arial", Font.PLAIN, 12));
            //jLabelClock.setBorder(BorderFactory.createRaisedBevelBorder());
            //jLabelClock.setBorder(new EtchedBorder(EtchedBorder.RAISED));
            jLabelClock.setBorder(new EmptyBorder(0, 7, 0, 7));
            this.statusBar.addToCurrentRow(jLabelClock, 1, 1, 0.0, weightcolumn);
            addTimerClock();
            displayClock();
        }
    }

    private void displayClock() {
        jLabelClock.setText(formatInterface.getClockDateTimeFormat().format(new java.util.Date()));
        if (jLabelClock.getText().endsWith("0")) {
            runtime.gc();
        }
    }

    public void addStatusbarSeparator() {
        addStatusbar();
        JLabel jLabel = new JLabel();
        //jLabel.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        //jLabel.setBorder(BorderFactory.createRaisedBevelBorder());

        //jLabel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.BLACK));
        jLabel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, this.statusBar.getBackground().darker()));

        //jLabel.setBorder(new LineBorder(Color.GRAY, 1));
        //jLabel.setBorder(new LineBorder(this.getContentPane().getBackground(), 1));
        this.statusBar.addToCurrentRow(jLabel, 1, 1, 0.0, 0.0);
    }

    public void displayStatus(String sStatus) {
        if (jLabelStatus != null) {
            jLabelStatus.setText(sStatus);
        }
    }

    public void addStatusbarStatus(double weightcolumn) {
        addStatusbar();
        if (jLabelStatus == null) {
            jLabelStatus = new JLabel();
            jLabelStatus.setFont(new Font("Arial", Font.PLAIN, 12));
            jLabelStatus.setBorder(new EmptyBorder(0, 7, 0, 7));
            this.statusBar.addToCurrentRow(jLabelStatus, 1, 1, 0.0, weightcolumn);
        }
    }

    public void addStatusbarMemory(double weightcolumn) {
        addStatusbar();
        if (jLabelMemory == null) {
            jLabelMemory = new JLabel();
            jLabelMemory.setFont(new Font("Arial", Font.PLAIN, 12));
            //jLabelMemory.setBorder(new EtchedBorder(EtchedBorder.RAISED));
            jLabelMemory.setBorder(new EmptyBorder(0, 7, 0, 7));
            jLabelMemory.addMouseListener(this);
            this.statusBar.addToCurrentRow(jLabelMemory, 1, 1, 0.0, weightcolumn);
            sLabelMemoryToolTipText = "<html><table border=0><tr><th colspan=2>Java</th></tr>";
            sLabelMemoryToolTipText += "<tr><td>Vendor:</td><td>" + System.getProperty("java.vendor") + "</td></tr>";
            sLabelMemoryToolTipText += "<tr><td>Version:</td><td>" + System.getProperty("java.version") + "</td></tr>";
            sLabelMemoryToolTipText += "<tr><th colspan=2>Operating system</th></tr>";
            sLabelMemoryToolTipText += "<tr><td>Architecture:</td><td>" + System.getProperty("os.arch") + "</td></tr>";
            sLabelMemoryToolTipText += "<tr><td>Name:</td><td>" + System.getProperty("os.name") + "</td></tr>";
            sLabelMemoryToolTipText += "<tr><td>Version:</td><td>" + System.getProperty("os.version") + "</td></tr>";
            sLabelMemoryToolTipText += "<tr><th colspan=2>Memory</th></tr>";
            sLabelMemoryToolTipText += "<tr><td>Free:</td><td align=right>[freememory] Byte</td></tr>";
            sLabelMemoryToolTipText += "<tr><td>Total:</td><td align=right>[totalmemory] Byte</td></tr>";
            sLabelMemoryToolTipText += "<tr><td>Maximum:</td><td align=right>[maxmemory] Byte</td></tr>";
            sLabelMemoryToolTipText += "[connections]";
            sLabelMemoryToolTipText += "</table></html>";
            sLabelMemoryToolTipTextConnectionInfo = ""; //getConnectionInfoForDisplayMemory(); //MaG 2018.02.08.
            addTimerClock();
            bMemoryDisplayMode = (this.sDateTimeVersion.equals(DEVELOPER_VERSION) ? true : false);
            displayMemory(true);
        }
    }

//    private void displayMemoryInThread() {
//        Thread t = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                displayMemory();
//            }
//        });
//        t.setPriority(Thread.NORM_PRIORITY);
//        t.start();
//    }
    private String getConnectionInfoForDisplayMemory() {
        String sRetVal = "";
        Vector<String> vConnectionNames = this.getConnectionNames();
        for (int i = 0; i < vConnectionNames.size(); i++) {
            if (i == 0) {
                sRetVal += "<tr><th colspan=2>Connections</th></tr>";
            }
            String sConnectionName = vConnectionNames.elementAt(i);
            Connection conn = this.getTemporaryConnection(sConnectionName);
            String sInfo = "";
            try {
                if (conn != null) {
                    sInfo = conn.getMetaData().getDriverName() + " " + conn.getMetaData().getDriverVersion();
                    conn.close();
                    conn = null;
                }
            } catch (SQLException sqle) {
            }
            sRetVal += "<tr><td>" + sConnectionName + "</td><td align=right>" + sInfo + "</td></tr>";
        }
        return (sRetVal);
    }

    private void displayMemory(boolean bStart) {
        long lDivider = 1;
        String sUnit = "Byte";
        long lFreeMemory = runtime.freeMemory();
        long lTotalMemory = runtime.totalMemory();
        long lMaxMemory = runtime.maxMemory();
        if (lMaxMemory > 1023) {
            lDivider = 1024;
            sUnit = "KByte";
        }
        if (lMaxMemory > 1048575) {
            lDivider = 1048576;
            sUnit = "MByte";
        }
        if (bMemoryDisplayMode) {
            int iRed = 255;
            int iGreen = 255;
            double d = 1.0 * lFreeMemory / lMaxMemory;
            if (d > 0.5) {
                iRed = Math.min(Math.max(new Double(512.0 - d * 512.0).intValue(), 0), 255);
            }
            if (d < 0.5) {
                iGreen = Math.min(Math.max(new Double(d * 512.0).intValue(), 0), 255);
            }
            Color colorMemory = new Color(iRed, iGreen, 0);
            //System.out.println(Double.toString(d) + " " + Integer.toString(iRed) + " " + Integer.toString(iGreen));
            jLabelMemory.setText(memoryFormat.format(lFreeMemory) + " Byte " + Integer.toString(new Double(d * 100).intValue()) + "%");
            jLabelMemory.setOpaque(true);
            jLabelMemory.setBackground(colorMemory);
        } else {
            jLabelMemory.setText(Long.toString(lFreeMemory / lDivider) + "/" + Long.toString(lTotalMemory / lDivider) + "/" + Long.toString(lMaxMemory / lDivider) + " " + sUnit);
            jLabelMemory.setOpaque(false);
        }
        StringBuffer sbToolTipText = new StringBuffer(sLabelMemoryToolTipText);
        int iPos = sbToolTipText.indexOf("[freememory]");
        sbToolTipText.replace(iPos, iPos + 12, memoryFormat.format(lFreeMemory));
        iPos = sbToolTipText.indexOf("[totalmemory]");
        sbToolTipText.replace(iPos, iPos + 13, memoryFormat.format(lTotalMemory));
        iPos = sbToolTipText.indexOf("[maxmemory]");
        sbToolTipText.replace(iPos, iPos + 11, memoryFormat.format(lMaxMemory));
        iPos = sbToolTipText.indexOf("[connections]");
        if (!bStart && sLabelMemoryToolTipTextConnectionInfo.length() == 0) {
            sLabelMemoryToolTipTextConnectionInfo = ""; //getConnectionInfoForDisplayMemory(); //MaG 2018.02.08.
        }
        sbToolTipText.replace(iPos, iPos + 13, sLabelMemoryToolTipTextConnectionInfo);
//        sbToolTipText = new StringBuffer();
//        sbToolTipText.append("<html>");
//        sbToolTipText.append("<table border=1>");
//        sbToolTipText.append("<tr><th colspan=2>Java</th></tr>");
//        sbToolTipText.append("<tr><td>Vendor:</td><td>").append(System.getProperty("java.vendor")).append("</td></tr>");
//        sbToolTipText.append("<tr><td>Version:</td><td>").append(System.getProperty("java.version")).append("</td></tr>");
//        sbToolTipText.append("<tr><th colspan=2>Operating system</th></tr>");
//        sbToolTipText.append("<tr><td>Architecture:</td><td>").append(System.getProperty("os.arch")).append("</td></tr>");
//        sbToolTipText.append("<tr><td>Name:</td><td>").append(System.getProperty("os.name")).append("</td></tr>");
//        sbToolTipText.append("<tr><td>Version:</td><td>").append(System.getProperty("os.version")).append("</td></tr>");
//        sbToolTipText.append("<tr><th colspan=2>Memory</th></tr>"); //Memória
//        sbToolTipText.append("<tr><td>Free:</td><td align=right>").append(memoryFormat.format(lFreeMemory)).append(" Byte</td></tr>"); //Szabad
//        sbToolTipText.append("<tr><td>Total:</td><td align=right>").append(memoryFormat.format(lTotalMemory)).append(" Byte</td></tr>"); //Összes
//        sbToolTipText.append("<tr><td>Maximum:</td><td align=right>").append(memoryFormat.format(lMaxMemory)).append(" Byte</td></tr>"); //Maximális
//        sbToolTipText.append("</table>");
//        sbToolTipText.append("</html>");
        jLabelMemory.setToolTipText(sbToolTipText.toString());
        //jLabelMemory.setToolTipText(sLabelMemoryToolTipText.replace("[freememory]", memoryFormat.format(lFreeMemory)).replace("[totalmemory]", memoryFormat.format(lTotalMemory)).replace("[maxmemory]", memoryFormat.format(lMaxMemory)));
        //jLabelMemory.setToolTipText(sLabelMemoryToolTipText);
        //System.out.println(getDateTimeFormat().format(new java.util.Date()) + " " + memoryFormat.format(lFreeMemory));
        //System.out.println(timerClock.getActionListeners().length);
        //System.out.println(memoryFormat.format(lFreeMemory));
    }

    private void displayMemory_origin() {
        long lDivider = 1;
        String sUnit = "Byte";
        long lFreeMemory = runtime.freeMemory();
        long lTotalMemory = runtime.totalMemory();
        long lMaxMemory = runtime.maxMemory();
        if (lMaxMemory > 1023) {
            lDivider = 1024;
            sUnit = "KByte";
        }
        if (lMaxMemory > 1048575) {
            lDivider = 1048576;
            sUnit = "MByte";
        }
        jLabelMemory.setText(Long.toString(lFreeMemory / lDivider) + "/" + Long.toString(lTotalMemory / lDivider) + "/" + Long.toString(lMaxMemory / lDivider) + " " + sUnit);
        StringBuffer sbToolTipText = new StringBuffer();
        sbToolTipText = new StringBuffer();
        sbToolTipText.append("<html>");
        sbToolTipText.append("<table border=0>");
        sbToolTipText.append("<tr><th colspan=2>Java</th></tr>");
        sbToolTipText.append("<tr><td>Vendor:</td><td>");
        sbToolTipText.append(System.getProperty("java.vendor"));
        sbToolTipText.append("</td></tr>");
        sbToolTipText.append("<tr><td>Version:</td><td>");
        sbToolTipText.append(System.getProperty("java.version"));
        sbToolTipText.append("</td></tr>");
        sbToolTipText.append("<tr><th colspan=2>Operating system</th></tr>");
        sbToolTipText.append("<tr><td>Architecture:</td><td>");
        sbToolTipText.append(System.getProperty("os.arch"));
        sbToolTipText.append("</td></tr>");
        sbToolTipText.append("<tr><td>Name:</td><td>");
        sbToolTipText.append(System.getProperty("os.name"));
        sbToolTipText.append("</td></tr>");
        sbToolTipText.append("<tr><td>Version:</td><td>");
        sbToolTipText.append(System.getProperty("os.version"));
        sbToolTipText.append("</td></tr>");
        sbToolTipText.append("<tr><th colspan=2>Memory</th></tr>"); //Memória
        sbToolTipText.append("<tr><td>Free:</td><td align=right>");
        sbToolTipText.append(memoryFormat.format(lFreeMemory));
        sbToolTipText.append(" Byte</td></tr>"); //Szabad
        sbToolTipText.append("<tr><td>Total:</td><td align=right>");
        sbToolTipText.append(memoryFormat.format(lTotalMemory));
        sbToolTipText.append(" Byte</td></tr>"); //Összes
        sbToolTipText.append("<tr><td>Maximum:</td><td align=right>");
        sbToolTipText.append(memoryFormat.format(lMaxMemory));
        sbToolTipText.append(" Byte</td></tr>"); //Maximális
        sbToolTipText.append("</table>");
        sbToolTipText.append("</html>");
        jLabelMemory.setToolTipText(sbToolTipText.toString());
    }
//    private void displayMemory_test() {
//        long lDivider = 1;
//        String sUnit = "Byte";
//        long lFreeMemory = runtime.freeMemory();
//        long lTotalMemory = runtime.totalMemory();
//        long lMaxMemory = runtime.maxMemory();
//        if (lMaxMemory > 1023) {
//            lDivider = 1024;
//            sUnit = "KByte";
//        }
//        if (lMaxMemory > 1048575) {
//            lDivider = 1048576;
//            sUnit = "MByte";
//        }
//        jLabelMemory.setText(Long.toString(lFreeMemory / lDivider) + "/" + Long.toString(lTotalMemory / lDivider) + "/" + Long.toString(lMaxMemory / lDivider) + " " + sUnit);
//        String sToolTip = "<html><table border=0><tr><th colspan=2>Java</th></tr>";
//        sToolTip += "<tr><td>Vendor:</td><td>" + System.getProperty("java.vendor") + "</td></tr>";
//        sToolTip += "<tr><td>Version:</td><td>" + System.getProperty("java.version") + "</td></tr>";
//        sToolTip += "<tr><th colspan=2>Operating system</th></tr>";
//        sToolTip += "<tr><td>Architecture:</td><td>" + System.getProperty("os.arch") + "</td></tr>";
//        sToolTip += "<tr><td>Name:</td><td>" + System.getProperty("os.name") + "</td></tr>";
//        sToolTip += "<tr><td>Version:</td><td>" + System.getProperty("os.version") + "</td></tr>";
//        sToolTip += "<tr><th colspan=2>Memory</th></tr>";
//        sToolTip += "<tr><td>Free:</td><td align=right>" + memoryFormat.format(lFreeMemory) + " Byte</td></tr>";
//        sToolTip += "<tr><td>Total:</td><td align=right>" + memoryFormat.format(lTotalMemory) + " Byte</td></tr>";
//        sToolTip += "<tr><td>Maximum:</td><td align=right>" + memoryFormat.format(lMaxMemory) + " Byte</td></tr>";
//        sToolTip += "</table></html>";
//        jLabelMemory.setToolTipText(sToolTip);
//        sbToolTipText = null;
//    }

    public void addStatusbarWindow(double weightcolumn) {
        addStatusbar();
        if (jLabelWindow == null) {
            jLabelWindow = new JLabel();
            jLabelWindow.setFont(new Font("Arial", Font.PLAIN, 12));
            jLabelWindow.setBorder(new EmptyBorder(0, 7, 0, 7));
            jLabelWindow.addMouseListener(this);
            jLabelWindow.setText("");
            this.statusBar.addToCurrentRow(jLabelWindow, 1, 1, 0.0, weightcolumn);
        }
    }

    private void setStatusBarWindowInfo() {
        if (jLabelWindow == null) {
            return;
        }
        jLabelWindow.setText(jDesktopPane.getAllFrames().length > 1 ? jDesktopPane.getAllFrames().length + " ablak" : "");
        StringBuffer sbToolTipText = new StringBuffer();
        if (jDesktopPane.getAllFrames().length > 0) {
            sbToolTipText.append("<html><table border=0>");
            for (int i = 0; i < jDesktopPane.getAllFrames().length; i++) {
                sbToolTipText.append("<tr><td>").append(jDesktopPane.getAllFrames()[i].getTitle()).append("</td></tr>");
            }
            sbToolTipText.append("</table></html>");
            jLabelWindow.setToolTipText(sbToolTipText.toString());
        }
    }

    private void selectWindow() {
        if (jDesktopPane.getAllFrames().length < 1) {
            return;
        }
        SelectFromMagTableDialog sfmtd = new SelectFromMagTableDialog(this.getAppFrame(), this, true);
        sfmtd.setAlignedToTop(jLabelWindow);
        MemoryTable mt = MemoryTable.createMemoryTable("#", "Ablak választás");
        for (int i = 0; i < jDesktopPane.getAllFrames().length; i++) {
            mt.addRow(i + 1, jDesktopPane.getAllFrames()[i].getTitle());
        }
        int iReturn = sfmtd.showDialog(this.getAppFrame(), "Ablak választás", mt, "", "", StringUtils.createEmptyArray());
        if (iReturn != SelectFromMagTableDialog.OK) {
            return;
        }
        int iSelected = sfmtd.getSelectedRow();
        //System.out.println("ablak " + iReturn);
        if (iSelected < 1 || iSelected >= jDesktopPane.getAllFrames().length) {
            return;
        }
        //System.out.println("xxx");
        try {
            jDesktopPane.getAllFrames()[iSelected].setSelected(true);
        } catch (PropertyVetoException e) {
            handleError(e, 0);
        }
    }

    public void addStatusbarIP(double weightcolumn) {
        addStatusbar();
        if (jLabelIP == null) {
            jLabelIP = new JLabel();
            jLabelIP.setFont(new Font("Arial", Font.PLAIN, 12));
            jLabelIP.setBorder(new EmptyBorder(0, 7, 0, 7));
            StringBuffer sbToolTipText = new StringBuffer();
            if (inetAddress != null) {
                jLabelIP.setText(inetAddress.getHostAddress());
                sbToolTipText.append("<html><table border=0><tr><th colspan=2>Net</th></tr>");
                //@todo task : localisation, translation
                sbToolTipText.append("<tr><td>Host name:</td><td>").append(inetAddress.getHostName()).append("</td></tr>"); //Host név
                sbToolTipText.append("<tr><td>Host address:</td><td>").append(inetAddress.getHostAddress()).append("</td></tr>");//Host ip cím
                sbToolTipText.append("<tr><td>Host full name:</td><td>").append(inetAddress.getCanonicalHostName()).append("</td></tr>");//Host teljes név
                sbToolTipText.append("</table></html>");
                jLabelIP.setToolTipText(sbToolTipText.toString());
            }
            this.statusBar.addToCurrentRow(jLabelIP, 1, 1, 0.0, weightcolumn);
        }
    }

    public void addStatusbarProgress(double weightcolumn) {
        addStatusbar();
        if (jProgressBarProgress == null) {
            jProgressBarProgress = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);
            jProgressBarProgress.setPreferredSize(new Dimension(100, 1));
            jProgressBarProgress.setFont(new Font("Arial", Font.PLAIN, 12));
            jProgressBarProgress.setBorder(new EmptyBorder(1, 2, 1, 2));
            jProgressBarProgress.setBorderPainted(false);
            jProgressBarProgress.setStringPainted(false);

            //            jProgressBarProgress.setValue(50);
            //            jProgressBarProgress.setString("50%");
            //            jProgressBarProgress.setString("Kérem, várjon...");
            //            jProgressBarProgress.setToolTipText("Kérem, várjon...");
            //            jProgressBarProgress.setIndeterminate(true);
            this.statusBar.addToCurrentRow(new JLabel("  "), 1, 1, 0.0, weightcolumn);
            this.statusBar.addToCurrentRow(jProgressBarProgress, 1, 1, 0.0, weightcolumn);
            this.statusBar.addToCurrentRow(new JLabel("  "), 1, 1, 0.0, weightcolumn);
        }
    }

    @Override
    public void displayProgressValue(int iValue) {
        jProgressBarProgress.setIndeterminate(false);
        if ((iValue < jProgressBarProgress.getMinimum()) || (iValue > jProgressBarProgress.getMaximum())) {
            jProgressBarProgress.setValue(jProgressBarProgress.getMinimum());
            jProgressBarProgress.setString("");
            jProgressBarProgress.setStringPainted(false);
        } else {
            jProgressBarProgress.setValue(iValue);
            jProgressBarProgress.setString(Integer.toString(iValue) + "%");
            jProgressBarProgress.setStringPainted(true);
        }
    }

    @Override
    public void displayProgressWait(boolean bDisplay) {
        if (bDisplay) {
            jProgressBarProgress.setString("Kérem, várjon...");

            jProgressBarProgress.setBorder(BorderFactory.createLineBorder(Color.orange, 2));
            jProgressBarProgress.setBorderPainted(true);

            jProgressBarProgress.setStringPainted(true);
            jProgressBarProgress.setIndeterminate(true);
        } else {
            jProgressBarProgress.setString("");

            jProgressBarProgress.setBorder(new EmptyBorder(1, 2, 1, 2));
            jProgressBarProgress.setBorderPainted(false);

            jProgressBarProgress.setStringPainted(false);
            jProgressBarProgress.setIndeterminate(false);
        }
    }

    public void addStatusbarEmpty(double weightcolumn) {
        addStatusbar();
        JLabel jLabel = new JLabel();
        jLabel.setBorder(new EmptyBorder(0, 7, 0, 7));
        this.statusBar.addToCurrentRow(jLabel, 1, 1, 0.0, weightcolumn);
    }

    public void addStatusbarLog() {
        addStatusbar();

        addStatusbarSeparator();
        jLabelLog = new JLabel("Log");
        jLabelLog.setFont(new Font("Arial", Font.PLAIN, 12));
        jLabelLog.setBorder(new EmptyBorder(1, 7, 1, 7));
        //jLabelLog.setToolTipText("Log");
        jLabelLog.addMouseListener(this);
        this.statusBar.addToCurrentRow(jLabelLog, 1, 1, 0.0, 0.0);

        jEditorPaneLog = new JEditorPane();
        jEditorPaneLog.setEditable(false);
        jEditorPaneLog.setContentType("text/plain");
        jEditorPaneLog.setBackground(Color.cyan);
        jEditorPaneLog.setText("");
        //@todo task * : put controls to the panel in order to set the (display and record) log level
        jScrollPaneLog = new JScrollPane(jEditorPaneLog);
        jScrollPaneLog.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jScrollPaneLog.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPaneLog.setVisible(false);
        this.statusBar.nextRow();
        this.statusBar.addToCurrentRow(jScrollPaneLog, GridBagConstraints.REMAINDER, 1, 1.0, 1.0, GridBagConstraints.BOTH);
    }

    //--- SPLASH
    public void addSplash(String sImageName, Class classImage) {
        this.sImageName = sImageName;
        this.classImage = classImage;
    }

    public void displaySplash() {
        java.net.URL imageURL = classImage.getResource(sImageName);
        if (imageURL != null) {
            imageSplash = Toolkit.getDefaultToolkit().getImage(imageURL);
        }

        //imageSplash = Toolkit.getDefaultToolkit().getImage(sImageName);
        //appSplash = new AppSplash(imageSplash, 512, 316, AppUtils.getTitle(appParameters.getAppName(), iMajor, iMinor, iRevision));
        appSplash = new AppSplash(imageSplash, 512, 316, getTitleBasedOnLanguage());

        appSplash.toFront();
        appSplash.setVisible(true);
        timerSplash = new javax.swing.Timer(3000, this);
        timerSplash.setRepeats(false);
        timerSplash.start();
    }

    public void hideSplash() {
        appSplash.setVisible(false);
        appSplash.dispose();
        imageSplash = null; //felszabadítja a helyet???

    }

    //---
    public void setLocale(String sLanguage, String sCountry) {
        if ((!this.sLanguage.equals(sLanguage)) || (!this.sCountry.equals(sCountry))) {
            this.sLanguage = sLanguage;
            this.sCountry = sCountry;
            locale = new Locale(sLanguage, sCountry);
            //2011.05.17. resources = ResourceBundle.getBundle(appParameters.getResourceBundleBaseName(), locale);
            if (languageHandlerInterface != null) {
                languageHandlerInterface.setLanguage(sLanguage);
            }
            setTitleBasedOnLanguage();
        }
    }

    //2008.10.26. create
    //2011.05.17. delete
//    public ResourceBundle getResources()
//    {
//        return (resources);
//    }
    public void loadProperties(Properties properties, String sPropertyFileName) {
        try {
            FileInputStream in = new FileInputStream(sPropertyFileName);
            properties.load(in);
            in.close();
        } catch (IOException e) {
            handleError(e, 9);
        }
    }

    public void storeProperties(Properties properties, String sPropertyFileName) {
        try {
            FileOutputStream out = new FileOutputStream(sPropertyFileName);
            properties.store(out, "Application Property File");
            out.flush();
            out.close();
        } catch (IOException e) {
            handleError(e, 9);
        }
    }

    public String getOldLanguageString(int iKey) {
        return (getOldLanguageString(iKey, "???"));
    }

    public String getOldLanguageString(int iKey, String sDefaultValue) {
        if (languageHandlerInterface != null) {
            return (languageHandlerInterface.getLanguageString(iKey, sDefaultValue));
        }
        return (sDefaultValue);
    }

    public CommonMenu addMenu(int iLanguageStringID, String sActionCommand) {
        String sCaption = getOldLanguageString(iLanguageStringID);
        int iMnemonic = AppUtils.getMnemonic(sCaption);
        CommonMenu commonMenu = new CommonMenu();
        commonMenu.setText(AppUtils.getCaption(sCaption));
        commonMenu.setActionCommand(sActionCommand);
        if (iMnemonic > 0) {
            commonMenu.setMnemonic(iMnemonic);
        }
        commonMenu.setLanguageStringID(iLanguageStringID);
        jMenuBar.add(commonMenu);
        //System.out.println(sCaption + " " + Integer.toString(iMnemonic));
        return (commonMenu);
    }

    public CommonMenu addMenu(String sLanguageStringKey, String sActionCommand) {
        String sCaption = getLanguageString(sLanguageStringKey);
        int iMnemonic = AppUtils.getMnemonic(sCaption);
        CommonMenu commonMenu = new CommonMenu();
        commonMenu.setName(sLanguageStringKey);
        commonMenu.setText(AppUtils.getCaption(sCaption));
        commonMenu.setActionCommand(sActionCommand);
        if (iMnemonic > 0) {
            commonMenu.setMnemonic(iMnemonic);
            //System.out.println(sLanguageStringKey + " "+Integer.toString(iMnemonic));
        }
        commonMenu.setLanguageStringKey(sLanguageStringKey);
        jMenuBar.add(commonMenu);
        //System.out.println(sCaption + " " + Integer.toString(iMnemonic));
        return (commonMenu);
    }

    public CommonMenuItem addMenuItem(int iLanguageStringID, JMenu jMenuParent, ActionListener l, String sActionCommand) {
        String sCaption = getOldLanguageString(iLanguageStringID);
        int iMnemonic = AppUtils.getMnemonic(sCaption);
        KeyStroke keyStroke = AppUtils.getKeyStroke(sCaption);
        CommonMenuItem commonMenuItem = new CommonMenuItem();
        commonMenuItem.setName(Integer.toString(iLanguageStringID));
        commonMenuItem.setText(AppUtils.getCaption(sCaption));
        commonMenuItem.setActionCommand(sActionCommand);
        //System.out.println(sActionCommand);
        jMenuParent.add(commonMenuItem);
        if (iMnemonic > 0) {
            commonMenuItem.setMnemonic(iMnemonic);
        }
        if (keyStroke != null) {
            commonMenuItem.setAccelerator(keyStroke);
        }
        commonMenuItem.addActionListener(l);
        commonMenuItem.setLanguageStringID(iLanguageStringID);
        return (commonMenuItem);
    }

    public CommonMenuItem addMenuItem(String sLanguageStringKey, JMenu jMenuParent, ActionListener l, String sActionCommand) {
//        String sCaption = getOldLanguageString(iLanguageStringID);
        String sCaption = getLanguageString(sLanguageStringKey);
        int iMnemonic = AppUtils.getMnemonic(sCaption);
        KeyStroke keyStroke = AppUtils.getKeyStroke(sCaption);
        CommonMenuItem commonMenuItem = new CommonMenuItem();
        commonMenuItem.setName(sLanguageStringKey);
        commonMenuItem.setText(AppUtils.getCaption(sCaption));
        commonMenuItem.setActionCommand(sActionCommand);
        //System.out.println(sActionCommand);
        jMenuParent.add(commonMenuItem);
        if (iMnemonic > 0) {
            commonMenuItem.setMnemonic(iMnemonic);
        }
        if (keyStroke != null) {
            commonMenuItem.setAccelerator(keyStroke);
        }
        commonMenuItem.addActionListener(l);
        commonMenuItem.setLanguageStringKey(sLanguageStringKey);
        return (commonMenuItem);
    }

    public CommonMenu addSubMenu(String sLanguageStringKey, JMenu jMenuParent, ActionListener l, String sActionCommand) {
//        String sCaption = getOldLanguageString(iLanguageStringID);
        String sCaption = getLanguageString(sLanguageStringKey);
        int iMnemonic = AppUtils.getMnemonic(sCaption);
//        KeyStroke keyStroke = AppUtils.getKeyStroke(sCaption);
        CommonMenu commonMenu = new CommonMenu();
        commonMenu.setName(sLanguageStringKey);
        commonMenu.setText(AppUtils.getCaption(sCaption));
        commonMenu.setActionCommand(sActionCommand);
        //System.out.println(sActionCommand);
        jMenuParent.add(commonMenu);
        if (iMnemonic > 0) {
            commonMenu.setMnemonic(iMnemonic);
        }
//        if (keyStroke != null) {
//            commonMenu.setAccelerator(keyStroke);
//        }
        commonMenu.addActionListener(l);
        commonMenu.setLanguageStringKey(sLanguageStringKey);
        return (commonMenu);
    }

    public void setMenuLanguageID() {
        CommonMenu commonMenu;
        CommonMenuItem commonMenuItem;
        int iLanguageStringID;
        String sCaption;
        int iMnemonic;
        KeyStroke keyStroke;
        for (int i = 0; i < jMenuBar.getMenuCount(); i++) {
            //System.err.println(i);
            commonMenu = (CommonMenu) jMenuBar.getMenu(i);
            if (commonMenu != null) {
                iLanguageStringID = commonMenu.getLanguageStringID();
                sCaption = getOldLanguageString(iLanguageStringID);
                iMnemonic = AppUtils.getMnemonic(sCaption);
                commonMenu.setText(AppUtils.getCaption(sCaption));
                if (iMnemonic > 0) {
                    commonMenu.setMnemonic(iMnemonic);
                }
                //System.err.println(commonMenu.getItemCount());
                for (int j = 0; j < commonMenu.getItemCount(); j++) {
                    //System.err.print(" ");System.err.println(j);
                    commonMenuItem = (CommonMenuItem) commonMenu.getItem(j);
                    if (commonMenuItem != null) {
                        iLanguageStringID = commonMenuItem.getLanguageStringID();
                        sCaption = getOldLanguageString(iLanguageStringID);
                        iMnemonic = AppUtils.getMnemonic(sCaption);
                        keyStroke = AppUtils.getKeyStroke(sCaption);
                        commonMenuItem.setText(AppUtils.getCaption(sCaption));
                        if (iMnemonic > 0) {
                            commonMenuItem.setMnemonic(iMnemonic);
                        }
                        if (keyStroke != null) {
                            commonMenuItem.setAccelerator(keyStroke);
                        }
                    }
                }
            }
        }
    }

    public void setMenuLanguage() {
        CommonMenu commonMenu;
        CommonMenuItem commonMenuItem;
        String sLanguageStringKey;
        String sCaption;
        int iMnemonic;
        KeyStroke keyStroke;
        for (int i = 0; i < jMenuBar.getMenuCount(); i++) {
            //System.err.println(i);
            commonMenu = (CommonMenu) jMenuBar.getMenu(i);
            if (commonMenu != null) {
                sLanguageStringKey = commonMenu.getLanguageStringKey();
                sCaption = getLanguageString(sLanguageStringKey);
                iMnemonic = AppUtils.getMnemonic(sCaption);
                commonMenu.setText(AppUtils.getCaption(sCaption));
                if (iMnemonic > 0) {
                    commonMenu.setMnemonic(iMnemonic);
                }
                //System.err.println(commonMenu.getItemCount());
                for (int j = 0; j < commonMenu.getItemCount(); j++) {
                    //System.err.print(" ");System.err.println(j);
                    if (commonMenu.getItem(j) instanceof CommonMenuItem) {
                        commonMenuItem = (CommonMenuItem) commonMenu.getItem(j);
                        if (commonMenuItem != null) {
                            sLanguageStringKey = commonMenuItem.getLanguageStringKey();
                            sCaption = getLanguageString(sLanguageStringKey);
                            iMnemonic = AppUtils.getMnemonic(sCaption);
                            keyStroke = AppUtils.getKeyStroke(sCaption);
                            commonMenuItem.setText(AppUtils.getCaption(sCaption));
                            if (iMnemonic > 0) {
                                commonMenuItem.setMnemonic(iMnemonic);
                            }
                            if (keyStroke != null) {
                                commonMenuItem.setAccelerator(keyStroke);
                            }
                        }
                    }
                    if (commonMenu.getItem(j) instanceof CommonMenu) {
                        //System.out.println(commonMenu.getItem(j).getText());
                        CommonMenu subMenu = (CommonMenu) commonMenu.getItem(j);
                        sLanguageStringKey = subMenu.getLanguageStringKey();
                        sCaption = getLanguageString(sLanguageStringKey);
                        iMnemonic = AppUtils.getMnemonic(sCaption);
                        subMenu.setText(AppUtils.getCaption(sCaption));
                        if (iMnemonic > 0) {
                            subMenu.setMnemonic(iMnemonic);
                        }
                    }
                }
            }
        }
    }

    public void setCommonInternalFramesLanguage() {
        JInternalFrame internalFrames[] = jDesktopPane.getAllFrames();
        for (int iTmp = 0; iTmp < internalFrames.length; iTmp++) {
            ((CommonInternalFrame) internalFrames[iTmp]).languageChanged();
        }
    }

    public boolean languageDialog() {
        String sChoosenLanguage;
        LanguageDialog languageDialog = new LanguageDialog(this, formatInterface, languageHandlerInterface);
        if (languageDialog.showLoginDialog(this, sLanguage) == LoginDialog.YES) {
            sChoosenLanguage = languageDialog.getLanguage();
            if (languageHandlerInterface.setLanguage(sChoosenLanguage)) {
                sLanguage = sChoosenLanguage;
                this.setLocale(sLanguage, sLanguage);
                setMenuLanguage();
                setCommonInternalFramesLanguage();
                return (true);
            }
        }
        return (false);
    }

    public void setCommonInternalFramesFormat() {
        JInternalFrame internalFrames[] = jDesktopPane.getAllFrames();
        for (int iTmp = 0; iTmp < internalFrames.length; iTmp++) {
            ((CommonInternalFrame) internalFrames[iTmp]).formatChanged();
        }
    }

    public boolean formatDialog() {
        FormatDialog formatDialog = new FormatDialog(this, formatInterface, this);
        if (formatDialog.showFormatDialog(this) == FormatDialog.YES) {
            //if (formatDialog.changed()) {
            setCommonInternalFramesFormat();
            return (true);
            //}
        }
        return (false);
    }

    public void storeFormatsIntoPropertyFile() {
        this.setProperty("formats", this.getFormatsInXML());
    }

    public String getFormatsInXML() {
        StringBuffer sbRetVal = new StringBuffer();
        sbRetVal.append("<?xml version='1.0' encoding='ISO-8859-2'?><formats>");
        sbRetVal.append("<datePattern>").append(formatInterface.getDatePattern()).append("</datePattern>");
        sbRetVal.append("<dateSeparator>").append(formatInterface.getDateSeparator()).append("</dateSeparator>");
        sbRetVal.append("<timePattern>").append(formatInterface.getTimePattern()).append("</timePattern>");
        sbRetVal.append("<timeSeparator>").append(formatInterface.getTimeSeparator()).append("</timeSeparator>");
        //sbRetVal.append("<dateTimePattern>").append(formatInterface.getDateTimePattern()).append("</dateTimePattern>");
        sbRetVal.append("<decimalSeparator>").append(formatInterface.getDecimalSeparator()).append("</decimalSeparator>");
        sbRetVal.append("<groupingSeparator>").append(formatInterface.getGroupingSeparator()).append("</groupingSeparator>");
        sbRetVal.append("</formats>");
        return (sbRetVal.toString());
    }

    //@todo task : check valid formats, and set default if not valid the given
    //@todo task : set default formats if not given
    public void setFormatsFromXML(String sXML) {
        String sTmp = "";
        if (StringUtils.isNull(sXML, "").equalsIgnoreCase("")) {
            return;
        }
        DefaultXMLHandler xmlHandler = new DefaultXMLHandler(this);
        xmlHandler.readXMLString("formats", sXML, "ISO-8859-2");

        sTmp = StringUtils.isNull(XMLUtils.getElementText(xmlHandler.getFirstElement("formats", "datePattern")), "");
        if (!sTmp.equalsIgnoreCase("")) {
            formatInterface.setDatePattern(sTmp);
        }
        sTmp = StringUtils.isNull(XMLUtils.getElementText(xmlHandler.getFirstElement("formats", "dateSeparator")), "");
        if (!sTmp.equalsIgnoreCase("")) {
            formatInterface.setDateSeparator(sTmp.charAt(0));
        }
        sTmp = StringUtils.isNull(XMLUtils.getElementText(xmlHandler.getFirstElement("formats", "timePattern")), "");
        if (!sTmp.equalsIgnoreCase("")) {
            formatInterface.setTimePattern(sTmp);
        }
        sTmp = StringUtils.isNull(XMLUtils.getElementText(xmlHandler.getFirstElement("formats", "timeSeparator")), "");
        if (!sTmp.equalsIgnoreCase("")) {
            formatInterface.setTimeSeparator(sTmp.charAt(0));
        }
        sTmp = StringUtils.isNull(XMLUtils.getElementText(xmlHandler.getFirstElement("formats", "decimalSeparator")), "");
        if (!sTmp.equalsIgnoreCase("")) {
            formatInterface.setDecimalSeparator(sTmp.charAt(0));
        }
        sTmp = StringUtils.isNull(XMLUtils.getElementText(xmlHandler.getFirstElement("formats", "groupingSeparator")), "");
        if (!sTmp.equalsIgnoreCase("")) {
            formatInterface.setGroupingSeparator(sTmp.charAt(0));
        }
    }

    @Override
    public void setCursorWait() {
        //this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        ++iCursorWaitCount;
        if (iCursorWaitCount > 0) {
            this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        }
        //System.out.println(Integer.toString(iCursorWaitCount));
    }

    @Override
    public void setCursorDefault() {
        //this.setCursor(Cursor.getDefaultCursor());
        --iCursorWaitCount;
        if (iCursorWaitCount < 1) {
            this.setCursor(Cursor.getDefaultCursor());
            iCursorWaitCount = 0;
        }
        //System.out.println(Integer.toString(iCursorWaitCount));
    }

    private BufferedImage getScreenShot(Component component) {
        //source: http://stackoverflow.com/questions/5853879/swing-obtain-image-of-jframe
        BufferedImage image = new BufferedImage(component.getWidth(), component.getHeight(), BufferedImage.TYPE_INT_RGB);
        component.paint(image.getGraphics());
        return image;
    }

    public void selectedInternalFrameScreenshot() {
        JInternalFrame internalFrames[] = this.getInternalFrames();
        for (int i = 0; i < internalFrames.length; i++) {
            if (internalFrames[i].isSelected()) {
                BufferedImage bi = getScreenShot(internalFrames[i]);
                try {
                    //ImageIO.write(bi, "png", new File("screenshot.png"));
                    ImageIO.write(bi, "jpg", new File(FileUtils.createFileNameWithTimestamp("screenshot", "jpg")));
                } catch (Exception e) {
                    this.handleError(e);
                }
            }
        }
    }

    private void mockup(JPanel jPanel, int iLevel, StringBuffer sb, String sTitle, int x, int y, int w, int h) {
        int iCC = 0;
        Component comp = null;
        int iX = 0;
        int iY = 0;
        int iW = 0;
        int iH = 0;
        String sName = "";
        String sText = "";
        String sIndent = StringUtils.repeat("    ", iLevel);
        int iID = 0;
        String sTypeID = "";
        int iBaseFontSize = 8;
        int iFontSize = 12;
        String sCaption = "";
        int iOffsetVertical = 25;
        boolean bChild = false;
        String sLog = "";

        if (iLevel > 0) {
            iOffsetVertical = 0;
        }
        synchronized (jPanel.getTreeLock()) {
            iCC = jPanel.getComponentCount();
            System.out.println(sIndent + Integer.toString(iCC) + " components.");
            sb.append("{\"controls\":");
            sb.append(StringUtils.sCrLf);
            sb.append("{\"control\":[");
            sb.append(StringUtils.sCrLf);
            if (iLevel == 0) {
                sb.append(sIndent);
                sb.append("{");
                sb.append("\"ID\":\"" + Integer.toString(iID) + "\",");
                sb.append("\"h\":\"" + Integer.toString(h + iOffsetVertical) + "\",");
                sb.append("\"measuredH\":\"" + Integer.toString(h + iOffsetVertical) + "\",");
                sb.append("\"measuredW\":\"" + Integer.toString(w) + "\",");
                sb.append("\"properties\":{\"size\":\"" + Integer.toString(iFontSize) + "\",\"text\":\"" + sTitle + "\"},");
                sb.append("\"typeID\":\"TitleWindow\",");
                sb.append("\"x\":\"" + Integer.toString(x) + "\",");
                sb.append("\"y\":\"" + Integer.toString(y) + "\",");
                sb.append("\"w\":\"" + Integer.toString(w) + "\",");
                sb.append("\"zOrder\":\"" + Integer.toString(iID) + "\"");
                sb.append("}");
                sb.append(",");
                sb.append(StringUtils.sCrLf);
                bChild = true;
                sLog = sTitle + " (" + Integer.toString(x) + ", " + Integer.toString(y) + ", " + Integer.toString(w) + ", " + Integer.toString(h) + ")";
                sLog += "ID=" + Integer.toString(iID);
                System.out.println(sLog);
            }
            for (int i = 0; i < iCC; i++) {
                ++iID;
                comp = jPanel.getComponent(i);
                iW = comp.getWidth();
                iH = comp.getHeight();
                iX = comp.getLocation().x;
                iY = comp.getLocation().y;
                sName = StringUtils.isNull(comp.getName(), "");
                sText = "";
                sTypeID = "";
                iFontSize = iBaseFontSize;
                if (comp instanceof JLabel) {
                    sTypeID = "Label";
                    sText = ((JLabel) comp).getText();
                    iFontSize = 12;
                }
                if (comp instanceof MagTextField) {
                    sTypeID = "TextInput";
                }
                if (comp instanceof MagComboBoxField) {
                    sTypeID = "ComboBox";
                }
                if (comp instanceof JRadioButton) {
                    sTypeID = "RadioButton";
                    sText = ((JRadioButton) comp).getText();
                    iFontSize = 12;
                }
                if (comp instanceof JButton) {
                    sTypeID = "Button";
                    sText = ((JButton) comp).getText();
                    iFontSize = 12;
                }
                if (comp instanceof JScrollPane) {
                    //JScrollPane jsp = new JScrollPane(this);
                    JViewport jv = ((JScrollPane) comp).getViewport();
                    if (jv.getView() instanceof JTable) {
                        sTypeID = "DataGrid";
                        sText = "";
                        iFontSize = 12;
                    }
                }
                sCaption = sName;
                if (!sText.equalsIgnoreCase("")) {
                    sCaption = sText;
                }
                if (sCaption.equalsIgnoreCase("")) {
                    sCaption = " ";
                }
                sLog = sIndent + comp.getClass().getName() + " " + sName + " (" + Integer.toString(iX) + ", " + Integer.toString(iY) + ", " + Integer.toString(iW) + ", " + Integer.toString(iH) + ") " + sText + " ";
//                if (iLevel > 0 || iID > 0) {
//                    sb.append(",");
//                    sb.append(StringUtils.sCrLf);
//                }
                if (i > 0) {
                    //if ((iH > 0 || iW > 0 || iX > 0 || iY > 0) && !sCaption.equalsIgnoreCase("")) {
                    if (iH > 0 || iW > 0 || iX > 0 || iY > 0) {
                        if (bChild) {
                            sb.append(",");
                            sb.append(StringUtils.sCrLf);
                        }
                    }
                }
                if (comp instanceof hu.mgx.swing.CommonPanel) {
                    sb.append("{");
                    sb.append("\"ID\":\"" + Integer.toString(iID) + "\",");
                    sb.append("\"children\":");
                    sb.append(StringUtils.sCrLf);
                    System.out.println(sLog);
                    mockup((CommonPanel) comp, iLevel + 1, sb, sTitle, x, y, w, h);
                    sb.append(",");
                    sb.append("\"h\":\"" + Integer.toString(iH) + "\",");
                    sb.append("\"measuredH\":\"" + Integer.toString(iH) + "\",");
                    sb.append("\"measuredW\":\"" + Integer.toString(iW) + "\",");
                    sb.append("\"typeID\":\"__group__\",");
                    sb.append("\"w\":\"" + Integer.toString(iW) + "\",");
                    sb.append("\"x\":\"" + Integer.toString(iX) + "\",");
                    sb.append("\"y\":\"" + Integer.toString(iY + iOffsetVertical) + "\",");
                    sb.append("\"zOrder\":\"" + Integer.toString(iID) + "\"");
                    sb.append("}");
                    bChild = true;
                    sLog += "ID=" + Integer.toString(iID);
                } else {
                    //if ((iH > 0 || iW > 0 || iX > 0 || iY > 0) && !sCaption.equalsIgnoreCase("")) {
                    if (iH > 0 || iW > 0 || iX > 0 || iY > 0) {
                        sb.append(sIndent);
                        sb.append("{");
                        sb.append("\"ID\":\"" + Integer.toString(iID) + "\",");
                        sb.append("\"h\":\"" + Integer.toString(iH) + "\",");
                        sb.append("\"measuredH\":\"" + Integer.toString(iH) + "\",");
                        sb.append("\"measuredW\":\"" + Integer.toString(iW) + "\",");
                        sb.append("\"properties\":{\"size\":\"" + Integer.toString(iFontSize) + "\",\"text\":\"" + sCaption + "\"},");
                        sb.append("\"typeID\":\"" + sTypeID + "\",");
                        sb.append("\"x\":\"" + Integer.toString(iX) + "\",");
                        sb.append("\"y\":\"" + Integer.toString(iY + iOffsetVertical) + "\",");
                        sb.append("\"w\":\"" + Integer.toString(iW) + "\",");
                        sb.append("\"zOrder\":\"" + Integer.toString(iID) + "\"");
                        sb.append("}");
                        sLog += "ID=" + Integer.toString(iID);
                        bChild = true;
                    }
                }
                System.out.println(sLog);
            }
            sb.append(StringUtils.sCrLf);
            sb.append("]}");
            sb.append(StringUtils.sCrLf);
            if (iLevel == 0) {
                sb.append(",");
                sb.append("\"measuredH\":\"" + Integer.toString(h) + "\",");
                sb.append("\"measuredW\":\"" + Integer.toString(w) + "\",");
                sb.append("\"mockupH\":\"" + Integer.toString(h) + "\",");
                sb.append("\"mockupW\":\"" + Integer.toString(w) + "\",");
                sb.append("\"version\":\"1.0\"");
            }
            sb.append("}");
            sb.append(StringUtils.sCrLf);
        }
    }

    public void selectedInternalFrameMockup() {
        StringBuffer sb = null;
        JInternalFrame internalFrames[] = this.getInternalFrames();
        for (int i = 0; i < internalFrames.length; i++) {
            if (internalFrames[i].isSelected()) {
                sb = new StringBuffer();
                sb.append("{\"mockup\":");
                mockup((JPanel) internalFrames[i].getContentPane(), 0, sb, internalFrames[i].getTitle(), 0, 0, internalFrames[i].getWidth(), internalFrames[i].getHeight());
                sb.append("}");
                StringSelection stringSelection = new StringSelection(sb.toString());
                Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
                clpbrd.setContents(stringSelection, null);
            }
        }
    }

    public void copyStringBufferToClipboard(StringBuffer sb) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(sb.toString()), null);
    }

    public void copyStringToClipboard(String s) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(s), null);
    }

    @Override
    public void viewPdf(byte[] b) {
        this.viewPdf(b, "", MediaSizeName.ISO_A4, false);
    }

    @Override
    public void viewPdf(byte[] b, String sTitle) {
        this.viewPdf(b, sTitle, MediaSizeName.ISO_A4, false);
    }

    @Override
    public void viewPdf(byte[] b, String sTitle, MediaSizeName mediaSize) {
        this.viewPdf(b, sTitle, mediaSize, false);
    }

    @Override
    public void viewPdf(byte[] b, String sTitle, MediaSizeName mediaSize, boolean bMaximized) {
        if (bMaximized) {
            this.addInternalFrameMaximized(new PdfViewerInternalFrame(this, b, sTitle, mediaSize), sTitle);
        } else {
            this.addInternalFrame(new PdfViewerInternalFrame(this, b, sTitle, mediaSize), sTitle);
        }
    }

    @Override
    public void viewAndPrintPdf(byte[] b) {
        this.viewAndPrintPdf(b, "", MediaSizeName.ISO_A4);
    }

    @Override
    public void viewAndPrintPdf(byte[] b, String sTitle) {
        this.viewAndPrintPdf(b, sTitle, MediaSizeName.ISO_A4);
    }

    @Override
    public void viewAndPrintPdf(byte[] b, String sTitle, MediaSizeName mediaSize) {
        this.addInternalFrame(new PdfViewerInternalFrame(this, b, sTitle, mediaSize, true), sTitle);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == null) {
            if (e.getSource() != null) {
                if (e.getSource().equals(timerClock)) {
                    if (jLabelClock != null) {
                        displayClock();
                    }
                    if (jLabelMemory != null) {
                        displayMemory(false);
                        //displayMemoryInThread();
                    }
                }
                if (e.getSource().equals(timerSplash)) {
                    if (imageSplash != null) {
                        hideSplash();
                    }
                }
            }
        } else {
            if (e.getActionCommand().equals("mnuDeveloperScreenshot")) {
                this.selectedInternalFrameScreenshot();
            }
            if (e.getActionCommand().equals("mnuDeveloperMockup")) {
                this.selectedInternalFrameMockup();
            }
        }
    }

    //--- SwingAppInterface
    //--- AppInterface
    public String getAppName() {
        return (sAppName);
    }

    //--- ErrorHandlerInterface
    public void handleError(Exception e) {
        this.handleError(e, null, 0);
    }

    //--- ErrorHandlerInterface
    public void handleError(Exception e, int iErrorLevel) {
        this.handleError(e, null, iErrorLevel);
    }

    //--- ErrorHandlerInterface
    public void handleError(String sInfo) {
        this.handleError(null, sInfo, 0);
    }

    //--- ErrorHandlerInterface
    public void handleError(String sInfo, int iErrorLevel) {
        this.handleError(null, sInfo, iErrorLevel);
    }

    //--- ErrorHandlerInterface
    public void handleError(Exception e, String sInfo) {
        this.handleError(e, sInfo, 0);
    }

    //--- ErrorHandlerInterface
    public void handleError(Exception e, String sInfo, int iErrorLevel) {
        String sErrMsg = "";
        if (sInfo == null) {
            sInfo = "";
        }

        if (e != null) {
            sErrMsg = e.getLocalizedMessage();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            PrintStream errorStream = new PrintStream(byteArrayOutputStream);
            logLine(sErrMsg);
            e.printStackTrace(errorStream);
            logLine(byteArrayOutputStream.toString());
        }
        if (sInfo.length() > 0) {
            logLine(sInfo);
        }
        if (!bSilent) {
            errorBox.showError(this, "Error", sErrMsg + (sInfo.length() > 0 ? (sErrMsg.length() > 0 ? StringUtils.sCrLf : "") + sInfo : ""));
        }
        if (iErrorLevel > 8) {
            System.exit(-1);
        }
    }

    public void setSilent(boolean b) {
        bSilent = b;
    }

    public void setNoSQLLog(boolean b) {
        bNoSQLLog = b;
    }

    public void setLoggerInterface(LoggerInterface loggerInterface) {
        if (loggerInterface != null) {
            this.loggerInterface = loggerInterface;
        }
    }

    public LoggerInterface getLoggerInterface() {
        return (this.loggerInterface);
    }

    private void createLogFile() {
        try {
            logWriter = new PrintWriter(new FileOutputStream(sLogFilename + sLogFilenameEnd + ".log", true));
        } catch (FileNotFoundException e) {
            System.err.println(e.getLocalizedMessage());
            e.printStackTrace(System.err);
        }
    }

    private void deleteLogFileWhenEmpty() {
        File f = new File(sLogFilename + sLogFilenameEnd + ".log");
        if (f.exists()) {
            if (f.length() == 0L) {
                f.delete();
            }
        }
    }

    private String createLogLine(String sLine, int iLogLevel) {
        String sLevel = "";
        switch (iLogLevel) {
            case LOG_DEBUG:
                sLevel = "DEBUG";
                break;
            case LOG_INFO:
                sLevel = "INFO";
                break;
            case LOG_WARN:
                sLevel = "WARN";
                break;
            case LOG_ERROR:
                sLevel = "ERROR";
                break;
            case LOG_FATAL:
                sLevel = "FATAL";
                break;
            case LOG_SQL:
                sLevel = "SQL";
                break;
            default:
                sLevel = "INFO";
                break;
        }
        String sOsName = System.getProperty("os.name");
        String sOsVersion = System.getProperty("os.version");
        String sJavaVersion = System.getProperty("java.version");
        String sVersionString = getVersionString(iMajor, iMinor, iRevision, sDateTimeVersion);
        String sLogLine = "[" + (sOsName.equals("") ? "" : sOsName + " ") + (sOsVersion.equals("") ? "" : sOsVersion + " ") + (sJavaVersion.equals("") ? "" : sJavaVersion + " ") + (sVersionString.equals("") ? "" : sVersionString + " ") + logDateFormat.format(new java.util.Date()) + "] [" + sLevel + "] " + sLine;
        return (sLogLine);
    }

    //--- LoggerInterface
    public void logLine(String sLine) {
        if (loggerInterface != null) {
            loggerInterface.logLine(sLine);
        } else {
            logLine(sLine, LOG_INFO);
        }
    }

    //--- LoggerInterface
    public void logLine(String sLine, int iLogLevel) {
        if (loggerInterface != null) {
            loggerInterface.logLine(sLine, iLogLevel);
        } else {
            logLine(sLine, iLogLevel, "");
        }
    }

    //--- LoggerInterface
    public void logLine(String sLine, int iLogLevel, String sSearch) {
        if (iLogLevel == LOG_SQL && bNoSQLLog) {
            return;
        }
        if (loggerInterface != null) {
            loggerInterface.logLine(sLine, iLogLevel, sSearch);
        } else {
            if ((iLogLevel != LOG_SQL) && (iLogLevel < this.iLogLevelLimit)) {
                return;
            }
            if (sLine == null) {
                sLine = "NULL";
            }
            if (logWriter != null && iLogType == TYPE_DATED) {
                if (!sLogFilenameEnd.equals("_" + fileDateFormat.format(new java.util.Date()))) {
                    logWriter.close();
                    sLogFilenameEnd = "_" + fileDateFormat.format(new java.util.Date());
                    createLogFile();
                    if (jEditorPaneLog != null) {
                        jEditorPaneLog.setText("");
                    }
                }
            }
            String sLogLine = createLogLine(sLine, iLogLevel);
            if (jEditorPaneLog != null) {
                jEditorPaneLog.setText(StringUtils.right(jEditorPaneLog.getText(), 10000) + (jEditorPaneLog.getText().length() == 0 ? "" : StringUtils.sCrLf) + createLogLine(sLine, iLogLevel));
            }
            if (logWriter != null) {
                logWriter.println(sLogLine);
                logWriter.flush();
            } else {
                System.err.println(sLogLine);
            }
            return;
        }
    }

    //--- LoggerInterface
    public void setLogLevel(int iLogLevel) {
        if (loggerInterface != null) {
            loggerInterface.setLogLevel(iLogLevel);
        } else {
            this.iLogLevelLimit = iLogLevel;
        }
    }

    //--- LoggerInterface
    public int getLogLevel() {
        if (loggerInterface != null) {
            return (loggerInterface.getLogLevel());
        } else {
            return (iLogLevelLimit);
        }
    }

    //--- LoggerInterface
    public void setLogType(int iLogType) {
        if (loggerInterface != null) {
            loggerInterface.setLogType(iLogType);
        } else {
            this.iLogType = iLogType;
        }
    }

    //--- LoggerInterface
    public void closeLog() {
        if (loggerInterface != null) {
            loggerInterface.closeLog();
        } else {
            if (logWriter != null) {
                logWriter.close();
                deleteLogFileWhenEmpty();
            }
        }
    }

    //--- FormatInterface
    public char getDateSeparator() {
        return (formatInterface.getDateSeparator());
    }

    //--- FormatInterface
    public void setDateSeparator(char cDateSeparator) {
        formatInterface.setDateSeparator(cDateSeparator);
    }

    //--- FormatInterface
    public char getTimeSeparator() {
        return (formatInterface.getTimeSeparator());
    }

    //--- FormatInterface
    public void setTimeSeparator(char cTimeSeparator) {
        formatInterface.setTimeSeparator(cTimeSeparator);
    }

    //--- FormatInterface
    public String getDatePattern() {
        return (formatInterface.getDatePattern());
    }

    //--- FormatInterface
    public void setDatePattern(String sDatePattern) {
        formatInterface.setDatePattern(sDatePattern);
    }

    //--- FormatInterface
    public String getTimePattern() {
        return (formatInterface.getTimePattern());
    }

    //--- FormatInterface
    public void setTimePattern(String sTimePattern) {
        formatInterface.setTimePattern(sTimePattern);
    }

    //--- FormatInterface
    public String getDateTimePattern() {
        return (formatInterface.getDateTimePattern());
    }

    //--- FormatInterface
    public SimpleDateFormat getDateFormat() {
        return (formatInterface.getDateFormat());
    }

    //--- FormatInterface
    public SimpleDateFormat getTimeFormat() {
        return (formatInterface.getTimeFormat());
    }

    //--- FormatInterface
    public SimpleDateFormat getDateTimeFormat() {
        return (formatInterface.getDateTimeFormat());
    }

    //--- FormatInterface
    public SimpleDateFormat getClockDateTimeFormat() {
        return (formatInterface.getClockDateTimeFormat());
    }

    //--- FormatInterface
    public SimpleDateFormat getLogDateTimeFormat() {
        return (formatInterface.getLogDateTimeFormat());
    }

    //--- FormatInterface
    public SimpleDateFormat getSQLDateFormat() {
        return (formatInterface.getSQLDateFormat());
    }

    //--- FormatInterface
    public SimpleDateFormat getSQLTimeFormat() {
        return (formatInterface.getSQLTimeFormat());
    }

    //--- FormatInterface
    public SimpleDateFormat getSQLDateTimeFormat() {
        return (formatInterface.getSQLDateTimeFormat());
    }

    //--- FormatInterface
    public SimpleDateFormat getFileNameDateTimeFormat() {
        return (formatInterface.getFileNameDateTimeFormat());
    }

    //--- FormatInterface
    public char getDecimalSeparator() {
        return (formatInterface.getDecimalSeparator());
    }

    //--- FormatInterface
    public void setDecimalSeparator(char cDecimalSeparator) {
        formatInterface.setDecimalSeparator(cDecimalSeparator);
    }

    //--- FormatInterface
    public char getGroupingSeparator() {
        return (formatInterface.getGroupingSeparator());
    }

    //--- FormatInterface
    public void setGroupingSeparator(char cGroupingSeparator) {
        formatInterface.setGroupingSeparator(cGroupingSeparator);
    }

    //--- FormatInterface
    public DecimalFormat getDecimalFormat() {
        return (formatInterface.getDecimalFormat());
    }

    //--- FormatInterface
    public DecimalFormat getDecimalFormat(int iScale) {
        return (formatInterface.getDecimalFormat(iScale));
    }

    //--- VersionInterface
    public int getMajor() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //--- VersionInterface
    public int getMinor() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //--- VersionInterface
    public int getRevision() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //--- VersionInterface
    public VersionHistory getVersionHistory() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //--- LanguageInterface
    public void clearLanguages() {
        languageHandlerInterface.clearLanguages();
    }

    //--- LanguageInterface
    public void setLanguageSubstitute(boolean bSubstitute) {
        languageHandlerInterface.setLanguageSubstitute(bSubstitute);
    }

    //--- LanguageInterface
    public boolean isLanguageSubstitute() {
        return (languageHandlerInterface.isLanguageSubstitute());
    }

    //--- LanguageInterface
    public void setMarkUndefined(boolean bMarkUndefined) {
        languageHandlerInterface.setMarkUndefined(bMarkUndefined);
    }

    //--- LanguageInterface
    public boolean isMarkUndefined() {
        return (languageHandlerInterface.isMarkUndefined());
    }

    //--- LanguageInterface
    public boolean addLanguage(String sLanguage) {
        return (languageHandlerInterface.addLanguage(sLanguage));
    }

    //--- LanguageInterface
    public boolean setLanguage(String sLanguage) {
        return (languageHandlerInterface.setLanguage(sLanguage));
    }

    //--- LanguageInterface
    public String getLanguage() {
        return (languageHandlerInterface.getLanguage());
    }

    //--- LanguageInterface
    public Vector<String> getAvailableLanguages() {
        return (languageHandlerInterface.getAvailableLanguages());
    }

    //--- LanguageInterface
    public void setLanguageString(int iKey, String sValue) {
        languageHandlerInterface.setLanguageString(iKey, sValue);
    }

    //--- LanguageInterface
    public void setLanguageString(String sLanguage, int iKey, String sValue) {
        languageHandlerInterface.setLanguageString(sLanguage, iKey, sValue);
    }

    //--- LanguageInterface
    public String getLanguageString(int iKey) {
        return (languageHandlerInterface.getLanguageString(iKey));
    }

    //--- LanguageInterface
    public String getLanguageString(int iKey, String sDefaultValue) {
        return (languageHandlerInterface.getLanguageString(iKey, sDefaultValue));
    }

    //--- LanguageInterface
    public String getLanguageString(String sLanguage, int iKey, String sDefaultValue) {
        return (languageHandlerInterface.getLanguageString(sLanguage, iKey, sDefaultValue));
    }

    //--- LanguageInterface
    public void setEnabledLanguages(String sEnabledLanguages) {
        languageHandlerInterface.setEnabledLanguages(sEnabledLanguages);
    }

    public void setLanguageString(String sKey, String sValue) {
        languageHandlerInterface.setLanguageString(sKey, sValue);
    }

    public void setLanguageString(String sLanguage, String sKey, String sValue) {
        languageHandlerInterface.setLanguageString(sLanguage, sKey, sValue);
    }

    public String getLanguageString(String sKey) {
        return (languageHandlerInterface.getLanguageString(sKey));
    }

    public String getLanguageString(String sKey, String sDefaultValue) {
        return (languageHandlerInterface.getLanguageString(sKey, sDefaultValue));
    }

    public String getLanguageString(String sLanguage, String sKey, String sDefaultValue) {
        return (languageHandlerInterface.getLanguageString(sLanguage, sKey, sDefaultValue));
    }

    public void setLanguageStringXML(String sXML) {
        languageHandlerInterface.setLanguageStringXML(sXML);
    }

    public void closeConnections() {
        Vector<String> v = getConnectionNames();
        for (int i = 0; i < v.size(); i++) {
            disconnect(v.elementAt(i));
        }
    }

    //--- ConnectionHandlerInterface
    @Override
    public boolean createConnection(String sName, String sDriver, String sURL, String sUserid, String sPassword) {
        return (defaultConnectionHandler.createConnection(sName, sDriver, sURL, sUserid, sPassword));
    }

    //--- ConnectionHandlerInterface
    @Override
    public boolean createConnection(String sName, String sDriver, String sURL, String sUserid, String sPassword, String sDBMSType) {
        return (defaultConnectionHandler.createConnection(sName, sDriver, sURL, sUserid, sPassword, sDBMSType));
    }

    //--- ConnectionHandlerInterface
    @Override
    public boolean createConnection(String sName, String sDriver, String sURL, String sUserid, String sPassword, int iDBMSType) {
        return (defaultConnectionHandler.createConnection(sName, sDriver, sURL, sUserid, sPassword, iDBMSType));
    }

    //--- ConnectionHandlerInterface
    @Override
    public boolean connect(String sName) {
        return (defaultConnectionHandler.connect(sName));
    }

    //--- ConnectionHandlerInterface
    @Override
    public Connection getConnection(String sName) {
        return (defaultConnectionHandler.getConnection(sName));
    }

    //--- ConnectionHandlerInterface
    @Override
    public Connection getTemporaryConnection(String sName) {
        return (defaultConnectionHandler.getTemporaryConnection(sName));
    }

    //--- ConnectionHandlerInterface
    @Override
    public int getDBMSType(String sName) {
        return (defaultConnectionHandler.getDBMSType(sName));
    }

    //--- ConnectionHandlerInterface
    @Override
    public boolean disconnect(String sName) {
        return (defaultConnectionHandler.disconnect(sName));
    }

    //--- ConnectionHandlerInterface
    @Override
    public void setDatabaseInfo(String sName, DatabaseInfo di) {
        defaultConnectionHandler.setDatabaseInfo(sName, di);
    }

    //--- ConnectionHandlerInterface
    @Override
    public DatabaseInfo getDatabaseInfo(String sName) {
        return (defaultConnectionHandler.getDatabaseInfo(sName));
    }

    //--- ConnectionHandlerInterface
    @Override
    public Exception getConnectionLastException() {
        return (defaultConnectionHandler.getConnectionLastException());
    }

    @Override
    public Vector<String> getConnectionNames() {
        return (defaultConnectionHandler.getConnectionNames());
    }

    @Override
    public boolean checkConnection(String sName) {
        return (defaultConnectionHandler.checkConnection(sName));
    }

    //--- MessageHandlerInterface
    @Override
    public void handleMessage(MessageEvent e) {
        defaultMessageHandler.handleMessage(e);
    }

    //--- MessageHandlerInterface
    @Override
    public void addMessageEventListener(MessageEventListener mel) {
        defaultMessageHandler.addMessageEventListener(mel);
    }

    //--- MessageHandlerInterface
    @Override
    public void removeMessageEventListener(MessageEventListener mel) {
        defaultMessageHandler.removeMessageEventListener(mel);
    }

    public void setPermissionHandler(PermissionInterface pi) {
        this.permissionHandler = pi;
    }

    public PermissionInterface getPermissionHandler() {
        return (permissionHandler);
    }

    //--- PermissionInterface
    @Override
    public Object getPermissionUserIdentifier() {
        if (permissionHandler != null) {
            return (permissionHandler.getPermissionUserIdentifier());
        }
        return (null);
    }

    //--- PermissionInterface
    @Override
    public Object getPermissionUserName() {
        if (permissionHandler != null) {
            return (permissionHandler.getPermissionUserName());
        }
        return (null);
    }

    //--- PermissionInterface
    @Override
    public Object getPermissionUserDisplayName() {
        if (permissionHandler != null) {
            return (permissionHandler.getPermissionUserDisplayName());
        }
        return (null);
    }

    //--- PermissionInterface
    @Override
    public boolean permissionLogin(Object oUserIdentifier, Object oPassword) {
        if (permissionHandler != null) {
            return (permissionHandler.permissionLogin(oUserIdentifier, oPassword));
        }
        return (false);
    }

    //--- PermissionInterface
    @Override
    public boolean permissionLogout() {
        if (permissionHandler != null) {
            return (permissionHandler.permissionLogout());
        }
        return (false);
    }

    //--- PermissionInterface
    @Override
    public boolean hasPermission(Object oPermission) {
        if (permissionHandler != null) {
            return (permissionHandler.hasPermission(oPermission));
        }
        return (false);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(jLabelMemory)) {
            bMemoryDisplayMode = !bMemoryDisplayMode;
            displayMemory(false);
        }
        if (e.getSource().equals(jLabelLog)) {
//            jScrollPaneLog.setVisible(!jScrollPaneLog.isVisible());
//            this.statusBar.setPreferredSize(new java.awt.Dimension(21, (jScrollPaneLog.isVisible() ? 200 : 21)));
            if (jScrollPaneLog.isVisible()) {
                jScrollPaneLog.setVisible(false);
                this.statusBar.setPreferredSize(new java.awt.Dimension(21, 21));
            } else {
                this.statusBar.setPreferredSize(new java.awt.Dimension(21, 200));
                jScrollPaneLog.setVisible(true);
            }
        }
        if (e.getSource().equals(jLabelWindow)) {
            selectWindow();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    //SwingAppInterface
    @Override
    public JFrame getAppFrame() {
        return (this);
    }

    //SwingAppInterface
    @Override
    public String getBaseTitle() {
        return (getTitleBasedOnLanguage());
    }

    //SwingAppInterface
    @Override
    public String getFullTitle() {
        return (this.getTitle());
    }

    //SwingAppInterface
    @Override
    public JEditorPane getLogPane() {
        return (jEditorPaneLog);
    }

    //SwingAppInterface
    @Override
    public void setDesktopBackground(Color bg) {
        if (colorOldBackground == null) {
            colorOldBackground = jDesktopPane.getBackground();
        }
        if (bg != null) {
            jDesktopPane.setBackground(bg);
        } else {
            if (colorOldBackground != null) {
                jDesktopPane.setBackground(colorOldBackground);
            }
        }

    }

    //SwingAppInterface
    @Override
    public HashMap<String, JMenuItem> getMenuMap() {
        return (hmMenus);
    }

    public void setButtonFocusInputMapToEnter() {
        InputMap im = (InputMap) UIManager.get("Button.focusInputMap");
        //im.put(KeyStroke.getKeyStroke("ENTER"), "pressed");
        //im.put(KeyStroke.getKeyStroke("released ENTER"), "released");
        //im.put(KeyStroke.getKeyStroke("SPACE"), "none");
        //im.put(KeyStroke.getKeyStroke("released SPACE"), "none");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), "pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true), "released");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false), "none");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true), "none");
    }
}
