package hu.mag.pdf;

import com.itextpdf.text.TabStop;
import java.util.ArrayList;

/**
 *
 * @author MaG
 */
public class PdfTabList {

    private java.util.List<TabStop> tabStops = new ArrayList<TabStop>();
    private java.util.List<PdfTabStop> pdfTabStops = new ArrayList<PdfTabStop>();

    public PdfTabList() {
    }

    public void add(PdfTabStop pdfTabStop) {
        tabStops.add(pdfTabStop.getTabStop());
        pdfTabStops.add(pdfTabStop);
    }

    public java.util.List<TabStop> getTabStopList() {
        return (tabStops);
    }

    public java.util.List<PdfTabStop> getPdfTabStopList() {
        return (pdfTabStops);
    }

}
