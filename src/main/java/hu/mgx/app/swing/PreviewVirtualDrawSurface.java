package hu.mgx.app.swing;

//@todo a "papír" méretét is lehessen változtatni (az ablakhoz, illetve a képernyõfelbontáshoz igazodva)
import hu.mgx.swing.CommonPanel;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import hu.mgx.draw.*;

public class PreviewVirtualDrawSurface extends CommonPanel implements ActionListener, ChangeListener
{

    private Dimension dimPreferred;
    private JScrollPane jScrollPane;
    //private int iScrollPosition = 0;
    private CommonPanel previewPanel;
    //private PrintablePages printablePages;
    private PrintablePage printablePage;
    private SwingApp app = null;
    private PageFormat pageFormat;
    private int iWidth;
    private int iHeight;
    private int iPanelWidth;
    private int iPanelHeight;
    //private double dPaperSizeRatio=1.0;
    //private double dPrintSizeRatio=1.0;
    private double dPaperSizeRatio = 1.0;
    private double dPrintSizeRatio = 1.0;    //private Report report;
    private VirtualDrawSurface virtualDrawSurface;
    private JRadioButton jRadioButtonPortrait = null;
    private JRadioButton jRadioButtonLandscape = null;
    private JButton jButtonPrint;
    private JButton jButtonPageSetup;
    private JSpinner jSpinnerRate;
    private SpinnerNumberModel spinnerNumberModelRate;
    private JSpinner jSpinnerPage;
    private SpinnerNumberModel spinnerNumberModelPage;
    private JLabel jLabelPageCount;
    private int iPageCount = 1;
    private int iCurrentPage = 1;
    private boolean bFullPage = false;

    public PreviewVirtualDrawSurface(SwingApp app)
    {
        this(app, app.getPageFormat(), new Dimension(700, 500));
    }
    //    public PreviewReportPanelNew(PageFormat pageFormat) {
    //        this(pageFormat, new Dimension(700,500));
    //    }
    //
    //    public PreviewReportPanelNew(PageFormat pageFormat, Dimension dimPreferred) {
    //        this(null, pageFormat, dimPreferred);
    //    }
    public PreviewVirtualDrawSurface(SwingApp app, PageFormat pageFormat, Dimension dimPreferred)
    {
        super();
        this.app = app;
        setPreferredSize(dimPreferred);
        setBorder(new EmptyBorder(0, 0, 0, 0));
        //this.report = new EmptyReport();
        assignPageFormat(pageFormat);

        iCurrentPage = 1;
        jScrollPane = new JScrollPane(previewPanel);
        jScrollPane.getVerticalScrollBar().setUnitIncrement(10);
        addToGrid(jScrollPane, 0, 0, GridBagConstraints.REMAINDER, 1, 1.0, 1.0, GridBagConstraints.BOTH);
        //init();


        //- átemelve az eredetibõl:
        spinnerNumberModelRate = new SpinnerNumberModel(100, 1, 200, 1);
        spinnerNumberModelRate.addChangeListener(new ChangeListener()
                                         {

                                             public void stateChanged(ChangeEvent e)
                                             {
                                                 //int i = 0;
                                                 dPaperSizeRatio = ((Number) ((SpinnerNumberModel) e.getSource()).getNumber()).doubleValue() / 100.0;
                                                 iPanelWidth = (int) (iWidth * dPaperSizeRatio);
                                                 iPanelHeight = (int) (iHeight * dPaperSizeRatio);
                                                 printablePageAssignPageFormat(printablePage);
                                                 printablePage.setPaperSizeRatio(dPaperSizeRatio);
                                                 //                previewPanel.setMinimumSize(new Dimension(iPanelWidth, iPanelHeight));
                                                 //                previewPanel.setPreferredSize(new Dimension(iPanelWidth, iPanelHeight));
                                                 previewPanel.setFixSize(new Dimension(iPanelWidth, iPanelHeight));
                                                 previewPanel.revalidate();
                                             }
                                         });
        jSpinnerRate = new JSpinner(spinnerNumberModelRate);

        spinnerNumberModelPage = new SpinnerNumberModel(iCurrentPage, 1, iPageCount, 1);
        spinnerNumberModelPage.addChangeListener(this);
        jSpinnerPage = new JSpinner(spinnerNumberModelPage);

        addToGrid(jRadioButtonPortrait = AppUtils.createRadioButton("Álló", "Portrait", this), 1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NONE);
        addToGrid(jRadioButtonLandscape = AppUtils.createRadioButton("Fekvõ", "Landscape", this), 1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NONE);
        addToGrid(new JLabel(" Nagyítás(%):"), 1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NONE);
        addToGrid(jSpinnerRate, 1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NONE);
        addToGrid(new JLabel(" Oldal:"), 1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.NONE);
        addToGrid(jSpinnerPage, 1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.NONE);
        addToGrid(jLabelPageCount = new JLabel("/1   "), 1, 6, 1, 1, 0.0, 0.0, GridBagConstraints.NONE);
        addToGrid(jButtonPrint = AppUtils.createButton("Nyomtatás", "Print", this), 1, 7, 1, 1, 0.0, 0.0, GridBagConstraints.NONE);
        addToGrid(jButtonPageSetup = AppUtils.createButton("Oldalbeállítás", "PageSetup", this), 1, 8, 1, 1, 0.0, 0.0, GridBagConstraints.NONE);
        addToGrid(new JLabel(""), 1, 9, GridBagConstraints.REMAINDER, 1, 0.0, 1.0, GridBagConstraints.BOTH);

        ButtonGroup group = new ButtonGroup();
        group.add(jRadioButtonPortrait);
        group.add(jRadioButtonLandscape);

        if (pageFormat.getOrientation() == PageFormat.PORTRAIT)
        {
            jRadioButtonPortrait.setSelected(true);
        }
        if (pageFormat.getOrientation() == PageFormat.LANDSCAPE)
        {
            jRadioButtonLandscape.setSelected(true);
        }
        draw();
    }

