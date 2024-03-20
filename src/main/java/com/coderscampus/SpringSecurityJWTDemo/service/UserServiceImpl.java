package com.coderscampus.SpringSecurityJWTDemo.service;

import com.coderscampus.SpringSecurityJWTDemo.domain.Authority;
import com.coderscampus.SpringSecurityJWTDemo.domain.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.coderscampus.SpringSecurityJWTDemo.repository.UserRepository;
import com.coderscampus.SpringSecurityJWTDemo.web.RegistrationController;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
	
    private final UserRepository userRepository;
    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
            	User user = userRepository.findByEmail(username)
            			.orElseThrow(() -> new UsernameNotFoundException("User not found" + username));
            	
            	List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
            			.map(auth -> new SimpleGrantedAuthority(auth.getAuthority()))
            			.collect(Collectors.toList());
            	
            	return user;
            }
        };
    }
    

    @Secured({"ROLE_ADMIN"})
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    @Secured("ROLE_ADMIN")
    @Transactional // This annotation ensures that changes are committed to the database
    public void elevateUserToAdmin(Integer userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Check if the user doesn't already have the admin role
            if (user.getAuthorities().stream().noneMatch(auth -> "ROLE_ADMIN".equals(auth.getAuthority()))) {
                // Add the admin role to the user
                Authority adminAuthority = new Authority("ROLE_ADMIN");
                adminAuthority.setUser(user);
                user.getAuthorities().add(adminAuthority);

                logger.info("Setting Auth for user: " + user.getId() + user.getEmail());
                logger.info("Setting Authorities: " + user.getAuthorities());
                
                // Save the updated user
                userRepository.save(user);
            }
        } else {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
    }
    
    public User registerUser(User user) {
		if (userRepository.findByEmail(user.getEmail()).isPresent()) {

			return null;
		}
		return userRepository.save(user);
	}
    
    public Optional<User> findUserByEmail(String email) {
    	return userRepository.findByEmail(email);
    }
    
    public Optional<User> findUserById(Integer userId) {
    	return userRepository.findById(userId);
    }
    
    public User updateUser(User user) {
    	return userRepository.save(user);
    }
}