package com.h2rd.refactoring.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.io.IOException;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import com.h2rd.refactoring.Application;
import com.h2rd.refactoring.model.User;
import com.h2rd.refactoring.repository.UserRepository;
import com.h2rd.refactoring.util.JsonUtil;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;


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
    public void givenEmployees_whenGetEmployees_thenStatus200() throws Exception {
        createTestUser("bob");
        createTestUser("alex");

        // @formatter:off
        mvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON))
          .andDo(print())
          .andExpect(status().isOk())
          .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
          .andExpect(jsonPath("$[0].name", is("bob")))
          .andExpect(jsonPath("$[1].name", is("alex")));
        // @formatter:on
    }

    @Test
    public void whenValidInput_thenCreateEmployee() throws IOException, Exception {
        User bob = new User();
        bob.setName("bob");
        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(bob)));

        List<User> found = userRepository.findAll();
        assertThat(found).extracting(User::getName).containsOnly("bob");
    }

    private void createTestUser(String name) {
        User user = new User();
        user.setName(name);
        userRepository.saveAndFlush(user);
    }

}
