package com.h2rd.refactoring.integration;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import com.h2rd.refactoring.exception.BadRequestException;
import com.h2rd.refactoring.model.User;
import com.h2rd.refactoring.repository.UserRepository;
import com.h2rd.refactoring.util.TestUtil;

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
        //given
        User user1 = TestUtil.createUser("john","john@gmail.com","Student");
        entityManager.persist(user1);
        entityManager.flush();
        //when
        User foundUser = userRepository.findById("john@gmail.com").get();
        //then
        Assert.assertEquals(user1.getName(), foundUser.getName());
        Assert.assertEquals(user1.getEmail(), foundUser.getEmail());
        Assert.assertEquals(user1.getRoles().get(0).getName(), foundUser.getRoles().get(0).getName());
    }

    @Test
    public void givenUsers_whenFindAll_thenReturnAllUsers() {
        //given
        User user1 = TestUtil.createUser("john","john@gmail.com","Student");
        User user2 = TestUtil.createUser("peter","peter@gmail.com","Teacher");
        User user3 = TestUtil.createUser("steven","steven@gmail.com","Administrator");
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(user3);
        entityManager.flush();
        //when
        List<User> allUsers = userRepository.findAll();
        //then
        assertThat(allUsers).hasSize(3).extracting(User::getName).containsOnly(user1.getName(), user2.getName(), user3.getName());
    }

    @Test
    public void whenCreateUser_thenUserCreatedSuccessfully() {
        //given
        User user1 = TestUtil.createUser("john","john@gmail.com","Student");
        //when
        User createdUser = entityManager.persist(user1);
        entityManager.flush();
        //then
        assertThat(entityManager.find(User.class, user1.getEmail())!=null);
        assertThat(createdUser).extracting(User::getName).containsOnly(user1.getName());
    }

    @Test
    public void givenAUser_whenUpdateUser_thenUserUpdatedSuccessfully() {
        //given
        User user1 = TestUtil.createUser("john","john@gmail.com","Student");
        entityManager.persist(user1);
        entityManager.flush();
        //when
        user1.setName("john smith");
        User updatedUser = entityManager.persist(user1);
        entityManager.flush();
        //then
        assertThat(updatedUser).extracting(User::getName).containsOnly(user1.getName());
    }

    @Test
    public void whenDeleteUser_thenUserDeletedSuccessfully() {
        //given
        User user1 = TestUtil.createUser("john","john@gmail.com","Student");
        entityManager.persist(user1);
        entityManager.flush();
        //when
        entityManager.remove(user1);
        entityManager.flush();
        //then
        assertThat(entityManager.find(User.class, user1.getEmail())==null);
    }

    @Test(expected=BadRequestException.class)
    public void whenCreateUserAndEmailIsInvalid_thenThrowBadRequestException() {
        //given
        User user1 = TestUtil.createUser("john","johngmail.com","Student");
        //when
        userRepository.save(user1);
        //then
        //throw BadRequestException
    }

    @Test(expected=BadRequestException.class)
    public void whenCreateUserAndNameIsEmpty_thenThrowBadRequestException() {
        //given
        User user1 = TestUtil.createUser("","johngmail.com","Student");
        //when
        userRepository.save(user1);
        //then
        //throw BadRequestException
    }

    @Test(expected=BadRequestException.class)
    public void whenCreateUserAndRoleIsEmpty_thenThrowBadRequestException() {
        //given
        User user1 = TestUtil.createUser("john","johngmail.com","");
        //when
        userRepository.save(user1);
        //then
        //throw BadRequestException
    }

}
