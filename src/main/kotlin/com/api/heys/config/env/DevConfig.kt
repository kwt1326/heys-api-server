package com.api.heys.config.env

import com.api.heys.domain.aws.sms.service.SmsMessageService
import com.api.heys.domain.aws.sms.service.impl.SlackSmsMessageService
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
@ConditionalOnProperty(name = ["spring.profiles.active"], havingValue = "dev")
class DevConfig {

    @Bean
    fun smsMessageService(restTemplate: RestTemplate) : SmsMessageService {
        return SlackSmsMessageService(restTemplate)
    }
}