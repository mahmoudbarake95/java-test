package com.h2rd.refactoring.resource;

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
import com.h2rd.refactoring.model.User;
import com.h2rd.refactoring.repository.UserRepository;
import com.h2rd.refactoring.service.UserServiceImpl;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class UserResource {

//    @Autowired
//    private UserRepository userRepository;
    
    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/users")
    public List<User> getAllUsers() {
//        return userRepository.findAll();
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable long id) {
//        Optional<User> user = userRepository.findById(id);
        Optional<User> user = userService.getUser(id);

        if (!user.isPresent())
            try {
                throw new Exception("id-" + id);
            } catch (Exception e) {
                e.printStackTrace();
            }

        return user.get();
    }

    @PostMapping("/users")
    public ResponseEntity<Object> createUser(@RequestBody User user) {
//        User saveduser = userRepository.save(user);
        User saveduser = userService.createUser(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(saveduser.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<Object> updateUser(@RequestBody User user, @PathVariable long id) {

//        Optional<User> userOptional = userRepository.findById(id);
        Optional<User> userOptional = userService.getUser(id);

        if (!userOptional.isPresent())
            return ResponseEntity.notFound().build();
        
        userService.updateUser(user, id);
//        user.setId(id);
//
//        userRepository.save(user);

        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
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
