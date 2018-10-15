package com.h2rd.refactoring.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.h2rd.refactoring.exception.BadRequestException;
import com.h2rd.refactoring.model.Role;
import com.h2rd.refactoring.model.User;
import com.h2rd.refactoring.repository.UserRepository;


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
    public void givenUsers_whenGetUsers_thenStatus200() throws Exception {
        User user1 = createUser("john","john@gmail.com","student");
        userRepository.saveAndFlush(user1);

        //get a particular user
        mvc.perform(get("/users/john@gmail.com").contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.name", is("john")));

        User user2 = createUser("peter","peter@gmail.com","Teacher");
        userRepository.saveAndFlush(user2);

        //get all users
        mvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
        .andExpect(jsonPath("$[0].name", is("john")))
        .andExpect(jsonPath("$[1].name", is("peter")));
    }

    @Test
    public void whenValidInput_thenCreateUser() throws IOException, Exception {
        String jsonContent = "{\"name\": \"Mahmoud\",\"email\": \"mahmoud@gmail.com\",\"roles\": [\"student\"]}";
        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(jsonContent));
        List<User> foundUsers = userRepository.findAll();
        assertThat(foundUsers).extracting(User::getName).containsOnly("Mahmoud");
    }

    @Test
    public void whenInValidInput_thenDoNotCreateUser() throws IOException, Exception {
        //invalid email
        String jsonContentInvalidEmail = "{\"name\": \"Mahmoud\",\"email\": \"mahmoudbarake95gmail.com\",\"roles\": [\"student\"]}";
        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(jsonContentInvalidEmail));
        List<User> foundUsersFromInvalidEmail = userRepository.findAll();
        assertThat(foundUsersFromInvalidEmail.isEmpty());

        //empty name
        String jsonContentEmptyName = "{\"name\": \"\",\"email\": \"mahmoudbarake@95gmail.com\",\"roles\": [\"student\"]}";
        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(jsonContentEmptyName));
        List<User> foundUsersFromEmptyName = userRepository.findAll();
        assertThat(foundUsersFromEmptyName.isEmpty());

        //empty role
        String jsonContentEmptyRole = "{\"name\": \"Mahmoud\",\"email\": \"mahmoudbarake95gmail.com\",\"roles\": [\"\"]}";
        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(jsonContentEmptyRole));
        List<User> foundUsersFromEmptyRole = userRepository.findAll();
        assertThat(foundUsersFromEmptyRole.isEmpty());
    }

    @Test
    public void whenValidInput_thenUpdateUser() throws IOException, Exception {
        User user1 = createUser("john","john@gmail.com","student");
        userRepository.saveAndFlush(user1);

        //update name and role
        String jsonContent = "{\"name\": \"john smith\",\"email\": \"john@gmail.com\",\"roles\": [\"teacher\"]}";
        mvc.perform(put("/users/john@gmail.com").contentType(MediaType.APPLICATION_JSON).content(jsonContent));
        List<User> foundUsers = userRepository.findAll();
        assertThat(foundUsers).extracting(User::getName).containsOnly("john smith");
    }

    @Test
    public void whenInValidInput_thenDoNotUpdateUser() throws IOException, Exception {
        User user1 = createUser("john","john@gmail.com","student");
        userRepository.saveAndFlush(user1);

        //cannot update email
        String jsonContentUpdateEmail = "{\"name\": \"john smith\",\"email\": \"johngmail.com\",\"roles\": [\"teacher\"]}";
        mvc.perform(put("/users/john@gmail.com").contentType(MediaType.APPLICATION_JSON).content(jsonContentUpdateEmail));
        List<User> foundUsersUpdateEmail = userRepository.findAll();
        assertThat(foundUsersUpdateEmail).extracting(User::getEmail).containsOnly("john@gmail.com");//email remains john@gmail.com

        //empty name
        String jsonContentEmptyName = "{\"name\": \"\",\"email\": \"john@gmail.com\",\"roles\": [\"teacher\"]}";
        mvc.perform(put("/users/john@gmail.com").contentType(MediaType.APPLICATION_JSON).content(jsonContentEmptyName));
        List<User> foundUsersEmptyName = userRepository.findAll();
        assertThat(foundUsersEmptyName).extracting(User::getName).containsOnly("john");//name remains john

        //empty role
//        String jsonContentEmptyRole = "{\"name\": \"john smith\",\"email\": \"john@gmail.com\",\"roles\": [\"\"]}";
//        mvc.perform(put("/users/john@gmail.com").contentType(MediaType.APPLICATION_JSON).content(jsonContentEmptyRole));
//        List<User> foundUsersEmptyRole = userRepository.findAll();
//        assertThat(foundUsersEmptyRole).extracting(User::getRoles).containsOnly("teacher");//name remains john


    }

    @Test
    public void whenValidIdentifier_thenDeleteUser() throws IOException, Exception {
        User user1 = createUser("john","john@gmail.com","student");
        userRepository.saveAndFlush(user1);

        mvc.perform(get("/users/john@gmail.com").contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        List<User> foundUsersAfterDelete = userRepository.findAll();
        assertThat(foundUsersAfterDelete.isEmpty());
    }

    @Test
    public void whenInValidIdentifier_thenDoNotDeleteUser() throws IOException, Exception {
        User user1 = createUser("john","john@gmail.com","student");
        userRepository.saveAndFlush(user1);

        //invalid email
        mvc.perform(get("/users/johngmail.com").contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isNotFound());

        List<User> foundUsersAfterDelete = userRepository.findAll();
        assertThat(foundUsersAfterDelete.size()==1);
    }


    private User createUser(String name, String email, String roleName){
        return new User(name,email, new ArrayList<Role>(
                Arrays.asList(new Role(roleName))));
    }

}
