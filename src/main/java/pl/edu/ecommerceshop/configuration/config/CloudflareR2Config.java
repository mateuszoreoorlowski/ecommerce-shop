package pl.edu.ecommerceshop.configuration.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.edu.ecommerceshop.configuration.properties.CloudflareR2Properties;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;

@Configuration
@RequiredArgsConstructor
public class CloudflareR2Config {

    private final CloudflareR2Properties properties;

    @Bean
    public S3Client r2S3Client() {
        return S3Client.builder()
                .region(Region.of("auto"))
                .endpointOverride(URI.create(properties.getEndpoint()))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(
                                        properties.getAccessKeyId(),
                                        properties.getSecretAccessKey()
                                )
                        )
                )
                .serviceConfiguration(
                        S3Configuration.builder()
                                .pathStyleAccessEnabled(true)
                                .build()
                )
                .build();
    }
}