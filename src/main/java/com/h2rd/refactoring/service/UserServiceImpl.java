package com.h2rd.refactoring.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.h2rd.refactoring.constants.ErrorMessages;
import com.h2rd.refactoring.exception.BadRequestException;
import com.h2rd.refactoring.exception.ResourceNotFoundException;
import com.h2rd.refactoring.model.User;
import com.h2rd.refactoring.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUser(String email) {
        validateUserExistence(email);
        return userRepository.findById(email);
    }

    public User createUser(User user) {
        if(userRepository.existsById(user.getEmail())){
            throw new BadRequestException(ErrorMessages.USER_ALREADY_EXISTS);
        }
        return userRepository.save(user);
    }

    public User updateUser(User newUser, String email) {
        validateUserExistence(email);
        if(!newUser.getEmail().equals(email)){  //cannot check this condition in entityListener because @PreUpdate method takes only 1 argument 
            throw new BadRequestException(ErrorMessages.CANNOT_CHANGE_EMAIL_OF_USER);
        }
        //else user exists, perform update 
        return userRepository.save(newUser);
    }

    public void deleteUser(String email) {
        validateUserExistence(email);
        userRepository.deleteById(email);
    }
    
    private void validateUserExistence(String email){
        if(!userRepository.existsById(email)){
            throw new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND);
        }
    }
    
}
