package shop.view;

import shop.controller.article.*;
import shop.entity.*;
import shop.utils.*;
import shop.view.articolo.CategoryView;
import shop.view.articolo.PositionView;
import shop.view.articolo.UnitView;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;

import static shop.dao.ArticleDAO.*;
import static shop.dao.CategoriaDAO.getAllCategories;
import static shop.dao.PositionDAO.getAllPosizione;
import static shop.dao.UnitDAO.getAllUnita;
import static shop.utils.DesktopRender.*;


public class ArticoloPane extends AContainer implements ActionListener {

    private static final Color JTF_COLOR = new Color(169, 204, 227);
    public static JComboBox<String> jcbCategoria, jcbUnita, jcbPosizione;
    protected Font font;

    // Informazioni sull'articolo
    public static JLabel lblCodice, lblDescrizione, lblCategoria, lblPosizione, lblUnita, lblPrezzo, lblScorta, lblProvenienza;
    public static JTextField jtfCodice, jtfDescrizione, jtfProvenienza;

    public JTextField filterField;
    public static JFormattedTextField jtfCurrency;
    public static JSpinner jspScorta;
    // Pannello delle funzionalita'
    JPanel internPanel, wrapperPane;
    public static RoundedPanel articlePane;
    JButton btn_list_categoria, btn_list_posizione, btn_list_unita, btn_nuovo, btn_aggiorna, btn_elimina, btn_salva;
    JScrollPane scrollPane;
    RoundedPanel actionPaneWrapper, informationPane;
    // Creazione della tabella che contiene le categorie
    public static DefaultTableModel tableModel;
    JTableHeader tableHeader;
    public static JTable table;

    public ArticoloPane() {
        initPanel();
    }

