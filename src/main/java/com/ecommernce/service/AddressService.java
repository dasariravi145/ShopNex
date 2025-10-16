package com.ecommernce.service;

import java.util.List;

import com.ecommernce.model.User;
import com.ecommernce.payload.AddressDTO;

public interface AddressService {

	    AddressDTO createAddress(AddressDTO addressDTO,User user);
	    
	    List<AddressDTO> getAddress();
	    
	    AddressDTO getAddressById(Long addressId);
	    
	    List<AddressDTO> getUserAddress(User user);
	    
	    AddressDTO updateAddress(AddressDTO addressDTO,Long addressId);
	    
	    String deleteAddress(Long addressId);
}
