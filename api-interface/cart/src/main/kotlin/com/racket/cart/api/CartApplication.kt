package com.racket.cart.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@ServletComponentScan
@SpringBootApplication(scanBasePackages = ["com.racket.cart", "com.racket.api.shared", "com.racket.share"])
class CashApplication {
}

fun main(args: Array<String>) {
	runApplication<CashApplication>(*args)
}