package com.bot.marketing.security;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;


@Service
public class JWTokenService {

	
	// Verify the JWT-token send from the client and returns boolean
	public static boolean verifyToken(String token) {
		
		String tokenizedSecret = System.getenv("TOKEN_SECRET");
		
		
		try {
		    Algorithm algorithm = Algorithm.HMAC256(tokenizedSecret);
		    JWTVerifier verifier = JWT.require(algorithm)
		        .build();
		        
		    verifier.verify(token);
		    return true;
		} catch (JWTVerificationException exception){
			return false;
		}
	}
	
	
	//This function creates new JWT-token and returns the token in String
	public static String createToken() {
		
		String tokenizedSecret = System.getenv("TOKEN_SECRET");
		Algorithm algorithm = Algorithm.HMAC256(tokenizedSecret);
		
		String newToken = JWT.create()
				.withIssuedAt(new Date())
				.withExpiresAt(new Date(System.currentTimeMillis() + 60 * 180 * 1000))
				.sign(algorithm);
		
		return newToken;
	}
	

}
