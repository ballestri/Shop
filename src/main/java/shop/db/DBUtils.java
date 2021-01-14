package shop.db;

import shop.model.Articolo;
import shop.model.Movimento;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DBUtils {

    public static ArrayList<String> getListCodici() {
        ArrayList<String> codici = new ArrayList<>();
        try {
            Connection con = (new ConnectionManager()).getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT CODICE FROM ARTICOLO");
            while (rs.next()) {
                codici.add(rs.getString("CODICE"));
            }
            stmt.close();
            con.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return codici;
    }

    public static ArrayList<String> getListFornitori() {
        ArrayList<String> codici = new ArrayList<>();
        try {
            Connection con = (new ConnectionManager()).getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COGNOME FROM FORNITORE");
            while (rs.next()) {
                codici.add(rs.getString("COGNOME"));
            }
            stmt.close();
            con.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return codici;
    }

    public static Articolo getProduct(String codice) {
        Articolo articolo = new Articolo();
        try {
            Connection con = (new ConnectionManager()).getConnection();
            ResultSet rs = con.createStatement().executeQuery(String.format("SELECT* FROM ARTICOLO WHERE CODICE='%s'", codice));
            if (rs.next()) {
                articolo.setDescrizione(rs.getString("DESCRIZIONE"));
                articolo.setCategoria(rs.getString("CATEGORIA"));
                articolo.setPosizione(rs.getString("POSIZIONE"));
                articolo.setUnita(rs.getString("UNITA"));
                articolo.setPrezzo(rs.getDouble("PREZZO"));
                articolo.setScorta(rs.getInt("SCORTA"));
                articolo.setProvenienza(rs.getString("PROVENIENZA"));
            }
            con.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return articolo;
    }

    public static ArrayList<Movimento> getAllMoviments(String codice) {

        ArrayList<Movimento> movimenti = new ArrayList<>();

        try {
            Connection con = (new ConnectionManager()).getConnection();
            String QUERY = "SELECT T.codice, T.data, T.f, SUM(case when T.carico_quantita then T.carico_quantita else 0 end) as carico, SUM(case when T.scarico_quantita then T.scarico_quantita else 0 end) as scarico FROM (     select c.codice, coalesce(s.data, c.data) as data, coalesce (c.fornitore,s.fornitore) as f, c.quantita as carico_quantita, s.quantita as scarico_quantita     from (         select codice, datacarico as data, fornitore, sum(quantita) as quantita          from carico          GROUP BY codice, datacarico, fornitore     ) as c left join (         select codice, datascarico as data, fornitore, sum(quantita) as quantita         from scarico         GROUP BY codice, datascarico, fornitore     ) s on (c.data=s.data)  and (c.fornitore=s.fornitore)     union all     select s.codice, coalesce(s.data, c.data) as data, coalesce (c.fornitore,s.fornitore) as f, c.quantita as carico_quantita, s.quantita as scarico_quantita     from (         select codice, datacarico as data, fornitore, sum(quantita) as quantita          from carico          GROUP BY codice, datacarico, fornitore     ) as c right join (         select codice, datascarico as data, fornitore, sum(quantita) as quantita         from scarico         GROUP BY codice, datascarico, fornitore     ) s on (c.data=s.data)  and (c.fornitore=s.fornitore)     where c.data is null ) as T where T.codice ='%s' GROUP BY T.codice, T.data, T.f order by T.data";
            ResultSet rs = (con.createStatement()).executeQuery(String.format(QUERY, codice));
            while (rs.next()){
                Movimento movimento = new Movimento();
                movimento.setCodice(rs.getString("CODICE"));
                movimento.setData(rs.getDate("DATA"));
                movimento.setFornitore(rs.getString("F"));
                movimento.setCarico(rs.getInt("CARICO"));
                movimento.setScarico(rs.getInt("SCARICO"));
                movimenti.add(movimento);
            }
            con.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return movimenti;
    }
}
