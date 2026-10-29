package com.alvaro.thub.dao.criteria;
import java.util.Date;

import com.alvaro.thub.model.AbstractValueObject;

/**
 * Clase de criterios para filtrar la búsqueda de eventos.
 * Solo ponemos los que son usados en los filtros de búsqueda.
 */

public class EventCriteria extends AbstractValueObject{
	
	public final String ORDER_BY_ID = " event.id ";
	public final String ORDER_BY_NAME = " event.name ";
	public final String ORDER_BY_START_DATE = " event.start_date ";
	public final String ORDER_LOCALITY_NAME = " locality.name ";
	public final String ORDER_PROVINCE_NAME = " province.name ";
	public final String ORDER_COUNTRY_NAME = " country.name ";
	
	private Long id = null;
	private String name = null;
	private Date startDate = null;
	private Date endDate  = null;
	private Long formatId  = null;
	private Long creatorUserId  = null;
	private Long statusId  = null;
	private Long countryId  = null;
	private Long provinceId = null;
	private Long localityId = null;
	
	private String orderBy = ORDER_BY_NAME;
    private boolean ascDesc = true;
	
	public EventCriteria() {		
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


	public Date getStartDate() {
		return startDate;
	}


	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


	public Date getEndDate() {
		return endDate;
	}


	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


	public Long getFormatId() {
		return formatId;
	}


	public void setFormatId(Long formatId) {
		this.formatId = formatId;
	}


	public Long getStatusId() {
		return statusId;
	}


	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}


	public Long getCountryId() {
		return countryId;
	}


	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}


	public Long getCreatorUserId() {
		return creatorUserId;
	}


	public void setCreatorUserId(Long creatorUserId) {
		this.creatorUserId = creatorUserId;
	}


	public Long getProvinceId() {
		return provinceId;
	}


	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}


	public Long getLocalityId() {
		return localityId;
	}


	public void setLocalityId(Long localityId) {
		this.localityId = localityId;
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

	