package com.racket.consumer.config

import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration

@Configuration
@EnableFeignClients("com.racket.consumer")
class FeignConfig {
}