package com.example.ApplicationFull.Service;

import com.example.ApplicationFull.Entity.User;
import com.example.ApplicationFull.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("UserService")
public class UserServiceImpl implements UserService{

	@Autowired
	@Qualifier("UserRepository")
	private UserRepository userRepository;

	@Override
	public Iterable<User> getAllUsers() {
		return userRepository.findAll();
	}
	
	
	
}
