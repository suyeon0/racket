package com.racket.cash.consume.vo

data class DeadLetterQueueVO(
    val payload: String,
    val errorMessage: String
)