package com.racket.consumer.domain.delivery

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface DeliveryApiLogMongoRepository : MongoRepository<DeliveryApiLogEntity, ObjectId>