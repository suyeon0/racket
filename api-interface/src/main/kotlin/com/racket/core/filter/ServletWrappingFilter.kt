package com.racket.core.filter

import com.racket.shared.notification.exception.slack.RequestStorage
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class ServletWrappingFilter(
    private val requestStorage: RequestStorage
): OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val wrappedRequest = ContentCachingRequestWrapper(request)
        this.requestStorage.set(request = wrappedRequest)

        filterChain.doFilter(wrappedRequest, response)
    }
}