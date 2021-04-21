package com.example.ApplicationFull.Service;

import java.util.Optional;

import javax.validation.Valid;

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

	private boolean checkUsernameAvailable(User user) throws Exception{
        Optional<User> userfound = userRepository.findByUsername(user.getUsername());
		if (userfound.isPresent()) {
			throw new Exception("Username no disponible");
		}
		return true;
    }

	private boolean checkPasswordValid(User user) throws Exception{
		if (!user.getPassword().equals(user.getConfirmPassword())) {
			throw new Exception("Password y Confirm Password no son iguales"); 
		}
		return true;
	}

	@Override
	public User createUser(User user) throws Exception {
		if(checkUsernameAvailable(user) && checkPasswordValid(user)){
			user = userRepository.save(user);
		}
		return user;
	}
	
}
