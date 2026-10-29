package com.alvaro.thub.dao.criteria;

import com.alvaro.thub.model.AbstractValueObject;

public class ProvinceCriteria extends AbstractValueObject{

	private Long id = null;
	private String name = null;
	
	private Long countryId  = null;
	
	public ProvinceCriteria() {		
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

	

	
	
}
