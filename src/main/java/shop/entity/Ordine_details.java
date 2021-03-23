package shop.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)

@Table(name = "ORDER_DETAILS", uniqueConstraints = {
        @UniqueConstraint(columnNames = "orderID")})
public class Ordine_details implements Serializable {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer UID;

    @Column
    private String articleID;

    @Column
    private Integer quantity;

    @Column
    private Double price;

    @ManyToOne
    @JoinColumn(name = "orderID", nullable = false)
    private Ordine ordine;

    public Ordine_details() { }

    public Integer getUID() {
        return UID;
    }

    public void setUID(Integer UID) {
        this.UID = UID;
    }

    public String getArticleID() {
        return articleID;
    }

    public void setArticleID(String articleID) {
        this.articleID = articleID;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Ordine getOrdine() {
        return ordine;
    }

    public void setOrdine(Ordine ordine) {
        this.ordine = ordine;
    }
}
