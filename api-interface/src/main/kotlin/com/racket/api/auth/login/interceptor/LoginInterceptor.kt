package com.racket.api.auth.login.interceptor

import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class LoginInterceptor: HandlerInterceptor {

    @Throws(Exception::class)
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if(request.getSession(false) == null) {
            response.sendRedirect("/view/auth/login");
            return false;
        }
        return true;
    }
}
