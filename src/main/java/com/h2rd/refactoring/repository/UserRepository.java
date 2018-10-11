package com.h2rd.refactoring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.h2rd.refactoring.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{

}
