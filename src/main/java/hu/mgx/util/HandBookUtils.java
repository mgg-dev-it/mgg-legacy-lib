package hu.mgx.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import hu.mgx.app.common.AppInterface;
import hu.mgx.app.common.CommonAppUtils;
import hu.mgx.swing.table.MemoryTable;
import hu.mgx.xml.DefaultXMLHandler;
import hu.mgx.xml.XMLAttribute;
import hu.mgx.xml.XMLElement;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author gmagyar
 */
public class HandBookUtils {

    //@todo task: appInterface.logLine legyen LOG_DEBUG szintû
    private AppInterface appInterface = null;
//    private String sXMLInputFile = "";
    private String sOutputTextFile = "";
    private String sOutputWikiFile = "";
    private String sOutputPdfFile = "";
    private String sOutputHtmlFile = "";
    DefaultXMLHandler defaultXMLHandler = null;
    //
    HashMap<String, Image> hmImages = null;
    private StringBuffer sbXMLInput = null;
    private StringBuffer sbOutputText = null;
    private StringBuffer sbOutputWiki = null;
    private StringBuffer sbOutputPdf = null;
    private StringBuffer sbOutputHtml = null;

    public HandBookUtils(AppInterface appInterface) {
        this.appInterface = appInterface;
        hmImages = new HashMap<>();
    }

    private void init(String[] args) {
        HashMap<String, String> argsMap = CommonAppUtils.preProcessArgs(args);

//        sXMLInputFile = CommonAppUtils.getParameterValue(argsMap, "-xmlinput");
        sOutputTextFile = CommonAppUtils.getParameterValue(argsMap, "-textoutput");
        sOutputWikiFile = CommonAppUtils.getParameterValue(argsMap, "-wikioutput");
        sOutputPdfFile = CommonAppUtils.getParameterValue(argsMap, "-pdfoutput");
        sOutputHtmlFile = CommonAppUtils.getParameterValue(argsMap, "-htmloutput");

//        appInterface.logLine("xmlinput = " + sXMLInputFile);
        appInterface.logLine("textoutput = " + sOutputTextFile);
        appInterface.logLine("wikioutput = " + sOutputWikiFile);
        appInterface.logLine("pdfoutput = " + sOutputPdfFile);
        appInterface.logLine("htmloutput = " + sOutputHtmlFile);

//        if (sXMLInputFile.equals("")) {
//            appInterface.logLine("Hiba: file argumentum hiányzik!");
//            return;
//        }
        defaultXMLHandler = new DefaultXMLHandler(appInterface);
        //???run();
    }

    private Paragraph createParagraph() {
        Paragraph paragraph = null;
        paragraph = new Paragraph();
        paragraph.setAlignment(Paragraph.ALIGN_JUSTIFIED);
        paragraph.setFirstLineIndent(18);
        paragraph.setSpacingAfter(10); //@todo lehetne paraméter!
        return (paragraph);
    }

    private Phrase createPhrase(String s, boolean bBold) {
        Phrase phrase = null;
        Font font = null;
        try {
            BaseFont bf = BaseFont.createFont(BaseFont.TIMES_ROMAN, "ISO-8859-2", BaseFont.EMBEDDED);
            font = new Font(bf, 12);
            if (bBold) {
                font.setStyle(Font.BOLD);
            }
            phrase = new Phrase(s, font);
        } catch (IOException | DocumentException e) {
            appInterface.handleError(e);
        }
        return (phrase);
    }

    //@todo task: createImage from hashmap (database)
    private Image createImage(String sFileName) {
        Image image = null;

        try {
            image = Image.getInstance(sFileName);
            if (image.getHeight() > 520 || image.getWidth() > 520) {
                image.scaleToFit(520, 520); //@todo: lekérdezni a lehetséges maximumot a lap adataiból...
            }
            //image.scaleToFit(520, 520); //@todo: lekérdezni a lehetséges maximumot a lap adataiból...
            image.setAlignment(Image.ALIGN_CENTER);
        } catch (IOException | DocumentException e) {
            appInterface.handleError(e);
        }
        return (image);
    }

    private String searchChapterPrefixByID(MemoryTable mt, String sID) {
//        consoleAppInterface.logLine("searchChapterPrefixByID(" + sID + ")");
        for (int i = 0; i < mt.getRowCount(); i++) {
            if (mt.getValueAt(i, "id").toString().equals(sID)) {
                return (mt.getValueAt(i, "prefix").toString());
            }
        }
        return ("");
    }

