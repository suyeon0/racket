package com.racket.consumer.service

import com.racket.consumer.domain.failure.FailedEventEntity
import com.racket.consumer.domain.failure.FailedEventRepository
import com.racket.consumer.vo.DeadLetterQueueVO
import mu.KotlinLogging
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class FailedEventService(
    private val failedEventRepository: FailedEventRepository
) {
    private val log = KotlinLogging.logger { }

    fun register(originEventTimestamp: Instant,
                 topic: String,
                 key: String,
                 payload: String
    ) {
        val id = ObjectId().toHexString()
        this.failedEventRepository.save(
            FailedEventEntity(
                id = id,
                originEventTimestamp = originEventTimestamp,
                topic = topic,
                key = key,
                payload = payload,
                createdAt = Instant.now(),
                updatedAt = Instant.now(),
                isProcessed = false
            )
        )
        log.info { "failed event register. [${topic}.${id}] : $payload" }
    }


}