package com.racket.cash.consume

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@EnableFeignClients(basePackages = ["com.racket.cash.consume"])
@ServletComponentScan
@SpringBootApplication
class CashConsumerApplication {
}

fun main(args: Array<String>) {
	runApplication<CashConsumerApplication>(*args)
}