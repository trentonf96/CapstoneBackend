package com.capstone.domain;

public class UserDTO{
	public int id;
	public String username;
	public String password;
	
	
	public UserDTO() {
		this.id = 0;
		this.username = null;
		this.password = null;
	}
	
	public UserDTO(String username, String password)
	{
		this.id = 0;
		this.username = username;
		this.password = password;
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserDTO other = (UserDTO) obj;
		if (id != other.id)
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;

		return true;
	}
}