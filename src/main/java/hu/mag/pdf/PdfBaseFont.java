package hu.mag.pdf;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import hu.mgx.app.common.ErrorHandlerInterface;
import java.io.IOException;

/**
 *
 * @author MaG
 */
public class PdfBaseFont {

    private ErrorHandlerInterface errorHandlerInterface;
    private String sFontName;
    private String sEncoding;
    private boolean bEmbedded;
    private BaseFont baseFont;

    public PdfBaseFont(ErrorHandlerInterface errorHandlerInterface) {
        this.errorHandlerInterface = errorHandlerInterface;
        this.sFontName = BaseFont.TIMES_ROMAN;
        this.sEncoding = "ISO-8859-2";
        this.bEmbedded = true;
        createBaseFont();
    }

    public PdfBaseFont(ErrorHandlerInterface errorHandlerInterface, String sFontName, String sEncoding, boolean bEmbedded) {
        this.errorHandlerInterface = errorHandlerInterface;
        this.sFontName = sFontName;
        this.sEncoding = sEncoding;
        this.bEmbedded = bEmbedded;
        createBaseFont();
    }

    private void createBaseFont() {
        try {
            this.baseFont = BaseFont.createFont(sFontName, sEncoding, bEmbedded);
        } catch (DocumentException de) {
            errorHandlerInterface.handleError(de);
        } catch (IOException ioe) {
            errorHandlerInterface.handleError(ioe);
        }
    }

    public BaseFont getBaseFont() {
        return (baseFont);
    }

}
