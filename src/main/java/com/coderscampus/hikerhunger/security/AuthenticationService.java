package com.coderscampus.hikerhunger.security;

import com.coderscampus.hikerhunger.dao.request.SignInRequest;
import com.coderscampus.hikerhunger.dao.request.SignUpRequest;
import com.coderscampus.hikerhunger.dao.response.JwtAuthenticationResponse;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);
    JwtAuthenticationResponse signin(SignInRequest request);
}