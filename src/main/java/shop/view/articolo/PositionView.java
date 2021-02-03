package shop.view.articolo;

import org.hibernate.query.NativeQuery;
import shop.controller.article.RendererHighlighted;
import shop.controller.article.RowFilterUtil;
import shop.dao.JPAProvider;
import shop.entity.Posizione;
import shop.utils.*;

import javax.persistence.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

import static java.util.Objects.*;
import static shop.dao.PositionDAO.*;
import static shop.utils.DesktopRender.*;
import static shop.view.ArticoloPane.*;

public class PositionView extends JFrame implements ActionListener {

    private static final int WIDTH = 480;
    private static final int HEIGHT = 650;

    protected JButton btn_add, btn_salva, btn_remove, btn_update, btn_refresh;
    JScrollPane scrollPane;
    JPanel internPanel, headerPanel, tablePane, informationPane;
    RoundedPanel searchPanel;
    public static DefaultTableModel tableModel;
    public static JTable table;
    JTableHeader tableHeader;
    protected JTextField filterField;
    public static JTextField fieldPosition, jtfAmount;
    private final Font font;

    public PositionView(String attribute) {

        setTitle("Gestione ".concat(attribute));
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        Dimension size = new Dimension(new Dimension(WIDTH, HEIGHT));
        setSize(size);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = getSize();
        setLocation(new Point((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2));
        setIconImage(new ImageIcon(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource("images/ico.png"))).getImage());

        ToolTipManager.sharedInstance().setInitialDelay(500);
        ToolTipManager.sharedInstance().setDismissDelay(4000);
        // I pulsanti della Toolbar
        JToolBar toolbar = new JToolBar();

        JButton btn_close = new JButton();
        btn_close.setIcon(new ImageIcon(this.getClass().getResource("/images/esci.png")));
        toolbar.add(btn_close);
        btn_close.setFocusPainted(false);
        btn_close.setToolTipText("Chiudi");
        toolbar.addSeparator();
        btn_close.addActionListener(e -> dispose());

        font = new Font(FONT_FAMILY, Font.BOLD, 16);
        internPanel = new JPanel();
        headerPanel = new JPanel();
        searchPanel = new RoundedPanel();
        tablePane = new JPanel();
        informationPane= new JPanel();

        initComponents();
        toolbar.setFloatable(false);
        setLayout(new BorderLayout());
        add(toolbar, BorderLayout.NORTH);
        add(internPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    void initComponents() {
        internPanel.setBounds(20, 80, WIDTH - 20, HEIGHT - 20);
        internPanel.setBackground(new Color(116, 142, 203));
        internPanel.setPreferredSize(new Dimension(WIDTH - 20, HEIGHT - 20));
        headerPanel.setPreferredSize(new Dimension(WIDTH - 20, 60));
        searchPanel.setPreferredSize(new Dimension(WIDTH - 20, 65));
        tablePane.setPreferredSize(new Dimension(WIDTH - 20, HEIGHT - 280));
        informationPane.setPreferredSize(new Dimension(WIDTH - 20, 60));

        headerPanel.setBackground(internPanel.getBackground());
        tablePane.setBackground(internPanel.getBackground());
        internPanel.setLayout(new FlowLayout());

        createHeaderArea();
        createSearchArea();
        createTablePane();
        createInformationPane();

        internPanel.add(headerPanel);
        internPanel.add(searchPanel);
        internPanel.add(tablePane);
        table.getSelectionModel().addListSelectionListener(e -> getPositionAmount());
        internPanel.add(informationPane);
    }

    void createHeaderArea() {

        fieldPosition = new JTextField();
        fieldPosition.setCaretColor(Color.BLACK);
        fieldPosition.setBackground(JTF_COLOR);
        fieldPosition.setFont(font);
        fieldPosition.setBorder(new LineBorder(Color.BLACK));
        fieldPosition.setPreferredSize(new Dimension(320, 35));
        fieldPosition.setVisible(false);

        btn_add = new JButton("+ Add");
        btn_add.setFont(new Font(FONT_FAMILY, Font.BOLD, 16));
        btn_add.setForeground(Color.WHITE);
        btn_add.setBorder(new LineBorder(Color.BLACK));
        btn_add.setBackground(new Color(0, 128, 128));
        btn_add.setFocusPainted(false);
        btn_add.setPreferredSize(new Dimension(105, 35));
        btn_add.addActionListener(this);

        btn_salva = new JButton("Salva");
        btn_salva.setFont(new Font(FONT_FAMILY, Font.BOLD, 16));
        btn_salva.setForeground(Color.WHITE);
        btn_salva.setBorder(new LineBorder(Color.BLACK));
        btn_salva.setBackground(new Color(0, 128, 128));
        btn_salva.setFocusPainted(false);
        btn_salva.setPreferredSize(new Dimension(105, 35));
        btn_salva.addActionListener(this);

        headerPanel.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        gc.anchor = GridBagConstraints.EAST;
        gc.weightx = 0.5;
        gc.weighty = 0.5;

        gc.gridx = 0;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 5, 10, 10);

        headerPanel.add(btn_add, gc);
        headerPanel.add(btn_salva, gc);

        gc.anchor = GridBagConstraints.WEST;
        gc.gridx = 1;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.CENTER;
        gc.insets = new Insets(5, 10, 10, 10);
        headerPanel.add(fieldPosition, gc);
    }

    void createSearchArea() {

        searchPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(2, 10, 2, 10);

        JLabel lbl = new JLabel("Ricerca");
        lbl.setFont(new Font(FONT_FAMILY, Font.BOLD, 16));

        String[] header = {"ID", "Posizione"};
        tableModel = new DefaultTableModel(new Object[][]{}, header) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        int i = 0;
        for (Posizione posizione : loadPosizione()) {
            tableModel.addRow(new String[]{String.valueOf(++i), posizione.getPosizione()});
        }
        table = new JTable(tableModel) {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                if (!component.getBackground().equals(getSelectionBackground())) {
                    component.setBackground((row % 2 == 0 ? new Color(88, 214, 141) : Color.WHITE));
                }

                if (column == 0) {
                    ((JLabel) component).setHorizontalAlignment(JLabel.CENTER);
                    component.setFont(font);
                }

                return component;
            }
        };

        tableHeader = table.getTableHeader();
        tableHeader.setBackground(new Color(39, 55, 70));
        tableHeader.setForeground(Color.WHITE);
        filterField = RowFilterUtil.createRowFilter(table);
        filterField.setColumns(16);
        filterField.setBackground(DesktopRender.JTF_COLOR);
        filterField.setFont(font);
        filterField.setBorder(new LineBorder(Color.BLACK));
        btn_refresh = new JButton(new ImageIcon(requireNonNull(ClassLoader.getSystemClassLoader().getResource("images/refresh.png"))));
        btn_refresh.setPreferredSize(new Dimension(48, 48));
        btn_refresh.setContentAreaFilled(false);
        btn_refresh.setOpaque(false);
        btn_refresh.addActionListener(this);

        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1;
        c.weighty = 1;

        c.gridx = 0;
        c.gridy = 0;

        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(2, 10, 2, 10);
        searchPanel.add(btn_refresh, c);

        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 1;
        c.gridy = 0;

        c.anchor = GridBagConstraints.LINE_END;
        c.insets = new Insets(2, 10, 2, 10);
        searchPanel.add(lbl, c);

        c.gridx = 2;
        c.gridy = 0;

        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(2, 5, 2, 5);
        searchPanel.add(filterField, c);
    }

