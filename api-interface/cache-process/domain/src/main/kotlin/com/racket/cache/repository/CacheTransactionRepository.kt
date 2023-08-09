package com.racket.cache.repository

import com.racket.cache.entity.CacheTransaction
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface CacheTransactionRepository: MongoRepository<CacheTransaction, ObjectId> {
    fun findByUserId(userId: Long): List<CacheTransaction?>?
}