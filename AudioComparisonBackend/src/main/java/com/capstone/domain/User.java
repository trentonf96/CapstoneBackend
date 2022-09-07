package com.capstone.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String username;
	private String password;
	
	public User() {
		super();
	}
	
	public int getId() {
		return id;
	}
	
	public void setUser_id(int id)
	{
		this.id = id;
	}
	
	public String getName() {
		return username;
	}
	
	public void setUser_name(String username)
	{
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + "]";
	}
}