package com.tfcaielastic.services;

import java.util.List;

import com.tfcaielastic.dto.AccidentDTO;
import com.tfcaielastic.dto.CommonDTO;
import com.tfcaielastic.dto.DistanceDTO;
import com.tfcaielastic.dto.Point2DTO;
import com.tfcaielastic.dto.PointDTO;

public interface IAccidentService {

	public List<AccidentDTO> getAccidentsByDate(String desde, String hasta);
	
	public CommonDTO getAccidentsByCommonFeatures();
	
	public List<AccidentDTO> getAccidentsByLocation(double lat, double lon, double ratio);
	
	public List<DistanceDTO> getAccidentsByAverageDistance();
	
	public List<PointDTO> getAccidentsByDangerousPoints1(double ratio, List<PointDTO> points);
	
	public Point2DTO getAccidentsByDangerousPoints2();
}
