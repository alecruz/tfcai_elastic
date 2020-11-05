package com.tfcaielastic.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.tfcaielastic.model.Accident;

@Repository
public interface AccidentRepository extends ElasticsearchRepository<Accident, String> {

	public List<Accident> findByDateBetween(String desde, String hasta);
	
	
}
