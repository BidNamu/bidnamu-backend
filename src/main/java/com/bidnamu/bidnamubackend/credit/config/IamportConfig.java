package com.bidnamu.bidnamubackend.credit.config;

import com.siot.IamportRestClient.IamportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IamportConfig {

    private final String apiKey;
    private final String apiSecret;

    public IamportConfig(
        @Value("${portone.api.key}") final String apiKey,
        @Value("${portone.api.secret}") final String apiSecret
    ) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    @Bean
    public IamportClient iamportClient() {
        return new IamportClient(apiKey, apiSecret);
    }
}
