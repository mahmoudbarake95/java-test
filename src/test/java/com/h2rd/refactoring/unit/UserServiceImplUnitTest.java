package com.h2rd.refactoring.unit;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import com.h2rd.refactoring.exception.ResourceNotFoundException;
import com.h2rd.refactoring.model.User;
import com.h2rd.refactoring.repository.UserRepository;
import com.h2rd.refactoring.service.UserService;
import com.h2rd.refactoring.service.UserServiceImpl;
import com.h2rd.refactoring.util.TestUtil;

@RunWith(SpringRunner.class)
public class UserServiceImplUnitTest {
    
    @TestConfiguration
    static class UserServiceImplTestContextConfiguration {
        @Bean
        public UserService userService() {
            return new UserServiceImpl();
        }
    }

    @Autowired
    private UserServiceImpl userService;

    @MockBean
    private UserRepository userRepository;

    @Before
    public void setUp() {
        User user1 = TestUtil.createUser("Mahmoud", "mahmoudbarake95@gmail.com", "student");
        User user2 = TestUtil.createUser("john", "john@gmail.com", "teacher");
        List<User> allUsers = Arrays.asList(user1, user2);
        
        Mockito.when(userRepository.findById(user1.getEmail())).thenReturn(Optional.of(user1));
        Mockito.when(userRepository.findById(user2.getEmail())).thenReturn(Optional.of(user2));
        
        Mockito.when(userRepository.findAll()).thenReturn(allUsers);
        
        /*Employee john = new Employee("john");
        john.setId(11L);

        Employee bob = new Employee("bob");
        Employee alex = new Employee("alex");

        List<Employee> allEmployees = Arrays.asList(john, bob, alex);

        Mockito.when(employeeRepository.findByName(john.getName())).thenReturn(john);
        Mockito.when(employeeRepository.findByName(alex.getName())).thenReturn(alex);
        Mockito.when(employeeRepository.findByName("wrong_name")).thenReturn(null);
        Mockito.when(employeeRepository.findById(john.getId()).orElse(null)).thenReturn(john);
        Mockito.when(employeeRepository.findAll()).thenReturn(allEmployees);
        Mockito.when(employeeRepository.findById(-99L).orElse(null)).thenReturn(null);*/
    }
    
    @Test
    public void whenGetAllUsers_thenReturnAllUsers() {
        User user1 = TestUtil.createUser("Mahmoud", "mahmoudbarake95@gmail.com", "student");
        User user2 = TestUtil.createUser("john", "john@gmail.com", "teacher");
        List<User> allUsers = Arrays.asList(user1, user2);
        
        List<User> foundUsers = userService.getAllUsers();
        verifyFindAllUsesIsCalled(1);
        assertThat(foundUsers).hasSize(2).extracting(User::getName).contains(user1.getName(), user2.getName());
        assertThat(foundUsers).hasSize(2).extracting(User::getEmail).contains(user1.getEmail(), user2.getEmail());
    }
    
    @Test
    public void whenGetUserAndUserExists_thenReturnUser() {
        User user1 = TestUtil.createUser("Mahmoud", "mahmoudbarake95@gmail.com", "student");
        Mockito.when(userRepository.existsById(user1.getEmail())).thenReturn(true);
        User foundUser = userService.getUser(user1.getEmail()).get();
        
        verifyFindByIdIsCalled(1);
        assertThat(foundUser).extracting(User::getName).contains(user1.getName());
//        assertThat(foundUser).extracting(User::getName).contains(user1.getName());
    }
    
    @Test(expected=ResourceNotFoundException.class)
    public void whenGetUserAndUserDoesNotExist_thenThrowResourceNotFoundException() {
        User foundUser = userService.getUser("mahmoudbarake95@gmail.com").get();
    }
    
    
    
    private void verifyFindByIdIsCalled(int numberOfTimes) {
        Mockito.verify(userRepository, VerificationModeFactory.times(numberOfTimes)).findById(Mockito.anyString());
        Mockito.reset(userRepository);
    }
    
    private void verifyFindAllUsesIsCalled(int numberOfTimes) {
        Mockito.verify(userRepository, VerificationModeFactory.times(numberOfTimes)).findAll();
        Mockito.reset(userRepository);
    }
}