    void createTablePane() {
        tablePane.setLayout(new BorderLayout());
        table.getTableHeader().setBackground(new Color(39, 55, 70));
        table.getTableHeader().setForeground(Color.WHITE);
        RendererHighlighted renderer = new RendererHighlighted(filterField);
        table.setDefaultRenderer(Object.class, renderer);
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.setFillsViewportHeight(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(new Font(FONT_FAMILY, Font.BOLD, 16));
        table.setFont(new Font(FONT_FAMILY, Font.PLAIN, 15));
        table.setRowHeight(25);
        table.setCursor(new Cursor(Cursor.HAND_CURSOR));
        table.setFillsViewportHeight(true);
        table.getTableHeader().setReorderingAllowed(false);

        table.getColumnModel().getColumn(0).setMinWidth(50);
        table.getColumnModel().getColumn(0).setMaxWidth(50);

        scrollPane = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(WIDTH - 140, HEIGHT - 220));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        scrollPane.getViewport().setBackground(table.getBackground());

        JPanel wrapper = new JPanel();
        wrapper.setPreferredSize(new Dimension(105, HEIGHT - 220));
        wrapper.setBackground(internPanel.getBackground());

        btn_update = new JButton("Update");
        btn_update.setFont(font);
        btn_update.setForeground(Color.WHITE);
        btn_update.setBorder(new LineBorder(Color.BLACK));
        btn_update.setBackground(new Color(0, 128, 128));
        btn_update.setFocusPainted(false);
        btn_update.addActionListener(this);
        btn_update.setPreferredSize(new Dimension(105, 35));

        btn_remove = new JButton("Delete");
        btn_remove.setFont(font);
        btn_remove.setForeground(Color.WHITE);
        btn_remove.setBorder(new LineBorder(Color.BLACK));
        btn_remove.setBackground(new Color(0, 128, 128));
        btn_remove.setFocusPainted(false);
        btn_remove.addActionListener(this);
        btn_remove.setPreferredSize(new Dimension(105, 35));

        //wrapper.add(btn_update);
        wrapper.add(btn_remove);

        tablePane.add(scrollPane, BorderLayout.WEST);
        tablePane.add(wrapper, BorderLayout.CENTER);
    }

