package com.racket.api.auth.login.filter

import com.racket.api.auth.login.exception.NoSuchSessionException
import com.racket.api.auth.login.session.SessionRedisManager
import com.racket.api.common.file.logger
import org.springframework.stereotype.Component
import org.springframework.util.PatternMatchUtils
import java.io.IOException
import javax.servlet.*
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebFilter(urlPatterns = ["/*"])
class LoginCheckFilter(
    private val sessionRedisManager: SessionRedisManager
) : Filter {

    companion object {
        val excludeList = arrayOf(
            "/",
            "/view/auth/logout",
            "/view/auth/login",
            "/api/auth/login",
            "/css/*", "/js/*"
        )
    }

    private val log = logger()

    override fun init(filterConfig: FilterConfig?) {
        super.init(filterConfig)
        log.info("==============filter init==============")
    }


    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = request as HttpServletRequest
        val requestURI = httpRequest.requestURI
        val httpResponse = response as HttpServletResponse

        try {
            if (this.isLoginCheckPath(requestURI)) {
                this.sessionRedisManager.getSessionBySessionCookie(request)
            }
            chain.doFilter(request, response)

        } catch (e: NoSuchSessionException) {
            httpResponse.sendRedirect("/view/auth/login");
            return

        } catch (e: Exception) {
            throw e
        }
    }

    private fun isLoginCheckPath(requestURI: String): Boolean {
        return !PatternMatchUtils.simpleMatch(excludeList, requestURI)
    }

}