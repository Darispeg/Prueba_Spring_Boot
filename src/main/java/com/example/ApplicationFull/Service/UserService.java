package com.example.ApplicationFull.Service;


import com.example.ApplicationFull.Entity.User;

public interface UserService {

    public Iterable<User> getAllUsers();

    public User createUser(User user) throws Exception;

    public User getUserById(Long id) throws Exception;

    public User updateUser(User fromUser) throws Exception;

    public void deleteUser(Long id) throws Exception;
}
