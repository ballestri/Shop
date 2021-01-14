package shop.view.giacenza.controller;

import shop.db.ConnectionManager;
import shop.model.Giacenza;
import java.sql.*;
import java.util.ArrayList;

public class GiacenzaDbOperation {

    public static ArrayList<Giacenza> getGiacenzaDbItems() {

        ArrayList<Giacenza> list_giacenza = new ArrayList<>();
        try {
            Connection con = (new ConnectionManager()).getConnection();

            //String QUERY = "SELECT A.CODICE,A.DESCRIZIONE, (SELECT SUM(C.QUANTITA) - SELECT SUM(S.QUANTITA)) AS GIACENZA ,A.SCORTA,(A.SCORTA -(SELECT SUM(C.QUANTITA) - SELECT SUM(S.QUANTITA))) AS RIORDINO, (SELECT SUM(C.QUANTITA)) AS TOTCARICO,(SELECT SUM(S.QUANTITA)) AS TOTSCARICO,A.UNITA FROM ARTICOLO A JOIN CARICO C ON (A.CODICE = C.CODICE) JOIN SCARICO S ON (A.CODICE = S.CODICE) GROUP BY A.CODICE";
            //String QUERY ="SELECT a.codice, a.descrizione,((SELECT sum(quantita) from carico where codice = a.codice) -(SELECT sum(quantita) from scarico where codice = a.codice)) as giacenza, a.scorta, (a.scorta - ((SELECT sum(quantita) from carico where codice = a.codice) -(SELECT sum(quantita) from scarico where codice = a.codice))) as riordino, (SELECT sum(quantita) from carico where codice = a.codice) AS totcarico, (SELECT sum(quantita) from scarico where codice = a.codice) AS totscarico, a.unita FROM articolo a LEFT JOIN carico c ON (a.codice = c.codice)  LEFT join scarico s ON  (a.codice = s.codice) GROUP BY a.codice";
            String QUERY="SELECT a.codice, a.descrizione,((SELECT sum(quantita) from carico where codice = a.codice) -(SELECT sum(quantita) from scarico where codice = a.codice)) as giacenza, a.scorta, (a.scorta - ((SELECT sum(quantita) from carico where codice = a.codice) -(SELECT sum(quantita) from scarico where codice = a.codice))) as riordino, (SELECT sum(quantita) from carico where codice = a.codice) AS totcarico, (SELECT sum(quantita) from scarico where codice = a.codice) AS totscarico, a.unita FROM articolo a LEFT JOIN carico c ON (a.codice = c.codice)  LEFT join scarico s ON  (a.codice = s.codice) WHERE (((SELECT SUM(quantita) from carico where codice = a.codice) > 0) OR ((SELECT SUM(quantita) from scarico where codice = a.codice) > 0)) GROUP BY a.codice ORDER BY a.codice DESC ";
            ResultSet rs = con.createStatement().executeQuery(QUERY);

            while (rs.next()) {
                Giacenza giacenza = new Giacenza();
                giacenza.setCodice(rs.getString("CODICE"));
                giacenza.setDescrizione(rs.getString("DESCRIZIONE"));
                giacenza.setGiacenza(rs.getInt("GIACENZA"));
                giacenza.setScorta(rs.getInt("SCORTA"));
                giacenza.setRiordino(rs.getInt("RIORDINO"));
                giacenza.setTotcarico(rs.getInt("TOTCARICO"));
                giacenza.setTotscarico(rs.getInt("TOTSCARICO"));
                giacenza.setUnita(rs.getString("UNITA"));
                list_giacenza.add(giacenza);
            }
            con.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list_giacenza;
    }

}
