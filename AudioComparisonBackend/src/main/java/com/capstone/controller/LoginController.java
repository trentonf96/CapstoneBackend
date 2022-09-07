package com.capstone.controller;

import java.util.Base64;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.capstone.domain.User;
import com.capstone.domain.UserRepository;

@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LoginController {

	@Autowired
	UserRepository userRepository;
	
	@GetMapping("/user")
	public String user (@RequestParam("url") String url, @RequestParam("username") String user, @RequestParam("pass") String pass)
	{	
		// Login and check username/password
		User u = userRepository.findByUsername(user);
		System.out.println(user);
		System.out.println(pass);
		System.out.println(u);
		System.out.println(u.getPassword());
		if (u != null && u.getPassword().equals(pass)) {
			System.out.println("User exists!");
			byte[] decodedBytes = Base64.getUrlDecoder().decode(url);
			String decodedUrl = new String(decodedBytes);
			System.out.println(decodedUrl.substring(0, decodedUrl.length() - 5));
			//baseUrl: "http://example.com:8080"
			return "redirect:" + decodedUrl + "record";
			
		} else {
			System.out.println("User does not exist");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, user + " does not exist.");
		}

	}
}