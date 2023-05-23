package com.racket.api.auth

import com.racket.api.auth.response.LoginUserResponseView

interface LoginService {

    fun login(inputEmail: String, inputPassword: String): LoginUserResponseView
}