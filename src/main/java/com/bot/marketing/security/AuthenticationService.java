package com.bot.marketing.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;


import jakarta.servlet.http.HttpServletRequest;


//This class extract api key from request header and validate it
public class AuthenticationService{

    private static final String AUTH_TOKEN_HEADER_NAME = "X-API-KEY";
    private static final String AUTH_TOKEN = System.getenv("AUTH_TOKEN");
    


    public static Authentication getAuthentication(HttpServletRequest request) {
        String apiKey = request.getHeader(AUTH_TOKEN_HEADER_NAME);
        if (apiKey == null || !apiKey.equals(AUTH_TOKEN)) {
            throw new BadCredentialsException("Invalid API Key");
        }

        return new ApiKeyAuth(apiKey, AuthorityUtils.NO_AUTHORITIES);
    }
}

