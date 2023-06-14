package com.racket.api.auth.login.session

import com.racket.api.auth.login.exception.NoSuchSessionException
import com.racket.api.auth.login.session.domain.SessionRedisRepository
import com.racket.api.auth.login.session.domain.SessionUser
import com.racket.api.auth.login.session.vo.SessionVO
import com.racket.api.common.file.logger
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
@RequiredArgsConstructor
class SessionRedisManager(
    private val sessionRedisRepository: SessionRedisRepository
) {

    companion object {
        private const val SESSION_COOKIE = "SessionID"
    }

    private val logger = logger()

    fun setSession(sessionVO: SessionVO): String {
        val sessionId = UUID.randomUUID().toString()
        logger.info(">>>>>>>>>>>>>>>> setSession : $sessionId")
        val session = this.sessionRedisRepository.save(
            SessionUser(
                sessionId = sessionId,
                expireTime = LocalDateTime.now().plusMinutes(10),
                userId = sessionVO.userId,
                name = sessionVO.name,
                role = sessionVO.role,
                email = sessionVO.email
            )
        )
        return session.sessionId
    }

    fun getSession(sessionId: String): SessionVO {
        val savedSession = this.sessionRedisRepository.findById(sessionId).orElseThrow { NoSuchSessionException() }
        if (this.isInValidSession(savedSession)) {
            this.sessionRedisRepository.delete(savedSession)
            throw NoSuchSessionException()
        }
        return SessionVO(
            userId = savedSession.userId,
            name = savedSession.name,
            role = savedSession.role,
            email = savedSession.email
        )
    }

    private fun isInValidSession(session: SessionUser) = session.expireTime.isBefore(LocalDateTime.now())

    fun invalidSession(sessionId: String) {
        this.sessionRedisRepository.deleteById(sessionId)
    }

    /**
     * 세션 쿠키 세팅
     */
    fun setSessionCookie(sessionId: String, response: HttpServletResponse) {
        val sessionCookie = Cookie(SESSION_COOKIE, sessionId)
        sessionCookie.path = "/"
        sessionCookie.maxAge = 24 * 60 * 60
        response.addCookie(sessionCookie)
    }

    /**
     * 쿠키의 sessionID로 세션 조회
     */
    fun getSessionBySessionCookie(request: HttpServletRequest): SessionVO? {
        val sessionCookie = this.findCookie(request = request)
        return if(sessionCookie.isPresent) {
            this.getSession(sessionId = sessionCookie.get().value)
        } else {
            null
        }
    }

    private fun findCookie(request: HttpServletRequest): Optional<Cookie> {
        return if(request.cookies != null) {
            Arrays.stream(request.cookies)
                .filter { cookie -> cookie.name.equals(SESSION_COOKIE) }
                .findAny()
        } else {
            Optional.empty()
        }
    }

    fun deleteSessionCookie(response: HttpServletResponse, request: HttpServletRequest) {
        val sessionCookie = this.findCookie(request).get()
        sessionCookie.maxAge = 0
        response.addCookie(sessionCookie)
    }
}