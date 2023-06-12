package com.racket

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession

@ServletComponentScan
@SpringBootApplication
@EnableRedisHttpSession
class ApiInterface {
}

fun main(args: Array<String>) {
	runApplication<ApiInterface>(*args)
}