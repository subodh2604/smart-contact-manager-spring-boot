package com.smart.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class MyConfig {



	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		/*
		 * http.csrf(csrf -> csrf.disable()) .authorizeHttpRequests()
		 * .requestMatchers("/admin/**").hasRole("ADMIN").requestMatchers("/user/**")
		 * .hasRole("USER").requestMatchers("/**").permitAll().and().formLogin();
		 */
		http.csrf().disable().authorizeRequests().requestMatchers("/user/**").hasRole("USER").and().formLogin(
				form->form.loginPage("/signin").loginProcessingUrl("/dologin").defaultSuccessUrl("/user/index"));
		return http.build();

	}

	@Bean
	public UserDetailsService userDetailsService()
	{
		return new UserDetailsServiceImpl();
	}

	/*
	 * @Bean public BCryptPasswordEncoder bCryptPasswordEncoder() { return new
	 * BCryptPasswordEncoder(); }
	 */
	
	@Bean
	public AuthenticationProvider authenticationProvider()
	{
		DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService());
		/*
		 * authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
		 */		return authenticationProvider;
	}
}
