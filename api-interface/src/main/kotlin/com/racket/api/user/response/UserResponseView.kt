package com.racket.api.user.response

import com.racket.api.user.enums.UserRoleType
import com.racket.api.user.enums.UserStatusType

data class UserResponseView(
    val id: Long,
    val userName: String,
    val email: String,
    val status: UserStatusType,
    val role: UserRoleType,
    val password: String
)
