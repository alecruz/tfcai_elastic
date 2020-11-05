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
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tfcaielastic.dto.AccidentDTO;
import com.tfcaielastic.dto.CommonDTO;
import com.tfcaielastic.dto.DistanceDTO;
import com.tfcaielastic.dto.Feature;
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
		return result;
	}		
	
	@Override
	public CommonDTO getAccidentsByCommonFeatures()  {
		CommonDTO common = new CommonDTO();
		try {
			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();	
			searchSourceBuilder.aggregation(AggregationBuilders.terms("weather_condition").field("Weather_Condition.keyword"));
			searchSourceBuilder.aggregation(AggregationBuilders.terms("city").field("City.keyword"));
			searchSourceBuilder.aggregation(AggregationBuilders.terms("state").field("State.keyword"));
			
			SearchRequest searchRequest = new SearchRequest();
			searchRequest.source(searchSourceBuilder);
			
			SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
			
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
		/*try {			
			
		QueryBuilder qb = QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery());
		
		SearchSourceBuilder ssb = new SearchSourceBuilder();
		ssb.query(qb);
		ssb.size(3000000);		
		
		SearchRequest searchRequest = new SearchRequest();				
		searchRequest.indices("accidentes");
		searchRequest.source(ssb);	
		
		SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
		System.out.println("######## " + searchResponse.getHits().getTotalHits());
		
		
		/*		
		 * 
		 * QueryBuilder queryBuilder = QueryBuilders.boolQuery()
			      .must(QueryBuilders.termQuery("City", "Dayton"));
		client.search(searchRequest, options)(queryBuilder, Accident.class);
		
			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
			SearchRequest searchRequest = new SearchRequest();				
			searchSourceBuilder.query(queryBuilder);
			searchSourceBuilder.size(3000000);			
			searchRequest.indices("accidentes");
			searchRequest.source(searchSourceBuilder);	
			
			Page<Accident> res = (Page<Accident>) this.accidentRepository.search(queryBuilder);
			
			System.out.println("######## " + res.getTotalElements());*/
			
			//SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
			
			//System.out.println("######## " + searchResponse.getHits().getTotalHits());
		    
		/*SearchRequest searchRequest = new SearchRequest();
				
		
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()				
				.must(QueryBuilders.matchAllQuery());
		
		searchSourceBuilder.query(boolQueryBuilder);
		searchSourceBuilder.scriptField("distance", new )
		
		//searchSourceBuilder.query(QueryBuilders.scriptQuery(new Script(ScriptType.INLINE,  "distance", "(doc['Distance(mi)'].value * 1609.34) / 2", Collections.emptyMap())));
		
		searchSourceBuilder.size(3000000);			
		searchRequest.indices("accidentes");
		searchRequest.source(searchSourceBuilder);	
		
		SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
		
		System.out.println("######## " + searchResponse.getHits().getTotalHits());*/
			
		
	//	SearchResponse response = client().prepareSearch()
		//	    .setQuery(matchAllQuery())
		//	    .addScriptField("result", new Script(ScriptType.INLINE, "groovy", "doc['field1'].value / doc['field2'].value", Collections.emptyMap()))
			    
			    
		//SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

		//BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery());
		
		//QueryBuilder qb = QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery());
		//NativeSearchQuery nsq = new NativeSearchQueryBuilder()
				//.withQuery(matchQuery)
				
		
		/*searchSourceBuilder.query(boolQueryBuilder);
		searchSourceBuilder.size(3000000);			
		searchRequest.indices("accidentes");
		searchRequest.source(searchSourceBuilder);			
		
		SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
		
		System.out.println("######## " + searchResponse.getHits().getTotalHits());
		
		}
		catch (IOException e) {
			e.printStackTrace();
		}*/
		
		
		
	}
	
	@Override		
	public List<AccidentDTO> getAccidentsByLocation(double lat, double lon, double ratio) {
				
		List<AccidentDTO> result = new ArrayList<AccidentDTO>();
		try {				
			SearchRequest searchRequest = new SearchRequest();
			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
			
			GeoDistanceQueryBuilder qb = QueryBuilders
					  .geoDistanceQuery("start_location")
					  .point(lat, lon)
					  .distance(ratio, DistanceUnit.KILOMETERS);
			
			BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery()).filter(qb);
										
			searchSourceBuilder.query(boolQueryBuilder);
			searchSourceBuilder.size(3000000);			
			searchRequest.indices("accidentes");
			searchRequest.source(searchSourceBuilder);	
			
			SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
			
			AccidentDTO accident;
			for (SearchHit sh : searchResponse.getHits().getHits()) {	
				accident = new AccidentDTO();
				accident.setId(sh.getId().toString());
				accident.setIdentificador(sh.getSourceAsMap().get("ID").toString());
				accident.setCity(sh.getSourceAsMap().get("City").toString());
				accident.setState(sh.getSourceAsMap().get("State").toString());
				result.add(accident);
			}	
		} 		
		catch (IOException e) {
			e.printStackTrace();
		}			
		return result;
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