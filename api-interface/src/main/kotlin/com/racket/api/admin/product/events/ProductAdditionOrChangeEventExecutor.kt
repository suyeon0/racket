package com.racket.api.admin.product.events

import org.springframework.context.annotation.Bean
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Component

@Component
class ProductAdditionOrChangeEventExecutor {

//    @Bean("productAdditionOrChangeExecutor")
//    fun productAdditionOrChangeExecutor() =
//        ThreadPoolTaskExecutor().apply {
//            this.corePoolSize = 1
//            this.maxPoolSize = 4*2
//            this.queueCapacity = 100
//            this.threadNamePrefix = "productCRD_T-"
//        }

}