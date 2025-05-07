package com.myat.java.springBoot.lottery.dto;

import java.util.Date;

public class BankerDto extends UserDto{
	
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
}
