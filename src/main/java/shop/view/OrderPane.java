package shop.view;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;
import shop.controller.article.RendererHighlighted;
import shop.controller.article.RowFilterUtil;
import shop.entity.Articolo;
import shop.entity.Categoria;
import shop.entity.Posizione;
import shop.entity.Unita;
import shop.utils.ComboRenderer;
import shop.utils.DesktopRender;
import shop.utils.RoundedPanel;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.Objects.requireNonNull;
import static javax.swing.JOptionPane.showMessageDialog;
import static shop.dao.CategoriaDAO.getAllCategories;
import static shop.dao.OrderDAO.getAllOrderCount;
import static shop.dao.OrderDAO.insertOrder;
import static shop.dao.PositionDAO.getAllPosizione;
import static shop.dao.UnitDAO.getAllUnita;
import static shop.utils.DesktopRender.*;
import static shop.view.GestionePane.tableModelArticolo;
import static shop.view.GestionePane.tableModelCliente;

public class OrderPane extends AContainer implements ActionListener {

    // pannello interno
    protected JPanel internPane, wrapperPane, clientePanel, clientPaneWrapper, movimentPane, orderInfoPane, orderPane,
            infoClientePane, orderPanewrapper, articoloPaneWrapper, articoloOrderWrapper;
    protected RoundedPanel roundedPanel, titleClientePane, filterPane, orderButtonPane;
    private JTextField filterCustomer;
    private JTextField jtfClientID, jtfOrderID;
    private static JTextField jtfOrderImporto;
    public static JDateChooser dataOrder;
    public static DefaultTableModel orderTableModel;
    public JTable table, articoloTable;
    public static JTable orderTable;
    JScrollPane scrollPane, articoloScrollPane, orderScrollPane;
    protected JToolBar toolbar;
    protected JButton btn_filter, btn_refresh, btn_refresh_cliente, btn_cliente_logo, btn_insert, btn_all, btn_remove;
    protected JLabel lblCategoria;
    protected JComboBox<String> jcbCategoria, jcbPosizione, jcbUnita;
    private TableRowSorter<TableModel> sorter;

    // Pulsante di carica articolo
    private static Font font;

    public OrderPane() {
        initPanel();
    }

    public void initPanel() {

        font = new Font(FONT_FAMILY, Font.BOLD, 16);
        internPane = new JPanel();
        wrapperPane = new JPanel();
        clientePanel = new JPanel();
        movimentPane = new JPanel();
        orderPane = new JPanel();
        initComponents();
        container.setLayout(new BorderLayout());
    }

    public void initComponents() {

        roundedPanel = new RoundedPanel();
        roundedPanel.setLayout(new GridBagLayout());
        JLabel lblFormName = new JLabel("Orders Details");
        lblFormName.setForeground(Color.WHITE);
        lblFormName.setFont(new Font(FONT_FAMILY, Font.BOLD, 28));
        roundedPanel.setBackground(new Color(128, 0, 128));
        roundedPanel.setPreferredSize(new Dimension(1200, 60));
        roundedPanel.add(lblFormName);
        internPane.setBounds(55, 20, 1200, 850);
        wrapperPane.setPreferredSize(new Dimension(1200, 750));
        internPane.setBackground(container.getBackground());
        internPane.setLayout(new BorderLayout());
        buildWrapper();
        internPane.add(roundedPanel, BorderLayout.NORTH);
        internPane.add(wrapperPane, BorderLayout.CENTER);
        container.add(internPane);
    }

