package com.racket.api.delivery.application.service

import com.racket.api.delivery.adapter.`in`.rest.response.OptionDeliveryInformationResponseView
import com.racket.api.delivery.application.port.`in`.OptionDeliveryInformationUseCase
import com.racket.api.delivery.application.port.out.databases.OptionDeliveryInformationRepositoryPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OptionDeliveryInformationService(
  private val optionDeliveryInformationRepositoryPort: OptionDeliveryInformationRepositoryPort
) : OptionDeliveryInformationUseCase {

    @Transactional(readOnly = true)
    override fun getDeliveryInformationByOption(optionId: Long): OptionDeliveryInformationResponseView {
        val info = this.optionDeliveryInformationRepositoryPort.findByOptionId(optionId = optionId)
            .orElseThrow { RuntimeException("Delivery Day with ID $optionId not found") }

        return OptionDeliveryInformationResponseView(
            optionId = info.optionId,
            deliveryCost = info.deliveryCost,
            deliveryDays = info.deliveryDays
        )
    }
}