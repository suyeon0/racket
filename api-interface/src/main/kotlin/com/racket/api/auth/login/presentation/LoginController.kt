package com.racket.api.auth.login.presentation

import com.racket.api.auth.login.LoginService
import com.racket.api.auth.login.presentation.request.LoginRequestCommand
import com.racket.api.auth.login.presentation.response.LoginUserResponseView
import com.racket.api.auth.login.session.SessionRedisManager
import io.swagger.v3.oas.annotations.Hidden
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
class LoginController(
    private val loginService: LoginService,
    private val sessionRedisManager: SessionRedisManager
) {

    @Hidden
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