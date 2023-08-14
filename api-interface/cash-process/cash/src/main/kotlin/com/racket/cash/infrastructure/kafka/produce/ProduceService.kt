package com.racket.cash.infrastructure.kafka.produce

interface ProduceService {

    fun send(topic: String, message: String)

}