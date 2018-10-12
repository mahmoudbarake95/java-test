package com.h2rd.refactoring.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Role implements Serializable{
    /*In real case, we would have a Role endpoint for the CRUD operations on the entity. 
    This was skipped as it is not part of this Java exercise 
    (In this exercise we only do the CRUD operations for the user)
    
    Also in real case, we would have another entity called "Right" and the Role entity would have a list of Rights
    This was not implemented as the goal of this exercise is only to make sure that a user has at least 1 role*/
    
    public Role(String name) {
        this.name = name;
    }
    
    @Id
    private String name;

}
