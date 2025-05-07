package com.myat.java.springBoot.lottery.exception;

public class BankerNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public BankerNotFoundException(String msg) {
		super(msg);
	}
	
}
