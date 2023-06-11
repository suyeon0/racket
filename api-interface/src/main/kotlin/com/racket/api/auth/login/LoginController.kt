package com.racket.api.auth.login

import com.racket.api.auth.login.enums.SessionType
import com.racket.api.auth.login.request.LoginRequest
import com.racket.api.auth.login.response.LoginUserResponseView
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/auth")
class LoginController(
    private val loginService: LoginService
) {

    @ResponseBody
    @PostMapping("/login")
    fun login(
        request: HttpServletRequest,
        @RequestBody loginRequest: LoginRequest
    ): LoginUserResponseView {
        val loginResponse = this.loginService.login(inputEmail = loginRequest.email, inputPassword = loginRequest.password)

        // 로그인 성공 처리
        // 세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성 -> 로그인 회원 정보 보관
        request.getSession(true).setAttribute(SessionType.LOGIN_USER.key, loginResponse.user)
        return loginResponse
    }

}