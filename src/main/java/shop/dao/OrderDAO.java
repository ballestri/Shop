package shop.dao;

import org.hibernate.query.NativeQuery;
import shop.entity.Articolo;
import shop.entity.Cliente;
import shop.entity.Ordine;
import shop.entity.Ordine_details;
import javax.persistence.*;
import javax.swing.*;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import static javax.swing.JOptionPane.showMessageDialog;
import static shop.view.OrderPane.*;

public class OrderDAO {

    public static Integer getAllOrderCount() {

        EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        Query query = em.createNativeQuery("SELECT count(*) FROM Ordine WHERE state=false")
                .unwrap(NativeQuery.class);
        Integer rowCnt = Integer.parseInt(String.valueOf(query.getSingleResult()));
        em.getTransaction().commit();
        em.clear();
        em.close();
        return rowCnt;
    }

    public static void insertOrder(String orderID, String clientID, Date date) {


        Double importo = Double.valueOf(jtfOrderImporto.getText().trim().replace("Totale: ", "").trim().replace("€", "").replace(",", "."));
        Ordine ordine = new Ordine();
        ordine.setOrderID(orderID);

        Cliente cliente = new Cliente();
        cliente.setCodice(clientID);

        ordine.setCliente(cliente);
        ordine.setImporto(importo);
        ordine.setDate(date);

        Set<Ordine_details> OrderItems= new HashSet<>();

        IntStream.range(0, orderTableModel.getRowCount()).forEach(i -> {
            Ordine_details details = new Ordine_details();
            details.setOrdine(ordine);
            Articolo articolo = new Articolo();
            articolo.setCodice(String.valueOf(orderTable.getValueAt(i, 0)));
            details.setArticolo(articolo);
            details.setQuantity(Integer.valueOf(String.valueOf(orderTable.getValueAt(i, 3))));
            details.setPrezzo(Double.valueOf(String.valueOf(orderTable.getValueAt(i, 2)).trim().replace("€", "").replace(",", ".")));

            EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
            em.getTransaction().begin();
            Articolo al = em.find(Articolo.class, Integer.valueOf(articolo.getCodice()));
            details.setArticolo(al);
            em.persist(al);
            em.getTransaction().commit();
            em.clear();
            em.close();
            OrderItems.add(details);
        });

        ordine.setItems(Collections.unmodifiableSet(OrderItems));

        EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        Cliente cl = em.find(Cliente.class, Integer.valueOf(cliente.getCodice()));
        ordine.setCliente(cl);
        em.persist(ordine);
        em.getTransaction().commit();
        em.clear();
        em.close();



        /*
        IntStream.range(0, orderTableModel.getRowCount()).forEach(i -> {
            Ordine_details ordine = new Ordine();
            ordine.setOrderID(orderID);
            ordine.setClientID(clientID);

            ordine.setOrderDate(new Date());
            ordine.setArticoloID(String.valueOf(orderTable.getValueAt(i, 0)));
            ordine.setDescrizione(String.valueOf(orderTable.getValueAt(i, 1)));
            ordine.setPrezzo(Double.valueOf(String.valueOf(orderTable.getValueAt(i, 2)).trim().replace("€", "").replace(",", ".")));
            ordine.setQuantita(Integer.valueOf(String.valueOf(orderTable.getValueAt(i, 3))));
            ordine.setImporto(Double.valueOf(String.valueOf(orderTable.getValueAt(i, 4)).trim().replace("€", "").replace(",", ".")));

        });
        */


        showMessageDialog(null, "Ordine inserito", "Info Dialog", JOptionPane.INFORMATION_MESSAGE);
        orderTableModel.getDataVector().removeAllElements();
        orderTableModel.fireTableDataChanged();

    }
}
