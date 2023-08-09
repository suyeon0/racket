package com.racket.cache.consume

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@EnableFeignClients(basePackages = ["com.racket.cache.consume"])
@ServletComponentScan
@SpringBootApplication
class CacheConsumerApplication {
}

fun main(args: Array<String>) {
	runApplication<CacheConsumerApplication>(*args)
}