package shop.dao;

import org.hibernate.query.NativeQuery;
import shop.entity.Categoria;
import javax.persistence.*;
import javax.swing.*;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.*;

import static javax.swing.JOptionPane.showMessageDialog;
import static shop.view.articolo.CategoryView.*;

public class CategoriaDAO {

    private static Integer checkCategoria(String categoria) {
        EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        Query query = em.createNativeQuery("SELECT count(*) FROM CATEGORIA WHERE isDeleted=false and categoria=:categoria")
                .setParameter("categoria", categoria)
                .unwrap(NativeQuery.class);
        BigInteger rowCnt = (BigInteger) query.getSingleResult();
        em.getTransaction().commit();
        em.clear();
        em.close();
        return rowCnt.intValue();
    }

    public static Integer restoreCategoria(String categoria){
        EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        Query query = em.createNativeQuery("SELECT count(*) FROM CATEGORIA WHERE isDeleted=true and categoria=:categoria")
                .setParameter("categoria", categoria)
                .unwrap(NativeQuery.class);
        BigInteger rowCnt = (BigInteger) query.getSingleResult();
        em.getTransaction().commit();
        em.clear();
        em.close();
        return rowCnt.intValue();
    }

    public static void insertCategoria() {
        Categoria categoria = new Categoria(fieldCategoria.getText(), false);
        if (checkCategoria(categoria.getCategoria()) > 0) {
            showMessageDialog(null, "Categoria già presente", "Info Dialog", JOptionPane.ERROR_MESSAGE);
        } else if (categoria.getCategoria().isEmpty()) {
            showMessageDialog(null, "Categoria vuota", "Info Dialog", JOptionPane.ERROR_MESSAGE);
        } else if(restoreCategoria(categoria.getCategoria()) > 0) {
            EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
            em.getTransaction().begin();
            Categoria ca = em.find(Categoria.class, categoria.getCategoria());
            ca.setDeleted(false);
            em.persist(ca);
            em.getTransaction().commit();
            em.clear();
            em.close();
            tableModel.getDataVector().removeAllElements();
            tableModel.fireTableDataChanged();
            int i = 0;
            for (Categoria c : loadCategoria()) {
                tableModel.addRow(new String[]{String.valueOf(++i), c.getCategoria()});
            }
            table.repaint();
            table.revalidate();
        }else {
            EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
            em.getTransaction().begin();
            em.persist(categoria);
            em.getTransaction().commit();
            em.clear();
            em.close();
            tableModel.addRow(new String[]{String.valueOf(getCategoriaCount()), String.valueOf(categoria.getCategoria())});
            showMessageDialog(null, "Categoria inserita", "Info Dialog", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static ArrayList<Categoria> loadCategoria() {
        EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        TypedQuery<Categoria> query = em.createQuery("SELECT c FROM Categoria c WHERE c.isDeleted=false", Categoria.class);
        List<Categoria> results = query.getResultList();
        em.getTransaction().commit();
        em.clear();
        em.close();
        if (results.isEmpty())
            return new ArrayList<>();
        return new ArrayList<>(results);
    }

    public static void deleteCategoria() {

        if (table.getSelectedRow() == -1) {
            showMessageDialog(null, "Selezionare una categoria", "Info Dialog", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (table.getSelectedRow() != -1) {
            int index = table.getSelectedRow();
            EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
            em.getTransaction().begin();
            Categoria categoria = em.find(Categoria.class, table.getValueAt(index, 1));
            categoria.setDeleted(true);
            em.getTransaction().commit();
            em.clear();
            em.close();
            tableModel.removeRow(index);
        }
        IntStream.range(0, tableModel.getRowCount()).forEachOrdered(index -> tableModel.setValueAt(index + 1, index, 0));
        showMessageDialog(null, "Cancellazione effettuata", "Info Dialog", JOptionPane.INFORMATION_MESSAGE);
    }

    public static ArrayList<String> getAllCategories() {
        EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        TypedQuery<String> query = em.createQuery("SELECT c.categoria FROM Categoria c WHERE c.isDeleted=false", String.class);
        List<String> results = query.getResultList();
        em.getTransaction().commit();
        em.clear();
        em.close();
        return new ArrayList<>(results);
    }

    public static Integer getCategoriaCount() {
        EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        Query query = em.createNativeQuery("SELECT count(*) FROM CATEGORIA WHERE isDeleted=false")
                .unwrap(NativeQuery.class);
        Integer rowCnt = Integer.parseInt(String.valueOf(query.getSingleResult()));
        em.getTransaction().commit();
        em.clear();
        em.close();
        return rowCnt;
    }
}
