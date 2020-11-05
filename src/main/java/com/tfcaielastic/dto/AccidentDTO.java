package com.tfcaielastic.dto;

import com.tfcaielastic.model.Accident;

public class AccidentDTO {
	
	public String id;
	
	public String identificador;
	
	public String city;
	
	public String state;
	
	public AccidentDTO() {}
	
	public AccidentDTO(Accident anAccident) {
		this.setId(anAccident.getId());
		this.setCity(anAccident.getCity());
		this.setIdentificador(anAccident.getIdentificador());
		this.setState(anAccident.getState());
	}
	
	public String getId() {
		return this.id;
	}

	public void setId(String anId) {
		this.id = anId;
	}
	
	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
}
