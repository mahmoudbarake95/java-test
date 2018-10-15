package com.h2rd.refactoring.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.io.IOException;
import java.util.List;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import com.h2rd.refactoring.Application;
import com.h2rd.refactoring.model.User;
import com.h2rd.refactoring.repository.UserRepository;
import com.h2rd.refactoring.util.TestUtil;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = Application.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class UserControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @After
    public void resetDb() {
        userRepository.deleteAll();
    }

    @Test
    public void givenUsers_whenGetUsers_thenReturnUsersJsonArray() throws Exception {
        //given
        User user1 = TestUtil.createUser("john","john@gmail.com","student");
        User user2 = TestUtil.createUser("peter","peter@gmail.com","Teacher");
        userRepository.saveAndFlush(user1);
        userRepository.saveAndFlush(user2);
        //when
        mvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        //then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
        .andExpect(jsonPath("$[0].name", is("john")))
        .andExpect(jsonPath("$[1].name", is("peter")));
    }

    @Test
    public void givenUser_whenGetUser_thenReturnUserJsonArray() throws Exception {
        //given
        User user1 = TestUtil.createUser("john","john@gmail.com","student");
        userRepository.saveAndFlush(user1);
        //when
        mvc.perform(get("/users/john@gmail.com").contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        //then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is("john")));
    }

    @Test
    public void givenValidInput_whenCreateUser_thenUserCreatedSucessfully() throws IOException, Exception {
        //given
        String jsonContent = "{\"name\": \"Mahmoud\",\"email\": \"mahmoud@gmail.com\",\"roles\": [\"student\"]}";
        //when
        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(jsonContent));
        //then
        List<User> foundUsers = userRepository.findAll();
        assertThat(foundUsers).extracting(User::getName).containsOnly("Mahmoud");
    }

    @Test
    public void givenInValidInput_whenCreateUser_thenUserNotCreated() throws IOException, Exception {
        //given (invalid email)
        String jsonContentInvalidEmail = "{\"name\": \"Mahmoud\",\"email\": \"mahmoudbarake95gmail.com\",\"roles\": [\"student\"]}";
        //when
        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(jsonContentInvalidEmail));
        //then
        List<User> foundUsersFromInvalidEmail = userRepository.findAll();
        assertThat(foundUsersFromInvalidEmail.isEmpty());

        //given (empty name)
        String jsonContentEmptyName = "{\"name\": \"\",\"email\": \"mahmoudbarake@95gmail.com\",\"roles\": [\"student\"]}";
        //when
        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(jsonContentEmptyName));
        //then
        List<User> foundUsersFromEmptyName = userRepository.findAll();
        assertThat(foundUsersFromEmptyName.isEmpty());

        //given (empty role)
        String jsonContentEmptyRole = "{\"name\": \"Mahmoud\",\"email\": \"mahmoudbarake95gmail.com\",\"roles\": [\"\"]}";
        //when
        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(jsonContentEmptyRole));
        //then
        List<User> foundUsersFromEmptyRole = userRepository.findAll();
        assertThat(foundUsersFromEmptyRole.isEmpty());
    }

    @Test
    public void givenValidInput_whenUpdateUser_thenUserUpdatedSucessfully() throws IOException, Exception {
        //given
        User user1 = TestUtil.createUser("john","john@gmail.com","student");
        userRepository.saveAndFlush(user1);
        String jsonContent = "{\"name\": \"john smith\",\"email\": \"john@gmail.com\",\"roles\": [\"teacher\"]}";
        //when (update name and role)
        mvc.perform(put("/users/john@gmail.com").contentType(MediaType.APPLICATION_JSON).content(jsonContent));
        //then
        List<User> foundUsers = userRepository.findAll();
        assertThat(foundUsers).extracting(User::getName).containsOnly("john smith");
    }

    @Test
    public void givenInValidInput_whenUpdateUser_thenUserNotUpdated() throws IOException, Exception {
        //given (different email)
        User user1 = TestUtil.createUser("john","john@gmail.com","student");
        userRepository.saveAndFlush(user1);
        String jsonContentUpdateEmail = "{\"name\": \"john smith\",\"email\": \"johngmail.com\",\"roles\": [\"teacher\"]}";
        //when 
        mvc.perform(put("/users/john@gmail.com").contentType(MediaType.APPLICATION_JSON).content(jsonContentUpdateEmail));
        //then
        List<User> foundUsersUpdateEmail = userRepository.findAll();
        assertThat(foundUsersUpdateEmail).extracting(User::getEmail).containsOnly("john@gmail.com");//cannot update email, email remains john@gmail.com

        //given (empty name)
        String jsonContentEmptyName = "{\"name\": \"\",\"email\": \"john@gmail.com\",\"roles\": [\"teacher\"]}";
        //when
        mvc.perform(put("/users/john@gmail.com").contentType(MediaType.APPLICATION_JSON).content(jsonContentEmptyName));
        //then
        List<User> foundUsersEmptyName = userRepository.findAll();
        assertThat(foundUsersEmptyName).extracting(User::getName).containsOnly("john");//name remains john
    }

    @Test
    public void givenValidEmail_whenDeleteUser_thenUserDeleted() throws IOException, Exception {
        //given
        User user1 = TestUtil.createUser("john","john@gmail.com","student");
        userRepository.saveAndFlush(user1);
        //when
        mvc.perform(delete("/users/john@gmail.com").contentType(MediaType.TEXT_PLAIN))
        .andDo(print())
        //then
        .andExpect(status().isOk());
        List<User> foundUsersAfterDelete = userRepository.findAll();
        assertThat(foundUsersAfterDelete.isEmpty());
    }

    @Test
    public void givenInValidEmail_whenDeleteUser_thenUserNotDeleted() throws IOException, Exception {
        //given
        User user1 = TestUtil.createUser("john","john@gmail.com","student");
        userRepository.saveAndFlush(user1);
        //when (invalid email)
        mvc.perform(delete("/users/johngmail.com").contentType(MediaType.TEXT_PLAIN))
        .andDo(print())
        //then
        .andExpect(status().isNotFound());
        List<User> foundUsersAfterDelete = userRepository.findAll();
        assertThat(foundUsersAfterDelete.size()==1);
    }

}
