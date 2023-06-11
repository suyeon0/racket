package com.racket.view.auth.login

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("/view/auth")
class LoginViewController {

    @GetMapping("/login")
    fun loginForm(): String = "loginForm"

    @GetMapping("/logout")
    fun logout(request: HttpServletRequest): String {
        request.getSession(false)?.invalidate()
        return "redirect:/"
    }

    @GetMapping("/user-info")
    fun userInfo(): String {
        return "userInfo"
    }

}