package hu.mgx.report;

import java.awt.*;
import java.awt.print.*;
import java.util.*;

import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;
import javax.swing.table.*;

import hu.mgx.app.common.*;
import hu.mgx.db.*;
import hu.mgx.draw.*;

public class ReportUtils implements Printable
{

    private AppInterface appInterface = null;
    private VirtualDrawSurface vds;
    private Report report;
    private boolean bFullPage = false;

    public ReportUtils(AppInterface appInterface)
    {
        this.appInterface = appInterface;
    }
    //    private PageFormat createOKPageFormat(){
    //        PageFormat pageFormat = new PageFormat();
    //        Paper paper = new Paper();
    //        paper.setSize(595, 841);
    //        //paper.setImageableArea(20, 20, 555, 801);
    //        paper.setImageableArea(72, 72, 451, 697);
    //        pageFormat.setPaper(paper);
    //        return(pageFormat);
    //    }
    //
    //    public boolean aaa(){
    //        sun.awt.windows.WPrinterJob wPrinterJob = new sun.awt.windows.WPrinterJob();
    //        //        try {
    //        //            return (PrinterJob)Class.forName(nm).newInstance();
    //        //        } catch (ClassNotFoundException e) {
    //        //            throw new AWTError("PrinterJob not found: " + nm);
    //        //        } catch (InstantiationException e) {
    //        //            throw new AWTError("Could not instantiate PrinterJob: " + nm);
    //        //        } catch (IllegalAccessException e) {
    //        //            throw new AWTError("Could not access PrinterJob: " + nm);
    //        //        }
    //        return(wPrinterJob.pageSetup(createOKPageFormat(), null));
    //    }
    public void printReport(Report report, PageFormat pageFormat)
    {
        this.report = report;
        DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        //aset.add(OrientationRequested.LANDSCAPE);
        aset.add(new Copies(1));
        aset.add(new JobName("My job", null));
        /////aset.add(MediaSizeName.ISO_A4);
        PrintService[] services =
                PrintServiceLookup.lookupPrintServices(flavor, aset);
        for (int i = 0; i < services.length; i++)
        {
            System.out.println("printer " + services[i].getName());
        }
        if (services.length > 1)
        {

            // Virtuális rajzfelület létrehozása
            vds = new VirtualDrawSurface((int) pageFormat.getImageableWidth(), (int) pageFormat.getImageableHeight(), new Font("Arial", Font.ITALIC, 10));

            // Riport rajzolása a virtuális rajzfelületre:
            report.drawToVirtualSurface(vds);

            printt(services[2], flavor, aset);
        //            System.out.println("selected printer " + services[2].getName());
        //            DocPrintJob pj = services[2].createPrintJob();
        //            try {
        //                Doc doc = new SimpleDoc(this, flavor, null);
        //                pj.print(doc, aset);
        //            } catch (PrintException e) {
        //                System.err.println(e);
        //            }
        }

    }

    public void printReport3(Report report, PrintService service, DocFlavor flavor, PrintRequestAttributeSet aset, boolean bFullPage)
    {
        this.report = report;
        this.bFullPage = bFullPage;
        DocPrintJob docPrintJob = service.createPrintJob();
        try
        {
            Doc doc = new SimpleDoc(this, flavor, null);
            docPrintJob.print(doc, aset);
        }
        catch (PrintException e)
        {
            //System.err.println(e);
            if (appInterface != null)
            {
                appInterface.handleError(e, 1);
            }
        }
    }

    public void printReport2(Report report, PageFormat pageFormat, PrintService service)
    {
        this.report = report;
        //Class[] c = service.getSupportedAttributeCategories();
        DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        //aset.add(OrientationRequested.LANDSCAPE);
        aset.add(new Copies(1));
        aset.add(new JobName("My job", null));
        /////aset.add(MediaSizeName.ISO_A4);
        if (service != null)
        {

            // Virtuális rajzfelület létrehozása
            vds = new VirtualDrawSurface((int) pageFormat.getImageableWidth(), (int) pageFormat.getImageableHeight(), new Font("Arial", Font.ITALIC, 10));

            // Riport rajzolása a virtuális rajzfelületre:
            report.drawToVirtualSurface(vds);

            printt(service, flavor, aset);
        //            System.out.println("selected printer " + services[2].getName());
        //            DocPrintJob pj = services[2].createPrintJob();
        //            try {
        //                Doc doc = new SimpleDoc(this, flavor, null);
        //                pj.print(doc, aset);
        //            } catch (PrintException e) {
        //                System.err.println(e);
        //            }
        }

    }

