package com.coderscampus.SpringSecurityJWTDemo.security;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.coderscampus.SpringSecurityJWTDemo.dao.request.RefreshTokenRequest;
import com.coderscampus.SpringSecurityJWTDemo.domain.RefreshToken;
import com.coderscampus.SpringSecurityJWTDemo.domain.User;
import com.coderscampus.SpringSecurityJWTDemo.repository.RefreshTokenRepository;
import com.coderscampus.SpringSecurityJWTDemo.service.RefreshTokenService;
import com.coderscampus.SpringSecurityJWTDemo.service.UserService;
import com.coderscampus.SpringSecurityJWTDemo.service.UserServiceImpl;
import com.coderscampus.SpringSecurityJWTDemo.util.CookieUtils;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtServiceImpl jwtService;
	private final UserServiceImpl userService;
	private final RefreshTokenService refreshTokenService;
	private Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	public JwtAuthenticationFilter(JwtServiceImpl jwtService, UserServiceImpl userService,
			RefreshTokenService refreshTokenService) {
		super();
		this.jwtService = jwtService;
		this.userService = userService;
		this.refreshTokenService = refreshTokenService;
	}

	@Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        
        Cookie accessTokenCookie = null;
        Cookie refreshTokenCookie = null;
        
        if (request.getCookies() != null) {
        	for (Cookie cookie : request.getCookies()) {
        		if (cookie.getName().equals("accessToken")) {
        			accessTokenCookie = cookie;
        		} else if (cookie.getName().equals("refreshToken")) {
        			refreshTokenCookie = cookie;
        		}
        	}
        }
        
        if  (accessTokenCookie != null ) {
        
        	int loginAttempt = 0;

        	while (loginAttempt <= 5) {
        		String token = accessTokenCookie.getValue();

        		try {
        			String subject = jwtService.extractUserName(token);
        			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        			if (StringUtils.hasText(subject) && authentication == null) {
        				UserDetails userDetails = userService.userDetailsService().loadUserByUsername(subject);

        				if (jwtService.isTokenValid(token, userDetails)) {
        					SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken (userDetails,
        							userDetails.getPassword(),
        							userDetails.getAuthorities());
        					securityContext.setAuthentication(authToken);
        					SecurityContextHolder.setContext(securityContext);

        					// if successful login occurs:
        					break;
        				}
        			}
        		} catch (ExpiredJwtException e) {
        			try {
						token = refreshTokenService.createNewAccessToken(new RefreshTokenRequest(refreshTokenCookie.getValue()));
						accessTokenCookie = CookieUtils.createAccessTokenCookie(token);
						
						response.addCookie(accessTokenCookie);
						e.printStackTrace();
					} catch (Exception e1) {
						
						e1.printStackTrace();
					}
        		}
        		loginAttempt++;
        	}
        }
        logger.debug("Request URI: {}", request.getRequestURI());
        logger.debug("Auth Header: {}", authHeader);
        logger.debug("Access Token Cookie: {}", accessTokenCookie);
        logger.debug("Refresh Token Cookie: {}", refreshTokenCookie);
        
        filterChain.doFilter(request, response);

	}
}