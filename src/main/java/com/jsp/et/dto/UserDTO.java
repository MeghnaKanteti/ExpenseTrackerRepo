package com.jsp.et.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//DTO - Data Transfer Object which is used to transfer the data from controller to service
//it is used to store the data which entered by the user in front-end files...

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO 
{
	private int userId;
	private String fullName;
	private String userName;
	private String email;
	private String mobile;
	private String password;
	private String repassword;
	
	private ImageDTO image;
}
