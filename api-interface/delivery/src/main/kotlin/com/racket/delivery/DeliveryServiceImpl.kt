package com.racket.delivery

import com.racket.delivery.client.DeliveryClientFactory
import com.racket.delivery.domain.OptionDeliveryDaysRepository
import com.racket.delivery.enums.DeliveryCompanyType
import com.racket.delivery.response.OptionDeliveryDaysResponseView
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class DeliveryServiceImpl(
    private val optionDeliveryDaysRepository: OptionDeliveryDaysRepository,
    private val deliveryClientFactory: DeliveryClientFactory
) : DeliveryService {
    override fun getDeliveryDaysByOption(optionId: Long): OptionDeliveryDaysResponseView =
        this.optionDeliveryDaysRepository.findByOptionId(optionId = optionId).map { optionDeliveryDay ->
            OptionDeliveryDaysResponseView(
                statusCode = HttpStatus.OK.value().toLong(),
                statusMessage = "success",
                optionId = optionId,
                deliveryCost = 3_000,
                deliveryDays = optionDeliveryDay.days
            )
        }.orElseThrow {
            RuntimeException("Delivery Day with ID $optionId not found")
        }

    override fun trackDelivery(deliveryCompany: DeliveryCompanyType, invoiceNumber: String) =
        this.deliveryClientFactory.createDeliveryClient(deliveryCompany = deliveryCompany, invoiceNo = invoiceNumber).call()


}