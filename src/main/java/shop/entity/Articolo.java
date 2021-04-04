package shop.entity;
import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="ARTICOLO")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Articolo implements Serializable {

    @Id
    @Column(name = "articolo_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer UID;

    @Column(unique = true)
    private String codice;

    @Column(nullable = false)
    private String descrizione;

    @ManyToOne
    @JoinColumn(name="categoria", referencedColumnName="categoria", foreignKey=@ForeignKey(name = "FK_categoria"))
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name="posizione", referencedColumnName="posizione", foreignKey=@ForeignKey(name = "FK_posizione"))
    private Posizione posizione;

    @ManyToOne
    @JoinColumn(name="unita", referencedColumnName="unita", foreignKey=@ForeignKey(name = "FK_unita"))
    private Unita unita;

    @Column
    private Double prezzo;

    @Column
    private Integer scorta;

    @Column
    private String provenienza;

    @Column
    @Temporal(TemporalType.DATE)
    private Date dataIns;

    @Column
    private boolean isDeleted;

    //@OneToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    //@JoinColumn(name = "UID", nullable = false)
    //private Ordine_details ordineDetails;

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

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Posizione getPosizione() {
        return posizione;
    }

    public void setPosizione(Posizione posizione) {
        this.posizione = posizione;
    }

    public Unita getUnita() {
        return unita;
    }

    public void setUnita(Unita unita) {
        this.unita = unita;
    }

    public Double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(Double prezzo) {
        this.prezzo = prezzo;
    }

    public Integer getScorta() {
        return scorta;
    }

    public void setScorta(Integer scorta) {
        this.scorta = scorta;
    }

    public String getProvenienza() {
        return provenienza;
    }

    public void setProvenienza(String provenienza) {
        this.provenienza = provenienza;
    }

    public Date getDataIns() {
        return dataIns;
    }

    public void setDataIns(Date dataIns) {
        this.dataIns = dataIns;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }


}


