package com.example.ApplicationFull.Service;

import javax.validation.Valid;

import com.example.ApplicationFull.Entity.User;

public interface UserService {

    public Iterable<User> getAllUsers();

    public User createUser(User user) throws Exception;
}
