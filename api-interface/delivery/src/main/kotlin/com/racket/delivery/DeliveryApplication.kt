package com.racket.delivery

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@ServletComponentScan
@SpringBootApplication(scanBasePackages = ["com.racket.delivery"])
class CashApplication {
}

fun main(args: Array<String>) {
	runApplication<CashApplication>(*args)
}