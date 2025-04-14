package com.example.techblog.dto;

import com.example.techblog.model.User;

public class UserDto {
	 private Long id;
	 private String username;

	    // Constructors
    public UserDto(Long id, String username) {
        this.id = id;
        this.username = username;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
    
	  public static UserDto fromEntity(User user) {
	        if (user == null) return null;
	        return new UserDto(user.getId(), user.getUsername());
	    }
}
