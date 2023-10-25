package com.racket.delivery.adapter.out.client.feign.hanjin

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(name = "hanjinDelivery", url = "\${client.serviceUrl.cj}", contextId = "hanjin-delivery-client")
interface HanjinDeliveryApiFeignClient {

    @GetMapping("/tracking")
    fun getTrackingDeliveryList(request: HanjinDeliveryApiRequest): ResponseEntity<HanjinDeliveryApiResponse>?

}