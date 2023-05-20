package com.api.heys.config.env

import com.api.heys.domain.aws.sms.service.MessageService
import com.api.heys.domain.aws.sms.service.impl.AwsSnsMessageService
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty(name = ["spring.profiles.active"], havingValue = "real")
class RealConfig {

    @Bean
    fun messageService() : MessageService {
        return AwsSnsMessageService()
    }
}