package hu.mgx.util;

import java.awt.*;
import java.awt.geom.*;
import java.awt.print.*;
import javax.swing.*;
import hu.mgx.app.swing.*;

/**
 * A simple utility class that lets you very simply print an arbitrary
 * component. Just pass the component to the PrintUtilities.printComponent. The
 * component you want to print doesn't need a print method and doesn't have to
 * implement any interface or do anything special at all.
 * <P>
 * If you are going to be printing many times, it is marginally more efficient
 * to first do the following:
 * 
 * <PRE>
 * PrintUtilities printHelper = new PrintUtilities(theComponent);
 * </PRE>
 * 
 * then later do printHelper.print(). But this is a very tiny difference, so in
 * most cases just do the simpler
 * PrintUtilities.printComponent(componentToBePrinted).
 *
 * 7/99 Marty Hall, http://www.apl.jhu.edu/~hall/java/ May be freely used or
 * adapted.
 */

public class PrintUtilities implements Printable {
	private Component componentToBePrinted;
	private PageFormat f;
	private double dPrintSizeRatio = 1.0;
	private PrintablePages printablePages;

	public static void printComponent(Component c) {
		new PrintUtilities(c).print();
	}

	public static void printComponent(Component c, PageFormat f) {
		new PrintUtilities(c, f).print();
	}

	public static void printComponent(Component c, PageFormat f, double r) {
		new PrintUtilities(c, f, r).print();
	}

	public static void printComponent(PrintablePages pps, PageFormat f, double r) {
		new PrintUtilities(pps, f, r).print();
	}

	public PrintUtilities(Component componentToBePrinted) {
		this.componentToBePrinted = componentToBePrinted;
	}

	public PrintUtilities(Component componentToBePrinted, PageFormat f) {
		this.componentToBePrinted = componentToBePrinted;
		this.f = f;
	}

	public PrintUtilities(Component componentToBePrinted, PageFormat f, double r) {
		this.componentToBePrinted = componentToBePrinted;
		this.f = f;
		this.dPrintSizeRatio = r;
	}

	public PrintUtilities(PrintablePages pps, PageFormat f, double r) {
		this.printablePages = pps;
		this.componentToBePrinted = null;
		this.f = f;
		this.dPrintSizeRatio = r;
	}

	public void print() {
		PrinterJob printJob = PrinterJob.getPrinterJob();
		printJob.setPrintable(this, f);
		if (printJob.printDialog())
			try {
				printJob.print();
			} catch (PrinterException pe) {
				System.out.println("Error printing: " + pe);
			}
	}

	public int print(Graphics g, PageFormat pageFormat, int pageIndex) {
		Graphics2D g2d = (Graphics2D) g;
//////        GraphicsConfiguration gc = g2d.getDeviceConfiguration();
//////        //AffineTransform at = gc.getNormalizingTransform();
//////        AffineTransform at = gc.getDefaultTransform();
//////        double[] flatmatrix = {0.0,0.0,0.0,0.0,0.0,0.0};
//////        at.getMatrix(flatmatrix);
//////        System.err.println(flatmatrix[0]);
//////        System.err.println(flatmatrix[1]);
//////        System.err.println(flatmatrix[2]);
//////        System.err.println(flatmatrix[3]);
//////        System.err.println(flatmatrix[4]);
//////        System.err.println(flatmatrix[5]);
//////        //if (1==1) return(NO_SUCH_PAGE);
//////
//////        g2d.transform(new AffineTransform(2.0,0,0,2.0,0,0));

		// System.err.println(pageIndex);
		if (componentToBePrinted != null) {
			if (pageIndex > 0)
				return (NO_SUCH_PAGE);
		}
		if (printablePages != null) {
			if (pageIndex >= printablePages.getPageCount())
				return (NO_SUCH_PAGE);
		}
		// g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
		// g2d.transform(new AffineTransform(dPrintSizeRatio,0,0,dPrintSizeRatio,0,0));
		if (componentToBePrinted != null) {
			g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
			g2d.transform(new AffineTransform(dPrintSizeRatio, 0, 0, dPrintSizeRatio, 0, 0));
			disableDoubleBuffering(componentToBePrinted);
			componentToBePrinted.paint(g2d);
			enableDoubleBuffering(componentToBePrinted);
		}
		if (printablePages != null) {
			// g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
			g2d.transform(new AffineTransform(dPrintSizeRatio, 0, 0, dPrintSizeRatio, 0, 0));
			disableDoubleBuffering(printablePages.getPrintablePage(pageIndex));
			printablePages.getPrintablePage(pageIndex).paint(g2d);
			enableDoubleBuffering(printablePages.getPrintablePage(pageIndex));
		}
		return (PAGE_EXISTS);
	}

	/**
	 * The speed and quality of printing suffers dramatically if any of the
	 * containers have double buffering turned on. So this turns if off globally.
	 * 
	 * @param c c
	 * @see enableDoubleBuffering
	 */
	public static void disableDoubleBuffering(Component c) {
		RepaintManager currentManager = RepaintManager.currentManager(c);
		currentManager.setDoubleBufferingEnabled(false);
	}

	/**
	 * Re-enables double buffering globally.
	 * 
	 * @param c c
	 */

	public static void enableDoubleBuffering(Component c) {
		RepaintManager currentManager = RepaintManager.currentManager(c);
		currentManager.setDoubleBufferingEnabled(true);
	}
}
