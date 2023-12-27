package com.racket.consumer.domain.failure

import org.bson.types.ObjectId
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface FailedEventRepository : CrudRepository<FailedEventEntity, ObjectId>
