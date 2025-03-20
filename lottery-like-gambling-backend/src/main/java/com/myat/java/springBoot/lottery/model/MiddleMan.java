package com.myat.java.springBoot.lottery.model;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "middlemen")
public class MiddleMan extends User{

	@DBRef
	private Banker banker;

	public Banker getBanker() {
		return banker;
	}

	public void setBanker(Banker banker) {
		this.banker = banker;
	}
	
	
	
	
}
