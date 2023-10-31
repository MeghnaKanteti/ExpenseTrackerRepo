package com.jsp.et.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.jsp.et.dto.ImageDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="user_table")
public class User 
{
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private int userId;
	private String fullName;
	private String userName;
	private String email;
	
	@Column(unique = true)
	private String mobile;
	private String password;
	
	//Bidirectional traversing
		@OneToMany(mappedBy = "user")
		private List<Expense> expense;
	
		private byte[] image;
		
	
	
	
}
