package shop.view;


import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;
import shop.controller.ComboBoxFilterDecorator;
import shop.controller.CustomComboRenderer;
import shop.db.DBUtils;
import shop.model.Articolo;
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
import java.awt.event.ItemEvent;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


import static java.util.Objects.requireNonNull;
import static javax.swing.JOptionPane.showMessageDialog;
import static shop.utils.DesktopRender.FONT_FAMILY;

public class MovimentiPane extends AContainer implements ActionListener {

    private static final String DATE_FORMAT = "dd/MM/yyyy";

    // pannello interno
    protected JPanel internPane, wrapperPane, productPane, movimentPaneWrapper, movimentPane;
    protected RoundedPanel titleProductPane, infoProductPane, searchPane;

    protected JLabel lblCodice, lblDescrizione, lblCategoria, lblPosizione, lblUnita, lblPrezzo, lblScorta, lblProvenienza;
    public static JTextField jtfDescrizione, jtfCategoria, jtfPosizione, jtfUnita, jtfPrezzo, jtfScorta, jtfProvenienza;
    public static JComboBox<String> jcbCodice;

    public static DefaultTableModel tableModel;
    JTableHeader tableHeader;
    public static JTable table;
    JScrollPane scrollPane;

    protected JButton btn_search, btn_refresh;
    public static JDateChooser beginChooser, endChooser;
    private TableRowSorter<TableModel> sorter;

    // Pulsante di carica articolo
    private Font font;

    public MovimentiPane() {
        initPanel();
    }

    public void initPanel() {

        font = new Font(FONT_FAMILY, Font.BOLD, 16);
        ToolTipManager.sharedInstance().setInitialDelay(500);
        ToolTipManager.sharedInstance().setDismissDelay(4000);

        // I pulsanti della Toolbar
        RoundedPanel toolbar = new RoundedPanel();
        toolbar.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        JLabel lblFormName = new JLabel("Movimentazioni prodotti");
        lblFormName.setForeground(Color.WHITE);
        lblFormName.setFont(new Font("HelveticaNeue", Font.BOLD, 28));
        toolbar.setBackground(new Color(128, 0, 128));
        lblFormName.setPreferredSize(new Dimension(360, 40));
        toolbar.add(lblFormName, BorderLayout.CENTER);


        // I pulsanti delle funzionalita'
        internPane = new JPanel();
        wrapperPane = new JPanel();

        productPane = new JPanel();
        movimentPaneWrapper = new JPanel();

        movimentPane = new JPanel();
        searchPane = new RoundedPanel();

        initComponents();
        buildProductPanel();

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

    private void buildProductPanel() {

        wrapperPane.setBackground(new Color(39, 55, 70));
        Border line = BorderFactory.createLineBorder(Color.WHITE);
        Border empty = new EmptyBorder(20, 10, 20, 10);
        CompoundBorder border = new CompoundBorder(line, empty);
        wrapperPane.setBorder(border);

        wrapperPane.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        productPane.setBackground(wrapperPane.getBackground());
        productPane.setPreferredSize(new Dimension(450, 610));

        movimentPaneWrapper.setBackground(wrapperPane.getBackground());
        movimentPaneWrapper.setPreferredSize(new Dimension(650, 610));

        initProductPane();
        initMovimentPane();

        wrapperPane.add(productPane, BorderLayout.WEST);
        wrapperPane.add(movimentPaneWrapper, BorderLayout.CENTER);
    }

    void initProductPane() {

        productPane.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 15));
        titleProductPane = new RoundedPanel();
        infoProductPane = new RoundedPanel();

        titleProductPane.setPreferredSize(new Dimension(450, 70));
        infoProductPane.setPreferredSize(new Dimension(450, 500));


        titleProductPane.setLayout(new GridBagLayout());
        JLabel lblFormName = new JLabel("Prodotto in magazzino");
        lblFormName.setFont(new Font("HelveticaNeue", Font.BOLD, 18));
        titleProductPane.add(lblFormName);
        productPane.add(titleProductPane);

        // Parte informativa
        lblCodice = new JLabel("Codice");
        lblCodice.setFont(font);


