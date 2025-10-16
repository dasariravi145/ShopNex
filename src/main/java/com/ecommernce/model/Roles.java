package com.ecommernce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter 
@Data
@NoArgsConstructor
@Table(name="ROLES")
public class Roles {

	     @Id
	     @GeneratedValue(strategy=GenerationType.IDENTITY)
	     private Long roleId;
	     
	     @ToString.Exclude
	     @Enumerated(EnumType.STRING)
	     @Column(name="ROLE_NAME")
	     private APIRole roleName;
	     
	     public Roles(APIRole roleName) {
	    	 
	    	 this.roleName=roleName;
	     }
}
