package com.example.ApplicationFull.Service;


import java.util.HashSet;
import java.util.Set;

import com.example.ApplicationFull.Entity.Role;
import com.example.ApplicationFull.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    @Qualifier("UserRepository")
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        
        com.example.ApplicationFull.Entity.User appUser = 
            userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Login USername Invalido"));

        Set grantList = new HashSet();
        for (Role role : appUser.getRoles()) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getDescription());
            grantList.add(grantedAuthority);
        }

        UserDetails user = (UserDetails) new User(username, appUser.getPassword(), grantList);

        return user;
    }
    
}
