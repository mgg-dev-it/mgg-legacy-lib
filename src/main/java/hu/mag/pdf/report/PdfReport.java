package hu.mag.pdf.report;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import hu.mag.swing.table.MagTable;
import hu.mgx.app.common.CommonAppUtils;
import hu.mgx.app.swing.SwingAppInterface;
import hu.mgx.swing.table.MemoryTable;
import hu.mgx.util.IntegerUtils;
import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author MaG
 */
public class PdfReport {

    protected SwingAppInterface swingAppInterface;
    protected String[] args;
    protected HashMap<String, String> argsMap;
    protected int iCurrentPageNumber;
    protected int iTotalPageNumber;
    protected hu.mag.pdf.Pdf pdf;

    public PdfReport(SwingAppInterface swingAppInterface, String[] args) {
        this.swingAppInterface = swingAppInterface;
        this.args = args;
        argsMap = CommonAppUtils.preProcessArgs(args);
        iCurrentPageNumber = 0;
        iTotalPageNumber = 0;
    }

    public byte[] getReport(MemoryTable... mt) {
        iCurrentPageNumber = 0;
        iTotalPageNumber = 0;
        int iTPN = createReport(mt);
        if (iTPN > -1) {
            iCurrentPageNumber = 0;
            iTotalPageNumber = iTPN + 1;
            createReport(mt);
        }
        return (pdf.getContent());
    }

    public byte[] getReport(MagTable... mt) {
        iCurrentPageNumber = 0;
        iTotalPageNumber = 0;
        int iTPN = createReport(mt);
        if (iTPN > -1) {
            iCurrentPageNumber = 0;
            iTotalPageNumber = iTPN + 1;
            createReport(mt);
        }
        return (pdf.getContent());
    }

    public class PPEH extends PdfPageEventHelper {

