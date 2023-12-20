package com.racket.consumer.service.consumer

import com.racket.consumer.domain.dlq.DeadLetterEntity
import com.racket.consumer.domain.dlq.DeadLetterRepository
import com.racket.consumer.vo.DeadLetterQueueVO
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.Consumer
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class DeadLetterEventConsumer(
    private val deadLetterRepository: DeadLetterRepository
) {

    private val log = KotlinLogging.logger { }

    @KafkaListener(
        topics = ["dlq"], groupId = "racket", containerFactory = "kafkaDLQListenerContainerFactory"
    )
    fun consumeDeadLetterEvent(
        @Payload deadLetterQueueVO: DeadLetterQueueVO,
        consumer: Consumer<String, DeadLetterQueueVO>
    ) {
        try {
            // DeadLetterEntity 생성
            val deadLetterEntity = DeadLetterEntity(
                failureTopic = deadLetterQueueVO.failureTopic,
                key = deadLetterQueueVO.key,
                payload = deadLetterQueueVO.payload,
                createdAt = LocalDateTime.now(),
                isProcessed = false
            )
          //  val savedEntity = this.deadLetterRepository.save(deadLetterEntity)
            //log.info { "DeadLetterEntity saved with ID: ${savedEntity.id}, event: $deadLetterQueueVO" }
        } catch (e: Exception) {
            log.error("Error processing dead letter event: $deadLetterQueueVO", e)
        } finally {
            consumer.commitAsync()
        }
    }


}