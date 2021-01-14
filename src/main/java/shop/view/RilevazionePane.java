package shop.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import shop.utils.CreateRoundButton;
import shop.utils.DesktopRender;

public class RilevazionePane extends AContainer implements ActionListener {

    // Le funzionalita dell'app
    private JButton btn_carico;
    private JButton btn_scarico;
    private JPanel panel;

    public RilevazionePane() {

        initPanel();
    }

    public void initPanel() {
        // Pulsanti
        btn_carico = new CreateRoundButton(DesktopRender.formatButton("Gestione", "Carico"));
        btn_scarico = new CreateRoundButton(DesktopRender.formatButton("Gestione", "Scarico"));

        // Pannello interno
         panel = new JPanel();

        // Font dei pulsanti
        Font font = new Font("HelveticaNeue", Font.BOLD, 30);

        panel.setBounds(150, 105, 825, 625);
        Border whiteline = BorderFactory.createLineBorder(Color.WHITE);
        panel.setBorder(whiteline);
        panel.setBackground(new Color(128, 0, 128));


        // Pulsante dei HostMonitors
        btn_carico.setPreferredSize(new Dimension( 260, 260));
        btn_carico.setBackground(new Color(0, 128, 128));
        btn_carico.setFont(font);
        btn_carico.setForeground(Color.WHITE);
        btn_carico.setBorder(new LineBorder(Color.BLACK));
        btn_carico.setFocusPainted(false);
        btn_carico.addActionListener(this);

        // Pulsante per il patching
        btn_scarico.setPreferredSize(new Dimension( 260, 260));
        btn_scarico.setBackground(new Color(39, 55, 70));
        btn_scarico.setFont(font);
        btn_scarico.setForeground(Color.WHITE);
        btn_scarico.setBorder(new LineBorder(Color.BLACK));
        btn_scarico.setFocusPainted(false);
        btn_scarico.addActionListener(this);

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
        panel.add(btn_carico, gc);

        // second column//
        gc.anchor = GridBagConstraints.WEST;
        gc.gridx = 3;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.LINE_START;
        panel.add(btn_scarico, gc);

        container.add(panel);
        container.setLayout(new BorderLayout());
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        container.removeAll();
        container.revalidate();
        if (e.getSource() == btn_carico) {
            container.add(new CaricoPane().getPanel());
            CaricoPane.table.revalidate();
            CaricoPane.table.repaint();
        }else if (e.getSource() == btn_scarico) {
            container.add(new ScaricoPane().getPanel());
            ScaricoPane.table.revalidate();
            ScaricoPane.table.repaint();
        }
        container.repaint();

    }

}
