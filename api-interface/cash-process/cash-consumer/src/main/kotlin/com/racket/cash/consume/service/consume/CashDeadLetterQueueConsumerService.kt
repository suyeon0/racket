package com.racket.cash.consume.service.consume

import mu.KotlinLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CashDeadLetterQueueConsumerService {

    private val log = KotlinLogging.logger { }

    @KafkaListener(
        topics = ["charging-dlt"], groupId = "racket"
    )
    @Transactional
    fun consumeChargingDeadLetterQueue(message: String) {
        log.info { "!!!!!!!!!!!!!!!!!!" +  message}
    }
}