    public void setFullPage(boolean bFullPage)
    {
        this.bFullPage = bFullPage;
    }

    private void printablePageAssignPageFormat(PrintablePage printablePage)
    {
        printablePage.assignPageFormat(pageFormat);
    }
//    public void draw(Report report){
//        this.report = report;
//        draw();
//        //        jScrollPane.setViewportView(previewPanel);
//    }
    public void draw(VirtualDrawSurface virtualDrawSurface)
    {
        this.virtualDrawSurface = virtualDrawSurface;
    }

    public void draw()
    {
        virtualDrawSurface = new VirtualDrawSurface((int) pageFormat.getImageableWidth(), (int) pageFormat.getImageableHeight(), new Font("Arial", Font.ITALIC, 10));
        //report.drawToVirtualSurface(virtualDrawSurface);
        iPageCount = virtualDrawSurface.getPageCount();
        jLabelPageCount.setText("/" + Integer.toString(iPageCount) + "   ");
        //iCurrentPage = 1;
        if (iCurrentPage < 1)
        {
            iCurrentPage = 1;
        }
        if (iCurrentPage > iPageCount)
        {
            iCurrentPage = iPageCount;
        }
        spinnerNumberModelPage.setValue(new java.lang.Integer(iCurrentPage));
        spinnerNumberModelPage.setMinimum(new java.lang.Integer(1));
        spinnerNumberModelPage.setMaximum(new java.lang.Integer(iPageCount));
        spinnerNumberModelPage.setStepSize(new java.lang.Integer(1));
        jSpinnerPage.setModel(spinnerNumberModelPage);
        drawPage(iCurrentPage - 1);
        revalidate();
    }

    private void drawPage(int iPage)
    {
        printablePage = new PrintablePage(pageFormat, virtualDrawSurface, iPage);
        previewPanel = new CommonPanel();
        printablePage.setPaperSizeRatio(dPaperSizeRatio);
        previewPanel.addToGrid(printablePage, 0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NONE, GridBagConstraints.CENTER);
        previewPanel.setFixSize(new Dimension(iPanelWidth, iPanelHeight));
        //iScrollPosition = jScrollPane.getVerticalScrollBar().getValue();
        jScrollPane.setViewportView(previewPanel);
    //jScrollPane.getVerticalScrollBar().setValue(iScrollPosition);
    //jScrollPane.getViewport().revalidate();
    }

