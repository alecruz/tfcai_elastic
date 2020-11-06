package com.tfcaielastic.dto;

import java.util.ArrayList;
import java.util.List;

public class Point2DTO{

	private List<Feature> dangerous_points;
	
	public Point2DTO() {
		this.dangerous_points = new ArrayList<Feature>();
	}

	public List<Feature> getDangerous_points() {
		return dangerous_points;
	}

	public void setDangerous_points(List<Feature> dangerous_points) {
		this.dangerous_points = dangerous_points;
	}
		
}