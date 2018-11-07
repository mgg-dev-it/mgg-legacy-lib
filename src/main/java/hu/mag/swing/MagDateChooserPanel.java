package hu.mag.swing;

import hu.mgx.app.swing.SwingAppInterface;
import hu.mgx.swing.CommonPanel;
import hu.mgx.swing.table.MemoryTable;
import hu.mgx.util.DateTimeUtils;
import hu.mgx.util.StringUtils;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

/**
 *
 * @author MaG
 */
public class MagDateChooserPanel extends CommonPanel implements ActionListener, MouseListener {

    private final SwingAppInterface swingAppInterface;
    private MemoryTable mtLabels;
    //private String[] saColumnNames;
    private final String[] saColumnNames = {"week", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"};
    //private int iWeekColumn;
    //private int iMondayColumn;
    //private final static Border highlightBorder = BorderFactory.createLineBorder(Color.yellow, 3);
    //private Color c = Color.blue;
    private final static Color todayColor = new Color(0, 128, 255);
    private final static Color initialDateColor = new Color(255, 0, 255);
    private final static Color highlightColor = new Color(192, 255, 192);
    private final static Border highlightBorder = BorderFactory.createMatteBorder(3, 5, 3, 5, highlightColor);
    private final static Border emptytBorder = BorderFactory.createEmptyBorder(3, 5, 3, 5);
    private CommonPanel cpHead;
//    private JButton jButtonPreviousYear;
//    private JButton jButtonPreviousMonth;
//    private JButton jButtonNextMonth;
//    private JButton jButtonNextYear;
    private JLabel jLabelPreviousYear;
    private JLabel jLabelPreviousMonth;
    private JLabel jLabelSpace1;
    private JLabel jLabelSpace2;
    private JLabel jLabelYear;
    private JLabel jLabelMonth;
    private JLabel jLabelSpace3;
    private JLabel jLabelSpace4;
    private JLabel jLabelNextMonth;
    private JLabel jLabelNextYear;
    //private JLabel jLabelDate;
    private LocalDate baseDate;
    private LocalDate todayDate;
    private int iYear;
    private int iMonth;
    private int iDay;
    private int iInitialYear;
    private int iInitialMonth;
    private int iInitialDay;
    private int iCurrentYear;
    private int iCurrentMonth;
    private int iCurrentDay;
    private JLabel jLabelHighLighted;
    private LocalDate selectedDate;
    private MagButton mbCancel;

    private boolean bHighLightCursor;
    private boolean bDisplayOnly;

    private Vector<MagComponentEventListener> vMagComponentEventListeners = new Vector<>();

    public MagDateChooserPanel(SwingAppInterface swingAppInterface) {
        super();
        this.swingAppInterface = swingAppInterface;
        init();
    }

    private void init() {
        bHighLightCursor = true;
        bDisplayOnly = false;
        todayDate = LocalDate.now();
        selectedDate = todayDate;
        iCurrentYear = todayDate.getYear();
        iCurrentMonth = todayDate.getMonthValue();
        iCurrentDay = todayDate.getDayOfMonth();

        //setInsets(3, 5, 3, 5);
        setInsets(0, 0, 0, 0);
        //setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        setBorder(BorderFactory.createEmptyBorder(27, 27, 27, 27));
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 1), BorderFactory.createEmptyBorder(3, 3, 3, 3)));
        addMouseListener(this);
        //addToCurrentRow(new JLabel("MagDateChooserPanel"), saColumnNames.length, 1, 1.0, 1.0);
        //nextRow();

        cpHead = CommonPanel.createBorderLessCommonPanel();
//        jButtonPreviousYear = new JButton("<<");
//        jButtonPreviousMonth = new JButton("<");
        jLabelPreviousYear = new JLabel("<<");
        jLabelPreviousYear.setName("PreviousYear");
        jLabelPreviousYear.addMouseListener(this);
        jLabelPreviousYear.setBorder(emptytBorder);
        jLabelPreviousMonth = new JLabel("<");
        jLabelPreviousMonth.setName("PreviousMonth");
        jLabelPreviousMonth.addMouseListener(this);
        jLabelPreviousMonth.setBorder(emptytBorder);
        jLabelSpace1 = new JLabel(" ");
        jLabelSpace2 = new JLabel(" ");
        jLabelSpace3 = new JLabel(" ");
        jLabelSpace4 = new JLabel(" ");
        //jLabelYear = new JLabel(Integer.toString(todayDate.getYear()));
        //jLabelMonth = new JLabel(DateTimeUtils.getMonthName(todayDate));
        jLabelYear = new JLabel(" ");
        jLabelMonth = new JLabel(" ");
