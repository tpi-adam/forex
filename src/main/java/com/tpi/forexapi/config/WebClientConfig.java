package com.tpi.forexapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${forex.api.url}")
    private String forexApiUrl;

    @Bean(name = "forexWebClient")
    public WebClient forexWebClient() {
        return WebClient.builder()
                        .baseUrl(forexApiUrl)
                        .build();
    }

}
