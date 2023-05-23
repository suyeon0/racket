package com.racket.api.auth.session

import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class SessionManager {

    private val sessionStorage = ConcurrentHashMap<String, Any>();
    companion object {
        private const val SESSION_COOKIE_NAME = "SessionID"
    }

    /**
     * 세션 생성
     */
    fun createSession(value: Any, response: HttpServletResponse, request: HttpServletRequest) {
        val sessionIdValue = UUID.randomUUID().toString()
        sessionStorage[sessionIdValue] = value

        this.createSessionCookie(sessionIdValue = sessionIdValue, response = response, request = request)
    }

    /**
     * 쿠키 sessionID 세팅
     */
    private fun createSessionCookie(sessionIdValue: String, response: HttpServletResponse, request: HttpServletRequest) {
        this.expireExistCookie(request)

        val sessionCookie = Cookie(SESSION_COOKIE_NAME, sessionIdValue)
        sessionCookie.path = "/"
        sessionCookie.maxAge = 24 * 60
        response.addCookie(sessionCookie)
    }

    private fun expireExistCookie(request: HttpServletRequest){
        val existCookie = this.findCookie(request, SESSION_COOKIE_NAME)
        if(existCookie != null) {
            existCookie.maxAge = 0
        }
    }

    /**
     * 쿠키의 sessionID로 세션 조회
     */
    fun getSession(request: HttpServletRequest): Any? {
        val sessionId = this.findCookie(request = request, cookieName = SESSION_COOKIE_NAME)
        return if(sessionId == null) {
            null;
        } else {
            this.sessionStorage[sessionId.value]
        }
    }

    private fun findCookie(request: HttpServletRequest, cookieName: String): Cookie? {
        if(request.cookies == null) {
            return null
        }
        return Arrays.stream(request.cookies)
            .filter { cookie -> cookie.name.equals(cookieName) }
            .findAny()
            .orElse(null)
    }


    /**
     * 세션 만료
     */
    fun expire(request: HttpServletRequest) {
        val sessionCookie = this.findCookie(request = request, cookieName = SESSION_COOKIE_NAME)
        if(sessionCookie != null) {
            val sessionId = sessionCookie.value
            this.sessionStorage.remove(sessionId)
        }
    }

}
