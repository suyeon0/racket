package com.racket.api.user.events

import org.springframework.context.annotation.Bean
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Component

@Component
class UserSignedUpEventExecutor {

    @Bean("userSignedUpSendExecutor")
    fun userSignedUpMailSendExecutor() =
        ThreadPoolTaskExecutor().apply {
            this.corePoolSize = 1
            this.maxPoolSize = 4*2
            this.queueCapacity = 100
            this.setThreadNamePrefix("UserSignedUpSender_T-")
        }
}