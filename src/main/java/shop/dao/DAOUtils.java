package shop.dao;

import org.hibernate.query.NativeQuery;
import shop.entity.Articolo;
import shop.entity.Movimento;

import javax.persistence.*;
import java.sql.*;
import java.util.ArrayList;
import java.lang.Object;
import java.util.List;

public class DAOUtils {

    public static ArrayList<String> getListCodici() {

        EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        TypedQuery<String> query = em.createQuery("SELECT a.codice FROM Articolo a WHERE a.isDeleted=false", String.class);
        ArrayList<String> results = new ArrayList<>(query.getResultList());
        em.getTransaction().commit();
        em.clear();
        em.close();
        return results;
    }

    public static ArrayList<String> getListFornitore() {

        EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        TypedQuery<String> query = em.createQuery("SELECT f.cognome FROM Fornitore f WHERE f.isDeleted=false", String.class);

        List<String> results = query.getResultList();
        em.getTransaction().commit();
        em.clear();
        em.close();
        return new ArrayList<>(results);
    }

    public static Articolo getProduct(String codice) {

        EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        TypedQuery<Articolo> query = em.createQuery(
                "SELECT a FROM Articolo a WHERE a.isDeleted=false and a.codice=:codice", Articolo.class);
        query.setParameter("codice", codice);
        Articolo article = query.getSingleResult();
        em.getTransaction().commit();
        em.clear();
        em.close();
        return article;
    }

    public static ArrayList<Movimento> getAllMoviments(String codice) {

        EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        //Query query = em.createNativeQuery("SELECT T.codice,T.data, T.f,SUM(CASE WHEN T.carico_quantita THEN T.carico_quantita ELSE 0 END) AS carico,SUM(CASE WHEN T.scarico_quantita THEN T.scarico_quantita                ELSE 0 END) AS scarico FROM (SELECT c.codice,coalesce(s.data, c.data) AS DATA, COALESCE (c.fornitore,s.fornitore) AS f,c.quantita AS carico_quantita, s.quantita AS scarico_quantita FROM (SELECT codice,datacarico AS DATA,fornitore,sum(quantita) AS quantita FROM carico GROUP BY codice,datacarico,fornitore) AS c LEFT JOIN (SELECT codice,datascarico AS DATA, fornitore,sum(quantita) AS quantita FROM scarico GROUP BY codice,datascarico,fornitore) s ON (c.data=s.data) AND (c.fornitore=s.fornitore)  UNION ALL SELECT s.codice, coalesce(s.data, c.data) AS DATA,COALESCE (c.fornitore, s.fornitore) AS f, c.quantita AS carico_quantita, s.quantita AS scarico_quantita FROM (SELECT codice,datacarico AS DATA,fornitore,sum(quantita) AS quantita FROM carico GROUP BY codice, datacarico, fornitore) AS c RIGHT JOIN  (SELECT codice, datascarico AS DATA,fornitore, sum(quantita) AS quantita FROM scarico GROUP BY codice, datascarico, fornitore) s ON (c.data=s.data) AND (c.fornitore=s.fornitore) WHERE c.data IS NULL ) AS T WHERE T.codice =:codice GROUP BY T.codice, T.data, T.f ORDER BY T.data").unwrap(NativeQuery.class);
        Query query = em.createNativeQuery("SELECT T.codice,T.data, T.f,SUM(CASE WHEN T.carico_quantita THEN T.carico_quantita ELSE 0 END) AS carico,SUM(CASE WHEN T.scarico_quantita THEN T.scarico_quantita ELSE 0 END) AS scarico FROM   (SELECT c.codice,COALESCE(s.data, c.data) AS DATA, COALESCE (c.fornitore, s.fornitore) AS f, c.quantita   AS carico_quantita, s.quantita  AS scarico_quantita FROM   (SELECT codice, datacarico    AS DATA,fornitore, SUM(quantita) AS quantita FROM  CARICO WHERE ISDELETED=false GROUP  BY codice, datacarico, fornitore) AS c LEFT JOIN (SELECT codice,datascarico  AS DATA, fornitore,SUM(quantita) AS quantita FROM   SCARICO  WHERE ISDELETED=false GROUP  BY codice, datascarico, fornitore) s ON ( c.data = s.data )                                  AND ( c.fornitore = s.fornitore )         UNION ALL         SELECT s.codice,                COALESCE(s.data, c.data)            AS DATA,                COALESCE (c.fornitore, s.fornitore) AS f,                c.quantita                          AS carico_quantita,s.quantita  AS scarico_quantita FROM   (SELECT codice, datacarico AS DATA,fornitore,SUM(quantita) AS quantita FROM   CARICO WHERE ISDELETED=false GROUP  BY codice, datacarico,fornitore) AS c  RIGHT JOIN (SELECT codice,datascarico   AS DATA, fornitore, SUM(quantita) AS quantita FROM   SCARICO WHERE ISDELETED=false  GROUP  BY codice, datascarico,fornitore) s  ON ( c.data = s.data ) AND ( c.fornitore = s.fornitore ) WHERE  c.data IS NULL) AS T WHERE  T.codice = :codice GROUP  BY T.codice, T.data,T.f ORDER  BY T.data"
        ).setParameter("codice", codice)
                .unwrap(NativeQuery.class);

        List<Object[]> items = query.getResultList();
        em.getTransaction().commit();
        em.clear();
        em.close();

        if (items.isEmpty())
            return new ArrayList<>();

        ArrayList<Movimento> results = new ArrayList<>();
        items.forEach(value -> {
            Movimento movimento = new Movimento();
            movimento.setCodice(String.valueOf(value[0]));
            movimento.setData(new Date(((Timestamp) value[1]).getTime()));
            movimento.setFornitore(String.valueOf(value[2]));
            movimento.setCarico(Integer.valueOf(String.valueOf(value[3])));
            movimento.setScarico(Integer.valueOf(String.valueOf(value[4])));
            results.add(movimento);
        });

        return results;
    }
}
