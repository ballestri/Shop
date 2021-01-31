package shop.dao;

import shop.entity.Carico;
import shop.utils.DesktopRender;

import javax.persistence.TypedQuery;
import javax.swing.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import static shop.utils.DesktopRender.*;
import static javax.swing.JOptionPane.showMessageDialog;
import static shop.view.CaricoPane.*;
import static shop.view.rilevazione.InfoCaricoPane.*;

public class CaricoDAO {

    public static void deleteCarico() {

        if (table.getSelectedRow() == -1) {
            showMessageDialog(null, "Selezionare un carico", "Info Dialog", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (table.getSelectedRow() != -1) {
            int index = table.getSelectedRow();
            EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
            em.getTransaction().begin();
            Carico carico = em.find(Carico.class, Integer.parseInt(table.getValueAt(index, 0).toString()));
            carico.setDeleted(true);
            em.getTransaction().commit();
            em.clear();
            em.close();
            tableModel.removeRow(index);
            tableModel.getDataVector().removeAllElements();
            tableModel.fireTableDataChanged();
            loadCarico().forEach(ca -> tableModel.addRow(new String[]{String.valueOf(ca.getUID()), (new SimpleDateFormat(DATE_FORMAT)).format(ca.getDatacarico()), ca.getCodice(), ca.getDescrizione(), String.valueOf(ca.getQuantita()), String.valueOf(ca.getImporto()).concat(" €"), ca.getFornitore(), (new SimpleDateFormat(DATE_FORMAT)).format(carico.getDatascadenza()), ca.getNote()}));
            table.repaint();
            table.revalidate();
        }
        showMessageDialog(null, "Cancellazione effettuata", "Info Dialog", JOptionPane.INFORMATION_MESSAGE);
    }

    public static ArrayList<Carico> loadCarico() {

        EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        TypedQuery<Object[]> query = em.createQuery("SELECT c.UID, c.codice,a.descrizione,c.datacarico,c.quantita,(c.quantita*a.prezzo) as importo ,c.fornitore,c.datascadenza, c.note FROM Carico c JOIN Articolo a ON (c.codice=a.codice) and c.isDeleted=false", Object[].class);
        List<Object[]> items = query.getResultList();
        ArrayList<Carico> results = new ArrayList<>();
        em.getTransaction().commit();
        em.clear();
        em.close();
        if (items.isEmpty())
            return new ArrayList<>();
        items.forEach(value -> {
            Carico carico = new Carico();
            carico.setUID((Integer) value[0]);
            carico.setCodice((String) value[1]);
            carico.setDescrizione((String) value[2]);
            carico.setQuantita(Integer.valueOf(String.valueOf(value[4])));
            carico.setImporto(BigDecimal.valueOf((Double) value[5]));
            carico.setFornitore(String.valueOf(value[6]));
            carico.setDatacarico((Date) value[3]);
            carico.setDatascadenza((Date) value[7]);
            carico.setNote(String.valueOf(value[8]));
            results.add(carico);
        });
        return results;
    }

    public static void insertCarico() {

        Carico carico = new Carico();
        carico.setCodice(String.valueOf(jcbCodice.getSelectedItem()));
        carico.setDescrizione(jtfDescrizione.getText());
        carico.setDatacarico(jdcData.getDate());
        carico.setQuantita(Integer.valueOf(jspQuantita.getValue().toString()));
        carico.setFornitore(String.valueOf(jcbFornitore.getSelectedItem()));
        carico.setDatascadenza(jdcDatascadenza.getDate());
        carico.setNote(jtaNote.getText());

        if (carico.getCodice().isEmpty()) {
            showMessageDialog(null, "Codice articolo vuoto", "Info Dialog", JOptionPane.ERROR_MESSAGE);
        } else {
            EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
            em.getTransaction().begin();
            em.persist(carico);
            em.getTransaction().commit();
            Carico a = em.find(Carico.class, carico.getUID());
            em.getTransaction().begin();
            a.setImporto(calcolateImporto(carico.getUID()));
            em.getTransaction().commit();
            em.close();
            tableModel.addRow(new String[]{String.valueOf(carico.getUID()), (new SimpleDateFormat(DesktopRender.DATE_FORMAT)).format(carico.getDatacarico()), carico.getCodice(), carico.getDescrizione(), String.valueOf(carico.getQuantita()), String.valueOf(carico.getImporto()).concat(" €"), carico.getFornitore(), (new SimpleDateFormat(DesktopRender.DATE_FORMAT)).format(carico.getDatascadenza()), carico.getNote()});
            showMessageDialog(null, "Carico inserito", "Info Dialog", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static BigDecimal calcolateImporto(Integer UID) {
        EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        TypedQuery<Double> query = em.createQuery("SELECT (c.quantita*a.prezzo) as importo FROM Articolo a join Carico c on (c.codice = a.codice) WHERE c.UID=?1", Double.class);
        query.setParameter(1, UID);
        BigDecimal importo = BigDecimal.valueOf(query.getSingleResult());
        em.getTransaction().commit();
        em.clear();
        em.close();
        return importo;
    }
}