    public void initPanel() {

        font = new Font(FONT_FAMILY, Font.BOLD, 16);
        // I pulsanti delle funzionalita'
        wrapperPane = new JPanel();
        informationPane = new RoundedPanel();
        internPanel = new JPanel();
        articlePane = new RoundedPanel();

        btn_list_categoria = new JButton(DesktopRender.formatButton("Elenco categorie"));
        btn_list_posizione = new JButton(DesktopRender.formatButton("Elenco posizioni"));
        btn_list_unita = new JButton(DesktopRender.formatButton("Elenco unita'"));

        initComponents();
        buildProduct();
        buildArticleDetails();

        container.setLayout(new BorderLayout());
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
        wrapperPane.setBounds(50, 110, 1200, 750);
        wrapperPane.setBackground(container.getBackground());

        informationPane.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 15));
        formatButton(btn_list_categoria);
        formatButton(btn_list_posizione);
        formatButton(btn_list_unita);

        informationPane.add(btn_list_posizione);
        informationPane.add(btn_list_unita);
        informationPane.add(btn_list_categoria);

        btn_list_categoria.addActionListener(this);
        btn_list_posizione.addActionListener(this);
        btn_list_unita.addActionListener(this);

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

        jcbCategoria = new JComboBox<>(getAllCategories().toArray(new String[0]));
        jcbCategoria.setBorder(new LineBorder(Color.BLACK));
        jcbCategoria.setRenderer(new ComboRenderer());
        jcbCategoria.setFont(font);
        jcbCategoria.addActionListener(this);

        lblPosizione = new JLabel("Posizione");
        lblPosizione.setFont(font);

        jcbPosizione = new JComboBox<>(getAllPosizione().toArray(new String[0]));
        jcbPosizione.setBorder(new LineBorder(Color.BLACK));
        jcbPosizione.setRenderer(new ComboRenderer());
        jcbPosizione.setFont(font);
        jcbPosizione.addActionListener(this);

        lblUnita = new JLabel("Unita'");
        lblUnita.setFont(font);

        jcbUnita = new JComboBox<>(getAllUnita().toArray(new String[0]));
        jcbUnita.setBorder(new LineBorder(Color.BLACK));
        jcbUnita.setRenderer(new ComboRenderer());
        jcbUnita.setFont(font);
        jcbUnita.addActionListener(this);

        lblPrezzo = new JLabel("Prezzo");
        lblPrezzo.setFont(font);

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("sk", "SK"));
        currencyFormat.setMaximumFractionDigits(2);
        jtfCurrency = new JFormattedTextField(currencyFormat);
        jtfCurrency.setBorder(new EmptyBorder(0, 5, 0, 5));
        jtfCurrency.setBorder(new LineBorder(Color.BLACK));
        jtfCurrency.setBackground(JTF_COLOR);
        jtfCurrency.setFont(font);
        jtfCurrency.setHorizontalAlignment(SwingConstants.RIGHT);
        jtfCurrency.setPreferredSize(new Dimension(120, 25));
        jtfCurrency.setName("Prezzo");
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

        alignArticoloInit();

        internPanel.add(articlePane, BorderLayout.NORTH);
        internPanel.add(actionPaneWrapper);
    }

    public static void alignArticoloInit() {
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
        gc.insets = new Insets(5, 5, 5, 5);
        articlePane.add(lblDescrizione, gc);

        gc.gridx = 0;
        gc.gridy = 1;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 5, 5, 5);
        articlePane.add(lblUnita, gc);

        gc.gridx = 0;
        gc.gridy = 2;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 5, 5, 5);
        articlePane.add(lblScorta, gc);

        // second column
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridx = 1;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 5, 5, 5);
        articlePane.add(jtfDescrizione, gc);

        gc.gridx = 1;
        gc.gridy = 1;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 5, 5, 5);
        articlePane.add(jcbUnita, gc);

        gc.gridx = 1;
        gc.gridy = 2;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 5, 5, 5);
        articlePane.add(jspScorta, gc);


        // Third column
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridx = 2;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 10, 5, 10);
        articlePane.add(lblCategoria, gc);

        gc.gridx = 2;
        gc.gridy = 1;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 10, 5, 10);
        articlePane.add(lblProvenienza, gc);

        // Four column
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridx = 3;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 10, 5, 10);
        articlePane.add(jcbCategoria, gc);

        gc.gridx = 3;
        gc.gridy = 1;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 10, 5, 10);
        articlePane.add(jtfProvenienza, gc);

        gc.gridx = 3;
        gc.gridy = 2;


        // Five column
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridx = 4;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 10, 5, 10);
        articlePane.add(lblPosizione, gc);

        gc.gridx = 4;
        gc.gridy = 1;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 10, 5, 10);
        articlePane.add(lblPrezzo, gc);

        // Six column
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridx = 5;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 10, 5, 10);
        articlePane.add(jcbPosizione, gc);

        gc.gridx = 5;
        gc.gridy = 1;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 10, 5, 10);
        articlePane.add(jtfCurrency, gc);
    }

    public static void alignArticoloPost() {
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
        btn_salva.addActionListener(e -> insertArticle());
        btn_aggiorna.addActionListener(e -> updateArticle(getArticolo()));
        btn_elimina.addActionListener(e -> removeArticle());
        btn_nuovo.addActionListener(e -> initArticlePane());

        actionPaneWrapper.add(actionPane, BorderLayout.EAST);
    }

    void buildArticleDetails() {
        String[] header = {"UID", "Codice", "Descrizione", "Categoria", "Posizione", "Unita'", "Prezzo", "Scorta", "Provenienza", "Data inserimento"};
        tableModel = new DefaultTableModel(new Object[][]{}, header) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel) {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                int rendererWidth = component.getPreferredSize().width;
                TableColumn tableColumn = getColumnModel().getColumn(column);
                tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
                if (!component.getBackground().equals(getSelectionBackground())) {
                    component.setBackground((row % 2 == 0 ? new Color(88, 214, 141) : Color.WHITE));
                }

                if ((new ArrayList<>(Arrays.asList(1, 6, 7, 9, 10))).contains(column))
                    ((JLabel) component).setHorizontalAlignment(JLabel.CENTER);

                if ((new ArrayList<>(Arrays.asList(1, 6, 7))).contains(column))
                    component.setFont(font);
                return component;
            }

        };

        loadArticle().forEach(article -> tableModel.addRow(new String[]{String.valueOf(article.getUID()), String.valueOf(article.getCodice()), article.getDescrizione(), article.getCategoria().getCategoria(),
                article.getPosizione().getPosizione(), article.getUnita().getUnita(), formatMoney(article.getPrezzo()),
                String.valueOf(article.getScorta()), article.getProvenienza(), (new SimpleDateFormat(DATE_FORMAT)).format(article.getDataIns())}));

        tableHeader = table.getTableHeader();
        tableHeader.setBackground(new Color(39, 55, 70));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setFont(new Font(FONT_FAMILY, Font.BOLD, 16));
        tableHeader.setReorderingAllowed(false);

        filterField = RowFilterUtil.createRowFilter(table);
        filterField.setColumns(16);
        RendererHighlighted renderer = new RendererHighlighted(filterField);
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class, renderer);
        table.setAutoCreateRowSorter(false);
        table.setFillsViewportHeight(true);
        table.setFont(new Font(FONT_FAMILY, Font.PLAIN, 15));
        table.setRowHeight(25);
        table.setCursor(new Cursor(Cursor.HAND_CURSOR));
        table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        table.setPreferredScrollableViewportSize(new Dimension(1150, 420));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        DesktopRender.resizeColumnWidth(table);

        buildFonctionality();
        table.getSelectionModel().addListSelectionListener(e -> getSelectedArticle());
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(1).setMinWidth(100);
        table.getColumnModel().getColumn(1).setMaxWidth(100);
        table.getColumnModel().getColumn(6).setMinWidth(110);
        table.getColumnModel().getColumn(6).setMaxWidth(110);
        table.getColumnModel().getColumn(7).setMinWidth(110);
        table.getColumnModel().getColumn(7).setMaxWidth(110);
        table.getColumnModel().getColumn(9).setMinWidth(155);
        table.getColumnModel().getColumn(9).setMaxWidth(155);

        scrollPane = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(1150, 420));
        scrollPane.getViewport().setBackground(table.getBackground());
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        internPanel.add(scrollPane);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn_list_categoria) {
            new CategoryView(formatTitleFieldPane(e.getActionCommand()));
        } else if (e.getSource() == btn_list_unita) {
            new UnitView(formatTitleFieldPane(e.getActionCommand()));
        } else if (e.getSource() == btn_list_posizione) {
            new PositionView(formatTitleFieldPane(e.getActionCommand()));
        }
    }

    public void getSelectedArticle() {
        if (table.getSelectedRow() >= 0) {
            articlePane.removeAll();
            alignArticoloPost();
            articlePane.revalidate();
            articlePane.repaint();

            Articolo articolo = getArticolo();
            jtfCodice.setText(String.valueOf(articolo.getCodice()));
            jtfCodice.setEditable(false);
            jtfDescrizione.setText(articolo.getDescrizione());

            Categoria categoria = articolo.getCategoria();
            Unita unita = articolo.getUnita();
            Posizione posizione = articolo.getPosizione();

            jcbCategoria.setSelectedItem(categoria.getCategoria());
            jcbPosizione.setSelectedItem(posizione.getPosizione());
            jcbUnita.setSelectedItem(unita.getUnita());

            jtfCurrency.setText(formatMoney(articolo.getPrezzo()));
            jspScorta.setValue(articolo.getScorta());
            jtfProvenienza.setText(articolo.getProvenienza());
        }
        table.revalidate();
        table.repaint();
    }


    public static Articolo getArticolo() {

        Articolo articolo = new Articolo();
        if (table.getSelectedRow() >= 0) {
            int index = table.getSelectedRow();
            articolo.setUID(Integer.valueOf(String.valueOf(table.getValueAt(index, 0))));
            articolo.setCodice(formatProductCode(Integer.valueOf(String.valueOf(table.getValueAt(index, 0)))));
            articolo.setDescrizione(String.valueOf(table.getValueAt(index, 2)));

            articolo.setCategoria(new Categoria(String.valueOf(table.getValueAt(index, 3)), false));
            articolo.setPosizione(new Posizione(String.valueOf(table.getValueAt(index, 4)), false));
            articolo.setUnita(new Unita(String.valueOf(table.getValueAt(index, 5)), false));

            articolo.setPrezzo(Double.valueOf(table.getValueAt(index, 6).toString().trim().replace("€", "").replace(",", ".")));
            articolo.setScorta(Integer.valueOf(table.getValueAt(index, 7).toString()));
            articolo.setProvenienza(String.valueOf(table.getValueAt(index, 8)));
            try {
                articolo.setDataIns(table.getValueAt(index, 9) != null ? (new SimpleDateFormat(DATE_FORMAT)).parse(table.getValueAt(index, 9).toString()) : null);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return articolo;

    }

    String formatTitleFieldPane(String field) {
        return field.replace("<html><center>", "").replace("</center></html>", "");
    }
}