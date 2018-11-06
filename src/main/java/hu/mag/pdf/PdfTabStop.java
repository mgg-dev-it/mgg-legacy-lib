package hu.mag.pdf;

import com.itextpdf.text.TabStop;

/**
 *
 * @author MaG
 */
public class PdfTabStop {

    private float position = 36f;
    private TabStop.Alignment alignment = TabStop.Alignment.LEFT;

    public PdfTabStop() {
        this.position = 36f;
        this.alignment = TabStop.Alignment.LEFT;
    }

    public PdfTabStop(float position, TabStop.Alignment alignment) {
        this.position = position;
        this.alignment = alignment;
    }

    public TabStop getTabStop() {
        return (new TabStop(position, alignment));
    }

}
