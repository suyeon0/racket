package com.racket.api.common.response

data class Error(
    val message: String,
    val field: String = ""
) {
    companion object {
        fun from(message: String): Error {
            return Error(message = message)
        }

        fun from(message: String, field: String): Error {
            return Error(message = message, field = field)
        }
    }
}