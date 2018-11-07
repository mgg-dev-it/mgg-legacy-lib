package hu.mag.swing.pdf;

import hu.mgx.app.swing.SwingApp;
import hu.mgx.swing.CommonInternalFrame;
import hu.mgx.swing.CommonPanel;
import hu.mgx.util.StringUtils;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.io.File;
import javax.print.attribute.standard.MediaSizeName;
import javax.swing.JPanel;

import org.icepdf.ri.common.ComponentKeyBinding;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;
import org.icepdf.ri.common.ViewModel;

/**
 *
 * @author MaG
 */
public class PdfViewerInternalFrame extends CommonInternalFrame {

    protected SwingApp swingApp;
    private CommonPanel mainPane;
    private SwingController controller;
    private SwingViewBuilder factory;
    private JPanel viewerComponentPanel;
    private String sTitle;

    public PdfViewerInternalFrame(SwingApp swingApp) {
        this.swingApp = swingApp;
        init();
    }

    public PdfViewerInternalFrame(SwingApp swingApp, byte[] b) {
        this(swingApp, b, "", MediaSizeName.ISO_A4);
    }

    public PdfViewerInternalFrame(SwingApp swingApp, byte[] b, String sTitle) {
        this(swingApp, b, sTitle, MediaSizeName.ISO_A4);
    }

    public PdfViewerInternalFrame(SwingApp swingApp, byte[] b, String sTitle, MediaSizeName mediaSize) {
        this(swingApp, b, sTitle, mediaSize, false);
    }

    public PdfViewerInternalFrame(SwingApp swingApp, byte[] b, String sTitle, MediaSizeName mediaSize, boolean bPrint) {
        this.swingApp = swingApp;
        this.sTitle = StringUtils.isNull(sTitle, "").trim();
        init();
        viewPdf(b, sTitle, mediaSize, bPrint);
    }

    private void init() {
        //http://www.icesoft.org/wiki/display/PDF/API+Documentation
        //http://www.icesoft.org/wiki/display/PDF/Using+the+PDF+Viewer+Component

        //http://www.icesoft.org/JForum/posts/list/23268.page#sthash.PsInw3Vr.wdhl6MnC.dpbs
        //org.icepdf.core.pobjects.acroform.SignatureHandler Security.insertProviderAt((Provider) provider, 2); ==> Security.addProvider((Provider) provider);
        setTitle("PDF megtekintő" + (sTitle.length() > 0 ? " - " + sTitle : ""));
        mainPane = new CommonPanel();
        setContentPane(mainPane);
        mainPane.setPreferredSize(new Dimension(new Double(swingApp.getSize().width * 0.7).intValue(), new Double(swingApp.getSize().height * 0.7).intValue()));

        //System.setProperty("org.icepdf.core.awtFontLoading", "true");
        //System.getProperties().put("org.icepdf.core.awtFontLoading", "true");
        // build a controller
        controller = new SwingController();

        ViewModel.setDefaultFile(new File(javax.swing.filechooser.FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + (sTitle.length() > 0 ? File.separator + StringUtils.eliminateDoubleSpaces(sTitle).replace(" ", "_") + "_" + swingApp.getFileNameDateTimeFormat().format(new java.util.Date()) : File.separator + "pdf_" + swingApp.getFileNameDateTimeFormat().format(new java.util.Date()))));

        // Build a SwingViewFactory configured with the controller
        factory = new SwingViewBuilder(controller);

        // Use the factory to build a JPanel that is pre-configured
        // with a complete, active Viewer UI.
        viewerComponentPanel = factory.buildViewerPanel();

        // add copy keyboard command
        ComponentKeyBinding.install(controller, viewerComponentPanel);

        // add interactive mouse link annotation support via callback
        controller.getDocumentViewController().setAnnotationCallback(new org.icepdf.ri.common.MyAnnotationCallback(controller.getDocumentViewController()));

        mainPane.addToGrid(viewerComponentPanel, 0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.BOTH);
        pack();
        setVisible(true);
    }

    public void viewPdf(byte[] b) {
        viewPdf(b, "", MediaSizeName.ISO_A4, false);
    }

    public void viewPdf(byte[] b, String sTitle) {
        viewPdf(b, sTitle, MediaSizeName.ISO_A4, false);
    }

    public void viewPdf(byte[] b, String sTitle, MediaSizeName mediaSize, boolean bPrint) {
//        try {
//            System.out.println("before: " + javax.crypto.Cipher.getInstance("RSA").getProvider().getName());
//        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
//            swingApp.handleError(e);
//        }
        controller.openDocument(b, 0, b.length, sTitle, null);
        controller.setPrintDefaultMediaSizeName(mediaSize);
        if (bPrint) {
            controller.print(true);
        }
//        try {
//            System.out.println("after: " + javax.crypto.Cipher.getInstance("RSA").getProvider().getName());
//        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
//            swingApp.handleError(e);
//        }
    }
}
