package com.racket.core.cache

interface CacheKey {
    companion object {
        const val PRODUCT = "product"

        val caches = listOf(
            PRODUCT
        )
    }
}