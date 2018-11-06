package hu.mag.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import hu.mgx.app.common.ErrorHandlerInterface;

/**
 *
 * @author MaG
 */
public class PdfFont {

    private ErrorHandlerInterface errorHandlerInterface;
    private PdfBaseFont pdfBaseFont;
    private float fSize;
    private int iStyle;
    private BaseColor color;
    private Font font;
    private String sFontName;
    private String sEncoding;
    private boolean bEmbedded;

    public PdfFont(ErrorHandlerInterface errorHandlerInterface) {
        this.errorHandlerInterface = errorHandlerInterface;
        this.pdfBaseFont = new PdfBaseFont(errorHandlerInterface);
        this.fSize = 12f;
        this.iStyle = Font.NORMAL;
        this.color = BaseColor.BLACK;
        createFont();
    }

    public PdfFont(ErrorHandlerInterface errorHandlerInterface, PdfBaseFont pdfBaseFont, float fSize, int iStyle, BaseColor color) {
        this.errorHandlerInterface = errorHandlerInterface;
        this.pdfBaseFont = pdfBaseFont;
        this.fSize = fSize;
        this.iStyle = iStyle;
        this.color = color;
        createFont();
    }

    public PdfFont(ErrorHandlerInterface errorHandlerInterface, String sFontName, String sEncoding, boolean bEmbedded, float fSize, int style, BaseColor color) {
        this.errorHandlerInterface = errorHandlerInterface;
        this.pdfBaseFont = null;
        this.sFontName = sFontName;
        this.sEncoding = sEncoding;
        this.bEmbedded = bEmbedded;
        this.fSize = fSize;
        this.iStyle = iStyle;
        this.color = color;
        font = FontFactory.getFont(sFontName, sEncoding, bEmbedded, fSize, iStyle, color);
    }

    private void createFont() {
        this.font = new Font(pdfBaseFont.getBaseFont(), fSize, iStyle, color);
    }

    public Font getFont() {
        return (font);
    }
}
