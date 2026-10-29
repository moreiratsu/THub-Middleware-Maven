package com.alvaro.thub.service;

public class MailException extends ServiceException {

	 // Constructor que recibe solo un mensaje descriptivo
	public MailException(String message) {
		super(message);
	}

	// Constructor que recibe la causa original (útil para envolver excepciones de librerías de correo)
	public MailException(Throwable cause) {
		super(cause);
	}

	// Constructor que permite definir tanto un mensaje propio como la causa raíz
	public MailException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
