package com.myat.java.springBoot.lottery.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "bets")
public class Bet {

	@Id
	private String id;
	private String bankerId;
	private String middleManId;
	private List<BetDetails> chosenNumbers;
	private String status;
	
	@CreatedDate
	private Date betTime;

	public Bet(String id, String bankerId, String middleManId, 
				List<BetDetails> chosenNumbers, String status,
				Date betTime) {
		this.id = id;
		this.bankerId = bankerId;
		this.middleManId = middleManId;
		this.chosenNumbers = chosenNumbers;
		this.status = status;
		this.betTime = betTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBankerId() {
		return bankerId;
	}

	public void setBankerId(String bankerId) {
		this.bankerId = bankerId;
	}

	public String getMiddleManId() {
		return middleManId;
	}

	public void setMiddleManId(String middleManId) {
		this.middleManId = middleManId;
	}

	public List<BetDetails> getChosenNumbers() {
		return chosenNumbers;
	}

	public void setChosenNumbers(List<BetDetails> chosenNumbers) {
		this.chosenNumbers = chosenNumbers;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getBetTime() {
		return betTime;
	}

	public void setBetTime(Date betTime) {
		this.betTime = betTime;
	}
	
	
}
