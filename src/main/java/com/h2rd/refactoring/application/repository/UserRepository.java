package com.h2rd.refactoring.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.h2rd.refactoring.application.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

}
