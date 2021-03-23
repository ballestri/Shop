package shop.view;

import shop.controller.article.RendererHighlighted;
import shop.controller.article.RowFilterUtil;
import shop.entity.Cliente;
import shop.utils.DesktopRender;
import shop.utils.RoundedPanel;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;


import static java.util.Objects.requireNonNull;
import static shop.dao.ClienteDAO.*;
import static shop.utils.DesktopRender.*;
import static shop.view.GestionePane.tableModelCliente;

public class ClientePane extends AContainer implements ActionListener {

    // pannello interno
    protected JPanel internPane, wrapperPane, clientePanel, clientPaneWrapper, movimentPane, tablePanewrapper, orderPane;
    protected RoundedPanel titleClientePane, infoClientePane, buttonPane;

    protected JLabel lblNome, lblCognome, lblTelefono, lblEmail, lblIndirizzo, lblComune;
    public static JTextField filterCustomer,jtfNome, jtfCognome, jtfTelefono, jtfEmail, jtfIndirizzo, jtfComune;

    public static  JTable table;
    JScrollPane scrollPane;
    RoundedPanel roundedPanel;
    protected JToolBar toolbar;
    protected JButton btn_insert, btn_update, btn_remove, btn_clear, btn_refresh_cliente;

    // Pulsante di carica articolo
    private Font font;

    public ClientePane() {
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
        //loadCliente().forEach(cl -> clienteTableModel.addRow(new String[]{String.valueOf(cl.getUID()), cl.getCodice(),cl.getCognome(), cl.getNome(), cl.getTelefono(), cl.getEmail(), cl.getIndirizzo(), cl.getComune()}));
        container.setLayout(new BorderLayout());
    }

    public void initComponents() {

        roundedPanel = new RoundedPanel();
        roundedPanel.setLayout(new GridBagLayout());
        JLabel lblFormName = new JLabel("Cliente");
        lblFormName.setForeground(Color.WHITE);
        lblFormName.setFont(new Font(FONT_FAMILY, Font.BOLD, 28));
        roundedPanel.setBackground(new Color(128, 0, 128));
        roundedPanel.setPreferredSize(new Dimension(1200, 60));
        roundedPanel.add(lblFormName);

        internPane.setBounds(55, 80, 1200, 675);
        wrapperPane.setPreferredSize(new Dimension(1200, 675));
        internPane.setBackground(container.getBackground());
        internPane.setLayout(new BorderLayout());

        buildWrapperPanel();

        internPane.add(roundedPanel, BorderLayout.NORTH);
        internPane.add(wrapperPane, BorderLayout.CENTER);

        container.add(internPane);
    }

    private void buildWrapperPanel() {

        wrapperPane.setBackground(new Color(39, 55, 70));
        Border line = BorderFactory.createLineBorder(Color.WHITE);
        Border empty = new EmptyBorder(20, 40, 20, 20);
        CompoundBorder border = new CompoundBorder(line, empty);
        wrapperPane.setBorder(border);

        clientPaneWrapper = new JPanel();
        tablePanewrapper = new JPanel();

        clientPaneWrapper.setPreferredSize(new Dimension(400, 475));
        tablePanewrapper.setPreferredSize(new Dimension(675, 475));
        clientPaneWrapper.setBackground(wrapperPane.getBackground());
        tablePanewrapper.setBackground(wrapperPane.getBackground());
        orderPane.setPreferredSize(new Dimension(675, 80));
        orderPane.setBackground(wrapperPane.getBackground());

        buildTableWrapper();
        buildClientePane();
        buildOrderInformation();

        wrapperPane.setLayout(new BorderLayout());
        wrapperPane.add(clientPaneWrapper, BorderLayout.WEST);
        tablePanewrapper.add(orderPane, BorderLayout.SOUTH);
        wrapperPane.add(tablePanewrapper, BorderLayout.CENTER);

    }

