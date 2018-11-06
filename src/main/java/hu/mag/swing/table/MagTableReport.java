package hu.mag.swing.table;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import hu.mag.pdf.report.PdfReport;
import hu.mgx.app.swing.SwingAppInterface;

/**
 *
 * @author MaG
 */
public class MagTableReport extends PdfReport {
    
    protected String sHeader;
    protected String sFooter;
    
    public MagTableReport(SwingAppInterface swingAppInterface, String[] args) {
        super(swingAppInterface, args);
        sHeader = "";
        sFooter = "";
    }
    
    @Override
    protected void pageEnd(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        Phrase header = new Phrase(sHeader, pdf.getFontByName("header"));
        Phrase footer = new Phrase(sFooter, pdf.getFontByName("footer"));
        Phrase pages = new Phrase(Integer.toString(iCurrentPageNumber) + "/" + Integer.toString(iTotalPageNumber) + ". oldal", pdf.getFontByName("footer"));
        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, header, (document.right() - document.left()) / 2 + document.leftMargin(), document.top() + 10, 0);
        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, footer, (document.right() - document.left()) / 2 + document.leftMargin(), document.bottom() - 10, 0);
        ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT, pages, (document.right() - document.left()) + document.leftMargin(), document.bottom() - 10, 0);
    }
    
    @Override
    protected int createReport(MagTable... mt) {
        if (mt[0].getMagTableModel().getTableInfo() != null) {
            sHeader = mt[0].getMagTableModel().getTableInfo().getDisplayName();
        }
        sFooter = getDateTime();
        
        pdf = new hu.mag.pdf.Pdf(swingAppInterface);
        pdf.addPdfPageEventHelper(new PPEH());
        pdf.open(PageSize.A4, 36, 36, 36, 36);
        pdf.addFont("celltext", "defaultBaseFont", 10, Font.NORMAL, BaseColor.BLACK);
        pdf.addFont("headertext", "defaultBaseFont", 10, Font.BOLD, BaseColor.BLACK);
        pdf.addFont("header", "defaultBaseFont", 12, Font.NORMAL, BaseColor.BLACK);
        pdf.addFont("footer", "defaultBaseFont", 9, Font.NORMAL, BaseColor.BLACK);
        
        float fWidthPercentage = 100f;
        float fPadding = 1f;
        float fBorderWidth = 0.1f;

//        int[] iaColumns = new int[0];
        int iHiddenColumnCount = 0;
        for (int iColumn = 0; iColumn < mt[0].getColumnCount(); iColumn++) {
            if (mt[0].getMagTableModel().isHiddenColumn(iColumn)) {
                ++iHiddenColumnCount;
            }
        }
        //if (iHiddenColumnCount > 0) {
        int[] iaColumns = new int[mt[0].getColumnCount() - iHiddenColumnCount];
        iHiddenColumnCount = 0;
        for (int iColumn = 0; iColumn < mt[0].getColumnCount(); iColumn++) {
            if (mt[0].getMagTableModel().isHiddenColumn(iColumn)) {
                ++iHiddenColumnCount;
            } else {
                iaColumns[iColumn - iHiddenColumnCount] = iColumn;
            }
        }
        //}

        float[] faPdfTableInfo = getPdfTableInfo(mt[0], pdf.getFontByName("celltext"), pdf.getFontByName("headertext"), fWidthPercentage, fPadding, fBorderWidth, iaColumns);
//        for (int i = 0; i < faPdfTableInfo.length; i++) {
//            System.out.println(Integer.toString(i) + " " + Float.toString(faPdfTableInfo[i]));
//        }
        float[] faRelativeWidths = new float[faPdfTableInfo.length - 1];
        for (int i = 0; i < faRelativeWidths.length; i++) {
            faRelativeWidths[i] = faPdfTableInfo[i + 1];
        }
        
        pdf.addFont("celltext_modified", "defaultBaseFont", new Float(faPdfTableInfo[0]).intValue(), Font.NORMAL, BaseColor.BLACK);
        pdf.addFont("headertext_modified", "defaultBaseFont", new Float(faPdfTableInfo[0]).intValue(), Font.BOLD, BaseColor.BLACK);
        
        pdf.addTable(faRelativeWidths.length, fWidthPercentage, faRelativeWidths, 5, 5);
        
        for (int iCol = 0; iCol < iaColumns.length; iCol++) {
            pdf.addCellToTable(mt[0].getColumnName(iaColumns[iCol]), "headertext_modified", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, fPadding, 1, 1, Rectangle.BOX, fBorderWidth);
        }
//        for (int iCol = 0; iCol < mt[0].getColumnCount(); iCol++) {
//            pdf.addCellToTable(mt[0].getColumnName(iCol), "headertext_modified", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, fPadding, 1, 1, Rectangle.BOX, fBorderWidth);
//        }

        for (int iRow = 0; iRow < mt[0].getRowCount(); iRow++) {
            for (int iCol = 0; iCol < iaColumns.length; iCol++) {
                int iAlignment = Element.ALIGN_LEFT;
                if (mt[0].getValueAt(iRow, iaColumns[iCol]) != null) {
                    if ((mt[0].getValueAt(iRow, iaColumns[iCol]).getClass() == java.lang.Integer.class || mt[0].getValueAt(iRow, iaColumns[iCol]).getClass() == java.math.BigDecimal.class) && mt[0].getMagTableModel().getColumnLookupField(iaColumns[iCol]) == null) {
                        iAlignment = Element.ALIGN_RIGHT;
                    }
                }
                pdf.addCellToTable(mt[0].getRenderedStringValue(iRow, iaColumns[iCol]), "celltext_modified", iAlignment, Element.ALIGN_MIDDLE, fPadding, 1, 1, Rectangle.BOX, fBorderWidth);
            }
        }

        if (mt[0].getRowCount() < 1) {
            for (int iCol = 0; iCol < iaColumns.length; iCol++) {
                pdf.addCellToTable(" ", "celltext_modified", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, fPadding, 1, 1, Rectangle.BOX, fBorderWidth);
            }
        }
//        for (int iRow = 0; iRow < mt[0].getRowCount(); iRow++) {
//            for (int iCol = 0; iCol < mt[0].getColumnCount(); iCol++) {
//                int iAlignment = Element.ALIGN_LEFT;
//                if (mt[0].getValueAt(iRow, iCol) != null) {
//                    if ((mt[0].getValueAt(iRow, iCol).getClass() == java.lang.Integer.class || mt[0].getValueAt(iRow, iCol).getClass() == java.math.BigDecimal.class) && mt[0].getMagTableModel().getColumnLookupField(iCol) == null) {
//                        iAlignment = Element.ALIGN_RIGHT;
//                    }
//                }
//                pdf.addCellToTable(mt[0].getRenderedStringValue(iRow, iCol), "celltext_modified", iAlignment, Element.ALIGN_MIDDLE, fPadding, 1, 1, Rectangle.BOX, fBorderWidth);
//            }
//        }
        pdf.setTableHeaderRows(1);
        pdf.closeTable();

        return (pdf.getTotalPageNumber());
    }

