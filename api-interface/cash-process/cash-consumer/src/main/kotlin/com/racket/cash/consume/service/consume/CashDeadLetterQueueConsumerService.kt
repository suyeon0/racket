package com.racket.cash.consume.service.consume

import mu.KotlinLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.annotation.RetryableTopic
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service

@Service
class CashDeadLetterQueueConsumerService {

    private val log = KotlinLogging.logger { }

    @RetryableTopic
    @KafkaListener(
        topics = ["charging-dlt"], groupId = "racket"
    )
    fun consumeChargingDeadLetterQueue(message: String, ack: Acknowledgment) {
        log.info { "!!!!!!!!!!!!!!!!!!" +  message}
        ack.acknowledge()
    }
}