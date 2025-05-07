package com.myat.java.springBoot.lottery.exception;

public class MiddlemanNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public MiddlemanNotFoundException(String msg) {
		super(msg);
	}
	
}
