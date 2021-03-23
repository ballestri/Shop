package shop.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table
public class Categoria implements Serializable {

    @Id
    @Column
    private String categoria;

    @Column
    private boolean isDeleted;

    public Categoria() { }

    public Categoria(String categoria, boolean isDeleted) {
        this.categoria = categoria;
        this.isDeleted = isDeleted;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
