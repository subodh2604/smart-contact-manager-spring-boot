package com.smart.controllers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;
import com.smart.repositories.ContactRepository;
import com.smart.repositories.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	//Method for adding common data to all urls of controller
	@ModelAttribute
	public void addCommonData(Model model,Principal principal)
	{
		String userName = principal.getName();
		
		User user=userRepository.findByEmail(userName);
		
		System.out.println("User: "+user);
		
		model.addAttribute("user", user);
	}
	
	//home page
	@RequestMapping("/index")
	public String dashboard(Model model,Principal principal)
	{
		model.addAttribute("title","User Dashboard");
		/*
		 * String userName = principal.getName();
		 * 
		 * User user=userRepository.findByEmail(userName);
		 * 
		 * System.out.println("User: "+user);
		 * 
		 * model.addAttribute("user",user);
		 */
		
		return "normal/user_dashboard";
	}
	
	@RequestMapping("/add-contact")
	public String openAddContactForm(Model model,Principal principal)
	{
		model.addAttribute("title","add contact");
		model.addAttribute("contact", new Contact());
		
		return "normal/add_contact_form";
	}
	
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute("contact") Contact contact,@RequestParam("profileImage") MultipartFile file , Principal principal,HttpSession session)
	{
		try {
			String name=principal.getName();
			User user=this.userRepository.findByEmail(name);
			
			//processing and uploading file..
			
			if(file.isEmpty())
			{
				System.out.println("file is empty");
				contact.setImage("contact.png");
			}
			else {
				contact.setImage(file.getOriginalFilename());
				File saveFile=new ClassPathResource("static/img").getFile();
				Path path= Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				
			}
			
			contact.setUser(user);
			user.getContacts().add(contact);
			
			userRepository.save(user);
			session.setAttribute("message", new Message("Your contact is added!!!! Add more..","success"));
			
		} catch (Exception e) {
			System.out.println("Error "+e.getMessage());
			e.printStackTrace();
			session.setAttribute("message", new Message("Sorry, something went wrong!!!!. Please try again later. ","danger"));
		}
		
		
		
		/* System.out.println("contact saved: "+contact.toString()); */
		return "normal/add_contact_form";
	}
	
	//show contacts handler
	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page , Model model,Principal principal)
	{
		String email = principal.getName();
		
		User user = userRepository.findByEmail(email);
		
		Pageable pageable = PageRequest.of(page, 2);
		
		Page<Contact> contacts = contactRepository.findContactsByUser(user.getId(),pageable);
		
		model.addAttribute("title", "show user contacts");
		model.addAttribute("contacts", contacts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contacts.getTotalPages());
		
		return "normal/show_contacts";
	}
	
	//showing particular contact details
	@GetMapping("/{cId}/contact")
	public String showContactDetail(@PathVariable("cId") Integer id , Model model,Principal principal)
	{
		
		System.out.println("CID "+id);
		
		model.addAttribute("title", "show user contact details");
		Optional<Contact> contactOptional = this.contactRepository.findById(id);
		Contact contact=contactOptional.get();
		
		String userName = principal.getName();
		
		User user=userRepository.findByEmail(userName);
		
		if(user.getId()==contact.getUser().getId())
		{
			model.addAttribute("contact", contact);
		}
		
		
		System.out.println(contact.toString());
		return "normal/contact_details";
	}
	
	@GetMapping("delete/{cId}")
	public String deleteContact(@PathVariable("cId") Integer id , Model model,Principal principal,HttpSession session)
	{
		
		System.out.println("CID "+id);
		
		/*
		 * model.addAttribute("title", "show user contact details");
		 */
		Optional<Contact> contactOptional = this.contactRepository.findById(id);
		Contact contact=contactOptional.get();
		
		String userName = principal.getName();
		
		User user=userRepository.findByEmail(userName);
		
		if(user.getId()==contact.getUser().getId())
		{
			contact.setUser(null);
			this.contactRepository.delete(contact);
			
			session.setAttribute("message", new Message("contact deleted successfully!!!!", "success"));
		}
		
		
//		System.out.println(contact.toString());
		return "redirect:/user/show-contacts/0";
	}
	
	@PostMapping("/update-contact/{cid}")
	public String updateForm(@PathVariable("cid") Integer cid,Model model)
	{
		Optional<Contact> contactOptional = this.contactRepository.findById(cid);
		Contact contact=contactOptional.get();
		
		model.addAttribute("contact", contact);
		
		return "normal/update_contact";
	}
	
	@PostMapping("/process-update")
	public String updateHandler(@ModelAttribute("contact") Contact contact,@RequestParam("profileImage") MultipartFile file , Principal principal,HttpSession session,Model model)
	{
		
		
		
		try {
			System.err.println("inside handler");
			Contact oldContactDetail=this.contactRepository.findById(contact.getCId()).get();
			
			if(!file.isEmpty())
			{
				//delete old file
				File deleteFile=new ClassPathResource("static/img").getFile();
				File file1=new File(deleteFile, oldContactDetail.getImage());
				file1.delete();
				
				
				
				contact.setImage(file.getOriginalFilename());
				File saveFile=new ClassPathResource("static/img").getFile();
				Path path= Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				contact.setImage(file.getOriginalFilename());
			}
			else {
				contact.setImage(oldContactDetail.getImage());
			}
			
			String userName = principal.getName();
			
			User user=userRepository.findByEmail(userName);
			
			contact.setUser(user);
			
			this.contactRepository.save(contact);
			
			session.setAttribute("message", new Message("contact updated successfully!!!!", "success"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(contact.getCId());
		 return "redirect:/user/"+contact.getCId()+"/contact";
	}
}
