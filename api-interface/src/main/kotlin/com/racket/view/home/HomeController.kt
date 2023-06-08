package com.racket.view.home

import com.racket.view.auth.enums.SessionType
import com.racket.view.auth.login.vo.SessionUserVO
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.util.*
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping
class HomeController {
    @GetMapping("/")
    fun home(request: HttpServletRequest, model: Model): String {
        val session = request.getSession(false)
        if(session != null) {
            val loginUserSession = session.getAttribute(SessionType.LOGIN_USER.key) as SessionUserVO
            loginUserSession.sessionId = session.id
            model.addAttribute("loginUser", loginUserSession)
        }
        return "home"
    }
}