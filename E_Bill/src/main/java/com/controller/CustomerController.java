package com.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.detailservice.CustomerUserDetails;
import com.model.*;
import com.repository.CustomerRepository;

@Controller
public class CustomerController  {

	@Autowired
	private CustomerRepository customerRepository;

	@GetMapping("")
	public String viewHomePage() {
		return "index";
	}
	@GetMapping("/customer/login")
	public String loginPage() {
		return "customer/customer_login";
	}
	
	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("customer", new Customer());	
		return "customer/signup_form";
	}
	
	@PostMapping("/process_register")
	public String processRegister(@ModelAttribute("customer")Customer customer,Model model) {
		if(customerRepository.findByEmail(customer.getEmail() ) != null ){
			model.addAttribute("customer", new Customer());	
			model.addAttribute("error", "This email is alredy registered");
			return "customer/signup_form";
		}
		if(customerRepository.findByPhone(customer.getPhone()) != null ){
			model.addAttribute("customer", customer);	
			model.addAttribute("error1", "This phone number is alredy used");
			return "customer/signup_form";
		}
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(customer.getPassword());
		customer.setPassword(encodedPassword);	
		customerRepository.save(customer);		
		return "register_success";		
	}
	@PostMapping("/saveCustomer")
	public String saveCustomer(@ModelAttribute("customer") Customer customer) {
		customerRepository.save(customer);
        return "redirect:/customer/login";
	}
	
	@GetMapping("/customer/home")
	public String homeUsers(@AuthenticationPrincipal CustomerUserDetails user, Model model) {
		Customer customer = customerRepository.findByEmail(user.getUsername());
		model.addAttribute("Customer",customer);
		return "customer/customer_home";
	}
	
	@PostMapping("/customer/showdetails/{id}")
	public String detailsUsers(@PathVariable(value = "id") int id, Model model) {
		Customer customer = customerRepository.findByCustomerid(id);
		model.addAttribute("customer",customer);
		return "customer/customer_detail";
	}
	
	@PostMapping("/customer/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable(value = "id") int id, Model model) {
        Customer customer = customerRepository.findByCustomerid(id);     
        model.addAttribute("customer", customer);
        return "customer/update_customer";
    }

	@GetMapping("/knowcustomerid")
	public String knowId(Model model) {
		model.addAttribute("customer", new Customer());
		return "know_form";
	}
	
	@PostMapping("/knowcustomerid")
	public String knowIds(String email, Model model) {
		Customer customer = (customerRepository.findByEmail(email));
		if(customer !=null) {
			customer=customerRepository.findByEmail(email);
			String msg = "Hello! " +customer.getName() +"  Your customer id is " +"' " +customer.getCustomerid() +" '";		
			model.addAttribute("customerid", msg);
			return "know_form";
		}
		String err= "  Cannot find any customer id for this " + email;
		model.addAttribute("errorMessage",err);
		
		return "know_form";
	}
	 @PostMapping("customer/deleteCustomer/{id}")
	 public String deleteCustomer(@PathVariable(value = "id") int id) { 
	        this.customerRepository.deleteById(id);
	        return "redirect:/";
	 }
}