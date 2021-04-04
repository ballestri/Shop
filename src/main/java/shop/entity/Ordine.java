package shop.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;


@Entity(name = "Ordine")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "ORDINE", uniqueConstraints = { @UniqueConstraint(columnNames = "client_id") })
public class Ordine implements Serializable {

    @Id
    @Column(name = "order_id")
    private String orderID;

    @ManyToOne
    @JoinColumn(name="client_id", referencedColumnName="client_id", foreignKey=@ForeignKey(name = "FK_client"))
    private Cliente cliente;

    @Column
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column
    private Double importo;

    @Column
    private boolean state;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private Set<Ordine_details> items = new HashSet<>();

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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

    public Set<Ordine_details> getItems() {
        return items;
    }

    public void setItems(Set<Ordine_details> items) {
        this.items = items;
    }
}
