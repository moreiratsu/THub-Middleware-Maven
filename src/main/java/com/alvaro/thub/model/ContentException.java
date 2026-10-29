package com.alvaro.thub.model;

import com.alvaro.thub.service.ServiceException;

public class ContentException extends ServiceException {
	
	public ContentException(String message) {
		super(message);
	}

	
	public ContentException(String message, Throwable cause) {
		super(message, cause);
	}
}
