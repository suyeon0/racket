package com.racket.cache.exception

class CacheChargeException(): RuntimeException() {
    override val message = "cache charge failed"
}