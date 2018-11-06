package hu.mgx.report;

import java.awt.*;
import java.awt.print.*;
import java.util.*;

import hu.mgx.app.common.*;
import hu.mgx.draw.*;
import hu.mgx.swing.table.*;

public class TestReport extends Report
{

    private MemoryTable memoryTable;

    public TestReport()
    {
        super(new DefaultAppFormat());
        init();
    }

    private void init()
    {
        Font font = new Font("Arial", Font.ITALIC, 10);

        ReportSection section = new ReportSection("pageHeader", true, false);
        section.addCell(new Cell("pageHeader1", "pageHeader1", CellType.CHAR, font, 0, 0, 200, 50, DrawAlign.CENTER, DrawAlign.MIDDLE, true));
        section.addCell(new Cell("pageHeader2", "pageHeader2", CellType.SPECIAL_STATIC, font, 400, 0, 200, 50, DrawAlign.CENTER, DrawAlign.MIDDLE, true));
        section.addCell(new Cell("pageHeader3", "pageHeader3", CellType.SPECIAL_STATIC, font, 800, 0, 200, 50, DrawAlign.CENTER, DrawAlign.MIDDLE, true));
        setPageHeader(section);

        section = new ReportSection("section1");
        section.addCell(new Cell("name1", "value1", CellType.CHAR, font, 0, 0, 330, 100, DrawAlign.CENTER, DrawAlign.MIDDLE, true));
        section.addCell(new Cell("name2", "value2", CellType.CHAR, font, 330, 0, 340, 100, DrawAlign.CENTER, DrawAlign.MIDDLE, true));
        section.addCell(new Cell("name3", "value3", CellType.CHAR, font, 670, 0, 330, 100, DrawAlign.CENTER, DrawAlign.MIDDLE, true));
        addSection(section);

        section = new ReportSection("pageFooter", false, true);
        section.addCell(new Cell("pageFooter1", "pageFooter1", CellType.SPECIAL_STATIC, font, 0, 0, 330, 50, DrawAlign.CENTER, DrawAlign.MIDDLE, true));
        section.addCell(new Cell("pageFooter2", "pageFooter2", CellType.SPECIAL_PAGE, font, 330, 0, 340, 50, DrawAlign.CENTER, DrawAlign.MIDDLE, true));
        section.addCell(new Cell("pageFooter3", "pageFooter3", CellType.SPECIAL_DATETIME, font, 670, 0, 330, 50, DrawAlign.CENTER, DrawAlign.MIDDLE, true));
        setPageFooter(section);

    }

    public void test()
    {
        Vector vData;
        Vector vRow;
        Vector vColumnNames;

        //testReport = new TestReport();

        vColumnNames = new Vector();
        vColumnNames.add("pageHeader1");
        vData = new Vector();
        vRow = new Vector();
        vRow.add("company");
        vData.add(vRow);
        memoryTable = new MemoryTable(vData, vColumnNames);
        this.sectionMemoryTable("pageHeader", memoryTable);

        vColumnNames = new Vector();
        vColumnNames.add("name1");
        vColumnNames.add("name2");
        vColumnNames.add("name3");
        vData = new Vector();
        vRow = new Vector();
        vRow.add("value11");
        vRow.add("value12");
        vRow.add("value13");
        vData.add(vRow);
        vRow = new Vector();
        vRow.add("value21");
        vRow.add("value22");
        vRow.add("value23");
        vData.add(vRow);
        vRow = new Vector();
        vRow.add("value31");
        vRow.add("value32");
        vRow.add("value33");
        vData.add(vRow);
        vRow = new Vector();
        vRow.add("value41");
        vRow.add("value42");
        vRow.add("value43");
        vData.add(vRow);
        memoryTable = new MemoryTable(vData, vColumnNames);
        this.sectionMemoryTable("section1", memoryTable);
        new TestPrintReport(this, new PageFormat());
    }

    public static void main(String[] args)
    {
        TestReport testReport = new TestReport();
        testReport.test();
    }
}
