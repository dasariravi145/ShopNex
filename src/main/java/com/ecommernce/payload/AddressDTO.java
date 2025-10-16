package com.ecommernce.payload;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Data
public class AddressDTO {
	
	  private Long addressId;
	   @Size(min=5,message="Street Name Must Be At Least 5 Character")
	   private String street;
	   @Size(min=5,message="BuildingName Name Must Be At Least 5 Character")
	   private String buildingName;
	   @Size(min=4,message="City Name Must Be At Least 4 Character")
	   private String city;
	   @Size(min=2,message="State Name Must Be At Least 2 Character")
	   private String state;
	   @Size(min=2,message="Country Name Must Be At Least 2 Character")
	   private String country;
	   @Size(min=6,message="Pincode Name Must Be At Least 6 Character")
	   private String pincode;

}
