package shop.view;

import shop.controller.article.*;
import shop.db.ConnectionManager;
import shop.model.*;
import shop.utils.*;
import shop.view.articolo.CategoryPane;
import shop.view.articolo.PositionPane;
import shop.view.articolo.UnitPane;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.stream.*;

import static shop.view.articolo.controller.ArticleDbOperation.*;
import static shop.utils.DesktopRender.FONT_FAMILY;


public class ArticoloPane extends AContainer implements ActionListener {

    private static final Color JTF_COLOR = new Color(169, 204, 227 );
    public static JComboBox<String> jcbCategoria, jcbUnita, jcbPosizione;
    protected Font font;
    // la JToolbar
    protected JToolBar toolbar;
    protected JButton btn_prima;
    protected JButton btn_close;
    // Informazioni sull'articolo
    protected JLabel lblCodice, lblDescrizione, lblCategoria, lblPosizione, lblUnita, lblPrezzo, lblScorta, lblProvenienza;
    public static JTextField jtfCodice, jtfDescrizione, jtfProvenienza;

    protected JTextField filterField;
    public static JFormattedTextField jtfCurrency;
    public static JSpinner jspScorta;
    // Pannello delle funzionalita'
    JPanel internPanel, wrapperPane;
    RoundedPanel articlePane, informationPane;
    JButton btn_list_categoria, btn_list_posizione, btn_list_unita, btn_nuovo, btn_aggiorna, btn_elimina, btn_salva;
    JScrollPane scrollPane;
    RoundedPanel actionPaneWrapper;
    // Creazione della tabella che contiene le categorie
    public static DefaultTableModel tableModel;
    JTableHeader tableHeader;
    public static JTable table;

    public ArticoloPane() {
        initPanel();
    }

    public void initPanel() {

        font = new Font(FONT_FAMILY, Font.BOLD, 16);
        ToolTipManager.sharedInstance().setInitialDelay(500);
        ToolTipManager.sharedInstance().setDismissDelay(4000);

        // I pulsanti della Toolbar
        RoundedPanel toolbar = new RoundedPanel();
        toolbar.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.EAST;
        gc.weightx = 0.5;
        gc.weighty = 0.5;

        gc.gridx = 0;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(8, 150, 10, 10);

        JLabel lblFormName = new JLabel("Prodotto");
        lblFormName.setForeground(Color.WHITE);
        lblFormName.setFont(new Font("HelveticaNeue", Font.BOLD, 28));
        toolbar.setBackground(new Color(128, 0, 128));
        lblFormName.setPreferredSize(new Dimension(360, 40));
        toolbar.add(lblFormName, gc);

        gc.anchor = GridBagConstraints.EAST;
        gc.gridx = 1;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(0, 10, 0, 0);
        btn_prima = new JButton();
        btn_prima.setIcon(new ImageIcon(this.getClass().getResource("/images/back.png")));
        toolbar.add(btn_prima, gc);
        btn_prima.setFocusPainted(false);
        btn_prima.addActionListener(this);
        btn_prima.setToolTipText("Prima");
        btn_prima.addActionListener(this);

        btn_close = new JButton();
        // I pulsanti delle funzionalita'
        wrapperPane = new JPanel();
        informationPane = new RoundedPanel();
        internPanel = new JPanel();
        articlePane = new RoundedPanel();

        btn_list_categoria = new JButton(DesktopRender.formatButton("Lista categorie"));
        btn_list_posizione = new JButton(DesktopRender.formatButton("Lista posizioni"));
        btn_list_unita = new JButton(DesktopRender.formatButton("Lista unita'"));

        initComponents();
        buildProduct();
        buildArticleDetails();

        container.setLayout(new BorderLayout());
        container.add(toolbar, BorderLayout.NORTH);
    }

