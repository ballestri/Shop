package shop.view;

import shop.utils.DesktopRender;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import shop.utils.CreateRoundButton;

import static shop.utils.DesktopRender.FONT_FAMILY;

public class GestionePane extends AContainer implements ActionListener {

    // Le funzionalita dell'app
    protected JButton btn_anagrafica,btn_contabilita,btn_prima,btn_close;
    protected Font font;
    protected JToolBar toolbar;
    protected JPanel panel;

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

        // Pulsanti
        btn_anagrafica = new CreateRoundButton(DesktopRender.formatButton("Anagrafica"));
        btn_contabilita = new CreateRoundButton(DesktopRender.formatButton("Contabilita"));

        // Pannello interno
        panel = new JPanel();

        // Font dei pulsanti
        font = new Font(FONT_FAMILY, Font.BOLD, 30);

        panel.setBounds(375, 160, 825, 625);
        Border whiteline = BorderFactory.createLineBorder(Color.WHITE);
        panel.setBorder(whiteline);
        panel.setBackground(new Color(128, 0, 128));

        // Pulsante dei HostMonitors
        btn_anagrafica.setPreferredSize(new Dimension(260, 260));
        btn_anagrafica.setBackground(new Color(0, 128, 128));
        btn_anagrafica.setFont(font);
        btn_anagrafica.setForeground(Color.WHITE);
        btn_anagrafica.setBorder(new LineBorder(Color.BLACK));
        btn_anagrafica.setFocusPainted(false);
        btn_anagrafica.addActionListener(this);

        // Pulsante per il patching
        btn_contabilita.setPreferredSize(new Dimension(260, 260));
        btn_contabilita.setBackground(new Color(39, 55, 70));
        btn_contabilita.setFont(font);
        btn_contabilita.setForeground(Color.WHITE);
        btn_contabilita.setBorder(new LineBorder(Color.BLACK));
        btn_contabilita.setFocusPainted(false);
        btn_contabilita.addActionListener(this);

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
        panel.add(btn_anagrafica, gc);

        // second column//
        gc.anchor = GridBagConstraints.WEST;
        gc.gridx = 3;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.LINE_START;
        panel.add(btn_contabilita, gc);

        // aggiungo i pulsanti al pannello interno
        container.add(panel);
        toolbar.setFloatable(false);
        container.setLayout(new BorderLayout());
        container.add(toolbar, BorderLayout.NORTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        container.removeAll();
        container.revalidate();
        if (e.getSource() == btn_anagrafica)
            container.add(new Pannello().getPanel());
        else if (e.getSource() == btn_prima)
            container.add(new LoginPane().getPanel());
        else if(e.getSource() == btn_contabilita)
            container.add(new JPanel());
        container.doLayout();
        container.repaint();
    }
}
