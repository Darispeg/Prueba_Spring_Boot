package com.example.ApplicationFull.Repository;


import java.util.Optional;

import com.example.ApplicationFull.Entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("UserRepository")
public interface UserRepository extends JpaRepository<User, Long> {
    
    public Optional<User> findByUsername(String username);
}