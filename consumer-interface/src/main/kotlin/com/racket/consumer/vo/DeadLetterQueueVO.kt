package com.racket.consumer.vo

import com.racket.consumer.enums.DeadLetterType
import com.racket.consumer.enums.EventType

data class DeadLetterQueueVO(
    val deadLetterType: DeadLetterType,
    val failureTopic: String,
    val eventType: EventType,
    val key: String,
    val payload: String,
    val errorMessage: String
)