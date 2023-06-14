package com.racket.view.auth.login

import com.racket.api.auth.login.session.SessionRedisManager
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
@RequiredArgsConstructor
@RequestMapping("/view/auth")
class LoginViewController(private val sessionRedisManager: SessionRedisManager) {

    @GetMapping("/login")
    fun loginForm(): String = "loginForm"

    @GetMapping("/logout")
    fun logout(request: HttpServletRequest, response: HttpServletResponse): String {
        this.sessionRedisManager.deleteSessionCookie(request = request, response = response)
        //this.sessionRedisManager.invalidSession(request.session.id)
        return "redirect:/"
    }

    @GetMapping("/user-info")
    fun userInfo(): String {
        return "userInfo"
    }

}