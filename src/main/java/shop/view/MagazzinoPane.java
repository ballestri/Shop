package shop.view;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;
import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

public class MagazzinoPane extends AContainer implements ActionListener {

    public static final Color SELECTED_BG = new Color(128, 0, 128);
    public static final Color UNSELECTED_BG = new Color(17, 109, 91);
    protected JButton btn_prima, btn_close;
    protected JToolBar toolbar;
    protected JTabbedPane tabbedPane;

    public MagazzinoPane() {
        initPanel();
    }

    public void initPanel() {

        ToolTipManager.sharedInstance().setInitialDelay(500);
        ToolTipManager.sharedInstance().setDismissDelay(4000);

        // Toolbar
        // I pulsanti della Toolbar
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

        UIManager.put("TabbedPane.tabInsets", new Insets(12, 40, 12, 10));
        UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
        UIManager.put("TabbedPane.tabAreaInsets", new Insets(0, 0, 0, 0));
        UIManager.put("TabbedPane.selectedLabelShift", 0);
        UIManager.put("TabbedPane.labelShift", 0);

        tabbedPane = new JTabbedPane(SwingConstants.TOP) {
            @Override
            public void updateUI() {
                setOpaque(true);
                setForeground(Color.WHITE);
                setBackground(UNSELECTED_BG);
                setTabPlacement(LEFT);
                setTabLayoutPolicy(SCROLL_TAB_LAYOUT);
            }
        };

        ArrayList<String> list_actions = new ArrayList<>(Arrays.asList("ANAGRAFICA", "RILEVAZIONI", "GIACENZA", "STORICO", "MOVIMENTAZIONI", "RICHIESTE"));
        for (String action : list_actions)
            switch (action) {
                case "ANAGRAFICA":
                    tabbedPane.addTab(action, new AnagraficaPane().getPanel());
                    break;
                case "RILEVAZIONI":
                    tabbedPane.addTab(action, new RilevazionePane().getPanel());
                    break;
                case "GIACENZA":
                    tabbedPane.addTab(action, new GiacenzaPane().getPanel());
                    break;
                case "STORICO":
                    tabbedPane.addTab(action, new StoricoPane().getPanel());
                    break;
                case "MOVIMENTAZIONI":
                    tabbedPane.addTab(action, new MovimentiPane().getPanel());
                    break;
                default:
                    tabbedPane.addTab(action, new JPanel());
                    break;
            }

        tabbedPane.setFont(new Font("HelveticaNeue", Font.BOLD, 20));
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
    }

    public static class CustomMainMenuTabs extends BasicTabbedPaneUI {

        protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
            Graphics2D g2 = (Graphics2D) g;
            Color color;
            if (isSelected) {
                color = SELECTED_BG;
            } else if (getRolloverTab() == tabIndex) {
                color = UNSELECTED_BG;
            } else {
                color = UNSELECTED_BG;
            }
            g2.setPaint(color);
            g2.fill(new RoundRectangle2D.Double(x, y, w, h, 30, 30));
            //g2.fill(new Rectangle2D.Double(x + 100, y, w, h));
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
        container.revalidate();
        if (e.getSource() == btn_prima)
            container.add(new Pannello().getPanel());
        container.repaint();
    }
}
