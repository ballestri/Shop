package shop.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name="Ordine_details")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "order_details")
public class Ordine_details implements Serializable {

    @Id
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    @JoinColumn(name = "order_id", nullable = false, referencedColumnName = "order_id",foreignKey=@ForeignKey(name = "FK_order"))
    private Ordine ordine;

    @Id
    @PrimaryKeyJoinColumn
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "articolo_id", nullable = false, referencedColumnName = "articolo_id",foreignKey=@ForeignKey(name = "FK_articolo"))
    private Articolo articolo;

    @Column
    private Integer quantity;

    @Column
    private Double prezzo;

    public Ordine getOrdine() {
        return ordine;
    }

    public void setOrdine(Ordine ordine) {
        this.ordine = ordine;
    }

    public Articolo getArticolo() {
        return articolo;
    }

    public void setArticolo(Articolo articolo) {
        this.articolo = articolo;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(Double prezzo) {
        this.prezzo = prezzo;
    }
}
