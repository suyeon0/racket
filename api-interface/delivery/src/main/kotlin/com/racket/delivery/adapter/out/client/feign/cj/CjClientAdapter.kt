package com.racket.delivery.adapter.out.client.feign.cj

import com.racket.delivery.adapter.`in`.rest.response.TrackingDeliveryResponseView
import com.racket.delivery.application.port.out.client.DeliveryRequestClientPort
import org.springframework.stereotype.Component

@Component
class CjClientAdapter(
    private val client: CJDeliveryApiFeignClient
) : DeliveryRequestClientPort {
    override fun call(invoiceNo: String): TrackingDeliveryResponseView {
        val response =
            this.client.getTrackingDeliveryList(request = CjDeliveryApiRequest(invoiceNo = invoiceNo)).body as CjDeliveryApiResponse
        return response.toCommonView()
    }
}