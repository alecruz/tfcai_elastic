package com.tfcaielastic.dto;

public class Point implements Comparable<Point>{

	private String name;	
	
	private double lat;
	
	private double lon;
	
	private int accidents;
	
	public Point(String name, double lat, double lon, int accidents) {
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

	public int compareTo(Point comparestu) {
        int compareaccidents=((Point)comparestu).getAccidents();
        return compareaccidents-this.accidents;
    }
	
}