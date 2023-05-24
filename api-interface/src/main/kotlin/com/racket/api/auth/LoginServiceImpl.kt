package com.racket.api.auth

import com.racket.api.auth.response.LoginUserResponseView
import com.racket.api.auth.vo.SessionUser
import com.racket.api.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class LoginServiceImpl: LoginService {

    @Autowired lateinit var userService: UserService

    override fun login(inputEmail: String, inputPassword: String): LoginUserResponseView {
        return if (this.isValidateLoginRequest(inputEmail = inputEmail, inputPassword = inputPassword)) {
            val user = this.userService.getUserByEmail(inputEmail)
            LoginUserResponseView(
                result = "SUCCESS",
                user = SessionUser(id = user.id, name = user.userName, role = user.role.name),
                redirectURI = null
            )
        } else {
            LoginUserResponseView(result = "FAIL", null, null)
        }
    }

    private fun isValidateLoginRequest(inputEmail: String, inputPassword: String): Boolean
        = this.userService.getUserByEmail(inputEmail).password == inputPassword

}