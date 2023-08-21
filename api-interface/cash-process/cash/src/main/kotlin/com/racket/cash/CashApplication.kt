package com.racket.cash

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@EnableFeignClients(basePackages = ["com.racket.cash"])
@ServletComponentScan
@SpringBootApplication(scanBasePackages = ["com.racket.cash", "com.racket.api.shared"])
class CashApplication {
}

fun main(args: Array<String>) {
	runApplication<CashApplication>(*args)
}