    //private void processElements(XMLElement xmlElementRoot, FileOutputStream fosOutText, FileOutputStream fosOutWiki, FileOutputStream fosOutHtml, FileOutputStream fosOutPdf, Document document, Paragraph paragraph, MemoryTable mtChapters, boolean bTopLevel) throws IOException, DocumentException {
    private void processElements(XMLElement xmlElementRoot, FileOutputStream fosOutText, FileOutputStream fosOutWiki, FileOutputStream fosOutHtml, ByteArrayOutputStream baosPdf, Document document, Paragraph paragraph, MemoryTable mtChapters, boolean bTopLevel) throws IOException, DocumentException {
        XMLElement currentElement = null;
        Paragraph p = null;
        Paragraph p2 = null;
        int iLevel = 0;
        String sTitle = "";
        String sPrefix = "";

        if (bTopLevel) {
            if (baosPdf != null) {
                p2 = new Paragraph("Tartalomjegyzék", new Font(BaseFont.createFont(BaseFont.TIMES_ROMAN, "ISO-8859-2", BaseFont.EMBEDDED), 20 - iLevel * 2));
                document.add(p2);
            }
            if (fosOutHtml != null) {
                fosOutHtml.write(new String("<h1>Tartalomjegyzék</h1>").getBytes());
                fosOutHtml.write(StringUtils.sCrLf.getBytes());
            }
            for (int i = 0; i < mtChapters.getRowCount(); i++) {
                sTitle = mtChapters.getValueAt(i, "title").toString();
                sPrefix = mtChapters.getValueAt(i, "prefix").toString();
                iLevel = StringUtils.intValue(mtChapters.getValueAt(i, "level").toString());
                if (fosOutText != null) {
                    fosOutText.write(new String(sPrefix + " " + sTitle).getBytes());
                    fosOutText.write(StringUtils.sCrLf.getBytes());
                }
//                if (fosOutWiki != null) {
//                    fosOutWiki.write(new String(sPrefix + " " + sTitle).getBytes());
//                    fosOutWiki.write(StringUtils.sCrLf.getBytes());
//                }
                if (baosPdf != null) {
                    p2 = new Paragraph(sPrefix + " " + sTitle, new Font(BaseFont.createFont(BaseFont.TIMES_ROMAN, "ISO-8859-2", BaseFont.EMBEDDED), 20 - iLevel * 2));
//                    p2.setAlignment(Paragraph.ALIGN_JUSTIFIED);
                    p2.setFirstLineIndent(iLevel * 6);
                    //p2.setSpacingAfter(30 - iLevel * 3);
                    document.add(p2);
                }
                if (fosOutHtml != null) {
                    fosOutHtml.write(new String("<h" + Integer.toString(iLevel) + ">" + StringUtils.trimCrLf(sPrefix + " " + sTitle) + "</h" + Integer.toString(iLevel) + ">").getBytes());
                    fosOutHtml.write(StringUtils.sCrLf.getBytes());
                }
            }
            if (baosPdf != null) {
                document.newPage();
            }
        }

        Vector<XMLElement> vElements = defaultXMLHandler.getElements(xmlElementRoot, null, null, null, false);
        for (int i = 0; i < vElements.size(); i++) {
            currentElement = vElements.get(i);
            if (currentElement != xmlElementRoot) {
                appInterface.logLine("Element name=" + currentElement.getName());
                //if (currentElement.getName().equals("toc")) {
                //}
                if (currentElement.getName().equals("chapter")) {
                    appInterface.logLine("Element title=" + currentElement.getAttribute("title"));
                    iLevel = StringUtils.intValue(currentElement.getAttribute("level"));
                    appInterface.logLine("Level=" + Integer.toString(iLevel));
                    sTitle = searchChapterPrefixByID(mtChapters, currentElement.getAttribute("id")) + " " + currentElement.getAttribute("title");
                    sTitle = sTitle.trim();
                    appInterface.logLine("Title=" + sTitle);
                    if (fosOutText != null) {
                        fosOutText.write(sTitle.getBytes());
                        fosOutText.write(StringUtils.sCrLf.getBytes());
                    }
                    if (fosOutWiki != null) {
                        fosOutWiki.write(new String(StringUtils.repeat("=", iLevel) + "=" + currentElement.getAttribute("title") + "=" + StringUtils.repeat("=", iLevel)).getBytes());
                        fosOutWiki.write(StringUtils.sCrLf.getBytes());
                    }
                    if (baosPdf != null) {
                        if (currentElement.hasAttribute("newpage")) {
                            document.newPage();
                        }
                        p2 = new Paragraph(sTitle, new Font(BaseFont.createFont(BaseFont.TIMES_ROMAN, "ISO-8859-2", BaseFont.EMBEDDED), 20 - iLevel * 2));
                        p2.setAlignment(Paragraph.ALIGN_JUSTIFIED);
                        p2.setFirstLineIndent(iLevel * 6);
                        //p2.setSpacingAfter(30 - iLevel * 3);
                        document.add(p2);
                    }
                    if (fosOutHtml != null) {
                        fosOutHtml.write(new String("<h" + Integer.toString(iLevel) + ">" + StringUtils.trimCrLf(sTitle) + "</h" + Integer.toString(iLevel) + ">").getBytes());
                        fosOutHtml.write(StringUtils.sCrLf.getBytes());
                    }
                    processElements(currentElement, fosOutText, fosOutWiki, fosOutHtml, baosPdf, document, null, mtChapters, false);
                }
                if (currentElement.getName().equals("paragraph")) {
                    if (fosOutWiki != null) {
                        fosOutWiki.write(StringUtils.sCrLf.getBytes());
                    }
                    if (baosPdf != null) {
                        p = createParagraph();
                    }
                    if (fosOutHtml != null) {
                        fosOutHtml.write(new String("<p>").getBytes());
                        fosOutHtml.write(StringUtils.sCrLf.getBytes());
                    }
                    processElements(currentElement, fosOutText, fosOutWiki, fosOutHtml, baosPdf, document, p, mtChapters, false);
                    if (fosOutHtml != null) {
                        fosOutHtml.write(new String("</p>").getBytes());
                        fosOutHtml.write(StringUtils.sCrLf.getBytes());
                    }
                    if (baosPdf != null) {
                        document.add(p);
                    }
                }
                if (currentElement.getName().equals("text")) {
                    appInterface.logLine("Element text=" + currentElement.getText());
                    if (fosOutText != null) {
                        fosOutText.write(currentElement.getText().getBytes());
                        fosOutText.write(StringUtils.sCrLf.getBytes());
                    }
                    if (fosOutWiki != null) {
                        //fosOutWiki.write(currentElement.getText().getBytes());
                        fosOutWiki.write(StringUtils.stringReplace(StringUtils.stringReplace(currentElement.getText(), "<", "&lt;"), ">", "&gt;").getBytes());
                        fosOutWiki.write(StringUtils.sCrLf.getBytes());
                    }
                    if (baosPdf != null) {
                        paragraph.add(createPhrase((paragraph.isEmpty() ? "" : " ") + StringUtils.trimCrLf(currentElement.getText()), currentElement.getAttribute("bold").equals("true")));
                    }
                    if (fosOutHtml != null) {
                        fosOutHtml.write(new String((paragraph.isEmpty() ? "" : " ") + currentElement.getText()).getBytes());
                    }
                }
                if (currentElement.getName().equals("picture")) {
                    if (fosOutWiki != null) {
                        fosOutWiki.write(StringUtils.sCrLf.getBytes());
                        fosOutWiki.write(new String("[[Image:" + currentElement.getAttribute("file") + "]]").getBytes());
                        fosOutWiki.write(StringUtils.sCrLf.getBytes());
                    }
                    if (baosPdf != null) {
                        paragraph.add(createImage(currentElement.getAttribute("file")));
                    }
                    if (fosOutHtml != null) {
                        fosOutHtml.write(new String("<img src='" + StringUtils.trimCrLf(currentElement.getAttribute("file")) + "' />").getBytes());
                        fosOutHtml.write(StringUtils.sCrLf.getBytes());
                    }
                }
            }
        }
    }

