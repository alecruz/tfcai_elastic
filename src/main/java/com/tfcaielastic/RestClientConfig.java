package com.tfcaielastic;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

@Configuration
public class RestClientConfig {

	@Bean
    public RestHighLevelClient client() {
		RestClientBuilder builder = RestClient
                .builder(new HttpHost("localhost", 9200));

        builder.setRequestConfigCallback(requestConfigBuilder -> 
                requestConfigBuilder
                        .setConnectTimeout(10000)
                        .setSocketTimeout(60000)
                        .setConnectionRequestTimeout(0)
        );

        RestHighLevelClient client = new RestHighLevelClient(builder);
        return client;
		
		
		
		
        /*ClientConfiguration clientConfiguration 
            = ClientConfiguration.builder()
                .connectedTo("localhost:9200")
                .build();
       
        
        
        return RestClients.create(clientConfiguration).rest();*/
    }
 
    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchRestTemplate(client());
    }
}