        Font ffont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.ITALIC);

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            if (iTotalPageNumber < 0) {
                return;
            }
            ++iCurrentPageNumber;
            pageEnd(writer, document);
        }
    }

    protected String getDateTime() {
        return (swingAppInterface.getDateTimeFormat().format(new java.util.Date()));
    }

    protected void pageEnd(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        Phrase header = new Phrase("header", pdf.getFontByName("header"));
        Phrase footer = new Phrase("footer", pdf.getFontByName("footer"));
        Phrase pages = new Phrase(Integer.toString(iCurrentPageNumber) + "/" + Integer.toString(iTotalPageNumber) + ". oldal", pdf.getFontByName("footer"));
        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, header, (document.right() - document.left()) / 2 + document.leftMargin(), document.top() + 10, 0);
        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, footer, (document.right() - document.left()) / 2 + document.leftMargin(), document.bottom() - 10, 0);
        ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT, pages, (document.right() - document.left()) + document.leftMargin(), document.bottom() - 10, 0);
    }

    protected int createReport(MemoryTable... mt) {
        pdf = new hu.mag.pdf.Pdf(swingAppInterface);
        pdf.addPdfPageEventHelper(new PPEH());
        pdf.open(PageSize.A4, 36, 36, 36, 36);
        pdf.addFont("text", "defaultBaseFont", 12, Font.NORMAL, BaseColor.BLACK);
        pdf.addFont("header", "defaultBaseFont", 16, Font.NORMAL, BaseColor.BLACK);
        pdf.addFont("footer", "defaultBaseFont", 10, Font.NORMAL, BaseColor.BLACK);
        pdf.addParagraph("EMPTY REPORT", "text");
        return (pdf.getTotalPageNumber());
    }

    protected int createReport(MagTable... mt) {
        pdf = new hu.mag.pdf.Pdf(swingAppInterface);
        pdf.addPdfPageEventHelper(new PPEH());
        pdf.open(PageSize.A4, 36, 36, 36, 36);
        pdf.addFont("text", "defaultBaseFont", 12, Font.NORMAL, BaseColor.BLACK);
        pdf.addFont("header", "defaultBaseFont", 16, Font.NORMAL, BaseColor.BLACK);
        pdf.addFont("footer", "defaultBaseFont", 10, Font.NORMAL, BaseColor.BLACK);
        pdf.addParagraph("EMPTY REPORT", "text");
        return (pdf.getTotalPageNumber());
    }

    protected float[] getPdfTableInfo(MagTable mt, Font font, Font fontHeader, float fWidthPercentage, float fPadding, float fBorderWidth) {
        return (getPdfTableInfo(mt, font, fontHeader, fWidthPercentage, fPadding, fBorderWidth, new int[]{}));
    }

    protected float[] getPdfTableInfo(MagTable mt, Font font, Font fontHeader, float fWidthPercentage, float fPadding, float fBorderWidth, int[] iaColumns) {
        Vector<Float> vColumnWidths = new Vector<Float>();

//        if (iaColumns.length == 0) {
//            int iHiddenColumnCount = 0;
//            for (int iColumn = 0; iColumn < mt.getColumnCount(); iColumn++) {
//                if (mt.getMagTableModel().isHiddenColumn(iColumn)) {
//                    ++iHiddenColumnCount;
//                }
//            }
//            if (iHiddenColumnCount > 0) {
//                iaColumns = new int[mt.getColumnCount() - iHiddenColumnCount];
//                iHiddenColumnCount = 0;
//                for (int iColumn = 0; iColumn < mt.getColumnCount(); iColumn++) {
//                    if (mt.getMagTableModel().isHiddenColumn(iColumn)) {
//                        ++iHiddenColumnCount;
//                    } else {
//                        iaColumns[iColumn - iHiddenColumnCount] = iColumn;
//                    }
//                }
//            }
//        }
//        for (int i = 0; i < iaColumns.length; i++) {
//            System.out.println(Integer.toString(i)+" "+Integer.toString(iaColumns[i]));
//        }
        for (int i = 0; i < mt.getColumnCount(); i++) {
            vColumnWidths.add(new Float(0f));
        }
        for (int iColumn = 0; iColumn < mt.getColumnCount(); iColumn++) {
//            if (!mt.getMagTableModel().isHiddenColumn(iColumn)) {
            if (iaColumns.length == 0 || IntegerUtils.contains(iaColumns, iColumn)) {
                String sCellValue = mt.getColumnName(iColumn) + " ";
                if (pdf.getTextWidth(sCellValue, fontHeader) > vColumnWidths.elementAt(iColumn)) {
                    vColumnWidths.setElementAt(new Float(pdf.getTextWidth(sCellValue, fontHeader)), iColumn);
                }
            }
//            }
        }
        for (int iRow = 0; iRow < mt.getRowCount(); iRow++) {
            for (int iColumn = 0; iColumn < mt.getColumnCount(); iColumn++) {
//                if (!mt.getMagTableModel().isHiddenColumn(iColumn)) {
                if (iaColumns.length == 0 || IntegerUtils.contains(iaColumns, iColumn)) {
                    String sCellValue = mt.getRenderedStringValue(iRow, iColumn).trim() + " ";
                    if (pdf.getTextWidth(sCellValue, font) > vColumnWidths.elementAt(iColumn)) {
                        vColumnWidths.setElementAt(new Float(pdf.getTextWidth(sCellValue, font)), iColumn);
                    }
                }
//                }
            }
        }
        float f = 0f;
        for (int i = 0; i < vColumnWidths.size(); i++) {
            f += vColumnWidths.elementAt(i);
            //System.out.println(Integer.toString(i) + ": " + Float.toString(vColumnWidths.elementAt(i)));
        }
        f += vColumnWidths.size() * (2f * fPadding + 2f * fBorderWidth);
        //System.out.println(Float.toString(f));

        //float fRation = (PageSize.A4.getWidth() - pdf.getLeftMargin() - pdf.getRightMargin()) / f;
        float fRation = fWidthPercentage / 100f * (pdf.getPageSize().getWidth() - pdf.getLeftMargin() - pdf.getRightMargin()) / f;
        //float fRation = (pdf.getPageSize().getWidth() - pdf.getLeftMargin() - pdf.getRightMargin()) / f;
        //System.out.println(Float.toString(fWidthPercentage));
        //System.out.println(Float.toString(fWidthPercentage / 100f));

        //System.out.println(Float.toString(PageSize.A4.getWidth() - pdf.getLeftMargin() - pdf.getRightMargin()));
        //System.out.println(Float.toString(fRation));
        //System.out.println(Float.toString(12f * fRation));
        //System.out.println(Integer.toString(new Float(12f * fRation).intValue()));
        //pdf.addFont("text_", "defaultBaseFont", new Float(12f * fRation).intValue(), Font.NORMAL, BaseColor.BLACK);
        //pdf.addFont("text_", "defaultBaseFont", new Float(pdf.getFontByName("text").getSize() * fRation).intValue(), Font.NORMAL, BaseColor.BLACK);
        //pdf.addFont("text_bold", "defaultBaseFont", new Float(pdf.getFontByName("text").getSize() * fRation).intValue(), Font.BOLD, BaseColor.BLACK);
        //pdf.addParagraph("EMPTY REPORT", "text");
        float[] faRelativeWidths = new float[iaColumns.length == 0 ? vColumnWidths.size() + 1 : iaColumns.length + 1];
        //faRelativeWidths[0] = pdf.getFontByName("text").getSize() * fRation;
        faRelativeWidths[0] = font.getSize() * fRation;
        for (int i = 0; i < (iaColumns.length == 0 ? vColumnWidths.size() : iaColumns.length); i++) {
            //faRelativeWidths[i + 1] = vColumnWidths.elementAt(i).floatValue();
            faRelativeWidths[i + 1] = vColumnWidths.elementAt(iaColumns.length == 0 ? i : iaColumns[i]).floatValue();
        }
        return (faRelativeWidths);
    }

    protected boolean isRightAlignedClass(Class c) {
        if (c == java.lang.Short.class) {
            return (true);
        }
        if (c == java.lang.Integer.class) {
            return (true);
        }
        if (c == java.lang.Long.class) {
            return (true);
        }
        if (c == java.lang.Double.class) {
            return (true);
        }
        if (c == java.lang.Float.class) {
            return (true);
        }
        if (c == java.math.BigDecimal.class) {
            return (true);
        }
        return (false);
    }

    protected int getAlignment(Object oValue) {
        int iAlignment = Element.ALIGN_LEFT;
        if (oValue != null) {
            if (isRightAlignedClass(oValue.getClass())) {
                iAlignment = Element.ALIGN_RIGHT;
            }
        }
        return (iAlignment);
    }
}
