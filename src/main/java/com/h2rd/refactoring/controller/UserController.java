package com.h2rd.refactoring.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.h2rd.refactoring.constants.ErrorMessages;
import com.h2rd.refactoring.constants.SuccessMessages;
import com.h2rd.refactoring.exception.ResourceNotFoundException;
import com.h2rd.refactoring.model.User;
import com.h2rd.refactoring.repository.UserRepository;
import com.h2rd.refactoring.service.UserServiceImpl;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

//Spring boot is multi-threaded and therefore handles multiple requests concurrently
@RestController
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{email}")
    public ResponseEntity<User> getUser(@PathVariable String email) {
        Optional<User> user = userService.getUser(email);
        return ResponseEntity.ok(user.get());
    }

    @PostMapping(path = "/users", consumes="application/json")
    public ResponseEntity<Object> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{email}")
                .buildAndExpand(createdUser.getEmail()).toUri();

        return ResponseEntity.created(location).body(SuccessMessages.USER_CREATED_SUCCESSFULLY);
    }

    @PutMapping(path = "/users/{email}", consumes="application/json")
    public ResponseEntity<String> updateUser(@RequestBody User newUser, @PathVariable String email) {
        userService.updateUser(newUser, email);

        return ResponseEntity.ok(SuccessMessages.USER_UPDATED_SUCCESSFULLY);
    }

    @DeleteMapping("/users/{email}")
    public ResponseEntity<String> deleteUser(@PathVariable String email) {
        userService.deleteUser(email);
        return ResponseEntity.ok(SuccessMessages.USER_UPDATED_SUCCESSFULLY);
    }

}
