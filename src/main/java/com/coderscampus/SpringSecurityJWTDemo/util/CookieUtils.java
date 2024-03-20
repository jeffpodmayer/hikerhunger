package com.coderscampus.SpringSecurityJWTDemo.util;

import jakarta.servlet.http.Cookie;

public class CookieUtils {

	public static Cookie createAccessTokenCookie(String value) {
		Cookie accessTokenCookie = new Cookie("accessToken", value);
    	
		return accessTokenCookie;
	}
	
	public static Cookie createRefreshTokenCookie(String value) {
		Cookie refreshTokenCookie = new Cookie("refreshToken", value);
		
		return refreshTokenCookie;
	}		
}
