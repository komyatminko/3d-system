package com.myat.java.springBoot.lottery.model;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "bankers")
public class Banker extends User{

	private Date accountExpiredDate;
	private Boolean isAccountExpired;
	
	public Date getAccountExpiredDate() {
		return accountExpiredDate;
	}
	public void setAccountExpiredDate(Date accountExpiredDate) {
		this.accountExpiredDate = accountExpiredDate;
	}
	public Boolean getIsAccountExpired() {
		return isAccountExpired;
	}
	public void setIsAccountExpired(Boolean isAccountExpired) {
		this.isAccountExpired = isAccountExpired;
	}
	
	@Override
	public String toString() {
		return "Banker [accountExpiredDate=" + accountExpiredDate + ", isAccountExpired=" + isAccountExpired + "]";
	}
	
	
}
