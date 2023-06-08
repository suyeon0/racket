package com.racket.view.auth.login

import com.racket.view.auth.login.response.LoginUserResponseView
import com.racket.view.auth.login.vo.SessionUserVO
import com.racket.api.user.UserService
import com.racket.view.auth.login.exception.LoginFailException
import org.springframework.stereotype.Service

@Service
class LoginServiceImpl(
    private val userService: UserService
) : LoginService {

    override fun login(inputEmail: String, inputPassword: String): LoginUserResponseView {
        return if (this.isValidateLoginRequest(inputEmail = inputEmail, inputPassword = inputPassword)) {
            val user = this.userService.getUserByEmail(inputEmail)
            LoginUserResponseView(
                user = SessionUserVO(
                    id = user.id,
                    name = user.userName,
                    role = user.role.name,
                    sessionId = null
                ),
                redirectURI = "/"
            )
        } else {
            throw LoginFailException()
        }
    }

    private fun isValidateLoginRequest(inputEmail: String, inputPassword: String): Boolean =
        this.userService.getUserByEmail(inputEmail).password == inputPassword

}