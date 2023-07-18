package com.racket.api.user.request

import io.swagger.v3.oas.annotations.media.Schema

data class UserCreateRequestCommand(
    @Schema(required = true)
    val userName: String,

    @Schema(required = true)
    val email: String,

    @Schema(required = true)
    val password: String
) {
    fun validate() {
        if (this.userName.isEmpty()) {
            throw IllegalArgumentException("userName cannot be empty")
        }
        if (this.email.isEmpty()) {
            throw IllegalArgumentException("email cannot be empty")
        }
        if (this.password.isEmpty()) {
            throw IllegalArgumentException("password cannot be empty")
        } else {
            if (this.password.length < 6 || this.password.length > 20) {
                throw IllegalArgumentException("password length must be between 6 and 20")
            }
        }
    }
}
