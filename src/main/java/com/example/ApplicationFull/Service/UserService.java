package com.example.ApplicationFull.Service;


import com.example.ApplicationFull.Entity.User;
import com.example.ApplicationFull.Exception.UsernameOrIdNotFound;
import com.example.ApplicationFull.dto.ChangePasswordForm;

public interface UserService {

    public Iterable<User> getAllUsers();

    public User createUser(User user) throws Exception;

    public User getUserById(Long id) throws Exception;

    public User updateUser(User fromUser) throws Exception;

    public void deleteUser(Long id) throws UsernameOrIdNotFound;

    public User changePassword(ChangePasswordForm form) throws Exception;
}
