package com.racket.view.auth.login

import com.racket.api.auth.login.session.SessionManager
import lombok.RequiredArgsConstructor
import mu.KotlinLogging
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
@RequiredArgsConstructor
@RequestMapping("/view/auth")
class LoginViewController(private val sessionRedisManager: SessionManager) {

    private val log = KotlinLogging.logger { }

    @GetMapping("/login")
    fun loginForm(): String = "loginForm"

    @GetMapping("/logout")
    fun logout(request: HttpServletRequest, response: HttpServletResponse): String {
        this.sessionRedisManager.deleteSessionCookie(request = request, response = response)
        this.sessionRedisManager.invalidSession(this.sessionRedisManager.findSessionCookie(request).get().value)
        return "redirect:/"
    }

    @GetMapping("/user-info")
    fun userInfo(): String {
        return "userInfo"
    }

}