package com.coderscampus.SpringSecurityJWTDemo.web;

import com.coderscampus.SpringSecurityJWTDemo.domain.Authority;
import com.coderscampus.SpringSecurityJWTDemo.domain.Role;
import com.coderscampus.SpringSecurityJWTDemo.domain.User;
import com.coderscampus.SpringSecurityJWTDemo.repository.UserRepository;
import com.coderscampus.SpringSecurityJWTDemo.service.UserService;
import com.coderscampus.SpringSecurityJWTDemo.service.UserServiceImpl;

import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
//@RestController
@Controller
@RequestMapping("/admin")
public class AdminController {
    private UserServiceImpl userService;
    private UserRepository userRepo;
    private PasswordEncoder passwordEncoder;
    private Logger logger = LoggerFactory.getLogger(AdminController.class);
    
    public AdminController(UserServiceImpl userService, UserRepository userRepo, PasswordEncoder passwordEncoder) {
		super();
		this.userService = userService;
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
	}

    @PostConstruct // This annotation is used to create an admin user during application startup
    public void init() {
    	createAdminUser();
    }
    
	List<User> allAdmins = new ArrayList<>();

	
	public void createAdminUser() {
		User adminUser = new User();
		adminUser.setFirstName("Admin");
		adminUser.setLastName("User");
		adminUser.setEmail("admin@email.com");
		adminUser.setPassword(passwordEncoder.encode("adminPassword"));
		
		Authority adminAuth = new Authority("ROLE_ADMIN", adminUser);
		adminUser.setAuthorities(Collections.singletonList(adminAuth));
		
		userRepo.save(adminUser);
	}

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers () {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/dashboard")
    public String getDashboard (ModelMap model) {
    	List<User> users = userService.findAll();
    	model.addAttribute("userList", users);
    	return "dashboard";
    }
    
    @PostMapping("/makeAdmin")
    public ResponseEntity<String> elevateToAdmin (@RequestParam Integer userId) {
    	Optional<User> findUser = userService.findUserById(userId);
    	    	
    	userService.elevateUserToAdmin(userId);
    	logger.info("Processing elevation for user: {}", findUser.get().getEmail());
    	logger.info("Role: {}", findUser.get().getAuthorities());
    	return ResponseEntity.ok("User elevated to admin");
    }
}
