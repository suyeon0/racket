package com.racket.delivery.adapter.out.databases.rdb

import org.springframework.data.repository.CrudRepository
import java.util.*

interface OptionDeliveryInformationJpaRepository: CrudRepository<OptionDeliveryInformationEntity, Long> {
    fun findByOptionId(optionId: Long): Optional<OptionDeliveryInformationEntity>
}