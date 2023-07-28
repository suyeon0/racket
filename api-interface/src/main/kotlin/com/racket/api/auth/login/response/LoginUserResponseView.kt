package com.racket.api.auth.login.response

import com.racket.api.auth.login.session.vo.SessionVO

data class LoginUserResponseView(
    val user: SessionVO,
    var sessionId: String?,
    var redirectURI: String?
)