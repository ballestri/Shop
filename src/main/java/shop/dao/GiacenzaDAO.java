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
        Query query = em.createNativeQuery("WITH prodotto_carico as (SELECT SUM(QUANTITA) as totcarico,CODICE FROM CARICO WHERE CODICE = codice and ISDELETED=false GROUP BY CODICE),prodotto_scarico as (SELECT SUM(QUANTITA) as totscarico,CODICE FROM SCARICO WHERE CODICE = codice and ISDELETED= false GROUP BY CODICE) SELECT a.CODICE,a.DESCRIZIONE,COALESCE((c.totcarico-s.totscarico),0) as giacenza,a.SCORTA,COALESCE((a.SCORTA - (c.totcarico-s.totscarico)),0) as riordino,COALESCE(c.totcarico,0),COALESCE(s.totscarico,0),a.UNITA FROM ARTICOLO a LEFT JOIN prodotto_carico c ON ( a.CODICE = c.codice) LEFT JOIN prodotto_scarico s ON ( a.CODICE = s.codice) WHERE c.totcarico >0 or s.totscarico> 0 GROUP BY a.CODICE ORDER BY a.CODICE DESC")
                .unwrap(NativeQuery.class);
        List<Object[]> items = query.getResultList();
        em.getTransaction().commit();
        em.clear();
        em.close();
        ArrayList<Giacenza> results = new ArrayList<>();
        if (items.isEmpty()) {
            return new ArrayList<>();
        }

        items.forEach(value -> {
            Giacenza ga = new Giacenza();
            ga.setCodice(String.valueOf(value[0]));
            ga.setDescrizione(String.valueOf(value[1]));
            ga.setGiacenza(Integer.valueOf(String.valueOf(value[2])));
            ga.setScorta(Integer.valueOf(String.valueOf(value[3])));
            ga.setRiordino(Integer.valueOf(String.valueOf(value[4])));
            ga.setTotcarico(Integer.valueOf(String.valueOf(value[5])));
            ga.setTotscarico(Integer.valueOf(String.valueOf(value[6])));
            ga.setUnita(String.valueOf(value[7]));
            results.add(ga);
        });

        return results;
    }

}
