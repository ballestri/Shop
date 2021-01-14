package shop.view.fornitore;

import static shop.utils.DesktopRender.*;

import shop.utils.DesktopRender;
import shop.utils.RoundedPanel;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

import static shop.view.ClientePane.table;
import static shop.view.fornitore.controller.FornitoreDbOperation.insertFornitoreToDB;

public class FornitorePane extends JFrame implements ActionListener {

    private static final int WIDTH = 640;
    private static final int HEIGHT = 840;
    Font font;

    JPanel wrapperPane, actionPane;
    RoundedPanel infoPane, internPane;
    protected JLabel lblNome, lblCognome, lblIndirizzo, lblComune, lblPiva, lblMail, lblTelefono, lblFax, lblSito, lblNote;
    public static JTextField jtfNome, jtfCognome, jtfIndirizzo, jtfComune, jtfPiva, jtfMail, jtfTelefono, jtfFax, jtfSito;
    protected JButton btn_save, btn_clear;
    public static JTextArea jtaNote;

    public FornitorePane() {

        setTitle("Anagrafica Fornitori");
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        Dimension size = new Dimension(new Dimension(WIDTH, HEIGHT));
        setSize(size);
        setPreferredSize(size);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = getSize();
        setLocation(new Point((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2));
        setIconImage(new ImageIcon(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource("images/ico.png"))).getImage());
        ToolTipManager.sharedInstance().setInitialDelay(500);
        ToolTipManager.sharedInstance().setDismissDelay(4000);
        JToolBar toolbar = new JToolBar();

        JButton btn_close = new JButton();
        btn_close.setIcon(new ImageIcon(this.getClass().getResource("/images/esci.png")));
        toolbar.add(btn_close);
        btn_close.setFocusPainted(false);
        btn_close.setToolTipText("Chiudi");
        toolbar.addSeparator();
        btn_close.addActionListener(e -> dispose());

        font = new Font("HelveticaNeue", Font.BOLD, 17);

        wrapperPane = new JPanel();
        internPane = new RoundedPanel();
        actionPane = new JPanel();
        infoPane = new RoundedPanel();

        initComponents();

        add(wrapperPane);
        getContentPane().setBackground(new Color(116, 142, 203));
        toolbar.setFloatable(false);
        setLayout(new BorderLayout());
        add(toolbar, BorderLayout.NORTH);
        setVisible(true);
    }

