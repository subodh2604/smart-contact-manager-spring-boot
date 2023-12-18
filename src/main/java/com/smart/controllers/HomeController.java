package com.smart.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.entities.User;
import com.smart.helper.Message;
import com.smart.repositories.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	
	/*
	 * @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;
	 */	 
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping("/")
	public String test(Model model)
	{	
		model.addAttribute("title", "Home - smart contact manager");
		
		return "home";
	}
	
	@RequestMapping("/about")
	public String about(Model model)
	{	
		model.addAttribute("title", "About - smart contact manager");
		return "about";
	}
	
	@RequestMapping("/signup")
	public String signup(Model model)
	{	
		model.addAttribute("title", "Signup - smart contact manager");
		model.addAttribute("user",new User());
		return "signup";
	}
	
	@RequestMapping(value ="/do_register",method = RequestMethod.POST)
	public String registerUser(@ModelAttribute("user") User user,@RequestParam(value = "agreement",defaultValue = "false") boolean agreement,Model model,HttpSession session,BindingResult result) {
		
		try {
			if(!agreement)
			{
				System.out.println("You have not agreed on terms and conditions");
				throw new Exception("You have not agreed on terms and conditions");
			}
			if(result.hasErrors())
			{
				System.out.println("Error: "+result.toString());
				model.addAttribute("user",user);
				
				return "signup";
			}
			user.setRole("USER_ROLE");
			user.setEnabled(true);
			user.setImageUrl("image1.png");
			/*
			 * user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			 */			User savedUser=userRepository.save(user);
			
			model.addAttribute("user",new User());
			session.setAttribute("message",new Message("Successfully registered !!!!","alert-success"));

		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message",new Message("Something went wrong !! "+e.getMessage(),"alert-danger"));
		}
		return "signup";
	}
	
	@RequestMapping("/signin")
	public String customLogin(Model model)
	{	
		model.addAttribute("title", "Login - smart contact manager");
		/*
		 * model.addAttribute("user",new User());
		 */		return "login";
	}
}
