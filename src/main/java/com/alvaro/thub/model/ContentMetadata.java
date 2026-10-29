package com.alvaro.thub.model;

public class ContentMetadata {

	/*
	 * Clase  que representa la metadata asociada a un contenido,
	 *  incluyendo información adicional sobre el contenido,
	 *   como su tipo de entidad, identificador de entidad,
	 *    identificador de recurso y formato.
	 */

	private String entityType = null;    
	private String entityId = null; 
	private String resourceId = null; 
	private String format = null; 

	public ContentMetadata() {		
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}


	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

}
