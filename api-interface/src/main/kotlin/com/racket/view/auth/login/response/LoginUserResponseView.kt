package com.racket.view.auth.login.response

import com.racket.view.auth.login.vo.SessionUserVO

data class LoginUserResponseView(
    val user: SessionUserVO,
    var redirectURI: String?
) {

}