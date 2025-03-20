package com.myat.java.springBoot.lottery.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "bankers_setting")
public class BetSetting {

	@Id
	private String id;
	private String bankerId;
	private Double commissionRate;
	private Integer majorPrize;
	private Integer minorPrize;
	private Integer maxBetPerNumber;
	
	public BetSetting(String id, String bankerId, Double commissionRate, 
						Integer majorPrize, Integer minorPrize,Integer maxBetPerNumber) {
		this.id = id;
		this.bankerId = bankerId;
		this.commissionRate = commissionRate;
		this.majorPrize = majorPrize;
		this.minorPrize = minorPrize;
		this.maxBetPerNumber = maxBetPerNumber;
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
	public Double getCommissionRate() {
		return commissionRate;
	}
	public void setCommissionRate(Double commissionRate) {
		this.commissionRate = commissionRate;
	}
	public Integer getMajorPrize() {
		return majorPrize;
	}
	public void setMajorPrize(Integer majorPrize) {
		this.majorPrize = majorPrize;
	}
	public Integer getMinorPrize() {
		return minorPrize;
	}
	public void setMinorPrize(Integer minorPrize) {
		this.minorPrize = minorPrize;
	}
	public Integer getMaxBetPerNumber() {
		return maxBetPerNumber;
	}
	public void setMaxBetPerNumber(Integer maxBetPerNumber) {
		this.maxBetPerNumber = maxBetPerNumber;
	}
	
	
}
