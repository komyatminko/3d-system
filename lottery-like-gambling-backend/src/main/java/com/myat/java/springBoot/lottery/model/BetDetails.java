package com.myat.java.springBoot.lottery.model;

public class BetDetails {

	private Integer betNumber;
	private Integer amount;
	public BetDetails(Integer number, Integer amount) {
		this.betNumber = number;
		this.amount = amount;
	}
	
	public Integer getBetNumber() {
		return betNumber;
	}

	public void setBetNumber(Integer betNumber) {
		this.betNumber = betNumber;
	}

	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	
	
}
