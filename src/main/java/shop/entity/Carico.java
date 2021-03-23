package shop.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Carico implements Serializable {

    @Id
    @GeneratedValue
    @Column
    private Integer UID;

    @Column
    private String codice;

    @Column
    private String descrizione;

    @Column
    @Temporal(TemporalType.DATE)
    private Date datacarico;

    @Column
    private Integer quantita;

    @Column
    private Double importo;

    @Column
    private String fornitore;

    @Column
    @Temporal(TemporalType.DATE)
    private Date datascadenza;

    @Column
    private String note;

    @Column
    private boolean isDeleted;

    public Integer getUID() {
        return UID;
    }

    public void setUID(Integer UID) {
        this.UID = UID;
    }

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

    public Date getDatacarico() {
        return datacarico;
    }

    public void setDatacarico(Date datacarico) {
        this.datacarico = datacarico;
    }

    public Integer getQuantita() {
        return quantita;
    }

    public void setQuantita(Integer quantita) {
        this.quantita = quantita;
    }

    public Double getImporto() {
        return importo;
    }

    public void setImporto(Double importo) {
        this.importo = importo;
    }

    public String getFornitore() {
        return fornitore;
    }

    public void setFornitore(String fornitore) {
        this.fornitore = fornitore;
    }

    public Date getDatascadenza() {
        return datascadenza;
    }

    public void setDatascadenza(Date datascadenza) {
        this.datascadenza = datascadenza;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
