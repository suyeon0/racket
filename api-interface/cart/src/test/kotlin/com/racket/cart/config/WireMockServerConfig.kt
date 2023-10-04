package com.racket.cart.config

import com.github.tomakehurst.wiremock.WireMockServer
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean

@TestConfiguration
class WireMockServerConfig: ApplicationListener<ApplicationReadyEvent> {
    @Bean
    fun productWireMockServer(): WireMockServer = WireMockServer(18081)

    // TODO: @PostConstruct 차이점
    override fun onApplicationEvent(event: ApplicationReadyEvent) =
        productWireMockServer().start()
}