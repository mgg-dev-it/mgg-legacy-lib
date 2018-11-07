package hu.mag.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.TabSettings;
import com.itextpdf.text.TabStop;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import hu.mgx.app.common.ErrorHandlerInterface;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author MaG
 */
public class Pdf {
    //http://developers.itextpdf.com

    private ErrorHandlerInterface errorHandlerInterface = null;
    private ByteArrayOutputStream baos = null;
    private Document document = null;
    private Paragraph paragraph = null;
    private PdfPTable pdfPTable = null;
    private PdfWriter writer = null;
    private HashMap<String, PdfBaseFont> pdfBaseFontMap = null;
    private HashMap<String, PdfFont> pdfFontMap = null;
    private HashMap<String, PdfTabList> pdfTabListMap = null;
    private Font font;
    private PdfEvent pdfEvent;
    private int iTotalPageNumber;
    private List<PdfPageEventHelper> pdfPageEventHelperList;

    public Pdf(ErrorHandlerInterface errorHandlerInterface) {
        this.errorHandlerInterface = errorHandlerInterface;
        init();
    }

    public void addPdfPageEventHelper(PdfPageEventHelper ppeh) {
        pdfPageEventHelperList.forEach(item -> {
            if (item.equals(ppeh)) {
                return;
            }
        });
        pdfPageEventHelperList.add(ppeh);
    }

    private void init() {
        paragraph = null;
        pdfBaseFontMap = new HashMap<>();
        pdfFontMap = new HashMap<>();
        pdfTabListMap = new HashMap<>();
        pdfPageEventHelperList = new ArrayList<>();
        //addBaseFont("defaultBaseFont", BaseFont.TIMES_ROMAN, "ISO-8859-2", true);
        addBaseFont("defaultBaseFont", "hu/mag/pdf/fonts/times.ttf", "ISO-8859-2", true);
        addBaseFont("defaultBaseFontBold", "hu/mag/pdf/fonts/timesbd.ttf", "ISO-8859-2", true);
        addBaseFont("defaultBaseFontItalic", "hu/mag/pdf/fonts/timesi.ttf", "ISO-8859-2", true);
        addBaseFont("defaultBaseFontBoldItalic", "hu/mag/pdf/fonts/timesbi.ttf", "ISO-8859-2", true);

        addBaseFont("timesBaseFont", "hu/mag/pdf/fonts/times.ttf", "ISO-8859-2", true);
        addBaseFont("timesBaseFontBold", "hu/mag/pdf/fonts/timesbd.ttf", "ISO-8859-2", true);
        addBaseFont("timesBaseFontItalic", "hu/mag/pdf/fonts/timesi.ttf", "ISO-8859-2", true);
        addBaseFont("timesBaseFontBoldItalic", "hu/mag/pdf/fonts/timesbi.ttf", "ISO-8859-2", true);

        addBaseFont("arialBaseFont", "hu/mag/pdf/fonts/arial.ttf", "ISO-8859-2", true);
        addBaseFont("arialBaseFontBold", "hu/mag/pdf/fonts/arialbd.ttf", "ISO-8859-2", true);
        addBaseFont("arialBaseFontItalic", "hu/mag/pdf/fonts/ariali.ttf", "ISO-8859-2", true);
        addBaseFont("arialBaseFontBoldItalic", "hu/mag/pdf/fonts/arialbi.ttf", "ISO-8859-2", true);

        //addBaseFont("defaultBaseFont", BaseFont.TIMES_ROMAN, "UTF-8", true);
        iTotalPageNumber = 0;
    }

    public void open() {
        open(PageSize.A4, 36, 36, 36, 36);
    }

    public void open(Rectangle pageSize, float marginLeft, float marginRight, float marginTop, float marginBottom) {
        baos = new ByteArrayOutputStream();
        document = null;
        writer = null;
        document = new Document(pageSize, marginLeft, marginRight, marginTop, marginBottom);
        //document = new Document(PageSize.A4, 0, 0, 0, 0);
        try {
            writer = PdfWriter.getInstance(document, baos);
        } catch (DocumentException de) {
            errorHandlerInterface.handleError(de);
        }
        writer.setStrictImageSequence(true);
        pdfEvent = new PdfEvent();
        writer.setPageEvent(pdfEvent);
        document.open();
    }

