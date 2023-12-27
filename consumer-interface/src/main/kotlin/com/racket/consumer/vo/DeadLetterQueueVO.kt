package com.racket.consumer.vo

import com.racket.consumer.enums.DeadLetterType
import com.racket.consumer.enums.EventType
import java.time.Instant

data class DeadLetterQueueVO(
    val deadLetterType: DeadLetterType,
    val originEventTimestamp: Instant,
    val failureTopic: String,
    val eventType: EventType,
    val key: String,
    val payload: String,
    val errorMessage: String
)