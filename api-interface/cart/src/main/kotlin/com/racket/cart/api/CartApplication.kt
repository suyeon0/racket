package com.racket.cart.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@ServletComponentScan
@SpringBootApplication(
	scanBasePackages = [
		"com.racket.cart",
		"com.racket.api.shared",
		"com.racket.shared",
		"com.racket.core.config"
	])
class CartApplication {
}

fun main(args: Array<String>) {
	runApplication<CartApplication>(*args)
}