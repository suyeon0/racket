package com.racket.api.auth

import com.racket.api.auth.response.LoginUserResponseView
import com.racket.api.auth.session.SessionManager
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("/auth")
class AuthController(
    private val loginService: LoginService,
    private val sessionManager: SessionManager
) {

    @GetMapping("/login")
    fun loginForm(): String = "loginForm"

    @ResponseBody
    @PostMapping("/login")
    fun login(
        response: HttpServletResponse,
        request: HttpServletRequest,
        @RequestParam email: String,
        @RequestParam password: String
    ): LoginUserResponseView {
        val loginResponse = this.loginService.login(inputEmail = email, inputPassword = password)

        // 로그인 성공시 세션 설정 및 리다이렉트
        if (loginResponse.result == "SUCCESS") {
            this.sessionManager.createSession(value = loginResponse.user!!, response = response, request = request)
            loginResponse.redirectURI = "/"
        }
        return loginResponse
    }

    @GetMapping("/logout")
    fun logout(request: HttpServletRequest): String {
        this.sessionManager.expire(request)
        return "redirect:/"
    }


}