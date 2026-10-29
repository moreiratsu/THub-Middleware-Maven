package com.alvaro.thub.dao;

import com.alvaro.thub.model.ThubException;

public class DataException extends ThubException {

	// Constructor que recibe solo la Exception
	public DataException(Exception cause) {
		super(cause);
	}

	// Constructor que recibe un mensaje personalizado y la Exception
	public DataException(String message, Exception cause) {
		super(message, cause);
	}

	// Constructor para mensajes de error lógicos de datos sin Exception
	public DataException(String message) {
		super(message);
	}

	// Constructor que recibe otra DataException, por si hay llamado entre DAOs
	public DataException(DataException cause) {
		super(cause);
	}

}
