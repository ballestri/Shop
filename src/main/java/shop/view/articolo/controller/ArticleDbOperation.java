package shop.view.articolo.controller;

import shop.db.ConnectionManager;
import shop.db.DBUtils;
import shop.model.Articolo;
import shop.view.MovimentiPane;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

import static javax.swing.JOptionPane.showMessageDialog;
import static shop.view.ArticoloPane.*;

public class ArticleDbOperation {
    public static void deleteArticleFromDB() {

        if (table.getSelectedRow() == -1) {
            showMessageDialog(null, "Selezionare l'articolo", "Info Dialog", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (table.getSelectedRow() != -1) {
            try {
                Connection con = (new ConnectionManager()).getConnection();
                Statement stmt = con.createStatement();
                stmt.executeUpdate(String.format("DELETE FROM ARTICOLO WHERE CODICE='%s'", table.getValueAt(table.getSelectedRow(), 0)));
                stmt.close();
                con.close();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            tableModel.removeRow(table.getSelectedRow());
            initArticlePane();
            refreshMovimentTable();
        }
        showMessageDialog(null, "Cancellazione effettuata", "Info Dialog", JOptionPane.INFORMATION_MESSAGE);

    }

    public static void insertArticleToDB() {
        Articolo articolo = new Articolo();
        articolo.setCodice(jtfCodice.getText());
        articolo.setDescrizione(jtfDescrizione.getText());
        articolo.setCategoria(String.valueOf(jcbCategoria.getSelectedItem()));
        articolo.setPosizione(String.valueOf(jcbPosizione.getSelectedItem()));
        articolo.setUnita(String.valueOf(jcbUnita.getSelectedItem()));
        articolo.setPrezzo(Double.valueOf(jtfCurrency.getText().replace("€", "").replace(",", ".")));
        articolo.setScorta(Integer.valueOf(jspScorta.getValue().toString()));
        articolo.setProvenienza(jtfProvenienza.getText());

        if (articolo.getCodice().isEmpty()) {
            showMessageDialog(null, "Codice articolo vuoto", "Info Dialog", JOptionPane.ERROR_MESSAGE);
        } else {
            if (checkCodice(articolo.getCodice())) {
                showMessageDialog(null, "Articolo già presente", "Info Dialog", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    String QUERY = "INSERT INTO ARTICOLO VALUES (?, ?, ?, ?, ?,?,?,?)";
                    Connection con = (new ConnectionManager()).getConnection();
                    PreparedStatement preparedStmt = con.prepareStatement(QUERY);
                    preparedStmt.setString(1, articolo.getCodice());
                    preparedStmt.setString(2, articolo.getDescrizione());
                    preparedStmt.setString(3, articolo.getCategoria());
                    preparedStmt.setString(4, articolo.getPosizione());
                    preparedStmt.setString(5, articolo.getUnita());
                    preparedStmt.setDouble(6, articolo.getPrezzo());
                    preparedStmt.setInt(7, articolo.getScorta());
                    preparedStmt.setString(8, articolo.getProvenienza());
                    preparedStmt.execute();
                    con.close();
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
                tableModel.addRow(new String[]{articolo.getCodice(), articolo.getDescrizione(), articolo.getCategoria(), articolo.getPosizione(), articolo.getUnita(), String.valueOf(articolo.getPrezzo()).replace(".", ",").concat(" €"), String.valueOf(articolo.getScorta()), articolo.getProvenienza()});
                showMessageDialog(null, "Articolo inserito", "Info Dialog", JOptionPane.INFORMATION_MESSAGE);
                refreshMovimentTable();
            }
        }
        initArticlePane();
    }

    public static void updateArticleToDB() {
        Articolo articolo = new Articolo();
        articolo.setCodice(jtfCodice.getText());
        articolo.setDescrizione(jtfDescrizione.getText());
        articolo.setCategoria(String.valueOf(jcbCategoria.getSelectedItem()));
        articolo.setPosizione(String.valueOf(jcbPosizione.getSelectedItem()));
        articolo.setUnita(String.valueOf(jcbUnita.getSelectedItem()));
        articolo.setPrezzo(Double.valueOf(jtfCurrency.getText().replace("€", "").replace(",", ".")));
        articolo.setScorta(Integer.valueOf(jspScorta.getValue().toString()));
        articolo.setProvenienza(jtfProvenienza.getText());

        if (articolo.getCodice().isEmpty()) {
            showMessageDialog(null, "Codice articolo vuoto", "Info Dialog", JOptionPane.ERROR_MESSAGE);
        } else {
            if (checkCodice(articolo.getCodice())) {
                if (table.getSelectedRow() == -1) {
                    showMessageDialog(null, "Selezionare l'articolo", "Info Dialog", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    Connection con = (new ConnectionManager()).getConnection();
                    PreparedStatement ps = con.prepareStatement("UPDATE ARTICOLO SET DESCRIZIONE=?,CATEGORIA=?,POSIZIONE=?,UNITA=?,PREZZO=?,SCORTA=?,PROVENIENZA=? WHERE CODICE=?");
                    ps.setString(1, articolo.getDescrizione());
                    ps.setString(2, articolo.getCategoria());
                    ps.setString(3, articolo.getPosizione());
                    ps.setString(4, articolo.getUnita());
                    ps.setDouble(5, articolo.getPrezzo());
                    ps.setInt(6, articolo.getScorta());
                    ps.setString(7, articolo.getProvenienza());
                    ps.setString(8, articolo.getCodice());
                    ps.execute();

                    tableModel.setValueAt(articolo.getDescrizione(), table.getSelectedRow(), 1);
                    tableModel.setValueAt(articolo.getCategoria(), table.getSelectedRow(), 2);
                    tableModel.setValueAt(articolo.getPosizione(), table.getSelectedRow(), 3);
                    tableModel.setValueAt(articolo.getUnita(), table.getSelectedRow(), 4);
                    tableModel.setValueAt(String.valueOf(articolo.getPrezzo()).replace(".", ",").concat(" €"), table.getSelectedRow(), 5);
                    tableModel.setValueAt(articolo.getScorta(), table.getSelectedRow(), 6);
                    tableModel.setValueAt(articolo.getProvenienza(), table.getSelectedRow(), 7);
                    con.close();
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
                showMessageDialog(null, "Articolo aggiornato", "Info Dialog", JOptionPane.INFORMATION_MESSAGE);
                refreshMovimentTable();
            } else {
                showMessageDialog(null, "Articolo inesistente", "Info Dialog", JOptionPane.ERROR_MESSAGE);
            }
        }
        initArticlePane();
    }

    private static boolean checkCodice(String codice) {

        boolean isPresente = false;
        try {
            String QUERY = "SELECT* FROM ARTICOLO WHERE CODICE='%s' GROUP BY CODICE HAVING COUNT(*) > 0";
            Connection con = (new ConnectionManager()).getConnection();
            ResultSet rs = con.createStatement().executeQuery(String.format(QUERY, codice));
            if (rs.next()) isPresente = true;
            con.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return isPresente;
    }

    // inizializzazione del campi del pannello degli articoli
    public static void initArticlePane() {
        table.getSelectionModel().clearSelection();
        jtfCodice.setText(null);
        jtfCodice.setEditable(true);
        jtfDescrizione.setText(null);
        jcbCategoria.setSelectedIndex(0);
        jcbPosizione.setSelectedIndex(0);
        jcbUnita.setSelectedIndex(0);
        jtfCurrency.setValue(0);
        jspScorta.setValue(0);
        jtfProvenienza.setText(null);
    }

    public static ArrayList<Articolo> loadArticleFromDB() {
        ArrayList<Articolo> list_articles = new ArrayList<>();
        try {
            Connection con = (new ConnectionManager()).getConnection();
            ResultSet rs = con.createStatement().executeQuery("SELECT* FROM ARTICOLO");

            while (rs.next()) {
                Articolo article = new Articolo();
                article.setCodice(rs.getString("CODICE"));
                article.setDescrizione(rs.getString("DESCRIZIONE"));
                article.setCategoria(rs.getString("CATEGORIA"));
                article.setPosizione(rs.getString("POSIZIONE"));
                article.setUnita(rs.getString("UNITA"));
                article.setPrezzo(Double.valueOf(rs.getString("PREZZO")));
                article.setScorta(Integer.valueOf(rs.getString("SCORTA")));
                article.setProvenienza(rs.getString("PROVENIENZA"));
                list_articles.add(article);
            }
            con.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        table.validate();
        table.repaint();
        return list_articles;
    }

    public static void refreshMovimentTable() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(DBUtils.getListCodici().toArray(new String[0]));
        MovimentiPane.jcbCodice.setModel(model);
        MovimentiPane.jcbCodice.validate();
        MovimentiPane.jcbCodice.repaint();
    }

}
