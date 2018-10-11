package com.h2rd.refactoring.application.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

//add @data annotation
@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;

    String name;
    String email;
//    List<String> roles;
    
//    public User() {
//        super();
//    }
    
    //to remove later
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    
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
