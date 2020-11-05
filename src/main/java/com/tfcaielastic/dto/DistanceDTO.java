package com.tfcaielastic.dto;

public class DistanceDTO {

	public String id;
	
	public String identificador;
	
	public double distance;
	
	public DistanceDTO(String id, String identificador, double distance) {
		this.setId(id);
		this.setIdentificador(identificador);
		this.setDistance(distance);
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

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
	
}
