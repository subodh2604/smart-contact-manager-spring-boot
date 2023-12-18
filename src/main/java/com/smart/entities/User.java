package com.smart.entities;

import java.util.List;

import javax.validation.constraints.NotBlank;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "USER")
@Data
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private String role;
	
	@NotBlank
	@Size(min = 2,max = 20,message = "min 2 and max 20 characters are allowed !!!!")
	private String name;
	
	@Column(unique = true)
	private String email;
	
	private String password;
	
	private String imageUrl;
	
	@Column(length = 500)
	private String about;
	
	private boolean enabled;
	
	@OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
	private List<Contact> contacts;
	
	
}