    public void fonttest() {
        try {
            //BaseFont bfTimes = BaseFont.createFont("c:/windows/fonts/times.ttf", "ISO-8859-2", BaseFont.EMBEDDED);
            BaseFont bfTimes = BaseFont.createFont("hu/mag/pdf/fonts/times.ttf", "ISO-8859-2", BaseFont.EMBEDDED);
            //BaseFont bf = BaseFont.createFont(BaseFont.TIMES_ROMAN, "ISO-8859-2", BaseFont.EMBEDDED);
            //Font fontTitle = new Font(bf, 20, Font.BOLD, BaseColor.BLUE);
            Font fontTitle = new Font(bfTimes, 20, Font.NORMAL, BaseColor.BLUE);
            Font fontTitle2 = new Font(bfTimes, 14, Font.NORMAL, BaseColor.BLACK);
            document.add(new Paragraph("aáeéiíoóöőuúüű", fontTitle));
            document.add(new Paragraph("AÁEÉIÍOÓÖŐUÚÜŰ", fontTitle));
            document.add(new Paragraph("BÉRLETI SZERZŐDÉS", fontTitle2));
        } catch (java.io.IOException ioe) {
            errorHandlerInterface.handleError(ioe);
        } catch (DocumentException de) {
            errorHandlerInterface.handleError(de);
        }
    }

    public void addBaseFont(String sName, String sFontName, String sEncoding, boolean bEmbedded) {
        addBaseFont(sName, new PdfBaseFont(errorHandlerInterface, sFontName, sEncoding, bEmbedded));
    }

    public void addBaseFont(String sName, PdfBaseFont pdfBaseFont) {
        if (pdfBaseFontMap.containsKey(sName)) {
            pdfBaseFontMap.remove(sName);
        }
        pdfBaseFontMap.put(sName, pdfBaseFont);
    }

    public void addFont(String sName, String sFontName, String sEncoding, boolean bEmbedded, float fSize, int style, BaseColor color) {
        //if (pdfFontMap.containsKey(sName)) {
        addFont(sName, new PdfFont(errorHandlerInterface, sFontName, sEncoding, bEmbedded, fSize, style, color));
        //}
    }

    public void addFont(String sName, String sBaseFontName, float fSize, int iStyle, BaseColor color) {
        if (pdfBaseFontMap.containsKey(sBaseFontName)) {
            addFont(sName, new PdfFont(errorHandlerInterface, pdfBaseFontMap.get(sBaseFontName), fSize, iStyle, color));
        }
    }

    public void addFont(String sName, PdfFont pdfFont) {
        if (pdfFontMap.containsKey(sName)) {
            pdfFontMap.remove(sName);
        }
        pdfFontMap.put(sName, pdfFont);
    }

    public void createTabList(String sName) {
        if (pdfTabListMap.containsKey(sName)) {
            pdfTabListMap.remove(sName);
        }
        pdfTabListMap.put(sName, new PdfTabList());
    }

    public void addTabStopToTabList(String sTabListName, float position, TabStop.Alignment alignment) {
        if (!pdfTabListMap.containsKey(sTabListName)) {
            pdfTabListMap.put(sTabListName, new PdfTabList());
        }
        pdfTabListMap.get(sTabListName).add(new PdfTabStop(position, alignment));
    }

    public void addChunk(String sContent) {
        if (paragraph == null) {
            paragraph = new Paragraph();
        }
        paragraph.add(new Chunk(sContent, font));
    }

    public void addChunk(String sContent, String sFontName) {
        if (paragraph == null) {
            paragraph = new Paragraph();
        }
        this.setFont(sFontName);
        paragraph.add(new Chunk(sContent, font));
    }

