package shop.dao;

import org.hibernate.query.NativeQuery;
import shop.entity.*;
import shop.utils.DesktopRender;
import shop.view.MovimentiPane;

import javax.persistence.*;
import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import javax.persistence.Query;
import java.math.BigInteger;
import static javax.swing.JOptionPane.*;
import static shop.utils.DesktopRender.formatMoney;
import static shop.utils.DesktopRender.formatUIDCode;
import static shop.view.ArticoloPane.*;
import static shop.view.GestionePane.tableModelArticolo;

public class ArticleDAO {

    public static void removeArticle() {

        if (table.getSelectedRow() == -1) {
            showMessageDialog(null, "Selezionare un articolo", "Info Dialog", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (table.getSelectedRow() != -1) {
            int index = table.getSelectedRow();
            EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
            em.getTransaction().begin();
            Articolo articolo = em.find(Articolo.class, Integer.parseInt(table.getValueAt(index, 0).toString()));
            articolo.setDeleted(true);
            em.getTransaction().commit();
            em.clear();
            em.close();
            tableModelArticolo.removeRow(index);
            initArticlePane();
            //refreshMovimentTable();
        }
        showMessageDialog(null, "Cancellazione effettuata", "Info Dialog", JOptionPane.INFORMATION_MESSAGE);
    }

    public static Integer checkArticolo(Articolo a){
        EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        Query query = em.createNativeQuery("SELECT count(*) FROM ARTICOLO WHERE isDeleted=false and descrizione=:descrizione and categoria=:categoria and posizione=:posizione and unita=:unita and prezzo=:prezzo and scorta=:scorta and provenienza=:provenienza")
                .setParameter("descrizione", a.getDescrizione())
                .setParameter("categoria", a.getCategoria())
                .setParameter("posizione", a.getPosizione())
                .setParameter("unita", a.getUnita())
                .setParameter("prezzo", a.getPrezzo())
                .setParameter("scorta", a.getScorta())
                .setParameter("provenienza", a.getProvenienza())
                .unwrap(NativeQuery.class);
        BigInteger rowCnt = (BigInteger) query.getSingleResult();
        em.getTransaction().commit();
        em.clear();
        em.close();
        return rowCnt.intValue();
    }

    public static void insertArticle() {

        Articolo articolo = new Articolo();
        articolo.setCodice(formatUIDCode(articolo.getUID()));
        articolo.setDescrizione(jtfDescrizione.getText());
        articolo.setCategoria(new Categoria(String.valueOf(jcbCategoria.getSelectedItem()), false));
        articolo.setPosizione(new Posizione(String.valueOf(jcbPosizione.getSelectedItem()), false));
        articolo.setUnita(new Unita(String.valueOf(jcbUnita.getSelectedItem()), false));
        articolo.setPrezzo(Double.valueOf(String.valueOf(jtfCurrency.getValue())));
        articolo.setScorta(Integer.valueOf(jspScorta.getValue().toString()));
        articolo.setProvenienza(jtfProvenienza.getText());
        articolo.setDataIns(new Date());

        if (articolo.getDescrizione().isEmpty()) {
            showMessageDialog(null, "Descrizione articolo vuoto", "Info Dialog", JOptionPane.ERROR_MESSAGE);
        } else if (checkArticolo(articolo)>0) {
            showMessageDialog(null, "Articolo già presente", "Info Dialog", JOptionPane.ERROR_MESSAGE);
        } else {
            EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
            em.getTransaction().begin();
            em.persist(articolo);
            em.getTransaction().commit();
            Articolo a = em.find(Articolo.class, articolo.getUID());
            em.getTransaction().begin();
            a.setCodice(formatUIDCode(articolo.getUID()));
            em.getTransaction().commit();
            em.clear();
            em.close();
            tableModelArticolo.addRow(new String[]{String.valueOf(articolo.getUID()), formatUIDCode(articolo.getUID()), articolo.getDescrizione(), String.valueOf(articolo.getCategoria().getCategoria()), String.valueOf(articolo.getPosizione().getPosizione()), String.valueOf(articolo.getUnita().getUnita()), formatMoney(articolo.getPrezzo()), String.valueOf(articolo.getScorta()), articolo.getProvenienza(), (new SimpleDateFormat(DesktopRender.DATE_FORMAT)).format(articolo.getDataIns())});
            showMessageDialog(null, "Articolo inserito", "Info Dialog", JOptionPane.INFORMATION_MESSAGE);
            //refreshMovimentTable();
        }

        initArticlePane();
        articlePane.removeAll();
        articlePane.revalidate();
        articlePane.repaint();
        alignArticoloInit();
    }

    public static void updateArticle(Articolo item) {

        Articolo s = new Articolo();
        s.setUID(item.getUID());
        s.setDescrizione(jtfDescrizione.getText());
        s.setCategoria(new Categoria(String.valueOf(jcbCategoria.getSelectedItem()), false));
        s.setPosizione(new Posizione(String.valueOf(jcbPosizione.getSelectedItem()), false));
        s.setUnita(new Unita(String.valueOf(jcbUnita.getSelectedItem()), false));
        s.setPrezzo(Double.valueOf(jtfCurrency.getText().trim().replace("€", "").replace(",", ".")));
        s.setScorta(Integer.valueOf(jspScorta.getValue().toString()));
        s.setProvenienza(jtfProvenienza.getText());

        if (table.getSelectedRow() == -1) {
            showMessageDialog(null, "Selezionare l'articolo", "Info Dialog", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (checkUpdateArticle(s.getUID())) {
            articlePane.removeAll();
            articlePane.revalidate();
            articlePane.repaint();

            alignArticoloPost();

            EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
            em.getTransaction().begin();
            Articolo article = em.find(Articolo.class, s.getUID());
            article.setCodice(formatUIDCode(s.getUID()));
            article.setDescrizione(s.getDescrizione());

            Categoria categoria = new Categoria(String.valueOf(s.getCategoria().getCategoria()), false);
            article.setCategoria(categoria);
            Posizione posizione = new Posizione(String.valueOf(s.getPosizione().getPosizione()), false);
            article.setPosizione(posizione);
            Unita unita = new Unita(String.valueOf(s.getUnita().getUnita()), false);
            article.setUnita(unita);
            article.setPrezzo(s.getPrezzo());
            article.setScorta(s.getScorta());
            article.setProvenienza(s.getProvenienza());
            em.persist(article);
            em.getTransaction().commit();
            em.clear();
            em.close();

            int index = table.getSelectedRow();
            tableModelArticolo.setValueAt(article.getDescrizione(), index, 2);
            tableModelArticolo.setValueAt(categoria.getCategoria(), index, 3);
            tableModelArticolo.setValueAt(posizione.getPosizione(), index, 4);
            tableModelArticolo.setValueAt(unita.getUnita(), index, 5);
            tableModelArticolo.setValueAt(formatMoney(article.getPrezzo()), index, 6);
            tableModelArticolo.setValueAt(article.getScorta(), index, 7);
            tableModelArticolo.setValueAt(article.getProvenienza(), index, 8);

            showMessageDialog(null, "Articolo aggiornato", "Info Dialog", JOptionPane.INFORMATION_MESSAGE);
            //refreshMovimentTable();
        }

        initArticlePane();
        articlePane.removeAll();
        articlePane.revalidate();
        articlePane.repaint();
        alignArticoloInit();
    }


    private static boolean checkUpdateArticle(Integer UID) {
        EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        Articolo articolo = em.find(Articolo.class, UID);
        boolean isPresent = em.contains(articolo);
        em.getTransaction().commit();
        em.clear();
        em.close();
        return isPresent;
    }



    // inizializzazione del campi del pannello degli articoli
    public static void initArticlePane() {

        articlePane.removeAll();
        alignArticoloInit();
        articlePane.revalidate();
        articlePane.repaint();
        table.getSelectionModel().clearSelection();
        jtfCodice.setText(null);
        jtfDescrizione.setText(null);
        jcbCategoria.setSelectedIndex(0);
        jcbPosizione.setSelectedIndex(0);
        jcbUnita.setSelectedIndex(0);
        jtfCurrency.setValue(0);
        jspScorta.setValue(0);
        jtfProvenienza.setText(null);
    }

    public static ArrayList<Articolo> loadArticle() {

        EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        TypedQuery<Articolo> query = em.createQuery("SELECT a FROM Articolo a WHERE a.isDeleted=false", Articolo.class);
        List<Articolo> results = query.getResultList();
        em.getTransaction().commit();
        em.clear();
        em.close();
        if (results.isEmpty())
            return new ArrayList<>();
        return new ArrayList<>(results);
    }

    /*
    public static void refreshMovimentTable() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(DAOUtils.getAllCodici().toArray(new String[0]));
        MovimentiPane.jcbCodice.setModel(model);
        MovimentiPane.jcbCodice.validate();
        MovimentiPane.jcbCodice.repaint();
    }

     */

}
