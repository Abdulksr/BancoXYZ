package com.banco.xyz.bff_web.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class MsBankLegacyClientConfig {

    @Bean
    public RestClient msBankLegacyRestClient(
            @Value("${ms-bank-legacy.url}") String baseUrl,
            @Value("${ms-bank-legacy.connect-timeout:5000}") int connectTimeout,
            @Value("${ms-bank-legacy.read-timeout:5000}") int readTimeout) {
            
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofMillis(connectTimeout));
        factory.setReadTimeout(Duration.ofMillis(readTimeout));
        
        return RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(factory)
                .build();
    }

}