        ArrayList<String> items = DBUtils.getListCodici();
        items.add(0, null);
        jcbCodice = new JComboBox<>(items.toArray(new String[0]));
        ComboBoxFilterDecorator<String> decorate = ComboBoxFilterDecorator.decorate(jcbCodice, MovimentiPane::codiceFilter);
        jcbCodice.setRenderer(new CustomComboRenderer(decorate.getFilterLabel()));
        jcbCodice.setBorder(new LineBorder(Color.BLACK));
        jcbCodice.setFont(font);
        jcbCodice.addActionListener(this);

        lblDescrizione = new JLabel("Descrizione");
        lblDescrizione.setFont(font);

        jtfDescrizione = new JTextField(18);
        jtfDescrizione.setCaretColor(Color.BLACK);
        jtfDescrizione.setBackground(DesktopRender.JTF_COLOR);
        jtfDescrizione.setBorder(new LineBorder(Color.BLACK));
        jtfDescrizione.setFont(font);

        lblCategoria = new JLabel("Categoria");
        lblCategoria.setFont(font);

        jtfCategoria = new JTextField(18);
        jtfCategoria.setCaretColor(Color.BLACK);
        jtfCategoria.setBackground(DesktopRender.JTF_COLOR);
        jtfCategoria.setBorder(new LineBorder(Color.BLACK));
        jtfCategoria.setFont(font);

        lblPosizione = new JLabel("Posizione");
        lblPosizione.setFont(font);

        jtfPosizione = new JTextField(18);
        jtfPosizione.setCaretColor(Color.BLACK);
        jtfPosizione.setBackground(DesktopRender.JTF_COLOR);
        jtfPosizione.setBorder(new LineBorder(Color.BLACK));
        jtfPosizione.setFont(font);


        lblUnita = new JLabel("Unita' di misura");
        lblUnita.setFont(font);

        jtfUnita = new JTextField(18);
        jtfUnita.setCaretColor(Color.BLACK);
        jtfUnita.setBackground(DesktopRender.JTF_COLOR);
        jtfUnita.setBorder(new LineBorder(Color.BLACK));
        jtfUnita.setFont(font);


        lblPrezzo = new JLabel("Prezzo");
        lblPrezzo.setFont(font);

        jtfPrezzo = new JTextField();
        jtfPrezzo.setCaretColor(Color.BLACK);
        jtfPrezzo.setBackground(DesktopRender.JTF_COLOR);
        jtfPrezzo.setBorder(new LineBorder(Color.BLACK));
        jtfPrezzo.setFont(font);
        jtfPrezzo.setPreferredSize(new Dimension(120, 25));

        lblScorta = new JLabel("Scorta");
        lblScorta.setFont(font);

        jtfScorta = new JTextField();
        jtfScorta.setCaretColor(Color.BLACK);
        jtfScorta.setBackground(DesktopRender.JTF_COLOR);
        jtfScorta.setBorder(new LineBorder(Color.BLACK));
        jtfScorta.setFont(font);
        jtfScorta.setPreferredSize(new Dimension(120, 25));


        lblProvenienza = new JLabel("Provenienza");
        lblProvenienza.setFont(font);

        jtfProvenienza = new JTextField(18);
        jtfProvenienza.setCaretColor(Color.BLACK);
        jtfProvenienza.setBackground(DesktopRender.JTF_COLOR);
        jtfProvenienza.setBorder(new LineBorder(Color.BLACK));
        jtfProvenienza.setFont(font);

        infoProductPane.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();


        // first column
        gc.anchor = GridBagConstraints.EAST;
        gc.weightx = 1;
        gc.weighty = 1;

        gc.gridx = 0;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(2, 10, 2, 10);
        infoProductPane.add(lblCodice, gc);

        gc.gridx = 0;
        gc.gridy = 1;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(2, 10, 2, 10);
        infoProductPane.add(lblDescrizione, gc);

        gc.gridx = 0;
        gc.gridy = 2;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(2, 10, 2, 10);
        infoProductPane.add(lblCategoria, gc);

