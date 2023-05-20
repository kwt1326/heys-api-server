package com.api.heys.config.env

import com.api.heys.domain.aws.sms.service.MessageService
import com.api.heys.domain.aws.sms.service.impl.SlackMessageService
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
@ConditionalOnProperty(name = ["spring.profiles.active"], havingValue = "dev")
class DevConfig {

    @Bean
    fun messageService(restTemplate: RestTemplate) : MessageService {
        return SlackMessageService(restTemplate)
    }
}