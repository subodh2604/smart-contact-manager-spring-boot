package com.smart.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.entities.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	
//	@Query("select u from User u where u.email=:email")
//	public User getUserByUsername(@Param("email") String email);
	
	public User findByEmail(String email);
	/* public User getUserByUse(String name); */
}
