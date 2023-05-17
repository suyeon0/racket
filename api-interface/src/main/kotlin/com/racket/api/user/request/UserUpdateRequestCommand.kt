package com.racket.api.user.request

import com.racket.api.user.domain.Address
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class UserUpdateRequestCommand(
    @field:NotBlank(message = "userName cannot be empty")
    val userName: String,

    @field:Size(min = 6, max = 20, message = "password format is invalid")
    val password: String,

    @field:NotBlank(message = "mobile cannot be empty")
    val mobile: String,

    @Valid
    val address: Address
) {
}