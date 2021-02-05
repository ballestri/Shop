package shop.view.rilevazione;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;
import shop.controller.*;
import shop.dao.DAOUtils;
import shop.dao.JPAProvider;
import shop.entity.Carico;
import shop.utils.DesktopRender;
import shop.utils.RoundedPanel;
import static shop.utils.DesktopRender.*;
import javax.persistence.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;

import static javax.swing.JOptionPane.*;
import static shop.utils.DesktopRender.formatButton;
import static shop.view.CaricoPane.*;
import static shop.dao.CaricoDAO.*;
public class CaricoPaneUPD extends JFrame implements ActionListener {

    private static final int WIDTH = 640;
    private static final int HEIGHT = 720;
    Font font;
    public Integer UID;
    public String COD;
    JPanel wrapperPane, actionPane;
    RoundedPanel infoPane, internPane;
    protected JLabel lblCodice, lblDescrizione, lblData, lblQuantita, lblFornitore, lblDatascadenza, lblNote;
    public static JTextField jtfDescrizione;
    public static JSpinner jspQuantita;
    public static JDateChooser jdcData, jdcDatascadenza;
    protected JButton btn_update, btn_clear;
    public static JTextArea jtaNote;
    public static JComboBox<String> jcbCodice, jcbFornitore;

    public CaricoPaneUPD(Carico carico) {

        setTitle("Informazioni Carico");
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

        font = new Font(DesktopRender.FONT_FAMILY, Font.BOLD, 16);

        wrapperPane = new JPanel();
        internPane = new RoundedPanel();
        actionPane = new JPanel();
        infoPane = new RoundedPanel();

        UID = carico.getUID();
        COD = carico.getCodice();
        initComponents();
        setElementsPane(carico);
        add(wrapperPane);
        getContentPane().setBackground(new Color(116, 142, 203));
        toolbar.setFloatable(false);
        setLayout(new BorderLayout());
        add(toolbar, BorderLayout.NORTH);
        setVisible(true);
    }

    void setElementsPane(Carico carico) {
        jcbCodice.setSelectedItem(carico.getCodice());
        jcbCodice.setEditable(false);
        jtfDescrizione.setText(carico.getDescrizione());
        jtfDescrizione.setEditable(false);
        jspQuantita.setValue(carico.getQuantita());
        jcbFornitore.setSelectedItem(carico.getFornitore());
        jdcData.setDate(carico.getDatacarico());
        jtaNote.setText(carico.getNote());
        jdcDatascadenza.setDate(carico.getDatascadenza());

    }

