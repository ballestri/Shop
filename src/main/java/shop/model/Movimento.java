package shop.model;

import java.util.Date;

public class Movimento {
    private String codice;
    private Date data;
    private String fornitore;
    private Integer carico;
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
