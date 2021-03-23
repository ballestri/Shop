package shop.dao;

import org.hibernate.query.NativeQuery;
import shop.entity.Ordine;

import javax.persistence.*;
import javax.swing.*;
import java.math.BigInteger;
import java.util.Date;
import java.util.stream.IntStream;

import static javax.swing.JOptionPane.showMessageDialog;
import static shop.view.OrderPane.orderTable;
import static shop.view.OrderPane.orderTableModel;

public class OrderDAO {

    public static Integer getAllOrderCount() {

        EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        Query query = em.createNativeQuery("SELECT count(*) FROM Ordine WHERE isDeleted=false")
                .unwrap(NativeQuery.class);
        Integer rowCnt = Integer.parseInt(String.valueOf(query.getSingleResult()));
        em.getTransaction().commit();
        em.clear();
        em.close();
        return rowCnt;
    }

    public static void insertOrder(String orderID, String clientID) {

        /*
        IntStream.range(0, orderTableModel.getRowCount()).forEach(i -> {
            Ordine ordine = new Ordine();
            ordine.setOrderID(orderID);
            ordine.setClientID(clientID);

            ordine.setOrderDate(new Date());
            ordine.setArticoloID(String.valueOf(orderTable.getValueAt(i, 0)));
            ordine.setDescrizione(String.valueOf(orderTable.getValueAt(i, 1)));
            ordine.setPrezzo(Double.valueOf(String.valueOf(orderTable.getValueAt(i, 2)).trim().replace("€", "").replace(",", ".")));
            ordine.setQuantita(Integer.valueOf(String.valueOf(orderTable.getValueAt(i, 3))));
            ordine.setImporto(Double.valueOf(String.valueOf(orderTable.getValueAt(i, 4)).trim().replace("€", "").replace(",", ".")));

            EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
            em.getTransaction().begin();
            em.persist(ordine);
            em.getTransaction().commit();
            em.clear();
            em.close();

        });

         */

        showMessageDialog(null, "Ordine inserito", "Info Dialog", JOptionPane.INFORMATION_MESSAGE);
        orderTableModel.getDataVector().removeAllElements();
        orderTableModel.fireTableDataChanged();

    }
}
