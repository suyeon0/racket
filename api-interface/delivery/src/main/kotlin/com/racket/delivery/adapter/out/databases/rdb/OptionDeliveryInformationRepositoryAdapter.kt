package com.racket.delivery.adapter.out.databases.rdb

import com.racket.delivery.application.port.out.databases.OptionDeliveryInformationRepositoryPort
import com.racket.delivery.domain.OptionDeliveryInformation
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class OptionDeliveryInformationRepositoryAdapter(
    private val optionDeliveryInformationJpaRepository: OptionDeliveryInformationJpaRepository
): OptionDeliveryInformationRepositoryPort {
    override fun findById(id: Long): Optional<OptionDeliveryInformation> =
        this.optionDeliveryInformationJpaRepository.findById(id).map { it.toModel() }

    override fun findByOptionId(optionId: Long): Optional<OptionDeliveryInformation> =
        this.optionDeliveryInformationJpaRepository.findByOptionId(optionId).map { it.toModel() }

}