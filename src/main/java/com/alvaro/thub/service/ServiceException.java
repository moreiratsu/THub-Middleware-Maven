package com.alvaro.thub.service;

import com.alvaro.thub.model.ThubException;

public class ServiceException extends ThubException {

	// Constructor para mensajes de error personalizados de la lógica de negocio
	public ServiceException(String message) {
		super(message);
	}

	// Constructor para envolver otras excepciones (como DataException o errores inesperados)
	public ServiceException(Throwable cause) {
		super(cause);
	}

	// Constructor que combina un mensaje descriptivo con la causa original
	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
