package com.api.heys.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class AWSConfig(
    @Value("\${cloud.aws.credentials.accessKey}") private val accessKey: String,
    @Value("\${cloud.aws.credentials.secretKey}") private val secretKey: String,
) {
//    private AWSCredentials awsCredentials;
//
//    @PostConstruct
//    public void init() {
//        awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
//    }
//
//    @Bean
//    public AWSCredentialsProvider awsCredentialsProvider() {
//        return new AWSStaticCredentialsProvider(awsCredentials);
//    }
}