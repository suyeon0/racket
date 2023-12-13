package com.racket.api.delivery.adapter.out.databases.rdb

import com.racket.api.delivery.adapter.out.databases.rdb.OptionDeliveryInformationEntity
import org.springframework.data.repository.CrudRepository
import java.util.*

interface OptionDeliveryInformationJpaRepository: CrudRepository<OptionDeliveryInformationEntity, Long> {
    fun findByOptionId(optionId: Long): Optional<OptionDeliveryInformationEntity>
}