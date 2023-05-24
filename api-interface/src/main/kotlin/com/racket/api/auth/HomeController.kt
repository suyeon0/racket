package com.racket.api.auth

import com.racket.api.auth.enums.SessionConst
import com.racket.api.auth.vo.SessionUser
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.util.*
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping
class HomeController() {
    @GetMapping("/")
    fun home(request: HttpServletRequest, model: Model): String {
        val session = request.getSession(false)
        if(session != null) {
            val loginUserSession = session.getAttribute(SessionConst.LOGIN_USER.key) as SessionUser
            model.addAttribute("loginUser", loginUserSession)
        }
        return "home"
    }
}