package shop.entity;

import javax.persistence.*;

@Entity
@Table
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Giacenza {

    @Id
    @Column
    String codice;

    @Column
    String descrizione;

    @Column
    Integer giacenza;

    @Column
    Integer scorta;

    @Column
    Integer riordino;

    @Column
    Integer totcarico;

    @Column
    Integer totscarico;

    @Column
    String unita;

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Integer getGiacenza() {
        return giacenza;
    }

    public void setGiacenza(Integer giacenza) {
        this.giacenza = giacenza;
    }

    public Integer getScorta() {
        return scorta;
    }

    public void setScorta(Integer scorta) {
        this.scorta = scorta;
    }

    public Integer getRiordino() {
        return riordino;
    }

    public void setRiordino(Integer riordino) {
        this.riordino = riordino;
    }

    public Integer getTotcarico() {
        return totcarico;
    }

    public void setTotcarico(Integer totcarico) {
        this.totcarico = totcarico;
    }

    public Integer getTotscarico() {
        return totscarico;
    }

    public void setTotscarico(Integer totscarico) {
        this.totscarico = totscarico;
    }

    public String getUnita() {
        return unita;
    }

    public void setUnita(String unita) {
        this.unita = unita;
    }
}
