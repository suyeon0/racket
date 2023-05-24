package com.racket.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession

@SpringBootApplication
@EnableRedisHttpSession
class ApiInterface {
}

fun main(args: Array<String>) {
	runApplication<ApiInterface>(*args)
}