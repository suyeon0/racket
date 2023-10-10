package com.racket.delivery.adapter.out.kafka

import com.racket.delivery.domain.DeliveryApiLog
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class DeliveryApiLogProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>
) {

    companion object {
        private const val topic: String = "delivery-log"
    }

    fun produce(@Payload payload: DeliveryApiLog) {
        this.kafkaTemplate.send(record)
    }
}