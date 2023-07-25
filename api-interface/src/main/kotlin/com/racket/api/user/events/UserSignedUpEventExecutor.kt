package com.racket.api.user.events

import org.springframework.context.annotation.Bean
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Component

@Component
class UserSignedUpEventExecutor {

    @Bean("userSignedUpMailSendExecutor")
    fun userSignedUpMailSendExecutor() =
        ThreadPoolTaskExecutor().apply {
            this.corePoolSize = 4
            this.maxPoolSize = 4*2
            this.queueCapacity = 200
            this.setThreadNamePrefix("userSignedUpMailSendExecutor-")
        }

    @Bean("userSignedUpSmsSendExecutor")
    fun userSignedUpSmsSendExecutor() =
        ThreadPoolTaskExecutor().apply {
            this.corePoolSize = 4
            this.maxPoolSize = 4*2
            this.queueCapacity = 200
            this.setThreadNamePrefix("userSignedUpSmsSendExecutor-")
        }
}