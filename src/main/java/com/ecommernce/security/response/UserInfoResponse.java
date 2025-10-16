package com.ecommernce.security.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse {
	
	     private Long id;
	     
	     private String jwtToken;
	     
	     private String username;
	     
	     private List<String> roles;

		 public UserInfoResponse(Long id, String username, List<String> roles) {
			super();
			this.id = id;
			this.username = username;
			this.roles = roles;
		 }
	     
	     

}
