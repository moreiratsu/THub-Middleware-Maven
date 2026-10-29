package com.alvaro.thub.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Clase base para los objetos de valor (Value Objects) en el modelo.
 * Proporciona una implementación predeterminada del método toString()
 * utilizando la reflexión para generar una representación en cadena
 * de los atributos del objeto.
 * Es la clase padre para representar objetos.
 */

public abstract class AbstractValueObject {
	
	public AbstractValueObject() {
	}

	@Override
	public final String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