//    @Override
//    protected int createReport(MagTable... mt) {
//        if (mt[0].getMagTableModel().getTableInfo() != null) {
//            sHeader = mt[0].getMagTableModel().getTableInfo().getDisplayName();
//        }
//        sFooter = getDateTime();
//
//        pdf = new hu.mag.pdf.Pdf(swingAppInterface);
//        pdf.addPdfPageEventHelper(new PPEH());
//        pdf.open(PageSize.A4, 36, 36, 36, 36);
//        pdf.addFont("text", "defaultBaseFont", 10, Font.NORMAL, BaseColor.BLACK);
//        pdf.addFont("textbold", "defaultBaseFont", 10, Font.BOLD, BaseColor.BLACK);
//        pdf.addFont("header", "defaultBaseFont", 12, Font.NORMAL, BaseColor.BLACK);
//        pdf.addFont("footer", "defaultBaseFont", 9, Font.NORMAL, BaseColor.BLACK);
//
//        float fPadding = 1f;
//        float fBorderWidth = 0.1f;
//
//        Vector<Float> vColumnWidths = getColumnWidths(mt[0], pdf.getFontByName("text"), pdf.getFontByName("textbold"));
//        float f = 0f;
//        for (int i = 0; i < vColumnWidths.size(); i++) {
//            f += vColumnWidths.elementAt(i);
//            //System.out.println(Integer.toString(i) + ": " + Float.toString(vColumnWidths.elementAt(i)));
//        }
//        f += vColumnWidths.size() * (2f * fPadding + 2f * fBorderWidth);
//        //System.out.println(Float.toString(f));
//
//        float fRation = (PageSize.A4.getWidth() - pdf.getLeftMargin() - pdf.getRightMargin()) / f;
//        //System.out.println(Float.toString(PageSize.A4.getWidth() - pdf.getLeftMargin() - pdf.getRightMargin()));
//        //System.out.println(Float.toString(fRation));
//        //System.out.println(Float.toString(12f * fRation));
//        //System.out.println(Integer.toString(new Float(12f * fRation).intValue()));
//
//        //pdf.addFont("text_", "defaultBaseFont", new Float(12f * fRation).intValue(), Font.NORMAL, BaseColor.BLACK);
//        pdf.addFont("text_", "defaultBaseFont", new Float(pdf.getFontByName("text").getSize() * fRation).intValue(), Font.NORMAL, BaseColor.BLACK);
//        pdf.addFont("text_bold", "defaultBaseFont", new Float(pdf.getFontByName("text").getSize() * fRation).intValue(), Font.BOLD, BaseColor.BLACK);
//
//        //pdf.addParagraph("EMPTY REPORT", "text");
//        float[] faRelativeWidths = new float[vColumnWidths.size()];
//        for (int i = 0; i < vColumnWidths.size(); i++) {
//            faRelativeWidths[i] = vColumnWidths.elementAt(i).floatValue();
//        }
//        //faRelativeWidths[0] *= 2f;
//        //faRelativeWidths[faRelativeWidths.length - 1] *= 2f;
//        pdf.addTable(vColumnWidths.size(), 100f, faRelativeWidths, 5, 5);
//        for (int iCol = 0; iCol < mt[0].getColumnCount(); iCol++) {
//            pdf.addCellToTable(mt[0].getColumnName(iCol), "text_bold", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, fPadding, 1, 1, Rectangle.BOX, fBorderWidth);
//        }
//        for (int iRow = 0; iRow < mt[0].getRowCount(); iRow++) {
//            for (int iCol = 0; iCol < mt[0].getColumnCount(); iCol++) {
//                int iAlignment = Element.ALIGN_LEFT;
//                if (mt[0].getValueAt(iRow, iCol).getClass() == java.lang.Integer.class && mt[0].getMagTableModel().getColumnLookupField(iCol) == null) {
//                    iAlignment = Element.ALIGN_RIGHT;
//                }
//                pdf.addCellToTable(mt[0].getRenderedStringValue(iRow, iCol), "text_", iAlignment, Element.ALIGN_MIDDLE, fPadding, 1, 1, Rectangle.BOX, fBorderWidth);
//            }
//        }
//        pdf.setTableHeaderRows(1);
//        pdf.closeTable();
//
//        return (pdf.getTotalPageNumber());
//    }
//
//    private Vector<Float> getColumnWidths(MagTable mt, Font font, Font fontbold) {
//        Vector<Float> vColumnWidths = new Vector<Float>();
//
//        for (int i = 0; i < mt.getColumnCount(); i++) {
//            vColumnWidths.add(new Float(0f));
//        }
//        for (int iColumn = 0; iColumn < mt.getColumnCount(); iColumn++) {
//            String sCellValue = mt.getColumnName(iColumn) + " ";
//            if (pdf.getTextWidth(sCellValue, fontbold) > vColumnWidths.elementAt(iColumn)) {
//                vColumnWidths.setElementAt(new Float(pdf.getTextWidth(sCellValue, fontbold)), iColumn);
//            }
//        }
//        for (int iRow = 0; iRow < mt.getRowCount(); iRow++) {
//            for (int iColumn = 0; iColumn < mt.getColumnCount(); iColumn++) {
//                String sCellValue = mt.getRenderedStringValue(iRow, iColumn) + " ";
//                if (pdf.getTextWidth(sCellValue, font) > vColumnWidths.elementAt(iColumn)) {
//                    vColumnWidths.setElementAt(new Float(pdf.getTextWidth(sCellValue, font)), iColumn);
//                }
//            }
//        }
//        return (vColumnWidths);
//    }
}
