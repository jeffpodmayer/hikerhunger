package com.coderscampus.hikerhunger.service;

import com.coderscampus.hikerhunger.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {
    UserDetailsService userDetailsService();
    List<User> findAll();
}