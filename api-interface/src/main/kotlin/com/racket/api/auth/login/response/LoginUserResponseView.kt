package com.racket.api.auth.login.response

import com.racket.api.auth.login.vo.SessionUserVO

data class LoginUserResponseView(
    val user: SessionUserVO,
    var redirectURI: String?
) {

}