package com.myat.java.springBoot.lottery.model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "middlemen")
public class MiddleMan extends User{

	private String bankerId;

	public String getBankerId() {
		return bankerId;
	}

	public void setBankerId(String bankerId) {
		this.bankerId = bankerId;
	}
	
	
	
	
	
}
