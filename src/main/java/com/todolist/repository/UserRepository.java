package com.todolist.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.todolist.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    
    Optional<User> findByUser(String user);
    
    Optional<User> findByDni(String dni);

    Optional<UserDetails> findFirstByEmail(String email);
    
    Optional<UserDetails> findFirstByUser(String user);

    boolean existsByUser(String user);
    
}
