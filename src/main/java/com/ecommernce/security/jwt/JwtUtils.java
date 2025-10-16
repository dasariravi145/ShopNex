package com.ecommernce.security.jwt;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.ecommernce.security.service.UserDetailsImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtils {

      
	
	        private static final Logger logger=LoggerFactory.getLogger(JwtUtils.class);
	        
	        @Value("${spring.app.jwtExpirationMs}")
	        private int jwtExpirationMs;
	        @Value("${spring.app.jwtSecret}")
	        private String jwtSecret;
	        @Value("${spring.ecom.app.jwtCookieName}")
	        private String jwtCookie;

      
	        public String getJwtFromCookies(HttpServletRequest request) {
	        	
	        	   Cookie cookie=WebUtils.getCookie(request, jwtCookie);
	        	   if(cookie!=null) {
	        		   return cookie.getValue();
	        	   }else {
	        		   return null;
	        	   }
	        }
	        
	        public ResponseCookie generateJwtCookie(UserDetailsImpl userDetailsImpl) {
	        	
	        	  String jwt=generateTokenFromUserName(userDetailsImpl.getUsername());
	        	  ResponseCookie responseCookie=ResponseCookie.from(jwtCookie,jwt)
	        			  .path("/api")
	        			  .maxAge(24*60*60)
	        			  .httpOnly(false)
	        			  .build();
	        	  return responseCookie;
	        	
	        }
	        
	        public ResponseCookie getClearJwtCookie() {
	        	   ResponseCookie responseCookie=ResponseCookie.from(jwtCookie,null)
	        			   .path("/api")
	        			   .build();
	        	   return responseCookie;
	        }
	        
	        public String generateTokenFromUserName(String username) {
	        	
	        	  return Jwts.builder()
	        			  .subject(username)
	        			  .issuedAt(new Date())
	        			  .expiration(new Date(new Date()
	        			   .getTime()+jwtExpirationMs))
	        			  .signWith(key())
	        			  .compact();
	        }
	        public String getUserNameFromJWTToken(String token) {
	        	
	        	 return Jwts.parser().verifyWith((SecretKey) key())
	        			 .build()
	        			 .parseSignedClaims(token)
	        			 .getPayload()
	        			 .getSubject();
	        }
	        
	        public Key key() {
	        	return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtCookie));
	        }
	        
	        public boolean validateJWTToken(String authToken) {
	        	
	        	  try {
	        		  System.out.println("Validate");
	        		  Jwts.parser().verifyWith((SecretKey) key())
	        		  .build()
	        		  .parseSignedClaims(authToken);
	        		  return true;
	        	  }catch(MalformedJwtException ex) {
	        		     logger.error("Invalid JWT token:{}", ex.getMessage());
	        	  }catch(ExpiredJwtException ex) {
	        		    logger.error("JWT  Token is Expired: {}", ex.getMessage());
	        	  }
	        	  catch(UnsupportedJwtException ex) {
	        		  logger.error("JWT  Token is Unauthorized: {}", ex.getMessage());
	        	  }
	        	  catch(IllegalArgumentException ex) {
	        		  logger.error("JWT  Claim screen  is Empty: {}", ex.getMessage());
	        	  }
	        	  
	        	  return false;
	        }

}
