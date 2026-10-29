package com.alvaro.thub.dao.criteria;

import com.alvaro.thub.model.AbstractValueObject;

public class LocalityCriteria extends AbstractValueObject{
	
	private Long id = null;
	private String name = null;
	
	private Long provinceId  = null;
	
	public LocalityCriteria() {		
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

	public Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}
}
