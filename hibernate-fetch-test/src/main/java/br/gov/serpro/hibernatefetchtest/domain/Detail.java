package br.gov.serpro.hibernatefetchtest.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Detail {

	@Id
	private Long id;

	private String description;
	
	public Detail(){
	}

	public Detail(Long id, String description) {
		super();
		this.id = id;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	

}