    void createInformationPane() {
        informationPane.setBackground(internPanel.getBackground());
        JLabel lblFormName = new JLabel("Numero articoli: ");
        lblFormName.setFont(font);
        informationPane.add(lblFormName);
        jtfAmount = new JTextField();
        jtfAmount.setBackground(internPanel.getBackground());
        jtfAmount.setFont(font);
        jtfAmount.setBorder(null);
        jtfAmount.setText(null);
        informationPane.add(jtfAmount);
        informationPane.setVisible(false);
    }

    void getPositionAmount() {
        jtfAmount.setText(String.valueOf(getCountPositionArticolo(getSelectedPosizione().getPosizione())));
        informationPane.setVisible(true);
        internPanel.revalidate();
        internPanel.repaint();
    }


    public static Integer getCountPositionArticolo(String posizione) {
        EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        Query query = em.createNativeQuery("SELECT count(*) FROM ARTICOLO a WHERE a.isDeleted= false and a.posizione=:posizione")
                .setParameter("posizione", posizione)
                .unwrap(NativeQuery.class);
        Integer rowCnt = Integer.parseInt(String.valueOf(query.getSingleResult()));
        em.getTransaction().commit();
        em.clear();
        em.close();
        return rowCnt;
    }

    Posizione getSelectedPosizione() {
        Posizione posizione = new Posizione();
        if (table.getSelectedRow() >= 0) {
            int index = table.getSelectedRow();
            posizione.setPosizione(String.valueOf(table.getValueAt(index, 1)));
            posizione.setDeleted(false);

        }
        return posizione;
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btn_add) {
            fieldPosition.setVisible(true);
            fieldPosition.setText(null);
            btn_salva.setVisible(true);
            btn_add.setVisible(false);
        } else if (e.getSource() == btn_salva) {
            fieldPosition.setVisible(false);
            btn_salva.setVisible(false);
            btn_add.setVisible(true);
            insertPosizione();
            fieldPosition.setText(null);
        } else if (e.getSource() == btn_remove) {
            deletePosizione();
        } else if (e.getSource() == btn_refresh) {
            table.getSelectionModel().clearSelection();
            filterField.setText(null);
            informationPane.setVisible(false);
        }

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(getAllPosizione().toArray(new String[0]));
        jcbPosizione.setModel(model);
        jcbPosizione.validate();
        jcbPosizione.repaint();
    }
}
