package com.ecommernce.security;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ecommernce.model.APIRole;
import com.ecommernce.model.Roles;
import com.ecommernce.model.User;
import com.ecommernce.repository.RolesRepository;
import com.ecommernce.repository.UserRepository;
import com.ecommernce.security.jwt.AuthEntryPointJWT;
import com.ecommernce.security.jwt.AuthTokenFilter;
import com.ecommernce.security.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	
	  @Autowired
	  private UserDetailsServiceImpl userDetailsServiceImpl;
	  
	  @Autowired
	  private AuthEntryPointJWT unauthEntryPointJWT;
	  
	  @Bean
	  public AuthTokenFilter authTokenFilter() {
		  return new AuthTokenFilter();
	  }
	  
	  @Bean
	  public DaoAuthenticationProvider authenticationProvider() {
		  
		     DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
		     authenticationProvider.setUserDetailsService(userDetailsServiceImpl);
		     authenticationProvider.setPasswordEncoder(passwordEncoder());
		     return authenticationProvider;
	  }
	      
	  @Bean
	  public PasswordEncoder passwordEncoder() {
		  return new BCryptPasswordEncoder();
	  }
	  
	  @Bean
	  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)throws Exception {
		  return authenticationConfiguration.getAuthenticationManager();
	  }
	  
	  @Bean
	  public SecurityFilterChain filterChain(HttpSecurity httpSecurity)throws Exception {
		  
		    httpSecurity.csrf(csrf->csrf.disable())
		    .exceptionHandling(exception->exception.authenticationEntryPoint(unauthEntryPointJWT))
		    .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		    .authorizeHttpRequests(auth->
		      auth.requestMatchers("/api/auth/**").permitAll()
		          .requestMatchers("/V3/api-docs/**").permitAll()
		          .requestMatchers("/h2-console/**").permitAll()
		          .requestMatchers("/api/admin/**").permitAll()
		          //.requestMatchers("/api/public/**").permitAll()
		          .requestMatchers("/swagger-ui/**").permitAll()
		          .requestMatchers("/api/test/**").permitAll()
		          .requestMatchers("/images/**").permitAll()
		          .anyRequest().authenticated()
		    		
		    );
		      
		    httpSecurity.authenticationProvider(authenticationProvider());
		    httpSecurity.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		    httpSecurity.headers(headers->headers.frameOptions(frameOptions->frameOptions.sameOrigin()));
		    
		    return httpSecurity.build();
	  }
	  @Bean
	  public WebSecurityCustomizer webSecurityCustomizer() {
		  return (web->web.ignoring().requestMatchers("/v1/api-docs",
				  "/configuration/ui",
				  "/swagger-resources/**",
				  "/configuration/security",
				  "/swagger-ui.html",
				  "/webjars/**"));
	  }
	  
	  @Bean
	  public CommandLineRunner initData(RolesRepository rolesRepository,UserRepository userRepository,PasswordEncoder passwordEncoder) {
		  
		  return args->{
			  Roles userRole=rolesRepository.findByRoleName(APIRole.ROLE_USER).orElseGet(()->{
				  Roles newUserRole=new Roles(APIRole.ROLE_USER);
				  return rolesRepository.save(newUserRole);
			  });
			  
			  Roles sellerRole=rolesRepository.findByRoleName(APIRole.ROLE_SELLER).orElseGet(()->{
				  Roles newUserRole=new Roles(APIRole.ROLE_SELLER);
				  return rolesRepository.save(newUserRole);
			  });
			  
			  Roles adminRole=rolesRepository.findByRoleName(APIRole.ROLE_ADMIN).orElseGet(()->{
				  Roles newUserRole=new Roles(APIRole.ROLE_ADMIN);
				  return rolesRepository.save(newUserRole);
			  });
			  
			  Set<Roles> userRoles=Set.of(userRole);
			  Set<Roles> sellerRoles=Set.of(sellerRole);
			  Set<Roles> adminRoles=Set.of(userRole,sellerRole,adminRole);
			  
			  if(!userRepository.existsByUserName("user1")){
				
				  User user1=new User("user1","user1@gmail.com",passwordEncoder.encode("password1"));
				  userRepository.save(user1);
			  }
			  
			  if(!userRepository.existsByUserName("seller1")){
					
				  User seller1=new User("seller1","seller1@gmail.com",passwordEncoder.encode("password2"));
				  userRepository.save(seller1);
			  }
			  
			  if(!userRepository.existsByUserName("admin")){
					
				  User admin=new User("admin","admin@gmail.com",passwordEncoder.encode("adminpass"));
				  userRepository.save(admin);
			  }
			  //Update roles for existing users
			  
			  userRepository.findByUserName("user1").ifPresent(user->{
				  user.setRoles(userRoles);
				  userRepository.save(user);
			  });
			  
			  userRepository.findByUserName("seller1").ifPresent(seller->{
				  seller.setRoles(sellerRoles);
				  userRepository.save(seller);
			  });
			  
			  userRepository.findByUserName("admin").ifPresent(admin->{
				  admin.setRoles(adminRoles);
				  userRepository.save(admin);
			  });
			  
		  };
	  }
	  
}

