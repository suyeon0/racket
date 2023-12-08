package com.racket.shared.notification.exception.slack

import org.springframework.web.util.ContentCachingRequestWrapper

open class RequestStorage {
    private lateinit var request: ContentCachingRequestWrapper

    fun set(request: ContentCachingRequestWrapper) {
        this.request = request
    }

    fun get() = request
}