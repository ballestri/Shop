package shop.view;

import shop.utils.DesktopRender;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.geom.RoundRectangle2D;
import java.text.SimpleDateFormat;
import java.util.stream.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.table.DefaultTableModel;

import static shop.dao.ArticleDAO.loadArticle;
import static shop.dao.ClienteDAO.loadCliente;
import static shop.utils.DesktopRender.*;


public class GestionePane extends AContainer implements ActionListener {

    public static final Color SELECTED_BG = new Color(224, 122, 95);
    //public static final Color UNSELECTED_BG = new Color(162, 210, 255);
    public static final Color UNSELECTED_BG = new Color(162, 214, 249);


    // Le funzionalita dell'app
    protected JButton btn_prima, btn_close;
    protected Font font;
    protected JToolBar toolbar;
    protected JTabbedPane tabbedPane;
    public static DefaultTableModel tableModelArticolo,tableModelCliente;

    public GestionePane() {
        initPanel();
    }

    public void initPanel() {

        ToolTipManager.sharedInstance().setInitialDelay(500);
        ToolTipManager.sharedInstance().setDismissDelay(4000);

        // Toolbar
        // I pulsanti della Toolbar
        toolbar = new JToolBar();
        btn_prima = new JButton();
        btn_prima.setIcon(new ImageIcon(getClass().getResource("/images/prima.png")));
        btn_prima.setPreferredSize(new Dimension(48, 48));
        btn_prima.setFocusPainted(false);
        btn_prima.setToolTipText("Prima");
        btn_prima.setContentAreaFilled(true);
        btn_prima.setOpaque(true);
        toolbar.add(btn_prima);
        toolbar.addSeparator();

        btn_close = new JButton();
        btn_close.setIcon(new ImageIcon(this.getClass().getResource("/images/esci.png")));
        btn_close.setPreferredSize(new Dimension(48, 48));
        btn_close.setFocusPainted(false);
        btn_close.setContentAreaFilled(true);
        btn_close.setToolTipText("Chiudi");
        btn_close.setOpaque(true);
        toolbar.add(btn_close);
        toolbar.addSeparator();

        btn_prima.addActionListener(this);
        btn_close.addActionListener(evt -> System.exit(0));

        UIManager.put("TabbedPane.tabInsets", new Insets(12, 10, 12, 10));
        UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
        UIManager.put("TabbedPane.tabAreaInsets", new Insets(0, 0, 0, 0));
        UIManager.put("TabbedPane.selectedLabelShift", 0);
        UIManager.put("TabbedPane.labelShift", 0);

        buildTableModel();

        tabbedPane = new JTabbedPane(SwingConstants.LEFT);
        tabbedPane.setOpaque(true);
        tabbedPane.setForeground(Color.WHITE);
        tabbedPane.setBackground(UNSELECTED_BG);
        tabbedPane.setTabPlacement(SwingConstants.LEFT);
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.addTab(DesktopRender.formatButton("Inventory", "System"), new ImageIcon(getClass().getResource("/images/logo.png")), new JPanel());
        tabbedPane.addTab(DesktopRender.formatButton("Dashboard"), new ImageIcon(getClass().getResource("/images/home.png")), new JPanel());
        tabbedPane.addTab(DesktopRender.formatButton("Gestione", "Magazzino"), new ImageIcon(getClass().getResource("/images/magazzino.png")), new MagazzinoPane().getPanel());
        tabbedPane.addTab(DesktopRender.formatButton("Gestione", "Clienti"), new ImageIcon(getClass().getResource("/images/clienti.png")), new ClientePane().getPanel());
        tabbedPane.addTab(DesktopRender.formatButton("Gestione", "Fornitori"), new ImageIcon(getClass().getResource("/images/fornitori.png")), new FornitorePane().getPanel());
        tabbedPane.addTab(DesktopRender.formatButton("Vendite"), new ImageIcon(getClass().getResource("/images/vendita.png")), new OrderPane().getPanel());
        tabbedPane.addTab(DesktopRender.formatButton("Contabilita'"), new ImageIcon(getClass().getResource("/images/contabilita.png")), new JPanel());
        tabbedPane.addTab(DesktopRender.formatButton("Report"), new ImageIcon(getClass().getResource("/images/report.png")), new JPanel());
        tabbedPane.setFont(new Font(FONT_FAMILY, Font.BOLD, 20));
        toolbar.setFloatable(false);
        toolbar.setBorderPainted(true);

        container.setLayout(new BorderLayout());
        container.add(toolbar, BorderLayout.NORTH);
        container.add(tabbedPane, BorderLayout.CENTER);

        tabbedPane.addMouseWheelListener(e -> {
            JTabbedPane pane = (JTabbedPane) e.getSource();
            int units = e.getWheelRotation();
            int oldIndex = pane.getSelectedIndex();
            int newIndex = oldIndex + units;
            if (newIndex < 0)
                pane.setSelectedIndex(0);
            else if (newIndex >= pane.getTabCount())
                pane.setSelectedIndex(pane.getTabCount() - 1);
            else
                pane.setSelectedIndex(newIndex);
        });

        tabbedPane.setUI(new CustomMainMenuTabs());
        MouseMotionListener listener = new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                JTabbedPane tabbedPane = (JTabbedPane) e.getSource();
                if (findTabPaneIndex(e.getPoint(), tabbedPane) > -1) {
                    tabbedPane.setCursor(new Cursor((Cursor.HAND_CURSOR)));
                } else {
                    tabbedPane.setCursor(new Cursor((Cursor.DEFAULT_CURSOR)));
                }
            }
        };
        tabbedPane.addMouseMotionListener(listener);
        tabbedPane.setBackground(container.getBackground());
        tabbedPane.addChangeListener(e -> {

            switch (tabbedPane.getSelectedIndex()) {
                case 2:
                    tableModelArticolo.getDataVector().removeAllElements();
                    tableModelArticolo.fireTableDataChanged();
                    loadArticle().forEach(article -> tableModelArticolo.addRow(new String[]{String.valueOf(article.getUID()), String.valueOf(article.getCodice()), article.getDescrizione(), article.getCategoria().getCategoria(),
                            article.getPosizione().getPosizione(), article.getUnita().getUnita(), formatMoney(article.getPrezzo()),
                            String.valueOf(article.getScorta()), article.getProvenienza(), (new SimpleDateFormat(DATE_FORMAT)).format(article.getDataIns())}));
                    break;
                case 3:
                    tableModelCliente.getDataVector().removeAllElements();
                    tableModelCliente.fireTableDataChanged();
                    loadCliente().forEach(cl -> tableModelCliente.addRow(new String[]{String.valueOf(cl.getUID()), cl.getCodice(), cl.getCognome(), cl.getNome(), cl.getTelefono(), cl.getEmail(), cl.getIndirizzo(), cl.getComune()}));
                    break;
                case 5:
                    tableModelCliente.getDataVector().removeAllElements();
                    tableModelCliente.fireTableDataChanged();
                    loadCliente().forEach(cl -> tableModelCliente.addRow(new String[]{String.valueOf(cl.getUID()), cl.getCodice(), cl.getCognome(), cl.getNome(), cl.getTelefono(), cl.getEmail(), cl.getIndirizzo(), cl.getComune()}));

                    tableModelArticolo.getDataVector().removeAllElements();
                    tableModelArticolo.fireTableDataChanged();
                    loadArticle().forEach(article -> tableModelArticolo.addRow(new String[]{String.valueOf(article.getUID()), String.valueOf(article.getCodice()), article.getDescrizione(), article.getCategoria().getCategoria(),
                            article.getPosizione().getPosizione(), article.getUnita().getUnita(), formatMoney(article.getPrezzo()),
                            String.valueOf(article.getScorta()), article.getProvenienza(), (new SimpleDateFormat(DATE_FORMAT)).format(article.getDataIns())}));
                    break;
            }
        });
    }


    void buildTableModel(){

        tableModelArticolo = new DefaultTableModel(new Object[][]{}, new String[]{"UID", "Codice", "Descrizione", "Categoria", "Posizione", "Unita'", "Prezzo", "Scorta", "Provenienza", "Data inserimento"}) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableModelCliente = new DefaultTableModel(new Object[][]{},new String[] {"UID", "Cliente ID", "Cognome", "Nome", "Telefono", "Email", "Indirizzo", "Comune"}) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }


    public static class CustomMainMenuTabs extends BasicTabbedPaneUI {

        protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
            Graphics2D g2 = (Graphics2D) g;
            Color color = new Color(39, 55, 70);
            if (tabIndex > 0) {

                if (isSelected) {
                    color = SELECTED_BG;
                } else if (getRolloverTab() == tabIndex) {
                    color = UNSELECTED_BG;
                } else {
                    color = UNSELECTED_BG;
                }
            }
            g2.setPaint(color);
            g2.fill(new RoundRectangle2D.Double(x, y, w, h, 30, 30));
        }

        protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
            g.setColor(new Color(116, 142, 203));
            g.drawRect(x, y, w, h);
        }

        protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect, boolean isSelected) {
        }
    }

    private static int findTabPaneIndex(Point p, JTabbedPane tabbedPane) {
        return IntStream.range(0, tabbedPane.getTabCount()).filter(i -> tabbedPane.getBoundsAt(i).contains(p.x, p.y)).findFirst().orElse(-1);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        container.removeAll();
        if (e.getSource() == btn_prima)
            container.add(new LoginPane().getPanel());
        container.doLayout();
        container.revalidate();
        container.repaint();
    }
}
