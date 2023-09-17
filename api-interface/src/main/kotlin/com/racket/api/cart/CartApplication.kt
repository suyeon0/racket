package com.racket.api.cart

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@ServletComponentScan
@SpringBootApplication(scanBasePackages = ["com.racket.cart"])
class CashApplication {
}

fun main(args: Array<String>) {
	runApplication<CashApplication>(*args)
}