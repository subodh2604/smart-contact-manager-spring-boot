package com.smart.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "CONTACT")
@Getter
@Setter
public class Contact {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int cId;
	
	private String secondName;
	
	private String name;
	
	private String email;
	
	private String work;
	
	private String image;
	
	@Column(length = 50000)
	private String description;
	
	private String phone;
	
	@ManyToOne
	private User user;
}
