package shop.dao;

import org.hibernate.query.NativeQuery;
import shop.entity.Fornitore;

import javax.persistence.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static javax.swing.JOptionPane.showMessageDialog;
import static shop.view.ClientePane.tableModel;
import static shop.view.ClientePane.table;
import static shop.view.fornitore.FornitorePane.*;

public class FornitoreDAO {

    public static void deleteFornitore() {

        if (table.getSelectedRow() == -1) {
            showMessageDialog(null, "Selezionare un fornitore", "Info Dialog", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (table.getSelectedRow() != -1) {

            int index = table.getSelectedRow();
            EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
            em.getTransaction().begin();
            Fornitore fornitore = em.find(Fornitore.class, table.getValueAt(index, 5));
            fornitore.setDeleted(true);
            em.getTransaction().commit();
            em.clear();
            em.close();
            tableModel.removeRow(index);
        }

        IntStream.range(0, tableModel.getRowCount()).forEachOrdered(index -> tableModel.setValueAt(index + 1, index, 0));
        showMessageDialog(null, "Cancellazione effettuata", "Info Dialog", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void insertFornitore() {

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
            if (checkPiva(fornitore.getPiva()) >= 1) {
                showMessageDialog(null, "Fornitore già presente", "Info Dialog", JOptionPane.ERROR_MESSAGE);
            } else {
                EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
                em.getTransaction().begin();
                em.persist(fornitore);
                em.getTransaction().commit();
                em.clear();
                em.close();
                tableModel.addRow(new String[]{String.valueOf(getFornitoreCount()), fornitore.getNome(), fornitore.getCognome(), fornitore.getIndirizzo(), fornitore.getComune(), fornitore.getPiva(), fornitore.getMail(), fornitore.getTelefono(), fornitore.getFax(), fornitore.getWebsite(), fornitore.getNote()});
                showMessageDialog(null, "Fornitore inserito", "Info Dialog", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        initFornitorePane();
    }

    private static Integer checkPiva(String piva) {
        EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        Query query = em.createNativeQuery("SELECT count(*) FROM Fornitore WHERE isDeleted=false and piva=:piva")
                .setParameter("piva", piva)
                .unwrap(NativeQuery.class);
        Integer rowCnt = (Integer) query.getSingleResult();
        em.getTransaction().commit();
        em.clear();
        em.close();
        return rowCnt;
    }

    public static Integer getFornitoreCount() {
        EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        Query query = em.createNativeQuery("SELECT count(*) FROM Fornitore WHERE isDeleted=false")
                .unwrap(NativeQuery.class);
        Integer rowCnt = (Integer)query.getSingleResult();
        em.getTransaction().commit();
        em.clear();
        em.close();
        return rowCnt;
    }


    public static ArrayList<Fornitore> loadFornitore() {
        EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        TypedQuery<Fornitore> query = em.createQuery("SELECT f FROM Fornitore f WHERE f.isDeleted=false", Fornitore.class);
        List<Fornitore> results = query.getResultList();
        em.getTransaction().commit();
        em.clear();
        em.close();
        return new ArrayList<>(results);
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
}
