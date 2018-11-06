/*
 * TestPrint.java
 *
 * Created on 2004. december 6., 13:30
 */
package hu.mgx.draw;

/**
 *
 * @author  gmagyar
 */
import java.awt.*;
import java.awt.print.*;

import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;

public class TestPrint implements Printable
{

    VirtualDrawSurface vds;

    /** Creates a new instance of TestPrint */
    public TestPrint()
    {
        init();
    }

    private void init()
    {
        PageFormat pf = new PageFormat();
        int iX = (int) (pf.getImageableX());
        int iY = (int) (pf.getImageableY());
        int iWidth = (int) (pf.getImageableWidth());
        int iHeight = (int) (pf.getImageableHeight());
        //        System.err.println("---");
        //        System.err.println(iWidth); //468
        //        System.err.println(iHeight); //648
        vds = new VirtualDrawSurface((int) new PageFormat().getImageableWidth(), (int) new PageFormat().getImageableHeight(), new Font("Arial", Font.ITALIC, 10));
        //vds.drawRectangle(1, 1, iWidth-100, iHeight-100);
        vds.drawLine(0, 15, iWidth, 15);
        vds.drawString("fejléc", 0, 0, iWidth, 10, DrawAlign.CENTER, DrawAlign.TOP, false);

        vds.drawLine(0, iHeight - 15, iWidth, iHeight - 15);
        vds.drawString("lábléc", 0, iHeight - 10, iWidth, 10, DrawAlign.CENTER, DrawAlign.BOTTOM, false);
        //vds.drawRectangle(0, 0, iWidth, iHeight);
        //vds.drawString("drawRectangle", 0, 0, iWidth, iHeight);
        vds.drawString("center", 10, 100, 100, 50, DrawAlign.CENTER, DrawAlign.MIDDLE, true);
        vds.drawString("left", 10, 150, 100, 50, DrawAlign.LEFT, DrawAlign.MIDDLE, true);
        vds.drawString("right", 10, 200, 100, 50, DrawAlign.RIGHT, DrawAlign.MIDDLE, true);
        vds.drawString("top", 10, 250, 100, 50, DrawAlign.CENTER, DrawAlign.TOP, true);
        vds.drawString("bottom", 10, 300, 100, 50, DrawAlign.CENTER, DrawAlign.BOTTOM, true);
        vds.addPage();
        vds.drawString("2. oldal!", 10, 100, 100, 50, DrawAlign.CENTER, DrawAlign.MIDDLE, true);
        //vds.drawString("example string", new Font("Arial", Font.ITALIC, 8), 0, 50, 100, 12);

        DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        //aset.add(OrientationRequested.LANDSCAPE);
        aset.add(new Copies(1));
        aset.add(new JobName("My job", null));
        //aset.add(MediaSizeName.ISO_A4);
        PrintService[] services =
                PrintServiceLookup.lookupPrintServices(flavor, aset);
        for (int i = 0; i < services.length; i++)
        {
            System.out.println("printer " + services[i].getName());
        }
        if (services.length > 1)
        {
            System.out.println("selected printer " + services[2].getName());
            DocPrintJob pj = services[2].createPrintJob();
            try
            {
                Doc doc = new SimpleDoc(this, flavor, null);
                pj.print(doc, aset);
            }
            catch (PrintException e)
            {
                System.err.println(e);
            }
        }

    }

    public int print(java.awt.Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException
    {

        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        if (vds != null)
        {
            if (vds.drawToGraphics(pageIndex, g2d))
            {
                return Printable.PAGE_EXISTS;
            }
        }
        return Printable.NO_SUCH_PAGE;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        TestPrint testPrint = new TestPrint();
    }
}
