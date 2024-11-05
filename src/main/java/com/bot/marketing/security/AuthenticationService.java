package com.bot.marketing.security;


import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;



import jakarta.servlet.http.HttpServletRequest;


//This class extract apikey and access token from request header and validate those
public class AuthenticationService{

	
    private static final String AUTH_TOKEN_HEADER_NAME = "X-API-KEY";
    private static final String AUTH_TOKEN = System.getenv("AUTH_TOKEN");
    private static final String SESSION_TOKEN_HEADER = "Authorization";
    
    

    public static Authentication getAuthentication(HttpServletRequest request) {
    	
    	
        String apiKey = request.getHeader(AUTH_TOKEN_HEADER_NAME);
        if (apiKey == null || !apiKey.equals(AUTH_TOKEN)) {
            throw new BadCredentialsException("Invalid API Key");
        }
        String accessToken = request.getHeader(SESSION_TOKEN_HEADER);
        if (accessToken == null || !JWTokenService.verifyToken(accessToken))  {
        	throw new BadCredentialsException("Invalid or empty JWT-token");
        }
        return new ApiKeyAuth(apiKey, AuthorityUtils.NO_AUTHORITIES);
    }
}

