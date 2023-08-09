package com.racket.cache.infrastructure.client

import com.racket.api.shared.request.MessageRequest
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(name = "producer", url = "\${client.serviceUrl.producer}")
interface ProducerApiClient {

    /**
     * Kafka producer api call.
     */
    @PostMapping
    fun sendMessage(messageRequest: MessageRequest): ResponseEntity<String>
}