    private void buildWrapper() {

        wrapperPane.setBackground(new Color(39, 55, 70));
        Border line = BorderFactory.createLineBorder(Color.WHITE);
        Border empty = new EmptyBorder(20, 20, 20, 20);
        CompoundBorder border = new CompoundBorder(line, empty);
        wrapperPane.setBorder(border);

        JPanel clientPanel = new JPanel();
        JPanel orderPanel = new JPanel();

        clientPanel.setPreferredSize(new Dimension(1100, 330));
        orderPanel.setPreferredSize(new Dimension(1100, 400));

        clientPanel.setBackground(wrapperPane.getBackground());
        orderPanel.setBackground(wrapperPane.getBackground());

        // Informazioni sul cliente
        clientPaneWrapper = new JPanel();
        orderPanewrapper = new JPanel();

        clientPaneWrapper.setPreferredSize(new Dimension(550, 330));
        orderPanewrapper.setPreferredSize(new Dimension(550, 400));
        clientPaneWrapper.setBackground(wrapperPane.getBackground());
        orderPanewrapper.setBackground(wrapperPane.getBackground());

        buildClientWrapper();
        buildOrderWrapper();

        clientPanel.setLayout(new BorderLayout());
        clientPanel.add(clientPaneWrapper, BorderLayout.WEST);
        clientPanel.add(orderPanewrapper, BorderLayout.EAST);

        // Informazioni sul prodotto

        articoloPaneWrapper = new JPanel();
        articoloOrderWrapper = new JPanel();

        articoloPaneWrapper.setPreferredSize(new Dimension(550, 330));
        articoloOrderWrapper.setPreferredSize(new Dimension(550, 400));
        articoloPaneWrapper.setBackground(wrapperPane.getBackground());
        articoloOrderWrapper.setBackground(wrapperPane.getBackground());

        JButton btnSelected = new JButton(new ImageIcon(requireNonNull(ClassLoader.getSystemClassLoader().getResource("images/dopo.png"))));

        btnSelected.setPreferredSize(new Dimension(48, 48));
        btnSelected.setContentAreaFilled(false);
        btnSelected.setOpaque(false);
        btnSelected.setBorderPainted(false);
        btnSelected.setFocusPainted(false);

        articoloPaneWrapper.setLayout(new BorderLayout());
        filterPane = new RoundedPanel();
        filterPane.setBackground(roundedPanel.getBackground());

        buildFilterPane();
        buildTableArticoloWrapper();

        articoloOrderWrapper.setLayout(new BorderLayout());

        buildTableArticoloWrapper();
        makeButtonPane();
        buildTableOrderWrapper();

        orderPanel.setLayout(new BorderLayout());
        orderPanel.add(articoloPaneWrapper, BorderLayout.WEST);
        orderPanel.add(btnSelected);
        orderPanel.add(articoloOrderWrapper, BorderLayout.EAST);

        wrapperPane.setLayout(new BorderLayout());
        wrapperPane.add(clientPanel, BorderLayout.NORTH);
        wrapperPane.add(orderPanel, BorderLayout.SOUTH);

        btnSelected.addActionListener(e -> selectedArticle());
    }

    void buildClientWrapper() {

        titleClientePane = new RoundedPanel();
        infoClientePane = new JPanel();

        titleClientePane.setPreferredSize(new Dimension(525, 60));
        infoClientePane.setPreferredSize(new Dimension(525, 260));

        buildTableCustomerWrapper();

        titleClientePane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
        btn_cliente_logo = new JButton(new ImageIcon(requireNonNull(ClassLoader.getSystemClassLoader().getResource("images/cliente_32.png"))));
        btn_cliente_logo.setPreferredSize(new Dimension(48, 48));
        btn_cliente_logo.setContentAreaFilled(false);
        btn_cliente_logo.setOpaque(false);
        btn_cliente_logo.setBorderPainted(false);

        btn_refresh_cliente = new JButton(new ImageIcon(requireNonNull(ClassLoader.getSystemClassLoader().getResource("images/refresh_32.png"))));
        btn_refresh_cliente.setPreferredSize(new Dimension(32, 32));
        btn_refresh_cliente.setContentAreaFilled(false);
        btn_refresh_cliente.setOpaque(false);
        btn_refresh_cliente.setFocusPainted(true);
        btn_refresh_cliente.setBorderPainted(false);

        JLabel lblFormName = new JLabel("Ricerca");
        lblFormName.setFont(new Font(FONT_FAMILY, Font.BOLD, 18));
        titleClientePane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        filterCustomer.setBackground(DesktopRender.JTF_COLOR);
        filterCustomer.setFont(font);
        filterCustomer.setBorder(new LineBorder(Color.BLACK));

        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1;
        c.weighty = 1;

        c.gridx = 0;
        c.gridy = 0;

        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(2, 20, 2, 0);
        titleClientePane.add(btn_cliente_logo, c);

        c.anchor = GridBagConstraints.LINE_START;
        c.gridx = 1;
        c.gridy = 0;

        c.insets = new Insets(2, 0, 2, 0);
        titleClientePane.add(lblFormName, c);


        c.anchor = GridBagConstraints.LINE_START;
        c.gridx = 2;
        c.gridy = 0;

        c.insets = new Insets(2, 0, 2, 0);
        titleClientePane.add(filterCustomer, c);


        c.anchor = GridBagConstraints.LINE_START;
        c.gridx = 3;
        c.gridy = 0;

        c.insets = new Insets(2, 5, 2, 0);
        titleClientePane.add(btn_refresh_cliente, c);

        btn_refresh_cliente.addActionListener(e -> {
            table.getSelectionModel().clearSelection();
            filterCustomer.setText(null);
            jtfOrderID.setText(null);
            jtfClientID.setText(null);
        });

        clientPaneWrapper.add(titleClientePane, BorderLayout.NORTH);
        clientPaneWrapper.add(scrollPane, BorderLayout.CENTER);
    }

