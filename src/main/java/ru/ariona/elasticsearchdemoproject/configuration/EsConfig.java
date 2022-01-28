package ru.ariona.elasticsearchdemoproject.configuration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EsConfig {
    @Bean
    public ElasticsearchClient elasticsearchClient() {
        ElasticsearchTransport elasticsearchTransport = elasticsearchTransport();

        return new ElasticsearchClient(elasticsearchTransport);
    }

    @Bean
    public ElasticsearchTransport elasticsearchTransport() {
        RestClient restClient = RestClient.builder(new HttpHost("localhost", 9200)).build();

        return new RestClientTransport(restClient, new JacksonJsonpMapper());
    }
}
