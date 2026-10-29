package com.alvaro.thub.model;

public class ContentData {
	
	/*
	 *  Clase que representa los datos de un contenido, incluyendo los datos binarios
	 *  (raw) y la metadata asociada al contenido. 
	 *  La metadata se almacena en un objeto de tipo ContentMetadata, que contiene información adicional sobre el contenido, como su nombre, tipo MIME, tamaño, etc.
	 */
	
	private byte[] raw = null;
	private ContentMetadata metadata = null;
	
	
	public ContentData() {		
	}

	public byte[] getRaw() {
		return raw;
	}

	public void setRaw(byte[] raw) {
		this.raw = raw;
	}

	public ContentMetadata getMetadata() {
		return metadata;
	}

	public void setMetadata(ContentMetadata metadata) {
		this.metadata = metadata;
	}
}
