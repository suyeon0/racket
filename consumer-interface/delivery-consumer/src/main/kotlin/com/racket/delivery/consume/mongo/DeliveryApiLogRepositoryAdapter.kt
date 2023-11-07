package com.racket.delivery.consume.mongo

import com.racket.delivery.consume.domain.DeliveryApiLog
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class DeliveryApiLogRepositoryAdapter(
    private val deliveryApiLogMongoRepository: DeliveryApiLogMongoRepository
) {

    fun save(log: DeliveryApiLog): Optional<DeliveryApiLog> {
        val result = this.deliveryApiLogMongoRepository.save(DeliveryApiLogEntity.of(log))
        return Optional.of(result.toDomain())
    }

}