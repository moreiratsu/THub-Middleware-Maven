package com.alvaro.thub.model;

import java.util.Date;

/**
 * POJO (Plain Old Java Object) para la entidad Event.
 * las clases que tengan foreing keys de otras clases, se convierten en DTOs.
 * Por ejemplo, como Event tiene una foreing key a Status entre otras, esta clase se llamaria EventDTO
 * para operaciones CRUD básicas (create, update, delete)
 */

public class Event extends AbstractValueObject{

	private Long id = null;
	private String name = null;
	private Date startDate = null;
	private Date endDate  = null;
	private Long formatId  = null;
	private Long statusId  = null;
	private Long creatorUserId  = null;
	private Long countryId  = null;
	private Long provinceId  = null;
	private Long localityId  = null;

	public Event() {		
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





	public Long getCreatorUserId() {
		return creatorUserId;
	}


	public void setCreatorUserId(Long creatorUserId) {
		this.creatorUserId = creatorUserId;
	}


	public Long getCountryId() {
		return countryId;
	}


	public void setCountryId(Long countryId) {
		this.countryId = countryId;
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





}
