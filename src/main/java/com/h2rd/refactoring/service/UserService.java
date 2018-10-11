package com.h2rd.refactoring.service;

import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import com.h2rd.refactoring.model.User;

public interface UserService {
    
    public List<User> getAllUsers();
    public Optional<User> getUser(long id);
    public User createUser(User user);
    public User updateUser(User user, long id);
    public void deleteUser(long id);
    
}
