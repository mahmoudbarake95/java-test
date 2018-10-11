package com.h2rd.refactoring.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.h2rd.refactoring.model.User;
import com.h2rd.refactoring.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUser(String email) {
        return userRepository.findById(email);
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User userToUpdate, User userFromRequestBody) {
//        user.setId(id);
//        userToUpdate.setEmail(userFromRequestBody.getEmail());
        userToUpdate.setName(userFromRequestBody.getName());
        userToUpdate = userRepository.save(userToUpdate);
        return userToUpdate;
    }

    @Override
    public void deleteUser(String email) {
        userRepository.deleteById(email);
    }
    
}
