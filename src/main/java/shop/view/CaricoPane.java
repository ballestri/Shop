package shop.view;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;
import shop.entity.Articolo;
import shop.entity.Carico;
import shop.utils.DesktopRender;
import shop.utils.RoundedPanel;
import shop.view.rilevazione.CaricoPaneUPD;
import shop.view.rilevazione.InfoCaricoPane;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static javax.swing.JOptionPane.showMessageDialog;
import static shop.dao.CaricoDAO.*;
import static shop.utils.DesktopRender.*;

public class CaricoPane extends AContainer implements ActionListener {

    public JButton btn_prima;

    // pannello interno
    private JPanel internPane, wrapperPane, clientPane;
    private RoundedPanel searchPane;
    //private static final Color JTF_COLOR = new Color(46, 134, 193);

    public static DefaultTableModel tableModel;
    JTableHeader tableHeader;
    public static JTable table;
    JScrollPane scrollPane;

    protected JButton btn_add, btn_update, btn_remove, btn_search, btn_refresh;
    public JDateChooser beginChooser, endChooser;
    private TableRowSorter<TableModel> sorter;

    // Pulsante di carica articolo
    private Font font;

    public CaricoPane() {
        initPanel();
    }

    public void initPanel() {

        font = new Font(FONT_FAMILY, Font.BOLD, 16);

        // I pulsanti delle funzionalita'
        internPane = new JPanel();
        wrapperPane = new JPanel();
        clientPane = new JPanel();
        searchPane = new RoundedPanel();
        initComponents();
        buildClientArea();
        container.setLayout(new BorderLayout());
    }

    public void initComponents() {
        internPane.setBounds(50, 110, 1200, 675);
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
        btn_search.setBorderPainted(false);

        btn_refresh = new JButton(new ImageIcon(requireNonNull(ClassLoader.getSystemClassLoader().getResource("images/refresh.png"))));
        btn_refresh.setPreferredSize(new Dimension(48, 48));
        btn_refresh.setContentAreaFilled(false);
        btn_refresh.setOpaque(false);
        btn_refresh.setBorderPainted(false);


        btn_add = new JButton(DesktopRender.formatButton("+ New"));
        btn_update = new JButton(DesktopRender.formatButton("Update"));
        btn_remove = new JButton(DesktopRender.formatButton("Remove"));

        formatButton(btn_add);
        formatButton(btn_update);
        formatButton(btn_remove);

        searchPane.add(lbl_begin, c);
        searchPane.add(beginChooser, c);
        searchPane.add(lbl_end, c);
        searchPane.add(endChooser, c);
        searchPane.add(btn_search, c);
        searchPane.add(btn_refresh, c);
        searchPane.add(btn_add, c);
        searchPane.add(btn_update, c);
        searchPane.add(btn_remove, c);

        wrapperPane.add(searchPane, BorderLayout.NORTH);
        wrapperPane.add(clientPane, BorderLayout.CENTER);

        btn_remove.addActionListener(e -> {
            deleteCarico();
            table.getSelectionModel().clearSelection();
        });
        btn_search.addActionListener(e -> filterTable());
        btn_refresh.addActionListener(e -> refreshTable());
    }

    void buildArticleDetails() {
        String[] header = {"UID", "Data Carico", "Codice Prodotto", "Descrizione", "Quantita'", "Importo", "Fornitore", "Data scadenza", "Note"};
        tableModel = new DefaultTableModel(new Object[][]{}, header) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        loadCarico().forEach(carico -> tableModel.addRow(new String[]{String.valueOf(carico.getUID()), (new SimpleDateFormat(DATE_FORMAT)).format(carico.getDatacarico()), carico.getArticolo().getCodice(), carico.getDescrizione(), String.valueOf(carico.getQuantita()), formatMoney(carico.getImporto()), carico.getFornitore(), (new SimpleDateFormat(DATE_FORMAT)).format(carico.getDatascadenza()), carico.getNote()}));

        table = new JTable(tableModel) {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component returnComp = super.prepareRenderer(renderer, row, column);
                int rendererWidth = returnComp.getPreferredSize().width;
                TableColumn tableColumn = getColumnModel().getColumn(column);
                tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
                if (!returnComp.getBackground().equals(getSelectionBackground())) {
                    returnComp.setBackground((row % 2 == 0 ? new Color(88, 214, 141) : Color.WHITE));
                }
                ((JLabel) returnComp).setHorizontalAlignment(JLabel.CENTER);
                if (column == 2 || column == 4|| column == 5)
                    returnComp.setFont(font);

                return returnComp;
            }
        };

