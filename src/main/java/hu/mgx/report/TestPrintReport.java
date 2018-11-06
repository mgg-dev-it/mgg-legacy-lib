package hu.mgx.report;

import java.awt.*;
import java.awt.print.*;

import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;

import hu.mgx.draw.*;

public class TestPrintReport implements Printable
{

    private VirtualDrawSurface vds;
    //Report report;
    //private PageFormat pageFormat;

    public TestPrintReport(Report report, PageFormat pageFormat)
    {
        //this.report = report;
        init(report, pageFormat);
    }

    /**
     * Printert v�laszt, v�grehajtja ("megrajzolja") a riportot �s nyomtat.
     */
    private void init(Report report, PageFormat pageFormat)
    {

        DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        //aset.add(OrientationRequested.LANDSCAPE);
        aset.add(new Copies(1));
        aset.add(new JobName("My job", null));
        //aset.add(MediaSizeName.ISO_A4);
        PrintService[] services = PrintServiceLookup.lookupPrintServices(flavor, aset);
        for (int i = 0; i < services.length; i++)
        {
            System.out.println("printer " + services[i].getName());
        }
        if (services.length > 1)
        {

            // Virtu�lis rajzfel�let l�trehoz�sa
            vds = new VirtualDrawSurface((int) pageFormat.getImageableWidth(), (int) pageFormat.getImageableHeight(), new Font("Arial", Font.ITALIC, 10));

            // Riport rajzol�sa a virtu�lis rajzfel�letre:
            report.drawToVirtualSurface(vds);

            printt(services[0], flavor, aset);
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
            System.err.println(e); //@todo apperrorhandler-t bek�tni!!!
        }
    }

    /**
     * A Printable interf�sz �ltal meghat�rozott visszah�vand� met�dus, amelyik "kinyomtatja" a VirtualDrawSurface lapjainak tartalm�t a megadott Grgaphics-ra.
     * @return Printable.PAGE_EXISTS, ha VirtualDrawSurface true-val t�rt vissza (rajzolt a grafikus objektumra), illetve Printable.NO_SUCH_PAGE, ha nem
     */
    public int print(java.awt.Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException
    {
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        if (vds != null)
        {
            // Virtu�lis rajzfel�let tartalm�nak grafikus objektumra rajzol�sa
            if (vds.drawToGraphics(pageIndex, g2d))
            {
                return (Printable.PAGE_EXISTS);
            }
        }
        return (Printable.NO_SUCH_PAGE);
    }
}
