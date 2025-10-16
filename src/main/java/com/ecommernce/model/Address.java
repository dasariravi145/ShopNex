package com.ecommernce.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
	   @Id
	   @GeneratedValue(strategy=GenerationType.IDENTITY)
	   private Long addressId;
	   
	   private String street;
	   
	   private String buildingName;
	   
	   private String city;
	   
	   private String state;
	   
	   private String country;
	   
	   private String pincode;

	   public Address(String street, String buildingName, String city, String state, String country, String pincode) {
		super();
		this.street = street;
		this.buildingName = buildingName;
		this.city = city;
		this.state = state;
		this.country = country;
		this.pincode = pincode;
	   }
	   
	   @ManyToOne
	   @JoinColumn(name="user_id")
	   private User user;

}
