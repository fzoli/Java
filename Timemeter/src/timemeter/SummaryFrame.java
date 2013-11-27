package timemeter;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import static timemeter.Timemeter.STORAGE;

/**
 *
 * @author zoli
 */
public class SummaryFrame extends JFrame {
    
    private static final Object LOCK = new Object();
    
    private static final List<Interval> INTERVALS = STORAGE.getIntervals();
    
    private static final DayInfoMap SUMMARY = new DayInfoMap();
    private static final SummaryListModel LIST_MODEL = new SummaryListModel();
    
    private static final SimpleDateFormat FMT_DAY = new SimpleDateFormat("yyyy.MM.dd.");
    private static final SimpleDateFormat FMT_TIME = new SimpleDateFormat("HH:mm:ss");
    private static final SimpleDateFormat FMT_DAY_TIME = new SimpleDateFormat("yyyy.MM.dd. HH:mm:ss");
    private static final NumberFormat FMT_CURRENCY = NumberFormat.getCurrencyInstance();
    
    private final JTextNumberField TF_PRICE = new JTextNumberField(new DecimalFormat("#.##").format(STORAGE.getPrice()).replaceAll(",", ".")) {

        {
            setRange(0, 1000000);
        }
        
        @Override
        protected void onNumberChanged() {
            refreshCurrency();
            STORAGE.setPrice(getNumber());
        }
        
    };
    
    private final JLabel LB_CURRENCY = new JLabel(FMT_CURRENCY.format(0), SwingConstants.RIGHT);
    
    private final JLabel LB_CURRENCY_TIME = new JLabel(DetailsFrame.createTimeText(0, false), SwingConstants.RIGHT) {
        
        @Override
        public Dimension getMinimumSize() {
            String s = getText() + " Munkaidő:";
            Dimension d = super.getPreferredSize();
            return new Dimension(getFontMetrics(getFont()).stringWidth(s), d.height);
        }
        
    };
    
    private final JList<DayInfo> DAY_INFO_LIST = new JList<DayInfo>(LIST_MODEL) {
        {
            setCellRenderer(new SummaryListRenderer());
            setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            if (LIST_MODEL.getSize() > 0) setSelectedIndex(0);
        }
    };
    
    private final SummaryTableModel TABLE_MODEL = new SummaryTableModel(DAY_INFO_LIST);
    
    private final TableRowSorter<SummaryTableModel> TABLE_SORTER  = new TableRowSorter<SummaryTableModel>(TABLE_MODEL) {
        
        final Comparator<Date> DATE_CMP = new Comparator<Date>() {

            @Override
            public int compare(Date o1, Date o2) {
                if (o1 == null || o2 == null) return 0;
                return o1.compareTo(o2);
            }

        };

        final Comparator<Long> LONG_CMP = new Comparator<Long>() {

            @Override
            public int compare(Long o1, Long o2) {
                return o1.compareTo(o2);
            }

        };

        @Override
        public Comparator<?> getComparator(int column) {
            if (column == 2) return LONG_CMP;
            return DATE_CMP;
        }

        @Override
        public int convertRowIndexToView(int index) {
            try {
                return super.convertRowIndexToView(index);
            }
            catch (Exception ex) {
                return -1;
            }
        }

        @Override
        public void rowsUpdated(int firstRow, int endRow) {
            try {
                super.rowsUpdated(firstRow, endRow);
            }
            catch (Exception ex) {
                ;
            }
        }

    };
    
