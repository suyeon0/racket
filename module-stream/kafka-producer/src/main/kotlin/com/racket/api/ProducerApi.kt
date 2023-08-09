package com.racket.api

import com.racket.api.request.MessageRequest
import com.racket.service.ProduceService
import mu.KotlinLogging
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/produce/v1")
class ProducerApi (
    private val produceService: ProduceService
) {

    @PostMapping
    fun sendMessage(@RequestBody messageRequest: MessageRequest) {
        this.produceService.send(
            topic = messageRequest.topic,
            message = messageRequest.message
        )
    }

}