    void buildFilterPane() {
        lblCategoria = new JLabel("Categoria");
        lblCategoria.setFont(new Font(FONT_FAMILY, Font.BOLD, 16));
        lblCategoria.setForeground(Color.WHITE);

        // Categoria
        jcbCategoria = new JComboBox<>(getAllCategories().toArray(new String[0]));
        jcbCategoria.insertItemAt("Categoria", 0);
        jcbCategoria.setSelectedIndex(0);
        jcbCategoria.setFocusable(false);
        jcbCategoria.setBorder(new LineBorder(Color.BLACK));
        jcbCategoria.setRenderer(new ComboRenderer());
        jcbCategoria.setFont(font);
        jcbCategoria.addActionListener(this);

        // Posizione
        jcbPosizione = new JComboBox<>(getAllPosizione().toArray(new String[0]));
        jcbPosizione.insertItemAt("Posizione", 0);
        jcbPosizione.setSelectedIndex(0);
        jcbPosizione.setFocusable(false);
        jcbPosizione.setBorder(new LineBorder(Color.BLACK));
        jcbPosizione.setRenderer(new ComboRenderer());
        jcbPosizione.setFont(font);
        jcbPosizione.addActionListener(this);

        // Unita
        jcbUnita = new JComboBox<>(getAllUnita().toArray(new String[0]));
        jcbUnita.insertItemAt("Unita", 0);
        jcbUnita.setSelectedIndex(0);
        jcbUnita.setFocusable(false);
        jcbUnita.setBorder(new LineBorder(Color.BLACK));
        jcbUnita.setRenderer(new ComboRenderer());
        jcbUnita.setFont(font);
        jcbUnita.addActionListener(this);

        // filter
        btn_filter = new JButton(new ImageIcon(requireNonNull(ClassLoader.getSystemClassLoader().getResource("images/search_32.png"))));
        btn_filter.setPreferredSize(new Dimension(32, 32));
        btn_filter.setContentAreaFilled(false);
        btn_filter.setOpaque(false);
        btn_filter.setFocusPainted(true);
        btn_filter.setBorderPainted(false);

        btn_refresh = new JButton(new ImageIcon(requireNonNull(ClassLoader.getSystemClassLoader().getResource("images/refresh_32.png"))));
        btn_refresh.setPreferredSize(new Dimension(32, 32));
        btn_refresh.setContentAreaFilled(false);
        btn_refresh.setOpaque(false);
        btn_refresh.setFocusPainted(true);
        btn_refresh.setBorderPainted(false);

        filterPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
        filterPane.add(btn_refresh);
        filterPane.add(jcbCategoria);
        filterPane.add(jcbPosizione);
        filterPane.add(jcbUnita);
        filterPane.add(btn_filter);

        btn_filter.addActionListener(e -> filterTable());
        btn_refresh.addActionListener(e -> refreshTable());

        articoloPaneWrapper.add(filterPane, BorderLayout.NORTH);
    }


