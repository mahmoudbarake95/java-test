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
    
//    @Autowired
//    private UserRepository userRepository;
    
    @Autowired
    private UserServiceImpl userService;

//    @GetMapping("/users")
    @GetMapping(path = "/users")
    public List<User> getAllUsers() {
//        return userRepository.findAll();
        return userService.getAllUsers();
    }

    @GetMapping("/users/{email}")
    public ResponseEntity<Object> getUser(@PathVariable String email) {
//        Optional<User> user = userRepository.findById(id);
        Optional<User> user = userService.getUser(email);

        if (!user.isPresent()){
//            return ResponseEntity.notFound().build();
            throw new ResourceNotFoundException("This user cannot be found");
        }
        return ResponseEntity.ok(user.get());
//        return user.get();
    }

    @PostMapping(path = "/users", consumes="application/json")
    public ResponseEntity<Object> createUser(@RequestBody User user) {
//        User saveduser = userRepository.save(user);
        if(userService.getUser(user.getEmail()).isPresent()){
            return ResponseEntity.badRequest().body(ErrorMessages.USER_ALREADY_EXISTS);
        }
        User saveduser = userService.createUser(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{email}")
                .buildAndExpand(saveduser.getEmail()).toUri();

        return ResponseEntity.created(location).body(SuccessMessages.USER_CREATED_SUCCESSFULLY);
    }

    @PutMapping(path = "/users/{email}", consumes="application/json")
    public ResponseEntity<Object> updateUser(@RequestBody User userFromRequestBody, @PathVariable String email) {

//        Optional<User> userOptional = userRepository.findById(id);
        Optional<User> userToUpdate = userService.getUser(email);

        if (!userToUpdate.isPresent())
            return ResponseEntity.notFound().build();
        
        if(!userFromRequestBody.getEmail().equals(email)){
            return ResponseEntity.badRequest().body(ErrorMessages.CANNOT_CHANGE_EMAIL_OF_USER);
        }
        
        userService.updateUser(userToUpdate.get(), userFromRequestBody);
//        user.setId(id);
//
//        userRepository.save(user);

//        return ResponseEntity.noContent().build();
        return ResponseEntity.ok(SuccessMessages.USER_UPDATED_SUCCESSFULLY);
    }
    
    @DeleteMapping("/users/{email}")
    public void deleteUser(@PathVariable String email) {
        userService.deleteUser(email);
//        userRepository.deleteById(id);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /*@Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public User retrieveUser(@PathVariable long id) {
        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent())
            try {
                throw new Exception("id-" + id);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        return user.get();
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable long id) {
        userRepository.deleteById(id);
    }

    @PostMapping("/users")
    public ResponseEntity<Object> createUser(@RequestBody User user) {
        User saveduser = userRepository.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(saveduser.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<Object> updateUser(@RequestBody User user, @PathVariable long id) {

        Optional<User> userOptional = userRepository.findById(id);

        if (!userOptional.isPresent())
            return ResponseEntity.notFound().build();

        user.setId(id);

        userRepository.save(user);

        return ResponseEntity.noContent().build();
    }*/
}
