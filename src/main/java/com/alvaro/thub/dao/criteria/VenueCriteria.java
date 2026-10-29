package com.alvaro.thub.dao.criteria;

import com.alvaro.thub.model.AbstractValueObject;

public class VenueCriteria extends AbstractValueObject{
    
    private Long id = null;
    private String name = null;
    private String address = null;
    private Long localityId = null;
    private String provinceId = null;
    private String countryId = null;
    
    public VenueCriteria() {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getLocalityId() {
        return localityId;
    }

    public void setLocalityId(Long localityId) {
        this.localityId = localityId;
    }

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}
}