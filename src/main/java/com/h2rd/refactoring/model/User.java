package com.h2rd.refactoring.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

//add @data annotation

@Entity
@Table(
        name="Users", 
        uniqueConstraints= @UniqueConstraint(columnNames={"email"})
        )
public class User {


    //    @GeneratedValue
    //    private Long id;

    @NotBlank
    String name;

    @Id
    @Email(message = "Email should be valid")
    String email;
    //    List<String> roles;

    //    public User() {
    //        super();
    //    }

    //to remove later
    //    public Long getId() {
    //        return id;
    //    }
    //    public void setId(Long id) {
    //        this.id = id;
    //    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    //    public List<String> getRoles() {
    //        return roles;
    //    }
    //    public void setRoles(List<String> roles) {
    //        this.roles = roles;
    //    }
}
