package shop.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table
public class Credentials implements Serializable{

    @Id
    @Column
    private String username;

    @Id
    @Column
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
