package com.alvaro.thub.model;

public class Status extends AbstractValueObject{
	
	private Long id = null;
	private String name = null;
	
	public Status() {		
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
}
