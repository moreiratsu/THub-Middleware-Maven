package com.alvaro.thub.dao.criteria;

import com.alvaro.thub.model.AbstractValueObject;

public class TeamCriteria extends AbstractValueObject{

	public final String ORDER_BY_ID = " t.id ";
	public final String ORDER_BY_NAME = " t.name ";
	public final String ORDER_BY_INITIALISM = " t.initialism ";
	public final String ORDER_BY_LOCALITY_NAME = " l.name ";
	
	
	private Long id = null;
	private String initialism = null;
	private String name = null;
	
	private Long sportId = null;
	
	private Long localityId = null;

	// CAMPOS para filtrar directamente si quiero saber el equipo de una provincia o país, sin necesidad de hacer un join con localidad
    private Long provinceId = null;
    private Long countryId = null;
    
    // Para poner filtros de busqueda (por defecto por nombre las ordena)
    private String orderBy = ORDER_BY_NAME;
    private boolean ascDesc = true;
	
	public TeamCriteria() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getInitialism() {
		return initialism;
	}

	public void setInitialism(String initialism) {
		this.initialism = initialism;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getSportId() {
		return sportId;
	}

	public void setSportId(Long sportId) {
		this.sportId = sportId;
	}

	public Long getLocalityId() {
		return localityId;
	}

	public void setLocalityId(Long localityId) {
		this.localityId = localityId;
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