    private void processChapters(XMLElement xmlElementRoot, MemoryTable mt, String sPrefix, int iLevel) throws IOException, DocumentException {
        XMLElement currentElement = null;
        int iSerial = 0;
//        String sID = "";

        //HashMap hm = null;
        //ArrayList<String> al = null;
        //hm=new HashMap();
//        Vector<String> vColumnNames = new Vector<String>();
//        vColumnNames.add("id");
//        vColumnNames.add("title");
//        vColumnNames.add("prefix");
//        vColumnNames.add("level");
//        MemoryTable mt = new MemoryTable(vColumnNames, 0);
        Vector<String> vRow = null;

        Vector<XMLElement> vElements = defaultXMLHandler.getElements(xmlElementRoot, null, null, null, false);
        for (int i = 0; i < vElements.size(); i++) {
            currentElement = vElements.get(i);
            if (currentElement != xmlElementRoot) {
                appInterface.logLine("Element name=" + currentElement.getName());
                if (currentElement.getName().equals("chapter")) {
                    appInterface.logLine("Element title=" + currentElement.getAttribute("title"));
                    //iLevel = StringUtils.intValue(currentElement.getAttribute("level"));
                    ++iSerial;
                    vRow = new Vector<String>();
//                    sID = currentElement.getAttribute("id");
//                    if (sID.equals("")) {
//                        sID = "chapter_" + StringUtils.stringReplace(sPrefix, ".", "_") + Integer.toString(iSerial);
//                        //felesleges, nem mûködik ... currentElement.setAttribute("id", sID);
//                    }
//                    vRow.add(sID);
                    vRow.add(currentElement.getAttribute("id"));
                    vRow.add(currentElement.getAttribute("title"));
                    vRow.add(sPrefix + Integer.toString(iSerial) + ".");
                    //vRow.add(currentElement.getAttribute("level"));
                    vRow.add(Integer.toString(iLevel));
                    mt.addRow(vRow);
//                    consoleAppInterface.logLine(sID);
                    appInterface.logLine("Chapter " + sPrefix + Integer.toString(iSerial) + ". " + currentElement.getAttribute("title"));
                    processChapters(currentElement, mt, sPrefix + Integer.toString(iSerial) + ".", iLevel + 1);
                }
            }
        }
    }

