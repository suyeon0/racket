package com.racket.delivery.consume.mongo

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface DeliveryApiLogMongoRepository : MongoRepository<DeliveryApiLogEntity, ObjectId>