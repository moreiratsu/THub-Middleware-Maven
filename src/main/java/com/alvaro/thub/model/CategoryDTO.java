package com.alvaro.thub.model;

public class CategoryDTO extends AbstractValueObject{
	
	private Long id = null;
	private String name = null;
	private String description = null;
	
	private Long genderId = null;
	private String genderName = null;
	
	public CategoryDTO() {
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Long getGenderId() {
		return genderId;
	}
	
	public void setGenderId(Long genderId) {
		this.genderId = genderId;
	}

	public String getGenderName() {
		return genderName;
	}

	public void setGenderName(String genderName) {
		this.genderName = genderName;
	}



	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}
}
