package com.myat.java.springBoot.lottery.dto;

import com.myat.java.springBoot.lottery.model.Banker;

public class MiddleManDto extends UserDto{
	
	private String bankerId;

	public String getBankerId() {
		return bankerId;
	}

	public void setBankerId(String bankerId) {
		this.bankerId = bankerId;
	}

	@Override
	public String toString() {
		return "MiddleManDto [bankerId=" + bankerId + ", getId()=" + getId() + ", getUsername()=" + getUsername()
				+ ", getRoles()=" + getRoles() + ", getPhone()=" + getPhone() + ", getAddress()=" + getAddress()
				+ ", getCreated_at()=" + getCreated_at() + ", getUpdated_at()=" + getUpdated_at() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}
	
	
}
