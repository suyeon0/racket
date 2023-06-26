package com.racket.view.home

import com.racket.api.auth.login.exception.NoSuchSessionException
import com.racket.api.auth.login.session.SessionManager
import com.racket.api.auth.login.session.SessionRedisManager
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
    private val sessionRedisManager: SessionManager
) {
    @GetMapping("/")
    fun home(request: HttpServletRequest, model: Model): String {

        if (this.sessionRedisManager.isExistSessionCookie(request)) {
            try {
                val session = this.sessionRedisManager.getSessionBySessionCookie(request)
                model.addAttribute("userId", session.userId)
            } catch (_: NoSuchSessionException) {
            }
        }

        return "home"
    }
}