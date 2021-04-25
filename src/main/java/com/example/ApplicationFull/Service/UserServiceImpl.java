package com.example.ApplicationFull.Service;

import java.util.Optional;

import com.example.ApplicationFull.Entity.User;
import com.example.ApplicationFull.Exception.UsernameOrIdNotFound;
import com.example.ApplicationFull.Repository.UserRepository;
import com.example.ApplicationFull.dto.ChangePasswordForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("UserService")
public class UserServiceImpl implements UserService{

	@Autowired
	@Qualifier("UserRepository")
	private UserRepository userRepository;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	PasswordEncoder passwordEncoder;

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

			String encodePassword = bCryptPasswordEncoder.encode(user.getPassword());
			user.setPassword(encodePassword);

			user = userRepository.save(user);
		}
		return user;
	}

	@Override
	public User getUserById(Long id) throws UsernameOrIdNotFound {
		return userRepository.findById(id).orElseThrow(() -> new UsernameOrIdNotFound("El username o Id del Usuario que busca no existe "));
	}

	@Override
	public User updateUser(User fromUser) throws Exception {
		User toUser = getUserById(fromUser.getId());
		mapUser(fromUser, toUser);
		return userRepository.save(toUser);
	}

	/**
	 * Map everythin but the password.
	 * @param from
	 * @param to
	 */
	protected void mapUser(User from,User to) {
		to.setUsername(from.getUsername());
		to.setFirstName(from.getFirstName());
		to.setLastName(from.getLastName());
		to.setEmail(from.getEmail());
		to.setRoles(from.getRoles());
	}

	@Override
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')") // Para Controlar roles a nivel de Rest o Capa
	public void deleteUser(Long id) throws UsernameOrIdNotFound {
		User user = getUserById(id);
		
		userRepository.delete(user);
	}

	@Override
	public User changePassword(ChangePasswordForm form) throws Exception {
		User user = getUserById(form.getId());
		
		/*encoder.matches("123456", passwd)*/
		if ( !isLoggedUserADMIN() && ! passwordEncoder.matches(form.getCurrentPassword(), user.getPassword())) {
			throw new Exception ("Current Password invalido.");
		}

		if(passwordEncoder.matches(form.getNewPassword(), user.getPassword())) {
			throw new Exception ("Nuevo debe ser diferente al password actual.");
		}
		
		if( !form.getNewPassword().equals(form.getConfirmPassword())) {
			throw new Exception ("Nuevo Password y Confirm Password no coinciden.");
		}
		
		String encodePassword = bCryptPasswordEncoder.encode(form.getNewPassword());
		user.setPassword(encodePassword);
		return userRepository.save(user);
	}

	public boolean isLoggedUserADMIN(){
		return loggedUserHasRole("ROLE_ADMIN");
	}
	
	public boolean loggedUserHasRole(String role) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails loggedUser = null;
		Object roles = null; 
		if (principal instanceof UserDetails) {
			loggedUser = (UserDetails) principal;
		
			roles = loggedUser.getAuthorities().stream()
					.filter(x -> role.equals(x.getAuthority() ))      
					.findFirst().orElse(null); //loggedUser = null;
		}
		return roles != null ?true :false;
	}

	public User getLoggedUser() throws Exception{
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		UserDetails loggedUser = null;

		if (principal instanceof UserDetails) {
			loggedUser = (UserDetails) principal;
		}
		User myUser = 
				userRepository.findByUsername(loggedUser.getUsername()).orElseThrow(() -> new Exception("Problemas obteniento usuario d eSecion."));
		return myUser;
	}
}
