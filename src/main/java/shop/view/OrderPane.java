package shop.view;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;
import shop.controller.article.RendererHighlighted;
import shop.controller.article.RowFilterUtil;
import shop.utils.ComboRenderer;
import shop.utils.DesktopRender;
import shop.utils.RoundedPanel;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;


import static java.util.Objects.requireNonNull;
import static shop.dao.CategoriaDAO.getAllCategories;
import static shop.utils.DesktopRender.*;

public class OrderPane extends AContainer implements ActionListener {

    // pannello interno
    protected JPanel internPane, wrapperPane, clientePanel, clientPaneWrapper, movimentPane, orderInfoPane, orderPane,
            infoClientePane, orderPanewrapper, articoloPaneWrapper, articoloOrderWrapper;
    protected RoundedPanel titleClientePane, categoriePane, orderButtonPane;

    JTextField filterCustomer,jtfClientID, jtfOrderID;
    public static JDateChooser dataOrder;
    public static DefaultTableModel tableModel,articoloTableModel,orderTableModel;
    protected JTable table, articoloTable,orderTable;
    JScrollPane scrollPane,articoloScrollPane, orderScrollPane;

    RoundedPanel roundedPanel;
    protected JToolBar toolbar;
    protected JButton btn_refresh_cliente, btn_insert, btn_all, btn_remove;

    protected JLabel lblCategoria;
    protected JComboBox<String> jcbCategoria;

    // Pulsante di carica articolo
    private Font font;

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
        categoriePane = new RoundedPanel();
        categoriePane.setBackground(roundedPanel.getBackground());

        buildCategoriePane();
        buildTableArticoloWrapper();


        articoloOrderWrapper.setLayout(new BorderLayout());

        buildTableArticoloWrapper();
        makeButtonPane();
        buildTableOrderWrapper();

        orderPanel.setLayout(new BorderLayout());
        orderPanel.add(articoloPaneWrapper,BorderLayout.WEST);
        orderPanel.add(btnSelected);
        orderPanel.add(articoloOrderWrapper,BorderLayout.EAST);

