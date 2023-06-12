package com.api.heys.config.env

import com.api.heys.domain.aws.sms.service.SmsMessageService
import com.api.heys.domain.aws.sms.service.impl.AwsSmsMessageService
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty(name = ["spring.profiles.active"], havingValue = "real")
class RealConfig {

    @Bean
    fun smsMessageService() : SmsMessageService {
        return AwsSmsMessageService()
    }

}