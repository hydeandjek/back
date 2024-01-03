package com.example.demo.shareapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3BaseClientBuilder;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;

@Configuration
public class AwsConfig {
    @Value("${aws.credentials.access-key}")
    private String ACCESS_KEY;

    @Value("${aws.credentials.secret-key}")
    private String SECRET_KEY;

    @Value("${aws.region}")
    private String REGION;




    @Bean
    @Primary
    public AwsBasicCredentials awsCredentialsProvider(){


        return AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY);

    }

    @Bean
    public S3Client amazonS3() {
        ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create();
        Region region = Region.of(REGION);

        return S3Client.builder()
                .region(region)
                .credentialsProvider(credentialsProvider)
                .build();

    }
//
//    // aws S3 client 생성
////    @Bean
////    public S3Client amazonS3() {
////        return S3Client.builder()
////                .region(Region.of(REGION))
////                .credentialsProvider(new AWSStaticCredentialsProvider(awsCredentialsProvider))
////                .build();
////    }


}