    void buildClientePane() {

        titleClientePane = new RoundedPanel();
        infoClientePane = new RoundedPanel();
        buttonPane = new RoundedPanel();

        titleClientePane.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 15));
        titleClientePane.setPreferredSize(new Dimension(400, 70));
        infoClientePane.setPreferredSize(new Dimension(400, 325));
        buttonPane.setPreferredSize(new Dimension(400, 70));
        buttonPane.setBackground(roundedPanel.getBackground());
        infoClientePane.setBorder(new EmptyBorder(20, 10, 40, 10));

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
        c.insets = new Insets(2, 15, 2, 0);
        titleClientePane.add(btn_refresh_cliente, c);

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


        lblCognome = new JLabel("Cognome");
        lblCognome.setFont(font);

        jtfCognome = new JTextField(18);
        jtfCognome.setCaretColor(Color.BLACK);
        jtfCognome.setBackground(JTF_COLOR);
        jtfCognome.setBorder(new LineBorder(Color.BLACK));
        jtfCognome.setFont(font);

        lblNome = new JLabel("Nome");
        lblNome.setFont(font);

        jtfNome = new JTextField(18);
        jtfNome.setCaretColor(Color.BLACK);
        jtfNome.setBackground(JTF_COLOR);
        jtfNome.setBorder(new LineBorder(Color.BLACK));
        jtfNome.setFont(font);

        lblTelefono = new JLabel("Telefono");
        lblTelefono.setFont(font);

        jtfTelefono = new JTextField(18);
        jtfTelefono.setCaretColor(Color.BLACK);
        jtfTelefono.setBackground(JTF_COLOR);
        jtfTelefono.setBorder(new LineBorder(Color.BLACK));
        jtfTelefono.setFont(font);

        lblEmail = new JLabel("Email");
        lblEmail.setFont(font);

        jtfEmail = new JTextField(18);
        jtfEmail.setCaretColor(Color.BLACK);
        jtfEmail.setBackground(JTF_COLOR);
        jtfEmail.setBorder(new LineBorder(Color.BLACK));
        jtfEmail.setFont(font);

        lblIndirizzo = new JLabel("Indirizzo");
        lblIndirizzo.setFont(font);

        jtfIndirizzo = new JTextField(18);
        jtfIndirizzo.setCaretColor(Color.BLACK);
        jtfIndirizzo.setBackground(JTF_COLOR);
        jtfIndirizzo.setBorder(new LineBorder(Color.BLACK));
        jtfIndirizzo.setFont(font);

        lblComune = new JLabel("Comune");
        lblComune.setFont(font);

        jtfComune = new JTextField(18);
        jtfComune.setCaretColor(Color.BLACK);
        jtfComune.setBackground(JTF_COLOR);
        jtfComune.setBorder(new LineBorder(Color.BLACK));
        jtfComune.setFont(font);

        infoClientePane.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        // first column
        gc.anchor = GridBagConstraints.EAST;
        gc.weightx = 1;
        gc.weighty = 1;

        gc.gridx = 0;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(2, 10, 2, 10);
        infoClientePane.add(lblCognome, gc);

        gc.gridx = 0;
        gc.gridy = 1;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(2, 10, 2, 10);
        infoClientePane.add(lblNome, gc);

        gc.gridx = 0;
        gc.gridy = 2;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(2, 10, 2, 10);
        infoClientePane.add(lblTelefono, gc);

        gc.gridx = 0;
        gc.gridy = 3;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(2, 10, 2, 10);
        infoClientePane.add(lblEmail, gc);

        gc.gridx = 0;
        gc.gridy = 4;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(2, 10, 2, 10);
        infoClientePane.add(lblIndirizzo, gc);

        gc.gridx = 0;
        gc.gridy = 5;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(2, 10, 2, 10);
        infoClientePane.add(lblComune, gc);

        // second column//
        gc.anchor = GridBagConstraints.WEST;
        gc.gridx = 1;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.LINE_START;
        infoClientePane.add(jtfCognome, gc);

        gc.gridx = 1;
        gc.gridy = 1;

        gc.anchor = GridBagConstraints.LINE_START;
        infoClientePane.add(jtfNome, gc);

        gc.gridx = 1;
        gc.gridy = 2;

        gc.anchor = GridBagConstraints.LINE_START;
        infoClientePane.add(jtfTelefono, gc);

        gc.gridx = 1;
        gc.gridy = 3;

        gc.anchor = GridBagConstraints.LINE_START;
        infoClientePane.add(jtfEmail, gc);

        gc.gridx = 1;
        gc.gridy = 4;

        gc.anchor = GridBagConstraints.LINE_START;
        infoClientePane.add(jtfIndirizzo, gc);

        gc.gridx = 1;
        gc.gridy = 5;

        gc.anchor = GridBagConstraints.LINE_START;
        infoClientePane.add(jtfComune, gc);

        buildButtonPane();

        clientPaneWrapper.add(titleClientePane);
        clientPaneWrapper.add(infoClientePane);
        clientPaneWrapper.add(buttonPane);
    }

    void buildButtonPane() {

        btn_insert = new JButton(DesktopRender.formatButton("Insert"));
        btn_update = new JButton(DesktopRender.formatButton("Update"));
        btn_remove = new JButton(DesktopRender.formatButton("Remove"));
        btn_clear = new JButton(DesktopRender.formatButton("+ New"));

        buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 15));

        formatButton(btn_insert);
        formatButton(btn_update);
        formatButton(btn_remove);
        formatButton(btn_clear);

        buttonPane.add(btn_insert);
        buttonPane.add(btn_update);
        buttonPane.add(btn_remove);
        buttonPane.add(btn_clear);

        btn_insert.addActionListener(e -> insertCliente());
        btn_remove.addActionListener(e -> deleteCliente());
        table.getSelectionModel().addListSelectionListener(e -> getSelectedCliente());
        btn_clear.addActionListener(e -> {
            clearField();
            table.getSelectionModel().clearSelection();
        });

        btn_update.addActionListener(e -> {
            updateCliente(getCliente());
            table.getSelectionModel().clearSelection();
            clearField();
        });


    }


    void formatButton(JButton btn) {
        btn.setFont(font);
        btn.setForeground(Color.WHITE);
        btn.setBorder(new LineBorder(Color.BLACK));
        btn.setBackground(new Color(0, 128, 128));
        btn.setFocusPainted(false);
        btn.addActionListener(this);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(80, 40));
    }

    void buildTableWrapper() {


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
        filterCustomer.setColumns(14);
        RendererHighlighted renderer = new RendererHighlighted(filterCustomer);
        table.setDefaultRenderer(Object.class, renderer);
        table.getTableHeader().setBackground(new Color(39, 55, 70));
        table.getTableHeader().setForeground(Color.WHITE);
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setFillsViewportHeight(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(new Font(FONT_FAMILY, Font.BOLD, 16));
        table.setFont(new Font(FONT_FAMILY, Font.PLAIN, 15));
        table.setRowHeight(25);
        table.setCursor(new Cursor(Cursor.HAND_CURSOR));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setPreferredScrollableViewportSize(new Dimension(675, 475));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(1).setMinWidth(120);
        table.getColumnModel().getColumn(2).setMinWidth(130);
        table.getColumnModel().getColumn(3).setMinWidth(130);
        table.getColumnModel().getColumn(4).setMinWidth(220);
        table.setFocusable(false);

        scrollPane = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(675, 475));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        scrollPane.getViewport().setBackground(table.getBackground());

        tablePanewrapper.add(scrollPane, BorderLayout.CENTER);
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

    public static  Cliente getCliente() {
        Cliente cliente = new Cliente();
        if (table.getSelectedRow() >= 0) {
            int index = table.getSelectedRow();
            cliente.setUID(Integer.valueOf(String.valueOf(table.getValueAt(index, 0))));
            cliente.setCodice(formatUIDCode(Integer.valueOf(String.valueOf(table.getValueAt(index, 0)))));
            cliente.setCognome(String.valueOf(table.getValueAt(index, 2)));
            cliente.setNome(String.valueOf(table.getValueAt(index, 3)));
            cliente.setTelefono(String.valueOf(table.getValueAt(index, 4)));
            cliente.setEmail(String.valueOf(table.getValueAt(index, 5)));
            cliente.setIndirizzo(String.valueOf(table.getValueAt(index, 6)));
            cliente.setComune(String.valueOf(table.getValueAt(index, 7)));
        }
        return cliente;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}


