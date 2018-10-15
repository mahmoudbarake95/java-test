package com.h2rd.refactoring.listener;

import java.util.List;
import java.util.Set;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.springframework.stereotype.Component;
import com.h2rd.refactoring.constants.ErrorMessages;
import com.h2rd.refactoring.exception.BadRequestException;
import com.h2rd.refactoring.model.Role;
import com.h2rd.refactoring.model.User;

@Component
public class UserListener {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    @PrePersist
    @PreUpdate
    public void onPreUpdate(User user) {
        if(hasViolatedConstraints(user)){
            throw new BadRequestException(ErrorMessages.REQUEST_BODY_MALFORMED);
        }
    }

    public boolean hasEmptyRoleName(User user){
        List<Role> userRoles = user.getRoles();
        for (int i=0; i < userRoles.size(); i++) {
            if(userRoles.get(i).getName().trim().equals("")){
                return true;
            }
        }
        return false;
    }

    public boolean hasViolatedConstraints(User user){
        Set<ConstraintViolation<User>> userConstraintViolations = validator.validate(user);
        if(userConstraintViolations.size() > 0 || hasEmptyRoleName(user)){    //1 or more constraints were violated
            return true;
        }
        return false;
    }
}
