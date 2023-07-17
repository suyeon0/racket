package com.racket.api.user.response

import com.racket.api.user.enums.UserRoleType
import com.racket.api.user.enums.UserStatusType
import io.swagger.v3.oas.annotations.media.Schema

data class UserResponseView(
    @Schema(example = "10")
    val id: Long,

    @Schema(example = "suyeon")
    val userName: String,

    @Schema(example = "suyeon@naver.com")
    val email: String,

    @Schema(example = "ACTIVE")
    val status: UserStatusType,

    @Schema(example = "USER")
    val role: UserRoleType,

    val password: String
)
