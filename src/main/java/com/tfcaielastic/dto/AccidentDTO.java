package com.tfcaielastic.dto;

import com.tfcaielastic.model.Accident;

public class AccidentDTO {
	
	private String id;
	
	private String identificador;
	
	private String city;
	
	private String state;
	
	private String start_time;
	
	public AccidentDTO() {}
	
	public AccidentDTO(Accident anAccident) {
		this.setId(anAccident.getId());
		this.setCity(anAccident.getCity());
		this.setIdentificador(anAccident.getIdentificador());
		this.setState(anAccident.getState());
		this.setStart_time(anAccident.getDate());
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

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	
}
