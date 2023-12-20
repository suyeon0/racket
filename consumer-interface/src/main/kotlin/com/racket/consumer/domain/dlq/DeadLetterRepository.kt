package com.racket.consumer.domain.dlq

import org.bson.types.ObjectId
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface DeadLetterRepository : CrudRepository<DeadLetterEntity, ObjectId>