    public void initComponents() {

        internPanel.setPreferredSize(new Dimension(1200, 675));
        Border line = BorderFactory.createLineBorder(Color.WHITE);
        Border empty = new EmptyBorder(5, 10, 5, 10);
        CompoundBorder border = new CompoundBorder(line, empty);
        internPanel.setBorder(border);
        internPanel.setBackground(new Color(39, 55, 70));
        articlePane.setPreferredSize(new Dimension(1150, 140));
        informationPane.setPreferredSize(new Dimension(1200, 70));

        // pannello delle azioni
        actionPaneWrapper = new RoundedPanel();
        actionPaneWrapper.setPreferredSize(new Dimension(1150, 70));
        wrapperPane.setBounds(90, 90, 1200, 750);
        wrapperPane.setBackground(container.getBackground());

        informationPane.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 15));
        formatButton(btn_list_categoria);
        formatButton(btn_list_posizione);
        formatButton(btn_list_unita);

        informationPane.add(btn_list_posizione);
        informationPane.add(btn_list_unita);
        informationPane.add(btn_list_categoria);

        wrapperPane.setLayout(new BorderLayout());
        wrapperPane.add(informationPane, BorderLayout.NORTH);
        wrapperPane.add(internPanel, BorderLayout.SOUTH);
        container.add(wrapperPane);
    }

    void buildProduct() {

        font = new Font(FONT_FAMILY, Font.BOLD, 16);

        lblCodice = new JLabel("Codice");
        lblCodice.setFont(font);

        // Testo
        jtfCodice = new JTextField(16);
        jtfCodice.setCaretColor(Color.BLACK);
        jtfCodice.setBackground(JTF_COLOR);
        jtfCodice.setBorder(new LineBorder(Color.BLACK));
        jtfCodice.setFont(font);

        lblDescrizione = new JLabel("Descrizione");
        lblDescrizione.setFont(font);

        // Testo
        jtfDescrizione = new JTextField(16);
        jtfDescrizione.setCaretColor(Color.BLACK);
        jtfDescrizione.setBackground(JTF_COLOR);
        jtfDescrizione.setFont(font);
        jtfDescrizione.setBorder(new LineBorder(Color.BLACK));

        lblCategoria = new JLabel("Categoria");
        lblCategoria.setFont(font);

        jcbCategoria = new JComboBox<>(loadProductAttribute(Attribute.CATEGORIA.getAttribute()).toArray(new String[0]));
        jcbCategoria.setBorder(new LineBorder(Color.BLACK));
        jcbCategoria.setRenderer(new ComboRenderer());
        jcbCategoria.setFont(font);
        jcbCategoria.addActionListener(this);

        lblPosizione = new JLabel("Posizione");
        lblPosizione.setFont(font);
        // Testo

        jcbPosizione = new JComboBox<>(loadProductAttribute(Attribute.POSIZIONE.getAttribute()).toArray(new String[0]));
        jcbPosizione.setBorder(new LineBorder(Color.BLACK));
        jcbPosizione.setRenderer(new ComboRenderer());
        jcbPosizione.setFont(font);
        jcbPosizione.addActionListener(this);

        lblUnita = new JLabel("Unita'");
        lblUnita.setFont(font);

        //jcbUnita = new JComboBox(new String[]{"Kilogrammi", "Metri", "Pacchi"});
        jcbUnita = new JComboBox<>(loadProductAttribute(Attribute.UNITA.getAttribute()).toArray(new String[0]));
        jcbUnita.setBorder(new LineBorder(Color.BLACK));
        jcbUnita.setRenderer(new ComboRenderer());
        jcbUnita.setFont(font);
        jcbUnita.addActionListener(this);


        lblPrezzo = new JLabel("Prezzo");
        lblPrezzo.setFont(font);

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.ITALY);
        jtfCurrency = new JFormattedTextField(currencyFormat);
        jtfCurrency.setBorder(new EmptyBorder(0, 5, 0, 5));
        jtfCurrency.setBorder(new LineBorder(Color.BLACK));
        jtfCurrency.setBackground(JTF_COLOR);
        jtfCurrency.setFont(font);
        jtfCurrency.setHorizontalAlignment(SwingConstants.RIGHT);
        jtfCurrency.setPreferredSize(new Dimension(120, 25));
        jtfCurrency.setValue(0);

        lblScorta = new JLabel("Scorta");
        lblScorta.setFont(font);

        jspScorta = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        jspScorta.setBorder(new EmptyBorder(0, 5, 0, 0));
        jspScorta.setBorder(new LineBorder(Color.BLACK));
        jspScorta.setPreferredSize(new Dimension(120, 25));
        jspScorta.setFont(font);
        JTextField jtfScorta = ((JSpinner.DefaultEditor) jspScorta.getEditor()).getTextField();
        jtfScorta.setBackground(JTF_COLOR);
        jtfScorta.setCaretColor(new Color(255, 255, 255));


        lblProvenienza = new JLabel("Provenienza");
        lblProvenienza.setFont(font);
        // Testo
        jtfProvenienza = new JTextField(16);
        jtfProvenienza.setCaretColor(new Color(255, 255, 255));
        jtfProvenienza.setBackground(JTF_COLOR);
        jtfProvenienza.setFont(font);
        jtfProvenienza.setBorder(new LineBorder(Color.BLACK));

        // aggiunto nel pannelli interno
        articlePane.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        // first column
        gc.anchor = GridBagConstraints.EAST;
        gc.weightx = 0.5;
        gc.weighty = 0.5;

        gc.gridx = 0;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 10, 5, 10);
        articlePane.add(lblCodice, gc);

        gc.gridx = 0;
        gc.gridy = 1;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 10, 5, 10);
        articlePane.add(lblPosizione, gc);

        gc.gridx = 0;
        gc.gridy = 2;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 10, 5, 10);
        articlePane.add(lblPrezzo, gc);

        // second column
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridx = 1;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 10, 5, 10);
        articlePane.add(jtfCodice, gc);

        gc.gridx = 1;
        gc.gridy = 1;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 10, 5, 10);
        articlePane.add(jcbPosizione, gc);

        gc.gridx = 1;
        gc.gridy = 2;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 10, 5, 10);
        //articlePane.add(jtfPrezzo, gc);
        articlePane.add(jtfCurrency, gc);


        // Third column
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridx = 2;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 10, 5, 10);
        articlePane.add(lblDescrizione, gc);

        gc.gridx = 2;
        gc.gridy = 1;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 10, 5, 10);
        articlePane.add(lblUnita, gc);

        gc.gridx = 2;
        gc.gridy = 2;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 10, 5, 10);
        articlePane.add(lblScorta, gc);

        // Four column
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridx = 3;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 10, 5, 10);
        articlePane.add(jtfDescrizione, gc);

        gc.gridx = 3;
        gc.gridy = 1;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 10, 5, 10);
        articlePane.add(jcbUnita, gc);

        gc.gridx = 3;
        gc.gridy = 2;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 10, 5, 10);
        articlePane.add(jspScorta, gc);

        // Five column
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridx = 4;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 10, 5, 10);
        articlePane.add(lblCategoria, gc);

        gc.gridx = 4;
        gc.gridy = 1;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 10, 5, 10);
        articlePane.add(lblProvenienza, gc);


        // Six column
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridx = 5;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 10, 5, 10);
        articlePane.add(jcbCategoria, gc);

        gc.gridx = 5;
        gc.gridy = 1;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 10, 5, 10);
        articlePane.add(jtfProvenienza, gc);


        internPanel.add(articlePane, BorderLayout.NORTH);
        internPanel.add(actionPaneWrapper);
    }

    void buildFonctionality() {

        actionPaneWrapper.setLayout(new FlowLayout());
        JPanel searchPane = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 13, 3, 10);
        JLabel lbl = new JLabel("Ricerca");
        lbl.setFont(font);

        filterField.setBackground(JTF_COLOR);
        filterField.setFont(font);
        filterField.setBorder(new LineBorder(Color.BLACK));
        searchPane.add(lbl, c);
        searchPane.add(filterField, c);

        actionPaneWrapper.add(searchPane, BorderLayout.WEST);

        btn_nuovo = new JButton(DesktopRender.formatButton("+ New"));
        btn_salva = new JButton(DesktopRender.formatButton("Save"));
        btn_aggiorna = new JButton(DesktopRender.formatButton("Update"));
        btn_elimina = new JButton(DesktopRender.formatButton("Delete"));

        JPanel actionPane = new JPanel();
        actionPane.setLayout(new GridBagLayout());
        GridBagConstraints ca = new GridBagConstraints();
        ca.insets = new Insets(5, 10, 3, 10);
        formatButton(btn_salva);
        formatButton(btn_aggiorna);
        formatButton(btn_elimina);
        formatButton(btn_nuovo);

        actionPane.add(btn_salva, ca);
        actionPane.add(btn_aggiorna, ca);
        actionPane.add(btn_elimina, ca);
        actionPane.add(btn_nuovo, ca);

        // Gestione degli eventi
        btn_salva.addActionListener(e -> insertArticleToDB());
        btn_aggiorna.addActionListener(e -> updateArticleToDB());
        btn_elimina.addActionListener(e -> deleteArticleFromDB());
        btn_nuovo.addActionListener(e -> initArticlePane());

        actionPaneWrapper.add(actionPane, BorderLayout.EAST);
    }

    void buildArticleDetails() {
        String[] header = {"Codice", "Descrizione", "Categoria", "Posizione", "Unita'", "Prezzo", "Scorta", "Provenienza"};
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

        loadArticleFromDB().forEach(article -> tableModel.addRow(new String[]{article.getCodice(), article.getDescrizione(), article.getCategoria(), article.getPosizione(), article.getUnita(), String.valueOf(article.getPrezzo()).replace(".", ",").concat(" €"), String.valueOf(article.getScorta()), article.getProvenienza()}));

        tableHeader = table.getTableHeader();
        tableHeader.setBackground(new Color(39, 55, 70));
        tableHeader.setForeground(Color.WHITE);

        filterField = RowFilterUtil.createRowFilter(table);
        filterField.setColumns(16);
        RendererHighlighted renderer = new RendererHighlighted(filterField);
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
        DesktopRender.resizeColumnWidth(table);

        buildFonctionality();
        table.getSelectionModel().addListSelectionListener(e -> getSelectedArticle());
        table.getColumnModel().getColumn(0).setMinWidth(80);
        table.getColumnModel().getColumn(6).setMinWidth(80);

        scrollPane = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(1150, 420));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        scrollPane.getViewport().setBackground(table.getBackground());

        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        internPanel.add(scrollPane);
    }

    void formatButton(JButton btn) {
        btn.setFont(font);
        btn.setForeground(Color.WHITE);
        btn.setBorder(new LineBorder(Color.BLACK));
        btn.setBackground(new Color(0, 128, 128));
        btn.setFocusPainted(false);
        btn.addActionListener(this);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 40));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btn_prima) {
            container.removeAll();
            container.revalidate();
            container.add(new AnagraficaPane().getPanel());
            container.repaint();
            container.doLayout();
        } else if (e.getSource() == btn_list_categoria) {
            new CategoryPane(formatTitleFieldPane(e.getActionCommand()), IntStream.range(0, jcbCategoria.getItemCount()).mapToObj(i -> jcbCategoria.getItemAt(i)).collect(Collectors.toCollection(ArrayList::new)));
        } else if (e.getSource() == btn_list_unita) {
            new UnitPane(formatTitleFieldPane(e.getActionCommand()), IntStream.range(0, jcbUnita.getItemCount()).mapToObj(i -> jcbUnita.getItemAt(i)).collect(Collectors.toCollection(ArrayList::new)));
        } else if (e.getSource() == btn_list_posizione) {
            new PositionPane(formatTitleFieldPane(e.getActionCommand()), IntStream.range(0, jcbPosizione.getItemCount()).mapToObj(i -> jcbPosizione.getItemAt(i)).collect(Collectors.toCollection(ArrayList::new)));
        }
    }

    public void getSelectedArticle() {
        if (table.getSelectedRow() >= 0) {
            Articolo articolo = new Articolo();
            articolo.setCodice(String.valueOf(table.getValueAt(table.getSelectedRow(), 0)));
            articolo.setDescrizione(String.valueOf(table.getValueAt(table.getSelectedRow(), 1)));
            articolo.setCategoria(String.valueOf(table.getValueAt(table.getSelectedRow(), 2)));
            articolo.setPosizione(String.valueOf(table.getValueAt(table.getSelectedRow(), 3)));
            articolo.setUnita(String.valueOf(table.getValueAt(table.getSelectedRow(), 4)));
            articolo.setPrezzo(Double.valueOf(table.getValueAt(table.getSelectedRow(), 5).toString().replace("€", "").replace(",", ".")));
            articolo.setScorta(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 6).toString()));
            articolo.setProvenienza(String.valueOf(table.getValueAt(table.getSelectedRow(), 7)));

            // set dei valori dell'articolo per l'aggiornamento
            jtfCodice.setText(articolo.getCodice());
            jtfCodice.setEditable(false);
            jtfDescrizione.setText(articolo.getDescrizione());
            jcbCategoria.setSelectedItem(articolo.getCategoria());
            jcbPosizione.setSelectedItem(articolo.getPosizione());
            jcbUnita.setSelectedItem(articolo.getUnita());
            jtfCurrency.setText("€ ".concat(String.valueOf(articolo.getPrezzo())).replace(".", ","));
            jspScorta.setValue(articolo.getScorta());
            jtfProvenienza.setText(articolo.getProvenienza());
        }
        table.revalidate();
        table.repaint();
    }

    String formatTitleFieldPane(String field) {
        return field.replace("<html><center>", "").replace("</center></html>", "");
    }

    // Gestione attributi del prodotto
    public enum Attribute {
        CATEGORIA("CATEGORIA"), UNITA("UNITA"), POSIZIONE("POSIZIONE");
        String attribute;

        Attribute(String s) {
            attribute = s;
        }

        public String getAttribute() {
            return attribute;
        }
    }

    // Informazioni sull'articolo

    public static Set<String> loadProductAttribute(String attribute) {
        Set<String> lista = new HashSet<String>() {
            {
                add("NOT CATEGORIZED");
            }
        };
        try {
            Connection con = (new ConnectionManager()).getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(String.format("SELECT* FROM %s_PRODOTTO", attribute));
            while (rs.next())
                lista.add(rs.getString(attribute));
            stmt.close();
            con.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return lista;

    }

}