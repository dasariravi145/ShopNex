package com.ecommernce.security.jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ecommernce.security.service.UserDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;

	private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		logger.debug("AuthTokenFilter Called for URI:{}", request.getRequestURI());

		try {
			String jwt = parseJwt(request);
			if (jwt != null && jwtUtils.validateJWTToken(jwt)) {
				
				String username=jwtUtils.getUserNameFromJWTToken(jwt);
				UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
				
				UsernamePasswordAuthenticationToken authenticationToken
				=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
				logger.debug("Roles From JWT: {}", userDetails.getAuthorities());
				
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}

		} catch (Exception e) {
			// TODO: handle exception
			logger.error("Cannot Set User Authentication: {}",e);
		}
		filterChain.doFilter(request, response);

	}

	private String parseJwt(HttpServletRequest request) {
		String jwt = jwtUtils.getJwtFromCookies(request);
		logger.debug("AuthTokenFilter.java:{}", jwt);
		return jwt;
	}

}
