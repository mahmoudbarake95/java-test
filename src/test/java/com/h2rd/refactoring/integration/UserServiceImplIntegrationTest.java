package com.h2rd.refactoring.integration;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.h2rd.refactoring.exception.BadRequestException;
import com.h2rd.refactoring.exception.ResourceNotFoundException;
import com.h2rd.refactoring.model.User;
import com.h2rd.refactoring.repository.UserRepository;
import com.h2rd.refactoring.service.UserServiceImpl;
import com.h2rd.refactoring.util.TestUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplIntegrationTest {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserServiceImpl userService;
    
    @After
    public void resetDb() {
        userRepository.deleteAll();
    }
    
    @Test
    public void whenGetAllUsersAndUsersExist_thenAllUsersFound() {
        User user1  = TestUtil.createUser("john", "john@gmail.com", "student");
        User user2  = TestUtil.createUser("mahmoud", "mahmoud@gmail.com", "student");
        userRepository.saveAndFlush(user1);
        userRepository.saveAndFlush(user2);
        
        List<User> foundUsers = userService.getAllUsers();
        assertThat(foundUsers.size()==2);
        assertThat(foundUsers).extracting(User::getName).contains(user1.getName(),user2.getName());
    }
    
    @Test
    public void whenGetUserAndUsersExist_thenUserFound() {
        User user1  = TestUtil.createUser("john", "john@gmail.com", "student");
        userRepository.saveAndFlush(user1);
        
        User foundUser = userService.getUser(user1.getEmail()).get();
        assertThat(foundUser).extracting(User::getName).containsOnly(user1.getName());
    }
    
    @Test
    public void whenCreateUser_thenUserCreatedSuccessfully() {
        User user1  = TestUtil.createUser("john", "john@gmail.com", "student");
        userService.createUser(user1);
        assertThat(userRepository.existsById(user1.getEmail()));
    }
    
    @Test
    public void whenUpdateUser_thenUserUpdatedSuccessfully() {
        User user1  = TestUtil.createUser("john", "john@gmail.com", "student");
        userRepository.saveAndFlush(user1);
        
        user1.setName("john smith");
        User updatedUser = userService.updateUser(user1, "john@gmail.com");
        assertThat(updatedUser).extracting(User::getName).containsOnly(updatedUser.getName());
    }
    
    @Test
    public void whenDeleteUser_thenUserDeletedSuccessfully() {
        User user1  = TestUtil.createUser("john", "john@gmail.com", "student");
        userRepository.saveAndFlush(user1);
        userService.deleteUser(user1.getEmail());
        assertThat(!userRepository.existsById(user1.getEmail()));
    }
    
    @Test(expected=ResourceNotFoundException.class)
    public void whenGetUsersAndNoUsersExist_thenNoUsersFound() {
        userService.getUser("john@gmail.com").get();
    }
    
    @Test(expected=BadRequestException.class)
    public void whenUpdateUserAndEmailChanged_thenUserNotUpdated() {
        User user1  = TestUtil.createUser("john", "john@gmail.com", "student");
        userRepository.saveAndFlush(user1);
        
        user1.setEmail("john.smith@gmail.com");
        userService.updateUser(user1, "john@gmail.com");
    }
    
}
