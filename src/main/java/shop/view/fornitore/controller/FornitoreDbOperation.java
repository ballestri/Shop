package shop.view.fornitore.controller;
import shop.db.ConnectionManager;
import shop.model.Fornitore;
import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.stream.IntStream;

import static javax.swing.JOptionPane.showMessageDialog;
import static shop.view.ClientePane.tableModel;
import static shop.view.ClientePane.table;
import static shop.view.fornitore.FornitorePane.*;

public class FornitoreDbOperation {

    public static void deleteFornitoreFromDB() {

        if (table.getSelectedRow() == -1) {
            showMessageDialog(null, "Selezionare un fornitore", "Info Dialog", JOptionPane.ERROR_MESSAGE);
            return;
        }

        while (table.getSelectedRow() != -1) {
            try {
                Connection con = (new ConnectionManager()).getConnection();
                Statement stmt = con.createStatement();
                stmt.executeUpdate(String.format("DELETE FROM FORNITORE WHERE PIVA='%s'", table.getValueAt(table.getSelectedRow(), 5)));
                stmt.executeUpdate(String.format("UPDATE ARTICOLO SET FORNITORE='%s' WHERE FORNITORE='%s'", "NOT CATEGORIZED", table.getValueAt(table.getSelectedRow(), 2)));
                stmt.close();
                con.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
            tableModel.removeRow(table.getSelectedRow());
        }

        IntStream.range(0, tableModel.getRowCount()).forEachOrdered(index -> tableModel.setValueAt(index + 1, index, 0));
        showMessageDialog(null, "Cancellazione effettuata", "Info Dialog", JOptionPane.INFORMATION_MESSAGE);
    }


    public static void insertFornitoreToDB() {

        Fornitore fornitore = new Fornitore();
        fornitore.setNome(jtfNome.getText());
        fornitore.setCognome(jtfCognome.getText());
        fornitore.setIndirizzo(jtfIndirizzo.getText());
        fornitore.setComune(jtfComune.getText());
        fornitore.setPiva(jtfPiva.getText());
        fornitore.setMail(jtfMail.getText());
        fornitore.setTelefono(jtfTelefono.getText());
        fornitore.setFax(jtfFax.getText());
        fornitore.setWebsite(jtfSito.getText());
        fornitore.setNote(jtaNote.getText());

        if (fornitore.getPiva().isEmpty()) {
            showMessageDialog(null, "Partita IVA fornitore vuoto", "Info Dialog", JOptionPane.ERROR_MESSAGE);
        } else {
            if (checkPiva(fornitore.getPiva())) {
                showMessageDialog(null, "Fornitore già presente", "Info Dialog", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    Connection con = (new ConnectionManager()).getConnection();
                    PreparedStatement preparedStmt = con.prepareStatement("INSERT INTO FORNITORE VALUES (?, ?, ?, ?, ?,?,?,?,?,?)");
                    preparedStmt.setString(1, fornitore.getNome());
                    preparedStmt.setString(2, fornitore.getCognome());
                    preparedStmt.setString(3, fornitore.getIndirizzo());
                    preparedStmt.setString(4, fornitore.getComune());
                    preparedStmt.setString(5, fornitore.getPiva());
                    preparedStmt.setString(6, fornitore.getMail());
                    preparedStmt.setString(7, fornitore.getTelefono());
                    preparedStmt.setString(8, fornitore.getFax());
                    preparedStmt.setString(9, fornitore.getWebsite());
                    preparedStmt.setString(10, fornitore.getNote());
                    preparedStmt.execute();
                    con.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }

                tableModel.addRow(new String[]{String.valueOf(getFornitoreCountItems()), fornitore.getNome(), fornitore.getCognome(), fornitore.getIndirizzo(), fornitore.getComune(), fornitore.getPiva(), fornitore.getMail(), fornitore.getTelefono(), fornitore.getFax(), fornitore.getWebsite(), fornitore.getNote()});
                showMessageDialog(null, "Fornitore inserito", "Info Dialog", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        initFornitorePane();
    }

    private static boolean checkPiva(String piva) {

        boolean isPresente = false;
        try {
            Connection con = (new ConnectionManager()).getConnection();
            ResultSet rs = con.createStatement().executeQuery(String.format("SELECT* FROM FORNITORE WHERE PIVA='%s' GROUP BY PIVA HAVING COUNT(*) > 0", piva));
            if (rs.next()) isPresente = true;
            con.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return isPresente;
    }

    public static int getFornitoreCountItems() {
        int count=0;
        try {
            Connection con = (new ConnectionManager()).getConnection();
            ResultSet rs = con.createStatement().executeQuery("SELECT COUNT(*) FROM FORNITORE");
            rs.next();
            count = rs.getInt(1);
            con.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return count;
    }



    public static ArrayList<Fornitore>  loadFornitoreFromDB() {
        ArrayList<Fornitore> list_fornitori = new ArrayList<>();
        try {
            Connection con = (new ConnectionManager()).getConnection();
            ResultSet rs = con.createStatement().executeQuery("SELECT* FROM FORNITORE");

            while (rs.next()) {
                Fornitore fornitore = new Fornitore();
                fornitore.setNome(rs.getString("NOME"));
                fornitore.setCognome(rs.getString("COGNOME"));
                fornitore.setIndirizzo(rs.getString("INDIRIZZO"));
                fornitore.setComune(rs.getString("COMUNE"));
                fornitore.setPiva(rs.getString("PIVA"));
                fornitore.setMail(rs.getString("MAIL"));
                fornitore.setTelefono(rs.getString("TELEFONO"));
                fornitore.setFax(rs.getString("FAX"));
                fornitore.setWebsite(rs.getString("WEBSITE"));
                fornitore.setNote(rs.getString("NOTE"));
                list_fornitori.add(fornitore);
            }
            con.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return list_fornitori;
    }

    public static void initFornitorePane() {
        if (table.getSelectedRow() != 0) {
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
            jtaNote.setText(null);
        }

    }

    public static ArrayList<String> getListFornitore() {
        ArrayList<String> fornitori = new ArrayList<>();
        try {
            Connection con = (new ConnectionManager()).getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COGNOME FROM FORNITORE");
            while (rs.next()) {
                fornitori.add(rs.getString("COGNOME"));
            }
            stmt.close();
            con.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return fornitori;
    }
}