    public void addTabChunk() {
        if (paragraph == null) {
            paragraph = new Paragraph();
        }
        paragraph.add(Chunk.TABBING);
    }

//    public void addPhrase(String sContent, float fLeading) {
//        if (paragraph == null) {
//            paragraph = new Paragraph();
//        }
//        Phrase phrase = new Phrase(sContent);
//        phrase.setLeading(fLeading);
//        paragraph.add(phrase);
//    }
    public void addParagraphIntoParagraph(Paragraph p) {
        if (paragraph == null) {
            paragraph = new Paragraph();
        }
        paragraph.add(p);
    }

//    public void addParagraphInParagraph(String sContent, float fIndentFirst, float fIndentLeft, float fIndentRight) {
//        if (paragraph == null) {
//            paragraph = new Paragraph();
//        }
//        Paragraph p2 = new Paragraph();
//        p2.add(new Chunk(sContent, font));
//        p2.setFirstLineIndent(fIndentFirst);
//        p2.setIndentationLeft(fIndentLeft);
//        p2.setIndentationRight(fIndentRight);
//        paragraph.add(p2);
//    }
    public void addParagraph(String sText) {
        if (paragraph != null) {
            try {
                document.add(paragraph);
            } catch (DocumentException de) {
                errorHandlerInterface.handleError(de);
            }
        }
        paragraph = new Paragraph(sText, font);
    }

    public void addParagraph(String sText, String sFontName) {
        this.setFont(sFontName);
        addParagraph(sText);
    }

    public void addParagraph() {
        addParagraph("");
    }

    public void setParagraphIndentationLeft(float indentation) {
        paragraph.setIndentationLeft(indentation);
    }

    public void setParagraphIndentationRight(float indentation) {
        paragraph.setIndentationRight(indentation);
    }

    public void setParagraphIndentationFirst(float indentation) {
        paragraph.setFirstLineIndent(indentation);
    }

    public void setParagraphAlignmentCentered() {
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
    }

    public void setParagraphAlignmentJustified() {
        paragraph.setAlignment(Paragraph.ALIGN_JUSTIFIED);
    }

    public void setParagraphAlignmentRight() {
        paragraph.setAlignment(Paragraph.ALIGN_RIGHT);
    }

    public void setParagraphSpacingAfter(float spacing) {
        paragraph.setSpacingAfter(spacing);
    }

    public void setParagraphMultipliedLeading(float multipliedLeading) {
        paragraph.setMultipliedLeading(multipliedLeading);
    }

    public void setParagraphKeepTogether(boolean keeptogether) {
        paragraph.setKeepTogether(keeptogether);
    }

    public void setParagraphTabList(String sTabListName) {
        if (pdfTabListMap.containsKey(sTabListName)) {
        }
        paragraph.setTabSettings(new TabSettings(pdfTabListMap.get(sTabListName).getTabStopList()));
    }

    public PdfTabList getPdfTabListByName(String sTabListName) {
        return (pdfTabListMap.get(sTabListName));
    }

    public void closeParagraph() {
        if (paragraph != null) {
            try {
                document.add(paragraph);
            } catch (DocumentException de) {
                errorHandlerInterface.handleError(de);
            }
        }
        paragraph = null;
    }

    public Font getFontByName(String sFontName) {
        return (pdfFontMap.get(sFontName).getFont());
    }

    public void setFont(String sFontName) {
        if (pdfFontMap.containsKey(sFontName)) {
            font = pdfFontMap.get(sFontName).getFont();
        }
    }

    public void addTable(int iColumnCount, float fWidthPercentage, float[] faRelativeWidths, float fSpacingBefore, float fSpacingAfter) {
        closeParagraph();
        closeTable();
        pdfPTable = new PdfPTable(iColumnCount);
        pdfPTable.setWidthPercentage(fWidthPercentage);
        pdfPTable.setSpacingBefore(fSpacingBefore);
        pdfPTable.setSpacingAfter(fSpacingAfter);
        try {
            pdfPTable.setWidths(faRelativeWidths);
        } catch (DocumentException de) {
            errorHandlerInterface.handleError(de);
        }
    }

    public void setTableAlignmentLeft() {
        pdfPTable.setHorizontalAlignment(Element.ALIGN_LEFT);
    }

    public void setTableAlignmentCentered() {
        pdfPTable.setHorizontalAlignment(Element.ALIGN_CENTER);
    }

    public void setTableAlignmentRight() {
        pdfPTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
    }

    public void addCellToTable(String sText) {
        if (pdfPTable != null) {
            pdfPTable.addCell(sText);
        }
    }

    public void addBorderlessCellToTable(String sText) {
        if (pdfPTable != null) {
            PdfPCell pdfPCell = new PdfPCell(new Phrase(sText));
            pdfPCell.setBorder(Rectangle.NO_BORDER);
            pdfPTable.addCell(pdfPCell);
        }
    }

