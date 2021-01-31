package shop.dao;

import org.hibernate.query.NativeQuery;
import shop.entity.Posizione;

import javax.persistence.*;
import javax.swing.*;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.*;
import static javax.swing.JOptionPane.showMessageDialog;
import static shop.view.articolo.PositionView.*;

public class PositionDAO {

    private static Integer checkPosition(String posizione) {
        EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        Query query = em.createNativeQuery("SELECT count(*) FROM POSIZIONE WHERE isDeleted=false and posizione=:posizione")
                .setParameter("posizione", posizione)
                .unwrap(NativeQuery.class);
        BigInteger rowCnt = (BigInteger) query.getSingleResult();
        em.getTransaction().commit();
        em.clear();
        em.close();
        return rowCnt.intValue();
    }

    public static void insertPosizione() {
        Posizione posizione = new Posizione(fieldPosition.getText(), false);
        if (checkPosition(posizione.getPosizione()) > 0) {
            showMessageDialog(null, "Posizione già presente", "Info Dialog", JOptionPane.ERROR_MESSAGE);
        } else if (posizione.getPosizione().isEmpty()) {
            showMessageDialog(null, "Posizione vuota", "Info Dialog", JOptionPane.ERROR_MESSAGE);
        } else {
            EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
            em.getTransaction().begin();
            em.persist(posizione);
            em.getTransaction().commit();
            em.clear();
            em.close();
            tableModel.addRow(new String[]{String.valueOf(getPosizioneCount()), String.valueOf(posizione.getPosizione())});
            showMessageDialog(null, "Posizione inserita", "Info Dialog", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static ArrayList<Posizione> loadPosizione() {

        EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        TypedQuery<Posizione> query = em.createQuery("SELECT p FROM Posizione p WHERE p.isDeleted=false", Posizione.class);
        List<Posizione> results = query.getResultList();
        em.getTransaction().commit();
        em.clear();
        em.close();
        if (results.isEmpty())
            return new ArrayList<>();
        return new ArrayList<>(results);
    }

    public static void deletePosizione() {

        if (table.getSelectedRow() == -1) {
            showMessageDialog(null, "Selezionare una posizione", "Info Dialog", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (table.getSelectedRow() != -1) {
            int index = table.getSelectedRow();
            EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
            em.getTransaction().begin();
            Posizione posizione = em.find(Posizione.class, table.getValueAt(index, 1));
            posizione.setDeleted(true);
            em.getTransaction().commit();
            em.clear();
            em.close();
            tableModel.removeRow(index);
        }
        IntStream.range(0, tableModel.getRowCount()).forEachOrdered(index -> tableModel.setValueAt(index + 1, index, 0));
        showMessageDialog(null, "Cancellazione effettuata", "Info Dialog", JOptionPane.INFORMATION_MESSAGE);
    }

    public static ArrayList<String> getAllPosizione() {

        EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        TypedQuery<String> query = em.createQuery("SELECT p.posizione FROM Posizione p WHERE p.isDeleted=false", String.class);
        List<String> results = query.getResultList();
        em.getTransaction().commit();
        em.clear();
        em.close();
        return new ArrayList<>(results);
    }

    public static Integer getPosizioneCount() {
        EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        Query query = em.createNativeQuery("SELECT count(*) FROM POSIZIONE WHERE isDeleted=false")
                .unwrap(NativeQuery.class);
        Integer rowCnt = Integer.parseInt(String.valueOf(query.getSingleResult()));
        em.getTransaction().commit();
        em.clear();
        em.close();
        return rowCnt;
    }
}
