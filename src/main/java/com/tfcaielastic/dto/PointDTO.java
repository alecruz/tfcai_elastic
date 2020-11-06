package com.tfcaielastic.dto;

public class PointDTO implements Comparable<PointDTO>{

	private String name;	
	
	private double lat;
	
	private double lon;
	
	private int accidents;
	
	public PointDTO(String name, double lat, double lon, int accidents) {
		this.setName(name);
		this.setLat(lat);
		this.setLon(lon);
		this.setAccidents(accidents);
	}

	public String getName() {
		return name;
	}

	public double getLat() {
		return lat;
	}

	public double getLon() {
		return lon;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public int getAccidents() {
		return accidents;
	}

	public void setAccidents(int accidents) {
		this.accidents = accidents;
	}

	public int compareTo(PointDTO comparestu) {
        int compareaccidents=((PointDTO)comparestu).getAccidents();
        return compareaccidents-this.accidents;
    }
	
}