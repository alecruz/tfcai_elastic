package com.tfcaielastic.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

@Document(indexName = "accidentes")
public class Accident {

	@Id
	private String id;
	
	@Field("ID")
	public String identificador;

	@Field("City")
	private String city;	
	
	@Field("State")
	private String state;
	
	@Field("@timestamp")
	private String date;
	
	@GeoPointField
	private GeoPoint start_location;
	
	public double distance;
	
	public Accident(String id, String identificador, String city, String state, String date, GeoPoint start_location) {
		this.setId(id);
		this.setIdentificador(identificador);
		this.setCity(city);
		this.setState(state);
		this.setDate(date);
		this.setStart_location(start_location);
	}	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public GeoPoint getStart_location() {
		return start_location;
	}

	public void setStart_location(GeoPoint start_location) {
		this.start_location = start_location;
	}
	
	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}	

}