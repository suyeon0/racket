package com.racket.cash.consume.service.consume

import com.racket.cash.consume.const.DeadLetterType
import com.racket.cash.consume.domain.CashFailLog
import com.racket.cash.consume.domain.CashFailLogRepository
import com.racket.cash.consume.vo.DeadLetterQueueVO
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.Consumer
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service

@Service
class CashDeadLetterQueueConsumerService(
    private val cashFailLogRepository: CashFailLogRepository
) {

    private val log = KotlinLogging.logger { }

    @KafkaListener(
        topics = [DeadLetterType.INSERT_DB], groupId = "cash_dlq" , containerFactory = "kafkaDLQListenerContainerFactory"
    )
    fun consumeInsertDBProcess(
        @Payload value: DeadLetterQueueVO,
        consumer: Consumer<String, DeadLetterQueueVO>
    ) {
        this.cashFailLogRepository.save(
            CashFailLog(
                payload = value.payload,
                errorMessage = value.errorMessage
            )
        )
    }

    @KafkaListener(
        topics = [DeadLetterType.RETRY], groupId = "cash_dlq", containerFactory = "kafkaDLQListenerContainerFactory"
    )
    fun consumeRetryProcess(
        @Payload message: String,
        consumer: Consumer<String, String>
    ) {

    }

    @KafkaListener(
        topics = [DeadLetterType.REQUEST_ADMIN], groupId = "cash_dlq"//, containerFactory = "kafkaDLQListenerContainerFactory"
    )
    fun consumeRequestAdminProcess(
        @Payload message: String,
        consumer: Consumer<String, String>
    ) {
        log.info { "send to admin" }
    }
}

