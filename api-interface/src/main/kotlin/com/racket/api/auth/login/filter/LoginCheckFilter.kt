package com.racket.api.auth.login.filter

import com.racket.api.common.file.logger
import org.springframework.util.PatternMatchUtils
import java.io.IOException
import javax.servlet.*
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebFilter(urlPatterns = ["/*"])
class LoginCheckFilter: Filter {

    companion object {
        val excludeList = arrayOf("/", "/view/auth/logout", "/view/auth/login", "/api/auth/login", "/resources/*")
        val log = logger()
    }

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
            if (isLoginCheckPath(requestURI)) {
                if(httpRequest.getSession(false) == null) {
                    httpResponse.sendRedirect("/view/auth/login");
                    return
                }
            }
            chain.doFilter(request, response)
        } catch (e: Exception) {
            throw e
        }
    }

    private fun isLoginCheckPath(requestURI: String): Boolean {
       return !PatternMatchUtils.simpleMatch(excludeList, requestURI)
    }

}