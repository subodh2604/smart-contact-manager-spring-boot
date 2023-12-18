package com.smart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smart.entities.User;
import com.smart.repositories.UserRepository;

public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user=null;
		user= userRepository.findByEmail(username);
//		System.out.println("User fetched"+user);
		if(user==null)
		{
			throw new UsernameNotFoundException("User doesn't exists");
		}
		
		UserDetails userDetails= new CustomUserDetails(user);
		
		return userDetails;
	}

}