//        jButtonNextMonth = new JButton(">");
//        jButtonNextYear = new JButton(">>");
        jLabelNextMonth = new JLabel(">");
        jLabelNextMonth.setName("NextMonth");
        jLabelNextMonth.addMouseListener(this);
        jLabelNextMonth.setBorder(emptytBorder);
        jLabelNextYear = new JLabel(">>");
        jLabelNextYear.setName("NextYear");
        jLabelNextYear.addMouseListener(this);
        jLabelNextYear.setBorder(emptytBorder);
//        cpHead.addToCurrentRow(jButtonPreviousYear, 1, 1, 0.0, 0.0);
//        cpHead.addToCurrentRow(jButtonPreviousMonth, 1, 1, 0.0, 0.0);
        cpHead.addToCurrentRow(jLabelPreviousYear, 1, 1, 0.0, 0.0);
        //*cpHead.addToCurrentRow(jLabelSpace1, 1, 1, 0.0, 0.01);
        cpHead.addToCurrentRow(jLabelPreviousMonth, 1, 1, 0.0, 0.0);
        cpHead.addToCurrentRow(jLabelSpace2, 1, 1, 0.0, 0.1);
        cpHead.addToCurrentRow(jLabelYear, 1, 1, 0.0, 0.0);
        cpHead.addToCurrentRow(jLabelMonth, 1, 1, 0.0, 0.0);
        cpHead.addToCurrentRow(jLabelSpace3, 1, 1, 0.0, 0.1);
