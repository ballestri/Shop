package shop.view;


import shop.utils.DesktopRender;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static shop.utils.DesktopRender.FONT_FAMILY;

public class MagazzinoPane extends AContainer implements ActionListener {


    public static final Color SELECTED = new Color(224, 122, 95);
    public static final Color UNSELECTED = new Color(39, 55, 70);

    // pannello interno
    protected JPanel buttonPane;
    protected Font font;
    protected JButton btn_anagrafica, btn_carico, btn_giacenza, btn_movimentazione;

    public MagazzinoPane() {
        initPanel();
    }

    public void initPanel() {

        font = new Font(FONT_FAMILY, Font.BOLD, 20);

        // I pulsanti della Toolbar
        buttonPane = new JPanel();

        buildButtonPane();
        container.setLayout(new BorderLayout());
        container.add(buttonPane, BorderLayout.NORTH);
    }

    void buildButtonPane() {

        buttonPane.setBackground(container.getBackground());
        buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 15));

        btn_anagrafica = new JButton(DesktopRender.formatButton("Info articolo"));
        btn_carico = new JButton(DesktopRender.formatButton("Carico"));
        btn_giacenza = new JButton(DesktopRender.formatButton("Giacenza"));
        btn_movimentazione = new JButton(DesktopRender.formatButton("Movimentazione"));

        formatButton(btn_anagrafica);
        formatButton(btn_carico);
        formatButton(btn_giacenza);
        formatButton(btn_movimentazione);

        buttonPane.add(btn_anagrafica);
        buttonPane.add(btn_carico);
        buttonPane.add(btn_giacenza);
        buttonPane.add(btn_movimentazione);

        btn_anagrafica.addActionListener(this);
        btn_carico.addActionListener(this);
        btn_giacenza.addActionListener(this);
        btn_movimentazione.addActionListener(this);
    }


    void formatButton(JButton btn) {
        btn.setFont(font);
        btn.setForeground(Color.WHITE);
        btn.setBorder(new LineBorder(Color.BLACK));
        btn.setBackground(UNSELECTED);
        btn.setFocusPainted(false);
        btn.addActionListener(this);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(220, 60));
    }


    void unselectedButton() {
        btn_anagrafica.setBackground(UNSELECTED);
        btn_carico.setBackground(UNSELECTED);
        btn_giacenza.setBackground(UNSELECTED);
        btn_movimentazione.setBackground(UNSELECTED);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        container.removeAll();
        container.add(buttonPane);
        unselectedButton();
        container.revalidate();

        if (e.getSource() == btn_anagrafica) {
            container.add(new ArticoloPane().getPanel());
            btn_anagrafica.setBackground(SELECTED);
        } else if (e.getSource() == btn_carico) {
            container.add(new CaricoPane().getPanel());
            btn_carico.setBackground(SELECTED);
        } else if (e.getSource() == btn_giacenza) {
            container.add(new GiacenzaPane().getPanel());
            btn_giacenza.setBackground(SELECTED);
        } else if (e.getSource() == btn_movimentazione) {
            container.add(new MovimentiPane().getPanel());
            btn_movimentazione.setBackground(SELECTED);
        }
        container.revalidate();
        container.repaint();
    }

}