    private final JTable INTERVAL_TABLE = new JTable(TABLE_MODEL) {
        
        {
            ((DefaultTableCellRenderer)getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
            SummaryTableRenderer renderer = new SummaryTableRenderer(TABLE_MODEL);
            setDefaultRenderer(Date.class, renderer);
            setDefaultRenderer(Long.class, renderer);
            setRowSorter(TABLE_SORTER);
        }

        @Override
        public String getToolTipText(MouseEvent event) {
            int row = rowAtPoint(event.getPoint());
            if (row < 0) return null;
            row = convertRowIndexToModel(row);
            long time = TABLE_MODEL.getIntervals().get(row).getTime();
            if (time < 1000) return null;
            return FMT_CURRENCY.format(getHours(time, false) * STORAGE.getPrice());
        }
        
    };
    
    private static class DayInfo {
        
        public final Date DATE;
        private double hours;

        public DayInfo(Date DATE) {
            this.DATE = DATE;
        }

        public double getHours() {
            return hours;
        }

        public void setHours(double hours) {
            this.hours = hours;
        }
        
    }
    
    private static class DayInfoMap extends LinkedHashMap<DayInfo, List<Interval>> {
        
        public boolean contains(Date d) {
            return findDay(d) != null;
        }
        
        public DayInfo findDay(Date d) {
            for (DayInfo i : keySet()) {
                if (isSameDay(i.DATE, d)) return i;
            }
            return null;
        }
        
        public List<Interval> findList(Date d) {
            DayInfo info = SUMMARY.findDay(d);
            if (info != null) return SUMMARY.get(info);
            return null;
        }
        
    }
    
    private static class SummaryListModel extends AbstractListModel<DayInfo> {

        @Override
        public int getSize() {
            return SUMMARY.size();
        }

        @Override
        public DayInfo getElementAt(int index) {
            synchronized (LOCK) {
                if (index < 0 || index >= getSize()) return null;
                int c = 0;
                Iterator<DayInfo> it = SUMMARY.keySet().iterator();
                while (it.hasNext()) {
                    DayInfo i = it.next();
                    if (c == index) return i;
                    c++;
                }
            }
            return null;
        }

        public boolean fireTable = true;
        
        public void refresh(boolean fireTable) {
            this.fireTable = fireTable;
            fireContentsChanged(this, 0, Math.max(getSize() - 1, 0));
        }
        
    }
    
    private static class SummaryListRenderer extends DefaultListCellRenderer {

        private final DecimalFormat DF = new DecimalFormat("00.000");
        
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel lb = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            DayInfo info = LIST_MODEL.getElementAt(index);
            if (info != null) lb.setText(createDayString(info.DATE) + " [" + createHourString(info.hours) + ']');
            lb.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            lb.setHorizontalAlignment(SwingConstants.CENTER);
            return lb;
        }
        
        private String createDayString(Date d) {
            return FMT_DAY.format(d);
        }
        
        private String createHourString(double d) {
            return DF.format(d);
        }
        
    }
    
    private static class ListDataAdapter implements ListDataListener {

        @Override
        public void intervalAdded(ListDataEvent e) {
            ;
        }

        @Override
        public void intervalRemoved(ListDataEvent e) {
            ;
        }

        @Override
        public void contentsChanged(ListDataEvent e) {
            ;
        }
        
    }
    
    private class SummaryTableModel extends AbstractTableModel {

        private final JList<DayInfo> DAY_LIST;

        public SummaryTableModel(JList<DayInfo> dayList) {
            DAY_LIST = dayList;
        }

        @Override
        public void fireTableChanged(TableModelEvent e) {
            TABLE_SORTER.allRowsChanged();
            super.fireTableChanged(e);
        }
        
        public void refresh() {
            fireTableRowsUpdated(0, Math.max(getRowCount() - 1, 0));
        }
        
        public boolean isLastDay() {
            return DAY_LIST.getSelectedIndex() + 1 == DAY_LIST.getModel().getSize();
        }
        
        public DayInfo getDayInfo() {
            return DAY_LIST.getSelectedValue();
        }
        
        public List<Interval> getIntervals() {
            DayInfo inf = getDayInfo();
            if (inf == null) return null;
            return SUMMARY.get(inf);
        }
        
        @Override
        public int getRowCount() {
            List<Interval> ls = getIntervals();
            if (ls == null) return 0;
            return ls.size();
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 2) return Long.class;
            return Date.class;
        }

        @Override
        public int getColumnCount() {
            return 3;
        }

