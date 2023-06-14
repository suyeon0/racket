package com.racket.view.home

import com.racket.api.auth.login.enums.SessionType
import com.racket.api.auth.login.session.SessionRedisManager
import com.racket.api.auth.login.session.domain.SessionUser
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.util.*
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping
@RequiredArgsConstructor
class HomeController(
    private val sessionRedisManager: SessionRedisManager
) {
    @GetMapping("/")
    fun home(request: HttpServletRequest, model: Model): String {

        val session = this.sessionRedisManager.getSessionBySessionCookie(request)
        if (session != null) {
            model.addAttribute("userId", session.userId)
        }

        return "home"
    }
}