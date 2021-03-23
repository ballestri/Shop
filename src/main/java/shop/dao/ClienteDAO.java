package shop.dao;

import org.hibernate.query.*;
import shop.entity.Cliente;

import javax.persistence.*;
import javax.persistence.Query;
import javax.swing.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.JOptionPane.showMessageDialog;
import static shop.utils.DesktopRender.*;
import static shop.view.ClientePane.*;
import static shop.view.GestionePane.tableModelCliente;

public class ClienteDAO {


    public static void deleteCliente() {

        if (table.getSelectedRow() == -1) {
            showMessageDialog(null, "Selezionare un cliente", "Info Dialog", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (table.getSelectedRow() != -1) {
            int index = table.getSelectedRow();
            EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
            em.getTransaction().begin();
            Cliente cliente = em.find(Cliente.class, Integer.parseInt(table.getValueAt(index, 0).toString()));
            cliente.setDeleted(true);
            em.getTransaction().commit();
            em.clear();
            em.close();
            tableModelCliente.removeRow(index);
            tableModelCliente.getDataVector().removeAllElements();
            tableModelCliente.fireTableDataChanged();
            loadCliente().forEach(cl -> tableModelCliente.addRow(new String[]{String.valueOf(cl.getUID()), cl.getCodice(), cl.getCognome(), cl.getNome(), cl.getTelefono(), cl.getEmail(), cl.getIndirizzo(), cl.getComune()}));
            clearField();
            table.repaint();
            table.revalidate();
        }
        showMessageDialog(null, "Cancellazione effettuata", "Info Dialog", JOptionPane.INFORMATION_MESSAGE);
    }

    private static Integer checkCliente(Cliente cliente) {
        EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        Query query = em.createNativeQuery("SELECT count(*) FROM Cliente WHERE isDeleted=false and cognome=:cognome and nome=:nome and telefono=:telefono and email=:email and indirizzo=:indirizzo and comune=:comune")
                .setParameter("cognome", cliente.getCognome())
                .setParameter("nome", cliente.getNome())
                .setParameter("telefono", cliente.getTelefono())
                .setParameter("email", cliente.getEmail())
                .setParameter("indirizzo", cliente.getIndirizzo())
                .setParameter("comune", cliente.getComune())
                .unwrap(NativeQuery.class);
        BigInteger rowCnt = (BigInteger) query.getSingleResult();
        em.getTransaction().commit();
        em.clear();
        em.close();
        return rowCnt.intValue();
    }


    public static void insertCliente() {

        Cliente cliente = new Cliente();
        cliente.setCodice(formatUIDCode(cliente.getUID()));
        cliente.setCognome(jtfCognome.getText());
        cliente.setNome(jtfNome.getText());
        cliente.setTelefono(jtfTelefono.getText());
        cliente.setEmail(jtfEmail.getText());
        cliente.setIndirizzo(jtfIndirizzo.getText());
        cliente.setComune(jtfComune.getText());

        if (checkCliente(cliente) > 0) {
            showMessageDialog(null, "Cliente già presente", "Info Dialog", JOptionPane.ERROR_MESSAGE);
        } else if (cliente.getCognome().isEmpty()) {
            showMessageDialog(null, "Cognome vuoto", "Info Dialog", JOptionPane.ERROR_MESSAGE);
        } else {
            EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
            em.getTransaction().begin();
            em.persist(cliente);
            em.getTransaction().commit();
            Cliente c = em.find(Cliente.class, cliente.getUID());
            em.getTransaction().begin();
            c.setCodice(formatUIDCode(cliente.getUID()));
            em.getTransaction().commit();
            em.clear();
            em.close();
            tableModelCliente.addRow(new String[]{String.valueOf(cliente.getUID()), formatUIDCode(cliente.getUID()), cliente.getCognome(), cliente.getNome(), cliente.getTelefono(), cliente.getEmail(), cliente.getIndirizzo(), cliente.getComune()});
            clearField();
            showMessageDialog(null, "Cliente inserito", "Info Dialog", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static ArrayList<Cliente> loadCliente() {
        EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        TypedQuery<Cliente> query = em.createQuery("SELECT c FROM Cliente c WHERE c.isDeleted=false", Cliente.class);
        //TypedQuery<Cliente> query = em.createQuery("SELECT c.id,c.codice,c.cognome,c.nome,c.telefono,c.email,c.indirizzo,c.comune FROM Cliente c WHERE c.isDeleted=false", Cliente.class);
        List<Cliente> elements = query.getResultList();
        em.getTransaction().commit();
        em.clear();
        em.close();

        if (elements.isEmpty()) return new ArrayList<>();

        ArrayList<Cliente> results = new ArrayList<>();
        elements.forEach(row -> {
            Cliente c = new Cliente();
            c.setUID(Integer.valueOf(String.valueOf(row.getUID())));
            c.setCodice(String.valueOf(row.getCodice()));
            c.setCognome(String.valueOf(row.getCognome()));
            c.setNome(String.valueOf(row.getNome()));
            c.setTelefono(String.valueOf(row.getTelefono()));
            c.setEmail(String.valueOf(row.getEmail()));
            c.setIndirizzo(String.valueOf(row.getIndirizzo()));
            c.setComune(String.valueOf(row.getComune()));
            results.add(c);
        });

        return results;



        /*
        if (results.isEmpty())
            return new ArrayList<>();
        return new ArrayList<>(results);

         */
    }

    public static void getSelectedCliente() {
        if (table.getSelectedRow() >= 0) {
            Cliente cliente = getCliente();
            jtfCognome.setText(String.valueOf(cliente.getCognome()));
            jtfNome.setText(String.valueOf(cliente.getNome()));
            jtfTelefono.setText(String.valueOf(cliente.getTelefono()));
            jtfEmail.setText(String.valueOf(cliente.getEmail()));
            jtfIndirizzo.setText(String.valueOf(cliente.getIndirizzo()));
            jtfComune.setText(String.valueOf(cliente.getComune()));
        }
    }

    public static void updateCliente(Cliente item) {

        if (table.getSelectedRow() == -1) {
            showMessageDialog(null, "Selezionare un cliente", "Info Dialog", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Cliente c = new Cliente();
        c.setUID(item.getUID());
        c.setCodice(item.getCodice());
        c.setCognome(jtfCognome.getText());
        c.setNome(jtfNome.getText());
        c.setTelefono(jtfTelefono.getText());
        c.setEmail(jtfEmail.getText());
        c.setIndirizzo(jtfIndirizzo.getText());
        c.setComune(jtfComune.getText());

        EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        Cliente cliente = em.find(Cliente.class, item.getUID());
        cliente.setCodice(c.getCodice());
        cliente.setCognome(c.getCognome());
        cliente.setNome(c.getNome());
        cliente.setTelefono(c.getTelefono());
        cliente.setEmail(c.getEmail());
        cliente.setIndirizzo(c.getIndirizzo());
        cliente.setComune(c.getComune());
        em.persist(cliente);
        em.getTransaction().commit();
        em.clear();
        em.close();
        tableModelCliente.getDataVector().removeAllElements();
        tableModelCliente.fireTableDataChanged();
        loadCliente().forEach(cl -> tableModelCliente.addRow(new String[]{String.valueOf(cl.getUID()), cl.getCodice(), cl.getCognome(), cl.getNome(), cl.getTelefono(), cl.getEmail(), cl.getIndirizzo(), cl.getComune()}));
        table.revalidate();
        table.repaint();
        showMessageDialog(null, "Cliente aggiornato", "Info Dialog", JOptionPane.INFORMATION_MESSAGE);
    }


    public static void clearField() {
        jtfCognome.setText(null);
        jtfNome.setText(null);
        jtfTelefono.setText(null);
        jtfEmail.setText(null);
        jtfIndirizzo.setText(null);
        jtfComune.setText(null);
        table.getSelectionModel().clearSelection();
    }
}
