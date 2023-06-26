package com.racket

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan

@ServletComponentScan
@SpringBootApplication
class ApiInterface {
}

fun main(args: Array<String>) {
	runApplication<ApiInterface>(*args)
}