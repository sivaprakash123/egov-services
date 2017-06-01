package org.egov.web.indexer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.tracer.config.TracerConfiguration;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.TimeZone;

@SpringBootApplication
@Import(TracerConfiguration.class)
public class SearchIndexerApplication {

    private static final String IST = "Asia/Calcutta";
    private static final String CLUSTER_NAME = "cluster.name";

    private TransportClient client;

    @Value("${es.host}")
    private String elasticSearchHost;

    @Value("${es.transport.port}")
    private Integer elasticSearchTransportPort;

    @Value("${es.cluster.name}")
    private String elasticSearchClusterName;

    @PostConstruct
    public void init() throws UnknownHostException {
        Settings settings = Settings.builder()
            .put(CLUSTER_NAME, elasticSearchClusterName)
            .build();
        final InetAddress esAddress = InetAddress.getByName(elasticSearchHost);
        final InetSocketTransportAddress transportAddress =
            new InetSocketTransportAddress(esAddress, elasticSearchTransportPort);
        client = new PreBuiltTransportClient(settings)
            .addTransportAddress(transportAddress);
    }

    @Bean
    public ObjectMapper getObjectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return objectMapper;
    }

    @Bean
    public MappingJackson2HttpMessageConverter jacksonConverter(ObjectMapper objectMapper) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);
        return converter;
    }

    @Bean
    public TransportClient getTransportClient() {
        return client;
    }

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone(IST));
        SpringApplication.run(SearchIndexerApplication.class, args);
    }

}