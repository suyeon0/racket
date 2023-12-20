package com.racket.consumer.domain.cash

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface CashFailLogRepository: MongoRepository<CashFailLog, ObjectId> {
}