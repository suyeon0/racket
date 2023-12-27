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

    fun register(deadLetterQueueVO: DeadLetterQueueVO) {
        this.failedEventRepository.save(
            FailedEventEntity(
                id = ObjectId().toHexString(),
                originEventTimestamp = deadLetterQueueVO.originEventTimestamp,
                topic = deadLetterQueueVO.failureTopic,
                key = deadLetterQueueVO.key,
                payload = deadLetterQueueVO.payload,
                createdAt = Instant.now(),
                updatedAt = Instant.now(),
                isProcessed = false
            )
        )
        log.info { "failed event register. [$deadLetterQueueVO]" }
    }


}