package com.racket.consumer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableAsync

@EntityScan(basePackages = ["com.racket.consumer"])
@EnableJpaRepositories(basePackages = ["com.racket.consumer"])
@SpringBootApplication(scanBasePackages = ["com.racket.consumer"])
class ConsumerInterface {
}

fun main(args: Array<String>) {
	runApplication<ConsumerInterface>(*args)
}