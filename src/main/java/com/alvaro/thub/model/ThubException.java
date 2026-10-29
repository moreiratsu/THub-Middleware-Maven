package com.alvaro.thub.model;

public class ThubException extends RuntimeException {

	public ThubException(String message) {
		super(message); 
	}
	
	public ThubException(Throwable cause) { 
		super(cause); 
	}
	
	public ThubException(String message, Throwable cause) { 
		super(message, cause); 
	}

}
