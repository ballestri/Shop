package shop.dao;

import org.hibernate.query.NativeQuery;
import shop.entity.Fornitore;

import javax.persistence.*;
import javax.swing.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static javax.swing.JOptionPane.showMessageDialog;
import static shop.view.FornitorePane.tableModel;
import static shop.view.FornitorePane.table;
import static shop.view.fornitore.FornitorePaneEdit.*;

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


    public static Integer restoreFornitore(String piva) {
        EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        Query query = em.createNativeQuery("SELECT count(*) FROM FORNITORE WHERE isDeleted=true and piva=:piva")
                .setParameter("piva", piva)
                .unwrap(NativeQuery.class);
        BigInteger rowCnt = (BigInteger) query.getSingleResult();
        em.getTransaction().commit();
        em.clear();
        em.close();
        return rowCnt.intValue();
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

        if (checkPiva(fornitore.getPiva()) > 0) {
            showMessageDialog(null, "Fornitore già presente", "Info Dialog", JOptionPane.ERROR_MESSAGE);
        } else if (fornitore.getPiva().isEmpty()) {
            showMessageDialog(null, "Partita IVA fornitore vuota", "Info Dialog", JOptionPane.ERROR_MESSAGE);
        } else if (restoreFornitore(fornitore.getPiva()) > 0) {
            EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
            em.getTransaction().begin();
            Fornitore fe = em.find(Fornitore.class, fornitore.getPiva());
            fe.setDeleted(false);
            fe.setNome(fornitore.getNome());
            fe.setCognome(fornitore.getCognome());
            fe.setIndirizzo(fornitore.getIndirizzo());
            fe.setComune(fornitore.getComune());
            fe.setMail(fornitore.getMail());
            fe.setTelefono(fornitore.getTelefono());
            fe.setFax(fornitore.getFax());
            fe.setWebsite(fornitore.getWebsite());
            fe.setNote(fornitore.getNote());
            fe.setPiva(fornitore.getPiva());
            em.persist(fe);
            em.getTransaction().commit();
            em.clear();
            em.close();
            tableModel.getDataVector().removeAllElements();
            tableModel.fireTableDataChanged();
            int i = 0;
            for (Fornitore fr : loadFornitore()) {
                tableModel.addRow(new String[]{String.valueOf(++i), fr.getNome(), fr.getCognome(), fr.getIndirizzo(), fr.getComune(), fr.getPiva(), fr.getMail(), fr.getTelefono(), fr.getFax(), fr.getWebsite(), fr.getNote()});
            }
            table.repaint();
            table.revalidate();
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
        initFornitorePane();
    }

    private static Integer checkPiva(String piva) {
        EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        Query query = em.createNativeQuery("SELECT count(*) FROM Fornitore WHERE isDeleted=false and piva=:piva")
                .setParameter("piva", piva)
                .unwrap(NativeQuery.class);
        BigInteger rowCnt = (BigInteger) query.getSingleResult();
        em.getTransaction().commit();
        em.clear();
        em.close();
        return rowCnt.intValue();
    }

    public static Integer getFornitoreCount() {
        EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        Query query = em.createNativeQuery("SELECT count(*) FROM Fornitore WHERE isDeleted=false")
                .unwrap(NativeQuery.class);
        Integer rowCnt = Integer.parseInt(String.valueOf(query.getSingleResult()));
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
