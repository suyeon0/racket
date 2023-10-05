package com.racket.delivery.adapter.out.client.feign.hanjin

import com.racket.delivery.adapter.`in`.rest.response.TrackingDeliveryResponseView
import com.racket.delivery.application.port.out.client.DeliveryRequestClientPort
import org.springframework.stereotype.Component

@Component
class HanjinClientAdapter(
    private val client: HanjinDeliveryApiFeignClient
): DeliveryRequestClientPort {
    override fun call(invoiceNo: String): TrackingDeliveryResponseView {
        val response =
            this.client.getTrackingDeliveryList(request = HanjinDeliveryApiRequest(invoiceNo = invoiceNo)).body as HanjinDeliveryApiResponse
        return response.toCommonView()
    }

}