        gc.gridx = 0;
        gc.gridy = 3;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(2, 10, 2, 10);
        infoProductPane.add(lblPosizione, gc);

        gc.gridx = 0;
        gc.gridy = 4;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(2, 10, 2, 10);
        infoProductPane.add(lblUnita, gc);

        gc.gridx = 0;
        gc.gridy = 5;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(2, 10, 2, 10);
        infoProductPane.add(lblPrezzo, gc);


        gc.gridx = 0;
        gc.gridy = 6;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(2, 10, 2, 10);
        infoProductPane.add(lblScorta, gc);

        gc.gridx = 0;
        gc.gridy = 7;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(2, 10, 2, 10);
        infoProductPane.add(lblProvenienza, gc);


        // second column//
        gc.anchor = GridBagConstraints.WEST;
        gc.gridx = 1;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.LINE_START;
        infoProductPane.add(jcbCodice, gc);

        gc.gridx = 1;
        gc.gridy = 1;

        gc.anchor = GridBagConstraints.LINE_START;
        infoProductPane.add(jtfDescrizione, gc);

        gc.gridx = 1;
        gc.gridy = 2;

        gc.anchor = GridBagConstraints.LINE_START;
        infoProductPane.add(jtfCategoria, gc);

        gc.gridx = 1;
        gc.gridy = 3;

        gc.anchor = GridBagConstraints.LINE_START;
        infoProductPane.add(jtfPosizione, gc);

        gc.gridx = 1;
        gc.gridy = 4;

        gc.anchor = GridBagConstraints.LINE_START;
        infoProductPane.add(jtfUnita, gc);

        gc.gridx = 1;
        gc.gridy = 5;

        gc.anchor = GridBagConstraints.LINE_START;
        infoProductPane.add(jtfPrezzo, gc);


        gc.gridx = 1;
        gc.gridy = 6;

        gc.anchor = GridBagConstraints.LINE_START;
        infoProductPane.add(jtfScorta, gc);


        gc.gridx = 1;
        gc.gridy = 7;

        gc.anchor = GridBagConstraints.LINE_START;
        infoProductPane.add(jtfProvenienza, gc);

