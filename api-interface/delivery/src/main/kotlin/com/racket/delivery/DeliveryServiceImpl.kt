package com.racket.delivery

import com.racket.delivery.domain.DeliveryRepository
import com.racket.delivery.response.DeliveryResponseView
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class DeliveryServiceImpl(
    private val deliveryRepository: DeliveryRepository
): DeliveryService {
    override fun get(optionId: Long): DeliveryResponseView {
        return DeliveryResponseView(
            statusCode = null,
            statusMessage = null,
            deliveryCost = 5000L,
            expectedDate = LocalDate.now().plusDays(1)
        )
    }

}