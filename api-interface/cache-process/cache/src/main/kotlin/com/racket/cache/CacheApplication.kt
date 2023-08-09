package com.racket.cache

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@EnableFeignClients(basePackages = ["com.racket.cache"])
@ServletComponentScan
@SpringBootApplication(scanBasePackages = ["com.racket.cache"])
class CacheApplication {
}

fun main(args: Array<String>) {
	runApplication<CacheApplication>(*args)
}