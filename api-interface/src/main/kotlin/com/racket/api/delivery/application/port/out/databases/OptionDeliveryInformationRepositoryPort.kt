package com.racket.api.delivery.application.port.out.databases

import com.racket.api.delivery.domain.OptionDeliveryInformation
import java.util.*

interface OptionDeliveryInformationRepositoryPort {
    fun findById(id: Long): Optional<OptionDeliveryInformation>
    fun findByOptionId(optionId: Long): Optional<OptionDeliveryInformation>
}