    void initComponents() {
        wrapperPane.setBounds(20, 90, WIDTH - 40, HEIGHT - 160);
        internPane.setPreferredSize(new Dimension(WIDTH - 80, HEIGHT - 340));
        infoPane.setPreferredSize(new Dimension(WIDTH - 80, 60));

        wrapperPane.setBackground(new Color(39, 55, 70));
        Border line = BorderFactory.createLineBorder(Color.WHITE);
        Border empty = new EmptyBorder(5, 10, 5, 10);
        CompoundBorder border = new CompoundBorder(line, empty);
        wrapperPane.setBorder(border);

        buildFornitore();

        wrapperPane.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 15));
        wrapperPane.add(infoPane);
        wrapperPane.add(internPane);
        wrapperPane.add(actionPane);
    }

    void buildFornitore() {
        infoPane.setLayout(new GridBagLayout());
        JLabel lblFormName = new JLabel("Anagrafica fornitori");
        lblFormName.setFont(new Font("HelveticaNeue", Font.BOLD, 18));
        infoPane.add(lblFormName);

        // pannello interno
        lblNome = new JLabel("Nome");
        lblNome.setFont(font);

        jtfNome = new JTextField(18);
        jtfNome.setCaretColor(Color.BLACK);
        jtfNome.setBackground(JTF_COLOR);
        jtfNome.setBorder(new LineBorder(Color.BLACK));
        jtfNome.setFont(font);

        lblCognome = new JLabel("Cognome | Ragione soc");
        lblCognome.setFont(font);

        jtfCognome = new JTextField(18);
        jtfCognome.setCaretColor(Color.BLACK);
        jtfCognome.setBackground(JTF_COLOR);
        jtfCognome.setBorder(new LineBorder(Color.BLACK));
        jtfCognome.setFont(font);

        lblIndirizzo = new JLabel("Indirizzo");
        lblIndirizzo.setFont(font);

        jtfIndirizzo = new JTextField(18);
        jtfIndirizzo.setCaretColor(Color.BLACK);
        jtfIndirizzo.setBackground(JTF_COLOR);
        jtfIndirizzo.setBorder(new LineBorder(Color.BLACK));
        jtfIndirizzo.setFont(font);

        lblComune = new JLabel("Comune");
        lblComune.setFont(font);

        jtfComune = new JTextField(18);
        jtfComune.setCaretColor(Color.BLACK);
        jtfComune.setBackground(JTF_COLOR);
        jtfComune.setBorder(new LineBorder(Color.BLACK));
        jtfComune.setFont(font);

        lblPiva = new JLabel("Partita IVA");
        lblPiva.setFont(font);

        jtfPiva = new JTextField(18);
        jtfPiva.setCaretColor(Color.BLACK);
        jtfPiva.setBackground(JTF_COLOR);
        jtfPiva.setBorder(new LineBorder(Color.BLACK));
        jtfPiva.setFont(font);


        lblMail = new JLabel("Mail");
        lblMail.setFont(font);

        jtfMail = new JTextField(18);
        jtfMail.setCaretColor(Color.BLACK);
        jtfMail.setBackground(JTF_COLOR);
        jtfMail.setBorder(new LineBorder(Color.BLACK));
        jtfMail.setFont(font);

        lblTelefono = new JLabel("Telefono");
        lblTelefono.setFont(font);

        jtfTelefono = new JTextField(18);
        jtfTelefono.setCaretColor(Color.BLACK);
        jtfTelefono.setBackground(JTF_COLOR);
        jtfTelefono.setBorder(new LineBorder(Color.BLACK));
        jtfTelefono.setFont(font);

        lblFax = new JLabel("Fax");
        lblFax.setFont(font);

        jtfFax = new JTextField(18);
        jtfFax.setCaretColor(Color.BLACK);
        jtfFax.setBackground(JTF_COLOR);
        jtfFax.setBorder(new LineBorder(Color.BLACK));
        jtfFax.setFont(font);

        lblSito = new JLabel("Website");
        lblSito.setFont(font);

        jtfSito = new JTextField(18);
        jtfSito.setCaretColor(Color.BLACK);
        jtfSito.setBackground(JTF_COLOR);
        jtfSito.setBorder(new LineBorder(Color.BLACK));
        jtfSito.setFont(font);

        lblNote = new JLabel("Note");
        lblNote.setFont(font);


        jtaNote = new JTextArea(3, 18);
        jtaNote.setLineWrap(true);
        jtaNote.setWrapStyleWord(true);

        jtaNote.setCaretColor(Color.BLACK);
        jtaNote.setBackground(JTF_COLOR);
        jtaNote.setBorder(new LineBorder(Color.BLACK));
        jtaNote.setFont(font);


        internPane.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        // first column
        gc.anchor = GridBagConstraints.EAST;
        gc.weightx = 1;
        gc.weighty = 1;

        gc.gridx = 0;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(2, 10, 2, 10);
        internPane.add(lblNome, gc);

        gc.gridx = 0;
        gc.gridy = 1;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(2, 10, 2, 10);
        internPane.add(lblCognome, gc);

        gc.gridx = 0;
        gc.gridy = 2;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(2, 10, 2, 10);
        internPane.add(lblIndirizzo, gc);

        gc.gridx = 0;
        gc.gridy = 3;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(2, 10, 2, 10);
        internPane.add(lblComune, gc);

        gc.gridx = 0;
        gc.gridy = 4;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(2, 10, 2, 10);
        internPane.add(lblPiva, gc);

        gc.gridx = 0;
        gc.gridy = 5;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(2, 10, 2, 10);
        internPane.add(lblMail, gc);

        gc.gridx = 0;
        gc.gridy = 6;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(2, 10, 2, 10);
        internPane.add(lblTelefono, gc);

        gc.gridx = 0;
        gc.gridy = 7;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(2, 10, 2, 10);
        internPane.add(lblFax, gc);

        gc.gridx = 0;
        gc.gridy = 8;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(2, 10, 2, 10);
        internPane.add(lblSito, gc);

        gc.gridx = 0;
        gc.gridy = 9;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(2, 10, 2, 10);
        internPane.add(lblNote, gc);

        // second column//
        gc.anchor = GridBagConstraints.WEST;
        gc.gridx = 1;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.LINE_START;
        internPane.add(jtfNome, gc);

        gc.gridx = 1;
        gc.gridy = 1;

        gc.anchor = GridBagConstraints.LINE_START;
        internPane.add(jtfCognome, gc);

        gc.gridx = 1;
        gc.gridy = 2;

        gc.anchor = GridBagConstraints.LINE_START;
        internPane.add(jtfIndirizzo, gc);

        gc.gridx = 1;
        gc.gridy = 3;

        gc.anchor = GridBagConstraints.LINE_START;
        internPane.add(jtfComune, gc);

        gc.gridx = 1;
        gc.gridy = 4;

        gc.anchor = GridBagConstraints.LINE_START;
        internPane.add(jtfPiva, gc);

        gc.gridx = 1;
        gc.gridy = 5;

        gc.anchor = GridBagConstraints.LINE_START;
        internPane.add(jtfMail, gc);

        gc.gridx = 1;
        gc.gridy = 6;

        gc.anchor = GridBagConstraints.LINE_START;
        internPane.add(jtfTelefono, gc);

        gc.gridx = 1;
        gc.gridy = 7;

        gc.anchor = GridBagConstraints.LINE_START;
        internPane.add(jtfFax, gc);

        gc.gridx = 1;
        gc.gridy = 8;

        gc.anchor = GridBagConstraints.LINE_START;
        internPane.add(jtfSito, gc);

        gc.gridx = 1;
        gc.gridy = 9;

        gc.anchor = GridBagConstraints.LINE_START;
        internPane.add(jtaNote, gc);

        btn_save = new JButton(DesktopRender.formatButton("Save"));
        btn_clear = new JButton(DesktopRender.formatButton("Clear"));

        formatButton(btn_clear);
        formatButton(btn_save);

        actionPane.setBackground(wrapperPane.getBackground());
        actionPane.setLayout(new GridBagLayout());


        GridBagConstraints ca = new GridBagConstraints();
        ca.insets = new Insets(5, 10, 15, 28);

        actionPane.add(btn_clear, ca);
        actionPane.add(btn_save, ca);

        btn_clear.addActionListener(e -> initFornitorePane());
        btn_save.addActionListener(e -> {
            insertFornitoreToDB();
            dispose();
        });
    }

    void formatButton(JButton btn) {
        btn.setFont(font);
        btn.setForeground(Color.WHITE);
        btn.setBorder(new LineBorder(Color.WHITE));
        btn.setBackground(new Color(0, 128, 128));
        btn.setFocusPainted(false);
        btn.addActionListener(this);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160, 40));
    }

    public static void initFornitorePane() {
        table.getSelectionModel().clearSelection();
        jtfNome.setText(null);
        jtfCognome.setText(null);
        jtfIndirizzo.setText(null);
        jtfComune.setText(null);
        jtfPiva.setText(null);
        jtfMail.setText(null);
        jtfTelefono.setText(null);
        jtfFax.setText(null);
        jtfSito.setText(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
