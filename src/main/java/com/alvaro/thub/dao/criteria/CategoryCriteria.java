package com.alvaro.thub.dao.criteria;

import com.alvaro.thub.model.AbstractValueObject;

/**
 * Clase de criterios para filtrar la búsqueda de categorías.
 * Solo ponemos los que son usados en los filtros de búsqueda.
 */

public class CategoryCriteria extends AbstractValueObject {
 	
	private Long id = null;
	private String name = null;
	private Long genderId = null;
	
	
	// atributo nuevo de ordenación
	private String orderBy = null;
	private boolean ascDesc = true; // por defecto, orden ascendente
	
	public CategoryCriteria() {		
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

	public Long getGenderId() {
		return genderId;
	}

	public void setGenderId(Long genderId) {
		this.genderId = genderId;
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