//        cpHead.addToCurrentRow(jButtonNextMonth, 1, 1, 0.0, 0.0);
//        cpHead.addToCurrentRow(jButtonNextYear, 1, 1, 0.0, 0.0);
        cpHead.addToCurrentRow(jLabelNextMonth, 1, 1, 0.0, 0.0);
        //*cpHead.addToCurrentRow(jLabelSpace4, 1, 1, 0.0, 0.01);
        cpHead.addToCurrentRow(jLabelNextYear, 1, 1, 0.0, 0.0);
        addToCurrentRow(cpHead, saColumnNames.length, 1, 0.0, 0.01);
        nextRow();

        addToCurrentRow(new JLabel(""), 1, 1, 0.0, 0.0);
        addToCurrentRow(createHeaderLabel("H"), 1, 1, 0.0, 0.0);
        addToCurrentRow(createHeaderLabel("K"), 1, 1, 0.0, 0.0);
        addToCurrentRow(createHeaderLabel("Sze"), 1, 1, 0.0, 0.0);
        addToCurrentRow(createHeaderLabel("Cs"), 1, 1, 0.0, 0.0);
        addToCurrentRow(createHeaderLabel("P"), 1, 1, 0.0, 0.0);
        addToCurrentRow(createHeaderLabel("Szo"), 1, 1, 0.0, 0.0);
        addToCurrentRow(createHeaderLabel("V"), 1, 1, 0.0, 0.0);
        nextRow();
        mtLabels = new MemoryTable(saColumnNames, 0);
        Vector<JLabel> vRow = new Vector<>();
        for (int iRow = 0; iRow < 6; iRow++) {
            vRow = new Vector<>();
            for (int iCol = 0; iCol < saColumnNames.length; iCol++) {
                if (iCol == 0) {
                    vRow.add(createWeekLabel("XY"));
                    addToCurrentRow(vRow.elementAt(vRow.size() - 1), 1, 1, 0.0, 0.01);
                } else {
                    JLabel jLabel = new JLabel("XY", SwingConstants.RIGHT);
                    jLabel.setName(iRow + "|" + iCol + "|");
                    jLabel.setBorder(emptytBorder);
                    jLabel.addMouseListener(this);
                    vRow.add(jLabel);
                    addToCurrentRow(vRow.elementAt(vRow.size() - 1), 1, 1, 0.0, 0.0);
                }
            }
            mtLabels.addRow(vRow);
            //addToCurrentRow(new JLabel(" "), 1, 1, 0.0, 0.0);
            nextRow();
        }
        //jLabelDate = new JLabel(" ", SwingConstants.LEFT);
        //addToCurrentRow(jLabelDate, saColumnNames.length, 1, 1.0, 1.0);
        //nextRow();
        setDate(todayDate);
        nextRow();
        mbCancel = MagButton.createMagButton(swingAppInterface, MagButton.BUTTON_CANCEL, this);
        addToCurrentRow(mbCancel, saColumnNames.length, 1, 1.0, 1.0);
        mbCancel.setVisible(false);

    }

    public void setHighLightCursor(boolean b) {
        this.bHighLightCursor = b;
    }

    public void setDate(java.util.Date date) {
        if (date == null) {
            setDate(LocalDate.now());
            return;
        }
        setDate(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }

    public void setDate(LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        iInitialYear = date.getYear();
        iInitialMonth = date.getMonthValue();
        iInitialDay = date.getDayOfMonth();
        setDateInner(date);
    }

    private void setDateInner(LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        iYear = date.getYear();
        iMonth = date.getMonthValue();
        iDay = date.getDayOfMonth();
        jLabelYear.setText(Integer.toString(date.getYear()));
        jLabelMonth.setText(DateTimeUtils.getMonthName(date));
        LocalDate date1 = date.minusDays(date.getDayOfMonth() - 1);
        date1 = date1.minusDays(date1.getDayOfWeek().getValue() - 1);
        baseDate = date1;
        int iWeek = 0;
        for (int iRow = 0; iRow < 6; iRow++) {
            for (int iCol = 0; iCol < saColumnNames.length; iCol++) {
                //@todo week
                if (iCol == 0) {
                    iWeek = DateTimeUtils.getWeek(date1);
                    ((JLabel) mtLabels.getValueAt(iRow, iCol)).setText(Integer.toString(iWeek));
                    ((JLabel) mtLabels.getValueAt(iRow, iCol)).setForeground(Color.gray);
                } else {
                    if (date1.getMonthValue() == iMonth) {
                        iWeek = DateTimeUtils.getWeek(date1);
                    }
                    ((JLabel) mtLabels.getValueAt(iRow, iCol)).setText(Integer.toString(date1.getDayOfMonth()));
                    ((JLabel) mtLabels.getValueAt(iRow, iCol)).setForeground(Color.black);
                    ((JLabel) mtLabels.getValueAt(iRow, iCol)).setName(iRow + "|" + iCol + "|" + Integer.toString(date1.getYear()) + "|" + Integer.toString(date1.getMonthValue()) + "|" + Integer.toString(date1.getDayOfMonth()));
                    if (date1.getMonthValue() != iMonth) {
                        ((JLabel) mtLabels.getValueAt(iRow, iCol)).setForeground(Color.lightGray);
                    } else {
                        if (date1.getDayOfWeek() == DayOfWeek.SUNDAY) {
                            ((JLabel) mtLabels.getValueAt(iRow, iCol)).setForeground(Color.red);
                        }
                        if (date1.getYear() == iInitialYear && date1.getMonthValue() == iInitialMonth && date1.getDayOfMonth() == iInitialDay) {
                            if (!bDisplayOnly) {
                                ((JLabel) mtLabels.getValueAt(iRow, iCol)).setForeground(initialDateColor);
                            }
                        }
                        if (date1.getYear() == iCurrentYear && date1.getMonthValue() == iCurrentMonth && date1.getDayOfMonth() == iCurrentDay) {
                            ((JLabel) mtLabels.getValueAt(iRow, iCol)).setForeground(todayColor);
                        }
                    }
                    date1 = date1.plusDays(1);
                }
            }
            //((JLabel) mtLabels.getValueAt(iRow, 0)).setText(StringUtils.right("  " + Integer.toString(iWeek), 2));
            ((JLabel) mtLabels.getValueAt(iRow, 0)).setText(Integer.toString(iWeek));
        }
        //jLabelDate.setText(" " + swingAppInterface.getDateFormat().format(DateTimeUtils.convertToUtilDate(date)));
    }

    private JLabel createHeaderLabel(String sCaption) {
        JLabel jLabel = new JLabel(sCaption, SwingConstants.CENTER);
        jLabel.setBorder(emptytBorder);
        //jLabel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray), BorderFactory.createEmptyBorder(3, 5, 3, 5)));
        jLabel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2), BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray), BorderFactory.createEmptyBorder(3, 3, 3, 3))));
        return (jLabel);
    }

    private JLabel createWeekLabel(String sCaption) {
        JLabel jLabel = createBaseLabel(sCaption);
        //jLabel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray), BorderFactory.createEmptyBorder(3, 5, 3, 5)));
        jLabel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(1, 0, 1, 0), BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.gray), BorderFactory.createEmptyBorder(2, 5, 2, 5))));
        return (jLabel);
    }

    private JLabel createBaseLabel(String sCaption) {
        return (createBaseLabel(sCaption, -1, -1));
    }

    private JLabel createBaseLabel(String sCaption, int iRow, int iCol) {
        JLabel jLabel = new JLabel(sCaption, SwingConstants.RIGHT);
        if (iRow > -1 && iCol > -1) {
            jLabel.setName(iRow + "-" + iCol);
        }
        jLabel.setBorder(emptytBorder);
        jLabel.addMouseListener(this);
        return (jLabel);
    }

