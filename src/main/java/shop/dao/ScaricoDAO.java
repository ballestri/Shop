package shop.dao;

import shop.entity.Articolo;
import shop.entity.Scarico;
import shop.utils.DesktopRender;

import javax.persistence.*;
import javax.swing.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.JOptionPane.showMessageDialog;
import static shop.utils.DesktopRender.formatMoney;
import static shop.view.ScaricoPane.*;
import static shop.view.rilevazione.InfoScaricoPane.*;

public class ScaricoDAO {


    public static void deleteScarico() {

        if (table.getSelectedRow() == -1) {
            showMessageDialog(null, "Selezionare uno scarico", "Info Dialog", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (table.getSelectedRow() != -1) {

            int index = table.getSelectedRow();
            EntityManager entityManager = JPAProvider.getEntityManagerFactory().createEntityManager();
            entityManager.getTransaction().begin();
            Scarico scarico = entityManager.find(Scarico.class, Integer.parseInt(table.getValueAt(index, 0).toString()));
            scarico.setDeleted(true);
            entityManager.getTransaction().commit();
            entityManager.clear();
            entityManager.close();
            tableModel.removeRow(index);
        }
        showMessageDialog(null, "Cancellazione effettuata", "Info Dialog", JOptionPane.INFORMATION_MESSAGE);
    }

    public static ArrayList<Scarico> loadScarico() {

        EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        TypedQuery<Object[]> query = em.createQuery("SELECT s.UID, s.articolo.codice,a.descrizione,s.datascarico,s.quantita,round((s.quantita*a.prezzo),2) as importo ,s.fornitore, s.note FROM Scarico s JOIN Articolo a ON (s.articolo.codice=a.codice) and s.isDeleted=false", Object[].class);
        List<Object[]> items = query.getResultList();
        em.getTransaction().commit();
        em.clear();
        em.close();
        ArrayList<Scarico> results = new ArrayList<>();
        if (items.isEmpty())
            return new ArrayList<>();
        for (Object[] value : items) {
            Scarico scarico = new Scarico();
            scarico.setUID((Integer) value[0]);
            Articolo articolo= new Articolo();
            articolo.setCodice(String.valueOf(value[1]));
            scarico.setArticolo(articolo);
            scarico.setDescrizione((String) value[2]);
            scarico.setQuantita(Integer.valueOf(String.valueOf(value[4])));
            scarico.setImporto(Double.valueOf(String.valueOf(value[5])));
            scarico.setFornitore(String.valueOf(value[6]));
            scarico.setDatascarico(new java.sql.Date(((Timestamp) value[3]).getTime()));
            scarico.setNote(String.valueOf(value[7]));
            results.add(scarico);
        }
        return results;
    }

    public static void insertScarico() {

        Scarico scarico = new Scarico();
        Articolo articolo= new Articolo();
        articolo.setCodice(String.valueOf(jcbCodice.getSelectedItem()));
        scarico.setArticolo(articolo);
        scarico.setDescrizione(jtfDescrizione.getText());
        scarico.setDatascarico(jdcData.getDate());
        scarico.setQuantita(Integer.valueOf(jspQuantita.getValue().toString()));
        scarico.setFornitore(String.valueOf(jcbFornitore.getSelectedItem()));
        scarico.setNote(jtaNote.getText());

        if (scarico.getArticolo().getCodice().isEmpty()) {
            showMessageDialog(null, "Codice articolo vuoto", "Info Dialog", JOptionPane.ERROR_MESSAGE);
        } else {
            EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
            em.getTransaction().begin();
            em.persist(scarico);
            em.getTransaction().commit();
            Scarico a = em.find(Scarico.class, scarico.getUID());
            em.getTransaction().begin();
            a.setImporto(calcolateImporto(scarico.getUID()));
            em.getTransaction().commit();
            em.clear();
            em.close();
            tableModel.addRow(new String[]{String.valueOf(scarico.getUID()), (new SimpleDateFormat(DesktopRender.DATE_FORMAT)).format(scarico.getDatascarico()), scarico.getArticolo().getCodice(), scarico.getDescrizione(), String.valueOf(scarico.getQuantita()),formatMoney(scarico.getImporto()), scarico.getFornitore(), scarico.getNote()});
            showMessageDialog(null, "Scarico inserito", "Info Dialog", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static Double calcolateImporto(Integer UID) {
        EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        TypedQuery<Double> query = em.createQuery("SELECT round((s.quantita*a.prezzo),2) as importo FROM Articolo a join Scarico s on (s.articolo.codice = a.codice) and s.UID=?1", Double.class);
        query.setParameter(1, UID);
        Double importo = query.getSingleResult();
        em.getTransaction().commit();
        em.clear();
        em.close();
        return importo;
    }

}

