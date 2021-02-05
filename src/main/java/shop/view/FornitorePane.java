package shop.view;

import shop.controller.article.RendererHighlighted;
import shop.controller.article.RowFilterUtil;
import shop.entity.Fornitore;
import shop.utils.DesktopRender;
import shop.utils.RoundedPanel;
import shop.view.fornitore.FornitorePaneEdit;
import shop.view.fornitore.FornitorePaneUPD;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.JOptionPane.showMessageDialog;
import static shop.utils.DesktopRender.FONT_FAMILY;
import static shop.dao.FornitoreDAO.*;

public class FornitorePane extends AContainer implements ActionListener {

    public JButton btn_prima;

    // pannello interno
    private JPanel internPane, wrapperPane, clientPane;
    private RoundedPanel searchPane;
    protected JTextField filterField;
    public static DefaultTableModel tableModel;
    JTableHeader tableHeader;
    public static JTable table;
    JScrollPane scrollPane;

    protected JButton  btn_add, btn_update, btn_remove;
    private Font font;

    public FornitorePane() {
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

        JLabel lblFormName = new JLabel("Fornitore");
        lblFormName.setForeground(Color.WHITE);
        lblFormName.setFont( new Font(FONT_FAMILY, Font.BOLD, 28));
        toolbar.setBackground(new Color(128, 0, 128));
        lblFormName.setPreferredSize(new Dimension(360, 40));
        toolbar.add(lblFormName,gc);

        gc.anchor = GridBagConstraints.EAST;
        gc.gridx = 1;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(0, 10, 0, 0);
        btn_prima = new JButton();
        btn_prima.setIcon(new ImageIcon(this.getClass().getResource("/images/back.png")));
        toolbar.add(btn_prima,gc);
        btn_prima.setFocusPainted(false);
        btn_prima.addActionListener(this);
        btn_prima.setToolTipText("Prima");

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
        JLabel lbl = new JLabel("Ricerca");
        lbl.setFont(new Font(FONT_FAMILY, Font.BOLD, 18));
        filterField.setBackground(DesktopRender.JTF_COLOR);
        filterField.setFont(font);
        filterField.setBorder(new LineBorder(Color.BLACK));

        btn_add = new JButton(DesktopRender.formatButton("+ New"));
        btn_update = new JButton(DesktopRender.formatButton("Update"));
        btn_remove = new JButton(DesktopRender.formatButton("Remove"));

        formatButton(btn_add);
        formatButton(btn_update);
        formatButton(btn_remove);

        searchPane.add(lbl, c);
        searchPane.add(filterField, c);
        searchPane.add(btn_add, c);
        searchPane.add(btn_update, c);
        searchPane.add(btn_remove, c);

        wrapperPane.add(searchPane, BorderLayout.NORTH);
        wrapperPane.add(clientPane, BorderLayout.CENTER);

        btn_remove.addActionListener(e -> deleteFornitore());
    }

    void buildArticleDetails() {
        String[] header = {"ID", "Nome", "Cognome|RS", "Indirizzo", "Comune", "PIVA", "Mail", "Telefono", "Fax", "Sito web", "Note"};
        tableModel = new DefaultTableModel(new Object[][]{}, header) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        int i=0;
        for (Fornitore fornitore : loadFornitore()) {
            tableModel.addRow(new String[]{String.valueOf(++i), fornitore.getNome(), fornitore.getCognome(), fornitore.getIndirizzo(), fornitore.getComune(), fornitore.getPiva(), fornitore.getMail(), fornitore.getTelefono(), fornitore.getFax(), fornitore.getWebsite(), fornitore.getNote()});
        }

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

        filterField = RowFilterUtil.createRowFilter(table);
        filterField.setColumns(20);
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

        scrollPane = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(1150, 420));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        scrollPane.getViewport().setBackground(table.getBackground());

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
        btn.setPreferredSize(new Dimension(180, 40));
    }


    public Fornitore getSelectedFornitore() {
        Fornitore fornitore = new Fornitore();
        if (table.getSelectedRow() >= 0) {
            int index = table.getSelectedRow();
            fornitore.setNome(String.valueOf(table.getValueAt(index, 1)));
            fornitore.setCognome(String.valueOf(table.getValueAt(index, 2)));
            fornitore.setIndirizzo(String.valueOf(table.getValueAt(index, 3)));
            fornitore.setComune(String.valueOf(table.getValueAt(index, 4)));
            fornitore.setPiva(String.valueOf(table.getValueAt(index, 5)));
            fornitore.setMail(String.valueOf(table.getValueAt(index, 6)));
            fornitore.setTelefono(String.valueOf(table.getValueAt(index, 7)));
            fornitore.setFax(String.valueOf(table.getValueAt(index, 8)));
            fornitore.setWebsite(String.valueOf(table.getValueAt(index, 9)));
            fornitore.setNote(String.valueOf(table.getValueAt(index, 10)));
        }
        return fornitore;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn_prima) {
            container.removeAll();
            container.revalidate();
            container.add(new AnagraficaPane().getPanel());
            container.repaint();
        } else if (e.getSource() == btn_add) {
            table.getSelectionModel().clearSelection();
            new FornitorePaneEdit();
        } else if (e.getSource() == btn_update) {
            if (table.getSelectedRow() == -1) {
                showMessageDialog(null, "Selezionare il fornitore", "Info Dialog", JOptionPane.ERROR_MESSAGE);
            } else
                new FornitorePaneUPD(getSelectedFornitore());
        }
    }

}
