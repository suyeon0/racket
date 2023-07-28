package com.racket.api.user.vo

data class UserSignedUpEventVO(
    val userId: Long,
    val userName: String,
    val userEmail: String,
    val userMobileNumber: String
)