package com.racket.cash.repository

import com.racket.cash.entity.CashTransaction
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface CashTransactionRepository: MongoRepository<CashTransaction, ObjectId> {
    fun findByUserId(userId: Long): List<CashTransaction?>
}