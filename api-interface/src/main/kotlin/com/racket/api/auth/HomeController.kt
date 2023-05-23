package com.racket.api.auth

import com.racket.api.auth.session.SessionManager
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.util.*
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping
class HomeController(
    private val sessionManager: SessionManager
) {
    @GetMapping("/")
    fun home(request: HttpServletRequest, model: Model): String {
        val loginUserSession = this.sessionManager.getSession(request)
        if(loginUserSession != null) {
            model.addAttribute("loginUser", loginUserSession)
        }
        return "home"
    }
}