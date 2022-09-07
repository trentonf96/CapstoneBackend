package com.capstone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import com.capstone.domain.User;
import com.capstone.domain.UserDTO;
import com.capstone.domain.UserRepository;


@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RegisterController
{
	@Autowired
	UserRepository userRepository;
	
	@PostMapping("/register")
	@Transactional
	public void registration(@RequestBody UserDTO userDTO)
	{
		User u = userRepository.findByUsername(userDTO.username);
		
		//Register if user does not exist
		
		if(u == null)
		{
			User temp = new User();
			temp.setUser_name(userDTO.username);
			temp.setPassword(userDTO.password);
			userRepository.save(temp);
		}
		else
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, userDTO.username +  " Already Exists");
		}
	}
}