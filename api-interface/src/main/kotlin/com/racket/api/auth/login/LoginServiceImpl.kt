package com.racket.api.auth.login

import com.racket.api.auth.login.response.LoginUserResponseView
import com.racket.api.auth.login.vo.SessionUserVO
import com.racket.api.user.UserService
import com.racket.api.auth.login.exception.LoginFailException
import com.racket.api.user.domain.User
import org.springframework.stereotype.Service

@Service
class LoginServiceImpl(
    private val userService: UserService
) : LoginService {

    override fun login(inputEmail: String, inputPassword: String): LoginUserResponseView {
        val loginUser = this.getLoginUserByLoginRequest(inputEmail = inputEmail, inputPassword = inputPassword)
        return LoginUserResponseView(
            user = SessionUserVO(
                id = loginUser.id!!,
                name = loginUser.userName,
                role = loginUser.role.name
            ),
            redirectURI = "/"
        )
    }

    fun getLoginUserByLoginRequest(inputEmail: String, inputPassword: String): User =
        this.userService.getUserByEmailAndPassword(email = inputEmail, password = inputPassword)
            .orElseThrow { LoginFailException() }

}


