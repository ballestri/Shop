package shop.view.rilevazione.controller;

import shop.db.ConnectionManager;
import shop.model.Scarico;
import shop.utils.DesktopRender;

import javax.swing.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static javax.swing.JOptionPane.showMessageDialog;
import static shop.view.ScaricoPane.*;
import static shop.view.rilevazione.InfoScaricoPane.*;

public class ScaricoDbOperation {


    public static void deleteScaricoFromDB() {

        if (table.getSelectedRow() == -1) {
            showMessageDialog(null, "Selezionare uno scarico", "Info Dialog", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (table.getSelectedRow() != -1) {
            try {
                Connection con = (new ConnectionManager()).getConnection();
                String QUERY = "DELETE FROM SCARICO WHERE ID=?";
                PreparedStatement preparedStmt = con.prepareStatement(QUERY);
                preparedStmt.setInt(1, Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString()));
                preparedStmt.execute();
                con.close();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            tableModel.removeRow(table.getSelectedRow());
        }
        showMessageDialog(null, "Cancellazione effettuata", "Info Dialog", JOptionPane.INFORMATION_MESSAGE);
    }

    public static ArrayList<Scarico> loadScaricoFromDB() {
        ArrayList<Scarico> list_scarico = new ArrayList<>();
        try {
            Connection con = (new ConnectionManager()).getConnection();
            String QUERY = "SELECT S.ID, S.CODICE,A.DESCRIZIONE,S.DATASCARICO,S.QUANTITA,S.FORNITORE,S.NOTE FROM SCARICO S JOIN ARTICOLO A ON (S.CODICE=A.CODICE)";
            ResultSet rs = con.createStatement().executeQuery(QUERY);

            while (rs.next()) {
                Scarico scarico = new Scarico();
                scarico.setID(rs.getInt("ID"));
                scarico.setCodice(rs.getString("CODICE"));
                scarico.setDescrizione(rs.getString("DESCRIZIONE"));
                scarico.setDatascarico(rs.getDate("DATASCARICO"));
                scarico.setQuantita(rs.getInt("QUANTITA"));
                scarico.setFornitore(rs.getString("FORNITORE"));
                scarico.setNote(rs.getString("NOTE"));
                list_scarico.add(scarico);
            }
            con.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return list_scarico;
    }

    public static void insertScaricoToDB() {

        Scarico scarico = new Scarico();
        scarico.setCodice(String.valueOf(jcbCodice.getSelectedItem()));
        scarico.setDescrizione(jtfDescrizione.getText());
        scarico.setDatascarico(jdcData.getDate());
        scarico.setQuantita(Integer.valueOf(jspQuantita.getValue().toString()));
        scarico.setFornitore(String.valueOf(jcbFornitore.getSelectedItem()));
        scarico.setNote(jtaNote.getText());

        if (scarico.getCodice().isEmpty()) {
            showMessageDialog(null, "Codice articolo vuoto", "Info Dialog", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                Connection con = (new ConnectionManager()).getConnection();
                String QUERY = "INSERT INTO SCARICO (CODICE,DESCRIZIONE,DATASCARICO,QUANTITA,FORNITORE,NOTE) VALUES (?,?, ?, ?, ?, ?)";
                PreparedStatement preparedStmt = con.prepareStatement(QUERY, Statement.RETURN_GENERATED_KEYS);
                preparedStmt.setString(1, scarico.getCodice());
                preparedStmt.setString(2, scarico.getDescrizione());
                preparedStmt.setDate(3, new Date(jdcData.getDate().getTime()));
                preparedStmt.setInt(4, scarico.getQuantita());
                preparedStmt.setString(5, scarico.getFornitore());
                preparedStmt.setString(6, scarico.getNote());
                preparedStmt.execute();
                ResultSet rs = preparedStmt.getGeneratedKeys();
                if (rs.next()) scarico.setID(rs.getInt(1));
                con.close();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            tableModel.addRow(new String[]{String.valueOf(scarico.getID()), (new SimpleDateFormat(DesktopRender.DATE_FORMAT)).format(scarico.getDatascarico()), scarico.getCodice(), scarico.getDescrizione(), String.valueOf(scarico.getQuantita()), scarico.getFornitore(), scarico.getNote()});
            showMessageDialog(null, "Scarico inserito", "Info Dialog", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}

