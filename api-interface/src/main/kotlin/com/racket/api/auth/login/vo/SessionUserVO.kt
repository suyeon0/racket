package com.racket.api.auth.login.vo

import java.io.Serializable

data class SessionUserVO(
    val id: Long,
    val name: String,
    val role: String
) : Serializable