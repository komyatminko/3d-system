package com.myat.java.springBoot.lottery.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "bet_summary")
public class BetSummary {

	@Id
	private String id;
	private String bankerId;
	private Integer betNumber;
	private Integer totalBetAmount;
	private Boolean isLimitReached;
	
	public BetSummary(String id, String bankerId, Integer betNumber, 
						Integer totalBetAmount, Boolean isLimitReached) {
		this.id = id;
		this.bankerId = bankerId;
		this.betNumber = betNumber;
		this.totalBetAmount = totalBetAmount;
		this.isLimitReached = isLimitReached;
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
	public Integer getBetNumber() {
		return betNumber;
	}
	public void setBetNumber(Integer betNumber) {
		this.betNumber = betNumber;
	}
	public Integer getTotalBetAmount() {
		return totalBetAmount;
	}
	public void setTotalBetAmount(Integer totalBetAmount) {
		this.totalBetAmount = totalBetAmount;
	}
	public Boolean getIsLimitReached() {
		return isLimitReached;
	}
	public void setIsLimitReached(Boolean isLimitReached) {
		this.isLimitReached = isLimitReached;
	}
	
	
}
