package com.ecommernce.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommernce.exceptions.ResourseNotFoundException;
import com.ecommernce.model.Address;
import com.ecommernce.model.User;
import com.ecommernce.payload.AddressDTO;
import com.ecommernce.repository.AddressRepository;
import com.ecommernce.repository.UserRepository;

@Service
public class AddressServiceImpl implements AddressService{
	
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public AddressDTO createAddress(AddressDTO addressDTO, User user) {
		
		Address address=modelMapper.map(addressDTO, Address.class);
		
		List<Address> addressList=user.getAddress();
		
		addressList.add(address);
		user.setAddress(addressList);
		
		address.setUser(user);
		Address savedAddress=addressRepository.save(address);
		// TODO Auto-generated method stub
		return modelMapper.map(savedAddress, AddressDTO.class);
	}

	@Override
	public List<AddressDTO> getAddress() {
		// TODO Auto-generated method stub
		
		List<Address> addressList=addressRepository.findAll();
		List<AddressDTO> addressDTO=addressList.stream()
				.map(address->modelMapper.map(address, AddressDTO.class)).toList();
		return addressDTO;
	}

	@Override
	public AddressDTO getAddressById(Long addressId) {
		
		Address address=addressRepository.findById(addressId)
				.orElseThrow(()->new ResourseNotFoundException("Address","addressId",addressId));
		AddressDTO addressDTO=modelMapper.map(address, AddressDTO.class);
		// TODO Auto-generated method stub
		return addressDTO;
	}

	@Override
	public List<AddressDTO> getUserAddress(User user) {
		List<Address> addressList=user.getAddress();
		// TODO Auto-generated method stub
		List<AddressDTO> addressDTOList=addressList.stream()
				.map(address->modelMapper.map(address, AddressDTO.class))
				.toList();
		return addressDTOList;
	}

	@Override
	public AddressDTO updateAddress(AddressDTO addressDTO, Long addressId) {
		
		Address address=addressRepository.findById(addressId)
				.orElseThrow(()->new ResourseNotFoundException("Address","addressId",addressId));
		
		address.setCity(addressDTO.getCity());
		address.setState(addressDTO.getState());
		address.setBuildingName(addressDTO.getBuildingName());
		address.setCountry(addressDTO.getCountry());
		address.setPincode(addressDTO.getPincode());
		
		Address updateAddress=addressRepository.save(address);
		
		User user=address.getUser();
		
		user.getAddress().removeIf(add->add.getAddressId().equals(addressId));
		user.getAddress().add(updateAddress);
		userRepository.save(user);
		
		// TODO Auto-generated method stub
		return modelMapper.map(updateAddress, AddressDTO.class);
	}

	@Override
	public String deleteAddress(Long addressId) {
		// TODO Auto-generated method stub
		Address address=addressRepository.findById(addressId)
				.orElseThrow(()->new ResourseNotFoundException("Address","addressId",addressId));
		
		addressRepository.deleteById(addressId);
		User user=address.getUser();
		user.getAddress().removeIf(add->add.getAddressId().equals(addressId));
		userRepository.save(user);
		return "Deleted Address:" +address.getAddressId()+":Sucessfully";
	}

}