        productPane.add(infoProductPane);
    }

    void initMovimentPane() {

        movimentPaneWrapper.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 15));
        movimentPaneWrapper.setBackground(wrapperPane.getBackground());


        searchPane.setPreferredSize(new Dimension(650, 70));
        searchPane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 10, 3, 10);

        JLabel lbl_begin = new JLabel("Seleziona date: dal");
        lbl_begin.setFont(new Font(FONT_FAMILY, Font.BOLD, 16));

        beginChooser = new JDateChooser();
        beginChooser.setDateFormatString(DATE_FORMAT);
        beginChooser.setPreferredSize(new Dimension(110, 48));
        beginChooser.setFont(new Font(FONT_FAMILY, Font.BOLD, 16));
        beginChooser.setMaxSelectableDate(new Date());
        JTextFieldDateEditor dateEditor = (JTextFieldDateEditor) beginChooser.getComponent(1);
        dateEditor.setHorizontalAlignment(JTextField.RIGHT);
        dateEditor.setFont(font);
        dateEditor.setBackground(DesktopRender.JTF_COLOR);
        dateEditor.setBorder(new LineBorder(Color.BLACK));

        JLabel lbl_end = new JLabel("al");
        lbl_end.setFont(new Font(FONT_FAMILY, Font.BOLD, 16));

        endChooser = new JDateChooser();
        endChooser.setDateFormatString(DATE_FORMAT);
        endChooser.setPreferredSize(new Dimension(110, 48));
        endChooser.setFont(new Font(FONT_FAMILY, Font.BOLD, 16));
        endChooser.setMaxSelectableDate(new Date());
        JTextFieldDateEditor dt_editor = (JTextFieldDateEditor) endChooser.getComponent(1);
        dt_editor.setHorizontalAlignment(JTextField.RIGHT);
        dt_editor.setFont(font);
        dt_editor.setBackground(DesktopRender.JTF_COLOR);
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
        searchPane.add(endChooser, c);
        searchPane.add(btn_search, c);
        searchPane.add(btn_refresh, c);

        movimentPane.setPreferredSize(new Dimension(650, 500));
        buildProductDetails();

        movimentPaneWrapper.add(searchPane);
        movimentPaneWrapper.add(movimentPane);

        jcbCodice.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                Articolo articolo = DBUtils.getProduct(String.valueOf(jcbCodice.getSelectedItem()));
                jtfDescrizione.setText(articolo.getDescrizione());
                jtfCategoria.setText(articolo.getCategoria());
                jtfPosizione.setText(articolo.getPosizione());
                jtfUnita.setText(articolo.getUnita());
                jtfPrezzo.setText(String.valueOf(articolo.getPrezzo()).concat(" €"));
                jtfScorta.setText(String.valueOf(articolo.getScorta()));
                jtfProvenienza.setText(articolo.getProvenienza());
                jtfDescrizione.setEditable(false);
                jtfCategoria.setEditable(false);
                jtfPosizione.setEditable(false);
                jtfUnita.setEditable(false);
                jtfPrezzo.setEditable(false);
                jtfScorta.setEditable(false);
                jtfProvenienza.setEditable(false);
                tableModel.getDataVector().removeAllElements();
                tableModel.fireTableDataChanged();
                loadMovimenti(String.valueOf(jcbCodice.getSelectedItem()));
            }
        });

        btn_search.addActionListener(e -> filterTable());
        btn_refresh.addActionListener(e -> refreshTable());
    }

    void buildProductDetails() {
        String[] header = {"Data", "Carico", "Scarico", "Fornitore/Cliente"};
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

        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) table.getDefaultRenderer(Object.class);
        table.setDefaultRenderer(Object.class, renderer);
        tableHeader = table.getTableHeader();
        tableHeader.setBackground(new Color(39, 55, 70));
        tableHeader.setForeground(Color.WHITE);
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.setFillsViewportHeight(true);
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(new Font(FONT_FAMILY, Font.BOLD, 16));
        table.setFont(new Font(FONT_FAMILY, Font.PLAIN, 15));
        table.setRowHeight(25);
        table.setCursor(new Cursor(Cursor.HAND_CURSOR));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setPreferredScrollableViewportSize(new Dimension(650, 500));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setMinWidth(220);
        table.getColumnModel().getColumn(1).setMinWidth(105);
        table.getColumnModel().getColumn(2).setMinWidth(105);
        table.getColumnModel().getColumn(3).setMinWidth(220);
        table.setAutoCreateRowSorter(true);

        scrollPane = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(650, 500));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        scrollPane.setViewportView(table);
        scrollPane.getViewport().setBackground(table.getBackground());

        TableRowSorter<TableModel> ts = new TableRowSorter<>(table.getModel());
        table.setRowSorter(ts);
        ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
        ts.setSortKeys(sortKeys);
        ts.sort();

        scrollPane = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(650, 500));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        scrollPane.getViewport().setBackground(table.getBackground());

        movimentPane.add(scrollPane, BorderLayout.CENTER);
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


    public void refreshTable() {
        sorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(sorter);
        ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
        sorter.setSortKeys(sortKeys);
        sorter.sort();
        beginChooser.setCalendar(null);
        endChooser.setCalendar(null);
        table.setRowSorter(sorter);
        sorter.setRowFilter(null);
    }

    private static boolean codiceFilter(String codice, String textToFilter) {
        if (textToFilter.isEmpty()) {
            return true;
        }
        return CustomComboRenderer.getProcessDisplayText(codice).toLowerCase().contains(textToFilter.toLowerCase());
    }

    void loadMovimenti(String codice) {
        if (!codice.isEmpty()) {
            DBUtils.getAllMoviments(codice).forEach(ps -> tableModel.addRow(new String[]{(new SimpleDateFormat(DesktopRender.DATE_FORMAT)).format(ps.getData()), String.valueOf(ps.getCarico()), String.valueOf(ps.getScarico()), ps.getFornitore()}));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }


}


