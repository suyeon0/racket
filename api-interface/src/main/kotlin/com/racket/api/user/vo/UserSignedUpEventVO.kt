package com.racket.api.user.vo

data class UserSignedUpEventVO(
    var retryCount: Int = 0,
    val userId: Long,
    val userName: String,
    val userEmail: String
)