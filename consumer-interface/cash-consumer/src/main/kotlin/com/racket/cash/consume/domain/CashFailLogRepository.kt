package com.racket.cash.consume.domain

import com.racket.cash.entity.CashTransaction
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface CashFailLogRepository: MongoRepository<CashFailLog, ObjectId> {
}