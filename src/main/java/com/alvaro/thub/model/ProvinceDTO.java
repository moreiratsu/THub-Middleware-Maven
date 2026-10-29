package com.alvaro.thub.model;

public class ProvinceDTO extends AbstractValueObject{
	
	private Long id = null;
	private String name = null;
	
	private Long countryId  = null;
	private String countryName = null;
	
	public ProvinceDTO() {		
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Long getCountryId() {
		return countryId;
	}


	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}


	public String getCountryName() {
		return countryName;
	}


	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}


	
	
}
