package com.racket.cash.consume.vo

data class DeadLetterQueueVO(
    val topic: String,
    val key: String?,
    val payload: Any?,
    val errorMessage: String?
)