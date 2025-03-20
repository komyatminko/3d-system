package com.myat.java.springBoot.lottery.dto;

import java.util.Date;


public class UserDto {

	private String id;
	private String username;
	private String role;
	private String phone;
	private String address;
	private Date created_at;
	private Date updated_at;
	
	public UserDto() {}
	
	public UserDto(String id, String username, String password, 
			String role, String phone, String address,
			Date created_at, Date updated_at) 
	{
		this.id = id;
		this.username = username;
		this.role = role;
		this.phone = phone;
		this.address = address;
		this.created_at = created_at;
		this.updated_at = updated_at;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}
}
