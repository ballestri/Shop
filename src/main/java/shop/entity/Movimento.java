package shop.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Movimento {

    @Id
    @Column
    private String codice;

    @Column
    private Date data;

    @Column
    private String fornitore;

    @Column
    private Integer carico;

    @Column
    private Integer scarico;

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getFornitore() {
        return fornitore;
    }

    public void setFornitore(String fornitore) {
        this.fornitore = fornitore;
    }

    public Integer getCarico() {
        return carico;
    }

    public void setCarico(Integer carico) {
        this.carico = carico;
    }

    public Integer getScarico() {
        return scarico;
    }

    public void setScarico(Integer scarico) {
        this.scarico = scarico;
    }
}
