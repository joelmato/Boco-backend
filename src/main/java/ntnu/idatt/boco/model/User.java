package ntnu.idatt.boco.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity @Data @NoArgsConstructor @AllArgsConstructor
@Table(name = "user")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;
    @Column(name = "fname")
    private String fname;
    @Column(name = "lname")
    private String lname;

    @Column(
            unique = true,
            name = "email"

    )
    private String email;
    @Column(name = "password")
    private String password;
    @ElementCollection
    private Collection<Role> roles = new ArrayList<>();

/**

    public Integer getUserId() {
        return id;
    }
    public String getFname() {
        return fname;
    }
    public String getLname() {
        return lname;
    }
    public String getEmail() {
        return username;
    }
    //@JsonIgnore

    public String getPassword() {
        return password;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }
    public void setLname(String lname) {
        this.lname = lname;
    }
    public void setEmail(String email) {
        this.username = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }


 **/
}


