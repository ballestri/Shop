package shop.view;

import shop.utils.DesktopRender;
import shop.utils.RoundedPanel;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;


import static java.util.Objects.requireNonNull;
import static shop.utils.DesktopRender.*;

public class ClientePane extends AContainer implements ActionListener {

    // pannello interno
    protected JPanel internPane, wrapperPane, clientePanel, clientPaneWrapper, movimentPane, tablePanewrapper,orderPane;
    protected RoundedPanel titleClientePane, infoClientePane, buttonPane;

    protected JLabel lblNome, lblCognome, lblTelefono, lblEmail, lblIndirizzo,lblComune;
    public static JTextField jtfNome, jtfCognome, jtfTelefono, jtfEmail, jtfIndirizzo,jtfComune;

    public static DefaultTableModel tableModel;
    JTableHeader tableHeader;
    public static JTable table;
    JScrollPane scrollPane;

    RoundedPanel roundedPanel;
    protected JButton btn_prima, btn_close;
    protected JToolBar toolbar;
    protected JButton btn_insert,btn_edit,btn_remove, btn_refresh_cliente;

    // Pulsante di carica articolo
    private Font font;

    public ClientePane() {
        initPanel();
    }

    public void initPanel() {

        font = new Font(FONT_FAMILY, Font.BOLD, 16);

        ToolTipManager.sharedInstance().setInitialDelay(500);
        ToolTipManager.sharedInstance().setDismissDelay(4000);

        toolbar = new JToolBar();
        btn_prima = new JButton();
        btn_prima.setIcon(new ImageIcon(this.getClass().getResource("/images/prima.png")));
        toolbar.add(btn_prima);
        btn_prima.setFocusPainted(false);
        btn_prima.addActionListener(this);
        btn_prima.setToolTipText("Prima");
        toolbar.addSeparator();

        btn_close = new JButton();
        btn_close.setIcon(new ImageIcon(this.getClass().getResource("/images/esci.png")));
        toolbar.add(btn_close);
        btn_close.setFocusPainted(false);
        btn_close.setToolTipText("Chiudi");
        toolbar.addSeparator();
        btn_close.addActionListener(evt -> System.exit(0));

        internPane = new JPanel();
        wrapperPane = new JPanel();
        clientePanel = new JPanel();
        movimentPane = new JPanel();
        orderPane= new JPanel();

        initComponents();

        toolbar.setFloatable(false);
        container.setLayout(new BorderLayout());
        container.add(toolbar, BorderLayout.NORTH);
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

        internPane.setBounds(150, 140, 1200, 675);
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
        tablePanewrapper.add(orderPane,BorderLayout.SOUTH);
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
        infoClientePane.setBorder(new EmptyBorder(20, 10, 40, 10));

        btn_refresh_cliente = new JButton(new ImageIcon(requireNonNull(ClassLoader.getSystemClassLoader().getResource("images/refresh.png"))));
        btn_refresh_cliente.setPreferredSize(new Dimension(48, 48));
        btn_refresh_cliente.setContentAreaFilled(false);
        btn_refresh_cliente.setOpaque(false);

        JLabel lblFormName = new JLabel("Anagrafica cliente");
        lblFormName.setFont(new Font(FONT_FAMILY, Font.BOLD, 18));
        titleClientePane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1;
        c.weighty = 1;

        c.gridx = 0;
        c.gridy = 0;

        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(2, 20, 2, 0);
        titleClientePane.add(btn_refresh_cliente, c);

        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 1;
        c.gridy = 0;

        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(2, 0, 2, 0);
        titleClientePane.add(lblFormName, c);

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

    void buildButtonPane(){

        btn_insert = new JButton(DesktopRender.formatButton("Insert"));
        btn_edit = new JButton(DesktopRender.formatButton("Edit"));
        btn_remove = new JButton(DesktopRender.formatButton("Remove"));

        buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 15));

        formatButton(btn_insert);
        formatButton(btn_edit);
        formatButton(btn_remove);

        buttonPane.add(btn_insert);
        buttonPane.add(btn_edit);
        buttonPane.add(btn_remove);
    }


    void formatButton(JButton btn) {
        btn.setFont(font);
        btn.setForeground(Color.WHITE);
        btn.setBorder(new LineBorder(Color.BLACK));
        btn.setBackground(new Color(0, 128, 128));
        btn.setFocusPainted(false);
        btn.addActionListener(this);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(100, 40));
    }

    void buildTableWrapper() {
        String[] header = {"Codice", "Cognome", "Nome", "Telefono", "Email"};

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
        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) table.getDefaultRenderer(Object.class);
        table.setDefaultRenderer(Object.class, renderer);
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
        table.setPreferredScrollableViewportSize(new Dimension(675, 475));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setMinWidth(120);
        table.getColumnModel().getColumn(1).setMinWidth(130);
        table.getColumnModel().getColumn(2).setMinWidth(130);
        table.getColumnModel().getColumn(3).setMinWidth(220);
        table.setAutoCreateRowSorter(true);
        table.setFocusable(false);

        scrollPane = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(675, 475));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        scrollPane.getViewport().setBackground(table.getBackground());

        tablePanewrapper.add(scrollPane, BorderLayout.CENTER);
    }


    void buildOrderInformation(){

        orderPane.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
        JPanel orderCountPane= new JPanel();
        JPanel orderTotalAmount= new JPanel();
        JPanel orderLastDate= new JPanel();

        orderCountPane.setPreferredSize(new Dimension(210,65));
        orderTotalAmount.setPreferredSize(new Dimension(210,65));
        orderLastDate.setPreferredSize(new Dimension(210,65));

        Border line = BorderFactory.createLineBorder(Color.WHITE);
        Border empty = new EmptyBorder(5, 5, 5, 5);
        CompoundBorder border = new CompoundBorder(line, empty);


        orderCountPane.setBorder(border);
        orderTotalAmount.setBorder(border);
        orderLastDate.setBorder(border);

        orderCountPane.setBackground(wrapperPane.getBackground());
        orderTotalAmount.setBackground(wrapperPane.getBackground());
        orderLastDate.setBackground(wrapperPane.getBackground());


        JLabel jlOrderCount= new JLabel(formatOrderText("Numero di ordine","##"));
        JLabel jlTotalAmount= new JLabel(formatOrderText("Importo totale ordine","##"));
        JLabel jlLastOrderDate= new JLabel(formatOrderText("Data ultimo ordine","10-02-2021"));

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
    public void actionPerformed(ActionEvent e) {
        container.removeAll();
        container.revalidate();
        if (e.getSource() == btn_prima) {
            container.add(new Pannello().getPanel());
        }
        container.repaint();
    }
}


