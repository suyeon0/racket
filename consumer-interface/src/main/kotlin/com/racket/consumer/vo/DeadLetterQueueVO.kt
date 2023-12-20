package com.racket.consumer.vo

import com.racket.consumer.enums.DeadLetterType

data class DeadLetterQueueVO(
    val deadLetterType: DeadLetterType,
    val failureTopic: String,
    val key: String,
    val payload: String,
    val errorMessage: String
)