package com.h2rd.refactoring.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import com.h2rd.refactoring.model.Role;
import com.h2rd.refactoring.model.User;
import com.h2rd.refactoring.repository.UserRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryIntegrationTest {
    @Autowired
    private TestEntityManager entityManager;
 
    @Autowired
    private UserRepository userRepository;
    
    @After
    public void resetDb() {
        userRepository.deleteAll();
    }
    
    @Test
    public void givenAUser_whenFindById_thenReturnTheUser() {
        User user1 = createUser("john","john@gmail.com","Student");
        entityManager.persist(user1);
        entityManager.flush();
        
        User foundUser = userRepository.findById("john@gmail.com").get();
        Assert.assertEquals(user1.getName(), foundUser.getName());
        Assert.assertEquals(user1.getEmail(), foundUser.getEmail());
        Assert.assertEquals(user1.getRoles().get(0).getName(), foundUser.getRoles().get(0).getName());
    }
    
    @Test
    public void givenSetOfUsers_whenFindAll_thenReturnAllEmployees() {
        User user1 = createUser("john","john@gmail.com","Student");
        User user2 = createUser("peter","peter@gmail.com","Teacher");
        User user3 = createUser("steven","steven@gmail.com","Administrator");
        
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(user3);
        entityManager.flush();

        List<User> allUsers = userRepository.findAll();

        assertThat(allUsers).hasSize(3).extracting(User::getName).containsOnly(user1.getName(), user2.getName(), user3.getName());
    }
    
    private User createUser(String name, String email, String roleName){
        return new User(name,email, new ArrayList<Role>(
                Arrays.asList(new Role(roleName))));
    }

}
