package com.tfcaielastic.dto;

public class CommonDTO {

	private Feature weather_condition;
	
	private Feature city;
		
	private Feature state;
	
	public CommonDTO() {}

	public Feature getWeather_condition() {
		return weather_condition;
	}

	public Feature getCity() {
		return city;
	}

	public Feature getState() {
		return state;
	}

	public void setWeather_condition(Feature weather_condition) {
		this.weather_condition = weather_condition;
	}

	public void setCity(Feature city) {
		this.city = city;
	}

	public void setState(Feature state) {
		this.state = state;
	}
		
	
}

