package com.racket.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ApiInterface {
}

fun main(args: Array<String>) {
	runApplication<ApiInterface>(*args)
}