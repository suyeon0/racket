package com.racket.api.auth.login

import com.racket.api.auth.login.presentation.response.LoginUserResponseView

interface LoginService {

    fun login(inputEmail: String, inputPassword: String): LoginUserResponseView
}