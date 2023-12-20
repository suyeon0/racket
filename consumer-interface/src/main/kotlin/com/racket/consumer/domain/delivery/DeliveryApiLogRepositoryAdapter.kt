package com.racket.consumer.domain.delivery

import org.springframework.stereotype.Repository
import java.util.*

@Repository
class DeliveryApiLogRepositoryAdapter(
    private val deliveryApiLogMongoRepository: DeliveryApiLogMongoRepository
) {

    fun save(log: DeliveryApiLog): Optional<DeliveryApiLog> {
        val entity = DeliveryApiLogEntity.of(log)
        val result = this.deliveryApiLogMongoRepository.save(entity)
        return Optional.of(result.toDomain())
    }

}