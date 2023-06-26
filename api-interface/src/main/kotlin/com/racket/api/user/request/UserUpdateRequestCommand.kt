package com.racket.api.user.request

data class UserUpdateRequestCommand(
    val userName: String,

    val password: String
) {
    fun validate() {
        if (this.userName.isEmpty()) {
            throw IllegalArgumentException("userName cannot be empty")
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