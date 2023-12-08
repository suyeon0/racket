package com.racket.api.product.exception

class CacheStoreProcessException() : RuntimeException() {
    override val message: String = "error occurred while storing cache"
}