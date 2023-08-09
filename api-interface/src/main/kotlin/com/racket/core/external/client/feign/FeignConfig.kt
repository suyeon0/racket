package com.racket.core.external.client.feign

import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration

@Configuration
@EnableFeignClients("com.racket.api")
class FeignConfig {
}