        wrapperPane.setLayout(new BorderLayout());
        wrapperPane.add(clientPanel, BorderLayout.NORTH);
        wrapperPane.add(orderPanel, BorderLayout.SOUTH);
    }

    void buildClientWrapper() {

        titleClientePane = new RoundedPanel();
        infoClientePane = new JPanel();

        titleClientePane.setPreferredSize(new Dimension(525, 60));
        infoClientePane.setPreferredSize(new Dimension(525, 260));

        buildTableCustomerWrapper();

        titleClientePane.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));

        btn_refresh_cliente = new JButton(new ImageIcon(requireNonNull(ClassLoader.getSystemClassLoader().getResource("images/cliente.png"))));
        btn_refresh_cliente.setPreferredSize(new Dimension(48, 48));
        btn_refresh_cliente.setContentAreaFilled(false);
        btn_refresh_cliente.setOpaque(false);
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
        titleClientePane.add(btn_refresh_cliente, c);

        c.anchor = GridBagConstraints.LINE_START;
        c.gridx = 1;
        c.gridy = 0;

        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(2, 0, 2, 0);
        titleClientePane.add(lblFormName, c);


        c.anchor = GridBagConstraints.LINE_START;
        c.gridx = 2;
        c.gridy = 0;

        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(2, 0, 2, 0);
        titleClientePane.add(filterCustomer, c);

        clientPaneWrapper.add(titleClientePane,BorderLayout.NORTH);
        clientPaneWrapper.add(scrollPane,BorderLayout.CENTER);
    }

    void buildCategoriePane() {
        lblCategoria = new JLabel("Categoria");
        lblCategoria.setFont(new Font(FONT_FAMILY, Font.BOLD, 16));
        lblCategoria.setForeground(Color.WHITE);
        jcbCategoria = new JComboBox<>(getAllCategories().toArray(new String[0]));
        jcbCategoria.setBorder(new LineBorder(Color.BLACK));
        jcbCategoria.setRenderer(new ComboRenderer());
        jcbCategoria.setFont(font);
        jcbCategoria.addActionListener(this);
        categoriePane.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        categoriePane.add(lblCategoria);
        categoriePane.add(jcbCategoria);
        articoloPaneWrapper.add(categoriePane,BorderLayout.NORTH);
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
        String[] header = {"Cliente ID", "Cognome", "Nome", "Telefono", "Email"};

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


        filterCustomer = RowFilterUtil.createRowFilter(table);
        filterCustomer.setColumns(18);
        RendererHighlighted renderer = new RendererHighlighted(filterCustomer);
        table.setDefaultRenderer(Object.class, renderer);
        table.getTableHeader().setBackground(new Color(39, 55, 70));
        table.getTableHeader().setForeground(Color.WHITE);
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.setFillsViewportHeight(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(new Font(FONT_FAMILY, Font.BOLD, 16));
        table.setFont(new Font(FONT_FAMILY, Font.PLAIN, 15));
        table.setRowHeight(25);
        table.setCursor(new Cursor(Cursor.HAND_CURSOR));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setPreferredScrollableViewportSize(new Dimension(525, 260));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setMinWidth(120);
        table.getColumnModel().getColumn(1).setMinWidth(130);
        table.getColumnModel().getColumn(2).setMinWidth(130);
        table.getColumnModel().getColumn(3).setMinWidth(220);
        table.setAutoCreateRowSorter(true);
        table.setFocusable(false);

        scrollPane = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(525, 260));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        scrollPane.getViewport().setBackground(table.getBackground());
    }


    void buildTableArticoloWrapper() {
        String[] header = {"Articolo ID", "Descrizione", "Prezzo", "Provenienza"};

        articoloTableModel = new DefaultTableModel(new Object[][]{}, header) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        articoloTable = new JTable(articoloTableModel) {
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

        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) articoloTable.getDefaultRenderer(Object.class);
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        articoloTable.setDefaultRenderer(Object.class, renderer);
        articoloTable.getTableHeader().setBackground(new Color(39, 55, 70));
        articoloTable.getTableHeader().setForeground(Color.WHITE);
        articoloTable.setFillsViewportHeight(true);
        articoloTable.getTableHeader().setReorderingAllowed(false);
        articoloTable.getTableHeader().setFont(new Font(FONT_FAMILY, Font.BOLD, 16));
        articoloTable.setFont(new Font(FONT_FAMILY, Font.PLAIN, 15));
        articoloTable.setRowHeight(25);
        articoloTable.setCursor(new Cursor(Cursor.HAND_CURSOR));
        articoloTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        articoloTable.setPreferredScrollableViewportSize(new Dimension(525, 260));
        articoloTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        articoloTable.getColumnModel().getColumn(0).setMinWidth(120);
        articoloTable.getColumnModel().getColumn(1).setMinWidth(130);
        articoloTable.getColumnModel().getColumn(2).setMinWidth(130);
        articoloTable.getColumnModel().getColumn(3).setMinWidth(220);
        articoloTable.setAutoCreateRowSorter(true);
        articoloTable.setFocusable(false);

        articoloScrollPane = new JScrollPane(articoloTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        articoloScrollPane.setPreferredSize(new Dimension(525, 260));
        articoloScrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        articoloScrollPane.getViewport().setBackground(articoloTable.getBackground());


        articoloScrollPane.setBorder(new EmptyBorder(20, 0, 0, 0));
        articoloScrollPane.setBackground(wrapperPane.getBackground());

        articoloPaneWrapper.add(articoloScrollPane, BorderLayout.CENTER);
    }


    void buildTableOrderWrapper() {
        String[] header = {"Order ID", "Nome", "Quantita", "Importo"};

        orderTableModel = new DefaultTableModel(new Object[][]{}, header) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        orderTable = new JTable(orderTableModel) {
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

        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) orderTable.getDefaultRenderer(Object.class);
        orderTable.setDefaultRenderer(Object.class, renderer);
        orderTable.getTableHeader().setBackground(new Color(39, 55, 70));
        orderTable.getTableHeader().setForeground(Color.WHITE);
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        orderTable.setFillsViewportHeight(true);
        orderTable.getTableHeader().setReorderingAllowed(false);
        orderTable.getTableHeader().setFont(new Font(FONT_FAMILY, Font.BOLD, 16));
        orderTable.setFont(new Font(FONT_FAMILY, Font.PLAIN, 15));
        orderTable.setRowHeight(25);
        orderTable.setCursor(new Cursor(Cursor.HAND_CURSOR));
        orderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        orderTable.setPreferredScrollableViewportSize(new Dimension(525, 320));
        orderTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        orderTable.getColumnModel().getColumn(0).setMinWidth(120);
        orderTable.getColumnModel().getColumn(1).setMinWidth(130);
        orderTable.getColumnModel().getColumn(2).setMinWidth(130);
        orderTable.getColumnModel().getColumn(3).setMinWidth(220);
        orderTable.setAutoCreateRowSorter(true);
        orderTable.setFocusable(false);

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
        orderButtonPane.setBackground(roundedPanel.getBackground());
        makeOrderInfoPane();
        orderPanewrapper.add(orderInfoPane,BorderLayout.NORTH);
    }


    void makeButtonPane() {

        wrapperPane.setBackground(new Color(39, 55, 70));
        Border line = BorderFactory.createLineBorder(Color.WHITE);
        Border empty = new EmptyBorder(5, 5, 5, 5);
        CompoundBorder border = new CompoundBorder(line, empty);


        btn_insert = new JButton(DesktopRender.formatButton("Insert Order"));
        btn_remove = new JButton(DesktopRender.formatButton("Remove product"));

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(180,40));
        panel.setBackground(wrapperPane.getBackground());
        panel.setLayout(new BorderLayout());
        panel.setBorder(border);


        JLabel jlOrderImporto= new JLabel(formatOrderText("Totale: 1.500.000 €"));
        jlOrderImporto.setFont(font);
        jlOrderImporto.setBackground(Color.WHITE);
        JPanel orderImportoPane = new JPanel();
        orderImportoPane.setBackground(wrapperPane.getBackground());
        orderImportoPane.setPreferredSize(new Dimension(180,40));
        orderImportoPane.add(jlOrderImporto);

        panel.add(orderImportoPane, BorderLayout.CENTER);
        orderButtonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        formatButton(btn_insert);
        formatButton(btn_remove);

        orderButtonPane.add(btn_remove);
        orderButtonPane.add(panel);
        orderButtonPane.add(btn_insert);
        articoloOrderWrapper.add(orderButtonPane,BorderLayout.SOUTH);
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
        jtfClientID.setFont(new Font(FONT_FAMILY, Font.BOLD, 20));

        JLabel lblOrderID = new JLabel("Order ID");
        lblOrderID.setFont(new Font(FONT_FAMILY, Font.BOLD, 18));
        lblOrderID.setForeground(Color.WHITE);

        jtfOrderID = new JTextField();
        jtfOrderID.setPreferredSize(new Dimension(220, 35));
        jtfOrderID.setCaretColor(Color.WHITE);
        jtfOrderID.setBackground(JTF_COLOR);
        jtfOrderID.setBorder(new LineBorder(Color.WHITE));
        jtfOrderID.setFont(font);

        JLabel lblOrderDate = new JLabel("Order date");
        lblOrderDate.setFont(new Font(FONT_FAMILY, Font.BOLD, 18));
        lblOrderDate.setForeground(Color.WHITE);

        dataOrder = new JDateChooser();
        dataOrder.setDateFormatString(DATE_FORMAT);
        dataOrder.setPreferredSize(new Dimension(220, 35));

        dataOrder.setFont(new Font(FONT_FAMILY, Font.BOLD, 16));
        Date date = new Date();
        dataOrder.setDate(date);
        dataOrder.setMinSelectableDate(new Date());
        JTextFieldDateEditor dateEditor = (JTextFieldDateEditor) dataOrder.getComponent(1);
        dateEditor.setHorizontalAlignment(JTextField.RIGHT);
        dateEditor.setFont(font);
        dateEditor.setBackground(JTF_COLOR);
        dateEditor.setBorder(new LineBorder(Color.WHITE));
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

    @Override
    public void actionPerformed(ActionEvent e) { }
}


