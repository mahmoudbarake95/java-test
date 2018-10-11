package com.h2rd.refactoring.unit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
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

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mvc;
    
    @MockBean
    private UserServiceImpl userService;    //mock the user service to test the resource in isolation
    
    @Test
    public void givenUsers_whenGetUsers_thenReturnJsonArray()
      throws Exception {
         
        User alex = new User();
        alex.setName("alex");
     
        List<User> allUsers = Arrays.asList(alex);
     
        given(userService.getAllUsers()).willReturn(allUsers);
     
        mvc.perform(get("/users")
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", hasSize(1)))
          .andExpect(jsonPath("$[0].name", is(alex.getName())));
        
        verify(userService, VerificationModeFactory.times(1)).getAllUsers();
        reset(userService);
    }

}
