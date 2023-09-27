package com.racket.delivery

import com.racket.delivery.domain.OptionDeliveryDaysRepository
import com.racket.delivery.response.DeliveryResponseView
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class DeliveryServiceImpl(
    private val optionDeliveryDaysRepository: OptionDeliveryDaysRepository
) : DeliveryService {
    override fun getDeliveryDaysByOption(optionId: Long): DeliveryResponseView =
        this.optionDeliveryDaysRepository.findByOptionId(optionId = optionId).map { optionDeliveryDay ->
            DeliveryResponseView(
                statusCode = HttpStatus.OK.value().toLong(),
                statusMessage = "success",
                optionId = optionId,
                deliveryCost = 3_000,
                deliveryDays = optionDeliveryDay.days
            )
        }.orElseThrow {
            RuntimeException("Delivery Day with ID $optionId not found")
        }

}