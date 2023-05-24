package com.racket.api.auth.response

import com.racket.api.auth.vo.SessionUser

data class LoginUserResponseView(
    val result: String,
    val user: SessionUser?,
    var redirectURI: String?
) {

}