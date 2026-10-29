package com.alvaro.thub.model;

/**
 * DTO (Data Transfer Object) para la entidad Event.
 */

public class EventDTO extends Event {

	/**
	 * DTO (Data Transfer Object) para la entidad Event.
	 * Úsalo para mostrar datos en la UI (incluye nombres de Format, Status, etc.)
	 * Extiende Event para reutilizar sus campos.
	 */

	private String formatName  = null;

	private String statusName  = null;

	private String creatorUserName  = null;
	private String creatorUserLastName = null;
	private String creatorUserEmail = null;
	private String creatorUserPhoneNumber = null;
	
	private String countryName = null;
    private String provinceName = null;   
    private String localityName = null;   

	public EventDTO() {		
	}


	public String getFormatName() {
		return formatName;
	}

	public void setFormatName(String formatName) {
		this.formatName = formatName;
	}


	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}



	public String getCreatorUserName() {
		return creatorUserName;
	}

	public void setCreatorUserName(String creatorUserName) {
		this.creatorUserName = creatorUserName;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}


	public String getProvinceName() {
		return provinceName;
	}


	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}


	public String getLocalityName() {
		return localityName;
	}


	public void setLocalityName(String localityName) {
		this.localityName = localityName;
	}


	public String getCreatorUserLastName() {
		return creatorUserLastName;
	}


	public void setCreatorUserLastName(String creatorUserLastName) {
		this.creatorUserLastName = creatorUserLastName;
	}


	public String getCreatorUserEmail() {
		return creatorUserEmail;
	}


	public void setCreatorUserEmail(String creatorUserEmail) {
		this.creatorUserEmail = creatorUserEmail;
	}


	public String getCreatorUserPhoneNumber() {
		return creatorUserPhoneNumber;
	}


	public void setCreatorUserPhoneNumber(String creatorUserPhoneNumber) {
		this.creatorUserPhoneNumber = creatorUserPhoneNumber;
	}


}
