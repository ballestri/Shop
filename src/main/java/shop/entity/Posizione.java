package shop.entity;

import javax.persistence.*;

@Entity
@Table
public class Posizione {

    @Id
    @Column
    private String posizione;

    @Column
    private boolean isDeleted;

    public Posizione() { }

    public Posizione(String posizione, boolean isDeleted) {
        this.posizione = posizione;
        this.isDeleted = isDeleted;
    }

    public String getPosizione() {
        return posizione;
    }

    public void setPosizione(String posizione) {
        this.posizione = posizione;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
