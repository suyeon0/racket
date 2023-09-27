package com.racket.core.external.client.feign

import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration

@Configuration
@EnableFeignClients("com.racket.api")
class FeignConfig {
}

// cart 패키지에 api 가 없어서 Feign 못찾고 있음. 수정해야함!