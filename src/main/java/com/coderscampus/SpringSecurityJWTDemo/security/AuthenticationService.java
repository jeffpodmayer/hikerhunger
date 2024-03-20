package com.coderscampus.SpringSecurityJWTDemo.security;

import com.coderscampus.SpringSecurityJWTDemo.dao.request.SignInRequest;
import com.coderscampus.SpringSecurityJWTDemo.dao.request.SignUpRequest;
import com.coderscampus.SpringSecurityJWTDemo.dao.response.JwtAuthenticationResponse;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);
    JwtAuthenticationResponse signin(SignInRequest request);
}