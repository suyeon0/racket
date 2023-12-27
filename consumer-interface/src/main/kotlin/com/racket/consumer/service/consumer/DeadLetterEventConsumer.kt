package com.racket.consumer.service.consumer

import com.racket.consumer.enums.EventType
import com.racket.consumer.service.FailedEventService
import com.racket.consumer.service.component.CashRetryComponent
import com.racket.consumer.service.component.DeliveryRetryComponent
import com.racket.consumer.vo.DeadLetterQueueVO
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.Consumer
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service

@Service
class DeadLetterEventConsumer(
    private val failedEventService: FailedEventService,
    private val cashRetryComponent: CashRetryComponent,
    private val deliveryRetryComponent: DeliveryRetryComponent
) {
    @KafkaListener(
        topics = ["dlq"], groupId = "racket", containerFactory = "kafkaDLQListenerContainerFactory"
    )
    fun consumeDeadLetterEvent(
        @Payload deadLetter: DeadLetterQueueVO,
        consumer: Consumer<String, DeadLetterQueueVO>
    ) {
        try {
            when(deadLetter.eventType) {
                EventType.CASH -> this.cashRetryComponent.handle(message = deadLetter.payload)
                EventType.DELIVERY -> {
                    println()
                }
            }

        } catch (e: Exception) {
            this.failedEventService.register(deadLetter)
            throw e
        } finally {
            consumer.commitAsync()
        }
    }

    private val log = KotlinLogging.logger { }
}