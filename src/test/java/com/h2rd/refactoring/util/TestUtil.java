package com.h2rd.refactoring.util;

import java.util.ArrayList;
import java.util.Arrays;
import com.h2rd.refactoring.model.Role;
import com.h2rd.refactoring.model.User;

public class TestUtil {
    
    public static User createUser(String name, String email, String roleName){
        return new User(name,email, new ArrayList<Role>(
                Arrays.asList(new Role(roleName))));
    }
    
}
