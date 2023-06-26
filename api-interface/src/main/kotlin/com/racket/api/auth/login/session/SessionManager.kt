package com.racket.api.auth.login.session

import com.racket.api.auth.login.session.vo.SessionVO
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

interface SessionManager {

    companion object {
        private const val SESSION_COOKIE = "SessionID"
    }

    fun setSession(sessionVO: SessionVO): String

    fun getSession(sessionId: String): SessionVO

    fun invalidSession(sessionId: String)

    fun findSessionCookie(request: HttpServletRequest): Optional<Cookie> {
        return if(request.cookies != null) {
            Arrays.stream(request.cookies)
                .filter { cookie -> cookie.name.equals(SessionManager.SESSION_COOKIE) }
                .findAny()
        } else {
            Optional.empty()
        }
    }

    fun isExistSessionCookie(request: HttpServletRequest): Boolean
    fun getSessionBySessionCookie(request: HttpServletRequest): SessionVO

    fun deleteSessionCookie(response: HttpServletResponse, request: HttpServletRequest)

}