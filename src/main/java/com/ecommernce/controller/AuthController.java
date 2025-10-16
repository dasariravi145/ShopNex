package com.ecommernce.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommernce.model.APIRole;
import com.ecommernce.model.Roles;
import com.ecommernce.model.User;
import com.ecommernce.repository.RolesRepository;
import com.ecommernce.repository.UserRepository;
import com.ecommernce.security.jwt.JwtUtils;
import com.ecommernce.security.request.LoginRequest;
import com.ecommernce.security.request.SignupRequest;
import com.ecommernce.security.response.MessageResponse;
import com.ecommernce.security.response.UserInfoResponse;
import com.ecommernce.security.service.UserDetailsImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private RolesRepository rolesRepository;

	@PostMapping("/singin")
	public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

		Authentication authentication;
		try {
			authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		} catch (AuthenticationException ex) {

			Map<String, Object> map = new HashMap<>();
			map.put("message", "Bad Credential");
			map.put("status", false);
			return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
		}
		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
		List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());
		UserInfoResponse response = new UserInfoResponse(userDetails.getId(), userDetails.getUsername(), roles);
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(response);
	}

	@PostMapping("/singup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {

		if (userRepository.existsByUserName(signupRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error:User Name Already is Taken!!!"));
		}

		if (userRepository.existsByEmail(signupRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error:Email ID Already is Taken!!!"));
		}

		User user = new User(signupRequest.getUsername(), signupRequest.getEmail(),
				encoder.encode(signupRequest.getPassword()));
		Set<String> strRole = signupRequest.getRole();
		Set<Roles> roles = new HashSet<>();

		if (strRole == null) {

			Roles userRole = rolesRepository.findByRoleName(APIRole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role Name Already Taken!!!"));
			roles.add(userRole);
		} else {

			strRole.forEach(role -> {
				switch (role) {
				case "admin":
					Roles adminRol = rolesRepository.findByRoleName(APIRole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role Is Not Found"));
					roles.add(adminRol);
					break;
				case "seller":
					Roles sellerRole = rolesRepository.findByRoleName(APIRole.ROLE_SELLER)
							.orElseThrow(() -> new RuntimeException("Error:Role Is Not Found!!!"));
					roles.add(sellerRole);
					break;
				default:
					Roles userRole = rolesRepository.findByRoleName(APIRole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role Is Not Found"));
					roles.add(userRole);
				}
			});

		}
		user.setRoles(roles);
		userRepository.save(user);
		return ResponseEntity.ok(new MessageResponse("User Registered Sucessfully"));
	}

	@GetMapping("/username")
	public String currentUserName(Authentication authentication) {
		if (authentication != null) {
			return authentication.getName();
		} else {
			return " ";
		}

	}

	@GetMapping("/getAllUsers")
	public ResponseEntity<?> getAllUsers(Authentication authentication) {

		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();

		List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());
		UserInfoResponse response = new UserInfoResponse(userDetailsImpl.getId(), userDetailsImpl.getUsername(), roles);
		return ResponseEntity.ok().body(response);

	}
    @PostMapping("/signout")
	public ResponseEntity<?> signoutUser(){
		
    	   ResponseCookie responseCookie=jwtUtils.getClearJwtCookie();
    	   return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString())
    			   .body(new MessageResponse("You've been singed out"));
	}
}
