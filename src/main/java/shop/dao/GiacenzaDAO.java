package shop.dao;

import org.hibernate.query.NativeQuery;
import shop.entity.Giacenza;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.lang.Object;

public class GiacenzaDAO {

    public static ArrayList<Giacenza> getAllGiacenza() {

        EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        Query query = em.createNativeQuery("WITH prodotto_carico" +
                " AS (" +
                " SELECT SUM(QUANTITA) AS totcarico" +
                ",CODICE" +
                " FROM CARICO" +
                " WHERE CODICE = codice" +
                " AND ISDELETED = false" +
                " GROUP BY CODICE" +
                ")" +
                ",prodotto_scarico" +
                " AS (" +
                " SELECT SUM(QUANTITA) AS totscarico " +
                ",CODICE" +
                " FROM SCARICO" +
                " WHERE CODICE = codice" +
                " AND ISDELETED = false" +
                " GROUP BY CODICE" +
                " )" +
                "SELECT a.CODICE" +
                ",a.DESCRIZIONE" +
                ",COALESCE((c.totcarico - s.totscarico), 0) AS giacenza" +
                ",a.SCORTA" +
                ",COALESCE((a.SCORTA - (c.totcarico - s.totscarico)), 0) AS riordino" +
                ",COALESCE(c.totcarico, 0)" +
                ",COALESCE(s.totscarico, 0)" +
                ",a.UNITA" +
                " FROM ARTICOLO a " +
                "LEFT JOIN prodotto_carico c ON (a.CODICE = c.codice) " +
                "LEFT JOIN prodotto_scarico s ON (a.CODICE = s.codice) " +
                "WHERE c.totcarico > 0" +
                " OR s.totscarico > 0" +
                " GROUP BY a.CODICE" +
                " ORDER BY a.CODICE DESC")
                .unwrap(NativeQuery.class);
        List<?> items = query.getResultList();
        em.getTransaction().commit();
        em.clear();
        em.close();
        ArrayList<Giacenza> results = new ArrayList<>();
        if (items.isEmpty()) {
            return new ArrayList<>();
        }

        items.stream().map(item -> (Object[]) item).forEach(row -> {
            Giacenza ga = new Giacenza();
            ga.setCodice(String.valueOf(row[0]));
            ga.setDescrizione(String.valueOf(row[1]));
            ga.setGiacenza(Integer.valueOf(String.valueOf(row[2])));
            ga.setScorta(Integer.valueOf(String.valueOf(row[3])));
            ga.setRiordino(Integer.valueOf(String.valueOf(row[4])));
            ga.setTotcarico(Integer.valueOf(String.valueOf(row[5])));
            ga.setTotscarico(Integer.valueOf(String.valueOf(row[6])));
            ga.setUnita(String.valueOf(row[7]));
            results.add(ga);
        });

        return results;
    }

}
