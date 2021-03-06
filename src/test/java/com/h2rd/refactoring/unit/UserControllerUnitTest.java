package com.h2rd.refactoring.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import com.h2rd.refactoring.controller.UserController;
import com.h2rd.refactoring.model.User;
import com.h2rd.refactoring.service.UserServiceImpl;
import com.h2rd.refactoring.util.TestUtil;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerUnitTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserServiceImpl userService;    //mock the user service to test the resource in isolation

    @After
    public void resetDb() {
        reset(userService);
    }

    @Test
    public void givenUsers_whenGetUsers_thenReturnUsersJsonArray() throws Exception {
        //given
        User user1 = TestUtil.createUser("john","john@gmail.com","student"); 
        List<User> allUsers = Arrays.asList(user1);
        given(userService.getAllUsers()).willReturn(allUsers);
        //when
        mvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].name", is(user1.getName())));
        //then
        verify(userService, VerificationModeFactory.times(1)).getAllUsers();
    }

    @Test
    public void givenUser_whenGetUser_thenReturnUserJsonArray() throws Exception {
        //given
        User user1 = TestUtil.createUser("john","john@gmail.com","student");
        given(userService.getUser(Mockito.anyString())).willReturn(Optional.of(user1));
        //when
        mvc.perform(get("/users/john@gmail.com")
                .contentType(MediaType.APPLICATION_JSON))
        //then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is(user1.getName())));
        verify(userService, VerificationModeFactory.times(1)).getUser("john@gmail.com"); 
    }

    @Test
    public void givenValidInput_whenCreateUser_thenUserCreated() throws Exception {
        //given
        User user1 = TestUtil.createUser("mahmoud","mahmoud@gmail.com","student");
        given(userService.createUser(Mockito.anyObject())).willReturn(user1);
        //when
        String jsonContent = "{\"name\": \"Mahmoud\",\"email\": \"mahmoud@gmail.com\",\"roles\": [\"student\"]}";
        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
        //then
        .andExpect(status().isCreated());
        verify(userService, VerificationModeFactory.times(1)).createUser(Mockito.anyObject());
    }

    @Test
    public void givenValidInput_whenUpdateUser_thenUserUpdated() throws Exception {
        //given
        User user1 = TestUtil.createUser("mahmoud","mahmoud@gmail.com","student");
        given(userService.createUser(Mockito.anyObject())).willReturn(user1);
        userService.createUser(user1);

        User user2 = TestUtil.createUser("mahmoud barake","mahmoud@gmail.com","student");//simulate the new user after the update
        given(userService.updateUser(Mockito.anyObject(),Mockito.anyString())).willReturn(user2);

        //when (update name)
        String jsonContent = "{\"name\": \"mahmoud barake\",\"email\": \"mahmoud@gmail.com\",\"roles\": [\"student\"]}";
        mvc.perform(put("/users/mahmoud@gmail.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
        //then
        .andExpect(status().isOk());
        verify(userService, VerificationModeFactory.times(1)).updateUser(Mockito.anyObject(),Mockito.anyString());
        assertThat(user2.getName().equals("mahmoud barake"));
    }

    //Delete test skipped
    //testing invalid inputs was skipped

}
