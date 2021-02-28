package shop.view;

import shop.controller.article.RendererHighlighted;
import shop.controller.article.RowFilterUtil;
import shop.utils.DesktopRender;
import shop.utils.RoundedPanel;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.stream.IntStream;

import static java.util.Objects.requireNonNull;
import static shop.utils.DesktopRender.FONT_FAMILY;
import static shop.dao.GiacenzaDAO.getAllGiacenza;
import static shop.utils.DesktopRender.JTF_COLOR;

public class GiacenzaPane extends AContainer implements ActionListener {

    // pannello interno
    private JPanel internPane, wrapperPane, clientPane;
    private RoundedPanel searchPane;
    protected JTextField filterField;
    public static DefaultTableModel tableModel;
    JTableHeader tableHeader;
    public static JTable table;
    JScrollPane scrollPane;
    protected JButton btn_refresh;
    private Font font;

    public GiacenzaPane() {
        initPanel();
    }

    public void initPanel() {

        font = new Font(FONT_FAMILY, Font.BOLD, 16);
        ToolTipManager.sharedInstance().setInitialDelay(500);
        ToolTipManager.sharedInstance().setDismissDelay(4000);

        // I pulsanti della Toolbar
        RoundedPanel toolbar = new RoundedPanel();
        toolbar.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        JLabel lblFormName = new JLabel("Giacenze prodotti");
        lblFormName.setForeground(Color.WHITE);
        lblFormName.setFont(new Font(FONT_FAMILY, Font.BOLD, 28));
        toolbar.setBackground(new Color(128, 0, 128));
        lblFormName.setPreferredSize(new Dimension(360, 40));
        toolbar.add(lblFormName, BorderLayout.CENTER);

        // I pulsanti delle funzionalita'
        internPane = new JPanel();
        wrapperPane = new JPanel();
        clientPane = new JPanel();
        searchPane = new RoundedPanel();
        build();
        buildClientArea();
        container.setLayout(new BorderLayout());
        container.add(toolbar, BorderLayout.NORTH);
    }


    public void build() {
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


        JLabel lbl = new JLabel("Ricerca");
        lbl.setFont(new Font(FONT_FAMILY, Font.BOLD, 20));
        filterField.setBackground(DesktopRender.JTF_COLOR);
        filterField.setFont(font);
        filterField.setBorder(new LineBorder(Color.BLACK));

        btn_refresh = new JButton(new ImageIcon(requireNonNull(ClassLoader.getSystemClassLoader().getResource("images/refresh.png"))));
        btn_refresh.setPreferredSize(new Dimension(48, 48));
        btn_refresh.setContentAreaFilled(false);
        btn_refresh.setOpaque(false);

        searchPane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1;
        c.weighty = 1;

        c.gridx = 0;
        c.gridy = 0;

        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(2, 10, 2, 0);
        searchPane.add(btn_refresh, c);


        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 1;
        c.gridy = 0;

        c.anchor = GridBagConstraints.LINE_END;
        c.insets = new Insets(2, 0, 2, 40);
        searchPane.add(lbl, c);

        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 2;
        c.gridy = 0;

        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(2, 0, 2, 160);
        searchPane.add(filterField, c);
        wrapperPane.add(searchPane, BorderLayout.NORTH);
        wrapperPane.add(clientPane, BorderLayout.CENTER);

        btn_refresh.addActionListener(e -> refreshTable());
    }

    void buildArticleDetails() {
        String[] header = {"Codice", "Descrizione", "Giacenza", "Scorta", "Riordino", "Totale carico", "Totale scarico", "Unita'"};
        tableModel = new DefaultTableModel(new Object[][]{}, header) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        getAllGiacenza().forEach(ga -> tableModel.addRow(new String[]{ga.getCodice(), ga.getDescrizione(), String.valueOf(ga.getGiacenza()), String.valueOf(ga.getScorta()), String.valueOf(ga.getRiordino()), String.valueOf(ga.getTotcarico()), String.valueOf(ga.getTotscarico()), ga.getUnita()}));

        table = new JTable(tableModel) {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component returnComp = super.prepareRenderer(renderer, row, column);
                int rendererWidth = returnComp.getPreferredSize().width;
                TableColumn tableColumn = getColumnModel().getColumn(column);
                tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
                if (!returnComp.getBackground().equals(getSelectionBackground())) {
                    returnComp.setBackground((row % 2 == 0 ? new Color(88, 214, 141) : Color.WHITE));
                }

                if (column >= 2 && column <= 6) {
                    ((JLabel) returnComp).setHorizontalAlignment(JLabel.CENTER);
                    returnComp.setBackground((row % 2 == 0 ? JTF_COLOR: Color.WHITE));
                    returnComp.setFont(font);
                } else if (column == 0 || column == 1) {
                    ((JLabel) returnComp).setHorizontalAlignment(JLabel.CENTER);
                } else
                    ((JLabel) returnComp).setHorizontalAlignment(JLabel.LEFT);
                return returnComp;
            }
        };

        tableHeader = table.getTableHeader();
        tableHeader.setBackground(new Color(39, 55, 70));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(font);

        IntStream.rangeClosed(2, 6).mapToObj(columnIndex -> table.getTableHeader().getColumnModel().getColumn(columnIndex)).forEachOrdered(tc -> tc.setHeaderRenderer(new WonHeaderRenderer()));

        filterField = RowFilterUtil.createRowFilter(table);
        filterField.setColumns(20);
        RendererHighlighted renderer = new RendererHighlighted(filterField);
        table.setDefaultRenderer(Object.class, renderer);
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.setFillsViewportHeight(true);
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(new Font(FONT_FAMILY, Font.BOLD, 16));
        table.setFont(new Font(FONT_FAMILY, Font.PLAIN, 15));
        table.setRowHeight(25);
        table.setCursor(new Cursor(Cursor.HAND_CURSOR));
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setPreferredScrollableViewportSize(new Dimension(1150, 420));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        DesktopRender.resizeColumnWidth(table);
        table.getColumnModel().getColumn(0).setMinWidth(150);
        table.getColumnModel().getColumn(1).setMinWidth(250);
        table.getColumnModel().getColumn(2).setMinWidth(50);
        table.getColumnModel().getColumn(3).setMinWidth(50);
        table.getColumnModel().getColumn(4).setMinWidth(50);
        table.getColumnModel().getColumn(5).setMinWidth(50);
        table.getColumnModel().getColumn(6).setMinWidth(50);

        scrollPane = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(1150, 420));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        scrollPane.getViewport().setBackground(table.getBackground());

        clientPane.add(scrollPane, BorderLayout.CENTER);
    }

    public class WonHeaderRenderer extends JLabel implements TableCellRenderer {

        public WonHeaderRenderer() {
            setFont(font);
            setOpaque(true);
            setForeground(Color.WHITE);
            setBackground(new Color(88, 24, 69));
            setHorizontalAlignment(JLabel.CENTER);
            setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value.toString());
            return this;
        }
    }

    void refreshTable() {
        tableModel.getDataVector().removeAllElements();
        tableModel.fireTableDataChanged();
        getAllGiacenza().forEach(a -> tableModel.addRow(new String[]{a.getCodice(), a.getDescrizione(), String.valueOf(a.getGiacenza()), String.valueOf(a.getScorta()), String.valueOf(a.getRiordino()), String.valueOf(a.getTotcarico()), String.valueOf(a.getTotscarico()), a.getUnita()}));
        table.revalidate();
        table.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {}

}
