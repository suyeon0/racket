package com.racket.api.auth

import com.racket.api.auth.enums.SessionConst
import com.racket.api.auth.response.LoginUserResponseView
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("/auth")
class AuthController(
    private val loginService: LoginService
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

        // 로그인 성공 처리
        // 세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성 -> 로그인 회원 정보 보관
        if (loginResponse.result == "SUCCESS") {
            request.getSession(true).setAttribute(SessionConst.LOGIN_USER.key, loginResponse.user)
            loginResponse.redirectURI = "/"
        }
        return loginResponse
    }

    @GetMapping("/logout")
    fun logout(request: HttpServletRequest): String {
        request.getSession(false)?.invalidate()
        return "redirect:/"
    }


}