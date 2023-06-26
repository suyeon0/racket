package com.racket.api.auth.login

import com.racket.api.auth.login.request.LoginRequestCommand
import com.racket.api.auth.login.response.LoginUserResponseView
import com.racket.api.auth.login.session.SessionRedisManager
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
class LoginController(
    private val loginService: LoginService,
    private val sessionRedisManager: SessionRedisManager
) {

    @ResponseBody
    @PostMapping("/login")
    fun login(
        response: HttpServletResponse,
        request: HttpServletRequest,
        @RequestBody loginRequestCommand: LoginRequestCommand
    ): LoginUserResponseView {
        val loginResponse = this.loginService.login(inputEmail = loginRequestCommand.email, inputPassword = loginRequestCommand.password)
        val sessionId = this.sessionRedisManager.setSession(sessionVO = loginResponse.user)
        this.sessionRedisManager.setSessionCookie(sessionId = sessionId, response = response)

        return loginResponse
    }

}