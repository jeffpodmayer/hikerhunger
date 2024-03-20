
package com.coderscampus.SpringSecurityJWTDemo.security;

import com.coderscampus.SpringSecurityJWTDemo.domain.Authority;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.coderscampus.SpringSecurityJWTDemo.dao.request.SignInRequest;
import com.coderscampus.SpringSecurityJWTDemo.dao.request.SignUpRequest;
import com.coderscampus.SpringSecurityJWTDemo.dao.response.JwtAuthenticationResponse;
import com.coderscampus.SpringSecurityJWTDemo.domain.Role;
import com.coderscampus.SpringSecurityJWTDemo.domain.User;
import com.coderscampus.SpringSecurityJWTDemo.repository.UserRepository;
import com.coderscampus.SpringSecurityJWTDemo.service.RefreshTokenService;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
	
	private Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
	
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    
    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
            JwtService jwtService, AuthenticationManager authenticationManager, RefreshTokenService refreshTokenService) {
        super();
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
    }

	@Override
    public JwtAuthenticationResponse signup(SignUpRequest request) {
        var user = new User()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .authority(Role.USER.name()).build();
        request.authorityOpt().ifPresent(auth -> user.getAuthorities().add(new Authority(auth, user)));
        userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getId());
        
        String encodedPassword = passwordEncoder.encode(request.password());
        logger.info("Raw Password during registation: {}, Encoded Password during registation: {}", request.password(), encodedPassword);
        
        return new JwtAuthenticationResponse(jwt, refreshToken.getToken());
    }

    @Override
    public JwtAuthenticationResponse signin(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        var user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        var jwt = jwtService.generateToken(user);
        var refreshTokenOpt = refreshTokenService.findByToken(jwt);
        
        logger.info("Raw password during login: {}", "Encoded password during login: {}", request.password(), user.getPassword());
        
        if (refreshTokenOpt.isPresent()) {
            return new JwtAuthenticationResponse(jwt, refreshTokenOpt.get().getToken()); 
        } else {
            return new JwtAuthenticationResponse(jwt, refreshTokenService.createRefreshToken(user.getId()).getToken());
        }
    }
}

