package com.tfcaielastic.services.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tfcaielastic.dto.AccidentDTO;
import com.tfcaielastic.dto.CommonDTO;
import com.tfcaielastic.dto.DistanceDTO;
import com.tfcaielastic.dto.Feature;
import com.tfcaielastic.dto.Point2DTO;
import com.tfcaielastic.dto.PointDTO;
import com.tfcaielastic.repository.AccidentRepository;
import com.tfcaielastic.services.IAccidentService;

@Service
@Transactional
public class AccidentService implements IAccidentService {

	@Inject
	public AccidentRepository accidentRepository;
		
	@Autowired
	public RestHighLevelClient client;
		
	@Override
	public List<AccidentDTO> getAccidentsByDate(String desde, String hasta) {		
		List<AccidentDTO> result = new ArrayList<AccidentDTO>();
		this.getAccidentRepository().findByDateBetween(desde, hasta).stream().map(a -> new AccidentDTO(a))
			.collect(Collectors.toCollection(() -> result));	
		System.out.println("#################: " + result.size());
		return result;
	}		
	
	@Override
	public CommonDTO getAccidentsByCommonFeatures()  {
		CommonDTO common = new CommonDTO();
		try {
			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();	
			
			//Creo las agregaciones para los 3 campos
			searchSourceBuilder.aggregation(AggregationBuilders.terms("weather_condition").field("Weather_Condition.keyword"));
			searchSourceBuilder.aggregation(AggregationBuilders.terms("city").field("City.keyword"));
			searchSourceBuilder.aggregation(AggregationBuilders.terms("state").field("State.keyword"));
			
			SearchRequest searchRequest = new SearchRequest();
			searchRequest.source(searchSourceBuilder);
			
			//Eejecuto la consulta
			SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
			
			//Obtengo el resultado de las agregaciones y los asigno al objeto Common
			Aggregations aggregations = searchResponse.getAggregations();
			Terms terms = aggregations.get("weather_condition");
			common.setWeather_condition(new Feature(terms.getBuckets().get(0).getKeyAsString(), terms.getBuckets().get(0).getDocCount()));
			
			terms = aggregations.get("city");
			common.setCity(new Feature(terms.getBuckets().get(0).getKeyAsString(), terms.getBuckets().get(0).getDocCount()));
							
			terms = aggregations.get("state");
			common.setState(new Feature(terms.getBuckets().get(0).getKeyAsString(), terms.getBuckets().get(0).getDocCount()));
					
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return common;		
	}
	
	@Override		
	public List<AccidentDTO> getAccidentsByLocation(double lat, double lon, double ratio) {
				
		List<AccidentDTO> result = new ArrayList<AccidentDTO>();
		try {				
			SearchRequest searchRequest = new SearchRequest();
			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
			
			//Creo la consulta de geolocalizacion"
			GeoDistanceQueryBuilder qb = QueryBuilders
					  .geoDistanceQuery("start_location")
					  .point(lat, lon)
					  .distance(ratio, DistanceUnit.KILOMETERS);
			
			//Armo la consulta y le agrego como filtro la consulta de geolocalizaion
			BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery()).filter(qb);
										
			searchSourceBuilder.query(boolQueryBuilder);
			searchSourceBuilder.size(3000000);			
			searchRequest.indices("accidentes");
			searchRequest.source(searchSourceBuilder);	
			
			SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
			
			//Recorro la colecion con los resultados, armo el objeto que contiene los datos a mostrar y los agrego a una coleccion
			AccidentDTO accident;
			for (SearchHit sh : searchResponse.getHits().getHits()) {	
				accident = new AccidentDTO();
				accident.setId(sh.getId().toString());
				accident.setIdentificador(sh.getSourceAsMap().get("ID").toString());
				accident.setCity(sh.getSourceAsMap().get("City").toString());
				accident.setState(sh.getSourceAsMap().get("State").toString());
				accident.setStart_time(sh.getSourceAsMap().get("@timestamp").toString());
				result.add(accident);
			}	
		} 		
		catch (IOException e) {
			e.printStackTrace();
		}			
		System.out.println("#################: " + result.size());
		return result;
	}
		
	public List<PointDTO> getAccidentsByDangerousPoints1(double ratio, List<PointDTO> points) {	
		
		try {
			SearchRequest searchRequest;
			SearchSourceBuilder searchSourceBuilder;
			GeoDistanceQueryBuilder qb;
			BoolQueryBuilder boolQueryBuilder;
			
			for(PointDTO p: points) {
				
				searchRequest = new SearchRequest();
				searchSourceBuilder = new SearchSourceBuilder();
				
				//Consulta para la geolocalización
				qb = QueryBuilders.geoDistanceQuery("start_location").point(p.getLat(), p.getLon()).distance(ratio, DistanceUnit.KILOMETERS);
				
				//Creo la consulta con el filtro de la geolocalizacion
				boolQueryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery()).filter(qb);
											
				searchSourceBuilder.query(boolQueryBuilder);
				searchSourceBuilder.size(3000000);			
				searchRequest.indices("accidentes");
				searchRequest.source(searchSourceBuilder);	
				
				////Por cada punto ingresado por el usuario cuento la cantidad de accidentes teniendo en cuenta el radio
				p.setAccidents((int)client.search(searchRequest, RequestOptions.DEFAULT).getHits().getTotalHits().value);			
				
			}
			
			//Ordeno la colección de forma descendente
			Collections.sort(points);
			
			//Retorno solo los primeros 5 elementos
			return points.stream().limit(5).collect(Collectors.toList());
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public Point2DTO getAccidentsByDangerousPoints2() {	
		Point2DTO points = new Point2DTO();
		try {
			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();	
			
			//Creo la agregacion para agrupar por el campo new_location y mostrar solo los primeros resultados
			searchSourceBuilder.aggregation(AggregationBuilders.terms("dangerous_points").field("new_location.keyword").minDocCount(1).size(5));
			
			SearchRequest searchRequest = new SearchRequest();
			searchRequest.source(searchSourceBuilder);
			
			//Realizo la consulta
			SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
			
			//Obtengo los resultados de la agregacion
			Aggregations aggregations = searchResponse.getAggregations();
			Terms terms = aggregations.get("dangerous_points");
			
			//Agrego a la lista los cinco puntos mas peligrosos
			for(Bucket b : terms.getBuckets()) {			
				points.getDangerous_points().add(new Feature(b.getKeyAsString(), b.getDocCount()));
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}	
		
		return points;
	}
	
	@Override
	public List<DistanceDTO> getAccidentsByAverageDistance() {
		List<DistanceDTO> result = new ArrayList<DistanceDTO>();		
		try {		
			
			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
			searchSourceBuilder.scriptField("distance", new Script(ScriptType.INLINE, "expression", "(doc['Distance(mi)'].value * 1609.34) / 2", Collections.emptyMap()));
			searchSourceBuilder.scriptField("identificador", new Script(ScriptType.INLINE, "painless", "params._source.ID", Collections.emptyMap()));
			
			SearchRequest searchRequest = new SearchRequest();
			searchSourceBuilder.size(3000000);	
			searchRequest.indices("accidentes");
			searchRequest.source(searchSourceBuilder);	
			
			SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
			
			for (SearchHit sh : searchResponse.getHits().getHits()) {	
				if(sh != null) {			
					result.add(new DistanceDTO(sh.getId(), sh.getFields().get("identificador").getValue(), sh.getFields().get("distance").getValue()));						
				}
			}			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("################ " + result.size());
		return result;
	}
	
	
	public AccidentRepository getAccidentRepository() {
		return this.accidentRepository;
	}
	
}