        tableHeader = table.getTableHeader();
        tableHeader.setBackground(new Color(39, 55, 70));
        tableHeader.setForeground(Color.WHITE);

        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) table.getDefaultRenderer(Object.class);
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class,renderer);
        table.setFillsViewportHeight(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(new Font(FONT_FAMILY, Font.BOLD, 16));
        table.setFont(new Font(FONT_FAMILY, Font.PLAIN, 15));
        table.setRowHeight(25);
        table.setCursor(new Cursor(Cursor.HAND_CURSOR));
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setPreferredScrollableViewportSize(new Dimension(1150, 420));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setFocusable(false);
        DesktopRender.resizeColumnWidth(table);

        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(1).setMinWidth(150);
        table.getColumnModel().getColumn(2).setMinWidth(150);
        table.getColumnModel().getColumn(2).setMaxWidth(150);
        table.getColumnModel().getColumn(3).setMinWidth(260);
        table.getColumnModel().getColumn(4).setMinWidth(80);
        table.getColumnModel().getColumn(5).setMinWidth(220);
        table.getColumnModel().getColumn(6).setMinWidth(150);
        table.getColumnModel().getColumn(7).setMinWidth(220);

        scrollPane = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(1150, 420));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        scrollPane.getViewport().setBackground(table.getBackground());

        TableRowSorter<TableModel> ts = new TableRowSorter<>(table.getModel());
        table.setRowSorter(ts);
        ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(1, SortOrder.DESCENDING));
        ts.setSortKeys(sortKeys);
        ts.sort();

        clientPane.add(scrollPane, BorderLayout.CENTER);
    }

    void formatButton(JButton btn) {
        btn.setFont(font);
        btn.setForeground(Color.WHITE);
        btn.setBorder(new LineBorder(Color.BLACK));
        btn.setBackground(new Color(0, 128, 128));
        btn.setFocusPainted(false);
        btn.addActionListener(this);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(120, 48));
    }

    public Carico getSelectedCarico() {

        Carico carico = new Carico();
        if (table.getSelectedRow() >= 0) {
            int index = table.getSelectedRow();
            carico.setUID(Integer.valueOf(String.valueOf(table.getValueAt(index, 0))));
            try {
                carico.setDatacarico((new SimpleDateFormat(DATE_FORMAT)).parse(table.getValueAt(index, 1).toString()));
                carico.setDatascadenza((new SimpleDateFormat(DATE_FORMAT)).parse(table.getValueAt(index, 7).toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Articolo articolo= new Articolo();
            articolo.setCodice(String.valueOf(table.getValueAt(index, 2)));
            carico.setArticolo(articolo);
            carico.setDescrizione(String.valueOf(table.getValueAt(index, 3)));
            carico.setQuantita(Integer.valueOf(String.valueOf(table.getValueAt(index, 4))));

            carico.setFornitore(String.valueOf(table.getValueAt(index, 6)));
            carico.setNote(String.valueOf(table.getValueAt(index, 8)));
        }
        return carico;
    }

    void filterTable() {
        if (beginChooser.getDate() != null && endChooser.getDate() != null) {
            if (endChooser.getDate().compareTo(beginChooser.getDate()) > 0) {
                List<RowFilter<Object, Object>> filters;
                sorter = new TableRowSorter<>(table.getModel());
                table.setRowSorter(sorter);
                filters = getDatesBetween(beginChooser.getDate(), endChooser.getDate()).stream().map(date -> RowFilter.regexFilter((new SimpleDateFormat(DATE_FORMAT)).format(date.getTime()), 1)).collect(Collectors.toList());
                RowFilter<Object, Object> rf = RowFilter.orFilter(filters);
                sorter.setRowFilter(rf);
            } else {
                showMessageDialog(null, "Controllare le date di ricerca", "Info Dialog", JOptionPane.ERROR_MESSAGE);
                refreshTable();
            }
        } else
            showMessageDialog(null, "Inserire il range di ricerca", "Info Dialog", JOptionPane.ERROR_MESSAGE);
    }

    public void refreshTable() {
        sorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(sorter);
        ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(1, SortOrder.DESCENDING));
        sorter.setSortKeys(sortKeys);
        sorter.sort();
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
        if (e.getSource() == btn_prima) {
            container.removeAll();
            container.revalidate();
            container.add(new RilevazionePane().getPanel());
            container.repaint();
        } else if (e.getSource() == btn_add) {
            new InfoCaricoPane();
        } else if (e.getSource() == btn_update) {
            if (table.getSelectedRow() == -1) {
                showMessageDialog(null, "Selezionare un carico", "Info Dialog", JOptionPane.ERROR_MESSAGE);
            } else
                new CaricoPaneUPD(getSelectedCarico());
        }
    }
}
