package com.racket.cash.consume.service.consume

import mu.KotlinLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.annotation.RetryableTopic
import org.springframework.stereotype.Service

@Service
class CashDeadLetterQueueConsumerService {

    private val log = KotlinLogging.logger { }

//    @RetryableTopic
//    @KafkaListener(
//        topics = ["CHARGING-dlt"], groupId = "CHARGING-dlt"
//    )
//    fun consumeChargingDeadLetterQueue(message: String) {
//        log.info {
//            "[CHARGING-dlt] Consume ----${message}"
//            "TODO :::: 실패 로그 DB Insert!"
//        }
//
//    }
}