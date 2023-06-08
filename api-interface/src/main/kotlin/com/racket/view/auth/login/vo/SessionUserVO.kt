package com.racket.view.auth.login.vo

import java.io.Serializable

data class SessionUserVO(
    val id: Long,
    var sessionId: String?,
    val name: String,
    val role: String
) : Serializable