package com.racket.api.auth.response

import com.racket.api.auth.vo.User

data class LoginUserResponseView(
    val result: String,
    val user: User?,
    var redirectURI: String?
) {

}