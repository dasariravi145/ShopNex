package com.ecommernce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommernce.model.User;
import com.ecommernce.payload.AddressDTO;
import com.ecommernce.service.AddressService;
import com.ecommernce.util.AuthUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class AddressController {

	@Autowired
	private AddressService addressService;

	@Autowired
	private AuthUtil authUtil;

	@PostMapping("/addAddress")
	public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO) {

		User user = authUtil.loggedInUser();
		AddressDTO savedAddressDTO = addressService.createAddress(addressDTO, user);
		return new ResponseEntity<AddressDTO>(savedAddressDTO, HttpStatus.CREATED);

	}

	@GetMapping("/getAddress")
	public ResponseEntity<List<AddressDTO>> getAddress() {

		List<AddressDTO> addressDTList = addressService.getAddress();
		return new ResponseEntity<List<AddressDTO>>(addressDTList, HttpStatus.OK);
	}

	@GetMapping("/getAddress/{addressId}")
	public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId) {

		AddressDTO addressDTO = addressService.getAddressById(addressId);
		return new ResponseEntity<AddressDTO>(addressDTO, HttpStatus.OK);

	}

	@GetMapping("/getAddress/username")
	public ResponseEntity<List<AddressDTO>> getUserAddressByUserName() {

		User user = authUtil.loggedInUser();
		List<AddressDTO> addressDToList = addressService.getUserAddress(user);
		return new ResponseEntity<List<AddressDTO>>(addressDToList, HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<AddressDTO> updateAddress(@Valid @RequestBody AddressDTO addressDTO,
			@PathVariable Long addressId) {
		AddressDTO savedAddressDTO = addressService.updateAddress(addressDTO, addressId);
		return new ResponseEntity<AddressDTO>(savedAddressDTO, HttpStatus.OK);
	}

	@DeleteMapping("deleteAddress/{addressId}")
	public ResponseEntity<String> deleteAddress(@PathVariable Long addressId) {

		String addressDTO = addressService.deleteAddress(addressId);
		return new ResponseEntity<String>(addressDTO, HttpStatus.OK);
	}
}