    private class CellTitle implements PdfPCellEvent {

        protected String sTitle;
        protected String sFontName;

        public CellTitle(String sTitle, String sFontName) {
            this.sTitle = sTitle;
            this.sFontName = sFontName;
        }

        @Override
        public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
            Font f = getFontByName(sFontName);
            float height = f.getBaseFont().getAscentPoint(sTitle, f.getSize()) - f.getBaseFont().getDescentPoint(sTitle, f.getSize());
            float width = f.getBaseFont().getWidthPoint(sTitle, f.getSize());
            Chunk c = new Chunk(sTitle, getFontByName(sFontName));
            c.setBackground(BaseColor.WHITE);
            PdfContentByte canvas = canvases[PdfPTable.TEXTCANVAS];
            canvas.setColorFill(BaseColor.WHITE);
            canvas.rectangle(position.getLeft(5), position.getTop(3), width, height);
            canvas.fill();
            ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(c), position.getLeft(5), position.getTop(3), 0);
        }
    }

    public void addCellToTable(String sText, String sFontName, String sTitle, int iHorizontalAlignment, int iVerticalAlignment, float fPadding, int iRowSpan, int iColSpan, int iBorder, float fBorderWidth) {
        if (pdfPTable == null) {
            return;
        }
        if (sText == null) {
            sText = "";
        }
        if (sFontName == null) {
            sFontName = "";
        }
        if (sTitle == null) {
            sTitle = "";
        }
        PdfPCell pdfPCell;
        if (sText.equalsIgnoreCase("") && sFontName.equalsIgnoreCase("")) {
            pdfPCell = new PdfPCell(new Phrase());
        } else {
            pdfPCell = new PdfPCell(new Phrase(sText, getFontByName(sFontName)));
        }
        if (!sTitle.equalsIgnoreCase("")) {
            pdfPCell.setCellEvent(new CellTitle(sTitle, sFontName));
        }
        if (iHorizontalAlignment != Element.ALIGN_UNDEFINED) {
            pdfPCell.setHorizontalAlignment(iHorizontalAlignment);
        }
        if (iVerticalAlignment != Element.ALIGN_UNDEFINED) {
            pdfPCell.setVerticalAlignment(iVerticalAlignment);
        }
        pdfPCell.setPadding(fPadding);
        if (iRowSpan > 1) {
            pdfPCell.setRowspan(iRowSpan);
        }
        if (iColSpan > 1) {
            pdfPCell.setColspan(iColSpan);
        }
        if (iBorder != Rectangle.UNDEFINED) {
            pdfPCell.setBorder(iBorder);
            pdfPCell.setBorderWidth(fBorderWidth);
        }
        pdfPTable.addCell(pdfPCell);
    }

    public void addCellToTable(String sText, String sFontName, int iHorizontalAlignment, int iVerticalAlignment, float fPadding, int iRowSpan, int iColSpan, int iBorder, float fBorderWidth) {
        if (pdfPTable != null) {
            PdfPCell pdfPCell;
            if (sText.equalsIgnoreCase("") && sFontName.equalsIgnoreCase("")) {
                pdfPCell = new PdfPCell(new Phrase());
            } else {
                pdfPCell = new PdfPCell(new Phrase(sText, getFontByName(sFontName)));
            }
            //PdfPCell pdfPCell = new PdfPCell(new Phrase(sText, getFontByName(sFontName)));
            if (iHorizontalAlignment != Element.ALIGN_UNDEFINED) {
                pdfPCell.setHorizontalAlignment(iHorizontalAlignment);
            }
            if (iVerticalAlignment != Element.ALIGN_UNDEFINED) {
                pdfPCell.setVerticalAlignment(iVerticalAlignment);
            }
            pdfPCell.setPadding(fPadding);
            if (iRowSpan > 1) {
                pdfPCell.setRowspan(iRowSpan);
            }
            if (iColSpan > 1) {
                pdfPCell.setColspan(iColSpan);
            }
            if (iBorder != Rectangle.UNDEFINED) {
                pdfPCell.setBorder(iBorder);
                pdfPCell.setBorderWidth(fBorderWidth);
            }
            pdfPTable.addCell(pdfPCell);
        }
    }

    public void completeTableRow() {
        if (pdfPTable != null) {
            pdfPTable.completeRow();
        }
    }

    public void setTableHeaderRows(int iHeaderRows) {
        pdfPTable.setHeaderRows(iHeaderRows);
    }

    public void keepTableRowTogether() {
        int iLastRow = pdfPTable.getRows().size() - 1;
        pdfPTable.keepRowsTogether(iLastRow, iLastRow);
        pdfPTable.setKeepTogether(true);
    }

    public void keepTableLastRowsTogether(int iNumberOfRows) {
        if (iNumberOfRows < 1) {
            return;
        }
        int iLastRow = pdfPTable.getRows().size() - 1;
        if ((iNumberOfRows - 1) > iLastRow) {
            return;
        }
        pdfPTable.keepRowsTogether(iLastRow - (iNumberOfRows - 1), iLastRow);
    }

    public void keepTableRowTogether(int iRow) {
        pdfPTable.keepRowsTogether(iRow, iRow);
    }

    public void keepTableRowsTogether(int iStartRow, int iEndRow) {
        pdfPTable.keepRowsTogether(iStartRow, iEndRow);
    }

    public void setTableSpacingBefore(float spacing) {
        if (pdfPTable != null) {
            pdfPTable.setSpacingBefore(spacing);
        }
    }

    public void setTableSpacingAfter(float spacing) {
        if (pdfPTable != null) {
            pdfPTable.setSpacingAfter(spacing);
        }
    }

    public void closeTable() {
        if (pdfPTable != null) {
            this.completeTableRow();
            try {
                document.add(pdfPTable);
            } catch (DocumentException de) {
                errorHandlerInterface.handleError(de);
            }
        }
        pdfPTable = null;
    }

    public void addLine() {
        //Element element;
        //element=new Element();
//        Rectangle rectangle = new Rectangle(0, 0, 10, 20);
//        rectangle.setBorder(Rectangle.BOX);
//        rectangle.setBorderWidth(1);
//        try {
//            document.add(rectangle);
//        } catch (DocumentException de) {
//            errorHandlerInterface.handleError(de);
//        }
        this.addTable(1, 100f, new float[]{1}, 0, 0);
        addCellToTable("", "", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, 1, 1, Rectangle.TOP, 0.5f);
        closeTable();
    }

    public float getVerticalPosition() {
        return (writer.getVerticalPosition(false));
    }

    public Rectangle getPageSize() {
        return (document.getPageSize());
    }

    public float getLeftMargin() {
        return (document.leftMargin());
    }

    public float getTopMargin() {
        return (document.topMargin());
    }

    public float getRightMargin() {
        return (document.rightMargin());
    }

    public float getBottomMargin() {
        return (document.bottomMargin());
    }

    public void close() {
        if (paragraph != null) {
            try {
                document.add(paragraph);
            } catch (DocumentException de) {
                errorHandlerInterface.handleError(de);
            }
        }
        document.close();
    }

    public byte[] getContent() {
        if (document.isOpen()) {
            close();
        }
        return (baos.toByteArray());
    }

    public int getTotalPageNumber() {
        return (iTotalPageNumber);
    }

    class PdfEvent extends PdfPageEventHelper {

        Font ffont = new Font(Font.FontFamily.UNDEFINED, 8, Font.ITALIC);

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            ++iTotalPageNumber;
//            PdfContentByte cb = writer.getDirectContent();
//            Phrase header = new Phrase("this is a header", ffont);
//            Phrase footer = new Phrase("this is a footer", ffont);
//            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, header, (document.right() - document.left()) / 2 + document.leftMargin(), document.top() + 10, 0);
//            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, footer, (document.right() - document.left()) / 2 + document.leftMargin(), document.bottom() - 10, 0);
            pdfPageEventHelperList.forEach(eventHelper -> {
                eventHelper.onEndPage(writer, document);
            });
        }
    }

    public void newPage() {
        this.addParagraph();
        document.newPage();
    }

    public float getTextWidth(String sText, Font font) {
        return (font.getCalculatedBaseFont(true).getWidthPoint(sText, font.getCalculatedSize()));
    }

    public void addImage(Image img) {
        try {
            document.add(img);
        } catch (DocumentException de) {
            errorHandlerInterface.handleError(de);
        }
    }

    public PdfContentByte getDirectContent() {
        return (writer.getDirectContent());
    }
}
