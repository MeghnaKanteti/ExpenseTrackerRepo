package com.jsp.et.service;

import com.jsp.et.dto.UserDTO;

public interface UserService 
{
	//this method is called in the controller for the user registration for the front end file
	int registration(UserDTO dto);
	
	//UserDTO login(String username, String password);
	UserDTO login(UserDTO userdto);

	UserDTO findByUserId(int userId);

	UserDTO updateUserProfile(int id, UserDTO dto);
	
	int deleteUserProfile(int id);

	


	
}
