package com.racket.api.user.response

import com.racket.api.user.domain.UserRole
import com.racket.api.user.domain.UserStatus

data class UserResponseView(
    val id: Long,
    val userName: String,
    val email: String,
    val status: UserStatus,
    val role: UserRole,
    val password: String
)