        @Override
        public String getColumnName(int column) {
            switch (column) {
                case 0:
                    return "Kezdés";
                case 1:
                    return "Befejezés";
                case 2:
                    return "Időtartam";
            }
            return "";
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            List<Interval> ls = getIntervals();
            if (ls == null) return null;
            Interval i = ls.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return i.getBegin();
                case 1:
                    return i.getEnd();
                case 2:
                    return i.getTime();
            }
            return null;
        }
        
    }
    
    private static class SummaryTableRenderer extends DefaultTableCellRenderer {

        private final SummaryTableModel MODEL;

        public SummaryTableRenderer(SummaryTableModel model) {
            MODEL = model;
        }
        
        private void setTimeText(JLabel c, long l) {
            c.setText(DetailsFrame.createFullTimeText(l, false, true));
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, false, false, row, column);
            c.setHorizontalAlignment(SwingConstants.CENTER);
            DayInfo info = MODEL.getDayInfo();
            if (value == null) {
                c.setText("");
                if (info == null) return c;
            }
            switch (table.convertColumnIndexToModel(column)) {
                case 0:
                    c.setText(FMT_TIME.format((Date) value));
                    break;
                case 1:
                    if (value != null) {
                        c.setText(FMT_TIME.format((Date) value));
                    }
                    break;
                case 2:
                    long l = (Long) value;
                    if (l == 0) {
                        int modelRow = table.convertRowIndexToModel(row);
                        Interval i = MODEL.getIntervals().get(modelRow);
                        if (i.getEnd() != null) {
                            setTimeText(c, l);
                        }
                        else {
                            if (MODEL.isLastDay()) {
                                boolean lastRow = modelRow + 1 == table.getRowCount();
                                if (lastRow && STORAGE.isRunning()) {
                                    c.setText("folyamatban");
                                }
                                else {
                                    c.setText("sérült adat");
                                }
                            }
                        }
                    }
                    else {
                        setTimeText(c, l);
                    }
                    break;
            }
            return c;
        }
        
    }
    
    public SummaryFrame() {
        super("Összegzés");
        refreshCurrency();
        GridBagConstraints cl = new GridBagConstraints();
        cl.fill = GridBagConstraints.BOTH;
        cl.weightx = 1;
        cl.weighty = 1;
        cl.insets = new Insets(5, 5, 5, 5);
        JPanel leftPanel = new JPanel(new GridBagLayout());
        JPanel rightPanel = new JPanel(new GridBagLayout());
        cl.gridy = 1;
        leftPanel.add(new JScrollPane(DAY_INFO_LIST, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) {

            @Override
            public Dimension getMinimumSize() {
                Dimension d = DAY_INFO_LIST.getPreferredSize();
                d.width += getVerticalScrollBar().getSize().width;
                return d;
            }
            
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(getMinimumSize().width, 200);
            }
            
        }, cl);
        cl.gridy = 0;
        cl.weighty = 0;
        cl.insets = new Insets(5, 5, 0, 5);
        leftPanel.add(new JLabel("Rögzített napok", SwingConstants.CENTER), cl);
        cl.gridy = 2;
        cl.insets = new Insets(0, 5, 5, 5);
        JPanel pricePanel = new JPanel(new GridBagLayout());
        leftPanel.add(pricePanel, cl);
        GridBagConstraints cp = new GridBagConstraints();
        cp.fill = GridBagConstraints.BOTH;
        cp.weighty = 1;
        pricePanel.add(new JLabel("Óradíj: "), cp);
        cp.gridx = 1;
        cp.weightx = 1;
        pricePanel.add(TF_PRICE, cp);
        cp.gridx = 0;
        cp.gridy = 1;
        cp.gridwidth = 2;
        cp.insets = new Insets(5, 0, 0, 0);
        pricePanel.add(new JLabel("Munkaidő:"), cp);
        pricePanel.add(LB_CURRENCY_TIME, cp);
        cp.gridy = 2;
        pricePanel.add(new JLabel("Fizetendő:"), cp);
        pricePanel.add(LB_CURRENCY, cp);
        GridBagConstraints cr = new GridBagConstraints();
        cr.fill = GridBagConstraints.BOTH;
        cr.weightx = 1;
        cr.weighty = 1;
        cr.insets = new Insets(5, 5, 5, 5);
        rightPanel.add(new JScrollPane(INTERVAL_TABLE) {
            
            {
                setBorder(BorderFactory.createEmptyBorder());
                setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(super.getPreferredSize().width, 0);
            }
            
        }, cr);
        add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel));
        pack();
        setMinimumSize(getSize());
        setSize(400, 200);
        setLocationRelativeTo(null);
        LIST_MODEL.addListDataListener(new ListDataAdapter() {

            @Override
            public void contentsChanged(ListDataEvent e) {
                refreshCurrency();
                if (LIST_MODEL.fireTable) {
                    TABLE_MODEL.refresh();
                    INTERVAL_TABLE.revalidate();
                }
            }
            
        });
        DAY_INFO_LIST.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                TABLE_MODEL.refresh();
                INTERVAL_TABLE.revalidate();
            }
            
        });
    }
    
    public final void refreshCurrency() {
        long time = STORAGE.getRunningTimeSum();
        long roundedTime = createTime(time, true);
        double money = getHours(time, true) * TF_PRICE.getNumber();
        LB_CURRENCY.setText(FMT_CURRENCY.format(round(money)));
        LB_CURRENCY.setToolTipText(FMT_CURRENCY.format(money));
        LB_CURRENCY_TIME.setText(DetailsFrame.createFullTimeText(roundedTime, false));
        LB_CURRENCY_TIME.setToolTipText(DetailsFrame.createFullTimeText(time, false));
    }
    
    private final DecimalFormat DF_ROUNDER = new DecimalFormat("0.00");
    
    private int round(double d) {
        String str = DF_ROUNDER.format(d);
        str = str.replaceAll(",", ".");
        int index = str.length() - 4;
        double end = Double.parseDouble(str.substring(index));
        StringBuilder sb = new StringBuilder(str);
        String ending = "0";
        boolean upper = false;
        if (end > 0 && end < 2.5) ending = "0";
        if (end >= 2.5 && end <= 5.0) ending = "5";
        if (end > 5.0 && end < 7.5) ending = "5";
        if (end >= 7.5 && end < 10) { ending = "0"; upper = true; }
        sb.replace(index, index + 1, ending);
        int rounded = (int) Double.parseDouble(sb.toString());
        if (upper) rounded += 10;
        return rounded;
    }
    
    @Override
    public void setVisible(boolean b) {
        if (b) repaintIcon();
        super.setVisible(b);
    }
    
    public void repaintIcon() {
        setIconImage(Timemeter.getTrayImage());
    }
    
    private static boolean isSameDay(Date d1, Date d2) {
        return FMT_DAY.format(d1).equals(FMT_DAY.format(d2));
    }
    
    private static boolean isDifferentDay(Interval i) {
        return i.getEnd() != null && !isSameDay(i.getBegin(), i.getEnd());
    }
    
    public static void refresh() {
        synchronized (LOCK) {
            SUMMARY.clear();
            List<Interval> ls = createMergedList();
            for (Interval i : ls) {
                if (!SUMMARY.contains(i.getBegin())) {
                    SUMMARY.put(new DayInfo(i.getBegin()), new ArrayList<Interval>());
                    if (isDifferentDay(i)) {
                        SUMMARY.put(new DayInfo(i.getEnd()), new ArrayList<Interval>());
                    }
                }
            }
            for (Interval i : ls) {
                if (!isDifferentDay(i)) {
                    SUMMARY.findList(i.getBegin()).add(i);
                }
                else {
                    Date newDay = createDay(i.getEnd());
                    List<Interval> ls1 = SUMMARY.findList(i.getBegin());
                    List<Interval> ls2 = SUMMARY.findList(i.getEnd());
                    Interval i1 = new Interval().setBegin(i.getBegin()).setEnd(newDay);
                    Interval i2 = new Interval().setBegin(newDay).setEnd(i.getEnd());
                    ls1.add(i1);
                    ls2.add(i2);
                }
            }
            Iterator<Map.Entry<DayInfo, List<Interval>>> it = SUMMARY.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<DayInfo, List<Interval>> e = it.next();
                DayInfo i = e.getKey();
                List<Interval> l = e.getValue();
                i.setHours(IntervalStorage.getTimeSum(l) / (1000.0 * 60 * 60));
            }
        }
        LIST_MODEL.refresh(true);
    }
    
    private static List<Interval> createMergedList() {
        List<Interval> ls = new ArrayList<Interval>(INTERVALS);
        int i = 0;
        while (i < ls.size()) {
            Interval iv = ls.get(i);
            if (iv.getBegin() == null/* || (iv.getEnd() != null && (iv.getTime() < 1000))*/) {
                ls.remove(i);
                continue;
            }
            if (iv.getEnd() == null) {
                i++;
                continue;
            }
            if (i + 1 < ls.size()) {
                Interval iv2 = ls.get(i + 1);
                if (iv2.getBegin() == null) {
                    ls.remove(i + 1);
                    continue;
                }
                if (FMT_DAY_TIME.format(iv.getEnd()).equals(FMT_DAY_TIME.format(iv2.getBegin()))) {
                    Interval ni = new Interval().setBegin(iv.getBegin()).setEnd(iv2.getEnd());
                    if (iv2.getEnd() != null) ni.setTime(iv.getTime() + iv2.getTime());
                    ls.set(i, ni);
                    ls.remove(i + 1);
                    continue;
                }
            }
            i++;
        }
        return ls;
    }
    
    private static double getHours(long l, boolean round) {
        return createTime(l, round) / (1000.0 * 60 * 60);
    }
    
    private static long createTime(long l, boolean round) {
        if (!round) return l;
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(l);
        if (c.get(Calendar.MILLISECOND) > 0) c.set(Calendar.SECOND, c.get(Calendar.SECOND) + 1);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }
    
    private static Date createDay(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(d.getTime());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    
//    public static void main(String[] args) throws Exception {
//        Timemeter.main(args);
//        Timemeter.getSummaryFrame().setVisible(true);
//        Timemeter.getSummaryFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    }
    
}

