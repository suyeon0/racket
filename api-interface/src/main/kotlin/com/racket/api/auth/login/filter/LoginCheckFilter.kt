package com.racket.api.auth.login.filter

import com.racket.api.auth.login.exception.NoSuchSessionException
import com.racket.api.auth.login.session.SessionManager
import org.springframework.util.PatternMatchUtils
import javax.servlet.*
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.filter.OncePerRequestFilter

@WebFilter(urlPatterns = ["/*"])
class LoginCheckFilter(
    private val sessionRedisManager: SessionManager
) : OncePerRequestFilter() {

    private val log = KotlinLogging.logger { }

    companion object {
        val excludeList = arrayOf(
            "/",
            "/view/auth/logout",
            "/view/auth/login",
            "/api/auth/login",
            "/css/*", "/js/*", "/favicon.ico"
        )
    }

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        log.info { "init done" }

        val requestURI = request.requestURI

        try {
            if (this.isLoginCheckPath(requestURI)) {
                this.sessionRedisManager.getSessionBySessionCookie(request)
            }
            filterChain.doFilter(request, response)

        } catch (e: NoSuchSessionException) {
            // response status
            response.status = HttpStatus.UNAUTHORIZED.value()

            if (this.isApiRequest(requestURI)) {
                log.info { "api request - UNAUTHORIZED" }
                // response body
                response.writer.write(e.message)

            } else {
                log.info { "view request - UNAUTHORIZED" }
                response.sendRedirect("/view/auth/login");
            }

        } catch (e: Exception) {
            throw e
        }
    }

    private fun isApiRequest(requestURI: String) = requestURI.startsWith("/api")

    private fun isLoginCheckPath(requestURI: String): Boolean {
        return !PatternMatchUtils.simpleMatch(excludeList, requestURI)
    }

}