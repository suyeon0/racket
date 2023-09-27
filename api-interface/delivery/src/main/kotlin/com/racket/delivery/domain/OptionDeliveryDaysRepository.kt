package com.racket.delivery.domain

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface OptionDeliveryDaysRepository: CrudRepository<OptionDeliveryDays, Long> {
    fun findByOptionId(optionId: Long): Optional<OptionDeliveryDays>
}