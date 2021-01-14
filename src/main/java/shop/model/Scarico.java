package shop.model;

import java.util.Date;

public class Scarico {

    private Integer id;
    private String codice;
    private String descrizione;
    private Date datascarico;
    private Integer quantita;
    private String fornitore;
    private String note;

    public Integer getID() {
        return id;
    }

    public void setID(Integer id) {
        this.id = id;
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


    public Date getDatascarico() {
        return datascarico;
    }

    public void setDatascarico(Date datascarico) {
        this.datascarico = datascarico;
    }

    public Integer getQuantita() {
        return quantita;
    }

    public void setQuantita(Integer quantita) {
        this.quantita = quantita;
    }

    public String getFornitore() {
        return fornitore;
    }

    public void setFornitore(String fornitore) {
        this.fornitore = fornitore;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