class JTextNumberField extends JTextField {

    private double number;
    private double min = 0, max = Double.MAX_VALUE;
    private int precision = 5;
    
    public JTextNumberField(String s) {
        super(s);
        number = Double.parseDouble(s);
        setHorizontalAlignment(RIGHT);
        setupFilter();
    }

    public double getNumber() {
        return number;
    }

    private void setNumber(double number) {
        this.number = number;
        onNumberChanged();
    }
    
    public void setPrecisionLength(int precision) {
        this.precision = precision;
    }
    
    protected void onNumberChanged() {
        ;
    }
    
    public void setRange(double min, double max) {
        this.min = min;
        this.max = max;
    }
    
    private void setupFilter() {
        ((AbstractDocument) getDocument()).setDocumentFilter(new DocumentFilter() {

            @Override
            public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
                ;
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attr) throws BadLocationException {
                StringBuilder sb = new StringBuilder();
                sb.append(fb.getDocument().getText(0, fb.getDocument().getLength()));
                sb.replace(offset, offset + length, text);
                if (isValidNumber(sb.toString())) super.replace(fb, offset, length, text, attr);
            }

            @Override
            public void remove(DocumentFilter.FilterBypass fb, int offset, int length) throws BadLocationException {
                StringBuilder sb = new StringBuilder();
                sb.append(fb.getDocument().getText(0, offset));
                sb.append(fb.getDocument().getText(offset + length, fb.getDocument().getLength() - offset - length));
                if (isValidNumber(sb.toString())) super.remove(fb, offset, length);
            }
            
            private boolean isValidNumber(String text) {
                String txt = text.trim();
                if (txt.isEmpty()) {
                    setNumber(0);
                    return true;
                }
                if (txt.startsWith(".") || txt.startsWith("-")) return false;
                try {
                    double d = Double.parseDouble(txt);
                    int dotIndex = txt.indexOf('.');
                    if (dotIndex > 0 && dotIndex < txt.length()) {
                        String zs = txt.substring(dotIndex + 1);
                        if (zs.length() > precision) return false;
                    }
                    boolean ok = (d >= min && d <= max) && !txt.startsWith((int) d == 0 ? "00" : "0");
                    if (ok) setNumber(d);
                    return ok;
                }
                catch(Exception ex) {
                    return false;
                }
            }

        });
    }

}
