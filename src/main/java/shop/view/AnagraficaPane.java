package shop.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import shop.utils.CreateRoundButton;
import shop.utils.DesktopRender;

public class AnagraficaPane extends AContainer implements ActionListener {

    // Le funzionalita dell'app
    private JPanel panel;
    private JButton btn_articolo;
    private JButton btn_cliente;

    public AnagraficaPane() {

        initPanel();
    }

    public void initPanel() {
        // Pulsanti
        btn_articolo = new CreateRoundButton(DesktopRender.formatButton("Gestione", "Articoli"));
        btn_cliente = new CreateRoundButton(DesktopRender.formatButton("Gestione", "Clienti"));

        // Pannello interno
        panel = new JPanel();

        // Font dei pulsanti
        Font font = new Font("HelveticaNeue", Font.BOLD, 30);

        panel.setBounds(150, 105, 825, 625);
        Border whiteline = BorderFactory.createLineBorder(Color.WHITE);
        panel.setBorder(whiteline);
        panel.setBackground(new Color(128, 0, 128));


        // Pulsante dei HostMonitors
        btn_articolo.setPreferredSize(new Dimension( 260, 260));
        btn_articolo.setBackground(new Color(0, 128, 128));
        btn_articolo.setFont(font);
        btn_articolo.setForeground(Color.WHITE);
        btn_articolo.setBorder(new LineBorder(Color.BLACK));
        btn_articolo.setFocusPainted(false);
        btn_articolo.addActionListener(this);

        // Pulsante per il patching
        btn_cliente.setPreferredSize(new Dimension( 260, 260));
        btn_cliente.setBackground(new Color(39, 55, 70));
        btn_cliente.setFont(font);
        btn_cliente.setForeground(Color.WHITE);
        btn_cliente.setBorder(new LineBorder(Color.BLACK));
        btn_cliente.setFocusPainted(false);
        btn_cliente.addActionListener(this);

        panel.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        // first column of the grid//
        gc.anchor = GridBagConstraints.EAST;
        gc.weightx = 0.5;
        gc.weighty = 0.5;

        gc.gridx = 0;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(5, 10, 10, 10);
        panel.add(btn_articolo, gc);


        // second column//
        gc.anchor = GridBagConstraints.WEST;
        gc.gridx = 3;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.LINE_START;
        panel.add(btn_cliente, gc);

        container.add(panel);

        container.setLayout(new BorderLayout());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        container.removeAll();
        container.revalidate();
        if (e.getSource() == btn_articolo)
            container.add(new ArticoloPane().getPanel());
        else if (e.getSource() == btn_cliente)
            container.add(new ClientePane().getPanel());

        container.repaint();
    }

}
