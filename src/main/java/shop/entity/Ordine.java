package shop.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;


@Entity(name = "Ordine")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "ORDINE", uniqueConstraints = {
        @UniqueConstraint(columnNames = "orderID"),
        @UniqueConstraint(columnNames = "clientID") })

public class Ordine implements Serializable {

    @Id
    @Column
    private String orderID;

    @Column
    private String clientID;

    @Column
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column
    private Double importo;

    @Column
    private boolean state;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "orderID")
    private List<Ordine_details> items = new ArrayList<>();

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getImporto() {
        return importo;
    }

    public void setImporto(Double importo) {
        this.importo = importo;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
