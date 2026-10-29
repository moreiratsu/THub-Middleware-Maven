package com.alvaro.thub.dao.criteria;

import com.alvaro.thub.model.AbstractValueObject;

public class UserCriteria extends AbstractValueObject{
	
	public final String ORDER_BY_ID = " user.id ";
	public final String ORDER_BY_NAME = " user.name ";
	public final String ORDER_BY_LOCALITY_NAME = " locality.name ";
	
	/*
	 * Criteria para filtrar busquedas de usuarios
	 */
	private Long id = null;
	private String name = null;
	private String dni = null;
	private String email = null;	
	private Long localityId = null;
	private Long provinceId = null;
	private Long countryId = null;
	private Long rolId = null;
	private Long genderId = null;
	
	private String orderBy = ORDER_BY_NAME;
    private boolean ascDesc = true;

	public UserCriteria() {		
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

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public Long getLocalityId() {
		return localityId;
	}

	public void setLocalityId(Long localityId) {
		this.localityId = localityId;
	}


	public Long getRolId() {
		return rolId;
	}

	public void setRolId(Long rolId) {
		this.rolId = rolId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getGenderId() {
		return genderId;
	}

	public void setGenderId(Long genderId) {
		this.genderId = genderId;
	}

	public Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public boolean isAscDesc() {
		return ascDesc;
	}

	public void setAscDesc(boolean ascDesc) {
		this.ascDesc = ascDesc;
	}
	
	
}	