    private void createChapterIDs(XMLElement e, int iLevel) {
        Random rnd = new Random();
        int iID = 1000;
        if (e == null) {
            return;
        }
        iID = rnd.nextInt(999999);
        Vector<XMLElement> v123 = e.getChildren();
        //consoleAppInterface.logLine(Integer.toString(v123.size()));
        for (int i = 0; i < v123.size(); i++) {
            if (v123.get(i).getName().equals("chapter")) {
                if (v123.get(i).getAttribute("id").equals("")) {
                    if (!v123.get(i).hasAttribute("id")) {
                        v123.get(i).addAttribute(new XMLAttribute("id", "auto_chapter_id_" + Integer.toString(iLevel) + "_" + Integer.toString(iID)));
                        appInterface.logLine("auto_chapter_id_" + Integer.toString(iLevel) + "_" + Integer.toString(iID));
                        iID = rnd.nextInt(999999);
                    }
                }
                createChapterIDs(v123.get(i), ++iLevel);
            }
        }
    }

    public void create(StringBuffer sbXMLInput, StringBuffer sbOutputPdf) {
        this.sbXMLInput = sbXMLInput;
        this.sbOutputPdf = sbOutputPdf;
        
        ByteArrayOutputStream baosPdf = new ByteArrayOutputStream();
        
        FileOutputStream fosOutText = null;
        FileOutputStream fosOutWiki = null;
        FileOutputStream fosOutHtml = null;
//        FileOutputStream fosOutPdf = null;
        Document document = null;
        Paragraph p = null;
        Font fontTitle = null;
        Vector<String> vColumnNames = new Vector<String>();
        vColumnNames.add("id");
        vColumnNames.add("title");
        vColumnNames.add("prefix");
        vColumnNames.add("level");
        MemoryTable mt = new MemoryTable(vColumnNames, 0);

        defaultXMLHandler = new DefaultXMLHandler(appInterface);

        try {
            if (!sOutputTextFile.equals("")) {
                fosOutText = new FileOutputStream(sOutputTextFile);
            }
            if (!sOutputWikiFile.equals("")) {
                fosOutWiki = new FileOutputStream(sOutputWikiFile);
            }
            if (!sOutputHtmlFile.equals("")) {
                fosOutHtml = new FileOutputStream(sOutputHtmlFile);
            }
            if (!sOutputPdfFile.equals("")) {
                //fosOutPdf = new FileOutputStream(sOutputPdfFile);
                document = new Document();
                //PdfWriter.getInstance(document, fosOutPdf).setStrictImageSequence(true);
                PdfWriter.getInstance(document, baosPdf).setStrictImageSequence(true);
                document.open();
                BaseFont bf = BaseFont.createFont(BaseFont.TIMES_ROMAN, "ISO-8859-2", BaseFont.EMBEDDED);
                fontTitle = new Font(bf, 20, Font.BOLD, BaseColor.BLUE);
                //document.add(new Paragraph("aáeéiíoóöõuúüû", font));
                //document.add(new Paragraph("AÁEÉIÍOÓÖÕUÚÜÛ", font));
            }

            //if (!defaultXMLHandler.readXMLFile("handbook", new File(sXMLInputFile))) { //, "ISO-8859-2"
            if (!defaultXMLHandler.readXMLString("handbook", sbXMLInput.toString())) { //, "ISO-8859-2"
                appInterface.logLine("XML error");
                return;
            }

            //2014.10.06. automatikus fejezet azonosítók:
            XMLElement xmlElementHandbookRoot = defaultXMLHandler.getRootElementByName("handbook");
            if (xmlElementHandbookRoot != null) {
                createChapterIDs(xmlElementHandbookRoot, 0);
            }

            XMLElement xmlElementHandbook = defaultXMLHandler.getFirstElement("handbook", "handbook");
            appInterface.logLine("Title=" + xmlElementHandbook.getAttribute("title"));
            if (fosOutText != null) {
                fosOutText.write(xmlElementHandbook.getAttribute("title").getBytes());
                fosOutText.write(StringUtils.sCrLf.getBytes());
            }
            if (fosOutWiki != null) {
                //fosOutWiki.write(xmlElementHandbook.getAttribute("title").getBytes());
                //fosOutWiki.write(new String("=" + xmlElementHandbook.getAttribute("title") + "=").getBytes());
                //foOutWiki.write(StringUtils.sCrLf.getBytes());
            }
//            if (fosOutPdf != null) {
                p = new Paragraph(xmlElementHandbook.getAttribute("title"), fontTitle);
                p.setAlignment(Paragraph.ALIGN_CENTER);
                p.setSpacingAfter(20);
                document.add(p);
//            }
            if (fosOutHtml != null) {
                fosOutHtml.write(new String("<html>").getBytes());
                fosOutHtml.write(StringUtils.sCrLf.getBytes());
                fosOutHtml.write(new String("<head>").getBytes());
                fosOutHtml.write(StringUtils.sCrLf.getBytes());
                fosOutHtml.write(new String("<title>" + xmlElementHandbook.getAttribute("title") + "</title>").getBytes());
                fosOutHtml.write(StringUtils.sCrLf.getBytes());
                fosOutHtml.write(new String("</head>").getBytes());
                fosOutHtml.write(StringUtils.sCrLf.getBytes());
                fosOutHtml.write(new String("<body>").getBytes());
                fosOutHtml.write(StringUtils.sCrLf.getBytes());
            }
            processChapters(xmlElementHandbook, mt, "", 1);
//            processElements(xmlElementHandbook, fosOutText, fosOutWiki, fosOutHtml, fosOutPdf, document, null, mt, true);
            processElements(xmlElementHandbook, fosOutText, fosOutWiki, fosOutHtml, baosPdf, document, null, mt, true);
            if (fosOutText != null) {
                fosOutText.flush();
                fosOutText.close();
            }
            if (fosOutWiki != null) {
                fosOutWiki.write(new String("[[Category:SQLSzla]]").getBytes()); //@todo paraméterbe tenni!!!
                fosOutWiki.write(StringUtils.sCrLf.getBytes());
                fosOutWiki.flush();
                fosOutWiki.close();
            }
            if (!sOutputPdfFile.equals("")) {
                document.close();
            }
            if (fosOutHtml != null) {
                fosOutHtml.write(new String("</body>").getBytes());
                fosOutHtml.write(StringUtils.sCrLf.getBytes());
                fosOutHtml.write(new String("</html>").getBytes());
                fosOutHtml.write(StringUtils.sCrLf.getBytes());
                fosOutHtml.flush();
                fosOutHtml.close();
            }
        } catch (IOException | DocumentException e) {
            appInterface.handleError(e);
            return;
        }
        if (sbOutputPdf != null) {
            sbOutputPdf.append(baosPdf);
        }
    }
}
