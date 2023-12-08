package com.racket.shared.notification.exception.slack

import org.springframework.stereotype.Component
import org.springframework.web.util.ContentCachingRequestWrapper

@Component
class RequestStorage {
    private lateinit var request: ContentCachingRequestWrapper

    fun set(request: ContentCachingRequestWrapper) {
        this.request = request
    }

    fun get() = request
}