//    private int getRowFromName(String sName) {
//        String[] sa = sName.split("\\|");
//        int iRetVal = -1;
//        if (sName != null && sName.contains("-")) {
//            iRetVal = StringUtils.intValue(StringUtils.left(sName, sName.indexOf("-")));
//        }
//        return (iRetVal);
//    }
//
//    private int getColFromName(String sName) {
//        int iRetVal = -1;
//        if (sName != null && sName.contains("-")) {
//            iRetVal = StringUtils.intValue(StringUtils.mid(sName, sName.indexOf("-") + 1));
//        }
//        return (iRetVal);
//    }
    public LocalDate getSelectedLocalDate() {
        return (selectedDate);
    }

    public java.util.Date getSelectedUtilDate() {
        return (DateTimeUtils.convertToUtilDate(selectedDate));
    }

    public void setDisplayOnly() {
        mbCancel.setVisible(false);
        jLabelPreviousYear.setVisible(false);
        jLabelPreviousMonth.setVisible(false);
        jLabelNextMonth.setVisible(false);
        jLabelNextYear.setVisible(false);
        bDisplayOnly = true;
    }

    public void setAllDateBackColor(Color color) {
        for (int iRow = 0; iRow < mtLabels.getRowCount(); iRow++) {
            for (int iCol = 0; iCol < mtLabels.getColumnCount(); iCol++) {
                String sName = ((JLabel) mtLabels.getValueAt(iRow, iCol)).getName();
                if (sName != null) {
                    String[] sa = sName.split("\\|");
                    if (sa.length > 4) {
                        //if (ld.isEqual(LocalDate.of(StringUtils.intValue(sa[2]), StringUtils.intValue(sa[3]), StringUtils.intValue(sa[4])))) {
                            ((JLabel) mtLabels.getValueAt(iRow, iCol)).setOpaque(true);
                            ((JLabel) mtLabels.getValueAt(iRow, iCol)).setBackground(color);
                        //}
                    }
                }
            }
        }
    }

    public void setDateBackColor(LocalDate ld, Color color) {
        for (int iRow = 0; iRow < mtLabels.getRowCount(); iRow++) {
            for (int iCol = 0; iCol < mtLabels.getColumnCount(); iCol++) {
                String sName = ((JLabel) mtLabels.getValueAt(iRow, iCol)).getName();
                if (sName != null) {
                    String[] sa = sName.split("\\|");
                    if (sa.length > 4) {
                        if (ld.isEqual(LocalDate.of(StringUtils.intValue(sa[2]), StringUtils.intValue(sa[3]), StringUtils.intValue(sa[4])))) {
                            ((JLabel) mtLabels.getValueAt(iRow, iCol)).setOpaque(true);
                            ((JLabel) mtLabels.getValueAt(iRow, iCol)).setBackground(color);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().getClass() == JLabel.class) {
            JLabel jLabelSource = (JLabel) e.getSource();
            switch (jLabelSource.getName()) {
                case "PreviousYear":
                    --iYear;
                    setDateInner(LocalDate.of(iYear, iMonth, 1));
                    return;
                case "PreviousMonth":
                    --iMonth;
                    if (iMonth < 1) {
                        iMonth = 12;
                        --iYear;
                    }
                    setDateInner(LocalDate.of(iYear, iMonth, 1));
                    return;
                case "NextMonth":
                    ++iMonth;
                    if (iMonth > 12) {
                        iMonth = 1;
                        ++iYear;
                    }
                    setDateInner(LocalDate.of(iYear, iMonth, 1));
                    return;
                case "NextYear":
                    ++iYear;
                    setDateInner(LocalDate.of(iYear, iMonth, 1));
                    return;
            }
            if (jLabelSource.getName() != null) {
                String[] sa = jLabelSource.getName().split("\\|");
                if (sa.length > 4) {
                    selectedDate = LocalDate.of(StringUtils.intValue(sa[2]), StringUtils.intValue(sa[3]), StringUtils.intValue(sa[4]));
                    fireMagComponentEventActionOK();
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getSource().getClass() == JLabel.class) {
            JLabel jLabelSource = (JLabel) e.getSource();
            if (jLabelSource == jLabelPreviousYear || jLabelSource == jLabelPreviousMonth || jLabelSource == jLabelNextMonth || jLabelSource == jLabelNextYear) {
                jLabelSource.setBorder(highlightBorder);
                if (bHighLightCursor) {
                    jLabelSource.setOpaque(true);
                    jLabelSource.setBackground(highlightColor);
                }
                jLabelHighLighted = ((JLabel) e.getSource());
            } else {
                if (jLabelSource.getName() != null) {
                    String[] sa = jLabelSource.getName().split("\\|");
                    if (sa.length > 1) {
                        int iRow = StringUtils.intValue(sa[0]);
                        int iCol = StringUtils.intValue(sa[1]);
                        if (iRow > -1 && iCol > 0) {
                            jLabelSource.setBorder(highlightBorder);
                            if (bHighLightCursor) {
                                jLabelSource.setOpaque(true);
                                jLabelSource.setBackground(highlightColor);
                            }
                            jLabelHighLighted = ((JLabel) e.getSource());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getSource().getClass() == JLabel.class) {
            JLabel jLabelSource = (JLabel) e.getSource();
            if (jLabelHighLighted != null && jLabelHighLighted == jLabelSource) {
                jLabelSource.setBorder(emptytBorder);
                if (bHighLightCursor) {
                    jLabelSource.setOpaque(false);
                    jLabelSource.setBackground(null);
                }
            } else {
                if (jLabelSource.getName() != null) {
                    String[] sa = jLabelSource.getName().split("\\|");
                    if (sa.length > 1) {
                        int iRow = StringUtils.intValue(sa[0]);
                        int iCol = StringUtils.intValue(sa[1]);
                        if (iRow > -1 && iCol > 0) {
                            jLabelSource.setBorder(emptytBorder);
                            if (bHighLightCursor) {
                                jLabelSource.setOpaque(false);
                                jLabelSource.setBackground(null);
                            }
                        }
                    }
                }
            }
        }
    }

    public void addMagComponentEventListener(MagComponentEventListener mcel) {
        for (int i = 0; i < vMagComponentEventListeners.size(); i++) {
            if (vMagComponentEventListeners.elementAt(i).equals(mcel)) {
                return;
            }
        }
        vMagComponentEventListeners.add(mcel);
    }

    public void removeMagComponentEventListener(MagComponentEventListener mcel) {
        for (int i = 0; i < vMagComponentEventListeners.size(); i++) {
            if (vMagComponentEventListeners.elementAt(i).equals(mcel)) {
                vMagComponentEventListeners.remove(i);
            }
        }
    }

    private void fireMagComponentEventActionOK() {
        MagComponentEvent mce = new MagComponentEvent(this, MagComponentEvent.ACTION_OK);
        for (int i = 0; i < vMagComponentEventListeners.size(); i++) {
            vMagComponentEventListeners.elementAt(i).componentEventPerformed(mce);
        }
    }

//    private void fireMagComponentEventActionESCAPE() {
//        MagComponentEvent mce = new MagComponentEvent(this, MagComponentEvent.ACTION_ESCAPE);
//        for (int i = 0; i < vMagComponentEventListeners.size(); i++) {
//            vMagComponentEventListeners.elementAt(i).componentEventPerformed(mce);
//        }
//    }
    private void fireMagComponentEventActionCancel() {
        MagComponentEvent mce = new MagComponentEvent(this, MagComponentEvent.ACTION_CANCEL);
        for (int i = 0; i < vMagComponentEventListeners.size(); i++) {
            vMagComponentEventListeners.elementAt(i).componentEventPerformed(mce);
        }
    }

    public void actionPerformed(ActionEvent e) {
//        if (e.getActionCommand().equals("action_ok")) {
//            actionOK();
//        }
        if (e.getActionCommand().equals("action_cancel")) {
            fireMagComponentEventActionCancel();
        }
    }

}
