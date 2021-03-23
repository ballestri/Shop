package shop.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table
public class Unita implements Serializable {

    @Id
    @Column
    private String unita;

    @Column
    private boolean isDeleted;

    public Unita() {}

    public Unita(String unita, boolean isDeleted) {
        this.unita = unita;
        this.isDeleted = isDeleted;
    }

    public String getUnita() {
        return unita;
    }

    public void setUnita(String unita) {
        this.unita = unita;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
