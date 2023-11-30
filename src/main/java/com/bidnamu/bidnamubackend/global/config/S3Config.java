package com.bidnamu.bidnamubackend.global.config;

import io.awspring.cloud.autoconfigure.core.CredentialsProperties;
import io.awspring.cloud.autoconfigure.core.RegionProperties;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@RequiredArgsConstructor
public class S3Config {

    private final RegionProperties regionProperties;
    private final CredentialsProperties credentialsProperties;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
            .credentialsProvider(this::awsCredentials)
            .region(Region.of(Objects.requireNonNull(regionProperties.getStatic())))
            .build();
    }

    private AwsCredentials awsCredentials() {
        return AwsBasicCredentials.create(credentialsProperties.getAccessKey(),
            credentialsProperties.getSecretKey());
    }
}