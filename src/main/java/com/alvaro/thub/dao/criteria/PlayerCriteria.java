package com.alvaro.thub.dao.criteria;

import java.util.Date;

import com.alvaro.thub.model.AbstractValueObject;

public class PlayerCriteria extends AbstractValueObject{
	
	public final String ORDER_BY_ID = " p.id ";
	public final String ORDER_BY_NAME = " p.name ";
	public final String ORDER_BY_LOCALITY_NAME = " locality.name ";
	
	private Long id = null;
	private String dni = null;
	private String name = null;
	private String lastName1 = null;
	private String lastName2 = null;
	
	
	private Date bornDateFrom = null;  // Fecha de nacimiento desde
	private Date bornDateTo = null;    // Fecha de nacimiento hasta
	
	private Long localityId = null;
	private Long provinceId = null;
	private Long countryId = null;
	
	private Long teamId = null;
	private Long genderId = null;
	
	private String orderBy = ORDER_BY_NAME;
	private boolean ascDesc = true;
	
	public PlayerCriteria() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName1() {
		return lastName1;
	}

	public void setLastName1(String lastName1) {
		this.lastName1 = lastName1;
	}

	public Long getLocalityId() {
		return localityId;
	}

	public void setLocalityId(Long localityId) {
		this.localityId = localityId;
	}

	public Long getTeamId() {
		return teamId;
	}

	public void setTeamId(Long teamId) {
		this.teamId = teamId;
	}

	public Long getGenderId() {
		return genderId;
	}

	public void setGenderId(Long genderId) {
		this.genderId = genderId;
	}

	public String getLastName2() {
		return lastName2;
	}

	public void setLastName2(String lastName2) {
		this.lastName2 = lastName2;
	}

	public Date getBornDateFrom() {
		return bornDateFrom;
	}

	public void setBornDateFrom(Date bornDateFrom) {
		this.bornDateFrom = bornDateFrom;
	}

	public Date getBornDateTo() {
		return bornDateTo;
	}

	public void setBornDateTo(Date bornDateTo) {
		this.bornDateTo = bornDateTo;
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