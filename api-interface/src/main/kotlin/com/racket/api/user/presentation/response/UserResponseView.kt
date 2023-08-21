package com.racket.api.user.presentation.response

import com.racket.share.domain.user.enums.UserRoleType
import com.racket.share.domain.user.enums.UserStatusType
import com.racket.share.vo.MobileVO
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

    val password: String,

    @Schema(example = "01012341234")
    val mobile: MobileVO
)
