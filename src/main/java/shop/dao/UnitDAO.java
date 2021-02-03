package shop.dao;

import org.hibernate.query.NativeQuery;
import shop.entity.Unita;

import javax.persistence.*;
import javax.swing.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static javax.swing.JOptionPane.*;
import static shop.view.articolo.UnitView.*;

public class UnitDAO {

    private static Integer checkUnita(String unita) {
        EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        Query query = em.createNativeQuery("SELECT count(*) FROM UNITA WHERE isDeleted=false and unita=:unita")
                .setParameter("unita", unita)
                .unwrap(NativeQuery.class);
        BigInteger rowCnt = (BigInteger) query.getSingleResult();
        em.getTransaction().commit();
        em.clear();
        em.close();
        return rowCnt.intValue();
    }

    public static Integer restoreUnita(String unita) {
        EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        Query query = em.createNativeQuery("SELECT count(*) FROM UNITA WHERE isDeleted=true and unita=:unita")
                .setParameter("unita", unita)
                .unwrap(NativeQuery.class);
        BigInteger rowCnt = (BigInteger) query.getSingleResult();
        em.getTransaction().commit();
        em.clear();
        em.close();
        return rowCnt.intValue();
    }

    public static void insertUnita() {
        Unita unita = new Unita(fieldUnita.getText(), false);
        if (checkUnita(unita.getUnita()) > 0) {
            showMessageDialog(null, "Unita già presente", "Info Dialog", JOptionPane.ERROR_MESSAGE);
        } else if (unita.getUnita().isEmpty()) {
            showMessageDialog(null, "Unita vuota", "Info Dialog", JOptionPane.ERROR_MESSAGE);
        } else if (restoreUnita(unita.getUnita()) > 0) {
            EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
            em.getTransaction().begin();
            Unita ca = em.find(Unita.class, unita.getUnita());
            ca.setDeleted(false);
            em.persist(ca);
            em.getTransaction().commit();
            em.clear();
            em.close();
            tableModel.getDataVector().removeAllElements();
            tableModel.fireTableDataChanged();
            int i = 0;
            for (Unita c : loadUnita()) {
                tableModel.addRow(new String[]{String.valueOf(++i), c.getUnita()});
            }
            table.repaint();
            table.revalidate();
        } else {
            EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
            em.getTransaction().begin();
            em.persist(unita);
            em.getTransaction().commit();
            em.clear();
            em.close();
            tableModel.addRow(new String[]{String.valueOf(getUnitaCount()), String.valueOf(unita.getUnita())});
            showMessageDialog(null, "Unita inserita", "Info Dialog", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static ArrayList<Unita> loadUnita() {
        EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        TypedQuery<Unita> query = em.createQuery("SELECT u FROM Unita u WHERE u.isDeleted=false", Unita.class);
        List<Unita> results = query.getResultList();
        em.getTransaction().commit();
        em.clear();
        em.close();
        if (results.isEmpty())
            return new ArrayList<>();
        return new ArrayList<>(results);
    }

    public static Integer getCountUnitArticolo(String unita) {
        EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        Query query = em.createNativeQuery("SELECT count(*) FROM ARTICOLO a WHERE a.isDeleted= false and a.unita=:unita")
                .setParameter("unita", unita)
                .unwrap(NativeQuery.class);
        Integer rowCnt = Integer.parseInt(String.valueOf(query.getSingleResult()));
        em.getTransaction().commit();
        em.clear();
        em.close();
        return rowCnt;
    }

    public static void deleteUnita() {

        int index = table.getSelectedRow();
        if (index == -1) {
            showMessageDialog(null, "Selezionare una unita", "Info Dialog", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if ((getCountUnitArticolo(String.valueOf(table.getValueAt(index, 1))) == 0)) {
            EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
            em.getTransaction().begin();
            Unita unita = em.find(Unita.class, table.getValueAt(index, 1));
            unita.setDeleted(true);
            em.getTransaction().commit();
            em.clear();
            em.close();
            tableModel.removeRow(index);

            IntStream.range(0, tableModel.getRowCount()).forEachOrdered(i -> tableModel.setValueAt(i + 1, i, 0));
            showMessageDialog(null, "Cancellazione effettuata", "Info Dialog", JOptionPane.INFORMATION_MESSAGE);
        } else
            showMessageDialog(null, "Impossible cancellare: Ci articoli associati", "Info Dialog", JOptionPane.INFORMATION_MESSAGE);
    }

    public static ArrayList<String> getAllUnita() {

        EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        TypedQuery<String> query = em.createQuery("SELECT u.unita FROM Unita u WHERE u.isDeleted=false", String.class);
        List<String> results = query.getResultList();
        em.getTransaction().commit();
        em.clear();
        em.close();
        return new ArrayList<>(results);
    }


    public static Integer getUnitaCount() {
        EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        Query query = em.createNativeQuery("SELECT count(*) FROM UNITA WHERE isDeleted=false")
                .unwrap(NativeQuery.class);
        Integer rowCnt = Integer.parseInt(String.valueOf(query.getSingleResult()));
        em.getTransaction().commit();
        em.clear();
        em.close();
        return rowCnt;
    }
}
