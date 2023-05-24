package com.racket.api.auth.vo

import java.io.Serializable

data class SessionUser(
    val id: Long,
    val name: String,
    val role: String
) : Serializable