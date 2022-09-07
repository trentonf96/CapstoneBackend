package com.capstone.domain;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository <User, Integer>
{
	//declare the following method to return a single User object

	
	public User findByUsername(String username);
	
}