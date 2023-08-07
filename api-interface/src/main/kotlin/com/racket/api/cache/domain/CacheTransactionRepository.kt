package com.racket.api.cache.domain

import com.racket.api.cache.domain.entity.CacheTransaction
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface CacheTransactionRepository: MongoRepository<CacheTransaction, Long> {
    fun findByUserId(userId: Long): List<CacheTransaction?>?
}