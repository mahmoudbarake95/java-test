package com.h2rd.refactoring.unit;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

    @Test
    public void givenUsers_whenGetAllUsers_thenReturnAllUsers() {
        //given
        User user1 = TestUtil.createUser("Mahmoud", "mahmoud@gmail.com", "student");
        User user2 = TestUtil.createUser("john", "john@gmail.com", "teacher");
        List<User> allUsers = Arrays.asList(user1, user2);
        Mockito.when(userRepository.findAll()).thenReturn(allUsers);
        //when
        List<User> foundUsers = userService.getAllUsers();
        //then
        verifyFindAllUsersIsCalled(1);
        assertThat(foundUsers).hasSize(2).extracting(User::getName).contains(user1.getName(), user2.getName());
        assertThat(foundUsers).hasSize(2).extracting(User::getEmail).contains(user1.getEmail(), user2.getEmail());
    }

    @Test
    public void givenUser_whenGetUser_thenReturnUser() {
        //given
        User user1 = TestUtil.createUser("Mahmoud", "mahmoud@gmail.com", "student");
        Mockito.when(userRepository.existsById(user1.getEmail())).thenReturn(true);
        Mockito.when(userRepository.findById(user1.getEmail())).thenReturn(Optional.of(user1));
        //when
        User foundUser = userService.getUser(user1.getEmail()).get();
        //then
        verifyFindByIdIsCalled(1);
        assertThat(foundUser).extracting(User::getName).contains(user1.getName());
    }

    @Test
    public void whenCreateUser_thenUserCreatedSuccessfully() {
        //given
        User user1 = TestUtil.createUser("Mahmoud", "mahmoud@gmail.com", "student");
        Mockito.when(userRepository.save(user1)).thenReturn(user1);
        Mockito.when(userRepository.findById(user1.getEmail())).thenReturn(Optional.of(user1));
        //when
        User createdUser = userService.createUser(user1);
        //then
        verifySaveIsCalled(1);
        assertThat(createdUser).extracting(User::getName).containsOnly(user1.getName());
    }

    @Test
    public void whenUpdateUser_thenUserUpdatedSuccessfully() {
        //given
        User user1 = TestUtil.createUser("Mahmoud", "mahmoud@gmail.com", "student");
        user1.setName("Mahmoud Barake");
        Mockito.when(userRepository.save(user1)).thenReturn(user1);//return user with updated name
        Mockito.when(userRepository.existsById(user1.getEmail())).thenReturn(true);
        //when
        User updatedUser = userService.updateUser(user1, user1.getEmail());
        //then
        verifySaveIsCalled(1);
        assertThat(updatedUser).extracting(User::getName).contains(user1.getName());
    }

    //testing delete method skipped

    @Test(expected=ResourceNotFoundException.class)
    public void givenUserDoesNotExist_whenGetUser_thenThrowResourceNotFoundException() {
        //given
        //user does not exist
        //when
        userService.getUser("mahmoud@gmail.com").get();
        //then
        //throw ResourceNotFoundException
    }

    @Test(expected=ResourceNotFoundException.class)
    public void givenUserDoesNotExist_whenUpdateUser_thenThrowResourceNotFoundException() {
        //given
        User user1 = TestUtil.createUser("Mahmoud", "mahmoud@gmail.com", "student");
        user1.setName("Mahmoud Barake");
        Mockito.when(userRepository.save(user1)).thenReturn(user1);
        //when
        userService.updateUser(user1, user1.getEmail());
        //then
        //a ResourceNotFoundException is thrown
    }


    private void verifyFindByIdIsCalled(int numberOfTimes) {
        Mockito.verify(userRepository, VerificationModeFactory.times(numberOfTimes)).findById(Mockito.anyString());
        Mockito.reset(userRepository);
    }

    private void verifyFindAllUsersIsCalled(int numberOfTimes) {
        Mockito.verify(userRepository, VerificationModeFactory.times(numberOfTimes)).findAll();
        Mockito.reset(userRepository);
    }

    private void verifySaveIsCalled(int numberOfTimes) {
        Mockito.verify(userRepository, VerificationModeFactory.times(numberOfTimes)).save(Mockito.any(User.class));
        Mockito.reset(userRepository);
    }
}
