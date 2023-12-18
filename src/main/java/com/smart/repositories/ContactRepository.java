package com.smart.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.smart.entities.Contact;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
	@Query(
			  value = "SELECT * FROM contact c WHERE c.user_id = ?", 
			  nativeQuery = true)
	public Page<Contact> findContactsByUser(int userId,Pageable pageable);
}
