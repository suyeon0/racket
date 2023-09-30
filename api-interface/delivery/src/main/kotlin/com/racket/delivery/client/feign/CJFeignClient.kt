package com.racket.delivery.client.feign

import org.springframework.cloud.openfeign.FeignClient

@FeignClient(name = "cjDelivery", url = "\${client.serviceUrl.cj}", contextId = "cj-delivery-client")
interface CJFeignClient