package shop.view;


import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;
import shop.utils.DesktopRender;
import shop.utils.RoundedPanel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static javax.swing.JOptionPane.showMessageDialog;
import static shop.utils.DesktopRender.FONT_FAMILY;

public class StoricoPane extends AContainer implements ActionListener {

    private static final String DATE_FORMAT = "dd/MM/yyyy";

    // pannello interno
    private JPanel internPane, wrapperPane, clientPane;
    private RoundedPanel searchPane;
    private static final Color JTF_COLOR = new Color(46, 134, 193);

    public static DefaultTableModel tableModel;
    JTableHeader tableHeader;
    public static JTable table;
    JScrollPane scrollPane;

    protected JButton btn_search, btn_refresh;
    public static JDateChooser beginChooser, endChooser;
    private TableRowSorter<TableModel> sorter;

    // Pulsante di carica articolo
    private Font font;

    public StoricoPane() {
        initPanel();
    }

    public void initPanel() {

        font = new Font(FONT_FAMILY, Font.BOLD, 16);
        ToolTipManager.sharedInstance().setInitialDelay(500);
        ToolTipManager.sharedInstance().setDismissDelay(4000);

        // I pulsanti della Toolbar
        RoundedPanel toolbar = new RoundedPanel();
        toolbar.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));


        JLabel lblFormName = new JLabel("Storico prodotti");
        lblFormName.setForeground(Color.WHITE);
        lblFormName.setFont(new Font("HelveticaNeue", Font.BOLD, 28));
        toolbar.setBackground(new Color(128, 0, 128));
        lblFormName.setPreferredSize(new Dimension(360, 40));
        toolbar.add(lblFormName, BorderLayout.CENTER);


        // I pulsanti delle funzionalita'
        internPane = new JPanel();
        wrapperPane = new JPanel();
        clientPane = new JPanel();
        searchPane = new RoundedPanel();
        initComponents();
        buildClientArea();
        container.setLayout(new BorderLayout());
        container.add(toolbar, BorderLayout.NORTH);
    }

    public void initComponents() {
        internPane.setBounds(90, 110, 1200, 675);
        wrapperPane.setPreferredSize(new Dimension(1200, 675));
        internPane.setBackground(container.getBackground());
        internPane.setLayout(new BorderLayout());
        internPane.add(wrapperPane, BorderLayout.CENTER);
        container.add(internPane);
    }

    private void buildClientArea() {

        wrapperPane.setBackground(new Color(39, 55, 70));
        Border line = BorderFactory.createLineBorder(Color.WHITE);
        Border empty = new EmptyBorder(5, 10, 5, 10);
        CompoundBorder border = new CompoundBorder(line, empty);
        wrapperPane.setBorder(border);
        wrapperPane.setLayout(new FlowLayout());

        clientPane.setBackground(wrapperPane.getBackground());
        clientPane.setPreferredSize(new Dimension(1150, 450));
        buildArticleDetails();
        searchPane.setPreferredSize(new Dimension(1150, 80));
        searchPane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 10, 3, 10);

        JLabel lbl_begin = new JLabel("Seleziona date: dal");
        lbl_begin.setFont(new Font(FONT_FAMILY, Font.BOLD, 18));

        beginChooser = new JDateChooser();
        beginChooser.setDateFormatString(DATE_FORMAT);
        beginChooser.setPreferredSize(new Dimension(120, 48));
        beginChooser.setFont(new Font(FONT_FAMILY, Font.BOLD, 16));
        beginChooser.setMaxSelectableDate(new Date());
        JTextFieldDateEditor dateEditor = (JTextFieldDateEditor) beginChooser.getComponent(1);
        dateEditor.setHorizontalAlignment(JTextField.RIGHT);
        dateEditor.setFont(font);
        dateEditor.setBackground(JTF_COLOR);
        dateEditor.setBorder(new LineBorder(Color.BLACK));

        JLabel lbl_end = new JLabel("al");
        lbl_end.setFont(new Font(FONT_FAMILY, Font.BOLD, 16));

        endChooser = new JDateChooser();
        endChooser.setDateFormatString(DATE_FORMAT);
        endChooser.setPreferredSize(new Dimension(120, 48));
        endChooser.setFont(new Font(FONT_FAMILY, Font.BOLD, 16));
        endChooser.setMaxSelectableDate(new Date());
        JTextFieldDateEditor dt_editor = (JTextFieldDateEditor) endChooser.getComponent(1);
        dt_editor.setHorizontalAlignment(JTextField.RIGHT);
        dt_editor.setFont(font);
        dt_editor.setBackground(JTF_COLOR);
        dt_editor.setBorder(new LineBorder(Color.BLACK));

        btn_search = new JButton(new ImageIcon(requireNonNull(ClassLoader.getSystemClassLoader().getResource("images/search.png"))));
        btn_search.setPreferredSize(new Dimension(48, 48));
        btn_search.setContentAreaFilled(false);
        btn_search.setOpaque(false);

        btn_refresh = new JButton(new ImageIcon(requireNonNull(ClassLoader.getSystemClassLoader().getResource("images/refresh.png"))));
        btn_refresh.setPreferredSize(new Dimension(48, 48));
        btn_refresh.setContentAreaFilled(false);
        btn_refresh.setOpaque(false);

        searchPane.add(lbl_begin, c);
        searchPane.add(beginChooser, c);
        searchPane.add(lbl_end, c);
        searchPane.add(endChooser, c);
        searchPane.add(btn_search, c);
        searchPane.add(btn_refresh, c);

        wrapperPane.add(searchPane, BorderLayout.NORTH);
        wrapperPane.add(clientPane, BorderLayout.CENTER);
    }

    void buildArticleDetails() {
        String[] header = {"Data Carico", "Codice Prodotto", "Descrizione", "Quantita'", "Fornitore", "Note", "Data Carico", "Codice Prodotto", "Descrizione", "Quantita'", "Fornitore", "Note"};
        tableModel = new DefaultTableModel(new Object[][]{}, header) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel) {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component returnComp = super.prepareRenderer(renderer, row, column);
                int rendererWidth = returnComp.getPreferredSize().width;
                TableColumn tableColumn = getColumnModel().getColumn(column);
                tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
                if (!returnComp.getBackground().equals(getSelectionBackground())) {
                    returnComp.setBackground((row % 2 == 0 ? new Color(88, 214, 141) : Color.WHITE));
                }
                return returnComp;
            }
        };

        tableHeader = table.getTableHeader();
        tableHeader.setBackground(new Color(39, 55, 70));
        tableHeader.setForeground(Color.WHITE);

        DesktopRender.resizeColumnWidth(table);

        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) table.getDefaultRenderer(Object.class);
        table.setDefaultRenderer(Object.class, renderer);
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.setFillsViewportHeight(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(new Font(FONT_FAMILY, Font.BOLD, 16));
        table.setFont(new Font(FONT_FAMILY, Font.PLAIN, 15));
        table.setRowHeight(25);
        table.setCursor(new Cursor(Cursor.HAND_CURSOR));
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setPreferredScrollableViewportSize(new Dimension(1150, 420));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        scrollPane = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(1150, 420));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        scrollPane.getViewport().setBackground(table.getBackground());

        TableRowSorter<TableModel> ts = new TableRowSorter<>(table.getModel());
        table.setRowSorter(ts);
        ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
        ts.setSortKeys(sortKeys);
        ts.sort();

        scrollPane = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(1150, 420));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        scrollPane.getViewport().setBackground(table.getBackground());

        clientPane.add(scrollPane, BorderLayout.CENTER);
    }


    void filterTable() {
        if (beginChooser.getDate() != null && endChooser.getDate() != null) {
            if (endChooser.getDate().compareTo(beginChooser.getDate()) > 0) {
                List<RowFilter<Object, Object>> filters;
                sorter = new TableRowSorter<>(table.getModel());
                table.setRowSorter(sorter);
                filters = getDatesBetween(beginChooser.getDate(), endChooser.getDate()).stream().map(date -> RowFilter.regexFilter((new SimpleDateFormat(DATE_FORMAT)).format(date.getTime()), 0)).collect(Collectors.toList());
                RowFilter<Object, Object> rf = RowFilter.orFilter(filters);
                sorter.setRowFilter(rf);
            } else {
                showMessageDialog(null, "Controllare le date di ricerca", "Info Dialog", JOptionPane.ERROR_MESSAGE);
                refreshTable();
            }
        } else
            showMessageDialog(null, "Inserire il range di ricerca", "Info Dialog", JOptionPane.ERROR_MESSAGE);
    }

    void refreshTable() {
        sorter = new TableRowSorter<>(table.getModel());
        beginChooser.setCalendar(null);
        endChooser.setCalendar(null);
        table.setRowSorter(sorter);
        sorter.setRowFilter(null);
    }


    public static List<Date> getDatesBetween(Date startDate, Date endDate) {

        List<Date> datesInRange = new ArrayList<>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);

        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(endDate);

        while (calendar.before(endCalendar)) {
            Date result = calendar.getTime();
            datesInRange.add(result);
            calendar.add(Calendar.DATE, 1);
        }
        return datesInRange;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