    public void assignPageFormat(PageFormat pageFormat)
    {
        this.pageFormat = pageFormat;
        iWidth = (int) pageFormat.getWidth();
        iHeight = (int) pageFormat.getHeight();
        iPanelWidth = (int) (iWidth * dPaperSizeRatio);
        iPanelHeight = (int) (iHeight * dPaperSizeRatio);
        //System.err.println(iWidth);
        //System.err.println(iHeight);
        if (jRadioButtonPortrait != null)
        {
            if (pageFormat.getOrientation() == PageFormat.PORTRAIT)
            {
                jRadioButtonPortrait.setSelected(true);
            }
        }
        if (jRadioButtonLandscape != null)
        {
            if (pageFormat.getOrientation() == PageFormat.LANDSCAPE)
            {
                jRadioButtonLandscape.setSelected(true);
            }
        }
        if (jRadioButtonLandscape != null)
        {
            if (pageFormat.getOrientation() == PageFormat.REVERSE_LANDSCAPE)
            {
                jRadioButtonLandscape.setSelected(true);
            }
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand().equals("Portrait"))
        {
            pageFormat.setOrientation(PageFormat.PORTRAIT);
            if (app != null)
            {
                app.setPageFormat(pageFormat);
            }
            assignPageFormat(pageFormat);
            draw();
        //previewPanel.repaint();
        }
        if (e.getActionCommand().equals("Landscape"))
        {
            pageFormat.setOrientation(PageFormat.LANDSCAPE);
            if (app != null)
            {
                app.setPageFormat(pageFormat);
            }
            assignPageFormat(pageFormat);
            draw();
        //previewPanel.repaint();
        }
        /*if (e.getActionCommand().equals("Print")) {
        //régen ez volt: PrintUtilities.printComponent(printablePages, pageFormat,dPrintSizeRatio);
        printReport();
        }*/
        if (e.getActionCommand().equals("PageSetup"))
        {
            if (app != null)
            {
                pageFormat = app.pageSetup();
            }
            assignPageFormat(pageFormat);
            printablePageAssignPageFormat(printablePage);
            previewPanel.revalidate();
            draw();
        }
    }
    //meg kell valósítani VDS-hez is!
    /*private void printReport(){
    //System.err.println("printReport");
    PrinterDialog printerDialog = new PrinterDialog();
    ReportUtils reportUtils = new ReportUtils(app);
    //System.err.println(printerDialog.showDialog(this, "Nyomtató kiválasztás", ""));
    
    PrinterJob printerJob = PrinterJob.getPrinterJob();
    //printerJob.pageDialog(createA4PageFormat());
    //printerJob.defaultPage(createA4PageFormat());
    printerJob.defaultPage(pageFormat);
    PrintService oneservice;
    DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
    PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
    if (pageFormat.getOrientation()==PageFormat.PORTRAIT) aset.add(OrientationRequested.PORTRAIT);
    if (pageFormat.getOrientation()==PageFormat.LANDSCAPE) aset.add(OrientationRequested.LANDSCAPE);
    if (pageFormat.getOrientation()==PageFormat.REVERSE_LANDSCAPE) aset.add(OrientationRequested.REVERSE_LANDSCAPE);
    aset.add(new Copies(1));
    aset.add(new JobName(app.getLanguageString(DefaultLanguageConstants.STRING_CAPTION_LIST), null));
    aset.add(MediaSizeName.ISO_A4);
    //aset.add(new MediaPrintableArea(30, 30, 100, 100, MediaPrintableArea.MM));
    //aset.add(new MediaPrintableArea(0, 0, 595, 841, MediaPrintableArea.MM));
    //aset.add(new MediaPrintableArea(36, 36, 523, 769, MediaPrintableArea.MM));
    
    if (pageFormat.getOrientation()==PageFormat.PORTRAIT) aset.add(new MediaPrintableArea(new Double(pageFormat.getImageableX() / 72.0).floatValue(), new Double(pageFormat.getImageableY() / 72.0).floatValue(), new Double(pageFormat.getImageableWidth() / 72.0).floatValue(), new Double(pageFormat.getImageableHeight() / 72.0).floatValue(), MediaPrintableArea.INCH));
    if (pageFormat.getOrientation()==PageFormat.LANDSCAPE) aset.add(new MediaPrintableArea(new Double(pageFormat.getImageableY() / 72.0).floatValue(), new Double(pageFormat.getImageableX() / 72.0).floatValue(), new Double(pageFormat.getImageableHeight() / 72.0).floatValue(), new Double(pageFormat.getImageableWidth() / 72.0).floatValue(), MediaPrintableArea.INCH));
    if (pageFormat.getOrientation()==PageFormat.REVERSE_LANDSCAPE) aset.add(new MediaPrintableArea(new Double(pageFormat.getImageableY() / 72.0).floatValue(), new Double(pageFormat.getImageableX() / 72.0).floatValue(), new Double(pageFormat.getImageableHeight() / 72.0).floatValue(), new Double(pageFormat.getImageableWidth() / 72.0).floatValue(), MediaPrintableArea.INCH));
    //aset.add(new MediaPrintableArea(pageFormat.getImageableX() / 72, pageFormat.getImageableY() / 72, pageFormat.getImageableWidth() / 72, pageFormat.getImageableHeight() / 72, MediaPrintableArea.INCH));
    //aset.add(new MediaPrintableArea(10, 10, 190, 277, MediaPrintableArea.MM));
    
    if (printerJob.printDialog(aset)) {
    oneservice = printerJob.getPrintService();
    //testService(oneservice);
    reportUtils.printReport3(report, oneservice, flavor, aset, bFullPage);
    }
    }*/
    public void stateChanged(ChangeEvent e)
    {
        if (e.getSource() != null)
        {
            if (e.getSource() == spinnerNumberModelPage)
            {
                iCurrentPage = ((SpinnerNumberModel) e.getSource()).getNumber().intValue();
                drawPage(iCurrentPage - 1);
            }
        }
    }
}
