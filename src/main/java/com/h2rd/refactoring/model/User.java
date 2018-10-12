package com.h2rd.refactoring.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import com.h2rd.refactoring.listener.UserListener;

//add @data annotation

@Entity
@Table(
        name="Users", 
        uniqueConstraints= @UniqueConstraint(columnNames={"email"})
        )
@EntityListeners(UserListener.class)
public class User {
    @Id
    @Email(message = "Email should be valid")
    String email;
    
    @NotBlank
    String name;
    
//    @NotEmpty.List(@NotEmpty Role) 
    ArrayList<Role> roles;

    //    public User() {
    //        super();
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
    
    public List<Role> getRoles() {
        return roles;
    }
    
    public void setRoles(ArrayList<Role> roles) {
        this.roles = roles;
    }
    
    public boolean hasRoles(){
        return !this.roles.isEmpty();
    }
    
    public void addRole(Role role){
        this.roles.add(role);
    }
    
}
