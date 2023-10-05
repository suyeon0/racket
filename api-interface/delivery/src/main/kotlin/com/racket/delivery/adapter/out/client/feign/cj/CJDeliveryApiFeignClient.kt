package com.racket.delivery.adapter.out.client.feign.cj

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(name = "cjDelivery", url = "\${client.serviceUrl.cj}", contextId = "cj-delivery-client")
interface CJDeliveryApiFeignClient{

    @GetMapping("/tracking")
    fun getTrackingDeliveryList(request: CjDeliveryApiRequest): ResponseEntity<CjDeliveryApiResponse>

}