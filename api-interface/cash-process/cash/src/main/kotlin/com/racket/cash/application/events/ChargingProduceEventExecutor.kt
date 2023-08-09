package com.racket.cash.application.events

import org.springframework.context.annotation.Bean
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Component

@Component
class ChargingProduceEventExecutor {

    @Bean("chargingEventExecutor")
    fun chargingProduceEventExecutor() =
        ThreadPoolTaskExecutor().apply {
            this.corePoolSize = 1
            this.maxPoolSize = 4*2
            this.queueCapacity = 100
            this.setThreadNamePrefix("ChargingProduceExecutor_T-")
        }
}