    void initComponents() {
        wrapperPane.setBounds(20, 90, WIDTH - 40, HEIGHT - 160);
        internPane.setPreferredSize(new Dimension(WIDTH - 200, HEIGHT - 350));
        infoPane.setPreferredSize(new Dimension(WIDTH - 200, 60));

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
        JLabel lblFormName = new JLabel("Operazioni di carico");
        lblFormName.setFont(new Font(DesktopRender.FONT_FAMILY, Font.BOLD, 18));
        infoPane.add(lblFormName);

        // pannello interno
        lblCodice = new JLabel("Codice prodotto");
        lblCodice.setFont(font);

        ArrayList<String> items = new ArrayList<>(Collections.singletonList(COD));
        jcbCodice = new JComboBox<>(items.toArray(new String[0]));
        jcbCodice.setBorder(new LineBorder(Color.BLACK));
        jcbCodice.setFont(font);
        jcbCodice.addActionListener(this);

        lblDescrizione = new JLabel("Descrizione");
        lblDescrizione.setFont(font);

        jtfDescrizione = new JTextField(16);
        jtfDescrizione.setCaretColor(Color.BLACK);
        jtfDescrizione.setBackground(DesktopRender.JTF_COLOR);
        jtfDescrizione.setBorder(new LineBorder(Color.BLACK));
        jtfDescrizione.setFont(font);

        lblData = new JLabel("Data carico");
        lblData.setFont(font);

        jdcData = new JDateChooser();
        jdcData.setDateFormatString(DesktopRender.DATE_FORMAT);
        jdcData.setPreferredSize(new Dimension(120, 30));

        jdcData.setFont(new Font(DesktopRender.FONT_FAMILY, Font.BOLD, 16));
        Date date = new Date();
        jdcData.setDate(date);
        jdcData.setMaxSelectableDate(new Date());
        JTextFieldDateEditor dateEditor = (JTextFieldDateEditor) jdcData.getComponent(1);
        dateEditor.setHorizontalAlignment(JTextField.RIGHT);
        dateEditor.setFont(font);
        dateEditor.setBackground(DesktopRender.JTF_COLOR);
        dateEditor.setBorder(new LineBorder(Color.BLACK));

        lblQuantita = new JLabel("Quantita' carico");
        lblQuantita.setFont(font);

        jspQuantita = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        jspQuantita.setBorder(new EmptyBorder(0, 5, 0, 0));
        jspQuantita.setBorder(new LineBorder(Color.BLACK));
        jspQuantita.setPreferredSize(new Dimension(120, 30));
        jspQuantita.setFont(font);
        JTextField jtfQuantita = ((JSpinner.DefaultEditor) jspQuantita.getEditor()).getTextField();
        jtfQuantita.setBackground(JTF_COLOR);
        jtfQuantita.setCaretColor(new Color(255, 255, 255));

        lblFornitore = new JLabel("Fornitore");
        lblFornitore.setFont(font);

        ArrayList<String> elements = DAOUtils.getAllFornitori();
        items.add(0, null);
        jcbFornitore = new JComboBox<>(elements.toArray(new String[0]));
        ComboBoxFilterDecorator<String> decorator = ComboBoxFilterDecorator.decorate(jcbFornitore, CaricoPaneUPD::fornitoreFilter);
        jcbFornitore.setRenderer(new CustomComboRenderer(decorator.getFilterLabel()));
        jcbFornitore.setBorder(new LineBorder(Color.BLACK));
        jcbFornitore.setFont(font);
        jcbFornitore.addActionListener(this);

        lblDatascadenza = new JLabel("Data Scadenza");
        lblDatascadenza.setFont(font);

        jdcDatascadenza = new JDateChooser();
        jdcDatascadenza.setDateFormatString(DesktopRender.DATE_FORMAT);
        jdcDatascadenza.setPreferredSize(new Dimension(120, 30));
        jdcDatascadenza.setFont(new Font(DesktopRender.FONT_FAMILY, Font.BOLD, 16));

        jdcDatascadenza.setMinSelectableDate(new Date());
        JTextFieldDateEditor editor = (JTextFieldDateEditor) jdcDatascadenza.getComponent(1);
        editor.setHorizontalAlignment(JTextField.RIGHT);
        editor.setFont(font);
        editor.setBackground(DesktopRender.JTF_COLOR);
        editor.setBorder(new LineBorder(Color.BLACK));


        lblNote = new JLabel("Note");
        lblNote.setFont(font);

        jtaNote = new JTextArea(3, 16);
        jtaNote.setLineWrap(true);
        jtaNote.setWrapStyleWord(true);

        jtaNote.setCaretColor(Color.BLACK);
        jtaNote.setBackground(JTF_COLOR);
        jtaNote.setBorder(new LineBorder(Color.BLACK));
        jtaNote.setFont(font);
        JScrollPane jScrollNote = new JScrollPane(jtaNote, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

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
        internPane.add(lblCodice, gc);

        gc.gridx = 0;
        gc.gridy = 1;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(2, 10, 2, 10);
        internPane.add(lblDescrizione, gc);

        gc.gridx = 0;
        gc.gridy = 2;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(2, 10, 2, 10);
        internPane.add(lblData, gc);

        gc.gridx = 0;
        gc.gridy = 3;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(2, 10, 2, 10);
        internPane.add(lblQuantita, gc);

        gc.gridx = 0;
        gc.gridy = 4;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(2, 10, 2, 10);
        internPane.add(lblFornitore, gc);


        gc.gridx = 0;
        gc.gridy = 5;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(2, 10, 2, 10);
        internPane.add(lblDatascadenza, gc);


        gc.gridx = 0;
        gc.gridy = 6;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(2, 10, 2, 10);
        internPane.add(lblNote, gc);

        // second column//
        gc.anchor = GridBagConstraints.WEST;
        gc.gridx = 1;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.LINE_START;
        internPane.add(jcbCodice, gc);

        gc.gridx = 1;
        gc.gridy = 1;

        gc.anchor = GridBagConstraints.LINE_START;
        internPane.add(jtfDescrizione, gc);

        gc.gridx = 1;
        gc.gridy = 2;

        gc.anchor = GridBagConstraints.LINE_START;
        internPane.add(jdcData, gc);

        gc.gridx = 1;
        gc.gridy = 3;

        gc.anchor = GridBagConstraints.LINE_START;
        internPane.add(jspQuantita, gc);

        gc.gridx = 1;
        gc.gridy = 4;

        gc.anchor = GridBagConstraints.LINE_START;
        internPane.add(jcbFornitore, gc);

        gc.gridx = 1;
        gc.gridy = 5;

        gc.anchor = GridBagConstraints.LINE_START;
        internPane.add(jdcDatascadenza, gc);

        gc.gridx = 1;
        gc.gridy = 6;

        gc.anchor = GridBagConstraints.LINE_START;
        internPane.add(jScrollNote, gc);

        btn_update = new JButton(formatButton("Update"));
        btn_clear = new JButton(formatButton("Clear"));

        formatButton(btn_clear);
        formatButton(btn_update);

        actionPane.setBackground(wrapperPane.getBackground());
        actionPane.setLayout(new GridBagLayout());


        GridBagConstraints ca = new GridBagConstraints();
        ca.insets = new Insets(5, 10, 15, 28);

        actionPane.add(btn_clear, ca);
        actionPane.add(btn_update, ca);

        jcbCodice.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                jtfDescrizione.setText(DAOUtils.getProduct(String.valueOf(jcbCodice.getSelectedItem())).getDescrizione());
                jtfDescrizione.setEditable(false);
            }
        });

        //btn_clear.addActionListener(e -> initInfoCaricoPane());
        btn_update.addActionListener(e ->
                {
                    updateCarico();
                    dispose();
                    table.getSelectionModel().clearSelection();
                }
        );
    }

    public void updateCarico() {
        Carico c = new Carico();
        c.setCodice(String.valueOf(jcbCodice.getSelectedItem()));
        c.setDescrizione(jtfDescrizione.getText());
        c.setDatacarico(jdcData.getDate());
        c.setQuantita(Integer.valueOf(jspQuantita.getValue().toString()));
        c.setFornitore(String.valueOf(jcbFornitore.getSelectedItem()));
        c.setDatascadenza(jdcDatascadenza.getDate());
        c.setNote(jtaNote.getText());

        EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        Carico carico = em.find(Carico.class,UID);
        carico.setCodice(c.getCodice());
        carico.setDescrizione(c.getDescrizione());
        carico.setDatacarico(c.getDatacarico());
        carico.setQuantita(c.getQuantita());
        carico.setFornitore(c.getFornitore());
        carico.setDatascadenza(c.getDatascadenza());
        carico.setNote(c.getNote());
        em.persist(carico);
        em.getTransaction().commit();
        em.clear();
        em.close();
        tableModel.getDataVector().removeAllElements();
        tableModel.fireTableDataChanged();
        loadCarico().forEach(ca -> tableModel.addRow(new String[]{String.valueOf(ca.getUID()), (new SimpleDateFormat(DesktopRender.DATE_FORMAT)).format(ca.getDatacarico()), ca.getCodice(), ca.getDescrizione(), String.valueOf(ca.getQuantita()), formatMoney(ca.getImporto()),ca.getFornitore(), (new SimpleDateFormat(DesktopRender.DATE_FORMAT)).format(ca.getDatascadenza()), ca.getNote()}));
        table.revalidate();
        table.repaint();
        showMessageDialog(null, "Carico aggiornato", "Info Dialog", JOptionPane.INFORMATION_MESSAGE);
    }


    private static boolean fornitoreFilter(String fornitore, String textToFilter) {
        if (textToFilter.isEmpty()) {
            return true;
        }
        return CustomComboRenderer.getProcessDisplayText(fornitore).toLowerCase().contains(textToFilter.toLowerCase());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}