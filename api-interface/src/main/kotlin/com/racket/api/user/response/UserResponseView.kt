package com.racket.api.user.response

import com.racket.api.user.domain.Address
import com.racket.api.user.domain.UserGrade
import com.racket.api.user.domain.UserStatus

data class UserResponseView(
    val id: Long,
    val userName: String,
    val email: String,
    val mobile: String,
    val address: Address,
    val status: UserStatus,
    val grade: UserGrade
)