    void formatButton(JButton btn) {
        btn.setFont(new Font(FONT_FAMILY, Font.BOLD, 15));
        btn.setForeground(Color.WHITE);
        btn.setBorder(new LineBorder(Color.WHITE));
        btn.setBackground(new Color(0, 128, 128));
        btn.setFocusPainted(false);
        btn.addActionListener(this);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(130, 35));
    }

    void buildTableCustomerWrapper() {


        table = new JTable(tableModelCliente) {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component returnComp = super.prepareRenderer(renderer, row, column);
                int rendererWidth = returnComp.getPreferredSize().width;
                TableColumn tableColumn = getColumnModel().getColumn(column);
                tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
                if (!returnComp.getBackground().equals(getSelectionBackground())) {
                    returnComp.setBackground((row % 2 == 0 ? new Color(88, 214, 141) : Color.WHITE));
                }
                if (column == 1)
                    returnComp.setFont(font);
                return returnComp;
            }
        };

        filterCustomer = RowFilterUtil.createRowFilter(table);
        filterCustomer.setColumns(18);
        RendererHighlighted renderer = new RendererHighlighted(filterCustomer);
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class, renderer);
        table.getTableHeader().setBackground(new Color(39, 55, 70));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setFillsViewportHeight(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(new Font(FONT_FAMILY, Font.BOLD, 16));
        table.setFont(new Font(FONT_FAMILY, Font.PLAIN, 15));
        table.getTableHeader().setEnabled(false);
        table.setRowHeight(25);
        table.setCursor(new Cursor(Cursor.HAND_CURSOR));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setPreferredScrollableViewportSize(new Dimension(525, 260));
        table.getSelectionModel().addListSelectionListener(e -> getClientID());
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(1).setMinWidth(120);
        table.getColumnModel().getColumn(1).setMaxWidth(120);
        table.getColumnModel().getColumn(2).setMinWidth(120);
        table.getColumnModel().getColumn(3).setMinWidth(130);
        table.getColumnModel().getColumn(4).setMinWidth(130);
        table.getColumnModel().getColumn(5).setMinWidth(220);
        table.setFocusable(false);
        table.setBorder(BorderFactory.createEmptyBorder());

        scrollPane = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(525, 260));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        scrollPane.getViewport().setBackground(table.getBackground());
    }

    void buildTableArticoloWrapper() {

        articoloTable = new JTable(tableModelArticolo) {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component returnComp = super.prepareRenderer(renderer, row, column);
                int rendererWidth = returnComp.getPreferredSize().width;
                TableColumn tableColumn = getColumnModel().getColumn(column);
                tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
                if (!returnComp.getBackground().equals(getSelectionBackground())) {
                    returnComp.setBackground((row % 2 == 0 ? new Color(88, 214, 141) : Color.WHITE));
                }
                if (column == 1)
                    returnComp.setFont(font);
                return returnComp;
            }
        };


        /*
        loadArticle().forEach(article -> tableModelArticolo.addRow(new String[]{String.valueOf(article.getUID()), String.valueOf(article.getCodice()), article.getDescrizione(), article.getCategoria().getCategoria(),
                article.getPosizione().getPosizione(), article.getUnita().getUnita(), formatMoney(article.getPrezzo()),
                String.valueOf(article.getScorta()), article.getProvenienza(), (new SimpleDateFormat(DATE_FORMAT)).format(article.getDataIns())}));
        */


        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) articoloTable.getDefaultRenderer(Object.class);
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        articoloTable.setDefaultRenderer(Object.class, renderer);
        articoloTable.getTableHeader().setBackground(new Color(39, 55, 70));
        articoloTable.getTableHeader().setForeground(Color.WHITE);
        articoloTable.setFillsViewportHeight(true);
        articoloTable.getTableHeader().setReorderingAllowed(false);
        articoloTable.getTableHeader().setFont(new Font(FONT_FAMILY, Font.BOLD, 16));
        articoloTable.getTableHeader().setEnabled(false);
        articoloTable.setFont(new Font(FONT_FAMILY, Font.PLAIN, 15));
        articoloTable.setRowHeight(25);
        articoloTable.setCursor(new Cursor(Cursor.HAND_CURSOR));
        articoloTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        articoloTable.setPreferredScrollableViewportSize(new Dimension(525, 260));
        articoloTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        articoloTable.getColumnModel().getColumn(0).setMinWidth(0);
        articoloTable.getColumnModel().getColumn(0).setMaxWidth(0);
        articoloTable.getColumnModel().getColumn(1).setMinWidth(120);
        articoloTable.getColumnModel().getColumn(1).setMaxWidth(120);
        articoloTable.getColumnModel().getColumn(2).setMinWidth(120);
        articoloTable.getColumnModel().getColumn(2).setMaxWidth(120);
        articoloTable.getColumnModel().getColumn(3).setMinWidth(120);
        articoloTable.getColumnModel().getColumn(3).setMaxWidth(120);
        articoloTable.getColumnModel().getColumn(4).setMinWidth(120);
        articoloTable.getColumnModel().getColumn(4).setMaxWidth(120);
        articoloTable.getColumnModel().getColumn(6).setMinWidth(120);
        articoloTable.getColumnModel().getColumn(6).setMaxWidth(120);
        articoloTable.getColumnModel().getColumn(8).setMinWidth(155);
        articoloTable.getColumnModel().getColumn(8).setMaxWidth(155);
        articoloTable.getColumnModel().getColumn(9).setMinWidth(155);
        articoloTable.getColumnModel().getColumn(9).setMaxWidth(155);
        articoloTable.setAutoCreateRowSorter(false);
        articoloTable.setBorder(BorderFactory.createEmptyBorder());
        articoloTable.setAutoCreateRowSorter(false);
        articoloTable.setFocusable(false);

        articoloScrollPane = new JScrollPane(articoloTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        articoloScrollPane.setPreferredSize(new Dimension(525, 260));
        articoloScrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        articoloScrollPane.getViewport().setBackground(articoloTable.getBackground());
        articoloScrollPane.setBorder(new EmptyBorder(20, 0, 0, 0));
        articoloScrollPane.setBackground(wrapperPane.getBackground());

        /*
        TableRowSorter<TableModel> ts = new TableRowSorter<>(articoloTable.getModel());
        articoloTable.setRowSorter(ts);
        ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(1, SortOrder.DESCENDING));

        ts.setSortKeys(sortKeys);
        ts.sort();

         */

        articoloPaneWrapper.add(articoloScrollPane, BorderLayout.CENTER);
    }

    void buildTableOrderWrapper() {

        String[] header = {"Articolo ID", "Descrizione", "Prezzo", "Quantita", "Importo"};

        orderTableModel = new DefaultTableModel(new Object[][]{}, header);
        orderTable = new JTable(orderTableModel) {
            @Override
            public void setValueAt(Object aValue, int row, int column) {
                super.setValueAt(aValue, row, column);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }

            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component returnComp = super.prepareRenderer(renderer, row, column);
                int rendererWidth = returnComp.getPreferredSize().width;
                TableColumn tableColumn = getColumnModel().getColumn(column);
                tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
                if (!returnComp.getBackground().equals(getSelectionBackground())) {
                    returnComp.setBackground((row % 2 == 0 ? new Color(88, 214, 141) : Color.WHITE));
                }
                if (column == 0 || column == 4)
                    returnComp.setFont(font);
                return returnComp;
            }
        };


        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) orderTable.getDefaultRenderer(Object.class);
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        orderTable.setDefaultRenderer(Object.class, renderer);
        orderTable.getTableHeader().setBackground(new Color(39, 55, 70));
        orderTable.getTableHeader().setForeground(Color.WHITE);
        orderTable.setFillsViewportHeight(true);
        orderTable.getTableHeader().setReorderingAllowed(false);
        orderTable.getTableHeader().setFont(new Font(FONT_FAMILY, Font.BOLD, 16));
        orderTable.setFont(new Font(FONT_FAMILY, Font.PLAIN, 15));
        orderTable.setRowHeight(30);
        orderTable.setCursor(new Cursor(Cursor.HAND_CURSOR));
        orderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        orderTable.setPreferredScrollableViewportSize(new Dimension(525, 320));
        orderTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        orderTable.setCellSelectionEnabled(true);
        orderTable.setFocusable(true);

        TableColumnModel colModel = orderTable.getColumnModel();
        colModel.getColumn(0).setMinWidth(135);
        colModel.getColumn(1).setMinWidth(140);
        colModel.getColumn(2).setMinWidth(100);
        colModel.getColumn(2).setMaxWidth(100);
        colModel.getColumn(3).setMinWidth(100);
        colModel.getColumn(3).setMinWidth(100);

        orderTable.setBorder(BorderFactory.createEmptyBorder());
        colModel.getColumn(3).setCellEditor(new SpinnerEditor());

        orderScrollPane = new JScrollPane(orderTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        orderScrollPane.setPreferredSize(new Dimension(525, 320));
        orderScrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        orderScrollPane.getViewport().setBackground(orderTable.getBackground());
        articoloOrderWrapper.add(orderScrollPane, BorderLayout.NORTH);
    }

    void buildOrderWrapper() {

        orderInfoPane = new JPanel();
        orderButtonPane = new RoundedPanel();
        Border line = BorderFactory.createLineBorder(Color.WHITE);
        Border empty = new EmptyBorder(5, 5, 5, 5);
        CompoundBorder border = new CompoundBorder(line, empty);
        orderInfoPane.setBorder(border);
        orderInfoPane.setPreferredSize(new Dimension(550, 320));
        orderInfoPane.setBackground(wrapperPane.getBackground());
        orderButtonPane.setPreferredSize(new Dimension(550, 60));
        orderButtonPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        orderButtonPane.setBackground(roundedPanel.getBackground());
        makeOrderInfoPane();
        orderPanewrapper.add(orderInfoPane, BorderLayout.NORTH);
    }

    void makeButtonPane() {

        wrapperPane.setBackground(new Color(39, 55, 70));
        Border line = BorderFactory.createLineBorder(Color.WHITE);
        Border empty = new EmptyBorder(5, 5, 5, 5);
        CompoundBorder border = new CompoundBorder(line, empty);
        btn_insert = new JButton(DesktopRender.formatButton("Insert Order"));
        btn_remove = new JButton(DesktopRender.formatButton("Remove product"));
        jtfOrderImporto = new JTextField();
        jtfOrderImporto.setFont(font);
        jtfOrderImporto.setBackground(new Color(39, 55, 70));
        jtfOrderImporto.setForeground(Color.WHITE);
        jtfOrderImporto.setEditable(false);
        jtfOrderImporto.setPreferredSize(new Dimension(180, 35));
        jtfOrderImporto.setBorder(border);
        jtfOrderImporto.setHorizontalAlignment(JTextField.CENTER);
        orderButtonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        jtfOrderImporto.setText("Totale: ".concat(formatMoney(0.0)));

        formatButton(btn_insert);
        formatButton(btn_remove);

        orderButtonPane.add(btn_remove);
        orderButtonPane.add(btn_insert);
        orderButtonPane.add(jtfOrderImporto);
        articoloOrderWrapper.add(orderButtonPane, BorderLayout.SOUTH);

        btn_insert.addActionListener(e -> {
            if (jtfClientID.getText().isEmpty() || jtfOrderID.getText().isEmpty()) {
                showMessageDialog(null, "Selezionare un cliente", "Info Dialog", JOptionPane.ERROR_MESSAGE);
            } else
                insertOrder(jtfOrderID.getText(), jtfClientID.getText());
        });
        btn_remove.addActionListener(e -> removeArticleOrder());
    }

    void makeOrderInfoPane() {

        JLabel lblClienteID = new JLabel("Cliente ID");
        lblClienteID.setFont(new Font(FONT_FAMILY, Font.BOLD, 18));
        lblClienteID.setForeground(Color.WHITE);

        jtfClientID = new JTextField();
        jtfClientID.setPreferredSize(new Dimension(220, 35));
        jtfClientID.setCaretColor(Color.WHITE);
        jtfClientID.setBackground(JTF_COLOR);
        jtfClientID.setBorder(new LineBorder(Color.BLACK));
        jtfClientID.setFont(new Font(FONT_FAMILY, Font.BOLD, 18));
        jtfClientID.setHorizontalAlignment(SwingConstants.RIGHT);
        jtfClientID.setBorder(new EmptyBorder(0, 0, 0, 10));

        JLabel lblOrderID = new JLabel("Order ID");
        lblOrderID.setFont(new Font(FONT_FAMILY, Font.BOLD, 18));
        lblOrderID.setForeground(Color.WHITE);

        jtfOrderID = new JTextField();
        jtfOrderID.setPreferredSize(new Dimension(220, 35));
        jtfOrderID.setCaretColor(Color.WHITE);
        jtfOrderID.setBackground(JTF_COLOR);
        jtfOrderID.setFont(font);
        jtfOrderID.setBorder(new LineBorder(Color.BLACK));
        jtfOrderID.setFont(new Font(FONT_FAMILY, Font.BOLD, 18));
        jtfOrderID.setHorizontalAlignment(SwingConstants.RIGHT);
        jtfOrderID.setBorder(new EmptyBorder(0, 0, 0, 10));

        JLabel lblOrderDate = new JLabel("Order date");
        lblOrderDate.setFont(new Font(FONT_FAMILY, Font.BOLD, 18));
        lblOrderDate.setForeground(Color.WHITE);

        dataOrder = new JDateChooser();
        dataOrder.setDateFormatString(DATE_FORMAT);
        dataOrder.setPreferredSize(new Dimension(220, 35));
        dataOrder.setFont(new Font(FONT_FAMILY, Font.BOLD, 18));
        dataOrder.setDate(new Date());
        dataOrder.setMinSelectableDate(new Date());
        JTextFieldDateEditor dateEditor = (JTextFieldDateEditor) dataOrder.getComponent(1);
        dateEditor.setHorizontalAlignment(JTextField.RIGHT);
        dateEditor.setFont(font);
        dateEditor.setBackground(JTF_COLOR);
        dateEditor.setBorder(new LineBorder(Color.WHITE));
        dateEditor.setBorder(new EmptyBorder(0, 0, 0, 10));
        btn_all = new JButton(DesktopRender.formatButton("Show all orders"));

        formatButton(btn_all);

        orderInfoPane.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        // first column
        gc.anchor = GridBagConstraints.EAST;
        gc.weightx = 1;
        gc.weighty = 1;

        gc.gridx = 0;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(2, 10, 2, 10);
        orderInfoPane.add(lblClienteID, gc);

        gc.gridx = 0;
        gc.gridy = 1;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(2, 10, 2, 10);
        orderInfoPane.add(lblOrderID, gc);

        gc.gridx = 0;
        gc.gridy = 2;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(2, 10, 2, 10);
        orderInfoPane.add(lblOrderDate, gc);


        // second column//
        gc.anchor = GridBagConstraints.WEST;
        gc.gridx = 1;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.LINE_START;
        orderInfoPane.add(jtfClientID, gc);

        gc.gridx = 1;
        gc.gridy = 1;

        gc.anchor = GridBagConstraints.LINE_START;
        orderInfoPane.add(jtfOrderID, gc);

        gc.gridx = 1;
        gc.gridy = 2;

        gc.anchor = GridBagConstraints.LINE_START;
        orderInfoPane.add(dataOrder, gc);

        gc.gridx = 1;
        gc.gridy = 3;

        gc.anchor = GridBagConstraints.LINE_END;
        orderInfoPane.add(btn_all, gc);
    }

    void filterTable() {
        List<RowFilter<Object, Object>> filters = new ArrayList<>();
        sorter = new TableRowSorter<>(articoloTable.getModel());
        articoloTable.setRowSorter(sorter);
        filters.add(RowFilter.regexFilter(requireNonNull(jcbCategoria.getSelectedItem()).toString(), 3));
        filters.add(RowFilter.regexFilter(requireNonNull(jcbPosizione.getSelectedItem()).toString(), 4));
        filters.add(RowFilter.regexFilter(requireNonNull(jcbUnita.getSelectedItem()).toString(), 5));
        RowFilter<Object, Object> rf = RowFilter.orFilter(filters);
        sorter.setRowFilter(rf);
    }

    public void refreshTable() {

        // resetto le jcombobox
        jcbCategoria.setSelectedIndex(0);
        jcbPosizione.setSelectedIndex(0);
        jcbUnita.setSelectedIndex(0);

        sorter = new TableRowSorter<>(articoloTable.getModel());
        articoloTable.setRowSorter(sorter);
        ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
        sorter.setSortKeys(sortKeys);
        sorter.sort();
        articoloTable.setRowSorter(sorter);
        sorter.setRowFilter(null);
    }

    public Articolo getArticle() {
        Articolo articolo = new Articolo();
        if (articoloTable.getSelectedRow() >= 0) {
            int index = articoloTable.getSelectedRow();
            articolo.setUID(Integer.valueOf(String.valueOf(articoloTable.getValueAt(index, 0))));
            articolo.setCodice(formatUIDCode(Integer.valueOf(String.valueOf(articoloTable.getValueAt(index, 0)))));
            articolo.setDescrizione(String.valueOf(articoloTable.getValueAt(index, 2)));
            articolo.setCategoria(new Categoria(String.valueOf(articoloTable.getValueAt(index, 3)), false));
            articolo.setPosizione(new Posizione(String.valueOf(articoloTable.getValueAt(index, 4)), false));
            articolo.setUnita(new Unita(String.valueOf(articoloTable.getValueAt(index, 5)), false));
            articolo.setPrezzo(Double.valueOf(articoloTable.getValueAt(index, 6).toString().trim().replace("€", "").replace(",", ".")));
            articolo.setScorta(Integer.valueOf(articoloTable.getValueAt(index, 7).toString()));
            articolo.setProvenienza(String.valueOf(articoloTable.getValueAt(index, 8)));
            try {
                articolo.setDataIns(articoloTable.getValueAt(index, 9) != null ? (new SimpleDateFormat(DATE_FORMAT)).parse(articoloTable.getValueAt(index, 9).toString()) : null);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return articolo;
    }

    void selectedArticle() {

        Articolo articolo = getArticle();
        if (articolo.getPrezzo() != null) {
            if (!checkArticleOrder(articolo)) {
                orderTableModel.addRow(new Object[]{articolo.getCodice(), articolo.getDescrizione(), formatMoney(articolo.getPrezzo()), 1, formatMoney(articolo.getPrezzo())});
                jtfOrderImporto.setText("Totale: ".concat(formatMoney(round(IntStream.range(0, orderTable.getRowCount()).mapToDouble(i -> Double.parseDouble(orderTable.getValueAt(i, 4).toString().trim().replace("€", "").replace(",", "."))).sum(), 2))));
            }
        }
        articoloTable.getSelectionModel().clearSelection();
        orderTable.getSelectionModel().clearSelection();
    }

    boolean checkArticleOrder(Articolo articolo) {
        if (orderTableModel.getRowCount() > 0)
            return IntStream.range(0, orderTableModel.getRowCount()).anyMatch(i -> Objects.equals(orderTableModel.getValueAt(i, 0), articolo.getCodice()));
        return false;
    }

    void buildOrderInformation() {

        orderPane.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
        JPanel orderCountPane = new JPanel();
        JPanel orderTotalAmount = new JPanel();
        JPanel orderLastDate = new JPanel();

        orderCountPane.setPreferredSize(new Dimension(210, 65));
        orderTotalAmount.setPreferredSize(new Dimension(210, 65));
        orderLastDate.setPreferredSize(new Dimension(210, 65));

        Border line = BorderFactory.createLineBorder(Color.WHITE);
        Border empty = new EmptyBorder(5, 5, 5, 5);
        CompoundBorder border = new CompoundBorder(line, empty);

        orderCountPane.setBorder(border);
        orderTotalAmount.setBorder(border);
        orderLastDate.setBorder(border);

        orderCountPane.setBackground(wrapperPane.getBackground());
        orderTotalAmount.setBackground(wrapperPane.getBackground());
        orderLastDate.setBackground(wrapperPane.getBackground());


        JLabel jlOrderCount = new JLabel(formatOrderText("Numero di ordine", "##"));
        JLabel jlTotalAmount = new JLabel(formatOrderText("Importo totale ordine", "##"));
        JLabel jlLastOrderDate = new JLabel(formatOrderText("Data ultimo ordine", "10-02-2021"));

        jlOrderCount.setFont(font);
        jlTotalAmount.setFont(font);
        jlLastOrderDate.setFont(font);

        jlOrderCount.setBackground(Color.WHITE);
        jlTotalAmount.setBackground(Color.WHITE);
        jlLastOrderDate.setBackground(Color.WHITE);

        orderCountPane.add(jlOrderCount);
        orderTotalAmount.add(jlTotalAmount);
        orderLastDate.add(jlLastOrderDate);

        orderPane.add(orderCountPane);
        orderPane.add(orderTotalAmount);
        orderPane.add(orderLastDate);
    }

    void removeArticleOrder() {
        if (orderTable.getSelectedRow() == -1) {
            showMessageDialog(null, "Selezionare un articolo", "Info Dialog", JOptionPane.ERROR_MESSAGE);
        } else {
            orderTableModel.removeRow(orderTable.getSelectedRow());
            showMessageDialog(null, "Cancellazione effettuata", "Info Dialog", JOptionPane.INFORMATION_MESSAGE);
            jtfOrderImporto.setText("Totale: ".concat(formatMoney(round(IntStream.range(0, orderTable.getRowCount()).mapToDouble(i -> Double.parseDouble(orderTable.getValueAt(i, 4).toString().trim().replace("€", "").replace(",", "."))).sum(), 2))));
        }
    }

    void getClientID() {
        if (table.getSelectedRow() >= 0) {
            jtfClientID.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 1)));
            jtfClientID.setEditable(false);
            jtfOrderID.setText(formatUIDCode(1 + getAllOrderCount()));
            jtfOrderID.setEditable(false);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    public static class SpinnerEditor extends DefaultCellEditor {
        JSpinner spinner;
        JSpinner.DefaultEditor editor;
        JTextField textField;
        boolean valueSet;

        // Initializes the spinner.
        public SpinnerEditor() {
            super(new JTextField());
            spinner = new JSpinner(new SpinnerNumberModel(1, 1, 10000, 1));
            spinner.setBorder(new EmptyBorder(0, 5, 0, 0));
            spinner.setBorder(new LineBorder(Color.BLACK));
            spinner.setPreferredSize(new Dimension(120, 25));
            spinner.setFont(font);
            editor = ((JSpinner.NumberEditor) spinner.getEditor());
            textField = editor.getTextField();
            //textField.setAllowsInvalid(false);
            textField.setCaretColor(new Color(255, 255, 255));

            textField.addFocusListener(new FocusListener() {
                public void focusGained(FocusEvent fe) {
                    SwingUtilities.invokeLater(() -> {
                        if (valueSet) {
                            textField.setCaretPosition(1);
                        }
                    });
                }

                public void focusLost(FocusEvent fe) {
                }
            });
            textField.addActionListener(ae -> stopCellEditing());
        }

        // Prepares the spinner component and returns it.
        public Component getTableCellEditorComponent(
                JTable table, Object value, boolean isSelected, int row, int column
        ) {
            if (!valueSet) {
                spinner.setValue(value);
            }
            SwingUtilities.invokeLater(() -> textField.requestFocus());
            return spinner;
        }

        public boolean isCellEditable(EventObject eo) {
            if (eo instanceof KeyEvent) {
                KeyEvent ke = (KeyEvent) eo;
                textField.setText(String.valueOf(ke.getKeyChar()));
                valueSet = true;
            } else {
                valueSet = false;
            }
            return true;
        }

        // Returns the spinners current value.
        public Object getCellEditorValue() {
            return spinner.getValue();
        }

        public boolean stopCellEditing() {
            int index = orderTable.getSelectedRow();
            orderTable.setValueAt(formatMoney(round(Integer.parseInt(textField.getText().replace(".", "")) * Double.parseDouble(orderTable.getValueAt(index, 2).toString().trim().replace("€", "").replace(",", ".")), 2)), index, 4);
            orderTable.getSelectionModel().clearSelection();
            jtfOrderImporto.setText("Totale: ".concat(formatMoney(round(IntStream.range(0, orderTable.getRowCount()).mapToDouble(i -> Double.parseDouble(orderTable.getValueAt(i, 4).toString().trim().replace("€", "").replace(",", "."))).sum(), 2))));
            try {
                editor.commitEdit();
                spinner.commitEdit();
            } catch (java.text.ParseException e) {
                JOptionPane.showMessageDialog(null,
                        "Invalid value, discarding.");
            }
            return super.stopCellEditing();
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

}