    private void printt(PrintService printService, DocFlavor docFlavor, PrintRequestAttributeSet printRequestAttributeSet)
    {
        DocPrintJob docPrintJob = printService.createPrintJob();
        try
        {
            Doc doc = new SimpleDoc(this, docFlavor, null);
            docPrintJob.print(doc, printRequestAttributeSet);
        }
        catch (PrintException e)
        {
            System.err.println(e); //@todo apperrorhandler-t bekötni!!!
        }
    }

    /**
     * A Printable interfész által meghatározott visszahívandó metódus, amelyik "kinyomtatja" a VirtualDrawSurface lapjainak tartalmát a megadott Grgaphics-ra.
     * @return Printable.PAGE_EXISTS, ha VirtualDrawSurface true-val tért vissza (rajzolt a grafikus objektumra), illetve Printable.NO_SUCH_PAGE, ha nem
     */
    public int print(java.awt.Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException
    {

        //System.err.println(Double.toString(pageFormat.getWidth()) + "  " + Double.toString(pageFormat.getHeight()) + "  " + Double.toString(pageFormat.getImageableWidth()) + "  " + Double.toString(pageFormat.getImageableHeight()) + "  " + Double.toString(pageFormat.getImageableX()) + "  " + Double.toString(pageFormat.getImageableY()) + " " + (bFullPage ? "full page" :"no full page"));
        //if (1 == 1) return (Printable.NO_SUCH_PAGE);

        // Virtuális rajzfelület létrehozása
        if (!bFullPage)
        {
            vds = new VirtualDrawSurface((int) pageFormat.getImageableWidth(), (int) pageFormat.getImageableHeight(), new Font("Arial", Font.ITALIC, 10));
        }
        else
        {
            vds = new VirtualDrawSurface((int) pageFormat.getWidth(), (int) pageFormat.getHeight(), new Font("Arial", Font.ITALIC, 10));
        }

        // Riport rajzolása a virtuális rajzfelületre:
        report.drawToVirtualSurface(vds);

        Graphics2D g2d = (Graphics2D) graphics;
        if (!bFullPage)
        {
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        //g2d.drawRect(0, 0,  (int)pageFormat.getImageableWidth(), (int)pageFormat.getImageableHeight());
        }
        if (vds != null)
        {
            // Virtuális rajzfelület tartalmának grafikus objektumra rajzolása
            if (vds.drawToGraphics(pageIndex, g2d))
            {
                return (Printable.PAGE_EXISTS);
            }
        }
        return (Printable.NO_SUCH_PAGE);
    }
    //    public Vector calculateColumnWidths_(DefaultTableModel defaultTableModel, TableDefinition tableDefinition, MgxFormat mgxFormat){
    //        Vector v = new Vector();
    //        Object o;
    //        String sValue;
    //        for (int i=0; i<defaultTableModel.getColumnCount(); i++){
    //            v.add(new Integer(0));
    //        }
    //        for (int r=0; r<defaultTableModel.getRowCount(); r++){
    //            for (int i=0; i<defaultTableModel.getColumnCount(); i++){
    //                o = defaultTableModel.getValueAt(r, i);
    //                sValue = (o == null ? "" : o.toString());
    //                //System.err.println(sValue);
    //                v.add(new Integer(0));
    //            }
    //        }
    //        return(v);
    //    }
    public Vector calculateColumnWidths(DefaultTableModel defaultTableModel, TableDefinition tableDefinition, FormatInterface mgxFormat, Font font, Vector vColumnNames)
    {
        //Font font = new Font("Monospaced", Font.PLAIN, 10);
        //Font font = new Font("Arial", Font.PLAIN, 10);
        FontMetrics fm = new javax.swing.JPanel().getFontMetrics(font);
        //fm.stringWidth("");

        //Font f = new Font("Times", 0, 10);
        //FontMetrics fm = f.getFontMetrics
        //FontMetrics fm = new FontMetrics(f);

        Vector v = new Vector();
        double d;
        java.util.Date utilDate;
        String sValue = "";
        String sCellValue = "";
        int iRow;
        short shColumn;
        short shOffset = 0;
        try
        {
            for (shColumn = 0; shColumn < defaultTableModel.getColumnCount(); shColumn++)
            {
                //System.err.println(shColumn);
                if (tableDefinition.getFieldDefinition(shColumn).isID())
                {
                    ++shOffset;
                }
                else
                {
                    v.add(new Integer(10));
                    sCellValue = (vColumnNames.elementAt(shColumn) != null ? vColumnNames.elementAt(shColumn).toString() : "");
                    if (fm.stringWidth(sCellValue) > ((Integer) v.elementAt(shColumn - shOffset)).intValue())
                    {
                        v.setElementAt(new Integer(fm.stringWidth(sCellValue)), shColumn - shOffset);
                    }
                }
            }
            for (iRow = 0; iRow < defaultTableModel.getRowCount(); iRow++)
            {
                shOffset = 0;
                for (shColumn = 0; shColumn < defaultTableModel.getColumnCount(); shColumn++)
                {
                    if (tableDefinition.getFieldDefinition(shColumn).isID())
                    {
                        ++shOffset;
                    }
                    else
                    {
                        Object o = defaultTableModel.getValueAt(iRow, shColumn);
                        sValue = (o == null ? "" : o.toString());
                        //System.err.println(sValue);
                        if (tableDefinition.getFieldDefinition(shColumn).isLookup())
                        {
                            sValue = tableDefinition.getLookupDisplayFromValue(tableDefinition.getFieldDefinition(shColumn).getLookup(), sValue);
                            sCellValue = sValue;
                        }
                        else
                        {
                            if (tableDefinition.getFieldDefinition(shColumn).getType() == hu.mgx.db.FieldType.DECIMAL || tableDefinition.getFieldDefinition(shColumn).getType() == hu.mgx.db.FieldType.INT)
                            {
                                if (sValue.equals(""))
                                {
                                    sCellValue = "";
                                }
                                else
                                {
                                    try
                                    {
                                        d = Double.parseDouble(sValue);
                                    }
                                    catch (NumberFormatException e)
                                    {
                                        d = 0.0;
                                    }
                                    //sCellValue=d;
                                    sCellValue = sValue;
                                }
                            }
                            else if (tableDefinition.getFieldDefinition(shColumn).getType() == hu.mgx.db.FieldType.DATE)
                            {
                                if (o == null)
                                {
                                    sCellValue = "";
                                }
                                else
                                {
                                    if (o.getClass().getName().startsWith("[B"))
                                    {
                                        String sTmp = new String((byte[]) o);
                                        sTmp += " 00:00:00";
                                        try
                                        {
                                            o = new java.util.Date(mgxFormat.getSQLDateTimeFormat().parse(sTmp).getTime());
                                            sValue = mgxFormat.getDateFormat().format(o);
                                        }
                                        catch (java.text.ParseException pe)
                                        {
                                            o = null;
                                        }
                                        if (sValue.equals("1999/01/01"))
                                        {
                                            sValue = "Nem aktu�lis";
                                        }
                                        else if (sValue.equals("1999/01/02"))
                                        {
                                            sValue = "Folyamatban";
                                        }
                                        else if (sValue.equals("1999/01/03"))
                                        {
                                            sValue = "Megl�togatni";
                                        }
                                        else if (sValue.equals("1999/01/04"))
                                        {
                                            sValue = "Besz�ll�t�";
                                        }
                                    }
                                    else
                                    {
                                        sValue = mgxFormat.getDateFormat().format(o);
                                    }
                                    //sValue = mgxFormat.getDateFormat().format(o);
                                    sCellValue = sValue;
                                }
                            }
                            else if (tableDefinition.getFieldDefinition(shColumn).getType() == hu.mgx.db.FieldType.DATETIME)
                            {
                                if (o == null)
                                {
                                    sCellValue = "";
                                }
                                else
                                {
                                    sValue = mgxFormat.getDateTimeFormat().format(o);
                                    sCellValue = sValue;
                                }
                            }
                            else if (tableDefinition.getFieldDefinition(shColumn).getType() == hu.mgx.db.FieldType.BIT)
                            {
                                if (o == null)
                                {
                                    sCellValue = "";
                                }
                                else
                                {
                                    if (o.toString().equals("1") || o.toString().equals("true"))
                                    {
                                        sCellValue = "+";
                                    }
                                    else
                                    {
                                        sCellValue = "";
                                    }
                                }
                            }
                            //                            else if (tableDefinition.getFieldDefinition(shColumn).getType() == hu.mgx.db.FieldType.CHAR) {
                            //                                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                            //                                cell.setCellValue(sValue);
                            //                            }
                            else
                            {
                                sCellValue = sValue;
                            }
                        }
                        //                        if (sCellValue.length() > ((Integer)v.elementAt(shColumn-shOffset)).intValue()){
                        //                            v.setElementAt(new Integer(sCellValue.length()), shColumn-shOffset);
                        //                        }
                        if (fm.stringWidth(sCellValue) > ((Integer) v.elementAt(shColumn - shOffset)).intValue())
                        {
                            v.setElementAt(new Integer(fm.stringWidth(sCellValue)), shColumn - shOffset);
                        }
                    }
                }
            }
        }
        catch (TableDefinitionException e)
        {
        }
        return (v);
    }
}
