package com.racket.view.auth.login

import com.racket.view.auth.login.response.LoginUserResponseView

interface LoginService {

    fun login(inputEmail: String, inputPassword: String): LoginUserResponseView
}